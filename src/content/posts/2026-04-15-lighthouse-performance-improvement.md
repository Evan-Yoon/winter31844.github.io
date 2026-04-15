---
title: "내 블로그 Lighthouse 점수를 89점에서 97점으로 올린 기록"
slug: "lighthouse-performance-improvement"
date: 2026-04-15
author: "Evan Yoon"
category: "explore"
subcategory: "new-tech"
description: "Threads에서 우연히 접한 Lighthouse. Mongle Admin Web에서 한 번 써봤던 경험을 내 블로그에도 적용해봤다. F12 오류, CLI 우회, 세 가지 코드 수정으로 Performance 89점 → 97점까지 올린 전 과정."
tags:
  - lighthouse
  - web-performance
  - astro
  - accessibility
  - core-web-vitals
  - new-tech
readTime: 10
toc: true
featured: false
draft: false
---

얼마 전 Threads를 보다가 Lighthouse 관련 글을 접했다.

웹 성능 측정 도구인데, 브라우저 F12 하나로 Performance · Accessibility · Best Practices · SEO를 수치로 보여준다는 내용이었다. 사실 처음 보는 건 아니었다. Mongle 앱을 만들 무렵 Admin Web 쪽 성능 이슈가 있어서 그때 Lighthouse를 처음 써봤고, 실제로 개선을 해본 경험이 있었다.

그래서 바로 내 블로그에 F12를 눌러봤다.

## F12 → 오류, Incognito → 또 오류

<div style="display: flex; justify-content: center; margin: 2rem 0;">
  <img
    src="/images/posts/lighthouse-performance/f12-lighthouse.png"
    alt="블로그에서 F12를 눌러 Lighthouse 탭을 열었을 때"
    style="max-width: 720px; width: 100%; border-radius: 12px; box-shadow: 0 8px 32px rgba(0,0,0,0.12);"
  />
</div>

F12를 열고 Lighthouse 탭에서 "Analyze page load"를 눌렀더니 Performance 측정이 계속 오류가 났다. 에러 메시지를 보니 이런 내용이 있었다.

> *"There may be stored data affecting loading performance in this location. IndexedDB. Audit this page in an incognito window to prevent those resources from affecting your scores."*

캐시나 확장 프로그램이 측정에 영향을 준다는 뜻이라 시크릿 모드(Incognito)로 열어서 다시 시도했다.

<div style="display: flex; justify-content: center; margin: 2rem 0;">
  <img
    src="/images/posts/lighthouse-performance/github-error.png"
    alt="시크릿 모드에서도 GitHub 불러오기 부분 때문에 Performance 오류 발생"
    style="max-width: 720px; width: 100%; border-radius: 12px; box-shadow: 0 8px 32px rgba(0,0,0,0.12);"
  />
</div>

그런데 Incognito에서도 똑같이 Performance가 오류였다. 원인을 찾아보니, 내 메인 페이지 Hero 섹션에서 **GitHub Contributions Calendar를 외부 API로 실시간으로 불러오는 부분**이 Lighthouse의 타임아웃을 유발하는 것 같았다. 브라우저 내에서는 네트워크 상태에 따라 측정 자체가 흔들려서 정확한 점수가 나오지 않는 것이다.

## CLI로 직접 측정

브라우저 환경에서 안 되면 CLI로 직접 돌리면 된다. 터미널에서 아래 명령어 하나로 해결했다.

```bash
npx lighthouse https://evan-yoon.com --preset=desktop --view
```

`--preset=desktop`은 데스크톱 환경 기준으로 측정하겠다는 옵션이고, `--view`는 결과를 바로 HTML 파일로 열어서 보여준다.

<div style="display: flex; justify-content: center; margin: 2rem 0;">
  <img
    src="/images/posts/lighthouse-performance/cli-result.png"
    alt="CLI로 실행한 Lighthouse 초기 결과 — Performance 89점"
    style="max-width: 720px; width: 100%; border-radius: 12px; box-shadow: 0 8px 32px rgba(0,0,0,0.12);"
  />
</div>

결과는 이랬다.

| 항목 | 점수 |
|---|---|
| **Performance** | **89** |
| Accessibility | 90 |
| Best Practices | 100 |
| SEO | 100 |

Best Practices와 SEO는 만점이고 Accessibility도 90점이라 나쁘지 않다. 하지만 Performance가 89점이고 Core Web Vitals 중 **LCP(Largest Contentful Paint)가 2.0s**로 가장 낮은 점수(0.62)를 기록하고 있었다. 이 부분을 집중적으로 개선해보기로 했다.

## 무엇이 점수를 깎고 있었나

Lighthouse 리포트에서 낮은 점수를 주는 항목들을 정리해보면 크게 세 가지였다.

### 1. Render-Blocking Google Fonts

```html
<!-- 기존 — 렌더 차단 방식 -->
<link href="https://fonts.googleapis.com/css2?..." rel="stylesheet" />
```

구글 폰트를 `rel="stylesheet"`로 그냥 불러오면 **브라우저가 이 CSS를 다 받을 때까지 화면을 그리지 않는다.** Lighthouse는 이것 때문에 약 500ms 손해를 보고 있다고 알려줬다.

### 2. aria-hidden 안의 포커스 가능한 요소

내 블로그에는 검색 모달이 있는데, 닫혀 있을 때 `aria-hidden="true"` 상태지만 내부의 `<input>`과 `<button>`은 탭 키로 여전히 접근이 가능한 상태였다. 스크린 리더 사용자에게 혼란을 줄 수 있는 접근성 문제다.

### 3. GitHub Calendar의 작은 터치 타겟

GitHub Contributions Calendar 위젯이 그리는 `<td>` 셀들이 `tabindex` 속성을 가지고 있어서 포커스 가능한 요소로 인식되는데, 각 셀의 크기가 터치 최소 기준인 24px에 미치지 못했다.

## 개선 작업 — Before / After

### Fix 1. Google Fonts 비동기 로딩

```html
<!-- Before -->
<link href="https://fonts.googleapis.com/css2?..." rel="stylesheet" />

<!-- After -->
<link rel="preload" as="style" href="https://fonts.googleapis.com/css2?..." />
<link href="https://fonts.googleapis.com/css2?..."
      rel="stylesheet"
      media="print"
      onload="this.media='all'" />
<noscript>
  <link href="https://fonts.googleapis.com/css2?..." rel="stylesheet" />
</noscript>
```

`media="print"` 트릭이다. 브라우저는 print 미디어 CSS를 렌더링을 멈추지 않고 백그라운드에서 다운로드한다. 다운로드가 완료되면 `onload`에서 `media='all'`로 전환해서 폰트를 적용한다. `<noscript>`는 JS가 비활성화된 환경을 위한 폴백.

**결과: LCP 약 500ms 단축.**

---

### Fix 2. 검색 오버레이에 `inert` 속성 추가

```html
<!-- Before -->
<div id="search-overlay" aria-hidden="true">
  <input id="search-input" ... />
  <!-- ... -->
</div>

<!-- After -->
<div id="search-overlay" aria-hidden="true" inert>
  <input id="search-input" ... />
  <!-- ... -->
</div>
```

HTML `inert` 속성은 해당 요소와 그 자식 전체를 **포커스, 클릭, 스크린 리더 접근 모두 차단**한다. `aria-hidden`은 시각적으로 숨기는 역할만 하는 반면, `inert`는 실제 인터랙션을 막는다. 검색창을 열 때는 `inert`를 제거하고, 닫을 때 다시 붙이도록 JS를 수정했다.

```js
// open
overlay.removeAttribute('inert');
overlay.setAttribute('aria-hidden', 'false');

// close
overlay.setAttribute('inert', '');
overlay.setAttribute('aria-hidden', 'true');
```

---

### Fix 3. GitHub Calendar `tabindex` 제거

```js
// GitHub Calendar 로드 완료 후 실행
.then(() => {
  // 기존: 불필요한 요소들 숨기기
  const toHide = calendar.querySelectorAll('.year-tabs, .contrib-footer, ...');
  toHide.forEach((el) => { el.style.display = 'none'; });

  // 추가: 너무 작은 td 터치 타겟 문제 제거
  calendar.querySelectorAll('td[tabindex]').forEach((td) => {
    td.removeAttribute('tabindex');
  });
})
```

GitHub Calendar 위젯이 생성하는 테이블 셀은 직접 수정할 수 없으므로, 로드 완료 후 JavaScript로 `tabindex` 속성을 제거해서 포커스 대상에서 제외했다.

---

### Fix 4. 색상 대비 개선

```css
/* Before — 명도 대비 3.85:1 (WCAG AA 불합격) */
--ink-3: #808080;

/* After — 명도 대비 4.76:1 (WCAG AA 합격) */
--ink-3: #696969;
```

`#808080`은 밝은 배경(`#F9F9F7`)에서 WCAG AA 기준인 4.5:1을 충족하지 못한다. `#696969`로 약간 어둡게 조정해서 기준을 통과했다. 다크 모드의 `--ink-3`는 어두운 배경 위에서 이미 기준을 통과하고 있어 변경하지 않았다.

## 결과 — 89점 → 97점

<div style="display: flex; justify-content: center; margin: 2rem 0;">
  <img
    src="/images/posts/lighthouse-performance/result-after.png"
    alt="개선 후 Lighthouse 결과 — Performance 97점"
    style="max-width: 720px; width: 100%; border-radius: 12px; box-shadow: 0 8px 32px rgba(0,0,0,0.12);"
  />
</div>

| 항목 | Before | After |
|---|---|---|
| **Performance** | **89** | **97** |
| Accessibility | 90 | 95 |
| Best Practices | 100 | 100 |
| SEO | 100 | 100 |

| Core Web Vital | Before | After |
|---|---|---|
| FCP | 1.0s | 1.0s |
| **LCP** | **2.0s** | **1.0s** |
| TBT | 0ms | 0ms |
| CLS | 0 | 0 |

LCP가 2.0s → 1.0s로 절반으로 줄었다. 폰트 로딩 하나만 비동기로 바꿔도 이렇게 차이가 난다.

## 마치며

Lighthouse는 개선 여지를 찾아주는 도구지, 100점 자체가 목표는 아니다. 이번에 개선하지 못한 항목도 있는데, Firebase SDK가 모든 페이지에서 로드되면서 미사용 JS가 약 40KiB 발생하는 문제와, GoatCounter · GitHub Calendar 같은 외부 스크립트로 인한 Back/Forward Cache 실패는 아직 남아 있다.

남은 것들은 다음 개선 과제로 두고, 일단 **코드 네 줄 수정으로 8점을 끌어올렸다**는 사실로 오늘의 기록은 충분하다.
