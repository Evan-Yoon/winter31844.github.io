---
title: "Java 학습 6일차: 다형성과 인터페이스 설계"
slug: java-study-day6-polymorphism-and-interfaces
date: 2026-01-13
author: Evan Yoon
category: study
subcategory: self-study
description: "1월 13일 Java 학습 기록. 추상화, 다형성, 인터페이스 구현과 상속, 기본 메서드와 정적 메서드까지 실습한 내용을 정리했다."
tags:
  - java
  - study
  - interface
  - polymorphism
  - oop
readTime: 9
series: Java Study
seriesOrder: 6
featured: false
draft: false
toc: true
---

## 오늘은 '무엇을 할 수 있는가' 중심으로 설계했다
`reference/javastudy/day0113/src`는 `javaStudy`와 `javaStudy2` 두 패키지로 나뉘어 있다. 전날이 상속 중심이었다면, 이날은 "객체가 어떤 역할을 수행하는가"를 기준으로 설계를 정리하는 날이었다. 즉 클래스보다 인터페이스와 다형성이 핵심이었다.

## 다형성 복습: 부모 타입으로 자식 다루기
`Study01.java`, `Study02.java`, `Study04.java`, `Study05.java`는 전형적인 다형성 예제를 담고 있었다.

- `Driver`가 `Bus`, `Taxi`를 같은 방식으로 운전
- `Person` 타입으로 `Student` 객체 참조
- `Animal` 타입으로 `Dog`, `Cat`를 받아 같은 메서드에서 다른 소리 출력
- `Person` 계층을 통해 `Employee`, `Manager`, `Director` 업무 처리

이 구조가 중요한 이유는 호출부가 구체 클래스에 덜 의존하게 되기 때문이다. `animalSound(Animal animal)`처럼 작성하면, 이후 `Cat`이든 `Dog`든 `Animal`을 상속하는 객체는 모두 같은 메서드에 넣을 수 있다.

## 인터페이스 실습이 본격화됐다
`RemoteControl`, `Searchable`, `Service`, `InterfaceA/B/C`, `Vehicle`, `Tire` 같은 인터페이스 파일이 많이 보인다. 각각의 예제가 다른 포인트를 보여 준다.

- `RemoteControl`: 상수와 추상 메서드, 기본 메서드
- `Service`: `default` 메서드와 `static` 메서드
- `InterfaceA/B/C`: 인터페이스 상속 구조
- `Vehicle`, `Tire`: 구현 객체 교체 가능한 구조

`SmartTelevision.java`는 `RemoteControl`과 `Searchable`을 함께 구현한다. 이 한 파일만으로도 "클래스는 단일 상속만 가능하지만, 인터페이스는 여러 개 구현할 수 있다"는 점을 바로 보여 준다.

## 교체 가능한 구조를 만드는 연습
`javaStudy2` 패키지의 `Car`, `Tire`, `HankookTire`, `KumhoTire`, `Driver` 구조는 특히 실용적이었다. 자동차는 타이어의 구체 타입을 몰라도 `roll()`만 호출하면 된다. 그래서 실제 장착 부품을 바꾸더라도 상위 로직은 바뀌지 않는다.

이런 식으로 작성해 두면 유지보수가 쉬워진다.

- 구현체를 바꿔도 호출부 수정이 적다.
- 테스트용 가짜 객체를 넣기도 쉽다.
- 기능이 늘어나도 구조가 덜 무너진다.

입문 단계에서 이런 구조를 여러 번 손으로 작성해 보는 건 꽤 큰 자산이다.

## 오늘의 정리
1월 13일은 "객체지향"을 조금 더 언어적으로 이해한 날이 아니라, 실제 설계 원리로 느끼기 시작한 날이었다. 상속만으로는 충분하지 않고, 역할을 분리해 인터페이스로 연결하면 훨씬 유연해진다는 점을 다양한 예제로 확인했다.

이후 예외 처리, 제네릭, 스레드로 넘어가도 이런 인터페이스 감각은 계속 남는다. 특히 UI나 파일 처리, 외부 프로그램 연동처럼 구현이 자주 바뀌는 영역에서 더 크게 빛난다.
