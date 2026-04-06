# Feloria 대화 내역 분할본 27

- 원본 파일: Feloria 대화 내역.docx
- 분할 번호: 27/28
- 문단 범위: 5134~5249
- 문자 수(대략): 25315

---

Approved — proceed with the centralized audio system implementation.

The overall direction is correct.
Implement it under the following exact rules so the current game remains stable and audio does not overlap or desync.

Important:
I will do all verification manually myself.
Do not claim anything is verified.

--------------------------------
PHASE GOAL
--------------------------------

Implement a centralized AudioManager for:
- BGM
- BGS
- ME
- SE

This must be the only system responsible for background music control.

Scenes may request music changes, but scenes must not directly manage overlapping BGM logic themselves.

--------------------------------
CORE ARCHITECTURE RULES
--------------------------------

1. AudioManager must be the single source of truth for audio playback.

2. Scenes should not directly use ad-hoc BGM playback such as:
- this.sound.play(...) for long-running music
except through AudioManager.

3. Separate audio responsibilities clearly:
- BGM = main background music
- BGS = looping ambient background sound
- ME = short musical event cue, may temporarily override BGM
- SE = short sound effects

4. Only one BGM may exist at a time.
5. Only one BGS may exist at a time.
6. Multiple SEs may overlap when reasonable.
7. ME should not permanently replace BGM.

--------------------------------
ASSET STRUCTURE RULES
--------------------------------

Register all audio in assetPaths.js under:

ASSETS.AUDIO = {
  BGM: {...},
  BGS: {...},
  ME: {...},
  SE: {...}
}

Use stable keys and exact file mappings.

--------------------------------
BGM MAPPING RULES
--------------------------------

Use the following mapping exactly:

Title screen:
- Airship

Intro scene:
- The Giant's Piano

Hyunseok quest reveal:
- Dark_-_Criminal_Mind

Maps:
- Starwhisk Village -> Happy Town Theme
- Greenpaw Forest -> Field2
- Mosslight Path -> Field1
- Ancient Forest -> Peaceful_-_Princess_Dreams
- Mosslight Shrine -> Dungeon9

Battles:
- Wild encounter -> Battle8
- Trainer Kyle -> Battle5
- Trainer Sera -> Battle Nothing
- Trainer Luke -> Abominations Of the Soul
- Rowan boss -> Namazu
- Chief Hyunseok boss -> In Darkness Where Elves Burn

Post-Rowan event:
- Scene2

--------------------------------
BGS RULES
--------------------------------

Legendary seal break sequence:
- play Quake as BGS

BGS must be independently stoppable from BGM.

--------------------------------
ME RULES
--------------------------------

Use exact ME mapping:

- Level up -> Fanfare1
- New cat capture success -> Fanfare2
- Evolution -> Fanfare3
- Game over -> Gameover1
- Receive item from NPC -> item
- Wild battle victory -> Victory1
- Trainer battle victory -> Victory2

ME behavior rule:
- ME may temporarily duck or pause BGM
- after ME finishes, the correct BGM must resume automatically if appropriate

--------------------------------
SE RULES
--------------------------------

Use exact SE mapping:

- Basic attack -> Attack3
- Encounter start -> Encounter
- Exit menu -> Cancel2
- Player cat death -> Collapse2
- Enemy death -> Collapse1
- Healing interaction -> Recovery
- Map transfer -> Move
- Menu/shop hover -> Cursor1

Skill SE mapping by type:
- fire -> Fire1
- ice -> Ice1
- thunder/storm -> Thunder1
- shadow -> Darkness1
- rock/earth -> Earth1
- wind/forest/grass -> Wind1
- light -> Light1
- mystic/spirit -> Special1
- fallback -> Attack3

--------------------------------
AUDIO MANAGER API RULES
--------------------------------

Implement methods such as:

- playBGM(key, options)
- stopBGM(options)
- playBGS(key, options)
- stopBGS(options)
- playME(key, options)
- playSE(key, options)
- pauseBGM()
- resumeBGM()
- setMapBGM(key)
- resumeMapBGM()

Recommended behavior:
- if playBGM is called with the same currently playing key, do not restart it unnecessarily
- if a new BGM is requested, stop or fade out the previous BGM first
- map BGM should be remembered so it can resume after battle or cutscene

--------------------------------
STATE MANAGEMENT RULES
--------------------------------

AudioManager should track:
- currentBGMKey
- currentBGSKey
- currentMapBGMKey
- previousBGMKey if needed
- whether ME is currently active

This state must be used to prevent overlap and wrong resumption.

--------------------------------
SCENE INTEGRATION RULES
--------------------------------

PreloadScene:
- load all ASSETS.AUDIO entries

StartScene:
- request title BGM through AudioManager

WorldScene:
- request map BGM through AudioManager based on current map
- request special story BGM only for explicit events
- resume correct map BGM after battle/cutscene

BattleScene:
- stop or suspend map BGM through AudioManager
- start battle BGM through AudioManager
- on battle end, play victory/defeat ME through AudioManager
- restore the correct map BGM after battle flow finishes

DialogScene:
- do not directly own BGM logic unless an explicit story trigger requires it

EvolutionScene:
- play Fanfare3 through AudioManager
- return safely to the appropriate BGM afterward

UIScene:
- SE only
- no direct BGM ownership

--------------------------------
BUG PREVENTION RULES
--------------------------------

Prevent all of the following:

1. Two BGMs playing simultaneously
2. BGM restarting every frame or every scene refresh
3. Battle ending and restoring the wrong map music
4. ME finishing and leaving the game silent
5. Entering a cutscene and failing to restore map music after it
6. Hover/cursor SE stacking too aggressively
7. Game over music conflicting with battle victory music
8. Audio instances being recreated constantly instead of reused
9. Scene shutdown causing orphaned looping sounds
10. Quake BGS continuing forever after the climax event

--------------------------------
PERFORMANCE RULES
--------------------------------

Reuse audio instances when possible.
Do not create unnecessary duplicate sound objects for the same long-running BGM/BGS.
SE may be more lightweight, but still avoid careless duplication.

--------------------------------
TRANSITION RULES
--------------------------------

Use fade behavior for BGM where appropriate, but keep it simple and reliable.
Correctness is more important than fancy transitions.

Recommended:
- fade out old BGM
- start new BGM
- restore map BGM after battle/cutscene using remembered key

--------------------------------
MANUAL TEST TARGETS
--------------------------------

Implement this so I can manually test:

1. Title screen plays Airship
2. Each current map plays the correct BGM
3. Wild/trainer/boss battles use the correct battle BGM
4. Victory and defeat MEs play correctly
5. Level up, capture, evolution, item receive all trigger the correct MEs
6. Skill SEs map correctly by type
7. Menu hover and exit sounds play correctly
8. No overlapping BGM occurs
9. After battle or cutscene, the correct map BGM resumes
10. Quake BGS plays only during the legendary release sequence and stops correctly

--------------------------------
FINAL NOTE
--------------------------------

This phase is only for stable centralized audio integration.
Do not mix in unrelated system refactors.
Preserve the currently working gameplay and add audio as a controlled layer on top.

이렇게 보내면 훨씬 안전합니다.
추가로 한 가지 더 권합니다.
현재 프로젝트 루트에 dist와 node_modules가 그대로 보이는데, 오디오 파일까지 넣기 시작하면 저장소가 더 무거워질 수 있으니 .gitignore 정리는 같이 해두는 게 좋습니다.

https://github.com/Evan-Yoon/Feloria 다 끝났어. 이제 코드 문제 없는지 쭉 봐줘. 그리고 src\game\data\graphics\startscene 이거는 필요하니까 지우거나 로직 바꾸지 말고. 다 끝났으면 맵도 좀 개선해줘. 맵을 이동할 때 막힌 곳이 있거나, 길을 따라가면 다음 맵으로 이어지지 않는데도 길이 넓게 되어 있는 부분 이런거 json으로 직접 작성해서 수정할 수 있게 맵을 많이 개선해줘

7m 31s 동안 생각함
쭉 봤고, 지금 상태는 “플레이 가능한 프로토타입”은 넘었는데, 유지보수성과 맵 정합성은 아직 손볼 부분이 남아 있는 상태입니다.
코드 쪽에서 바로 보이는 문제는 이렇습니다.
루트에 dist/와 node_modules/가 그대로 커밋돼 있습니다. 둘 다 버전 관리 대상이 아니고, 지금처럼 남겨두면 저장소가 불필요하게 무거워집니다. 또 루트에 append_creatures.js, classify_creatures.cjs, fix_phase14.cjs, phase14.cjs, phase14.js, localize_maps.cjs, translate_types.cjs, init_dirs.ps1 같은 작업용 스크립트가 다 섞여 있어서, 실제 런타임 코드와 일회성 도구가 분리되어 있지 않습니다.
README도 현재 구현 상태와 맞지 않습니다. 아직 “Features up to Phase 9”, “30 unique creatures”, “Trainer Red”, “Healing Herbs” 같은 설명이 남아 있는데, 지금 Feloria는 훨씬 더 많이 진행된 상태입니다. 이건 나중에 꼭 정리해야 합니다.
WorldScene.js는 기능이 너무 많이 몰려 있습니다. 맵 로딩, 플레이어 생성, NPC 생성, 전설 등장, 강제 인트로, 퀘스트 진행, 워프, 조우, 상호작용이 한 파일에 다 들어가 있습니다. 그리고 현재 preload()에서는 starwhisk_village, greenpaw_forest, mosslight_path만 미리 로드하는데, 같은 파일 안에서 ancient_forest와 mosslight_shrine까지 직접 사용하고 있습니다. 동작은 할 수 있어도 구조상 불안정합니다.
가장 눈에 띄는 실제 버그 후보는 NPC id 불일치입니다.
starwhisk_village.json에서는 촌장 스폰 id가 elder_hyunseok인데, WorldScene의 강제 대화와 특수 분기에서는 mira를 기준으로 처리하고 있습니다. 이런 식이면 인트로 대화, 감금 이벤트, 퀘스트 트리거가 특정 시점에 어긋날 가능성이 큽니다.
그리고 요청하신 부분은 분명히 지켰습니다.
src/game/data/graphics/startscene는 현재 프리로드에서 직접 import해서 쓰고 있으므로, 그 폴더는 지우면 안 됩니다. 이 부분은 그대로 유지하는 게 맞습니다.

맵 정합성 진단
현재 연결 구조는 이렇게 보입니다.
starwhisk_village 20×20 → 북쪽 4칸 워프 → greenpaw_forest 남쪽 4칸
greenpaw_forest 25×25 → 북쪽 4칸 워프 → mosslight_path 남쪽 4칸
mosslight_path 20×20 → 북쪽 4칸 워프 → ancient_forest 남쪽 4칸
ancient_forest 25×25 → 북쪽 4칸 워프 → mosslight_shrine 남쪽 4칸
연결 폭 자체는 일관적입니다.
그래서 문제는 “워프 개수”보다 길 폭과 맵 내부 동선이 실제 워프 폭과 안 맞는 것, 그리고 NPC가 길목을 애매하게 막는 것일 가능성이 높습니다.
특히 지금 스폰 좌표를 보면 길 한가운데 또는 길과 겹칠 수 있는 NPC가 있어 동선이 답답해질 수 있습니다.
starwhisk_village: elder_hyunseok (10,8), shopkeeper (13,7), villager1 (6,8), eugene (14,14)
greenpaw_forest: trainer_kyle (17,14), darin (10,18)
mosslight_path: trainer_sera (11,8), noah (11,15)
ancient_forest: trainer_luke (16,14), evan (8,14)
mosslight_shrine: ellie (7,8), trainer_guardian_rowan (7,3)

바로 적용 가능한 맵 개선 방향
지금 여기서 가장 안전한 수정은 두 단계입니다.
1. 스폰/워프 JSON부터 정리
이건 현재 스키마를 확실히 알고 있어서 바로 고칠 수 있습니다.
2. 타일 레이아웃은 “통로 폭 = 워프 폭” 원칙으로 재정리
이건 현재 한 줄짜리 raw JSON만으로 타일 인덱스를 안전하게 재작성하기 어려워서,
제가 여기서는 직접 붙여넣을 수 있는 JSON 블록과 수정 원칙까지 드리겠습니다.

1차 적용용 JSON 수정안
아래는 스폰/워프 정리용 직접 교체안입니다.
starwhisk_village.json의 spawns 추천 교체
촌장 id를 mira가 아니라 elder_hyunseok로 계속 쓸 거면, 나중에 WorldScene 분기를 맞춰야 합니다.
일단 맵 기준으로는 길목을 조금 비켜 배치하는 게 좋습니다.
"spawns": [
  { "type": "player", "x": 10, "y": 14 },

  { "type": "npc", "x": 10, "y": 7, "id": "elder_hyunseok", "name": "촌장 현석" },
  { "type": "npc", "x": 14, "y": 8, "id": "shopkeeper", "name": "상인 토비" },
  { "type": "npc", "x": 5, "y": 9, "id": "villager1", "name": "마을 주민 리나" },
  { "type": "npc", "x": 14, "y": 14, "id": "eugene", "name": "고양이 연구가 유진" },

  { "type": "npc", "x": 16, "y": 15, "id": "item_potion", "name": "숨겨진 포션" }
]
starwhisk_village.json의 warps 유지/정리
현재 폭 4칸은 괜찮습니다. 대신 마을 북쪽 길 폭도 정확히 4칸으로 맞추세요.
"warps": [
  { "x": 8, "y": 0, "targetMap": "greenpaw_forest", "targetX": 8, "targetY": 23 },
  { "x": 9, "y": 0, "targetMap": "greenpaw_forest", "targetX": 9, "targetY": 23 },
  { "x": 10, "y": 0, "targetMap": "greenpaw_forest", "targetX": 10, "targetY": 23 },
  { "x": 11, "y": 0, "targetMap": "greenpaw_forest", "targetX": 11, "targetY": 23 }
]

greenpaw_forest.json 추천 교체
남쪽 입구 직후 NPC가 바로 길을 막지 않도록 darin을 옆으로 살짝 이동시키는 편이 낫습니다.
"spawns": [
  { "type": "npc", "x": 17, "y": 14, "id": "trainer_kyle", "name": "초보 트레이너 카일" },
  { "type": "npc", "x": 13, "y": 18, "id": "darin", "name": "숲 탐험가 다린" },
  { "type": "npc", "x": 3, "y": 14, "id": "item_potion", "name": "숨겨진 아이템" }
]
워프는 그대로 써도 됩니다.
"warps": [
  { "x": 8, "y": 24, "targetMap": "starwhisk_village", "targetX": 8, "targetY": 1 },
  { "x": 9, "y": 24, "targetMap": "starwhisk_village", "targetX": 9, "targetY": 1 },
  { "x": 10, "y": 24, "targetMap": "starwhisk_village", "targetX": 10, "targetY": 1 },
  { "x": 11, "y": 24, "targetMap": "starwhisk_village", "targetX": 11, "targetY": 1 },

  { "x": 8, "y": 0, "targetMap": "mosslight_path", "targetX": 8, "targetY": 18 },
  { "x": 9, "y": 0, "targetMap": "mosslight_path", "targetX": 9, "targetY": 18 },
  { "x": 10, "y": 0, "targetMap": "mosslight_path", "targetX": 10, "targetY": 18 },
  { "x": 11, "y": 0, "targetMap": "mosslight_path", "targetX": 11, "targetY": 18 }
]

mosslight_path.json 추천 교체
noah와 trainer_sera가 중앙 축을 둘 다 차지하면 길 체감이 답답합니다. 한쪽으로 치우는 게 좋습니다.
"spawns": [
  { "type": "npc", "x": 13, "y": 8, "id": "trainer_sera", "name": "트레이너 세라" },
  { "type": "npc", "x": 9, "y": 15, "id": "noah", "name": "여행자 노아" }
]
워프는 유지 가능합니다.
"warps": [
  { "x": 8, "y": 19, "targetMap": "greenpaw_forest", "targetX": 8, "targetY": 1 },
  { "x": 9, "y": 19, "targetMap": "greenpaw_forest", "targetX": 9, "targetY": 1 },
  { "x": 10, "y": 19, "targetMap": "greenpaw_forest", "targetX": 10, "targetY": 1 },
  { "x": 11, "y": 19, "targetMap": "greenpaw_forest", "targetX": 11, "targetY": 1 },

  { "x": 8, "y": 0, "targetMap": "ancient_forest", "targetX": 8, "targetY": 23 },
  { "x": 9, "y": 0, "targetMap": "ancient_forest", "targetX": 9, "targetY": 23 },
  { "x": 10, "y": 0, "targetMap": "ancient_forest", "targetX": 10, "targetY": 23 },
  { "x": 11, "y": 0, "targetMap": "ancient_forest", "targetX": 11, "targetY": 23 }
]

ancient_forest.json 추천 교체
evan을 메인 동선에서 한 칸 옆으로 빼면 흐름이 자연스러워집니다.
"spawns": [
  { "type": "npc", "x": 16, "y": 14, "id": "trainer_luke", "name": "숲 수호자 견습 루크" },
  { "type": "npc", "x": 7, "y": 14, "id": "evan", "name": "신비한 여행자 에반" }
]
워프는 유지 가능합니다.
"warps": [
  { "x": 8, "y": 24, "targetMap": "mosslight_path", "targetX": 8, "targetY": 1 },
  { "x": 9, "y": 24, "targetMap": "mosslight_path", "targetX": 9, "targetY": 1 },
  { "x": 10, "y": 24, "targetMap": "mosslight_path", "targetX": 10, "targetY": 1 },
  { "x": 11, "y": 24, "targetMap": "mosslight_path", "targetX": 11, "targetY": 1 },

  { "x": 8, "y": 0, "targetMap": "mosslight_shrine", "targetX": 8, "targetY": 13 },
  { "x": 9, "y": 0, "targetMap": "mosslight_shrine", "targetX": 9, "targetY": 13 },
  { "x": 10, "y": 0, "targetMap": "mosslight_shrine", "targetX": 10, "targetY": 13 },
  { "x": 11, "y": 0, "targetMap": "mosslight_shrine", "targetX": 11, "targetY": 13 }
]

mosslight_shrine.json 추천 교체
신전은 보스 직선 진입 느낌을 더 살리는 편이 좋습니다.
"spawns": [
  { "type": "npc", "x": 6, "y": 8, "id": "ellie", "name": "순례자 엘리" },
  { "type": "npc", "x": 7, "y": 3, "id": "trainer_guardian_rowan", "name": "신전 수호자 로완" }
]
워프는 유지해도 됩니다.
"warps": [
  { "x": 8, "y": 14, "targetMap": "ancient_forest", "targetX": 8, "targetY": 1 },
  { "x": 9, "y": 14, "targetMap": "ancient_forest", "targetX": 9, "targetY": 1 },
  { "x": 10, "y": 14, "targetMap": "ancient_forest", "targetX": 10, "targetY": 1 },
  { "x": 11, "y": 14, "targetMap": "ancient_forest", "targetX": 11, "targetY": 1 }
]

2차 맵 레이아웃 개선 원칙
타일 레이아웃은 현재 제가 원본 layers.data를 안전하게 재작성할 정도로 다 열어볼 수는 없어서,
여기서는 직접 JSON 수정 시 적용할 원칙을 드립니다.
원칙 A: 길 폭 = 워프 폭
현재 워프가 4칸이면, 연결되는 길도 최소 4칸 폭으로 끝까지 유지하세요.
중간은 6칸인데 출구는 4칸이거나, 반대로 길은 넓은데 다음 맵이 안 이어지면 체감이 나쁩니다.
원칙 B: 다음 맵으로 안 가는 넓은 길은 막다른샛길 느낌으로 줄이기
다음 맵 워프가 없는 곳에 큰 대로가 있으면 플레이어가 헷갈립니다.
그런 곳은:
길 폭을 2~3칸으로 줄이거나
잔디/바위/나무로 시각적으로 막거나
아이템 루트처럼 보이게 따로 빼는 편이 좋습니다.
원칙 C: NPC는 중앙 축 길 정면에 두지 않기
NPC는 길 중심이 아니라 1칸 옆에 배치하는 게 좋습니다.
특히 입구/출구 근처에서는 더 그렇습니다.
원칙 D: 각 맵 입구/출구 앞 2칸은 비우기
워프 직후 플레이어가 바로 NPC, 바위, 풀숲에 끼이면 답답합니다.

꼭 고쳐야 할 코드 한 가지
맵보다 먼저, WorldScene의 NPC id 분기는 정리해야 합니다.
현재 맵은 elder_hyunseok를 쓰는데, 씬에서는 mira를 강하게 가정하는 코드가 남아 있습니다.
이건 나중에 분명히 문제 납니다. mira를 유지할 거면 맵 id를 통일하고, elder_hyunseok를 유지할 거면 WorldScene 분기를 다 맞춰야 합니다.

결론
지금 상태는:
프로젝트 구조상 돌아가긴 함
하지만
저장소 정리 필요
README 갱신 필요
WorldScene 책임 분산 필요
NPC id 통일 필요
맵은 워프/스폰 우선 정리가 가장 효과적
입니다.
원하시면 다음 단계로는 제가
starwhisk_village.json부터 mosslight_shrine.json까지, 붙여넣기 가능한 “개선판 전체 JSON” 형식으로 순서대로 작성해드리겠습니다.

starwhisk_village.json부터 mosslight_shrine.json까지, 붙여넣기 가능한 “개선판 전체 JSON” 형식

1m 26s 동안 생각함
좋습니다. 다만 한 가지는 분명히 말씀드리겠습니다.
현재 저장소의 맵 파일들은 starwhisk_village, greenpaw_forest, mosslight_path, ancient_forest, mosslight_shrine로 이어지는 구조이고, 제가 앞서 확인한 범위에서는 spawns와 warps는 읽을 수 있었지만, 원본 layers.data 전체를 이 환경에서 안전하게 복원해 그대로 덮어쓸 정도로 파싱하긴 어려웠습니다. 그래서 아래는 **현재 연결 구조를 유지하면서 길 정합성과 NPC 배치를 개선한 “직접 붙여넣기용 전체 JSON 초안”**입니다. 현재 맵 연결 순서 자체는 위 다섯 맵 기준으로 맞습니다.
중요한 점:
src/game/data/graphics/startscene는 건드리지 않았습니다.
아래 JSON은 지금 대화에서 파악된 현재 스키마를 최대한 보수적으로 가정한 개선안입니다.
만약 현재 코드가 layers.ground, layers.collision, layers.encounter 같은 필드명을 다르게 쓰면, 기존 필드명만 유지해서 값만 옮겨 넣으시면 됩니다.
아래는 길 폭 4칸 통일, 입구/출구 앞 공간 확보, NPC가 중앙 길목을 완전히 막지 않게 배치하는 방향으로 정리한 버전입니다.

starwhisk_village.json
{
  "id": "starwhisk_village",
  "name": "Starwhisk Village",
  "width": 20,
  "height": 20,
  "tileSize": 32,
  "layers": {
    "ground": [
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,
      2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,
      2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0
    ],
    "collision": [
      1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,
      1,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,1,
      1,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,1,
      1,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,1,
      1,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,1,
      1,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,1,
      1,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,1,
      1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
      1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
      1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
      1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
      1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
      1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
      1,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,1,
      1,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,1,
      1,0,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,0,0,1,
      1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1
    ],
    "encounter": [
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
    ]
  },
  "warps": [
    { "x": 8, "y": 0, "targetMap": "greenpaw_forest", "targetX": 8, "targetY": 23 },
    { "x": 9, "y": 0, "targetMap": "greenpaw_forest", "targetX": 9, "targetY": 23 },
    { "x": 10, "y": 0, "targetMap": "greenpaw_forest", "targetX": 10, "targetY": 23 },
    { "x": 11, "y": 0, "targetMap": "greenpaw_forest", "targetX": 11, "targetY": 23 }
  ],
  "spawns": [
    { "type": "player", "x": 10, "y": 14 },
    { "type": "npc", "x": 10, "y": 7, "id": "elder_hyunseok", "name": "촌장 현석" },
    { "type": "npc", "x": 14, "y": 8, "id": "shopkeeper", "name": "상인 토비" },
    { "type": "npc", "x": 5, "y": 9, "id": "villager1", "name": "마을 주민 리나" },
    { "type": "npc", "x": 14, "y": 14, "id": "eugene", "name": "고양이 연구가 유진" },
    { "type": "npc", "x": 16, "y": 15, "id": "item_potion", "name": "숨겨진 포션" }
  ]
}

greenpaw_forest.json
{
  "id": "greenpaw_forest",
  "name": "Greenpaw Forest",
  "width": 25,
  "height": 25,
  "tileSize": 32,
  "layers": {
    "ground": [
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,3,3,3,3,0,0,0,2,2,2,2,0,0,0,3,3,3,3,3,0,0,0,0,0,
      0,3,3,3,3,0,0,0,2,2,2,2,0,0,0,3,3,3,3,3,0,0,0,0,0,
      0,3,3,3,3,0,0,0,2,2,2,2,0,0,0,3,3,3,3,3,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,3,3,3,3,2,2,2,2,3,3,3,3,0,0,0,0,0,0,0,0,0,
      0,0,0,0,3,3,3,3,2,2,2,2,3,3,3,3,0,0,0,0,0,0,0,0,0,
      0,0,0,0,3,3,3,3,2,2,2,2,3,3,3,3,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,3,3,3,0,0,0,2,2,2,2,0,0,0,0,3,3,3,0,0,0,0,0,0,
      0,0,3,3,3,0,0,0,2,2,2,2,0,0,0,0,3,3,3,0,0,0,0,0,0,
      0,0,3,3,3,0,0,0,2,2,2,2,0,0,0,0,3,3,3,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,3,3,3,2,2,2,2,3,3,3,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,3,3,3,2,2,2,2,3,3,3,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,3,3,3,2,2,2,2,3,3,3,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,3,3,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,3,3,0,0,0,0,
      0,3,3,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,3,3,0,0,0,0,
      0,3,3,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,3,3,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,2,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0
    ],
    "collision": [
      1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,
      1,0,0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,0,1,1,1,1,1,
      1,0,0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,0,1,1,1,1,1,
      1,0,0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,0,1,1,1,1,1,
      1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,
      1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,
      1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,
      1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,
      1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,
      1,1,0,0,0,1,1,1,0,0,0,0,1,1,1,1,0,0,0,1,1,1,1,1,1,
      1,1,0,0,0,1,1,1,0,0,0,0,1,1,1,1,0,0,0,1,1,1,1,1,1,
      1,1,0,0,0,1,1,1,0,0,0,0,1,1,1,1,0,0,0,1,1,1,1,1,1,
      1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,
      1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,
      1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,
      1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,
      1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,
      1,0,0,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,0,0,1,1,1,1,
      1,0,0,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,0,0,1,1,1,1,
      1,0,0,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,0,0,1,1,1,1,
      1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,
      1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,
      1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,
      1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,
      1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1
    ],
    "encounter": [
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,
      0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,
      0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,1,1,1,1,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,
      0,0,0,0,1,1,1,1,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,
      0,0,0,0,1,1,1,1,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
      0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
      0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,
      0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,
      0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
      0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
    ]
  },
  "warps": [
    { "x": 8, "y": 24, "targetMap": "starwhisk_village", "targetX": 8, "targetY": 1 },
    { "x": 9, "y": 24, "targetMap": "starwhisk_village", "targetX": 9, "targetY": 1 },
    { "x": 10, "y": 24, "targetMap": "starwhisk_village", "targetX": 10, "targetY": 1 },
    { "x": 11, "y": 24, "targetMap": "starwhisk_village", "targetX": 11, "targetY": 1 },

    { "x": 8, "y": 0, "targetMap": "mosslight_path", "targetX": 8, "targetY": 18 },
    { "x": 9, "y": 0, "targetMap": "mosslight_path", "targetX": 9, "targetY": 18 },
    { "x": 10, "y": 0, "targetMap": "mosslight_path", "targetX": 10, "targetY": 18 },
    { "x": 11, "y": 0, "targetMap": "mosslight_path", "targetX": 11, "targetY": 18 }
  ],
  "spawns": [
    { "type": "npc", "x": 17, "y": 14, "id": "trainer_kyle", "name": "초보 트레이너 카일" },
    { "type": "npc", "x": 13, "y": 18, "id": "darin", "name": "숲 탐험가 다린" },
    { "type": "npc", "x": 3, "y": 14, "id": "item_potion", "name": "숨겨진 아이템" }
  ]
}

mosslight_path.json
