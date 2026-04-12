---
title: "[App Dev] Day 1: HTML, 다시 처음부터"
slug: intel-app-day1-html-basics
date: 2026-03-03
author: Evan Yoon
category: study
subcategory: bootcamp
description: "Intel AI 과정 앱 개발 모듈 1일차. HTML 구조, 태그, 폼, 링크, 이미지를 직접 만들며 웹의 뼈대를 다시 익혔다."
thumbnail: ""
tags:
  - html
  - web
  - frontend
  - bootcamp
  - study
readTime: 8
series: "Intel App Dev 2026"
seriesOrder: 1
featured: false
draft: false
toc: true
---

## 오늘부터 앱 개발 모듈이 시작됐다

3월 3일, Intel AI 과정에서 새로운 챕터가 열렸다. Python, Java, 머신러닝을 지나서 이번에는 **웹과 앱 개발**이다. 기간은 약 3주. 앞으로 HTML, CSS, JavaScript, React를 거쳐 React Native까지 올라가는 일정이다.

첫날 주제는 HTML이었다. "이미 알잖아"라고 생각할 수도 있었다. 어디선가 한 번쯤 본 태그들이고, 구조도 단순하다. 그런데 막상 아무것도 없는 파일에서 하나씩 직접 치다 보니, 생각보다 손에 익지 않은 부분이 남아 있었다.

오늘은 욕심내지 않고 HTML이 무엇인지, 어떤 태그가 무슨 역할을 하는지부터 다시 손으로 확인하는 시간이었다.

---

## HTML이 하는 일

HTML은 웹 페이지의 **뼈대**다. 텍스트가 어디 있고, 이미지가 어디 있고, 버튼이 어디 있는지를 브라우저에게 알려주는 언어다. 디자인은 CSS가 담당하고, 동작은 JavaScript가 담당한다. HTML은 오직 "무엇이 있는가"만 정의한다.

모든 HTML 파일은 이 기본 틀에서 시작한다.

```html
<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <title>페이지 제목</title>
  </head>
  <body>
    <!-- 화면에 보이는 내용은 모두 여기 -->
  </body>
</html>
```

`<!DOCTYPE html>`은 이 파일이 HTML5 문서라는 선언이다. 빼먹으면 브라우저가 호환성 모드로 동작해서 레이아웃이 이상하게 잡히는 경우가 생긴다. 오늘 처음에 선언 없이 작성했다가 한 번 확인했다.

---

## 기본 텍스트 태그들

`h1`부터 `h6`까지는 제목 태그다. 숫자가 클수록 작아진다. `p`는 문단이다. 이 둘만 알아도 글 구조를 잡을 수 있다.

```html
<h1>가장 큰 제목</h1>
<h2>두 번째 제목</h2>
<p>이곳이 본문 문단입니다. 줄바꿈은 자동으로 되지 않습니다.</p>
```

강조할 때는 `<strong>`과 `<em>`을 쓴다. `<strong>`은 굵게, `<em>`은 기울임이다. 단순히 굵게 보이려고 `<b>`를 쓰는 경우가 많은데, `<strong>`이 의미론적으로 더 정확하다. 시각장애인을 위한 스크린리더도 `<strong>`을 다르게 읽는다.

---

## 이미지와 링크

이미지는 `<img>`, 링크는 `<a>` 태그를 쓴다.

```html
<!-- 이미지 -->
<img src="./img/photo.jpg" alt="사진 설명" width="300" />

<!-- 링크 -->
<a href="https://google.com" target="_blank">구글로 이동</a>

<!-- 페이지 내 이동 -->
<a href="#section2">본문으로 이동</a>
```

`alt` 속성이 오늘 처음 신경 쓴 부분이었다. 이미지가 로드 실패했을 때 대신 표시되는 텍스트이기도 하고, 스크린리더가 읽어주는 텍스트이기도 하다. 귀찮다고 비워두면 안 된다.

`target="_blank"`는 새 탭에서 열기다. 외부 링크에는 보통 이게 붙는다.

---

## 목록 태그

오늘 레시피 페이지(`02_레시피.html`)를 만들면서 목록 태그를 많이 썼다.

```html
<!-- 순서 없는 목록 -->
<ul>
  <li>재료 1</li>
  <li>재료 2</li>
</ul>

<!-- 순서 있는 목록 -->
<ol>
  <li>첫 번째 단계</li>
  <li>두 번째 단계</li>
</ol>
```

레시피 구조에서 재료 목록은 `<ul>`, 조리 순서는 `<ol>`이 자연스럽다. 단순해 보이지만 직접 적용해보면 어떤 상황에 어느 태그를 써야 하는지 감이 잡힌다.

---

## 폼(Form) 태그

오늘 가장 오래 시간을 쓴 부분이 폼이었다. `03_회원가입.html`을 만들면서 여러 입력 요소를 처음으로 한 번에 써봤다.

```html
<form>
  <!-- 텍스트 입력 -->
  <input type="text" placeholder="이름을 입력하세요" />

  <!-- 비밀번호 -->
  <input type="password" placeholder="비밀번호" />

  <!-- 이메일 -->
  <input type="email" placeholder="이메일" />

  <!-- 라디오 버튼 -->
  <input type="radio" name="gender" value="male" /> 남성
  <input type="radio" name="gender" value="female" /> 여성

  <!-- 체크박스 -->
  <input type="checkbox" /> 약관에 동의합니다

  <!-- 드롭다운 선택 -->
  <select>
    <option value="seoul">서울</option>
    <option value="busan">부산</option>
  </select>

  <!-- 버튼 -->
  <button type="submit">회원가입</button>
</form>
```

라디오 버튼에서 `name` 속성이 같아야 같은 그룹으로 묶인다는 걸 처음 직접 확인했다. `name`을 다르게 쓰면 두 개를 동시에 선택할 수 있어서 라디오가 아니라 체크박스처럼 동작해버린다.

---

## 시맨틱 태그

HTML5부터 추가된 시맨틱 태그들은 코드를 읽는 사람에게, 그리고 검색엔진에게 페이지 구조를 더 명확하게 전달한다.

```html
<header>헤더 영역</header>
<nav>네비게이션</nav>
<main>
  <section>섹션 구분</section>
  <article>독립된 콘텐츠</article>
  <aside>사이드 내용</aside>
</main>
<footer>푸터 영역</footer>
```

`<div>`로만 구성해도 화면에는 똑같이 보인다. 하지만 코드만 봤을 때 어떤 영역인지 알 수가 없고, 스크린리더가 구조를 파악하기 어렵다. 오늘 `index.html`에서 네비게이션을 `<nav>` 안에 작성했을 때 의도가 훨씬 분명해지는 걸 느꼈다.

---

## 오늘 만든 것들

오늘 하루 동안 만든 파일은 네 개였다.

- `01_basic.html` — 기본 태그 연습: 제목, 문단, 이미지, 링크, 목록
- `02_레시피.html` — 레시피 페이지: ul, ol 태그 중심
- `03_회원가입.html` — 회원가입 폼: 다양한 input 타입 포함
- `index.html` — 위 파일들을 한데 모은 네비게이션 허브

---

## 헷갈렸던 점

**self-closing 태그**: `<img />`나 `<input />`처럼 닫는 태그가 없는 경우와, `<p></p>`처럼 닫아야 하는 경우를 처음에 헷갈렸다. 내용을 포함하지 않는 태그들은 self-closing이다.

**라디오 버튼 name 속성**: 앞에서 언급했지만, `name`이 같아야 같은 그룹이 된다. 직접 실수해보기 전에는 크게 와닿지 않았다.

**`<b>` vs `<strong>`**: 결과는 똑같이 굵게 보이지만 의미가 다르다. 단순히 굵게 보여주려면 `<b>`, 중요성을 강조하려면 `<strong>`.

---

## 복습용 질문

<details>
<summary>1. &lt;ul&gt;과 &lt;ol&gt;의 차이는 무엇인가?</summary>

`<ul>`은 순서가 없는 목록(unordered list), `<ol>`은 순서가 있는 목록(ordered list)이다.
재료 목록처럼 순서가 중요하지 않으면 `<ul>`, 조리 단계처럼 순서가 의미 있으면 `<ol>`이 맞다.

</details>

<details>
<summary>2. 라디오 버튼 여러 개를 같은 그룹으로 묶으려면?</summary>

`name` 속성 값을 동일하게 설정하면 같은 그룹이 된다. 같은 그룹 안에서는 하나만 선택 가능해진다.

```html
<input type="radio" name="gender" value="male" /> 남성
<input type="radio" name="gender" value="female" /> 여성
```

</details>

<details>
<summary>3. &lt;img&gt; 태그에서 alt 속성이 왜 필요한가?</summary>

두 가지 역할을 한다.
- 이미지 로딩에 실패했을 때 대신 표시할 텍스트
- 스크린리더가 시각장애인에게 읽어주는 설명 텍스트

비워두면 접근성 측면에서 문제가 생긴다.

</details>

---

## 한 줄 정리

HTML은 구조가 단순하다. 태그 이름과 속성 의미를 정확히 알고 쓰는 게 중요하다. 내일은 CSS로 오늘 만든 구조에 스타일을 입힌다.
