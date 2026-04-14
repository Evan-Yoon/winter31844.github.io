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
 * trackVisit
 *
 * 클라이언트에서 직접 visits/ 노드에 쓰는 대신 이 함수를 호출한다.
 * - 서버에서 날짜 유효성 검증
 * - IP 해시 기반 rate limiting: 같은 IP는 하루에 한 번만 카운트
 * - Admin SDK로 atomic 증가 (클라이언트 조작 불가)
 *
 * 요청: { date: "YYYY-MM-DD" }  ← KST 기준 날짜
 * 응답: { counted: boolean, current: number }
 */
exports.trackVisit = (0, https_1.onCall)({ region: 'asia-northeast3' }, // 서울 리전
async (request) => {
    var _a, _b, _c, _d, _e, _f, _g;
    const date = (_a = request.data) === null || _a === void 0 ? void 0 : _a.date;
    const userAgent = (_b = request.rawRequest.headers['user-agent']) !== null && _b !== void 0 ? _b : '';
    // IP 수집 — 역방향 프록시 헤더 우선, 없으면 소켓 IP
    const rawIp = (_g = (_e = (_d = (_c = request.rawRequest.headers['x-forwarded-for']) === null || _c === void 0 ? void 0 : _c.split(',')[0]) === null || _d === void 0 ? void 0 : _d.trim()) !== null && _e !== void 0 ? _e : (_f = request.rawRequest.socket) === null || _f === void 0 ? void 0 : _f.remoteAddress) !== null && _g !== void 0 ? _g : 'unknown';
    // IP를 직접 저장하지 않고 16자리 해시만 보관 (개인정보 보호)
    const ipHash = (0, crypto_1.createHash)('sha256').update(rawIp).digest('hex').slice(0, 16);
    const db = (0, database_1.getDatabase)();
    // 날짜 형식 검증 (클라이언트가 보낸 값을 신뢰하지 않음)
    if (typeof date !== 'string' || !DATE_REGEX.test(date)) {
        const ts = new Date().toISOString();
        console.warn(JSON.stringify({ event: 'invalid_date_rejected', date, ipHash, ua: userAgent, timestamp: ts }));
        // 어드민 대시보드에서 볼 수 있도록 Firebase에도 기록
        db.ref('_logs/security').push({ event: 'invalid_date_rejected', date, ipHash, ua: userAgent, timestamp: ts }).catch(() => { });
        return { counted: false, reason: 'invalid_date', current: 0 };
    }
    // rate limit 노드: _rateLimit/visits/{date}/{ipHash}
    // → firebase rules에서 클라이언트 읽기/쓰기 모두 차단
    const rateLimitRef = db.ref(`_rateLimit/visits/${date}/${ipHash}`);
    const alreadyCounted = (await rateLimitRef.get()).exists();
    // 현재 카운트 읽기
    const currentSnap = await db.ref(`visits/${date}`).get();
    const currentCount = currentSnap.exists() ? currentSnap.val() : 0;
    if (alreadyCounted) {
        console.log(JSON.stringify({
            event: 'visit_deduplicated',
            date,
            ipHash,
            current: currentCount,
            timestamp: new Date().toISOString(),
        }));
        return { counted: false, current: currentCount };
    }
    // atomic 증가 (동시 요청이 와도 중복 없이 처리)
    const result = await db.ref(`visits/${date}`).transaction((value) => (value !== null && value !== void 0 ? value : 0) + 1);
    // rate limit 기록
    await rateLimitRef.set(true);
    const finalCount = result.snapshot.val();
    console.log(JSON.stringify({
        event: 'visit_counted',
        date,
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