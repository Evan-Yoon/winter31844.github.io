---
title: "[NLP] day 7: 호텔 FAQ 챗봇과 Word2Vec 직접 구현"
slug: intel-nlp-day7-hotel-chatbot-doc2vec-word2vec
date: 2026-04-15
author: Evan Yoon
category: study
subcategory: bootcamp
description: |
  2026년 4월 15일 수업에서는 질문-답변 딕셔너리 기반 호텔 FAQ 챗봇을 만들었다.
  코사인 유사도와 Doc2Vec 두 가지 방식을 비교하고,
  TensorFlow로 Skip-Gram Word2Vec를 직접 구현해 2D 임베딩 공간을 시각화했다.
thumbnail: ""
tags:
  - NLP
  - chatbot
  - doc2vec
  - word2vec
  - cosine-similarity
  - tfidf
  - gensim
  - tensorflow
  - nlp-study
readTime: 11
series: "Intel NLP 과정"
seriesOrder: 7
featured: false
draft: false
toc: true
---

2026년 4월 15일 수업은 두 파트로 나뉘었다. 앞부분은 호텔 FAQ 챗봇으로, day 6에서 다룬 코사인 유사도 챗봇 구조를 질문-답변 쌍 데이터에 적용하고 Doc2Vec 방식과 비교하는 내용이었다. 뒷부분은 Word2Vec를 TensorFlow로 직접 구현하면서 임베딩이 실제로 어떻게 학습되는지 확인했다.

## 1. 호텔 FAQ 챗봇: 질문-답변 딕셔너리 구조

`4월 15일.ipynb`의 출발점은 day 6 코사인 유사도 챗봇과 같았다. 달라진 건 데이터 구조다. 이전에는 단일 문서에서 가장 유사한 문장을 찾아 그대로 돌려줬다면, 이번에는 질문 파일(`Module27(ques).txt`)과 답변 파일(`Module27 (ans).txt`)을 따로 불러와 딕셔너리로 연결했다.

```python
sent_tokens = nltk.sent_tokenize(raw_data)         # 질문 리스트
sent_tokens_ans = nltk.sent_tokenize(raw_data_ans)  # 답변 리스트

res = dict(zip(sent_tokens, sent_tokens_ans))
```

이렇게 하면 키는 질문 문장, 값은 그에 대응하는 답변이다. 챗봇이 사용자 입력과 가장 유사한 질문을 찾으면, 딕셔너리에서 해당 답변을 꺼내 돌려주는 구조다.

### TF-IDF 코사인 유사도 응답 함수

`response()` 함수는 day 6와 거의 같다. 사용자 입력을 `sent_tokens` 끝에 임시로 붙이고, 전체를 TF-IDF 벡터화한 뒤 마지막 벡터(입력)와 나머지 사이의 코사인 유사도를 구한다. 가장 가까운 인덱스를 찾아 `res` 딕셔너리에서 답을 가져온다.

```python
def response(user_response):
    sent_tokens.append(user_response)

    TfidfVec = TfidfVectorizer(tokenizer=LemNormalize, stop_words='english')
    tfidf = TfidfVec.fit_transform(sent_tokens)

    vals = cosine_similarity(tfidf[-1], tfidf)
    idx = vals.argsort()[0][-2]
    flat = vals.flatten()
    flat.sort()
    req_tfidf = flat[-2]

    if req_tfidf == 0:
        return "I am sorry! I don't understand you", req_tfidf
    else:
        bot_response = res[sent_tokens[idx]]
        return bot_response, req_tfidf
```

day 6 코사인 유사도 챗봇과 달라진 점은 `sent_tokens[idx]`를 바로 반환하는 게 아니라, 이를 키로 `res` 딕셔너리를 조회해서 답변을 꺼낸다는 것이다. 결국 사용자 질문 → 유사한 FAQ 질문 탐색 → 대응 답변 반환 순서다.

`LemNormalize` 함수는 소문자 변환, 구두점 제거, 단어 토큰화, 품사 기반 표제어 추출을 한 번에 처리한다. POS 태그를 WordNet 형식으로 변환해서 lemmatize할 때 정확도를 높인다.

```python
def get_wordnet_pos(tag):
    if tag.startswith('J'):
        return 'a'
    elif tag.startswith('V'):
        return 'v'
    elif tag.startswith('R'):
        return 'r'
    else:
        return 'n'

def LemTokens(tokens):
    pos_tags = pos_tag(tokens)
    return [lemmer.lemmatize(word, get_wordnet_pos(tag)) for word, tag in pos_tags]
```

## 2. Doc2Vec 기반 챗봇

같은 데이터로 Doc2Vec 방식도 시도했다. gensim의 `Doc2Vec`은 word2vec을 문서 단위로 확장한 모델이다. 각 문서(문장)를 고유한 태그와 함께 `TaggedDocument`로 감싸서 학습 데이터를 만든다.

```python
tagged_data = [
    TaggedDocument(words=word_tokenize(_d.lower()), tags=[str(i)])
    for i, _d in enumerate(sent_tokens)
]
```

학습은 100 에폭, 벡터 차원 20, 학습률 0.025로 진행했다. `dm=1`은 CBOW 방식과 유사하게 주변 단어로 타겟 단어를 예측하는 방식이다.

```python
model = Doc2Vec(vector_size=20, alpha=0.025, min_alpha=0.00025, min_count=1, dm=1)
model.build_vocab(tagged_data)

for epoch in range(100):
    model.train(tagged_data, total_examples=model.corpus_count, epochs=100)
    model.alpha -= 0.0002
    model.min_alpha = model.alpha
```

응답 함수는 사용자 입력을 `infer_vector`로 벡터화한 뒤, `most_similar`로 가장 유사한 문서 태그를 찾아 답변을 반환한다.

```python
def doc2vec_response(user_response):
    user_tokens = word_tokenize(user_response.lower())
    v1 = model.infer_vector(user_tokens)
    similar_doc = model.dv.most_similar(positive=[v1], topn=1)
    return sent_tokens_ans[int(similar_doc[0][0])], similar_doc[0][1]
```

### Doc2Vec의 한계

실제 테스트에서 Doc2Vec는 TF-IDF 방식보다 부정확했다. "How much is the price?"를 입력했을 때 가장 유사한 문서로 `why do the costs vary from day to day?`(인덱스 31)를 찾아 `The rates may vary as per availability and demand.`를 반환했다. 가격 관련 질문이긴 하지만 체감상 정확도가 낮았다.

이유는 명확하다. Doc2Vec는 충분히 큰 코퍼스에서 의미 있는 벡터 표현을 학습한다. 47개 문장짜리 FAQ 데이터는 너무 작다. 반면 TF-IDF는 단어 빈도 기반이라 소규모 데이터에서도 어느 정도 작동한다. 데이터 규모에 따라 어떤 방식이 적합한지가 달라진다는 점을 확인한 셈이다.

또 하나 주목할 부분은 gensim 4.3.2와 최신 scipy의 호환 문제였다. `scipy.linalg`에서 `triu`를 가져오는 코드가 최신 scipy에서는 동작하지 않아 `ImportError`가 발생했다. 이런 환경 의존성 문제는 실습 중에 종종 마주치는 현실이다.

## 3. Word2Vec 직접 구현

`4월 15일 (1).ipynb`에서는 Word2Vec를 처음부터 만들었다. gensim을 쓰지 않고 TensorFlow로 신경망을 직접 구성해서, 임베딩이 어떻게 학습되는지 과정 자체를 확인하는 게 목적이었다.

### 데이터 준비

왕, 왕비, 왕자, 공주 같은 성별과 역할이 연관된 10개 문장을 코퍼스로 사용했다.

```python
corpus = [
    'king is a strong man',
    'queen is a wise woman',
    'boy is a young man',
    'girl is a young woman',
    'prince is a young king',
    'princess is a young queen',
    'man is strong',
    'woman is pretty',
    'prince is a boy will be king',
    'princess is a girl will be queen'
]
```

`is`, `a`, `will`, `be` 같은 stop words를 제거한 뒤, 남은 단어로 어휘 집합을 만들었다. 각 단어에 정수 인덱스를 부여하는 `word2int` 딕셔너리도 만들었다.

```python
word2int = {word: i for i, word in enumerate(words)}
```

### Skip-Gram 학습 데이터 생성

window size 2로 각 단어와 주변 단어를 `[입력, 타겟]` 쌍으로 만들었다. 총 100개의 쌍이 생성됐다.

```python
WINDOW_SIZE = 2
data = []
for sentence in sentences:
    for idx, word in enumerate(sentence):
        for neighbor in sentence[max(idx - WINDOW_SIZE, 0) : min(idx + WINDOW_SIZE, len(sentence)) + 1]:
            if neighbor != word:
                data.append([word, neighbor])
```

각 단어는 원핫 인코딩으로 변환해서 학습 데이터를 구성했다.

### 신경망 구조

임베딩 차원을 2로 설정했다. 2차원이면 학습 결과를 그래프로 바로 시각화할 수 있어서다.

- 입력: 원핫 벡터 (어휘 크기 = 15)
- 은닉층 W1: `15 × 2` 행렬 — 이게 실제 임베딩 가중치
- 출력층 W2: `2 × 15` 행렬 + softmax

```python
W1 = tf.Variable(tf.random.normal([ONE_HOT_DIM, EMBEDDING_DIM]))  # 15 x 2
b1 = tf.Variable(tf.random.normal([1]))
W2 = tf.Variable(tf.random.normal([EMBEDDING_DIM, ONE_HOT_DIM]))  # 2 x 15
b2 = tf.Variable(tf.random.normal([1]))

optimizer = tf.optimizers.SGD(learning_rate=0.05)
```

학습은 20000 이터레이션, `GradientTape`로 손실을 계산하고 경사를 직접 적용했다. 손실 함수는 크로스 엔트로피다.

```python
for i in range(20000):
    with tf.GradientTape() as tape:
        hidden_layer = tf.add(tf.matmul(X_train, W1), b1)
        prediction = tf.nn.softmax(tf.add(tf.matmul(hidden_layer, W2), b2))
        loss_value = tf.reduce_mean(-tf.reduce_sum(Y_train * tf.math.log(prediction), axis=[1]))

    grads = tape.gradient(loss_value, [W1, b1, W2, b2])
    optimizer.apply_gradients(zip(grads, [W1, b1, W2, b2]))
```

손실은 iteration 0에서 약 7.16, 18000에서 약 2.08까지 떨어졌다.

### 임베딩 결과

학습이 끝난 뒤 W1 + b1을 각 단어의 벡터 좌표로 사용해 2D 그래프에 찍었다.

| 단어 | x1 | x2 |
|---|---|---|
| boy | 0.04 | -1.85 |
| girl | 0.04 | -1.85 |
| man | 2.29 | -2.54 |
| woman | 3.50 | -3.06 |
| king | 1.33 | -2.04 |
| queen | 1.74 | -2.05 |
| prince | -1.90 | -3.52 |
| princess | -1.83 | -3.46 |

boy와 girl이 거의 같은 위치에 놓이고, man과 woman이 근처 군집을 이루며, king/queen이 별도 그룹을 형성하는 경향이 보인다. 10개 문장, 15개 단어짜리 코퍼스임에도 의미 유사성이 공간 거리에 반영되기 시작했다.

## 4. 4월 15일 수업에서 얻은 것

이날 수업은 세 가지가 교차했다.

하나는 같은 챗봇 문제를 두 가지 방법으로 풀어보는 경험이었다. TF-IDF 코사인 유사도는 소규모 데이터에서 바로 작동했고, Doc2Vec는 작동은 했지만 데이터가 부족해 정확도가 낮았다. 어떤 방법이 무조건 좋은 게 아니라, 데이터 규모와 목적에 맞는 방법을 골라야 한다는 걸 다시 확인했다.

다른 하나는 word2vec의 내부 구조를 직접 짜본 경험이다. gensim 한 줄로 끝나는 작업을 원핫 인코딩, 윈도우 슬라이딩, 역전파까지 분해해서 구현하면 "임베딩이 학습된다"는 말의 실제 의미가 달라진다.

마지막으로 day 6까지는 텍스트를 어떻게 숫자로 바꾸는지에 집중했다면, 이날부터는 그 표현이 의미 공간에서 어떤 구조를 갖는지로 관심이 옮겨가기 시작했다.
