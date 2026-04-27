---
title: "[DeToks] Day 6 - Task Graph 로직을 왜 이렇게 짰는지"
slug: tokit-day6-task-graph-logic-rationale
date: 2026-04-26
author: Evan Yoon
category: project
subcategory: team-project
description: "4월 26일에는 DeToks의 Task Graph 생성, DAG 위상 정렬, 병렬 stage 분류, Strict Cascade 실패 처리 로직을 왜 선택했는지 조사하고 정리했다."
thumbnail: ""
tags:
  - tokit
  - detoks
  - llm
  - task-graph
  - dag
  - orchestration
  - team-project
readTime: 13
series: "Tokit"
seriesOrder: 10
featured: false
draft: false
toc: true
---

4월 26일에는 코드를 새로 많이 짠 날이라기보다, 지금까지 만든 Task Graph 로직을 다시 붙잡고 "왜 이 방식이어야 하는지"를 정리한 날이었다. 구현 자체는 며칠 전부터 이어서 하고 있었지만, 막상 발표나 문서로 설명하려고 하니 단순히 `DAG를 썼다`, `Topological Sort를 했다` 정도로는 부족했다.

처음 보는 사람이 들었을 때는 이런 질문이 자연스럽게 나올 수밖에 없다.

- 왜 그냥 순서대로 실행하지 않았는가?
- 왜 LLM에게 의존성을 판단하게 하지 않았는가?
- 왜 Priority Queue나 Event-driven 방식이 아니라 DAG인가?
- 어떤 task가 실패하면 나머지는 계속 실행해야 하는가, 멈춰야 하는가?

이 질문들에 답하려고 보니, 결국 DeToks의 Task Graph는 단순한 자료구조가 아니라 **LLM 작업을 실행 가능한 흐름으로 바꾸기 위한 작은 오케스트레이터**에 가깝다는 생각이 들었다. 사용자는 자연어로 "찾고, 분석하고, 고치고, 테스트해줘"라고 말하지만, 시스템은 그 말을 그대로 실행할 수 없다. 먼저 작업을 나누고, 각 작업의 성격을 정하고, 순서를 만들고, 실행 전에 말이 되는 그래프인지 확인해야 한다.

오늘 정리한 핵심은 아래 한 문장으로 줄일 수 있다.

> DeToks는 자연어 요청을 바로 실행하지 않고, 먼저 결정 가능한 Task Graph로 바꾼 다음, DAG 검증과 위상 정렬을 거쳐 실행한다.

이 글은 그 결론까지 가는 과정이다.

## 출발점은 "문장을 어떻게 task로 바꿀 것인가"였다

Day 4에서 Role 1과 Role 2.1의 경계를 나눴다. Role 1은 한국어 입력을 정리하고 문장 단위로 나눠서 넘긴다. Role 2.1은 그 문장들을 받아서 실제 Task Graph로 바꾼다.

처음에는 이 구분이 단순한 역할 분리처럼 보였다. 그런데 구현을 해보니 여기서 중요한 결정이 하나 생겼다. Role 2.1이 해야 하는 일은 생각보다 많았다.

```text
사용자 입력
  ↓
Role 1
  - 한국어 입력 정리
  - 영어 명령문으로 압축
  - sentences[] 생성
  ↓
Role 2.1
  - 각 문장의 task type 분류
  - task id 생성
  - depends_on 결정
  - Task Graph 생성
  - DAG 검증
  - 실행 stage 분류
```

즉 Role 2.1은 단순히 배열을 JSON으로 포장하는 역할이 아니었다. 자연어 문장을 실행 가능한 그래프로 바꾸는 단계였다. 그래서 제일 먼저 고민한 것은 **task type을 어떻게 정할 것인가**였다.

DeToks에서는 task type을 크게 8개로 잡았다.

```text
explore   : 파일이나 코드 위치를 찾는 작업
analyze   : 원인, 구조, 흐름을 분석하는 작업
create    : 새 기능이나 파일을 만드는 작업
modify    : 기존 코드를 수정하는 작업
validate  : 테스트, 검증, 확인 작업
execute   : 명령 실행, 설치, 배포 같은 실행 작업
plan      : 작업 계획을 세우는 작업
document  : 문서화하는 작업
```

예를 들어 `Find all references to UserService`는 `explore`에 가깝고, `Explain why login fails`는 `analyze`에 가깝다. `Run the tests`는 겉으로 보면 `run`이 들어가서 `execute`처럼 보이지만, 목적은 테스트 결과 확인이므로 `validate`로 보는 편이 맞다.

이런 기준을 정해두지 않으면 뒤쪽 의존성도 같이 흔들린다. `explore -> analyze -> modify -> validate`는 자연스럽지만, 같은 문장이 어떤 날은 `execute`, 어떤 날은 `validate`로 분류되면 그래프 구조도 달라진다.

## LLM에게 맡기면 편하지만, 이번에는 맞지 않았다

가장 쉬운 방법은 LLM에게 물어보는 것이다.

```text
입력: "Run the tests and check the result"
질문: 이 문장은 explore, analyze, create, modify, validate, execute, plan, document 중 무엇인가?
응답: validate
```

처음 생각만 하면 이 방식이 제일 자연스럽다. 의미를 잘 이해할 수 있고, 애매한 문장도 사람이 보듯이 판단할 수 있다. 그런데 DeToks의 위치를 생각하면 이 방식은 별로였다.

DeToks는 LLM CLI 앞단에서 토큰과 실행 흐름을 줄이려는 도구다. 그런데 task type 하나 정하려고 LLM을 다시 호출하면, 실행 전에 이미 비용과 지연이 생긴다. 문장이 5개면 type 분류만으로 5번 호출해야 한다. 여기에 문장 간 의존성까지 LLM에게 묻기 시작하면 호출 횟수는 더 늘어난다.

더 큰 문제는 테스트였다. 같은 문장이 항상 같은 type으로 나와야 `TaskGraphProcessor`를 단위 테스트로 고정할 수 있다. 그런데 LLM 기반 분류는 프롬프트나 모델 상태에 따라 미묘하게 달라질 수 있다.

```text
"Find all usages"

1회차: explore
2회차: analyze
```

사람이 보기에는 둘 다 말이 된다. 하지만 시스템 입장에서는 곤란하다. 어제는 `explore -> analyze`라서 순차 실행됐는데, 오늘은 첫 문장이 `analyze`로 잡혀서 다른 그래프가 나오면 디버깅하기 어렵다. DeToks의 Task Graph는 "그럴듯한 의미 해석"보다 "반복 가능한 실행 계획"이 더 중요했다.

통계적 ML 분류기도 검토했다. BERT나 TF-IDF + SVM 같은 방식으로 문장을 분류할 수는 있다. 하지만 그것도 초기 프로젝트에는 무거웠다. 모델 파일이 필요하고, 8개 type에 맞는 학습 데이터도 필요하고, 분류 경계를 설명하기도 어렵다. 지금 필요한 건 대규모 자연어 이해 모델이 아니라, 개발자 명령문이라는 좁은 도메인에서 빠르게 고정되는 분류 규칙이었다.

그래서 지금 단계에서는 정규식 기반 First-match 방식을 선택했다.

## First-match 분류를 선택한 이유

First-match는 말 그대로 위에서부터 패턴을 검사하다가 처음 맞는 type을 반환하는 방식이다.

```text
입력: "Run the tests and check the results"
  ↓ 소문자화
"run the tests and check the results"
  ↓
explore   패턴 검사: 매칭 없음
document  패턴 검사: 매칭 없음
create    패턴 검사: 매칭 없음
modify    패턴 검사: 매칭 없음
analyze   패턴 검사: 매칭 없음
validate  패턴 검사: "test" 매칭
  ↓
결과: validate
```

그림으로 보면 흐름은 아주 단순하다.

```text
┌──────────────────────────────┐
│ sentence                     │
│ "Run the tests"              │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│ normalize                    │
│ "run the tests"              │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│ TYPE_PATTERNS 순회           │
│ explore -> document -> ...   │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│ 첫 매칭 반환                 │
│ validate                     │
└──────────────────────────────┘
```

이 방식의 장점은 분명하다.

첫째, 결정론적이다. 같은 입력이면 항상 같은 type이 나온다. 단위 테스트를 쓰기 쉽고, 결과가 바뀌면 패턴 순서나 내용이 바뀌었기 때문이라는 것도 바로 추적할 수 있다.

둘째, 비용이 없다. LLM 호출도 없고 모델 로딩도 없다. 패턴 몇십 개를 순회하는 정도라서 CLI 앞단에서 돌리기에 부담이 작다.

셋째, 수정이 쉽다. `Run the tests`를 `execute`가 아니라 `validate`로 보고 싶으면 `validate` 패턴을 보강하면 된다. 모델을 다시 학습시킬 필요가 없다.

물론 단점도 있다. First-match는 의미 파서가 아니다. 문장 전체의 깊은 의미를 이해하지 못하고, 우리가 작성한 패턴과 순서에 의존한다. 그래서 `run`이라는 동사보다 `tests`라는 목적어를 더 중요하게 보려면 패턴 순서를 의도적으로 설계해야 한다.

그래도 지금 단계에서는 이 단점이 오히려 받아들일 만했다. DeToks가 다루는 입력은 모든 자연어가 아니라 개발자가 CLI에 던지는 명령문이다. 도메인이 좁고, type도 8개로 제한되어 있다. 이런 상황에서는 "완벽한 의미 이해"보다 "예측 가능한 규칙"이 더 중요했다.

## 그다음 문제는 "의존성을 어떻게 만들 것인가"였다

type이 정해졌다고 바로 그래프가 완성되는 것은 아니다. 이제 문장들 사이에 순서가 있는지 판단해야 한다.

예를 들어 아래 요청은 순차 실행이 자연스럽다.

```text
1. Explore the auth module
2. Analyze the login failure
3. Fix the bug
4. Run tests
```

이 경우 그래프는 이렇게 된다.

```text
t1(explore) ──► t2(analyze) ──► t3(modify) ──► t4(validate)
```

반대로 아래처럼 앞뒤가 꼭 이어지지 않는 요청도 있다.

```text
1. Create a README template
2. Explore the auth module
```

이건 `create`가 끝나야 `explore`를 할 수 있는 구조가 아니다. 서로 독립적으로 볼 수 있다.

```text
t1(create)        t2(explore)
    │                 │
    └── 병렬 가능 ────┘
```

여기서도 LLM에게 문장 쌍을 보내서 "이 둘은 순서가 있나요?"라고 물어볼 수 있다. 하지만 type 분류 때와 같은 문제가 생긴다. 문장 5개면 인접한 관계만 봐도 4번의 추가 호출이 필요하다. 게다가 LLM이 실수로 `t1 -> t2`, `t2 -> t3`, `t3 -> t1` 같은 순환 구조를 만들 수도 있다.

그래서 의존성도 정적 규칙으로 만들었다. 이때 사용한 것이 `FLOWS_TO` 전이 테이블이다.

```text
explore  -> analyze, modify, create, validate, plan, document
analyze  -> modify, validate, document, create, plan
create   -> validate, modify, document, execute
modify   -> analyze, validate, document, execute
validate -> explore, analyze, document, execute, modify
plan     -> explore, create, execute, document
execute  -> explore, analyze, validate, document, plan, create
document -> 없음
```

핵심은 "이전 type 다음에 현재 type이 자연스럽게 이어지는가"를 보는 것이다.

```text
입력:
  t1: explore
  t2: analyze
  t3: create

판단:
  FLOWS_TO["explore"]에 "analyze"가 있는가?  yes -> t2 depends_on t1
  FLOWS_TO["analyze"]에 "create"가 있는가?   yes -> t3 depends_on t2

결과:
  t1 ──► t2 ──► t3
```

다른 예시는 이렇게 된다.

```text
입력:
  t1: create
  t2: explore

판단:
  FLOWS_TO["create"]에 "explore"가 있는가? no

결과:
  t1      t2
  서로 독립
```

이 방식이 완벽하다고 생각하지는 않는다. 사용자의 실제 의도는 더 복잡할 수 있다. 그래도 첫 버전의 목표는 "모든 문장을 완벽하게 이해하는 것"이 아니라, 흔한 개발 작업 흐름을 안정적으로 그래프로 만드는 것이다.

특히 아래 흐름은 개발 작업에서 자주 나온다.

```text
┌─────────┐    ┌─────────┐    ┌────────┐    ┌──────────┐
│ explore │ -> │ analyze │ -> │ modify │ -> │ validate │
└─────────┘    └─────────┘    └────────┘    └──────────┘

코드 찾기       원인 파악       수정          테스트
```

이 기본 흐름만 안정적으로 잡아도, DeToks가 "복합 요청을 한 덩어리 프롬프트로 던지는 방식"에서 벗어날 수 있다. 각 단계가 분리되면 뒤쪽에서 state와 context도 단계별로 줄여서 넘길 수 있다.

## 그래프를 만들었다면, 실행 전에 검증해야 한다

Task Graph가 만들어진 뒤에는 바로 실행하지 않는다. 여기서부터 DAG 검증이 들어간다.

DAG는 Directed Acyclic Graph, 즉 방향이 있고 순환이 없는 그래프다. Task Graph에서 방향은 의존성을 의미한다.

```text
t1 ──► t2

t2는 t1이 끝나야 실행 가능하다.
```

순환이 없다는 것은 이런 구조가 없어야 한다는 뜻이다.

```text
t1 ──► t2 ──► t3
▲                 │
└─────────────────┘

t1을 하려면 t3이 필요하고,
t3을 하려면 t2가 필요하고,
t2를 하려면 t1이 필요하다.
결국 아무것도 시작할 수 없다.
```

Task Graph에서 검증해야 하는 것은 크게 세 가지였다.

```text
1. UNKNOWN_DEPENDENCY
   depends_on에 존재하지 않는 task id가 들어갔는가?

2. CYCLE_DETECTED
   task 사이에 순환 의존성이 있는가?

3. DISCONNECTED_NODE
   전체 흐름에서 고립된 task가 있는가?
```

이 검증을 실행 전에 하는 것이 중요했다. Event-driven 방식처럼 앞 task가 끝날 때마다 다음 task를 trigger하면, 없는 task를 참조하거나 순환이 있는 문제를 실행 도중에야 발견할 수 있다. 이미 앞 단계가 반쯤 실행된 뒤에 실패하면 처리하기가 훨씬 애매해진다.

DeToks에서는 반대로 실행 전에 전체 그래프를 한 번 본다.

```text
┌────────────────────┐
│ Task Graph 생성     │
└─────────┬──────────┘
          │
          ▼
┌────────────────────┐
│ DAGValidator        │
│ - 없는 의존성 검사  │
│ - 순환 검사         │
│ - 고립 노드 검사    │
└─────────┬──────────┘
          │
          ▼
┌────────────────────┐
│ 통과한 그래프만 실행 │
└────────────────────┘
```

이 구조를 선택한 이유는 단순하다. LLM 작업은 한 번 실행되면 비용이 든다. 파일을 수정하는 task라면 부작용도 생길 수 있다. 그러니 "일단 실행해보고 중간에 안 되면 알자"보다 "실행 전에 구조적으로 말이 되는지 먼저 보자"가 맞았다.

## 왜 Priority Queue가 아니었나

처음 비교한 방법 중 하나는 Priority Queue였다. 각 task에 우선순위를 주고, 높은 것부터 실행하는 방식이다.

```text
t1(explore)  priority 3
t2(analyze)  priority 2
t3(modify)   priority 1
t4(validate) priority 0

실행 순서: t1 -> t2 -> t3 -> t4
```

단순한 예시에서는 잘 되는 것처럼 보인다. 하지만 이 방식은 의존성을 직접 표현하지 못한다. `t2 depends_on t1`이라는 관계를 숫자로 바꿔 넣어야 한다. task가 고정되어 있으면 사람이 계산할 수 있지만, DeToks는 사용자 요청마다 task가 동적으로 생긴다.

더 큰 문제는 병렬 실행이다.

```text
        t1
       /  \
      ▼    ▼
     t2    t3
       \  /
        ▼
        t4
```

이 그래프에서 `t2`와 `t3`는 둘 다 `t1`만 기다리면 된다. 서로 의존하지 않으므로 동시에 실행할 수 있다. 하지만 Priority Queue는 이 둘이 같은 stage에 놓일 수 있다는 사실을 자연스럽게 알려주지 않는다. 숫자 우선순위만으로는 "같이 실행해도 되는 그룹"을 계산하기 어렵다.

또 순환 의존성도 사전에 잡기 어렵다.

```text
t1 -> t2 -> t3 -> t1
```

우선순위 값은 있을 수 있지만, 이 구조가 실행 불가능하다는 사실은 별도의 그래프 검증 없이는 알기 어렵다. 결국 그래프 검증을 다시 붙여야 한다면, 처음부터 DAG로 보는 편이 더 낫다.

## 왜 Event-driven도 애매했나

Event-driven 방식은 task가 끝나면 다음 task를 시작하는 구조다.

```text
t1 완료 이벤트 -> t2 시작
t2 완료 이벤트 -> t3 시작
t3 완료 이벤트 -> t4 시작
```

이 방식은 실제 시스템에서 많이 쓰인다. Makefile이나 워크플로 엔진을 생각하면 익숙하다. 하지만 DeToks의 경우에는 실행 전 그래프 검증이 더 중요했다.

Event-driven 방식은 현재 완료된 task를 기준으로 다음 행동을 정한다. 그래서 전체 구조가 유효한지, 뒤쪽에 없는 task를 참조하는지, 순환이 숨어 있는지 미리 보기 어렵다.

```text
t1 완료
  ↓
t2 실행
  ↓
t3 실행
  ↓
t99 실행 시도
  ↓
"t99가 없음" 발견
```

이렇게 되면 실패를 너무 늦게 발견한다. DeToks가 다루는 작업은 단순 이벤트 처리보다 "실행 계획을 먼저 세우고 검증하는 문제"에 가까웠다. 그래서 Event-driven trigger보다는 DAG 검증 후 실행이 더 맞다고 판단했다.

## 그래서 DAG + Topological Sort로 갔다

DAG로 task를 표현하면, 의존성과 실행 순서를 같은 구조 안에서 다룰 수 있다. 그리고 DAG를 실행 가능한 순서로 펼치는 데 사용하는 대표적인 방법이 Topological Sort다.

위상 정렬은 말로 하면 어렵지만, 감각은 단순하다.

> 나를 기다리는 선행 task가 없는 것부터 꺼내고, 그것을 꺼냈을 때 새로 실행 가능해지는 task를 다시 큐에 넣는다.

예를 들어 아래 그래프가 있다.

```text
  t1 ──► t2 ──► t4
   │              ▲
   └────► t3 ─────┘
```

각 task로 들어오는 화살표 수를 `in-degree`라고 보면 이렇게 된다.

```text
t1: 0
t2: 1
t3: 1
t4: 2
```

처음에는 `in-degree`가 0인 `t1`만 실행할 수 있다. `t1`을 꺼내면 `t2`, `t3`의 의존성이 하나씩 줄어든다. 그러면 둘 다 `in-degree`가 0이 되고 실행 가능해진다. 그 다음 `t2`, `t3`을 처리하면 `t4`도 실행 가능해진다.

```text
Step 1
  queue: [t1]
  result: []

Step 2
  pop t1
  queue: [t2, t3]
  result: [t1]

Step 3
  pop t2
  queue: [t3]
  result: [t1, t2]

Step 4
  pop t3
  queue: [t4]
  result: [t1, t2, t3]

Step 5
  pop t4
  queue: []
  result: [t1, t2, t3, t4]
```

여기서 중요한 점은 순서 하나만 얻는 것이 아니라는 점이다. 이 과정에서 순환도 감지할 수 있다. 모든 노드를 꺼내지 못했는데 큐가 비어버리면, 어딘가에 순환이 있다는 뜻이다.

DeToks에서는 이 흐름을 세 단계로 나눴다.

```text
┌──────────────────────┐
│ 1. DAGValidator       │
│ 그래프 유효성 검사    │
│ topologicalOrder 계산 │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ 2. DependencyResolver │
│ id 배열을 Task 객체로 │
│ 변환하고 deps 연결    │
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ 3. ParallelClassifier │
│ 실행 가능한 stage로   │
│ task를 묶음           │
└──────────────────────┘
```

이 구조가 마음에 들었던 이유는 각 단계의 책임이 잘 나뉘기 때문이다. Validator는 그래프가 말이 되는지만 본다. Resolver는 id 중심의 그래프를 실제 task 객체와 연결한다. ParallelClassifier는 실행 최적화를 위해 stage를 만든다.

## 병렬 stage는 "가장 늦은 의존성 + 1"로 계산했다

위상 정렬만 하면 순서가 하나 나온다. 하지만 DeToks에서 필요한 것은 단순한 1열 실행만이 아니었다. 서로 독립적인 task는 같은 stage에 묶어서 병렬 실행할 수 있어야 했다.

예를 들어 아래 그래프를 다시 보면:

```text
  t1 ──► t2 ──► t4
   │              ▲
   └────► t3 ─────┘
```

실행 stage는 이렇게 잡을 수 있다.

```text
stage 0: [t1]
stage 1: [t2, t3]
stage 2: [t4]
```

규칙은 간단하다.

```text
의존성이 없으면 stage 0
의존성이 있으면 max(선행 task들의 stage) + 1
```

계산 과정을 풀면 아래처럼 된다.

```text
t1: depends_on 없음
    -> stage 0

t2: depends_on [t1]
    -> max(stage(t1)) + 1
    -> 0 + 1 = stage 1

t3: depends_on [t1]
    -> max(stage(t1)) + 1
    -> 0 + 1 = stage 1

t4: depends_on [t2, t3]
    -> max(stage(t2), stage(t3)) + 1
    -> max(1, 1) + 1 = stage 2
```

이 방식은 DeToks의 목표와도 맞았다. 모든 것을 무조건 순차 실행하면 안전하긴 하지만 느리다. 반대로 아무거나 병렬로 실행하면 의존성이 깨진다. stage 분류는 그 중간 지점이다.

```text
무조건 순차 실행:
  t1 -> t2 -> t3 -> t4

stage 기반 실행:
  stage 0: t1
  stage 1: t2 + t3
  stage 2: t4
```

LLM CLI 작업에서 실제 병렬 실행을 어디까지 할지는 나중에 더 조정해야 한다. 하지만 적어도 Task Graph 단계에서는 "병렬 가능성"을 구조적으로 표시해둘 필요가 있었다. 그래야 실행기 쪽에서 정책을 선택할 수 있다. 실제로 병렬로 돌릴지, 디버그 모드에서는 순차로 돌릴지, 실패 시 어떻게 끊을지 같은 선택이 가능해진다.

## 마지막 고민은 실패 처리였다

그래프를 만들고, 검증하고, 실행 순서까지 정했다면 다음 문제는 실패 처리다.

예를 들어 이런 흐름이 있다.

```text
t1(explore) ──► t2(analyze) ──► t3(modify) ──► t4(validate)
```

여기서 `t2`가 실패하면 어떻게 해야 할까?

처음에는 "그래도 뒤 task를 가능한 만큼 실행하면 되지 않을까?"라는 생각도 했다. 하지만 곧 문제가 보였다.

```text
t2 실패
  ↓
t3를 t2 결과 없이 실행
  ↓
t4도 실행
  ↓
겉으로는 completed가 많아 보임
```

이건 위험하다. `t3`는 분석 결과를 바탕으로 수정해야 하는 task인데, 분석이 실패한 상태에서 실행되면 수정 결과가 의미 없을 수 있다. 더 나쁘게는 잘못된 수정을 할 수도 있다. 그런데 상태 기록에는 `completed`로 남으면, 사용자는 이것을 성공으로 오해할 수 있다.

그래서 Graceful Degradation은 제외했다. 부분 성공을 많이 만들 수는 있지만, DeToks의 작업에서는 그 부분 성공이 가짜 성공이 될 가능성이 컸다.

Rollback도 검토했다. 실패하면 이미 실행된 작업을 되돌리는 방식이다. 분산 시스템에서는 saga 패턴처럼 각 단계마다 보상 트랜잭션을 정의할 수 있다. 하지만 LLM 기반 코드 작업에서는 되돌리기의 의미가 애매하다.

```text
파일 생성 -> 파일 삭제로 되돌릴 수 있음
코드 수정 -> diff가 있으면 되돌릴 수 있음
분석 결과 -> 되돌린다는 개념이 애매함
LLM 판단 -> 보상 로직을 정의하기 어려움
```

그래서 현재 단계에서는 Strict Cascade를 선택했다. 어떤 task가 실패하면, 그 task에 의존하는 후속 task는 실행하지 않고 `skipped`로 기록한다. 그리고 왜 건너뛰었는지 `blockedBy`를 남긴다.

```text
t1(explore) ──► t2(analyze) ──► t3(modify) ──► t4(validate)

t2 실패 시:

t1: completed
t2: failed
t3: skipped  (blockedBy: t2)
t4: skipped  (blockedBy: t3)
```

그림으로 보면 이렇게 된다.

```text
┌────────────┐    ┌────────────┐    ┌────────────┐    ┌────────────┐
│ t1 explore │ -> │ t2 analyze │ -> │ t3 modify  │ -> │ t4 validate│
│ completed  │    │ failed     │    │ skipped    │    │ skipped    │
└────────────┘    └─────┬──────┘    └─────┬──────┘    └────────────┘
                        │                 │
                        ▼                 ▼
                  blocked source     blockedBy: t3
                  for t3
```

이 방식은 결과가 덜 화려하다. 성공한 척하지 않기 때문이다. 대신 실패 원인이 명확하다.

```json
{
  "taskId": "t3",
  "status": "skipped",
  "blockedBy": "t2"
}
```

실패를 숨기고 억지로 진행하는 것보다, 어디서 막혔는지 정확히 드러내는 편이 지금 시스템에는 더 맞았다. DeToks는 사용자의 코드를 다룬다. 애매한 성공보다 명확한 실패가 낫다.

## 정리하면, 선택 기준은 "똑똑함"보다 "예측 가능성"이었다

오늘 정리하면서 느낀 것은, 내가 선택한 방식들이 전부 같은 방향을 보고 있었다는 점이다.

LLM 분류 대신 Regex First-match를 고른 것도, 동적 의존성 예측 대신 `FLOWS_TO`를 고른 것도, Event-driven 실행 대신 DAG 사전 검증을 고른 것도, Graceful Degradation 대신 Strict Cascade를 고른 것도 결국 같은 이유였다.

```text
예측 가능해야 한다.
테스트 가능해야 한다.
실패 원인이 남아야 한다.
실행 전에 최대한 많이 걸러야 한다.
```

DeToks가 나중에 더 똑똑해질 수는 있다. type 분류에 LLM을 섞거나, 사용자의 과거 작업 패턴을 보고 의존성을 더 잘 판단할 수도 있다. 하지만 지금 첫 구현에서 중요한 것은 "멋진 추론"이 아니라 "흔들리지 않는 실행 흐름"이었다.

그래서 Day 6 기준 Task Graph 로직은 아래처럼 정리된다.

```text
1. 문장 입력을 받는다.
2. 정규식 First-match로 task type을 분류한다.
3. FLOWS_TO 전이 테이블로 depends_on을 만든다.
4. DAGValidator가 없는 의존성, 순환, 고립 노드를 검사한다.
5. Kahn's Algorithm으로 topological order를 만든다.
6. DependencyResolver가 id 기반 결과를 task 객체로 연결한다.
7. ParallelClassifier가 stage를 계산한다.
8. 실행 중 실패가 생기면 Strict Cascade로 후속 task를 skipped 처리한다.
```

전체 흐름을 한 장으로 그리면 이렇다.

```text
┌─────────────────────┐
│ sentences[]          │
│ 자연어에서 나온 문장 │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Type Classification  │
│ Regex First-match    │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Dependency Decision  │
│ FLOWS_TO table       │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Task Graph           │
│ id, type, depends_on │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ DAG Validation       │
│ unknown / cycle check│
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Topological Sort     │
│ executable order     │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Parallel Stages      │
│ stage 0, 1, 2...     │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Execution            │
│ Strict Cascade on fail│
└─────────────────────┘
```

## 오늘 남은 찝찝함도 있다

정리하고 나니 방향은 꽤 명확해졌지만, 아쉬운 부분도 남았다. First-match와 `FLOWS_TO`는 의도적으로 단순한 방식이다. 그래서 복잡한 요청에서는 틀릴 수 있다.

예를 들어 사용자가 이렇게 말하면:

```text
"Fix the bug after checking the failing test"
```

문장 순서만 보면 `fix -> check`처럼 보일 수 있지만, 실제 의미는 `check -> fix`에 가깝다. 이런 경우는 `TaskSentenceSplitter`나 패턴 보강에서 더 봐야 한다. `after`, `before`, `then` 같은 연결어는 단순 분리보다 순서 해석이 중요하다.

또 `document`를 terminal 성격으로 둔 것도 상황에 따라 바뀔 수 있다. 문서화 뒤에 배포하는 흐름이 있을 수도 있고, 배포 뒤 문서화가 자연스러울 수도 있다. 지금 테이블은 "자주 나오는 개발 작업 흐름"을 기준으로 한 보수적인 선택이지, 영원한 정답은 아니다.

그래도 지금 단계에서는 이 정도의 제한이 필요했다. 범위를 너무 크게 잡으면 테스트도 설명도 어려워진다. 반대로 기준을 좁게 잡으면 틀린 부분을 하나씩 고쳐갈 수 있다. DeToks는 아직 연구용으로 모든 자연어를 이해하는 시스템이 아니라, 팀 프로젝트 안에서 실제로 돌아가는 CLI 최적화 레이어를 만드는 중이다.

## 마무리

4월 26일의 결론은 꽤 현실적이었다. Task Graph 로직은 똑똑해 보이려고 만든 게 아니라, 자연어 요청을 실행 가능한 순서로 바꾸고, 실행 전에 위험한 구조를 걸러내고, 실패했을 때 어디서 막혔는지 남기기 위해 만든 것이다.

처음에는 DAG나 Topological Sort 같은 단어가 괜히 거창하게 느껴졌는데, 정리하고 보니 오히려 가장 실용적인 선택에 가까웠다. DeToks의 입력은 매번 달라지고, task는 동적으로 생기고, 실행에는 비용과 부작용이 있다. 이런 조건에서는 실행 중에 감으로 이어 붙이는 방식보다, 실행 전에 그래프를 만들고 검증하는 방식이 더 맞다.

오늘 정리한 내용은 발표 자료에도 그대로 가져갈 수 있을 것 같다. "DAG를 사용했습니다"가 아니라, **Priority Queue와 Event-driven 방식은 왜 부족했고, First-match와 FLOWS_TO는 왜 현재 단계에 맞으며, 실패 처리는 왜 Strict Cascade가 안전한지**까지 설명할 수 있어야 한다. 그래야 Task Graph가 단순 구현물이 아니라 DeToks의 workflow 최적화 중심이라는 점이 보일 것 같다.
