---
title: "Java 학습 11일차: ProcessBuilder로 Python 연동하고 라벨링 준비하기"
slug: java-study-day11-java-python-interop-and-labeling-prep
date: 2026-01-21
author: Evan Yoon
category: study
subcategory: self-study
description: "1월 21일 Java 학습 기록. Java에서 Python 스크립트를 실행하고, 문장 데이터 라벨링 도구의 초안과 파일 구조를 준비한 과정을 정리했다."
tags:
  - java
  - study
  - processbuilder
  - python
  - labeling
readTime: 9
series: Java Study
seriesOrder: 11
featured: false
draft: false
toc: true
---

## Java 안에서 다른 프로그램을 실행해 본 날
`reference/javastudy/day0121/src` 폴더는 날짜상으로 1월 21일과 1월 22일 작업이 섞여 있는데, 수정 시각을 기준으로 보면 1월 21일에는 `study01`, `study02` 초안, 그리고 이후 감정 분석에 사용할 Python 실행 구조의 바탕이 먼저 만들어졌다.

## ProcessBuilder 실습: Java에서 Python 실행
`study01/MainApp.java`는 아주 직관적인 예제다. Java 콘솔에서 단수를 입력받고, `ProcessBuilder`로 `gugudan.py`를 실행한 뒤 표준 출력 결과를 `BufferedReader`로 읽어 다시 Java 콘솔에 출력한다.

```java
ProcessBuilder pb = new ProcessBuilder("python", "src\\study01\\gugudan.py", n);
Process process = pb.start();
BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
```

이 예제 하나로 배운 점이 많다.

- Java가 외부 프로세스를 실행할 수 있다는 점
- 프로세스 표준 출력을 Java에서 읽을 수 있다는 점
- `waitFor()`로 종료 시점과 종료 코드를 확인할 수 있다는 점

이후 감정 분석 모델을 JavaFX에서 호출할 때도 결국 같은 방식이 반복된다. 즉, 이날 실습은 단순 구구단 호출이 아니라 다음 단계의 준비였다.

## 라벨링 도구 초안 만들기
`study02/MainApp.java`와 `study02/FileManager.java`를 보면, 이 날은 문장 데이터셋을 읽고 정리하는 초안 작업도 함께 진행됐다. `data.txt` 파일을 한 줄씩 읽고, `]` 문자 기준으로 필요한 문장 본문만 잘라 출력한다.

```java
String str = originalFm.getAllData();
String[] subStr = str.split("\\n");
for (int i = 0; i < subStr.length; i++) {
    String[] subSubStr = subStr[i].split("]");
    ...
}
```

즉, 이 단계에서는 아직 완성된 JavaFX 라벨링 UI라기보다 다음을 준비하는 데이터 정리 단계에 가깝다.

- 원본 문장에서 실제 라벨링할 텍스트만 추출
- 파일 읽기/쓰기 유틸 재사용
- 나중에 UI에서 순차 표시할 수 있는 형태로 변환

## 구조를 미리 나눠 둔 점이 좋았다
1월 21일 작업은 기능적으로는 아직 초안이지만, 역할 분리가 명확하다.

- `study01`: Java-Python 통신 실험
- `study02`: 라벨링용 원본 데이터 정리
- `FileManager`: 입출력 공통 모듈

이렇게 나눠 두면 다음 날 JavaFX UI가 붙고, 저장 파일이 생기고, Python 모델이 연결돼도 각 부분을 독립적으로 수정할 수 있다.

## 오늘의 정리
1월 21일은 눈에 띄는 완성 앱보다 "다음 단계로 가기 위한 연결부"를 만든 날이었다. Java가 Python을 실행할 수 있다는 걸 확인했고, 라벨링 데이터셋을 읽고 전처리하는 흐름도 마련했다.

겉보기엔 소박한 실습처럼 보여도, 이 연결 실험이 있었기 때문에 다음 날에는 JavaFX 화면에서 Python 감정 분석 모델까지 호출하는 구조로 자연스럽게 확장될 수 있었다.
