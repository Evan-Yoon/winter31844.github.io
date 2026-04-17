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

2026년 4월 15일 수업은 두 파트로 나뉘었다. 앞부분은 day 6 코사인 유사도 챗봇 구조에 질문-답변 딕셔너리를 결합한 호텔 FAQ 챗봇이었다. Doc2Vec 방식과 비교해 데이터 규모가 성능에 어떤 영향을 주는지 확인했다. 뒷부분은 Word2Vec를 TensorFlow로 직접 구현해서 임베딩이 실제로 어떻게 학습되는지 확인했다.

## 1. 호텔 FAQ 챗봇: 질문-답변 딕셔너리 구조

day 6 코사인 유사도 챗봇과 달라진 점은 데이터 구조다.  
**day 6**: 단일 문서에서 가장 유사한 문장을 찾아 그대로 반환  
**day 7**: 질문 파일과 답변 파일을 따로 불러와 딕셔너리로 연결 → 질문과 유사한 FAQ를 찾으면 대응하는 답변을 반환

```python
import nltk

# 질문 파일과 답변 파일을 각각 로드
sent_tokens     = nltk.sent_tokenize(raw_data)      # 질문 리스트
sent_tokens_ans = nltk.sent_tokenize(raw_data_ans)  # 답변 리스트

# 질문을 key, 답변을 value로 연결
res = dict(zip(sent_tokens, sent_tokens_ans))
# {'How much is the price?': 'Our standard room starts at $120 per night.', ...}
```

### TF-IDF 코사인 유사도 응답 함수

```python
def response(user_response):
    # 사용자 입력을 질문 목록 끝에 임시 추가
    sent_tokens.append(user_response)
    
    # 전체 질문(입력 포함)을 TF-IDF 벡터로 변환
    TfidfVec = TfidfVectorizer(tokenizer=LemNormalize, stop_words='english')
    tfidf = TfidfVec.fit_transform(sent_tokens)
    
    # 입력 벡터(마지막)와 나머지 질문들의 코사인 유사도
    vals = cosine_similarity(tfidf[-1], tfidf)
    idx  = vals.argsort()[0][-2]   # 가장 유사한 FAQ 질문 인덱스
    
    flat = vals.flatten()
    flat.sort()
    req_tfidf = flat[-2]  # 두 번째로 높은 유사도 값 (자기 자신 제외)
    
    sent_tokens.remove(user_response)
    
    if req_tfidf == 0:
        return "I am sorry! I don't understand you", req_tfidf
    else:
        # 유사한 FAQ 질문에 대응하는 답변을 딕셔너리에서 조회
        bot_response = res[sent_tokens[idx]]
        return bot_response, req_tfidf
```

day 6과의 차이: `sent_tokens[idx]`를 그대로 반환하는 게 아니라, 이를 키로 `res` 딕셔너리를 조회해 대응하는 답변을 가져온다.

`LemNormalize`는 소문자 변환, 구두점 제거, 단어 토큰화, 품사 기반 표제어 추출을 처리한다.

```python
def get_wordnet_pos(tag):
    """NLTK POS 태그를 WordNet 형식으로 변환 — lemmatize 정확도를 높이기 위함"""
    if tag.startswith('J'): return 'a'   # 형용사
    elif tag.startswith('V'): return 'v' # 동사
    elif tag.startswith('R'): return 'r' # 부사
    else: return 'n'                     # 명사(기본값)

def LemTokens(tokens):
    pos_tags = pos_tag(tokens)
    return [lemmer.lemmatize(word, get_wordnet_pos(tag)) for word, tag in pos_tags]
```

## 2. Doc2Vec 기반 챗봇

**이게 뭔지**: Word2Vec을 단어 단위에서 문서(문장) 단위로 확장한 모델. 각 문서 전체를 하나의 벡터로 표현한다.  
**왜 시도하는가**: TF-IDF는 단어 빈도 기반이라 의미적 유사성을 잡기 어렵다. Doc2Vec은 문맥을 학습하므로 더 정확할 것이라는 기대였다.

```python
from gensim.models.doc2vec import Doc2Vec, TaggedDocument
from nltk.tokenize import word_tokenize

# 각 문장에 고유 태그 부여 — Doc2Vec 학습의 핵심
# TaggedDocument(words=토큰 리스트, tags=[식별자])
tagged_data = [
    TaggedDocument(words=word_tokenize(_d.lower()), tags=[str(i)])
    for i, _d in enumerate(sent_tokens)
]

# 학습
model = Doc2Vec(vector_size=20, alpha=0.025, min_alpha=0.00025, min_count=1, dm=1)
# dm=1: CBOW 방식 — 주변 단어로 중심 단어(문서) 예측
model.build_vocab(tagged_data)

for epoch in range(100):
    model.train(tagged_data, total_examples=model.corpus_count, epochs=100)
    model.alpha    -= 0.0002  # 에폭마다 학습률 감소
    model.min_alpha = model.alpha
```

응답 함수:

```python
def doc2vec_response(user_response):
    # 입력 문장을 벡터로 추론
    user_tokens = word_tokenize(user_response.lower())
    v1 = model.infer_vector(user_tokens)
    
    # 가장 유사한 문서 태그 검색
    similar_doc = model.dv.most_similar(positive=[v1], topn=1)
    
    # 태그(인덱스)를 이용해 답변 반환
    return sent_tokens_ans[int(similar_doc[0][0])], similar_doc[0][1]
```

### Doc2Vec의 한계

실제 테스트에서 Doc2Vec는 TF-IDF보다 부정확했다. "How much is the price?"를 입력했을 때 인덱스 31 문장(`why do the costs vary from day to day?`)을 찾아 관련 답변을 반환했지만, TF-IDF는 더 직접적인 질문을 찾아냈다.

**이유**: Doc2Vec는 대규모 코퍼스에서 의미 있는 벡터를 학습한다. 47개 문장짜리 FAQ 데이터는 너무 작다. TF-IDF는 단어 빈도 기반이라 소규모 데이터에서도 잘 작동한다.

<div class="compare-wrap">
  <div class="compare-card compare-before">
    <div class="compare-label">TF-IDF</div>
    <div class="compare-body">
      <p><strong>필요한 데이터:</strong> 적어도 가능</p>
      <p><strong>소규모 FAQ 정확도:</strong> 높음</p>
      <p><strong>의미 유사성 파악:</strong> 제한적</p>
    </div>
  </div>
  <div class="compare-card compare-after">
    <div class="compare-label">Doc2Vec</div>
    <div class="compare-body">
      <p><strong>필요한 데이터:</strong> 대규모 코퍼스 필요</p>
      <p><strong>소규모 FAQ 정확도:</strong> 낮음</p>
      <p><strong>의미 유사성 파악:</strong> 학습 시 가능</p>
    </div>
  </div>
</div>

추가로, gensim 4.3.2와 최신 scipy 사이에 `scipy.linalg`의 `triu` 함수 경로가 변경되어 `ImportError`가 발생했다. 라이브러리 버전 호환성 문제는 실습 중 흔히 만나는 현실이다.

## 3. Word2Vec 직접 구현

gensim 한 줄로 끝나는 작업을 TensorFlow로 직접 구현했다. 목적은 "임베딩이 학습된다"는 말의 실제 의미를 코드로 확인하는 것이었다.

### 데이터 준비

왕, 왕비, 왕자 등 성별·역할이 연관된 10개 문장을 코퍼스로 사용했다.

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

`is`, `a`, `will`, `be` 같은 불용어 제거 후 남은 단어로 어휘 집합을 만들었다.

```python
word2int = {word: i for i, word in enumerate(words)}
# {'king': 0, 'strong': 1, 'man': 2, 'queen': 3, ...}
```

### Skip-Gram 학습 데이터 생성

**Skip-Gram의 원리**: 중심 단어로 주변 단어들을 예측한다. window size 2면 앞뒤 2개 단어씩이 타겟이 된다.

```python
WINDOW_SIZE = 2
data = []
for sentence in sentences:
    for idx, word in enumerate(sentence):
        for neighbor in sentence[max(idx - WINDOW_SIZE, 0) : min(idx + WINDOW_SIZE, len(sentence)) + 1]:
            if neighbor != word:
                data.append([word, neighbor])
# 결과: [['king', 'strong'], ['king', 'man'], ['strong', 'king'], ...]
```

각 단어는 원핫 벡터로 변환해서 모델 입력으로 사용한다.

### 신경망 구조

임베딩 차원을 2로 설정한 이유: 2차원이면 학습 결과를 그래프로 바로 시각화할 수 있다.

```
입력: 원핫 벡터 (15차원, 어휘 크기)
   ↓ W1 (15×2) — 이 가중치 행렬이 곧 임베딩
은닉층: 2차원 벡터 (저차원 임베딩)
   ↓ W2 (2×15) + softmax
출력: 어휘 크기만큼의 확률 분포 (타겟 단어 예측)
```

```python
import tensorflow as tf

W1 = tf.Variable(tf.random.normal([ONE_HOT_DIM, EMBEDDING_DIM]))  # 15×2
b1 = tf.Variable(tf.random.normal([1]))
W2 = tf.Variable(tf.random.normal([EMBEDDING_DIM, ONE_HOT_DIM]))  # 2×15
b2 = tf.Variable(tf.random.normal([1]))

optimizer = tf.optimizers.SGD(learning_rate=0.05)

for i in range(20000):
    with tf.GradientTape() as tape:
        hidden_layer = tf.add(tf.matmul(X_train, W1), b1)
        prediction   = tf.nn.softmax(tf.add(tf.matmul(hidden_layer, W2), b2))
        # 크로스 엔트로피: 예측 확률 분포와 실제 단어(원핫) 사이의 차이
        loss_value   = tf.reduce_mean(-tf.reduce_sum(Y_train * tf.math.log(prediction), axis=[1]))
    
    grads = tape.gradient(loss_value, [W1, b1, W2, b2])
    optimizer.apply_gradients(zip(grads, [W1, b1, W2, b2]))
```

손실: iteration 0에서 약 7.16 → 18000에서 약 2.08

### 임베딩 결과

학습된 W1을 각 단어의 2D 좌표로 사용해 시각화했다.

<div class="compare-wrap">
  <div class="compare-card">
    <div class="compare-label">boy / girl</div>
    <div class="compare-body">
      <p>boy: (0.04, -1.85)</p>
      <p>girl: (0.04, -1.85)</p>
    </div>
  </div>
  <div class="compare-card">
    <div class="compare-label">man / woman</div>
    <div class="compare-body">
      <p>man: (2.29, -2.54)</p>
      <p>woman: (3.50, -3.06)</p>
    </div>
  </div>
  <div class="compare-card">
    <div class="compare-label">king / queen</div>
    <div class="compare-body">
      <p>king: (1.33, -2.04)</p>
      <p>queen: (1.74, -2.05)</p>
    </div>
  </div>
  <div class="compare-card">
    <div class="compare-label">prince / princess</div>
    <div class="compare-body">
      <p>prince: (-1.90, -3.52)</p>
      <p>princess: (-1.83, -3.46)</p>
    </div>
  </div>
</div>

boy와 girl이 거의 같은 위치에 놓이고, king/queen이 별도 군집을 형성한다. 10개 문장, 15개 단어짜리 소규모 코퍼스임에도 의미 유사성이 공간 거리에 반영됐다.

---

## 핵심 정리

<div class="compare-wrap">
  <div class="compare-card compare-before">
    <div class="compare-label">TF-IDF + 코사인 유사도</div>
    <div class="compare-body">
      <p><strong>방식:</strong> 단어 빈도 기반 유사 질문 검색</p>
      <p><strong>적합한 규모:</strong> 소규모 (수십~수백 문장)</p>
    </div>
  </div>
  <div class="compare-card compare-after">
    <div class="compare-label">Doc2Vec</div>
    <div class="compare-body">
      <p><strong>방식:</strong> 문서 벡터 의미 유사도 검색</p>
      <p><strong>적합한 규모:</strong> 대규모 코퍼스 필요</p>
    </div>
  </div>
</div>

Word2Vec 직접 구현에서 확인한 것: gensim `Word2Vec` 한 줄이 내부적으로는 원핫 인코딩 → 행렬 곱 → 소프트맥스 → 역전파 전체를 처리한다. W1 행렬이 바로 임베딩 벡터들이다.
