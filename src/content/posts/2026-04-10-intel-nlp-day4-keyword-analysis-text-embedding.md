---
title: "[NLP] day 4: 키워드 분석부터 텍스트 임베딩까지 - Word2Vec과 신경망 기초"
slug: nlp-study-keyword-analysis-word2vec-embedding
date: 2026-04-10
author: Evan Yoon
category: study
subcategory: bootcamp
description: |
  한국어 형태소 분석, 키워드 추출부터 시작해 TF-IDF, Word2Vec, 
  그리고 RNN/LSTM 신경망까지 텍스트를 벡터로 표현하고 학습하는 전체 파이프라인을 다룬다.
  TF-IDF의 한계와 Word2Vec의 강점, 신경망이 필요한 이유를 체계적으로 정리했다.
thumbnail: ""
tags:
  - nlp
  - word2vec
  - 임베딩
  - 유사도
  - text-embedding
  - rnn
  - lstm
  - 한국어처리
  - nlp-study
readTime: 15
series: "Intel NLP 과정"
seriesOrder: 4
featured: false
draft: false
toc: true
---

Day 3까지 배운 TF-IDF는 단어를 빈도로만 본다. "curry on fire"라는 문장에서 "fire"가 재난인지, 스포츠에서 "잘하고 있다"는 의미인지를 구별하지 못한다. 이날의 핵심 질문은 하나다: **컴퓨터가 단어의 "의미"를 어떻게 이해할 수 있을까?**

## 1. 한국어 형태소 분석

**이게 뭔지**: 한국어 문장을 의미 있는 최소 단위(형태소)로 쪼개는 작업.  
**왜 필요한지**: 영어는 공백이 단어 경계지만, 한국어는 조사와 어미가 붙어있다. "집중할"은 "집중(명사) + 할(보조동사)"이다. 쪼개지 않으면 "집중할"과 "집중하다"를 다른 단어로 취급한다.

```python
from konlpy.tag import Mecab

tagger = Mecab()
sentence = '언제나 현재에 집중할 수 있다면 행복할 것이다.'

tagger.morphs(sentence)
# ['언제나', '현재', '에', '집중', '할', '수', '있', '다면', '행복', '할', '것', '이', '다', '.']

tagger.nouns(sentence)    # ['현재', '집중', '수', '행복'] — 명사만
tagger.pos(sentence)      # [('언제나', 'MAG'), ('현재', 'NNG'), ('에', 'JKB'), ...]
```

주요 품사 태그: `NNG`(일반 명사), `VV`(동사), `MAG`(부사), `JKB`(부사격 조사)

## 2. 불용어 정의 및 키워드 추출

**왜 불용어가 도메인마다 다른가**: 금융 텍스트에서 "투자", "주식"은 중요하지만, 영화 리뷰에서는 자주 나오지만 의미가 없는 단어들이다. 불용어는 고정된 목록이 아니라 분석 목적에 맞게 정의해야 한다.

```python
# 영화 리뷰 도메인 불용어 예시
stop_words = ['영화', '전', '난', '일', '걸', '뭐', '줄', '만', '건', '분']
```

네이버 영화 리뷰 약 100,000건 분석 예시:

```python
from konlpy.tag import Mecab
from collections import Counter

tagger = Mecab()
nouns = []

for review in reviews:
    for noun in tagger.nouns(review):
        if noun not in stop_words and len(noun) > 1:  # 1글자 명사 제외
            nouns.append(noun)

nouns_counter = Counter(nouns)
top_nouns = dict(nouns_counter.most_common(50))
# {'연기': 9174, '최고': 8811, '평점': 8513, '스토리': 7165, ...}
```

50개 단어만 봐도 "사람들이 영화 리뷰에서 무엇을 중요하게 보는지"가 드러난다.

## 3. 시각화 방법 비교

같은 빈도 데이터를 세 가지 방식으로 시각화할 수 있다.

```python
# 1. 수평 막대 그래프 — 숫자 비교에 정확
import matplotlib.pyplot as plt
plt.barh(list(top_nouns.keys()), list(top_nouns.values()))
plt.show()

# 2. 워드클라우드 — 분포를 한눈에
from wordcloud import WordCloud
wc = WordCloud(font_path='./font/NanumBarunGothic.ttf')
wc.generate_from_frequencies(top_nouns)
plt.imshow(wc)
plt.show()

# 3. 트리맵 — 계층적 구조를 크기로 표현
import squarify
squarify.plot(label=list(top_nouns.keys()), sizes=list(top_nouns.values()))
plt.show()
```

<div class="compare-wrap">
  <div class="compare-card">
    <div class="compare-label">막대 그래프</div>
    <div class="compare-body">
      <p><strong>장점:</strong> 정확한 수치 비교 가능</p>
      <p><strong>단점:</strong> 항목이 많으면 복잡</p>
    </div>
  </div>
  <div class="compare-card">
    <div class="compare-label">워드클라우드</div>
    <div class="compare-body">
      <p><strong>장점:</strong> 직관적, 보기 쉬움</p>
      <p><strong>단점:</strong> 정확한 숫자 파악 어려움</p>
    </div>
  </div>
  <div class="compare-card">
    <div class="compare-label">트리맵</div>
    <div class="compare-body">
      <p><strong>장점:</strong> 비율 시각화 명확</p>
      <p><strong>단점:</strong> 항목 이름이 겹칠 수 있음</p>
    </div>
  </div>
</div>

## 4. 텍스트 유사도 계산

**이게 뭔지**: 벡터로 변환된 두 텍스트 사이의 "거리" 또는 "방향 차이"를 수치화하는 것.

### 코사인 유사도 — 가장 많이 쓰이는 방법

**이게 뭔지**: 두 벡터가 이루는 각도의 코사인 값. 크기는 무시하고 방향만 비교한다.  
**왜 텍스트에 적합한가**: 긴 문서는 단어 개수가 많아서 벡터 크기도 크다. 코사인 유사도는 크기를 무시하므로 문서 길이 차이에 영향받지 않는다.

```
각도 0°  → cos(0°) = 1  → 완전히 같은 방향 (최대 유사)
각도 90° → cos(90°) = 0  → 무관
각도 180° → cos(180°) = -1 → 반대 방향
```

```python
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import TfidfVectorizer

s1 = 'The iPhone was made by Apple.'
s2 = 'I love Apple iPhone.'
s3 = 'Facebook was created by Meta.'

tfidf = TfidfVectorizer().fit_transform([s1, s2, s3])
sim = cosine_similarity(tfidf)

# s1 vs s2: 0.486 (Apple, iPhone 공유)
# s1 vs s3: 0.000 (공통 단어 없음)
```

### 유클리디안 거리와 자카드 유사도

```python
import numpy as np

# 유클리디안: 두 점 사이의 직선 거리
euclidean = np.linalg.norm(v1 - v2)

# 자카드: 교집합 / 합집합 (단어 존재 여부만 봄, 빈도 무시)
def jaccard(set1, set2):
    return len(set1 & set2) / len(set1 | set2)
```

## 5. Word2Vec — 의미를 학습하는 임베딩

**TF-IDF의 한계**: "커피"와 "카페인"이 실제로 관련 있어도, 같은 문서에 함께 등장하지 않으면 TF-IDF 벡터에서는 전혀 다른 단어로 취급된다.

**Word2Vec의 핵심 아이디어**: "비슷한 문맥에서 등장하는 단어는 비슷한 의미를 가진다." "커피"가 항상 "마시다", "아침", "카페인"과 함께 나온다면, 이 단어들의 벡터를 가깝게 학습한다.

```python
from gensim.models import Word2Vec

sentences = [
    ['나는', '아침에', '커피를', '마신다'],
    ['따뜻한', '커피를', '마시면', '하루가', '시작된다'],
    ['나는', '점심에', '국밥을', '먹는다']
]

model = Word2Vec(
    sentences,
    window=5,    # 앞뒤 5개 단어를 "문맥"으로 사용
    min_count=1, # 1회 이상 등장한 단어 포함
    sg=0         # sg=0: CBOW (문맥→중심 단어), sg=1: Skip-Gram (중심→문맥)
)

word_vector = model.wv['커피']   # 100차원 벡터
model.wv.similarity('커피', '차') # 유사도 점수 (0~1)
```

<div class="compare-wrap">
  <div class="compare-card compare-before">
    <div class="compare-label">TF-IDF</div>
    <div class="compare-body">
      <p><strong>벡터 형태:</strong> 단어 수 차원 (sparse)</p>
      <p><strong>단어 의미:</strong> 빈도만 반영</p>
      <p><strong>"커피"와 "카페인":</strong> 별개 단어</p>
      <p><strong>데이터 요구량:</strong> 적어도 가능</p>
    </div>
  </div>
  <div class="compare-card compare-after">
    <div class="compare-label">Word2Vec</div>
    <div class="compare-body">
      <p><strong>벡터 형태:</strong> 고정 차원 100~300 (dense)</p>
      <p><strong>단어 의미:</strong> 문맥 관계 반영</p>
      <p><strong>"커피"와 "카페인":</strong> 벡터 공간에서 가까움</p>
      <p><strong>데이터 요구량:</strong> 충분한 코퍼스 필요</p>
    </div>
  </div>
</div>

## 6. RNN/LSTM — 순서를 고려한 학습

**왜 필요한가**: Word2Vec은 단어의 의미를 벡터로 표현하지만, 문장 안에서의 순서를 무시한다. "나는 커피를 좋아한다"와 "커피가 나를 좋아한다"를 구별하지 못한다. RNN은 앞 단어의 정보를 뒤로 전달하면서 순서를 학습한다.

```python
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Embedding, LSTM, Dense

model = Sequential([
    # One-hot(vocab_size 차원) → 100차원 dense 벡터로 변환
    # 이 과정 자체가 Word2Vec과 같은 임베딩 학습
    Embedding(vocab_size, 100, input_length=seq_length - 1),
    
    LSTM(50),  # 이전 단어들의 정보를 상태(state)로 유지하며 다음 단어 예측
    
    Dense(vocab_size, activation='softmax')  # 다음 단어 확률 분포
])

model.compile(optimizer='adam', loss='categorical_crossentropy')
model.fit(X_train, y_train, epochs=10)
```

<div class="compare-wrap">
  <div class="compare-card">
    <div class="compare-label">RNN</div>
    <div class="compare-body">
      <p><strong>구조:</strong> 단순</p>
      <p><strong>긴 문장 처리:</strong> 기울기 소실로 취약</p>
      <p><strong>속도:</strong> 빠름</p>
      <p><strong>사용 시점:</strong> 짧은 시퀀스</p>
    </div>
  </div>
  <div class="compare-card">
    <div class="compare-label">LSTM</div>
    <div class="compare-body">
      <p><strong>구조:</strong> 복잡 (3개 게이트)</p>
      <p><strong>긴 문장 처리:</strong> 게이트로 해결</p>
      <p><strong>속도:</strong> 느림</p>
      <p><strong>사용 시점:</strong> 긴 의존성 필요</p>
    </div>
  </div>
  <div class="compare-card">
    <div class="compare-label">GRU</div>
    <div class="compare-body">
      <p><strong>구조:</strong> 중간 (2개 게이트)</p>
      <p><strong>긴 문장 처리:</strong> LSTM보다 간단</p>
      <p><strong>속도:</strong> LSTM보다 빠름</p>
      <p><strong>사용 시점:</strong> 빠른 학습 필요</p>
    </div>
  </div>
</div>

---

## 핵심 정리

```
TF-IDF (Day 3까지)
  → 단어 빈도 기반
  → 의미 관계 없음
  → 소규모 데이터에서도 동작

Word2Vec (Day 4)
  → 문맥 기반 의미 학습
  → 의미 유사 단어가 가까운 위치
  → 충분한 코퍼스 필요

RNN/LSTM (Day 4 후반)
  → 단어 순서까지 고려
  → 문장 전체 맥락 이해
  → 더 많은 데이터와 연산 필요
```

"curry on fire" 문제: TF-IDF는 "fire"라는 단어 하나를 보고 분류한다. Word2Vec은 "curry", "goal", "tournament" 같은 주변 단어들을 함께 보고 스포츠 문맥임을 인식한다. LSTM은 문장 전체의 순서를 고려해서 판단한다.
