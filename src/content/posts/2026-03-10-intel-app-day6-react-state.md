---
title: "[App Dev] Day 6: React 상태관리, useState로 화면 업데이트하기"
slug: intel-app-day6-react-state
date: 2026-03-10
author: Evan Yoon
category: study
subcategory: bootcamp
description: "Intel AI 앱 개발 6일차. useState와 useEffect를 Counter, Like 버튼, 카페 주문 앱으로 직접 만들며 React 상태 관리를 익혔다."
thumbnail: ""
tags:
  - react
  - useState
  - useEffect
  - state-management
  - bootcamp
readTime: 10
series: "Intel App Dev 2026"
seriesOrder: 6
featured: false
draft: false
toc: true
---

## 화면이 바뀌려면 상태가 필요하다

지금까지 만든 컴포넌트들은 한 번 렌더링되고 나면 바뀌지 않는다. 버튼을 눌러도, 입력을 해도 화면이 그대로다. 실제 앱이 되려면 **사용자의 행동에 반응해서 화면이 바뀌어야** 한다. 그게 상태(State)다.

오늘은 `useState`와 `useEffect`를 중심으로, Counter, Like 버튼, 카페 주문 앱을 만들면서 상태 관리의 기본 패턴을 익혔다.

---

## useState 기본

`useState`는 React의 내장 Hook이다. 컴포넌트 안에서 변하는 데이터를 선언할 때 쓴다.

```jsx
import { useState } from "react";

function Counter() {
  const [count, setCount] = useState(0);
  // count: 현재 상태값
  // setCount: 상태를 바꾸는 함수
  // 0: 초기값

  return (
    <div>
      <p>{count}</p>
      <button onClick={() => setCount(count + 1)}>+1</button>
      <button onClick={() => setCount(count - 1)}>-1</button>
    </div>
  );
}
```

`setCount(count + 1)`을 호출하면 두 가지가 동시에 일어난다. count 값이 바뀌고, 컴포넌트가 다시 렌더링된다. 직접 `count = count + 1`처럼 수정하면 화면이 바뀌지 않는다. 반드시 `setCount`를 써야 한다.

---

## Counter: 숫자 색상도 상태에 반응

`Counter.jsx`를 만들면서 상태값에 따라 스타일도 바뀌도록 했다.

```jsx
function Counter() {
  const [counterCount, setCounterCount] = useState(0);

  const handlePlus = () => setCounterCount(counterCount + 1);
  const handleMinus = () => setCounterCount(counterCount - 1);

  return (
    <div className="flex flex-col items-center gap-4 p-8">
      <h1
        className={
          counterCount > 0
            ? "text-blue-500 text-6xl font-bold"
            : counterCount < 0
            ? "text-red-500 text-6xl font-bold"
            : "text-black text-6xl font-bold"
        }
      >
        {counterCount}
      </h1>
      <div className="flex gap-4">
        <button onClick={handlePlus} className="px-6 py-2 bg-blue-500 text-white rounded">
          +
        </button>
        <button onClick={handleMinus} className="px-6 py-2 bg-red-500 text-white rounded">
          -
        </button>
      </div>
    </div>
  );
}
```

숫자가 양수면 파란색, 음수면 빨간색, 0이면 검정. 상태값 하나로 텍스트와 스타일을 동시에 제어한다. className 안에서 삼항연산자를 쓰는 패턴을 여기서 처음으로 자연스럽게 활용했다.

---

## Like 버튼: 토글 상태

`Like.jsx`는 좋아요 버튼의 on/off 토글 상태를 관리한다.

```jsx
import { useState } from "react";
import { Heart } from "lucide-react";

function Like() {
  const [liked, setLiked] = useState(false);
  const [count, setCount] = useState(0);

  const handleLike = () => {
    if (liked) {
      setLiked(false);
      setCount(count - 1);
    } else {
      setLiked(true);
      setCount(count + 1);
    }
  };

  return (
    <button
      onClick={handleLike}
      className={`flex items-center gap-2 px-4 py-2 rounded-full border transition ${
        liked ? "border-red-400 text-red-500" : "border-gray-300 text-gray-500"
      }`}
    >
      <Heart
        size={20}
        className={liked ? "fill-red-500 text-red-500" : "text-gray-400"}
      />
      <span>{count}</span>
    </button>
  );
}
```

상태가 두 개(`liked`, `count`)다. 버튼을 누를 때 둘 다 함께 업데이트한다. `lucide-react`의 `Heart` 아이콘을 쓰면서, 아이콘 라이브러리를 React에서 어떻게 import해서 쓰는지도 함께 익혔다.

---

## CafeMenu: 배열 상태 관리

`CafeMenu.jsx`는 카페 주문 앱이다. 선택한 메뉴를 배열로 관리하고, `reduce()`로 합계를 계산한다.

```jsx
const MENU = [
  { id: 1, name: "아메리카노", price: 3500 },
  { id: 2, name: "카페라떼", price: 4500 },
  { id: 3, name: "에스프레소", price: 3000 },
  { id: 4, name: "카푸치노", price: 4000 },
];

function CafeMenu() {
  const [selectedItems, setSelectedItems] = useState([]);

  const handleAdd = (item) => {
    setSelectedItems([...selectedItems, item]);
  };

  const handleRemove = (index) => {
    setSelectedItems(selectedItems.filter((_, i) => i !== index));
  };

  const total = selectedItems.reduce((acc, item) => acc + item.price, 0);

  return (
    <div className="p-6">
      {/* 메뉴 목록 */}
      <div className="grid grid-cols-2 gap-3">
        {MENU.map((item) => (
          <button
            key={item.id}
            onClick={() => handleAdd(item)}
            className="p-4 border rounded-lg hover:bg-gray-50 text-left"
          >
            <p className="font-semibold">{item.name}</p>
            <p className="text-gray-500 text-sm">{item.price.toLocaleString()}원</p>
          </button>
        ))}
      </div>

      {/* 주문 목록 */}
      <div className="mt-6">
        {selectedItems.map((item, i) => (
          <div key={i} className="flex justify-between py-2 border-b">
            <span>{item.name}</span>
            <div className="flex items-center gap-3">
              <span>{item.price.toLocaleString()}원</span>
              <button onClick={() => handleRemove(i)} className="text-red-400">
                삭제
              </button>
            </div>
          </div>
        ))}
      </div>

      {/* 합계 */}
      <div className="flex justify-between font-bold text-lg mt-4">
        <span>합계</span>
        <span>{total.toLocaleString()}원</span>
      </div>
    </div>
  );
}
```

배열 상태를 관리하는 핵심 패턴 두 가지를 여기서 익혔다.
- **추가**: `[...selectedItems, newItem]` (스프레드로 새 배열 생성)
- **삭제**: `selectedItems.filter(...)` (filter로 해당 항목 제외)

직접 `selectedItems.push(item)`을 쓰면 안 된다. 상태를 직접 변경하면 React가 변경을 감지하지 못해서 화면이 다시 렌더링되지 않는다.

---

## useEffect 기본

`useEffect`는 컴포넌트가 렌더링된 후에 부수 효과(side effect)를 실행하는 Hook이다.

```jsx
import { useState, useEffect } from "react";

function Timer() {
  const [seconds, setSeconds] = useState(0);

  useEffect(() => {
    // 컴포넌트가 마운트된 후 실행
    const interval = setInterval(() => {
      setSeconds((prev) => prev + 1);
    }, 1000);

    // 컴포넌트가 언마운트될 때 정리
    return () => clearInterval(interval);
  }, []); // 빈 배열: 처음 한 번만 실행

  return <p>경과 시간: {seconds}초</p>;
}
```

`useEffect`의 두 번째 인자는 의존성 배열이다.
- `[]`: 처음 한 번만 실행 (마운트 시)
- `[value]`: value가 바뀔 때마다 실행
- 없으면: 매 렌더링마다 실행 (보통 원하는 게 아님)

---

## 함수형 업데이트

상태를 이전 값 기반으로 바꿀 때는 콜백 형태를 쓰는 게 더 안전하다.

```jsx
// 직접 참조 (연속 호출 시 문제 생길 수 있음)
setCount(count + 1);

// 함수형 업데이트 (이전 값을 기반으로)
setCount((prev) => prev + 1);
```

`setInterval`처럼 비동기 상황에서는 특히 함수형 업데이트를 써야 최신 상태를 정확히 가져온다.

---

## 헷갈렸던 점

**상태 업데이트는 비동기다**: `setCount(count + 1)` 다음 줄에서 `console.log(count)`를 출력하면 아직 이전 값이 나온다. 상태 업데이트는 즉시 반영되지 않고 다음 렌더링 때 반영된다.

**배열/객체 상태는 새 참조를 만들어야 한다**: `state.push(item)`이나 `state.field = value`처럼 직접 변경하면 React가 변경을 감지하지 못한다. 항상 새 배열/객체를 만들어서 setState에 넘겨야 한다.

---

## 복습용 질문

<details>
<summary>1. useState로 선언한 상태를 직접 변경하면 안 되는 이유는?</summary>

React는 상태값이 바뀌면 컴포넌트를 다시 렌더링한다. 직접 변경하면 React가 변경을 알 수 없어서 화면이 업데이트되지 않는다. 반드시 setState 함수를 통해 업데이트해야 React가 재렌더링을 트리거한다.

</details>

<details>
<summary>2. useEffect의 의존성 배열 []과 [value]의 차이는?</summary>

`[]`는 컴포넌트가 처음 마운트될 때 한 번만 실행된다. `[value]`는 컴포넌트 마운트 시 + value가 바뀔 때마다 실행된다. 의존성 배열을 아예 생략하면 매 렌더링마다 실행된다.

</details>

<details>
<summary>3. 배열 상태에서 항목을 추가하는 올바른 방법은?</summary>

스프레드 연산자로 새 배열을 만들어서 setState에 넘긴다.

```jsx
setItems([...items, newItem]);
// items.push(newItem) 이후 setItems(items)는 잘못된 방식
```

</details>

---

## 한 줄 정리

상태(state)가 바뀌어야 화면이 바뀐다. 직접 바꾸지 않고, 새 값을 만들어서 set 함수에 넘기는 패턴이 React의 기본 원칙이다. 내일은 입력 폼을 상태로 제어하는 제어 컴포넌트와 TodoList를 완성한다.
