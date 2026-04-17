---
title: "[NLP] Day 2: Twitter 감정 분석 - 데이터 전처리와 탐색"
slug: intel-nlp-day2-twitter-sentiment-preprocessing-eda
date: 2026-04-08
author: Evan Yoon
category: study
subcategory: bootcamp
description: "Twitter 데이터를 정제하고, 워드클라우드로 시각화하는 EDA를 배웠다."
thumbnail: ""
tags:
  - NLP
  - 감정분석
  - Sentiment Analysis
  - 데이터 전처리
  - 워드클라우드
  - Python
readTime: 10
series: "Intel NLP 과정"
seriesOrder: 2
featured: false
draft: false
toc: true
---

Day 1에서 텍스트 처리 기초를 배웠다면, Day 2에서는 실제 Twitter 데이터를 다룬다. 트위터 텍스트는 @멘션, 해시태그, URL, 이모지 같은 노이즈가 많다. 이것들을 정제하고 감정별 특징을 시각화하는 것이 오늘의 주제다.

## 1. 프로젝트 목표: 왜 감정 분석인가

소셜 미디어 텍스트에서 감정(긍정/부정)을 자동으로 분류하는 것이 목표다. 수작업으로 31,000개 트윗을 읽을 수는 없다. 자동화가 필요하다.

실제 활용: 브랜드 평판 모니터링, 제품 리뷰 요약, 여론 조사 대체 등.

## 2. 데이터 준비

Kaggle Twitter Sentiment Analysis 데이터셋을 사용했다.

```python
import pandas as pd

train = pd.read_csv('train.csv')
test = pd.read_csv('test.csv')

# 전처리 편의를 위해 합친다
merge = train._append(test, ignore_index=True, sort=True)
print(f"Total tweets: {len(merge)}")  # 49,159개
```

<div class="info-grid">
  <div class="info-item">
    <div class="info-title">id</div>
    <div class="info-desc">트윗 고유 번호</div>
  </div>
  <div class="info-item">
    <div class="info-title">tweet</div>
    <div class="info-desc">트윗 원문</div>
  </div>
  <div class="info-item">
    <div class="info-title">label</div>
    <div class="info-desc">0 = 부정, 1 = 긍정</div>
  </div>
</div>

`_append()` 대신 `pd.concat([train, test], ignore_index=True)`를 써도 된다. `ignore_index=True`가 없으면 인덱스 중복이 생긴다.

## 3. 텍스트 정제

트위터 원문 예시: `"@user1 I love #Python!!! Check http://t.co/xyz"`  
실제로 의미 있는 내용: `"I love Python"`

나머지는 노이즈다. 순서대로 제거한다.

### 3-1. @멘션 제거

```python
import re
import numpy as np

def remove_pattern(text, pattern):
    return re.sub(pattern, "", text)

# @로 시작하는 모든 멘션 제거
# \w* = 알파벳, 숫자, 언더스코어 0개 이상
merge['Clean_Tweets'] = np.vectorize(remove_pattern)(
    merge['tweet'],
    r"@[\w]*"
)
# "@user1 I love this!" → " I love this!"
```

`np.vectorize()`는 pandas 열 전체에 함수를 적용할 때 쓰는 방법이다. `apply()`와 유사하지만 여러 인자를 받을 수 있다.

### 3-2. 특수문자와 숫자 제거

```python
def clean_text(text):
    text = text.lower()                        # 대소문자 통일
    return re.sub("[^a-zA-Z#]", " ", text)    # 영문과 # 외 모두 공백으로

merge['Clean_Tweets'] = merge['Clean_Tweets'].apply(clean_text)
# "I LOVE Python123!!!" → "i love python   "
```

`[^a-zA-Z#]`에서 `^`는 NOT을 의미한다. 해시태그(`#`)는 주제 정보를 담고 있어 일부러 남긴다.

### 3-3. 토큰화 후 재조합

```python
tokenized_tweet = merge['Clean_Tweets'].apply(lambda x: x.split())

# 토큰화 후 다시 합치는 이유: 연속 공백 제거
for i in range(len(tokenized_tweet)):
    tokenized_tweet[i] = ' '.join(tokenized_tweet[i])
# "i love  python  " → "i love python"
```

### 전처리 순서가 중요한 이유

전처리는 단순히 지우는 작업이 아니라, **무엇을 남기고 무엇을 버릴지 결정하는 과정**이다. 순서도 중요하다.

1. `@멘션`처럼 감정과 무관한 패턴을 먼저 제거한다.
2. 그 다음 특수문자와 숫자를 정리한다.
3. 마지막에 공백을 정리하고 토큰화한다.

이 순서를 뒤섞으면 불필요한 공백이 다시 생기거나, 남겨야 할 정보까지 같이 사라질 수 있다. 이번 실습에서 해시태그 기호 `#`를 일부러 남긴 것도 같은 이유다. 해시태그는 문장 본문이 아니라 사용자가 직접 붙인 주제 힌트이기 때문이다.

## 4. 워드클라우드 시각화

**이게 뭔지**: 단어의 등장 빈도를 크기로 표현하는 시각화 도구.  
**왜 필요한지**: 수만 개의 트윗에서 어떤 단어가 많이 쓰이는지 한눈에 파악할 수 있다.

```python
from wordcloud import WordCloud
import matplotlib.pyplot as plt

# 긍정(label=1) 트윗의 모든 단어를 하나의 문자열로 합침
all_words_positive = ' '.join(
    text for text in merge[merge['label'] == 1]['Clean_Tweets']
)
all_words_negative = ' '.join(
    text for text in merge[merge['label'] == 0]['Clean_Tweets']
)

wordcloud_pos = WordCloud(width=800, height=400).generate(all_words_positive)
wordcloud_neg = WordCloud(width=800, height=400).generate(all_words_negative)

plt.figure(figsize=(16, 8))
plt.subplot(1, 2, 1)
plt.imshow(wordcloud_pos, interpolation='bilinear')
plt.title('Positive')
plt.axis('off')

plt.subplot(1, 2, 2)
plt.imshow(wordcloud_neg, interpolation='bilinear')
plt.title('Negative')
plt.axis('off')
plt.show()
```

실제 결과:
- **긍정 트윗**: love, thanks, great, happy, best가 크게 표시
- **부정 트윗**: hate, sad, bad, angry, worst가 크게 표시

단어가 클수록 그 감정 데이터에서 자주 등장했다는 뜻이다.

다만 워드클라우드는 어디까지나 탐색용 시각화다. `"happy"`가 `"great"`보다 몇 번 더 많이 나왔는지 같은 정량 비교는 어렵다. 그래서 실무에서는 워드클라우드로 먼저 감을 잡고, 빈도표나 막대그래프로 다시 확인하는 식으로 함께 해석하는 경우가 많다.

## 5. 해시태그 분석

**이게 뭔지**: 트윗의 `#태그` 부분만 추출해서 분석하는 것.  
**왜 필요한지**: 해시태그는 사용자가 직접 붙인 메타데이터다. 게시물의 주제와 감정을 명시적으로 나타낸다.

```python
def Hashtags_Extract(tweets):
    hashtags = []
    for tweet in tweets:
        # r"#(\w+)" — # 이후의 단어만 괄호로 캡처
        ht = re.findall(r"#(\w+)", tweet)
        hashtags.append(ht)
    return hashtags

ht_positive = Hashtags_Extract(merge[merge['label'] == 1]['Clean_Tweets'])
ht_negative = Hashtags_Extract(merge[merge['label'] == 0]['Clean_Tweets'])
```

정규식 `#(\w+)`: `#`을 찾고, 그 뒤의 알파벳/숫자를 `()`로 캡처한다.  
괄호가 없으면 `#AI`가 추출되고, 괄호가 있으면 `AI`만 추출된다.

### EDA에서 실제로 확인하려는 것

이번 수업의 목표는 예쁜 시각화를 만드는 것이 아니라, Day 3 모델링 전에 데이터를 이해하는 것이었다. 구체적으로는 다음을 확인하게 된다.

- 긍정과 부정이 어떤 단어를 많이 쓰는가
- 해시태그가 감정과 함께 움직이는가
- 전처리 후에도 노이즈가 남아 있는가
- 특정 표현이 한쪽 레이블에 과하게 치우쳐 있는가

즉, EDA는 모델을 만들기 전에 "이 데이터가 정말 분류 가능한 구조를 갖고 있는가"를 확인하는 단계다. 이 과정을 건너뛰면 나중에 성능이 안 나와도 원인을 찾기 어렵다.

### Day 2와 Day 3의 연결

오늘 만든 `Clean_Tweets`는 단순한 중간 산출물이 아니다. 바로 이 컬럼이 Day 3에서 `CountVectorizer`, `TfidfVectorizer`의 입력이 된다. 정리하면:

- Day 1: 텍스트 처리 도구를 배움
- Day 2: 실제 noisy text를 정제함
- Day 3: 정제된 텍스트를 숫자로 바꾸고 분류 모델을 학습함

---

## 핵심 정리

<div class="info-grid">
  <div class="info-item">
    <div class="info-title">@멘션 제거 — 사람 이름은 감정과 무관</div>
    <div class="info-desc"><code>re.sub(r"@[\w]*", "")</code></div>
  </div>
  <div class="info-item">
    <div class="info-title">특수문자 제거 — 노이즈 제거, 소문자 통일</div>
    <div class="info-desc"><code>re.sub("[^a-zA-Z#]", " ")</code></div>
  </div>
  <div class="info-item">
    <div class="info-title">토큰화 — 단어 단위로 분리</div>
    <div class="info-desc"><code>str.split()</code></div>
  </div>
  <div class="info-item">
    <div class="info-title">워드클라우드 — 감정별 특징 단어 시각화</div>
    <div class="info-desc"><code>WordCloud</code></div>
  </div>
  <div class="info-item">
    <div class="info-title">해시태그 추출 — 주제/의도 파악</div>
    <div class="info-desc"><code>re.findall(r"#(\w+)")</code></div>
  </div>
</div>

전처리 결론: Twitter 텍스트는 지저분하지만 정규식 몇 줄로 의미 있는 데이터로 바꿀 수 있다. Day 3에서는 이 정제된 데이터로 실제 분류 모델을 만든다.
