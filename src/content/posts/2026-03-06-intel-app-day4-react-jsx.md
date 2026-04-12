---
title: "[App Dev] Day 4: React와 JSX, 컴포넌트의 시작"
slug: intel-app-day4-react-jsx
date: 2026-03-06
author: Evan Yoon
category: study
subcategory: bootcamp
description: "Intel AI 앱 개발 4일차. Vite로 React 프로젝트를 셋업하고 JSX 문법과 컴포넌트 개념을 처음으로 익혔다."
thumbnail: ""
tags:
  - react
  - jsx
  - vite
  - frontend
  - bootcamp
readTime: 9
series: "Intel App Dev 2026"
seriesOrder: 4
featured: false
draft: false
toc: true
---

## HTML 파일 하나에서 React 프로젝트로

지금까지는 파일 하나에 HTML을 쓰고, CSS 파일을 연결하고, JS 파일을 따로 불러오는 방식이었다. 규모가 작을 때는 그래도 되지만, 화면이 수십 개로 늘어나면 이 방식은 금방 한계에 부딪힌다.

React는 이 문제를 **컴포넌트**라는 개념으로 해결한다. UI를 독립적인 조각으로 쪼개고, 조각을 조합해서 화면을 만드는 방식이다. 오늘은 React 프로젝트를 처음 셋업하고, JSX 문법을 익히는 것이 목표였다.

---

## Vite로 React 프로젝트 만들기

예전에는 `create-react-app`을 많이 썼지만 요즘은 **Vite**가 더 빠르고 가볍다. 오늘 프로젝트는 Vite 기반으로 시작했다.

```bash
npm create vite@latest react-web -- --template react
cd react-web
npm install
npm run dev
```

`npm run dev`를 실행하면 로컬 개발 서버가 켜진다. 기본적으로 `http://localhost:5173`에서 열린다. 파일을 저장할 때마다 브라우저가 자동으로 새로고침된다. 이걸 HMR(Hot Module Replacement)이라고 한다.

생성된 폴더 구조:

```
react-web/
├── src/
│   ├── App.jsx       ← 메인 컴포넌트
│   ├── main.jsx      ← 진입점
│   └── index.css     ← 전역 스타일
├── index.html        ← HTML 껍데기 (거의 비어 있음)
├── vite.config.js
└── package.json
```

`index.html`을 열어보면 `<div id="root"></div>` 하나밖에 없다. React가 이 div 안에 모든 걸 그려 넣는다.

---

## JSX란 무엇인가

JSX는 **JavaScript 안에 HTML처럼 생긴 문법을 쓸 수 있게 해주는 확장 문법**이다. React 컴포넌트는 JSX를 반환한다.

```jsx
// 일반 JavaScript로 DOM 요소 만들기
const element = document.createElement("h1");
element.textContent = "Hello";

// JSX 방식
const element = <h1>Hello</h1>;
```

JSX는 브라우저가 직접 읽지 못한다. Vite(내부적으로 Babel)가 이걸 `React.createElement()` 호출로 변환한다. 우리가 눈으로 보는 것과 실제 실행되는 코드는 다르다.

---

## 첫 번째 컴포넌트

React에서 컴포넌트는 **JSX를 반환하는 함수**다. 이름은 반드시 대문자로 시작해야 한다.

```jsx
// src/App.jsx
function App() {
  return (
    <div>
      <h1>안녕하세요</h1>
      <p>첫 번째 React 컴포넌트입니다.</p>
    </div>
  );
}

export default App;
```

그리고 이 컴포넌트를 화면에 올리는 진입점:

```jsx
// src/main.jsx
import ReactDOM from "react-dom/client";
import App from "./App.jsx";
import "./index.css";

ReactDOM.createRoot(document.getElementById("root")).render(<App />);
```

`<App />`처럼 컴포넌트를 HTML 태그처럼 쓴다. 이게 JSX의 핵심이다.

---

## JSX 문법 규칙

JSX는 HTML과 비슷하지만 다른 점이 있다.

**1. 반드시 하나의 루트 요소로 감싸야 한다**

```jsx
// 오류
return (
  <h1>제목</h1>
  <p>내용</p>
);

// 올바른 방식 1: div로 감싸기
return (
  <div>
    <h1>제목</h1>
    <p>내용</p>
  </div>
);

// 올바른 방식 2: Fragment 사용 (불필요한 div 없이)
return (
  <>
    <h1>제목</h1>
    <p>내용</p>
  </>
);
```

**2. className, htmlFor**

HTML의 `class`는 JSX에서 `className`으로 써야 한다. `for`는 `htmlFor`.

```jsx
<div className="container">...</div>
<label htmlFor="input-id">라벨</label>
```

**3. 중괄호로 JavaScript 표현식 삽입**

```jsx
const name = "Evan";
const isLoggedIn = true;

return (
  <div>
    <p>안녕하세요, {name}님</p>
    <p>현재 시각: {new Date().toLocaleTimeString()}</p>
    {isLoggedIn && <p>로그인 상태입니다.</p>}
  </div>
);
```

JSX 안에서 `{}`로 감싸면 어떤 JavaScript 표현식이든 쓸 수 있다. 단, `if` 문은 직접 못 쓰고 삼항 연산자나 `&&`를 쓴다.

**4. self-closing 태그**

내용이 없는 태그는 반드시 닫아야 한다.

```jsx
<img src="photo.jpg" alt="사진" />
<input type="text" />
<br />
```

---

## 조건부 렌더링

```jsx
function Greeting({ isLoggedIn }) {
  return (
    <div>
      {isLoggedIn ? (
        <p>환영합니다!</p>
      ) : (
        <p>로그인해주세요.</p>
      )}

      {/* &&를 쓰면 true일 때만 렌더링 */}
      {isLoggedIn && <button>로그아웃</button>}
    </div>
  );
}
```

---

## 리스트 렌더링

배열을 `map()`으로 돌려서 JSX를 반환하는 패턴이 React에서 가장 자주 쓰인다.

```jsx
const fruits = ["사과", "바나나", "체리"];

return (
  <ul>
    {fruits.map((fruit, index) => (
      <li key={index}>{fruit}</li>
    ))}
  </ul>
);
```

**`key` 속성은 필수다.** React가 목록에서 어떤 항목이 바뀌었는지 추적하는 데 사용한다. `index`보다는 고유 id를 쓰는 게 좋다.

---

## 라우팅: React Router

한 페이지에서 여러 화면을 관리하려면 라우터가 필요하다. `react-router-dom`을 설치해서 사용했다.

```jsx
// App.jsx
import { Routes, Route } from "react-router-dom";
import TeamPage from "./pages/TeamPage";
import TodoList from "./components/TodoList";

function App() {
  return (
    <Routes>
      <Route path="/" element={<TeamPage />} />
      <Route path="/todolist" element={<TodoList />} />
      <Route path="/*" element={<NotFoundPage />} />
    </Routes>
  );
}
```

URL이 `/`면 `TeamPage`, `/todolist`면 `TodoList`가 렌더링된다. URL이 바뀌어도 페이지 전체를 다시 로드하지 않는다. 이게 SPA(Single Page Application)의 핵심이다.

---

## 헷갈렸던 점

**`export default` vs `export`**: `export default`는 파일당 하나만 쓸 수 있고 import할 때 이름을 자유롭게 붙일 수 있다. `export`는 여러 개 쓸 수 있고 import할 때 중괄호`{}`로 정확한 이름을 써야 한다.

**JSX 안에서 주석**: HTML의 `<!-- 주석 -->`은 JSX에서 작동하지 않는다. JSX 안에서 주석은 `{/* 주석 */}` 형태를 써야 한다.

**컴포넌트 이름 대문자**: `<app />`처럼 소문자로 쓰면 HTML 태그로 인식한다. 커스텀 컴포넌트는 반드시 `<App />`처럼 대문자로 시작해야 한다.

---

## 복습용 질문

<details>
<summary>1. JSX에서 class 대신 무엇을 써야 하는가?</summary>

`className`을 써야 한다. `class`는 JavaScript의 예약어이기 때문에 JSX에서는 `className`으로 대체한다. 마찬가지로 `for`는 `htmlFor`로 쓴다.

</details>

<details>
<summary>2. 리스트를 map()으로 렌더링할 때 key 속성이 필요한 이유는?</summary>

React가 목록의 어떤 항목이 추가·삭제·수정됐는지 효율적으로 추적하기 위해서다. key가 없으면 React는 목록 전체를 다시 그려야 할 수 있다. key는 같은 목록 안에서 고유해야 한다.

</details>

<details>
<summary>3. React에서 조건부 렌더링에 if를 쓰지 않고 삼항연산자나 &&를 쓰는 이유는?</summary>

JSX의 `{}` 안에는 표현식(expression)만 올 수 있고, `if` 문처럼 문(statement)은 올 수 없다. 삼항연산자 `? :`와 `&&`는 표현식이므로 JSX 안에서 바로 쓸 수 있다.

</details>

---

## 한 줄 정리

JSX는 처음에 "HTML인가 JS인가" 헷갈리지만, 결국 **JS 안에서 UI를 선언하는 방법**이라고 이해하면 된다. 내일은 이 컴포넌트에 데이터를 전달하는 props와 컴포넌트 분리를 배운다.
