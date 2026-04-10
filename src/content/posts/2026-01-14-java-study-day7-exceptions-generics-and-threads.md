---
title: "Java 학습 7일차: 예외 처리, 제네릭, 래퍼 클래스, 스레드 입문"
slug: java-study-day7-exceptions-generics-and-threads
date: 2026-01-14
author: Evan Yoon
category: study
subcategory: self-study
description: "1월 14일 Java 학습 기록. try-catch, 사용자 정의 예외, 제네릭과 제한된 타입 매개변수, 오토박싱, 스레드 생성 방식까지 실습했다."
tags:
  - java
  - study
  - exception
  - generics
  - thread
readTime: 9
series: Java Study
seriesOrder: 7
featured: false
draft: false
toc: true
---

## 실무에 가까운 문법이 한꺼번에 나왔다
`reference/javastudy/day0114/src`는 입문 과정에서 꽤 중요한 전환점이다. 문법을 "쓴다" 수준을 넘어, 오류를 안전하게 처리하고, 재사용 가능한 타입을 만들고, 동시에 여러 작업을 실행하는 개념까지 한 번에 들어온다.

## 예외 처리: 실패를 코드 안으로 끌어들이기
`Study01.java`부터 `Study05.java`까지는 예외 처리 흐름이 잘 드러난다.

- `Study01.java`: `NullPointerException`
- `Study02.java`, `Study04.java`: `Class.forName()`과 `ClassNotFoundException`
- `Study03.java`: 숫자 변환 오류, `null`, 배열 범위 초과를 한 번에 다루기
- `Study05.java`: 사용자 정의 예외를 이용한 계좌 출금 처리

`Account.java`와 `InsufficientExeption.java` 조합은 특히 기억할 만하다. 잔액보다 큰 금액을 출금하려고 할 때 커스텀 예외를 던지도록 해 두었기 때문이다. 이건 "예외는 시스템 오류일 때만 쓰는 것"이 아니라, 비즈니스 규칙 위반도 예외로 표현할 수 있다는 감각을 준다.

## 제네릭으로 타입 안정성 높이기
이날 두 번째 핵심은 제네릭이었다.

- `Box<T>`: 어떤 타입이든 담을 수 있는 박스
- `Product<K, M>`: 두 가지 타입 매개변수를 갖는 객체
- `Study10.java`: 제네릭 메서드
- `Study12.java`: `T extends Number` 제한
- `Study13.java`: 와일드카드를 활용한 수강 등록 예제

특히 `Course.registerCourse1`, `registerCourse2`, `registerCourse3` 예제는 와일드카드가 왜 필요한지 보여 준다. 누구나 등록 가능한 과정, 학생 계열만 가능한 과정, 직장인 계열만 가능한 과정을 타입 제한으로 표현하고 있기 때문이다.

```java
Course.registerCourse2(new Applicant<Student>(new Student()));
Course.registerCourse2(new Applicant<HighStudent>(new HighStudent()));
```

이 코드는 "컴파일 단계에서 잘못된 조합을 막는다"는 제네릭의 장점을 아주 직접적으로 보여 준다.

## 래퍼 클래스와 오토박싱
`Study11.java`, `Study12.java`, 그리고 `wrapper class.txt`를 보면 이 날 래퍼 클래스도 함께 정리한 것으로 보인다. `Integer obj = 100;`처럼 기본형과 객체형이 자연스럽게 오가는 오토박싱/언박싱을 익히는 구간이다.

초보 때는 사소해 보이지만, 컬렉션이나 제네릭을 쓰기 시작하면 기본형과 래퍼 클래스 차이는 꼭 알아야 한다.

## 스레드 입문
`javaStudy2` 패키지의 `Study14.java`, `Study15.java`, `Study16.java`, `SumThread.java`, `WorkThread.java`는 스레드의 기초를 다루고 있다.

- `Thread` 상속 방식
- `Runnable` 구현 방식
- `run()`과 `start()`의 차이
- 별도 작업 흐름 만들기

처음 스레드를 배울 때 중요한 건 "메서드를 호출하는 것"과 "새 실행 흐름을 시작하는 것"이 다르다는 점인데, 이 날 코드가 그 감각을 만들기에 적절했다.

## 오늘의 정리
1월 14일은 초급 Java에서 중급으로 넘어가는 길목 같은 날이었다. 예외 처리로 프로그램을 더 안전하게 만들고, 제네릭으로 타입을 일반화하고, 스레드로 실행 흐름을 분리하는 법을 배웠기 때문이다.

다음 학습일에는 이 스레드 개념이 `synchronized`, `wait/notify`, 데몬 스레드, 그리고 파일 입출력과 결합되면서 훨씬 실용적인 코드로 이어진다.
