"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.trackVisit = void 0;
const https_1 = require("firebase-functions/v2/https");
const database_1 = require("firebase-admin/database");
const app_1 = require("firebase-admin/app");
const crypto_1 = require("crypto");
(0, app_1.initializeApp)();
const DATE_REGEX = /^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$/;
/**
 * Counts one visit per browser identity per day.
 * Anonymous Auth UID is used first so different users behind the same public IP
 * are counted separately. If auth is unavailable, the function falls back to
 * an IP hash to avoid dropping the request entirely.
 */
exports.trackVisit = (0, https_1.onCall)({ region: 'asia-northeast3', invoker: 'public' }, async (request) => {
    var _a, _b, _c, _d, _e, _f, _g, _h, _j;
    const date = (_a = request.data) === null || _a === void 0 ? void 0 : _a.date;
    const userAgent = (_b = request.rawRequest.headers['user-agent']) !== null && _b !== void 0 ? _b : '';
    const visitorUid = (_d = (_c = request.auth) === null || _c === void 0 ? void 0 : _c.uid) !== null && _d !== void 0 ? _d : null;
    const rawIp = (_j = (_g = (_f = (_e = request.rawRequest.headers['x-forwarded-for']) === null || _e === void 0 ? void 0 : _e.split(',')[0]) === null || _f === void 0 ? void 0 : _f.trim()) !== null && _g !== void 0 ? _g : (_h = request.rawRequest.socket) === null || _h === void 0 ? void 0 : _h.remoteAddress) !== null && _j !== void 0 ? _j : 'unknown';
    const ipHash = (0, crypto_1.createHash)('sha256').update(rawIp).digest('hex').slice(0, 16);
    const visitorKey = visitorUid !== null && visitorUid !== void 0 ? visitorUid : `ip:${ipHash}`;
    const db = (0, database_1.getDatabase)();
    if (typeof date !== 'string' || !DATE_REGEX.test(date)) {
        const ts = new Date().toISOString();
        console.warn(JSON.stringify({ event: 'invalid_date_rejected', date, ipHash, ua: userAgent, timestamp: ts }));
        db.ref('_logs/security')
            .push({ event: 'invalid_date_rejected', date, ipHash, ua: userAgent, timestamp: ts })
            .catch(() => { });
        return { counted: false, reason: 'invalid_date', current: 0 };
    }
    const rateLimitRef = db.ref(`_rateLimit/visits/${date}/${visitorKey}`);
    const alreadyCounted = (await rateLimitRef.get()).exists();
    const currentSnap = await db.ref(`visits/${date}`).get();
    const currentCount = currentSnap.exists() ? currentSnap.val() : 0;
    if (alreadyCounted) {
        console.log(JSON.stringify({
            event: 'visit_deduplicated',
            date,
            visitorKey,
            authUid: visitorUid,
            ipHash,
            current: currentCount,
            timestamp: new Date().toISOString(),
        }));
        return { counted: false, current: currentCount };
    }
    const result = await db.ref(`visits/${date}`).transaction((value) => (value !== null && value !== void 0 ? value : 0) + 1);
    await rateLimitRef.set(true);
    const finalCount = result.snapshot.val();
    console.log(JSON.stringify({
        event: 'visit_counted',
        date,
        visitorKey,
        authUid: visitorUid,
        ipHash,
        ua: userAgent,
        current: finalCount,
        timestamp: new Date().toISOString(),
    }));
    return {
        counted: true,
        current: finalCount,
    };
});
//# sourceMappingURL=index.js.map