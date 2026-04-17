---
title: "[NLP] extra 4: Transformer를 기초부터 다시 이해하기"
slug: intel-nlp-extra4-transformer
date: 2026-04-17
author: Evan Yoon
category: study
subcategory: bootcamp
description: |
  RNN, LSTM, GRU, Transformer 강의 자료 중 69~80페이지를 바탕으로
  Transformer가 왜 중심 구조가 되었는지, embedding, positional encoding,
  self-attention, masked attention, feed forward, residual connection,
  layer normalization까지 쉽게 풀어 정리했다.
thumbnail: /images/posts/intel-nlp-extra4-transformer/transformer-slide-69-69.png
tags:
  - NLP
  - Transformer
  - Attention
  - intel-nlp-extra
  - self-attention
  - deep-learning
  - nlp-study
readTime: 18
series: "Intel NLP extra"
seriesOrder: 4
featured: false
draft: false
toc: true
---

RNN, LSTM, GRU까지 보면 흐름은 어느 정도 이해된다. 순서대로 읽으면서 이전 정보를 넘기고, 긴 문장을 더 잘 처리하려고 구조를 조금씩 고친 것이다. 그런데 실제 NLP의 중심 구조는 결국 Transformer로 넘어왔다. 강의 자료는 Transformer를 2017년에 Google이 발표한 구조로 소개하면서, 가장 큰 특징으로 **병렬 실행이 가능하다**는 점을 먼저 강조한다.

이번 글에서는 강의 자료 69~80페이지를 기준으로 Transformer를 다음 순서로 정리해 보려고 한다.

1. 왜 RNN 계열에서 Transformer로 넘어왔는가
2. Transformer의 전체 구조는 어떻게 생겼는가
3. Embedding과 Positional Encoding은 왜 필요한가
4. Self-Attention은 무엇을 하는가
5. Masked Attention, Feed Forward, Add, Norm은 왜 붙는가

## 1. 왜 Transformer가 필요했는가

RNN, LSTM, GRU는 모두 순차 처리 구조였다. 즉, 앞 단어를 읽어야 다음 단어를 계산할 수 있다.

예를 들어 `"I love NLP"`를 처리할 때:

1. `I`를 읽고
2. 그 결과를 바탕으로 `love`를 읽고
3. 다시 그 결과를 바탕으로 `NLP`를 읽는다

이 방식은 자연스럽지만, 두 가지 부담이 있다.

- 계산이 순차적이라 병렬화가 어렵다
- 문장이 길어질수록 먼 단어 관계를 바로 보기가 어렵다

Transformer는 이 문제를 다르게 푼다.

> "순서대로 하나씩 읽지 말고, 문장 전체 단어 관계를 한 번에 보자."

이 발상이 Attention 중심 구조로 이어진다.

## 2. Transformer 전체 구조 한눈에 보기

강의 자료 69페이지는 Transformer의 큰 구조를 먼저 보여 준다.

![Transformer 전체 구조 슬라이드](/images/posts/intel-nlp-extra4-transformer/transformer-slide-69-69.png)

강의 자료 기준 핵심 구성 요소는 다음 여섯 가지다.

1. Embedding
2. Positional Encoding
3. Multi-Head Attention
4. Feed Forward
5. Add (Residual Connection)
6. Norm

그리고 Encoder-Decoder 구조를 가진다고 설명한다.

이걸 너무 복잡하게 볼 필요는 없다. 큰 흐름만 먼저 잡으면 된다.

- 단어를 벡터로 바꾼다
- 위치 정보도 함께 넣는다
- 단어들끼리 서로 얼마나 관련 있는지 본다
- 그 결과를 더 복잡한 표현으로 바꾼다
- 깊은 모델이 안정적으로 학습되도록 더하고 정규화한다

즉, Transformer는 "문장 전체 관계를 한 번에 계산하는 블록"을 여러 번 쌓은 구조라고 생각하면 된다.

## 3. Embedding: 단어를 숫자 벡터로 바꾸기

강의 자료 70페이지는 Embedding부터 시작한다.

![Embedding 슬라이드](/images/posts/intel-nlp-extra4-transformer/transformer-slide-70-70.png)

이 부분은 앞 수업들과 연결된다. 모델은 문자를 그대로 이해하지 못하므로, 단어를 먼저 벡터로 바꿔야 한다.

예를 들면:

- `"student"`라는 단어를
- 수백 차원의 숫자 벡터로 바꿔서
- 모델 입력으로 사용하는 것이다

Transformer에서도 이 기본은 같다.  
다만 중요한 점은, 이 벡터가 단순한 번호표가 아니라 **단어 의미를 담는 학습 가능한 표현**이라는 것이다.

강의 자료에서는 디코더 쪽 `output embedding`도 언급한다.  
훈련할 때는 정답 문장을 디코더 입력으로 넣고, 추론할 때는 모델이 직전에 만든 단어를 다음 입력으로 사용한다는 설명이 나온다. 이건 나중에 번역, 요약, 생성 모델을 이해할 때 중요한 부분이다.

## 4. 그런데 Transformer는 순서를 어떻게 아는가

RNN 계열은 순서대로 읽었기 때문에 위치 정보가 자연스럽게 들어갔다.  
하지만 Transformer는 병렬 계산을 하므로, 단어를 한 번에 본다. 그러면 문제가 생긴다.

> `"I am a student"`와 `"student a am I"`를 같은 단어 집합으로 볼 위험이 있다.

그래서 필요한 것이 Positional Encoding이다.

## 5. Positional Encoding: 위치 정보를 따로 넣는다

강의 자료 71페이지는 positional encoding이 왜 필요한지 설명한다.

![Positional Encoding 슬라이드](/images/posts/intel-nlp-extra4-transformer/transformer-slide-71-71.png)

핵심은 단순하다.

- 단어 의미 벡터만 넣으면 순서 정보가 없다
- 그래서 위치 전용 벡터를 따로 만든다
- 그 위치 벡터를 단어 임베딩에 더한다

즉, 입력 벡터는 아래 두 가지가 합쳐진 결과다.

```text
최종 입력 = 단어 의미 벡터 + 위치 벡터
```

강의 자료에서는 sin, cos 함수를 번갈아 쓰는 방식을 소개한다.  
수학 자체를 외울 필요는 없고, 아래 직관만 잡으면 충분하다.

- 위치마다 다른 패턴의 숫자를 준다
- 비슷한 위치끼리는 어느 정도 구조적 관계가 생긴다
- 모델이 "이 단어가 몇 번째쯤 있는지"를 알 수 있게 된다

강의 자료 72페이지 그림은 이걸 더 직관적으로 보여 준다.

![Positional encoding과 embedding 결합 슬라이드](/images/posts/intel-nlp-extra4-transformer/transformer-slide-72-72.png)

즉, Transformer는 순서를 읽는 대신, **위치 정보를 입력에 직접 추가**하는 방식으로 순서를 다룬다.

### 5-1. Positional Encoding의 수학은 어떻게 보면 되는가

강의 자료에서는 위치 벡터를 만들 때 `sin`, `cos` 함수를 사용한다고 설명한다.  
처음 보면 식이 꽤 낯설다. 하지만 입문 단계에서는 아래 정도로 이해하면 충분하다.

- 위치마다 다른 숫자 패턴을 만들고
- 그 패턴을 각 차원에 나눠 담고
- 비슷한 위치끼리는 어느 정도 비슷한 구조를 갖게 만든다

즉, 중요한 것은 "왜 하필 사인, 코사인인가"를 증명하는 것이 아니라,  
**문장 순서를 숫자 패턴으로 표현해 임베딩에 더해 준다**는 점이다.

수학적으로 보면 결국 positional encoding도 벡터이고,  
Transformer는 그 벡터를 단어 임베딩과 더해서 입력 표현을 만든다.

## 6. Self-Attention: 문장 안 단어 관계를 한 번에 본다

Transformer의 핵심은 결국 Self-Attention이다. 강의 자료 73페이지는 Query, Key, Value를 소개하면서 단어들 간 상관관계를 구한다고 설명한다.

![Self-Attention 개요 슬라이드](/images/posts/intel-nlp-extra4-transformer/transformer-slide-73-73.png)

수학 기호는 잠시 내려놓고, 직관적으로 보면 Self-Attention은 이런 질문을 한다.

> 어떤 단어를 이해할 때, 문장 안의 다른 단어들 중 누구를 얼마나 참고해야 할까?

예를 들어:

> The cat sat on the mat.

이 문장에서 `sat`를 볼 때 `cat`이 중요한 관계를 가진다는 것을 모델이 학습할 수 있다.  
강의 자료 76페이지도 바로 이 예시를 든다.

![Multi-Head Attention 설명 슬라이드](/images/posts/intel-nlp-extra4-transformer/transformer-slide-76-76.png)

즉, Self-Attention은 각 단어를 독립적으로 보는 것이 아니라,  
**문장 안 다른 단어들과의 관련도를 함께 계산해서 표현을 다시 만드는 과정**이다.

### 6-1. Self-Attention을 수학 없이 먼저 읽는 방법

Self-Attention은 아래 네 단계로 생각하면 된다.

1. 지금 단어가 무엇을 참고해야 하는지 기준을 만든다
2. 다른 단어들이 그 기준과 얼마나 맞는지 비교한다
3. 비교 점수를 확률처럼 정리한다
4. 그 비율만큼 다른 단어 정보를 섞어 새 표현을 만든다

즉, Self-Attention은 "문장 안에서 누구 말을 얼마나 참고할지 정하는 계산"이다.  
이 흐름만 잡히면 뒤에 나오는 `Q`, `K`, `V`, `softmax`도 훨씬 덜 낯설다.

## 7. Query, Key, Value를 꼭 어려운 수학으로 볼 필요는 없다

처음 보면 `Q`, `K`, `V`가 가장 막막하다.  
하지만 입문 단계에서는 이렇게만 이해해도 충분하다.

- Query: 지금 내가 참고하고 싶은 기준
- Key: 각 단어가 가진 색인표
- Value: 실제로 가져올 정보

즉, 어떤 단어를 볼 때:

1. 내가 지금 무엇을 찾고 싶은지(Query)를 만든다
2. 다른 단어들의 Key와 비교한다
3. 얼마나 관련 있는지 점수를 만든다
4. 그 점수만큼 Value를 섞어 새 표현을 만든다

강의 자료 74~75페이지 그림은 이 계산 흐름을 시각적으로 보여 준다.

![Multi-Head Attention 구조 그림 1](/images/posts/intel-nlp-extra4-transformer/transformer-slide-74-74.png)

![Multi-Head Attention 구조 그림 2](/images/posts/intel-nlp-extra4-transformer/transformer-slide-75-75.png)

결국 중요한 것은 Q, K, V 이름보다,  
**단어들이 서로 얼마나 관련 있는지 점수를 만들어 다시 표현한다**는 점이다.

### 7-1. Q, K, V는 수학적으로 무엇인가

Transformer 식에서는 보통 아래처럼 쓴다.

```text
Q = XW_Q
K = XW_K
V = XW_V
```

이 식을 말로 바꾸면 이렇다.

- 원래 입력 벡터 `X`가 있고
- 거기에 서로 다른 가중치 행렬 `W_Q`, `W_K`, `W_V`를 곱해서
- Query, Key, Value라는 세 가지 표현을 만든다

즉, `Q`, `K`, `V`는 처음부터 따로 존재하는 것이 아니라,  
같은 입력을 서로 다른 관점으로 변환한 결과다.

왜 굳이 세 개로 나누느냐면 역할이 다르기 때문이다.

- Query: 지금 내가 찾고 싶은 기준
- Key: 각 단어가 가진 비교용 특징
- Value: 실제로 가져와서 섞을 정보

수학적으로는 선형변환이지만, 개념적으로는 "같은 단어를 세 가지 용도로 다시 표현한 것"이라고 보면 된다.

### 7-2. Attention score는 어떻게 만들어지는가

강의 자료에서 핵심이 되는 식은 보통 아래 형태다.

```text
Attention(Q, K, V) = softmax(QK^T / sqrt(d_k)) V
```

처음 보면 매우 복잡해 보이지만, 사실 세 부분으로 나눠 읽으면 된다.

1. `QK^T`: Query와 Key를 비교해서 관련도 점수를 만든다
2. `/ sqrt(d_k)`: 점수가 너무 커지지 않게 크기를 조절한다
3. `softmax(... ) V`: 점수를 확률처럼 바꾼 뒤 Value를 가중합한다

즉, 이 식 전체는 "비교 → 조절 → 섞기"의 흐름이다.

### 7-3. `QK^T`는 왜 dot product를 쓰는가

`QK^T`는 각 Query와 각 Key를 서로 비교하는 단계다.  
여기서 dot product를 쓰는 이유는 두 벡터가 얼마나 비슷한 방향을 보는지 숫자로 만들기 쉽기 때문이다.

직관적으로 보면:

- 값이 크면: 지금 단어가 저 단어를 많이 참고해야 한다
- 값이 작으면: 참고 비중이 낮다

예를 들어 `sat`를 해석할 때 `cat`과의 관련도 점수가 높게 나오면,  
attention은 `cat`의 정보를 더 많이 섞게 된다.

### 7-4. 왜 `sqrt(d_k)`로 나누는가

이 부분은 수학을 잘 모르는 입장에서 특히 걸리기 쉽다.

`Q`와 `K` 차원이 커질수록 dot product 값도 커질 가능성이 높다.  
그러면 softmax를 통과하기 전에 점수 차이가 너무 벌어져서,
어떤 단어 하나에만 지나치게 쏠릴 수 있다.

그래서 `sqrt(d_k)`로 나눠 점수의 크기를 적당히 눌러 준다.

즉, 이 항은 복잡한 트릭이라기보다,
**attention 점수가 너무 과격해지지 않게 하는 스케일 조정 장치**라고 이해하면 된다.

### 7-5. softmax는 왜 필요한가

점수만 있다고 바로 Value를 섞을 수는 없다.  
각 단어를 얼마나 참고할지를 "비율" 형태로 바꾸는 단계가 필요하다.  
그 역할이 softmax다.

softmax를 거치면:

- 모든 점수가 0 이상이 되고
- 전체 합이 1이 되므로
- 가중치처럼 쓰기 쉬워진다

예를 들어 어떤 단어를 볼 때 attention 분포가 아래처럼 나왔다고 해 보자.

```text
[0.1, 0.7, 0.2]
```

그러면 첫 번째 단어 정보는 조금, 두 번째 단어 정보는 많이, 세 번째 단어 정보는 중간 정도로 반영하겠다는 뜻이다.

즉, softmax는 "누구를 더 참고할지"를 사람이 읽을 수 있는 비율 형태로 바꿔 준다.

### 7-6. 마지막 `V` 곱은 결국 가중합이다

softmax까지 끝나면 이제 각 단어를 얼마나 참고할지가 정해진다.  
그 다음에는 그 비율만큼 Value를 섞어 새 표현을 만든다.

즉, 마지막 `... V`는 어려운 수학이라기보다,  
**참고 비율을 이용해 다른 단어 정보를 평균 내듯 섞는 과정**이다.

그래서 Self-Attention의 결과는 단순히 원래 단어 벡터가 아니라,  
문장 안 다른 단어 정보가 반영된 새로운 문맥 표현이 된다.

## 8. 왜 Multi-Head인가

강의 자료는 Multi-Head라는 표현도 강조한다.  
이것도 직관적으로 보면 어렵지 않다.

하나의 attention만 쓰면, 단어 관계를 한 가지 방식으로만 볼 수 있다.  
하지만 여러 head를 쓰면 서로 다른 관점의 관계를 동시에 볼 수 있다.

예를 들어 같은 문장에서:

- 어떤 head는 주어-동사 관계를 더 강하게 보고
- 어떤 head는 수식어-피수식어 관계를 더 강하게 보고
- 어떤 head는 멀리 떨어진 단어 연결을 더 강하게 볼 수 있다

강의 자료에서는 CNN에서 여러 개 커널을 두는 것과 비슷한 발상이라고 설명한다.  
즉, Multi-Head는 "관계를 한 가지 방식으로만 보지 말자"는 아이디어다.

### 8-1. 수학적으로 Multi-Head는 무엇을 뜻하는가

Multi-Head Attention은 attention을 한 번만 하는 것이 아니라,  
서로 다른 가중치 세트를 가진 attention을 여러 번 병렬로 수행하는 구조다.

즉, 각 head마다 다음이 따로 있다.

- `W_Q`
- `W_K`
- `W_V`

그래서 head마다 서로 다른 관점으로 Q, K, V를 만들 수 있다.  
어떤 head는 가까운 단어 관계에 집중하고, 어떤 head는 멀리 떨어진 문법 관계를 더 볼 수 있다.

결과적으로 Multi-Head는 "한 번의 attention"보다 더 풍부한 관계 표현을 만든다.

## 9. Transformer가 병렬 처리가 가능하다는 말의 의미

강의 자료 초반부터 계속 "병렬 실행이 가능하다"는 표현이 나온다.  
이건 단순히 빠르다는 말이 아니라, 구조 자체가 RNN과 다르다는 뜻이다.

RNN은:

- 첫 단어 계산 후 둘째 단어
- 둘째 단어 계산 후 셋째 단어

순서로 간다.

Transformer는:

- 문장 전체 단어를 한 번에 입력으로 받고
- 단어들 사이 상관관계를 동시에 계산할 수 있다

물론 디코더에서 생성 단계는 여전히 순차성이 남지만, 적어도 인코더의 문장 이해 부분은 병렬화에 훨씬 유리하다. 그래서 긴 문장을 처리할 때 학습 효율 면에서 큰 장점이 생긴다.

## 10. Masked Multi-Head Attention은 왜 필요한가

강의 자료 77페이지는 `Masked Multi-Head Attention`을 설명한다.

![Masked Multi-Head Attention 슬라이드](/images/posts/intel-nlp-extra4-transformer/transformer-slide-77-77.png)

여기서 masked의 뜻은 간단하다.

> 디코더가 미래 단어를 미리 보지 못하게 막는다.

예를 들어 문장을 생성할 때:

- 지금 세 번째 단어를 예측하는 중인데
- 네 번째, 다섯 번째 정답 단어를 미리 보면 안 된다

그래서 디코더에서는 현재 시점 이후의 단어를 가려 놓는다.  
이렇게 해야 모델이 진짜 생성 문제를 풀게 된다.

즉, masked attention은 공정한 시험 환경을 만드는 장치라고 보면 된다.

### 10-1. mask는 수학적으로 어떻게 동작하는가

masked attention은 보통 미래 위치의 score에 아주 작은 값을 넣어 softmax 이후 거의 0이 되게 만든다.

직관적으로 보면:

- 볼 수 있는 위치는 그대로 점수를 계산하고
- 보면 안 되는 위치는 사실상 선택 불가능하게 만든다

즉, mask는 attention 구조를 바꾸는 것이 아니라,  
**참고하면 안 되는 위치의 점수를 강제로 막는 장치**다.

## 11. Feed Forward는 왜 또 필요한가

Attention만으로 끝나는 것이 아니라, 강의 자료 78페이지는 Feed Forward 층도 따로 설명한다.

![Feed Forward 슬라이드](/images/posts/intel-nlp-extra4-transformer/transformer-slide-78-78.png)

직관적으로 보면 이 층은 attention 결과를 더 풍부하게 가공하는 역할을 한다.

- attention으로 단어 관계를 반영한 표현을 만들고
- feed forward로 그 표현을 더 복잡한 특징으로 변환한다

즉, attention이 "누가 누구와 관련 있는지"를 본다면,  
feed forward는 "그 관계를 반영한 표현을 더 잘 쓰게 다듬는 과정"에 가깝다.

강의 자료에서도 이 층이 비선형성을 도입해 표현력을 강화한다고 설명한다.

### 11-1. Feed Forward의 수학은 왜 필요한가

attention만 있으면 단어 관계를 섞는 데는 강하지만,  
각 위치에서 표현 자체를 더 복잡하게 가공하는 단계가 약할 수 있다.

그래서 보통 각 위치마다 같은 작은 신경망을 통과시킨다.  
형태는 대략 아래처럼 생각하면 된다.

```text
FFN(x) = W_2 * activation(W_1 * x + b_1) + b_2
```

즉,

- 한 번 차원을 늘리거나 변환하고
- 비선형 함수를 통과시키고
- 다시 원하는 차원으로 줄이는 구조다

이 단계가 들어가야 attention이 만든 문맥 표현을 더 풍부하게 바꿀 수 있다.

## 12. Add: 왜 원래 입력을 다시 더하나

강의 자료 79페이지는 Add 단계에서 residual connection을 쓴다고 설명한다.

![Residual Connection 슬라이드](/images/posts/intel-nlp-extra4-transformer/transformer-slide-79-79.png)

수식은 단순하다.

```text
출력 = 층을 지난 결과 + 원래 입력
```

이걸 왜 하느냐가 중요하다.

- 모델이 깊어질수록 학습이 어려워질 수 있다
- 중간 층이 원래 정보까지 너무 많이 망가뜨릴 수 있다
- 원래 입력을 더해 주면 정보 흐름이 더 안정적이 된다

즉, residual connection은 "새로 계산한 결과만 믿지 말고, 원래 정보도 같이 가져가자"는 장치다. 깊은 네트워크에서 학습 안정성을 높이는 데 매우 자주 쓰인다.

### 12-1. Add가 수학적으로 중요한 이유

Residual connection은 아래처럼 매우 단순하다.

```text
y = F(x) + x
```

하지만 이 단순함이 중요하다.  
만약 중간 블록이 완벽하지 않아도, 최소한 원래 정보 `x`는 다음 층으로 전달될 수 있기 때문이다.

즉, 깊은 모델에서 "새 계산이 망가져도 원래 입력이 완전히 사라지지 않게 하는 안전장치"라고 볼 수 있다.

## 13. Norm: 왜 정규화를 붙이나

강의 자료 80페이지는 Norm, 즉 Layer Normalization을 설명한다.

![Layer Normalization 슬라이드](/images/posts/intel-nlp-extra4-transformer/transformer-slide-80-80.png)

직관적으로는 이렇게 보면 된다.

- 층을 지나면서 값 분포가 계속 흔들릴 수 있다
- 그러면 학습이 불안정해질 수 있다
- 정규화를 통해 값의 스케일을 안정적으로 맞춰 준다

즉, Norm은 모델이 너무 들쭉날쭉하게 흔들리지 않도록 균형을 잡아 주는 단계다.

Residual Connection이 정보 흐름을 안정화한다면,  
Layer Normalization은 값의 분포를 안정화한다고 보면 된다.

### 13-1. Layer Normalization을 너무 어렵게 볼 필요는 없다

Layer Normalization도 수학식으로 쓰면 평균과 분산을 이용한 정규화라서 복잡해 보일 수 있다.  
하지만 입문 단계에서는 아래처럼 이해해도 충분하다.

- 한 위치의 벡터 값들이 너무 치우치지 않게 정리하고
- 학습 중 값의 스케일이 흔들리는 것을 줄여서
- 다음 층이 더 안정적으로 계산하게 만든다

즉, Norm은 attention이나 feed forward처럼 의미를 새로 만드는 층이 아니라,  
**계산이 지나치게 흔들리지 않도록 균형을 잡는 층**이다.

## 14. Transformer 블록을 사람 말로 바꾸면

여기까지를 아주 단순하게 요약하면 Transformer 블록은 아래처럼 이해할 수 있다.

1. 단어를 벡터로 바꾼다
2. 위치 정보도 넣는다
3. 각 단어가 다른 단어를 얼마나 참고해야 하는지 계산한다
4. 그 결과를 더 복잡한 표현으로 바꾼다
5. 원래 입력도 더해 준다
6. 정규화해서 다음 블록으로 넘긴다

즉, Transformer는 "문장 전체 관계를 한 번에 보고, 그 표현을 안정적으로 반복해서 다듬는 구조"라고 볼 수 있다.

## 15. RNN, LSTM, GRU와 비교하면 무엇이 달라졌는가

이 부분이 가장 중요하다.

### 15-1. RNN 계열

- 순서대로 읽는다
- 이전 state를 다음 state로 넘긴다
- 시간축 흐름이 핵심이다

### 15-2. Transformer

- 단어 관계를 한 번에 본다
- attention으로 문맥 연결을 계산한다
- 병렬 계산이 가능하다

즉, RNN 계열은 "흐름 중심"이고, Transformer는 "관계 중심"이라고 정리할 수 있다.

그래서 긴 문장 안에서 멀리 떨어진 단어 관계를 잡는 데 Transformer가 훨씬 유리해진다. `not ... good`처럼 떨어진 표현이나, 복잡한 수식 관계, 긴 문장 구조를 다루는 데 강점을 보이는 이유도 여기에 있다.

## 16. 강의 자료 기준으로 꼭 남겨야 할 핵심

강의 자료 69~80페이지를 한 문장씩 줄이면 아래처럼 정리된다.

### 16-1. Transformer는 Attention 중심 구조다

순환 계산보다 단어 간 관계 계산이 핵심이다.

### 16-2. Positional Encoding이 필요하다

병렬 처리 구조라서 순서 정보는 따로 넣어야 한다.

### 16-3. Self-Attention이 문맥 이해의 중심이다

각 단어가 다른 단어를 얼마나 참고해야 하는지 계산한다.

### 16-4. Multi-Head는 여러 관점의 관계를 동시에 본다

하나의 관계만 보지 않고, 다양한 패턴을 함께 학습한다.

### 16-5. Feed Forward, Add, Norm은 성능과 안정성을 위한 장치다

Attention 결과를 더 풍부하게 만들고, 깊은 모델도 안정적으로 학습하게 한다.

### 16-6. 수학적으로 꼭 잡아야 하는 핵심

Transformer 수학을 전부 계산하지 않아도, 아래 여섯 줄이 이해되면 충분하다.

- `Q`, `K`, `V`는 같은 입력을 서로 다른 역할로 선형변환한 결과다.
- `QK^T`는 단어끼리 얼마나 관련 있는지 점수를 만드는 단계다.
- `/ sqrt(d_k)`는 점수가 너무 커지는 것을 막는 스케일 조정이다.
- `softmax`는 그 점수를 참고 비율로 바꾼다.
- 마지막 `V` 곱은 그 비율대로 다른 단어 정보를 가중합하는 과정이다.
- Multi-Head는 이런 attention을 여러 관점으로 동시에 수행하는 구조다.

## 마무리

Transformer는 처음 보면 용어가 많아서 더 어렵게 느껴질 수 있다. 하지만 큰 흐름은 오히려 명확하다. 순서대로 하나씩 읽는 대신, 문장 전체를 보고 단어 간 관계를 계산하자는 구조다. 그리고 순서 정보는 positional encoding으로 따로 넣고, attention 결과는 feed forward, residual, normalization으로 계속 다듬는다.

즉, RNN에서 LSTM, GRU로 가는 흐름이 "기억 구조를 조금씩 개선하는 과정"이었다면, Transformer는 아예 문장을 보는 방식 자체를 바꾼 구조라고 볼 수 있다. 여기까지 이해하면 이후 BERT, GPT 같은 모델도 결국 Transformer 위에 서 있다는 사실이 자연스럽게 연결된다.
