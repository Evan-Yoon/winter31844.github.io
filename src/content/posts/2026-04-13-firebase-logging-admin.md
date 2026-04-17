---
title: |
  사고 대응은 로그부터 — 
  블로그에 보안 로그 붙이기
slug: "firebase-logging-admin"
date: 2026-04-13
author: "Evan Yoon"
category: "explore"
subcategory: "new-tech"
description: "정적 사이트 + Firebase 구조에서 별도 서버 없이 보안 로그를 남기는 방법. Cloud Functions에 구조화 로그를 추가하고, 어드민 대시보드에서 바로 확인."
tags:
  - security
  - firebase
  - cloud-functions
  - logging
  - admin
readTime: 7
toc: true
featured: false
draft: false
---

침투 테스트를 마치고 얼마 지나지 않아 Threads에서 이런 글을 봤다.

> 보안 사고 났을 때 제일 먼저 해야 할 것, 대부분 "어떻게 막지" 생각합니다.
> 그 전에 먼저 — 1. 어떤 데이터가 나갔는가 2. 언제부터 나갔는가 3. 누가 가져갔는가
> 로그 없으면 이 세 질문에 답 못 합니다. 사고 대응은 로그부터 시작합니다.

읽자마자 찔렸다. 방금 전까지 Firebase Rules 취약점을 찾고 막는 데 집중했는데, 정작 **"뭔가 이상한 일이 벌어졌을 때 어떻게 알 수 있나"** 는 전혀 생각하지 않았다.

---

## 문제 정의

이 블로그의 인프라는 이렇다.

- **Astro 정적 사이트** — GitHub Pages 배포, 서버 없음
- **Firebase Realtime Database** — 댓글, 방명록, 방문자 통계
- **Cloud Functions** — 방문자 카운팅 (`trackVisit`)

서버가 없으니 서버 로그도 없다. Firebase Console에서 DB 접근 현황을 볼 수는 있지만, "누가 이상한 날짜로 요청을 보냈는지"를 나중에 추적하는 건 불가능하다.

---

## 별도 사이트가 필요할까?

아니다. 이미 있는 인프라에서 다 해결된다.

<div class="info-grid">
  <div class="info-item">
    <div class="info-title">Cloud Function 요청/오류</div>
    <div class="info-desc">Google Cloud Logging (자동) — 무료</div>
  </div>
  <div class="info-item">
    <div class="info-title">보안 이벤트 (이상 요청)</div>
    <div class="info-desc">console.log + 구조화 JSON — 무료</div>
  </div>
  <div class="info-item">
    <div class="info-title">어드민 대시보드 표시</div>
    <div class="info-desc">Firebase <code>_logs/security</code> — 무료</div>
  </div>
</div>

Cloud Functions는 배포하는 순간부터 Google Cloud Logging이 자동 수집한다. `console.log()`로 찍은 것도 전부 여기 저장된다. 무료 티어는 한 달 50GB — 개인 블로그는 평생 무료 수준이다.

문제는 **Cloud Logging은 클라이언트에서 직접 읽을 수 없다**는 것. 그래서 전략을 나눴다.

- 일반 이벤트 → `console.log()` → Cloud Logging
- 보안 이벤트 → `console.warn()` + Firebase DB 기록 → 어드민 대시보드

---

## 구조화 로그 추가

`trackVisit` Cloud Function에 로그를 추가했다.

```typescript
// functions/src/index.ts
export const trackVisit = onCall(
  { region: "asia-northeast3" },
  async (request) => {
    const date: unknown = request.data?.date;
    const userAgent = request.rawRequest.headers["user-agent"] ?? "";
    const rawIp = request.rawRequest.headers["x-forwarded-for"] ?? "...";
    const ipHash = createHash("sha256")
      .update(rawIp)
      .digest("hex")
      .slice(0, 16);

    const db = getDatabase();

    if (typeof date !== "string" || !DATE_REGEX.test(date)) {
      const ts = new Date().toISOString();

      // Cloud Logging
      console.warn(
        JSON.stringify({
          event: "invalid_date_rejected",
          date,
          ipHash,
          ua: userAgent,
          timestamp: ts,
        }),
      );

      // 어드민 대시보드용 Firebase 기록
      db.ref("_logs/security")
        .push({
          event: "invalid_date_rejected",
          date,
          ipHash,
          ua: userAgent,
          timestamp: ts,
        })
        .catch(() => {});

      return { counted: false, reason: "invalid_date", current: 0 };
    }

    // ... 중복 방문 처리
    if (alreadyCounted) {
      console.log(
        JSON.stringify({
          event: "visit_deduplicated",
          date,
          ipHash,
          timestamp: new Date().toISOString(),
        }),
      );
      return { counted: false, current: currentCount };
    }

    // ... atomic 증가
    console.log(
      JSON.stringify({
        event: "visit_counted",
        date,
        ipHash,
        ua: userAgent,
        current: finalCount,
        timestamp: new Date().toISOString(),
      }),
    );
    return { counted: true, current: finalCount };
  },
);
```

### 로그 3종

<div class="info-grid">
  <div class="info-item">
    <div class="info-title">invalid_date_rejected</div>
    <div class="info-desc">날짜 형식이 이상한 요청 (공격 시도 가능성) — Cloud Logging + Firebase</div>
  </div>
  <div class="info-item">
    <div class="info-title">visit_deduplicated</div>
    <div class="info-desc">같은 IP 하루 2회째 방문 — Cloud Logging</div>
  </div>
  <div class="info-item">
    <div class="info-title">visit_counted</div>
    <div class="info-desc">정상 카운트 — Cloud Logging</div>
  </div>
</div>

보안 이벤트(`invalid_date_rejected`)만 Firebase에도 기록한다. 정상 이벤트를 전부 Firebase에 쓰면 쓰기 비용이 발생하고, 의미 없는 데이터가 쌓인다.

---

## IP는 저장하지 않는다

IP 원문을 저장하면 개인정보 이슈가 생긴다. SHA-256 해시의 앞 16자리만 보관한다.

```typescript
const ipHash = createHash("sha256").update(rawIp).digest("hex").slice(0, 16);
```

이렇게 해도 "사건 당시 같은 IP인지 아닌지"는 판단할 수 있다. 누구인지는 알 수 없고, 동일 출처인지만 구분 가능하다.

---

## Firebase Rules 설정

`_logs/security` 노드는 어드민만 읽고, 클라이언트는 쓰기 불가로 설정한다. Cloud Functions는 Admin SDK라 Rules를 우회하므로 서버 쪽 기록은 정상 동작한다.

```json
"_logs": {
  ".read": "auth != null && root.child('admins').child(auth.uid).val() == true",
  ".write": false
}
```

---

## 어드민 대시보드

어드민 페이지(`/admin/comments`)에 Security Logs 패널을 추가했다.

Firebase 로그인 → 관리자 계정 확인 → Comments 패널과 함께 Security Logs 자동 로드.

```typescript
// 최신 100건만 조회
const logsQuery = query(
  ref(database, "_logs/security"),
  orderByKey(),
  limitToLast(100),
);
const snapshot = await get(logsQuery);
```

각 로그 항목에는 이벤트 종류, 시각, IP 해시, 요청 날짜, User-Agent가 표시된다. **Clear All** 버튼으로 전체 삭제도 가능하다.

---

## Cloud Logging 활용

Firebase에 기록되지 않는 일반 로그는 Google Cloud Console에서 확인한다.

```
Google Cloud Console → Logging → 로그 탐색기
```

이벤트별로 필터링할 수 있다.

```
# 이상한 날짜 요청만
jsonPayload.event="invalid_date_rejected"

# 특정 날짜 방문 전체 흐름
jsonPayload.date="2026-04-13"
```

알림이 필요하면 **로그 기반 측정항목 만들기**로 이메일 알림도 설정할 수 있다. 예: `invalid_date_rejected`가 5분에 10회 이상이면 알림.

---

## 회고

Firebase 규칙이 막아줬다고 해서 기록이 필요 없는 건 아니다. 규칙이 놓친 게 있을 수도 있고, 규칙이 바뀐 사이에 뭔가 들어올 수도 있다. 사고가 났을 때 "언제, 누가, 무엇을"에 답하려면 기록이 있어야 한다.

추가한 코드는 Cloud Function 곳곳에 넣은 `console.log` 한 줄이 전부다.

```
console.log(JSON.stringify({ event, ipHash, timestamp }))
```
