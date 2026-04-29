---
title: "[DeToks] Day 9 - Role 2.1 분류기 보강과 발표 준비 시작"
slug: tokit-day9-role2-classifier-and-presentation-prep
date: 2026-04-29
author: Evan Yoon
category: project
subcategory: team-project
description: "4월 29일에는 DeToks의 Role 2.1 task graph를 실제 trace와 live llama 결과로 다시 살펴봤다. trace 파일명을 정리하고, TaskCandidateExtractor와 TaskGraphProcessor에서 어색하게 분류되던 표현들을 고쳤으며, 발표 준비 자료도 만들기 시작했다."
thumbnail: ""
tags:
  - tokit
  - detoks
  - llm
  - workflow
  - task-graph
  - role2
  - trace
  - classifier
  - presentation
  - team-project
readTime: 8
series: "Tokit"
seriesOrder: 13
featured: false
draft: false
toc: true
---

4월 29일은 어제 고친 Role 2.1 쪽을 실제 실행 결과로 다시 들여다본 날이었다. 어제는 `사용자 입력 -> 정규화 입력 -> Role 2.1 -> Task Graph`로 이어지게 큰 구조를 바꿨다. 오늘은 그 구조가 결제나 API 에러 처리 같은 다른 프롬프트에서도 자연스럽게 동작하는지 확인했다.

오늘 병합된 PR은 세 개였다.

```text
#177 Add and rename various test traces and templates
#180 Fix pipeline order in docs and improve task candidate extraction
#184 Enhance TaskGraphProcessor with new IDIOMs and refactoring verbs
```

짧게 말하면, 오늘은 DeToks가 사용자의 말을 작업 단위로 덜 어색하게 읽도록 고친 날이었다.

```text
어제:
  Role 2.1이 어떤 입력을 보고 task graph를 만들지 바꿨다.

오늘:
  그 입력 안에서 무엇을 작업으로 볼지,
  무엇은 그냥 설명으로 남겨둘지 손봤다.
```

## trace 파일부터 다시 정리했다

처음에는 검증 자료부터 정리했다. `.trace` 아래에 실행 결과 파일이 꽤 많이 쌓였는데, 파일명만 봐서는 어떤 프롬프트를 돌린 결과인지 알기 어려웠다.

기존에는 이런 이름이 많았다.

```text
.trace/028e697ffa83-trace.json
.trace/0a3743ac9f03-trace.json
.trace/VmT3xRaFqFMSVB9RtxQPp0RF-trace.json
```

이름만 봐서는 아무것도 알 수 없다. 결국 파일을 열어서 `raw_input`, `normalized_input`, task graph를 직접 봐야 했다. trace는 문제를 다시 확인하려고 남겨두는 자료인데, 정작 필요한 trace를 찾는 데 시간이 걸리면 계속 흐름이 끊긴다.

그래서 파일명에 실행 시각, 주제, 반복 순번이 보이게 바꿨다.

```text
YYYYMMDD-HHMMSS-topic-rNN-trace.json
```

예를 들면 이런 식이다.

```text
.trace/20260428-045421-calculator-project-add-subtract-r01-trace.json
.trace/20260427-024119-github-issue-pr-template-r06-trace.json
.trace/20260425-002902-python-bubble-sort-r01-trace.json
```

Role 2 live result도 같이 남겨뒀다.

```text
.trace/role2-handoff-5-task-real-result.json
.trace/role2-handoff-5-task-compression-real-result.json
.trace/role2-korean-hybrid-live-result.json
.trace/role2-korean-hybrid-alt-live-result.json
```

겉으로는 파일명만 바꾼 일이지만, 뒤에서 한 분류기 보강에도 도움이 됐다. 이제 어떤 입력에서 어떤 graph가 나왔는지 파일 목록만 봐도 대략 따라갈 수 있다.

## 문서의 파이프라인 순서도 맞췄다

문서도 다시 봤다. 일부 문서에는 압축이 task graph 분류보다 먼저 일어나는 것처럼 적혀 있었다. 어제 Role 2.1 handoff를 바꾸고 나니 이 설명이 더 이상 맞지 않았다.

```text
잘못 이해하기 쉬운 순서:
  번역/정규화 -> 압축 -> 분류

고친 순서:
  번역/정규화 -> 분류(task graph) -> 압축(task 실행 context)
```

압축은 실행 context를 줄일 때 필요하다. 다만 task graph를 만들기 전부터 압축해 버리면 사용자가 말한 작업 경계나 원인 맥락이 같이 사라질 수 있다. Role 2.1이 작업을 나누려면 압축된 요약보다 정규화된 원문 쪽이 더 낫다.

그래서 `docs/API_SPEC.md`, `docs/DES_DATA_FLOW.md`, `docs/PIPELINE.md`, `docs/SCHEMA_FLOW.md`, `docs/SHARED_DATA_FLOW.md`의 설명을 코드가 실제로 도는 순서에 맞게 고쳤다. 문서만 예쁘게 고친 게 아니라, 읽는 사람이 Role 2.1의 위치를 헷갈리지 않도록 맞춰둔 셈이다.

## 결제 프롬프트에서 걸린 부분

그다음에는 결제 도메인 한국어 프롬프트를 실제 llama 경로로 돌려봤다. 여기서 몇 가지 어색한 결과가 나왔다.

원래 보고 싶었던 순서는 이랬다.

```text
explore -> analyze -> modify -> validate -> validate -> document
```

작업 문장으로 풀면 대략 이런 내용이다.

```text
1. 결제/구독 관련 코드가 어디 있는지 찾는다.
2. 결제 버튼 이후 데이터가 어떻게 흐르는지 분석한다.
3. 중복된 validation logic을 공통 함수나 서비스로 정리한다.
4. 기존 결제 흐름이 깨지지 않았는지 확인한다.
5. 관련 단위 테스트나 회귀 테스트를 실행한다.
6. 변경 이유, 수정 파일, 테스트 결과를 문서화한다.
```

그런데 실제 LLM 번역 결과는 테스트용으로 손으로 만든 문장보다 훨씬 거칠었다. 특히 세 군데가 눈에 걸렸다.

```text
1. "and the test results..." 같은 명사구가 별도 작업처럼 잡혔다.
2. "briefly summarize..."처럼 부사로 시작하는 문장이 작업 후보에서 빠졌다.
3. validate -> validate처럼 검증이 이어질 때 dependency가 끊겼다.
```

첫 번째는 `TaskCandidateExtractor`가 절을 너무 넓게 잡아서 생긴 문제였다. `and organize...`처럼 실제 동작이 이어지는 경우는 살려야 한다. 반대로 `and the test results...`처럼 명사구만 이어지는 경우는 작업으로 보면 안 된다.

그래서 추출 조건을 조금 더 빡빡하게 바꿨다.

```text
기존:
  match.index > 8

수정:
  match.index > 5
```

두 번째는 문장 앞에 붙은 부사였다.

```text
briefly summarize the reason for the change
quickly check whether the payment flow is intact
```

사람은 여기서 `summarize`, `check`를 바로 본다. 그런데 규칙 기반 로직은 앞에 붙은 `briefly`, `quickly` 때문에 동작을 놓칠 수 있다. 그래서 작업 동사를 찾기 전에 이런 부사를 먼저 걷어내도록 했다.

```text
briefly summarize ...
  -> summarize ...

quickly check ...
  -> check ...
```

세 번째는 dependency였다. 기존 `FLOWS_TO`에는 일부 type 전환이 빠져 있었다. 그래서 `validate -> validate`처럼 검증이 연달아 나올 때 고립된 node가 생길 수 있었다.

사용자가 프롬프트에서 작업을 순서대로 말한 경우라면, type이 같거나 예상 밖 조합이어도 앞 작업 다음에 이어지는 것으로 보는 편이 자연스럽다. 그래서 모든 type이 다른 type으로 이어질 수 있게 `FLOWS_TO`를 채웠다.

그 뒤에는 dependency가 기대한 대로 이어졌다.

```text
t1 -> t2 -> t3 -> t4 -> t5 -> t6
```

## organize와 check는 단어만 보면 안 됐다

오늘 계속 걸렸던 단어가 `organize`와 `check`였다. 둘 다 단어 하나만 보고 type을 정하기 어렵다.

예를 들어 `organize`는 이렇게 다르게 쓰인다.

```text
organize the changes in the work note
organize the duplicated validation logic into a shared service
organize the flow of how error handling is done
```

셋 다 `organize`로 시작하지만 실제로 시키는 일은 다르다.

```text
organize + changes/results/work note
  -> document

organize + logic/code/service/validation
  -> modify

organize + flow/pipeline
  -> analyze
```

그래서 `TaskGraphProcessor`에 IDIOM 규칙을 추가했다. 단어 하나만 보지 말고, 뒤에 붙은 명사와 목적어까지 같이 보게 한 것이다.

`check`도 마찬가지였다.

```text
check whether the payment flow is intact
  -> validate

check if the same error handling logic is duplicated
  -> analyze
```

첫 번째는 이미 만든 기능이 정상인지 확인하는 말이다. 두 번째는 중복이나 품질 문제를 조사하라는 말이다. 둘 다 `check`지만 하나는 `validate`, 하나는 `analyze`가 되어야 한다.

그래서 `check if/whether/that`이 나오더라도 `duplicated`, `redundant`, `scattered`, `overlap`, `similar` 같은 말이 붙어 있으면 `analyze`로 먼저 보내게 했다.

## API 에러 처리 프롬프트도 다시 걸렸다

결제 도메인을 고친 뒤에는 API 에러 처리 도메인 프롬프트를 다시 돌렸다. 여기서도 분류기가 놓치는 표현이 있었다.

이번에 보고 싶었던 순서는 이랬다.

```text
explore -> analyze -> analyze -> modify -> validate -> validate -> document
```

최종 작업 후보는 이런 식으로 나와야 했다.

```text
1. 프로젝트 전체에서 API 관련 코드가 흩어진 위치를 찾는다.
2. 에러 처리나 응답 파싱 흐름이 어떻게 되어 있는지 정리한다.
3. 같은 유형의 에러 처리나 응답 변환 로직이 중복되어 있는지 확인한다.
4. 공통 utility나 middleware로 정리되도록 구조를 개선한다.
5. 주요 시나리오 기준으로 기존 기능이 정상 동작하는지 확인한다.
6. 가능한 범위에서 단위 테스트나 간단한 통합 테스트를 실행한다.
7. 핵심 내용만 README나 별도 문서에 정리한다.
```

그런데 기존 분류기는 아래 표현들을 제대로 처리하지 못했다.

```text
audit the error handling code
identify issues in the module
catalog all API endpoints
extract the common logic into a utility
```

사람이 보기에는 자연스러운 요청이다. 하지만 `TaskGraphProcessor`의 type pattern에 없으면 기본값으로 밀리거나 엉뚱한 type이 붙는다.

그래서 동사 범위를 조금 더 넓혔다.

```text
explore:
  catalog, enumerate

analyze:
  audit
  identify/detect/spot + 품질 이슈 명사

modify:
  extract, consolidate, centralize, decouple,
  restructure, reorganize, streamline
```

특히 `identify`, `detect`, `spot`은 조심해서 넣었다. 이 단어들이 항상 `analyze`를 뜻하지는 않기 때문이다.

```text
identify where the files are
  -> explore

identify issues in the module
  -> analyze
```

그래서 단독 키워드로 등록하지는 않았다. `bugs`, `issues`, `problems`, `duplicat`, `redundan`, `errors`, `vulnerabilit`처럼 품질 문제를 가리키는 말이 같이 나올 때만 `analyze`로 처리했다.

## 테스트와 실제 실행 결과

오늘은 단위 테스트만 돌리고 끝내지 않았다. 실제 llama + Kompress 경로도 같이 봤다.

#180에서는 결제 도메인 프롬프트로 아래 값을 확인했다.

```text
language: mixed
candidateSentenceCountIsSix: true
typeFlowMatchesExpected: true
dependencyFlowMatchesExpected: true
```

task graph 테스트는 다음 상태였다.

```text
Test Files  7 passed (7)
Tests       880 passed (880)
```

#184에서는 API 에러 처리 도메인 프롬프트로 다시 확인했다.

```text
language: mixed
candidateSentenceCountIsExpected: true (7개)
typeFlowMatchesExpected: true
dependencyFlowMatchesExpected: true
noIsolatedNodes: true
```

task graph 테스트도 더 늘었다.

```text
Test Files  7 passed (7)
Tests       895 passed (895)
```

여기서 보고 싶었던 건 테스트 숫자 자체가 아니었다. 한국어 프롬프트가 llama 번역을 거치고, normalized input으로 Role 2.1에 들어간 뒤, 후보 추출기와 graph processor를 지나 최종 DAG까지 이어지는지 보고 싶었다.

## 오늘 한 일을 다시 놓고 보면

오늘 한 일을 한 줄씩 놓으면 이렇다.

```text
trace 파일명을 읽을 수 있게 정리한다
  ↓
live result를 보고 문제 프롬프트를 고른다
  ↓
TaskCandidateExtractor가 작업이 아닌 조각을 버리게 한다
  ↓
선행 부사와 담화 표현 때문에 작업 동사를 놓치지 않게 한다
  ↓
TaskGraphProcessor가 organize/check 같은 애매한 표현을 문맥으로 분류하게 한다
  ↓
FLOWS_TO를 완성해 고립된 node를 막는다
  ↓
llama 경로와 단위 테스트로 다시 확인한다
```

어제는 "Role 2.1이 무엇을 봐야 하는가"를 고쳤고, 오늘은 "그 안에서 무엇을 작업으로 봐야 하는가"를 고쳤다.

이 차이는 꽤 크다. DeToks는 자연어를 실행 가능한 작업 단위로 바꾸는 도구다. 그런데 사람이 쓰는 자연어는 늘 깔끔하지 않다.

```text
가능하면
간단히
먼저
혹시 괜찮다면
중요한 것만
테스트 결과도 같이
```

이런 표현은 사용자에게는 자연스럽지만 graph를 만들 때는 혼동을 만든다. 오늘은 이런 말을 무조건 지우기보다, 작업 판단에 필요한 말과 아닌 말을 나눠 보려고 했다.

## 발표 준비도 시작했다

코드 작업과 별개로 발표 준비 자료도 만들기 시작했다.

아직 완성본은 아니고, 지금까지 만든 것을 발표에서 어떤 순서로 보여줄지 잡는 단계다. DeToks를 그냥 "LLM으로 작업을 자동화하는 도구"라고 말하면 너무 뭉뚱그려진다. 그래서 발표에서는 아래처럼 문제와 구조가 바로 보이게 가져가려고 했다.

```text
문제:
  사용자의 긴 자연어 요청은 바로 실행하기 어렵다.

구조:
  번역/정규화 -> task 후보 추출 -> task graph 생성 -> adapter 실행

보여줄 변화:
  압축 전 normalized input을 Role 2.1 입력으로 사용
  작업이 아닌 표현을 제거
  type 분류와 dependency를 안정화
  실제 trace로 검증

시연 포인트:
  한국어 프롬프트가 task graph로 바뀌는 과정
  explore -> analyze -> modify -> validate -> document 순서
```

오늘 trace 파일명을 정리한 것도 발표 준비와 이어진다. 발표에서는 "이렇게 동작합니다"라고 말로만 설명하는 것보다, 실제 입력과 출력이 어떻게 바뀌었는지를 보여주는 편이 낫다. trace 이름이 읽히면 그 자료를 슬라이드나 데모로 옮기기도 훨씬 쉽다.

## 마무리

오늘은 새로운 기능을 크게 붙인 날은 아니었다. 어제 만든 Role 2.1 구조를 실제 프롬프트와 trace 위에서 더 촘촘하게 다듬은 날에 가까웠다.

처음에는 trace 파일명 정리처럼 단순한 검증 자료 정리에서 시작했다. 그런데 실제 llama 결과를 다시 보니 `briefly summarize`, `organize the flow`, `check if duplicated`, `extract common logic` 같은 표현들이 계속 분류기의 빈틈을 드러냈다.

그래서 오늘은 규칙을 많이 추가했다기보다, 어떤 규칙이 어디에 있어야 하는지를 다시 나눈 날이었다.

```text
TaskSentenceSplitter:
  문장 경계를 나눈다.

TaskCandidateExtractor:
  실행 가능한 작업 후보만 남긴다.

TaskGraphProcessor:
  type과 dependency를 만든다.
```

이 세 책임이 섞이면 작은 표현 하나를 고치려고 전체 구조를 건드리게 된다. 오늘은 그 선을 유지하면서도 실제 표현을 더 많이 견디게 만들었다.

이제 DeToks는 결제 도메인과 API 에러 처리 도메인처럼 서로 다른 프롬프트에서도 비슷한 방식으로 task graph를 만들 수 있다. 아직 더 많은 도메인과 표현을 봐야 하지만, 적어도 오늘은 trace를 보면서 "왜 이 작업이 나오고, 왜 이 dependency가 생겼는지"를 조금 더 설명할 수 있게 됐다.
