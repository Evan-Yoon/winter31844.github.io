---
title: "[DeToks] Day 5 - 통합 회의와 Task Graph 안정화"
slug: tokit-day5-integration-meeting-and-task-graph-stabilization
date: 2026-04-24
author: Evan Yoon
category: project
subcategory: team-project
description: "4월 24일에는 DeToks 팀 회의에서 모듈 간 책임과 통합 방식을 다시 맞췄고, Task Graph 쪽에서는 검증, 병렬 분류, 오케스트레이션, 문장 분해 로직을 이어서 정리했다."
thumbnail: ""
tags:
  - tokit
  - detoks
  - llm
  - task-graph
  - orchestration
  - team-project
readTime: 7
series: "Tokit"
seriesOrder: 9
featured: false
draft: false
toc: true
---

4월 24일에는 오전에 팀 회의를 하고, 오후에는 회의에서 나온 내용을 기준으로 Task Graph 쪽 구현을 계속 이어갔다. 전날까지는 Role 1과 Role 2.1의 경계를 정리하고 Task Graph를 만드는 흐름을 잡는 데 집중했다면, 이날은 그 구조가 다른 모듈과 실제로 어떻게 붙어야 하는지를 더 구체적으로 맞춘 날이었다.

회의에서 가장 많이 나온 이야기는 단순했다. 각자 맡은 모듈이 어느 정도 만들어지고 있었기 때문에, 이제는 "내 코드가 돌아간다"보다 "다른 모듈과 같은 스키마로 이어진다"가 더 중요해졌다. 그래서 공용 스키마, 세션 상태, 실행 모드, 실패 처리 방식 같은 부분을 하나씩 확인했다.

## 오전 회의에서 맞춘 것들

회의는 오전 10시부터 11시까지 진행했다. 참석자는 규철님, 나, 지호님, 시호님이었고, 각자 맡은 파트의 진행 상황을 공유한 뒤 통합 전에 맞춰야 할 부분을 정리했다.

시호님은 번역 파이프라인을 Python에서 TypeScript로 옮긴 상태였고, 다음 단계인 요약 명세를 작성 중이었다. 지호님은 State & Context 쪽 Unit Test를 통과시켰고, PR 제출을 준비하고 있었다. 규철님은 전체 파이프라인 구조와 공용 스키마를 잡아 둔 상태였고, CLI 실행 환경과 통합 테스트 쪽을 보고 있었다.

내가 맡은 Task Graph 쪽은 전체 흐름 중 6단계 정도까지 구현된 상태로 공유했다. `depends_on` 파악, DAG 기반 병렬/직렬 분기, `DAGValidator`, Topological Sort 검증까지는 어느 정도 형태가 잡혔다. 남은 일은 테스트를 더 촘촘히 붙이고, State & Context 쪽과 실제로 오가는 구조를 맞추는 것이었다.

이날 결정한 내용 중 중요한 것은 아래 정도였다.

- 모듈 연결은 API가 아니라 TypeScript 내부 메소드 호출로 간다.
- 공용 데이터 계약은 `src/schemas/pipeline.ts`를 기준으로 맞춘다.
- Task Graph 형식은 DAG와 Topological Sort 방식으로 확정한다.
- 순환 참조 검증은 Task Graph에서 1차로 하고, StateValidator에서 fail-safe로 한 번 더 본다.
- 의존 Task가 실패하면 후속 Task는 실행하지 않는 Strict 모드로 간다.

회의 자체는 거창한 방향 전환이라기보다, 이제 통합을 시작해도 되는지 확인하는 시간에 가까웠다. 다만 이 회의 덕분에 이후 코드를 작성할 때 기준이 조금 더 분명해졌다. 특히 Task Graph와 State가 서로 영향을 주고받는 구조는 피하기 어렵다는 점을 인정하고, 대신 각 단계에서 무엇을 책임질지 정리한 게 컸다.

## Task Graph 쪽은 검증과 테스트를 먼저 보강했다

이날 첫 커밋 흐름은 `ParallelClassifier`와 테스트 보강이었다. `ParallelClassifier`는 `DependencyResolver`가 정리한 task 순서를 받아서, 동시에 실행 가능한 task들을 stage 단위로 묶는 역할이다.

규칙은 복잡하게 만들지 않았다.

- 선행 task가 없으면 stage 0
- 선행 task가 있으면 의존 task 중 가장 늦은 stage + 1
- 같은 stage에 있는 task는 서로 의존하지 않으므로 병렬 실행 가능

이 구조를 만들고 나서 `DAGValidator`, `DependencyResolver`, `ParallelClassifier`, `TaskGraphProcessor` 테스트를 추가했다. 테스트 케이스는 단일 task, 선형 의존, fan-out, fan-in, diamond 구조처럼 그래프에서 자주 나오는 모양 위주로 잡았다. 사이클이나 없는 dependency를 참조하는 경우도 같이 확인했다.

이 작업은 눈에 띄는 기능 추가라기보다, Task Graph가 기본적인 그래프 구조를 틀리지 않게 다루는지 확인하는 쪽이었다. 이후 통합 테스트에서 에러가 나더라도 그래프 자체의 문제인지, 다른 모듈 연결 문제인지 구분할 수 있어야 했기 때문이다.

## 오케스트레이터에 실제 흐름을 붙이기 시작했다

그다음에는 `orchestrator` 쪽을 만졌다. 이전에는 파이프라인 경계만 있고 내부 단계는 대부분 stub에 가까웠는데, 이날은 Task Graph, Context, Executor, State Manager를 하나의 흐름 안에서 이어 보기 시작했다.

대략적인 실행 순서는 이렇게 잡았다.

1. 입력을 받아 Task Graph를 만든다.
2. DAG 검증을 통과시키고 dependency를 해석한다.
3. 병렬 실행 가능한 stage로 분류한다.
4. 세션 상태를 만들거나 불러온다.
5. 각 task에 대해 ExecutionContext를 만든다.
6. executor를 호출하고 결과를 세션 상태에 저장한다.

회의에서 정한 Strict 모드도 이 흐름에 넣었다. 어떤 task가 실패하면 그 task에 의존하는 후속 task는 실행하지 않고 skipped 상태로 남긴다. 지금 단계에서는 실패 복구까지 다루기보다, 실패했을 때 다음 task를 애매하게 실행하지 않는 쪽이 더 맞다고 봤다.

이 과정에서 State & Context 쪽과의 연결도 조금 더 현실적으로 보이기 시작했다. Task Graph는 "무엇을 어떤 순서로 실행할지"를 만들고, State & Context는 "그 task를 실행할 때 어떤 이전 결과를 넘길지"를 고르는 식이다. 회의에서 이야기했던 상호작용 구조가 코드에서도 그대로 드러났다.

## type 정의를 문서와 코드 기준으로 맞췄다

오후에는 task type 정의도 다시 정리했다. DeToks에서는 `explore`, `analyze`, `create`, `modify`, `validate`, `execute`, `document`, `plan` 같은 상위 분류를 쓰는데, 이 값들이 단순한 enum으로만 있으면 기준이 흔들리기 쉽다.

그래서 `docs/TYPE_DEFINITION.md`를 추가해서 각 type의 의미를 문서로 정리하고, `TaskGraphProcessor`의 분류 로직도 그 기준에 맞춰 보강했다. 예를 들어 `Run the tests`는 단순 실행인 `execute`가 아니라 검증 목적이 강하므로 `validate`로 보는 식이다. 반대로 `Run the build`나 `Restart the server`는 실제 명령 실행에 가까워서 `execute`로 둔다.

이 부분은 생각보다 중요했다. type이 흔들리면 dependency도 같이 흔들린다. `explore -> analyze -> modify -> validate` 같은 흐름을 기대하고 있는데, 중간 type이 엉뚱하게 잡히면 Task Graph 전체 순서가 달라질 수 있다. 그래서 문서와 코드가 같은 기준을 보도록 맞춰 두는 게 필요했다.

## 자연어 입력을 문장 단위 task로 나누기 시작했다

마지막으로 `TaskSentenceSplitter`를 추가했다. 전날에는 Role 1이 `{ sentences: string[] }`를 넘겨주고, Role 2.1이 그걸 Task Graph로 바꾸는 구조를 정리했다. 그런데 실제 통합 흐름에서는 아직 Role 1이 완전히 붙지 않은 상태라, raw input을 어느 정도 문장 단위로 나눠줄 장치가 필요했다.

`TaskSentenceSplitter`는 복잡한 NLP 모델을 붙인 것이 아니라, 정규식 기반으로 명령형 문장을 보수적으로 나누는 방식이다. 예를 들면 아래 같은 입력을 나눈다.

- `Create a new endpoint and test it`
- `Find the module, inspect the flow, and patch the bug`
- 번호 목록이나 bullet 목록으로 들어온 요청
- `Run tests after deploy`처럼 순서를 뒤집어 읽어야 하는 표현

반대로 무조건 많이 쪼개지는 것은 피하려고 했다. 따옴표 안의 명령어는 보호하고, 조건문이나 부정문은 한 문장으로 유지했다. `find and replace`처럼 하나의 동작으로 보는 표현도 분리하지 않도록 테스트를 추가했다.

이 splitter는 완성된 자연어 파서라기보다, 지금 단계에서 Task Graph가 여러 문장을 받아볼 수 있게 해주는 연결부에 가깝다. 그래도 `TaskGraphProcessor`와 함께 돌렸을 때 `create -> validate`, `explore -> analyze -> modify` 같은 흐름이 만들어지는지 확인할 수 있게 된 점은 의미가 있었다.

## 이날 정리된 흐름

4월 24일 작업을 한 줄로 정리하면, Task Graph를 단독 모듈에서 통합 가능한 모듈로 조금 옮긴 날이었다.

코드 기준으로는 아래 작업이 이어졌다.

- `ParallelClassifier` 구현
- DAG, dependency, parallel, task graph 테스트 추가
- 오케스트레이터에 Task Graph, Context, Executor, State 흐름 연결
- task type 정의 문서화와 분류 키워드 보강
- `TaskSentenceSplitter` 구현과 테스트 추가
- TypeScript 중심 CI 흐름 정리

회의 기준으로는 각자 만든 모듈을 하나의 흐름으로 묶기 위한 합의가 생겼다. API 없이 내부 메소드 호출로 간다는 점, 공용 스키마를 기준으로 맞춘다는 점, DAG 검증과 StateValidator를 이중 안전장치로 둔다는 점, 실패 처리는 Strict 모드로 간다는 점이 이후 구현의 기준이 됐다.

## 이날 느낀 점

이날은 구현량이 많긴 했지만, 막 새로운 기능을 크게 만든 날이라기보다는 통합을 앞두고 빈틈을 메우는 날에 가까웠다. 전날까지는 Task Graph 내부 구조를 정리하는 데 집중했다면, 이제는 이 그래프가 세션 상태와 실행기 사이에서 실제로 어떤 역할을 해야 하는지가 보이기 시작했다.

회의를 하고 나니 혼자 정리하던 구조가 팀 전체 흐름 안에서 다시 보였다. Task Graph는 혼자 완성되는 모듈이 아니라, Prompt Compiler가 넘긴 문장을 받고, State & Context에 다음 실행 정보를 넘기고, CLI 실행 결과를 다시 상태로 돌려받는 중간 지점이었다. 그래서 이날 만든 테스트와 오케스트레이터 연결은 완성이라기보다, 통합 테스트를 시작하기 위한 기반을 마련한 작업에 더 가까웠다.

아직 남은 부분도 분명했다. 세션 아이디를 언제 만들고 어떻게 이어갈지, 체크포인트를 어느 시점에 남길지, context 압축 기준을 어떻게 잡을지는 더 맞춰야 한다. 그래도 이날을 지나면서 적어도 Task Graph 쪽은 "그래프를 만든다"에서 "실행 흐름에 들어간다"로 한 단계 넘어간 느낌이었다.
