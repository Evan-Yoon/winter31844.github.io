---
title: "Java 학습 10일차: JavaFX 메모장, 이미지 뷰어, GUI 주소록"
slug: java-study-day10-javafx-addressbook-and-memo-app
date: 2026-01-20
author: Evan Yoon
category: study
subcategory: self-study
description: "1월 20일 Java 학습 기록. JavaFX로 이미지 전환, 메모 저장, 다중 창 주소록 CRUD를 구현하며 콘솔 앱을 GUI 앱으로 확장했다."
tags:
  - java
  - study
  - javafx
  - address-book
  - gui
readTime: 10
series: Java Study
seriesOrder: 10
featured: false
draft: false
toc: true
---

## 어제 배운 JavaFX를 실제 앱으로 확장한 날
`reference/javastudy/day0120/src`는 전날의 JavaFX 기초 예제를 작은 프로그램들로 확장한 결과물이다. `study01`, `study02`, `addressBook` 세 묶음이 각기 다른 성격의 앱을 보여 준다.

## 이미지 뷰어: 이벤트에 따라 화면 바꾸기
`study01/rootController.java`는 `ImageView` 하나를 두고, 판다/호랑이/곰 버튼을 눌렀을 때 이미지가 바뀌게 구현돼 있다.

핵심은 `getResourceAsStream("/image/panda.jpg")` 형태로 리소스를 읽어 와서 `ivAnimal.setImage(img)`로 반영하는 흐름이다. 이 예제 하나로 다음 개념을 익힐 수 있다.

- 이미지 리소스 경로 관리
- `@FXML`로 `ImageView` 연결
- 버튼 이벤트별 로직 분기
- 화면 상태 갱신

## 메모장: 파일과 UI를 연결하기
`study02`는 더 실용적이다. `memo.txt` 내용을 앱이 시작될 때 읽어 오고, 사용자가 수정한 내용을 저장하거나 지울 수 있다.

`rootController.java`를 보면 `initialize()`에서 파일 내용을 `TextArea`에 먼저 채우고, 저장 버튼을 누르면 `FileManager`를 통해 파일에 다시 기록한다. 즉 콘솔 시절에 만들었던 파일 입출력을 JavaFX UI와 연결한 것이다.

이 구조가 중요한 이유는 GUI 프로그램도 결국 내부 로직은 크게 다르지 않다는 점을 보여 주기 때문이다. 입력 위치가 키보드/콘솔에서 `TextArea`로 바뀌었을 뿐, 읽고 쓰고 갱신하는 본질은 그대로다.

## GUI 주소록: 창이 여러 개인 프로그램
이 날의 핵심은 역시 `addressBook` 패키지였다. `root.fxml`, `create.fxml`, `update.fxml`, `delete.fxml`가 각각 따로 있고, `rootController.java`는 버튼 클릭에 따라 새 `Stage`를 열어 입력/수정/삭제 화면을 띄운다.

```java
private void openCreateStage() throws Exception {
    Parent create = FXMLLoader.load(getClass().getResource("create.fxml"));
    Scene scene = new Scene(create);
    createStage = new Stage();
    createStage.setScene(scene);
    createStage.show();
}
```

즉, 이제는 단일 화면이 아니라 기능별 창을 나눠 다루는 수준까지 올라온 것이다.

## 백그라운드 갱신도 시도했다
`rootController.java`의 `initialize()`에서는 `ShowDataThread`를 데몬 스레드로 실행해 `TextArea` 내용을 갱신하는 구조를 사용하고 있다. 아직 학습 단계라 완전히 정교한 구조는 아니지만, "UI 앱에서도 백그라운드 작업이 필요하다"는 점을 실습에 반영한 것이 좋았다.

또 `createController`, `updateController`, `deleteController`가 각각 입력 검증과 파일 저장 로직을 맡아 역할을 분리한 점도 눈에 띄었다. 규모가 작아도 컨트롤러를 나눠 두면 읽기와 수정이 훨씬 쉬워진다.

## 오늘의 정리
1월 20일은 JavaFX를 단순 예제 수준에서 벗어나 실제 CRUD 앱 형태로 끌고 간 날이었다. 이미지 리소스 처리, 메모 파일 저장, 다중 창 주소록, 백그라운드 표시 갱신까지 들어가면서 GUI 프로그램 구조를 한 단계 더 이해하게 됐다.

다음 날에는 여기서 한 걸음 더 나아가 Java와 Python을 연결하는 실험이 시작된다.
