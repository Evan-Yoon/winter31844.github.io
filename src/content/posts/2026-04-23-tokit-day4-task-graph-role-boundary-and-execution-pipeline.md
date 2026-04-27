---
title: "[DeToks] Day 4 - Task Graph 책임과 실행 흐름 정리"
slug: tokit-day4-task-graph-role-boundary-and-execution-pipeline
date: 2026-04-23
author: Evan Yoon
category: project
subcategory: team-project
description: "Tokit 프로젝트 4일차에는 Role 1과 Role 2.1의 책임을 다시 나누고, 문장 기반 입력과 Task Graph 실행 흐름을 정리했다."
thumbnail: /images/posts/tokit/mermaid4.png
tags:
  - tokit
  - detoks
  - llm
  - task-graph
  - workflow
  - team-project
readTime: 7
series: "Tokit"
seriesOrder: 8
featured: false
draft: false
toc: true
---

4월 23일에는 기능을 하나씩 추가하기보다, **Task Graph를 누가 어디까지 만들어야 하는지**를 다시 정리하는 데 시간을 썼다. 이전 구조에서는 Role 1이 `tasks[]`, `id`, `depends_on`까지 만들어 주는 쪽으로 잡혀 있었다. 하지만 실제 구현을 시작하려고 보니 이 방식은 Role 2.1과 책임이 겹쳤다.

그래서 이날 정리한 핵심은 단순했다. Role 1은 전처리만 맡고, Role 2.1은 분석과 Task Graph 생성을 맡는다. 이 경계를 먼저 분명하게 잡고 나서, 그 아래에 붙는 검증과 실행 준비 흐름을 순서대로 정리했다.

## Role 1은 문장을 정리하고, Role 2.1은 그래프를 만든다

이번 변경에서 가장 중요한 부분은 입력을 Task 중심에서 Sentence 중심으로 바꾼 것이다. 즉, 처음부터 완성된 task를 만들기보다 먼저 문장 배열을 만들고, 그다음 단계에서 그래프를 만들도록 책임을 나눴다.

기존에는 Role 1이 아래 일까지 맡는 흐름이었다.

- 한국어를 영어로 변환
- task 분해
- id 생성
- depends_on 생성
- JSON Task Graph 출력

이렇게 두면 Role 1이 이미 그래프를 만든 뒤 넘기는 셈이 된다. 그러면 Role 2.1이 맡기로 했던 Task Graph 생성 책임이 애매해진다. 그래서 이번에는 역할을 아래처럼 다시 나눴다.

### Role 1

- 한국어를 영어로 변환
- 문장 단위로 분리
- 불필요한 정보 제거와 압축
- `{ sentences: string[] }` 형식으로 출력

```json
{
  "sentences": [
    "Read the auth module",
    "Find bugs in the code",
    "Fix the identified issues"
  ]
}
```

### Role 2.1

- `sentences[]` 입력 받기
- 문장별 type 분류
- task id 생성
- single / multi 구분
- `depends_on` 결정
- 최종 Task Graph 만들기

이렇게 나누고 나니 역할이 훨씬 명확해졌다. Role 1은 입력을 정리하고, Role 2.1은 그 문장들을 실제 실행 가능한 그래프로 바꾼다.

## TaskGraphProcessor도 이 경계에 맞춰 다시 정리했다

역할을 다시 나누면서 `TaskGraphProcessor`의 입력도 바뀌었다. 더 이상 `{ intent, tasks[] }`를 받지 않고, `{ sentences: string[] }`를 받도록 수정했다.

이 변경에 맞춰 `pipeline.ts`에서는 `RawTaskSchema`, `RawAnalyzedRequestSchema`를 제거하고 `CompiledSentencesSchema`를 추가했다. 문서에서도 `AnalyzedRequest` 대신 `CompiledSentences`를 기준으로 흐름을 다시 설명했다.

처리 방식도 단순해졌다.

- 문장이 1개면 single 요청으로 처리
- 문장이 2개 이상이면 multi 요청으로 처리
- 각 문장을 `explore`, `analyze`, `modify`, `document` 같은 type으로 분류
- type 흐름을 보고 `depends_on`을 자동으로 정함

예를 들어 `Read -> Analyze -> Fix`처럼 이어지는 문장은 순차 그래프로 묶고, 의미상 독립적인 문장들은 병렬 실행 후보로 남긴다. 이전보다 입력과 출력의 책임이 더 분리됐고, 나중에 분류 기준을 바꾸기도 쉬워졌다.

## 그래프를 만든 뒤에는 검증, 해석, 병렬 분류 순서로 이어진다

이번에 함께 정리한 나머지 모듈들은 Task Graph를 실제로 실행할 수 있게 준비하는 흐름에 가깝다.

먼저 `DAGValidator`는 그래프가 유효한지 검사한다. 여기서는 세 가지를 본다.

- 존재하지 않는 dependency를 참조하는지
- 순환 구조가 생겼는지
- 완전히 고립된 노드가 있는지

검증이 통과하면 `topologicalOrder`를 반환한다. 이 값은 다음 단계에서 바로 사용된다.

그다음 `DependencyResolver`는 검증된 그래프를 실제 실행 순서로 바꾼다. 단순히 ID 목록만 넘기는 것이 아니라, 각 task가 어떤 task에 의존하는지 실제 객체 기준으로 풀어낸 `ResolvedTask`를 만든다. 여기까지 오면 "무엇이 먼저 실행돼야 하는가"가 코드에서도 명확해진다.

이후 `ParallelClassifier`는 정리된 task들을 stage 단위로 묶는다. 규칙은 비교적 단순하다.

- dependency가 없으면 stage 0
- dependency가 있으면 `max(deps stage) + 1`

같은 stage 안에 있는 task들은 서로 의존하지 않기 때문에 함께 실행할 수 있다. 실제 병렬 실행은 별도 executor가 맡더라도, 적어도 여기서 어느 작업을 같이 돌릴 수 있는지는 분명해진다.

## 이번 정리는 기능 추가보다 구조 정리에 가까웠다

겉으로 보면 `DAGValidator`, `DependencyResolver`, `ParallelClassifier`처럼 새 파일이 여러 개 생겼다. 하지만 실제로 더 중요했던 건 이 모듈들이 모두 **다시 나눈 역할 위에 올라갔다는 점**이었다.

정리하면 지금 구조는 아래처럼 읽힌다.

1. Role 1이 한국어 입력을 영어 문장 배열로 정리한다.
2. Role 2.1이 문장 배열을 Task Graph로 바꾼다.
3. `DAGValidator`가 그래프의 유효성을 검사한다.
4. `DependencyResolver`가 실행 가능한 순서와 dependency 객체를 정리한다.
5. `ParallelClassifier`가 병렬 실행 가능한 stage로 분류한다.

이 흐름으로 바뀌면서 역할 경계도 선명해졌고, 각 모듈이 무엇을 맡는지도 더 설명하기 쉬워졌다. 특히 Role 1이 너무 많은 판단을 하던 구조를 줄인 건 꽤 의미가 있었다. 전처리와 그래프 생성을 분리해두면, 이후 성능이나 정확도를 손볼 때도 어느 쪽을 고쳐야 하는지가 분명해지기 때문이다.

## 이날 느낀 점

이번 작업은 눈에 띄는 기능을 보여 주는 날이라기보다, 구현하면서 생길 수 있는 책임 충돌을 미리 정리한 날에 가까웠다. 처음에는 "문장을 잘 정리해서 task까지 뽑아주면 더 편하지 않을까" 싶었지만, 실제로는 그 편의가 역할 중복으로 이어졌다.

오히려 Role 1을 가볍게 두고, Role 2.1이 Task Graph를 맡는 쪽이 더 자연스러웠다. 문장 입력, 그래프 생성, DAG 검증, dependency 해석, 병렬 분류까지 한 줄로 이어 놓고 보니, 이제 Tokit의 실행 흐름이 이전보다 훨씬 덜 애매해졌다는 느낌이 들었다.
