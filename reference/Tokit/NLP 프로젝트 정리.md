# NLP 프로젝트 정리

# 🎯 목표

1. 입력 토큰 감소
2. 출력 토큰 감소
3. 성능 유지 또는 향상
4. Codex CLI / Gemini CLI와 완전 호환

# 🧠 핵심 전략 (한 줄 요약)

> **프롬프트를 번역하는 게 아니라 “압축 + 재작성 + 출력 제어”한다**
> 

프로젝트의 정의

- 입력 프롬프트를 재구성하여 모델의 출력 길이를 제어하고 총 토큰 비용을 최적화한다
- skill.md는 사람이 출력 길이를 제어하는 방법이고 이 프로젝트는 그걸 자동으로 수행하는 시스템이다

# 🏗️ 전체 아키텍처

```
사용자 입력 (한국어)
→ 코드 전처리
→ 모델 (영어 최적 프롬프트 생성)
→ 코드 후처리
→ Codex / Gemini CLI
→ 출력 (제어된 길이)
```

# ⚙️ 역할 분담

## 🧩 1. 코드 로직 (빠르고 안정적인 처리)

### 담당

- slash command pass-through (`/`, `@`, `!`)
- 코드블록 / 파일경로 / 에러 보호
- 공백 및 형식 정리
- 짧은 입력 필터링
- 토큰 계산
- 캐싱
- diff/log 출력

👉 역할: **안전성 + 속도 + 호환성**

## 🤖 2. 모델 (핵심 지능)

### 반드시 해야 할 3가지

### 1) 의미 파악

- 사용자의 실제 작업 의도 추출

### 2) 영어 변환

- 항상 영어로 변환

### 3) 프롬프트 최적화

- 명령형
- 짧고 명확하게
- 불필요한 표현 제거
- 출력 길이 제어 포함

👉 역할: **프롬프트 컴파일러**

# 🔥 핵심 차별점 (중요)

❌ 단순 번역:

```
Please explain this code in detail...
```

✅ 최적화:

```
Explain this code briefly and identify the main issue.
```

👉 차이:

- 길이 감소
- 출력 토큰 감소
- 응답 품질 유지

# 📉 토큰 최적화 전략

## 1. 입력 토큰 절감

방법:

- 군더더기 제거
- 명령형 변환
- 중복 제거
- 영어 압축

효과:

👉 **확실하게 감소 (핵심 성과)**

## 2. 출력 토큰 절감

방법:

- 답변 형식 강제
- 범위 제한
- 불필요한 설명 제거

예:

- `in 3 bullet points`
- `keep it concise`
- `only root cause and fix`

효과:

👉 **조건부 감소 (설계에 따라 달라짐)**

# 🧠 작업 유형별 최적화 정책

이게 프로젝트에서 가장 중요한 부분이다.

## 🐞 Debug (버그/에러)

```
Identify the root cause and suggest a fix.
```

👉 출력 최소화 가능 (효과 큼)

---

## ⚡ Performance (성능)

```
Identify the main bottleneck and suggest up to 3 improvements.
```

👉 출력 제한 가능

---

## 🔍 Review (코드 리뷰)

```
List up to 3 critical issues in this code.
```

👉 출력 길이 강하게 제어 가능

---

## 🔧 Refactor

```
Suggest concise refactoring improvements.
```

---

## 📖 Explain (설명)

```
Explain briefly.
```

⚠️ 필요 시:

```
Explain in detail.
```

👉 여기만 선택적으로 길어질 수 있음

# ⚠️ 중요한 설계 원칙

## 1. 항상 개입 (always-on)

- 모든 프롬프트 처리

---

## 2. 명령어는 절대 건드리지 않음

- `/`
- `@`
- `!`

👉 기존 CLI 100% 유지

---

## 3. 출력은 항상 짧게 (기본값)

- verbose는 opt-in

---

## 4. 모델은 “번역기”가 아니라 “컴파일러”

- 의미 보존
- 압축
- 재작성

---

## 5. 전체 비용 기준으로 평가

```
총 비용 =
(최적화 모델 비용 + 메인 모델 비용)
```

👉 이것이 줄어야 성공

# 📊 기대 효과

## 입력 토큰

✅ 확실히 감소

## 출력 토큰

⚠️ 작업별로 감소

## 총 비용

✅ 잘 설계하면 감소

## 성능

✅ 유지 또는 향상 가능

# 🧪 성능 리스크

## 1. 의미 손실

→ 조건 누락 위험

## 2. 지연 증가

→ 매 프롬프트 모델 호출

## 3. 짧은 입력에서 비효율

→ 오히려 손해 가능

# 🛠️ 해결 전략

- 출력 길이 제한
- 캐싱
- 빠른 모델 사용
- fallback (원문 유지 옵션)

# 🎯 최종 포지셔닝

이 프로젝트는 이렇게 말하는 게 가장 강하다:

> **LLM CLI를 위한 프롬프트 컴파일러로서, 입력과 출력 토큰을 동시에 최적화하는 시스템**
> 

# 🧠 한 줄 핵심 요약

> **우리는 한국어를 영어로 번역하는 것이 아니라, 개발자 프롬프트를 더 짧고 명확한 영어 명령으로 컴파일해 전체 토큰 비용을 줄인다.**
> 

base system prompt <<

You are a prompt compiler for developer-focused LLM workflows.

Your job is to convert a Korean user prompt into a compact, precise English prompt for coding-oriented LLM CLIs.

Follow these rules strictly:

1. Preserve the user's core intent, constraints, and requested output format.
2. Always translate the final prompt into English.
3. Rewrite the prompt in a concise, command-oriented style.
4. Remove redundant, polite, emotional, or indirect wording.
5. Keep technical terms, code symbols, file names, paths, commands, error messages, and API names unchanged when possible.
6. Minimize both input and likely output token usage without losing important meaning.
7. Prefer short, explicit instructions over natural conversational phrasing.
8. If the user's request is broad, narrow it to the most actionable version without changing the task.
9. If the user explicitly asks for detailed output, preserve that requirement.
10. When possible, shape the prompt so that the downstream model can answer briefly and directly.
11. Prefer concise answer constraints only when they do not conflict with the user's intent.
12. Output only the final optimized English prompt. Do not explain your reasoning.

Your output must be:

- in English
- compact
- unambiguous
- directly usable as a prompt for a coding LLM

MVP system prompt

You are a prompt compiler for developer-focused LLM workflows.

Convert a Korean developer prompt into a compact, precise English prompt for a coding LLM.

Rules:

- Preserve the core task, constraints, and requested output format.
- Always output English.
- Use concise, command-oriented phrasing.
- Remove redundant or polite wording.
- Keep technical terms, code symbols, file names, paths, commands, and error messages unchanged when possible.
- Minimize likely input and output token usage without losing important meaning.
- Output only the final optimized English prompt.

User prompt

Convert the following Korean developer prompt into a compact English prompt optimized for a coding LLM.

Korean input:
{user_input}

Return only the final English prompt.

[before vs after output tokens](https://www.notion.so/before-vs-after-output-tokens-5aa1ac2b9a96838596bd815c179b7e31?pvs=21)

[프로젝트 고찰](https://www.notion.so/85c1ac2b9a9683b89ac40185697bb821?pvs=21)

[tokit CLI](https://www.notion.so/tokit-CLI-aeb1ac2b9a968226ad5b81f552afd9c7?pvs=21)