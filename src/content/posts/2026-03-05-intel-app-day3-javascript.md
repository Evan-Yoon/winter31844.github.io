---
title: "[App Dev] Day 3: JavaScript, 웹에 동작을 심다"
slug: intel-app-day3-javascript
date: 2026-03-05
author: Evan Yoon
category: study
subcategory: bootcamp
description: "Intel AI 앱 개발 3일차. 변수, 함수, 배열 메서드, 스프레드 연산자, 비동기 처리까지 JavaScript 핵심을 하루에 집중했다."
thumbnail: ""
tags:
  - javascript
  - es6
  - async
  - frontend
  - bootcamp
readTime: 11
series: "Intel App Dev 2026"
seriesOrder: 3
featured: false
draft: false
toc: true
---

## JavaScript 시작

JavaScript는 버튼을 눌렀을 때 무언가가 바뀌고, 사용자 입력을 받아서 처리하고, 서버에서 데이터를 가져오는 역할을 한다. HTML과 CSS만으로는 이런 동작을 구현할 수 없다.

오늘은 `basic/js/` 폴더에 파일을 하나씩 만들면서 JavaScript 핵심 개념들을 순서대로 익혔다. Python을 먼저 배웠던 덕에 변수나 함수 개념 자체는 낯설지 않았지만, 문법이 다른 곳에서 계속 손이 멈췄다.

---

## 변수: let과 const

JavaScript에서 변수 선언은 `let`과 `const`를 주로 쓴다. 예전에는 `var`를 썼지만 스코프 문제 때문에 요즘은 잘 쓰지 않는다.

```javascript
// 변경 가능한 변수
let modifiable = "can be changed";
modifiable = "changed";

// 변경 불가한 상수
const immutable = "cannot be changed";
// immutable = "error"; → 오류 발생

// 블록 스코프
{
  let blockScoped = "confined to {}";
  console.log(blockScoped); // 정상
}
// console.log(blockScoped); → 오류 발생
```

`const`는 재할당이 안 될 뿐, 객체나 배열이면 내부 값은 바꿀 수 있다. 이 부분에서 처음에 헷갈렸다.

```javascript
const user = { name: "Evan" };
user.name = "Tom"; // 가능
user = {}; // 오류
```

---

## 템플릿 리터럴

문자열 안에 변수를 넣을 때 쓴다. 백틱(`` ` ``)으로 감싸고 `${}` 안에 변수나 표현식을 넣는다.

```javascript
const name = "이름";
const age = 25;

// 예전 방식
console.log("이름: " + name + ", 나이: " + age);

// 템플릿 리터럴
console.log(`이름:${name}, 나이:${age}`);
```

여러 줄 문자열도 그대로 쓸 수 있어서 훨씬 편하다.

---

## 함수: 일반 함수와 화살표 함수

JavaScript에서 함수를 만드는 방법은 여러 가지다.

```javascript
// 일반 함수
function greet(name = "고객님", age = 999) {
  return `안녕하세요, ${name}님! (${age}세)`;
}

// 화살표 함수 - 한 줄이면 return 생략 가능
const circleArea = (r) => 3.14 * r * r;
const square = (n) => n * n;

// 인자가 하나면 괄호도 생략 가능
const double = n => n * 2;
```

화살표 함수는 처음에 문법이 낯설었다. `=>` 이후에 중괄호 없이 바로 표현식을 쓰면 그게 리턴값이 된다. 중괄호를 쓰면 반드시 `return`을 명시해야 한다.

```javascript
const add1 = (a, b) => a + b;       // return 생략
const add2 = (a, b) => { return a + b; }; // return 명시
```

---

## 자료형 변환

Python과 달리 JavaScript는 자동으로 자료형을 변환하는 경우가 많아서 오히려 헷갈린다. 명시적으로 변환하는 습관이 중요하다.

```javascript
// 문자열로 변환
String(123)     // "123"
String(true)    // "true"

// 숫자로 변환
Number("42")    // 42
Number("abc")   // NaN
Number(true)    // 1
Number(false)   // 0

// 불리언으로 변환
Boolean(0)      // false
Boolean("")     // false
Boolean(null)   // false
Boolean("abc")  // true
Boolean(1)      // true
```

`Number("abc")`가 오류가 아니라 `NaN`을 반환한다는 점이 Python과 달랐다. `NaN`은 "Not a Number"인데, 타입은 `number`다. 이 부분이 처음에는 직관에 반했다.

---

## 단축 평가 (Short-circuit Evaluation)

`&&`와 `||` 연산자는 JavaScript에서 단순 논리 연산 이상의 역할을 한다.

```javascript
// && : 앞이 falsy면 앞을 반환, 앞이 truthy면 뒤를 반환
const user = null;
const name = user && user.name; // null (user가 falsy라 단락)

// || : 앞이 falsy면 뒤를 반환, 앞이 truthy면 앞을 반환
const input = "";
const display = input || "기본값"; // "기본값"

// 실용적인 활용
const isLoggedIn = true;
isLoggedIn && console.log("로그인됨"); // 실행됨

const value = null;
const safeValue = value || "fallback";
```

React에서 JSX 안에서 조건부 렌더링할 때 `&&`를 자주 쓰게 된다. 오늘 여기서 익혀두니까 나중에 자연스럽게 연결됐다.

---

## 배열 메서드: map과 filter

`07_map.js`에서 가장 실용적으로 느껴진 부분이다. 데이터 배열을 원하는 형태로 가공하는 데 쓴다.

```javascript
const users = [
  { id: 1, name: "철수", age: 25, isActive: true },
  { id: 2, name: "영희", age: 30, isActive: false },
  { id: 3, name: "민수", age: 22, isActive: true },
];

// map: 각 요소를 변환해서 새 배열 반환
const names = users.map((user) => user.name);
// ["철수", "영희", "민수"]

// HTML 태그 형태로 변환
const userTags = users.map((user) => `<p>${user.name}(${user.age})</p>`);

// filter: 조건에 맞는 요소만 추려서 새 배열 반환
const activeUsers = users.filter((u) => u.isActive === true);
// 철수, 민수만 남음

// 조합: 활성 유저의 이름만 추출
const activeNames = users
  .filter((u) => u.isActive)
  .map((u) => u.name);
```

`map`과 `filter`는 원본 배열을 바꾸지 않는다. 새 배열을 반환한다. 이 점이 처음에는 왜 중요한지 몰랐는데, React에서 상태를 불변으로 다뤄야 할 때 핵심이 되는 개념이라는 걸 나중에 알게 됐다.

---

## 스프레드 연산자 (...)

배열이나 객체를 펼쳐서 복사하거나 합칠 때 쓴다.

```javascript
// 배열 복사 및 합치기
const arr1 = [1, 2, 3];
const arr2 = [4, 5, 6];
const combined = [...arr1, ...arr2]; // [1, 2, 3, 4, 5, 6]

// 배열에 요소 추가
const newArr = [...arr1, 99]; // [1, 2, 3, 99]

// 객체 복사 및 수정
const original = { name: "Evan", age: 30 };
const updated = { ...original, age: 31 }; // age만 덮어씀
// { name: "Evan", age: 31 }
```

React에서 배열 상태에 새 항목을 추가할 때 `setItems([...items, newItem])`처럼 쓰는 패턴이 여기서 온다. 오늘 이걸 먼저 익혀두지 않았으면 나중에 코드를 보고 무슨 뜻인지 몰랐을 것이다.

---

## 비동기 처리: async/await

`10_비동기.js`에서 다룬 내용이다. 서버에서 데이터를 받아오는 건 시간이 걸린다. 그 시간 동안 브라우저를 멈추지 않으려면 비동기 처리가 필요하다.

```javascript
async function getUserPosts() {
  try {
    const response = await fetch("https://jsonplaceholder.typicode.com/posts");
    const posts = await response.json();
    console.log(posts);
  } catch (error) {
    console.log("에러 발생:", error.message);
  }
}

getUserPosts();
```

`async` 함수 안에서 `await`를 쓰면 그 줄이 완료될 때까지 기다린다. `fetch()`는 브라우저 내장 함수로 HTTP 요청을 보낸다. 응답은 `response.json()`으로 파싱해야 실제 데이터를 쓸 수 있다.

`Promise.all`은 여러 요청을 동시에 보내고 전부 완료되면 결과를 받는다.

```javascript
async function fetchAll() {
  const [posts, users] = await Promise.all([
    fetch("https://jsonplaceholder.typicode.com/posts").then(r => r.json()),
    fetch("https://jsonplaceholder.typicode.com/users").then(r => r.json()),
  ]);
  console.log(posts, users);
}
```

---

## 헷갈렸던 점

**`==` vs `===`**: JavaScript는 `==`로 비교하면 자료형을 자동으로 맞춰서 비교한다. `1 == "1"`이 `true`가 된다. `===`는 값과 자료형 모두 같아야 `true`다. **항상 `===`를 쓴다.**

**`undefined` vs `null`**: `undefined`는 값이 할당되지 않은 것, `null`은 의도적으로 없음을 나타낸 것이다. 둘 다 falsy지만 엄연히 다르다.

**`NaN !== NaN`**: `NaN`은 자기 자신과 같지 않다. `isNaN()` 함수로 확인해야 한다.

---

## 복습용 질문

<details>
<summary>1. const로 선언한 객체의 속성을 바꿀 수 있는가?</summary>

가능하다. `const`는 재할당만 막는다. 객체 내부 속성은 바꿀 수 있다.

```javascript
const user = { name: "Evan" };
user.name = "Tom"; // OK
user = {};         // TypeError
```

</details>

<details>
<summary>2. map과 filter의 차이는?</summary>

`map`은 배열의 모든 요소를 변환해서 같은 길이의 새 배열을 반환한다.
`filter`는 조건에 맞는 요소만 걸러내서 길이가 같거나 짧은 새 배열을 반환한다.
둘 다 원본 배열을 바꾸지 않는다.

</details>

<details>
<summary>3. async/await에서 try/catch를 쓰는 이유는?</summary>

`await`로 기다리는 작업이 실패할 수 있다. 네트워크 오류, 서버 오류 등. 이때 에러를 잡지 않으면 프로그램이 예기치 않게 멈춘다. `try/catch`로 에러를 잡아서 적절히 처리한다.

</details>

---

## 한 줄 정리

JavaScript 하루 치를 다 쓰고 나니, 내일부터 할 React가 왜 이런 문법들을 이렇게 많이 쓰는지 이해가 됐다. 내일은 React의 JSX와 프로젝트 세팅이다.
