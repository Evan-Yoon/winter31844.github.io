# Agent Guide: `explore` 카테고리

이 문서는 `category: explore` 글을 작성하는 에이전트를 위한 가이드다.
반드시 [style.md](../style.md)의 GLOBAL RULES를 먼저 따르고, 이 문서의 규칙을 추가로 적용한다.

---

## 1. 이 카테고리가 담는 것

`explore`는 **직접 경험하고 탐구한 것을 이야기로 풀어내는 글**이다. 진로 에세이, 현장 방문기, 새로운 기술이나 도구 탐방, 특강 후기가 포함된다.
`study`나 `project`와 달리, 핵심은 **사건보다 경험자의 시선**이다. 무슨 일이 있었는가보다, 그것이 나에게 어떤 의미였는가가 중심이 된다.

---

## 2. 서브카테고리별 성격

### `essay` — 개인 에세이 / 진로 회고

- 커리어 전환, 결심, 삶의 방향에 대해 쓰는 글이다.
- 블로그에서 가장 "날 것"에 가까운 글이다. 감정과 내적 변화가 전면에 나온다.
- 영어로 쓰는 경우가 많다 (독자층이 넓거나 국제적 맥락일 때).
- 타이틀은 독자가 궁금해하도록 유도하는 형태. 질문형, 역설형, 비유형이 잘 맞는다.
- 예: `My Real-Life 'Isekai': Why I Quit My HR Career to Become a Developer`

### `field-visit` — 현장 방문 / 특강 후기

- 외부 기관이나 회사 방문, 특강 참석 기록이다.
- 일정 순서대로 나열하지 않고, 인상 깊었던 순간을 중심으로 재구성한다.
- 어떤 말이 남았는가, 어떤 장면이 기억에 남는가를 선택적으로 담는다.
- 이동 경로, 준비 과정, 소소한 에피소드(헤어컷, 이동 거리 등)를 포함하면 글이 살아난다.

### `new-tech` — 새로운 도구 / 기술 탐방

- 개발자 문화 안에서 발견한 재미있거나 유용한 것을 소개하는 글이다.
- 소개 + 사용 방법(코드/설정 포함) + 직접 써본 느낌을 조합한다.
- 홍보성이나 리뷰 글처럼 읽히지 않도록, 내가 왜 흥미로웠는지를 중심으로 쓴다.
- 예: `깃허브에서 동물 키우기 — GitAnimals`

### `lecture` — 특강 / 외부 강의 후기

- 학교 외부 연사가 진행한 특강이나 세미나 내용을 정리하는 글이다.
- 강연 내용의 핵심 메시지와, 그것이 나에게 어떻게 연결됐는지를 함께 적는다.
- 발표자 이름과 소속을 명시하되, 개인정보 보안이 필요한 내용은 생략한다.

---

## 3. 문체 및 구조

- `explore`는 세 카테고리 중 **가장 자유로운 구조**를 허용한다. 반드시 특정 순서를 지킬 필요는 없다.
- 그러나 반드시 지켜야 하는 것은: **도입에서 독자를 잡고, 중간에 전환점이 있고, 끝에 감정적 잔상이 남아야 한다.**
- 영어 글은 style.md의 English Style을 따른다.
- 한국어 글은 style.md의 Korean Style을 따른다.
- 이미지는 글의 흐름을 끊지 않는 자리에 삽입한다. 이미지 아래에 짧은 캡션을 달면 좋다.

### `essay` 구조

1. 특정 기억이나 사건으로 시작 (시점을 구체적으로: "2023년 1월, ~")
2. 그 경험이 어떤 생각을 불러왔는가
3. 이후 삶에서 그 생각이 어떻게 발전했는가
4. 결심 또는 변화
5. 끝은 다짐 또는 열린 결말 — 완성된 결론보다 "계속 가고 있다"는 느낌

### `field-visit` 구조

1. 방문 배경과 설렘 (왜 갔는가)
2. 현장 도착 전 소소한 에피소드
3. 강연/방문 내용 — 발표자별, 순서별로 간략히
4. 가장 인상 깊었던 말이나 장면
5. 떠나면서의 생각, 이후에 남은 것

### `new-tech` 구조

1. 어떻게 발견했는가 (지인 추천, 우연히 등)
2. 처음 봤을 때 반응
3. 실제 사용법 (코드/설정 포함)
4. 직접 써보니 어땠는가
5. 이 분야/문화에서 이것이 왜 재미있는가

---

## 4. Frontmatter 작성법

```yaml
---
title: "My Real-Life 'Isekai': Why I Quit My HR Career to Become a Developer"
slug: my-real-life-isekai-why-i-quit-my-hr-career-to-become-a-developer
date: 2025-11-28
author: Evan Yoon
category: explore
subcategory: essay             # essay | field-visit | new-tech | lecture
description: "한 문장 요약. 글의 핵심 질문이나 결론을 담는다."
thumbnail: /images/posts/폴더명/cover.jpg
tags:
  - 관련-주제-태그
readTime: 22
series: ""                     # 시리즈가 있으면 채움 (field-visit는 종종 시리즈)
seriesOrder: 1
featured: false
draft: false
toc: true
---
```

**주의사항**
- `essay`, `field-visit` 글에는 `description` 필드가 없는 경우도 있다 (초기 글). 최근 글에는 넣는다.
- `thumbnail`은 실제 방문 현장 사진 또는 대표 이미지.
- `toc: true`는 소제목이 3개 이상이면 켠다. essay는 `toc: false`도 괜찮다.
- `series`는 Intel AI for Future Workforce 방문기처럼 연속된 현장 기록에 사용.

---

## 5. 실제 글 패턴 예시

### essay — 도입부 패턴 (영어)

```markdown
To explain why I suddenly fell into the world of AI and development, we have to
travel back in time. Not to 2025, but to **January 2023.**

Back then, ChatGPT was barely a whisper in Korea. But my cousin, who was doing
his master's in the US, had just come back to visit.
```

→ 시점을 명확히 하고, 한 장면으로 독자를 끌어들인다.

### field-visit — 현장 분위기를 살리는 패턴 (영어)

```markdown
My home is in Yongin, which is quite a trek from Yeouido. Since being late is
absolutely unacceptable to me, I planned to leave early.

But before that, I had another mission. My hair was a bit of a mess, so I booked
a haircut at my usual salon at 11 AM. Why? Well, this program offers internship
opportunities at Intel for top-performing graduates.
```

→ 이동 준비, 사소한 에피소드까지 담아 실제감을 높인다.

### new-tech — 발견 → 반응 → 사용법 패턴 (한국어)

```markdown
오늘 글은 "신문물 탐방"이라기보다는, 지호님이 귀엽고 재밌는 걸 찾았다고 같이
해보자고 해서 시작한 이야기다.

이름은 **GitAnimals**.

처음 링크 받았을 때 반신반의했다. 깃허브에서 동물을 키운다고? 근데 직접 해보고
나서 생각이 바뀌었다.
```

→ 소개 전에 "왜 나에게 왔는가"를 먼저 쓴다.

---

## 6. 하면 안 되는 것

- 특강/방문 내용을 PPT 목차처럼 정리하기 (날 것의 경험이 사라짐)
- essay에서 "저는 이런 사람입니다"식 자기소개서 말투 사용
- new-tech 글에서 공식 문서를 복붙하듯 기능 목록만 나열하기
- 경험 중에 느낀 감정이나 순간적인 생각을 삭제하기
- "이상으로 포스팅을 마치겠습니다" 같은 끝맺음
