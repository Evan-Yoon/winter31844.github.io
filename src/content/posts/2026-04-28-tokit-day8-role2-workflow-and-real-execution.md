---
title: "[DeToks] Day 8 - Role 2.1 워크플로우 재정리와 실제 실행 경로 안정화"
slug: tokit-day8-role2-workflow-and-real-execution
date: 2026-04-28
author: Evan Yoon
category: project
subcategory: team-project
description: "4월 28일에는 DeToks의 사용자 프롬프트 처리 흐름을 다시 정리했다. Codex real adapter 실행 방식을 고치고, Role 2.1이 압축 결과가 아니라 정규화 입력을 기준으로 task graph를 만들도록 바꿨으며, 담화 표현과 실제 LLM 번역 표현 때문에 생긴 task 분리 문제를 보강했다."
thumbnail: ""
tags:
  - tokit
  - detoks
  - llm
  - cli
  - workflow
  - task-graph
  - role2
  - real-mode
  - team-project
readTime: 12
series: "Tokit"
seriesOrder: 12
featured: false
draft: false
toc: true
---

4월 28일은 DeToks의 실행 흐름을 다시 잡은 날이었다. 전날에는 세션 저장, 재개, task type 저장처럼 "작업을 어떻게 이어갈 것인가"를 정리했다. 오늘은 그보다 앞단으로 돌아가서, 사용자가 프롬프트를 입력했을 때 그 문장이 어떤 과정을 거쳐 실제 task graph와 실행 adapter까지 이어지는지를 손봤다.

쉽게 말하면 이렇다.

> 어제는 만들어진 작업 흐름을 저장하고 이어가는 문제를 봤다면,  
> 오늘은 애초에 작업 흐름이 만들어지는 입구를 다시 정리한 날이었다.

오늘 병합된 내 PR은 다섯 개였다.

```text
#153 Codex adapter 실행 방식 수정과 WSL Ubuntu 실행 문서화
#158 문장 분리 안정화와 실제 실행 adapter 로직 보강
#159 Role 2.1 handoff 입력을 압축 결과에서 정규화 입력으로 변경
#169 Role 2.1 task 후보 추출기 추가
#171 실제 LLM 번역 표현 기준 task 후보 추출 보강
```

겉으로 보면 adapter, splitter, compiler, extractor가 따로 움직인 것처럼 보인다. 하지만 실제로는 하나의 문제로 이어져 있었다.

```text
사용자가 프롬프트를 입력한다
  ↓
프롬프트를 정규화하고 압축한다
  ↓
Role 2.1이 실행 가능한 작업을 뽑는다
  ↓
Task Graph를 만든다
  ↓
Codex/Gemini adapter가 실제 실행한다
```

이 흐름 중 한 군데만 틀어져도 결과가 이상해진다. 오늘은 특히 `사용자 프롬프트 -> Role 2.1 -> Task Graph` 구간에서 많은 문제가 드러났다.

## 오늘 한 일을 먼저 짧게 정리하면

오늘 작업은 크게 다섯 가지였다.

```text
1. Codex real mode가 interactive CLI 대신 codex exec - 로 실행되게 했다.
2. Windows native real mode는 지원 대상에서 빼고 WSL Ubuntu 실행 경로를 문서화했다.
3. TaskSentenceSplitter가 수식 숫자나 순서 표식을 task로 잘못 자르는 문제를 고쳤다.
4. Role 2.1이 compressed prompt가 아니라 normalized input을 기준으로 task graph를 만들게 했다.
5. TaskCandidateExtractor를 추가해 담화 표현, 공손 표현, 메타 지시문을 task 후보에서 걷어냈다.
```

오늘의 중심은 4번과 5번이었다. DeToks는 한국어로 길게 말한 요청을 내부에서 영어에 가까운 작업 문장으로 정리한 뒤 task graph를 만든다. 그런데 실제 사용자는 명령만 또렷하게 쓰지 않는다.

예를 들어 이런 식이다.

```text
음, 지금 바로 급한 건 아니지만 그래도 가능하면 차근차근 처리해줘.
혹시 괜찮다면 먼저 인증 관련 코드가 어디 있는지 찾아보고,
그 다음에는 로그인 요청이 컨트롤러에서 서비스와 저장소까지 어떻게 흘러가는지 자세히 분석해줘.
그리고 아마 중복 검증 로직 때문에 문제가 생기는 것 같으니까 그 버그를 수정해줘.
수정이 끝나면 꼭 회귀 테스트랑 관련 단위 테스트를 실행해서 제대로 고쳐졌는지 확인해줘.
마지막으로 같은 내용을 두 번 말하는 것 같긴 한데, 변경한 이유와 확인한 테스트 결과를 README나 작업 노트에 문서화해줘.
불필요한 말은 줄여도 되고, 중요한 작업 순서만 유지해줘.
```

실제로 해야 할 일은 다섯 개다.

```text
1. 인증 관련 코드 위치 찾기
2. 로그인 요청 흐름 분석하기
3. 중복 검증 버그 수정하기
4. 회귀 테스트와 단위 테스트 실행하기
5. 변경 이유와 테스트 결과 문서화하기
```

하지만 문장 안에는 실행할 일이 아닌 표현도 많이 섞여 있다.

```text
지금 바로 급한 건 아니지만
혹시 괜찮다면
같은 내용을 두 번 말하는 것 같긴 한데
불필요한 말은 줄여도 되고
```

사람은 이런 표현을 자연스럽게 무시한다. 하지만 코드 입장에서는 이 표현들도 모두 문장이다. 그래서 오늘은 DeToks가 사람처럼 "실제로 해야 할 일"만 골라낼 수 있게 흐름을 바꿨다.

## 먼저 실제 실행 경로를 고쳤다

아침에는 Codex real adapter부터 봤다. DeToks에는 mock 실행이 아니라 실제 Codex CLI를 subprocess로 호출하는 real mode가 있다. 이 경로가 안정적으로 돌아야 Role 2.1에서 만든 task graph가 실제 실행까지 이어진다.

문제는 기존 Codex adapter가 interactive CLI를 stdin으로 호출하던 점이었다.

```text
detoks
  ↓
subprocess
  ↓
codex
  ↓
stdin으로 prompt 전달
```

이 방식은 Windows native 환경에서 npm global shim 때문에 불안정했고, WSL Ubuntu에서도 `stdin is not a terminal` 문제가 날 수 있었다. CLI를 사람이 직접 여는 방식과 프로그램이 비대화형으로 호출하는 방식은 다르다.

그래서 Codex adapter를 `codex exec -` 기반으로 바꿨다.

```text
기존:
  codex

수정:
  codex exec - --sandbox workspace-write --skip-git-repo-check --color never
```

이렇게 바꾸면 DeToks는 Codex를 interactive 화면으로 여는 게 아니라, 입력을 받아 실행하고 결과를 돌려주는 비대화형 명령으로 다룬다.

그리고 Windows native real mode는 지원 대상에서 제외했다. 대신 Windows 사용자는 WSL Ubuntu에서 실행하는 것으로 README를 정리했다.

```text
Windows native PowerShell
  -> real mode 지원 대상에서 제외

WSL Ubuntu
  -> Node 설치
  -> npm install
  -> Windows llama-server와 연결
  -> --execution-mode real 실행
```

이 결정을 한 이유는 단순하다. 모든 환경을 억지로 지원하려고 하면 adapter 코드가 지저분해지고, 실제로 검증하기 어려운 실행 경로가 늘어난다. 지금 단계에서는 "되는 환경을 명확히 정하고, 그 경로를 안정화하는 것"이 더 중요했다.

또 하나 고친 것은 실패 출력이었다. subprocess가 실패했는데 stdout이 비어 있고 stderr에만 원인이 있으면, 기존에는 `rawOutput`이 비어 보일 수 있었다.

```text
stdout: ""
stderr: "stdin is not a terminal"
```

이 경우 사용자에게는 실패 이유가 사라진 것처럼 보인다. 그래서 stdout이 비어 있으면 stderr를 raw output으로 남기도록 했다.

```text
rawOutput = stdout || stderr
```

작은 수정이지만, 실제 CLI에서는 중요하다. 실패가 났을 때 원인을 볼 수 있어야 다음 수정이 가능하다.

## 문장 분리에서 수식과 순서 표식이 문제를 만들었다

다음으로 본 것은 `TaskSentenceSplitter`였다. 이 컴포넌트는 사용자의 긴 입력을 task 후보가 될 만한 문장과 절로 나누는 역할을 한다.

기존 splitter에는 번호 목록을 처리하는 규칙이 있었다.

```text
1. create module 2. run tests 3. document result
```

이런 문장이 들어오면 `1.`, `2.`, `3.` 앞에서 나눠야 한다. 문제는 이 규칙이 너무 넓었다.

예를 들어 계산기 요청에서 이런 문장이 들어왔다.

```text
Create the result of 4 + 5. Also check it.
```

여기서 `5.`는 번호 목록이 아니다. 그냥 수식 뒤에 온 문장 마침표다. 그런데 기존 로직은 숫자와 점이 나오면 번호 목록처럼 보고 문장을 잘랐다.

```text
수정 전:
Create the result of 4 +
Also check it.
```

이렇게 잘리면 뒤쪽 graph는 처음부터 틀어진다. `4 +`는 완성된 작업 문장이 아니기 때문이다.

그래서 숫자 앞의 의미 있는 문자가 산술 연산자인지 확인하도록 조건을 좁혔다.

```text
4 + 5.
10 minus 3.
4 plus 5.
```

이런 표현은 번호 목록으로 보지 않고 수식으로 보존한다.

또 하나는 순서 표식이었다.

```text
First, create an addition design.
Next, create a subtraction design.
Finally, verify the result.
```

기존에는 쉼표 기준으로 나뉘면서 `First`, `Next`, `Finally`가 독립 task처럼 남을 수 있었다.

```text
수정 전:
First
create an addition design
Next
create a subtraction design
Finally
verify the result
```

이러면 실제로는 세 개 작업인데 여섯 개처럼 보인다. `First` 같은 단어는 작업이 아니다. 순서를 알려주는 표시일 뿐이다.

수정 후에는 순서 표식만 남은 fragment를 뒤 action clause와 합쳤다.

```text
수정 후:
create an addition design
create a subtraction design
verify the result
```

이 작업을 하면서 `TaskSentenceSplitter`의 책임도 다시 생각하게 됐다. splitter는 문장을 나누는 도구이지, 그 문장이 실행 가능한 작업인지 판단하는 도구가 아니다. 이 구분이 뒤에서 더 중요해졌다.

## Role 2.1 handoff 입력을 바꿨다

오늘 가장 큰 전환점은 Role 2.1 handoff 입력을 바꾼 것이었다.

기존 흐름은 대략 이랬다.

```text
사용자 입력
  ↓
정규화
  ↓
압축
  ↓
compressed_prompt를 Role 2.1에 전달
  ↓
문장 분리
  ↓
Task Graph 생성
```

처음에는 자연스러운 흐름처럼 보였다. DeToks가 프롬프트를 줄이고 정리하는 도구라면, 압축된 결과를 다음 단계에 넘기는 게 맞아 보였기 때문이다.

하지만 Role 2.1은 "프롬프트를 요약해서 읽는 역할"이 아니라 "실행 가능한 작업을 정확히 나누는 역할"이다. 이 역할에서는 압축 결과가 오히려 위험할 수 있었다.

압축은 문장을 짧게 만들면서 표현을 바꾼다. 그러다 보면 action signal이 약해지거나, 원래 있던 작업 경계가 흐려질 수 있다.

예를 들어 원래 입력이 이랬다고 하자.

```text
Please create a new file and test it.
```

Role 2.1 입장에서는 이 문장을 두 작업으로 봐야 한다.

```text
create a new file
test it
```

그런데 압축 결과를 기준으로 보면 `create`와 `test`의 경계가 흐려질 수 있다. 그래서 Role 2.1에는 압축 전 정규화 입력을 넘기도록 바꿨다.

```text
수정 전:
compiledPrompt.compressed_prompt -> Role 2.1

수정 후:
compiledPrompt.normalized_input -> Role 2.1
```

여기서 중요한 점은 `compiledPrompt` 자체를 없앤 것이 아니라는 점이다. 압축 결과는 여전히 기록되고, 다른 단계에서 쓸 수 있다. 다만 task graph를 만드는 Role 2.1은 정규화 입력을 직접 본다.

수정 후 흐름은 이렇게 됐다.

```text
사용자 입력
  ↓
정규화
  ├─ compressed prompt 생성 및 기록
  └─ normalized input을 Role 2.1에 전달
        ↓
      task graph 생성
```

이렇게 바꾸자 다른 문제가 드러났다. Role 2.1이 압축 전 입력을 직접 보게 되면서 공손 표현도 같이 보게 된 것이다.

```text
Please create a new file
Can you please run tests
Could you please document it
Would you please fix this
```

사람에게는 자연스럽지만, action starter를 찾는 규칙에는 방해가 된다. 그래서 `cleanClause()`에서 이런 polite prefix를 제거했다.

```text
Please create a new file
  -> create a new file

Can you please run tests
  -> run tests
```

이 변경은 단순한 입력값 교체가 아니었다. DeToks의 workflow에서 Role 2.1의 위치를 다시 정한 작업이었다.

## workflow가 왜 바뀌었는가

오늘 바꾼 workflow의 이유는 하나였다.

```text
압축된 문장은 읽기에는 좋을 수 있지만,
task graph를 만들기에는 원문 정보가 부족할 수 있다.
```

DeToks의 앞단에는 서로 다른 목적의 작업이 섞여 있다.

```text
정규화:
  사용자의 말을 처리하기 쉬운 형태로 정리한다

압축:
  토큰을 줄이고 불필요한 표현을 줄인다

Role 2.1:
  실제 실행 가능한 task를 뽑고 순서를 만든다
```

이 셋은 비슷해 보이지만 목적이 다르다. 압축은 정보를 줄이는 작업이고, task graph 생성은 정보를 잃으면 안 되는 작업이다. 특히 사용자가 여러 작업을 한 문장에 섞어 말할 때는 작은 접속사나 동사가 작업 경계를 알려준다.

그래서 흐름을 이렇게 바꿨다.

```text
수정 전:
사용자 입력
  -> 정규화
  -> 압축
  -> Role 2.1
  -> Task Graph

수정 후:
사용자 입력
  -> 정규화
  -> Role 2.1
  -> TaskCandidateExtractor
  -> Task Graph

별도 기록:
정규화
  -> 압축 결과 저장
```

압축은 계속 필요하다. 하지만 Role 2.1의 기본 입력으로 쓰기에는 맞지 않았다. Role 2.1은 사용자가 실제로 말한 작업 구조를 최대한 보존한 상태에서 graph를 만들어야 했다.

## TaskCandidateExtractor를 추가했다

Role 2.1이 정규화 입력을 직접 보게 되자, `TaskSentenceSplitter`만으로는 부족하다는 점이 더 분명해졌다.

splitter는 문장을 나눈다. 하지만 "이 문장이 실행할 작업인가?"까지 판단하면 책임이 너무 커진다.

그래서 새로 `TaskCandidateExtractor`를 추가했다.

```text
Role 2.1 normalized input
  ↓
TaskSentenceSplitter
  ↓
TaskCandidateExtractor
  ↓
TaskGraphProcessor
  ↓
TaskGraph
```

이전에는 splitter 결과가 바로 processor로 들어갔다.

```text
수정 전:
normalized input
  -> TaskSentenceSplitter
  -> TaskGraphProcessor
```

수정 후에는 중간에 candidate extractor가 들어간다.

```text
수정 후:
normalized input
  -> TaskSentenceSplitter
  -> TaskCandidateExtractor
  -> TaskGraphProcessor
```

이 extractor의 역할은 실행 가능한 task sentence만 남기는 것이다.

제거하는 표현은 이런 것들이다.

```text
not urgent right now
if it's okay
although it seems like you are saying the same thing twice
reduce unnecessary words
maintain only the important sequence
```

이 표현들은 사용자의 의도를 부드럽게 만들거나, 전체 진행 방식을 설명할 뿐 실제로 실행해야 할 task는 아니다.

반대로 남겨야 하는 문장은 이런 것이다.

```text
find where the authentication-related code is
analyze how the login request flows
fix that bug
run regression tests and related unit tests
document the reason for the change and the test results
```

결과적으로 기대 흐름은 이렇게 된다.

```text
explore -> analyze -> modify -> validate -> document
```

여기서 중요한 점은 splitter를 더 똑똑하게 만든 게 아니라는 점이다. splitter에는 저수준 분리 책임만 남기고, 실행 가능한 후보 판정은 extractor로 뺐다. 이게 오늘 workflow 변경의 핵심이었다.

## 실제 LLM 번역 결과가 또 다른 문제를 보여줬다

처음 `TaskCandidateExtractor`를 추가했을 때는 담화 표현이 많은 테스트 문장을 기준으로 충분해 보였다. 하지만 실제 llama + Kompress 경로를 돌려보니 새로운 문제가 나왔다.

테스트 문장은 사람이 기대하는 형태에 가깝다. 하지만 실제 LLM 번역 결과는 조금씩 다르게 나온다.

예를 들어 사용자가 결제와 캐시 문제를 말한 프롬프트에서는 이런 흐름을 기대했다.

```text
explore -> analyze -> modify -> validate -> document
```

그런데 실제 번역 결과에는 이런 표현이 섞였다.

```text
organize it in order when you have time
```

이 문장은 전체 진행을 부드럽게 말한 메타 문장이다. 작업이 아니다. 그런데 그대로 들어가면 별도의 `plan` task처럼 잡힐 수 있었다.

```text
수정 전:
plan -> explore -> analyze -> modify -> validate -> document
```

실제로 해야 할 일은 다섯 개인데 여섯 개가 된다. 그래서 이런 전체 진행 메타 문장은 task candidate에서 제외했다.

또 다른 문제는 원인 맥락이 사라지는 것이었다.

```text
because the cache expiration conditions do not match,
the amount seems to be left with old values,
so fix that defect
```

이 문장에서 핵심은 `fix that defect`만이 아니다. 어떤 결함인지가 앞쪽 원인에 들어 있다.

그런데 후보 추출이 뒤쪽 action만 잘라내면 이렇게 된다.

```text
fix that defect
```

이러면 실제 수정 task가 너무 빈약해진다. Codex 같은 실행 adapter에 넘겼을 때 "어떤 결함?"이라는 정보가 빠진다.

그래서 원인-수정 문장은 원인 맥락을 보존하도록 정규화했다.

```text
수정 전:
fix that defect

수정 후:
fix the defect because the cache expiration conditions do not match,
the amount seems to be left with old values
```

이 변경은 작지만 중요했다. task graph는 단순히 type과 dependency만 맞으면 되는 게 아니다. 각 task가 실행에 필요한 정보를 충분히 들고 있어야 한다.

## trace와 organize도 문맥에 따라 다르게 봐야 했다

실제 LLM 출력에서는 단어 하나만 보고 type을 정하기 어려운 경우도 있었다.

첫 번째는 `trace`였다.

```text
trace and explain the order in which data passes through
the router, handler, domain service, and data layer
```

`trace`만 보면 뭔가를 찾아가는 `explore`처럼 보일 수 있다. 하지만 여기서는 데이터가 어떤 순서로 흐르는지 설명하라는 요청이다. 위치를 찾는 것이 아니라 흐름을 해석하는 일이다.

그래서 `trace and explain ... order/passes through` 형태는 `analyze`로 우선 분류했다.

```text
단순 trace:
  explore

trace and explain order/passes through:
  analyze
```

두 번째는 `organize`였다.

```text
organize the changes made and the results of the commands checked in the work note
```

`organize`만 보면 plan으로 갈 수 있다. 하지만 `changes`, `results`, `commands`, `work note`가 같이 있으면 작업 노트에 정리하라는 뜻이다. 이건 계획이 아니라 문서화다.

그래서 아래 조건을 같이 보도록 했다.

```text
organize + work note
organize + changes/results/commands
```

이런 경우는 `document`로 분류한다.

최종적으로 실제 llama 번역 결과 기준 기대 후보는 이렇게 정리됐다.

```text
1. find where the module related to payment processing is scattered in the project.
2. trace and explain the order in which data passes through the router, handler, domain service, and data layer from the shopping cart to payment approval.
3. fix the defect because the cache expiration conditions do not match, the amount seems to be left with old values.
4. run smoke tests and related regression tests to verify that the same symptoms do not occur again.
5. organize the changes made and the results of the commands checked in the work note.
```

분류 결과는 이렇게 나와야 한다.

```text
explore -> analyze -> modify -> validate -> document
```

dependency도 순차 흐름을 유지해야 한다.

```text
t1 -> t2 -> t3 -> t4 -> t5
```

## 오늘 바뀐 전체 workflow

오늘 작업을 반영한 전체 흐름은 이렇게 볼 수 있다.

```text
사용자 프롬프트
  ↓
언어 감지와 정규화
  ↓
압축 결과 생성
  - compiledPrompt에는 압축 결과를 기록
  - Role 2.1에는 normalized input을 전달
  ↓
TaskSentenceSplitter
  - 수식 숫자 보존
  - 순서 표식 단독 task 방지
  - list preamble 제거
  ↓
TaskCandidateExtractor
  - 공손 표현 제거
  - 담화 표현 제거
  - 메타 지시문 제거
  - 원인-수정 문장 맥락 보존
  ↓
TaskGraphProcessor
  - task type 분류
  - trace/explain 흐름 분석 보정
  - work note 문서화 보정
  ↓
Task Graph
  - explore -> analyze -> modify -> validate -> document
  - t1 -> t2 -> t3 -> t4 -> t5
  ↓
Orchestrator
  ↓
Execution adapter
  - Codex real mode는 codex exec - 사용
  - 실패 시 stderr도 rawOutput으로 보존
```

이전 흐름과 비교하면 가장 큰 차이는 두 가지다.

```text
1. Role 2.1이 compressed prompt가 아니라 normalized input을 본다.
2. splitter와 processor 사이에 TaskCandidateExtractor가 들어간다.
```

전자는 정보 손실을 줄이기 위한 변경이고, 후자는 실행할 일과 실행하지 않을 말을 분리하기 위한 변경이다.

## 바꾸면서 생긴 문제들

workflow를 바꾸면 항상 다른 문제가 따라온다. 오늘도 그랬다.

첫 번째 문제는 Role 2.1이 정규화 입력을 직접 보면서 공손 표현까지 같이 들어온 것이다.

```text
Please create a file
Can you please test it
```

압축 결과를 쓸 때는 이런 표현이 줄어들 수 있었지만, 정규화 입력을 직접 쓰면 그대로 남는다. 그래서 polite prefix 제거가 필요해졌다.

두 번째 문제는 splitter 책임이 커질 뻔한 것이다. 처음에는 담화 표현 제거도 splitter에 넣을 수 있어 보였다. 하지만 그렇게 하면 splitter가 문장 경계 분리와 의미 판단을 모두 떠안게 된다. 번역 표현이 하나 늘 때마다 splitter 예외 규칙이 계속 늘어났을 것이다.

그래서 `TaskCandidateExtractor`를 분리했다.

```text
TaskSentenceSplitter:
  문장과 절을 나눈다

TaskCandidateExtractor:
  실행 가능한 task 후보만 남긴다

TaskGraphProcessor:
  type과 dependency를 만든다
```

세 번째 문제는 실제 LLM 번역 결과가 테스트 문장보다 더 지저분하다는 점이었다. 사람 손으로 만든 테스트는 보통 깔끔하다. 하지만 live llama 번역 결과는 같은 뜻이어도 표현이 달라진다.

```text
테스트에서 예상한 표현:
fix duplicate validation bug

실제 번역에서 나온 표현:
because ..., so fix that defect
```

이 차이 때문에 cause context가 잘릴 수 있었다. 그래서 실제 LLM 출력 파일을 기준으로 다시 테스트를 추가했다.

네 번째 문제는 단어 하나만으로 type을 정하기 어렵다는 점이었다.

```text
trace
organize
```

이 단어들은 문맥에 따라 type이 달라진다. 그래서 단어 하나가 아니라 주변 단어와 목적어를 같이 봐야 했다.

```text
trace + explain + order/passes through -> analyze
organize + changes/results + work note -> document
```

이런 보정은 규칙 기반 로직의 한계이기도 하다. 하지만 지금 단계에서는 deterministic rule이 필요했다. LLM에게 매번 type 판단을 맡기면 재현성이 떨어지고, 테스트로 고정하기도 어렵다.

## 검증은 실제 경로까지 같이 봤다

오늘은 단위 테스트만 보지 않았다. 실제 WSL Ubuntu에서 llama + Kompress 경로까지 돌려봤다.

검증한 내용은 아래와 같다.

```text
Role 2.1이 normalized input을 쓰는가?
task candidate가 정확히 5개인가?
type 흐름이 explore -> analyze -> modify -> validate -> document인가?
dependency가 t1 -> t2 -> t3 -> t4 -> t5인가?
Codex adapter가 codex exec - 로 호출되는가?
실패 stderr가 rawOutput에 남는가?
```

PR별 테스트도 같이 정리했다.

```text
#153
adapter-path.test.ts
real-path.test.ts
2 files, 7 tests passed

#158
TaskSentenceSplitter.test.ts
task-graph 전체
7 files, 865 tests passed

#159
compiler / orchestrator / batch
task-graph 전체
888개 수준의 관련 테스트 통과

#169
TaskCandidateExtractor / TaskSentenceSplitter / orchestrator
task-graph 전체
7 files, 870 tests passed

#171
TaskCandidateExtractor / TaskGraphProcessor
task-graph 전체
7 files, 874 tests passed
```

실제 llama 실행에서는 이런 값도 확인했다.

```text
language: ko
role2UsesNormalizedBeforeCompression: true
candidateSentenceCountIsFive: true
typeFlowMatchesExpected: true
dependencyFlowMatchesExpected: true
```

이 결과가 중요했던 이유는, 오늘 고친 문제가 단순히 한두 개 유닛 테스트에서만 보이는 문제가 아니었기 때문이다. 실제 한국어 프롬프트가 번역되고, 정규화되고, Role 2.1로 넘어가고, graph로 만들어지는 전체 경로에서 맞아야 했다.

## 오늘 작업을 하나의 흐름으로 보면

오늘 작업은 처음에는 execution adapter 문제에서 시작했다. real mode가 제대로 실행되어야 실제 CLI라고 할 수 있기 때문이다.

그다음 실제 실행을 돌리다 보니 문장 분리 문제가 보였다.

```text
4 + 5. 를 번호 목록으로 오인함
First, Next, Finally를 task로 오인함
```

문장 분리를 고치고 나니 Role 2.1이 어떤 입력을 봐야 하는지가 문제로 올라왔다.

```text
compressed prompt를 볼 것인가?
normalized input을 볼 것인가?
```

여기서 Role 2.1은 압축 결과가 아니라 정규화 입력을 보는 쪽으로 바꿨다. 그리고 그 결과, 정규화 입력에 남아 있는 담화 표현과 메타 지시문을 걸러야 했다.

그래서 `TaskCandidateExtractor`가 들어갔다.

마지막으로 실제 LLM 번역 결과를 보니, 테스트 문장에서는 보이지 않던 표현들이 나왔다.

```text
organize it in order when you have time
because ..., so fix that defect
trace and explain ... passes through
organize ... work note
```

이 표현들을 기준으로 cause context 보존, meta sentence 제거, analyze/document 분류 보정을 추가했다.

즉, 오늘의 흐름은 이렇게 정리할 수 있다.

```text
실제 실행을 붙인다
  ↓
실제 프롬프트를 돌린다
  ↓
문장 분리 문제가 보인다
  ↓
Role 2.1 입력 기준을 바꾼다
  ↓
실행 가능한 task 후보만 추출한다
  ↓
실제 LLM 번역 표현에 맞춰 보강한다
```

## 마무리

오늘은 DeToks의 workflow가 꽤 많이 바뀐 날이었다. 특히 `사용자 입력 -> 압축 -> Role 2.1`로 이어지던 흐름을 그대로 두지 않고, Role 2.1에는 정규화 입력을 넘기도록 바꾼 것이 컸다.

이유는 분명했다. 압축은 토큰을 줄이는 데 좋지만, task graph를 만들 때 필요한 작업 경계와 원인 맥락까지 줄일 수 있다. Role 2.1은 짧은 문장을 읽는 역할이 아니라, 사용자가 실제로 시킨 일을 정확히 나누는 역할이다. 그래서 정보가 덜 손실된 입력을 보는 쪽이 맞았다.

대신 그만큼 처리해야 할 말도 많아졌다. 공손 표현, 담화 표현, "나중에 해도 된다" 같은 메타 문장, "불필요한 말은 줄여도 된다" 같은 지시문이 그대로 들어왔다. 이 문제를 splitter에 억지로 넣지 않고 `TaskCandidateExtractor`로 분리한 것이 오늘 작업의 핵심이었다.

실제 llama 번역 결과를 보면서는 테스트 문장만으로는 부족하다는 것도 다시 확인했다. 같은 의미라도 LLM은 `fix that defect`, `organize it in order`, `trace and explain`처럼 다른 표현을 만든다. 이 표현 하나 때문에 task 개수가 늘거나, 원인 맥락이 사라지거나, type이 틀어질 수 있었다.

오늘 작업을 지나면서 DeToks는 "프롬프트를 대충 나눠서 실행하는 도구"에서 조금 더 멀어졌다. 이제는 사용자의 장황한 말에서 실행할 일만 골라내고, 그 작업의 순서와 맥락을 보존한 뒤, 실제 adapter로 넘기는 구조에 가까워졌다. 아직 규칙 기반 보정이 계속 늘어나는 부담은 있지만, 적어도 오늘은 어디에서 정보를 줄이고, 어디에서 정보를 보존해야 하는지 기준이 더 분명해졌다.
