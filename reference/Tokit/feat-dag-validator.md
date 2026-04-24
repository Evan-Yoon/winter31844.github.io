## 요약

- `DAGValidator` 구현 — TaskGraph가 유효한 DAG인지 3단계로 검증
- 검증 실패 시 이유(`reason`)와 상세 메시지(`detail`)를 포함한 결과 반환
- 성공 시 `topologicalOrder` 반환 — DependencyResolver가 바로 사용 가능한 구조

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
  - `src/core/task-graph/DAGValidator.ts` — 신규 생성
- 영향받지 않는 모듈:
  - `src/schemas/pipeline.ts` (스키마 변경 없음)
  - `src/core/task-graph/TaskGraphProcessor.ts`
  - Role 2.2, Role 3 모듈 전체

## 검증 로직

| 단계 | reason | 탐지 조건 |
|---|---|---|
| 1 | `UNKNOWN_DEPENDENCY` | `depends_on`에 존재하지 않는 task ID 참조 |
| 2 | `CYCLE_DETECTED` | DFS white/gray/black 컬러링으로 순환 탐지 |
| 3 | `DISCONNECTED_NODE` | 연결된 그래프 안에서 완전히 고립된 node |
| ✅ | 성공 | `topologicalOrder: string[]` 반환 |

## 테스트 방법

1. `npm run typecheck` — 타입 에러 없음 확인
2. 정상 DAG 검증:
```ts
DAGValidator.validate({
  tasks: [
    { id: "t1", depends_on: [], ... },
    { id: "t2", depends_on: ["t1"], ... },
  ]
})
// → { valid: true, topologicalOrder: ["t1", "t2"] }
```
3. 사이클 탐지:
```ts
// t1 → t2 → t1
// → { valid: false, reason: "CYCLE_DETECTED", detail: "Cycle detected: t1 → t2 → t1" }
```
4. 존재하지 않는 ID 참조:
```ts
// t2.depends_on: ["t99"] (t99 없음)
// → { valid: false, reason: "UNKNOWN_DEPENDENCY", detail: "..." }
```
5. 고립 노드:
```ts
// t1→t2 연결 있는데 t3는 아무 관계 없음
// → { valid: false, reason: "DISCONNECTED_NODE", detail: "..." }
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

- 병렬 task(`depends_on: []`)만 있는 그래프는 DISCONNECTED_NODE 미발생 — 의도적 병렬 실행이므로 정상
- DependencyResolver는 `DAGValidationSuccess.topologicalOrder`를 그대로 입력으로 받아 사용
- DFS는 white(0)/gray(1)/black(2) 컬러링 방식, topological sort는 Kahn's algorithm 사용
