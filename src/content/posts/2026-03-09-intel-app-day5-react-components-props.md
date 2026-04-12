---
title: "[App Dev] Day 5: React 컴포넌트와 Props, 조각을 조합하다"
slug: intel-app-day5-react-components-props
date: 2026-03-09
author: Evan Yoon
category: study
subcategory: bootcamp
description: "Intel AI 앱 개발 5일차. 컴포넌트를 쪼개고 props로 데이터를 전달하는 패턴을 팀원 카드와 유저 카드를 만들며 익혔다."
thumbnail: ""
tags:
  - react
  - components
  - props
  - frontend
  - bootcamp
readTime: 9
series: "Intel App Dev 2026"
seriesOrder: 5
featured: false
draft: false
toc: true
---

## 컴포넌트는 왜 쪼개는가

지난주 금요일에 JSX와 기본 컴포넌트 개념을 익혔다. 오늘은 그것을 실제로 활용하는 핵심 패턴인 **컴포넌트 분리와 props**를 배웠다.

처음에는 "한 파일에 다 쓰면 되지 않나"라는 생각을 했다. 그런데 팀원 목록 페이지를 만들면서 바로 이해가 됐다. 팀원이 5명이면 같은 카드 구조를 5번 반복해야 한다. 이때 카드 하나를 컴포넌트로 만들어두면, 데이터만 다르게 넘겨서 똑같은 구조를 재사용할 수 있다. 코드는 훨씬 짧아지고, 카드 디자인을 바꿀 때 한 곳만 고치면 된다.

---

## Props란 무엇인가

Props는 **부모 컴포넌트가 자식 컴포넌트에 데이터를 전달하는 방법**이다. HTML 속성처럼 전달하고, 함수 인자처럼 받는다.

```jsx
// 부모 컴포넌트
function App() {
  return (
    <UserCard
      name="Evan Yoon"
      age={30}
      hobbies={["코딩", "독서", "헬스"]}
    />
  );
}

// 자식 컴포넌트
function UserCard({ name, age, hobbies }) {
  return (
    <div className="card">
      <h2>{name}</h2>
      <p>나이: {age}</p>
      <ul>
        {hobbies.map((hobby, i) => (
          <li key={i}>{hobby}</li>
        ))}
      </ul>
    </div>
  );
}
```

`props`는 객체로 전달되고, 구조 분해 할당으로 받으면 코드가 깔끔해진다. 문자열은 따옴표로, 나머지(숫자, 배열, 객체, 함수)는 `{}`로 전달한다.

---

## TeamMember 컴포넌트 만들기

오늘 가장 많이 시간을 쓴 건 팀원 카드를 설계하는 것이었다. `TeamMember.jsx`는 팀원 한 명의 정보를 props로 받아서 카드 형태로 보여준다.

```jsx
function TeamMember({ name, role, skills, github, email, avatar }) {
  return (
    <div className="member-card group hover:shadow-xl transition-all duration-300">
      {/* 아바타 */}
      <div className="avatar-wrapper">
        <img src={avatar} alt={`${name} 프로필`} className="rounded-full" />
      </div>

      {/* 이름과 역할 */}
      <h3 className="text-lg font-bold">{name}</h3>
      <p className="text-sm text-gray-400">{role}</p>

      {/* 스킬 태그 */}
      <div className="skills flex flex-wrap gap-1 mt-2">
        {skills.map((skill) => (
          <span key={skill} className="px-2 py-1 text-xs bg-gray-700 rounded-full">
            {skill}
          </span>
        ))}
      </div>

      {/* 링크 */}
      <div className="links mt-3 flex gap-3">
        {github && (
          <a href={github} target="_blank" rel="noreferrer">
            GitHub
          </a>
        )}
        {email && <a href={`mailto:${email}`}>Email</a>}
      </div>
    </div>
  );
}
```

`{github && <a>...}` 패턴은 github prop이 전달된 경우에만 링크를 보여준다. props가 없으면 렌더링 자체를 건너뛴다.

---

## TeamPage: 컴포넌트를 조합하다

`TeamPage.jsx`에서는 팀원 데이터를 배열로 정의하고, `TeamMember`를 반복해서 그린다.

```jsx
const members = [
  {
    name: "윤이반",
    role: "AI Developer",
    skills: ["Python", "React", "FastAPI"],
    github: "https://github.com/Evan-Yoon",
    avatar: "/images/evan.jpg",
  },
  {
    name: "최현석",
    role: "Backend Developer",
    skills: ["Java", "Spring", "MySQL"],
    github: "https://github.com/hyunseok",
    avatar: "/images/hyunseok.jpg",
  },
  // ...
];

function TeamPage() {
  return (
    <main className="min-h-screen bg-black text-white">
      <h1 className="text-3xl font-bold text-center py-10">우리 팀</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 px-6">
        {members.map((member) => (
          <TeamMember key={member.name} {...member} />
        ))}
      </div>
    </main>
  );
}
```

`{...member}`는 스프레드 연산자로 member 객체의 모든 속성을 props로 펼쳐서 전달한다. 일일이 `name={member.name} role={member.role}`처럼 쓰지 않아도 된다.

---

## UserCard: 조건부 렌더링 실습

`UserCard.jsx`에서는 특정 props가 있을 때만 해당 요소를 보여주는 패턴을 연습했다.

```jsx
function UserCard({ name, age, hobbies, isVip }) {
  return (
    <div className={`card ${isVip ? "border-yellow-400" : "border-gray-200"}`}>
      {isVip && (
        <span className="badge bg-yellow-400 text-black">VIP</span>
      )}
      <h2>{name}</h2>
      <p>나이: {age}세</p>

      {hobbies && hobbies.length > 0 ? (
        <ul>
          {hobbies.map((h, i) => <li key={i}>{h}</li>)}
        </ul>
      ) : (
        <p className="text-gray-400">취미 정보 없음</p>
      )}
    </div>
  );
}
```

className 안에서도 삼항연산자를 써서 조건에 따라 다른 스타일을 줄 수 있다. `isVip`가 true면 금색 테두리, false면 회색 테두리.

---

## NameCard: 폼 입력 + 카드 미리보기

`NameCard.jsx`는 폼 입력값을 실시간으로 카드에 반영하는 컴포넌트다. 아직 상태(state)를 다루기 전 단계라, 오늘은 구조만 설계하고 완성은 내일로 미뤘다.

```jsx
function NameCard({ name, job, color }) {
  return (
    <div
      className="rounded-xl p-6 shadow-lg text-white"
      style={{ backgroundColor: color || "#3b82f6" }}
    >
      <h2 className="text-2xl font-bold">{name || "이름 입력"}</h2>
      <p className="text-sm mt-1 opacity-80">{job || "직업 입력"}</p>
    </div>
  );
}
```

`style={{ backgroundColor: color }}` 처럼 인라인 스타일은 객체로 전달한다. CSS 속성명은 camelCase로 쓴다(`background-color` → `backgroundColor`).

---

## Props의 기본값 설정

props가 전달되지 않았을 때 기본값을 주는 방법은 두 가지다.

```jsx
// 방법 1: 구조 분해 할당에서 기본값
function Greeting({ name = "방문자", color = "#333" }) {
  return <h1 style={{ color }}>안녕하세요, {name}님</h1>;
}

// 방법 2: defaultProps (옛날 방식, 현재는 위 방식 권장)
Greeting.defaultProps = {
  name: "방문자",
  color: "#333",
};
```

---

## 헷갈렸던 점

**props는 읽기 전용이다**: 자식 컴포넌트가 props를 직접 바꿀 수 없다. 데이터의 흐름은 항상 부모 → 자식으로만 흐른다. 자식이 부모의 데이터를 바꾸고 싶으면 부모가 함수를 props로 넘겨줘야 한다.

**`{...member}` 스프레드**: 객체의 모든 속성을 한 번에 props로 전달한다. 편리하지만 불필요한 props까지 넘어갈 수 있어서 컴포넌트가 어떤 props를 받는지 분명히 알고 쓰는 게 좋다.

**children prop**: `<Card>내용</Card>`처럼 컴포넌트 태그 사이에 넣은 것은 `props.children`으로 받을 수 있다. 오늘은 짧게만 봤고 내일 더 쓰게 될 것 같다.

---

## 복습용 질문

<details>
<summary>1. 부모에서 자식으로 데이터를 어떻게 전달하는가?</summary>

props를 통해 전달한다. 부모가 자식 컴포넌트를 쓸 때 HTML 속성처럼 `<Child name="값" count={10} />`으로 넘기고, 자식은 함수 파라미터에서 `function Child({ name, count })`로 받는다.

</details>

<details>
<summary>2. props를 자식이 직접 변경할 수 없는 이유는?</summary>

React의 단방향 데이터 흐름 원칙 때문이다. 데이터는 항상 부모 → 자식으로만 흐른다. props를 자식이 수정하면 어디서 데이터가 바뀌는지 추적하기 어려워진다. 자식이 부모 데이터를 바꾸고 싶으면 부모가 전달한 함수를 호출한다.

</details>

<details>
<summary>3. 컴포넌트 이름이 반드시 대문자로 시작해야 하는 이유는?</summary>

소문자로 시작하면 React가 HTML 기본 태그(`div`, `span` 등)로 인식한다. 대문자로 시작해야 React가 커스텀 컴포넌트로 인식하고 해당 함수를 호출한다.

</details>

---

## 한 줄 정리

컴포넌트 분리는 처음에는 귀찮아 보이지만, 데이터가 늘어날수록 코드가 짧아지고 수정이 쉬워진다는 걸 직접 확인했다. 내일은 컴포넌트 안에서 데이터가 변하는 상황, 즉 상태(state) 관리를 배운다.
