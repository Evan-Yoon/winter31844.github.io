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

Day 2에서 정제한 Twitter 텍스트를 이제 숫자 벡터로 바꾸고 분류 모델을 학습한다. BOW와 TF-IDF 두 가지 벡터화 방식이 성능에 어떤 차이를 만드는지, 그리고 정확도 하나만으로 모델을 평가하면 왜 부족한지를 배웠다.

## 1. 텍스트 벡터화: BOW vs TF-IDF

기계학습 모델은 숫자만 이해한다. 정제된 텍스트를 어떻게 숫자로 바꾸느냐에 따라 성능이 달라진다.

### Bag of Words (BOW)

**이게 뭔지**: 각 단어가 문서에 몇 번 등장했는지 세는 방식.

```python
from sklearn.feature_extraction.text import CountVectorizer

count_vect = CountVectorizer(
    analyzer='word',          # 단어 단위 분석
    token_pattern=r'\w{1,}',  # 1글자 이상만 포함
    max_features=30000        # 최빈도 상위 30,000개 단어만 사용
)

train_bow = count_vect.fit_transform(merge['Clean_Tweets'])
print(train_bow.shape)  # (49159, 30000) — 49,159 문서 × 30,000 단어
```

결과: 49,159개 트윗 × 30,000개 단어 행렬. 각 칸은 해당 트윗에서 그 단어가 나온 횟수.

**한계**: "the", "is" 같은 흔한 단어도 높은 값을 가진다. 중요한 단어와 흔한 단어를 구별하지 못한다.

### TF-IDF

**이게 뭔지**: 단어 빈도(TF)와 희소성(IDF)을 곱해 "이 문서에서 특별히 중요한 단어"에 높은 가중치를 부여하는 방식.

```
TF = 문서 내 단어 빈도 / 문서 총 단어 수
IDF = log(전체 문서 수 / 해당 단어를 포함한 문서 수)
TF-IDF = TF × IDF
```

"the"는 모든 문서에 등장 → IDF 낮음 → TF-IDF 낮음  
"love"는 긍정 트윗에 집중 등장 → IDF 높음 → TF-IDF 높음

```python
from sklearn.feature_extraction.text import TfidfVectorizer

tfidf_vect = TfidfVectorizer(
    analyzer='word',
    token_pattern=r'\w{1,}',
    max_features=1000,      # BOW보다 적은 단어 수로도 충분
    stop_words='english'    # 불용어 자동 제거
)

train_tfidf = tfidf_vect.fit_transform(merge['Clean_Tweets'])
print(train_tfidf.shape)  # (49159, 1000)
```

<div class="compare-wrap">
  <div class="compare-card compare-before">
    <div class="compare-label">BOW</div>
    <div class="compare-body">
      <p><strong>특성 수:</strong> 30,000</p>
      <p><strong>가중치 기준:</strong> 등장 횟수</p>
      <p><strong>흔한 단어 처리:</strong> 높은 가중치</p>
      <p><strong>불용어 자동 제거:</strong> 불가</p>
    </div>
  </div>
  <div class="compare-card compare-after">
    <div class="compare-label">TF-IDF</div>
    <div class="compare-body">
      <p><strong>특성 수:</strong> 1,000</p>
      <p><strong>가중치 기준:</strong> 빈도 × 희소성</p>
      <p><strong>흔한 단어 처리:</strong> 낮은 가중치</p>
      <p><strong>불용어 자동 제거:</strong> 가능</p>
    </div>
  </div>
</div>

## 2. 데이터 분할

**왜 필요한지**: 같은 데이터로 학습하고 평가하면 "외운" 것과 "이해한" 것을 구별할 수 없다. 보지 못한 데이터로 평가해야 실제 성능을 안다.

```python
from sklearn.model_selection import train_test_split

# BOW 데이터 분할
x_train_bow, x_test_bow, y_train_bow, y_test_bow = train_test_split(
    train_bow,          # 특성(X)
    merge['label'],     # 레이블(y)
    test_size=0.3,      # 30%를 테스트 세트로
    random_state=42     # 재현 가능한 결과를 위한 시드
)

# TF-IDF 데이터 분할
x_train_tfidf, x_test_tfidf, y_train_tfidf, y_test_tfidf = train_test_split(
    train_tfidf, merge['label'], test_size=0.3, random_state=42
)

print(x_train_bow.shape)  # (34411, 30000) — 학습용 70%
print(x_test_bow.shape)   # (14748, 30000) — 평가용 30%
```

## 3. 로지스틱 회귀 모델 학습

**이게 뭔지**: 텍스트 벡터를 받아 긍정(1)/부정(0) 확률을 출력하는 선형 분류기.  
**왜 로지스틱 회귀인가**: 텍스트 분류의 베이스라인 모델로 자주 쓰인다. 계산이 빠르고, 어떤 단어가 예측에 영향을 미쳤는지 해석할 수 있다.

내부 동작: `확률 = 1 / (1 + e^(-z))`  시그모이드 함수로 0~1 확률 출력 → 0.5 이상이면 긍정 분류

```python
from sklearn.linear_model import LogisticRegression

# BOW 기반 모델
lr_bow = LogisticRegression(
    solver='liblinear',  # 소규모/중규모 데이터에 적합한 최적화 방식
    max_iter=100
)
lr_bow.fit(x_train_bow, y_train_bow)

# TF-IDF 기반 모델
lr_tfidf = LogisticRegression(solver='liblinear', max_iter=100)
lr_tfidf.fit(x_train_tfidf, y_train_tfidf)
```

## 4. 모델 평가

**왜 정확도만으로 부족한가**: 부정 트윗이 1%뿐인 데이터에서 모든 것을 "긍정"으로 예측하면 정확도 99%가 나온다. 하지만 쓸모없는 모델이다. 여러 지표를 함께 봐야 한다.

```python
from sklearn import metrics

y_pred_bow = lr_bow.predict(x_test_bow)
y_pred_tfidf = lr_tfidf.predict(x_test_tfidf)
```

### 정확도 (Accuracy)

```
정확도 = 맞은 예측 수 / 전체 예측 수
```

```python
acc_bow   = metrics.accuracy_score(y_test_bow, y_pred_bow)
acc_tfidf = metrics.accuracy_score(y_test_tfidf, y_pred_tfidf)
```

### 정밀도(Precision)와 재현율(Recall)

```
정밀도 = 긍정으로 예측한 것 중 실제 긍정 비율
       = TP / (TP + FP)

재현율 = 실제 긍정 중 모델이 긍정으로 맞춘 비율
       = TP / (TP + FN)
```

- **정밀도가 중요한 경우**: 스팸 필터 — 일반 메일을 스팸으로 잘못 분류(FP)하면 중요 메일을 놓침
- **재현율이 중요한 경우**: 의료 진단 — 실제 환자를 놓치는 것(FN)이 위험

```python
precision_tfidf = metrics.precision_score(y_test_tfidf, y_pred_tfidf)
recall_tfidf    = metrics.recall_score(y_test_tfidf, y_pred_tfidf)
```

### F1 점수

**이게 뭔지**: 정밀도와 재현율의 조화평균. 둘 중 하나가 낮으면 F1도 낮아진다.

```
F1 = 2 × (정밀도 × 재현율) / (정밀도 + 재현율)
```

```python
f1_bow   = metrics.f1_score(y_test_bow, y_pred_bow)
f1_tfidf = metrics.f1_score(y_test_tfidf, y_pred_tfidf)
```

### ROC-AUC

**이게 뭔지**: 모델이 긍정과 부정을 얼마나 잘 구분하는지 나타내는 단일 지표. 0.5 = 동전 던지기 수준, 1.0 = 완벽.

```python
# predict_proba: 분류 결과(0/1)가 아닌 확률(0.0~1.0) 반환
y_pred_proba_tfidf = lr_tfidf.predict_proba(x_test_tfidf)[:, 1]  # 긍정 확률만
roc_auc_tfidf = metrics.roc_auc_score(y_test_tfidf, y_pred_proba_tfidf)
```

기준:

<div class="compare-wrap">
  <div class="compare-card">
    <div class="compare-label">0.5</div>
    <div class="compare-body"><p>동전 던지기 수준</p></div>
  </div>
  <div class="compare-card">
    <div class="compare-label">0.7 ~ 0.8</div>
    <div class="compare-body"><p>좋음</p></div>
  </div>
  <div class="compare-card">
    <div class="compare-label">0.8 ~ 0.9</div>
    <div class="compare-body"><p>매우 좋음</p></div>
  </div>
  <div class="compare-card">
    <div class="compare-label">0.9 이상</div>
    <div class="compare-body"><p>뛰어남</p></div>
  </div>
</div>

## 5. 최종 예측 및 제출

```python
test_original = pd.read_csv('test.csv')

# 주의: 테스트 데이터는 fit_transform이 아니라 transform만 사용
# fit_transform: 어휘를 새로 만들고 변환 (학습 데이터에만)
# transform: 기존 어휘 기준으로 변환 (테스트 데이터에 사용)
test_tfidf = tfidf_vect.transform(test_original['tweet'])

y_pred_final = lr_tfidf.predict(test_tfidf)

submission = pd.DataFrame({'id': test_original['id'], 'label': y_pred_final})
submission.to_csv('submission.csv', index=False)
```

테스트 데이터에 `fit_transform()`을 쓰면 안 되는 이유: 테스트 데이터의 단어 분포로 어휘 사전을 다시 만들면, 학습 때와 다른 기준으로 변환된다. 이를 **데이터 누수(data leakage)**라고 한다.

---

## 핵심 정리

```
정제된 텍스트
    ↓ CountVectorizer 또는 TfidfVectorizer
숫자 행렬 (문서 × 단어)
    ↓ train_test_split
학습셋 / 테스트셋
    ↓ LogisticRegression.fit()
모델
    ↓ predict() / predict_proba()
평가: Accuracy, Precision, Recall, F1, ROC-AUC
```

정확도 하나로 모델을 판단하면 안 된다는 것이 이날의 핵심이다. 어떤 실수가 더 비싼지(FP vs FN)를 먼저 정의하고, 그에 맞는 지표를 선택해야 한다.
