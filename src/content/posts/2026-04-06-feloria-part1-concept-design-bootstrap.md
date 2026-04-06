---
title: "[Feloria] 고양이만 등장하는 RPG를 직접 만들어봤다 — 발상, 재시작, 그리고 하루"
slug: "feloria-part1-concept-design-bootstrap"
date: 2026-04-06
author: "Evan Yoon"
category: "project"
subcategory: "personal-project"
description: "고양이과 동물만 등장하는 몬스터 수집 RPG 'Feloria'. 포켓몬에서 출발했지만 두 번의 전면 재시작을 거쳐, 3월 10일 하루 동안 게임의 핵심 루프를 처음으로 통과시켰다."
thumbnail: "/images/posts/feloria-part1/cover.jpg"
tags:
  - feloria
  - phaser3
  - javascript
  - game-dev
  - personal-project
  - monster-taming-rpg
readTime: 14
series: "Feloria 프로젝트"
seriesOrder: 1
featured: true
draft: false
toc: true
---

## 시작은 단순한 질문이었다

"고양이과 동물만으로 포켓몬 같은 걸 만들면 어떨까?"

게임 개발을 배운 적 없다. 엔진 문서를 통독한 적도 없다. 그냥 고양이가 좋았고, 몬스터 수집 RPG가 좋았고, 그걸 직접 만들면 어떨지 궁금했다. 그게 출발이었다.

프로젝트 이름은 **Feloria**. 인간이 없는 고양이 문명 세계다. 집고양이, 야생 고양이과, 정령 고양이, 전설 고양이가 공존하는 세계에서, 플레이어는 잃어버린 별의 유산을 찾아 대륙을 탐험한다. 포켓몬의 감성을 참고했지만 이름·설정·UI·전투 구조는 처음부터 독창적으로 설계했다. 저작권 문제도 있었고, 어차피 참고할 수 있는 건 감성뿐이니까.

결론부터 말하면 Feloria는 3월 10일 하루 동안 게임의 핵심 루프를 처음 통과했다. 스타터 선택 → 마을 탐험 → 야생 조우 → 전투 → 포획 → 파티 관리. 폴리시는 없었고 그래픽도 placeholder였지만, 그 날 처음으로 "이게 게임이다"라는 느낌이 왔다.

---

## 처음 기획은 너무 컸다

첫 번째 프롬프트에는 이미 이런 것들이 담겨 있었다.

- 몬스터 100종 이상
- 스킬 200개
- 7가지 속성 체계와 상성표
- 체육관 시스템
- 전설 이벤트
- 저장/불러오기, 도감, 파티, 인벤토리

한 번에 너무 많이 요구했다. AI가 코드를 만들어줬고, 실행은 됐고, 화면도 뜨긴 했다. 하지만 맵은 텍스트 타일이었고, 충돌은 없었고, "진짜 게임처럼" 보이지 않았다.

이게 첫 번째 교훈이었다. **기능 목록이 길다고 게임이 되지 않는다.** 오버월드 하나가 제대로 작동하는 게, 시스템 열 개가 placeholder인 것보다 훨씬 낫다.

---

## 두 번 방향을 바꿨다

### 첫 번째 전환: 쯔꾸르 감성으로 축소

대규모 포켓몬식 설계를 버리고 "쯔꾸르 감성의 탑다운 고양이 RPG"로 방향을 잡았다. 마을 2개, 숲 1개, NPC 대화, 저장/퀘스트. 훨씬 작아졌다.

세계관도 이때 확정됐다. 게임 이름은 Feloria. 스타터는 세 마리.

- **Leafkit** — Forest 타입, 균형형
- **Emberpaw** — Fire 타입, 공격형
- **Misttail** — Water 타입, 방어/보조형

지역도 정했다. Starwhisk Village, Greenpaw Forest, Mosslight Path, Aurora Creek, Shadowvale Ruins, Celestial Gate. 이 구조는 이후 커밋까지 거의 바뀌지 않았다.

### 두 번째 전환: 전면 재시작 — Phaser 3 + JavaScript

React + TypeScript + Canvas 스택은 동작은 했지만 맵 렌더러가 계속 문제였다. 충돌 처리, 카메라 팔로우, NPC 레이어, 조우 구역 — 이것들을 직접 Canvas에 구현하려니 매번 무너졌다.

재시작을 결정한 이유는 두 가지였다.

첫째, **엔진 문제**. Phaser 3는 타일맵 레이어, 카메라, 충돌, 씬 전환을 엔진 수준에서 제공한다. 직접 만들던 것들을 버리고 Phaser의 구조를 따르는 편이 훨씬 안전했다.

둘째, **가독성 문제**. TypeScript strict mode는 코드 안전성은 높이지만, 게임 개발에 처음 입문한 상태에서 타입 에러와 씨름하며 게임 로직을 동시에 짜는 건 너무 느렸다. JavaScript로 바꾸면서 코드를 읽고 수정하는 속도가 눈에 띄게 빨라졌다.

이때 만든 씬 구조가 이후 개발의 뼈대가 됐다.

```
BootScene → PreloadScene → StartScene → NameScene → StarterSelectScene
                                              ↓
                         WorldScene ↔ DialogScene ↔ BattleScene
                                    ↓
              MenuScene / PartyScene / CodexScene / QuestScene / EvolutionScene
```

각 씬이 자신의 역할만 담당하고 데이터는 Phaser registry를 통해 공유하는 구조다.

---

## 왜 이렇게 설계했는가

### 맵 데이터: 8-레이어 구조

초기 맵은 단순한 2D 배열이었다. 충돌 타일이 화면에 그대로 보이거나, 조우 구역과 장식이 구분되지 않는 문제가 반복됐다. 결국 맵 데이터 구조를 이렇게 확정했다.

```
groundLayer    — 지면 (풀, 흙길, 모래)
detailLayer    — 꽃, 장식, 환경 디테일
collisionLayer — 나무/벽/바위/집 (렌더링 없이 충돌만)
objectLayer    — 표지판, 상자, 문
encounterLayer — 야생 조우 트리거 (키 큰 풀)
warpPoints     — 맵 전환 지점
npcSpawns      — NPC 위치
itemSpawns     — 아이템 위치
```

`collisionLayer`를 시각적으로 렌더링하지 않는다는 규칙이 반복 강조됐다. 실제로 개발 초기에 충돌 데이터가 화면에 그대로 보이는 문제가 한 번 발생했기 때문이다.

### 크리처 데이터: creatures.js 하나로

모든 크리처는 `creatures.js` 단일 파일에서 관리한다. 각 크리처가 갖는 필드 구조는 아래와 같다.

```js
LEAFKIT: {
  id: 'LEAFKIT',
  name: 'Leafkit',
  type: 'Forest',
  description: 'A playful green kitten. It smells like fresh pine.',
  baseHp: 20,
  baseAttack: 5,
  baseDefense: 4,
  catchRate: 1.0,
  skills: ['vine_swipe', 'leaf_dart', 'forest_guard'],
  evolution: { target: 'BRAMBLECAT', level: 10 }
}
```

BattleScene과 PartyScene이 같은 데이터 파일을 참조하므로, 두 씬이 서로 다른 크리처 정보를 보는 불일치가 구조적으로 발생하지 않는다. 고양이를 추가하거나 수정할 때 씬 코드는 건드리지 않아도 된다.

스킬은 등급 체계로 분류했다. 기본형은 3등급 스킬 3개, 진화형은 2등급 3개, 최종 진화형은 1등급 3개, 전설형은 고유 스킬 4개.

### 에셋 파이프라인: assetPaths.js 집중 관리

에셋 경로가 여러 파일에 흩어지면 경로 하나 바꿀 때 모든 씬을 뒤져야 한다. `assetPaths.js`에 BATTLEBACKS, CHARACTERS, FACES, SPRITES를 모두 몰아놓고, PreloadScene이 이를 순회하며 로드한다.

```js
// assetPaths.js 구조 (일부)
SPRITES: {
  LEAFKIT:    { KEY: "leafkit",    PATH: "/assets/sprites/creatures/leafkit.png" },
  EMBERPAW:   { KEY: "emberpaw",   PATH: "/assets/sprites/creatures/emberpaw.png" },
  CINDERCLAW: { KEY: "cinderclaw", PATH: "/assets/sprites/creatures/cinderclaw.png" }
}
```

초기에는 외부 에셋 없이도 게임이 실행돼야 했기 때문에, PreloadScene은 `pixelArtGenerator`를 사용해 타일셋·플레이어·NPC·크리처를 16×16 픽셀 배열로 런타임에 직접 생성했다. 나중에 실제 이미지가 준비되면 파일만 교체하면 됐다.

> placeholder가 debug 사각형처럼 보여선 안 된다는 조건이 붙었다. 그래서 pixel art generator가 필요했다.

---

## 3월 10일 — 하루에 무슨 일이 있었나

3월 10일에 7개의 커밋이 집중됐다. 이 날이 Feloria의 실질적인 첫 날이다.

### 1. 이름 입력과 스타터 선택

가장 먼저 만들어진 건 플레이어 이름 입력과 스타터 3마리 선택 UI다. 이유는 단순하다. 이 흐름이 한 번이라도 작동해야 이후 어떤 씬을 만들어도 "시작이 연결되는지"를 검증할 수 있기 때문이다.

선택된 스타터와 이름은 Phaser registry에 저장한다. BattleScene에서 파티 데이터를 읽을 때도, PartyScene에서 크리처를 보여줄 때도 같은 registry를 참조한다.

### 2. 마을 2개와 상호작용

Starwhisk Village와 Greenpaw Forest, NPC 상호작용이 한 커밋에 들어갔다. 맵을 2개로 제한한 건 의도적인 결정이었다. 이전 시도에서 맵을 많이 만들다가 렌더러가 무너진 경험이 있었다. 2개에서 이동·충돌·NPC 대화·워프가 정상 동작하는 것을 먼저 잡는 게 훨씬 안전했다.

### 3. Preload씬, 맵 로더, 마을 맵 데이터

에셋이 없어도 게임이 실행돼야 했다. PreloadScene은 외부 이미지 없이도 runtime에서 pixel art 텍스처를 생성한다. 로딩 바, 에러 핸들링, placeholder 텍스처 생성이 한 커밋에 들어갔다.

```js
// PreloadScene: placeholder 텍스처 생성 (grass 타일)
const tGrass = [
  'EEEEEEEEEEEEEEEE',
  'EEGEEEEEEEEEEEEE',
  'EEEEEEEEEEEEEEEE',
  // ...
];
pixelArtGenerator.createTileset(this, 'overworld-tiles',
  [tEmpty, tGrass, tDirt, tWater, tWall, tTallGrass, tTree], 2
);
```

### 4. 전투, 스타터 선택, 코어 데이터

턴제 전투 시스템, 스타터 선택 로직, creatures.js 초기 데이터가 한 커밋에 담겼다. 스타터 3종의 baseHp, baseAttack, baseDefense, catchRate, skills, evolution 필드가 이때 자리를 잡았다. 이후 전투 상성·로그 가독성·HP 바 개선은 모두 이 기반 위에 쌓였다.

### 5. 포획과 상태 유지

포획 후 데이터가 registry에 정확히 반영돼야 PartyScene과 Codex가 올바른 값을 보여준다. 그래서 이 시점에 상태 지속 구조를 잡는 게 필수였다. 포획 성공/실패 피드백, 초반 과포획 방지를 위한 catchRate 설계도 함께 들어갔다.

### 6. 전투 UI와 경험치

HP 바, 스킬 선택 UI, 경험치 획득 로직이 추가됐다. 이 시점의 구현은 나중에 버그가 많이 발견됐다. EXP가 씬 전환 후 초기화되거나, HP가 레벨업 후 maxHp를 초과하는 문제가 Phase 5에서 집중적으로 잡혔다.

### 7. PartyScene과 BattleScene 분리

하루의 마지막 커밋에서 파티 관리와 전투가 별도 씬으로 분리됐다. 파티 관리 로직이 전투 로직과 섞이면 상태 관리가 꼬인다. 각 씬이 자신의 역할만 담당하는 구조가 이때 자리를 잡았다.

---

## 하루가 의미하는 것

3월 10일 하루 동안 스타터 선택 → 마을 → 전투 → 포획 → 파티까지 수직 슬라이스가 통과됐다. 그래픽은 placeholder, 폴리시는 없었으며, 많은 것이 나중에 재작성됐다.

그럼에도 이 날이 중요한 이유는 하나다. **게임의 핵심 루프가 처음으로 끝에서 끝까지 연결됐다.**

이후 Foundation 단계에서 도감·상점·저장·퀘스트가 추가되고, Combat 단계에서 전투가 정교해지고, Asset 단계에서 실제 고양이 아트가 붙는 것은 모두 이 날 만들어진 뼈대 위에서 이루어진다.

---

다음 글에서는 Foundation ~ Content 단계를 다룬다. 씬 골격이 갖춰지고, 도감·상점·퀘스트·저장이 붙으면서 "시스템 모음"이 "게임"처럼 보이기 시작하는 구간이다.

그리고 왜 그렇게 많은 버그가 Phase 5에서 한꺼번에 터졌는지도 얘기할 것이다.
