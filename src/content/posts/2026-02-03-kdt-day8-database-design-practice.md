---
title: "[APP] 8일차: DB 설계 실습 - 모든 것을 종합하다"
slug: "app-day8-database-design-practice"
date: 2026-02-03
author: "Evan Yoon"
category: "study"
subcategory: "bootcamp"
description: "한 주 동안 배운 모든 개념을 종합해서 실제 데이터베이스를 설계하고 구현한 마지막 날."
thumbnail: ""
tags:
  - app
  - database-design
  - sql
  - project
  - study
  - bootcamp
readTime: 9
series: "APP 과정"
seriesOrder: 8
featured: false
draft: false
toc: true
---

한 주간의 부트캠프가 마지막 날을 맞이했다. 월요일에 **"어떻게 생각할 것인가"**를 배웠다면, 오늘은 **"실제로 설계하고 구현할 수 있는가"**를 시험하는 날이었다. 가장 실전적이고 가장 배우는 점이 많은 하루였다.

## 오늘 다룬 범위

- 실제 비즈니스 요구사항 분석
- 테이블 설계 및 정규화
- Primary Key, Foreign Key 설정
- CREATE TABLE 쿼리 작성
- 샘플 데이터 삽입 및 쿼리 작성
- 설계 검토 및 개선

## 핵심 개념 정리

### 데이터베이스 설계의 단계

#### 1단계: 요구사항 분석

> "어떤 데이터를 저장해야 하나?"

예를 들어, "온라인 쇼핑몰 시스템"을 만든다면:
- 회원 정보: 이름, 이메일, 전화번호, 주소
- 상품 정보: 상품명, 가격, 설명, 재고
- 주문 정보: 누가, 언제, 어떤 상품을, 몇 개
- 리뷰 정보: 누가, 어떤 상품을, 별점, 내용

#### 2단계: 엔티티와 관계 파악

```
회원 (Users)
  ↓ 1:N (한 회원이 여러 주문)
주문 (Orders)
  ↓ 1:N (한 주문에 여러 상품)
주문상세 (OrderDetails)
  ↓ N:1 (여러 주문상세가 한 상품)
상품 (Products)
```

#### 3단계: 테이블 설계

```sql
-- 회원 테이블
CREATE TABLE Users (
    UserID INT PRIMARY KEY AUTO_INCREMENT,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Name VARCHAR(50) NOT NULL,
    Phone VARCHAR(20),
    Address VARCHAR(200),
    CreatedAt DATETIME DEFAULT NOW()
);

-- 상품 테이블
CREATE TABLE Products (
    ProductID INT PRIMARY KEY AUTO_INCREMENT,
    ProductName VARCHAR(100) NOT NULL,
    Price DECIMAL(10, 2) NOT NULL,
    Description TEXT,
    Stock INT DEFAULT 0
);

-- 주문 테이블
CREATE TABLE Orders (
    OrderID INT PRIMARY KEY AUTO_INCREMENT,
    UserID INT NOT NULL,
    OrderDate DATETIME DEFAULT NOW(),
    TotalAmount DECIMAL(10, 2),
    Status VARCHAR(20) DEFAULT 'Pending',
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- 주문상세 테이블
CREATE TABLE OrderDetails (
    OrderDetailID INT PRIMARY KEY AUTO_INCREMENT,
    OrderID INT NOT NULL,
    ProductID INT NOT NULL,
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);
```

#### 4단계: 쿼리 작성 및 테스트

```sql
-- 1. 2025년 1월의 모든 주문
SELECT o.OrderID, u.Name, o.OrderDate, o.TotalAmount
FROM Orders o
JOIN Users u ON o.UserID = u.UserID
WHERE MONTH(o.OrderDate) = 1 AND YEAR(o.OrderDate) = 2025;

-- 2. 각 회원별 총 주문 금액
SELECT u.Name, COUNT(o.OrderID) as order_count, SUM(o.TotalAmount) as total_spent
FROM Users u
LEFT JOIN Orders o ON u.UserID = o.UserID
GROUP BY u.UserID, u.Name
ORDER BY total_spent DESC;

-- 3. 가장 인기 있는 상품 TOP 5
SELECT p.ProductName, COUNT(od.OrderDetailID) as times_ordered
FROM Products p
JOIN OrderDetails od ON p.ProductID = od.ProductID
GROUP BY p.ProductID, p.ProductName
ORDER BY times_ordered DESC
LIMIT 5;
```

## 헷갈렸던 점 / 실수 포인트

### "정말 이 테이블 구조가 맞나?"

설계를 마친 후, 강사님이 한 질문이 정말 좋았다: **"이 구조로 모든 필요한 쿼리를 짤 수 있나?"**

내가 처음 설계할 때:
```
Users - Orders - Products (1:N:1)
```

문제: 한 주문에 여러 상품이 있을 때, Orders 테이블에 ProductID를 여러 개 넣을 수 없다.

해결:
```
Users - Orders - OrderDetails - Products
```

**중간에 OrderDetails(주문상세) 테이블을 만들어서 해결했다.**

**교훈:** "나중에 쿼리를 짤 때 어떤 조인이 필요할지를 먼저 생각하고 테이블을 설계해야 한다."

### "DECIMAL vs INT vs VARCHAR, 어떤 타입을 써야 하나?"

각 데이터의 특성에 맞는 타입을 선택하는 게 중요하다.

```sql
-- 가격: DECIMAL (실수, 정밀도 중요)
Price DECIMAL(10, 2)  -- 최대 8자리 + 소수점 2자리

-- 나이: INT
Age INT

-- 이름: VARCHAR (가변 길이 문자열)
Name VARCHAR(50)

-- 설명: TEXT (긴 문자열)
Description TEXT

-- 날짜: DATETIME
CreatedAt DATETIME

-- 상태: VARCHAR (정해진 값만)
Status VARCHAR(20)  -- 'Pending', 'Shipped', 'Delivered'
```

**타입 선택의 영향:**
- 저장 공간 효율성
- 쿼리 성능
- 데이터 무결성

### "NOT NULL과 DEFAULT는 언제 쓰나?"

```sql
-- NOT NULL: 반드시 값이 있어야 함 (필수 항목)
Email VARCHAR(100) NOT NULL

-- DEFAULT: 값을 넣지 않으면 기본값 사용
CreatedAt DATETIME DEFAULT NOW()
Status VARCHAR(20) DEFAULT 'Pending'

-- NULL 허용: 값이 있을 수도, 없을 수도
Phone VARCHAR(20)  -- 전화번호가 없는 사용자도 있을 수 있음
```

**원칙:**
- "반드시 있어야 한다" → NOT NULL
- "없을 수도 있다" → NULL 허용
- "보통 이 값을 사용한다" → DEFAULT 설정

## 복습 Q&A

<details>
<summary>Q. 한 테이블에 몇 개의 Foreign Key를 가질 수 있나?</summary>

A. 제한이 없다. 필요한 만큼 가질 수 있다.

예를 들어, OrderDetails 테이블:
```sql
CREATE TABLE OrderDetails (
    OrderDetailID INT PRIMARY KEY,
    OrderID INT NOT NULL,           -- FK 1: Orders 참조
    ProductID INT NOT NULL,         -- FK 2: Products 참조
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID),
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID)
);
```

다만, FK가 너무 많으면 테이블이 복잡해지므로 설계를 다시 검토해야 한다.

</details>

<details>
<summary>Q. 같은 이메일을 가진 회원이 두 명 등록되면?</summary>

A. UNIQUE 제약조건으로 막는다.

```sql
CREATE TABLE Users (
    UserID INT PRIMARY KEY,
    Email VARCHAR(100) NOT NULL UNIQUE,  -- 중복 불가
    Name VARCHAR(50)
);
```

이 구조로 같은 이메일을 가진 회원을 두 번 등록하려면 에러가 난다.

</details>

<details>
<summary>Q. LEFT JOIN과 INNER JOIN은 언제 쓰는 차이?</summary>

A. 
```sql
-- INNER JOIN: 양쪽 테이블 모두에 데이터가 있어야 함
SELECT u.Name, o.OrderID
FROM Users u
INNER JOIN Orders o ON u.UserID = o.UserID;
-- 주문이 있는 회원만 나옴

-- LEFT JOIN: 왼쪽 테이블의 모든 행을 보임 (오른쪽이 없으면 NULL)
SELECT u.Name, o.OrderID
FROM Users u
LEFT JOIN Orders o ON u.UserID = o.UserID;
-- 주문이 없는 회원도 나옴 (OrderID는 NULL)
```

**"회원별로 주문 수를 집계할 때" → LEFT JOIN**
**"주문과 회원의 정보를 함께 보일 때" → INNER JOIN**

</details>

## 한 줄 정리

**데이터베이스 설계는 프로그래밍의 기초 위에 세워지는 건축물이다. 처음 설계를 잘못하면 나중에 고치기가 매우 어렵다. 따라서 "나중에 이 데이터를 어떻게 쿼리할 것인가"를 먼저 생각하고 설계해야 한다.**

---

## 한 주를 정리하며

월요일부터 오늘까지, 정말 밀도 있는 한 주였다.

- **월요일**: Computational Thinking (어떻게 생각할 것인가)
- **화요일**: 자료구조 (데이터를 어떻게 담을 것인가)
- **수요일**: 알고리즘 (효율적으로 어떻게 처리할 것인가)
- **목요일**: 개발 도구 (실전에서 어떻게 협업할 것인가)
- **금요일**: DB 개념 (데이터를 어떻게 영구보관할 것인가)
- **토요일**: SQL (데이터를 어떻게 조작할 것인가)
- **일요일**: NoSQL (다른 방식의 접근)
- **월요일**: DB 설계 (모든 개념을 종합해서 설계하기)

**이제 느껴진다.** 처음 월요일에 배웠던 Computational Thinking이 모든 주차의 기초였다는 걸. 

"문제를 어떻게 분석할 것인가"부터 시작해서 "실제로 어떻게 구현하고 협업할 것인가"까지, 모든 게 연결되어 있다.

다음 주부터는 프로젝트 실습이 시작된다고 했다. 이번 주 배운 내용을 실제로 써먹을 때가 온 것 같다.

벌써 부트캠프 2주차를 손꼽아 기다리고 있다.
