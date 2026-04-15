---
title: |
  화면 안 켜도 Claude가
  알아서 말 걸어줌
  — Clawd on Desk
slug: clawd-on-desk-review
date: 2026-04-15
author: Evan Yoon
category: explore
subcategory: new-tech
description: |
  여러 IDE, 여러 터미널을 동시에 돌릴 때
  Claude가 뭘 하고 있는지 화면을 직접 안 봐도 알 수 있다면?
  Clawd on Desk가 그걸 해줬다.
thumbnail: /images/posts/clawd-on-desk/readme-animations.png
tags:
  - clawd-on-desk
  - claude-code
  - developer-tools
  - new-tech
  - productivity
readTime: 7
featured: false
draft: false
toc: true
---

## 찾게 된 계기

Claude Code를 쓰다 보면 자연스럽게 이런 상황이 생긴다.

터미널은 세 개 열어두고, IDE는 두 개, 브라우저도 켜놓고 — Claude한테 뭔가 시켜두고 다른 걸 하고 있으면 "얘 지금 뭐 하고 있지?" 하고 화면을 왔다 갔다 하게 된다. 허가 요청이 떠 있는데 한참 지나서야 발견하거나, 이미 끝난 작업인데 모르고 계속 기다리거나.

그러다 발견한 게 **Clawd on Desk**다.

> **GitHub:** [rullerzhou-afk/clawd-on-desk](https://github.com/rullerzhou-afk/clawd-on-desk)

---

## Clawd on Desk가 뭔가

한 줄로 요약하면 — **Claude Code의 상태를 데스크탑 위에 캐릭터로 띄워주는 도구**다.

Claude가 뭘 하고 있냐에 따라 캐릭터가 달라진다. 생각 중이면 생각하는 애니메이션, 코드 치는 중이면 타이핑 모션, 에러 나면 에러 모션, 완료되면 신나는 모션. 별도로 Claude 터미널 창을 켜두지 않아도, 바탕화면 한쪽에 작은 캐릭터가 상태를 계속 알려준다.

여러 IDE와 여러 터미널에서 동시에 작업할 때 특히 유용하다. Claude가 어떤 세션에서 뭘 하고 있는지 굳이 그 창으로 포커스를 돌리지 않아도 파악이 된다.

![캐릭터 애니메이션 상태 목록](/images/posts/clawd-on-desk/readme-animations.png)

상태 감지는 생각보다 세밀하다. Idle, Thinking, Typing, Building, Juggling, Conducting 등 — Claude Code의 내부 이벤트와 HTTP 바운더리를 추적해서 캐릭터 상태가 바뀐다.

---

## 설치

설치는 GitHub에서 클론 후 그대로 실행하면 된다.

```bash
# Clone the repo
git clone https://github.com/rullerzhou-afk/clawd-on-desk.git
cd clawd-on-desk

# Install dependencies
npm install

# Start Clawd (auto-registers claude code hooks on launch)
npm start
```

`npm start` 하는 순간부터 Claude Code 훅에 자동으로 등록된다. Claude Code를 실행하면 Clawd가 알아서 감지를 시작한다.

---

## 실제로 어떻게 동작하나

### 상태별로 캐릭터가 다르다

가장 핵심 기능이다. Claude Code가 내부에서 어떤 상태인지를 실시간으로 읽어서 캐릭터 애니메이션에 반영한다.

Animation Map을 보면 어떤 상황에 어떤 애니메이션이 뜨는지 설정할 수 있다.

![Animation Map 설정 화면](/images/posts/clawd-on-desk/settings-animation.png)

- **Error flash** — 툴 호출이 실패했을 때
- **Notification** — 알림이 필요할 때 (권한 허용 요청 등)
- **Context sweep** — PromptCache / 컨텍스트 정리 중
- **Task complete (happy)** — 작업이 완료됐을 때
- **Workflow carry** — 워크플로가 이어질 때

권한 허용 요청이 떴을 때 캐릭터가 Notification 상태로 바뀌는 게 특히 유용하다. 다른 창 보다가도 캐릭터 보고 "아, 허가 요청 떴구나" 하고 바로 알 수 있다.

---

### 다중 에이전트를 지원한다

Claude Code 하나만이 아니다. Agents 설정 탭을 보면 지원하는 에이전트 목록이 꽤 길다.

![Agents 설정 화면](/images/posts/clawd-on-desk/settings-agents.png)

- Claude Code
- Codex CLI
- Copilot CLI
- Gemini CLI
- Cursor Agent
- CodeBuddy
- Kiro CLI
- OpenCode 등

각 에이전트별로 버블(팝업 알림) 표시 여부를 개별 토글로 제어할 수 있다. 여러 IDE에서 다른 에이전트들을 동시에 쓰는 환경이라면 굉장히 유용한 구성이다.

---

## 직접 써본 소감

<iframe
  src="https://www.youtube.com/embed/Mwv9T7s_MZM"
  title="claude-on-desk 실사용 후기"
  frameborder="0"
  allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
  allowfullscreen
  style="display:block; width:100%; max-width:760px; margin:1rem auto; aspect-ratio:16/9;"
></iframe>

솔직히 처음엔 "그냥 귀여운 위젯 아닌가" 싶었다. 근데 실제로 붙여두고 하루 써보니 달랐다.

Claude한테 뭔가 시켜두고 다른 작업하다 보면, 자연스럽게 캐릭터 쪽으로 눈이 간다. Notification 상태로 바뀌면 "권한 요청이네" 하고 바로 반응할 수 있고, happy 모션 뜨면 "아 끝났구나" 하고 확인하러 가면 된다. 화면을 왔다갔다 하는 횟수가 눈에 띄게 줄었다.

특히 **마우스 커서를 따라다니는 기능**이 있다는 게 재밌었다.

<iframe
  src="https://www.youtube.com/embed/tcD4pvZkquI"
  title="마우스 커서 따라다니는 claude-on-desk"
  frameborder="0"
  allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
  allowfullscreen
  style="display:block; width:100%; max-width:760px; margin:1rem auto; aspect-ratio:16/9;"
></iframe>

커서 따라다니는 건 사실 기능적으로 필수는 아닌데, 덕분에 화면 어디를 봐도 캐릭터가 시야에 들어온다. 묘하게 Claude랑 같이 작업하는 느낌이 든다.

---

## 설정 커스터마이징

기능 외에도 설정이 꽤 세밀하게 돼 있다.

### General 설정

![General 설정 화면](/images/posts/clawd-on-desk/settings-general.png)

- 언어 설정 (현재 English)
- 사운드 이펙트 on/off
- 로그인 시 자동 실행
- Claude Code 세션 시작 시 자동 시작
- 버블이 커서를 따라다닐지 여부
- 모든 버블 숨기기
- 세션 ID 표시 여부

### 테마 선택

![Theme 설정 화면](/images/posts/clawd-on-desk/settings-theme.png)

캐릭터 테마를 고를 수 있다. 현재 빌트인으로 세 가지 — Calico (고양이), Clawd (기본 로봇), My Theme (회색 로봇) 가 있다. 커뮤니티 테마도 별도 폴더에 추가하면 쓸 수 있다.

---

## 마무리

Claude Code를 쓰면서 생긴 불편함 — "지금 뭐 하고 있지? 권한 요청 떴나? 끝났나?" — 을 생각보다 깔끔하게 해결해줬다.

화면 하나하나 뒤지러 다닐 필요 없이, 캐릭터 하나로 Claude 상태를 주변부 시야에서 계속 파악할 수 있다. 여러 IDE랑 터미널을 같이 굴리는 환경이라면 실질적인 생산성 차이가 생긴다.

오픈소스라 직접 테마나 애니메이션을 추가하는 것도 가능하다. 관심 있으면 한번 설치해봐도 좋을 것 같다.

> **GitHub:** [rullerzhou-afk/clawd-on-desk](https://github.com/rullerzhou-afk/clawd-on-desk)
