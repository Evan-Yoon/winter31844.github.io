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

어제 NLP의 기초를 배웠다면, 오늘은 실제 프로젝트에 손을 댈 차례였다. Twitter의 감정 분석 프로젝트인데, 텍스트가 지저분하다는 걸 금방 알았다. @멘션, 해시태그, 특수문자가 잔뜩 있었고, 이걸 정제하는 과정이 생각보다 복잡했다.

## 오늘 다룬 범위

1. **프로젝트 이해**: 왜 감정 분석인가?
2. **데이터 로드**: Kaggle Twitter 데이터셋 준비
3. **텍스트 정제**: 노이즈 제거 (멘션, 특수문자)
4. **시각화**: 워드클라우드로 감정별 키워드 분석

---

## 핵심 개념 정리

### 1. 프로젝트의 목표: 감정 분석이 왜 필요한가?

처음엔 "트위터의 감정을 분류하는 게 뭐가 중요한가?"라고 생각했다. 하지만 실제로는:

- **브랜드 평판 관리**: 제품 출시 후 고객 반응 모니터링
- **위기 대응**: 부정적 감정이 폭증하는 순간을 조기 발견
- **마케팅 효과 측정**: 캠페인 이후 감정 변화 추적
- **제품 개선**: 어떤 부분이 호평/악평을 받는지 파악

실제로 회사들이 소셜 미디어 모니터링에 엄청난 돈을 쓴다고 배웠다.

### 2. 데이터 준비: 규모 이해하기

Kaggle의 Twitter Sentiment Analysis 데이터셋을 썼다:

```
Train 데이터: 31,962개 트윗
Test 데이터: 17,197개 트윗
전체: 49,159개 트윗
```

데이터 구조는 간단했다:

| id  | tweet                  | label    |
| --- | ---------------------- | -------- |
| 1   | "I love this movie..." | 1 (긍정) |
| 2   | "hate this weather"    | 0 (부정) |

레이블은 이진 분류다. 0 = 부정, 1 = 긍정.

```python
import pandas as pd

train = pd.read_csv('train.csv')
test = pd.read_csv('test.csv')

# 전처리용으로 모두 병합
merge = train._append(test, ignore_index=True, sort=True)
print(f"Total tweets: {len(merge)}")  # 49,159
```

### 3. 텍스트 정제 — 노이즈 제거의 중요성

Twitter 텍스트는 엉망이다. 예시:

```
"@user1 @user2 I love #Python #AI!!! Check out http://t.co/xyz..."
```

이 안에 정말 의미 있는 건 "I love Python AI"뿐이다. 나머지는 노이즈다.

#### 3-1. @멘션 제거

```python
import re
import numpy as np

def remove_pattern(text, pattern):
    return re.sub(pattern, "", text)

# @로 시작하는 모든 멘션 제거
merge['Clean_Tweets'] = np.vectorize(remove_pattern)(
    merge['tweet'],
    r"@[\w]*"
)

# 예시
# Before: "@user1 I love this! @user2 agrees"
# After: " I love this!  agrees"
```

정규식 `@[\w]*`는 "@"로 시작해서 연속된 알파벳/숫자/언더스코어를 모두 매치한다.

#### 3-2. 특수문자 및 숫자 제거

```python
def clean_text(text):
    text = text.lower()  # 소문자 변환
    return re.sub("[^a-zA-Z#]", " ", text)  # 영문과 #만 유지

merge['Clean_Tweets'] = merge['Clean_Tweets'].apply(clean_text)

# 예시
# Before: "I LOVE Python123!!! Check #AI"
# After: "i love python   check  ai"
```

`[^a-zA-Z#]`는 영문자와 #을 **제외한** 모든 것을 공백으로 바꾼다. 왜 #을 유지할까? 해시태그는 나중에 분석할 데이터기 때문이다.

### 4. 토큰화 재정의

어제 배운 토큰화를 다시 했다. 하지만 이번엔 더 간단했다.

```python
# 공백 기준 분리 후 다시 조인
tokenized_tweet = merge['Clean_Tweets'].apply(lambda x: x.split())

# 예: "i love python" → ['i', 'love', 'python']

# 정제된 문자열로 복원
for i in range(len(tokenized_tweet)):
    tokenized_tweet[i] = ' '.join(tokenized_tweet[i])
```

이 과정에서 느낀 점: 정규식으로 정제한 텍스트는 이미 거의 토큰화된 상태였다.

### 5. 워드클라우드 시각화 — 감정을 시각으로 이해하기

여기서 신기한 도구를 배웠다. 워드클라우드다.

```python
from wordcloud import WordCloud
import matplotlib.pyplot as plt

# 긍정 감정(label=1) 트윗에서 모든 단어 추출
all_words_positive = ' '.join(
    text for text in merge[merge['label']==1]['Clean_Tweets']
)

# 부정 감정(label=0) 트윗에서 모든 단어 추출
all_words_negative = ' '.join(
    text for text in merge[merge['label']==0]['Clean_Tweets']
)

# WordCloud 생성
wordcloud_positive = WordCloud(width=800, height=400).generate(all_words_positive)
wordcloud_negative = WordCloud(width=800, height=400).generate(all_words_negative)

# 시각화
plt.figure(figsize=(16, 8))
plt.subplot(1, 2, 1)
plt.imshow(wordcloud_positive, interpolation='bilinear')
plt.title('Positive Sentiment')
plt.axis('off')

plt.subplot(1, 2, 2)
plt.imshow(wordcloud_negative, interpolation='bilinear')
plt.title('Negative Sentiment')
plt.axis('off')

plt.show()
```

결과가 정말 직관적이었다:

- **긍정 트윗**: love, thanks, great, happy, best 등이 크게 표시
- **부정 트윗**: hate, sad, bad, angry, worst 등이 크게 표시

단어의 크기는 빈도를 나타낸다. 자주 나타나는 단어가 더 크다.

### 6. 해시태그 추출 및 분석

해시태그도 중요한 정보다. #AI, #Python처럼 주제를 나타낸다.

```python
def Hashtags_Extract(x):
    hashtags = []
    for i in x:
        # 정규식으로 #뒤의 단어만 추출
        ht = re.findall(r"#(\w+)", i)
        hashtags.append(ht)
    return hashtags

# 감정별 해시태그 분석
ht_positive = Hashtags_Extract(merge[merge['label']==1]['Clean_Tweets'])
ht_negative = Hashtags_Extract(merge[merge['label']==0]['Clean_Tweets'])

# 결과
# ht_positive: [['AI'], ['python', 'code'], ['love'], ...]
# ht_negative: [['fail'], ['bad'], ['hate', 'bug'], ...]
```

정규식 `#(\w+)`는:

- `#`: 샵 문자 찾기
- `(\w+)`: 그 뒤의 알파벳/숫자를 캡처 그룹으로 추출

`findall()`은 모든 매치를 리스트로 반환하므로, 한 트윗에 여러 해시태그가 있어도 모두 추출된다.

---

## 헷갈렸던 점 / 실수 포인트

### 1. 정규식에서 캡처 그룹의 필요성

```python
# 잘못된 예
re.findall(r"#\w+", text)      # ['#AI', '#python']

# 올바른 예
re.findall(r"#(\w+)", text)    # ['AI', 'python']
```

괄호 `()`가 없으면 #도 함께 추출된다. 괄호로 감싸면 그 부분만 추출된다.

### 2. `_append()` vs `concat()`

```python
# 예전 Pandas 방식
merge = train.append(test)

# 최신 Pandas 방식 (권장)
merge = train._append(test, ignore_index=True, sort=True)

# 또는 더 현대적인 방식
merge = pd.concat([train, test], ignore_index=True)
```

`ignore_index=True`를 빠뜨리면 인덱스가 중복된다.

### 3. 워드클라우드 생성 시 성능 문제

단어가 너무 많으면 렌더링이 느려진다. 하지만 대부분의 경우 워드클라우드는 상위 200개 단어만 표시한다. 따라서 불용어를 먼저 제거하면 더 의미 있는 시각화가 된다.

```python
from nltk.corpus import stopwords

stop_words = set(stopwords.words('english'))

# 불용어 제거 후 워드클라우드 생성
clean_words = ' '.join(
    [w for w in merge[merge['label']==1]['Clean_Tweets'].split()
     if w not in stop_words]
)

wordcloud = WordCloud().generate(clean_words)
```

### 4. 정제 과정에서 정보 손실

정규식으로 너무 많이 제거하면 의미 있는 정보도 날아간다. 예:

```python
# 너무 공격적인 정제
"I don't like this movie"
→ "i dont like this movie"  # "don't"이 "dont"로 변함

# 이상적인 정제
"I don't like this movie"
→ "i do not like this movie"  # 부정을 유지
```

실제로 "don't"와 "like" 사이의 부정 의미가 중요한데, 너무 단순하게 정제하면 이를 놓친다.

---

## 복습 Q&A

<details>
<summary><strong>1. 왜 @멘션을 제거할까?</strong></summary>

@멘션은 특정 사용자를 지칭할 뿐, 감정을 결정하는 핵심 내용이 아니다. 오히려 노이즈로 작용한다.

예: "@john I hate this!" → "I hate this!" (멘션 제거 후)

감정은 "hate"에 있지, "@john"에 있지 않다.

</details>

<details>
<summary><strong>2. 워드클라우드의 크기가 무엇을 의미하나?</strong></summary>

단어가 나타난 횟수(빈도)를 나타낸다. 크면 클수록 그 감정 데이터에서 자주 등장했다는 뜻이다.

긍정 데이터에서 "love"가 크다면, 사람들이 긍정적 감정을 표현할 때 "love"를 자주 쓴다는 의미다.

</details>

<details>
<summary><strong>3. 정규식 `[^a-zA-Z#]`의 의미는?</strong></summary>

`[^...]`는 NOT을 의미한다. 즉:

- `[^a-zA-Z#]`: 영문자(a-z, A-Z)와 #을 **제외한** 모든 것
- 따라서 숫자, 특수문자, 공백 등이 매치된다.

이들을 공백으로 치환하면 순수 영문과 #만 남는다.

</details>

<details>
<summary><strong>4. 해시태그를 왜 따로 분석할까?</strong></summary>

해시태그는 사용자가 의도적으로 붙인 메타데이터다. 게시물의 주제나 감정을 명시적으로 나타낸다.

예: "#love #happy #blessed" → 명확한 긍정 신호

해시태그만 모아보면 감정별 핵심 주제를 한눈에 파악할 수 있다.

</details>

---

## 한 줄 정리

Twitter의 지저분한 텍스트도 정규식과 간단한 정제 과정으로 의미 있는 데이터로 변환할 수 있고, 워드클라우드로 감정별 특징을 시각적으로 이해할 수 있다.

내일은 이 정제된 데이터를 가지고 실제 모델을 만들어보자. BOW와 TF-IDF 두 가지 방식을 비교한다고 들었다.
