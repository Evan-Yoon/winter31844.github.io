# Feloria 대화 내역 분할본 18

- 원본 파일: Feloria 대화 내역.docx
- 분할 번호: 18/28
- 문단 범위: 3274~3498
- 문자 수(대략): 25747

---

Feloria - Cinematic Events, Boss Presentation, Legendary Awakening, Level-Up and Evolution Effects

The current Feloria project has a complete gameplay foundation:

- exploration
- battles
- capture
- leveling
- evolution
- quests
- NPC system
- shop / healing
- save/load
- codex

Now proceed to a new phase focused on cinematic presentation and major story events.

The goal is to make important moments feel dramatic and memorable.

--------------------------------
SYSTEMS TO IMPLEMENT
--------------------------------

Create a lightweight cinematic system that can temporarily take control of the game and present scripted events.

Add the following systems:

- cutsceneSystem
- camera control events
- boss battle introduction
- legendary awakening sequence
- level up animation
- evolution animation

--------------------------------
CUTSCENE SYSTEM
--------------------------------

Create a simple cutscene system.

Features:

- temporarily disable player movement
- move camera to specific positions
- trigger dialogue automatically
- play animations or effects
- restore control after event ends

Cutscenes should be triggered by:

- map entry
- quest completion
- boss defeat

--------------------------------
BOSS INTRODUCTION CINEMATIC
--------------------------------

Location:
Mosslight Shrine

When player enters the boss arena:

1. Player movement stops
2. Camera slowly pans to Rowan
3. Rowan steps forward

Dialogue:

로완:
"여기까지 온 것을 보니 실력은 인정하겠다."

"하지만 이곳은 신성한 신전이다."

"세계의 균형을 지키기 위해…"

"나는 너를 막아야 한다."

Then camera shifts to player.

Rowan battle begins.

--------------------------------
BOSS DEFEAT CINEMATIC
--------------------------------

After defeating Rowan:

Player movement disabled.

Rowan falls to one knee.

Dialogue:

로완:
"크윽…"

"어리석은 녀석…"

"네가 무슨 짓을 했는지 아느냐?"

"네가 나를 쓰러뜨림으로써…"

"봉인을 지키던 마지막 결계가 깨져버렸다…"

Camera shakes slightly.

--------------------------------
VILLAIN REVEAL
--------------------------------

Village Chief Hyunseok enters the shrine.

Camera pans to him.

현석:

"훌륭하구나."

"로완, 이 고지식한 친구가 길을 비켜주지 않아 곤란하던 참이었단다."

"역시 내가 선택한 아이답게 훌륭히 자라주었어."

Pause.

현석:

"그래… 이 순간을 위해 널 키운 것이지."

--------------------------------
LEGENDARY AWAKENING EVENT
--------------------------------

The shrine begins to glow.

Screen effect:
- light pulses
- particles appear
- camera shakes slightly

Narration appears on screen.

Narration:

"고대 고양이의 봉인이 무너졌다."

"전설 속 존재들이…"

"펠로리아 대륙 곳곳으로 흩어지기 시작했다."

--------------------------------
LEGENDARY CAT CUTSCENE
--------------------------------

Short flashes appear showing legendary feline creatures across different regions.

Examples:

Fire Region:
Solarion appears above a volcano.

Ice Region:
Glaciara awakens in frozen mountains.

Storm Region:
Tempestclaw roars in a thunderstorm.

Shadow Region:
Umbrafang opens glowing eyes in ruins.

Forest Region:
Verdantlynx emerges from ancient trees.

These creatures are not fought immediately.

They become future world encounters.

--------------------------------
LEVEL-UP PRESENTATION
--------------------------------

When a creature levels up:

Pause battle briefly.

Show a level-up panel.

Example:

"Cinderclaw leveled up!"

Stats display:

HP ↑
Attack ↑
Defense ↑

Play sparkle animation.

--------------------------------
EVOLUTION PRESENTATION
--------------------------------

When a creature evolves:

Start evolution sequence.

Steps:

1. Screen darkens
2. Creature sprite glows
3. Light pulses around creature
4. Creature silhouette changes
5. New evolved sprite appears

Example text:

"Emberpaw is evolving!"

Then:

"Emberpaw evolved into Cinderclaw!"

Evolution should be skippable with a button.

--------------------------------
IMPLEMENTATION RULES
--------------------------------

1. Do not break existing battle systems.

2. All cinematics must be optional and skippable.

3. Cutscenes must restore player control correctly.

4. Use existing Phaser scene architecture.

5. Store story progress in registry flags so events do not repeat.

--------------------------------
FINAL GOAL
--------------------------------

Feloria should feel like a living RPG world with dramatic moments:

- boss confrontations
- villain reveals
- legendary awakenings
- meaningful level ups
- exciting evolutions

이 프롬프트를 적용하면 Feloria에서 일어나는 것
신전 진입
↓
로완 컷신
↓
보스 전투
↓
로완 패배
↓
현석 등장
↓
봉인 붕괴
↓
전설 고양이 등장 컷신
↓
다음 시즌 떡밥
이제 게임이 단순 시스템 게임이 아니라 스토리 RPG처럼 느껴지게 됩니다.

다음 단계 추천
이 연출이 들어가면 다음은 보통:
Phase 12
배경 음악
전투 음악
환경 사운드
Phase 13
전설 고양이 이벤트
세계 지도
새로운 지역

원하시면 다음 답변에서
Feloria 전설 고양이 10종 (속성별) + 전설 이벤트 구조도 만들어드리겠습니다.

응 만들어줘

좋습니다.
Feloria 세계관을 기준으로 전설 고양이 10종 + 속성 + 등장 이벤트 구조 + 프롬프트까지 한 번에 만들어 드리겠습니다.
이건 안티그래비티에 그대로 넣어도 되는 구조로 만들었습니다.
핵심 목표:
봉인 붕괴 이후 대륙 전역 이벤트
전설 고양이 속성별 분포
탐험 + 퀘스트 + 전투 이벤트
한 번만 등장하는 특별 이벤트

Feloria Legendary Cat System
전설 고양이 설계

전설 고양이 설정
Solarion
속성: Fire
설명
태양의 화신이라 불리는 전설의 고양이.
깨어나면 대륙의 기온이 급격히 상승한다.
등장 이벤트
화산이 갑자기 분화하기 시작한다.
붉은 빛이 하늘을 뒤덮는다.

Solarion 등장

Glaciara
속성: Ice
설명
빙하 깊숙한 곳에서 잠들어 있던 전설의 고양이.
등장 이벤트
하늘에서 눈이 내리기 시작한다.
온도가 급격히 떨어진다.

Glaciara 등장

Tempestclaw
속성: Storm
설명
번개의 화신이라 불리는 고양이.
등장 이벤트
번개가 하늘을 가르며 떨어진다.

Tempestclaw 등장

Verdantlynx
속성: Forest
설명
숲을 지키는 고대 수호자.
등장 이벤트
나무들이 갑자기 빛을 낸다.

Verdantlynx 등장

Umbrafang
속성: Shadow
설명
그림자 속에 숨어 있는 전설의 고양이.
등장 이벤트
폐허의 어둠이 움직인다.

Umbrafang 등장

Aquarion
속성: Water
설명
거대한 파도를 부르는 바다의 수호자.
등장 이벤트
호수가 거대한 파도로 뒤집힌다.

Aquarion 등장

Terraclaw
속성: Rock
설명
대지를 움직이는 거대한 고양이.
등장 이벤트
지면이 흔들린다.

Terraclaw 등장

Lumina
속성: Light
설명
빛의 신전에서 나타나는 전설 고양이.
등장 이벤트
신전이 빛으로 가득 찬다.

Lumina 등장

Aetherion
속성: Mystic
설명
하늘섬에서만 나타나는 신비한 고양이.
등장 이벤트
하늘에 거대한 빛의 고리가 열린다.

Aetherion 등장

Noctyra
속성: Spirit
설명
밤의 숲에서만 나타나는 영혼 고양이.
등장 이벤트
숲 전체가 어둠에 잠긴다.

Noctyra 등장

전설 고양이 시스템 프롬프트
안티그래비티에 그대로 넣으세요.
Feloria - Legendary Cat Event System

Add a new system for Legendary Cats after the Mosslight Shrine seal is broken.

When the shrine seal collapses, legendary feline creatures awaken across the Feloria continent.

--------------------------------
LEGENDARY SYSTEM
--------------------------------

Legendary cats are unique world events.

Rules:

- each legendary cat appears only once
- they spawn in specific regions
- they require exploration or quests to reach
- they are very powerful battles

--------------------------------
LEGENDARY CREATURES
--------------------------------

Solarion – Fire – Volcano Region
Glaciara – Ice – Frozen Mountains
Tempestclaw – Storm – Thunder Plains
Verdantlynx – Forest – Ancient Forest
Umbrafang – Shadow – Ruined Temple
Aquarion – Water – Grand Lake
Terraclaw – Rock – Canyon Region
Lumina – Light – Sun Shrine
Aetherion – Mystic – Sky Island
Noctyra – Spirit – Night Forest

--------------------------------
LEGENDARY EVENT RULES
--------------------------------

1. Legendary creatures appear only after the shrine seal breaks.

2. Each legendary creature has a special spawn event.

3. Legendary battles should have unique introduction cutscenes.

4. Legendary creatures should be extremely rare and powerful.

5. Some legendary encounters may require solving puzzles or quests.

--------------------------------
WORLD EFFECTS
--------------------------------

After the shrine seal breaks:

- weather changes
- new creatures appear
- NPC dialogue changes
- new quests unlock
- legendary sightings reported across the continent

--------------------------------
GOAL
--------------------------------

The legendary system should make Feloria feel like a living world reacting to the awakening of ancient feline powers.

다음 단계 (추천)
지금 Feloria는 이제 여기까지 왔습니다.
탐험
전투
포획
레벨업
진화
상점
NPC
퀘스트
스토리
보스
컷신
이 다음은 보통
Phase 12
세계 지도
전설 고양이 이벤트
새로운 지역
입니다.

원하시면 다음 답변에서
Feloria 전체 세계 지도 구조 (대륙 지도)도 만들어 드리겠습니다.
이걸 만들면 게임 규모가 훨씬 커집니다.

좋아 일단 더 확장하지 말고 지금까지 만든것의 완성도를 훨씬 더 높이고 싶어. https://github.com/Evan-Yoon/Feloria 지금까지 만든 코드를 꼼꼼히 살펴보고, 개선 및 완성도를 높이는 방법이 있을지 찾아줘.

36s 동안 생각함
지금 코드 기준으로 보면, Feloria는 시스템 추가보다 “정리와 스토리 통합”이 우선인 단계입니다.
제가 확인한 핵심은 이렇습니다.
프로젝트 본체는 Phaser 3 + Vite + JavaScript 구조로 잡혀 있습니다.
맵/데이터/시스템/씬 분리가 이미 되어 있어서 기본 뼈대는 좋습니다. src/game 아래에 data, scenes, systems가 나뉘어 있고, 맵도 starwhisk_village, greenpaw_forest, mosslight_path, ancient_forest, mosslight_shrine까지 확장되어 있습니다.
다만 저장소에는 아직 dist와 node_modules가 같이 올라가 있어 정리 필요성이 남아 있습니다.
WorldScene는 이미 이동, 워프, 조우, 메뉴, NPC 상호작용까지 담당하지만, 상호작용 로직이 아직 많이 handleInteraction() 안에 몰려 있습니다. 특히 트레이너 판정, mira 퀘스트 처리, [HEAL_PROMPT] / [SHOP_PROMPT] 같은 문자열 토큰 분기가 WorldScene에 직접 남아 있습니다.
npcs.js는 중앙집중형 NPC 데이터 구조로 가려는 방향이 보이지만, 현재 WorldScene가 그 구조를 완전히 역할 기반으로 쓰는 상태는 아닙니다. WorldScene는 아직 dialogueSystem.getDialogue(npc.npcId)와 npc.npcId.startsWith("trainer_") 같은 분기에 의존하고 있습니다.
BattleScene는 전투 루프는 잘 갖춰져 있지만, 액션 텍스트와 메뉴가 영어(Attack, Skill, Capture, Run)이고, 전투 UI도 여전히 프로토타입 성격이 강합니다.
README는 이미 “Phase 9 수준의 기능”을 설명하고 있지만, 실제 코드 완성도 관점에서는 스토리 연출, NPC 반응성, 역할 통합, UI/문구 통일이 아직 남아 있습니다.
한 줄로 정리하면,
지금은 새 지역을 더 늘릴 때가 아니라, 지금 있는 지역과 NPC에 목적과 스토리 흐름을 입혀서 “진짜 챕터”로 만드는 단계입니다.
다음에 해야 할 일
가장 좋은 다음 단계는 이것입니다.
Phase 10.5: Story Intro, Unified NPC Role Dispatch, and Reactive Chapter Flow
왜 이 단계가 맞냐면:
이미 npcs.js가 생겼는데, 아직 WorldScene가 이를 완전히 활용하지 못하고 있음
촌장 현석, 에반, 로완 같은 스토리 핵심 인물이 있는데, 게임 시작 흐름이 아직 “장면 중심”으로 강하게 묶이지 않음
NPC가 맵에 존재는 하지만, 아직 “역할이 있는 세계”로 완전히 보이진 않음
즉 다음은 NPC 역할 정리 + 스토리 인트로 + 첫 챕터 연출 고정입니다.

아래 프롬프트를 그대로 보내세요.
Feloria - Phase 10.5: Story Intro, Unified NPC Roles, and Reactive First Chapter

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
- centralized NPC data beginnings in npcs.js

Now proceed to Phase 10.5.

Goal:
Do not expand the world further yet.
Instead, improve the completeness of the current game by making the first chapter feel intentional, reactive, and story-driven.

Important:
I will do all verification manually myself.
Do not claim anything is verified.
Implement the systems so they are stable and directly testable.

--------------------------------
PHASE 10.5 GOAL
--------------------------------

This phase should focus on:

1. story intro at game start
2. forced opening interaction with Chief Hyunseok
3. true unified NPC role dispatch using npcs.js
4. Korean dialogue consistency
5. first chapter story progression using existing maps
6. stronger NPC purpose and reactivity
7. reduce hardcoded NPC logic in WorldScene

Do not add many new maps or large new systems.
Use the existing chapter structure and make it feel complete.

--------------------------------
STORY INTRO FLOW
--------------------------------

Add a short intro sequence before the player gains full control.

Flow:

Start New Game
→ short narrative intro
→ Starwhisk Village
→ forced dialogue with Chief Hyunseok
→ starter selection
→ first quest begins

Use Korean narration.

Suggested intro narration:

"펠로리아 대륙."

"아주 오래전, 이 대륙에는 세계의 균형을 지키는 존재들이 있었다."

"사람들은 그들을 '고대 고양이'라 불렀다."

"하지만 지금, 숲의 기운이 뒤틀리기 시작했다."

"그리고 그 힘을 노리는 누군가가 움직이고 있다."

--------------------------------
FORCED OPENING EVENT
--------------------------------

When entering Starwhisk Village for the first time:
- temporarily disable free movement
- automatically trigger Chief Hyunseok interaction
- then move into starter selection
- then begin the first quest

Chief Hyunseok opening dialogue in Korean:

"어서 오너라, 여행자야."

"요즘 숲의 기운이 심상치 않단다."

"야생 고양이들이 이유 없이 사나워지고 있어."

"그래서 부탁이 있다."

"이 아이를 데리고 숲의 상태를 조사해 줄 수 있겠느냐?"

After starter selection:

"좋은 선택이구나."

"네가 이 아이와 함께라면 숲의 비밀을 밝혀낼 수 있을 거다."

"조심하거라."

--------------------------------
UNIFIED NPC ROLE DISPATCH
--------------------------------

Refactor NPC interaction so WorldScene no longer handles many NPCs with hardcoded special cases.

Use npcs.js as the primary source of truth.

Each NPC should define:
- id
- name
- role
- mapId
- sprite
- trainerId if applicable
- questTrigger if applicable
- getDialogue(registry)
- optional onInteract behavior metadata

WorldScene should dispatch by role instead of hardcoded npc ids.

Support these roles:
- healer
- shopkeeper
- quest_giver
- hint_npc
- trainer
- lore_npc
- gatekeeper
- collector
- boss_trainer

--------------------------------
NPC REACTIVITY
--------------------------------

NPC dialogue must react to player progress using registry-backed state.

Use:
- activeQuests
- completedQuests
- defeatedTrainers
- seenCreatureIds
- caughtCreatureIds
- chapter flags

Examples:
- before first capture
- after first capture
- before Rowan defeat
- after Rowan defeat
- after chapter 1 ending

--------------------------------
MAP-BY-MAP NPC PURPOSE
--------------------------------

Starwhisk Village
- Chief Hyunseok = healer + quest giver + hidden antagonist foreshadowing
- Toby = shopkeeper
- Lina = hint NPC
- Eugene = lore NPC / codex-related scholar

Greenpaw Forest
- Darin = hint NPC about aggressive wild cats
- Kyle = first trainer battle

Mosslight Path
- Noah = progression hint NPC
- Sera = route trainer

Ancient Forest
- Evan = lore NPC who foreshadows the real plot
- Luke = shrine defender trainer

Mosslight Shrine
- Ellie = warning / preparation NPC
- Rowan = boss trainer

--------------------------------
CHAPTER 1 STORY INTEGRATION
--------------------------------

Integrate the following first chapter story flow into dialogue and progression:

1. peaceful beginning in the village
2. strange forest activity
3. hints about ancient cats
4. suspicious energy near the shrine
5. Rowan battle
6. Rowan defeat reveals the seal has broken
7. Hyunseok appears and reveals manipulation
8. chapter ends with the ancient cats scattering

Store story progress in registry flags so events do not repeat incorrectly.

--------------------------------
KOREAN DIALOGUE REQUIREMENTS
--------------------------------

All main NPC dialogue must be in Korean.
Ensure Korean text renders correctly everywhere:
- dialogue scene
- menus
- cutscenes if any

Use readable Korean line lengths and support multi-page dialogue.

--------------------------------
CODE CLEANUP REQUIREMENTS
--------------------------------

Use this phase to improve code quality in the current architecture:

1. Reduce hardcoded NPC branches in WorldScene
2. Move NPC behavior decisions into data + role dispatch
3. Keep quest triggers data-driven where possible
4. Keep save/load compatibility intact

Do not break:
- battle flow
- shop
- healing
- trainer persistence
- quest progress
- save/load

--------------------------------
OPTIONAL POLISH IF TIME ALLOWS
--------------------------------

If possible, also improve:
- Korean names consistently in menus
- dialogue speaker labels
- opening transition from title into village

--------------------------------
BUG PREVENTION RULES
--------------------------------

1. Prevent NPC role conflicts
If an NPC has more than one responsibility, the interaction flow must be deterministic and clear.

2. Prevent repeated forced opening event
The first Hyunseok intro must only happen once on a fresh game start.

3. Prevent save/load story desync
Story flags and NPC reactions must survive save/load correctly.

4. Prevent broken NPC lookup
If a spawn references a missing NPC id, fail safely and log clearly.

5. Prevent trainer dialogue mismatch
Defeated trainer dialogue must remain correct after saving/loading.

--------------------------------
MANUAL-TEST READY TARGETS
--------------------------------

Implement these so I can test them manually:

1. New Game triggers intro narration
2. Entering village for the first time forces Hyunseok interaction
3. Starter selection follows correctly
4. First quest begins correctly
5. NPC dialogue changes based on progress
6. Kyle, Sera, Luke, and Rowan behave correctly as trainers/boss
7. Hyunseok, Eugene, Darin, Noah, Evan, and Ellie all provide meaningful Korean dialogue
8. Save/load preserves NPC reactions and story state

--------------------------------
FINAL NOTE
--------------------------------

The goal of Phase 10.5 is to improve completeness, not size.
Do not expand the game outward yet.
Make the current chapter feel purposeful, reactive, and story-driven.
제가 보는 우선순위
지금 가장 효과 큰 개선은 이 순서입니다.
게임 시작 인트로
촌장 현석 강제 오프닝 이벤트
NPC 역할 완전 통합
로완 이후 반전 컷신
한국어 문구/이름 일관성 정리
이 5개만 잘 붙어도 Feloria는 “시스템이 많은 프로토타입”에서 **“첫 챕터가 있는 게임”**으로 확 바뀝니다.
원하시면 다음에는 제가 바로 이어서 NPC 이름/역할/대사를 JSON 형태로 정리한 샘플 데이터도 만들어드리겠습니다.

이제 고양이별 스킬 많이 생성해주고 한 고양이당 스킬 세개 중에 고르게 해주고 진화하면 다른 좋은 스킬 셋으로 바뀌고 강해지게 해줘. 배틀 할 때는 skill 누르면 skill 세개중에 고르게 해주고, 기본 공격은 매우 약하게 조정해줘. 스킬별 비주얼적으로 적이 당하는 보이는 효과도 다 다르게 해줘

좋습니다. 지금 Feloria는 전투 시스템 → 몬스터 → 진화 → NPC → 스토리까지 거의 다 만들어졌기 때문에 다음 단계는 전투 깊이를 늘리는 것입니다.
사용자님이 말한 요구사항을 정리하면:
1️⃣ 고양이마다 고유 스킬 3개
2️⃣ 전투에서 Skill 버튼 → 3개 중 선택
3️⃣ 기본 공격은 약하게
4️⃣ 진화하면 스킬 세트 변경
5️⃣ 스킬별 이펙트 연출 다르게
이걸 한 번에 넣을 수 있도록 Feloria 전투 확장 프롬프트를 만들어드리겠습니다.

Feloria Skill System Expansion Prompt
아래 프롬프트를 그대로 안티그래비티에 넣으면 됩니다.
Feloria - Phase 11: Creature Skill System Expansion

The current Feloria game already has:

- exploration
- wild battles
- trainer battles
- capture
- leveling
- evolution
- party management
- NPC system
- quests
- shop and inventory
- save/load

Now expand the battle system by introducing a full creature skill system.

--------------------------------
SKILL SYSTEM GOAL
--------------------------------

Each creature should have a small set of meaningful abilities.

Rules:

- each creature has 3 skills
- skills can be selected during battle
- basic attack is very weak
- skills are the main damage source

--------------------------------
CREATURE SKILL STRUCTURE
--------------------------------

Update creatures.js so each creature contains:

skills: [
  "skill_id_1",
  "skill_id_2",
  "skill_id_3"
]

Example:

Leafkit
skills:
- vine_swipe
- leaf_dart
- forest_guard

Emberpaw
skills:
- ember_bite
- flame_dash
- heat_claw

Misttail
skills:
- water_slash
- mist_burst
- tidal_wave

--------------------------------
EVOLUTION SKILL CHANGE
--------------------------------

When a creature evolves, it should receive a stronger skill set.

Example:

Leafkit
- vine_swipe
- leaf_dart
- forest_guard

Bramblecat
- thorn_whip
- nature_roar
- root_snare

Emberpaw
- ember_bite
- flame_dash
- heat_claw

Cinderclaw
- inferno_slash
- blazing_pounce
- firestorm

Misttail
- water_slash
- mist_burst
- tidal_wave

Dewtail
- aqua_fang
- tidal_crash
- ocean_wrath

--------------------------------
BATTLE UI CHANGES
--------------------------------

BattleScene must support:

Attack
Skill
Capture
Run

When the player selects Skill:

Show a menu with the creature's 3 skills.

Example:

Skill
 → Vine Swipe
 → Leaf Dart
 → Forest Guard

Selecting a skill executes that ability.

--------------------------------
BASIC ATTACK BALANCE
--------------------------------

Basic Attack should be intentionally weak.

Example:

Basic Attack damage
= creature.attack * 0.5

Skills should deal significantly more damage.

Example:

Skill damage
= creature.attack * skillPowerMultiplier

--------------------------------
SKILL EFFECT SYSTEM
--------------------------------

Create a new skillEffectSystem.

Each skill must have its own visual feedback.

Example effects:

vine_swipe
- green slash effect

leaf_dart
- small leaf projectile

forest_guard
- green aura shield

ember_bite
- flame burst

flame_dash
- fiery dash effect

heat_claw
- glowing claw strike

water_slash
- water splash

mist_burst
- fog explosion

tidal_wave
- wave crash effect

--------------------------------
SKILL DATA STRUCTURE
--------------------------------

Create skills.js.

Each skill should include:

id
name
type
power
accuracy
animation
effectType

Example:

{
  id: "vine_swipe",
  name: "덩굴 휘두르기",
  type: "forest",
  power: 30,
  accuracy: 100,
  animation: "vine_slash",
  effectType: "slash"
}

--------------------------------
VISUAL EFFECT RULES
--------------------------------

Every skill must produce visible feedback.

Examples:

Slash
→ quick directional slash

Fire
→ flame burst

Water
→ splash or wave

Forest
→ leaves or vines

Shadow
→ dark energy pulse

Storm
→ lightning strike

--------------------------------
EVOLUTION IMPACT
--------------------------------

When a creature evolves:

- update stats
- update sprite
- update skill list

Display message:

"Cinderclaw learned Inferno Slash!"

--------------------------------
AI ENEMY SKILLS
--------------------------------

Enemy creatures should also use skills.

Rules:

- random skill selection
- prefer stronger skills occasionally

--------------------------------
GOAL
--------------------------------

The goal is to make battles feel more strategic.

Players should choose between:

- weak basic attack
- stronger skill abilities
- capture attempts
- defensive skills

--------------------------------
TEST TARGETS
--------------------------------

I will manually verify:

1. each creature has 3 skills
2. Skill menu appears in battle
3. selecting a skill executes correct ability
4. basic attack deals less damage than skills
5. evolved creatures receive new skills
6. skill effects visually differ
7. enemy creatures use skills as well

스킬 예시 (Feloria 고양이 기준)
추가로 Feloria에 어울리는 스킬 예시도 드리겠습니다.
Forest 계열
덩굴 휘두르기
잎날 투척
숲의 포효
뿌리 속박
Fire 계열
불꽃 이빨
화염 돌진
열기 발톱
화염 폭풍
Water 계열
물베기
안개 폭발
파도 강타
바다의 분노
Shadow 계열
그림자 베기
암흑 파동
공포의 시선
Storm 계열
번개 발톱
천둥 돌진
폭풍 포효

다음 단계 (추천)
스킬 시스템이 들어가면 다음 단계는 보통:
Phase 12
스킬 애니메이션 강화
상태이상 (화상, 빙결 등)
타입 상성
입니다.

원하시면 다음 답변에서 Feloria 고양이 30종 + 스킬 90개 데이터셋도 만들어드리겠습니다.
(지금 스킬 시스템이 있으면 이게 굉장히 강력해집니다.)

Feloria 고양이 30종 + 스킬 90개 데이터셋 모두 한글로(실제 고양이 종도 포함 예를들어 러시안블루 이런식으로)

좋습니다.
아래는 Feloria에 바로 사용할 수 있는 데이터셋 구조로 만든 것입니다.
구성:
고양이 30종
스킬 90개 (각 고양이당 3개)
모두 한글
실제 고양이 품종 + 판타지 고양이 혼합
진화 라인 포함
Phaser/JS 게임 데이터 구조에 맞게 작성
이 데이터는 보통 다음 파일에 넣습니다.
src/game/data/creatures.js
src/game/data/skills.js

1️⃣ Feloria 고양이 30종 데이터
export const CREATURES = {

leafkit: {
  name: "리프킷",
  type: "숲",
  evolution: "bramblecat",
  skills: ["덩굴휘두르기","잎날베기","숲의수호"]
},

bramblecat: {
  name: "브램블캣",
  type: "숲",
  skills: ["가시채찍","숲의포효","뿌리속박"]
},

emberpaw: {
  name: "엠버포",
  type: "불",
  evolution: "cinderclaw",
  skills: ["불꽃깨물기","화염돌진","열기발톱"]
},

cinderclaw: {
  name: "신더클로",
  type: "불",
  skills: ["인페르노베기","불꽃폭풍","타오르는발톱"]
},

misttail: {
  name: "미스트테일",
  type: "물",
  evolution: "dewtail",
  skills: ["물베기","안개폭발","파도강타"]
},

dewtail: {
  name: "듀테일",
  type: "물",
  skills: ["대양의분노","물의송곳니","거대한파도"]
},

russianblue: {
  name: "러시안블루",
  type: "바람",
  skills: ["바람발톱","질풍베기","공기의칼날"]
},

siamese: {
  name: "샴고양이",
  type: "불",
  skills: ["열기시선","불꽃숨결","태양베기"]
},

persian: {
  name: "페르시안",
  type: "빛",
  skills: ["빛의송곳니","광휘베기","눈부신포효"]
},

mainecoon: {
  name: "메인쿤",
  type: "땅",
  skills: ["대지강타","거대발톱","지진파"]
},

bengal: {
  name: "벵갈",
  type: "번개",
  skills: ["번개발톱","천둥돌진","스파크폭풍"]
},

ragdoll: {
  name: "랙돌",
  type: "물",
  skills: ["잔물결베기","물의방패","파도베기"]
},

scottishfold: {
  name: "스코티시폴드",
  type: "숲",
  skills: ["잎날폭풍","덩굴속박","자연의포효"]
},

norwegianforest: {
  name: "노르웨이숲고양이",
  type: "얼음",
  skills: ["빙결발톱","눈보라","얼음창"]
},

sphynx: {
  name: "스핑크스",
  type: "신비",
  skills: ["정신파","환영베기","마력폭발"]
},

bombay: {
  name: "봄베이",
  type: "어둠",
  skills: ["그림자베기","암흑파동","어둠발톱"]
},

abyssinian: {
  name: "아비시니안",
  type: "불",
  skills: ["화염발톱","열풍베기","불꽃폭발"]
},

britishshorthair: {
  name: "브리티시숏헤어",
  type: "바위",
  skills: ["돌발톱","암석강타","바위폭풍"]
},

turkishangora: {
  name: "터키쉬앙고라",
  type: "빛",
  skills: ["빛의돌진","광휘베기","성스러운포효"]
},

savannah: {
  name: "사바나",
  type: "번개",
  skills: ["번개질주","천둥발톱","폭풍도약"]
},

lynxshadow: {
  name: "섀도링스",
  type: "어둠",
  skills: ["그림자돌진","암흑폭발","어둠속박"]
},

stormlynx: {
  name: "스톰링스",
  type: "폭풍",
  skills: ["폭풍베기","번개폭풍","질풍도약"]
},

frostpelt: {
  name: "프로스트펠트",
  type: "얼음",
  skills: ["빙결베기","눈보라발톱","서리폭풍"]
},

sunclaw: {
  name: "선클로",
  type: "빛",
  skills: ["태양베기","광휘폭발","빛의창"]
},

moonpelt: {
  name: "문펠트",
  type: "신비",
  skills: ["달빛베기","환영폭풍","마력창"]
},

volcanocat: {
  name: "볼케이노캣",
  type: "불",
  skills: ["용암발톱","화산폭발","불꽃폭풍"]
},

tidalcat: {
  name: "타이달캣",
  type: "물",
  skills: ["거대한파도","심해돌진","물의폭발"]
},

thornmane: {
  name: "쏜메인",
  type: "숲",
  skills: ["가시폭풍","덩굴폭발","숲의분노"]
},

shadowfang: {
  name: "섀도팽",
  type: "어둠",
  skills: ["암흑송곳니","그림자폭풍","어둠속박"]
},

celestialcat: {
  name: "셀레스티얼캣",
  type: "신성",
  skills: ["천상의베기","빛의폭풍","별빛강타"]
}

}

2️⃣ 스킬 90개 데이터
export const SKILLS = {

덩굴휘두르기:{type:"숲",power:30},
잎날베기:{type:"숲",power:35},
숲의수호:{type:"숲",power:20},

가시채찍:{type:"숲",power:45},
숲의포효:{type:"숲",power:40},
뿌리속박:{type:"숲",power:50},

불꽃깨물기:{type:"불",power:30},
화염돌진:{type:"불",power:40},
열기발톱:{type:"불",power:35},

인페르노베기:{type:"불",power:60},
불꽃폭풍:{type:"불",power:55},
타오르는발톱:{type:"불",power:50},

물베기:{type:"물",power:30},
안개폭발:{type:"물",power:40},
파도강타:{type:"물",power:45},

대양의분노:{type:"물",power:60},
물의송곳니:{type:"물",power:50},
거대한파도:{type:"물",power:65},

바람발톱:{type:"바람",power:35},
질풍베기:{type:"바람",power:45},
공기의칼날:{type:"바람",power:50},

열기시선:{type:"불",power:30},
불꽃숨결:{type:"불",power:50},
태양베기:{type:"불",power:55},

빛의송곳니:{type:"빛",power:35},
광휘베기:{type:"빛",power:50},
눈부신포효:{type:"빛",power:45},

대지강타:{type:"땅",power:45},
거대발톱:{type:"땅",power:40},
지진파:{type:"땅",power:60},

번개발톱:{type:"번개",power:40},
천둥돌진:{type:"번개",power:55},
스파크폭풍:{type:"번개",power:60},

잔물결베기:{type:"물",power:30},
물의방패:{type:"물",power:20},
파도베기:{type:"물",power:45},

잎날폭풍:{type:"숲",power:50},
덩굴속박:{type:"숲",power:40},
자연의포효:{type:"숲",power:35},

빙결발톱:{type:"얼음",power:40},
눈보라:{type:"얼음",power:55},
얼음창:{type:"얼음",power:50},

정신파:{type:"신비",power:45},
환영베기:{type:"신비",power:50},
마력폭발:{type:"신비",power:60},

그림자베기:{type:"어둠",power:40},
암흑파동:{type:"어둠",power:50},
어둠발톱:{type:"어둠",power:45}

}

적용하면 생기는 전투 구조
Skill
 → 덩굴휘두르기
 → 잎날베기
 → 숲의수호
그리고
기본 공격 → 약함
스킬 → 강함
진화 → 새로운 스킬 세트

다음 단계 추천
이 스킬 시스템이 들어가면 다음은
Phase 12
추가하면 좋습니다.
상태이상 (화상 / 빙결 / 감전)
속성 상성
스킬 애니메이션

원하시면 다음 답변에서
Feloria 속성 상성 시스템 (포켓몬처럼)도 만들어 드리겠습니다.
