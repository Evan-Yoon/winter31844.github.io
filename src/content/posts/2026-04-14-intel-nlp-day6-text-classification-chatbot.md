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

2026년 4월 14일 수업은 [day 4의 키워드 분석과 텍스트 임베딩](/posts/nlp-study-keyword-analysis-word2vec-embedding/)에서 다룬 벡터화 개념을 실제 분류와 응답 생성 쪽으로 밀어붙이는 흐름이었다. 앞에서는 단어를 어떻게 수치화할지에 집중했다면, 이번에는 그 벡터를 이용해 문장을 분류하고, 질의에 반응하는 챗봇 형태까지 만들어 봤다.

특히 이날은 한 가지 모델만 깊게 파기보다, 같은 NLP 파이프라인이 서로 다른 문제에 어떻게 적용되는지를 연속해서 확인한 날이었다. TF-IDF 기반 영화 리뷰 감정 분류, 간단한 의도 분류 챗봇, 코사인 유사도 기반 질의응답 챗봇이 그 축이었다.

## 1. TF-IDF 분류 과제: 영화 리뷰 감정 분석

`4월 14일.ipynb`에서는 앞부분에서 재난 트윗 분류를 이어가고, 마지막 `5. NLP 분류 과제!`부터는 IMDB 영화 리뷰 데이터로 감정 분석을 진행했다. 여기서는 훈련용 `pkl` 파일과 테스트용 `pkl` 파일을 불러와 리뷰가 긍정인지 부정인지 분류했다.

핵심 흐름은 단순했다.

1. `CountVectorizer(max_features=5000)`로 단어 가방을 만든다.
2. `TfidfTransformer()`로 빈도 벡터를 TF-IDF로 바꾼다.
3. `LogisticRegression()`으로 긍정/부정을 학습한다.
4. 테스트 세트 정확도로 성능을 확인한다.

수업 코드에서는 아래와 같이 진행했다.

```python
vectorizer = CountVectorizer(
    analyzer="word",
    strip_accents=None,
    tokenizer=None,
    preprocessor=None,
    stop_words=None,
    max_features=5000
)

train_data_features = vectorizer.fit_transform(df_raw["text"])
test_data_features = vectorizer.transform(df_raw_test["text"])

tfidfier = TfidfTransformer()
tfidf = tfidfier.fit_transform(train_data_features)
tfidf_test = tfidfier.transform(test_data_features)
```

이후 로지스틱 회귀로 학습했을 때 테스트 정확도는 약 `0.88256`이 나왔다. 복잡한 딥러닝 없이도 전처리와 벡터화가 정리되면, 전통적인 선형 모델만으로도 꽤 안정적인 분류가 가능하다는 점을 다시 확인한 셈이다.

```python
rf = LogisticRegression()
rf.fit(X_all, y_all)
print(rf.score(X_test, y_test))
```

이 과제의 의미는 단순히 점수를 얻는 데 있지 않았다. day 4에서 TF-IDF와 임베딩을 배울 때는 "이 숫자 표현이 왜 필요한가"에 더 가까웠다면, 이번에는 그 표현이 실제 분류 성능으로 어떻게 이어지는지 확인했다. 텍스트를 벡터로 바꾸는 작업이 전처리의 끝이 아니라, 모델 입력을 설계하는 핵심 단계라는 점이 더 분명해졌다.

추가로, 같은 노트북 앞부분에서는 재난 트윗 분류 예제를 통해 `GridSearchCV`로 하이퍼파라미터를 조정하고, 입력 문장을 직접 넣어 관련성 여부를 판별하는 파이프라인도 만들었다. 결국 리뷰 감정 분석 과제는 그 흐름을 데이터셋만 바꿔 다시 적용한 형태에 가깝다.

## 2. 신경망으로 의도 분류 챗봇 만들기

`4월 14일 (2).ipynb`에서는 아주 작은 문장 집합을 가지고 챗봇의 기본 구조를 직접 만들었다. 데이터는 `Hi`, `Hello`, `bye`, `working` 같은 짧은 문장이고, 라벨은 `greeting`, `busy`, `bye` 세 가지였다.

여기서 먼저 한 일은 텍스트 정리였다.

- 소문자 변환
- 영문자와 공백 외 문자 제거
- 불필요한 공백 정리
- 단어 집합(vocabulary) 구축
- 문장을 BoW 형태의 이진 벡터로 인코딩

예를 들면 이런 식이다.

```python
def preprocess_data(X):
    X = [data_point.lower() for data_point in X]
    X = [remove_non_alpha_characters(sentence) for sentence in X]
    X = [data_point.strip() for data_point in X]
    X = [re.sub(' +', ' ', data_point) for data_point in X]
    return X

def encode_sentence(sentence):
    sentence = preprocess_data([sentence])[0]
    sentence_encoded = [0] * len(vocabulary)
    for i in range(len(vocabulary)):
        if vocabulary[i] in sentence.split(' '):
            sentence_encoded[i] = 1
    return sentence_encoded
```

그 다음에는 Keras `Sequential` 모델로 간단한 다층 퍼셉트론을 만들었다. 입력층 뒤에 `Dense(64, activation='sigmoid')`, 출력층에 `softmax`를 두고, 다중 클래스 분류 형태로 학습했다.

```python
model = Sequential()
model.add(Dense(units=64, activation='sigmoid', input_dim=len(X_train[0])))
model.add(Dense(units=len(y_train[0]), activation='softmax'))
model.compile(
    loss=categorical_crossentropy,
    optimizer=SGD(learning_rate=0.01, momentum=0.9, nesterov=True)
)
model.fit(np.array(X_train), np.array(y_train), epochs=100, batch_size=16)
```

이 챗봇은 아직 "대화를 이해한다"기보다, 입력 문장을 의도 라벨로 분류하는 구조에 가깝다. 사용자가 문장을 입력하면 모델이 `greeting`, `busy`, `bye` 중 하나를 예측하고 그 결과를 돌려준다.

```python
prediction = model.predict(np.array([encode_sentence(sentence)]))
print(classes[np.argmax(prediction)])
```

규모는 작지만, 챗봇의 기본 뼈대는 여기서 이미 드러난다. 결국 챗봇도 입력 문장을 적절한 표현으로 바꾸고, 그 표현을 기준으로 다음 행동을 결정하는 시스템이다. 이 노트북은 그 과정을 가장 단순한 형태로 보여줬다.

## 3. 코사인 유사도 기반 질의응답 챗봇

`4월 14일(3).ipynb`에서는 접근 방식이 달라졌다. 이번에는 문장을 특정 클래스에 분류하는 대신, 지식 문서 안에서 사용자의 질문과 가장 비슷한 문장을 찾아 답으로 돌려주는 방식이었다.

핵심 구성은 다음과 같았다.

- `nltk.sent_tokenize`로 문서를 문장 단위로 분리
- `nltk.word_tokenize`로 단어 토큰화
- `WordNetLemmatizer`로 표제어 정규화
- `TfidfVectorizer`로 문장 벡터화
- `cosine_similarity`로 질문과 문장 간 유사도 계산

정규화 함수는 아래처럼 구성했다.

```python
remove_punct_dict = dict((ord(punct), None) for punct in string.punctuation)

def LemNormalize(text):
    return LemTokens(
        nltk.word_tokenize(text.lower().translate(remove_punct_dict))
    )
```

실제 응답 함수에서는 사용자 질문을 기존 문장 목록에 임시로 추가한 뒤, TF-IDF 벡터를 만들고 마지막 문장인 질문과 나머지 문장들 사이의 코사인 유사도를 계산했다. 가장 유사한 문장을 찾아 그대로 응답으로 반환하는 구조다.

```python
def response(user_response):
    sent_tokens.append(user_response)
    TfidfVec = TfidfVectorizer(tokenizer=LemNormalize, stop_words='english')
    tfidf = TfidfVec.fit_transform(sent_tokens)
    vals = cosine_similarity(tfidf[-1], tfidf)

    idx = vals.argsort()[0][-2]
    best_sim = vals[0][idx]

    if best_sim == 0:
        robo_response = "I am sorry! I don't understand you"
    else:
        robo_response = sent_tokens[idx]

    return robo_response
```

이 방식은 앞의 의도 분류 챗봇과 성격이 다르다. 앞쪽 모델이 입력을 몇 개의 클래스 중 하나로 보내는 구조였다면, 여기서는 질문과 가장 가까운 문장을 검색해서 답한다. 분류보다는 검색 기반 챗봇에 더 가깝고, 지식 문서가 좋아질수록 답변도 같이 좋아지는 구조다.

또 하나 흥미로웠던 부분은 인사말 처리와 일반 질의응답을 분리했다는 점이다. `greeting()` 함수로 간단한 인삿말을 먼저 처리하고, 그 외 질문만 유사도 기반 응답 함수로 넘겼다. 실제 서비스에서도 규칙 기반 처리와 모델 기반 처리를 섞는 경우가 많은데, 이 노트북이 그 구조를 가볍게 보여줬다.

## 4. 4월 14일 수업에서 정리된 흐름

세 노트북을 이어서 보면 이날 수업은 하나의 방향으로 묶인다. 텍스트를 전처리하고, 벡터로 바꾸고, 그 벡터를 이용해 분류하거나 가장 적절한 응답을 찾는 흐름이다.

- TF-IDF + 로지스틱 회귀: 리뷰 감정 분류
- BoW + 신경망: 의도 분류 챗봇
- TF-IDF + 코사인 유사도: 검색형 질의응답 챗봇

같은 NLP라도 문제 정의에 따라 모델 구조가 달라진다는 점이 분명했다. 어떤 경우에는 선형 분류기가 충분하고, 어떤 경우에는 작은 신경망이 필요하고, 또 어떤 경우에는 분류보다 검색이 더 자연스럽다. 중요한 것은 항상 앞단의 텍스트 표현 방식이다.

day 4에서 배운 벡터화와 임베딩이 이론으로만 남지 않고, day 6에서는 실제 분류기와 챗봇 구조로 이어졌다. 이 연결이 잡히고 나니 NLP를 개별 기법의 목록으로 보는 대신, 입력 표현과 문제 유형에 따라 조합되는 파이프라인으로 보기 시작했다.
