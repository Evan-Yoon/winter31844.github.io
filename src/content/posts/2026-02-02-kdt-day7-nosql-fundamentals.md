---
title: "[KDT] 7일차: NoSQL 이해 - 다른 방식의 데이터 저장"
slug: "kdt-day7-nosql-fundamentals"
date: 2026-02-02
author: "Evan Yoon"
category: "study"
subcategory: "bootcamp"
description: "SQL만이 아닌 NoSQL, 특히 MongoDB를 배우며 데이터 저장의 다양성을 이해한 하루."
thumbnail: ""
tags:
  - kdt
  - nosql
  - mongodb
  - json
  - database
  - study
  - bootcamp
readTime: 8
series: "KDT 부트캠프 기록"
seriesOrder: 7
featured: false
draft: false
toc: true
---

지금까지 SQL의 **정형적이고 엄격한** 세계에 있었다면, 오늘은 **유연하고 동적인** NoSQL의 세계로 들어갔다. "모든 데이터베이스가 SQL처럼 작동해야 하나?"라는 질문에서 출발한 NoSQL은 대규모 비정형 데이터를 다루는 현대 웹의 핵심이 되었다.

## 오늘 다룬 범위

- NoSQL의 정의와 등장 배경
- 관계형 DB와 NoSQL의 차이
- MongoDB의 기본 구조 (Collection, Document, Field)
- JSON 형식 이해
- MongoDB의 기본 CRUD 연산

## 핵심 개념 정리

### NoSQL은 왜 나왔을까?

강사님의 질문부터 시작했다: **"SQL로 충분하지 않나?"**

SQL은 완벽하지만, **한 가지 약점이 있다: 구조가 고정되어 있다.**

```
SQL의 문제점:
┌─────┬──────┬───────┐
│ ID  │ Name │ Email │  ← 모든 행이 같은 구조
├─────┼──────┼───────┤
│ 1   │ Kim  │ ...   │
│ 2   │ Park │ ...   │
│ 3   │ Lee  │ ...   │
└─────┴──────┴───────┘

만약 한 학생만 전화번호가 있다면? 
→ 모든 학생을 위한 Phone 열을 만들어야 함 (NULL로 채움)
```

반면 NoSQL은 **각 문서가 다른 구조를 가질 수 있다.**

```json
// 문서 1
{
  "_id": 1,
  "name": "Kim",
  "email": "kim@example.com"
}

// 문서 2
{
  "_id": 2,
  "name": "Park",
  "email": "park@example.com",
  "phone": "010-1234-5678",
  "interests": ["reading", "gaming"]
}
```

**같은 컬렉션 안의 문서들이 서로 다른 구조를 가질 수 있다.**

### NoSQL의 특징

#### 1. **유연한 스키마**
미리 구조를 정의할 필요가 없다. 데이터를 추가하면서 구조가 자동으로 확장된다.

#### 2. **확장성**
SQL은 수직 확장(서버 업그레이드)이 주인데, NoSQL은 수평 확장(서버 추가)이 쉽다.

#### 3. **빠른 개발 속도**
스키마를 미리 정의할 필요 없어서, 프로토타이핑이 빠르다.

### MongoDB의 구조

MongoDB는 NoSQL의 가장 대중적인 예다.

```
Database (데이터베이스)
  ├─ Collection (테이블 같은 개념)
  │   ├─ Document (행 같은 개념)
  │   │   ├─ Field (열 같은 개념)
  │   │   └─ Field
  │   └─ Document
```

#### Collection과 Document

```javascript
// MongoDB의 기본 쿼리

// 1. 삽입 (CREATE)
db.students.insertOne({
  name: "Kim",
  email: "kim@example.com",
  age: 20,
  tags: ["cs", "python"]
});

// 2. 조회 (READ)
db.students.find({ age: { $gt: 20 } });

// 3. 수정 (UPDATE)
db.students.updateOne(
  { name: "Kim" },
  { $set: { email: "newemail@example.com" } }
);

// 4. 삭제 (DELETE)
db.students.deleteOne({ name: "Kim" });
```

### SQL과 NoSQL의 비교

| 개념 | SQL | NoSQL |
|------|-----|-------|
| 데이터 구조 | Table | Collection |
| 행 | Row/Record | Document |
| 열 | Column/Attribute | Field |
| 관계 | Primary/Foreign Key | Reference, Embedding |
| 데이터 형식 | 정형 | JSON |
| 확장성 | 수직 | 수평 |
| 트랜잭션 | 강함 | 약함 |

## 헷갈렸던 점 / 실수 포인트

### "NoSQL이 SQL보다 좋은 건가?"

절대 아니다. 강사님이 명확히 했다:

> "NoSQL이 SQL을 대체하지 않는다. 상황에 따라 어떤 걸 선택할지가 중요하다."

**SQL을 써야 할 때:**
- 데이터 구조가 명확하고 변하지 않을 때
- 복잡한 JOIN이 필요할 때
- ACID 트랜잭션이 중요할 때 (은행, 금융)

**NoSQL을 써야 할 때:**
- 구조가 자주 바뀔 때 (SNS 피드, 추천 시스템)
- 대규모 비정형 데이터를 빠르게 저장할 때
- 높은 쓰기 성능이 필요할 때 (IoT 센서 데이터)

### "JSON은 무엇인가?"

처음엔 JSON이 복잡해 보였다. 하지만 강사님이 명확히 해줬다:

**JSON = JavaScript Object Notation**

파이썬의 딕셔너리나 JavaScript의 객체와 매우 유사하다.

```json
{
  "name": "Kim",
  "age": 20,
  "tags": ["cs", "python"],
  "address": {
    "city": "Seoul",
    "zip": "12345"
  }
}
```

구조가 단순하고 모든 프로그래밍 언어에서 쉽게 파싱할 수 있어서, 웹 개발의 표준 형식이 되었다.

### "MongoDB에서 정규화는 어떻게 하나?"

SQL에서는 정규화를 위해 테이블을 분리했는데, MongoDB는?

두 가지 방식이 있다:

#### 1. Embedding (포함) - 정규화 X
```javascript
{
  _id: 1,
  name: "Kim",
  address: {
    city: "Seoul",
    zip: "12345"
  }
}
```

단순하고 빠르지만, 중복이 생길 수 있다.

#### 2. Reference (참조) - 정규화 O
```javascript
// 학생 문서
{ _id: 1, name: "Kim", addressId: 100 }

// 주소 문서
{ _id: 100, city: "Seoul", zip: "12345" }
```

SQL처럼 분리하지만, JOIN이 필요해진다.

**"상황에 따라 선택한다"**는 유연성이 NoSQL의 강점이다.

## 복습 Q&A

<details>
<summary>Q. MongoDB에서 _id는 무엇인가?</summary>

A. 모든 Document가 필수로 가져야 하는 고유한 식별자다.

```javascript
// 자동 생성 (ObjectID)
{ _id: ObjectId("507f1f77bcf86cd799439011"), name: "Kim" }

// 또는 직접 지정 가능
{ _id: 1, name: "Kim" }
{ _id: "user_kim", name: "Kim" }
```

SQL의 Primary Key처럼 각 문서를 고유하게 구분한다.

</details>

<details>
<summary>Q. NoSQL에서는 JOIN이 없나?</summary>

A. 없는 건 아니지만, SQL의 JOIN보다 훨씬 복잡하다.

MongoDB의 `$lookup` 연산자를 사용할 수 있지만, 성능이 떨어진다.

그래서 NoSQL 설계 철학은: **"JOIN을 피하도록 데이터를 구조화한다"**

대신 embedding을 많이 사용해서 조인이 필요 없게 만든다.

</details>

<details>
<summary>Q. NoSQL은 ACID를 지원하지 않나?</summary>

A. MongoDB 같은 최신 NoSQL도 ACID를 부분적으로 지원한다.

하지만 SQL의 강력한 ACID 트랜잭션에 비하면 제약이 많다.

금융 거래처럼 데이터 무결성이 절대 중요한 경우엔 SQL이 더 적합하다.

</details>

## 한 줄 정리

**NoSQL은 SQL의 반대가 아니라, 다른 선택이다. 데이터의 특성과 사용 패턴에 따라 올바른 도구를 선택하는 것이 중요하다.**

내일은 한 주의 마무리로 **DB 설계 실습**을 한다. 지금까지 배운 모든 개념을 종합해서 실제로 데이터베이스를 설계하고 구현해보는 시간이 될 것 같다.
