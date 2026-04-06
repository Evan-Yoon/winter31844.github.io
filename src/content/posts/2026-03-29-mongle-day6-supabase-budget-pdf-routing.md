---
title: "[Mongle] Day 6 — 쉬어야 할 일요일, 예산·잔금 Supabase 연동 완료"
slug: "mongle-day6-supabase-budget-pdf-routing"
date: 2026-03-29
author: "Evan Yoon"
category: "project"
subcategory: "team-project"
description: "원래 쉬기로 했던 일요일. 예산 AI 가중치 튜닝, 잔금 상세 페이지, Supabase 전체 연동, PDF 생성, 뒤로가기 버그까지 혼자 다 잡았다."
tags:
  - mongle
  - supabase
  - expo-router
  - pdf
  - budget-ai
  - react-native
  - team-project
readTime: 10
series: "Mongle 프로젝트"
seriesOrder: 6
featured: false
draft: false
toc: true
---

## 쉬기로 했던 날

Day 1 기획에서 주말(3/28~3/29)은 **개발 불가**로 못 박아뒀다. 실 개발 가능일이 9일에서 7일로 줄어든 원인이기도 했다.

그런데 일요일 오후 2시가 넘어서 커밋이 올라오기 시작했다. 머릿속에서 해결 안 된 것들이 계속 맴돌다가 결국 노트북을 열었다. 팀원들 PR부터 처리하고, 그 다음엔 내 작업으로 이어졌다.

---

## 오후 2시: PR 머지와 팀 작업 확인

| 시각 | PR |
|------|----|
| 14:31 | PR #69 — vendors 데이터 Supabase 연동 (팀원 B) |
| 14:44 | PR #70 — 상담 신청 및 알림 시스템 (팀원 A) |
| 16:23 | PR #71 — 플래너 계정 알림 Supabase 연동 (팀원 A) |

팀원들도 주말에 작업을 해뒀다. 코드 리뷰 후 세 개를 머지하고 나서 내 작업을 시작했다.

---

## 예산 AI 가중치 튜닝

> `feat: 비용관리 ai 응답 개선` — 16:37
> 9 files, 528 insertions(+)

어제 `BudgetAnalyzer`를 만들면서 스타일 유사도와 가격 절감을 6:4로 가중합했다. 직접 테스트해보니 추천 결과가 생각보다 비싼 업체가 올라왔다. 예산 최적화를 요청하는 사용자는 스타일보다 **가격 절감**을 더 원하는 게 당연하다.

```python
# 수정 전: 스타일 60%, 가격 40%
score = (sim * 0.6) + (price_score * 0.4)

# 수정 후: 스타일 30%, 가격 70%
score = (sim * 0.3) + (price_score * 0.7)
```

가중치 하나 바꿨을 뿐인데 추천 결과가 훨씬 납득 가는 방향으로 바뀌었다. 알고리즘 설계에서 가중치는 수식이 아니라 **도메인 판단**이라는 걸 다시 느꼈다.

같은 커밋에서 `BudgetOptimizationModal.jsx`(245줄)도 손봤다. AI 응답을 보여주는 방식을 개선하고, 업체별 절감액 표시와 스타일 태그를 더 명확하게 렌더링하도록 수정했다.

또 `budget_analyzer.py`에서 하드코딩된 절대 경로를 발견해서 수정했다.

```python
# 수정 전: 특정 환경에서만 동작
base_path = "c:/app-project/what-the-menu/Mongle-server/ai/script/vendors/"

# 수정 후: 어느 환경에서나 동작
current_dir = os.path.dirname(os.path.abspath(__file__))
base_path = os.path.join(current_dir, "script", "vendors")
```

`c:/app-project/...` 경로는 내 로컬 환경에서만 유효하다. 다른 팀원이 서버를 돌리면 즉시 `FileNotFoundError`가 난다. 실제로 이 경로를 심었던 게 나 자신이라 조금 민망했다.

---

## 잔금 상세 기능 구현

> `feat: 잔금 상세 편집 기능 추가` — 16:40
> `feat: 잔금 상세 페이지 구현` — 17:47

예산 화면에서 각 항목의 **잔금 일정**을 관리하는 기능을 만들었다. 업체 계약을 하면 계약금 + 잔금으로 나뉘는데, 잔금 납부일과 금액을 트래킹하는 화면이 없었다.

16:40 커밋에서 편집 UI를 먼저 추가하고, 17:47 커밋에서 상세 페이지로 분리했다. `budget.jsx`가 이날 하루에만 683→818줄로 늘어났다.

백엔드(`vendors.py` API)도 같이 수정했다. 잔금 데이터를 Supabase `vendor_costs` 테이블에서 읽고 쓸 수 있도록 라우터를 업데이트했다.

---

## Supabase 전체 연동

> `feat: supabase와 예산, 잔금 데이터 연동 완료` — 18:37
> 13 files, 804 insertions(+)

이날의 가장 큰 커밋이다. 예산과 잔금 데이터를 더미가 아닌 실제 Supabase에서 읽고 쓰는 연동을 완성했다.

작업 중에 Supabase 스키마가 프론트에서 예상하는 구조와 맞지 않는 경우가 계속 나왔다. 그래서 스키마를 직접 확인하는 유틸 스크립트를 만들었다.

**`inspect_db.py`** — 각 테이블의 실제 컬럼을 빠르게 확인하는 도구.

```python
def inspect_tables():
    tables = ['projects', 'cost_items', 'weddings', 'vendor_costs']
    for table in tables:
        result = supabase.table(table).select('*').limit(1).execute()
        if result.data:
            print(f"{table}: {list(result.data[0].keys())}")
```

테이블 스키마가 기억 안 날 때 `python inspect_db.py` 하나로 전체 컬럼을 확인할 수 있다. 팀원들도 이 스크립트 덕분에 "이 테이블에 뭐가 있더라?" 할 때마다 Supabase 대시보드를 열지 않아도 됐다.

**`seed_budget.py`** — 예산 초기 데이터를 자동으로 심는 스크립트.

```python
def seed_budget():
    res = supabase.table('projects').select('id').limit(1).execute()
    project_id = res.data[0]['id']

    # 중복 체크 후 없으면 생성
    res = supabase.table('budgets').select('*').eq('project_id', project_id).execute()
    if not res.data:
        budget_data = {
            'project_id': project_id,
            'total_amount': 3500.0,  # 3,500만원
            'spent': 0.0,
            'category': 'Wedding'
        }
        supabase.table('budgets').insert(budget_data).execute()
```

`seed_db.py`와 `test_seed.py`도 추가했다. 처음 환경을 세팅하는 팀원이 데이터 없이 빈 화면만 보는 상황을 막기 위한 것이다.

백엔드 라우터 세 곳(`budget.py`, `chat.py`, `vendors.py`)도 이번에 전반적으로 정리했다.

---

## PDF 생성 기능

> `feat: pdf 생성 기능` — 19:27
> 4 files, 105 insertions(+)

어제 만든 `PDFService.js`와 `PDFTemplates.js`를 실제 Supabase 데이터에 연결했다. 더미 데이터 기반 PDF가 아닌, 실제 사용자의 예산 항목을 읽어서 명세서를 생성한다.

`budget.py` 라우터에 PDF 전용 엔드포인트를 추가하고, `lib/api.js`에서 호출 경로를 맞췄다.

---

## 19:36 — 뒤로가기 버그 해결을 위한 대규모 라우팅 재설계

> `fix: 예산 관리 페이지에서 뒤로가기 누르면 직전 화면으로 이동하도록 수정` — 19:36
> 18 files, 4,547 insertions(+), 3,997 deletions(-)

오늘의 마지막 커밋이 가장 크다. "뒤로가기 수정"이라고 적었지만, 사실상 **앱 전체 라우팅 구조 재설계**다.

**문제:** 예산 화면에서 뒤로가기를 누르면 직전 화면이 아닌 엉뚱한 곳으로 이동했다.

**원인:** Expo Router에서 탭 네비게이터와 스택 네비게이터가 섞이면 뒤로가기 스택이 꼬인다. `(couple)` 그룹 아래 탭 화면들이 스택 히스토리에 독립적으로 쌓이지 않아서 발생하는 문제였다.

**해결:** `(couple)/(tabs)/` 중첩 그룹을 만들어서 탭 화면을 완전히 분리했다.

```
수정 전:
app/(couple)/
├── _layout.jsx       # 탭 네비게이터
├── budget.jsx        # 탭 화면 (스택과 섞임)
├── timeline.jsx
├── chat/
└── ...

수정 후:
app/(couple)/
├── _layout.jsx
└── (tabs)/           # 탭 전용 중첩 그룹
    ├── _layout.jsx   # 탭 네비게이터만 담당
    ├── budget.jsx    # 스택과 분리된 탭 화면
    ├── timeline.jsx
    ├── chat/
    └── ...
```

기존 파일들은 내용을 그대로 유지하면서 경로만 이동시키는 방식으로 처리했다. 파일 이동이라 `+4,547 -3,997`이라는 숫자가 나왔지만, 실질적인 로직 변경은 거의 없다.

뒤로가기 버그가 사라졌다.

---

## 오늘을 돌아보며

원래 쉬어야 할 날이었다. 그런데 막상 앉아보니 7시간 넘게 작업했다.

오늘 혼자 처리한 것들:
- AI 가중치 튜닝 (30:70으로 조정)
- 잔금 상세 페이지 + 편집 기능
- Supabase 전체 연동 (예산·잔금 CRUD)
- DB 디버깅 유틸 스크립트 3개 (`inspect_db`, `seed_budget`, `seed_db`)
- PDF 생성 → Supabase 실데이터 연결
- 뒤로가기 버그 → 전체 라우팅 구조 재설계

내일부터는 팀이 다시 모인다. 이번 주 막바지까지 Must 기능의 연동 완성도를 최대한 끌어올려야 한다.

---

**GitHub:** [APP-Project-Team1/mongle](https://github.com/APP-Project-Team1/mongle)
---

## 데이터 일관성과 라우팅을 정리한 의미

사용자 입장에서는 예산 데이터가 저장되고, 화면이 자연스럽게 넘어가고, PDF가 잘 생성되면 그걸로 충분하다. 하지만 개발자 입장에서는 이 세 가지가 서로 다른 문제처럼 보여도 실제로는 하나의 흐름이다. 저장 포맷이 흔들리면 다음 화면에서 값이 비고, 라우팅이 꼬이면 생성 결과를 확인하지 못하며, PDF가 깨지면 결국 이전 단계 데이터의 신뢰성도 의심받게 된다.

Day 6에서 Supabase, 예산 데이터, PDF, 라우팅을 한 번에 정리한 것은 서비스 흐름을 끊기지 않게 만드는 작업이었다. 사용자가 한 단계씩 전진하는 제품에서는 이런 연결부의 완성도가 체감 품질을 크게 좌우한다.