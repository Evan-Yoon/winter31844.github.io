# Feloria 대화 내역 분할본 16

- 원본 파일: Feloria 대화 내역.docx
- 분할 번호: 16/28
- 문단 범위: 2956~3209
- 문자 수(대략): 23798

---

Feloria - Phase 10: First Boss Chapter and New Region Expansion

The current Feloria project has reached a stable gameplay foundation.

Existing systems include:
- exploration
- wild encounters
- trainer battles
- capture mechanics
- leveling and evolution
- healing
- shop and inventory
- codex
- quest progression
- save/load
- visual overworld tiles and sprites

Now proceed to Phase 10.

Goal:
Create the first real story chapter and boss challenge.

--------------------------------
PHASE 10 FEATURES
--------------------------------

1. New maps

Add two new connected maps:

Ancient Forest
Mosslight Shrine

These should connect to the existing map progression:

Starwhisk Village
→ Greenpaw Forest
→ Mosslight Path
→ Ancient Forest
→ Mosslight Shrine

Ancient Forest:
- dense forest map
- stronger wild creatures
- trainer encounters

Mosslight Shrine:
- small sacred ruin
- boss battle location

2. First Boss Trainer

Add a boss trainer or guardian battle in Mosslight Shrine.

Example:

Name: Guardian Rowan

Party example:
- Mosslynx Lv12
- Thornmane Lv14

Boss battles should:
- use trainer battle logic
- reward large EXP and Gold
- trigger story dialogue after victory

3. Boss reward

After defeating the boss:

Give the player a special reward such as:

Forest Badge
or
Ancient Relic

This reward should unlock further world progression.

4. New creature additions

Add several new feline creatures tied to the forest region.

Examples:

Thornkit → Thornmane  
Mosslynx → Verdantlynx  
Vinefang  

These creatures should appear in Ancient Forest encounters.

5. Quest progression

Add a new quest after "First Steps":

Quest: Forest Awakening

Objectives:
- Explore Mosslight Path
- Enter Ancient Forest
- Defeat Guardian Rowan at Mosslight Shrine

Completing the quest unlocks the next region.

--------------------------------
CONSTRAINTS
--------------------------------

Do not break existing systems.

Ensure compatibility with:
- save/load
- party system
- codex tracking
- evolution
- inventory
- trainer defeat persistence

--------------------------------
MANUAL TEST TARGETS
--------------------------------

The following must be testable:

- new maps load correctly
- warps between all maps work
- new creatures appear in encounters
- boss battle starts correctly
- defeating boss grants reward
- quest updates after boss defeat
- save/load preserves boss completion state

--------------------------------
FINAL NOTE
--------------------------------

The goal of Phase 10 is to turn Feloria from a sandbox prototype into the first real playable chapter of the game.

앞으로 남은 큰 단계
Feloria를 완성 게임 방향으로 보면 앞으로는 보통 이렇게 갑니다.
Phase 10
첫 챕터 / 보스

Phase 11
사운드 + 애니메이션

Phase 12
UI polish

Phase 13
새 지역 + 몬스터 확장

Phase 14
밸런싱

솔직한 평가
지금 Feloria 상태는 개인 프로젝트 기준으로 꽤 잘 진행된 상태입니다.
특히 이미 구현된 것:
전투
포획
진화
상점
저장
퀘스트
이 정도면 사실 대부분 인디 몬스터 RPG 프로젝트가 못 가는 단계입니다.
지금부터는 엔진 개발이 아니라 게임 디자인 단계입니다.

원하시면 제가 다음 답변에서
**Feloria를 훨씬 게임답게 만드는 “맵 디자인 규칙 10개”**도 알려드리겠습니다.
이걸 적용하면 지금 맵 퀄리티가 바로 좋아집니다.

응 알려줘

좋습니다. 지금 Feloria는 엔진과 시스템이 거의 다 만들어진 상태라서 앞으로 게임의 완성도를 좌우하는 건 맵 디자인입니다.
아래는 **RPG 스타일 게임에서 많이 쓰는 “맵 디자인 규칙 10개”**입니다. Phaser 같은 엔진에서도 그대로 적용할 수 있습니다.

Feloria 맵 디자인 규칙 10가지
1. 플레이어가 갈 길은 항상 “눈에 보이게”
좋은 맵은 플레이어가 어디로 가야 하는지 자연스럽게 보입니다.
예:
길(Path)
강
나무 벽
절벽
이런 요소로 자연스럽게 길을 만듭니다.
예시 구조:
Village
  ↓
Forest Path
  ↓
Mosslight Path
  ↓
Ancient Forest

2. 막힌 길은 자연스럽게 막기
보이지 않는 벽 대신 환경으로 막아야 합니다.
좋은 방법:
나무
바위
물
절벽
울타리
나쁜 방법:
보이지 않는 collision

3. 맵은 “큰 길 + 작은 탐험” 구조
맵은 항상 두 가지가 있어야 합니다.
1️⃣ 메인 루트
2️⃣ 탐험 공간
예:
      hidden item
         ↓
   🌲🌲🌲
   🌲   🌲
   🌲   🌲
길 →  🌲   🌲
   🌲   🌲
   🌲🌲🌲
즉 메인 길 옆에 작은 탐험 공간을 넣습니다.

4. 플레이어를 자연스럽게 유도하기
맵에서 플레이어를 보스나 이벤트로 유도해야 합니다.
예:
Village
   ↓
Forest
   ↓
Broken Bridge
   ↓
Shrine (Boss)
브리지 같은 장애물이 있으면 플레이어는 자연스럽게 그쪽으로 갑니다.

5. 맵 크기는 너무 크게 만들지 않기
초보자가 가장 많이 하는 실수입니다.
❌ 나쁜 예
100x100 맵
좋은 크기:
Village: 20x20
Forest: 30x30
Route: 25x25
Shrine: 15x15

6. NPC는 항상 의미 있게 배치
NPC는 그냥 세워두면 안 됩니다.
좋은 위치:
교차로
입구
보스 앞
상점 근처
예:
Village
  NPC: 힌트
Forest Entrance
  NPC: 위험 경고
Boss Shrine
  NPC: 스토리

7. 전투 구역을 명확하게
플레이어는 어디서 몬스터가 나오는지 알아야 합니다.
예:
길 = 안전
Tall Grass = 조우
그래서 RPG에서는 항상:
🌱 Tall Grass
같은 요소가 있습니다.

8. 랜드마크를 만들어라
플레이어가 기억할 수 있는 장소가 필요합니다.
예:
거대한 나무
폐허
탑
신전
폭포
예:
Ancient Tree
플레이어는 이렇게 기억합니다.
"아 그 큰 나무 있던 맵"

9. 맵은 항상 “앞으로 확장 가능”하게
처음부터 이런 구조로 만드는 게 좋습니다.
Village
  ↓
Forest
  ↓
Route
  ↓
Boss Shrine
하지만 옆길도 만들어둡니다.
Forest
 ├ Route A
 └ Route B (나중에 추가)

10. 보스 앞에는 항상 “준비 구역”
보스 바로 앞에는 이런 걸 둡니다.
예:
힐 NPC
저장 포인트
상점
짧은 스토리
예:
Forest Shrine
   ↓
Healing NPC
   ↓
Boss

Feloria에 적용하면
지금 구조 기준으로 추천 맵 흐름은 이렇게입니다.
Starwhisk Village
      ↓
Greenpaw Forest
      ↓
Mosslight Path
      ↓
Ancient Forest
      ↓
Mosslight Shrine (Boss)
그리고 탐험 루트:
Forest
 ├ hidden item
 ├ rare cat spawn
 └ trainer battle

맵 디자인 팁 (진짜 중요)
좋은 맵은 항상 이렇게 느껴집니다.
"길을 따라가면 다음 지역이 나오고
옆으로 가면 탐험이 있다."
즉:
Main Path
+ Side Exploration
이게 핵심입니다.

Feloria에서 다음으로 추천하는 작업
지금 가장 효과 큰 작업은 이것입니다.
1️⃣ Ancient Forest 맵 만들기
2️⃣ Mosslight Shrine 보스 맵
3️⃣ Forest Guardian 보스 추가
그러면 게임이 첫 챕터 완성 느낌이 납니다.

원하시면 다음 답변에서
**Feloria에 딱 맞는 “맵 5개 실제 레이아웃 설계”**도 만들어드리겠습니다.
(타일 단위로 설계해 드립니다.)

방금 말해준 10개 규칙을 지금 만든 게임에 안티그래비티 프롬프트로 실제로 적용할 수 있게 만들어줘

좋습니다.
아래 프롬프트는 방금 설명한 10가지 맵 디자인 규칙을 Feloria 프로젝트에 실제로 적용하도록 만드는 안티그래비티용 프롬프트입니다.
핵심 목적은:
지금 만든 맵을 다 갈아엎는 것이 아니라
구조를 RPG 스타일로 정리
탐험 루트 + 보스 루트 + 숨겨진 요소 추가
입니다.
그대로 복사해서 보내시면 됩니다.

Feloria Map Design Application Prompt
Feloria - Map Design Improvement Pass

Apply RPG-style map design improvements to the existing Feloria maps.

Do NOT rewrite the entire game or break existing systems.
This task focuses only on improving map layout and exploration flow.

Use the existing Phaser map structure and current systems:
- collisions
- encounters
- NPC interaction
- warps
- quests
- save/load

--------------------------------
CORE MAP DESIGN RULES
--------------------------------

Apply the following rules to the current maps.

1. Visible navigation paths
The player must always be able to visually understand where to go.

Use clear path tiles and terrain shapes to guide the player.

Main paths should clearly connect:

Starwhisk Village
→ Greenpaw Forest
→ Mosslight Path
→ Ancient Forest
→ Mosslight Shrine

2. Natural map boundaries
Avoid invisible walls.

Use environmental elements to block movement:

- trees
- rocks
- cliffs
- water
- fences

Collision should match visible terrain.

3. Main path + side exploration
Each map must contain:

- one clear main path
- at least two optional side exploration areas

Examples:
- hidden item location
- rare creature encounter zone
- optional trainer battle

4. Exploration rewards
Side areas should reward exploration with at least one of the following:

- hidden item
- rare cat spawn area
- NPC dialogue hint
- shortcut path

5. Proper map scale
Maps should remain moderate in size.

Recommended sizes:

Village: 20x20 to 25x25
Forest: 25x25 to 35x35
Route: 20x20 to 30x30
Shrine: 15x15 to 20x20

Avoid extremely large empty maps.

6. NPC placement
NPCs should be placed intentionally.

Good locations:
- crossroads
- map entrances
- near important landmarks
- before boss areas

Example NPC roles:
- hint NPC
- trainer NPC
- shopkeeper
- healer

7. Encounter zones
Wild encounters must only happen in specific areas.

Rules:
- Tall grass areas trigger encounters
- Paths and open ground should be safe
- Rare creatures may appear in special grass zones

8. Landmarks
Each map should contain a recognizable landmark.

Examples:
- giant tree
- ruined shrine
- ancient statue
- waterfall
- glowing stone

These landmarks help players remember locations.

9. Future expansion paths
Maps should contain blocked paths that can later open.

Example:
Forest split:

Main Path
→ Mosslight Path

Blocked Path
→ Future Region

Use rocks, fallen trees, or broken bridges to block the route initially.

10. Boss preparation area
Before any boss battle location, add a preparation zone.

Example structure:

Ancient Forest
→ Healing NPC
→ Boss Shrine Entrance
→ Boss Battle

Players should have time to heal and prepare.

--------------------------------
MAPS TO IMPROVE
--------------------------------

Apply these rules to the following maps:

Starwhisk Village
Greenpaw Forest
Mosslight Path

Additionally create two new maps if they do not yet exist:

Ancient Forest
Mosslight Shrine

--------------------------------
MAP CONNECTION STRUCTURE
--------------------------------

Starwhisk Village
→ Greenpaw Forest
→ Mosslight Path
→ Ancient Forest
→ Mosslight Shrine

Ancient Forest should contain stronger creatures.

Mosslight Shrine should be a small boss map.

--------------------------------
EXPLORATION CONTENT
--------------------------------

Add at least the following elements:

Greenpaw Forest
- hidden item area
- rare cat encounter grass

Mosslight Path
- trainer NPC
- optional path branch

Ancient Forest
- stronger encounters
- landmark tree or ruin

Mosslight Shrine
- boss trainer or guardian creature

--------------------------------
GAMEPLAY CONSTRAINTS
--------------------------------

Do not break existing systems:

- save/load
- quest tracking
- inventory
- codex
- battle logic
- NPC interaction

Only adjust map layout, tile placement, and object placement.

--------------------------------
GOAL
--------------------------------

After this update, Feloria maps should feel like real RPG exploration spaces instead of simple prototype layouts.

Players should experience:

Village
→ Forest exploration
→ Route challenges
→ Boss shrine
→ Chapter completion

이 프롬프트의 효과
이걸 넣으면 안티그래비티가 다음을 하게 됩니다.
맵을 RPG 구조로 재정리
숨겨진 아이템 위치 생성
탐험 루트 추가
보스 전용 맵 생성
NPC 위치 정리
조우 지역 정리
즉 지금 시스템 위에 실제 게임 디자인을 얹는 작업입니다.

추천
지금 Feloria 단계에서 가장 큰 체감 업그레이드는 사실 이것입니다.
1️⃣ 맵 디자인 개선
2️⃣ 보스 챕터 추가
그래서 다음 작업은
Map Design Pass
→ Boss Shrine
→ First Boss Battle
이 흐름이 좋습니다.

원하시면 제가 다음 답변에서
**Feloria 맵을 실제로 쓸 수 있는 “타일 좌표 기반 레이아웃”**도 만들어드리겠습니다.
(그걸 넣으면 맵 퀄리티가 확 좋아집니다.)

응 타일 좌표 기반 레이아웃까지 포함해서 위 답변이랑 합쳐줘

좋습니다.
아래는 Feloria Map Design + 타일 좌표 기반 레이아웃까지 포함된 완전한 안티그래비티 프롬프트입니다.
이 프롬프트는 다음을 동시에 합니다.
RPG 맵 디자인 규칙 적용
기존 맵 구조 유지
탐험 루트 추가
보스 맵 추가
타일 좌표 기반 레이아웃 생성
기존 시스템과 충돌 없이 적용
그대로 복사해서 보내면 됩니다.

Feloria Map Design + Tile Layout Prompt
Feloria - Map Design Upgrade with Tile-Based Layout

Apply RPG-style map design improvements to the existing Feloria maps.

Do NOT rewrite the entire game.
Do NOT break existing systems.

Only improve map layout, exploration flow, and landmark structure.

All existing systems must remain intact:
- save/load
- battle
- inventory
- shop
- quests
- codex
- trainer battles
- encounters

--------------------------------
MAP DESIGN RULES
--------------------------------

Apply the following RPG map design principles.

1. Visible navigation
Players must always see a clear path forward.

Main progression path:

Starwhisk Village
→ Greenpaw Forest
→ Mosslight Path
→ Ancient Forest
→ Mosslight Shrine

Paths should use visible terrain tiles.

2. Natural boundaries

Use environmental objects for boundaries:
- trees
- rocks
- cliffs
- water
- fences

Avoid invisible collision walls.

3. Main path + side exploration

Each map must contain:
- 1 main route
- 2 optional exploration areas

Side areas may contain:
- hidden item
- rare encounter grass
- optional trainer
- NPC hint dialogue

4. Encounter zones

Wild encounters must only occur in tall grass tiles.

Paths and open ground must remain safe zones.

5. Landmarks

Each map must include a visual landmark.

Examples:
- large tree
- shrine ruin
- waterfall
- glowing stone
- ancient statue

6. Boss preparation area

Before boss battles, add a preparation zone.

Structure:

Ancient Forest
→ Healing NPC
→ Shrine Entrance
→ Boss Battle

7. Map size guidelines

Village: ~20x20
Forest: ~30x30
Route: ~25x25
Shrine: ~15x15

Avoid extremely large empty spaces.

--------------------------------
MAP TILE LEGEND
--------------------------------

Use simple tile IDs.

0 = grass
1 = path
2 = tall grass (encounter zone)
3 = tree
4 = rock
5 = water
6 = building
7 = shrine floor
8 = landmark object
9 = NPC spawn

--------------------------------
MAP LAYOUT DEFINITIONS
--------------------------------

Starwhisk Village (20x20)

Main path crossroad village layout.

Example tile layout:

[
[3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3],
[3,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,3],
[3,0,6,6,0,0,0,0,1,0,1,0,0,0,6,6,0,0,0,3],
[3,0,6,6,0,0,0,0,1,0,1,0,0,0,6,6,0,0,0,3],
[3,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,3],
[3,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,3],
[3,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,3],
[3,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,3],
[3,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,3],
[3,0,0,0,0,0,0,0,1,9,1,0,0,0,0,0,0,0,0,3],
[3,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,3],
[3,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,3],
[3,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,3],
[3,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,3],
[3,1,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,3],
[3,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,3],
[3,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,3],
[3,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,3],
[3,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,3],
[3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3]
]

NPC spawn tile (9) marks Elder Mira.

--------------------------------
Greenpaw Forest (~30x30)

Structure:

Main path through forest
Side grass encounter zones
Hidden item clearing

Use tall grass tiles (2) for encounter areas.

Landmark:
Large central forest tree (tile 8).

--------------------------------
Mosslight Path (~25x25)

Structure:

Rocky route with branching paths.

Features:
- trainer NPC (tile 9)
- tall grass pockets
- rock obstacles

Landmark:
Broken bridge or rock formation.

--------------------------------
Ancient Forest (~30x30)

Darker forest region.

Features:
- stronger wild encounters
- rare encounter zone
- hidden shrine entrance

Landmark:
Ancient glowing tree (tile 8).

--------------------------------
Mosslight Shrine (~15x15)

Boss map.

Layout example:

[
[4,4,4,4,4,4,4,4,4,4,4,4,4,4,4],
[4,7,7,7,7,7,7,7,7,7,7,7,7,7,4],
[4,7,0,0,0,0,0,0,0,0,0,0,0,7,4],
[4,7,0,0,0,0,0,0,0,0,0,0,0,7,4],
[4,7,0,0,0,8,8,8,8,8,0,0,0,7,4],
[4,7,0,0,0,8,9,8,8,8,0,0,0,7,4],
[4,7,0,0,0,8,8,8,8,8,0,0,0,7,4],
[4,7,0,0,0,0,0,0,0,0,0,0,0,7,4],
[4,7,0,0,0,0,0,0,0,0,0,0,0,7,4],
[4,7,7,7,7,7,7,7,7,7,7,7,7,7,4],
[4,4,4,4,4,4,4,4,4,4,4,4,4,4,4]
]

Tile 9 = boss trainer spawn.

--------------------------------
CONSTRAINTS
--------------------------------

Do not break:

- save/load
- encounter logic
- trainer battles
- quests
- codex
- inventory
- shop
- evolution

Only update map layout and tiles.

--------------------------------
GOAL
--------------------------------

After this change Feloria maps should feel like real RPG exploration spaces with:

- clear progression
- exploration rewards
- memorable landmarks
- boss progression

이 프롬프트의 효과
이걸 적용하면 Feloria 맵이 다음처럼 변합니다.
Village
   ↓
Forest
   ↓
Route
   ↓
Ancient Forest
   ↓
Boss Shrine
그리고
숨겨진 아이템
탐험 루트
트레이너 위치
보스 위치
가 자동으로 정리됩니다.

다음 추천 Phase
이 단계 끝나면 다음은 보통
Phase 10
보스 / 체육관 시스템

Phase 11
사운드 + 애니메이션

Phase 12
게임 밸런싱
입니다.

원하시면 다음 답변에서
Feloria 맵을 훨씬 자연스럽게 만드는 "타일셋 선택 방법 + 무료 타일셋 5개"도 알려드리겠습니다.

https://github.com/Evan-Yoon/Feloria 이 코드를 보고 지금까지 내가 뭐했는지 꼼꼼히 보고, 다음에 뭐해야할지 만들어줘. 아 그리고 거기에 마을이랑 맵에 그냥 npc들이 생겼는데 얘네도 뭔가 역할을 부여하는 것도 추가해서 알려줘

2m 8s 동안 생각함
코드를 기준으로 보면, 지금까지는 **“프로토타입”을 넘어서 “초반 챕터가 돌아가는 RPG 기반”**까지 왔습니다.
확인되는 점은 이렇습니다.
프로젝트는 Vite + Phaser 3 기반입니다. package.json에 vite와 phaser만 들어가 있어 구조가 단순합니다.
게임 코드는 src/game 아래로 정리돼 있고, config / data / scenes / systems 구조를 갖췄습니다.
씬은 전투, 도감, 대화, 진화, 인벤토리, 메뉴, 파티, 퀘스트, 세이브/로드, 스타트, 스타터 선택, 월드까지 분리돼 있습니다.
시스템도 전투, 도감, 대화, 조우, 진화, 맵 로딩, 퀘스트, 세이브, 상점으로 나뉘어 있습니다.
데이터도 맵, 크리처, 조우, 아이템, 스킬, 트레이너까지 별도 파일로 분리돼 있습니다.
맵은 starwhisk_village, greenpaw_forest, mosslight_path, ancient_forest, mosslight_shrine까지 존재합니다.
README 기준으로는 탐험, 야생 배틀, 트레이너 배틀, 진화, 경제, 세이브/로드, 퀘스트, 도감, 픽셀 아트 방향까지 이미 목표에 포함돼 있습니다.
다만 코드 레벨에서 한 가지는 보입니다.
루트 src 아래에 예전 React/TS 구조 흔적이 아직 남아 있습니다. src/App.tsx, src/main.tsx, src/components, src/screens, src/store, src/types가 여전히 있고, App.tsx 내용도 예전 WorldCanvas / BattleScreen / StarterSelectScreen 흐름 흔적이 남아 있습니다. 반면 실제 게임 구조는 src/game 기준 Phaser 씬 구조입니다. 즉, 현재 저장소는 Phaser 구조가 본체인데, 예전 React 구조가 일부 같이 남아 있는 혼합 상태로 보입니다.
그래서 지금까지 한 일을 한 문장으로 정리하면 이렇습니다.
Feloria는 이제 “코어 시스템을 다 갖춘 초반 챕터형 몬스터 RPG 프로토타입” 상태이고, 다음부터는 시스템 추가보다 “세계에 의미를 부여하는 단계”로 넘어가야 합니다.
여기서 다음으로 가장 좋은 작업은 NPC 역할 부여 + 월드 반응성 강화입니다.
지금 말씀하신 “마을이랑 맵에 그냥 NPC들이 생겼는데 얘네도 역할을 부여하고 싶다”가 정확히 다음 단계입니다.
추천 다음 단계
Phase 10: NPC Roles, World Reactivity, and First Chapter Structure
핵심은 새 시스템을 하나 더 만드는 게 아니라, 이미 있는 시스템을 NPC를 통해 세계 안에 녹이는 것입니다.
예를 들면 이런 역할입니다.
Healer NPC: 파티 회복
Shop NPC: 상점 열기
Quest Giver NPC: 퀘스트 시작/완료
Hint NPC: 지역/보스/희귀 몬스터 힌트
Trainer NPC: 1회성 전투
Lore NPC: 세계관 설명
Gatekeeper NPC: 조건 충족 전 길 막기
Collector NPC: 도감/포획 수에 따른 보상
Breeder / Tamer NPC: 파티 관련 팁 제공
Travel NPC: 다음 지역 방향 유도
지금 단계에서는 모든 NPC를 다 복잡하게 만들 필요는 없고, 각 맵마다 2~4명 정도에게 명확한 역할만 주면 게임이 갑자기 훨씬 살아납니다.
아래 프롬프트를 그대로 보내세요.
Feloria - Phase 10: NPC Roles, World Reactivity, and First Chapter Structure

The current Feloria project already has a strong gameplay foundation:
- exploration
- wild battles
- trainer battles
- capture
- leveling and evolution
- healing
- shop and inventory
- codex
- quest progression
- save/load
- multiple connected maps

Now proceed to Phase 10.

Goal:
Turn the current prototype into a more believable RPG world by giving NPCs clear gameplay roles and making the world react to player progress.

Important:
I will do all verification manually myself.
Do not claim anything is verified.
Implement the systems so they are stable and easy to test.

--------------------------------
PHASE 10 GOAL
--------------------------------

This phase should focus on:
- meaningful NPC roles
- map-specific NPC behavior
- world reactivity to progress
- stronger chapter flow through NPCs
- clearer progression guidance for the player

Do not add too many new systems.
Use the systems that already exist and connect them to the world through NPCs.

--------------------------------
NPC ROLE SYSTEM
--------------------------------

Add structured NPC roles.

Every important NPC should have a role, not just dialogue.

Supported NPC role types:

1. healer
- restores party HP
- can be Elder Mira or another dedicated NPC

2. shopkeeper
- opens ShopScene
- uses existing inventory / gold systems

3. quest_giver
- starts or completes quests
- can change dialogue depending on quest state

4. hint_npc
- gives gameplay hints
- points player toward routes, bosses, rare encounters, or healing/shop options

5. trainer
- starts a trainer battle
- changes dialogue after defeat

6. lore_npc
- explains worldbuilding, faction lore, shrines, or legendary cats

7. gatekeeper
- blocks progression until a condition is met
- example: shrine access locked until Ancient Forest objective is complete

8. collector
- reacts to codex progress or number of captured cats
- may reward the player for milestones later

--------------------------------
MAP-SPECIFIC NPC ASSIGNMENTS
--------------------------------

Assign meaningful roles to existing maps.

Starwhisk Village:
- Elder Mira = healer + quest progression NPC
- Shopkeeper = shopkeeper
- One villager = hint_npc
- One villager = lore_npc

Greenpaw Forest:
- Forest Explorer = hint_npc
- Optional trainer = trainer
- Optional researcher / ranger = lore_npc

Mosslight Path:
- Route trainer(s) = trainer
- One traveler = hint_npc
- One blocked-path NPC or sign = gatekeeper if needed

Ancient Forest:
- One mysterious wanderer = lore_npc
- One stronger trainer = trainer
- One shrine guide = hint_npc

Mosslight Shrine:
- Boss/Guardian = trainer or boss_npc role
- One preparation NPC before boss = healer or hint_npc

--------------------------------
WORLD REACTIVITY
--------------------------------

NPC dialogue and behavior must change based on game progress.

Examples:
- Before first capture: hint NPC teaches capture basics
- After first capture: same NPC gives different advice
- Before trainer defeat: trainer challenges player
- After trainer defeat: trainer uses post-battle dialogue
- Before shrine cleared: villagers mention danger
- After shrine cleared: villagers acknowledge the player’s success

Use existing quest, codex, and defeatedTrainers state to drive dialogue variation.

--------------------------------
IMPLEMENTATION RULES
--------------------------------

1. Keep Phaser 3 + JavaScript
- Do not switch to TypeScript
- Do not reintroduce React gameplay components

2. Keep data-driven NPC definitions
Create or extend structured NPC data such as:
- id
- mapId
- role
- name
- sprite
- dialogueBefore
- dialogueAfter
- interactionType
- optional quest hooks
- optional trainer hooks
- optional shop/heal hooks

Do not hardcode all NPC behavior directly inside WorldScene.

3. Use existing systems
Connect NPCs to existing systems where possible:
- healer -> party healing logic
- shopkeeper -> ShopScene
- quest_giver -> questSystem
- trainer -> BattleScene with trainer data
- collector -> codexSystem / playerCollection checks

4. Preserve existing systems
Do not break:
- save/load
- inventory
- shop
- battle
- codex
- party
- quests
- trainer defeat persistence

5. NPC interactions must remain readable
Use consistent interaction flow:
- player faces NPC
- presses interaction key
- correct role logic executes
- dialogue updates after state changes

--------------------------------
FIRST CHAPTER STRUCTURE
--------------------------------

Use NPCs to strengthen the first chapter flow:

Starwhisk Village
→ Greenpaw Forest
→ Mosslight Path
→ Ancient Forest
→ Mosslight Shrine

Players should always know:
- where to go next
- who to talk to next
- why the next area matters

NPC hints should support this.

--------------------------------
BUG PREVENTION RULES
--------------------------------

1. Prevent NPC role overlap confusion
- each important NPC should have one primary role
- if an NPC has multiple roles, interaction flow must be explicit and predictable

2. Prevent trainer/shop/heal logic from conflicting
- role dispatch must be clear
- talking to a healer should not accidentally open shop or start battle

3. Prevent quest dialogue desync
- dialogue variants must use the same quest state source of truth
- do not use scene-local flags for permanent progression

4. Prevent defeated trainer reset
- post-battle dialogue must remain correct after save/load

5. Prevent progression dead ends
- at least one NPC in each major map should clearly hint the next objective

--------------------------------
MANUAL-TEST READY TARGETS
--------------------------------

Implement these so I can test them manually:

1. Elder Mira heals and also reacts to quest progress
2. Shopkeeper opens shop reliably
3. Hint NPCs change dialogue when progress changes
4. Trainers do not rebattle after defeat
5. At least one lore NPC gives worldbuilding dialogue
6. At least one gatekeeper-style NPC or equivalent interaction controls progression
7. Ancient Forest / Shrine NPCs make the chapter flow clearer
8. Save/load preserves all NPC state reactions

--------------------------------
FINAL NOTE
--------------------------------

The goal of Phase 10 is to make Feloria feel like a world with purpose.
NPCs should not just exist on the map — they should teach, guide, react, reward, block, and reinforce progression.
추가로 제 판단을 한 줄로 정리하면 이렇습니다.
지금부터 Feloria는 “새 시스템”보다 “세계 설계”가 더 중요합니다.
그래서 다음 우선순위는
NPC 역할
챕터 흐름
지역별 목적성
이 3개입니다.
원하시면 다음 답변에서 Starwhisk Village / Greenpaw Forest / Mosslight Path / Ancient Forest / Mosslight Shrine 각각에 NPC를 몇 명 두고, 누구에게 무슨 역할을 주면 좋은지 구체적으로 바로 짜드리겠습니다.

위 내용에 합쳐서 각 NPC별 이름도 한번에 적어줘. 대사는 참고로 모두 한글로 적어줘.

좋습니다.
아래는 **지금까지 만든 Phase 10 프롬프트 + NPC 역할 + NPC 이름 + 실제 한글 대사까지 전부 포함된 “완성 프롬프트”**입니다.
이걸 안티그래비티에 그대로 복사해서 보내면 됩니다.
이 프롬프트는 다음을 합니다.
NPC 역할 시스템 추가
NPC 데이터 구조 생성
각 맵별 NPC 배치
NPC 이름 설정
모든 대사 한국어
게임 진행에 맞는 대사 변화
기존 시스템과 충돌 없이 연결

Feloria Phase 10 + NPC Design Prompt
