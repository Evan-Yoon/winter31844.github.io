## 요약

- `DependencyResolver` 구현 — DAGValidator 결과를 실제 실행 가능한 Task 순서로 변환
- `DAGValidationSuccess.topologicalOrder`를 입력받아 전체 Task 객체 + resolved deps 배열 반환
- `ResolvedTask`, `DependencyResolution` 타입 정의 — ParallelClassifier가 바로 사용 가능한 구조

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
  - `src/core/task-graph/DependencyResolver.ts` — 신규 생성
- 영향받지 않는 모듈:
  - `src/core/task-graph/DAGValidator.ts`
  - `src/core/task-graph/TaskGraphProcessor.ts`
  - `src/schemas/pipeline.ts` (스키마 변경 없음)

## 설계

```
DAGValidator.validate(graph)
  → { valid: true, topologicalOrder: string[] }

DependencyResolver.resolve(graph, validation)
  → { orderedTasks: ResolvedTask[] }
```

| 타입 | 설명 |
|---|---|
| `ResolvedTask` | `task: Task` + `deps: Task[]` (depends_on ID → 실제 객체) |
| `DependencyResolution` | `orderedTasks: ResolvedTask[]` — topological 순서 보장 |

## 테스트 방법

1. `npm run typecheck` — 타입 에러 없음 확인
2. 정상 케이스:
```ts
const graph = TaskGraphProcessor.process({ sentences: ["explore X", "create Y"] });
const validation = DAGValidator.validate(graph);
// validation.valid === true

const resolution = DependencyResolver.resolve(graph, validation as DAGValidationSuccess);
// resolution.orderedTasks[0].task.id === "t1"
// resolution.orderedTasks[1].task.id === "t2"
// resolution.orderedTasks[1].deps[0].id === "t1"
```
3. 병렬 케이스 (depends_on 없음):
```ts
// 모든 task.depends_on === [] → 각 ResolvedTask.deps === []
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

- DAGValidator가 이미 topological sort를 완료하므로 DependencyResolver는 ID → 객체 변환만 담당
- `deps` 배열은 `depends_on` 순서를 그대로 따름 (DAGValidator에서 순환 없음 보장)
- ParallelClassifier는 `orderedTasks`를 받아 `deps`가 모두 이전 stage에 있는지 기준으로 stage 분류 예정
