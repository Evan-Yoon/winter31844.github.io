---
title: "[DeToks] Day 7 - 세션 안정화, 타입 분류 보강, 그리고 멘토링"
slug: tokit-day7-session-stability-and-mentoring
date: 2026-04-27
author: Evan Yoon
category: project
subcategory: team-project
description: "4월 27일에는 DeToks의 세션 지속성, task type 분류, REPL 세션 이어가기, DAG 연결성, task type 저장 로직을 정리했고, 최재흥 강사님 팀 멘토링을 통해 로컬 모델 번들링과 에러 처리 방향도 다시 잡았다."
thumbnail: ""
tags:
  - tokit
  - detoks
  - llm
  - cli
  - session
  - task-graph
  - mentoring
  - team-project
readTime: 12
series: "Tokit"
seriesOrder: 11
featured: false
draft: false
toc: true
---

4월 27일은 DeToks 프로젝트에서 꽤 빽빽한 하루였다. 전날까지는 Task Graph 로직을 왜 DAG로 풀었는지, 왜 First-match와 `FLOWS_TO` 같은 보수적인 규칙을 선택했는지 정리했다. 오늘은 그 로직이 실제 CLI 세션 안에서 계속 이어질 수 있게 만드는 쪽으로 작업이 옮겨갔다.

단순히 "기능 하나 추가했다"라고 적기에는 작업 범위가 조금 넓었다. 오늘 올라간 PR 기준으로 보면 세션 지속성, task type 분류 보강, 세션 ID 안정화, 문서화 후속 작업의 DAG 연결성 수정, task type 저장까지 이어졌다.

오늘 병합된 내 PR은 아래 다섯 개였다.

```text
#134 Feat/session persistence
#138 Fix/task graph type classification
#142 Feat/session id base62
#145 Enhance DAG connectivity documentation and translation
#147 Add task type persistence and documentation notes
```

숫자로만 보면 PR 5개, 변경 파일 수는 각각 7개, 4개, 14개, 5개, 4개였다. 물론 숫자가 곧 좋은 작업이라는 뜻은 아니지만, 오늘은 확실히 한 모듈만 만진 날은 아니었다. Task Graph와 Orchestrator, SessionStateManager, Context, CLI smoke test, 문서까지 계속 이어 보면서 "돌아는 가는데 상태가 불안한 부분"을 하나씩 줄이는 쪽에 가까웠다.

## 먼저 세션을 이어갈 수 있게 만들었다

가장 먼저 붙잡은 건 세션 지속성이었다. DeToks는 한 번의 요청을 처리하고 끝나는 도구가 아니라, CLI 안에서 사용자의 작업 흐름을 이어가야 한다. 그러려면 이전에 실행한 task 결과를 세션 파일에 저장하고, 같은 세션으로 다시 들어왔을 때 이미 끝난 task는 다시 실행하지 않아야 한다.

처음에는 세션 저장 자체만 생각하면 단순해 보였다. 그런데 실제로 구현하려고 하니 봐야 할 게 많았다.

```text
기존 세션이 있는가?
  ├─ 없다 -> 새 세션 생성
  └─ 있다 -> 세션 로드
        ├─ 버전이 맞는가?
        ├─ 현재 Task Graph와 맞는가?
        ├─ 이미 완료된 task가 있는가?
        └─ 로드 중 파일 충돌은 없는가?
```

그래서 `orchestrator`에는 `SESSION_VERSION`을 두고, 기존 세션을 로드할 때 버전이 맞는지 확인하도록 했다. 버전이 안 맞으면 억지로 이어가지 않고 새 상태로 초기화한다. 또 이전 세션에는 있는데 현재 그래프에는 없는 task id가 있으면, 그 세션을 그대로 재사용하지 않도록 했다. 이걸 고아 task라고 봤다.

이 부분이 필요한 이유는 단순하다. 세션 파일은 과거의 실행 결과이고, Task Graph는 현재 입력에서 다시 만들어진 실행 계획이다. 둘이 어긋난 상태에서 억지로 이어가면 "이미 완료된 task" 판단이 틀어질 수 있다. 그러면 실행해야 할 task가 skip되거나, 반대로 이미 처리한 task가 다시 실행될 수 있다.

완료 task를 skip하는 흐름도 같이 넣었다.

```text
for task in tasks:
  1. dependency 실패로 막힌 task인지 확인
  2. completed_task_ids에 이미 들어 있는지 확인
  3. 이미 완료됐다면 저장된 rawOutput을 재사용
  4. 아니면 executor 실행
  5. 실행 결과를 세션에 저장
```

이렇게 되면 세션을 재개했을 때 이미 끝난 작업을 다시 하지 않아도 된다. 특히 LLM 실행은 비용이 있고, 파일 작업은 부작용이 생길 수 있으니 "완료한 작업을 다시 하지 않는 것" 자체가 중요한 안정성이다.

## 파일 락도 같이 넣었다

세션 파일을 읽고 쓰기 시작하면 동시성 문제도 생긴다. 같은 세션을 동시에 건드리는 상황이 흔하지는 않더라도, CLI에서는 충분히 생길 수 있다. 테스트나 REPL, checkpoint 같은 기능이 붙으면 더 그렇다.

그래서 `SessionStateManager`에 파일 락을 추가했다.

```text
loadSession / saveSession
  ↓
acquireLock(sessionId)
  ├─ lock 파일 생성 성공 -> 진행
  ├─ 이미 lock 있음 -> 100ms 간격 재시도
  └─ 5초 초과 -> stale lock으로 보고 처리
  ↓
try { 읽기 또는 쓰기 }
finally { releaseLock(sessionId) }
```

여기서 중요하게 본 건 `finally`였다. 세션 저장 중간에 에러가 나더라도 lock을 해제하지 않으면 다음 작업이 계속 막힌다. 그래서 성공과 실패와 무관하게 마지막에는 lock을 풀도록 했다.

처음에는 이 정도면 충분할 것 같았는데, 뒤쪽 PR에서 stale lock 처리도 다시 손봤다. 단순히 `unlink`하고 다시 여는 방식은 아주 짧은 순간이지만 race window가 생길 수 있다. 그래서 이후에는 atomic rename 기반으로 더 보강했다. 이런 부분은 처음 설계할 때부터 완벽하게 잡기 어렵고, 실제 세션 흐름을 테스트하면서 조금씩 드러났다.

## type 분류는 corpus로 다시 검증했다

두 번째로 크게 만진 부분은 Task Graph의 type 분류였다. Day 6에서 정리했듯이 DeToks는 LLM 분류 대신 정규식 기반 First-match를 쓴다. 이 방식은 빠르고 결정론적이지만, 패턴 순서가 곧 로직이기 때문에 작은 충돌도 실제 그래프에 영향을 준다.

오늘은 `dataTest_Compact` 106개 파일을 전수 검증하는 corpus 테스트를 추가했다. `TaskSentenceSplitter`에서 문장을 나누고, `TaskGraphProcessor`가 그 문장들을 task로 바꾸는 전체 흐름을 돌렸다.

검증한 항목은 대략 이런 것들이었다.

```text
compiled_prompt
  ↓
TaskSentenceSplitter
  ↓
TaskGraphProcessor
  ↓
검증:
  - 문장이 비어 있지 않은가
  - task type이 8개 카테고리 중 하나인가
  - task id가 t1, t2 순서로 생성되는가
  - status가 pending인가
  - input_hash가 16자리 hex인가
  - depends_on이 앞선 task만 참조하는가
```

이 테스트를 돌리면서 오분류가 보였다. 대표적인 게 `make`였다.

`make`는 보통 create 쪽으로 볼 수 있다. 하지만 `make sure`는 생성이 아니라 검증에 가깝다. `make changes`는 수정이고, `make a note`는 문서화에 가깝다. 그런데 단일 키워드 `make`가 먼저 잡히면 전부 create로 빨려 들어갈 수 있었다.

그래서 `TYPE_PATTERNS` 앞에 `IDIOM_PATTERNS` pre-check 레이어를 추가했다.

```text
make sure / make certain       -> validate
make changes / improvements    -> modify
make use of                    -> execute
make a note / notes            -> document
make a plan / roadmap          -> plan
```

이건 거창한 NLP라기보다, 실제로 깨진 부분을 보고 가장 좁은 범위로 막은 수정이다. `TYPE_PATTERNS` 전체 순서를 뒤집으면 다른 케이스가 깨질 수 있어서, 다단어 숙어만 먼저 보는 작은 레이어를 둔 쪽이 맞았다.

질문형 문장도 하나 더 손봤다.

```text
"What should we address first?"
```

이런 문장은 명시적인 행동 키워드가 없으면 마지막 fallback 때문에 `execute`로 갈 수 있었다. 하지만 실제로는 실행이라기보다 분석에 가깝다. 그래서 모든 키워드 패턴을 다 통과한 뒤, 마지막에 `?`로 끝나는 문장을 `analyze`로 보내는 tier를 추가했다.

여기서도 위치가 중요했다. analyze 위치에 너무 앞쪽으로 넣으면 `How can we validate this?` 같은 문장이 validate가 아니라 analyze로 잡힐 수 있다. 그래서 이 규칙은 맨 끝에 두었다. 키워드가 있는 질문은 원래 type으로 가고, 정말 키워드가 없는 질문만 analyze로 빠지게 한 것이다.

## 세션 ID는 timestamp에서 base62 랜덤으로 바꿨다

세션 지속성을 붙이고 나니 세션 ID 생성 방식도 다시 봐야 했다. 기존에는 timestamp 기반 SHA256에서 12자 hex를 쓰는 방식이었다. 보통은 괜찮지만, 같은 밀리초에 생성되면 충돌 가능성이 있다. 세션 파일을 다루는 입장에서는 낮은 확률이라도 충돌하면 기존 세션을 덮어쓸 수 있다는 점이 문제였다.

그래서 세션 ID를 cryptographically random base62 문자열로 바꿨다.

```text
orchestrator session id:
  24자 base62
  A-Z, a-z, 0-9

REPL session id:
  repl- + 16자 base62
```

`randomInt`를 사용해서 모듈로 편향 없이 62개 문자 중 하나를 고르게 했다. 여기에 bounded allocation 루프를 붙여서, 새로 만든 ID가 이미 존재하면 다시 생성하도록 했다. 충돌 확률은 낮지만, 충돌했을 때의 동작을 정해두는 게 더 중요했다.

이 PR에서 세션 관련 안정성도 많이 같이 손봤다. 기존 세션을 로드하다가 실패했을 때 새 세션으로 덮어쓰는 위험을 막았고, checkpoint restore에서 session id를 더 엄격하게 파싱하도록 했다. `SessionStateManager`에는 failed task 수와 checkpoint 수 같은 metadata도 보강했다.

특히 기억에 남는 건 REPL continuation 문제였다.

REPL에서는 같은 세션으로 여러 번 입력을 이어간다. 그런데 Task Graph는 새 입력이 들어올 때마다 `t1`부터 task id를 만든다. 그러면 첫 번째 입력의 `t1`이 이미 completed로 저장되어 있고, 두 번째 입력도 다시 `t1`로 생성되면서 "이미 완료된 task"로 오해될 수 있었다.

```text
1번째 입력:
  t1 실행 완료
  completed_task_ids = ["t1"]

2번째 입력:
  새 task도 t1로 생성
  completed_task_ids에 t1 있음
  -> 실행하지 않고 skip
```

이건 세션 재개 로직을 만들고 나서야 보이는 문제였다. 해결은 새 입력의 task id를 기존 최대 task id 다음 번호로 다시 부여하는 방식으로 했다.

```text
1번째 입력 -> t1
2번째 입력 -> t2
3번째 입력 -> t3
```

그리고 후속 입력이 명시적인 dependency를 갖지 않더라도 최근 완료 task 결과를 context로 받을 수 있게 했다. REPL에서 사용자는 보통 "방금 한 것 이어서 해줘"라는 식으로 말하기 때문에, 같은 세션 안에서는 직전 결과를 어느 정도 이어주는 게 맞다.

## 멘토링에서는 방향을 다시 확인했다

오후에는 최재흥 강사님께 팀 멘토링을 받았다. 멘토링을 신청한 이유는 팀 안에서 로컬 모델을 번들링할지, API 통신으로 갈지 의견이 갈렸기 때문이다.

우리 팀의 고민은 이랬다.

```text
CLI 도구라면 가벼워야 한다
  vs
번역과 프롬프트 변환 품질을 생각하면 큰 모델이 필요하다
```

소형 모델을 쓰면 CLI답게 가볍지만, 한국어를 영어 개발 명령으로 바꾸는 품질이 기대만큼 나오지 않았다. 반대로 3GB 이상 모델을 쓰면 품질은 나아지지만, 설치 용량과 메모리 점유가 부담이 된다.

강사님 피드백에서 제일 먼저 나온 건 "CLI의 목표가 무엇인가"였다. 경량성이 먼저라면 로컬 소형 모델을 쓰고 정확도를 단계적으로 올리는 로드맵이 맞고, 정확도가 먼저라면 API 통신을 허용해서 Gemini Pro나 Claude Sonnet급 모델을 쓰는 편이 맞다는 이야기였다.

처음에는 이 질문이 당연해 보였는데, 사실 팀에서 계속 흔들리던 지점이 바로 여기였다. 우리는 CLI 앞단에서 개입하는 경량 최적화 레이어를 만들고 싶어 했다. 그렇다면 큰 모델을 무작정 붙여서 품질을 올리는 것보다, 어디까지 로컬에서 감당할지 기준을 먼저 잡아야 했다.

다만 멘토링 후 결론은 단순히 "무조건 작게"가 아니었다. 강사님은 CLI 설치 시 모델을 같이 설치하는 것도 사용자 경험상 이상하지 않을 수 있다고 했다. 중간에 갑자기 3GB를 추가 다운로드하게 하는 것보다, 처음부터 용량을 명시하고 설치하는 편이 더 자연스러울 수 있다는 관점이었다.

그래서 팀 결론은 이렇게 정리됐다.

```text
기본 방향:
  로컬 모델 번들링 방식 유지

단, 조건:
  - 타겟 사용자 디바이스 스펙을 정해야 함
  - 메모리 점유 상한선을 잡아야 함
  - API 방식은 성능 비교 대상으로 남겨둘 수 있음
  - 자체 서버 호스팅은 비용 문제로 제외
```

나한테는 이 결론이 꽤 현실적으로 느껴졌다. "CLI니까 무조건 작아야 한다"도 아니고, "품질이 중요하니까 큰 모델을 쓰자"도 아니었다. 결국 찾아야 하는 건 성능과 정확도가 나쁘지 않은 가성비 구간이었다.

## 에러 처리는 try-catch와 결과값을 같이 쓰기로 했다

멘토링에서 에러 처리 이야기도 나왔다. 팀 안에서는 try-catch로 처리할지, 아니면 결과값 자체에 성공/실패를 담을지 고민이 있었다. 오늘 세션 로드 실패나 파일 락, checkpoint restore를 만지면서 나도 이 부분을 계속 생각하고 있었다.

강사님 피드백은 명확했다.

```text
try-catch와 결과값 처리는 대립 관계가 아니다.
예외는 try-catch로 잡고,
catch에서 예외를 죽이지 말고,
실패 상태를 리턴 값으로 반환해야 한다.
```

이 말이 오늘 작업과도 잘 맞았다. 예를 들어 기존 세션 로드에 실패했을 때 그냥 catch하고 새 세션으로 이어가면 겉으로는 복구된 것처럼 보인다. 하지만 실제로는 기존 세션 파일을 덮어쓸 수 있다. 그래서 catch는 필요하지만, catch 이후에는 구조화된 실패 결과를 반환하고 저장을 멈추는 게 맞다.

오늘 수정한 세션 로드 실패 방어도 이 방향과 이어진다.

```text
기존 방식에 가까운 흐름:
  loadSession 실패
  -> catch
  -> 새 세션 생성
  -> 저장
  -> 기존 세션 손상 가능

수정한 흐름:
  loadSession 실패
  -> catch
  -> 실패 결과 반환
  -> 저장 중단
  -> 기존 상태 보호
```

결국 CLI에서 중요한 건 실패를 없애는 게 아니라, 실패를 사용자가 이해할 수 있는 상태로 남기는 것이다. 오늘 만든 세션 보호 로직과 멘토링에서 들은 에러 처리 방향이 같은 쪽을 보고 있어서, 이후 통합 테스트 때도 이 기준을 유지하면 될 것 같다.

## 모듈 책임은 컨버터 패턴으로 풀기로 했다

멘토링에서 또 하나 도움이 된 부분은 모듈 간 데이터 흐름이었다. DeToks는 Role 1, Task Graph, State & Context, Orchestrator, CLI가 서로 데이터를 주고받는다. 그러다 보니 "어느 모듈이 어디까지 책임져야 하는가"가 계속 애매해진다.

강사님은 처음부터 모든 충돌을 막으려고 하지 말고, 우선 합의한 데이터 형태로 개발한 다음, 실제 충돌이 생기면 해당 모듈에 컨버터 함수를 두는 방식을 제안했다.

이건 오늘 작업에도 그대로 적용할 수 있는 말이었다. `task.type` 저장이 좋은 예다. Phase 7.1부터 7.3까지 schema와 state persistence 계층은 `type` 필드를 받을 준비가 되어 있었다. 하지만 실제 Orchestrator 실행 결과를 거쳐 session state에 저장되는 경로가 빠져 있었다.

그래서 #147에서 그 흐름을 연결했다.

```text
task.type
  -> ExecutionResult / TaskResult
  -> SessionState
  -> Session File
```

성공한 task뿐 아니라 실패한 task, dependency 실패로 skip된 task도 type을 보존하도록 했다. 나중에 세션을 다시 열었을 때 단순히 성공/실패만 보는 게 아니라, 어떤 종류의 task가 실패했는지까지 볼 수 있어야 하기 때문이다.

예전 session result는 이런 느낌이었다.

```json
{
  "task_id": "t1",
  "success": true,
  "summary": "...",
  "raw_output": "..."
}
```

수정 후에는 type이 같이 남는다.

```json
{
  "task_id": "t1",
  "success": true,
  "summary": "...",
  "raw_output": "...",
  "type": "analyze"
}
```

이 변경은 작아 보이지만, Role 2.1에서 만든 분류 결과가 실행 이후에도 사라지지 않게 만드는 작업이었다. Task Graph와 State 사이의 데이터 흐름을 실제로 이어 붙인 셈이다.

## DAG 연결성도 다시 손봤다

오후 늦게는 문서화 task와 관련된 DAG 연결성 문제도 수정했다. 원인은 두 가지였다.

첫째, `create comprehensive documentation` 같은 표현이 `document`가 아니라 `create`로 분류될 수 있었다. 둘째, 기존에는 `document`를 거의 terminal type처럼 다뤘기 때문에, 문서화 이후 명시적인 후속 작업이 있어도 그래프가 끊길 수 있었다.

예를 들어 이런 요청이 있다.

```text
Analyze the entire codebase
create a comprehensive documentation with examples
implement all suggested improvements
validate everything
```

수정 전에는 이런 식으로 끊어진 그래프가 나올 수 있었다.

```text
t1 analyze -> t2 document

t3 create
  -> t4 validate
```

이 상태에서는 `DAGValidator`가 `t3`를 고립 노드로 보고 거부한다. 중요한 건 여기서 `DAGValidator`가 틀린 게 아니라는 점이다. 검증기는 끊어진 그래프를 제대로 거부한 것이다. 고쳐야 할 곳은 graph builder였다.

그래서 문서 생성 표현을 `document`로 더 잘 잡도록 패턴을 보강하고, `document` 뒤에도 명시적인 후속 작업이 있으면 `analyze`, `modify`, `validate`, `execute`, `create`, `plan`으로 이어질 수 있게 `FLOWS_TO.document`를 수정했다.

수정 후 기대 흐름은 이렇다.

```text
t1 analyze -> t2 document -> t3 create -> t4 validate
```

이 작업은 Day 6에서 정리한 DAG 원칙과도 이어진다. DAG 검증은 느슨하게 만들면 안 된다. 그래프가 끊겼으면 거부하는 게 맞다. 대신 TaskGraphProcessor가 실제 사용자 문장을 더 잘 반영해서, 순서가 있는 workflow를 끊어진 그래프로 만들지 않도록 해야 한다.

## 오늘 작업을 한 흐름으로 보면

오늘 작업은 각각 다른 PR로 나뉘어 있지만, 한 줄로 묶으면 "세션이 있는 Task Graph 실행을 실제 CLI에 견딜 수 있게 다듬은 날"이었다.

흐름을 그리면 이렇다.

```text
사용자 입력
  ↓
TaskSentenceSplitter
  ↓
TaskGraphProcessor
  - IDIOM_PATTERNS
  - 질문형 analyze fallback
  - document 후속 연결성 보강
  ↓
DAGValidator / DependencyResolver / ParallelClassifier
  ↓
Orchestrator
  - session load
  - completed task skip
  - failed task cascade
  - task.type 전달
  ↓
SessionStateManager
  - file lock
  - version check
  - checkpoint
  - failed task metadata
  ↓
Session File
```

전날에는 "왜 DAG인가"를 설명했다면, 오늘은 그 DAG가 세션과 만나면서 생기는 문제를 많이 봤다. 그래프만 만들 때는 `t1`, `t2`, `t3`이면 충분하다. 하지만 세션이 붙으면 `t1`이 과거의 `t1`인지, 방금 새로 만든 `t1`인지 구분해야 한다. checkpoint가 붙으면 이 세션 ID가 정말 복구 가능한 ID인지 확인해야 한다. REPL이 붙으면 직전 task 결과를 다음 입력의 context로 넘길지 판단해야 한다.

이런 것들은 겉으로 보기에 화려한 기능은 아니다. 하지만 실제로 CLI가 계속 실행되려면 이런 부분이 더 중요하다.

## 마무리

오늘은 작업량도 많았고, 생각할 거리도 많았다. 세션 지속성을 붙이다 보니 ID 충돌, 파일 락, 로드 실패, REPL continuation, checkpoint restore 같은 문제가 줄줄이 따라왔다. type 분류를 corpus로 돌려보니 `make sure` 같은 작은 표현 하나가 전체 task type을 바꿀 수 있다는 것도 확인했다. 문서화 task는 terminal이라고만 생각했는데, 실제 workflow에서는 문서화 뒤에도 구현과 검증이 이어질 수 있었다.

멘토링도 방향을 잡는 데 도움이 됐다. 로컬 모델 번들링과 API 통신 문제는 단순히 용량이 크다 작다로 볼 게 아니었고, CLI의 목표와 타겟 사용자 환경을 같이 봐야 했다. 에러 처리도 try-catch냐 결과값이냐로 나눌 게 아니라, 예외는 잡되 실패 상태는 구조적으로 반환하는 쪽이 맞다는 기준이 생겼다.

오늘 만든 변경들이 전부 눈에 띄는 기능처럼 보이지는 않을 수 있다. 그래도 DeToks가 "한 번 실행되는 데모"가 아니라 "세션을 이어가며 작업하는 CLI"가 되려면 필요한 작업들이었다. 내 기준에서는 오늘이 Task Graph를 설명하는 단계에서, 실제 실행 상태를 책임지는 단계로 넘어간 날에 가까웠다.
