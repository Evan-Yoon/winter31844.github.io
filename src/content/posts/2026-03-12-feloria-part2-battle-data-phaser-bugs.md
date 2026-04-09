---
title: |
  [Feloria] Part 2
  전투 시스템과 버그
slug: feloria-part2-battle-data-phaser-bugs
date: 2026-03-12
author: Evan Yoon
category: project
subcategory: personal-project
description: Feloria 개발에서 가장 많이 고친 건 전투였다. EXP가 사라지고, 진화가 두 번 발동하고, 방향키를 누를 때마다 캐릭터가
  바뀌었다. 그 과정에서 배운 설계 판단들을 정리했다.
thumbnail: /images/posts/feloria-part2/cover.png
tags:
  - feloria
  - phaser3
  - javascript
  - game-dev
  - personal-project
  - battle-system
  - data-driven
  - troubleshooting
  - bugfix
readTime: 16
series: Feloria
seriesOrder: 2
featured: false
draft: false
toc: true
---

## 전투는 세 번 다시 쓰였다

첫 번째 전투는 화면이 뜨기만 했다. 두 번째는 싸울 수 있었지만 결과가 사라졌다. 세 번째부터 전투가 게임의 일부가 됐다.

Feloria 개발에서 가장 많이 손댄 시스템은 전투다. 단순하게 시작했지만, 경험치가 사라지고, 진화가 두 번 발동하고, EXP 바가 항상 0을 보여주는 문제들이 차례로 터졌다. 이 글은 그 과정과 그 안에서 내린 설계 판단들을 정리한 것이다.

<img src="/images/posts/feloria-part2/battle-logic-test-before-art.png" alt="고양이 아트 적용 전 전투 로직 테스트 화면" style="display:block; width:100%; max-width:780px; margin:1rem auto; border-radius:16px;" />
_고양이 사진을 넣기 전, 전투 로직이 최소한 돌아가는지만 먼저 확인하던 시기다. 보기엔 허전해도 이 검증이 없으면 이후 작업이 전부 흔들렸다._

<img src="/images/posts/feloria-part2/battle-scene-after-100-sprites.png" alt="100마리 스프라이트를 직접 크롭해 넣은 뒤의 전투 화면" style="display:block; width:100%; max-width:780px; margin:1rem auto; border-radius:16px;" />
_100마리 스프라이트를 직접 잘라 넣은 뒤의 전투 씬. 여기까지 오기 전까지 손으로 처리한 반복 작업이 정말 많았다._

---

## 전투 시스템 설계 및 개선 과정

### 처음부터 단순하게

Phase 3의 전투는 4가지 행동만 가졌다.

```
Attack  — 스탯 기반 데미지
Skill   — Attack보다 약간 강한 공격
Capture — 체력 낮을수록 성공률 증가
Run     — 야생 전투에서 탈출
```

단순하게 시작한 이유가 있었다. "탐험 → 야생 조우 → 전투 → 복귀" 루프가 끊기지 않고 한 번이라도 통과하는 것이 목표였기 때문이다. 타입 상성도, EXP도, 애니메이션도 없었다. 루프가 먼저고 나머지는 그 다음이었다.

한 가지만 처음부터 지킨 규칙이 있었다. **데미지 계산과 포획 확률은 `battleSystem.js`로 분리한다.**

```
BattleScene.js   → 전투 진행과 UI
battleSystem.js  → 데미지 계산, EXP, 레벨업, 진화 조건
```

나중에 타입 상성이 붙고, EXP 공식이 생기고, 진화 체크가 추가됐을 때 BattleScene 코드가 폭발하지 않은 건 이 분리 덕분이었다.

### 첫 번째 문제: 전투 결과가 사라졌다

전투를 마치고 WorldScene으로 돌아오면 파티 상태가 초기화됐다. HP가 깎였는데 원래대로 돌아오고, 포획한 고양이도 없어졌다.

원인은 생각보다 간단했다. BattleScene이 registry에 쓰지 않고 씬 로컬 변수에만 저장하고 있었다. 씬이 전환되면 로컬 변수는 사라진다.

해결 규칙을 하나 확정했다. **전투가 끝날 때 registry를 반드시 갱신한다.**

```js
// 전투 종료 시 registry 갱신 — 빠뜨리면 다 날아간다
this.registry.set('playerParty', updatedParty);
this.registry.set('playerCollection', updatedCollection);
this.registry.set('seenCreatureIds', seenIds);
this.registry.set('caughtCreatureIds', caughtIds);
```

이 규칙은 이후 Phase 내내 반복됐다. 씬이 끝날 때마다 registry에 쓴다. registry에 없으면 없는 것이다.

### 두 번째 문제: EXP가 보이지 않았다

전투에서 이겼는데 PartyScene의 EXP가 항상 0이었다.

UI에서만 EXP가 안 보이는 건지, 실제로 지급이 안 되는 건지 구분이 안 됐다. 그래서 **Phase 5.5**를 따로 만들었다. 새 기능을 넣기 전에 "지금 것이 진짜 작동하는지" 확인하는 단계였다.

두 가지를 추가했다.

첫째, 전투 결과 패널. 전투가 끝나고 WorldScene으로 돌아가기 전에 요약 화면을 띄웠다.

```
Victory
EXP Gained: 45
Total EXP: 120 / 250
Level Up: No
Evolution: No
```

둘째, PartyScene 디버그 컨트롤. 파티 화면에서 키를 눌러 직접 EXP를 주입할 수 있게 했다.

```
1 → 활성 크리처 +50 EXP
2 → 활성 크리처 +200 EXP
3 → 레벨 9로 세팅
4 → HP 전회복
```

중요한 건 이 디버그 도구들이 "실제 로직을 통과"하도록 구현했다는 것이다. 숫자만 바꾸는 게 아니라 `battleSystem.gainExp()`를 직접 호출해서, 전투에서 EXP를 받는 것과 똑같은 경로로 처리했다. 그래야 디버그 결과가 실제 게임과 같다.

이때 Emberpaw가 레벨 10에서 Cinderclaw로 진화하는 것이 처음으로 눈으로 확인됐다.

### 세 번째 문제: 진화가 두 번 발동했다

진화 메시지가 같은 전투 안에서 여러 번 뜨거나, 이미 Cinderclaw가 된 개체가 다시 진화 조건을 만족하는 문제가 있었다.

원인은 진화 체크가 여러 위치에서 중복 실행됐기 때문이었다. 해결은 두 가지였다.

- 진화 후 `readyToEvolve` 플래그를 즉시 초기화
- 진화 체크를 전투 결과 처리 시점에 **딱 한 번만** 실행

```js
if (creature.readyToEvolve) {
  evolveCreature(creature);
  creature.readyToEvolve = false; // 반드시 초기화
}
```

### 트레이너 전투: 야생과 다른 규칙

Phase 6에서 트레이너 전투가 추가됐다. 야생 전투와 다른 점은 두 가지다.

첫째, 트레이너는 크리처가 쓰러지면 다음 크리처를 내보낸다. BattleScene이 트레이너 파티 배열을 순서대로 소비하는 로직이 필요했다.

둘째, 트레이너 전투에서는 포획이 금지된다. `isTrainer: true` 플래그를 BattleScene에 넘겨 Capture 버튼을 비활성화했다.

트레이너 데이터는 `trainers.js`로 분리했다. 트레이너 ID, 이름, 전/후 대사, 파티 구성이 모두 데이터로 관리된다. 한 트레이너를 처치하면 `defeatedTrainers` 배열에 ID가 추가되고, 재상호작용 시 전투 대신 패배 후 대사가 나온다.

<img src="/images/posts/feloria-part2/battle-command-system.png" alt="교체 포획 아이템 도망 공격 스킬로 구성한 전투 커맨드 화면" style="display:block; width:100%; max-width:780px; margin:1rem auto; border-radius:16px;" />
_교체, 포획, 아이템, 도망, 공격, 스킬까지 한 화면 안에 정리하려고 했던 흔적. 전투를 '진짜 게임 시스템'처럼 보이게 만들고 싶었던 욕심이 잘 드러난다._

---

## 데이터 구조 설계

### creatures.js: 100종, 중복 키 함정

크리처 데이터는 `CREATURES` 객체 하나로 모든 종을 관리한다. 처음에는 71개를 선언했는데 실제로 런타임에서 유효한 크리처는 69개였다.

이유는 JavaScript 객체 특성 때문이었다. 같은 키를 두 번 선언하면 뒤의 값이 앞을 덮어쓴다. `VERDANTLYNX`와 `UMBRAFANG`이 숲 진화형과 전설형에 각각 중복 선언돼 있었고, 전설형이 진화형을 덮어써서 진화 라인이 런타임에서 조용히 깨졌다.

```js
// 이런 구조에서 뒤의 VERDANTLYNX가 앞을 덮어씀
VERDANTLYNX: { /* 숲 진화형 */ },
// ... 다른 크리처들 ...
VERDANTLYNX: { /* 전설형 */ }, // ← 앞의 것이 사라짐
```

JavaScript는 에러를 내지 않는다. 조용히 덮어쓰고 넘어간다. 이런 버그는 전투에서 진화형이 올바른 스킬을 쓰지 않는다거나, 도감 항목이 이상하게 보인다는 형태로 나중에야 드러난다.

해결은 전설형 ID를 별도로 바꾸는 것이었다. 진화형 `VERDANTLYNX`는 그대로 두고 전설형은 다른 ID를 부여했다.

100종으로 확장할 때 스킬 등급 체계도 함께 확정했다.

| 단계 | 스킬 등급 | 스킬 수 | 이유 |
|------|----------|---------|------|
| 기본형 | 3등급 | 3개 | 기초 스킬 |
| 진화형 | 2등급 | 3개 | 중급 스킬 |
| 최종 진화형 | 1등급 | 3개 | 고급 스킬 |
| 전설형 | 전설 고유 | 4개 | 고유 스킬셋 |

기본형이 최종 스킬을 쓰면 진화할 이유가 없어진다. 진화할수록 쓸 수 있는 스킬 등급이 높아지는 구조가 파티 육성의 장기 동기를 만든다.

### 에셋 구조: 역할 혼용 금지

그래픽 팩은 RPG Maker 스타일로 폴더 역할이 고정돼 있다. 처음에 이 규칙을 무시했을 때 어떤 일이 생겼는지 경험했기 때문에, 나중에는 규칙을 문서화해서 고정했다.

```
/assets/animations/     — 스킬 이펙트 전용
/assets/battlebacks1/   — 전투 배경 지면
/assets/battlebacks2/   — 전투 배경 하늘/환경
/assets/characters/     — 오버월드 이동 캐릭터만
/assets/faces/          — 대화창 초상화만
/assets/tilesets/       — 맵 타일 전용
/assets/sprites/        — 크리처 전투 스프라이트
```

전투 배경은 지형 타입에 따라 두 레이어를 조합한다.

```
숲  → Grassland  + Forest1
신전 → Ruins1    + Temple
동굴 → RockCave1 + Cliff
화산 → Lava1     + Lava
설원 → Snowfield + Snow
```

스킬 애니메이션도 속성별로 고정했다. Fire 스킬에서 회복 연출이 나오는 혼선을 없애기 위해서다.

```
Fire   → Fire*
Ice    → Ice*
Storm  → Thunder*
Shadow → Darkness*
회복   → Heal*
필살   → Special*, Meteor
```

에셋 경로는 모두 `assetPaths.js` 하나에서 관리한다. PreloadScene이 이 파일을 순회하며 로드하고, 씬들은 경로 대신 에셋 키만 참조한다. 경로가 바뀌어도 `assetPaths.js` 한 곳만 수정하면 된다.

<img src="/images/posts/feloria-part2/wrong-rpgmaker-crop.png" alt="RPG Maker 스프라이트를 잘못 크롭한 초기 실수" style="display:block; width:100%; max-width:760px; margin:1rem auto; border-radius:16px;" />
_RPG Maker 스프라이트를 가져왔지만 처음엔 크롭부터 잘못됐다. 에셋을 가져오는 것과 제대로 파싱하는 것은 전혀 다른 문제였다._

<img src="/images/posts/feloria-part2/npc-cat-mistake.png" alt="사람 NPC가 고양이처럼 잘못 구현된 장면" style="display:block; width:100%; max-width:760px; margin:1rem auto; border-radius:16px;" />
_사람이어야 할 NPC가 고양이처럼 나와버린 장면. 에셋 역할 혼용 금지라는 규칙을 몸으로 배운 대표적인 사례였다._

<img src="/images/posts/feloria-part2/rpgmaker-full-sprite-attempt.png" alt="전체 스프라이트를 RPG Maker 요소로 바꿔보려다 포기한 시도" style="display:block; width:100%; max-width:820px; margin:1rem auto; border-radius:16px;" />
_전체 스프라이트를 RPG Maker 요소로 갈아엎는 것도 잠깐 시도했다. 하지만 이건 구현보다 노가다가 더 커질 거라는 걸 금방 깨닫고 멈췄다._

---

## Phaser 전환 과정에서의 설계 판단

### 왜 전환했는가

WorldScene이 두 번 placeholder 상태로 되돌아갔다. React+Canvas에서, 그 다음 Canvas 유지+맵 엔진 재설계에서도 월드는 "검은 배경 위의 색 블록"을 벗어나지 못했다.

당시의 판단을 그대로 옮기면 이렇다.

> "검은 화면에 단순 사각형이 움직이는 것은 의도된 최종 화면이 아니다. WorldScene 자체가 placeholder 코드로 되어 있고, 진짜 타일맵, 카메라, 충돌, 워프, NPC 배치가 제대로 구현된 상태가 아니다."

문제는 렌더러가 아니었다. **엔진 없이 엔진이 해야 할 것들을 직접 구현하려 했다는 것**이 문제였다. 카메라 팔로우, 타일맵 레이어 렌더링, 씬 전환, 충돌 레이어 분리는 게임 엔진이 기본 제공하는 기능이다. Phaser 3는 이것들을 엔진 수준에서 준다.

TypeScript를 버린 것도 판단이었다. strict mode는 코드 안전성을 높이지만, 게임 개발에 처음 입문한 상태에서 타입 에러와 씨름하며 게임 로직을 동시에 짜는 건 너무 느렸다. JavaScript로 바꾸면서 코드를 읽고 수정하는 속도가 눈에 띄게 빨라졌다.

### 씬 구조에서 가장 중요한 판단

DialogScene을 WorldScene에 합치지 않는 것이었다.

대화 UI를 WorldScene 안에 직접 넣으면 "대화 중인지 아닌지"에 따라 입력 처리, 카메라, 플레이어 이동이 모두 분기돼야 한다. DialogScene을 별도 overlay 씬으로 분리하면 WorldScene은 "대화 중에는 일시 정지"만 유지하면 된다.

```
WorldScene (배경에서 유지)
    ↑ overlay
DialogScene / MenuScene / CodexScene / QuestScene
```

어떤 overlay 씬이 열려 있든 WorldScene이 배경에서 살아 있고, overlay가 닫히면 탐험이 재개된다. 씬이 많아질수록 이 구조의 효과가 커졌다.

### registry를 단일 상태 소스로

React에서 Zustand가 했던 역할을 Phaser registry가 대신한다.

씬이 바뀌어도 데이터가 살아남아야 하는 모든 것은 registry를 통해서만 읽고 쓴다. 씬 로컬 변수에만 저장한 데이터는 씬이 전환되는 순간 사라진다. 이미 한 번 경험한 실패였다.

```js
// registry에 없으면 없는 것이다
this.registry.get('playerParty')
this.registry.get('playerGold')
this.registry.get('defeatedTrainers')
```

이 규칙이 무너지는 순간 씬 간 상태 불일치가 생긴다. PartyScene에서는 진화한 것처럼 보이는데 BattleScene에서는 진화 전 형태가 나오거나, 상점에서 구매한 아이템이 전투에서 사용이 안 되는 것들이 모두 이 규칙의 위반이었다.

---

## 가장 큰 기술적 문제와 해결

### 문제 1: 방향키를 누르면 캐릭터가 바뀌었다

RPG Maker 스타일 그래픽 팩을 적용한 직후, 방향키를 누를 때마다 플레이어가 다른 사람으로 바뀌는 버그가 생겼다.

`Actor1.png`를 보면 한 이미지 안에 여러 캐릭터가 격자로 배치돼 있다. 각 캐릭터는 3열×4행의 블록을 차지한다.

```
3열 = 걷기 3프레임 (왼발, 정면, 오른발)
4행 = 방향 (하/좌/우/상)
```

문제의 구현은 시트 전체를 하나의 spritesheet로 처리하고 방향 전환 시 프레임 인덱스를 증가시켰다. 결과적으로 "왼쪽 방향 전환 = 다음 캐릭터 블록으로 이동"이 됐다.

```
잘못된 방식
Actor1.png 전체를 spritesheet로 처리
방향키 누를 때마다 다음 프레임 인덱스 이동
→ 인접한 다른 캐릭터 칸까지 넘어감

맞는 방식
Actor1.png에서 캐릭터 한 명의 3×4 블록만 선택
그 블록 안에서만
  - 방향 전환 = 행(row) 변경
  - 걷기 애니메이션 = 같은 행의 3프레임 순환
```

해결은 `getCharacterFrames(textureKey, characterIndex)` 헬퍼를 만드는 것이었다. 시트 크기에서 블록 수를 계산하고, 선택한 캐릭터 인덱스의 오프셋을 구해 12개 프레임 번호만 반환한다.

```
idle   → 현재 방향의 3프레임 중 가운데(인덱스 1)
moving → 현재 방향의 0, 1, 2 프레임 순환
```

플레이어는 항상 같은 `characterIndex`를 쓴다. 방향 전환은 "블록 안에서 행을 바꾸는 것"이지 "다른 캐릭터로 넘어가는 것"이 아니다.

이게 단순한 시각적 버그처럼 보이지만 사실 더 큰 문제였다. 화면에 보이는 캐릭터 위치와 실제 충돌/상호작용 판정 위치가 어긋나기 시작하면 게임 전체의 신뢰도가 무너진다.

<img src="/images/posts/feloria-part2/stuck-in-wall.png" alt="주인공이 벽에 끼어버린 장면" style="display:block; width:100%; max-width:760px; margin:1rem auto; border-radius:16px;" />
_충돌 처리가 어긋나면 이런 식으로 주인공이 벽에 끼었다. 에러가 뜨지 않아 더 귀찮은 종류의 문제였다._

<img src="/images/posts/feloria-part2/story-scene-npc-tangled.png" alt="스토리 흐름이 꼬여 NPC 위치와 씬이 뒤엉킨 장면" style="display:block; width:100%; max-width:760px; margin:1rem auto; border-radius:16px;" />
_스토리 흐름이 뒤죽박죽이 되면서 NPC 위치와 씬 전환이 같이 꼬인 장면. 시스템을 늘린다고 세계가 생기지 않는다는 걸 다시 확인했다._

### 문제 2: 저장 없이 상태성 시스템이 쌓였다

Phase 7까지 개발하면서 게임 안에 상태성 데이터가 많이 쌓였다.

- 파티, 포획 컬렉션
- 골드, 인벤토리
- 퀘스트 진행 상태
- 트레이너 처치 기록
- 도감 seen/caught

이 모든 것이 새로고침하면 초기화됐다. 브라우저 게임에서 새로고침은 언제든 일어난다.

> 레벨 15, 포획 8마리, 골드 500, 퀘스트 3개 완료
> → 새로고침
> → Lv1 스타터, 몬스터 0, 골드 0, 퀘스트 초기화

localStorage 기반 Save/Load는 Phase 8에서 처리됐다. 한 가지 판단이 있었다. 저장 시스템을 너무 일찍 만들면 저장 항목이 계속 바뀌면서 구조가 복잡해진다. Phase 3~7을 거치면서 registry에 들어가야 할 데이터가 확정됐고, 그때 saveSystem을 만들면 불필요한 필드 없이 깔끔하게 설계할 수 있었다.

saveSystem이 저장하는 것들은 결국 registry에 있는 모든 상태다.

```js
// saveSystem.js 저장 항목
const saveData = {
  playerName, selectedStarter,
  playerParty, playerCollection,
  playerInventory, playerGold,
  seenCreatureIds, caughtCreatureIds,
  activeQuests, completedQuests,
  defeatedTrainers,
  currentMapId, playerPosition,
  saveTimestamp, saveVersion
};
localStorage.setItem('feloria_save', JSON.stringify(saveData));
```

저장이 붙기 전과 붙은 후는 플레이 경험이 완전히 다르다. 저장은 기능이 아니라 게임을 게임답게 만드는 전제 조건이다.

---

## 이 단계에서 배운 것

이 글을 쓰면서 정리된 판단들을 한 줄씩 쓰면 이렇다.

**전투 설계:** 루프가 먼저다. 단순하더라도 끝까지 연결되는 것이 타입 상성 10개보다 낫다.

**데이터 분리:** 씬과 데이터를 섞으면 데이터가 바뀔 때마다 씬을 고쳐야 한다. creatures.js가 BattleScene, PartyScene, CodexScene 어디에서도 같은 데이터를 보는 건 구조가 맞기 때문이다.

**Phaser 판단:** 엔진이 해주는 것을 직접 만들려 하지 말아야 한다. 카메라, 타일맵, 씬 전환 — 이것들은 이미 해결된 문제다.

**스프라이트 파싱:** 라이브러리가 만든 에셋 형식에는 반드시 그 형식의 파싱 규칙이 있다. RPG Maker 캐릭터 시트를 임의의 프레임 시트처럼 다루면 반드시 깨진다.

**저장 시점:** 상태성 시스템이 쌓인 다음에 저장을 붙이는 게 맞다. 단 너무 늦게 붙이면 그 전까지의 테스트가 다 초기화되는 고통을 반복해야 한다.

<img src="/images/posts/feloria-part2/crop-helper-site.png" alt="ChatGPT가 10개씩 만든 사진을 정리하기 위해 찾은 크롭 보조 사이트" style="display:block; width:100%; max-width:760px; margin:1rem auto; border-radius:16px;" />
_ChatGPT가 여러 장씩 만든 이미지를 정리하려고 별도의 크롭 사이트까지 동원했다. 자동 생성이 끝이 아니라, 그 결과물을 사람이 다시 다듬는 시간이 훨씬 길었다._

<img src="/images/posts/feloria-part2/manual-100-cat-cropping-and-naming.png" alt="100마리 고양이 스프라이트를 손으로 잘라 카카오톡에 붙여넣고 네이밍한 작업 흔적" style="display:block; width:100%; max-width:920px; margin:1rem auto; border-radius:16px;" />
_크기가 전부 다른 100마리 고양이를 손으로 잘라 카카오톡에 붙여넣고 이름을 붙였다. Feloria에서 내가 실제로 가장 많이 쓴 자원은 코드보다도 반복 노동에 가까웠다._

---

다음 글에서는 월드 구성을 다룬다. NPC 대사 데이터를 어떻게 설계했는지, 지역마다 다른 조우 테이블을 어떻게 관리했는지, 그리고 진짜 RPG처럼 느껴지기 시작한 시점이 언제였는지를 이야기할 것이다.
---

## 전투 구현보다 더 오래 남은 것은 디버깅 감각이었다

전투 데이터와 Phaser 버그를 다룬 과정은 단순히 오류를 고친 기록이 아니라, 게임 로직과 엔진 동작을 어떻게 분리해서 볼 것인지 배운 과정에 가까웠다. 게임 개발에서는 화면에 드러나는 현상이 같아 보여도 원인이 데이터 구조에 있을 수도 있고, 타이밍 문제일 수도 있으며, 엔진 라이프사이클과 충돌하는 경우도 많다. 이걸 한 덩어리로 보면 디버깅이 끝없이 늘어진다.

그래서 이 파트에서 의미 있었던 부분은 증상을 줄줄 나열하는 대신, 상태 전이와 데이터 흐름을 중심으로 문제를 다시 보는 습관이 생겼다는 점이다. 이런 감각은 이후 콘텐츠를 늘릴 때도 그대로 도움이 된다. 결국 버그를 하나 해결하는 것보다, 비슷한 문제를 더 빨리 좁혀갈 수 있는 기준을 만드는 편이 훨씬 큰 자산이 된다.
