---
title: "Java 학습 9일차: JavaFX 시작과 계산기 UI"
slug: java-study-day9-javafx-basics-and-calculator-ui
date: 2026-01-19
author: Evan Yoon
category: study
subcategory: self-study
description: "1월 19일 Java 학습 기록. JavaFX 애플리케이션 생명주기, FXML, 컨트롤러 연결, 이벤트 처리, 계산기 UI 구현까지 정리했다."
tags:
  - java
  - study
  - javafx
  - fxml
  - ui
readTime: 9
series: Java Study
seriesOrder: 9
featured: false
draft: false
toc: true
---

## 콘솔을 넘어 화면이 있는 프로그램으로
`reference/javastudy/day0119/src`는 분위기가 완전히 달라진다. 이제 `Application`을 상속하고, `Stage`, `Scene`, `FXMLLoader`가 등장한다. 드디어 JavaFX를 시작한 날이다.

## JavaFX 생명주기부터 익혔다
`study01/AppMain.java`는 JavaFX 입문용으로 아주 좋은 코드였다. `constructor`, `init()`, `start()`, `stop()`이 각각 어느 시점에 호출되는지 로그로 확인하게 만든 구조다. GUI 프레임워크는 메인 메서드만 아는 상태로 들어가면 헷갈리기 쉬운데, 이 파일은 실행 흐름을 눈으로 보게 해 준다.

즉, JavaFX 앱은 단순히 `main()` 안에서 모든 걸 처리하는 방식이 아니라, 프레임워크가 정해 둔 생명주기에 맞춰 동작한다는 걸 배운 날이었다.

## FXML과 컨트롤러 연결
`hellofx` 패키지와 `study03`, `study04`, `study05`, `study06`을 보면 JavaFX의 기본 구성이 반복된다.

- `AppMain.java` 또는 `Main.java`: FXML을 로드해서 창을 띄움
- `root.fxml`: 화면 레이아웃 정의
- `rootController.java`: 버튼 클릭, 입력값 처리, 화면 갱신

`hellofx/Controller.java`는 `@FXML Label label;`에 Java와 JavaFX 버전을 출력하는 가장 기본적인 예제다. 단순하지만 FXML과 컨트롤러가 제대로 연결됐는지 확인하기에는 가장 좋은 첫 단계다.

## 레이아웃을 코드와 FXML 두 방식으로 경험
`study02/AppMain.java`는 `VBox`, `Label`, `Button` 등을 자바 코드로 직접 만드는 예제이고, 나머지 예제는 FXML을 사용하는 방식이다. 둘 다 경험해 본 점이 좋았다.

- 코드 기반 UI는 구조를 명시적으로 이해하기 쉽다.
- FXML 기반 UI는 화면과 로직을 분리하기 쉽다.

입문 단계에서는 두 방식을 다 겪어 봐야 왜 FXML이 편한지, 또 언제 코드 기반 구성이 필요한지 감이 생긴다.

## 계산기 구현이 특히 인상적이었다
`study06/rootController.java`는 이 날 실습의 하이라이트였다. 숫자 버튼과 연산자 버튼을 눌렀을 때 `TextField`에 문자열을 쌓고, `=` 버튼을 누르면 `exp4j`의 `ExpressionBuilder`로 식을 계산한다.

```java
Expression e = new ExpressionBuilder(expressionStr).build();
double result = e.evaluate();
```

또 `Backspace`를 일반 버튼이 아니라 `Icon`으로 처리하고, 클릭 이벤트를 따로 연결한 부분도 흥미로웠다. 단순 계산기처럼 보여도 실제로는 다음 요소가 모두 들어 있다.

- FXML 컴포넌트 바인딩
- 이벤트 핸들러 등록
- 외부 라이브러리 사용
- 예외 처리 시 `Error` 출력
- 정수/실수 결과 포맷 분기

## 오늘의 정리
1월 19일은 Java 문법 공부가 "데스크톱 앱 개발"로 확장된 날이었다. 콘솔에서 하던 입력과 출력이 이제 버튼, 텍스트필드, 라벨, 창으로 바뀌었다.

특히 계산기 예제는 단순한 UI를 넘어, 입력 문자열을 파싱해서 계산하고 결과를 다시 화면에 반영하는 전체 흐름을 경험하게 해 준 좋은 실습이었다.
