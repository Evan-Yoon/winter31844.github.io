---
title: "[NLP] day 5: 텍스트 분류 - TF-IDF와 로지스틱 회귀로 재난 관련 트윗 분류하기"
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
seriesOrder: 5
featured: false
draft: false
toc: true
---

Day 1~4에서 배운 전처리, 벡터화, 분류 개념을 하나의 프로젝트로 이어붙이는 날이다. 재난 관련 소셜 미디어 텍스트를 분류하는 전체 파이프라인을 처음부터 끝까지 구현했다.

## 1. 데이터 탐색

```python
import pandas as pd

df_raw = pd.read_csv('[Dataset]_Module25_disasters_social_media.csv', encoding='ISO-8859-1')
df_raw.head(5)
```

핵심 컬럼은 `choose_one`:
- `1` = 재난과 관련된 트윗
- `0` = 재난과 관련 없는 트윗
- `"Can't Decide"` = 판단 불가

```python
# "Can't Decide"는 모델 학습에 악영향 → 제거
df_raw = df_raw[df_raw['choose_one'] != "Can't Decide"]
```

**왜 제거하는가**: 분류 모델은 명확한 경계를 학습한다. 사람도 판단하기 어려운 데이터는 모델도 학습하지 못한다. 노이즈가 되어 모델 성능을 떨어뜨린다.

## 2. 텍스트 전처리

Day 2에서 한 것과 같은 흐름이다.

```python
import re
from nltk.corpus import stopwords

stop_words = set(stopwords.words('english'))

def extract_words(text):
    text = text.lower()                        # 대소문자 통일
    text = re.sub(r'[^a-z0-9\s]', '', text)   # 특수문자 제거
    words = text.split()                       # 단어 분리
    words = [w for w in words if w not in stop_words]  # 불용어 제거
    return words
```

전처리 후 모든 트윗에서 등장한 단어들의 집합을 어휘(vocabulary)로 만든다. 이 어휘의 크기가 이후 모델의 입력 차원 수가 된다.

## 3. TF-IDF 벡터화

**이게 뭔지**: 각 단어에 "이 문서에서만 얼마나 특별한가"를 반영한 가중치를 부여하는 방법.

```
IDF = log(전체 문서 수 / 해당 단어가 등장하는 문서 수)
```

- "earthquake" → 재난 문서에만 등장 → IDF 높음
- "the" → 모든 문서에 등장 → IDF 낮음

```python
from sklearn.feature_extraction.text import TfidfTransformer

# 먼저 CountVectorizer로 단어 빈도 행렬 만들기
# 그 다음 TfidfTransformer로 IDF 가중치 적용
tfidf_transformer = TfidfTransformer()
tfidf = tfidf_transformer.fit_transform(word_count_matrix)
```

## 4. 로지스틱 회귀 모델 학습

```python
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split

X_train, X_test, y_train, y_test = train_test_split(X_all, y_all, shuffle=True)

logreg = LogisticRegression(solver='newton-cg')
logreg.fit(X_train, y_train)

score = logreg.score(X_test, y_test)
print(f'정확도: {score:.3f}')  # 약 0.75
```

75%는 시작점이다. 하이퍼파라미터 튜닝으로 더 높일 수 있다.

## 5. 하이퍼파라미터 튜닝

**이게 뭔지**: 모델이 학습으로 결정하는 것이 파라미터라면, 하이퍼파라미터는 학습 전에 사람이 설정하는 값이다.  
**왜 필요한가**: 같은 알고리즘이라도 설정값에 따라 성능이 달라진다. 최적값은 직접 시도해봐야 안다.

```python
from sklearn.model_selection import GridSearchCV

# 시도할 파라미터 조합 정의
parameters = {
    'C': [0.001, 0.01, 0.1, 1, 10],   # 정규화 강도 (작을수록 강함)
    'tol': [0.0001, 0.001, 0.01],       # 수렴 판정 기준
    'max_iter': [100, 1000]             # 최대 반복 횟수
}

# cv=3: 데이터를 3등분해서 3번 교차 검증
clf = GridSearchCV(logreg, parameters, cv=3, return_train_score=True)
clf.fit(X_all, y_all)

print(f"최적 파라미터: {clf.best_params_}")
print(f"최적 점수: {clf.best_score_}")
```

GridSearchCV의 동작:
1. 5 × 3 × 2 = 30가지 파라미터 조합 시도
2. 각 조합에 대해 3-Fold 교차 검증 실행 (총 90번)
3. 가장 좋은 조합 반환

결과를 보고 범위를 좁혀서 더 정밀하게 탐색하는 방식으로 반복한다.

## 6. 예측 함수와 실제 테스트

```python
def twitter_predictor(tweet):
    # 전처리
    words = extract_words(tweet)
    
    # 단어 빈도 벡터 생성
    tf = np.zeros(len(vocab))
    for word in words:
        if word in vocab:
            tf[vocab.index(word)] += 1
    
    # TF-IDF 변환
    tfidf_vec = tf * idf
    
    # 예측
    prediction = logreg.predict([tfidf_vec])
    label = '재난 관련 (1)' if prediction[0] == 1 else '재난 무관 (0)'
    print(f'"{tweet}" → [{label}]')
```

### "Curry on Fire" 테스트

```python
# 케이스 1: 명백한 재난
tweet1 = '200 houses were on fire after an electric spark'
twitter_predictor(tweet1)  # → 재난 관련 (1) ✓

# 케이스 2: 스포츠 — on the roll
tweet2 = 'Michael curry is on the roll as he scored the fifth goal on the football tournament.'
twitter_predictor(tweet2)  # → 재난 무관 (0) ✓

# 케이스 3: 스포츠 — on fire (은유적 표현)
tweet3 = 'Michael curry is on fire as he scored the fifth goal on the football tournament.'
twitter_predictor(tweet3)  # → ???
```

케이스 3이 핵심이다. "on fire"는 재난 맥락에서는 불이 났다는 의미지만, 스포츠 맥락에서는 "잘하고 있다"는 은유다. TF-IDF 모델은 이 차이를 구별하지 못한다. "fire"라는 단어 하나만 보기 때문이다.

**이것이 TF-IDF의 한계다.** 문맥 전체를 이해하려면 Word2Vec이나 BERT 같은 방식이 필요하다.

---

## 핵심 정리

| 단계 | 도구 | 핵심 |
|---|---|---|
| 데이터 탐색 | Pandas | 타겟 변수 확인, 모호한 레이블 제거 |
| 텍스트 전처리 | NLTK, Regex | 소문자화, 특수문자 제거, 불용어 제거 |
| 벡터화 | TF-IDF | 중요한 단어에 높은 가중치 |
| 모델 학습 | LogisticRegression | 확률 기반 이진 분류 |
| 튜닝 | GridSearchCV | 최적 하이퍼파라미터 자동 탐색 |

**오늘의 교훈**: TF-IDF + 로지스틱 회귀로 약 75% 정확도를 달성했다. 나쁘지 않지만, "curry on fire"처럼 문맥에 따라 의미가 달라지는 경우를 처리하려면 더 정교한 방법이 필요하다. 이것이 Word2Vec, BERT 등 더 발전된 방법들이 나온 이유다.
