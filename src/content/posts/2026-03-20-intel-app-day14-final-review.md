---
title: "[App Dev] Day 14: 총정리, 3주 만에 웹과 앱을 건드리다"
slug: intel-app-day14-final-review
date: 2026-03-20
author: Evan Yoon
category: study
subcategory: bootcamp
description: "Intel AI 앱 개발 14일차. HTML부터 React Native까지 3주 과정을 돌아보며 무엇을 배웠는지, 어디서 성장했는지 정리했다."
thumbnail: ""
tags:
  - react
  - react-native
  - html
  - javascript
  - fastapi
  - bootcamp
  - retrospective
readTime: 12
series: "Intel App Dev 2026"
seriesOrder: 14
featured: false
draft: false
toc: true
---

## 3주 전과 지금

3월 3일에 `01_basic.html` 파일을 처음 만들었다. `<h1>Hello</h1>` 하나를 브라우저에서 확인하던 날이 3주 전이다. 오늘 종강을 맞아 그 파일부터 업다운 게임 Lottie 애니메이션까지 어떻게 흘러왔는지 정리한다.

이 회고는 개념 설명이 아니다. 무엇이 막혔고, 어떻게 풀렸고, 지금 남은 것이 무엇인지에 대한 기록이다.

---

## Week 1: 웹의 기초를 쌓다 (HTML, CSS, JavaScript)

### HTML (Day 1)

처음에 HTML은 "아는 것"이라고 생각했다. 어디선가 본 태그들이고, 구조도 단순하다. 그런데 막상 직접 회원가입 폼을 만들면서 라디오 버튼의 `name` 속성을 틀리게 써서 두 개가 동시에 선택되는 걸 경험했다. "아는 것"과 "손에 익은 것"은 다르다.

시맨틱 태그(`<header>`, `<nav>`, `<main>`)는 결과는 똑같이 보이는데 왜 써야 하는지 처음에 납득이 안 됐다. 나중에 스크린리더와 SEO를 접하면서 다시 이해했다.

### CSS와 Tailwind (Day 2)

박스 모델은 도식으로 보면 쉬워 보이는데, `padding`과 `margin`을 실제로 쓸 때 자꾸 헷갈렸다. 배경색을 주면 어디까지 채워지는지를 직접 확인하면서 잡았다.

Tailwind는 처음에 클래스 이름이 너무 길어서 "이게 더 편한가?" 싶었다. 그런데 CSS 파일을 왔다갔다 안 해도 된다는 게 생각보다 큰 장점이었다. 특히 `hover:`, `md:`, `lg:` 접두사로 반응형을 처리하는 방식이 직관적이었다.

### JavaScript (Day 3)

Python을 먼저 배운 게 장점이 됐고 단점도 됐다. 개념은 빠르게 이해됐지만, `===` 대신 `==`를 쓰거나, `NaN !== NaN`이 true인 것 같은 JavaScript 특유의 동작에서 계속 놀랐다.

`map`과 `filter`는 처음 쓸 때는 이게 왜 이렇게 자주 나오는지 몰랐다. React에서 리스트를 렌더링하면서 이 함수들이 얼마나 자주 쓰이는지 파악됐다.

비동기(`async/await`)는 개념은 이해했지만, `useEffect` 안에서 어떻게 써야 하는지는 실제로 써보기 전까지 헷갈렸다.

---

## Week 2: React로 웹 앱을 만들다

### JSX와 컴포넌트 (Day 4, 5)

JSX는 처음에 "HTML인가 JS인가" 구분이 안 됐다. `className`, self-closing 태그, 중괄호 안의 표현식 등을 하나씩 맞닥뜨리면서 익혔다.

컴포넌트 분리는 처음에 왜 해야 하는지 이론으로만 알았다. `TeamPage`를 만들면서 팀원 데이터가 배열로 있고 `TeamMember` 컴포넌트가 재사용되는 걸 직접 경험하니, 컴포넌트를 왜 쪼개는지 이해됐다.

### 상태 관리 (Day 6)

`useState`의 핵심은 **직접 바꾸지 않는다**는 것이다. 처음에 배열에 `push`로 넣으면 되지 않나 생각했다가, 화면이 안 바뀌는 걸 경험하고 나서야 제대로 이해했다. 상태 불변성은 말로 배우는 게 아니라 실수해봐야 각인된다.

`CafeMenu`에서 `reduce()`로 합계를 계산하는 패턴이 실용적이었다. 배열 메서드를 React 상태와 조합하는 방법을 처음 제대로 써본 곳이다.

### 제어 컴포넌트와 TodoList (Day 7)

폼 입력 값을 상태로 관리한다는 게 처음에는 과하게 느껴졌다. 브라우저가 알아서 관리해주면 되지 않나 했다. 그런데 유효성 검사, 실시간 피드백, 제출 전 처리 같은 기능들이 상태 없이는 구현하기 어렵다는 걸 직접 만들면서 알게 됐다.

TodoList는 이번 과정에서 가장 많은 개념이 들어간 컴포넌트다. 상태, 이벤트 처리, 컴포넌트 분리, 부모-자식 데이터 흐름, LocalStorage, 모달까지. 하나를 완성하면서 React의 기본 패턴 대부분을 썼다.

### API 연동과 FastAPI (Day 8, 9)

비동기 API 연동에서 외부 데이터가 실제로 화면에 반영되는 흐름을 처음 완성했다.

FastAPI는 Python을 알면 진입 장벽이 낮다. 데코레이터로 엔드포인트를 만들고, 자동 문서가 생기는 게 직관적이었다. CORS를 빠뜨렸다가 React에서 빨간 에러를 보고 나서야 "아, 포트가 다르면 허용을 해줘야 하는구나"를 제대로 이해했다.

---

## Week 3: React Native로 모바일로 넘어가다

### 기본 컴포넌트 (Day 10)

React를 알면 React Native가 쉬울 줄 알았다. 문법은 같은데 환경이 완전히 달랐다. `<div>` 대신 `<View>`, `<p>` 대신 `<Text>`. `flex-direction` 기본값이 column이라는 것도 처음에 레이아웃이 이상하게 나와서 알게 됐다.

Expo Go 앱으로 QR 코드를 찍으면 실기기에서 바로 실행되는 게 신기했다. 에뮬레이터 없이 실제 폰에서 테스트하는 경험은 처음이었다.

### 사용자 입력 (Day 11)

모바일 입력이 웹보다 고려할 게 많았다. `keyboardType`으로 키보드 종류를 지정하고, `KeyboardAwareScrollView`로 키보드가 올라와도 입력 필드가 가려지지 않게 처리하고, `ref`로 다음 필드 이동을 구현하는 것까지. 웹에서 `<input type="email">`로 끝나던 것이 모바일에서는 여러 속성 조합이 필요했다.

### 데이터 기반 렌더링 (Day 12)

`MoodScreen`에서 데이터 배열이 UI를 결정하는 구조를 명확하게 만들었다. 기분 종류를 하나 추가하고 싶으면 배열에 하나만 추가하면 된다. 코드를 건드릴 필요가 없다. 이 구조가 자연스럽게 손에 익은 것 같다.

`ProfileCard`의 글래스모피즘 효과는 처음 해보는 시도였다. `LinearGradient`와 `BlurView`를 합쳐서 원하는 시각적 효과를 냈을 때, React Native로도 꽤 세련된 UI를 만들 수 있다는 걸 알게 됐다.

### 모델 서빙과 카메라 (Day 13)

갤러리에서 이미지를 골라 FastAPI 서버로 보내고, 모델의 추론 결과를 다시 화면에 보여주는 흐름을 완성했다. 이 파이프라인이 바로 Walkmate 같은 AI 앱의 기본 구조다.

Lottie 애니메이션은 기능적으로는 필수가 아니지만, 게임의 완성도를 높이는 데 효과적이었다. 정답을 맞혔을 때 폭죽 애니메이션 하나로 결과 화면이 달라졌다.

---

## 3주 동안 만든 것들

```
basic/
├── HTML 기본 구조 연습
├── 레시피 페이지
├── 회원가입 폼 (일반 CSS / Tailwind)
├── 박스모델 명함 카드
├── 플렉스박스 레이아웃
└── JavaScript 핵심 실습 (변수, 함수, map/filter, 비동기)

react-web/
├── 팀 소개 페이지 (컴포넌트 분리, props)
├── 카운터 (useState, 조건부 스타일)
├── 좋아요 버튼 (토글 상태, lucide-react)
├── 카페 주문 앱 (배열 상태, reduce)
├── 로그인 폼 (제어 컴포넌트, 유효성 검사)
├── TodoList (CRUD, LocalStorage, 모달)
├── 게시글 목록 (JSONPlaceholder API)
├── 고양이 이미지 그리드 (외부 이미지 API)
└── 랜덤 동물 (FastAPI 연결)

server/
└── FastAPI 서버 (/, /about, /animal 엔드포인트)

mobile-app/
├── 업다운 게임 (상태 관리, Lottie 애니메이션)
├── 입력 폼 (TextInput, 다양한 키보드 타입)
├── 프로필 카드 (BlurView, LinearGradient, 글래스모피즘)
└── 기분 선택 (FlatList, 데이터 기반 렌더링)
```

---

## 남은 것과 부족한 것

**알게 된 것**: HTML/CSS/JavaScript의 기본 동작 방식. React의 컴포넌트-상태-props 구조. 외부 API 연동 방법. FastAPI로 서버 만들기. React Native에서 모바일 앱 기초.

**아직 얕은 것**: TypeScript를 전혀 안 썼다. 상태 관리가 복잡해지면 `useContext`나 Zustand 같은 도구가 필요한데 거기까지는 못 갔다. React Native에서 React Navigation 없이 탭만 만들었다. 테스트 코드는 하나도 안 썼다.

**앞으로 할 것**: 이번 과정에서 배운 React와 React Native가 실제 프로젝트와 어떻게 연결되는지는 이미 Walkmate와 Mongle에서 경험하고 있다. 기초를 제대로 잡은 상태에서 실제 프로젝트에서 부딪히는 게 지금 단계에선 가장 빠른 방법이다.

---

## 한 줄 정리

3주 만에 HTML부터 React Native 모델 서빙까지 다 건드렸다. 깊게 알지는 못한다. 하지만 무엇이 있는지는 이제 안다. 그리고 그게 이 3주의 목표였다.
