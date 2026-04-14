import { onCall } from 'firebase-functions/v2/https';
import { getDatabase } from 'firebase-admin/database';
import { initializeApp } from 'firebase-admin/app';
import { createHash } from 'crypto';

initializeApp();

const DATE_REGEX = /^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$/;

/**
 * Counts one visit per browser identity per day.
 * Anonymous Auth UID is used first so different users behind the same public IP
 * are counted separately. If auth is unavailable, the function falls back to
 * an IP hash to avoid dropping the request entirely.
 */
export const trackVisit = onCall(
  { region: 'asia-northeast3', invoker: 'public' },
  async (request) => {
    const date: unknown = request.data?.date;
    const userAgent = (request.rawRequest.headers['user-agent'] as string | undefined) ?? '';
    const visitorUid = request.auth?.uid ?? null;

    const rawIp =
      (request.rawRequest.headers['x-forwarded-for'] as string | undefined)
        ?.split(',')[0]
        ?.trim() ??
      request.rawRequest.socket?.remoteAddress ??
      'unknown';

    const ipHash = createHash('sha256').update(rawIp).digest('hex').slice(0, 16);
    const visitorKey = visitorUid ?? `ip:${ipHash}`;
    const db = getDatabase();

    if (typeof date !== 'string' || !DATE_REGEX.test(date)) {
      const ts = new Date().toISOString();
      console.warn(JSON.stringify({ event: 'invalid_date_rejected', date, ipHash, ua: userAgent, timestamp: ts }));
      db.ref('_logs/security')
        .push({ event: 'invalid_date_rejected', date, ipHash, ua: userAgent, timestamp: ts })
        .catch(() => {});
      return { counted: false, reason: 'invalid_date', current: 0 };
    }

    const rateLimitRef = db.ref(`_rateLimit/visits/${date}/${visitorKey}`);
    const alreadyCounted = (await rateLimitRef.get()).exists();

    const currentSnap = await db.ref(`visits/${date}`).get();
    const currentCount: number = currentSnap.exists() ? (currentSnap.val() as number) : 0;

    if (alreadyCounted) {
      console.log(
        JSON.stringify({
          event: 'visit_deduplicated',
          date,
          visitorKey,
          authUid: visitorUid,
          ipHash,
          current: currentCount,
          timestamp: new Date().toISOString(),
        })
      );
      return { counted: false, current: currentCount };
    }

    const result = await db.ref(`visits/${date}`).transaction((value: number | null) => (value ?? 0) + 1);
    await rateLimitRef.set(true);

    const finalCount = result.snapshot.val() as number;
    console.log(
      JSON.stringify({
        event: 'visit_counted',
        date,
        visitorKey,
        authUid: visitorUid,
        ipHash,
        ua: userAgent,
        current: finalCount,
        timestamp: new Date().toISOString(),
      })
    );

    return {
      counted: true,
      current: finalCount,
    };
  }
);
