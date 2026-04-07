---
title: '신규 입사자를 위한 AI 온보딩 앱: React Native + GPT-4o-mini'
slug: onboarding-chatbot-react-native-openai
date: 2026-03-20
author: Evan Yoon
category: project
subcategory: personal-project
description: 신규 입사자가 혼자서도 온보딩을 완료할 수 있도록 FAQ 검색, 체크리스트, AI 챗봇을 하나의 모바일 앱으로 만든 프로젝트.
  React Native + Node.js + GPT-4o-mini 7시간 개발기.
tags:
  - react-native
  - expo
  - openai
  - gpt-4o-mini
  - hr
  - onboarding
  - nodejs
  - side-project
  - personal-project
readTime: 10
featured: false
draft: false
toc: true
---

## 온보딩의 현실

신규 입사자가 첫날 회사에 들어오면 정보의 홍수를 맞닥뜨린다. 복지 제도는 어떻게 되는지, 연차는 어떻게 쓰는지, 비품 신청은 어디서 하는지, 시스템 접근 권한은 언제 나오는지.

이 질문들은 담당자에게도 반복적이다. 새로운 사람이 들어올 때마다 HR 담당자는 같은 설명을 되풀이한다. 매뉴얼을 만들어봐도 찾아보는 사람이 많지 않다.

HR 업무를 하면서 느낀 이 구조적인 비효율을 앱으로 풀어보고 싶었다.

> "신규 입사자가 HR 담당자에게 묻지 않고도, 스스로 온보딩을 마칠 수 있다면?"

## 기획서와 시연 영상

프로젝트를 구현하기 전에 정리한 기획서는 아래 파일에서 확인할 수 있다.

- [onboarding assistant 기획서.pdf](/files/onboarding-assistant-proposal.pdf)

실제 앱 흐름은 아래 시연 영상으로 바로 볼 수 있다.

<div style="position:relative; width:100%; max-width:760px; margin:1rem auto; aspect-ratio:9 / 16;">
  <iframe
    src="https://www.youtube.com/embed/0RwLUvkwP70"
    title="온보딩 챗봇 시연 영상"
    loading="lazy"
    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
    referrerpolicy="strict-origin-when-cross-origin"
    allowfullscreen
    style="position:absolute; inset:0; width:100%; height:100%; border:0; border-radius:16px;"
  ></iframe>
</div>

## 프로젝트 개요

**Onboarding-Chatbot**은 신규 입사자를 위한 모바일 온보딩 어시스턴트 앱이다. FAQ 검색, 온보딩 체크리스트, AI 챗봇 세 가지 기능을 하나의 앱에서 제공한다.

- **GitHub**: [Evan-Yoon/Onboarding-Chatbot](https://github.com/Evan-Yoon/Onboarding-Chatbot)
- **Frontend**: React Native (Expo)
- **Backend**: Node.js + Express
- **AI**: OpenAI GPT-4o-mini
- **개발 기간**: 약 7시간 (2026년 3월 20일, 커밋 9개)

## 전체 구조

앱은 프론트엔드와 백엔드가 분리된 구조다.

```
Onboarding-Chatbot/
├── chatbot-assistant/       # React Native 앱 (Expo)
│   └── src/
│       ├── screens/         # 4개 화면
│       ├── components/      # 26개 UI 컴포넌트
│       ├── context/         # 전역 상태 (ChecklistContext)
│       ├── data/            # FAQ, 체크리스트, 챗봇 데이터
│       ├── navigation/      # 하단 탭 네비게이터
│       └── constants/       # 테마 (색상, 간격)
└── server/
    └── index.js             # Express + OpenAI API 서버
```

## 4개 화면

### Home — 대시보드

앱을 열면 대시보드가 가장 먼저 보인다.

```
ZeroWorks 헤더
└── 지현님, 환영합니다! 👋
    ├── 긴급 미션 카드: "오늘 오후 6시까지 근로계약서 제출"
    ├── 기능 그리드
    │   ├── FAQ 12건
    │   ├── 챗봇 제안 질문 5개
    │   └── 체크리스트 진행률 (남은 항목 수)
    └── 최근 완료 항목 타임라인
```

`useMemo`로 전체 항목 수, 완료 수, 잔여 수, 진행률(%)을 계산해서 표시한다. 체크리스트에서 항목을 완료할 때마다 홈 화면의 수치가 같이 업데이트된다.

### FAQ — 회사 지식베이스

12개의 FAQ가 6개 카테고리로 분류되어 있다.

| 카테고리 | 내용 예시 |
|----------|----------|
| 근무 | 유연근무제 운영 방식, 근무 시간 |
| 인사 | 승진 체계, 조직도 |
| 복지 | 건강검진, 교육 지원, 차량 할인 |
| 휴가 | 연차 정책, 공휴일 |
| 교육 | 사내 교육, 외국어 강좌 |
| 온보딩 | 첫날 절차 |

검색창에 텍스트를 입력하거나 카테고리 칩을 탭하면 `useMemo`로 필터링된 결과가 아코디언 형태로 열린다. FAQ에 없는 질문은 화면 하단의 "챗봇에게 물어보기" 버튼으로 바로 이어진다.

### Checklist — 단계별 온보딩 추적

온보딩 체크리스트는 3단계로 나뉜다.

**입사 첫날** (5개 항목)
- HR 오리엔테이션 참석
- 서류 제출
- 사원증 발급
- 장비 배급
- 시스템 접근 권한 확인

**입사 첫 주** (5개 항목)
- 부서 온보딩
- 유연근무 정책 검토
- 휴가 절차 학습
- 비품 신청 교육
- 보안 교육

**입사 첫 달** (5개 항목)
- 1:1 면담
- 업무 적응 피드백
- 교육 진행 평가
- 시스템 숙련도 확인
- 문화 만족도 설문

항목을 탭하면 완료/미완료가 토글되고 타임스탬프가 기록된다. 상단에는 진행률 바가 실시간으로 업데이트된다.

### Chatbot — 2단계 응답 전략

가장 핵심 화면이다. 단순히 AI API를 붙인 게 아니라 **로컬 매칭 → AI 폴백** 구조를 설계했다.

```javascript
const findBotAnswer = (userInput) => {
  const lowerInput = userInput.toLowerCase().trim();

  // 1단계: 로컬 규칙 매칭
  for (const rule of chatbotRules) {
    if (rule.keywords.some((kw) => lowerInput.includes(kw))) {
      return rule.answer;
    }
  }

  // 2단계: 로컬 FAQ 매칭
  for (const faq of faqData) {
    if (lowerInput.includes(faq.question.toLowerCase())) {
      return faq.answer;
    }
  }

  // 3단계: OpenAI API 호출
  return null; // null이면 서버로 요청
};
```

1. 먼저 로컬 데이터(연차, 비품 신청, 장비, 복지 등 5개 규칙)에서 키워드 매칭을 시도한다.
2. 없으면 FAQ 데이터에서 다시 찾는다.
3. 거기서도 없으면 백엔드 서버를 통해 GPT-4o-mini에 요청한다.

자주 묻는 질문은 API 호출 없이 즉각 응답하기 때문에 응답 속도가 빠르고 비용도 절감된다.

## 백엔드: GPT-4o-mini 연동

```javascript
// server/index.js
app.post("/chat", async (req, res) => {
  const { message } = req.body;

  const completion = await client.chat.completions.create({
    model: "gpt-4o-mini",
    messages: [
      {
        role: "system",
        content:
          "You are an onboarding assistant for new employees. Answer clearly and briefly in Korean.",
      },
      {
        role: "user",
        content: message,
      },
    ],
    temperature: 0.4,
  });

  const answer = completion.choices?.[0]?.message?.content
    ?? "답변을 생성하지 못했습니다.";
  res.json({ answer });
});
```

`temperature: 0.4`로 설정한 건 의도적인 선택이다. 창의적인 대화가 아닌 HR 정책 안내가 목적이기 때문에, 일관되고 사실에 가까운 답변이 나와야 한다. 온도가 높으면 같은 질문에 다른 답이 나올 수 있어서 신뢰도가 떨어진다.

시스템 프롬프트도 명확하게 역할을 한정했다. "신규 입사자를 위한 온보딩 어시스턴트"로 범위를 제한해서 HR 외 주제에 대한 엉뚱한 답변을 줄였다.

## 전역 상태 관리: Context API

체크리스트 완료 상태는 앱 전반에서 공유된다. 홈 화면에서도, 체크리스트 화면에서도 같은 데이터를 바라봐야 한다.

```javascript
// src/context/ChecklistContext.js
export const ChecklistProvider = ({ children }) => {
  const [checklist, setChecklist] = useState(initialChecklistData);

  const toggleItem = (phase, itemId) => {
    setChecklist((prev) =>
      prev.map((section) =>
        section.phase === phase
          ? {
              ...section,
              items: section.items.map((item) =>
                item.id === itemId
                  ? {
                      ...item,
                      completed: !item.completed,
                      completedAt: !item.completed ? new Date().toISOString() : null,
                    }
                  : item
              ),
            }
          : section
      )
    );
  };

  return (
    <ChecklistContext.Provider value={{ checklist, toggleItem }}>
      {children}
    </ChecklistContext.Provider>
  );
};
```

Redux 없이 Context API만으로 충분했다. 앱 규모에 비해 과도한 보일러플레이트를 피하는 선택이었다.

## 디자인 시스템

`theme.js` 하나에 모든 색상, 간격, 모서리 둥글기를 정의했다.

```javascript
export const theme = {
  colors: {
    primary: "#111C88",      // 메인 브랜드 컬러 (네이비)
    surface: "#F5F5F5",      // 배경 서피스
    text: {
      primary: "#1A1A1A",
      secondary: "#666666",
      disabled: "#AAAAAA",
    },
  },
  spacing: {
    xs: 6, sm: 10, md: 14, lg: 20, xl: 28, xxl: 32,
  },
  radius: {
    sm: 10, md: 14, lg: 20, xl: 28, pill: 999,
  },
};
```

26개 컴포넌트 모두 이 테마에서 값을 가져온다. 디자인을 바꿀 때 여기 한 곳만 수정하면 전체가 바뀐다.

## 커밋 기록으로 보는 개발 흐름

9개 커밋, 총 7시간 작업이었다.

```
01:01  Initial commit
01:26  setting up folders
01:37  making 4 simple screens and navigator function
03:05  make&refactoring home screen and FAQ screen
05:31  feat: add initial UI for onboarding chatbot
06:15  feat: set up initial onboarding chatbot assistant
06:31  chore: Install initial project dependencies
06:40  gitignore
08:09  feat: Implement initial onboarding chatbot server (OpenAI 연동)
```

폴더 구조 잡기 → 화면 4개 뼈대 → 홈/FAQ UI 구체화 → 챗봇 UI → 앱 기반 설정 → OpenAI 서버 연동 순서로 진행됐다. 화면 구조를 먼저 잡고 AI 연동을 마지막에 붙이는 방식이었다.

## 설계에서 신경 쓴 것들

**오프라인 우선**: API 비용과 응답 속도를 모두 고려해 로컬 매칭을 먼저 시도하는 구조를 잡았다. 서버가 내려가도 자주 묻는 질문은 여전히 답할 수 있다.

**3단계 체크리스트**: 입사 첫날/첫 주/첫 달로 나눈 건 심리적 부담을 줄이기 위해서다. 15개 항목을 한꺼번에 보여주면 압도감을 준다. 단계별로 나누면 지금 해야 할 것에 집중하게 된다.

**시스템 프롬프트 범위 제한**: GPT는 온보딩 질문 외에도 무엇이든 답할 수 있다. 하지만 이 앱에서는 그게 오히려 문제가 된다. 역할을 명확히 제한해서 신뢰할 수 있는 어시스턴트처럼 동작하게 했다.

## 마무리

이 프로젝트에서 가장 재미있었던 부분은 기술 선택보다 기능 설계였다.

FAQ 검색, 체크리스트, AI 챗봇이라는 세 기능은 사실 독립적으로 존재할 수도 있다. 그런데 온보딩이라는 맥락 안에서 이 셋은 서로 연결된다. FAQ에서 못 찾으면 챗봇으로 간다. 챗봇이 체크리스트 관련 질문에 답하면 자연스럽게 체크리스트 화면으로 이어진다. 홈 대시보드는 이 흐름을 전부 한눈에 보여준다.

HR 업무를 했기 때문에 신규 입사자가 어떤 순서로 혼란을 겪는지 알고 있었다. 그 경험이 기능 배치와 데이터 구조 설계에 직접적으로 반영됐다.

비품 발주 자동화 스크립트, 채용 결과 메일 발송기에 이어 세 번째 HR 도메인 프로젝트다. 작은 스크립트에서 시작해 모바일 앱까지 왔다. 이번 작업에서는 답변 품질 자체보다도, 신규 입사자가 중간에 막히지 않고 다음 단계로 자연스럽게 넘어가게 만드는 흐름 설계가 더 중요하다는 점을 분명히 배웠다.
