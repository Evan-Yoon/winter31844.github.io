---
title: |
  [Feloria] Part 3
  콘텐츠, 세계관, 회고
slug: feloria-part3-content-world-retrospective
date: 2026-03-21
author: Evan Yoon
category: project
subcategory: personal-project
description:
  Save/Load가 붙고 나서 Feloria는 처음으로 '게임처럼' 느껴지기 시작했다. 그 다음 문제는 시스템이 아니라 콘텐츠였다.
  스토리 구조, NPC 반응성, 100마리 배치 전략, 스킬 애니메이션, 오디오까지 — 후반 개발 전체를 정리했다.
thumbnail: /images/posts/feloria-part3/cover.png
tags:
  - feloria
  - phaser3
  - javascript
  - game-dev
  - personal-project
  - content-design
  - retrospective
readTime: 18
series: Feloria
seriesOrder: 3
featured: false
draft: false
toc: true
---

## Save/Load가 붙고 나서

Phase 8에서 저장 시스템이 붙기 전까지, Feloria는 새로고침하면 레벨 1 스타터로 돌아갔다. 저장이 붙은 이후부터 처음으로 "지속되는 세계"가 생겼다.

그리고 거기서 다른 문제가 보이기 시작했다.

시스템은 있었다. 전투, 포획, 도감, 퀘스트, 진화, 상점. 하지만 그 시스템들이 유기적으로 연결되지 않았다. 마을에서 숲으로 가고, 전투를 하고, 돌아왔을 때 세계가 아무것도 기억하지 않는 것처럼 느껴졌다. 처치한 트레이너가 대사를 바꾸지 않았고, NPC들은 진행과 무관하게 같은 말을 반복했고, 맵과 맵 사이의 연결이 이야기가 아니라 단순히 워프였다.

Phase 8 이후의 개발은 시스템을 더 추가하는 것이 아니라, 이미 있는 것들을 "세계처럼 보이게" 만드는 작업이었다.

---

## 레퍼런스 기반 아키텍처 재정립

Phase 9에서 중요한 결정이 있었다. 기존 코드를 계속 패치하는 대신, 검증된 monster-tamer 레퍼런스 프로젝트를 구조적 기준점으로 삼아 Phaser 아키텍처를 다시 정렬했다.

레퍼런스를 쓰는 방식에 명확한 제한이 있었다.

> "Use the reference project only for engine structure, scene organization, rendering flow, and overall gameplay presentation quality. Do NOT copy names, story, characters, monsters, or content."

즉 "어떻게 구성하는가"만 참고하고, "무엇을 담는가"는 처음부터 Feloria 것으로 채웠다. 이때 씬 구성, 렌더 순서, WorldScene과 BattleScene 사이의 데이터 흐름이 다시 한번 정리됐다.

렌더 순서도 이 시점에 명시적으로 확정됐다.

```
1. ground tiles
2. detail / decoration tiles
3. collidable objects
4. NPCs (Y-sorted)
5. player (Y-sorted)
6. overlay UI (DialogScene, MenuScene 등)
```

Y-sorting은 플레이어와 NPC가 나무 앞/뒤를 자연스럽게 지나가게 만드는 핵심이다. 이게 없으면 나무 뒤에 있는 NPC가 나무 앞에 떠 있는 것처럼 보인다.

---

## 스토리 구조가 생기다

초기 Feloria는 "탐험하고 싸우는" 게임이었다. Phase 9 이후부터 "왜 탐험하는가"가 붙기 시작했다.

스토리 구조는 포켓몬 골드의 챕터 진행 방식을 참고했다. 단 이름, 사건, 세계관은 처음부터 독창적으로 설계했다.

```
Chapter 1 — Starwhisk Village & Greenpaw Forest
  Elder Mira에게 스타터를 받고 여행 시작
  라이벌 Riven과 첫 만남

Chapter 2 — Mosslight Path
  첫 트레이너 루트
  Riven이 길을 막음

Chapter 3 — Ancient Forest & Mosslight Shrine
  Guardian Rowan과의 보스전
  신전의 봉인이 깨짐
  마을 촌장 현석의 정체 드러남

후반 — Shadowvale Ruins, Aurora Creek, Celestial Gate
  Obsidian Claw 조직 추격
  전설 고양이 각성 이벤트
  라이벌 Riven과의 최종 대결
```

가장 중요한 스토리 설계 결정은 **빌런의 위치**였다. 처음부터 악당 조직(Obsidian Claw)이 있었지만, 진짜 배신자는 마을에서 가장 신뢰받는 인물인 **촌장 현석**이었다. Guardian Rowan을 쓰러뜨린 직후 현석이 등장하는 장면이 Chapter 3의 핵심이다.

```
로완: "크윽… 네가 무슨 짓을 했는지 아느냐?"
      "네가 나를 쓰러뜨림으로써…"
      "봉인을 지키던 마지막 결계가 깨져버렸다…"

(촌장 현석 등장)

현석: "훌륭하구나."
      "로완, 이 고지식한 친구가 길을 비켜주지 않아 곤란하던 참이었단다."
      "역시 내가 선택한 아이답게 훌륭히 자라주었어."
```

이 구조가 좋았던 이유는 "플레이어가 모르게 빌런을 위해 일했다"는 반전이 탐험과 퀘스트 진행에 새로운 의미를 붙이기 때문이다. 단순히 강해지는 것이 아니라, 내가 뭔가를 망가뜨리고 있었다는 감각.

<img src="/images/posts/feloria-part3/story-outline.png" alt="Feloria 스토리 구상 화면" style="display:block; width:100%; max-width:860px; margin:1rem auto; border-radius:16px;" />
스토리 윤곽을 잡아가던 화면. 시스템이 아니라 서사가 게임을 앞으로 끌고 가기 시작한 시기였다.

`스토리 고도화.txt`를 다시 보면, 내가 당시 밀고 싶었던 비극 구조가 더 선명하다.

- 촌장 현석은 수호자를 몰아내고 봉인을 풀려는 흑막이다.
- 토비와 리나 같은 주민들은 "로완이 숲의 힘을 독차지한다"는 거짓말에 세뇌돼 있다.
- 플레이어는 선행을 한다고 믿고 퀘스트를 수행하지만, 실제로는 세라, 루크, 로완 같은 선한 수호자들을 하나씩 무너뜨리는 장기말이 된다.

이 요약을 다시 읽고 나니, Feloria의 후반부가 왜 단순한 배틀 확장이 아니라 '내가 무엇을 하고 있었는지 뒤늦게 깨닫는 이야기'가 되어야 했는지도 분명해졌다.

---

## 보스전과 신규 지역

Phase 10에서 처음으로 진짜 보스전이 생겼다. **Ancient Forest**와 **Mosslight Shrine**이 추가됐고, 신전에 보스 트레이너 **Guardian Rowan**이 배치됐다.

지도 진행 순서:

```
Starwhisk Village
    ↓
Greenpaw Forest
    ↓
Mosslight Path
    ↓
Ancient Forest    ← Phase 10에서 추가
    ↓
Mosslight Shrine  ← 보스전 위치
```

Rowan의 파티는 트레이너 전투 로직을 그대로 사용했다. 차이는 파티 규모와 보상, 그리고 전후 컷신이었다. 보스전 전에 카메라가 Rowan에게 패닝되고 대사가 나오고, 승리 후 플레이어에게 시점이 돌아오는 컷신 시스템이 이 시점에서 처음 사용됐다.

컷신 시스템은 구조 자체는 비교적 간단했다.

```
1. 플레이어 이동 비활성화
2. 카메라가 특정 위치로 이동
3. NPC가 앞으로 이동 (tween)
4. DialogScene 호출
5. 대사 종료 후 전투 시작 또는 이벤트 진행
6. 플레이어 이동 복원
```

이 구조는 이후 전설 각성 장면, 라이벌 최종 대결에도 재사용됐다.

---

## NPC가 반응하는 세계

Phase 10에서 NPC 역할 분화 시스템이 들어갔다. 이전까지 NPC는 고정 대사를 가진 오브젝트에 가까웠다. 이후부터 NPC는 역할을 갖고, 플레이어의 진행 상태에 따라 대사가 바뀐다.

NPC 역할 분류:

```
healer      → 파티 회복 기능 포함
shopkeeper  → 상점 연동
quest_giver → 퀘스트 트리거
hint_npc    → 다음 목적지/정보 제공
trainer     → 전투 트리거
lore_npc    → 세계관 설명
gatekeeper  → 진행 조건 충족 시 경로 개방
collector   → 특정 아이템/크리처 교환
```

여기서 내가 가장 신경 쓴 건 **세계 반응성**이었다. 같은 NPC라도 퀘스트 진행 상태나 맵 방문 여부에 따라 다른 대사를 보여준다.

```js
// Elder Mira 대사 분기 예시
if (!questStarted) {
  ("요즘 숲의 기운이 조금 이상하단다.");
} else if (questInProgress) {
  ("네 고양이를 잘 돌봐주거라.");
} else if (questCompleted) {
  ("고생했구나. 네가 해냈어.");
}
```

대사는 전부 한국어로 작성됐다. 세계관 밀도를 올리는 데 대사 언어가 생각보다 큰 역할을 한다. 영어 대사일 때는 "퀘스트 UI 텍스트"처럼 느껴지던 것이, 한국어로 바꾸니 캐릭터가 말하는 것처럼 읽혔다.

NPC 대화 데이터는 `mapNpcDialogues.js`로 분리됐다. 맵 ID → NPC ID → 대사 배열 구조다. 씬 코드에 대사 문자열을 직접 넣지 않는 것이 원칙이었다.

<img src="/images/posts/feloria-part3/ui-and-npcs.png" alt="Feloria의 UI와 NPC 구성이 제법 그럴듯해진 시점의 화면" style="display:block; width:100%; max-width:820px; margin:1rem auto; border-radius:16px;" />
UI와 NPC 배치가 어느 정도 자리를 잡고 나자 비로소 '게임 세계에 사람이 살고 있다'는 느낌이 생겼다.

<img src="/images/posts/feloria-part3/village-chief.png" alt="플레이어를 반겨주는 촌장 현석 장면" style="display:block; width:100%; max-width:760px; margin:1rem auto; border-radius:16px;" />
플레이어를 반겨주는 촌장 현석. 그래서 나중에 이 인물이 흑막이라는 설정이 더 잘 먹힌다. 처음엔 가장 믿음직해 보여야 했기 때문이다.

---

## 100마리의 배치 전략

100종의 크리처가 PNG로 준비됐다. 문제는 이걸 한 번에 게임에 다 넣으면 안 된다는 것이었다.

Greenpaw Forest에 레벨 60짜리 전설 고양이가 나타나면 게임 밸런스가 무너진다. 그래서 크리처마다 배치 상태를 데이터로 관리하는 구조를 만들었다.

```js
// creatures.js 배치 메타데이터
LEAFKIT: {
  activeNow: true,
  availableInChapter: 1,
  reservedForFuture: false,
  plannedRegion: null
}

SOLARION: {
  activeNow: false,
  availableInChapter: null,
  reservedForFuture: true,
  plannedRegion: "volcano"  // 미래 화산 지역
}
```

`activeNow: false`인 크리처는 조우 테이블에 절대 들어가지 않는다. 스프라이트는 로드되어 있고 데이터도 있지만, 게임 안에서는 존재하지 않는 것처럼 동작한다.

현재 맵별 배치 원칙:

| 지역                   | 배치 크리처                                   | 기준            |
| ---------------------- | --------------------------------------------- | --------------- |
| Starwhisk Village 주변 | Thistlekit, Snagpuss, Mosslynx                | 초기, 약함      |
| Greenpaw Forest        | Fernclaw, Thornkit, Barkpelt, Ripplepaw(희귀) | 숲/풀 타입 중심 |
| Mosslight Path         | Thornmane, Vinefang, Sparkpaw, Pebblepaw      | 중간 강도       |
| Ancient Forest         | Verdantlynx, Umbrafang, Mistlynx, Stormkit    | 강함            |
| Mosslight Shrine       | 랜덤 조우 없음 (보스만)                       | —               |

전설 크리처는 Chapter 1 엔딩 컷신에서만 짧게 등장한다. 전투도 포획도 없다. 봉인이 깨지는 순간 빠르게 플래시처럼 보였다가 사라지는 시각적 복선이다.

```
봉인 붕괴 연출:
  불의 전설 → 화산 플래시
  얼음의 전설 → 빙하 폭풍
  숲의 전설 → 고대 나무
  폭풍의 전설 → 번개
```

이 구조를 만든 이유는 확장성 때문이었다. 나중에 화산 지역을 추가할 때 `SOLARION.activeNow = true`로 바꾸고 조우 테이블에 넣으면 끝이다. 기존 코드를 건드리지 않는다.

---

## 스킬 애니메이션: 전투 위에 쌓는 시각 레이어

Phase 10 이후 스킬을 사용할 때 속성에 맞는 애니메이션이 재생되도록 시스템을 추가했다.

핵심 설계 원칙은 하나였다. **기존 전투 로직을 건드리지 않는다.** 스킬 애니메이션은 전투 결과에 아무런 영향을 주지 않는 순수한 시각 레이어다.

```
스킬 선택
    ↓
skillAnimationMap.js → 애니메이션 키 조회
    ↓
animationConfig.js  → 위치/크기/프레임 설정
    ↓
BattleScene에서 재생
    ↓
기존 데미지/턴 진행 계속
```

`skillAnimationMap.js`는 스킬 ID 또는 타입을 애니메이션 키로 매핑한다.

```js
const skillAnimationMap = {
  ember_bite: "Fire1",
  flame_dash: "Fire3",
  inferno_slash: "Fire4",
  water_slash: "Ice1",
  mist_burst: "Ice3",
  shadow_sneak: "Darkness1",
  thunder_paw: "Thunder2",
  storm_call: "Thunder4",
  vine_swipe: "Wind1",
  leaf_dart: "Wind2",
  soul_reap: "Special3",
  // ...
};
```

모든 애니메이션은 `/assets/animations/`에서만 가져온다. 다른 폴더를 참조하는 것은 금지됐다. 이 규칙이 있어야 새 스킬을 추가할 때 경로가 꼬이지 않는다.

애니메이션마다 설정이 다르다는 것도 문제였다. 불꽃 폭발은 타겟 중심에 재생하고, 얼음 기둥은 타겟 발 아래에서 올라오고, 전체 화면 필살기는 화면 중앙에 뜬다. 이걸 `animationConfig.js`로 분리했다.

```js
const animationConfig = {
  Fire1: {
    frameWidth: 192,
    frameHeight: 192,
    frameCount: 4,
    frameRate: 12,
    scale: 1.5,
    anchorType: "center", // 타겟 중심
  },
  Thunder2: {
    frameWidth: 96,
    frameHeight: 192,
    frameCount: 6,
    frameRate: 16,
    scale: 2.0,
    anchorType: "feet", // 타겟 발 아래
  },
};
```

가장 중요한 안전 규칙은 폴백이었다. 스킬 ID에 매핑이 없으면 타입으로 찾고, 타입에도 없으면 물리 공격 기본 이펙트를 쓴다. 어떤 경우에도 애니메이션 누락으로 전투가 멈추지 않는다.

---

## 타입 상성 시스템

스킬 애니메이션 다음으로 타입 상성이 붙었다. 기존 전투는 스탯 기반 단순 데미지였다. 타입 상성이 들어오면서 "같은 레벨이어도 타입이 맞으면 더 강하다"는 전략 요소가 생겼다.

설계 방침은 기존 전투 로직 보존이었다.

> "Preserve all currently working combat behavior. Treat type advantage as a multiplier layer on top of existing damage."

타입 배율 테이블은 `typeChart.js`에 분리됐다.

```js
// 일부 발췌
const typeChart = {
  Fire: { Forest: 2.0, Ice: 2.0, Water: 0.5 },
  Water: { Fire: 2.0, Rock: 2.0, Forest: 0.5 },
  Forest: { Water: 2.0, Rock: 2.0, Fire: 0.5 },
  Shadow: { Spirit: 0.5, Mystic: 2.0 },
  // ...
};
```

전투에서 배율을 적용할 때 피드백도 중요했다. "효과가 굉장했다!", "효과가 별로인 것 같다…" 같은 메시지가 전투 로그에 뜨도록 했다. 숫자만으로는 플레이어가 상성을 인지하기 어렵다.

---

## 오디오 시스템: 마지막으로 붙인 레이어

오디오는 개발 후반에 가장 마지막으로 붙었다. 게임플레이가 먼저고 소리는 나중이라는 판단이었고, 실제로 그렇게 됐다.

문제는 각 씬이 각자 BGM을 직접 재생하고 있다는 것이었다. 씬 전환 시 음악이 겹치거나, 전투에서 돌아왔는데 마을 BGM이 두 번 재생되는 현상이 생겼다.

해결은 `AudioManager`를 하나 만들어 모든 오디오를 중앙에서 관리하는 것이었다.

```
BGM  — 맵 배경 음악 (한 번에 하나만)
BGS  — 루핑 환경음 (바람소리, 물소리)
ME   — 짧은 이벤트 음악 (승리 팡파레, 진화 음악)
SE   — 짧은 효과음 (버튼, 포획, 피격)
```

씬은 AudioManager에게 "이 BGM으로 바꿔줘"라고 요청한다. 실제 페이드아웃/인 처리, 중복 방지, 씬 전환 후 복원은 AudioManager가 담당한다. 씬이 직접 `this.sound.play()`로 BGM을 관리하지 않는다.

맵별 BGM 매핑도 데이터로 관리됐다. `mapMusicConfig.js`에 맵 ID와 BGM 키를 연결해두면, WorldScene이 맵에 진입할 때 자동으로 AudioManager를 호출한다.

## 배포와 외부 피드백

끝까지 혼자만 본 프로젝트로 두지 않으려고 Vercel 배포도 붙였다. 브라우저에서 바로 열 수 있게 해두니, 그제야 다른 사람이 실제로 플레이해보고 피드백을 줄 수 있었다.

<img src="/images/posts/feloria-part3/vercel-deploy-success.png" alt="Feloria의 Vercel 배포 성공 화면" style="display:block; width:100%; max-width:760px; margin:1rem auto; border-radius:16px;" />
배포 성공 화면. 이 한 장 덕분에 로컬에서만 돌던 프로토타입이 '남이 직접 눌러볼 수 있는 게임'으로 넘어갔다.

특히 고마웠던 자료가 [Feloria 버그 리포트 by 지호](/files/feloria/feloria-bug-report-by-jiho.pdf)였다. 실제로 Vercel에 올린 빌드를 플레이한 뒤 직접 정리해준 리포트였고, 읽어보면 플레이어가 어디에서 막히는지가 아주 구체적으로 드러난다.

- 로딩바 고양이 연출, 조사 조사문구 조사 같은 UI 문장 처리
- 일반 공격 피드백 속도와 데미지 숫자 표시
- 골드와 포획 크리스탈을 평소에도 보이게 해달라는 자원 관리 문제
- 도감에서 빠져나올 때 입력 포커스가 메인 화면으로 새는 버그
- 상점에서 첫 포션 클릭 시 타입 에러
- 약초를 어떻게 캐는지 알기 어렵다는 퀘스트 전달 문제

마지막 항목이 특히 중요했다. "저기 있는 약초는 어떻게 캐나요..."라는 한 줄은, 내가 시스템을 만들었다고 생각한 것과 플레이어가 실제로 이해한 것 사이에 큰 간극이 있었다는 뜻이기 때문이다. 이 리포트는 Feloria가 아직 챕터 1 프로토타입이더라도, 적어도 남이 끝까지 눌러보며 문제를 짚어줄 만큼은 만들어졌다는 증거이기도 하다.

---

## 맵 데이터의 진화

초기 맵 데이터는 단순한 타일 인덱스 배열이었다. 개발이 진행되면서 레이어가 명확히 분리된 구조로 정착했다.

최종 Mosslight Path 맵 데이터 구조를 보면 변화가 보인다.

```json
{
  "id": "mosslight_path",
  "width": 20,
  "height": 20,
  "tileSize": 32,
  "layers": {
    "ground": [
      /* 지면 타일 배열 */
    ],
    "collision": [
      /* 1=막힘, 0=통과 */
    ],
    "encounter": [
      /* 1=풀숲조우 */
    ],
    "objects": [
      /* 표지판, 상자 */
    ],
    "npcs": [
      /* NPC 위치 */
    ],
    "warps": [
      /* 워프 포인트 */
    ]
  }
}
```

collision 레이어는 렌더링되지 않는다. 이 규칙은 처음부터 있었지만, 실제 맵 JSON이 완성되면서 각 레이어의 역할이 데이터 수준에서 명확하게 분리됐다는 게 중요하다. 코드 한 줄 바꾸지 않고 JSON만 수정해서 맵 레이아웃을 바꿀 수 있다.

---

## 지금 Feloria의 상태

3월 10일부터 12일까지 3일간 만들어진 것들을 정리하면:

**시스템 (동작하는 것):**

| 시스템                      | 상태 |
| --------------------------- | ---- |
| 탑다운 월드 탐험            | ✓    |
| 턴제 전투 (야생 + 트레이너) | ✓    |
| 포획 / 파티 / 도감          | ✓    |
| 레벨업 / 진화               | ✓    |
| 상점 / 인벤토리 / 골드      | ✓    |
| NPC 대화 + 퀘스트           | ✓    |
| 저장 / 불러오기             | ✓    |
| 타입 상성                   | ✓    |
| 스킬 애니메이션             | ✓    |
| 오디오 시스템               | ✓    |
| 보스전 + 컷신               | ✓    |
| 전설 복선 시스템            | ✓    |

**콘텐츠 (만들어진 것):**

- 크리처 100종, 스킬 200개
- 플레이 가능한 맵 5개 (Starwhisk Village, Greenpaw Forest, Mosslight Path, Ancient Forest, Mosslight Shrine)
- NPC 20명 이상, 맵별 반응형 대사
- 챕터 1 완성, 챕터 2~6 구조만 설계된 상태

**아직 없는 것:**

- Shadowvale Ruins 이후 챕터 구현
- 전설 크리처 실제 배틀
- 타입 상성 세부 밸런싱
- 모바일 최적화

---

## 회고: 이 프로젝트에서 실제로 배운 것

### "일단 연결"이 "잘 만들기"보다 중요하다

가장 많이 반복된 실수는 한 시스템을 너무 정교하게 만들려다가 연결을 못 하는 것이었다. 전투 UI를 예쁘게 만들다가 포획이 registry에 저장이 안 됐고, 맵을 화려하게 만들다가 워프가 끊겼다.

각 Phase에서 항상 같은 원칙으로 돌아왔다. "루프가 먼저다. 폴리시는 루프가 돌아간 다음에."

### 데이터 파일이 코드보다 더 오래 산다

씬 코드는 여러 번 다시 쓰였다. 하지만 `creatures.js`, `skills.js`, `typeChart.js`의 데이터는 한 번 만들어진 이후 크게 바뀌지 않았다. 데이터와 로직을 분리해두면 로직이 바뀌어도 데이터는 살아남는다.

반대로 대사 문자열을 씬 코드에 직접 박아 넣었던 초기 NPC들은 씬을 재작성할 때 대사까지 같이 사라졌다.

### 에러가 나지 않는 버그가 가장 무섭다

JavaScript 객체에서 같은 키를 두 번 쓰면 에러가 없다. 크리처가 조용히 덮어써진다. EXP가 registry에 저장되지 않아도 에러가 없다. 씬이 전환되면 값이 그냥 사라진다.

Feloria 개발 중 가장 오래 걸린 디버그는 전부 "에러가 없는데 이상하게 동작하는" 버그들이었다. 어떤 값이 어디에 있는지 항상 명시적으로 확인하는 습관이 중요했다.

### AI 어시스턴트와 개발하는 법

이 프로젝트는 AI(Antigravity)를 주 개발 도구로 사용했다. 그 과정에서 배운 것은 이렇다.

**잘 작동하는 것:** 코드 생성, 구조 제안, 데이터 파일 작성, 버그 원인 분석

**주의해야 하는 것:** 스코프 관리. 한 번에 너무 많이 요청하면 무너진다. 한 Phase가 끝날 때마다 "검증은 내가 직접 한다"는 조건을 명시하는 게 중요했다. AI가 검증했다고 주장하는 건 검증이 아니다.

**실제로 효과 있었던 것:** 버그를 직접 경험한 후에 "이런 버그가 다시 발생하지 않도록 방지 규칙을 같이 명시해서 다음 Phase를 작성해달라"는 방식. 이렇게 Phase 12에서 발생할 수 있는 버그를 Phase 5 시점에 미리 잡았다.

---

## 다음

Feloria는 챕터 1이 플레이 가능한 상태에서 멈춰 있다. Shadowvale Ruins부터의 챕터 2~6, 전설 크리처들과의 보스전, 라이벌 Riven과의 최종 대결이 남아 있다.

게임 개발을 배운 적 없는 상태에서 시작해, 3일 만에 턴제 RPG의 핵심 루프가 돌아가는 것을 눈으로 봤다. 그게 이 프로젝트에서 가장 놀라웠던 순간이었다. 레벨 1 Emberpaw가 처음으로 전투에서 이기고, 파티 화면에서 HP가 줄어든 채로 남아 있는 것을 확인했을 때.

저장이 되지 않던 세계가 저장되기 시작하는 순간처럼, 게임 개발도 어느 순간 "이게 실제로 돌아간다"는 감각이 온다.

## 그 감각이 오면 멈추기 어렵다.

## 콘텐츠와 세계관을 끝까지 밀어붙이며 느낀 점

게임 프로젝트 후반부로 갈수록 기술 구현보다 더 어려운 것은 일관성을 유지하는 일이다. 캐릭터 설정, 지역 분위기, 전투 보상, 탐험 동기, 서사 톤이 따로 놀기 시작하면 플레이 경험이 금방 산만해진다. Feloria 3편의 회고가 의미 있는 이유도, 콘텐츠 양 자체보다 이 일관성을 지키기 위해 어떤 판단을 했는지 기록하고 있다는 점에 있다.

## 특히 개인 프로젝트에서는 "내가 재미있다고 느끼는 것"과 "플레이어가 이해할 수 있는 것" 사이의 거리를 자주 점검해야 한다. 이 파트를 지나며 얻은 가장 큰 교훈은, 세계관은 텍스트 양으로 완성되는 것이 아니라 시스템과 플레이 동기 속에서 반복적으로 확인될 때 살아난다는 점이었다. <!-- short: 개인 프로젝트의 교훈 -->

## 플레이 링크

실제로 플레이해보려면 여기로 들어가면 된다.

[Feloria 플레이하기](https://feloria.vercel.app/)
