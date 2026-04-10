---
title: "Java 학습 5일차: 싱글턴, 상속, 오버라이딩"
slug: java-study-day5-constructors-singleton-inheritance-bankbook
date: 2026-01-12
author: Evan Yoon
category: study
subcategory: self-study
description: "1월 12일 Java 학습 기록. 싱글턴 패턴, 상속, 메서드 재정의, 다형성을 익히고 은행장부 콘솔 프로그램으로 구조를 정리했다."
tags:
  - java
  - study
  - inheritance
  - singleton
  - polymorphism
readTime: 9
series: Java Study
seriesOrder: 5
featured: false
draft: false
toc: true
---

## 객체지향이 한 단계 깊어진 날

`reference/javastudy/day0112/src/javaStudy`는 클래스가 더 많아지고 관계도 더 복잡해진다. `Singleton`, `Phone`-`SmartPhone`, `Airplane`-`SuperSonicAirplane`, `Tire`-`HankookTire`-`KumhoTire`, `Bank`-`BankTuple` 구조를 보면 이날 핵심 주제는 분명했다. "객체를 어떻게 잘 나누고, 어떻게 재사용할 것인가"였다.

## 싱글턴과 캡슐화

`Bank.java`는 가장 눈에 띄는 파일이었다. 생성자를 `private`으로 막고, 내부에 자기 자신 인스턴스를 하나만 들고 있는 전형적인 싱글턴 구조를 사용한다.

```java
private static Bank bank = new Bank();
private Bank(){}

public static Bank getBank() {
    return bank;
}
```

이 패턴을 가계부나 은행장부처럼 "전체에서 하나만 관리돼야 하는 객체"에 적용한 점이 좋았다. `BankTuple[] bankBook` 배열과 `cnt`를 내부에 두고, `setBankTuple()`과 `showBankBook()`으로만 접근하게 만든 것도 자연스러운 캡슐화 연습이었다.

`Study05.java`에서는 `Scanner`로 날짜, 내용, 금액을 받아 `BankTuple` 객체를 만들고, 이를 `Bank.getBank().setBankTuple(t)`로 저장한다. 조회 시에는 수입이면 합계를 더하고, 지출이면 빼면서 잔액을 함께 출력한다. 간단하지만 실제 서비스 로직의 골격과 비슷하다.

## 상속과 메서드 재정의

이날은 상속과 오버라이딩 실습도 많이 보였다.

- `Phone`과 `SmartPhone`
- `Calculator`와 `Computer`
- `Airplane`과 `SuperSonicAirplane`
- `Parent`와 `Child`
- `Car`와 `SportsCar`

`SuperSonicAirplane.java`에서 `fly()`를 재정의한 부분은 메서드 오버라이딩 감각을 익히기에 좋은 예였다. 부모 클래스의 기능을 그대로 물려받되, 필요한 부분만 자식 클래스에서 바꿔서 동작시킨다. 이 개념이 익숙해지면 나중에 프레임워크를 배울 때도 훨씬 편하다.

## 다형성의 시작

`Study13.java`에서는 `Car` 객체의 타이어를 `Tire` 타입으로 다루고, 실제 인스턴스로는 `HankookTire`와 `KumhoTire`를 갈아 끼우는 구조가 나온다. 바로 이 지점에서 "부모 타입으로 자식 객체를 참조한다"는 다형성의 핵심이 드러난다.

또 `Study11.java`, `Study12.java`에서는 부모 타입과 자식 타입 간 캐스팅을 실습하고 있었다. 어떤 경우는 바로 가능하고, 어떤 경우는 실제 객체 타입을 확인해야 한다는 점을 코드를 통해 익힌 셈이다.

## 오늘의 정리

1월 12일은 클래스가 서로 관계를 맺기 시작한 날이었다. 그냥 객체를 하나씩 만드는 수준을 넘어서, 상속으로 확장하고, 재정의로 동작을 바꾸고, 싱글턴으로 상태를 중앙 관리하는 구조를 연습했다.

특히 `Bank`와 `BankTuple` 실습은 이후 파일 저장, UI 연결, 데이터 모델링으로 이어질 수 있는 좋은 토대였다. 다음 학습일에는 이 다형성 개념이 인터페이스와 결합되면서 더 유연한 설계로 확장된다.
