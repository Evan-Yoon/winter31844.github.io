---
title: "Java 학습 4일차: 클래스, 객체, 메서드"
slug: java-study-day4-classes-methods-and-console-projects
date: 2026-01-08
author: Evan Yoon
category: study
subcategory: self-study
description: "1월 8일 Java 학습 기록. 클래스와 객체, 생성자, 메서드, static, 오버로딩을 익히고 주소록과 가계부 콘솔 프로그램까지 만들어 봤다."
tags:
  - java
  - study
  - class
  - object
  - mini-project
readTime: 9
series: Java Study
seriesOrder: 4
featured: false
draft: false
toc: true
---

## 객체지향이 본격적으로 시작된 날

`reference/javastudy/day0108/src/javaStudy`는 확실히 분위기가 달랐다. 앞선 날짜가 문법 조각들을 익히는 단계였다면, 이날부터는 클래스 파일이 여러 개 등장하고 서로 연결되기 시작한다. `Student`, `Car`, `Korean`, `Calculator`, `AddressBook`, `PinMoney` 같은 이름만 봐도 이제는 "대상"을 만들어 두고 거기에 데이터를 넣는 방식으로 사고가 바뀌었음을 알 수 있다.

## 실제로 만든 객체들

이날 연습한 대표 클래스는 다음과 같았다.

- `Car.java`: 회사명, 모델명, 색상, 속도, 연료 상태를 가진 자동차 객체
- `Korean.java`: 이름과 주민번호를 생성자로 받는 객체
- `Calculator.java`, `CalculatorStatic.java`: 인스턴스 메서드와 `static` 메서드 비교
- `AddressBook.java`: 이름, 전화번호, 나이를 보관하는 주소록 모델
- `PinMoney.java`: 날짜, 내용, 금액, 수입/지출 여부를 저장하는 가계부 모델

`Study06.java`, `Study07.java`, `Study08.java`에서는 `new`로 객체를 만들고 필드를 확인하는 가장 기본적인 생성 과정을 반복했다. `Korean` 클래스는 생성자를 통해 필수 데이터를 받도록 만들어 두어서, "객체가 만들어질 때 어떤 값이 꼭 필요할까?"를 생각하게 해 준다.

## 메서드와 오버로딩

`Calculator.java` 실습은 메서드 감각을 익히기에 좋았다. 전원 켜기/끄기, 더하기, 나누기, 직사각형 넓이 계산 같은 기능이 메서드로 분리돼 있고, `Study10.java`에서는 `plus()`를 여러 형태로 호출하면서 오버로딩 개념도 함께 다뤘다.

즉, 같은 이름의 메서드라도 매개변수 구성이 다르면 다른 메서드로 인식된다는 점을 실제 사용 코드에서 확인한 것이다. 이 구간은 "함수는 하나만 있어야 한다"는 초보적인 인식에서 벗어나게 해 준다.

## 콘솔 프로젝트 1: 주소록

`Study15.java`는 사실상 작은 콘솔 주소록 프로그램이다. `AddressBook[] abs = new AddressBook[20];` 배열을 만들고, 메뉴를 출력한 뒤 조회와 입력 기능을 제공한다.

```java
AddressBook[] abs = new AddressBook[20];
while (true) {
    printMenu();
    input = scan.nextLine();
    switch (input) {
        case "1":
            for (int i = 0; i < AddressBook.cnt; i++) {
                abs[i].showData();
            }
            break;
```

아직 파일 저장은 없고 메모리 안에서만 동작하지만, 사용자 입력을 받아 객체 배열에 넣고 다시 출력하는 흐름이 이미 작은 CRUD 프로그램의 모양을 갖추고 있다.

## 콘솔 프로젝트 2: 가계부

`Study16.java`와 `PinMoney.java`는 더 흥미로웠다. 수입과 지출을 구분하고, 항목이 쌓일 때마다 총액을 함께 관리하는 구조였다. `PinMoney.totalMoney` 같은 정적 필드를 통해 전체 합계를 관리하는 방식도 확인할 수 있다.

메뉴는 단순하지만 구조는 꽤 실용적이다.

- 내역 조회
- 수입 등록
- 지출 등록
- 종료

입문 단계에서 이 정도 흐름을 직접 만들면, 나중에 파일 저장이나 DB 연동이 붙어도 전체 구조를 이해하기 수월하다.

## 오늘의 정리

1월 8일은 자바가 "문법 과목"에서 "프로그램 설계 도구"로 바뀐 날이었다. 객체를 만들고, 생성자로 초기화하고, 메서드로 기능을 분리하고, 배열에 여러 객체를 담아 순회하는 구조를 실제로 손에 익히기 시작했다.

이후 학습에서 상속, 싱글턴, 다형성이 나와도 결국 기반은 이날 다진 클래스 설계 감각 위에 올라간다.
