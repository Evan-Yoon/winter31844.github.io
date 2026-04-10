---
title: "Java 학습 12일차: 데이터 라벨링 UI와 감정 분석 모델 연동"
slug: java-study-day12-data-labeling-ui-and-sentiment-analysis
date: 2026-01-22
author: Evan Yoon
category: study
subcategory: self-study
description: "1월 22일 Java 학습 기록. JavaFX 라벨링 UI를 만들고, Python으로 감정 분석 모델을 재학습한 뒤 Java에서 다시 호출하는 흐름을 정리했다."
tags:
  - java
  - study
  - javafx
  - machine-learning
  - sentiment-analysis
readTime: 11
series: Java Study
seriesOrder: 12
featured: false
draft: false
toc: true
---

## Java 학습 12일차에 작은 AI 프로젝트 형태가 완성됐다
`reference/javastudy/day0121/src` 안에서 1월 22일에 수정된 파일들을 보면, 이날은 단순 Java 문법 학습을 넘어 JavaFX UI, 데이터 라벨링, Python 모델 재학습, Java-Python 연동이 하나의 흐름으로 이어진 날이었다.

## 1. JavaFX 데이터 라벨링 앱 완성
`study02/rootController.java`, `root.fxml`, `application.css`, `labeled_data.txt`, `README.txt`가 모두 1월 22일에 정리됐다. 코드 흐름을 보면 앱이 시작될 때 `data.txt`를 읽고, 문장 본문만 추출해서 배열에 담은 뒤, 한 문장씩 화면에 보여 주고 라벨 버튼을 누를 때마다 결과를 파일에 저장한다.

핵심 구조는 이렇다.

- `initialize()`: 원본 파일 로드, 표시 가능한 문장만 추출, 첫 문장 표시
- `onActionLikeBtn()`: 긍정 라벨 저장
- `onActionNeutralBtn()`: 중립 버튼 핸들러 추가
- `onActionDislikeBtn()`: 부정 라벨 저장
- `lblPg`: 현재 진행 위치 표시

학습용 예제라고 해도 실제 데이터 작업 도구의 골격은 충분히 갖춰져 있다. `README.txt`에도 문장 단위 라벨링, 즉시 파일 저장, 진행 상황 표시, Gemini로 개선한 카드형 UI/CSS가 정리돼 있었다.

## 2. 데이터 부족 문제를 직접 해결
이 날의 진짜 핵심은 `study03` 패키지였다. `README.txt`를 읽어 보면 기존 `sentiment_10000_ko.csv`만으로 학습한 모델이 특정 표현을 잘못 분류하는 문제가 있었고, 이를 해결하기 위해 데이터를 직접 더 만들었다고 적혀 있다.

그 작업물이 바로 `make9999Sentence.py`다. 이 스크립트는 긍정/부정 문장을 조합해서 9,999개의 추가 문장을 생성하고 `generated_sentiment_9999.csv`로 저장한다. 원본 데이터와 합쳐서 `sentiment_20000_ko.csv` 규모로 확장한 뒤 재학습한 구조다.

즉, 이 날은 단순히 모델을 "돌려 본" 수준이 아니라, 성능 문제를 발견하고 데이터 증강으로 대응한 날이었다.

## 3. Python 모델을 Java에서 다시 호출
재학습 결과물은 `model.pkl`, `vectorizer.pkl`로 저장돼 있고, `emotionModelRunner.py`가 이 파일들을 읽어 표준 입력 문장을 예측한다.

```python
model = joblib.load(MODEL_PATH)
vectorizer = joblib.load(VECTORIZER_PATH)
x = vectorizer.transform([text])
pred = model.predict(x)[0]
print(int(pred))
```

그리고 `study03/rootController.java`는 JavaFX 화면에서 사용자가 입력한 문장을 `ProcessBuilder("python", "src/study03/emotionModelRunner.py")`로 넘긴 뒤, 파이썬 결과를 읽어서 라벨로 표시한다. 즉 전체 흐름이 이렇게 완성된다.

1. JavaFX에서 문장 입력
2. Java가 Python 스크립트 실행
3. Python이 `pkl` 모델 로드 후 예측
4. 결과를 Java로 반환
5. JavaFX 라벨에 결과 표시

입문 기간의 마지막 날에 이 정도 흐름까지 만들었다는 점이 꽤 인상적이다.

## 4. 왜 이 날이 중요했나
1월 22일 실습은 앞선 모든 학습 내용을 한데 묶는다.

- Java 기본 문법
- 클래스와 파일 입출력
- JavaFX UI
- 이벤트 처리
- 외부 프로세스 실행
- Python 머신러닝 모델 연동

즉, 하루짜리 작은 캡스톤처럼 작동한다. 특히 UI와 모델이 분리돼 있어서, 나중에 Python 모델만 바꾸거나 JavaFX 화면만 개선하기도 쉽다.

## 마지막 정리
1월 5일부터 1월 22일까지 이어진 Java 학습 12일은 단순 문법 암기가 아니라, 콘솔 프로그램에서 GUI 앱으로, 그리고 GUI 앱에서 Python AI 모델 연동으로 점차 확장되는 흐름이었다.

그 마지막 날인 1월 22일은 그 흐름이 가장 선명하게 보이는 날이었다. Java만 잘 쓰는 것이 아니라, 필요한 도구를 연결해서 실제로 동작하는 작은 시스템을 만드는 방향으로 학습이 정리됐기 때문이다.
