## 요약

- Role 경계 재정의에 따라 `TaskGraphProcessor` 전면 재작성
  - Role 1: `sentences[]` 출력만 담당 (한국어 → 영어 변환 + 문장 분리)
  - Role 2.1: `sentences[]` 수신 후 type 분류 / id 생성 / depends_on 결정 / TaskGraph 구성 전담
- `RawTaskSchema`, `RawAnalyzedRequestSchema` 제거 → `CompiledSentencesSchema` 추가
- `TaskGraphProcessor.process()` 입력이 `{ intent, tasks[] }` → `{ sentences: string[] }`로 변경

## 관련 이슈

- Closes #23

## 변경 유형

- [ ] 기능 추가
- [ ] 버그 수정
- [x] 리팩터링
- [ ] 문서 수정
- [ ] 테스트 추가/수정

## 영향 범위

- 영향받는 모듈:
  - `src/schemas/pipeline.ts` — `RawTaskSchema`, `RawAnalyzedRequestSchema` 제거, `CompiledSentencesSchema` 추가
  - `src/core/task-graph/TaskGraphProcessor.ts` — 입력 구조 및 로직 전면 재작성
- 영향받지 않는 모듈:
  - `src/core/state/` (Role 2.2 모듈)
  - `src/core/context/` (Role 2.2 모듈)

## 테스트 방법

1. `npm run typecheck` — 타입 에러 없음 확인
2. 아래 입력으로 `TaskGraphProcessor.process()` 호출 시 정상 변환 확인:
```json
{
  "sentences": [
    "Read the auth module",
    "Find bugs in the code",
    "Fix the identified issues"
  ]
}
```
3. 반환된 `TaskGraph`에 `id`, `type`, `depends_on`, `status: "pending"` 포함 확인
4. 각 문장이 올바른 type으로 분류되는지 확인 (`read` → `explore`, `fix` → `modify`)
5. 스키마 불일치 입력 시 ZodError 발생 확인

## 체크리스트

- [x] 로컬에서 테스트했습니다
- [ ] 필요한 경우 테스트를 추가하거나 수정했습니다
- [ ] 필요한 경우 문서를 수정했습니다
- [x] 브레이킹 체인지 여부를 확인했습니다
- [x] 리뷰하기 좋은 크기로 PR을 유지했습니다

## 스크린샷 / 로그

```
> detoks@0.1.0 typecheck
> tsc --noEmit -p tsconfig.json

(0 errors)
```

## 리뷰어 참고사항

- Role 경계 재정의로 인한 변경 — Role 1의 task 분해 책임을 Role 2.1로 이관
- type 분류는 현재 키워드 기반 (영어 문장 기준), 정확도 개선은 추후 TaskDecomposer에서 보완 예정
- depends_on 기본값은 순차 실행 — 의미 기반 병렬 분류는 ParallelClassifier에서 담당
