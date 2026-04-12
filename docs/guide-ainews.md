# Agent Guide: `ai-news` 카테고리

이 문서는 `category: ai-news` 글을 작성하는 에이전트를 위한 가이드다.
이 카테고리는 나머지 카테고리(`study`, `project`, `explore`)와 구조가 완전히 다르다.
반드시 이 문서의 포맷을 그대로 따른다.

---

## 1. 이 카테고리가 담는 것

`ai-news`는 **주간 AI 뉴스 카드뉴스**다. 해당 주의 주요 AI 관련 이슈를 12장 내외의 플립 카드 형식으로 정리한다.
각 카드는 앞면(뉴스 이미지 + 제목)과 뒷면(해설 + 핵심 포인트 + 원문 링크)으로 구성된다.

---

## 2. 글 저장 경로

```
src/content/posts/ainews/YYYY-MM-DD-weekly-ai-news-ko.md
```

일반 posts 하위가 아닌 **`posts/ainews/`** 하위에 저장한다.

---

## 3. Frontmatter 작성법

```yaml
---
title: "주간 AI: M월 DD일 - M월 DD일"
slug: "YYYY-MM-DD-weekly-ai-news-ko"
date: "YYYY-MM-DD"
author: "Evan Yoon"
category: "ai-news"
description: "이번 주 핵심 AI 이슈를 N장의 카드로 정리했다."
thumbnail: "/images/ai-news/YYYY-MM-DD-weekly-overview.svg"
tags: ["주간-ai-뉴스", "ai-브리핑", "카드뉴스", "기술트렌드"]
draft: false
toc: false
---
```

**주의사항**
- `toc: false` 고정 (카드뉴스 구조에 목차 불필요).
- `thumbnail`은 SVG 파일. 경로: `/images/ai-news/YYYY-MM-DD-weekly-overview.svg`.
- `slug`는 날짜 기준으로 고정 형식 사용.
- `series`, `seriesOrder`, `readTime`, `featured` 필드는 사용하지 않는다.

---

## 4. 본문 구조

### 인트로 문단

```html
<p class="weekly-news-intro">
M월 DD일부터 M월 DD일까지 주요 AI 이슈를 N장의 카드로 정리했습니다.
카드 뒷면에서 더 자세한 내용과 관련 링크를 확인하실 수 있습니다.
</p>
```

### 카드 그리드 컨테이너

```html
<div class="flip-card-grid">
  <!-- 카드들이 여기 들어감 -->
</div>
```

### 개별 카드 구조 (반드시 이 형식 그대로)

```html
<div class="flip-card" data-flip-card tabindex="0" aria-label="카드 설명 키워드 카드">
  <div class="flip-card-inner">

    <!-- 앞면 -->
    <section class="flip-card-face flip-card-front">
      <img class="flip-media" src="뉴스 이미지 URL" alt="이미지 설명" referrerpolicy="no-referrer" />
      <div class="flip-body">
        <h3 class="flip-title">앞면 제목 (뉴스 핵심을 짧게)</h3>
      </div>
    </section>

    <!-- 뒷면 -->
    <section class="flip-card-face flip-card-back">
      <div class="flip-back-top">
        <span class="flip-kicker">출처 이름 (예: AI타임스, TechCrunch)</span>
        <button class="flip-close" type="button" data-flip-close>Close</button>
      </div>
      <h3 class="flip-detail-title">뒷면 제목 (해설 관점의 제목)</h3>
      <p class="flip-detail">2~3문장 해설. 이 뉴스가 왜 중요한지, 어떤 맥락인지를 적는다.</p>
      <ul class="flip-points">
        <li>핵심 포인트 1 (한 문장)</li>
        <li>핵심 포인트 2 (한 문장)</li>
      </ul>
      <div class="flip-links">
        <a href="원문 URL" target="_blank" rel="noreferrer">원문 보기</a>
      </div>
    </section>

  </div>
</div>
```

---

## 5. 카드 작성 원칙

### 앞면 (`flip-title`)
- 뉴스 헤드라인을 그대로 옮기지 않는다.
- 핵심을 15자 내외로 압축해 흥미롭게 재작성한다.
- 예: `일본의 24조 반도체 자립 드라이브`

### 뒷면 제목 (`flip-detail-title`)
- 해설자의 관점에서 의미를 짚는 제목.
- "왜 이것이 중요한가"를 담는 제목.
- 예: `AI 경쟁은 결국 반도체 공급망 통제력으로 이어진다`

### 해설 (`flip-detail`)
- 2~3문장. 단순 요약이 아니라 **왜 이게 중요한가**를 중심으로 쓴다.
- Evan의 관점에서 해석을 담는다. 중립적인 보도 문체는 피한다.
- 예:
  ```
  딥시크가 자체 데이터센터 구축 쪽으로 움직인다는 건 GPU 확보 다음 단계가
  이미 전력, 냉각, 운영 엔지니어링으로 넘어갔다는 신호다. 이제 인프라는
  모델 기업의 부속이 아니라 본체에 가깝다.
  ```

### 핵심 포인트 (`flip-points`)
- 2개 고정. 각 1문장.
- 독자가 이 뉴스에서 가져갈 가장 중요한 사실 또는 시사점.
- 단순 부연이 아니라 새로운 각도의 포인트를 추가한다.

### 출처 (`flip-kicker`)
- 미디어 이름만. URL 아님.
- 예: `AI타임스`, `TechCrunch`, `Bloomberg`, `The Verge`, `Reuters`

---

## 6. 카드 수와 주제 선정

- 한 편에 **10~14장** 사이로 작성한다. 12장이 기준.
- 주제는 다음 분야에서 균형 있게 선택한다:
  - 모델/연구 (신규 모델 출시, 벤치마크)
  - 인프라/하드웨어 (반도체, 데이터센터, 전력)
  - 기업/정책 (빅테크 전략, 규제, 투자)
  - 산업 적용 (헬스케어, 교육, 제조 등)
  - 개발자 생태계 (오픈소스, 툴, API)
- 단순 신기한 뉴스보다 **구조적 변화를 드러내는 뉴스**를 우선 선정한다.

---

## 7. 이미지 처리

- 카드 이미지는 뉴스 원문의 대표 이미지 URL을 직접 사용한다.
- `referrerpolicy="no-referrer"` 속성을 반드시 포함한다.
- 이미지가 없는 뉴스는 관련 공식 로고 또는 대체 이미지를 사용한다.
- 썸네일(`/images/ai-news/YYYY-MM-DD-weekly-overview.svg`)은 별도 SVG 파일로 생성한다.

---

## 8. 하면 안 되는 것

- 카드 HTML 클래스명을 임의로 바꾸기 (`flip-card`, `flip-card-inner` 등은 CSS와 연결됨)
- 앞면에 긴 텍스트 넣기 (앞면은 이미지 + 제목 15자 내외)
- 뒷면 해설을 뉴스 본문 요약으로만 채우기 (Evan의 해석 관점 필요)
- `flip-points`를 1개 또는 3개 이상으로 작성하기 (2개 고정)
- `toc: true`로 설정하기
- 파일을 `posts/ainews/` 외 경로에 저장하기
