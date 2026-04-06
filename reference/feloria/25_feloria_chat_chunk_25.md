# Feloria 대화 내역 분할본 25

- 원본 파일: Feloria 대화 내역.docx
- 분할 번호: 25/28
- 문단 범위: 4841~4929
- 문자 수(대략): 27907

---

Feloria - Creature Sprite Linking, Region-Based Deployment, and Legendary Foreshadowing

The project now has 100 cropped creature PNG files under:

public/assets/sprites/creatures/

I want these creature images to be linked cleanly and safely into the current game without breaking the existing systems.

Important:
Do not guess randomly.
Do not assign creature images inconsistently.
Do not deploy all 100 creatures into the current game maps at once.

I will verify everything manually myself.
Do not claim anything is verified.

--------------------------------
PHASE GOAL
--------------------------------

This phase should do 4 things:

1. Register all creature PNG files with stable asset keys
2. Link those keys correctly to creature definitions
3. Deploy only currently needed creatures into the existing playable regions
4. Reserve the rest for future content patches

Also:
- legendary-class creatures may appear briefly in the Chapter 1 ending cutscene
- but they must NOT become normal encounters yet
- they will later become regional legendary bosses in future chapters

--------------------------------
1. SPRITE ASSET REGISTRATION
--------------------------------

All creature PNG files are located in:

/assets/sprites/creatures/

Register every creature PNG in assetPaths.js and PreloadScene.js with stable keys.

Rules:
- every file must get one deterministic asset key
- the key must exactly match the creature's sprite reference
- do not hardcode random exceptions
- do not rename keys unpredictably

Preferred convention:

File:
  /assets/sprites/creatures/creatures_01_01.png

Asset key:
  creature_creatures_01_01

or, if mapping to a named creature:
  creature_leafkit

Choose one convention and apply it consistently.

--------------------------------
2. CREATURE DATA LINKING
--------------------------------

Update creature definitions so each creature has a sprite key.

Add a field such as:

spriteKey: "creature_leafkit"

or equivalent.

Rules:
- every currently used creature must point to a valid loaded texture
- no currently used creature should fall back to placeholders unless absolutely necessary
- unresolved creatures may remain reserved, but must not break the game

If needed, create a mapping layer such as:
- creatureSpriteMap.js
or
- a spriteKey field directly in creatures.js

--------------------------------
3. REGION-BASED DEPLOYMENT
--------------------------------

Do NOT deploy all 100 creatures into the world immediately.

Only connect creatures that are appropriate for the currently built content.

Current playable regions are approximately:
- Starwhisk Village
- Greenpaw Forest
- Mosslight Path
- Ancient Forest
- Mosslight Shrine

Only assign active creatures to these regions.

Use a staged deployment system.

Example:
- activeNow: true
- availableInChapter: 1
- reservedForFuture: true
- plannedRegion: "volcano"
- plannedRegion: "snowfield"
- plannedRegion: "ruins"
- plannedRegion: "coast"
- plannedRegion: "desert"

--------------------------------
4. CURRENT ACTIVE CREATURE POOLS
--------------------------------

For the currently available maps, only deploy appropriate creatures.

Example guidance:

Greenpaw Forest:
- starter-adjacent early creatures
- forest / grass / small wildcat types
- weak shadow or water edge cases only if already intended

Mosslight Path:
- slightly stronger route creatures
- some rock / storm / mid-tier wildcats

Ancient Forest:
- stronger forest / shadow / spirit / mystic creatures

Mosslight Shrine:
- no normal random encounters unless already part of shrine design
- boss encounter logic remains separate

Do not place late-game creatures, rare biomes, or legendary creatures into early routes.

--------------------------------
5. FUTURE PATCH RESERVE SYSTEM
--------------------------------

The creatures not used yet must remain in the project, but not active in current encounter tables.

They should be clearly marked for future deployment.

Create a clean way to reserve them for later maps/patches.

Example future categories:
- volcano region
- snow mountain region
- coast / lake region
- ruins / shadow region
- storm plains
- celestial region
- desert
- city expansion
- legendary region finals

This should make future expansion easy without refactoring everything again.

--------------------------------
6. LEGENDARY CLASS HANDLING
--------------------------------

Creatures with class "전설" or equivalent legendary classification should NOT be active random encounters now.

Rules:
- do not put legendary creatures into normal encounter tables
- do not allow them to be casually captured in Chapter 1
- do not use them as regular trainer party creatures

However:
At the end of Chapter 1, when the seal is broken, legendary-class creatures may briefly appear in a cutscene / foreshadow sequence.

This is only visual foreshadowing.

Example:
- a brief flash of the fire legendary
- a brief flash of the ice legendary
- a brief flash of the forest legendary
- etc.

These are not battle encounters yet.

Later plan:
Each legendary creature will become the final major encounter / boss of its own future region.

So for now:
- keep them loaded
- keep them linked
- allow cutscene usage
- do not activate them in standard gameplay yet

--------------------------------
7. CUTSCENE LEGENDARY FORESHADOWING
--------------------------------

At Chapter 1 ending only, allow brief non-interactive appearance of legendary-class creatures during the broken seal sequence.

Rules:
- visual only
- short duration
- no capture
- no battle
- no codex unlock unless explicitly intended
- no encounter table registration

This should function as story foreshadowing only.

--------------------------------
8. SAFETY RULES
--------------------------------

1. Do not break existing creature encounters
2. Do not break save/load
3. Do not break battle scene sprite loading
4. Do not assign missing sprite keys to active creatures
5. Do not activate future creatures in current maps by accident
6. Do not activate legendary creatures in normal exploration yet
7. Keep the deployment system easy to expand later

--------------------------------
9. IMPLEMENTATION PREFERENCE
--------------------------------

Prefer a clean data-driven approach.

Recommended options:
- spriteKey inside creatures.js
- region availability metadata inside creatures.js or encounter tables
- reserved/future flags for undeployed creatures
- optional creatureSpriteMap.js if that makes linking cleaner

Use one consistent system rather than spreading special cases across scenes.

--------------------------------
10. MANUAL-TEST READY TARGETS
--------------------------------

Implement these so I can test them manually:

1. currently used creatures display the correct PNG in battle
2. encounter tables only use creatures intended for existing regions
3. unused creatures remain safely reserved for future updates
4. legendary-class creatures appear only in Chapter 1 ending foreshadow scenes
5. legendary-class creatures do not appear as normal encounters
6. sprite keys are stable and correctly linked

--------------------------------
FINAL NOTE
--------------------------------

The goal is not to use all 100 creatures immediately.
The goal is to build a clean expansion-ready structure:
- all sprites linked
- current regions populated correctly
- future creatures reserved cleanly
- legendary creatures foreshadowed now, fully activated later

추가로, 이 프롬프트와 같이 붙이면 더 좋습니다.
짧은 보충 문구
Use only the creatures appropriate for the currently completed maps.
Do not force all available assets into Chapter 1.
Future creatures should stay linked but inactive until their region is built.
Legendary creatures should be treated as sealed future bosses, with only brief visual foreshadowing at the Chapter 1 ending.

원하시면 다음 답변에서 제가 바로 이어서
“현재 맵별로 어떤 고양이들을 activeNow=true로 두면 좋은지”
구체적으로 배치표까지 짜드리겠습니다.

응 그렇게 해줘 위 내용이랑 같이 배치표까지 한번에 프롬프트로 적을 수 있게 만들어줘

좋습니다. 지금 단계에서는 Antigravity가 게임 구조를 망가뜨리지 않도록 매우 명확한 규칙 + 배치표를 같이 주는 것이 가장 중요합니다.
그래서 아래 프롬프트는 다음을 한 번에 해결하도록 작성되어 있습니다.
1️⃣ 100개 몬스터 스프라이트 전부 정확한 key로 등록
2️⃣ creatures.js와 spriteKey 자동 연결
3️⃣ 현재 맵에 필요한 몬스터만 활성화
4️⃣ 나머지는 futureReserve 상태로 보존
5️⃣ 전설 몬스터는 chapter1 엔딩 컷신에서만 등장
6️⃣ 지역 확장 시 자연스럽게 추가 가능
이 프롬프트를 그대로 Antigravity에 보내면 됩니다.

Antigravity용 전체 프롬프트
Feloria - Creature Sprite Integration, Region Deployment, and Legendary Foreshadow System

The project now contains approximately 100 creature PNG files located in:

/assets/sprites/creatures/

These images were automatically cropped from sprite sheets and should now be linked into the game safely.

Important:
Do NOT randomly assign creatures.
Do NOT deploy all 100 creatures into the world immediately.
Do NOT activate legendary creatures as normal encounters.

I will verify everything manually myself.
Do not claim anything is verified.

--------------------------------
PHASE GOAL
--------------------------------

This phase should accomplish the following:

1. Register all creature PNG files as Phaser assets
2. Link each creature definition to its correct sprite key
3. Activate only the creatures appropriate for currently implemented maps
4. Keep the remaining creatures reserved for future patches
5. Use legendary-class creatures only for Chapter 1 ending foreshadowing

--------------------------------
1. SPRITE ASSET REGISTRATION
--------------------------------

All creature PNG files are located in:

/assets/sprites/creatures/

Register them in assetPaths.js and PreloadScene.js.

Use deterministic keys.

Example rule:

File:
creatures_01_01.png

Key:
creature_creatures_01_01

or

creature_leafkit

Choose ONE naming convention and apply it consistently.

All keys must match the spriteKey used by creature definitions.

--------------------------------
2. CREATURE DATA LINKING
--------------------------------

Each creature in creatures.js must include:

spriteKey: "<asset_key>"

Example:

{
  id: "leafkit",
  name: "리프킷",
  type: "forest",
  class: "basic",
  spriteKey: "creature_leafkit"
}

Rules:

• spriteKey must reference a loaded texture
• no creature should reference a missing asset
• if a sprite is missing, fallback temporarily but log clearly

--------------------------------
3. CREATURE DEPLOYMENT SYSTEM
--------------------------------

Not all creatures should appear immediately.

Add deployment metadata to creature definitions.

Example fields:

activeNow: true / false
availableInChapter: 1
reservedForFuture: true
plannedRegion: "snow"
plannedRegion: "volcano"
plannedRegion: "desert"

Only creatures with:

activeNow = true

should appear in encounter tables.

--------------------------------
4. CURRENT PLAYABLE REGIONS
--------------------------------

The currently implemented maps are approximately:

Starwhisk Village
Greenpaw Forest
Mosslight Path
Ancient Forest
Mosslight Shrine

Only creatures appropriate for these environments should appear.

--------------------------------
5. ACTIVE CREATURE DEPLOYMENT TABLE
--------------------------------

Use the following encounter design as guidance.

--------------------------------
Starwhisk Village (very early encounters)
--------------------------------

Leafkit line
Thistlekit
Snagpuss
Mosslynx
Fernclaw

Encounter rarity:
very common

--------------------------------
Greenpaw Forest
--------------------------------

Snagpuss
Thistlekit
Mosslynx
Fernclaw
Thornkit
Barkpelt
Ripplepaw (rare water edge encounter)

--------------------------------
Mosslight Path
--------------------------------

Thornmane
Vinefang
Sparkpaw
Ashlynx
Pebblepaw
Nightkit

--------------------------------
Ancient Forest
--------------------------------

Verdantlynx
Thornprowler
Nightpelt
Umbrafang
Mistlynx
Frostkit
Stormkit

--------------------------------
Mosslight Shrine
--------------------------------

No random encounters.

Only:

Guardian Rowan battle
Story cutscene logic

--------------------------------
6. FUTURE REGION CREATURE RESERVE
--------------------------------

All remaining creatures should be reserved for future expansion regions.

Suggested future biomes:

Snow Mountains
Volcano Region
Desert Plateau
Ocean Coast
Ancient Ruins
Storm Highlands
Celestial Realm

Creatures assigned to these regions should be marked:

activeNow: false
reservedForFuture: true
plannedRegion: "<biome>"

--------------------------------
7. LEGENDARY CREATURE SYSTEM
--------------------------------

Creatures with class:

전설
legendary

must NOT appear in encounter tables.

They must NOT appear in trainer battles.

They must NOT be capturable in Chapter 1.

However:

At the end of Chapter 1,
when the ancient seal breaks,
legendary creatures may briefly appear in a cinematic sequence.

This is only visual foreshadowing.

Example sequence:

• fire legendary appears in a volcanic flash
• ice legendary appears in a frozen storm
• forest legendary appears in ancient trees
• storm legendary appears in lightning

These are visual only.

No battles.

No capture.

No codex unlock unless explicitly desired.

--------------------------------
8. FUTURE LEGENDARY DESIGN
--------------------------------

Later chapters will introduce regional legendary bosses.

Example structure:

Volcano Region → Fire Legendary
Snow Mountains → Ice Legendary
Ancient Woods → Forest Legendary
Storm Plains → Storm Legendary
Shadow Ruins → Shadow Legendary
Celestial Sky → Mystic Legendary

Each region ends with a legendary boss encounter.

--------------------------------
9. IMPLEMENTATION PREFERENCE
--------------------------------

Use a clean data-driven architecture.

Recommended approach:

creatures.js contains:

spriteKey
activeNow
reservedForFuture
plannedRegion
class

Encounter tables should reference only activeNow creatures.

--------------------------------
10. SAFETY RULES
--------------------------------

Do not break:

battle scene rendering
save/load system
existing encounters
NPC interactions
quest triggers

Legendary creatures must remain sealed until future chapters.

--------------------------------
11. MANUAL TEST TARGETS
--------------------------------

I will manually test:

1. active creatures appear in correct regions
2. spriteKey correctly loads PNG
3. reserved creatures do not appear in encounters
4. legendary creatures only appear in Chapter 1 ending cutscene
5. battle sprites display correctly
6. encounter tables remain stable

--------------------------------
FINAL GOAL
--------------------------------

All 100 creatures are integrated safely.

Current maps use only early-game creatures.

Future creatures remain ready for expansion.

Legendary creatures are foreshadowed but not yet accessible.

왜 이 방식이 좋은가
지금 구조에서 이 방식의 장점은:
지금 만든 맵과 자연스럽게 맞음
추후 지역 확장 가능
전설 고양이 스토리와 연결됨
100마리 몬스터 데이터가 깨지지 않음

다음 단계 (강력 추천)
지금 여기까지 왔으면 다음은 이것입니다.
Feloria 완성도를 크게 올리는 3가지
1️⃣ 스킬 애니메이션 타입 매핑 시스템
2️⃣ 고양이 타입 상성 시스템
3️⃣ 몬스터 도감 UI
이 3개가 들어가면 Feloria는 거의 완성된 RPG 수준이 됩니다.
원하시면 제가 다음 답변에서
Feloria 스킬 시스템 + 애니메이션 자동 매핑 구조도 만들어 드리겠습니다.

스킬이 해제가 된 고양이들이 있는데 예전에 썼던 skill matching 프롬프트 다시 써줘. ㄱ그 class 별로 분리해뒀던 스킬 지정 방식

좋습니다. 지금 문제는 고양이 일부의 스킬이 비어 있거나 연결이 끊어진 상태입니다.
예전에 사용했던 방식은 다음 구조였습니다.
핵심 구조
creature class
↓
skill tier
↓
skills[]
즉
기본형(class: basic) → 3등급 스킬 3개
중간 진화(class: elite) → 2등급 스킬 3개
최종 진화(class: apex) → 1등급 스킬 3개
전설(class: legendary) → 전용 스킬 4개
이 구조를 다시 강제로 맞추는 프롬프트를 보내면 됩니다.
아래를 그대로 Antigravity에 보내세요.

Skill Matching Recovery Prompt
Feloria - Skill Matching Restoration (Class-Based Skill Assignment)

Some creatures currently have missing or unlinked skills due to previous data edits.

Restore the class-based skill assignment system exactly as designed earlier.

Do NOT randomly assign skills.
Do NOT leave creatures without skills.

I will verify everything manually.

--------------------------------
SKILL SYSTEM STRUCTURE
--------------------------------

Creatures use a class-based skill system.

Creature classes:

basic
elite
apex
legendary

Skill tiers:

tier3 (weak / beginner)
tier2 (intermediate)
tier1 (strong)
legendary (unique)

--------------------------------
CLASS → SKILL RULES
--------------------------------

basic class creatures:
• must have exactly 3 tier3 skills

elite class creatures:
• must have exactly 3 tier2 skills

apex class creatures:
• must have exactly 3 tier1 skills

legendary class creatures:
• must have exactly 4 legendary skills

--------------------------------
SKILL ASSIGNMENT METHOD
--------------------------------

Use type matching first.

Creature types include:

forest
grass
fire
water
rock
shadow
ice
storm
spirit
mystic
light

Skills must match the creature's type.

Example:

Fire creatures → fire skills
Water creatures → water skills
Forest creatures → nature skills

Mixed types can include secondary type skills.

--------------------------------
EXAMPLE MAPPING
--------------------------------

basic fire creature

skills:
- ember_bite
- flame_dash
- heat_claw

elite fire creature

skills:
- blazing_pounce
- firestorm
- inferno_slash

apex fire creature

skills:
- volcanic_rage
- solar_burst
- infernal_collapse

--------------------------------
LEGENDARY CREATURE RULES
--------------------------------

Legendary creatures must use unique skill sets.

Each legendary must have:

4 unique skills

Example:

Solarion

skills:
- solar_beam
- supernova
- sunflare
- apocalypse_light

These skills must not appear on normal creatures.

--------------------------------
MISSING SKILL FIX
--------------------------------

Scan all creature definitions.

If any creature has:

skills: []

or

skills missing

then reassign skills using the class rule.

Do not change creature classes.

--------------------------------
FUTURE EXPANSION RULE
--------------------------------

Skill assignment must remain deterministic.

Meaning:

creature class + creature type
must always map to a predictable skill set.

This prevents skill mismatch when future creatures are added.

--------------------------------
FINAL VALIDATION RULES
--------------------------------

After restoration:

every creature must have skills

basic = 3 skills
elite = 3 skills
apex = 3 skills
legendary = 4 skills

no creature should have empty skill arrays.

참고 (이 시스템의 장점)
이 방식이 좋은 이유는 다음입니다.
1️⃣ 스킬 누락 자동 복구 가능
2️⃣ 신규 고양이 추가해도 자동 스킬 매칭 가능
3️⃣ 전설 스킬 충돌 방지
4️⃣ AI가 랜덤으로 망치는 것을 방지

추가로 하나 추천
지금 Feloria는 이미 100마리 몬스터 + 200 스킬 구조라서
다음 시스템 하나 넣으면 전투 완성도가 확 올라갑니다.
skillCategory
예
attack
status
buff
debuff
ultimate
그러면 전투 UI에서
Attack
Skill
Ultimate
이렇게 자연스럽게 나눌 수 있습니다.

원하시면 다음 답변에서 Feloria 스킬 200개를 class/tier/type 기준으로 자동 생성하는 구조도 만들어 드리겠습니다.
이걸 넣으면 새 몬스터 추가할 때 스킬 문제 다시 안 생깁니다.

1️⃣ 스킬 애니메이션 타입 매핑 시스템 2️⃣ 고양이 타입 상성 시스템 3️⃣ 몬스터 도감 UI 이 순서대로 이제 진행할건데 1번만 먼저 알려줘. 버그 방지 및 기존에 잘 세팅된 부분은 무너지지 않도록 한번에 프롬프트로 다 알려줘.

11s 동안 생각함
좋습니다. 지금은 1️⃣ 스킬 애니메이션 타입 매핑 시스템만 먼저 넣는 게 맞습니다.
특히 지금 Feloria는 이미
전투 루프
스킬 데이터
class 기반 스킬 배정
creature sprite 연결
graphics asset 경로
battle background
save/load
까지 어느 정도 잡혀 있기 때문에,
이번 단계에서는 절대 전투 규칙을 흔들지 말고 “스킬 실행 시 어떤 애니메이션을 보여줄지”만 안정적으로 붙여야 합니다.
즉 목표는 이겁니다.
스킬 사용
→ 스킬 타입/카테고리 확인
→ 맞는 animation key 선택
→ 타격 위치에 맞게 재생
→ 기존 데미지/턴 진행은 그대로 유지
아래 프롬프트를 그대로 한 번에 안티그래비티에 보내세요.

Feloria - Phase: Skill Animation Type Mapping System

We are now implementing only Step 1:
Skill Animation Type Mapping System.

Important:
Do NOT change the core combat rules.
Do NOT change skill damage balance.
Do NOT rewrite the existing battle loop unless absolutely necessary.
Do NOT break the currently working battle, save/load, quest, or creature systems.

I will verify everything manually myself.
Do not claim anything is verified.

The purpose of this phase is:
When a creature uses a skill, the game should automatically choose and play the correct battle animation based on the skill's type/category.

This must be implemented safely so that the already working parts of Feloria remain stable.

--------------------------------
PHASE GOAL
--------------------------------

Add a data-driven skill animation mapping layer.

Current gameplay systems that must remain intact:
- creature skills
- skill selection menu
- turn flow
- battle results
- save/load
- progression
- evolution
- encounter tables

This phase should only add:
- animation mapping
- animation config
- safe playback in battle

--------------------------------
1. CORE DESIGN
--------------------------------

Implement a type-based skill animation mapping system.

A skill should no longer rely on ad-hoc or hardcoded visual choices inside BattleScene.

Instead:

skill
→ animation mapping lookup
→ animation config lookup
→ battle playback

Use a data-driven approach.

--------------------------------
2. REQUIRED FILES / STRUCTURE
--------------------------------

Add or update a structure like:

src/game/data/skillAnimationMap.js
src/game/data/animationConfig.js

or equivalent.

Recommended responsibility split:

1. skillAnimationMap.js
- maps skill id OR skill type/category to an animation key

2. animationConfig.js
- defines how each animation is played:
  frame count
  frame size
  frame rate
  scale
  origin
  offsets
  anchor type
  blend mode if needed

Do not scatter animation logic across multiple scenes inconsistently.

--------------------------------
3. MAPPING RULES
--------------------------------

Map skills by type first.
If needed, allow specific skill overrides.

Supported elemental/type families should include at least:

forest / grass
fire
water
rock
shadow
ice
storm
spirit
mystic
light
normal / physical

Recommended default mapping examples:

forest / grass:
- vine_swipe → nature_slash
- leaf_dart → leaf_hit
- root_snare → forest_bind

fire:
- ember_bite → fire_burst
- flame_dash → fire_strike
- inferno_slash → fire_column

water:
- water_slash → water_hit
- mist_burst → mist_bloom
- tidal_wave → water_surge

rock:
- pebble_toss → rock_hit
- rock_smash → earth_impact

shadow:
- shadow_sneak → dark_slash
- dark_pulse → dark_burst
- phantom_claw → dark_hit

ice:
- ice_shard → ice_hit
- frost_breath → frost_wave
- absolute_zero → ice_field

storm:
- spark_strike → thunder_hit
- thunder_paw → lightning_burst
- storm_call → storm_column

spirit:
- soul_reap → spirit_burst
- spectral_strike → spirit_flash

mystic:
- mana_burst → mystic_bloom
- cosmic_roar → mystic_ring
- celestial_strike → star_flash

light:
- holy_smite → light_burst
- solar_beam → light_column
- sunflare → radiance_flash

normal / physical:
- scratch → slash_hit
- bite → impact_hit
- quick_strike → fast_slash
- flurry → multi_hit

--------------------------------
4. ANIMATION SOURCE RULES
--------------------------------

Use only the already loaded animation assets under:

/assets/animations/

Do not invent missing image paths.
Do not silently swap in unrelated assets.

Use existing folders/assets such as:
- Fire*
- Ice*
- Thunder*
- Wind*
- Darkness*
- Heal*
- Sword*
- Spear*
- Special*
- Meteor*
- Light*

--------------------------------
5. ANIMATION CONFIG SYSTEM
--------------------------------

Because animation sheets have different visual centers and sizes, each animation must have config.

Each animation config should support fields like:

key
frameWidth
frameHeight
frameCount
frameRate
scale
originX
originY
offsetX
offsetY
anchorType
loop
holdLastFrame
blendMode

Anchor types should support at least:

center
feet
aboveTarget
screenCenter

Examples:
- slash / hit → center
- pillar / beam → feet
- burst / flash → center
- full-screen ultimate → screenCenter

Do not assume every animation can use the same origin or the same offset.

--------------------------------
6. BATTLESCENE INTEGRATION
--------------------------------

BattleScene should remain the place that plays the effect,
but BattleScene must NOT hardcode animation choice per skill in a scattered way.

Required flow:

1. skill is selected
2. BattleScene reads skill id and skill type
3. mapping layer resolves animation key
4. animation config is loaded
5. effect spawns at target anchor
6. animation plays
7. existing battle logic continues normally

Important:
The existing damage / turn result flow must remain intact.

Animation playback must not block the battle permanently.

--------------------------------
7. SAFE FALLBACK RULE
--------------------------------

If a skill has no exact animation mapping:

fallback order:
1. exact skill mapping
2. type-based mapping
3. normal physical fallback

If nothing exists:
use a safe generic hit effect

Examples:
- slash_hit
- impact_hit
- light_burst

Do NOT crash the battle if one skill mapping is missing.

Log missing mappings clearly in the console.

--------------------------------
8. BUG PREVENTION RULES
--------------------------------

This phase must not break already working systems.

Specifically prevent:

1. battle soft-lock after animation
2. skill button no longer responding
3. damage applying before/after the wrong target state
4. animation spawning far away from the target
5. missing animation causing scene crash
6. wrong skill triggering wrong unrelated element effect
7. save/load corruption from animation-only data
8. enemy turn failing because skill animation lookup is missing
9. legendary / special skills accidentally using generic placeholder forever without logs

--------------------------------
9. POSITIONING RULES
--------------------------------

Animation placement must be target-relative.

Default rules:
- slash / impact / burst → target center
- fire pillar / ice field / beam → target feet or lower body
- flash / light pulse → target center
- screen-wide ultimates → screen center

Do not use top-left sprite position as the final visual anchor.

Use animation config to tune:
- scale
- origin
- offset

Keep this tuning data-driven.

--------------------------------
10. SKILL CATEGORY SUPPORT
--------------------------------

If the current skill system already has categories such as:
attack
buff
debuff
heal
ultimate

then animation mapping may also use category as a secondary rule.

Examples:
- heal category → Heal*
- debuff category → Darkness* or State*
- buff category → Light* or Special*
- ultimate category → Special* / Meteor*

But:
type identity should remain the primary source when possible.

--------------------------------
11. ENEMY SKILLS
--------------------------------

Enemy creatures must use the exact same mapping system.

Do not create a separate animation system for enemy skills.

The same skill id should always resolve to the same animation logic,
whether used by the player or by an enemy.

--------------------------------
12. MANUAL-TEST READY TARGETS
--------------------------------

Implement this so I can manually verify:

1. fire skills play fire-like effects
2. water skills play water-like effects
3. ice skills play ice-like effects
4. shadow skills play dark-like effects
5. normal physical skills play slash/impact effects
6. enemy skills also trigger mapped animations
7. if a skill mapping is missing, battle still continues with a safe fallback
8. already working battle flow remains stable

--------------------------------
13. IMPLEMENTATION PREFERENCE
--------------------------------

Use a simple and maintainable structure.

Recommended:
- one centralized mapping file
- one centralized config file
- one playback helper inside BattleScene or battle system

Avoid:
- giant if/else chains spread across BattleScene
- per-skill hardcoding mixed with damage logic
- animation path guesses at runtime

--------------------------------
14. FINAL NOTE
--------------------------------

This phase is only for skill animation type mapping.

Do not start:
- type advantage system
- codex UI
- battle rebalance
- new creature deployment
- major scene refactors

Only add a robust, data-driven animation mapping layer on top of the existing stable battle system.

이 프롬프트의 의도
이걸 보내면 안티그래비티가 해야 하는 일은 딱 이 범위로 제한됩니다.
skillAnimationMap.js 만들기
animationConfig.js 만들기
BattleScene에서 스킬 실행 시 애니메이션 lookup해서 재생
없는 경우 fallback
즉 이미 잘 되는 전투 시스템을 최대한 안 건드리고,
그 위에 **“보이기만 하는 계층”**을 얹는 방식이라 안전합니다.

추가로 아주 짧게 붙이면 좋은 한 줄
