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

NLP(자연어 처리)의 첫 번째 수업이다. 텍스트를 기계가 이해하는 숫자로 바꾸는 전 과정을 다뤘다. 문자 단위 조작에서 시작해 단어 수준 처리, 문서 수준 벡터화까지 계층적으로 이어진다.

## 1. 문자열 처리

**이게 뭔지**: Python 기본 문자열 메서드로 텍스트를 다루는 것.  
**왜 필요한지**: 이후의 모든 NLP 처리는 여기서 시작한다. 소문자 통일, 공백 정리 같은 기초 작업이 없으면 같은 단어를 다른 단어로 취급한다.

```python
s = 'No pain no gain'

'pain' in s        # True — 포함 여부 확인
s.split()          # ['No', 'pain', 'no', 'gain'] — 공백 기준 분리
s.replace('pain', 'gain')  # 'No gain no gain' — 치환
s[::-1]            # 'niag on nip oN' — 역순
s.lower()          # 'no pain no gain' — 대소문자 통일 (NLP 전처리 필수)
```

## 2. 정규식(Regular Expression)

**이게 뭔지**: 패턴으로 텍스트를 검색하고 변환하는 도구.  
**왜 필요한지**: `@멘션`, URL, 특수문자처럼 일정한 패턴을 가진 노이즈를 한 번에 제거할 수 있다. 조건문으로 처리하는 것보다 훨씬 간결하다.

```python
import re

re.match('[xyz]', 'x')        # 문자열 시작부터 x, y, z 중 하나 매치
re.search('[a-z]+', 'a1b2c')  # 전체에서 연속 소문자 탐색
re.findall(r'\d+', 'a1b2c3')  # ['1', '2', '3'] — 모든 숫자 추출
re.sub('[a-z]', '1', 'abc')   # '111' — 소문자 전체를 1로 치환
```

자주 쓰이는 패턴:

| 패턴 | 의미 | 예 |
|---|---|---|
| `\d` | 숫자 | `\d+` → 1개 이상의 숫자 |
| `\s` | 공백(스페이스, 탭, 줄바꿈) | `\s+` → 연속 공백 |
| `\w` | 영문자, 숫자, 언더스코어 | `\w{3,5}` → 3~5글자 단어 |
| `[^...]` | NOT | `[^a-z]` → 소문자가 아닌 것 |

같은 패턴을 반복 사용한다면 `re.compile()`로 미리 컴파일해두면 더 빠르다.

## 3. 토큰화(Tokenization)

**이게 뭔지**: 텍스트를 단어나 문장 단위로 쪼개는 작업.  
**왜 필요한지**: 기계학습 모델은 개별 단어(토큰) 단위로 처리한다. 분리 기준이 명확하지 않으면 "Mr."처럼 약자에서 문장이 잘못 나뉜다.

```python
from nltk.tokenize import sent_tokenize, word_tokenize

text = "Mr. Smith went to Dr. Johnson's office. It was great."

# 단순 split으로 나누면 "Mr" 다음에 잘못 끊김
text.split('.')  # 잘못된 방법

# NLTK는 약자(Mr., Dr.)를 인식해서 올바르게 분리
sent_tokenize(text)
# ["Mr. Smith went to Dr. Johnson's office.", 'It was great.']

# 단어 토큰화 — 구두점도 별도 토큰으로 분리
word_tokenize("Hello, world!")
# ['Hello', ',', 'world', '!']
```

`word_tokenize()`가 쉼표와 느낌표를 따로 분리하는 이유: 감정 분석이나 품사 태깅에서 구두점 자체가 의미를 가질 수 있기 때문이다.

## 4. 불용어 제거(Stopwords)

**이게 뭔지**: 분석에 도움이 되지 않는 빈도 높은 단어를 제거하는 작업.  
**왜 필요한지**: "the", "is", "a" 같은 단어는 어떤 문서에나 등장하므로 문서의 특징을 구별하는 데 쓸모가 없다. 이것들을 그대로 두면 모델이 노이즈를 학습한다.

```python
from nltk.corpus import stopwords

stop_words = set(stopwords.words('english'))
tokens = ['hello', 'this', 'is', 'a', 'test']

filtered = [w for w in tokens if w not in stop_words]
# ['hello', 'test'] — 의미 있는 단어만 남음
```

한국어 불용어는 NLTK에 없어서 직접 정의해야 한다. 도메인마다 불용어 목록이 다르다는 점도 중요하다.

## 5. Stemming vs Lemmatization

**이게 뭔지**: 단어의 여러 변형을 하나의 기본 형태로 통일하는 작업.  
**왜 필요한지**: "running", "ran", "runs"를 각각 다른 단어로 취급하면 같은 의미를 가진 단어들이 흩어진다. 통일하면 모델이 같은 개념으로 묶을 수 있다.

**Stemming** — 규칙 기반으로 어근만 잘라낸다. 빠르지만 결과가 실제 단어가 아닐 수 있다.

```python
from nltk.stem import PorterStemmer

stemmer = PorterStemmer()
stemmer.stem('flying')    # 'fli'  — 실제 단어가 아님
stemmer.stem('running')   # 'runn'
stemmer.stem('happily')   # 'happi'
```

**Lemmatization** — 사전 기반으로 표제어를 찾는다. 느리지만 결과가 실제 단어다. 품사(POS)를 알면 정확도가 올라간다.

```python
from nltk.stem import WordNetLemmatizer

lemmatizer = WordNetLemmatizer()
lemmatizer.lemmatize('flying', pos='v')  # 'fly'  — 동사로 지정 시 정확
lemmatizer.lemmatize('am', pos='v')      # 'be'
lemmatizer.lemmatize('happily')          # 'happily' — 품사 미지정 시 명사로 처리
```

| | Stemming | Lemmatization |
|---|---|---|
| 방식 | 규칙 기반 어근 잘라냄 | 사전 기반 표제어 변환 |
| 속도 | 빠름 | 느림 |
| 결과 | 실제 단어가 아닐 수 있음 | 항상 실제 단어 |
| 사용 시점 | 속도 중요, 정밀도 덜 중요 | 정확도 중요한 경우 |

## 6. 감정 분석과 품사 태깅

**이게 뭔지**: 문장이 긍정/부정인지 판단하고, 각 단어의 품사를 분류하는 작업.  
**왜 필요한지**: 규칙 기반으로도 어느 정도 감정 분석이 가능하다. 품사 태깅은 Lemmatization 정확도를 높이거나 명사/동사만 추출할 때 쓰인다.

```python
from textblob import TextBlob

blob = TextBlob("I absolutely love this!")
blob.sentiment.polarity      # 0.7 — -1.0(부정) ~ 1.0(긍정)
blob.sentiment.subjectivity  # 0.6 — 0.0(객관) ~ 1.0(주관)

blob.tags  # [('I', 'PRP'), ('absolutely', 'RB'), ('love', 'VB'), ('this', 'DT')]
```

TextBlob은 사전학습된 규칙으로 동작해서 별도 학습 없이도 쓸 수 있다. 복잡한 문맥이나 신조어는 오류 가능성이 있다.

## 7. 한국어 처리 — KoNLPy와 MeCab

**이게 뭔지**: 한국어 형태소를 분석하는 도구.  
**왜 필요한지**: 영어는 공백이 단어 경계지만 한국어는 "자연어처리는재미있다"처럼 붙어서 쓰인다. 형태소 분석기 없이는 단어를 올바르게 분리할 수 없다.

```python
from konlpy.tag import Mecab

tagger = Mecab()

tagger.morphs('자연어처리는 재미있다')
# ['자연어', '처리', '는', '재미', '있', '다']

tagger.nouns('자연어처리는 재미있다')
# ['자연어', '처리', '재미'] — 명사만 추출

tagger.pos('자연어처리는 재미있다')
# [('자연어', 'NNG'), ('처리', 'NNG'), ('는', 'JX'), ...]
```

주요 품사 태그: `NNG`(일반 명사), `VV`(동사), `JKS`(주격 조사), `EF`(어말어미)

신조어나 고유명사는 MeCab 사전에 없으면 오류가 날 수 있다. 설치 자체도 OS에 따라 복잡하다.

## 8. N-gram

**이게 뭔지**: 연속된 n개 단어를 하나의 단위로 보는 방식.  
**왜 필요한지**: 단어 하나만 보면 "natural language processing"에서 "natural"과 "language"의 관계를 모른다. N-gram은 이 연속성을 보존한다.

```python
from textblob import TextBlob

text = "I love natural language processing"
blob = TextBlob(text)

list(blob.ngrams(2))  # bigram: [('I', 'love'), ('love', 'natural'), ('natural', 'language'), ...]
list(blob.ngrams(3))  # trigram: [('I', 'love', 'natural'), ('love', 'natural', 'language'), ...]
```

Bigram/Trigram은 "natural language"처럼 함께 나오는 단어 쌍을 잡아낸다. N이 클수록 더 많은 문맥을 잡지만, 조합 수가 폭발적으로 늘어난다.

## 9. Bag of Words와 TF-IDF

**이게 뭔지**: 텍스트를 기계학습이 처리할 수 있는 숫자 벡터로 변환하는 방법.  
**왜 필요한지**: 모델은 문자를 이해하지 못한다. 텍스트를 반드시 숫자로 바꿔야 한다.

**Bag of Words (BoW)** — 단어별 등장 횟수를 센다.

```python
from sklearn.feature_extraction.text import CountVectorizer

docs = ['I love natural language processing', 'I love machine learning']
vectorizer = CountVectorizer()
X = vectorizer.fit_transform(docs)

print(vectorizer.get_feature_names_out())
# ['language', 'learning', 'love', 'machine', 'natural', 'processing']

print(X.toarray())
# [[1, 0, 1, 0, 1, 1],   # 문서1: language=1, love=1, natural=1, processing=1
#  [0, 1, 1, 1, 0, 0]]   # 문서2: learning=1, love=1, machine=1
```

**TF-IDF** — 단순 빈도가 아니라 "이 단어가 이 문서에서만 얼마나 특별한가"를 반영한다.

```
TF-IDF = TF × IDF
TF = 해당 단어 빈도 / 문서 총 단어 수
IDF = log(전체 문서 수 / 해당 단어가 등장하는 문서 수)
```

"the"는 모든 문서에 등장하므로 IDF가 낮다. "quantization"은 특정 문서에만 등장하면 IDF가 높다.

```python
from sklearn.feature_extraction.text import TfidfVectorizer

vectorizer = TfidfVectorizer()
X = vectorizer.fit_transform(docs)
```

| | Bag of Words | TF-IDF |
|---|---|---|
| 단어 가중치 | 등장 횟수 | 빈도 × 희소성 |
| 흔한 단어 처리 | 높은 가중치 | 낮은 가중치 |
| 불용어 제거 | 직접 해야 함 | `stop_words` 파라미터 지원 |
| 사용 시점 | 빠른 기준선 필요 시 | 단어 중요도 구별이 필요할 때 |

---

## 핵심 정리

```
텍스트 처리의 계층:
문자 수준 → 정규식, 문자열 처리
단어 수준 → 토큰화, 불용어 제거, Stemming/Lemmatization, N-gram
문서 수준 → BoW, TF-IDF (숫자 벡터로 변환)
```

NLP의 모든 작업은 "텍스트를 어떻게 숫자로 바꿀 것인가"에 귀결된다. Day 1에서 배운 BoW와 TF-IDF가 그 첫 번째 대답이다. 이어지는 수업에서는 이 벡터로 실제 분류 모델을 만든다.
