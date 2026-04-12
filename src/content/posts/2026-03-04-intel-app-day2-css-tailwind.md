---
title: "[App Dev] Day 2: CSS와 TailwindCSS 기초"
slug: intel-app-day2-css-tailwind
date: 2026-03-04
author: Evan Yoon
category: study
subcategory: bootcamp
description: "Intel AI 앱 개발 2일차. CSS 박스모델, flexbox, 그리고 TailwindCSS로 회원가입 폼을 스타일링했다."
thumbnail: ""
tags:
  - css
  - tailwindcss
  - flexbox
  - frontend
  - bootcamp
readTime: 9
series: "Intel App Dev 2026"
seriesOrder: 2
featured: false
draft: false
toc: true
---

## HTML 구조에 스타일 입히기

HTML로 구조를 잡으면 브라우저에 아무 스타일 없이 날것으로 나온다. 텍스트는 좌측 상단에 붙어 있고, 버튼은 작고 못생겼고, 여백은 하나도 없다. CSS가 하는 일이 바로 이걸 고치는 것이다.

오늘은 CSS 기본 문법, 박스 모델, 플렉스박스를 익히고, 마지막으로 **TailwindCSS**를 써서 어제 만든 회원가입 폼을 다시 만들었다. 클래스 기반 유틸리티 프레임워크가 실제로 어떻게 작동하는지 눈으로 확인할 수 있었던 날이었다.

---

## CSS 기본 문법

CSS는 **선택자(selector) + 속성(property) + 값(value)** 의 세트로 이루어진다.

```css
/* 태그 선택자 */
h1 {
  font-size: 24px;
  color: #333;
}

/* 클래스 선택자 */
.red {
  background-color: red;
}

/* id 선택자 */
#main-title {
  text-align: center;
}
```

HTML에서 클래스를 붙이려면 `class="red"`, id를 붙이려면 `id="main-title"`처럼 쓴다. 클래스는 여러 요소에 중복으로 쓸 수 있고, id는 페이지에서 하나만 써야 한다.

오늘 `styles.css`에서 연습한 텍스트 관련 속성들:

```css
.hello {
  text-decoration: underline;
  text-align: center;
  color: #fff;
  font-weight: 500;
}
```

---

## 박스 모델

CSS에서 모든 요소는 **박스**다. 그 박스는 네 겹으로 구성된다.

```
[ margin ]
  [ border ]
    [ padding ]
      [ content ]
```

- `content`: 텍스트나 이미지가 들어가는 실제 영역
- `padding`: content와 border 사이의 안쪽 여백
- `border`: 박스의 테두리
- `margin`: 다른 요소와의 바깥쪽 여백

오늘 `13_박스모델.html`에서 명함 카드를 만들면서 이 개념을 직접 써봤다.

```css
.card {
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  margin: 10px;
}

.card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  background-color: #f9f9f9;
  transition: all 0.2s ease;
}
```

`border-radius`로 모서리를 둥글게, `box-shadow`로 그림자를 줬다. `:hover`는 마우스를 올렸을 때 적용되는 가상 클래스다. `transition`을 추가하면 딱딱하게 바뀌지 않고 부드럽게 전환된다.

---

## Flexbox

`14_flexbox.html`에서 가장 오래 시간을 쓴 부분이 Flexbox다. 요소를 가로 또는 세로로 정렬하고, 비율을 나누는 데 자주 쓰인다.

```css
.container {
  display: flex;
  justify-content: center; /* 가로 방향 정렬 */
  align-items: center;     /* 세로 방향 정렬 */
  gap: 10px;               /* 아이템 간격 */
}

.item-large {
  flex: 3; /* 비율 3 */
}

.item-small {
  flex: 2; /* 비율 2 */
}
```

`flex: 3`과 `flex: 2`를 쓰면 부모 공간을 3:2 비율로 나눈다. 픽셀로 직접 지정하지 않아도 비율로 유연하게 배분할 수 있다는 게 핵심이다.

방향을 바꾸고 싶으면 `flex-direction: column`을 쓰면 세로로 쌓인다. `15_navigation.html`에서 반응형 햄버거 메뉴를 만들 때도 Flexbox가 기준이 됐다.

---

## TailwindCSS: 클래스로 스타일을 쓴다

Tailwind는 CSS를 직접 작성하지 않고, **미리 정의된 유틸리티 클래스**를 HTML에 직접 붙여서 스타일링하는 방식이다.

일반 CSS로 버튼을 만들면:

```css
/* styles.css */
.btn {
  background-color: blue;
  color: white;
  padding: 8px 16px;
  border-radius: 4px;
}
```

```html
<button class="btn">클릭</button>
```

Tailwind를 쓰면:

```html
<button class="bg-blue-500 text-white px-4 py-2 rounded">클릭</button>
```

별도의 CSS 파일을 안 만들어도 된다. 클래스 이름이 바로 스타일이다.

---

## 어제 폼을 Tailwind로 다시 만들기

`03_회원가입_tailwind.html`에서 어제 만든 회원가입 폼을 Tailwind로 다시 작성했다.

```html
<!-- 일반 CSS 버전 (어제) -->
<form>
  <input type="text" placeholder="이름" />
  <button type="submit">회원가입</button>
</form>

<!-- Tailwind 버전 (오늘) -->
<form class="max-w-md mx-auto p-6 bg-white rounded-lg shadow">
  <input
    type="text"
    placeholder="이름"
    class="w-full border border-gray-300 rounded px-3 py-2 mb-4 focus:outline-none focus:ring-2 focus:ring-blue-400"
  />
  <button
    type="submit"
    class="w-full bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 rounded transition"
  >
    회원가입
  </button>
</form>
```

클래스 이름이 길어지는 게 처음에는 어색했다. `max-w-md`가 최대 너비, `mx-auto`가 가운데 정렬, `p-6`이 padding, `rounded-lg`가 둥근 모서리라는 식으로 규칙이 있다. 외우기보다는 VS Code에서 자동완성을 보면서 감을 익히는 게 빠르다.

---

## Tailwind의 반응형 처리

Tailwind에서 반응형은 접두사로 처리한다.

```html
<!-- 기본은 1열, md 이상에서 2열, lg 이상에서 3열 -->
<div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
  ...
</div>
```

미디어 쿼리를 직접 작성하지 않아도 된다. `md:`, `lg:` 같은 접두사가 그 역할을 대신한다. 나중에 React 프로젝트의 `index.css`에서도 Tailwind 기반으로 컬러 팔레트와 폰트 시스템을 설정하는 걸 보게 됐는데, 그때 오늘 익힌 감이 도움이 됐다.

---

## 헷갈렸던 점

**`padding` vs `margin`**: 둘 다 여백인데 헷갈렸다. padding은 요소 안쪽, margin은 요소 바깥쪽이라고 기억하면 된다. 배경색을 주면 padding 영역까지 색이 채워지고, margin 영역은 채워지지 않는다.

**Tailwind 숫자 단위**: `p-4`는 `1rem`(16px), `p-2`는 `0.5rem`(8px)이다. 숫자 하나당 0.25rem(4px)씩 올라간다. 처음에 `p-10`이 몇 픽셀인지 감이 없어서 자꾸 적용해보며 확인했다.

**`flex`와 `grid`**: 오늘은 flex 위주로 연습했는데, grid는 2차원 레이아웃(행+열)을 동시에 다룰 때 더 편하다. 카드 목록처럼 격자 형태가 필요하면 grid가 낫다.

---

## 복습용 질문

<details>
<summary>1. CSS 박스 모델의 네 영역은 안쪽부터 순서대로 무엇인가?</summary>

content → padding → border → margin 순서다.
content는 실제 내용 영역, padding은 내용과 테두리 사이 안쪽 여백, border는 테두리 자체, margin은 다른 요소와의 바깥 여백이다.

</details>

<details>
<summary>2. Flexbox에서 justify-content와 align-items의 차이는?</summary>

`justify-content`는 주축(main axis) 방향의 정렬이다. `flex-direction`이 기본값(`row`)일 때 가로 정렬을 담당한다.
`align-items`는 교차축(cross axis) 방향의 정렬이다. 기본값 기준으로 세로 정렬을 담당한다.

</details>

<details>
<summary>3. Tailwind에서 hover 스타일을 적용하려면?</summary>

`hover:` 접두사를 붙이면 된다.

```html
<button class="bg-blue-500 hover:bg-blue-700 text-white px-4 py-2 rounded">
  버튼
</button>
```

마우스를 올리면 `bg-blue-700`이 적용된다.

</details>

---

## 한 줄 정리

CSS는 배우기 쉽지만 잘 쓰기는 어렵다. Tailwind는 그 어려움을 줄여주는 도구다. 내일은 이 뼈대와 모양에 **동작**을 입히는 JavaScript 차례다.
