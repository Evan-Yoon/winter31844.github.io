---
title: "[Intel NLP] Day 4: 키워드 분석부터 텍스트 임베딩까지"
slug: intel-nlp-day4-keyword-analysis-text-embedding
date: 2026-04-10
author: Evan Yoon
category: study
subcategory: bootcamp
description: "한국어 형태소 분석부터 단어 임베딩, 신경망까지 NLP의 전체 파이프라인을 배웠다."
thumbnail: ""
tags:
  - NLP
  - 자연어처리
  - Word2Vec
  - 임베딩
  - 유사도
  - 텍스트마이닝
  - RNN
  - LSTM
  - Python
readTime: 15
series: "Intel NLP 과정"
seriesOrder: 4
featured: false
draft: false
toc: true
---

지난 3일간은 감정 분석이라는 구체적 문제를 다뤘다면, 오늘은 한 발 물러서서 "텍스트를 어떻게 이해하고 처리할 것인가"의 다양한 관점을 배웠다. 한국어 데이터에서 키워드를 뽑아내는 것부터 시작해서, 단어를 숫자의 벡터로 표현하고, 그 벡터들 사이의 유사도를 계산하고, 결국 신경망으로 의미를 학습하는 과정까지. 오늘만큼 "NLP의 전체 파이프라인"을 느낀 날이 없었다.

## 오늘 다룬 범위

1. **한국어 형태소 분석**: Mecab으로 문장을 분해
2. **키워드 추출 & 빈도 분석**: 영화 리뷰 데이터에서 핵심 단어 발굴
3. **텍스트 벡터화**: 텍스트를 숫자로 변환 (BoW, TF-IDF)
4. **유사도 계산**: 텍스트와 텍스트의 거리를 측정하는 여러 방식
5. **단어 임베딩**: Word2Vec으로 단어의 의미를 벡터에 담기
6. **신경망 모델**: RNN, LSTM으로 시퀀스 학습

---

## 핵심 개념 정리

### 1. 한국어 형태소 분석 — 문장을 쪼개기

어제까지 배운 Twitter 영어 데이터는 공백으로 단어가 나뉜다. 하지만 한국어는 다르다. 문장을 의미 있는 단위로 쪼개려면 형태소 분석이 필수다.

```python
from konlpy.tag import Mecab

tagger = Mecab()
sentence = '언제나 현재에 집중할 수 있다면 행복할 것이다.'
result = tagger.pos(sentence)

# 출력
# [('언제나', 'MAG'), ('현재', 'NNG'), ('에', 'JKB'), 
#  ('집중', 'NNG'), ('할', 'XSV'), ('수', 'NNG'), ...]
```

**형태소란?** 더 이상 분석할 수 없는 최소 의미 단위다. "집중할"은 "집중(동사) + 할(보조동사)"로 분해된다.

**품사 태그의 의미:**
- **NNG**: 일반 명사 (현재, 집중)
- **VV**: 동사 (집중하다)
- **MAG**: 일반 부사 (언제나)
- **JKB**: 부사격 조사 (에, 에서)

형태소 분석 없이는 한국어 자연어 처리가 불가능하다고 배웠다.

### 2. 불용어(Stopwords) 정의 및 제거

모든 단어가 의미 있는 건 아니다. 영화 리뷰에서 "영화", "전" (이전), "난" (나) 같은 단어는 자주 나타나지만 실제로 감정을 드러내진 않는다.

```python
stop_words = ['영화', '전', '난', '일', '걸', '뭐', '줄', '만', '건', 
              '분', '개', '끝', '하다', '되다']
```

선생님이 강조했던 부분: "불용어는 도메인에 따라 다르다." 금융 데이터라면 다른 불용어가 필요하다.

### 3. 명사 추출 및 빈도 분석 — 핵심 키워드 발굴

네이버 영화 리뷰 약 100,000건을 분석했다. 먼저 명사만 추출하고 불용어를 제거했다.

```python
from konlpy.tag import Mecab
from collections import Counter

tagger = Mecab()
nouns = []

for review in reviews:
    for noun in tagger.nouns(review):
        if noun not in stop_words:
            nouns.append(noun)

# 빈도 계산
nouns_counter = Counter(nouns)
top_nouns = dict(nouns_counter.most_common(50))

# 결과
# {'연기': 9174, '최고': 8811, '평점': 8513, '스토리': 7165, '생각': 6919, ...}
```

**이게 중요한 이유:** 50개의 단어로 영화 리뷰의 핵심이 보인다. 사람들이 영화를 평가할 때 뭐에 집중하는지 알 수 있다.

### 4. 시각화 — 데이터를 눈으로 이해하기

같은 데이터를 여러 방식으로 시각화했다. 각각이 다른 인사이트를 준다.

#### 4-1. 수평 막대 그래프

```python
import matplotlib.pyplot as plt
import numpy as np

y_pos = np.arange(len(top_nouns))
plt.barh(y_pos, top_nouns.values())
plt.yticks(y_pos, top_nouns.keys())
plt.show()
```

가장 전통적이고 읽기 쉽다. 정확한 숫자를 비교할 때 좋다.

#### 4-2. WordCloud

```python
from wordcloud import WordCloud

wc = WordCloud(background_color='white', 
               font_path='./font/NanumBarunGothic.ttf')
wc.generate_from_frequencies(top_nouns)
plt.imshow(wc)
plt.show()
```

**신기한 점:** 자주 나타나는 단어가 크게 그려진다. 한눈에 분포를 파악할 수 있다.

#### 4-3. 트리맵(Treemap)

```python
import squarify
import matplotlib as mpl

colors = [mpl.cm.Blues(norm(value)) for value in top_nouns.values()]
squarify.plot(label=top_nouns.keys(), sizes=top_nouns.values(), 
              color=colors, alpha=0.7)
```

WordCloud보다 더 체계적이다. 각 단어가 차지하는 "영역"의 크기로 빈도를 표현한다.

### 5. WordNet과 의미론적 분석 — 단어의 관계 이해하기

여기서 흥미로운 개념을 배웠다. 컴퓨터가 단어의 뜻을 어떻게 이해할까?

```python
import nltk
from nltk.corpus import wordnet as wn

# 같은 의미의 단어들 (Synset)
synsets = wn.synsets('car')
# [Synset('car.n.01'), Synset('car.n.02'), ...]

# 정의 확인
definition = wn.synset('car.n.01').definition()
# 'a motor vehicle with four wheels; usually propelled by an internal combustion engine'

# 동의어와 반의어
relative_synset = wn.synset('relative.a.01')
synonyms = [lemma.name() for lemma in relative_synset.lemmas()]
# ['relative', 'comparative']

antonym = relative_synset.lemmas()[0].antonyms()[0].name()
# 'absolute'
```

**의미론적 유사도:**

```python
dog = wn.synset('dog.n.01')
cat = wn.synset('cat.n.01')

similarity = dog.path_similarity(cat)
# 0.2 (개와 고양이는 같은 상위 개념 "동물"을 가짐)
```

이 방식의 한계: 데이터베이스에 등록되지 않은 새로운 단어는 처리할 수 없다. 이게 바로 Word2Vec이 나온 이유다.

### 6. 텍스트 벡터화 — 텍스트를 숫자로 변환

#### 6-1. Bag of Words (BoW)

```python
from sklearn.feature_extraction.text import CountVectorizer

s1 = 'The iPhone was made by a company called Apple.'
s2 = 'I love Apple iPhone.'
s3 = 'Facebook was created by Meta company.'

corpus = [s1, s2, s3]
vectorizer = CountVectorizer(stop_words='english')
bow = vectorizer.fit_transform(corpus)

# 결과는 3×N 행렬 (3개 문서, N개 고유 단어)
```

**BoW의 원리:** 각 단어가 나타난 횟수를 세는 것. 간단하지만 놓치는 게 있다.

```
s1: [1, 0, 1, 1, 0, 1, ...] (Apple, iPhone, made, ... 빈도)
s2: [1, 1, 1, 0, 0, 0, ...] (Apple, iPhone, love, ... 빈도)
```

**단점:** "iPhone"이 1번, "Apple"이 1번 나타났으니 같은 가중치? 아니다. 자주 나타나는 단어("the")와 드물게 나타나는 단어("iPhone")를 구분해야 한다.

#### 6-2. TF-IDF

```python
from sklearn.feature_extraction.text import TfidfVectorizer

vectorizer = TfidfVectorizer()
tfidf = vectorizer.fit_transform(corpus)
```

어제도 배웠지만, 오늘은 더 명확해졌다.

- **TF (Term Frequency)**: 문서 내 단어 빈도
- **IDF (Inverse Document Frequency)**: 전체 문서에서 얼마나 드문가

```
TF-IDF = (문서 내 출현 횟수 / 문서 전체 단어 수) × log(전체 문서 수 / 해당 단어 포함 문서 수)
```

**결과**: "the"처럼 모든 문서에 나타나는 단어는 낮은 점수, "iPhone"처럼 특정 문서에만 나타나는 단어는 높은 점수.

### 7. 유사도 계산 — 텍스트 간 거리 측정

이제 벡터로 변환된 텍스트들 사이의 "거리"를 측정한다. 선택지가 여러 개다.

#### 7-1. 내적 (Dot Product)

```python
import numpy as np

s1_bow = np.array(df.iloc[0])
s2_bow = np.array(df.iloc[1])

dot_product = np.dot(s1_bow, s2_bow)
# 또는 sum(s1_bow * s2_bow)
```

**문제:** 벡터가 크면 내적도 크다. 절대 크기에 영향을 받는다.

#### 7-2. 유클리디안 거리

```python
euclidean_distance = np.linalg.norm(s1_bow - s2_bow)
```

**거리 = √((x₁-x₂)² + (y₁-y₂)² + ...)**

직관적이지만, 고차원에서는 "거리"의 의미가 희석된다고 배웠다.

#### 7-3. 코사인 유사도 — 가장 많이 쓰인다

```python
from sklearn.metrics.pairwise import cosine_similarity

similarity_matrix = cosine_similarity(tfidf)

# 또는 수동 계산
cos_sim = np.dot(s1_bow, s2_bow) / (np.linalg.norm(s1_bow) * np.linalg.norm(s2_bow))
```

**원리:** 두 벡터가 이루는 각도를 계산한다.

**특징:**
- -1 ~ 1 사이의 값 (1이 가장 유사)
- 벡터의 방향만 고려하고 크기는 무시
- 텍스트 길이가 달라도 공정하게 비교

실제 결과:
```
s1 vs s2: 0.486 (그나마 유사)
s1 vs s3: 0.000 (완전히 다름)
```

#### 7-4. 자카드 유사도

```python
def jaccard_similarity(set1, set2):
    intersection = len(set1 & set2)
    union = len(set1 | set2)
    return intersection / union if union > 0 else 0
```

**원리:** 교집합 / 합집합

단어의 빈도는 무시하고 "어떤 단어가 공존하는가"만 본다. 이미지 검색 같은 분야에서 효과적이라고 했다.

### 8. 차원 축소 (SVD) — 데이터를 단순화하기

고차원 데이터(예: 30,000개 특성)를 2차원 또는 3차원으로 축소해서 시각화할 수 있다.

```python
from numpy.linalg import svd

U, S, Vt = svd(tfidf_matrix.toarray())

# 2차원으로 축소
U_reduced = U[:, :2]

# 시각화
import matplotlib.pyplot as plt
plt.scatter(U_reduced[:, 0], U_reduced[:, 1])
plt.show()
```

**아이디어:** 행렬을 분해해서 가장 중요한 "성분"만 보존한다. 계산량을 크게 줄이면서도 핵심 정보는 유지한다.

### 9. Word2Vec — 단어의 의미를 벡터로 학습

WordNet은 사람이 만든 데이터베이스다. 새로운 단어나 신조어를 처리할 수 없다. Word2Vec은 다르다. 대규모 코퍼스를 학습해서 단어 간 관계를 자동으로 파악한다.

```python
from gensim.models import Word2Vec

sentences = [
    '나는 아침에 커피를 마신다',
    '따뜻한 커피를 마시면 하루를 시작할 준비가 된다',
    '나는 점심에 국밥을 먹는다'
]

# 모델 학습
model = Word2Vec(sentences, window=5, min_count=1, sg=0)

# window=5: 특정 단어 앞뒤 5개 단어를 "컨텍스트"로 본다
# min_count=1: 1회 이상 나타난 모든 단어 포함
# sg=0: CBOW (컨텍스트로 중심 단어 예측)
```

**핵심 아이디어:** "말을 보면 그 친구들을 안다" (Tell me who you go with, and I'll tell you who you are.)

- "커피"는 "아침", "따뜻한", "마시다"와 자주 함께 나타난다
- 그래서 Word2Vec 벡터에서 이들은 가깝다

```python
# 벡터 접근
word_vector = model.wv['커피']  # 100차원 벡터

# 유사도 계산
similarity = model.wv.similarity('커피', '차')
# 약 0.6 (관련성 있음)
```

**BoW/TF-IDF와의 차이:**
- BoW: 문서 × 단어 행렬 (sparse, 희소)
- Word2Vec: 단어 × 임베딩 벡터 (dense, 밀집)
- BoW는 단어 존재 여부, Word2Vec은 단어의 의미

### 10. RNN/LSTM — 시퀀스 학습

이제 진짜 신경망이다. 여기까지는 정적인 벡터들이었다면, RNN은 "순서"를 고려한다.

```python
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Embedding, LSTM, Dense

model = Sequential([
    Embedding(vocab_size, 100, input_length=seq_length-1),
    LSTM(50),
    Dense(vocab_size, activation='softmax')
])

model.compile(optimizer='adam', loss='categorical_crossentropy')
model.fit(X_train, y_train, epochs=10)
```

**각 층의 의미:**
- **Embedding**: One-hot 벡터 (예: [0,1,0,0,...])를 100차원 벡터로 변환
  - 이게 바로 Word2Vec 같은 임베딩을 자동으로 학습하는 것
- **LSTM**: 긴 시퀀스의 의존성을 학습
  - 예: "커피를 마시면" 이 문장에서 "마시면"을 예측할 때, "커피"라는 정보를 오래 기억해야 함
- **Dense**: 최종 분류 층

**목표:** 다음 단어 예측. 예: "나는 아침에 __를 마신다" → "커피"를 예측

#### RNN vs LSTM vs GRU

```python
# GRU는 더 간단한 버전
from tensorflow.keras.layers import GRU

model = Sequential([
    Embedding(vocab_size, 100, input_length=seq_length-1),
    GRU(50),
    Dense(vocab_size, activation='softmax')
])
```

**비교:**
- **RNN**: 가장 기본. 하지만 긴 시퀀스에서 "기울기 소실" 문제 발생
- **LSTM**: 복잡한 게이트로 중요한 정보를 오래 기억. 더 안정적
- **GRU**: LSTM보다 간단. 계산량 적음

---

## 헷갈렸던 점 / 실수 포인트

### 1. 형태소 분석의 양면성

```python
# Mecab은 강력하지만 모든 걸 명사로 분석할 수 있다
tagger.nouns('연기하다')  # ['연기'] (연기 = 배우의 연기)
tagger.nouns('연기가 나다')  # ['연기'] (연기 = 연기)

# 같은 글자지만 뜻은 다르다. 품사 정보 없이는 구분 불가
```

따라서 `tagger.pos()`로 품사까지 확인해야 정확하다.

### 2. BoW와 TF-IDF의 선택 기준

처음엔 "TF-IDF가 더 좋으니 항상 써야 하나?"라고 생각했다. 아니다.

```python
# BoW가 나을 때
- 짧은 문서 (트윗, SNS)
- 모든 단어가 중요한 도메인 (상품 리뷰의 모든 단어)
- 빠른 처리 필요

# TF-IDF가 나을 때
- 긴 문서 (기사, 논문)
- 흔한 단어와 중요한 단어의 구분이 필요한 경우
- 정확도가 더 중요
```

### 3. 코사인 유사도의 직관적 이해

공식을 봐도 이해 안 됐는데, 선생님이 설명해주니 깨달았다.

```
코사인 유사도 = 두 벡터가 이루는 각도의 코사인값

각도가 0° → cos(0) = 1 (완전히 같은 방향, 최대 유사)
각도가 90° → cos(90) = 0 (직각, 무관)
각도가 180° → cos(180) = -1 (반대 방향, 최대 비유사)
```

텍스트에서는 보통 0 ~ 1 범위에만 있다. (음수 경우는 드뭄)

### 4. Word2Vec의 window 파라미터

```python
# window=5일 때
"나는 [아침에] 커피를 마신다"
     ↑
컨텍스트: 나, 는, 커피, 를, 마 (앞뒤 2개씩)
```

window가 크면 먼 단어의 관계까지 학습. 작으면 가까운 단어의 관계만 학습.

### 5. Embedding 레이어의 역할

처음엔 "왜 One-hot을 또 다른 벡터로 변환하나?"라고 헷갈렸다.

```
One-hot: [0, 0, 1, 0, 0, ...]  ← 30,000 차원 (sparse)
         ↓ Embedding(vocab_size, 100)
Word2Vec: [0.2, -0.5, 0.1, ...]  ← 100 차원 (dense)
```

이 변환으로:
1. 메모리 효율 (30,000 → 100)
2. 의미 정보 포함 (One-hot은 숫자일 뿐, Embedding은 의미 담음)

---

## 복습 Q&A

<details>
<summary><strong>1. 불용어는 고정적인가?</strong></summary>

아니다. 도메인마다 다르다.

- **영화 리뷰**: '영화', '배우'는 불용어
- **의학 논문**: '환자', '치료'는 불용어 X (핵심 단어)
- **소셜 미디어**: '좋아요', '공유'는 불용어

불용어 리스트는 실험을 통해 만들어야 한다.

</details>

<details>
<summary><strong>2. WordCloud의 크기가 항상 정확한가?</strong></summary>

아니다. 시각적 표현일 뿐, 정확한 빈도는 데이터를 봐야 한다.

WordCloud는:
- 👍 한눈에 분포 파악
- 👎 정확한 숫자 비교 어려움

정확한 분석을 위해선 막대 그래프나 숫자 테이블을 함께 봐야 한다.

</details>

<details>
<summary><strong>3. 코사인 유사도가 0이면 관계가 없다는 뜻인가?</strong></summary>

그렇다. 90도 각도를 이룬다는 뜻이다. 즉, 공통 단어가 없다는 의미다.

```
s1 = "iPhone Apple"
s2 = "Facebook Meta"

cos_sim = 0 (공통 단어 없음)
```

</details>

<details>
<summary><strong>4. Word2Vec은 항상 BoW보다 좋은가?</strong></summary>

상황에 따라 다르다.

- **Word2Vec이 좋을 때**: 많은 데이터 있을 때, 새로운 단어 처리, 의미 유사도 중요
- **BoW가 좋을 때**: 데이터 적을 때, 해석 가능성 중요 (어떤 단어가 중요한지 명확)

Word2Vec은 "블랙박스"라서 왜 그런 예측을 했는지 설명하기 어렵다.

</details>

<details>
<summary><strong>5. LSTM이 모든 경우에 RNN보다 나은가?</strong></summary>

일반적으로 그렇다. 하지만 계산 비용이 크다.

- **LSTM**: 안정적, 긴 의존성 학습. 하지만 느림
- **GRU**: 거의 비슷한 성능, 더 빠름
- **RNN**: 간단, 가장 빠름. 하지만 기울기 소실 문제

시작은 간단한 RNN으로, 필요하면 LSTM/GRU로 업그레이드하는 식으로 진행한다.

</details>

---

## 한 줄 정리

한국어 텍스트에서 키워드를 추출하고, 이를 벡터로 표현하고, 벡터 간 유사도를 계산하고, 궁극적으로 신경망으로 단어의 의미를 학습하는 일련의 과정이 NLP의 기초다.

지금까지 배운 것들이 다 모여 있는 날이었다. Day 1~3에서 배운 텍스트 정제, 벡터화, 모델 평가가 여기서 모두 나타났다. 다음부터는 이런 기초 위에서 좀 더 실무적인 프로젝트나 최신 모델들(트랜스포머, BERT 등)을 배울 것 같다. 기대된다.
