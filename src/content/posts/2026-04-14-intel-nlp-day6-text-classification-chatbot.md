---
title: "[NLP] day 6: TF-IDF 분류 과제와 챗봇 구현"
slug: intel-nlp-day6-text-classification-chatbot
date: 2026-04-14
author: Evan Yoon
category: study
subcategory: bootcamp
description: |
  2026년 4월 14일 수업에서는 TF-IDF 기반 감정 분류 과제를 진행하고,
  간단한 신경망 챗봇과 코사인 유사도 기반 챗봇을 구현했다.
  전처리, 벡터화, 분류, 응답 생성이 실제로 어떻게 이어지는지 정리한다.
thumbnail: ""
tags:
  - NLP
  - text-classification
  - tfidf
  - sentiment-analysis
  - chatbot
  - cosine-similarity
  - keras
  - scikit-learn
  - nlp-study
readTime: 12
series: "Intel NLP 과정"
seriesOrder: 6
featured: false
draft: false
toc: true
---

Day 4~5에서 배운 벡터화와 분류 개념을 세 가지 방향으로 적용했다. 영화 리뷰 감정 분석, 의도 분류 챗봇, 코사인 유사도 기반 챗봇이다. 같은 NLP 파이프라인이 문제 정의에 따라 어떻게 다른 구조로 구현되는지 확인하는 날이었다.

## 1. TF-IDF 분류 과제: 영화 리뷰 감정 분석

**목표**: IMDB 영화 리뷰가 긍정인지 부정인지 분류한다.

**흐름**: 단어 빈도 행렬 → TF-IDF 가중치 적용 → 로지스틱 회귀 학습 → 평가

```python
from sklearn.feature_extraction.text import CountVectorizer, TfidfTransformer
from sklearn.linear_model import LogisticRegression

# 1단계: 단어 빈도 행렬 만들기
vectorizer = CountVectorizer(max_features=5000)  # 상위 5,000개 단어만 사용
train_data_features = vectorizer.fit_transform(df_raw["text"])
test_data_features  = vectorizer.transform(df_raw_test["text"])  # fit 없이 transform만

# 2단계: TF-IDF 가중치 적용
# TF: 문서 내 단어 빈도 / TF-IDF: 빈도 × 희소성(IDF)
tfidfier = TfidfTransformer()
tfidf      = tfidfier.fit_transform(train_data_features)
tfidf_test = tfidfier.transform(test_data_features)  # 역시 transform만

# 3단계: 학습과 평가
rf = LogisticRegression()
rf.fit(X_all, y_all)
print(rf.score(X_test, y_test))  # 약 0.88256
```

테스트 데이터에 `fit_transform` 대신 `transform`만 쓰는 이유: 학습 데이터에서 만든 어휘 사전과 IDF 값을 그대로 적용해야 한다. 테스트 데이터로 다시 fit하면 "모르는 데이터를 미리 보는" 꼴이 된다.

**결과**: 88.3% 정확도. 복잡한 딥러닝 없이 전처리와 TF-IDF만으로도 충분한 성능이 나온다.

## 2. 신경망으로 의도 분류 챗봇 만들기

**목표**: 사용자가 입력한 문장이 어떤 의도인지 분류한다.  
**구조**: 문장 → BoW 벡터 → 신경망 → 의도 레이블(greeting / busy / bye)

데이터 예시:
- `"Hi"`, `"Hello"` → `greeting`
- `"I'm working"`, `"Busy now"` → `busy`
- `"bye"`, `"See you"` → `bye`

### 전처리와 인코딩

```python
def preprocess_data(X):
    X = [s.lower() for s in X]            # 소문자
    X = [remove_non_alpha(s) for s in X]  # 영문만 남김
    X = [s.strip() for s in X]            # 앞뒤 공백 제거
    X = [re.sub(' +', ' ', s) for s in X] # 연속 공백 제거
    return X

def encode_sentence(sentence):
    """문장을 BoW 이진 벡터로 변환 — 단어 있으면 1, 없으면 0"""
    sentence = preprocess_data([sentence])[0]
    vector = [0] * len(vocabulary)
    for i, word in enumerate(vocabulary):
        if word in sentence.split():
            vector[i] = 1
    return vector
```

왜 이진 벡터를 쓰는가: 문장이 매우 짧아서 단어 빈도 차이가 의미 없다. 있냐/없냐만 구별하면 충분하다.

### 신경망 구조

```python
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense
from tensorflow.keras.losses import categorical_crossentropy
from tensorflow.keras.optimizers import SGD

model = Sequential()
# 입력: 어휘 크기 차원의 BoW 벡터
model.add(Dense(units=64, activation='sigmoid', input_dim=len(X_train[0])))
# 출력: 클래스 수(3)만큼 소프트맥스 확률 반환
model.add(Dense(units=len(y_train[0]), activation='softmax'))

model.compile(
    loss=categorical_crossentropy,
    optimizer=SGD(learning_rate=0.01, momentum=0.9, nesterov=True)
)
model.fit(np.array(X_train), np.array(y_train), epochs=100, batch_size=16)
```

`softmax`: 3개 클래스에 대한 확률 합이 1이 되도록 출력. 가장 높은 확률의 클래스가 예측 결과.

```python
# 예측: 출력 확률 중 가장 높은 인덱스의 클래스 선택
prediction = model.predict(np.array([encode_sentence(sentence)]))
print(classes[np.argmax(prediction)])  # 'greeting' 또는 'busy' 또는 'bye'
```

이 챗봇의 한계: 사전에 정의된 3가지 의도만 분류할 수 있다. 새로운 유형의 입력은 처리하지 못한다.

## 3. 코사인 유사도 기반 질의응답 챗봇

**목표**: 사전에 준비된 지식 문서에서 사용자 질문과 가장 유사한 문장을 찾아 답한다.  
**방식**: 분류가 아니라 검색이다. 미리 클래스를 정의하지 않아도 된다.

**왜 이 방식이 유용한가**: 챗봇이 답해야 할 내용이 문서 형태로 있을 때(FAQ, 제품 매뉴얼 등), 문서를 모두 클래스로 분류하는 것은 불가능하다. 대신 가장 관련 있는 문장을 검색해서 돌려준다.

### 텍스트 정규화 함수

```python
import string, nltk
from nltk.stem import WordNetLemmatizer

lemmer = WordNetLemmatizer()
remove_punct = dict((ord(punct), None) for punct in string.punctuation)

def LemNormalize(text):
    """소문자 변환 + 구두점 제거 + 단어 토큰화 + 표제어 추출"""
    tokens = nltk.word_tokenize(text.lower().translate(remove_punct))
    return [lemmer.lemmatize(t) for t in tokens]
```

### 응답 함수

```python
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

def response(user_response):
    # 1. 사용자 질문을 기존 문장 목록에 임시 추가
    sent_tokens.append(user_response)
    
    # 2. 전체 문장(질문 포함)을 TF-IDF 벡터로 변환
    TfidfVec = TfidfVectorizer(tokenizer=LemNormalize, stop_words='english')
    tfidf = TfidfVec.fit_transform(sent_tokens)
    
    # 3. 마지막(질문) 벡터와 나머지 문장들의 코사인 유사도 계산
    vals = cosine_similarity(tfidf[-1], tfidf)
    
    # 4. 유사도 가장 높은 문장 찾기 (자기 자신 제외 → [-2])
    idx = vals.argsort()[0][-2]
    best_sim = vals[0][idx]
    
    sent_tokens.remove(user_response)  # 추가했던 질문 제거
    
    if best_sim == 0:
        return "I am sorry! I don't understand you"
    return sent_tokens[idx]
```

`tfidf[-1]`: 방금 추가한 사용자 질문의 TF-IDF 벡터  
`vals.argsort()[0][-2]`: 유사도 순으로 정렬했을 때 가장 높은 것 ([-1]은 자기 자신이므로 [-2])

### 인사말 처리 분리

```python
GREETING_INPUTS  = ("hello", "hi", "greetings", "sup")
GREETING_OUTPUTS = ["hi", "hey", "hi there", "Hello!"]

def greeting(sentence):
    for word in sentence.split():
        if word.lower() in GREETING_INPUTS:
            return random.choice(GREETING_OUTPUTS)
    return None
```

인사말을 별도로 처리하는 이유: 짧은 인사말은 지식 문서에 없어서 유사도 기반 검색이 맞지 않는다. 규칙 기반으로 먼저 처리하고, 나머지만 유사도 검색으로 넘긴다. 실제 서비스도 이처럼 규칙 기반과 모델 기반을 섞는다.

---

## 핵심 정리

<div class="compare-wrap">
  <div class="compare-card">
    <div class="compare-label">TF-IDF + 로지스틱 회귀</div>
    <div class="compare-body">
      <p><strong>문제 유형:</strong> 고정된 클래스 분류 (감정 분석)</p>
      <p><strong>핵심 아이디어:</strong> 벡터화 후 선형 분류</p>
    </div>
  </div>
  <div class="compare-card">
    <div class="compare-label">BoW + 신경망</div>
    <div class="compare-body">
      <p><strong>문제 유형:</strong> 의도 분류 (챗봇)</p>
      <p><strong>핵심 아이디어:</strong> 이진 벡터 → 확률 분류</p>
    </div>
  </div>
  <div class="compare-card">
    <div class="compare-label">TF-IDF + 코사인 유사도</div>
    <div class="compare-body">
      <p><strong>문제 유형:</strong> 검색형 Q&A</p>
      <p><strong>핵심 아이디어:</strong> 분류 없이 가장 유사한 문장 검색</p>
    </div>
  </div>
</div>

문제가 달라지면 구조가 달라진다. 클래스가 고정되어 있으면 분류 모델, 지식 문서에서 답을 찾아야 하면 검색 기반이 더 자연스럽다. 공통점은 언제나 텍스트를 벡터로 바꾸는 것에서 시작한다는 점이다.
