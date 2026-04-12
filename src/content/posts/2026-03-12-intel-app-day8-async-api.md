---
title: "[App Dev] Day 8: 비동기 통신과 API 연동, 실제 데이터와 연결하다"
slug: intel-app-day8-async-api
date: 2026-03-12
author: Evan Yoon
category: study
subcategory: bootcamp
description: "Intel AI 앱 개발 8일차. fetch와 async/await로 외부 API 데이터를 React 컴포넌트에 연결하고, 로딩·에러 상태까지 처리했다."
thumbnail: ""
tags:
  - react
  - api
  - async
  - fetch
  - useEffect
  - bootcamp
readTime: 10
series: "Intel App Dev 2026"
seriesOrder: 8
featured: false
draft: false
toc: true
---

## 외부 API에서 데이터 받아오기

지금까지 만든 컴포넌트들은 모두 코드 안에 데이터를 직접 작성했다. 실제 서비스는 그렇게 작동하지 않는다. 서버에서 데이터를 받아와야 하고, 그 데이터는 비동기로 도착한다.

오늘은 `JSONPlaceholder`라는 무료 목업 API와 고양이 이미지 API를 사용해서, 실제 데이터를 받아 React 컴포넌트에 렌더링하는 패턴을 익혔다. 단순히 데이터를 화면에 보여주는 것뿐만 아니라 **로딩 중**, **에러 발생**, **데이터 없음** 같은 상태도 함께 처리했다.

---

## useEffect + fetch 패턴

외부 데이터를 가져오는 기본 패턴은 `useEffect` 안에서 `fetch`를 쓰는 것이다.

```jsx
import { useState, useEffect } from "react";

function Posts() {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function getPosts() {
      setLoading(true);
      try {
        const response = await fetch("https://jsonplaceholder.typicode.com/posts");
        if (!response.ok) throw new Error("서버 응답 오류");
        const data = await response.json();
        setPosts(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }
    getPosts();
  }, []);  // 마운트 시 한 번만 실행

  if (loading) return <p>불러오는 중...</p>;
  if (error) return <p className="text-red-500">에러: {error}</p>;
  if (posts.length === 0) return <p>게시글이 없습니다.</p>;

  return (
    <ul>
      {posts.slice(0, 10).map((post) => (
        <li key={post.id} className="border-b py-3">
          <h3 className="font-semibold">{post.title}</h3>
          <p className="text-sm text-gray-500">{post.body}</p>
        </li>
      ))}
    </ul>
  );
}
```

상태 세 가지를 함께 관리한다. `loading`은 로딩 스피너 표시용, `error`는 에러 메시지 표시용, `posts`는 실제 데이터다. `finally`로 성공이든 실패든 `loading`을 false로 돌려놓는다.

---

## JSONPlaceholder API

`https://jsonplaceholder.typicode.com`은 프론트엔드 연습용 무료 목업 API다. 회원 가입 없이 실제 HTTP 요청을 테스트할 수 있다.

```
GET /posts        → 게시글 100개
GET /posts/1      → 특정 게시글
GET /users        → 유저 10명
GET /todos        → 할 일 200개
GET /comments     → 댓글 500개
```

오늘은 `/posts` 엔드포인트로 게시글 목록을 불러오고, `/users`와 `/posts`를 동시에 요청하는 실습도 했다.

---

## Promise.all로 여러 요청 동시에

두 개 이상의 API를 동시에 요청하면 순차 요청보다 빠르다.

```jsx
useEffect(() => {
  async function fetchAll() {
    setLoading(true);
    try {
      const [postsRes, usersRes] = await Promise.all([
        fetch("https://jsonplaceholder.typicode.com/posts"),
        fetch("https://jsonplaceholder.typicode.com/users"),
      ]);
      const posts = await postsRes.json();
      const users = await usersRes.json();
      setPosts(posts);
      setUsers(users);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }
  fetchAll();
}, []);
```

`await`로 순차 요청하면 첫 번째 요청 완료 후 두 번째가 시작된다. `Promise.all`은 두 요청을 동시에 보내고 둘 다 완료되면 결과를 반환한다. 요청 개수가 많을수록 차이가 커진다.

---

## Cat: 이미지 API 연동

`Cat.jsx`는 고양이 이미지 API에서 랜덤 이미지를 받아 그리드로 보여주는 컴포넌트다.

```jsx
const CAT_API = "https://api.thecatapi.com/v1/images/search?limit=12";

function Cat() {
  const [cats, setCats] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchCats = async () => {
    setLoading(true);
    try {
      const res = await fetch(CAT_API);
      const data = await res.json();
      setCats(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCats();
  }, []);

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-xl font-bold">고양이 이미지</h2>
        <button
          onClick={fetchCats}
          disabled={loading}
          className="px-4 py-2 bg-blue-500 text-white rounded disabled:opacity-50"
        >
          {loading ? "불러오는 중..." : "새로 불러오기"}
        </button>
      </div>

      <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-3">
        {cats.map((cat) => (
          <div key={cat.id} className="aspect-square overflow-hidden rounded-lg">
            <img
              src={cat.url}
              alt="cat"
              className="w-full h-full object-cover hover:scale-105 transition-transform"
            />
          </div>
        ))}
      </div>
    </div>
  );
}
```

버튼을 누르면 `fetchCats`를 다시 호출해서 새로운 이미지를 불러온다. `loading` 상태일 때 버튼을 `disabled`로 만들어서 중복 요청을 막았다. `object-cover`로 이미지 비율에 상관없이 정사각형 셀을 채운다.

---

## API 요청의 생명주기

API 요청 하나가 처리되는 흐름을 정리하면:

```
1. 컴포넌트 마운트 (useEffect 실행)
2. loading = true → 로딩 UI 표시
3. fetch() 요청 전송
4. 응답 대기 (브라우저는 다른 작업 계속)
5. 응답 도착 → response.json() 파싱
6. setPosts(data) → 상태 업데이트 → 재렌더링
7. loading = false → 로딩 UI 제거, 실제 데이터 표시
```

에러가 발생하면:

```
3. fetch() 요청 전송
4. 네트워크 오류 또는 서버 오류
5. catch 블록 실행 → setError(err.message)
6. 에러 UI 표시
7. loading = false (finally에서)
```

---

## response.ok 체크

`fetch`는 404나 500 에러도 `catch`로 떨어지지 않는다. HTTP 에러는 정상 응답으로 간주된다. 이 때문에 `response.ok`를 체크해야 한다.

```jsx
const response = await fetch(url);

if (!response.ok) {
  throw new Error(`HTTP error: ${response.status}`);
}

const data = await response.json();
```

`response.ok`는 상태 코드가 200~299 사이일 때 true다. 이 체크를 빠뜨리면 404가 와도 에러 없이 통과하고, 빈 데이터나 에러 응답을 그냥 쓰게 된다.

---

## 헷갈렸던 점

**`useEffect` 안에서 async 함수를 직접 쓸 수 없는 이유**: `useEffect`의 콜백은 async 함수가 되면 안 된다. async 함수는 Promise를 반환하는데, `useEffect`는 cleanup 함수(또는 아무것도 반환하지 않음)만 받는다. 그래서 내부에 async 함수를 정의하고 바로 호출하는 패턴을 쓴다.

```jsx
// 잘못된 방식
useEffect(async () => { ... }, []);

// 올바른 방식
useEffect(() => {
  async function load() { ... }
  load();
}, []);
```

**fetch 에러 vs HTTP 에러**: 네트워크 자체가 연결되지 않으면 `catch`로 떨어진다. 하지만 서버가 404나 500을 반환하면 `fetch`는 에러로 처리하지 않는다. `response.ok`로 직접 체크해야 한다.

---

## 복습용 질문

<details>
<summary>1. useEffect 안에서 async를 직접 쓰지 않고 내부 함수로 감싸는 이유는?</summary>

useEffect의 콜백은 cleanup 함수나 undefined만 반환해야 한다. async 함수는 항상 Promise를 반환하기 때문에 직접 쓰면 안 된다. 내부에 별도의 async 함수를 정의하고 바로 호출하는 패턴을 쓴다.

</details>

<details>
<summary>2. Promise.all을 쓰면 순차 요청보다 빠른 이유는?</summary>

Promise.all은 전달된 Promise를 동시에 시작한다. 두 요청이 병렬로 진행되므로 가장 오래 걸리는 것만큼 기다리면 된다. 순차 요청은 첫 번째 완료 후 두 번째가 시작되므로 총 시간이 더 길다.

</details>

<details>
<summary>3. fetch에서 response.ok를 체크해야 하는 이유는?</summary>

fetch는 네트워크 오류(연결 실패 등)만 reject한다. HTTP 에러 응답(404, 500 등)은 정상 응답으로 처리한다. response.ok(상태 코드 200~299)를 확인하지 않으면 에러 응답인데도 데이터 처리를 계속하게 된다.

</details>

---

## 한 줄 정리

API 연동은 단순히 데이터를 가져오는 것이 아니라, **요청 중·완료·실패** 세 가지 상태를 모두 사용자에게 보여주는 것까지 포함한다. 내일은 오늘 프론트가 연결된 FastAPI 서버 구축을 직접 해본다.
