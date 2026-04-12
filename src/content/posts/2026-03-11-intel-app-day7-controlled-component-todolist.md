---
title: "[App Dev] Day 7: 제어 컴포넌트와 TodoList"
slug: intel-app-day7-controlled-component-todolist
date: 2026-03-11
author: Evan Yoon
category: study
subcategory: bootcamp
description: "Intel AI 앱 개발 7일차. 제어 컴포넌트로 폼 입력을 관리하고, localStorage까지 연동한 TodoList를 완성했다."
thumbnail: ""
tags:
  - react
  - controlled-component
  - todolist
  - form
  - localstorage
  - bootcamp
readTime: 11
series: "Intel App Dev 2026"
seriesOrder: 7
featured: false
draft: false
toc: true
---

## 입력값을 React가 직접 관리한다

HTML의 `<input>`은 기본적으로 브라우저가 입력값을 관리한다. 사용자가 타이핑하면 DOM이 알아서 바뀐다. React에서는 이 방식 대신 **상태(state)로 입력값을 직접 관리**하는 패턴을 쓴다. 이것이 제어 컴포넌트(Controlled Component)다.

오늘은 로그인 폼 예제로 제어 컴포넌트를 익히고, LocalStorage와 연동한 TodoList를 완성했다. 두 가지 모두 실제 서비스에서 가장 자주 만드는 패턴이다.

---

## 제어 컴포넌트 기본

```jsx
function ControlledInput() {
  const [value, setValue] = useState("");

  return (
    <input
      type="text"
      value={value}            // 상태가 input의 값을 결정
      onChange={(e) => setValue(e.target.value)}  // 입력할 때마다 상태 업데이트
    />
  );
}
```

`value`와 `onChange`를 함께 써야 제어 컴포넌트가 된다. `value`만 쓰면 읽기 전용이 되고, `onChange`만 쓰면 비제어 컴포넌트가 된다.

이렇게 하면 어떤 장점이 있는가.

- 현재 입력값을 언제든지 상태에서 읽을 수 있다
- 입력 전처리(소문자 변환, 길이 제한 등)를 쉽게 추가할 수 있다
- 유효성 검사를 실시간으로 할 수 있다

---

## ExampleInput: 로그인 폼

`ExampleInput.jsx`에서 여러 입력 필드를 하나의 상태 객체로 관리하는 패턴을 연습했다.

```jsx
function ExampleInput() {
  const [form, setForm] = useState({
    userId: "",
    password: "",
  });
  const [showPassword, setShowPassword] = useState(false);
  const [errors, setErrors] = useState({});

  // 공통 핸들러: name 속성으로 어떤 필드인지 구분
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  // 비밀번호 유효성 검사 (영문+숫자+특수문자 8자 이상)
  const validatePassword = (pw) => {
    const regex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/;
    return regex.test(pw);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const newErrors = {};
    if (!form.userId) newErrors.userId = "아이디를 입력하세요";
    if (!validatePassword(form.password)) {
      newErrors.password = "영문, 숫자, 특수문자 포함 8자 이상";
    }
    setErrors(newErrors);
    if (Object.keys(newErrors).length === 0) {
      alert("로그인 성공!");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="max-w-sm mx-auto p-6">
      <div className="mb-4">
        <input
          type="text"
          name="userId"
          value={form.userId}
          onChange={handleChange}
          placeholder="아이디"
          className="w-full border rounded px-3 py-2"
        />
        {errors.userId && (
          <p className="text-red-500 text-sm mt-1">{errors.userId}</p>
        )}
      </div>

      <div className="mb-4 relative">
        <input
          type={showPassword ? "text" : "password"}
          name="password"
          value={form.password}
          onChange={handleChange}
          placeholder="비밀번호"
          className="w-full border rounded px-3 py-2 pr-10"
        />
        <button
          type="button"
          onClick={() => setShowPassword(!showPassword)}
          className="absolute right-3 top-2.5 text-gray-400"
        >
          {showPassword ? "숨기기" : "보기"}
        </button>
        {errors.password && (
          <p className="text-red-500 text-sm mt-1">{errors.password}</p>
        )}
      </div>

      <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded">
        로그인
      </button>
    </form>
  );
}
```

**공통 핸들러 패턴**이 핵심이다. 각 input마다 별도의 핸들러를 만들지 않고, `name` 속성을 활용해서 하나의 함수로 처리한다. 필드가 10개라도 핸들러는 하나로 처리할 수 있다.

비밀번호 보이기/숨기기는 `type`을 `text`와 `password`로 전환하는 방식이다. 정규식으로 실시간 유효성 검사도 추가했다.

---

## TodoList: 전체 구조

`TodoList.jsx`는 이번 과정에서 만든 것 중 가장 기능이 많은 컴포넌트다. 추가, 완료 체크, 삭제, 전체 삭제, LocalStorage 저장까지 포함한다.

```jsx
function TodoList() {
  const [todos, setTodos] = useState(() => {
    // 초기값을 localStorage에서 불러옴
    const saved = localStorage.getItem("todos");
    return saved ? JSON.parse(saved) : [];
  });
  const [input, setInput] = useState("");
  const [showConfirm, setShowConfirm] = useState(false);

  // todos가 바뀔 때마다 localStorage에 저장
  useEffect(() => {
    localStorage.setItem("todos", JSON.stringify(todos));
  }, [todos]);

  // 할 일 추가
  const handleAdd = () => {
    if (!input.trim()) return;
    setTodos([
      ...todos,
      { id: Date.now(), text: input.trim(), completed: false },
    ]);
    setInput("");
  };

  // 완료 토글
  const handleToggle = (id) => {
    setTodos(
      todos.map((todo) =>
        todo.id === id ? { ...todo, completed: !todo.completed } : todo
      )
    );
  };

  // 단일 삭제
  const handleDelete = (id) => {
    setTodos(todos.filter((todo) => todo.id !== id));
  };

  // 전체 삭제 (확인 모달 후)
  const handleDeleteAll = () => {
    setTodos([]);
    setShowConfirm(false);
  };

  return (
    <div className="max-w-md mx-auto p-6">
      <h1 className="text-2xl font-bold mb-4">할 일 목록</h1>

      {/* 입력 영역 */}
      <div className="flex gap-2 mb-6">
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && handleAdd()}
          placeholder="할 일을 입력하세요"
          className="flex-1 border rounded px-3 py-2"
        />
        <button onClick={handleAdd} className="bg-blue-500 text-white px-4 py-2 rounded">
          추가
        </button>
      </div>

      {/* 할 일 목록 */}
      {todos.map((todo) => (
        <TodoItem
          key={todo.id}
          todo={todo}
          onToggle={handleToggle}
          onDelete={handleDelete}
        />
      ))}

      {/* 전체 삭제 버튼 */}
      {todos.length > 0 && (
        <button
          onClick={() => setShowConfirm(true)}
          className="mt-4 text-sm text-red-400 hover:text-red-600"
        >
          전체 삭제
        </button>
      )}

      {/* 확인 모달 */}
      {showConfirm && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center">
          <div className="bg-white p-6 rounded-lg shadow-xl">
            <p className="mb-4">정말 모두 삭제하시겠어요?</p>
            <div className="flex gap-3 justify-end">
              <button onClick={() => setShowConfirm(false)} className="px-4 py-2 border rounded">
                취소
              </button>
              <button onClick={handleDeleteAll} className="px-4 py-2 bg-red-500 text-white rounded">
                삭제
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
```

---

## TodoItem: 자식 컴포넌트

할 일 하나를 표시하는 컴포넌트다. 완료 상태, 삭제 기능을 props로 전달받은 함수로 처리한다.

```jsx
function TodoItem({ todo, onToggle, onDelete }) {
  return (
    <div className="flex items-center gap-3 py-3 border-b group">
      <input
        type="checkbox"
        checked={todo.completed}
        onChange={() => onToggle(todo.id)}
        className="w-4 h-4"
      />
      <span
        className={`flex-1 ${
          todo.completed ? "line-through text-gray-400" : "text-gray-800"
        }`}
      >
        {todo.text}
      </span>
      <button
        onClick={() => onDelete(todo.id)}
        className="text-gray-300 hover:text-red-500 opacity-0 group-hover:opacity-100 transition"
      >
        ✕
      </button>
    </div>
  );
}
```

`group-hover:opacity-100`은 Tailwind CSS 기능으로, 부모 요소에 `group`을 달면 부모에 hover했을 때 자식의 스타일을 바꿀 수 있다. 삭제 버튼이 평소에는 보이지 않다가 카드에 마우스를 올리면 나타나도록 했다.

---

## LocalStorage 연동

`useState`의 초기값을 함수로 전달하면 최초 렌더링 때 한 번만 실행된다.

```jsx
const [todos, setTodos] = useState(() => {
  const saved = localStorage.getItem("todos");
  return saved ? JSON.parse(saved) : [];
});
```

이 패턴으로 앱을 새로고침해도 저장된 할 일이 사라지지 않는다. `useEffect`로 todos가 바뀔 때마다 localStorage에 저장해서 동기화를 유지한다.

---

## 헷갈렸던 점

**`e.preventDefault()`**: form의 submit 이벤트는 기본적으로 페이지를 새로고침한다. React에서 폼을 다룰 때 이 기본 동작을 막지 않으면 상태가 초기화된다. `onSubmit` 핸들러에서 반드시 첫 줄에 넣어야 한다.

**완료 토글의 불변성 유지**: `todo.completed = true`처럼 직접 바꾸면 안 된다. `map()`으로 새 배열을 만들면서 해당 항목만 스프레드로 복사해서 바꿔야 한다.

```jsx
// 잘못된 방식
todos.map((t) => { if (t.id === id) t.completed = !t.completed; return t; });

// 올바른 방식
todos.map((t) => t.id === id ? { ...t, completed: !t.completed } : t);
```

**localStorage는 문자열만 저장**: 객체를 저장할 때는 `JSON.stringify()`, 불러올 때는 `JSON.parse()`를 써야 한다.

---

## 복습용 질문

<details>
<summary>1. 제어 컴포넌트와 비제어 컴포넌트의 차이는?</summary>

제어 컴포넌트는 input의 value를 React 상태(state)로 관리한다. value와 onChange를 함께 쓴다. 비제어 컴포넌트는 DOM이 직접 값을 관리하고, ref로 필요할 때만 값을 읽는다. React에서는 제어 컴포넌트 방식이 일반적이다.

</details>

<details>
<summary>2. TodoList에서 할 일을 완료로 토글할 때 ...t를 쓰는 이유는?</summary>

특정 필드(completed)만 바꾸고 나머지 필드(id, text)는 그대로 유지하기 위해서다. `{ ...t, completed: !t.completed }`는 기존 객체를 복사한 뒤 completed만 덮어쓴 새 객체를 만든다.

</details>

<details>
<summary>3. useState 초기값에 함수를 넣으면 무엇이 다른가?</summary>

`useState(값)` 형태로 쓰면 매 렌더링마다 초기값 표현식이 평가된다. `useState(() => 값)` 형태로 함수를 넣으면 최초 렌더링 때만 실행된다. localStorage 읽기처럼 비용이 있는 작업은 함수 형태로 넣어야 성능상 이점이 있다.

</details>

---

## 한 줄 정리

TodoList 하나 완성하고 나니 React의 핵심 패턴이 전부 들어 있다는 걸 알았다. 상태, 이벤트 처리, 컴포넌트 분리, 부모-자식 데이터 흐름, 부수 효과까지. 내일은 여기서 한 발 더 나아가 실제 서버에서 데이터를 가져오는 비동기 통신을 다룬다.
