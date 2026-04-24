## 요약

- `ParallelClassifier` 구현 — DependencyResolver 결과를 stage 단위 병렬 실행 그룹으로 변환
- `ExecutionStage`, `ParallelClassification` 타입 정의
- stage 배정 규칙: deps 없으면 stage 0, deps 있으면 `max(deps의 stage) + 1`
- 같은 stage 내 task들은 서로 의존하지 않으므로 병렬 실행 가능

## 관련 이슈

- Closes #

## 변경 유형

- [x] 기능 추가
- [ ] 버그 수정
- [ ] 리팩터링
- [ ] 문서 수정
- [ ] 테스트 추가/수정

## 영향 범위

- 영향받는 모듈:
  - `src/core/task-graph/ParallelClassifier.ts` — 신규 생성
- 영향받지 않는 모듈:
  - `src/core/task-graph/DependencyResolver.ts`
  - `src/core/task-graph/DAGValidator.ts`
  - `src/core/task-graph/TaskGraphProcessor.ts`
  - `src/schemas/pipeline.ts`

## 설계

```
DependencyResolver.resolve(graph, validation)
  → { orderedTasks: ResolvedTask[] }

ParallelClassifier.classify(resolution)
  → { stages: ExecutionStage[] }
```

| stage | 조건 | 실행 방식 |
|---|---|---|
| 0 | depends_on 없음 | 즉시 병렬 실행 |
| N | 모든 deps가 stage N-1 이하 | 이전 stage 완료 후 병렬 실행 |

## 테스트 방법

1. `npm run typecheck` — 타입 에러 없음 확인
2. 순차 구조 (`t1→t2→t3`):
```ts
// stages: [ {stage:0,[t1]}, {stage:1,[t2]}, {stage:2,[t3]} ]
```
3. 병렬 포함 구조 (`t1→t2`, `t1→t3`):
```ts
// stages: [ {stage:0,[t1]}, {stage:1,[t2,t3]} ]  // t2, t3 동시 실행 가능
```
4. 전부 독립 (`t1`, `t2`, `t3` 모두 depends_on:[]):
```ts
// stages: [ {stage:0,[t1,t2,t3]} ]  // 전부 동시 실행 가능
```

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

- `orderedTasks`가 이미 topological 순서이므로 각 task 처리 시 deps의 stage가 항상 먼저 결정됨이 보장됨
- 실행기(executor)는 `stages[]`를 순서대로 순회하며 각 stage를 `Promise.all()` 패턴으로 실행 가능
- 실제 병렬 실행 및 리소스 할당은 Out of Scope — 이 클래스는 그룹화만 담당
