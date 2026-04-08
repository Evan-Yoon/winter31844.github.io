---
title: '[Mongle] Day 7 — 알림 시스템 구현, 그리고 과감하게 폐기한 이유'
slug: mongle-day7-notification-terms-signup
date: 2026-03-30
author: Evan Yoon
category: project
subcategory: team-project
description: D-7/D-3/D-1 리마인드 알림 시스템을 만들었다가 통째로 폐기했다. 그리고 이용약관 페이지와 회원가입 OTP 인증까지
  하루에 다 처리했다.
tags:
  - mongle
  - expo-notifications
  - push-notification
  - cron
  - supabase
  - react-native
  - team-project
readTime: 10
series: Mongle
seriesOrder: 7
featured: false
draft: false
toc: true
---

## 아침: 어제의 후폭풍 정리

어제 Day 6에서 앱 전체 라우팅 구조를 `(couple)/(tabs)/`로 재설계했다. 대규모 파일 이동이라 오늘 아침에 팀원들이 pull 받으면서 충돌이 날 게 뻔했다.

예상대로였다.

> `fix: 폴더 구조 import 불일치 해결` — 09:40
> 8 files, 51 insertions(+), 23 deletions(-)

어제 탭 화면들을 `(couple)/(tabs)/` 하위로 옮기면서 import 경로가 전부 바뀌었다. `budget.jsx`, `chat/ai.jsx`, `index.jsx`, `mypage.jsx`에서 상대 경로로 참조하던 `../../lib/...`들이 경로가 달라져 빨간 줄을 띄웠다. 파일마다 찾아서 경로를 다시 맞췄다.

`.env.example`도 이 커밋에서 정리했다. 팀원들이 처음 환경을 세팅할 때 어떤 환경 변수가 필요한지 알 수 있도록.

> `fix: 바텀 탭 복구` — 09:51
> 1 file, 3 insertions(+), 524 deletions(-)

`app/(couple)/index.jsx`가 이상했다. 라우팅 재설계 과정에서 바텀 탭 네비게이션 코드가 이 파일 안으로 복사됐다. 원래 탭은 `_layout.jsx`가 담당해야 한다. 524줄이 삭제됐는데, 지운 게 아니라 **있으면 안 되는 코드를 걷어낸 것**이다.

---

## 오전 1: 견적 비교 AI 개선

> `feat: 견적 비교 분석 AI 개선` — 10:44
> 3 files, 212 insertions(+), 61 deletions(-)

Day 5에서 `ComparisonEngine.js`를 처음 만들 때 스튜디오·드레스·메이크업 위주로 설계했다. 오늘은 **웨딩홀 전용 필드**를 추가했다.

웨딩홀 견적서에는 스튜디오와 완전히 다른 항목들이 나온다. 보증 인원, 최소 인원, 수용 인원, 인당 식대, 대관료, 세팅비, 봉사료율 같은 것들이다. 이 필드들이 `undefined`로 들어오면 Day 5에서 고쳤던 것처럼 또 크래시가 난다.

```javascript
// 웨딩홀 전용 필드 — 기본값 방어
const guaranteedCapacity = Number(item.guaranteedCapacity) || 0;
const minCapacity        = Number(item.minCapacity)        || 0;
const capacity           = Number(item.capacity)           || 0;
const foodPricePerPerson = Number(item.foodPricePerPerson) || 0;
const totalFoodPrice     = Number(item.totalFoodPrice)     || 0;
const rentalFee          = Number(item.rentalFee)          || 0;
const decorationPrice    = Number(item.decorationPrice)    || 0;
const serviceChargePercent = Number(item.serviceChargePercent) || 0;
const vatPercent         = Number(item.vatPercent)         || 10;
```

부가세 계산도 수정했다. 기존에는 부가세를 무조건 10%로 고정했는데, 견적서에 따라 다를 수 있다. `vatPercent` 필드를 추출해서 실제 비율로 계산하도록 바꿨다.

```javascript
// 수정 전: 부가세 10% 하드코딩
const realCost = totalPrice + optionsPrice - discountPrice
    + (vatIncluded ? 0 : totalPrice * 0.1);

// 수정 후: 견적서의 실제 세율 적용
const vatRate = vatPercent / 100;
const realCost = totalPrice + optionsPrice - discountPrice
    + (vatIncluded ? 0 : totalPrice * vatRate);
```

`calculateCompletenessScore()`도 재설계했다. 기존에는 포함 항목 수, 원본 파일 여부, 촬영 시간만 봤다. 이 기준으로는 웨딩홀 견적서를 제대로 평가할 수 없다.

```javascript
// 수정 전: 스튜디오 기준으로만 설계된 점수
calculateCompletenessScore(item) {
    let score = 0;
    if (item.includedItems.length > 3) score += 40;
    if (item.rawFilesIncluded)         score += 20;
    if (item.retouchedFilesIncluded)   score += 20;
    if (item.duration)                 score += 20;
    return score;
}

// 수정 후: 웨딩홀·스드메 공통 기준
calculateCompletenessScore(item) {
    let score = 0;
    if (item.includedItems.length >= 3)  score += 20;
    if (item.includedItems.length >= 6)  score += 20;
    if (item.includedItems.length >= 10) score += 10;
    // 가격 투명성
    if (item.vatIncluded)          score += 15;
    if (item.deposit > 0)          score += 10;
    if (item.discountPrice > 0)    score += 10;
    // 상세 정보 제공 여부
    if (item.rentalFee > 0)              score += 5;
    if (item.foodPricePerPerson > 0)     score += 5;
    if (item.guaranteedCapacity > 0)     score += 5;
    return Math.min(score, 100);
}
```

가격 투명성(부가세 포함 여부, 계약금 명시)과 상세 정보 제공 여부를 충실도 지표로 추가했다.

---

## 오전 2: 알림 시스템 구현 시도

오늘의 주요 목표였다. 결혼식 D-7, D-3, D-1 시점에 자동으로 리마인더 알림이 가도록 만드는 것.

세 구간을 연결해야 한다.
1. **프론트 (Expo)** — 기기에 Push Token을 등록하고 Supabase에 저장
2. **백엔드 (FastAPI)** — 매일 실행되는 크론 잡이 일정을 확인하고 알림 발송
3. **외부 API (Expo Push Service)** — 실제 기기에 알림 전달

### `useNotifications.js` — Push Token 등록

```javascript
export function useNotifications() {
  const [expoPushToken, setExpoPushToken] = useState('');
  const { user } = useAuthStore();

  useEffect(() => {
    registerForPushNotificationsAsync().then(token => {
      if (token) {
        setExpoPushToken(token);
        if (user) saveTokenToSupabase(user.id, token);  // Supabase에 저장
      }
    });
    // ...
  }, [user]);
}
```

기기에서 푸시 알림 권한을 요청하고, 발급된 `ExponentPushToken[...]`을 `user_profiles` 테이블에 저장한다. 앱이 실행될 때마다 토큰이 갱신될 수 있어서 매번 upsert한다.

Android는 알림 채널도 별도로 설정해야 한다.

```javascript
if (Platform.OS === 'android') {
    await Notifications.setNotificationChannelAsync('default', {
        name: 'default',
        importance: Notifications.AndroidImportance.MAX,
        vibrationPattern: [0, 250, 250, 250],
        lightColor: '#FF231F7C',
    });
}
```

### `cron_jobs.py` — 백엔드 크론 잡

FastAPI 백엔드에서 매일 실행되는 `run_daily_notification_job()`을 만들었다.

```python
async def run_daily_notification_job():
    today = datetime.now().date()
    target_dates = {
        'D-7': today + timedelta(days=7),
        'D-3': today + timedelta(days=3),
        'D-1': today + timedelta(days=1)
    }

    # 일정(timelines) 중 D-7/D-3/D-1에 해당하는 것들 조회
    timelines_res = supabase.table('timelines') \
        .select('*, projects(couple_id)') \
        .eq('status', 'pending') \
        .execute()

    for t in timelines:
        due_date = datetime.strptime(t['due_date'], '%Y-%m-%d').date()
        diff_days = (due_date - today).days
        if diff_days in [1, 3, 7]:
            await notify_couple(
                couple_id=couple_id,
                title=f"📅 D-{diff_days} 일정 알림",
                body=f"'{t['step_name']}' 일정이 {diff_days}일 남았습니다.",
                ...
            )
```

잔금 일정도 같은 방식으로 D-1, D-3, D-7을 체크한다. 발송은 Expo의 공개 푸시 API(`https://exp.host/--/api/v2/push/send`)를 통해 한다.

### 그리고 폐기

그런데 로컬에서 테스트를 돌리니 알림이 오지 않았다. 원인을 추적하기 시작했다.

Push Token이 Supabase에 저장됐는지 → 저장은 됐다.
크론 잡이 실행됐는지 → 실행은 됐다.
Expo Push API가 응답했는지 → 200 OK가 왔다.
그런데 기기에 알림이 안 온다.

문제는 연결 지점이 너무 많다는 것이다. 실제 기기인지 시뮬레이터인지, 앱이 포그라운드인지 백그라운드인지, 권한이 맞게 부여됐는지, `notification_handler` 설정이 맞는지. 각 구간을 독립적으로 검증하려면 하루를 더 써야 한다.

**폐기를 결정했다.**

남은 개발 일정이 길지 않다. 알림 기능은 Must 기능이 아니다. 작동하지 않는 코드를 붙잡고 있는 것보다, 지금 폐기하고 Must 기능에 집중하는 게 맞다.

---

## 오후: PR 머지와 팀 작업 확인

알림 작업을 정리하고 팀원들 PR 처리로 전환했다.

| 시각 | PR | 내용 |
|------|----|------|
| 10:23 | PR #75 | 마이페이지 버그 수정 |
| 10:30 | PR #76 | 플래너 채팅 창 및 하단 내비게이션 |
| 10:54 | PR #78 | 메인 화면 업체 리스트 버그 수정 |
| 11:53 | PR #79 | 커플 채팅 Supabase 연동 |
| 15:07 | PR #81 | 커플 채팅 초대 기능 |
| 16:11 | PR #83 | 채팅 초대 버그 수정 |
| 16:35 | PR #85 | 채팅 그룹 채팅 버그 수정 |
| 17:32 | PR #86 | 그룹 채팅 테스트 완료 |

커플 채팅이 오늘 하루에만 PR 5개가 올라왔다. 채팅 → 연동 → 초대 기능 → 버그 → 재수정의 흐름이다. 팀원이 빠르게 이터레이션하면서 완성도를 올리고 있다.

---

## 이용약관 · 개인정보처리방침

> `이용약관, 개인정보처리방침 설정` — 16:13
> 7 files, 1,112 insertions(+), 13 deletions(-)

앱 심사나 배포 단계에서 필수로 요구되는 페이지다. 지금 당장은 아니어도, 나중에 급하게 만들면 퀄리티가 떨어지기 때문에 여유가 있을 때 구현해뒀다.

```
app/settings/
├── _layout.jsx      # 설정 라우터 그룹
├── terms.jsx        # 이용약관 (461줄)
└── privacy.jsx      # 개인정보처리방침 (600줄)
```

`mypage.jsx`에서 설정으로 진입하는 버튼을 추가하고, 각 페이지에서 WebView나 스크롤 텍스트로 내용을 보여주는 방식이다.

법적 텍스트는 분량이 많다. `terms.jsx`가 461줄, `privacy.jsx`가 600줄인 이유다.

---

## 저녁: 회원가입 로직 전면 수정

> `회원가입 로직 수정 완료` — 18:45
> 4 files, 406 insertions(+), 219 deletions(-)

오늘의 마지막이자 가장 의미 있는 작업이다.

기존 회원가입 흐름은 이메일과 비밀번호만 넣으면 끝나는 구조였다. 그런데 실제로 테스트해 보니 이미 가입된 이메일로도 가입이 시도됐고, 인증 없이 바로 계정이 만들어졌다.

### 이슈 1: PGRST116 오류

회원가입 또는 로그인 후 프로필을 로딩할 때 에러가 났다.

```
PGRST116: The result contains 0 rows
```

Supabase에서 `.single()`은 정확히 1개의 행이 있어야 한다. 0개이면 에러를 던진다. 신규 가입 직후에는 `user_profiles`에 아직 행이 없을 수 있다.

```javascript
// 수정 전: 0건이면 에러
const { data } = await supabase
    .from('user_profiles')
    .select('*')
    .eq('id', userId)
    .single();

// 수정 후: 0건이면 null 반환
const { data } = await supabase
    .from('user_profiles')
    .select('*')
    .eq('id', userId)
    .maybeSingle();

if (!data) {
    // 프로필이 아직 없는 신규 유저 처리
}
```

`.maybeSingle()`은 0건이면 에러 대신 `null`을 반환한다. null 방어 코드를 추가해서 신규 유저도 정상적으로 처리하도록 했다.

### 이슈 2: 이메일 중복 확인 및 OTP 인증

회원가입 플로우를 단계별로 재설계했다.

```javascript
// lib/auth.js에 추가된 함수들

// 이메일 중복 확인 (Supabase RPC 호출)
export const checkEmailAvailable = async (email) => {
    const { data, error } = await supabase.rpc('check_email_available', {
        check_email: email.toLowerCase(),
    });
    if (error) throw error;
    return data; // true = 사용 가능, false = 이미 사용 중
};

// 이메일 인증 코드(OTP) 검증
export const verifySignupOtp = async (email, token) => {
    const { data, error } = await supabase.auth.verifyOtp({
        email,
        token,
        type: 'signup',
    });
    if (error) throw error;
    if (!data?.user) throw new Error('인증번호가 올바르지 않거나 만료되었습니다.');
    return data;
};
```

`register.jsx`도 전면 재작성했다. 상태 변수만 봐도 단계가 늘어난 게 보인다.

```javascript
// 이메일 확인
const [emailCheckLoading, setEmailCheckLoading] = useState(false);
const [emailAvailable, setEmailAvailable]       = useState(null); // null | true | false

// OTP 발송 / 인증
const [otpSending, setOtpSending]       = useState(false);
const [otpSent, setOtpSent]             = useState(false);
const [otpCode, setOtpCode]             = useState('');
const [otpVerifying, setOtpVerifying]   = useState(false);
const [otpVerified, setOtpVerified]     = useState(false);

// 비밀번호
const [passwordConfirm, setPasswordConfirm] = useState('');
const [showPwConfirm, setShowPwConfirm]     = useState(false);
```

이메일 입력 → 중복 확인 → OTP 발송 → OTP 인증 → 비밀번호 설정 → 가입 완료. 이 흐름이 완성됐다.

---

## 오늘을 돌아보며

알림 시스템을 만들다가 폐기한 게 오늘의 핵심이다.

앱–서버–외부 API 세 구간이 모두 맞물려야 하는 기능은 변수가 너무 많다. 각 구간을 독립적으로 검증하지 않으면 어디서 문제가 났는지 파악하기 어렵다. 오늘은 그 복잡성을 직접 맞닥뜨렸고, 남은 일정을 고려해서 **과감히 폐기했다.**

작동하지 않는 기능을 붙잡고 시간을 쏟는 것보다, 우선순위를 정하고 더 중요한 것에 집중하는 결정이 때로는 더 나은 선택이다.

PGRST116 같은 Supabase 특유의 에러도 오늘 처음 제대로 이해했다. `.single()`과 `.maybeSingle()`의 차이는 작아 보이지만, 신규 가입 직후처럼 데이터가 아직 없는 경우를 얼마나 방어적으로 처리하느냐의 차이다.

---

## 화면 기록

알림, 회원가입, 약관 대응을 같이 정리한 날이라 관련 자료를 한 곳에 모았다. 실제 알림 화면과 인증 인프라 대응, 법적 문서 예시가 Day 7의 성격과 가장 잘 맞는다.

<img src="/images/posts/mongle/couple-notification-screen.png" alt="Mongle couple 알림 화면" style="display:block; width:100%; max-width:420px; margin:1rem auto;" />

<img src="/images/posts/mongle/supabase-email-smtp-settings.png" alt="Supabase 이메일 인증 제한을 AWS Route53, AWS SES, SMTP 설정으로 확장한 기록" style="display:block; width:100%; max-width:760px; margin:1rem auto;" />

<img src="/images/posts/mongle/legal-response-document.png" alt="법적 문제 대응을 위한 예시 문서" style="display:block; width:100%; max-width:760px; margin:1rem auto;" />

---

**GitHub:** [APP-Project-Team1/mongle](https://github.com/APP-Project-Team1/mongle)
---

## 가입 플로우가 서비스 첫인상을 결정한다

알림, 약관, 회원가입은 상대적으로 화려하지 않은 기능처럼 보이지만, 실제 제품에서는 가장 먼저 사용자를 만나게 되는 구간이다. 이 흐름이 어색하면 사용자는 핵심 기능을 보기 전에 이탈한다. Day 7에서 이 영역을 정리한 것은 "들어오게 만드는 경험"을 만들었다는 점에서 중요했다.

특히 커플이 함께 쓰는 서비스에서는 가입 단계에서 신뢰감이 더 중요하다. 개인정보와 일정, 예산이 얽혀 있기 때문에 사용자는 작은 문구 하나, 상태 표시 하나에도 민감하게 반응한다. 그래서 약관 문구, 인증 흐름, 예외 케이스 처리까지 디테일이 필요했고, 이 작업은 이후의 고급 기능 못지않게 서비스 완성도에 큰 영향을 준다.
