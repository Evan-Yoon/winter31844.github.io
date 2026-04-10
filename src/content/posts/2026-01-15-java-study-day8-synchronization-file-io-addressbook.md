---
title: "Java 학습 8일차: 동기화, 파일 입출력, 주소록 저장 기능"
slug: java-study-day8-synchronization-file-io-addressbook
date: 2026-01-15
author: Evan Yoon
category: study
subcategory: self-study
description: "1월 15일 Java 학습 기록. synchronized, wait/notify, 데몬 스레드, ExecutorService와 바이트/문자 스트림, 파일 기반 주소록 CRUD를 실습했다."
tags:
  - java
  - study
  - file-io
  - thread
  - crud
readTime: 10
series: Java Study
seriesOrder: 8
featured: false
draft: false
toc: true
---

## 배운 내용을 실제 저장 기능으로 연결한 날
`reference/javastudy/day0115/src`는 전날 배운 스레드 개념과 파일 입출력이 실제 프로그램 형태로 묶이는 날이었다. 특히 `addressBook` 패키지는 더 이상 일회성 콘솔 출력이 아니라, 파일에 데이터를 저장하고 수정하고 삭제하는 구조를 갖추고 있다.

## 스레드: 동시에 실행되는 코드를 제어하기
`javaStudy` 폴더의 전반부는 스레드 실습이다.

- `Study01.java`: 하나의 `Calculator`를 두 스레드가 공유
- `Study02.java`: `WorkObject`, `ThreadA`, `ThreadB`로 `wait/notify`
- `Study03.java`, `Study04.java`, `Study05.java`: 종료 제어와 인터럽트
- `Study06.java`: 데몬 스레드
- `Study07.java`, `Study08.java`: `ExecutorService`

`WorkObject.java`는 특히 의미가 있었다. `methodA()`와 `methodB()`를 `synchronized`로 묶고, 내부에서 `notify()`와 `wait()`를 사용해 두 스레드가 번갈아 실행되게 만든 구조다. 책으로만 보면 추상적인 개념인데, 실제 코드로 보면 "아, 공유 자원을 동시에 만질 때 이런 제어가 필요하구나"가 훨씬 선명해진다.

`AutoSaveThread.java`는 `setDaemon(true)`와 함께 사용된다. 메인 작업이 끝나면 자동 저장 스레드도 함께 종료된다는 점까지 익힌 셈이다.

## 파일 입출력: 바이트 스트림과 문자 스트림
후반부의 `Study09.java`부터 `Study16.java`는 파일 입출력 집중 훈련이다.

- `FileOutputStream`으로 바이트 쓰기
- `FileInputStream`으로 바이트 읽기
- 이미지 파일 복사
- `FileWriter`와 `FileReader`로 문자 저장

`Study14.java`에서는 `Cat.jpg`를 읽어서 `Cat2.jpg`로 복사하고 있다. 즉 텍스트가 아닌 바이너리 파일도 스트림으로 처리할 수 있다는 점을 실습한 것이다. 이 구간을 직접 해 보면 "파일은 전부 바이트의 흐름"이라는 말이 실제로 이해된다.

## 주소록 CRUD를 파일로 구현
가장 실용적인 부분은 `addressBook` 패키지였다.

- `Model.java`: 순번, 이름, 전화번호, 성별을 가지는 데이터 모델
- `FileManager.java`: 저장, 전체 조회, 삭제, 수정
- `MainApp.java`: 콘솔 메뉴 기반 실행
- `Data.txt`: 실제 저장 파일

`FileManager.java`를 보면 새 데이터는 append로 파일 뒤에 붙이고, 삭제와 수정은 전체 파일을 읽어서 새 문자열을 다시 만드는 방식으로 처리한다. 아직 DB는 아니지만 CRUD의 본질은 그대로 담겨 있다.

```java
public void updateData(int updateNum, String updatePhoneNum) throws IOException {
    String newData = "";
    String data = getAllData();
    String[] subData = data.split("\\n");
    ...
}
```

이 방식은 비효율적일 수 있지만, 입문 단계에서는 오히려 데이터 구조를 더 잘 이해하게 해 준다. 파일 전체를 읽고, 줄 단위로 나누고, 수정 대상만 바꿔 다시 저장하는 흐름이 눈에 보이기 때문이다.

## 오늘의 정리
1월 15일은 "자바로 실제 프로그램을 만들 수 있겠다"는 감각이 가장 강하게 온 날이었다. 스레드로 동시성의 기초를 배우고, 파일 입출력으로 데이터를 남기고, 그 둘을 바탕으로 주소록 저장 기능까지 구현했기 때문이다.

이후 JavaFX를 배우면서 이 콘솔 주소록은 자연스럽게 GUI 주소록으로 확장된다.
