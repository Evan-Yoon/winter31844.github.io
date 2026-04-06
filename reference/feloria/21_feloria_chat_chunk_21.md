# Feloria 대화 내역 분할본 21

- 원본 파일: Feloria 대화 내역.docx
- 분할 번호: 21/28
- 문단 범위: 3765~3888
- 문자 수(대략): 26373

---

Feloria - Graphics Asset Usage Rules

The project now includes a full RPG-style Graphics asset pack.

Do NOT guess where images should be used.
Use each folder strictly according to the rules below.

These assets follow RPG Maker folder conventions.

--------------------------------
GLOBAL RULE
--------------------------------

Never mix asset types.

Each folder has a fixed responsibility.

Do not use images from one category in another system.

--------------------------------
1. ANIMATIONS
--------------------------------

Folder:
Graphics/Animations

Purpose:
Skill effects and combat animations.

Use for:
- attack animations
- skill animations
- elemental effects

Examples:

Fire skills:
Fire1
Fire2
Fire3
Fire4

Ice skills:
Ice1
Ice2
Ice3
Ice4
Ice5

Lightning skills:
Thunder1
Thunder2
Thunder3
Thunder4

Wind skills:
Wind1
Wind2
Wind3

Dark skills:
Darkness1
Darkness2
Darkness3

Healing skills:
Heal1
Heal2
Heal3
Heal4
Heal5
Heal6

Weapon style attacks:
Sword1
Sword2
Sword3
Sword4
Sword5
Sword6
Sword7
Sword8
Sword9
Sword10

Projectile skills:
Spear1
Spear2
Spear3

Meteor / special ultimate skills:
Meteor
Special1
Special2
Special3
Special4
Special5
Special6
Special7
Special8
Special9
Special10
Special11
Special12
Special13
Special14
Special15
Special16
Special17

--------------------------------
2. BATTLEBACKS1
--------------------------------

Folder:
Graphics/Battlebacks1

Purpose:
Battle ground layer.

This is the lower battle background.

Use examples:

Forest battles:
Grassland
GrassMaze

Cave battles:
RockCave1
RockCave2
DirtCave

Volcano battles:
Lava1
Lava2
LavaCave

Snow battles:
Snow
Snowfield

Desert battles:
Desert
Sand

Poison area battles:
PoisonSwamp

Dungeon battles:
Ruins1
Ruins2
Ruins3

--------------------------------
3. BATTLEBACKS2
--------------------------------

Folder:
Graphics/Battlebacks2

Purpose:
Battle sky / environment background.

Use examples:

Forest:
Forest1
Forest2

Cave:
Cliff
RockCave

Volcano:
Lava
LavaCave

Snow:
Snowfield

Sea:
Sea
Ocean

Sky:
Sky
Clouds

Castle battles:
Castle1
Castle2
Castle3

Temple battles:
Temple

--------------------------------
4. BATTLERS
--------------------------------

Folder:
Graphics/Battlers

Purpose:
Enemy battle sprites.

Use for:
- enemy monsters
- boss enemies
- special battle creatures

Do NOT use these for overworld characters.

--------------------------------
5. CHARACTERS
--------------------------------

Folder:
Graphics/Characters

Purpose:
Overworld sprites.

Use for:

Player character:
Actor1
Actor2
Actor3
Actor4

NPCs:
People1
People2
People3
People4
People5
People6
People7
People8

Enemies walking in overworld:
Monster1
Monster2
Monster3

Animals / environmental NPCs:
Animal

Vehicles:
Vehicle

--------------------------------
6. FACES
--------------------------------

Folder:
Graphics/Faces

Purpose:
Dialogue portraits.

Use for:

NPC dialogue portraits:
People1
People2
People3
People4

Story characters:
Actor1
Actor2
Actor3
Actor4
Actor5

Villains:
Evil

Monster conversations if needed:
Monster1

--------------------------------
7. PARALLAXES
--------------------------------

Folder:
Graphics/Parallaxes

Purpose:
Large background scenery for maps.

Use for:

World map sky:
BlueSky
CloudySky1
CloudySky2

Mountains background:
Mountains1
Mountains2
Mountains3
Mountains4
Mountains5

Ocean horizon:
Ocean1
Ocean2
SeaOfClouds

Space / mystical areas:
DarkSpace1
DarkSpace2
StarlitSky

Special sky:
Sunset

--------------------------------
8. TILESETS
--------------------------------

Folder:
Graphics/Tilesets

Purpose:
Map building tiles.

These are used by the tilemap system.

Examples:

Dungeon tiles:
Dungeon_A1
Dungeon_A2
Dungeon_A4
Dungeon_A5
Dungeon_B
Dungeon_C

Interior tiles:
Inside_A1
Inside_A2
Inside_A4
Inside_A5
Inside_B
Inside_C

Exterior tiles:
Outside_A1
Outside_A2
Outside_A3
Outside_A4
Outside_A5
Outside_B

--------------------------------
9. TILES1
--------------------------------

Folder:
Graphics/Tiles1

Purpose:
Special map decorations or event backgrounds.

Use for:

World map screens:
WorldMap

Volcano events:
Volcano

Castle scenes:
Castle

Temple events:
Gates

Magical events:
Hexagram
Crystal

Story scenes:
Book
CrossedSwords
Dragon
Universe

--------------------------------
10. TILES2
--------------------------------

Folder:
Graphics/Tiles2

Purpose:
Decorative UI frames and thematic overlays.

Examples:

Fire themed UI:
Fire

Forest themed UI:
Forest
Leaves

Metal UI frames:
Metal

Mist environments:
Mist

Mountain UI scenes:
Mountains

Dragon frame effects:
Dragons

--------------------------------
INTEGRATION RULES
--------------------------------

1. Use tilemaps from Tilesets for world maps.

2. Use Characters for overworld movement.

3. Use Battlers for enemies.

4. Use Animations for skill effects.

5. Combine Battlebacks1 + Battlebacks2 to create battle scenes.

6. Use Faces only in dialogue windows.

7. Use Parallaxes for large scenic backgrounds.

--------------------------------
GOAL
--------------------------------

Integrate these graphics into Feloria so the game has:

- animated combat effects
- proper battle backgrounds
- character sprites
- tile-based maps
- cinematic environments

중요한 팁 (꼭 읽어주세요)
지금 이 Graphics 팩은 RPG Maker용이라서
Feloria에 넣을 때는 이렇게 사용하는 것이 안정적입니다.
Feloria 구조 예시:
public/assets/
   animations/
   battlebacks1/
   battlebacks2/
   battlers/
   characters/
   faces/
   parallaxes/
   tilesets/
그리고 PreloadScene에서 각각 불러옵니다.
예:
this.load.image("battle_grass", "assets/battlebacks1/Grassland.png")
this.load.image("battle_forest", "assets/battlebacks2/Forest1.png")

추천 전투 배경 조합
가장 자연스러운 조합 몇 개 알려드리면
숲 전투
Battleback1: Grassland
Battleback2: Forest1
동굴 전투
Battleback1: RockCave1
Battleback2: Cliff
화산 전투
Battleback1: Lava1
Battleback2: Lava
눈 지역
Battleback1: Snowfield
Battleback2: Snow

다음 단계 (진짜 중요)
지금 이 Graphics를 넣으면 Feloria는 바로
전투 연출
캐릭터 스프라이트
타일 기반 맵
배틀 배경
이 4개가 동시에 업그레이드됩니다.
원하시면 다음 답변에서 제가
Feloria용 "그래픽 적용 순서 7단계"
도 만들어 드리겠습니다.
이 순서대로 하면 게임 퀄리티가 갑자기 확 올라갑니다.


말해준대로 사진을 다 넣었어. 이 기준으로 위 프롬프트를 더 구체적으로 적어줘. path까지 antigravity가 헷갈리지 않게.

좋습니다. 지금 구조는 매우 좋게 정리된 상태입니다.
이제 중요한 것은 Antigravity가 경로(path)를 절대 헷갈리지 않게 하는 것입니다.
지금 폴더 구조는 다음입니다.
public/assets/
  animations/
  battlebacks1/
  battlebacks2/
  characters/
  faces/
  parallaxes/
  sprites/
  system/
  tilesets/
  tiles1/
  tiles2/
  ui/
따라서 모든 경로를 절대경로 /assets/... 기준으로 고정해서 프롬프트를 써야 합니다.

Antigravity용 완전 구체적 프롬프트 (경로 고정)
아래를 그대로 넣으면 됩니다.
Feloria - Graphics Integration Using Asset Pack

The project now includes a complete graphics asset pack placed under:

public/assets/

All assets must be loaded from the exact folder paths below.

Do not guess paths.
Do not change folder names.
Do not move files.

Always use the exact paths given.

--------------------------------
GLOBAL LOADING RULE
--------------------------------

All assets must be loaded in PreloadScene using this path format:

/assets/<folder>/<file>

Example:

this.load.image("battle_grass", "/assets/battlebacks1/Grassland.png")

--------------------------------
1. ANIMATIONS
--------------------------------

Folder:

public/assets/animations/

Use for:
- skill effects
- attack animations
- elemental effects

Example loads:

this.load.spritesheet("fire1", "/assets/animations/Fire1.png")
this.load.spritesheet("ice1", "/assets/animations/Ice1.png")
this.load.spritesheet("thunder1", "/assets/animations/Thunder1.png")
this.load.spritesheet("wind1", "/assets/animations/Wind1.png")
this.load.spritesheet("heal1", "/assets/animations/Heal1.png")

These animations should be used when executing creature skills.

--------------------------------
2. BATTLE BACKGROUNDS
--------------------------------

Ground layer:

public/assets/battlebacks1/

Sky/environment layer:

public/assets/battlebacks2/

Example battle background combinations:

Forest battle:

battlebacks1: Grassland
battlebacks2: Forest1

Load example:

this.load.image("battle_grassland", "/assets/battlebacks1/Grassland.png")
this.load.image("battle_forest", "/assets/battlebacks2/Forest1.png")

Volcano battle:

battlebacks1: Lava1
battlebacks2: Lava

Snow battle:

battlebacks1: Snowfield
battlebacks2: Snow

Desert battle:

battlebacks1: Desert
battlebacks2: Desert

--------------------------------
3. OVERWORLD CHARACTER SPRITES
--------------------------------

Folder:

public/assets/characters/

Use for:

Player character
NPC characters
Trainer NPCs

Example loads:

this.load.spritesheet("player", "/assets/characters/Actor1.png")
this.load.spritesheet("npc_people", "/assets/characters/People1.png")
this.load.spritesheet("npc_people2", "/assets/characters/People2.png")

Use these sprites for:

WorldScene movement
NPC dialogue interaction

--------------------------------
4. DIALOGUE PORTRAITS
--------------------------------

Folder:

public/assets/faces/

Use only in dialogue UI.

Example loads:

this.load.image("face_actor1", "/assets/faces/Actor1.png")
this.load.image("face_people1", "/assets/faces/People1.png")
this.load.image("face_evil", "/assets/faces/Evil.png")

Do not use these in the overworld.

--------------------------------
5. MAP TILESETS
--------------------------------

Folder:

public/assets/tilesets/

Use for building maps.

Examples:

this.load.image("tileset_outside", "/assets/tilesets/Outside_A1.png")
this.load.image("tileset_dungeon", "/assets/tilesets/Dungeon_A1.png")
this.load.image("tileset_inside", "/assets/tilesets/Inside_A1.png")

Use these for tilemaps in WorldScene.

--------------------------------
6. SPECIAL MAP SCENES
--------------------------------

Folder:

public/assets/tiles1/

Use for special scenes such as:

- world map
- story events
- special locations

Examples:

this.load.image("world_map", "/assets/tiles1/WorldMap.png")
this.load.image("volcano_scene", "/assets/tiles1/Volcano.png")
this.load.image("castle_scene", "/assets/tiles1/Castle.png")

--------------------------------
7. UI THEMES
--------------------------------

Folder:

public/assets/tiles2/

Use for UI decorations or thematic frames.

Examples:

Fire theme UI:

/assets/tiles2/Fire.png

Forest theme UI:

/assets/tiles2/Forest.png

Metal UI frame:

/assets/tiles2/Metal.png

--------------------------------
8. PARALLAX BACKGROUNDS
--------------------------------

Folder:

public/assets/parallaxes/

Used for large scenic backgrounds.

Examples:

this.load.image("sky_blue", "/assets/parallaxes/BlueSky.png")
this.load.image("mountain_bg", "/assets/parallaxes/Mountains1.png")
this.load.image("ocean_bg", "/assets/parallaxes/Ocean1.png")
this.load.image("sunset_bg", "/assets/parallaxes/Sunset.png")

Used in:

large maps
story cutscenes
world exploration scenes

--------------------------------
9. SYSTEM UI
--------------------------------

Folder:

public/assets/system/

Used for UI system graphics.

Examples:

window frames
menu UI
battle UI elements

--------------------------------
10. CREATURE SPRITES
--------------------------------

Folder:

public/assets/sprites/

Use for:

cat monsters
battle creatures

Example:

this.load.spritesheet("leafkit", "/assets/sprites/leafkit.png")

--------------------------------
GOAL
--------------------------------

Integrate these assets into Feloria so the game includes:

- animated combat effects
- layered battle backgrounds
- character sprites for overworld
- dialogue portraits
- tile-based maps
- scenic parallax environments

추가로 꼭 해야 하는 것 (중요)
지금 구조에서 PreloadScene을 다음처럼 정리하면 Antigravity가 안정적으로 씁니다.
예:
// PreloadScene.js

preload() {

  // Animations
  this.load.spritesheet("fire1", "/assets/animations/Fire1.png")

  // Battlebacks
  this.load.image("battle_grassland", "/assets/battlebacks1/Grassland.png")
  this.load.image("battle_forest", "/assets/battlebacks2/Forest1.png")

  // Characters
  this.load.spritesheet("player", "/assets/characters/Actor1.png")

  // Faces
  this.load.image("face_actor1", "/assets/faces/Actor1.png")

  // Tilesets
  this.load.image("tileset_outside", "/assets/tilesets/Outside_A1.png")

  // Parallax
  this.load.image("parallax_sky", "/assets/parallaxes/BlueSky.png")

}

하나만 더 팁
지금 asset pack 기준으로 Feloria에서 가장 잘 어울리는 조합은 이것입니다.
숲 전투
battlebacks1: Grassland
battlebacks2: Forest1
동굴
battlebacks1: RockCave1
battlebacks2: Cliff
신전
battlebacks1: Ruins1
battlebacks2: Temple
화산
battlebacks1: Lava1
battlebacks2: Lava

다음 단계 (그래픽 적용 후)
지금 단계에서 다음은 보통 이것입니다.
1️⃣ Battle animation 적용
2️⃣ Creature sprite 적용
3️⃣ Tilemap 적용
이 세 개를 순서대로 하면 Feloria 퀄리티가 갑자기 확 올라갑니다.
원하시면 다음 답변에서
**Feloria 그래픽 적용 “안전한 순서 6단계”**도 만들어드리겠습니다.
이 순서대로 하면 AI가 절대 프로젝트를 망가뜨리지 않습니다.

Implementation Plan - Graphics Integration Transition Feloria from placeholder pixel-art to a high-quality asset pack, including animations, battle backgrounds, character sprites, and UI elements. Proposed Changes Preload & Constants [MODIFY] assetPaths.js Update all constant paths to point to /assets/... instead of src/game/data/graphics/.... [MODIFY] PreloadScene.js Implement comprehensive asset loading for all categories: Animations: fire1, ice1, thunder1, etc. Battle Backgrounds: Grassland, Forest, Lava, Snow. Characters: Actor1.png, People1.png, etc. Faces: Actor1.png, People1.png, Evil.png. Sprites: (Monster1.png, Actor1.png as monster fallbacks until specific cat sprites are finalized). Tilesets: Outside_A1.png, Inside_A1.png, etc. Remove pixelArtGenerator calls as we move to real assets. Character & NPC Mapping [MODIFY] WorldScene.js Map player to /assets/characters/Actor1.png. Map elder_hyunseok and other key NPCs to specific People or Actor spritesheets. Update NPC interaction logic to use the new textures. Battle Enhancements [MODIFY] BattleScene.js Implement layered background loading (Back1 + Back2) based on the current map type. Update creature sprites to use the new Monster or Actor assets. Integrate skill animations using the newly loaded sprite sheets. UI & Cutscenes [MODIFY] DialogScene.js Add face portraits to the dialogue box using assets from /assets/faces/. [MODIFY] CutsceneScene.js Update backgrounds to use parallax assets from /assets/parallaxes/. Verification Plan Automated Tests Run npm run dev and verify no 404 errors for assets in the console. Manual Verification Verify player and NPC sprites in WorldScene. Verify background and skill animations in BattleScene. Verify portrait appearance in DialogScene. Verify the intro sequence with new high-quality backgrounds.

좋습니다. 이 플랜 방향은 맞습니다.
지금 단계에서 해야 할 핵심도 정확합니다.
특히 좋은 점은:
assetPaths.js를 /assets/... 기준으로 통일하려는 것
PreloadScene.js에서 실제 에셋 로딩으로 전환하려는 것
pixelArtGenerator 의존을 줄이려는 것
WorldScene, BattleScene, DialogScene, CutsceneScene에 각각 역할에 맞는 그래픽을 붙이려는 것
다만 이대로 진행하면 그래픽은 붙는데 게임이 깨질 가능성이 있어서, 아래 조건을 같이 붙여서 보내는 게 좋습니다.
아래 문구를 그대로 추가해서 보내세요.
Approved — proceed with the graphics integration pass.

The overall direction is correct.
However, implement this under the following constraints so visual upgrades do not break the current game.

Important:
I will do all verification manually myself.
Do not claim anything is verified.
Implement this in a way that is safe, incremental, and easy to test.

--------------------------------
GRAPHICS INTEGRATION GOAL
--------------------------------

The goal is to replace placeholder visuals with the new asset pack while preserving all existing gameplay systems.

Do not add new story content in this phase.
Do not change battle rules, save/load logic, quest flow, or NPC progression logic unless required for asset integration.

--------------------------------
PATH AND ASSET RULES
--------------------------------

1. Use only absolute public asset paths
All loaded assets must use:
- /assets/animations/...
- /assets/battlebacks1/...
- /assets/battlebacks2/...
- /assets/characters/...
- /assets/faces/...
- /assets/parallaxes/...
- /assets/sprites/...
- /assets/system/...
- /assets/tilesets/...
- /assets/tiles1/...
- /assets/tiles2/...
- /assets/ui/...

Do not load assets from src/game/data/graphics anymore.

2. Keep assetPaths.js as the single source of truth for frequently reused asset references
Do not scatter repeated hardcoded paths across multiple scenes if they refer to the same standard assets.

3. Fail safely for missing assets
If a requested asset is missing:
- do not crash the scene
- use a fallback texture or clear placeholder
- log the missing asset clearly

--------------------------------
WORLD GRAPHICS RULES
--------------------------------

4. Player and NPC overworld sprites must come only from:
- /assets/characters/

Do not use battlers, faces, or battlebacks as overworld sprites.

5. NPC role readability must be preserved
Even after sprite replacement:
- player must remain clearly distinct
- healer, shopkeeper, trainers, and lore NPCs must still be recognizable and readable
- do not assign random sprites inconsistently across scenes

6. Keep map gameplay intact
Replacing visuals must not break:
- collision
- warp logic
- encounter zones
- NPC interaction triggers
- save/load spawn restoration

--------------------------------
BATTLE GRAPHICS RULES
--------------------------------

7. Layered battle backgrounds
BattleScene should combine:
- one asset from /assets/battlebacks1/
- one asset from /assets/battlebacks2/

Use map-type-based combinations consistently.

Recommended mapping:
- forest maps -> Grassland + Forest1/Forest2
- shrine/ruin maps -> Ruins1 + Temple
- cave maps -> RockCave1 + Cliff
- snow maps -> Snowfield + Snow
- volcano maps -> Lava1 + Lava

8. Creature battle visuals
Use:
- /assets/sprites/ for creature-specific sprites if available
- fallback to battler-style assets only if specific creature art is missing

Do not use overworld character sheets as the preferred final creature battle art unless acting as an explicit temporary fallback.

9. Skill animations
Skill effects must come from:
- /assets/animations/

Do not assign random animation sheets.
Map animations by element/type where possible:

- fire -> Fire*
- ice -> Ice*
- thunder/storm -> Thunder*
- wind -> Wind*
- healing -> Heal*
- darkness/shadow -> Darkness*
- slash/weapon -> Sword* / Spear*
- ultimate/special -> Special* / Meteor

--------------------------------
DIALOGUE AND CUTSCENE RULES
--------------------------------

10. Dialogue portraits
DialogScene portraits must come only from:
- /assets/faces/

Do not use overworld sprites in portrait slots.

11. Cutscene backgrounds
CutsceneScene should use:
- /assets/parallaxes/ for scenic backgrounds
- /assets/tiles1/ for event-style special story backgrounds when appropriate

Use them intentionally, not randomly.

--------------------------------
CLEANUP RULES
--------------------------------

12. Remove or isolate placeholder generators
If pixelArtGenerator is no longer needed for runtime visuals:
- remove its runtime usage
- keep it only if clearly marked as a temporary fallback/dev utility

13. Keep visual integration separate from gameplay refactors
Do not combine this phase with major combat, quest, or NPC logic rewrites unless necessary.

--------------------------------
BUG PREVENTION RULES
--------------------------------

1. Do not break scene loading due to missing assets
2. Do not break save/load due to renamed or missing textures
3. Do not break NPC visibility or player visibility in WorldScene
4. Do not break battle layout due to oversized background or sprite scaling
5. Do not mix face assets, battlers, and character sheets into the wrong systems
6. Do not remove fallback behavior until replacement assets are confirmed loaded

--------------------------------
MANUAL-TEST READY TARGETS
--------------------------------

Implement these so I can test them manually:

1. StartScene loads with correct visuals and no broken paths
2. WorldScene shows visible player and NPC sprites from /assets/characters/
3. DialogScene displays face portraits from /assets/faces/
4. BattleScene uses battlebacks1 + battlebacks2 correctly
5. Skill animations display from /assets/animations/
6. CutsceneScene uses proper background assets from /assets/parallaxes/ or /assets/tiles1/
7. No major gameplay regression occurs after visual replacement

--------------------------------
FINAL NOTE
--------------------------------

The goal of this phase is visual replacement with minimal gameplay risk.
Prioritize path correctness, fallback safety, and role-appropriate asset usage over aggressive refactoring.
그리고 지금 플랜에서 제가 추가로 권하는 건 딱 3가지입니다.
첫째, 한 번에 전부 바꾸지 말고 순서를 나눠서 적용하는 겁니다.
가장 안전한 순서는:
PreloadScene 경로 정리
WorldScene 캐릭터 스프라이트 적용
BattleScene 배경 적용
DialogScene 얼굴 적용
CutsceneScene 배경 적용
마지막으로 스킬 애니메이션 적용
둘째, Player/NPC/Creature를 절대 섞지 않는 것입니다.
월드 이동 캐릭터 = characters
대화 초상화 = faces
배틀 배경 = battlebacks1/2
스킬 효과 = animations
몬스터 전투 스프라이트 = sprites 우선
셋째, fallback을 남겨두는 것입니다.
특정 고양이 스프라이트가 아직 없으면 바로 깨지지 말고, 임시 대체 텍스처로 넘어가게 해야 합니다.
원하시면 다음 답변에서 제가
Feloria용 battlebacks 매핑표를 맵별로 바로 써드리겠습니다.
예를 들면 greenpaw_forest → Grassland + Forest1 이런 식으로 바로 적용 가능한 형태로요.


큰일났어.. 캐릭터 크롭도 제대로 못하고, 방향키를 누를 때 마다 캐릭터가 바뀌네.. 뭐라고 해야하지

지금 상태는 스프라이트시트 해석을 완전히 잘못한 것입니다.
문제를 나누면 3개입니다.
크롭 실패
→ 캐릭터 한 명만 잘라 써야 하는데, 시트 전체 또는 잘못된 칸을 읽고 있음
방향키 누를 때 캐릭터 자체가 바뀜
→ 방향 전환이 아니라 다른 캐릭터 블록으로 인덱스가 이동하고 있음
행/열 규칙을 잘못 적용
→ RPG Maker 스타일 캐릭터 시트의 프레임 규칙을 제대로 안 지킴
즉, 지금은 “애니메이션 문제”가 아니라 spritesheet slicing 규칙이 틀린 겁니다.
아래 문구를 그대로 안티그래비티에 보내세요.
The character spritesheet parsing is incorrect and must be fixed before any further graphics work.

Current problems:
1. Character cropping is wrong
2. Pressing arrow keys changes the entire character instead of changing direction
3. Sprite frame selection is reading the wrong rows/columns from the character sheet

This is a spritesheet slicing / animation indexing problem.

Fix it under the following exact rules.

--------------------------------
CHARACTER SHEET RULES
--------------------------------

The overworld character graphics are RPG Maker style character sheets.

Do not treat them like arbitrary sprite atlases.

You must correctly slice and animate them using RPG-style frame logic.

Expected behavior:
- one selected character from a sheet
- 4 directions only: down, left, right, up
- 3 walking frames per direction
- movement changes animation frame
- direction key changes facing direction only
- direction key must NOT switch to a different character in the sheet

--------------------------------
REQUIRED FIXES
--------------------------------

1. Select one character block from the spritesheet
Each sheet may contain multiple characters.
You must choose a single character block and use only that block for one NPC or for the player.

Do not animate across the entire sheet.

2. Correct frame layout
Use the standard layout:
- 3 columns per direction
- 4 rows for directions
- directions must map consistently:
  down
  left
  right
  up

3. Correct animation logic
When idle:
- use the center frame of the current direction row

When moving:
- cycle only across the 3 frames of the current direction row

Do not move into another character block.
Do not change character identity when changing direction.

4. Correct sprite cropping
Crop exactly one character’s 3x4 block from the larger character sheet.

Do not render neighboring characters from the same sheet.
Do not show broken partial heads/bodies from adjacent sprites.

5. Stable player mapping
The player must always use one fixed character sprite.
Arrow keys should only change:
- facing direction
- walk animation frame

Never switch the player to another person on the sheet.

6. Stable NPC mapping
Each NPC must also use one fixed character selection from a sheet.
Do not let NPCs accidentally render as partial frames from nearby sprites.

--------------------------------
IMPLEMENTATION RULES
--------------------------------

1. Add a helper for character sheet extraction
Create a clear utility/helper that:
- takes sheet texture
- takes character index or block coordinates
- returns the correct 3x4 sub-grid for that one character

2. Use explicit frame math
Do not guess frame positions.

For each chosen character block, calculate:
- character block width
- character block height
- frame width = block width / 3
- frame height = block height / 4

3. Keep the current gameplay intact
Do not break:
- movement
- collisions
- NPC interaction
- save/load
- map transitions

--------------------------------
VISUAL CORRECTION GOAL
--------------------------------

After the fix:
- player appears as one complete human sprite
- NPCs appear as one complete human sprite each
- pressing left/right/up/down only changes facing direction
- walking animates correctly
- no partial characters or adjacent-sheet bleed appears

--------------------------------
MANUAL TEST TARGETS
--------------------------------

I will manually test:
1. player stands still correctly
2. player walks without switching identity
3. changing direction changes only facing row
4. NPCs render as complete single characters
5. no neighboring sprite fragments appear

Fix this spritesheet parsing issue first before doing any more visual polish.
그리고 원인 자체를 아주 쉽게 말하면 이겁니다.
지금 잘못된 방식
전체 Actor1.png를 하나의 큰 시트로 보고
방향키 입력마다 다음 행/열로 막 이동
결과적으로 다른 캐릭터 칸까지 넘어감
맞는 방식
Actor1.png 안에서 딱 한 명의 3x4 블록만 먼저 선택
그 블록 안에서만
위/아래/좌/우 행 전환
3프레임 걷기 애니메이션

원하시면 다음 답변에서 제가
RPG Maker Characters 시트를 Phaser에서 자르는 방식을 아주 쉽게 설명한 개발자용 프롬프트도 따로 써드리겠습니다.

Implementation Plan - Graphics Fixes & UI Polish Fix character spritesheet parsing (RPG Maker style) and refine the dialogue portrait layout. Proposed Changes Spritesheet Slicing System [MODIFY] assetPaths.js Add characterIndex to character definitions to identify which block to use. [MODIFY] WorldScene.js Implement getCharacterFrames(index) helper to calculate the correct frame indices. Update createPlayer and createNPCs to use the correct starting frame. Update movePlayer to animate across only the 3 frames of the current direction's row within the character block. Dialogue Layout Refinement [MODIFY] DialogScene.js Reorganize layout: Name at top-left, Portrait below it, Text to the right of the portrait. Adjust cropping/size of the face portrait. Verification Plan Manual Verification Verify player identity remains stable when moving/changing direction. Verify NPC identity remains stable. Verify dialogue UI matches the requested layout.

좋습니다. 이 플랜 방향은 맞습니다.
지금 최우선 과제가 정확히 RPG Maker 스타일 캐릭터 시트 파싱 수정이기 때문에, 이걸 먼저 잡는 게 맞습니다.
다만 그대로 진행시키기보다, 프레임 계산 규칙과 안전장치를 더 명확히 못 박아야 합니다.
안 그러면 또 “대충 맞는 것처럼 보이지만 다른 캐릭터 블록을 침범하는” 상태가 나올 수 있습니다.
아래 문구를 그대로 추가해서 보내세요.
