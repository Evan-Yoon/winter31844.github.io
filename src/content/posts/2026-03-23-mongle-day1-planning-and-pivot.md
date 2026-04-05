---
title: "[Mongle] Day 1 — 기획 피벗, 그리고 폴더 구조 설계"
slug: "mongle-day1-planning-and-pivot"
date: 2026-03-23
author: "Evan Yoon"
category: "project"
subcategory: "team-project"
description: "AI 메뉴판 번역 앱에서 웨딩 AI 플랜 서비스로 피벗. Mongle의 첫 날, 기획 확정과 Expo Router 폴더 구조 세팅까지."
tags:
  - mongle
  - react-native
  - expo
  - supabase
  - team-project
  - planning
readTime: 8
series: "Mongle 프로젝트"
seriesOrder: 1
featured: false
draft: false
toc: true
---

## 프로젝트 피벗: 메뉴판 번역 → 웨딩 AI 플랜

사실 처음부터 Mongle이 아니었다.

팀이 처음 잡았던 주제는 **AI 기반 메뉴판 사진 분석·번역 앱**이었다. 해외여행 중 외국어 메뉴판을 카메라로 찍으면 Vision API가 텍스트를 분석하고 번역해주는 앱. 아이디어 자체는 나쁘지 않았지만, 팀 내부 논의를 거치면서 문제들이 보이기 시작했다.

차별화 포인트가 약하다. 이미 Google Lens가 거의 완벽하게 해결하고 있는 문제다. 그리고 우리가 7일 안에 MVP를 만들어야 한다는 조건에서, 카메라·Vision API·번역 파이프라인을 전부 붙이는 건 리스크가 너무 컸다.

그래서 **피벗**을 결정했다.

새로운 주제: **예비 부부 & 웨딩 플래너 협업 AI 플랫폼 — Mongle**.

결혼 준비는 크게 두 당사자가 있다. 준비하는 예비 부부, 그리고 이를 도와주는 웨딩 플래너. 이 둘이 일정·예산·업체 정보를 실시간으로 공유하고, AI 챗봇이 중간에서 조율을 도와주는 서비스. 사용자 페르소나가 명확하고, 기능 범위도 현실적으로 잡힐 것 같았다.

---

## 기능 우선순위 정의 (MoSCoW)

MVP 범위를 잡기 위해 MoSCoW 기법으로 기능 우선순위를 정리했다.

| 구분 | 기능 |
|------|------|
| **Must** | 일정 관리, 예산 관리, AI 챗봇, 업체 리스트 |
| **Should** | 로그인/회원가입, 플래너 화면, 리마인드 알림 |
| **Could** | 서류 보관함, 잔금 알림, 커플 채팅 |
| **Won't** | 청첩장 기능 |

Must 기능 데드라인을 **3월 26일(목)**으로 설정했다. 주말(3/28~3/29) 개발 불가 조건이 추가되면서 실질 개발 가능일이 9일에서 7일로 줄었기 때문에, 일정표를 전체 재설계해야 했다.

---

## 4인 역할 분담

| 역할 | 담당 영역 |
|------|-----------|
| **나 (PM)** | 전체 일정·Git 관리, AI 챗봇 UI, 발표 |
| **팀원 B** | 로그인·회원가입, 업체 화면, 플래너 목록 |
| **팀원 C** | 백엔드 연동, DB 설계, Supabase, 전역 상태 |
| **팀원 D** | 커플·플래너 대시보드, 타임라인, 예산 관리 UI |

처음 초안에서는 발표 자료 제작과 최종 준비를 팀원 D에게 배정했는데, 내가 PM이면 발표도 직접 챙기는 게 맞다고 판단해서 조정했다. 그 대신 D의 여유 시간을 서류 보관함·잔금 알림 UI로 재배분했다.

---

## 커밋 1: 전체 폴더 구조 재세팅

> `chore: 프로젝트 주제 변경에 따른 전체 폴더 구조(Mongle-app) 재세팅`  
> 16:31 — 72 files changed, 173 insertions(+), 121 deletions(-)

기존 메뉴판 번역 앱 구조를 전부 걷어내고 Mongle 구조로 갈아엎었다. 변경 파일이 72개다. 사실상 새 프로젝트 초기화다.

**핵심 결정: Expo Router 기반 라우팅 구조**

```text
app/
├── (auth)/          # 로그인·회원가입 [팀원 B]
├── (couple)/        # 커플 전용 탭 [팀원 D + 나]
│   ├── chat.jsx     # AI 챗봇 UI [나]
│   ├── timeline.jsx # 결혼 준비 타임라인 [팀원 D]
│   └── budget.jsx   # 비용 명세서 [팀원 D]
└── (planner)/       # 플래너 전용 탭 [팀원 D]
    └── dashboard.jsx
```

Expo Router의 그룹 라우팅(`(couple)`, `(planner)`)으로 역할별 화면을 완전히 분리했다. 커플 앱과 플래너 앱이 하나의 코드베이스 안에서 role 기반으로 다른 탭을 보여주는 구조다.

빈 파일들로 placeholder를 먼저 생성해두고, 팀원 각자가 자기 파일을 채워가는 방식으로 협업을 시작한다.

---

## 커밋 2: 폴더별 역할 설정 & README 전면 개편

> `chore: 폴더별 역할 설정`  
> 16:46 — README.md 전면 재작성

README를 완전히 갈아엎으면서 폴더 구조에 담당자를 직접 명시했다.

```text
├── components/
│   ├── common/           # [공통] 전원 사용
│   ├── chat/             # [나(PM)] MessageBubble, ChatInput
│   ├── budget/
│   │   └── AiAnalysisCard.jsx  # [나(PM)] SSE 스트리밍 결과 표시
│   └── vendor/           # [팀원 B]
```

내가 직접 맡은 컴포넌트는 **채팅 UI**와 **AI 분석 카드**다. `AiAnalysisCard.jsx`는 AI 응답을 SSE(Server-Sent Events)로 스트리밍 받아서 표시하는 컴포넌트인데, 이건 나중에 백엔드 팀원 C와 붙여야 한다.

---

## 오늘 하루를 돌아보며

3/23은 코드를 한 줄도 짜지 않은 날이다. 그런데 이게 낭비라는 느낌이 전혀 없다.

기획 단계에서 역할 분담과 우선순위를 꼼꼼하게 정리해두니 내일부터 바로 코딩으로 들어갈 수 있다는 확신이 생겼다. 처음에는 일정이 빠듯하다는 불안감이 있었는데, 시간표를 시각화하고 나니 오히려 "이 정도면 할 수 있겠다"는 감각이 생겼다.

다만 주말 제외로 실 개발일이 생각보다 짧다는 점은 계속 신경 쓰인다.

**내일(3/24) 오전 안에 환경설정을 끝내는 게 첫 번째 분기점이 될 것 같다.**

- GitHub 레포 세팅 완료
- Expo 프로젝트 초기화
- Supabase 프로젝트 생성 + DB ERD 작성
- `lib/supabase.js`, `stores/authStore.js`, `app/_layout.jsx` 구현 시작
---

**GitHub:** [APP-Project-Team1/mongle](https://github.com/APP-Project-Team1/mongle)
