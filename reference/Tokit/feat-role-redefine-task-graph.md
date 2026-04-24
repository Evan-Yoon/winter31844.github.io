## 요약

- Role 1 / Role 2.1 책임 경계 재정의 및 TaskGraphProcessor 전면 재작성
- Role 1은 전처리(한국어 → 영어 변환 + 문장 단위 분리)만 담당, `{ sentences: string[] }` 출력
- Role 2.1이 `sentences[]`를 받아 type 분류 / id 생성 / single·multi 구분 / depends_on 결정 / TaskGraph 구성 전담
- TaskDecomposer를 별도 모듈에서 TaskGraphProcessor 내부로 흡수 (단순화)

## 관련 이슈

- Related to #

## 변경 유형

- [ ] 기능 추가
- [ ] 버그 수정
- [x] 리팩터링
- [x] 문서 수정
- [ ] 테스트 추가/수정

## 영향 범위

- 영향받는 모듈:
  - `src/schemas/pipeline.ts`
    - `RawTaskSchema`, `RawAnalyzedRequestSchema` 제거
    - `CompiledSentencesSchema` (`{ sentences: string[] }`) 추가
  - `src/core/task-graph/TaskGraphProcessor.ts`
    - 입력: `{ intent, tasks[] }` → `{ sentences: string[] }`
    - single(문장 1개) / multi(문장 2개 이상) 분기 추가
    - type 흐름 기반 `depends_on` 자동 결정 (`FLOWS_TO` 매핑)
  - `docs/SCHEMAS.md`, `docs/SCHEMA_FLOW.md`, `docs/SHARED_DATA_FLOW.md`
    - `AnalyzedRequest` → `CompiledSentences` 로 교체
    - Role 1 / Role 2.1 책임 기술 업데이트
- 영향받지 않는 모듈:
  - `src/core/state/` (Role 2.2 모듈)
  - `src/core/context/` (Role 2.2 모듈)
  - `src/core/executor/`, `src/integrations/` (Role 3 모듈)

## 테스트 방법

1. `npm run typecheck` — 타입 에러 없음 확인
2. `npm run test` — 12/12 통과 확인
3. single 요청 정상 동작 확인:
```json
{ "sentences": ["Read the auth module"] }
→ TaskGraph { tasks: [{ id: "t1", type: "explore", depends_on: [] }] }
```
4. multi sequential 정상 동작 확인:
```json
{ "sentences": ["Read the auth module", "Analyze the bug", "Fix the issue"] }
→ t1(explore) → t2(analyze) → t3(modify) 순차 실행
```
5. multi parallel 정상 동작 확인:
```json
{ "sentences": ["Fix the auth bug", "Update the README"] }
→ t1(modify), t2(document) 각각 depends_on: [] (독립 실행)
```

## 체크리스트

- [x] 로컬에서 테스트했습니다
- [ ] 필요한 경우 테스트를 추가하거나 수정했습니다
- [x] 필요한 경우 문서를 수정했습니다
- [x] 브레이킹 체인지 여부를 확인했습니다
- [x] 리뷰하기 좋은 크기로 PR을 유지했습니다

## 스크린샷 / 로그

```
> detoks@0.1.0 typecheck
> tsc --noEmit -p tsconfig.json
(0 errors)

> detoks@0.1.0 test
> vitest run
Test Files  6 passed (6)
      Tests  12 passed (12)
```

## 리뷰어 참고사항

- `AnalyzedRequestSchema` / `AnalyzedRequest` 타입은 `pipeline.ts`에 잔존 — 현재 참조 코드 없음, Role 1 구현 시 정리 예정
- `FLOWS_TO` 매핑이 sequential/parallel 판단의 핵심 — 추후 type 쌍 추가 가능
- Role 1 담당자는 output을 `{ sentences: string[] }` 형식에 맞춰야 함 (`CompiledSentencesSchema` 기준)
