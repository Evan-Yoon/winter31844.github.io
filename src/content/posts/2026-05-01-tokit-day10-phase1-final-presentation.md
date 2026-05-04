---
title: "[DeToks] Day 10 - Phase 1 최종 발표와 MVP 정리"
slug: tokit-day10-phase1-final-presentation
date: 2026-05-01
author: Evan Yoon
category: project
subcategory: team-project
description: "5월 1일은 Tokit 프로젝트 Phase 1 최종 발표일이었다. DeToks MVP가 왜 필요했는지, Prompt Compiler, Task Graph, Context Manager, CLI Wrapper가 어떻게 연결됐는지 발표자료를 기준으로 정리했다."
thumbnail: /images/posts/tokit-day10-final-presentation/slide-01.png
tags:
  - tokit
  - detoks
  - final-presentation
  - mvp
  - prompt-compiler
  - task-graph
  - context-manager
  - cli
  - team-project
readTime: 13
series: "Tokit"
seriesOrder: 14
featured: false
draft: false
toc: true
---

5월 1일은 Tokit 프로젝트의 Phase 1 최종 발표일이었다. 전체 일정은 Phase 1과 Phase 2로 나뉘어 있었고, 오늘은 그중 **Phase 1: Core Flow**를 어디까지 만들었는지 보여주는 자리였다.

발표용 이름은 이제 완전히 `DeToks`로 정리했다. 처음엔 Tokit이라는 이름으로 시작했지만, 프로젝트의 핵심은 결국 "토큰을 덜 쓰게 만들고, 그 대신 실행 흐름을 더 잘 통제하는 것"이었다. 그래서 발표 첫 장도 이 문장으로 시작했다.

![DeToks Phase 1 MVP 표지](/images/posts/tokit-day10-final-presentation/slide-01.png)

> Less Token, More Control.

발표 전에 넣은 영상은 DeToks가 왜 필요한지 분위기를 잡는 역할이었다.

https://youtu.be/vae16MKcBQA

이날 발표를 준비하면서 느낀 건, 기술을 많이 넣었다고 해서 발표가 자동으로 좋아지지는 않는다는 점이었다. 오히려 우리는 반대로 가야 했다. "이 기능을 만들었다"보다 "왜 이 구조가 필요했고, 어디까지 검증했는지"가 먼저 보여야 했다.

![인트로 슬라이드](/images/posts/tokit-day10-final-presentation/slide-02.png)

## 발표 흐름

목차는 크게 여섯 개로 잡았다.

![목차 슬라이드](/images/posts/tokit-day10-final-presentation/slide-03.png)

프로젝트 배경에서 문제를 잡고, 프로젝트 개요에서 MVP 범위를 설명한 다음, 아키텍처와 구현 상세로 들어갔다. 마지막에는 결과와 Phase 2 계획까지 정리했다.

![프로젝트 배경 섹션](/images/posts/tokit-day10-final-presentation/slide-04.png)

이번 발표에서 계속 붙잡고 간 질문은 하나였다.

```text
AI 코딩 도구를 많이 쓰게 될수록,
개발자는 왜 더 많은 토큰과 더 많은 프롬프트 관리 비용을 감당하게 되는가?
```

## 문제는 이미 커지고 있었다

발표에서는 먼저 AI 코딩 도구 사용량이 이미 충분히 커졌다는 점을 보여줬다. GitHub Copilot 누적 사용자는 2,000만 명, Codex CLI 주간 활성 사용자는 300만 명으로 정리했고, AI 도구를 매일 쓰는 개발팀 비율도 73%로 잡았다. Claude Code npm 월 다운로드 수는 4,630만 건까지 올라와 있었다.

![AI 코딩 도구 활용 현황과 토큰 증가 추세](/images/posts/tokit-day10-final-presentation/slide-05.png)

이 숫자들이 중요한 이유는 단순히 "AI가 유행이다"를 말하려는 게 아니었다. AI 코딩 도구를 자주 쓰면 쓸수록 입력 프롬프트, 이전 대화 맥락, 코드 설명, 실행 결과가 계속 쌓인다. 결국 토큰 비용도 늘고, 응답 지연도 늘고, 모델이 봐야 할 맥락도 점점 지저분해진다.

기존 해결책은 대체로 세 가지였다.

- 입력을 최대한 짧게 줄인다
- 세션을 새로 열거나 사람이 수동으로 요약한다
- 템플릿이나 체크리스트로 프롬프트를 관리한다

![기존 해결책과 한계](/images/posts/tokit-day10-final-presentation/slide-06.png)

그런데 셋 다 한계가 분명했다. 입력을 너무 줄이면 조건과 제약, 의존성이 빠진다. 세션을 새로 열면 이전 결정과 진행 상태가 끊긴다. 템플릿은 단일 요청에는 도움이 되지만, 여러 작업의 순서와 병렬 여부, 상태 전달까지 자동으로 관리하지는 못한다.

DeToks는 이 지점에서 시작했다. 프롬프트를 그냥 짧게 만드는 도구가 아니라, **입력을 컴파일하고, 작업 그래프를 만들고, 필요한 컨텍스트만 붙여서 실행하는 CLI Wrapper**로 잡았다.

![프로젝트 개요 섹션](/images/posts/tokit-day10-final-presentation/slide-07.png)

## Phase 1에서 만든 범위

Phase 1의 목표는 Core Flow였다. 입력을 받고, 영어 명령문으로 안정화하고, 작업을 분해하고, 필요한 컨텍스트만 붙이고, Codex나 Gemini로 실행하고, 결과를 저장하는 흐름이다.

![MVP와 Phase 1 구현 범위](/images/posts/tokit-day10-final-presentation/slide-08.png)

MVP는 네 덩어리로 설명했다.

```text
Prompt Compiler
  한국어/혼합 입력을 영어 명령문으로 안정화하고 압축

Task Graph
  작업 분해, type 분류, depends_on 기반 순서 고정

Context Manager
  필요한 세션 상태와 핵심 맥락만 선택·압축

CLI Wrapper
  Codex/Claude/Gemini adapter와 subprocess 실행 경계 관리
```

Phase 1은 `Input -> Compile -> TaskGraph -> Context -> Execute -> Store`까지를 만드는 단계였다. Phase 2는 Claude 확장, Web Metrics, `@` 기능, CLI UX 개선, Dashboard 쪽으로 잡았다.

프로젝트 관리는 문서와 스키마 중심으로 했다. API Spec, Architecture, Schemas, Guidelines, Dependency Workflow를 기준으로 맞췄고, 모델은 의미 해석에 쓰되 실행과 상태, 계약은 코드가 통제하게 두는 방향이었다.

![프로젝트 관리와 협업 방식](/images/posts/tokit-day10-final-presentation/slide-09.png)

## 팀 역할

팀 역할도 발표에서 분명히 나눴다.

![팀 역할 구조](/images/posts/tokit-day10-final-presentation/slide-10.png)

시호님은 한국어를 영어로 번역하고 압축하는 파이프라인을 맡았다. llama.cpp 기반 로컬 LLM 자동 실행과 설정, Kompress 도입, identifier 보존 guardrail, verify-role1 기준 토큰 절감률과 품질 검증이 핵심이었다.

나는 TaskGraphProcessor와 DAGValidator를 맡았다. 8개 task type 분류, `FLOWS_TO` 의존성 규칙, 한국어 프롬프트 회귀 테스트, normalized input handoff, Task Type Persistence를 정리했다.

지호님은 State & Context Engine 쪽이었다. 세션 영속성, Two-Tier 압축 구조, 동적 Compression Threshold, Sliding Window, Task Type Persistence 저장 파이프라인, 토큰 벤치마크와 AI 결과 품질 검사기를 맡았다.

규철님은 CLI와 REPL UX를 정리했다. slash command, adapter, checkpoint UX, Codex/Gemini 실제 실행 smoke test, spinner와 source badge까지 사용자 경험 쪽을 많이 다듬었다.

## DeToks가 끼어드는 위치

아키텍처 설명에서는 기존 LLM 호출 방식과 DeToks 방식을 비교했다.

![서비스 아키텍처 섹션](/images/posts/tokit-day10-final-presentation/slide-11.png)

기존 방식은 사용자 입력을 거의 그대로 LLM API에 전달한다. 반복 설명이나 긴 맥락, 이미 끝난 작업 정보까지 그대로 들어가기 쉽다. 이러면 과금이 늘고, 응답 품질이 흔들리고, 지연 시간도 늘어난다.

![기존 LLM 호출 방식과 DeToks 개입 방식](/images/posts/tokit-day10-final-presentation/slide-12.png)

DeToks 방식은 중간에 Wrapper가 들어간다. 사용자 입력을 바로 던지지 않고, 먼저 토큰을 압축하고 정규화한다. 그 다음 Compile / Graph 단계에서 구조화하고, LLM에는 최적화된 입력만 전달한다.

흐름을 조금 더 풀면 세 단계다.

![End-to-End 서비스 아키텍처](/images/posts/tokit-day10-final-presentation/slide-13.png)

첫 번째는 준비 및 설계다. 사용자의 모호한 한글 입력을 AI가 이해하기 좋고 토큰을 적게 쓰는 형태로 컴파일한다. 이후 작업 간 의존성을 분석해서 실행 순서, 즉 DAG를 만든다.

두 번째는 실행 루프다. 이미 완료된 작업은 건너뛰고, 현재 task에 꼭 필요한 context만 붙여서 LLM을 호출한다.

세 번째는 결과 및 영속화다. 실행 결과를 검증하고 세션에 저장한다. 중간에 작업이 끊기더라도 이어서 재개할 수 있어야 하기 때문이다.

데이터 흐름은 다섯 단계로 정리했다.

![데이터 흐름](/images/posts/tokit-day10-final-presentation/slide-14.png)

```text
1. 입력 컴파일
2. Task Graph 생성
3. 컨텍스트 조립
4. 실행 및 검증
5. 저장 및 루프
```

이 구조가 Phase 1의 뼈대였다.

![구현 상세 섹션](/images/posts/tokit-day10-final-presentation/slide-15.png)

## Prompt Compiler

Prompt Compiler에서는 네 가지 기준을 잡았다. 정확성, 명확성, 응답성, 비용 효율성이다.

![Prompt Compiler 섹션](/images/posts/tokit-day10-final-presentation/slide-16.png)

![프롬프트 컴파일 주요 고려사항](/images/posts/tokit-day10-final-presentation/slide-17.png)

처음에는 번역 방법부터 비교했다. 기계 번역 API, 클라우드 API, 소형 번역 모델, 로컬 LLM을 놓고 정확도, 응답 속도, 비용을 봤다.

![번역 방법 선택 1](/images/posts/tokit-day10-final-presentation/slide-18.png)

![번역 방법 선택 2](/images/posts/tokit-day10-final-presentation/slide-19.png)

![번역 방법 선택 3](/images/posts/tokit-day10-final-presentation/slide-20.png)

![번역 방법 선택 4](/images/posts/tokit-day10-final-presentation/slide-21.png)

비용만 보면 로컬이나 소형 모델이 좋아 보인다. 하지만 정확도가 흔들리면 오히려 뒤에서 재질문과 수정 요청이 늘어난다. 그래서 단순히 싼 모델을 고르는 문제가 아니었다.

![비용 효율성 확보](/images/posts/tokit-day10-final-presentation/slide-22.png)

대표적인 실패 사례가 `MySQL`이었다.

![수수께끼와 MySQL 1](/images/posts/tokit-day10-final-presentation/slide-23.png)

![수수께끼와 MySQL 2](/images/posts/tokit-day10-final-presentation/slide-24.png)

![수수께끼와 MySQL 3](/images/posts/tokit-day10-final-presentation/slide-25.png)

`기존 MySQL 기반의 인증 시스템을 PostgreSQL로 마이그레이션하려고 해`라는 문장에서 일부 소형 번역 모델은 MySQL을 mystery, mystique 같은 단어로 망가뜨렸다. 심지어 migrate도 mingrade, minorgrade, degrade처럼 틀어졌다.

![소형 번역 모델 정확도 실패 사례](/images/posts/tokit-day10-final-presentation/slide-26.png)

벤치마킹 결과도 같이 봤다. `quickmt/quickmt-ko-en`은 성공률 99.236%, 추론 시간 0.413초로 좋아 보였지만, 도메인 특화 언어가 왜곡되는 문제가 있었다. 커스터마이징이 어렵다는 점도 걸렸다.

![소형 번역 모델 벤치마킹 1](/images/posts/tokit-day10-final-presentation/slide-27.png)

![소형 번역 모델 벤치마킹 2](/images/posts/tokit-day10-final-presentation/slide-28.png)

로컬 LLM도 봤다.

![로컬 LLM 검토 1](/images/posts/tokit-day10-final-presentation/slide-29.png)

Qwen3.5 0.8B Q8처럼 작은 모델은 빠르고 가볍지만, 파라미터 수가 적어서 추론 성능이 낮게 나오는 경우가 있었다.

![로컬 LLM 검토 2](/images/posts/tokit-day10-final-presentation/slide-30.png)

그래서 보완 파이프라인을 만들었다.

![프롬프트 컴파일 보완 파이프라인](/images/posts/tokit-day10-final-presentation/slide-31.png)

전처리 단계에서는 보존 단어를 추출하고 placeholder로 치환한다. 검증 단계에서는 placeholder 개수와 순서, 금지 패턴, 길이 비율, 잔존 한글을 확인한다. 복원 단계에서는 placeholder를 되돌리고 문장을 정리한다. 실패하면 fallback prompt로 넘긴다.

예시 문장도 발표에 넣었다.

![보완 파이프라인 예시 문장](/images/posts/tokit-day10-final-presentation/slide-32.png)

![보완 파이프라인 예시 마스킹](/images/posts/tokit-day10-final-presentation/slide-33.png)

![보완 파이프라인 예시 결과](/images/posts/tokit-day10-final-presentation/slide-34.png)

이 방식으로 `MySQL`, `PostgreSQL` 같은 identifier를 살린 채 번역할 수 있었다.

로컬 LLM 벤치마킹에서는 `supergemma-4-e4b`가 1047개 샘플에서 성공률 100%, 추론 시간 2.008초, 토큰 감소율 21.174%를 기록했다. `gemma-4-e2b`도 성공률 99.904%, 추론 시간 1.017초, 토큰 감소율 21.177%로 꽤 안정적이었다.

![로컬 LLM 벤치마킹 1](/images/posts/tokit-day10-final-presentation/slide-35.png)

![로컬 LLM 벤치마킹 2](/images/posts/tokit-day10-final-presentation/slide-36.png)

정확성과 비용을 본 다음에는 명확성과 응답성도 봤다.

![정확성, 응답성 확보](/images/posts/tokit-day10-final-presentation/slide-37.png)

장황한 한국어 프롬프트를 영어로 번역하면 토큰 수가 줄기도 하지만, 문장이 여전히 길게 남을 때가 많다. Redis 캐싱 전략 예시에서는 원문 128 토큰이 번역 후 72 토큰으로 줄었다.

![장황한 프롬프트 번역 예시](/images/posts/tokit-day10-final-presentation/slide-38.png)

압축 모델도 비교했다. `kompress-base`는 106개 샘플에서 성공률 100%, 추론 시간 0.042초, 토큰 감소율 8.5%로 균형이 좋았다.

![압축 모델 벤치마킹](/images/posts/tokit-day10-final-presentation/slide-39.png)

같은 Redis 예시는 압축 후 51 토큰까지 줄었다. 번역 후 72 토큰에서 다시 29.92% 정도 줄어든 셈이다.

![압축 결과](/images/posts/tokit-day10-final-presentation/slide-40.png)

결과적으로 Prompt Compiler는 번역과 압축을 거쳐 평균 토큰 절감률 약 30%를 달성했다.

![프롬프트 컴파일 달성 결과](/images/posts/tokit-day10-final-presentation/slide-41.png)

이 파트의 Trouble Shooting은 꽤 현실적이었다. 특정 한글이 코드로 인식되어 마스킹되거나, 경량 LLM이 프롬프트에서 이탈하거나, llama-server가 `.env`를 읽지 않거나, 코드 단위 프롬프트 압축률이 낮게 나오는 문제가 있었다.

해결은 하나씩 했다. 한글은 마스킹 대상에서 제외할 조건을 조정했고, 0.8B에서 4B로 모델 체급을 올렸고, 실행마다 `.env`를 다시 로딩하게 했고, 앱 종료 시 llama-server도 같이 종료하게 했다. 진행률이 없어 답답했던 검증 과정은 `(current/total)` 형식으로 바꿨다.

최종적으로 동일 모델 기준 번역 성공률은 개선 전 92%에서 개선 후 99% 이상으로 올라갔고, 1047개 데이터 테스트까지 진행했다.

## Task Graph

내가 맡은 Task Graph 파트는 발표자료에 43번부터 들어갔다. 아쉽게도 42번 이후 슬라이드는 PPT 내부 미디어 때문에 이미지 export가 안정적으로 끝까지 되지는 않았지만, 내용은 PPTX 내부 텍스트에서 모두 확인했다.

전체 전처리 플로우는 다음 순서였다.

```text
1. Filler vs Task
2. 문장 분리
3. 작업 순서 파악
4. 숙어 여부 파악
5. Task 의도 파악
6. Dependency 결정
7. DAG 검증·정렬
8. Compact 실행
```

핵심은 "똑똑한 추론"보다 **반복 가능한 실행 계획**이었다. LLM이 매번 느낌으로 작업 순서를 정하게 두는 게 아니라, 규칙 기반으로 task type을 분류하고, `FLOWS_TO`로 의존성을 붙이고, DAG 검증을 통과한 그래프만 실행 단계로 넘기는 구조다.

Filler 판단에서는 `not urgent`, `when possible`, `reduce unnecessary explanations` 같은 공손 표현이나 메타 지시는 버리고, 실제 실행문만 남겼다. 예를 들어 결제/구독 코드 위치 찾기, 결제 버튼 이후 데이터 흐름 분석, 프리미엄 검증 중복 로직 공통화, 결제 플로우와 테스트 검증, 변경 이유와 결과 문서화는 KEEP으로 남겼다.

Task type은 8개로 정리했다.

```text
explore   탐색
analyze   분석
create    생성
modify    수정
validate  검증
execute   실행
document  문서화
plan      계획
```

처음에는 CUPS 기반 6개 태그로 시작했다. `create`, `modify`, `analyze`, `explore`, `validate`, `document`가 기본이었다. 여기에 DeToks에서는 `execute`와 `plan`을 추가했다. 이유는 간단했다. 개발자가 실제로 LLM에게 요청하는 말에는 "코드를 실행해줘"와 "계획을 세워줘"가 자주 나오는데, 이 둘을 기존 태그에 억지로 넣으면 실행 의도가 흐려졌다.

의존성은 `FLOWS_TO`로 잡았다. 이전 type에서 현재 type으로 자연스럽게 이어지면 순차 의존성을 붙이고, 아니면 병렬 후보로 둔다.

```text
depends_on = FLOWS_TO[prev].includes(curr) ? [prevId] : []
```

검증은 DAGValidator가 맡았다. `UNKNOWN_DEPENDENCY`, `CYCLE_DETECTED`, `DISCONNECTED_NODE` 같은 문제를 실행 전에 차단했다. 여기서 위상정렬을 쓴 이유도 분명했다. Priority 숫자만으로는 `depends_on`, cycle, 병렬 stage를 제대로 표현하기 어렵고, event-driven 방식은 앞 작업이 이미 실행된 뒤에야 순환이나 없는 의존성을 발견할 수 있다.

Trouble Shooting도 꽤 많았다.

`Make Sure`가 `Create`로 분류되는 문제는 `make` 단일 키워드가 먼저 잡혀서 생겼다. 그래서 `IDIOM_PATTERNS`를 `TYPE_PATTERNS`보다 먼저 검사하게 해서 `make sure -> validate`로 고정했다.

`4 + 5.`가 번호 목록처럼 잘리는 문제도 있었다. 숫자+마침표 split 규칙이 너무 넓어서 생긴 문제라 산술 표현 보존 조건을 추가했다.

`First`, `Next`, `Finally`가 task처럼 남는 문제는 순서 표식을 action clause와 결합하고, 단독 후보는 제거하는 방식으로 풀었다.

`document` 뒤 후속 작업이 끊기는 문제도 있었다. document를 terminal task로만 가정하면 뒤에 명시적인 작업이 있어도 고립 노드가 생긴다. 그래서 document 이후에도 명시 작업이 있으면 흐름을 연결하게 바꿨다.

50회 이상 복합 테스트와 106개 말뭉치 검증을 반복했고, Type 분류 성공률은 품질 개선 전 70%에서 개선 후 82%까지 올라갔다. 1000회 프롬프트 테스트도 진행했다.

## Context Manager

State & Context Engine은 실행 결과를 세션에 저장하고, 다음 요청에 필요한 맥락만 골라 전달하는 역할이었다.

핵심 작업은 세 가지였다.

```text
TaskGraph 실행 결과를 세션에 저장·압축
다음 요청에 필요한 맥락만 선택해 전달
AI가 같은 말을 반복하지 않도록 상태 유지
```

산출물은 `SessionState`, `SharedContext`, `TaskContext`로 잡았다. 연결 원칙은 `TaskGraph 실행 후 -> Normalize -> Save -> Compress`였다. 불필요한 토큰을 다시 보내지 않고, 다음 단계에 최소 정보만 전달하기 위한 흐름이다.

저장 파이프라인은 이렇게 정리했다.

```text
TaskGraph
-> Execute raw result
-> Normalize summary/type
-> Save session json
-> Compress context budget
```

여기서 `ContextCompressor`는 adapter context window 기반으로 동적 임계값을 계산했다. Gemini / Claude는 485K, Haiku는 85K, Codex는 185K로 잡고, 최근 3개 task만 상세 유지하는 방식이었다. 미등록 adapter는 Gemini 기준으로 fallback했다.

문제도 분명했다. 처음에는 고정 3K 기준이라 압축이 사실상 잘 발동하지 않았다. 저장하면 `type/success` 같은 핵심 정보가 사라져 세션 복구 시 성공/실패나 재처리 여부를 판단하기 어려웠다. 또 lock 없이 동시 write가 일어나면 REPL에서 같은 세션 파일을 덮어쓸 수 있었다.

개선 후에는 모델별 context window와 최근 3개 task 유지 방식으로 압축률이 0.3%에서 43%까지 올라갔다. 핵심 정보는 Zod Schema로 보존했고, `failed_task_ids`도 저장했다. 동시 저장 문제는 `acquireLock`과 `tmp -> rename` atomic write로 막았다.

## CLI Wrapper와 시연

CLI Wrapper 파트에서는 실제로 사용자가 보는 경험을 보여주는 게 중요했다.

시연 영상은 발표 중간에 넣었다.

https://youtu.be/5YBcDGlNG4I

CLI는 `detoks`로 REPL에 진입한 뒤 slash command를 쓰는 방식이다.

기본 명령어는 `/help`, `/clear`, `/model`, `/adapter`, `/mode`, `/verbose`, `/exit`이고, 인증 후에는 `/cms`, `/gms`, `/logout`까지 쓸 수 있게 했다. 총 11개 명령어다.

실행 피드백도 신경 썼다. ASCII spinner로 실행 중 상태를 보여주고, 파이프라인 단계별로 시작, 완료, 실패, 건너뜀을 표시한다. 종료 후에는 토큰 절감, 파이프라인 상태, 실행 결과를 요약한다. 실패하면 `stderr`와 exit code 1을 표면화하고, 사용자에게는 한국어 요약으로 보여준다.

Codex adapter는 Codex CLI subprocess, Gemini adapter는 Gemini API subprocess 경계에서 처리했다. 중요한 건 실패를 조용히 삼키지 않는 것이었다. CLI 도구는 멋진 성공 화면보다 실패했을 때 어디서 깨졌는지를 알려주는 쪽이 더 중요하다.

## 결과

성능 평가는 동일한 기준 HTML/CSS에서 시작했다. HTML/CSS는 원칙적으로 수정하지 않고 JS만 추가했다. 15턴 프롬프트 세트를 사용했고, 단일 명령과 복합 명령을 섞었다. 일반 프롬프트 버전과 DeToks 변환 버전을 각각 생성해서 비교했다.

비교 지표는 네 가지였다.

```text
입력 토큰 수 감소율
최종 UI 동일성: CSS, DOM 구조, 화면 캡처
기능 동일성: 추가, 삭제, 체크, 전체삭제, localStorage, 토글, 카운트
세션 유지: 이전 턴 기능 누락 여부, 이벤트 중복 등록 여부
```

결과는 꽤 선명했다.

```text
토큰 절감률: 34.8%
평균 추론 시간: 0.891초
기능 품질 유지: 106/106
validation 실패: 0건
```

DeToks 미사용 시 3,784 tokens였고, 사용 시 2,424 tokens였다. 번역 절감은 30.243%, 압축 절감은 6.507%로 정리했다. compression fallback은 2건이었다.

내 기준에서 이 결과가 의미 있었던 이유는 단순히 토큰을 줄였기 때문이 아니다. 기능 품질을 유지한 상태에서 줄였기 때문이다. 토큰 절감만 보려면 필요한 조건까지 지워버리면 된다. 하지만 그건 개발 도구로는 실패다. DeToks가 보여줘야 했던 건 "줄였는데도 맥락과 실행 품질이 유지된다"는 점이었다.

## Phase 2로 넘긴 것

향후 계획은 2주 단위로 잡았다.

Week 1은 Adapter & Evaluation이다. Claude adapter를 확장하고, 품질 저하 fallback 기준을 정의하는 쪽이다.

Week 2는 Dashboard & Packaging이다. README, 실행 가이드, 발표 시연 정리, 최종 demo packaging, 토큰 절감/성능 지표 확인 웹페이지가 들어간다.

파일럿 파인튜닝도 계획에 넣었다. 1400개 dataset 기준으로 best validation checkpoint 258에서 accuracy 0.9813, precision 0.9837, recall 0.9813, F1 Score 0.9815를 기록했다. Hard-eval test에서도 accuracy 0.9625, precision 0.9666, recall 0.9625, F1 Score 0.9629로 정리했다.

마무리 영상은 발표의 마지막 느낌을 정리하는 역할이었다.

https://youtu.be/dac5SwHTCpg

마지막 슬라이드 문장은 이거였다.

```text
토큰은 줄이고, 줄인 만큼 더 많은 작업을.
```

Phase 1을 끝내고 보니, DeToks는 단순한 프롬프트 압축기가 아니었다. 입력을 줄이는 일은 시작일 뿐이고, 진짜 핵심은 줄인 다음에도 작업 순서, 의존성, 필요한 맥락, 실행 결과를 계속 통제하는 데 있었다.

이번 발표가 Phase 1의 종료라면, Phase 2는 이 구조를 더 실제 제품처럼 만드는 단계가 될 것 같다. Claude adapter, dashboard, packaging, 평가 기준까지 붙이면 "발표용 MVP"에서 "반복해서 쓸 수 있는 도구"에 조금 더 가까워질 수 있다.

