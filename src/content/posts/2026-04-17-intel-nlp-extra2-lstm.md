---
title: "[NLP] extra 2: LSTM을 기초부터 다시 이해하기"
slug: intel-nlp-extra2-lstm
date: 2026-04-17
author: Evan Yoon
category: study
subcategory: bootcamp
description: |
  RNN, LSTM, GRU, Transformer 강의 자료 중 42~63페이지를 바탕으로
  LSTM이 왜 나왔는지, cell state와 hidden state가 어떻게 다르고,
  forget gate, input gate, output gate가 각각 무슨 역할을 하는지 쉽게 풀어 정리했다.
thumbnail: /images/posts/intel-nlp-extra2-lstm/lstm-slide-45-45.png
tags:
  - NLP
  - LSTM
  - RNN
  - intel-nlp-extra
  - sequence-model
  - deep-learning
  - nlp-study
readTime: 17
series: "Intel NLP extra"
seriesOrder: 2
featured: false
draft: false
toc: true
---

RNN을 이해하고 나면 자연스럽게 다음 질문이 생긴다.  
"이전 정보를 넘긴다는 건 알겠는데, 왜 RNN은 긴 문장에서 자꾸 약해질까?"  
LSTM은 바로 그 문제를 줄이기 위해 나온 구조다. 강의 자료에서는 LSTM을 "장기 의존성 문제를 완화한 RNN 개선 모델"이라고 설명한다.

이번 글에서는 강의 자료 42~63페이지를 기준으로, LSTM을 수식보다 역할 중심으로 다시 정리해 보려고 한다. 핵심은 아래 네 가지다.

1. LSTM이 왜 필요한가
2. cell state와 hidden state는 무엇이 다른가
3. forget, input, output gate는 각각 무슨 일을 하는가
4. LSTM이 RNN보다 긴 문장에서 왜 더 유리한가

## 1. RNN 다음에 바로 LSTM이 나온 이유

RNN 글 마지막에서 봤듯이, RNN은 구조는 자연스럽지만 긴 시퀀스를 학습할수록 초반 정보를 잘 유지하지 못한다. 강의 자료 42페이지는 먼저 그 문제를 완화하는 한 가지 실무적 방법으로 `Truncated BPTT`를 언급한다.

![Truncated BPTT 슬라이드](/images/posts/intel-nlp-extra2-lstm/lstm-slide-42-42.png)

이 방식은 아주 긴 시퀀스를 적당히 끊어서 역전파를 짧게 하는 방법이다.  
즉, 너무 긴 시간축 전체를 한 번에 학습하지 않고 구간을 나눠 처리한다.

다만 이 방법은 근본적으로 "기억 구조"를 바꾸는 것은 아니다.  
그래서 등장한 것이 LSTM이다.

## 2. LSTM은 RNN에 무엇을 추가했는가

강의 자료에서는 LSTM을 설명하면서 두 가지를 강조한다.

- `cell state (c_t)` 구조를 제안했다.
- 세 가지 gate를 추가했다.

![LSTM 소개 슬라이드](/images/posts/intel-nlp-extra2-lstm/lstm-slide-43-43.png)

RNN을 간단히 말하면 이랬다.

```text
이전 hidden state + 현재 입력 → 현재 hidden state
```

LSTM은 여기에 "기억 전용 통로"를 하나 더 둔다.

```text
이전 hidden state + 현재 입력 → gate 계산
이전 cell state와 새 후보 정보 → 현재 cell state
현재 cell state → 현재 hidden state
```

즉, LSTM은 RNN처럼 단순히 메모를 하나만 넘기는 방식이 아니라,

- 오래 들고 갈 기억용 통로
- 지금 바로 출력에 쓸 정보용 통로

를 구분한 구조라고 이해하면 훨씬 쉽다.

## 3. cell state와 hidden state를 따로 두는 이유

이 부분이 LSTM에서 가장 중요하다. 강의 자료 45페이지는 `cell state`와 `hidden state`를 분리해서 설명한다.

![Cell state와 hidden state 설명 슬라이드](/images/posts/intel-nlp-extra2-lstm/lstm-slide-45-45.png)

### 3-1. Cell state는 장기 메모리

강의 자료 표현대로 보면 cell state는 장기적으로 정보를 유지하는 통로다.

- 시퀀스 전체에서 중요한 정보를 오래 유지하려고 한다.
- 필요 없는 것은 지우고, 필요한 것만 추가한다.
- 시간축을 따라 비교적 안정적으로 흐른다.

사람으로 비유하면 아래와 비슷하다.

- 수업을 들으면서 핵심 개념만 노트에 남겨 두는 것
- 세부 문장 하나하나보다 "이건 중요한 내용"이라는 요약을 계속 들고 가는 것

### 3-2. Hidden state는 현재 시점 요약

hidden state는 현재 시점에서 바로 쓸 요약 정보다.

- 지금 순간의 상태를 반영한다.
- 출력 계산에 직접 연결된다.
- 다음 시점 계산에도 들어간다.

즉, LSTM은 기억을 두 층으로 나눈다고 보면 된다.

- `cell state`: 오래 가져갈 메모
- `hidden state`: 지금 바로 꺼내 쓸 메모

RNN이 메모 하나만 쓰는 구조였다면, LSTM은 메모장을 두 칸으로 나눈 셈이다.

## 4. LSTM의 전체 흐름을 먼저 한 번 보기

강의 자료는 LSTM 계산을 세 단계로 정리한다.

![LSTM 전체 흐름 요약 슬라이드](/images/posts/intel-nlp-extra2-lstm/lstm-slide-50-50.png)

1. gate 계산하기
2. cell state 업데이트하기
3. hidden state 업데이트하기

이걸 먼저 큰 흐름으로 이해하면 수식이 훨씬 덜 부담스럽다.

### 4-1. gate 계산

현재 입력 `x_t`와 이전 hidden state `h_{t-1}`를 보고 세 개의 gate 값을 만든다.

- forget gate `f_t`
- input gate `i_t`
- output gate `o_t`

### 4-2. cell state 업데이트

이전 cell state에서 무엇을 남길지 결정하고,  
현재 입력에서 새로 기억할 후보 정보를 더해서  
새로운 cell state를 만든다.

### 4-3. hidden state 업데이트

업데이트된 cell state 중에서 지금 출력으로 꺼낼 부분만 골라 hidden state를 만든다.

즉, LSTM은 아무 정보나 무조건 기억하는 것이 아니라,

- 버릴 것
- 새로 넣을 것
- 밖으로 꺼낼 것

을 따로 관리한다.

## 5. Gate는 무엇인가

강의 자료 51페이지는 gate를 0~1 사이 값을 가지는 벡터라고 설명한다.

![Gate 설명 슬라이드](/images/posts/intel-nlp-extra2-lstm/lstm-slide-51-51.png)

수학을 깊게 몰라도 이건 이렇게 이해하면 된다.

- 0에 가까우면: 거의 안 통과시킨다
- 1에 가까우면: 거의 그대로 통과시킨다
- 중간값이면: 일부만 반영한다

즉, gate는 일종의 **조절 밸브**다.  
물을 틀고 잠그는 수도꼭지처럼, 정보가 얼마나 지나갈지 조절한다.

그리고 중요한 점은 세 gate가 서로 같은 역할을 하지 않는다는 것이다.  
각 gate는 서로 다른 가중치를 써서, 서로 다른 판단을 하도록 학습된다.

## 6. Forget gate: 무엇을 잊을지 정한다

forget gate는 이름 그대로, 이전 기억 중 무엇을 지울지 정한다.

![Forget gate 슬라이드](/images/posts/intel-nlp-extra2-lstm/lstm-slide-52-52.png)

강의 자료 표현대로 보면 forget gate는 과거 cell state에서 사용하거나 사용하지 않을 데이터에 대한 가중치다.

이걸 문장 예시로 바꾸면 이해가 쉽다.

예를 들어 문장을 읽다가 주제가 바뀌는 경우가 있다.

> I was talking about the weather, but now I want to discuss the movie.

이럴 때 앞쪽의 날씨 관련 정보는 뒤로 갈수록 중요하지 않을 수 있다.  
forget gate는 이런 과거 정보를 얼마나 남길지 조절한다.

강의 자료 57~59페이지는 forget gate가 실제로 어떻게 작동하는지 숫자 예시로 보여 준다.

![Forget gate가 모두 1인 경우](/images/posts/intel-nlp-extra2-lstm/lstm-slide-57-57.png)

gate 값이 전부 1이면 이전 기억을 거의 그대로 가져간다.

![Forget gate가 모두 0인 경우](/images/posts/intel-nlp-extra2-lstm/lstm-slide-58-58.png)

gate 값이 전부 0이면 이전 기억을 사실상 다 지운다.

![Forget gate 일반적인 경우](/images/posts/intel-nlp-extra2-lstm/lstm-slide-59-59.png)

실제로는 0과 1 사이의 값이 나오므로, 어떤 정보는 많이 남기고 어떤 정보는 조금만 남긴다.

즉, forget gate는 "무조건 다 기억"도 아니고 "무조건 다 삭제"도 아니라,  
**과거 정보에 선택적으로 감쇠를 주는 장치**라고 보면 된다.

## 7. Input gate: 지금 정보를 얼마나 새로 넣을지 정한다

input gate는 현재 시점에서 새로 들어온 정보를 얼마나 기억할지 정한다.

![Input gate 슬라이드](/images/posts/intel-nlp-extra2-lstm/lstm-slide-53-53.png)

forget gate가 과거 정리 담당이라면, input gate는 새 정보 입력 담당이다.

예를 들어 아래 문장을 보자.

> The movie was boring at first, but the ending was amazing.

문장 후반부에서 `amazing` 같은 강한 단어가 나오면, 지금 시점의 정보는 더 강하게 기억할 필요가 있다. 반대로 의미가 약한 정보는 조금만 반영해도 된다.

강의 자료 56페이지는 먼저 현재 입력과 이전 hidden state를 바탕으로 임시 cell state 후보를 만들고,

![Cell state 후보 생성 슬라이드](/images/posts/intel-nlp-extra2-lstm/lstm-slide-56-56.png)

60페이지에서는 input gate가 그 후보 정보에 얼마나 가중치를 줄지 보여 준다.

![Input gate 적용 예시 슬라이드](/images/posts/intel-nlp-extra2-lstm/lstm-slide-60-60.png)

즉, input gate는 "새로운 정보를 기억할지 말지"를 정하는 게이트다.  
forget gate가 과거를 정리한다면, input gate는 현재를 받아 적는 역할이다.

## 8. Cell state 업데이트는 결국 두 가지의 합이다

강의 자료의 핵심 수식은 결국 아래 뜻으로 읽으면 된다.

```text
새 cell state
= 남겨 둘 과거 기억
+ 새로 쓸 현재 기억
```

조금 더 LSTM답게 쓰면:

```text
c_t = (forget gate × 이전 c_t-1) + (input gate × 현재 후보 정보)
```

이 구조가 중요한 이유는, RNN보다 훨씬 명시적으로 기억을 관리한다는 점이다.

- 과거는 forget gate를 통해 정리하고
- 현재는 input gate를 통해 반영한다

즉, LSTM은 "한 번에 뒤섞어 계산"하는 느낌보다, 기억을 관리하는 절차가 분리되어 있다.

그래서 긴 시퀀스에서 더 안정적으로 동작할 여지가 생긴다.

## 9. Output gate: 지금 무엇을 밖으로 보여 줄지 정한다

output gate는 cell state 전체를 다 노출하지 않고, 지금 시점에서 필요한 부분만 hidden state로 꺼내는 역할을 한다.

![Output gate 슬라이드](/images/posts/intel-nlp-extra2-lstm/lstm-slide-54-54.png)

그리고 강의 자료 61~62페이지는 hidden state 업데이트를 따로 정리한다.

![Hidden state 업데이트 요약 슬라이드](/images/posts/intel-nlp-extra2-lstm/lstm-slide-61-61.png)

![Output gate와 hidden state 계산 예시](/images/posts/intel-nlp-extra2-lstm/lstm-slide-62-62.png)

직관은 이렇다.

- cell state는 장기 메모리라 비교적 많이 들고 간다.
- 하지만 모든 걸 그대로 출력으로 쓰지는 않는다.
- output gate가 "지금 필요한 부분"만 걸러서 hidden state를 만든다.

즉, output gate는 메모장을 전부 펼치는 것이 아니라,  
**지금 발표에 필요한 부분만 발췌해서 말하는 역할**에 가깝다.

## 10. LSTM을 사람식으로 다시 풀어 보면

여기까지를 아주 일상적으로 바꾸면 아래처럼 이해할 수 있다.

### 10-1. Forget gate

"예전에 적어 둔 내용 중 지금은 덜 중요한 건 좀 흐리게 하자."

### 10-2. Input gate

"방금 들어온 정보 중 중요한 건 새로 적어 두자."

### 10-3. Cell state

"지금까지 유지해 온 핵심 메모"

### 10-4. Output gate

"그 메모 전체를 다 말하지 말고, 지금 필요한 부분만 꺼내서 쓰자."

이렇게 보면 LSTM은 수식 덩어리가 아니라,  
**기억을 지우고, 추가하고, 꺼내는 세 단계를 명확하게 나눈 모델**이라고 이해할 수 있다.

## 11. LSTM이 RNN보다 긴 문장에 유리한 이유

강의 자료의 첫 줄 요약은 결국 이것이다.

> LSTM은 장기 의존성 문제를 완화한 RNN 개선 모델이다.

왜 완화되는가를 직관적으로 보면 다음과 같다.

### 11-1. 기억 전용 통로가 따로 있다

RNN은 hidden state 하나에 모든 역할이 몰려 있었다.  
LSTM은 cell state를 따로 둬서 장기 기억 통로를 분리했다.

### 11-2. gate로 선택적으로 관리한다

정보를 무조건 다 전달하는 것이 아니라,

- 지울 것
- 새로 넣을 것
- 출력할 것

을 나눠서 관리한다.

### 11-3. 긴 문장에서 중요 정보가 덜 흐려진다

완벽하게 기억하는 것은 아니더라도,  
RNN처럼 단순 반복 곱으로만 밀려가는 구조보다  
중요한 정보를 좀 더 오래 유지할 수 있다.

즉, LSTM은 "기억을 잘하기 위해 더 복잡하게 만든 RNN"이라고 보면 된다.

## 12. 강의 자료 기준으로 꼭 기억할 것

강의 자료 63페이지는 LSTM 전체를 다시 한 번 요약한다.

![LSTM review 슬라이드](/images/posts/intel-nlp-extra2-lstm/lstm-slide-63-63.png)

이 페이지 기준으로 꼭 남겨야 할 것은 아래 네 줄이다.

### 12-1. LSTM은 RNN의 개선형이다

긴 시퀀스에서 RNN이 약한 문제를 줄이기 위해 나왔다.

### 12-2. Cell state와 hidden state를 나눈다

- cell state: 장기 기억
- hidden state: 현재 시점 출력용 요약

### 12-3. 세 gate가 있다

- forget gate: 무엇을 잊을지
- input gate: 무엇을 새로 기억할지
- output gate: 무엇을 밖으로 보여 줄지

### 12-4. 결국 기억 관리 모델이다

LSTM은 단순히 값을 전달하는 모델이 아니라,  
기억을 선택적으로 관리하는 순차 모델이다.

## 마무리

RNN을 이해한 뒤 LSTM을 보면, 구조가 갑자기 복잡해진 것처럼 느껴진다. 하지만 역할 단위로 나눠 보면 생각보다 단순하다. LSTM은 RNN에 기억 관리 장치를 덧붙인 모델이다. 과거를 얼마나 잊을지, 현재를 얼마나 기록할지, 그리고 지금 무엇을 출력할지를 따로 정하도록 만든 구조다.

그래서 긴 문장이나 긴 시계열처럼 "먼 과거 정보가 아직 중요할 수 있는 문제"에서 RNN보다 더 안정적으로 쓰일 수 있다. 다음 글에서는 이 LSTM을 조금 더 단순하게 만든 GRU를 같은 방식으로 이어서 정리할 예정이다.
