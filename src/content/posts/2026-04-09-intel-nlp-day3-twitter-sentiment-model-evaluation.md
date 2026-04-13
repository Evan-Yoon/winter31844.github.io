---
title: "[NLP] Day 3: Twitter 감정 분석 - 모델 구축과 평가"
slug: intel-nlp-day3-twitter-sentiment-model-evaluation
date: 2026-04-09
author: Evan Yoon
category: study
subcategory: bootcamp
description: "BOW와 TF-IDF 특성 추출 방식을 비교하고, 로지스틱 회귀 모델을 평가했다."
thumbnail: ""
tags:
  - NLP
  - 감정분석
  - Sentiment Analysis
  - 머신러닝
  - LogisticRegression
  - TF-IDF
  - Python
readTime: 11
series: "Intel NLP 과정"
seriesOrder: 3
featured: false
draft: false
toc: true
---

지금까지는 텍스트를 정제하고 시각화했다. 오늘은 정제된 텍스트를 숫자로 변환한 후, 실제 머신러닝 모델을 학습하고 평가하는 날이었다. BOW와 TF-IDF가 뭐가 다른지 이제 이해가 된다. 단순히 "더 좋다"는 게 아니라, 단어의 중요도를 어떻게 보느냐의 차이였다.

## 오늘 다룬 범위

1. **특성 추출**: BOW vs TF-IDF 비교
2. **데이터 분할**: 학습/테스트 세트 준비
3. **모델 학습**: 로지스틱 회귀 구축
4. **모델 평가**: 정확도, 정밀도, 재현율, F1 점수
5. **제출**: 테스트 데이터에 대한 최종 예측

---

## 핵심 개념 정리

### 1. 특성 추출의 딜레마: 어떤 방식을 쓸까?

어제까지 텍스트를 정제했다. 하지만 기계는 숫자만 이해한다. 정제된 텍스트를 어떻게 숫자로 바꿀까?

어제 배운 Bag of Words를 떠올렸는데, 선생님이 "TF-IDF가 더 좋을 수도 있다"고 했다. 차이를 이해하려면 먼저 BOW가 뭔지 확실히 알아야 했다.

#### 1-1. Bag of Words (BOW) — 단순 빈도 방식

```python
from sklearn.feature_extraction.text import CountVectorizer

# BOW 벡터라이저 생성
count_vect = CountVectorizer(
    analyzer='word',           # 단어 단위로 분석
    token_pattern=r'\w{1,}',   # 1글자 이상의 단어
    max_features=30000         # 상위 30,000개 단어만 사용
)

# 텍스트를 특성 행렬로 변환
train_bow = count_vect.fit_transform(merge['Clean_Tweets'])

print(f"BOW 행렬 크기: {train_bow.shape}")  # (49159, 30000)
```

결과는 49,159개의 문서 × 30,000개의 단어로 이루어진 행렬이다. 각 칸에는 그 문서에서 그 단어가 나타난 횟수가 들어간다.

```
문서 1: "I love python"
→ [0, 0, 1, 1, 0, 1, 0, ...] (python=1, love=1, i=1, 나머지=0)

문서 2: "I hate bugs"
→ [0, 1, 0, 0, 1, 1, 0, ...] (hate=1, bugs=1, i=1, 나머지=0)
```

**BOW의 장점**:

- 계산이 빠르다
- 이해하기 쉽다
- 대부분의 경우 작동한다

**BOW의 단점**:

- 모든 단어를 똑같이 취급한다
- "the", "is" 같은 흔한 단어도 높은 가중치를 받는다
- 드물지만 중요한 단어와 흔하지만 의미 없는 단어를 구분하지 않는다

#### 1-2. TF-IDF — 중요도를 고려한 방식

```python
from sklearn.feature_extraction.text import TfidfVectorizer

# TF-IDF 벡터라이저 생성
tfidf_vect = TfidfVectorizer(
    analyzer='word',
    token_pattern=r'\w{1,}',
    max_features=1000,           # BOW보다 적은 단어 (1000개)
    stop_words='english'         # 영문 불용어 자동 제거
)

# 텍스트를 특성 행렬로 변환
train_tfidf = tfidf_vect.fit_transform(merge['Clean_Tweets'])

print(f"TF-IDF 행렬 크기: {train_tfidf.shape}")  # (49159, 1000)
```

**TF-IDF의 수식**:

```
TF-IDF = TF × IDF

TF (Term Frequency) = 문서 내 단어 빈도
                    = 해당 단어가 나타난 횟수 / 문서 총 단어 수

IDF (Inverse Document Frequency) = 전체 문서에서의 희소성
                                 = log(전체 문서 수 / 해당 단어를 포함한 문서 수)
```

예시로 이해해보자:

- **"love"가 "the"보다 높은 가중치를 받는 이유**:
  - "the"는 모든 문서에 나타나므로 IDF가 매우 낮다
  - "love"는 특정 문서(긍정 감정)에만 자주 나타나므로 IDF가 높다
  - 따라서 "love"의 TF-IDF가 훨씬 크다

**TF-IDF의 장점**:

- 단어의 중요도를 반영한다
- 흔한 단어의 영향력을 줄인다
- 불용어 제거가 자동으로 된다

**TF-IDF의 단점**:

- 계산이 BOW보다 느리다
- 하이퍼파라미터 조정이 더 필요하다

**비교**:

|             | BOW            | TF-IDF           |
| ----------- | -------------- | ---------------- |
| 특성 수     | 30,000         | 1,000            |
| 단어 가중치 | 빈도만 고려    | 빈도 + 희소성    |
| 불용어 처리 | 직접 제거 필요 | 자동 제거 가능   |
| 계산 속도   | 빠름           | 느림             |
| 일반적 성능 | 좋음           | 더 좋음 (대부분) |

### 2. 데이터 분할: 과적합을 피하기 위해

정제되고 특성이 추출된 데이터를 가지고 모델을 학습했다. 하지만 처음 배웠던 "검증"의 중요성을 기억해야 했다.

```python
from sklearn.model_selection import train_test_split

# BOW 데이터 분할
x_train_bow, x_test_bow, y_train_bow, y_test_bow = train_test_split(
    train_bow,                 # 특성 (X)
    merge['label'],            # 레이블 (y)
    test_size=0.3,             # 30%를 테스트 세트로
    random_state=42            # 재현 가능하도록 시드 고정
)

# TF-IDF 데이터 분할
x_train_tfidf, x_test_tfidf, y_train_tfidf, y_test_tfidf = train_test_split(
    train_tfidf,
    merge['label'],
    test_size=0.3,
    random_state=42
)

print(f"BOW 학습 세트: {x_train_bow.shape}")
print(f"BOW 테스트 세트: {x_test_bow.shape}")
# BOW 학습 세트: (34411, 30000)
# BOW 테스트 세트: (14748, 30000)
```

**왜 30%를 테스트로 나눌까?**

- 70%로 학습하고 30%로 검증한다
- 이렇게 하면 학습한 데이터에 과적합되지 않았는지 확인할 수 있다
- `random_state=42`는 재현 가능성을 위해 고정한다

### 3. 모델 학습: 로지스틱 회귀

이제 실제로 모델을 학습했다. 선택한 건 로지스틱 회귀였다.

```python
from sklearn.linear_model import LogisticRegression

# BOW 기반 로지스틱 회귀 모델
lr_bow = LogisticRegression(
    solver='liblinear',    # 최적화 알고리즘 (소규모 데이터에 좋음)
    max_iter=100           # 최대 반복 횟수
)

# 모델 학습
lr_bow.fit(x_train_bow, y_train_bow)

# TF-IDF 기반 로지스틱 회귀 모델
lr_tfidf = LogisticRegression(
    solver='liblinear',
    max_iter=100
)

lr_tfidf.fit(x_train_tfidf, y_train_tfidf)

print("모델 학습 완료!")
```

**로지스틱 회귀를 선택한 이유:**

1. **이진 분류에 최적화됨** (긍정 vs 부정)
2. **확률을 반환** (단순히 분류만 하는 게 아니라 신뢰도 제공)
3. **해석 가능성** (어떤 특성이 예측에 영향을 미쳤는지 알 수 있음)
4. **계산 효율** (수백만 개의 특성을 다룰 수 있음)

다른 선택지도 있었다 (나이브 베이즈, SVM, 신경망). 하지만 텍스트 분류에선 로지스틱 회귀가 "베이스라인"처럼 작동한다.

### 4. 모델 평가: 여러 각도에서 보기

모델을 학습했으니 이제 성능을 평가해야 했다. 여기서 배운 게 중요했다. **정확도만으로는 부족하다**.

```python
from sklearn import metrics

# 예측 수행
y_pred_bow = lr_bow.predict(x_test_bow)
y_pred_tfidf = lr_tfidf.predict(x_test_tfidf)

# 평가 지표 계산
```

#### 4-1. 정확도 (Accuracy)

```python
acc_bow = metrics.accuracy_score(y_test_bow, y_pred_bow)
acc_tfidf = metrics.accuracy_score(y_test_tfidf, y_pred_tfidf)

print(f"BOW 정확도: {acc_bow:.4f}")
print(f"TF-IDF 정확도: {acc_tfidf:.4f}")
```

**정확도**: 전체 예측 중 맞은 비율

```
정확도 = 맞은 예측 수 / 전체 예측 수
```

하지만 이건 불완전하다. 예를 들어, 부정 감정이 1%뿐인 데이터셋에서 모든 트윗을 "긍정"으로 분류하면 정확도가 99%가 된다. 하지만 모델은 아무것도 배우지 못한 것이다.

#### 4-2. 정밀도 (Precision)와 재현율 (Recall)

```python
precision_bow = metrics.precision_score(y_test_bow, y_pred_bow)
recall_bow = metrics.recall_score(y_test_bow, y_pred_bow)

precision_tfidf = metrics.precision_score(y_test_tfidf, y_pred_tfidf)
recall_tfidf = metrics.recall_score(y_test_tfidf, y_pred_tfidf)

print(f"정밀도 (BOW): {precision_bow:.4f}")
print(f"재현율 (BOW): {recall_bow:.4f}")
print(f"정밀도 (TF-IDF): {precision_tfidf:.4f}")
print(f"재현율 (TF-IDF): {recall_tfidf:.4f}")
```

**정밀도 (Precision)**: "긍정이라고 예측한 것 중 정말 긍정인 비율"

```
정밀도 = 참 긍정 / (참 긍정 + 거짓 긍정)
```

**언제 중요한가?** 스팸 이메일 필터링. 일반 메일을 스팸으로 잘못 분류(거짓 긍정)하면 중요한 메일을 놓친다.

**재현율 (Recall)**: "실제 긍정 중 모델이 맞춘 비율"

```
재현율 = 참 긍정 / (참 긍정 + 거짓 부정)
```

**언제 중요한가?** 의료 진단. 병을 놓치는 거짓 부정이 위험하다.

#### 4-3. F1 점수

정밀도와 재현율의 조화평균이다.

```python
f1_bow = metrics.f1_score(y_test_bow, y_pred_bow)
f1_tfidf = metrics.f1_score(y_test_tfidf, y_pred_tfidf)

print(f"F1 점수 (BOW): {f1_bow:.4f}")
print(f"F1 점수 (TF-IDF): {f1_tfidf:.4f}")
```

```
F1 = 2 × (정밀도 × 재현율) / (정밀도 + 재현율)
```

정밀도와 재현율이 둘 다 높으면 F1도 높다. 하나만 높으면 F1은 낮다.

#### 4-4. ROC-AUC 점수

```python
# 확률 예측 (단순 분류가 아니라)
y_pred_proba_bow = lr_bow.predict_proba(x_test_bow)[:, 1]
y_pred_proba_tfidf = lr_tfidf.predict_proba(x_test_tfidf)[:, 1]

# ROC-AUC 계산
roc_auc_bow = metrics.roc_auc_score(y_test_bow, y_pred_proba_bow)
roc_auc_tfidf = metrics.roc_auc_score(y_test_tfidf, y_pred_proba_tfidf)

print(f"ROC-AUC (BOW): {roc_auc_bow:.4f}")
print(f"ROC-AUC (TF-IDF): {roc_auc_tfidf:.4f}")
```

**ROC-AUC**: 0 ~ 1 사이의 값. 1에 가까울수록 좋다.

- 0.5: 동전 던지기 수준 (아무 성능 없음)
- 0.7~0.8: 좋음
- 0.8~0.9: 매우 좋음
- 0.9~1.0: 뛰어남

### 5. 결과 저장: 최종 제출

가장 성능이 좋은 모델(아마 TF-IDF)을 선택해서 테스트 데이터에 대한 최종 예측을 했다.

```python
# 원본 테스트 데이터 로드
test_original = pd.read_csv('test.csv')

# 테스트 데이터를 TF-IDF로 변환 (fit_transform 아니라 transform!)
test_tfidf = tfidf_vect.transform(test_original['tweet'])

# 예측
y_pred_final = lr_tfidf.predict(test_tfidf)

# 결과 저장
submission = pd.DataFrame({
    'id': test_original['id'],
    'label': y_pred_final
})

submission.to_csv('submission.csv', index=False)
print(f"제출 파일 저장 완료! ({len(submission)} 개 예측)")
```

**주의사항**: 테스트 데이터는 `fit_transform()`이 아니라 `transform()`을 써야 한다. 왜냐하면 학습 데이터로 이미 "맞춰진" 변환기를 써야 하기 때문이다.

---

## 헷갈렸던 점 / 실수 포인트

### 1. fit_transform vs transform

```python
# 학습 데이터
vectorizer = TfidfVectorizer()
X_train = vectorizer.fit_transform(train_text)  # fit + transform

# 테스트 데이터
X_test = vectorizer.transform(test_text)        # transform만!
```

`fit()`을 테스트 데이터에도 하면, 테스트 데이터의 단어 분포를 학습 데이터에 "섞이게" 된다. 이는 데이터 누수다.

### 2. 로지스틱 회귀는 선형 분류기

로지스틱 회귀 이름에 "회귀"가 있어서 혼동했다. 하지만 이건 이진 분류 문제에 최적화된 분류기다. 내부적으로는:

```
확률 = 1 / (1 + e^(-z))  # 시그모이드 함수
예측 = 확률 > 0.5 ? 1 : 0
```

### 3. 정밀도와 재현율의 트레이드오프

모델이 예측을 더 "보수적"으로 하면 정밀도는 올라가지만 재현율은 내려간다. 반대도 마찬가지다. 비즈니스 요구에 따라 선택해야 한다.

### 4. max_features 선택의 중요성

BOW에서 30,000개, TF-IDF에서 1,000개를 선택했다. 더 많으면 더 정보가 풍부하지만:

- 계산이 느려진다
- 과적합 위험이 증가한다
- 메모리를 더 써야 한다

---

## 복습 Q&A

<details>
<summary><strong>1. BOW와 TF-IDF 중 무조건 TF-IDF가 좋은가?</strong></summary>

아니다. 상황에 따라 다르다:

- **TF-IDF가 나을 때**: 단어의 중요도 차이가 큰 분야 (감정분석, 주제 분류)
- **BOW가 충분할 때**: 이미 전처리된 깔끔한 텍스트, 빠른 처리 필요

실제론 둘 다 시도해보고 비교하는 게 정석이다.

</details>

<details>
<summary><strong>2. 정밀도 95%, 재현율 50%인 모델을 쓸까?</strong></summary>

상황에 따라 다르다:

- **암 진단**: 절대 안 된다. 50% 재현율은 절반의 환자를 놓친다.
- **스팸 필터**: 가능하다. 일부 스팸이 통과해도, 일반 메일을 스팸으로 분류할 위험이 적다.

트레이드오프를 이해하고 목적에 맞게 선택해야 한다.

</details>

<details>
<summary><strong>3. 테스트 정확도가 학습 정확도보다 높을 수 있나?</strong></summary>

드물지만 가능하다. 하지만 이는:

- 운이 좋아서 테스트 데이터가 쉬웠거나
- 데이터 샘플이 작아서 통계적 변동이 크거나
- 다른 버그가 있을 수 있다

보통은 과적합 때문에 테스트 정확도가 더 낮다.

</details>

<details>
<summary><strong>4. ROC-AUC 점수가 0.85면 좋은 건가?</strong></summary>

좋다! 기준은:

- 0.5 ~ 0.6: 거의 의미 없음
- 0.6 ~ 0.7: 약간의 예측력 있음
- 0.7 ~ 0.8: 좋음
- 0.8 ~ 0.9: 매우 좋음
- 0.9 이상: 뛰어남

  0.85는 "매우 좋음" 범주다.

</details>

---

## 한 줄 정리

정제된 텍스트를 BOW 또는 TF-IDF로 특성화한 후, 로지스틱 회귀로 분류하면, 정확도, 정밀도, 재현율, F1 점수, ROC-AUC 등 다양한 각도에서 모델을 평가할 수 있다.

이제 기초는 끝이다. 다음부터는 더 복잡한 모델들(신경망, 트랜스포머)을 배울 차례인 것 같다. 하지만 이 기초가 없으면 그것들도 이해할 수 없었을 것 같다.
