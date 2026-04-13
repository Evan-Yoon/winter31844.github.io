---
title: "[APP] 6일차: SQL 문법 기초 - 데이터에 질문을 던지다"
slug: "app-day6-sql-fundamentals"
date: 2026-01-30
author: "Evan Yoon"
category: "study"
subcategory: "bootcamp"
description: "SELECT, CREATE, INSERT, UPDATE, DELETE로 데이터를 다루는 기본 언어, SQL을 배운 하루."
thumbnail: ""
tags:
  - app
  - sql
  - ddl
  - dml
  - database
  - study
  - bootcamp
readTime: 8
series: "APP 과정"
seriesOrder: 6
featured: false
draft: false
toc: true
---

오늘은 드디어 **"실제로 데이터를 다루는" 경험**을 했다. 데이터베이스 테이블을 설계했다면, 오늘부터는 그 테이블에 데이터를 넣고, 빼고, 찾고, 수정한다. **SQL(Structured Query Language)**은 관계형 데이터베이스와 대화하는 가장 기본적인 언어다.

## 오늘 다룬 범위

- SQL의 정의와 특징
- DDL(Data Definition Language): CREATE, ALTER, DROP
- DML(Data Manipulation Language): SELECT, INSERT, UPDATE, DELETE
- WHERE, ORDER BY, GROUP BY 등 기본 쿼리 작성
- SQL Fiddle로 직접 연습

## 핵심 개념 정리

### SQL이란?

SQL은 관계형 데이터베이스에서 데이터를 다루기 위한 표준 언어다.

> "SQL은 프로그래밍 언어가 아니라, 데이터베이스에 대한 질문 언어다."

파이썬이나 자바는 "무엇을 어떻게 계산할 것인가"에 집중하지만, SQL은 "어떤 데이터를 원하는가"에 집중한다.

**예:**
```python
# 파이썬: "어떻게" 하는지 설명
students = []
for record in all_records:
    if record.age >= 18:
        students.append(record)

# SQL: "무엇을" 원하는지 표현
SELECT * FROM students WHERE age >= 18;
```

### DDL - 테이블을 만들고 변경하기

#### CREATE: 테이블 생성

```sql
CREATE TABLE Students(
    StudentID INTEGER PRIMARY KEY,
    Name TEXT,
    Age INTEGER,
    Department TEXT
);
```

**핵심:**
- **PRIMARY KEY**: 각 행을 고유하게 구분
- **데이터 타입**: INTEGER, TEXT, DATE, BOOLEAN 등

#### ALTER: 테이블 수정

```sql
-- 새로운 열 추가
ALTER TABLE Students ADD COLUMN GPA DECIMAL;

-- 열 삭제
ALTER TABLE Students DROP COLUMN GPA;

-- 열의 타입 변경
ALTER TABLE Students MODIFY COLUMN Age DECIMAL;
```

#### DROP: 테이블 삭제

```sql
DROP TABLE Students;  -- 주의: 데이터도 함께 삭제됨
```

### DML - 데이터를 조작하기

#### SELECT: 데이터 조회

```sql
-- 모든 학생 조회
SELECT * FROM Students;

-- 특정 열만 조회
SELECT Name, Department FROM Students;

-- 조건부 조회
SELECT * FROM Students WHERE Age >= 18;

-- 정렬
SELECT * FROM Students ORDER BY Age DESC;

-- 그룹화 및 집계
SELECT Department, COUNT(*) as student_count
FROM Students
GROUP BY Department;
```

SELECT는 단순해 보이지만, WHERE, GROUP BY, ORDER BY, HAVING, JOIN 등을 조합하면 매우 복잡한 쿼리가 된다.

#### INSERT: 데이터 추가

```sql
INSERT INTO Students(StudentID, Name, Age, Department)
VALUES (1001, 'Kim InTemple', 20, 'Computer Science');

INSERT INTO Students VALUES (1002, 'Park MinJun', 21, 'Business');
```

#### UPDATE: 데이터 수정

```sql
-- 특정 학생의 성적 수정
UPDATE Students
SET GPA = 3.5
WHERE StudentID = 1001;

-- 모든 학생의 학과를 수정 (주의!)
UPDATE Students
SET Department = 'Engineering';
```

**주의:** WHERE 절이 없으면 모든 행이 수정된다!

#### DELETE: 데이터 삭제

```sql
-- 특정 학생 삭제
DELETE FROM Students WHERE StudentID = 1001;

-- 모든 데이터 삭제 (주의!)
DELETE FROM Students;
```

**주의:** UPDATE와 마찬가지로 WHERE 절 없이 실행하면 모든 데이터가 삭제된다!

## 헷갈렸던 점 / 실수 포인트

### "SELECT *는 왜 나쁜가?"

처음엔 "모든 열을 보고 싶으면 *를 쓰면 되지, 왜 안 된다고 하나?"라고 생각했다.

하지만 강사님의 설명:

> "운영 중인 데이터베이스에서 SELECT * 하면 불필요한 모든 데이터를 불러온다. 네트워크 트래픽, 메모리 낭비가 심하다. 필요한 열만 명시해라."

**생산 환경에서는:**
```sql
-- 좋은 예
SELECT StudentID, Name, GPA FROM Students;

-- 나쁜 예
SELECT * FROM Students;
```

학습용으로는 상관없지만, 습관들이는 게 중요하다.

### "UPDATE 없이 WHERE를 빼먹으면 끔찍한 일이 생긴다"

오늘 가장 무서웠던 순간이었다. 실습할 때 실수로:

```sql
UPDATE Students SET GPA = 4.0;  -- WHERE를 빼먹음!
```

이 쿼리를 실행하면 **모든 학생의 GPA가 4.0으로 바뀌어 버린다.**

그래서 배운 교훈:
1. **UPDATE/DELETE 전에 항상 SELECT로 먼저 확인**
2. **WHERE 절 작성 후 테스트해보기**
3. **운영 환경에서는 트랜잭션 사용**

### "JOIN이 정말 복잡하다"

오늘 가장 어려웠던 부분이 JOIN이었다.

```sql
-- 학생과 학과 정보를 함께 보고 싶다
SELECT Students.Name, Departments.DeptName
FROM Students
JOIN Departments ON Students.DeptID = Departments.DeptID;
```

처음엔 이 문법이 왜 필요한지 몰랐는데, 강사님의 설명 후 깨달았다.

**정규화 때문에 테이블을 분리했으므로, 다시 합쳐서 보려면 JOIN을 써야 한다.** 정규화의 대가다.

## 복습 Q&A

<details>
<summary>Q. SQL은 대소문자를 구분하나?</summary>

A. 대부분의 데이터베이스에서 **SQL 키워드는 대소문자를 구분하지 않는다.**

```sql
-- 모두 같은 쿼리
SELECT * FROM Students;
select * from students;
Select * From Students;
```

하지만 **데이터 값이나 테이블명은 경우에 따라 다를 수 있다.**

관례상 **키워드는 대문자**, **테이블/열 이름은 소문자**로 쓴다:
```sql
SELECT Name FROM Students WHERE Age >= 18;
```

</details>

<details>
<summary>Q. WHERE 조건을 여러 개 쓸 수 있나?</summary>

A. 당연하다! **AND, OR을 사용**한다.

```sql
-- AND: 모든 조건을 만족
SELECT * FROM Students 
WHERE Age >= 18 AND Department = 'CS';

-- OR: 하나 이상의 조건을 만족
SELECT * FROM Students 
WHERE Department = 'CS' OR Department = 'Engineering';

-- 조합
SELECT * FROM Students 
WHERE Age >= 18 AND (Department = 'CS' OR Department = 'Engineering');
```

</details>

<details>
<summary>Q. NULL 값을 찾으려면?</summary>

A. **IS NULL을 사용** (= NULL은 안 됨)

```sql
-- 전화번호가 없는 학생
SELECT * FROM Students WHERE Phone IS NULL;

-- 전화번호가 있는 학생
SELECT * FROM Students WHERE Phone IS NOT NULL;
```

NULL은 "값이 없다"는 의미이므로, 일반적인 비교 연산자로는 찾을 수 없다.

</details>

## 한 줄 정리

**SQL은 처음엔 단순해 보이지만, 깊이 있고 강력한 언어다. 간단한 CRUD부터 복잡한 조인과 서브쿼리까지, 얼마나 효율적으로 쿼리를 짜느냐에 따라 프로그램의 성능이 결정된다.**

내일은 일주일 공부를 정리하는 시간을 가진다. NoSQL을 배우면서 "관계형 데이터베이스 말고 다른 접근법도 있다"는 걸 배우게 될 것 같다.
