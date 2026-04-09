---
title: |
  [Mongle] Day 5
  견적 AI, PDF, 머지 복구
slug: mongle-day5-estimation-pdf-rollback
date: 2026-03-27
author: Evan Yoon
category: project
subcategory: team-project
description: GPT-4o Vision으로 실제 견적서 사진을 분석하는 기능을 만들었다. 그리고 잘못된 머지 하나가 전체 시스템을 멈추는
  걸 경험하고, git log로 직접 복구했다.
tags:
  - mongle
  - gpt-4o
  - vision-api
  - pdf
  - react-native
  - debugging
  - git
  - team-project
  - troubleshooting
  - bugfix
readTime: 12
series: Mongle
seriesOrder: 5
featured: false
draft: false
toc: true
---

## 오전: 견적 비교 시스템 구축

> `견적 비교, 예산 pdf 저장, pdf 공유 기능` — 10:34
> 13 files, 1,369 insertions(+)

오늘 오전의 목표는 하나였다. 사용자가 웨딩 업체에서 받은 견적서(사진 또는 PDF)를 앱에서 비교할 수 있는 기능을 만드는 것.

구현된 핵심 파일들:

```
app/(couple)/
├── estimate-comparison/index.jsx  # 견적 비교 화면 (304줄)
├── budget-dashboard/index.jsx     # 예산 대시보드 (196줄)
└── pdf-history/index.jsx          # PDF 히스토리 (74줄)

lib/services/estimation/
├── EstimationParser.js    # 견적서 파싱 (112줄)
├── ComparisonEngine.js    # 비교 엔진 (109줄)
└── ComparisonModels.js    # 데이터 모델 (53줄)

lib/services/pdf/
├── PDFService.js          # PDF 생성 (69줄)
└── PDFTemplates.js        # PDF 템플릿 (160줄)
```

**`EstimationParser.js`** — 이 시점에서는 실제 OCR 대신 파일명 기반 mock 데이터를 반환하는 방식으로 먼저 구현했다. UI와 비교 로직을 먼저 완성하고, 실제 AI 연동은 나중에 붙이는 전략.

```javascript
async parsePdf(fileUri, fileName) {
    await new Promise(resolve => setTimeout(resolve, 1500)); // 처리 시간 시뮬레이션

    if (fileName.includes('가루다')) return this.getMockData1();
    else if (fileName.includes('라온')) return this.getMockData2();
    else return isHall ? this.getMockData1() : this.getMockData3();
}
```

mock 데이터에는 `vendorName`, `totalPrice`, `deposit`, `balance`, `includedItems`, `excludedItems`, `vatIncluded`, `refundPolicy` 등 실제 견적서에서 추출해야 할 필드를 그대로 담았다. 나중에 AI 연동으로 교체할 때 인터페이스만 맞추면 된다.

**`ComparisonEngine.js`** — 여러 견적서를 받아서 비교 지표를 계산하는 엔진이다.

```javascript
const realCost = item.totalPrice + item.optionsPrice - item.discountPrice
    + (item.vatIncluded ? 0 : item.totalPrice * 0.1);
```

부가세 포함 여부까지 감안한 실질 비용(`realCost`)을 계산하고, `riskScore` · `completenessScore` · `valueScore` 세 가지 점수를 매겨서 견적서마다 장단점을 분석한다.

---

## 오전의 복병: 머지 지옥

> `fix: 서버 에러 수정` — 11:01
> `fix: ai 채팅 오류 수정` — 11:44
> `fix: merge conflict 해결` — 12:02

기능을 올리자마자 팀 전체에서 에러 리포트가 쏟아졌다. 서버 에러, AI 채팅 오류, 머지 충돌이 연달아 발생했다. 문제는 오늘 아침에 여러 브랜치가 동시에 main에 머지되면서 충돌이 생겼고, 그 중 하나가 잘못된 상태로 들어온 것이었다.

---

## 이슈 1: 잘못된 머지 → 전체 시스템 마비

> `각 컴포넌트(홀, 스, 드, 메, 패키지, 플래너) 페이지 연동 문제 해결 및 기존 상태 rollback` — 12:21
> 5 files changed, 577 deletions

홀·스튜디오·드레스·메이크업·패키지·플래너 등 업체 카테고리 화면이 전부 동작을 멈췄다. 어제까지 멀쩡하던 기능들이다.

원인은 `vendors.jsx` 파일이었다. 누군가 작업 중이던 새 버전의 `vendors.jsx`(570줄)가 검증 없이 main에 들어오면서, 기존 라우팅 구조와 완전히 어긋나버렸다.

**해결 방법: git log 추적 후 수동 롤백**

```bash
git log --oneline Mongle-app/app/(couple)/vendors.jsx
git show <이전 정상 커밋 해시> -- Mongle-app/app/(couple)/vendors.jsx
```

git log를 거슬러 올라가면서 마지막으로 정상 동작하던 커밋을 찾았다. 그 버전의 코드를 직접 복사해서 이식했다. 결과적으로 문제가 된 `vendors.jsx` 파일 전체를 삭제하고, 라우팅을 이전 방식으로 되돌렸다.

---

## 이슈 2: AI 기능도 꼬였다

> `ai 기능 복원` — 14:04
> 3 files changed, 247 insertions(+), 209 deletions(-)

롤백 작업 중 AI 채팅 기능도 같이 꼬인 걸 발견했다. `chat/index.jsx`와 `chat/[id].jsx`가 오전 머지 과정에서 충돌 흔적이 남은 채 들어온 것이었다.

이것도 git log에서 직접 이전 버전을 찾아서 재작성했다. 채팅 기능 핵심 로직인 `useAi` 훅 연동, 메시지 렌더링, 스크롤 동작을 하나씩 확인하면서 복구했다.

> `이전 디자인으로 롤백` — 15:33

AI 복구와 함께 UI 디자인도 이전 버전으로 되돌렸다. 오늘 아침에 들어온 새 디자인이 기존 레이아웃과 맞지 않는 부분이 있어서, 안정적인 버전으로 먼저 돌린 다음 검토하기로 했다.

---

## 이슈 3: NFC/NFD 유니코드 매칭 실패

이건 오전부터 잡고 있던 버그인데, 오후에 복구 작업을 하면서 원인을 찾았다.

예산 최적화 AI가 반환하는 카테고리는 영문(`studio`, `dress`, ...)이고, 앱 UI에서 사용하는 레이블은 한글(`스튜디오`, `드레스`, ...)이다. 이 둘을 매핑하는 로직이 있는데, 특정 기기에서 "스튜디오"를 검색해도 매칭이 안 됐다.

원인은 한글 유니코드 정규화 방식 차이. macOS에서 입력한 한글은 NFD 방식(자모 분리), Android에서 입력한 한글은 NFC 방식(완성형)으로 저장되는 경우가 있다. 문자열이 눈에는 똑같이 보여도 바이트 레벨에서 달라서 `===` 비교가 실패한다.

```javascript
// 수정 전
if (label === '스튜디오') { ... }

// 수정 후
if (label.normalize('NFC') === '스튜디오'.normalize('NFC')) { ... }
```

`normalize('NFC')`를 적용해서 비교 전에 항상 같은 정규화 형태로 맞춰주도록 했다.

---

## 이슈 4: `toLocaleString` 크래시

> `분석 ai 성능 개선(gpt 모델 변경)` — 16:31
> `ComparisonEngine.js` 방어 코드 추가

공유 버튼을 누르면 앱이 크래시됐다. 스택 트레이스를 보니 `toLocaleString`에서 터졌다.

```
TypeError: Cannot read property 'toLocaleString' of undefined
```

AI가 견적서에서 일부 필드를 추출하지 못한 경우, 해당 값이 `undefined`로 들어온다. 이 상태에서 숫자 포맷팅을 시도하면 크래시가 난다.

**수정 전:**
```javascript
const realCost = item.totalPrice + item.optionsPrice - item.discountPrice + ...;
if (item.discountPrice > 0) pros.push(`할인 (${item.discountPrice.toLocaleString()}원)`);
if (item.includedItems.length > 5) pros.push("기본 포함 품목이 매우 다양함");
```

**수정 후 — 기본값 할당으로 방어:**
```javascript
const totalPrice   = Number(item.totalPrice)   || 0;
const optionsPrice = Number(item.optionsPrice) || 0;
const discountPrice = Number(item.discountPrice) || 0;
const includedItems = Array.isArray(item.includedItems) ? item.includedItems : [];
const vatIncluded  = !!item.vatIncluded;

const safeItem = { ...item, totalPrice, optionsPrice, discountPrice, includedItems, vatIncluded };

if (safeItem.discountPrice > 0) pros.push(`할인 (${(safeItem.discountPrice || 0).toLocaleString()}원)`);
if ((safeItem.includedItems || []).length > 5) pros.push("기본 포함 품목이 매우 다양함");
```

`Number()` 형 변환 + `|| 0` 기본값, `Array.isArray()` 체크로 데이터가 없어도 안전하게 동작하도록 했다.

---

## 오후: 실제 GPT-4o Vision API 연동

> `fix: 분석 로직 추가 및 오류 수정` — 16:32
> `Mongle-server/ai/api/estimation_api.py` — 86줄 신규 생성

오전에 mock으로 만들었던 `EstimationParser`를 실제 AI로 교체할 백엔드를 붙였다.

사용자가 견적서 사진을 업로드하면 base64로 인코딩해서 서버에 보내고, FastAPI가 GPT-4o Vision에게 분석을 요청한다.

```python
@router.post("/estimation/parse")
async def parse_estimation(req: ParseRequest):
    for file in req.files:
        messages = [{
            "role": "user",
            "content": [
                {"type": "text", "text": prompt},
                {
                    "type": "image_url",
                    "image_url": {
                        "url": f"data:image/jpeg;base64,{file['base64']}"
                    }
                }
            ]
        }]

        response = await openai_client.chat.completions.create(
            model="gpt-4o",   # Vision 지원 모델
            messages=messages,
            # ...
        )
```

프롬프트에는 웨딩 업계 도메인 지식을 구체적으로 담았다.

```python
prompt = """
당신은 전문적인 웨딩 견적서 분석 전문가입니다.
이미지가 다소 저화질이거나 흐릿하더라도, 문맥과 웨딩 업계의 일반적인 용어
(예: 스드메, 대관료, 식대, 원본데이터 등)를 바탕으로 최대한 정확하게 정보를 추출해야 합니다.

[분석 전략]
1. 이미지의 모든 텍스트를 꼼꼼히 읽습니다.
2. 특히 숫자(금액)와 단위(만원, 원)를 주의 깊게 확인하세요.
   '80'이 '800,000' 혹은 '80만원'인지 문맥으로 판단합니다.
3. 항목이 명시되지 않았더라도 금액의 규모를 보고
   카테고리(수백만원대면 웨딩홀, 수십만원대면 옵션 등)를 유추할 수 있습니다.
...
"""
```

저화질 사진에서 "80"이 8만원인지 80만원인지 판단하는 건 일반 OCR로는 불가능하다. GPT-4o가 웨딩 업계 상식("메이크업이 8만원일 리 없다")을 동원해서 문맥으로 유추하도록 프롬프트를 설계했다.

**모델 선택:** 오전에 `gpt-4o-mini`로 먼저 시도해봤는데 이미지 분석 정확도가 낮았다. 견적서처럼 정밀한 숫자 추출이 필요한 경우엔 `gpt-4o`가 필요하다.

---

## 오늘을 돌아보며

잘못된 머지 하나가 전체 시스템에 얼마나 큰 영향을 주는지 오늘 몸으로 배웠다. 기능이 멀쩡하게 돌아가다가 다른 사람의 커밋 하나로 전부 멈추는 건 팀 프로젝트에서 언제든 일어날 수 있는 일이다.

그런데 git log 덕분에 데이터를 유실하지 않고 복구할 수 있었다. `git log --oneline 파일명`으로 해당 파일의 변경 이력을 추적하고, `git show <해시>`로 정상이던 버전을 꺼내서 이식하는 흐름이 자연스럽게 됐다.

AI 기술을 도입하는 것만큼 중요한 건 **방어적 UI/UX**라는 점도 다시 느꼈다. `toLocaleString` 크래시처럼, AI가 데이터를 못 뽑아와도 앱이 멈추지 않도록 항상 기본값을 깔아두는 습관이 안정성을 만든다.

내일은 OCR 인식률을 더 높이는 방법을 찾고, 전체 기능 full debugging을 돌릴 예정이다.

---

## 화면 기록

Day 5의 중심은 견적 비교와 업체 정보 화면이었다. 비교 로직 설명 자료와 실제 업체 상세 화면을 같이 붙여 두면 이 날 작업한 기능이 바로 연결된다.

<img src="/images/posts/mongle/estimate-comparison-ai-logic.png" alt="견적 비교 AI 로직" style="display:block; max-width:560px; width:auto; height:auto; margin:1rem auto;" />

<img src="/images/posts/mongle/couple-vendor-detail-screen.png" alt="Mongle couple 업체 상세 화면" style="display:block; max-width:360px; width:auto; height:auto; margin:1rem auto;" />

---

**GitHub:** [APP-Project-Team1/mongle](https://github.com/APP-Project-Team1/mongle)
---

## 롤백 결정이 오히려 중요했던 이유

개발을 하다 보면 무언가를 추가하는 일보다 되돌리는 결정이 더 어렵다. 이미 투입한 시간과 구현한 기능이 아깝기 때문이다. Day 5에서 PDF와 견적 기능 일부를 롤백하거나 재조정한 과정은 그래서 더 의미가 있었다. 데모 일정이 있는 프로젝트에서 중요한 것은 "가능한 많은 기능"이 아니라 "끝까지 안정적으로 보여줄 수 있는 기능"이기 때문이다.

이 날의 판단은 기능 욕심보다 사용자 흐름과 안정성을 우선했다는 점에서 좋았다. 견적과 PDF는 사용자 가치가 큰 기능이지만, 생성 결과가 불안정하거나 예외 처리가 비어 있으면 오히려 전체 신뢰도를 떨어뜨린다. 구현 범위를 다시 좁히고 흐름을 정리한 덕분에 이후 단계에서 더 분명한 우선순위를 잡을 수 있었다.
