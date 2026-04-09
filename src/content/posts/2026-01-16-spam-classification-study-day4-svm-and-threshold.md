---
title: |
  [스팸 분류 스터디] [4회차]
  SVM과 임계값 조정으로 정상 메시지 지키기
slug: spam-classification-study-day4-svm-and-threshold
date: 2026-01-16
author: Evan Yoon
category: study
subcategory: self-study
description:
  2026년 1월 16일 스팸 분류 스터디 네 번째 기록. SVM과 N-gram을 적용하고, 정상 메시지를
  스팸으로 잘못 분류하지 않도록 safety threshold를 조정한 과정
thumbnail: /images/posts/spam-classification-study/confusion-matrix-zero-fp.png
tags:
  - nlp
  - spam-classification
  - svm
  - n-gram
  - threshold
  - confusion-matrix
  - study
readTime: 11
series: Spam Classification Study
seriesOrder: 4
featured: false
draft: false
toc: true
---

## 네 번째 기록에서는 기준을 바꿨다

2026년 1월 16일, 스팸 분류 스터디 네 번째 기록에서는 단순히 성능이 높은 모델을 찾는 것보다, 어떤 종류의 실수를 줄일 것인가에 더 집중했다. 앞선 기록들에서 데이터 정리, 시각화, 전처리, Bag of Words와 TF-IDF 기반 Naive Bayes 비교까지 해 보니 한 가지는 확실해졌다.

> 정확도가 높다고 해서 바로 좋은 분류기는 아니다.

스팸 분류에서는 두 종류의 실수가 있다.

- 스팸을 정상으로 놓치는 경우
- 정상 메시지를 스팸으로 잘못 보내는 경우

이 둘은 숫자상으로는 둘 다 오류지만, 실제 사용자 입장에서는 무게가 다르다. 정상 메시지가 스팸함으로 들어가 버리면 사용자는 중요한 연락을 놓칠 수 있다. 반대로 스팸 하나가 받은 편지함에 남는 것은 불편하지만 복구가 더 쉽다.

그래서 네 번째 기록에서는 모델을 바라보는 기준 자체를 바꿨다. 이번에는 "정상 메시지를 스팸으로 잘못 분류하지 않게 막는 것"에 더 무게를 뒀다.

## 왜 SVM을 다시 보게 됐는가

이전 회차에서 Naive Bayes로 baseline을 만들면서, 단순한 벡터화만으로도 성능이 꽤 잘 나온다는 것을 확인했다. 하지만 그 과정에서 동시에 느낀 것도 있었다.

- 단어 빈도 자체는 중요하다
- 그런데 더 정교하게 경계를 잡을 방법도 필요하다
- 특히 spam/ham 사이의 애매한 경계에서 기준을 조정할 수 있어야 한다

이런 이유로 SVM을 시도하게 됐다. SVM은 단순히 클래스만 예측하는 것이 아니라, decision score를 통해 현재 샘플이 어느 쪽 경계에 더 가까운지 수치로 볼 수 있다. 그리고 이 점수가 이번 실험에서는 아주 중요했다.

## 이번 회차에서는 N-gram도 같이 적용했다

이번에는 단어 하나만 보는 unigram보다, 두 단어 묶음까지 함께 보는 방식이 더 적합할 수 있다고 판단했다. 스팸 메시지는 특정 단어 하나보다 짧은 표현 묶음에서 더 강한 패턴이 나올 가능성이 높기 때문이다.

예를 들어 이런 식이다.

- `free entry`
- `claim prize`
- `reply stop`
- `call now`

이런 표현은 단어 하나씩 볼 때보다 두 단어를 같이 볼 때 더 스팸답게 보인다. 그래서 벡터화 단계에서 `ngram_range=(1, 2)`를 사용했다.

```python
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.svm import LinearSVC

vectorizer = CountVectorizer(ngram_range=(1, 2))
X_train_vec = vectorizer.fit_transform(X_train)
X_test_vec = vectorizer.transform(X_test)

svm_model = LinearSVC(random_state=42, dual='auto')
svm_model.fit(X_train_vec, y_train)
```

여기서 중요한 점은 이번 회차가 단순한 "모델 교체"가 아니라는 것이다. SVM을 쓴 이유는 decision score를 활용해 분류 기준을 직접 조정해 보기 위해서였다.

## 기본 분류 기준엔 한계가 있었다

SVM은 기본적으로 경계선 기준으로 양쪽 클래스를 나눈다. 하지만 이번 기록의 목적은 단순한 기본 예측이 아니었다. 내가 더 중요하게 본 것은 이 질문이었다.

> 어디까지를 스팸이라고 확신할 수 있는가

즉 "조금 스팸 같아 보이는 메시지"까지 잡아내려 하기보다, "정말 스팸이라고 확신할 수 있는 메시지"만 스팸으로 보내도록 기준을 보수적으로 잡고 싶었다.

이 생각은 실제 서비스 기준으로도 자연스럽다. 스팸을 너무 공격적으로 잡아내려 하면 정상 메시지까지 같이 걸릴 수 있기 때문이다.

## decision score를 직접 보기 시작했다

SVM을 학습한 뒤에는 `decision_function()`으로 각 테스트 메시지의 점수를 확인했다.

```python
y_scores = svm_model.decision_function(X_test_vec)
```

이 점수는 단순한 확률은 아니지만, 경계선으로부터 얼마나 떨어져 있는지를 보여 주는 값으로 볼 수 있다.

- 점수가 낮으면 정상 메시지 쪽
- 점수가 높으면 스팸 메시지 쪽

중요한 것은 0을 기준으로 딱 잘라 쓰는 것만이 유일한 방법은 아니라는 점이었다. 실제로 이번 회차에서는 그 기준을 일부러 더 엄격하게 바꿨다.

## 안전 임계값을 직접 정했다

이번 실험에서 실제로 중요한 부분은 아래 코드였다.

```python
max_ham_score = y_scores[y_test == 0].max()
safety_threshold = max_ham_score + 0.001
y_pred_safe = (y_scores > safety_threshold).astype(int)
```

이게 의미하는 바는 꽤 분명하다.

1. 테스트 데이터 중 실제 정상 메시지들의 score를 본다
2. 그중 가장 높은 score를 찾는다
3. 그 값보다 아주 조금 더 높은 선을 새 임계값으로 둔다

즉 정상 메시지가 도달했던 최고 점수보다 더 높은 점수를 받은 경우에만 스팸으로 인정하겠다는 뜻이다.

이 기준을 세운 이유는 간단했다.

> 정상 메시지가 스팸 영역으로 들어가지 않도록 하자.

이번 기록에서 내가 가장 중요하게 본 건 accuracy가 아니라 false positive였다.

## 점수 분포를 직접 보면서 임계값을 왜 조정해야 하는지 더 분명해졌다

점수 분포를 시각화해 보면, 정상 메시지와 스팸 메시지 점수가 완전히 깔끔하게 분리되는 것은 아니라는 점이 보인다. 일부 구간은 겹칠 수밖에 없다.

<img src="/images/posts/spam-classification-study/svm-score-distribution.png" alt="SVM decision score 분포와 safety threshold 시각화" style="display:block; width:100%; max-width:980px; margin:1rem auto; border-radius:16px;" />

이 그림을 보면 초록색 기준선, 즉 safety threshold를 어디에 두느냐에 따라 모델의 성격이 달라진다.

- 기준선을 왼쪽에 두면 더 많은 스팸을 잡을 수 있다
- 대신 정상 메시지가 스팸으로 잘못 들어갈 가능성도 커진다
- 기준선을 오른쪽으로 밀면 정상 메시지는 더 안전해진다
- 대신 일부 스팸을 놓치게 된다

즉 이번 회차는 "성능을 높이는" 작업이라기보다, "실수의 방향을 선택하는" 작업에 가까웠다.

## 이번 회차에서 가장 중요했던 혼동행렬

임계값을 조정한 뒤 가장 먼저 확인한 것은 혼동행렬이었다. 그리고 이 시각화가 이번 글의 핵심 결과라고 봐도 된다.

<img src="/images/posts/spam-classification-study/confusion-matrix-zero-fp.png" alt="False Positive를 0으로 맞춘 SVM 혼동행렬" style="display:block; width:100%; max-width:760px; margin:1rem auto; border-radius:16px;" />

이 결과를 숫자로 풀면 다음과 같았다.

- 실제 정상 메시지 903개 중 스팸으로 잘못 분류된 개수: `0`
- 실제 스팸 131개 중 정상으로 놓친 개수: `16`
- 실제 스팸으로 맞춘 개수: `115`

즉 이번 기준에서는 정상 메시지를 하나도 잃지 않는 대신, 일부 스팸은 놓치는 방향을 선택한 것이다.

이 결과는 아래 이미지가 말하는 메시지와 정확히 연결된다.

<img src="/images/posts/spam-classification-study/zero-false-positive.png" alt="정상 메일을 스팸으로 분류하지 않는 것이 중요하다는 시각 자료" style="display:block; width:100%; max-width:760px; margin:1rem auto; border-radius:16px;" />

## Accuracy만 보면 오히려 놓치기 쉬운 점

이번 회차에서 다시 확인한 것은, Accuracy가 높다고 해서 사용 경험이 좋은 모델은 아니라는 점이었다.

예를 들어 다음 두 모델이 있다고 가정해 보자.

- 모델 A: 스팸을 많이 잡지만 정상 메시지도 종종 잘못 막는다
- 모델 B: 스팸 일부를 놓치지만 정상 메시지는 거의 안전하다

숫자만 보면 모델 A가 더 좋아 보일 수도 있다. 하지만 메일 서비스나 문자 서비스 관점에서는 모델 B가 더 신뢰할 수 있을 가능성이 크다.

그래서 이번에는 Accuracy 외에도 아래 항목을 같이 봤다.

- False Positive
- F1-score
- Confusion Matrix
- decision score 분포

실제로 이번 실험에서는 Accuracy보다 "정상 메시지를 903개 모두 지켰다"는 사실이 훨씬 더 중요하게 느껴졌다.

## 이 회차를 통해 threshold tuning의 의미를 제대로 알게 됐다

이전까지는 threshold tuning이라는 말을 들으면 막연히 "분류 기준을 바꾸는 것" 정도로만 이해하고 있었다. 그런데 직접 점수를 보고 선을 옮기면서 보니 개념이 훨씬 선명해졌다.

threshold tuning이 중요한 이유는 이런 데 있었다.

- 모델은 완전히 정답/오답만 말하는 기계가 아니다
- 경계에 가까운 애매한 샘플이 항상 있다
- 서비스 목적에 따라 애매한 샘플을 어느 쪽으로 보낼지 선택해야 한다

즉 임계값 조정은 단순한 수학적 테크닉이 아니라, 서비스의 우선순위를 반영하는 작업이었다.

이번 회차에서는 우선순위를 이렇게 잡았다.

> 정상 메시지는 최대한 보호한다.

이 기준 하나 때문에 SVM 실험이 그냥 모델 비교를 넘어서 훨씬 실전적인 느낌으로 다가왔다.

## 직접 구현하면서 느낀 SVM의 장점

이번 회차에서 SVM을 써 보면서 느낀 장점은 아래와 같았다.

1. 짧은 텍스트 분류에 꽤 강하다
2. N-gram과 결합했을 때 표현 패턴을 잘 잡는다
3. decision score를 통해 기준을 조정하기 쉽다

특히 세 번째 이유가 컸다. Naive Bayes는 baseline으로 빠르고 강했지만, 이번처럼 "정상 메시지를 보호하는 방향"으로 세밀하게 조정해 보는 실험에서는 SVM 쪽이 더 직관적으로 다가왔다.

물론 trade-off도 있었다.

- 기준을 보수적으로 잡으면 recall이 떨어질 수 있다
- 즉 스팸 일부를 놓치는 것은 감수해야 한다

하지만 이번 회차의 목적에서는 이 trade-off가 오히려 합리적이었다.

## 이번 기록은 딥러닝 전 마지막 사고 정리였다

이 회차를 지나면서 다음 단계가 더 명확해졌다. 이제는 단순한 baseline을 넘어서 "서비스 기준"을 생각하는 분류기를 보기 시작했기 때문이다.

이 흐름은 이후 딥러닝 실험에도 그대로 이어진다.

- 단순히 accuracy 높은 모델을 찾는 것이 아니라
- false positive를 얼마나 줄일 수 있는지 보고
- precision을 높게 유지한 채 recall을 어디까지 끌어올릴지 고민하게 된다

즉 네 번째 기록은 전통적인 머신러닝 실험이면서도, 이후 BiLSTM 실험의 평가 기준을 미리 세워 준 회차였다.

## 마무리

2026년 1월 16일의 스팸 분류 스터디 네 번째 기록은, SVM과 N-gram을 적용해 본 것 자체보다도 "임계값을 어떻게 조정할 것인가"를 중심에 두고 실험했다는 점에서 의미가 컸다.

이번 회차에서 남은 건 아래 내용이었다.

- 모든 오류가 같은 무게를 갖는 것은 아니다
- 스팸 분류에서는 정상 메시지를 보호하는 쪽이 더 중요할 수 있다
- threshold tuning은 모델 성능이 아니라 서비스 우선순위를 반영하는 작업이다

다음 글은 2026년 1월 21일 기준으로, LSTM에서 시작해 Bidirectional LSTM으로 개선하고, 과적합과 precision/recall 균형까지 확인한 마지막 실험을 중심으로 정리할 생각이다.
