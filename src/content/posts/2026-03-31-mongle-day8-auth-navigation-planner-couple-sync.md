---
title: "[Mongle] Day 8 — 로그인 버그 3연타, 그리고 플래너-커플 연동"
slug: "mongle-day8-auth-navigation-planner-couple-sync"
date: 2026-03-31
author: "Evan Yoon"
category: "project"
subcategory: "team-project"
description: "로그인 화면 전환이 안 되고, 설정 화면이 홈으로 튕기고, 뒤로가기 버튼이 에러를 냈다. 세 개를 잡고 나서 플래너-커플 데이터 연동을 완성했다."
tags:
  - mongle
  - expo-router
  - auth
  - navigation
  - supabase
  - react-native
  - team-project
readTime: 11
series: "Mongle 프로젝트"
seriesOrder: 8
featured: false
draft: false
toc: true
---

## 오늘의 전쟁터: 인증과 네비게이션

어제 회원가입 플로우를 전면 재작성했다. OTP 인증, 이메일 중복 확인까지 추가했는데 — 오늘 아침에 팀원들이 실제로 써보니 버그가 세 곳에서 터졌다.

로그인 버튼을 눌러도 화면이 안 넘어가고, 이용약관 화면을 열면 홈으로 튕기고, 뒤로가기를 누르면 앱이 에러를 뱉었다. 각각 원인이 달랐다.

---

## 버그 1: 로그인 후 화면 전환 없음

> `로그인 에러 현상 해결` — 09:45  
> 2 files, 23 insertions(+), 11 deletions(-)

로그인 버튼을 누르고 인증에 성공해도 화면이 그대로였다.

원인은 `_layout.jsx`의 `AuthGate`에 있었다. 로그인 성공 후 화면을 전환하는 로직은 `onAuthStateChange`에서 role을 받아온 다음 실행된다. 그런데 `fetchUserRole()`이 실패하거나 느릴 경우, `setIsReady(true)`가 호출되지 않아 앱이 로딩 상태에서 멈췄다.

```javascript
// 수정 전: fetchUserRole 실패 시 setIsReady가 불리지 않음
supabase.auth.onAuthStateChange(async (_event, session) => {
    setSession(session);
    if (session) {
        const profile = await fetchUserRole(session.user.id);
        setProfile(profile);
        setRole(profile?.role || null);
        setUserId(session.user.id);
    } else {
        setProfile(null);
        setRole(null);
        setUserId(null);
    }
    setIsReady(true);  // ← 에러 발생 시 여기까지 못 옴
});

// 수정 후: try/finally로 어떤 경우든 setIsReady 보장
supabase.auth.onAuthStateChange(async (_event, session) => {
    setSession(session);
    try {
        if (session) {
            const profile = await fetchUserRole(session.user.id);
            setProfile(profile);
            setRole(profile?.role || null);
            setUserId(session.user.id);
        } else {
            setProfile(null);
            setRole(null);
            setUserId(null);
        }
    } catch (e) {
        console.error('onAuthStateChange fetchUserRole 에러:', e);
        setRole(null);
    } finally {
        setIsReady(true);  // 성공이든 실패든 반드시 실행
    }
});
```

`finally`로 감싸서 예외가 나도 `setIsReady(true)`가 반드시 불리도록 했다.

---

## 버그 2: 설정 화면 진입 시 홈으로 강제 이동

> `개인정보처리방침, 이용약관 화면 연결` — 10:21  
> 1 file, 3 insertions(+)

어제 만든 `app/settings/terms.jsx`, `app/settings/privacy.jsx`를 마이페이지에서 연결했는데, 탭하는 순간 홈 화면으로 튕겼다.

`AuthGate`가 문제였다. 로그인된 사용자가 `(couple)`이나 `(planner)` 그룹 밖으로 나가면 강제로 리디렉션하는 로직이 있다. `app/settings/`는 이 두 그룹 어디에도 속하지 않으니, 들어가는 즉시 홈으로 쫓겨난 것이다.

```javascript
// 수정 전: settings 경로 예외 없음
const inAuthGroup    = rootSegment === '(auth)';
const inPlannerGroup = rootSegment === '(planner)';
const inCoupleGroup  = rootSegment === '(couple)';

// 수정 후: settings 그룹 예외 추가
const inAuthGroup     = rootSegment === '(auth)';
const inPlannerGroup  = rootSegment === '(planner)';
const inCoupleGroup   = rootSegment === '(couple)';
const inSettingsGroup = rootSegment === 'settings';

// ...
if (role === null) return;
if (inSettingsGroup) return;  // 설정 화면은 리디렉션하지 않음
```

단 3줄 추가였지만, 이것 때문에 이용약관 화면이 완전히 막혀있었다.

---

## 버그 3: 뒤로가기 에러 + 화면 전환 타이밍

> `로그인 에러 현상 해결3` — 10:43  
> 2 files, 13 insertions(+), 2 deletions(-)

로그인·회원가입 화면에서 뒤로가기 버튼을 누르면 이런 에러가 났다.

```
The action 'GO_BACK' was not handled by any navigator.
```

딥링크로 진입하거나 앱을 최초 실행할 때는 스택에 이전 화면이 없다. 이 상태에서 `router.back()`을 호출하면 돌아갈 곳이 없으니 에러가 터진다.

```javascript
// 수정 전: 스택이 비어있어도 back() 호출
<TouchableOpacity onPress={() => router.back()}>

// 수정 후: 스택 확인 후 fallback
<TouchableOpacity onPress={() => {
    if (router.canGoBack()) {
        router.back();
    } else {
        router.replace('/(couple)');  // 돌아갈 곳 없으면 홈으로
    }
}}>
```

로그인 성공 후 화면 전환도 같이 수정했다. 기존에는 `AuthGate`가 role을 확인한 다음 자동으로 화면을 전환해주길 기다리는 방식이었다. 그런데 이 타이밍이 항상 맞지 않았다. 특히 iOS에서 role 상태가 null인 순간 redirect effect가 early return되어 화면이 멈추는 경우가 있었다.

```javascript
// 수정 전: AuthGate의 자동 전환을 기다림
// ...로그인 성공 후 아무 것도 하지 않음...

// 수정 후: 로그인 성공 즉시 명시적으로 이동
const userRole = /* 로그인 응답에서 추출 */;
if (userRole === 'planner') {
    router.replace('/(planner)/dashboard');
} else {
    router.replace('/(couple)');
}
```

회원가입 완료(비밀번호 입력 후)도 동일하게 처리했다.

---

## 네 번째 로그인 수정: `waitForResolvedAuth`

> `로그인 에러 수정` — 16:17  
> 3 files, 78 insertions(+), 2 deletions(-)

오전에 세 번 수정했는데도 특정 경우에 여전히 타이밍 문제가 생겼다.

본질적인 문제는 이거다. Supabase 로그인 직후 role 정보가 즉시 준비되지 않는다. `onAuthStateChange`가 비동기로 profile을 가져오는 동안, 화면 전환을 시도하면 role이 아직 null인 상태다.

해결책으로 `waitForResolvedAuth()`를 만들었다.

```javascript
// lib/auth.js
export const waitForResolvedAuth = async ({
    expectedRole = null,
    timeoutMs = 3000,
    pollMs = 150
} = {}) => {
    const startedAt = Date.now();

    while (Date.now() - startedAt < timeoutMs) {
        const { data: { session } } = await supabase.auth.getSession();
        const resolvedRole = await resolveAuthRole(session, expectedRole);

        if (session && resolvedRole) {
            return {
                session,
                role: resolvedRole,
                route: getPostAuthRoute(resolvedRole),
            };
        }

        await new Promise((resolve) => setTimeout(resolve, pollMs));
    }

    // 타임아웃 시 마지막으로 한 번 더 시도
    const { data: { session } } = await supabase.auth.getSession();
    const resolvedRole = await resolveAuthRole(session, expectedRole);
    return {
        session,
        role: resolvedRole,
        route: getPostAuthRoute(resolvedRole ?? expectedRole),
    };
};
```

150ms마다 role이 확인됐는지 폴링하고, 최대 3초 안에 확인되면 바로 이동한다. iOS는 이 함수를 사용해 role을 확인한 다음 이동하고, Android는 `AuthGate` 자동 전환에 의존하는 방식으로 플랫폼별로 분리했다.

`resolveAuthRole()`도 같이 추가했다. 세션의 `user_metadata.role`과 DB의 profile role 중 더 신뢰할 수 있는 값을 반환하는 유틸이다.

```javascript
export const resolveAuthRole = async (session, fallbackRole = null) => {
    const metadataRole = session?.user?.user_metadata?.role ?? fallbackRole ?? null;
    const userId = session?.user?.id;
    if (!userId) return metadataRole;

    try {
        const profile = await fetchUserRole(userId);
        return profile?.role ?? metadataRole;  // DB 우선, 없으면 metadata
    } catch (_error) {
        return metadataRole;  // DB 실패 시 metadata로 fallback
    }
};
```

---

## 오후: PR 머지 러시

팀원들이 플래너 기능들을 밀어 올리고 있었다.

| 시각 | PR | 내용 |
|------|----|------|
| 09:42 | PR #88 | 플래너 대시보드 |
| 10:50 | PR #93 | 플래너 타임라인·예산 |
| 13:54 | PR #94 | 플래너 타임라인·예산 업데이트 |
| 14:53 | PR #98 | 비밀번호 변경 기능 |
| 15:06 | PR #99 | 커플 알림 기능 |
| 16:13 | PR #101 | 계정 삭제 기능 |
| 16:33 | PR #103 | 플래너 파일 버그 수정 |
| 16:55 | PR #104 | 대시보드 bypass 처리 |
| 17:05 | PR #105 | 플래너 정보 편집 |
| 20:11 | PR #106, #107 | 채팅 버그 수정, 관리자 웹 Supabase 연동 |

---

## 플래너-커플 데이터 연동

> `커플 데이터와 플래너 데이터 연동` — 13:56  
> 14 files, 1,470 insertions(+), 1,991 deletions(-)

이날의 핵심 작업. 플래너와 커플이 같은 데이터를 바라보도록 연동했다.

**문제:** 커플이 타임라인·예산을 입력하면 플래너 대시보드에는 보이지 않았다. 각자의 화면이 서로 다른 데이터를 바라봤기 때문이다.

**해결:** `coupleIdentity.js`를 새로 만들어 커플과 플래너 사이의 연결 고리를 명확히 했다.

```javascript
// lib/coupleIdentity.js
export async function resolveCoupleContext(sessionUserId, profileCoupleId = null) {
    if (!sessionUserId) {
        return { coupleId: null, plannerId: null, couple: null };
    }

    if (profileCoupleId) {
        // 프로필에 coupleId가 있으면 그걸 기준으로 조회
        const { data } = await supabase
            .from('couples')
            .select('id, planner_id, groom_name, bride_name, wedding_date, user_id')
            .eq('id', profileCoupleId)
            .maybeSingle();

        return {
            coupleId: data?.id ?? profileCoupleId,
            plannerId: data?.planner_id ?? null,
            couple: data ?? null,
        };
    }

    // 없으면 user_id로 couples 테이블에서 찾기
    const { data } = await supabase
        .from('couples')
        .select('id, planner_id, groom_name, bride_name, wedding_date, user_id')
        .eq('user_id', sessionUserId)
        .order('created_at', { ascending: false })
        .limit(1)
        .maybeSingle();

    return {
        coupleId: data?.id ?? null,
        plannerId: data?.planner_id ?? null,
        couple: data ?? null,
    };
}
```

커플 ID를 기준으로 플래너를 찾고, 플래너 ID를 기준으로 커플을 찾는다. 어느 쪽에서 진입해도 같은 데이터에 도달한다.

Supabase의 Row Level Security(RLS) 정책도 `couple_access_policies.sql`로 정리했다. 306줄 분량이다. 커플은 자신의 데이터만, 플래너는 담당 커플의 데이터를 읽을 수 있도록 정책을 설정했다.

> `플래너-커플 스케줄 연동 완료` — 14:45  
> 8 files, 127 insertions(+), 27 deletions(-)

> `플래너-커플 예산 연동 완료` — 15:47  
> 4 files, 295 insertions(+), 53 deletions(-)

타임라인과 예산을 각각 이어서 연동했다. `timeline.jsx`는 이제 커플 ID 기반으로 데이터를 로드하고, `budget.jsx`는 `coupleBudgetData.js`를 통해 플래너도 같은 예산을 볼 수 있다.

---

## 저녁: 비용 관리 오류 수정

> `fix: 비용 관리 오류 수정` — 20:21  
> 7 files, 714 insertions(+), 76 deletions(-)

플래너-커플 연동을 붙이고 나서 예산 화면에서 새로운 버그가 나왔다. `budget.jsx`가 전면 수정됐고, `useBudgetOptimization.js`도 같이 정리했다. `coupleIdentity.js`에 새 케이스를 추가해서 플래너가 커플 예산을 조회하는 경로도 커버했다.

---

## 밤: 관리자 웹 복구

> `web recovery` — 22:22  
> 54 files, 6,349 insertions(+)

Day 4에서 `Mongle-admin-web/`으로 관리자 웹을 만들었는데, 이날 밤 `Mongle-server/ai/web-evan/`으로 별도 경로에 다시 스캐폴딩했다.

Day 4 버전이 main에서 분리되거나 변경이 생긴 것으로 보인다. 기존 컴포넌트 구조(`Dashboard`, `CoupleList`, `Budget`, `TodoList`, `Notifications`, `VendorPartners`, `auth/`)와 Context API(`AuthContext`, `NotificationsContext`, `ThemeContext`, `TodoContext`)를 그대로 재구성했다. `react + vite` 기반이다.

---

## 오늘을 돌아보며

로그인 버그를 네 번이나 수정했다. 한 번에 못 잡은 이유는 **네비게이션 타이밍 문제는 로그만으로 재현이 어렵기 때문**이다.

`AuthGate` → `onAuthStateChange` → `fetchUserRole` → `redirect effect`가 순서대로 실행되는 것처럼 보이지만, 각 단계는 비동기로 동작한다. 그 사이 어딘가에서 role이 null인 순간이 생기고, 그 타이밍에 redirect가 실행되면 화면이 멈춘다.

오늘 배운 건 두 가지다.

하나, **비동기 인증 흐름은 모든 실패 경로를 명시적으로 처리해야 한다.** `try/finally`로 `setIsReady`를 보장하고, `waitForResolvedAuth`로 role 확인을 명시적으로 기다리는 것이 그 결과다.

둘, **하나의 버그가 여러 화면에 걸쳐 연쇄적으로 영향을 미친다.** 뒤로가기 에러, 설정 화면 튕김, 로그인 멈춤이 각각 달라 보여도 전부 `AuthGate`의 리디렉션 로직에서 비롯된 문제였다. 수정 전에 전체 흐름을 먼저 파악했더라면 한 번에 잡을 수 있었을 것이다.

---

**GitHub:** [APP-Project-Team1/mongle](https://github.com/APP-Project-Team1/mongle)
---

## 동기화 기능이 생각보다 어려웠던 이유

커플 동기화나 플래너-사용자 연결은 화면상으로는 단순해 보여도, 실제로는 권한과 데이터 기준점을 어떻게 잡을지에 대한 문제다. 누가 어떤 정보를 수정할 수 있는지, 서로 다른 기기에서 동시에 변경이 일어나면 어떤 값을 기준으로 삼을지, 연결 해제나 초대 만료 같은 예외를 어떻게 처리할지까지 함께 고려해야 한다. Day 8에서 이 구조를 다룬 것은 Mongle이 단순 개인용 앱을 넘어 관계형 서비스로 확장되는 지점을 보여준다.

이 시점에 인증과 네비게이션을 함께 정리한 것도 적절했다. 권한 구조가 바뀌면 이동 흐름도 같이 바뀌기 때문이다. 결국 좋은 동기화 경험은 데이터베이스 테이블보다도, 사용자가 지금 어떤 관계 상태에 있는지 명확히 이해하게 만드는 화면 흐름에서 완성된다.