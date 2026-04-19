---
title: "[Tokit] Extra 3 - 서비스 방향과 진짜 효과가 나는 지점 정리"
slug: tokit-extra3-service-direction-and-leverage-points
date: 2026-04-19
author: Evan Yoon
category: project
subcategory: team-project
description: |
  Tokit을 실제로 유용하고 차별성 있는 서비스로 만들려면
  어디에 집중해야 하는지, 효과가 큰 것과 약한 것,
  그리고 적용 가능한 최신 기술까지 정리했다.
thumbnail: ""
tags:
  - tokit
  - product-strategy
  - cli
  - token-optimization
  - session-management
  - prompt-caching
  - team-project
readTime: 9
series: "Tokit"
seriesOrder: 3
featured: false
draft: false
toc: true
---

## 한 줄 결론

**Tokit은 "프롬프트를 예쁘게 줄이는 도구"보다, Claude Code나 Codex CLI에 설치하면 자동으로 토큰 비용과 지연을 줄여주는 한국어 개발자용 최적화 레이어로 가는 쪽이 더 유용하고 차별성도 크다.**

---

## 가장 좋은 서비스 방향

내가 보기엔 제일 좋은 방향은 이거다.

### 설치하면 자동 적용되는 CLI 레이어

형태는 단순해야 한다.

- `npm install -g tokit`
- `tokit init`
- 기존 CLI 앞단에 wrapper 또는 hook으로 연결
- 이후 입력 전처리, 세션 상태 저장, benchmark, 캐싱이 자동 적용

이 방식이 좋은 이유:

- 사용자가 습관을 바꿀 필요가 없다
- 설치 전/후 비교가 쉽다
- 데모가 강하다
- Tokit이 별도 앱이 아니라 기존 CLI를 더 잘 쓰게 만드는 도구로 보인다

즉, Tokit은 독립형 서비스보다 **개발자가 이미 쓰는 CLI에 붙는 최적화 레이어**로 가는 게 맞다.

---

## 실제로 효과가 큰 것

### 1. 세션 단위 최적화

이건 가장 중요하다.

입력 한 번 줄이는 것보다, 여러 턴에서 반복되는 컨텍스트를 안 보내는 쪽이 훨씬 효과가 크다.

그래서 Tokit은

- 이전 턴 결과 저장
- 다음 턴에 필요한 정보만 전달
- 중복 컨텍스트 제거
- 캐싱

이 중심이어야 한다.

### 2. Prompt caching 친화적인 구조

이건 실제 비용 절감과 바로 연결된다.

OpenAI와 Anthropic 모두 반복되는 prefix 재사용이 중요하다고 명시한다.

핵심은:

- static instructions / tools / examples는 앞에
- 변하는 user input은 뒤에
- 세션 요약도 재사용 가능한 prefix로 설계

즉, Tokit은 그냥 문장을 줄이는 도구가 아니라 **cache hit 잘 나는 prompt 구조를 만드는 도구**가 될 수 있다.

출처:

- [OpenAI Prompt Caching Best Practices](https://platform.openai.com/docs/guides/prompt-caching/best-practices)
- [Anthropic Prompt Caching](https://docs.anthropic.com/en/docs/build-with-claude/prompt-caching)

### 3. 보호 구간 처리

이건 절감 기능이라기보다 품질 방어 기능이다.

효과:

- 코드 블록이 망가지지 않음
- 파일 경로가 안 깨짐
- 에러 메시지가 유지됨
- 명령어가 오염되지 않음

Tokit이 신뢰를 가지려면 이게 먼저 있어야 한다.

### 4. 작업 유형 분류

debug, review, explain, generation을 같은 방식으로 처리하면 품질이 흔들린다.

그래서 Tokit은 단순 압축기보다 **작업 분류 기반 최적화기**에 가까워야 한다.

### 5. diff/patch 중심 출력 유도

출력 토큰 절감 쪽에서 가장 현실적인 방법이다.

전체 코드 재출력 대신

- diff
- patch
- 변경된 함수 단위
- 구조화된 결과

같이 작게 나오게 만들어야 한다.

이건 "짧게 대답하게 만들기"보다 훨씬 실용적이다.

### 6. AST 기반 코드 컨텍스트 축소

이건 지금 논의보다 더 강조할 가치가 있다.

파일 전체를 보내는 대신

- 관련 함수
- 관련 클래스
- 관련 심볼

만 추출해서 전달하면 토큰이 훨씬 줄어든다.

이건 자연어 압축보다 실제 효과가 클 가능성이 높다.

참고:

- [ast-grep](https://ast-grep.github.io/)

---

## 효과가 약하거나 위험한 것

### 1. 한국어를 영어로 바꾸는 것만으로 승부 보기

이건 필요는 하지만, 이것만으로는 약하다.

이유:

- 짧은 입력에서는 절감 폭이 작을 수 있다
- 잘못 압축하면 의미 손실이 생긴다
- provider prompt caching이나 세션 재사용보다 체감 이득이 작을 수도 있다

즉:

- 한국어 -> 영어 변환은 필요
- 하지만 Tokit의 핵심 가치 전부가 되면 약함

### 2. 출력을 강하게 압축하는 것

위험하다.

문제:

- 코드 품질 깨짐
- 설명 품질 깨짐
- 리뷰/디버깅에서 중요한 조건 누락

그래서 출력은 "짧게 만들기"보다 **작게 나오게 설계하기**가 맞다.

### 3. 무거운 멀티모델 체인

지금은 과하다.

문제:

- 느려짐
- 복잡해짐
- 데모 어려워짐
- 디버깅 어려워짐

초기에는

- 1개의 컴파일 단계
- 1개의 실행 단계
- 1개의 세션 상태 레이어

정도로 가는 게 맞다.

### 4. fine-tuning

지금 단계에서는 우선순위가 낮다.

Tokit의 문제는 모델 능력 부족보다

- 프롬프트 구조
- 세션 재사용
- 출력 형식

쪽에 더 가깝다.

---

## 지금 논의에는 없었지만 적용 가능한 최신 기술

### 1. Provider prompt caching 활용

가장 현실적이고 바로 효과가 난다.

Tokit이 할 수 있는 건:

- static prefix 구조화
- cache-friendly prompt layout
- cache hit metrics 노출

이건 실제 서비스화할 때 강한 포인트가 될 수 있다.

### 2. Semantic cache

정확히 같은 요청이 아니어도, 의미적으로 비슷한 요청이면 결과나 전략을 재사용하는 방향이다.

지금 당장 1순위는 아니지만, 중장기적으로는 분명 가치가 있다.

Tokit에 적용하면:

- 비슷한 debug 요청
- 비슷한 review 요청
- 비슷한 compile pattern

재사용이 가능해진다.

참고:

- [Semantic Caching for Low-Cost LLM Serving](https://www.microsoft.com/en-us/research/publication/semantic-caching-for-low-cost-llm-serving-from-offline-learning-to-online-adaptation/)

### 3. Faithful prompt compression

그냥 줄이는 게 아니라, 의미 보존형으로 압축하는 방향이다.

이건 한국어 입력 처리 쪽에서 나중에 꽤 유용할 수 있다.

적용 방식:

- 조사/완곡 표현 제거
- 유지해야 할 토큰 분류
- 규칙 기반 + 소형 분류기 조합

참고:

- [LLMLingua](https://llmlingua.com/llmlingua.html)
- [LLMLingua-2](https://llmlingua.com/llmlingua2.html)

### 4. Session memory layer

프롬프트 컴파일러에서 한 걸음 더 나가면, 세션 메모리 레이어가 된다.

이건 현재 에이전트 도구 흐름과도 맞는다.

적용하면:

- 최근 작업 기억
- 프로젝트 규칙 기억
- 실패했던 접근 기억
- 다음 세션으로 상태 이어주기

가 가능해진다.

참고:

- [succ](https://succ.ai/)
- [Mem0 OSS Overview](https://docs.mem0.ai/open-source)

---

## 내가 추천하는 Tokit 포지션

가장 설득력 있는 문장은 이거다.

**Tokit은 Claude Code / Codex CLI에 설치하면 자동으로 프롬프트 구조, 세션 맥락, 출력 형식을 최적화해서 토큰 비용과 지연을 줄여주는 한국어 개발자용 optimization layer다.**

이 포지션이 좋은 이유:

- 설치형이라 유용성 높음
- 기존 도구 위에 올라가서 도입 쉬움
- 한국어 개발자 입력이라는 차별점이 분명함
- 세션, patch, AST까지 확장 가능

---

## 1차 MVP에서 꼭 보여줘야 하는 것

1. 설치/초기화 한 번으로 연결
2. 한국어 입력 -> 영어 coding prompt 변환
3. 보호 구간 유지
4. 원문 vs 컴파일 토큰 비교
5. 세션 저장
6. 반복 요청에서 절감 예시

여기까지 되면 Tokit은 설명용 아이디어가 아니라 실제 서비스 후보처럼 보일 수 있다.

---

## 최종 정리

### 효과가 큰 것

- 설치형 CLI integration
- provider prompt caching 친화 구조
- 세션 상태 관리
- 보호 구간 처리
- 작업 유형 분류
- diff/patch 중심 출력
- AST 기반 컨텍스트 축소

### 효과가 약하거나 위험한 것

- 단순 한국어 -> 영어 변환만으로 차별화하려는 것
- 출력을 억지로 짧게 만드는 것
- 무거운 멀티모델 체인
- fine-tuning부터 들어가는 것

### Tokit의 진짜 차별점

- 한국어 개발자 입력
- 설치하면 자동 적용
- 세션 단위 최적화
- coding CLI 환경 특화
- 입력 / 출력 / 세션을 한 흐름으로 묶는 구조

지금 기준으로는 이 다섯 가지를 같이 가져가는 쪽이 Tokit을 제일 서비스답게 만든다.
