---
title: "[NLP] day 3: 텍스트 분류 - TF-IDF와 로지스틱 회귀로 재난 관련 트윗 분류하기"
slug: nlp-text-classification-study-tfidf-logistic-regression
date: 2026-04-13
author: Evan Yoon
category: study
subcategory: bootcamp
description: |
  재난 관련 트윗 데이터를 이용해 텍스트 분류 전체 파이프라인을 구현했다.
  데이터 탐색, 텍스트 전처리, TF-IDF 벡터화, 로지스틱 회귀 모델 훈련, 하이퍼파라미터 튜닝, 
  그리고 "curry on fire" 예제까지 진행한 학습 과정을 정리했다.
thumbnail: /images/posts/nlp-study/tfidf-classification.png
tags:
  - nlp
  - text-classification
  - tfidf
  - logistic-regression
  - machine-learning
  - sklearn
  - nlp-study
  - disaster-tweets
readTime: 12
series: "Intel NLP 과정"
seriesOrder: 1
featured: false
draft: false
toc: true
---

## 오늘의 학습 목표와 현황

2026년 4월 13일, NLP 스터디에서 **텍스트 분류(Text Classification)** 의 전체 파이프라인을 한 번에 경험했다.

아침부터 저녁까지 진행한 이 실습의 핵심은 다음과 같다:

> 재난 관련 트윗 데이터를 불러와 전처리하고, TF-IDF로 벡터화한 후, 로지스틱 회귀로 모델을 훈련해서,
> 새로운 트윗이 "재난과 관련이 있는지 없는지" 자동으로 판단할 수 있는 분류기 만들기

오늘 한 작업을 크게 나누면:

1. **데이터 탐색 (EDA)**
2. **텍스트 전처리**
3. **TF-IDF 벡터화**
4. **로지스틱 회귀 모델 훈련**
5. **하이퍼파라미터 튜닝**
6. **모델 평가 및 실제 트윗 테스트**

---

## 1단계: 데이터 불러오고 구조 파악하기

먼저 재난 관련 소셜 미디어 데이터 CSV 파일을 불러왔다.

```python
import pandas as pd
import numpy as np
import nltk
import re

df_raw = pd.read_csv('[Dataset]_Module25_disasters_social_media.csv', encoding='ISO-8859-1')
```

데이터를 불러온 후 가장 먼저 한 작업:

- `df_raw.head(5)` → 데이터 구조 확인
- `df_raw.describe()` → 전체 통계 확인
- 컬럼 목록 확인

### 중요한 발견: Target과 Label

이 데이터셋의 핵심은 **`'choose_one'`** 컬럼이었다. 이것이 우리가 예측해야 할 **타겟(target)** 이었다.

- 1 = 재난과 관련된 트윗
- 0 = 재난과 관련 없는 트윗

그런데 중요한 문제가 있었다. 데이터를 살펴보니 `'Can't Decide'` 라는 값이 있었다. 이건 사람이 판단하기 어려운 트윗들이었다.

```python
# 'Can't Decide'는 모델 훈련에 방해가 되므로 제거
df_raw = df_raw[df_raw['choose_one'] != "Can't Decide"]
```

**왜 이렇게 할까?** 분류 모델은 명확한 경계(decision boundary)를 배운다. 사람도 헷갈리는 데이터는 모델도 헷갈린다. 따라서 처음부터 제거하는 게 모델을 더 깔끔하게 훈련시킨다.

---

## 2단계: 텍스트 전처리 (Text Preprocessing)

raw 텍스트를 그대로 모델에 넣을 수 없다. 모델이 이해할 수 있도록 정리해야 한다.

### 핵심 전처리 작업들:

1. **소문자 변환** → 'Fire'와 'fire'를 같은 단어로 취급
2. **특수문자 제거** → '@', '#', URL 등
3. **불용어 제거 (Stop words)** → 'the', 'a', 'and' 같은 흔한 단어들
4. **토큰화 (Tokenization)** → 문장을 단어 단위로 분해

```python
def extract_words(text):
    """전처리된 단어들을 추출하는 함수"""
    # 소문자로 변환
    text = text.lower()
    # 특수문자 제거
    text = re.sub(r'[^a-z0-9\s]', '', text)
    # 토큰화
    words = text.split()
    # 불용어 제거
    words = [w for w in words if w not in stop_words]
    return words
```

### 전처리 후 '단어 어휘(Vocabulary)' 구축

모든 트윗을 처리하고 나면, 사용된 모든 단어의 목록(어휘)을 만든다.

예: `['earthquake', 'fire', 'disaster', 'injured', 'help', ...]`

이 어휘의 크기가 곧 모델이 다룰 특성(feature)의 개수가 된다.

---

## 3단계: 벡터화 (Vectorization) - TF와 TF-IDF

텍스트는 숫자가 아니라서 기계학습 모델에 직접 입력할 수 없다. 따라서 숫자로 변환해야 한다.

### TF (Term Frequency) - 단순 단어 빈도

```python
# 예시 트윗: "earthquake damaged the city"
# 어휘: ['earthquake', 'damaged', 'city', 'fire', 'help', ...]
# 결과 벡터: [1, 1, 1, 0, 0, ...]
```

이건 단순한 "단어가 몇 번 나왔는가"를 센다. 하지만 문제가 있다:

- 'the', 'and' 같은 흔한 단어가 높은 중요도를 받는다
- 모든 데이터에 공통으로 나타나는 단어는 구별력이 없다

### TF-IDF (Term Frequency - Inverse Document Frequency)

이를 개선한 방법이 TF-IDF다.

```
TF-IDF = TF × IDF

IDF = log(전체 문서 수 / 단어가 나타난 문서 수)
```

**개념:**

- 어떤 단어가 **많은 문서에 등장**하면 IDF가 낮아진다 → 덜 중요
- 어떤 단어가 **적은 문서에만 등장**하면 IDF가 높아진다 → 더 중요

**예시:**

- 'earthquake' → 재난 관련 문서에만 자주 나타남 → 높은 점수
- 'the' → 모든 문서에 나타남 → 낮은 점수

오늘 이 작업을 Scikit-Learn의 `TfidfTransformer`로 구현했다:

```python
from sklearn.feature_extraction.text import TfidfTransformer

tfidf_transformer = TfidfTransformer()
tfidf = tfidf_transformer.fit_transform(word_count_matrix)
```

---

## 4단계: 로지스틱 회귀로 분류 모델 훈련

"회귀"라는 이름이지만, 실제로는 **분류(Classification)** 문제에 자주 쓰인다.

로지스틱 회귀의 핵심:

- 입력: TF-IDF 벡터 (1000개+ 차원)
- 출력: 0 또는 1의 확률
- 확률 > 0.5 → 재난 관련 (1)
- 확률 ≤ 0.5 → 재난 무관 (0)

```python
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split

# 데이터를 훈련/테스트로 분할
X_train, X_test, y_train, y_test = train_test_split(
    X_all, y_all, shuffle=True
)

# 모델 생성 및 훈련
logreg = LogisticRegression(solver='newton-cg')
logreg.fit(X_train, y_train)

# 모델 평가
score = logreg.score(X_test, y_test)
print(f'정확도: {score:.3f}')  # 약 0.75
```

**첫 번째 결과: 약 75% 정확도**

나쁘지 않지만 더 좋게 만들 수 있다 → 하이퍼파라미터 튜닝으로 진입!

---

## 5단계: 하이퍼파라미터 튜닝 (Hyperparameter Tuning)

모델의 "설정값들"을 조정해서 성능을 높인다.

로지스틱 회귀의 주요 하이퍼파라미터:

- `C` → 정규화 강도 (작을수록 더 강함)
- `tol` → 수렴 판정 기준
- `max_iter` → 최대 반복 횟수

Scikit-Learn의 `GridSearchCV`를 사용해서 최적의 조합을 찾는다:

```python
from sklearn.model_selection import GridSearchCV

parameters = {
    'C': [0.001, 0.01, 0.1, 1, 10],
    'tol': [0.0001, 0.001, 0.01],
    'max_iter': [100, 1000]
}

clf = GridSearchCV(logreg, parameters, cv=3, return_train_score=True)
clf.fit(X_all, y_all)

print(f"최적 파라미터: {clf.best_params_}")
print(f"최적 점수: {clf.best_score_}")
```

**GridSearchCV가 하는 일:**

1. 가능한 모든 파라미터 조합을 시도 (총 5 × 3 × 2 = 30가지)
2. 각각에 대해 3-Fold 교차 검증(Cross Validation)을 실행
3. 가장 좋은 결과를 뽑아낸다

처음 결과를 보고 범위를 좁혀서 다시 탐색:

```python
new_parameters = {
    'C': [0.0001, 0.0005, 0.001, 0.002],
    'tol': [0.005, 0.01, 0.05, 0.1],
    'max_iter': [50, 100, 150]
}

clf_new = GridSearchCV(logreg, new_parameters, cv=3, return_train_score=True)
clf_new.fit(X_all, y_all)
```

이렇게 하면 더 세밀하게 튜닝할 수 있다.

---

## 6단계: 예측 함수 만들기

모델을 훈련시켰으니, 이제 새로운 트윗을 입력하면 분류해주는 함수를 만든다:

```python
def twitter_predictor(tweet):
    # 1. 입력 트윗을 TF 벡터로 변환
    words = extract_words(tweet)
    tf = np.zeros(len(vocab))
    for word in words:
        if word in vocab:
            tf[vocab.index(word)] += 1

    # 2. TF-IDF 벡터로 변환
    tfidf_vec = tf * idf

    # 3. 로지스틱 회귀로 예측
    prediction = logreg.predict([tfidf_vec])

    # 4. 결과 출력
    if prediction[0] == 1:
        print(f'"{tweet}" → [예측: 재난 관련 (1)]')
    else:
        print(f'"{tweet}" → [예측: 재난 무관 (0)]')
```

---

## 7단계: 실제 트윗으로 테스트 - "Curry on Fire" 예제

이제 우리 모델이 정말 잘 작동하는지 실제 예제로 테스트한다.

### 테스트 케이스 1: 명백한 재난

```python
tweet1 = '200 houses were on fire after an electric spark burns the stack of wood'
twitter_predictor(tweet1)
# 예상: 재난 관련 (1) ✓
```

'fire'라는 명백한 재난 키워드가 있으므로 분류해야 한다.

### 테스트 케이스 2: 스포츠 관련 - "on the roll"

```python
tweet2 = 'Michael curry is on the roll as he scored the fifth goal on the football tournament.'
twitter_predictor(tweet2)
# 예상: 재난 무관 (0) ✓
```

"Michael Curry" (농구 선수)가 경기에서 좋은 성적을 내는 것. 'fire'가 없고, 'goal'과 'tournament' 같은 스포츠 단어들이 있다.

### 테스트 케이스 3: 문맥에 따른 모호성 - "on fire" (은유적 의미)

```python
tweet3 = 'Michael curry is on fire as he scored the fifth goal on the football tournament.'
twitter_predictor(tweet3)
# 예상: ???
```

여기서는 'on fire'가 **재난이 아니라 은유적 표현**이다. 스포츠에서 "on fire"는 "잘하고 있다"는 뜻이다.

**이 예제의 의미:**

- 모델이 단순히 키워드만 보는 게 아니라, **문맥(context)을 이해**할 수 있는가?
- TF-IDF와 로지스틱 회귀 같은 전통적 방법의 **한계는 무엇인가?**

이게 오늘 배운 가장 중요한 교훈이다.

---

## 오늘 배운 핵심 개념 정리

| 단계        | 목적        | 사용 도구                   | 핵심 아이디어                      |
| ----------- | ----------- | --------------------------- | ---------------------------------- |
| 데이터 탐색 | 데이터 이해 | Pandas                      | 타겟 변수 확인, 라벨 불균형 확인   |
| 전처리      | 텍스트 정리 | NLTK, Regex                 | 소문자, 특수문자 제거, 불용어 제거 |
| 벡터화      | 숫자로 변환 | TF-IDF                      | 중요한 단어에 높은 가중치 부여     |
| 모델 훈련   | 패턴 학습   | Logistic Regression         | 확률 기반 분류                     |
| 튜닝        | 성능 향상   | GridSearchCV                | 최적의 파라미터 자동 탐색          |
| 평가        | 성능 측정   | Accuracy, Precision, Recall | 모델이 얼마나 잘 작동하는가        |

---

## 다음 스텝: 감정 분석 (Sentiment Analysis)

오늘 학습은 여기까지지만, 노트북의 다음 섹션은 **IMDB 영화 리뷰 데이터를 이용한 감정 분석(Sentiment Analysis)** 이다.

이건 오늘 배운 분류 파이프라인을 거의 그대로 사용하되, 데이터만 바뀌는 구조다.

```
긍정 리뷰 (1) vs 부정 리뷰 (0)
```

재난 분류 → 감정 분류로 확장하는 경험이 될 것 같다.

---

## 오늘의 깨달음

> **"Curry on Fire" 예제는 왜 중요한가?**

1. **단순 키워드 매칭의 한계를 보여준다**
   - "on fire"가 재난일 수도, 은유일 수도 있다
   - 모델이 문맥을 이해해야 한다

2. **NLP의 다음 단계로의 징검다리**
   - Word2Vec, BERT 같은 "문맥을 이해하는" 모델이 필요한 이유
   - 전통적 머신러닝의 한계와 딥러닝의 장점

3. **현실의 데이터는 항상 모호하다**
   - 사람도 판단 어려운 데이터가 많다
   - 모델의 정확도 75%는 나쁘지 않은 것

---

**오늘의 학습을 마치며:** NLP의 기본 파이프라인을 한 번에 경험했다. 데이터 → 전처리 → 벡터화 → 모델 훈련 → 평가라는 흐름이 이제 자연스럽다. 다음은 더 정교한 방법들(word embeddings, transformer 모델)을 배울 차례다.
