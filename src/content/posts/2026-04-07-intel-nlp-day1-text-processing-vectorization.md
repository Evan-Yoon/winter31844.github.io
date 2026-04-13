---
title: "[NLP] Day 1: 자연어 처리의 전체 파이프라인"
slug: intel-nlp-day1-text-processing-vectorization
date: 2026-04-07
author: Evan Yoon
category: study
subcategory: bootcamp
description: "문자 처리부터 문서 벡터화까지, NLP의 전체 파이프라인을 배웠다."
thumbnail: ""
tags:
  - NLP
  - 자연어처리
  - 텍스트처리
  - NLTK
  - KoNLPy
  - 벡터화
  - Python
readTime: 12
series: "Intel NLP 과정"
seriesOrder: 1
featured: false
draft: false
toc: true
---

Intel의 AI 교육 과정 중 NLP 첫 시간을 보냈다. 텍스트를 어떻게 처리하고 기계가 이해할 수 있는 형태로 변환하는지가 핵심이었다. 처음엔 문자열 조작이나 정규식처럼 단순해 보였지만, 실제로는 문제마다 다른 접근이 필요하다는 걸 깨달았다.

## 오늘 다룬 범위

1. **문자 수준**: 문자열 처리, 정규식
2. **단어 수준**: 토큰화, Stemming, Lemmatization, N-gram
3. **문장/문서 수준**: 감정분석, 문법 분석, 벡터화
4. **언어별 최적화**: 영어(NLTK), 한국어(KoNLPy/MeCab)

전체적으로는 텍스트를 어떻게 분해하고, 정제하고, 수치화하는 과정을 배웠다.

---

## 핵심 개념 정리

### 1. 문자열 처리 — 텍스트의 기초

가장 기본적인 것부터 시작했다. Python 문자열 메서드들을 활용한 텍스트 조작이다.

```python
s = 'No pain no gain'

# 포함 여부 확인
'pain' in s  # True

# 공백 기준 분리
s.split()    # ['No', 'pain', 'no', 'gain']

# 문자열 치환
s.replace('pain', 'gain')  # 'No gain no gain'

# 역순 반환
s[::-1]  # 'niag on nip oN'
```

이건 쉬워 보이지만, 실제로 텍스트 정제에서 자주 써인다. 예를 들어 `lower()`로 대소문자를 통일하는 건 감정 분석 전에 거의 필수다.

### 2. 정규식(Regular Expression) — 강력한 패턴 매칭

정규식은 처음엔 복잡해 보였다. 하지만 손으로 일일이 조건을 처리하는 것보다 훨씬 효율적이다.

```python
import re

# 문자 클래스
re.match('[xyz]', 'x')        # 처음부터 x, y, z 중 하나 찾기
re.search('[a-z]+', 'a1b2c')  # 전체에서 연속된 소문자 찾기
re.findall(r'\d+', 'a1b2c3')  # ['1', '2', '3'] - 모든 숫자 추출
re.sub('[a-z]', '1', 'abc')   # '111' - 소문자를 1로 치환

# 반복 횟수 지정
r'a{2,4}'  # a가 2~4번 반복
r'b+'      # b가 1회 이상
r'c?'      # c가 0~1회
```

자주 사용되는 패턴들:

- `\d`: 숫자
- `\s`: 공백(스페이스, 탭, 줄바꿈)
- `\w`: 영문자, 숫자, 언더스코어

정규식을 `compile()` 해서 미리 준비하면 성능이 좋다고 했다. 같은 패턴을 여러 번 사용할 땐 그렇게 하는 게 낫다.

### 3. 토큰화 — 텍스트를 작은 단위로 쪼개기

**문장 토큰화**부터 시작했다. 문장을 나누는 게 간단해 보이지만, 문제가 있다.

```python
text = "Mr. Smith went to Dr. Johnson's office. It was great."
text.split('.')  # 마침표마다 쪼개져서 "Mr" 다음에 끊김
```

그래서 NLTK의 `sent_tokenize()`를 써야 한다. 이건 "Mr.", "Dr."처럼 약자를 인식해서 올바르게 분리한다.

```python
from nltk.tokenize import sent_tokenize, word_tokenize

sent_tokenize(text)  # ['Mr. Smith went to Dr. Johnson\'s office.', 'It was great.']
```

**단어 토큰화**는 더 까다롭다. `word_tokenize()`는 공백뿐 아니라 구두점도 분리한다.

```python
word_tokenize("Hello, world!")
# ['Hello', ',', 'world', '!']
```

이렇게 구두점까지 따로 분리하는 게 처음엔 이상했는데, 감정분석이나 문법 분석할 땐 이 정도의 세밀함이 필요하다고 배웠다.

### 4. 불용어 제거(Stopwords)

모든 단어가 의미 있는 건 아니다. "the", "is", "in" 같은 단어들은 문맥에 거의 도움이 안 된다.

```python
from nltk.corpus import stopwords

stop_words = set(stopwords.words('english'))
tokens = ['hello', 'this', 'is', 'a', 'test']
filtered = [w for w in tokens if w not in stop_words]
# ['hello', 'test']
```

한국어도 마찬가지다. 물론 한국어 불용어는 따로 정의해야 한다.

### 5. Stemming vs Lemmatization — 단어의 원형 찾기

두 기술을 헷갈렸다. 차이를 정리하니 이해가 됐다.

**Stemming**은 어근(stem)만 남긴다:

```python
from nltk.stem import PorterStemmer

stemmer = PorterStemmer()
stemmer.stem('flying')   # 'fli'
stemmer.stem('running')  # 'runn'
stemmer.stem('happily')  # 'happi'
```

부작용이 있다. 어근이 아닐 수도 있고("fli"는 단어가 아님), 같은 단어를 다르게 처리할 수도 있다.

**Lemmatization**은 사전의 표제어로 변환한다:

```python
from nltk.stem import WordNetLemmatizer

lemmatizer = WordNetLemmatizer()
lemmatizer.lemmatize('flying')   # 'flying' (명사) 또는 'fly' (동사)
lemmatizer.lemmatize('am')       # 'be'
lemmatizer.lemmatize('happily')  # 'happily'
```

더 정확하지만, 단어의 품사(POS)를 알아야 정확하게 작동한다.

실무에선 어느 것을 쓸까? 상황에 따라 다르다. 빠른 처리가 필요하면 Stemming, 정확성이 중요하면 Lemmatization을 쓴다고 했다.

### 6. 문장 분석 — 의미 파악하기

**감정 분석(Sentiment Analysis)**을 배웠을 때가 신기했다. 문장이 긍정인지 부정인지 자동으로 판단하는 거다.

```python
from textblob import TextBlob

blob = TextBlob("I absolutely love this!")
blob.sentiment.polarity      # 0.7 (긍정)
blob.sentiment.subjectivity  # 0.6 (다소 주관적)
```

- **Polarity**: -1.0(매우 부정) ~ 1.0(매우 긍정)
- **Subjectivity**: 0.0(객관적) ~ 1.0(주관적)

이건 기계학습 없이도 작동한다. 미리 학습된 규칙이 들어있는 거다.

**품사 태깅(POS Tagging)**도 배웠다. 각 단어의 품사를 분류하는 것:

```python
blob.tags  # [('I', 'PRP'), ('absolutely', 'RB'), ('love', 'VB'), ...]
```

이건 문법 분석이나 개체명 인식의 기초가 된다.

### 7. 한국어 처리 — KoNLPy와 MeCab

영어는 공백으로 단어를 쉽게 분리할 수 있다. 하지만 한국어는 다르다.

```python
"자연어처리는 재미있다"  # 공백이 없음
```

그래서 형태소 분석기(morphological analyzer)가 필요하다. 이번에 배운 건 MeCab이다.

```python
from konlpy.tag import Mecab

tagger = Mecab()
tagger.morphs('자연어처리는 재미있다')
# ['자연어', '처리', '는', '재미', '있', '다']

tagger.nouns('자연어처리는 재미있다')
# ['자연어', '처리', '재미']  # 명사만 추출
```

이 기능들이 엄청 유용하다. 문서 분류나 검색 시스템에서 자주 써인다고 했다.

한국어 POS 태그도 영어와 다르다:

- NN: 명사
- VV: 동사
- JKS: 주격 조사
- EF: 어말어미

### 8. N-gram — 연속 단어의 조합

하나의 단어만으로는 맥락을 잃는다. 연속된 단어 조합을 보자는 아이디어다.

```python
from textblob import TextBlob

text = "I love natural language processing"
blob = TextBlob(text)

list(blob.ngrams(1))  # unigram: [('I',), ('love',), ...]
list(blob.ngrams(2))  # bigram: [('I', 'love'), ('love', 'natural'), ...]
list(blob.ngrams(3))  # trigram: [('I', 'love', 'natural'), ...]
```

Bigram과 Trigram은 단어의 관계를 잡는 데 도움이 된다. 예를 들어 "natural language"는 함께 나타나는 경향이 있다.

### 9. Bag of Words (BoW) — 문서의 수치화

여기부터 진짜 중요한 부분이다. 텍스트를 기계학습이 이해할 수 있는 숫자로 바꾸는 것이다.

```python
from sklearn.feature_extraction.text import CountVectorizer

docs = [
    'I love natural language processing',
    'I love machine learning'
]

vectorizer = CountVectorizer()
X = vectorizer.fit_transform(docs)

print(vectorizer.get_feature_names_out())
# ['language' 'learning' 'love' 'machine' 'natural' 'processing']

print(X.toarray())
# [[1 0 1 0 1 1]
#  [0 1 1 1 0 0]]
```

각 문서(행)에서 각 단어(열)가 몇 번 나타났는지를 나타낸다. 이걸 **문서-단어 행렬(DTM)**이라고 한다.

**TF-IDF**는 한 단계 더 나아간다. 단순히 빈도만이 아니라, 전체 문서에서 얼마나 희귀한 단어인지를 고려한다.

```python
from sklearn.feature_extraction.text import TfidfVectorizer

vectorizer = TfidfVectorizer()
X = vectorizer.fit_transform(docs)
```

- **TF(Term Frequency)**: 문서 내 단어의 빈도
- **IDF(Inverse Document Frequency)**: 모든 문서 중 그 단어가 얼마나 드문지

자주 나타나는 단어(the, is)는 낮은 점수를 받고, 특정 문서에만 나타나는 단어는 높은 점수를 받는다.

---

## 헷갈렸던 점 / 실수 포인트

### 1. 토큰화의 깊이

처음엔 `split()`이면 충분하다고 생각했다. 하지만 실제론:

- "can't" → ["can't"] vs ["can", "'t"] (어떻게 처리할지)
- "U.S.A." → 마침표마다 쪼갤 건지, 아니면 함께 처리할 건지

이런 선택이 결과에 영향을 미친다.

### 2. Stemming과 Lemmatization 구분

헷갈렸던 부분이다. 차이를 정리하니 이제 언제 뭘 쓸지 판단할 수 있다.

|        | Stemming            | Lemmatization         |
| ------ | ------------------- | --------------------- |
| 방식   | 규칙 기반 어근 추출 | 사전 기반 표제어 변환 |
| 속도   | 빠름                | 느림                  |
| 정확성 | 낮음 (오류 가능)    | 높음                  |
| 결과   | 어근이 아닐 수도    | 실제 단어             |

### 3. 정규식의 복잡성

`\d+`, `[a-z]*`, `\s{2,}` 같은 표기법이 직관적이지 않았다. 하지만 온라인 도구(regex101.com)에서 테스트하니 빨리 이해가 됐다.

### 4. 한국어 처리의 독특성

영어는 공백이 단어 경계다. 한국어는 아니다. 형태소 분석이 필수인데, 이건 미리 설치된 사전에 의존한다. 따라서:

- 신조어나 이름을 정확히 처리하지 못할 수 있다.
- 설치 과정이 복잡할 수 있다 (MeCab 의존성).

---

## 복습 Q&A

<details>
<summary><strong>1. 정규식 `[a-z]{2,4}`는 무엇을 매치하나?</strong></summary>

소문자 a~z가 2~4회 연속으로 나타나는 문자열을 매치한다.

- "he" ✓
- "hello" ✓ (h, e, l, l이 연속)
- "a" ✗ (1글자)
- "HELLO" ✗ (대문자)

</details>

<details>
<summary><strong>2. `word_tokenize()`와 `split()`의 차이는?</strong></summary>

```python
text = "Hello, world!"

text.split()           # ['Hello,', 'world!']
word_tokenize(text)    # ['Hello', ',', 'world', '!']
```

`word_tokenize()`는 구두점까지 분리한다. 감정 분석이나 품사 태깅 전에는 이 정도의 세밀함이 필요하다.

</details>

<details>
<summary><strong>3. Stemming 결과가 실제 단어가 아닐 수 있다는 게 무슨 뜻?</strong></summary>

```python
stemmer.stem('flying')   # 'fli'
stemmer.stem('sentimental')  # 'sent'
```

"fli"나 "sent"는 영어 사전에 없는 단어다. 어근만 추출한 것이다. 따라서 우리가 읽을 수는 없지만, 기계학습 모델은 "flying"과 "fly"를 같은 카테고리로 취급할 수 있다.

</details>

<details>
<summary><strong>4. TF-IDF에서 높은 값을 받는 단어는?</strong></summary>

- "the", "is" 같은 일반 단어: 낮은 값 (모든 문서에서 나타남)
- "특정 주제와 관련된 단어": 높은 값 (해당 문서에만 자주 나타남)

예: 스포츠 뉴스 기사에서 "goal"이나 "striker"는 높은 TF-IDF값을 받는다.

</details>

---

## 한 줄 정리

텍스트는 문자 → 단어 → 문서 순서로 처리되며, 각 단계마다 다른 기법을 적용해서 결국 기계가 이해할 수 있는 숫자로 변환된다. 이게 NLP의 기초다.

다음 시간에는 이 벡터화된 문서들을 가지고 실제로 분류나 군집화 같은 작업을 하게 될 것 같다. 기대된다.
