# Feloria 대화 내역 분할본 03

- 원본 파일: Feloria 대화 내역.docx
- 분할 번호: 3/28
- 문단 범위: 612~669
- 문자 수(대략): 27702

---

You are upgrading an existing React + TypeScript + Vite game project at:
https://github.com/Evan-Yoon/Feloria

Goal:
Transform the current prototype into a much more polished top-down pixel RPG that visually feels close to a classic handheld monster-collection game, while remaining fully original in names, world, UI, monsters, and systems.

Do NOT rebuild the project as a dashboard-style React app.
Do NOT keep placeholder debug visuals.
Do NOT use plain text tiles or generic colored blocks as final world graphics.

Core gameplay pillars that must exist and feel connected:
- exploration
- dialogue
- capture/recruitment
- turn-based battle
- codex / monster encyclopedia
- evolution
- quests

Important diagnosis of the current codebase:
- The current project already has a React + Zustand structure, a canvas world renderer, basic battle, starter selection, evolution, cats/skills/items data, and two early maps.
- However, the overworld is too primitive: hardcoded tile index rendering, very flat map layout, only a couple of maps, placeholder battle sprites, and no high-quality world art direction.
- Improve the existing codebase instead of replacing everything blindly.

Primary upgrade strategy:
1. Keep React + TypeScript + Vite + Zustand.
2. Replace the current hand-authored primitive map rendering with a proper overworld pipeline.
3. Preferred option: migrate overworld rendering to Phaser 3 while keeping React for menus/overlays.
4. If Phaser migration is too risky, keep Canvas but rebuild map rendering around proper tile atlas metadata and layered maps.
5. Use Tiled-compatible JSON maps or a strongly typed tilemap format with:
   - ground layer
   - detail layer
   - collision layer
   - object layer
   - tall grass / encounter layer
   - decoration layer
   - warp layer
6. Refactor the current project so that the world actually looks like a real retro pixel RPG field.

Visual target:
The field should look like a classic top-down pixel RPG with:
- curved paths
- tree walls and forest edges
- tall grass patches
- flowers and decoration
- fences
- water edges and shoreline
- houses with doors and roofs
- signposts
- layered terrain depth
- readable player sprite
- camera follow
- smooth tile movement
- subtle idle/walk animation
- natural map composition instead of flat test rectangles

World art direction:
Use pixel-art assets, not realistic photos, for the actual playable world.
If image references are needed, use them only as source inspiration for environment mood and color palette.
For the game itself, use consistent pixel tilesets and sprites.

Use these asset sources as references or downloadable packs:
ASSET_LINKS = {
  "tileset_main_kenney_rpg": "https://kenney.nl/assets/roguelike-rpg-pack",
  "tileset_town_kenney": "https://kenney.nl/assets/tiny-town",
  "characters_kenney": "https://kenney.nl/assets/roguelike-characters",
  "dungeon_kenney": "https://kenney.nl/assets/roguelike-caves-dungeons",
  "rpgmaker_style_tiles_finalbossblues": "https://finalbossblues.itch.io/openrtp-tiles"
}

Use those links as source references and organize the local assets in:
src/assets/
  tilesets/
  sprites/player/
  sprites/npcs/
  sprites/cats/
  ui/
  portraits/

Do not hotlink external assets at runtime if local copies are more stable.
Instead:
- document the source links
- import or place the files locally
- wire them properly through Vite-safe imports or /public assets

Technical rendering requirements:
- Tile size: 16x16 or 32x32, but be consistent across all maps
- Camera follow centered on player
- Movement: one tile at a time with interpolation
- Collision on trees, walls, water, rocks, houses, fences
- Separate visual layers from collision logic
- Animated tall grass when entered
- Better map transitions with fade in/out
- NPC rendering and facing direction
- Signs, chests, shop counters, warp doors, trial entrances

Map design requirements:
Rebuild at least these maps in a more polished way:
1. Starwhisk Village
   - proper central road
   - multiple houses
   - shop exterior
   - elder house
   - signpost
   - decorative flowers/fences
   - village square feeling
   - north exit to forest
2. Greenpaw Forest
   - curved walkable route
   - multiple tall grass zones
   - hidden item spot
   - one optional side clearing
   - at least one NPC encounter
3. Mosslight Path
   - route map between village and next area
   - trainer-style NPC battle
   - branching path
4. Aurora Creek
   - water-heavy map
   - shoreline tiles
   - water-themed encounter pool
5. Shadowvale Ruins
   - darker palette
   - puzzle/event layout
6. Celestial Gate
   - dramatic late-game entrance area

Gameplay system requirements:
Exploration
- player movement
- interact key
- camera follow
- map warps
- interactable signs/chests/NPCs

Dialogue
- proper retro text box UI
- speaker name
- multi-page dialogue
- simple yes/no choice support
- quest acceptance flow

Capture / recruit
- keep the current item-based recruit mechanic, but improve UX
- better text feedback
- recruit animation or screen flash
- visible recruit chance tuning logic
- prevent trivial over-capturing early game

Battle
- keep turn-based structure
- improve layout and visual clarity
- add enemy/back-facing player cat sprites
- improve enemy AI so it does not always pick the first move
- add type effectiveness table
- add status/support effects handling
- add better battle transitions and message pacing

Codex
- implement a proper codex screen
- seen / recruited states
- species portrait or sprite
- species description
- habitat / area found
- evolution chain preview

Evolution
- keep current level-based evolution
- show a better evolution screen
- add species portrait/sprite before and after
- update codex entry after evolution

Quests
- implement a lightweight quest system
- active quests / completed quests
- objective text
- simple trigger conditions:
  - talk to NPC
  - reach map
  - recruit cat
  - win battle
  - collect item

Data architecture:
Refactor data into separate files if needed:
- cats
- skills
- items
- maps
- encounter tables
- npcs
- quests
- codex entries
- type chart
- evolution chains

Code quality requirements:
- TypeScript strict mode
- avoid giant one-line components
- split large components into small maintainable files
- create reusable systems for map rendering and interaction
- use asset metadata instead of magic tile numbers where possible
- do not keep placeholder comments as final implementation
- ensure imports are clean and Vite-safe

Deliverables:
1. upgraded overworld rendering system
2. improved pixel-art map presentation
3. working NPC dialogue
4. improved battle presentation
5. working recruit/capture flow
6. codex screen
7. quest system MVP
8. evolution flow polish
9. asset folder structure and import cleanup
10. README update explaining controls, asset setup, and how to run

Also update README so it is no longer the default Vite template.
Document:
- controls
- how maps work
- how encounters work
- how to add new cats
- how to add new maps
- where asset packs came from

Final requirement:
The result must feel like a real playable monster-collection RPG prototype, not a debug tilemap demo.
Prioritize visual overworld quality first, because exploration is the foundation for dialogue, capture, battle, codex, evolution, and quests.
추가로, 안티그래비티가 에셋까지 건드릴 때 덜 헷갈리게 하려면 아래 보조 지시문도 같이 붙이시는 게 좋습니다.
Use these local asset path conventions:

const ASSET_PATHS = {
  tilesetOverworld: "/assets/tilesets/overworld_main.png",
  tilesetVillage: "/assets/tilesets/village_exteriors.png",
  tilesetForest: "/assets/tilesets/forest_details.png",
  tilesetDungeon: "/assets/tilesets/ruins_dungeon.png",
  playerSprite: "/assets/sprites/player/player_sheet.png",
  npcSprites: "/assets/sprites/npcs/",
  catSpritesFront: "/assets/sprites/cats/front/",
  catSpritesBack: "/assets/sprites/cats/back/",
  uiFrame: "/assets/ui/frame.png",
  uiPanel: "/assets/ui/panel.png"
};

Document these reference sources in comments:
- https://kenney.nl/assets/roguelike-rpg-pack
- https://kenney.nl/assets/tiny-town
- https://kenney.nl/assets/roguelike-characters
- https://kenney.nl/assets/roguelike-caves-dungeons
- https://finalbossblues.itch.io/openrtp-tiles
정리하면, 지금 저장소는 버릴 상태는 아니지만, “포켓몬처럼 보이게 만드는 핵심”인 오버월드 타일셋/맵 설계/스프라이트 계층이 아직 약합니다. 그 부분만 제대로 올리면 탐험, 대화, 포획, 배틀, 도감, 진화, 퀘스트가 전부 훨씬 자연스럽게 붙습니다.
원하시면 다음 답변에서 이 저장소 기준으로 “어떤 파일을 먼저 바꿔야 하는지”를 파일 단위로 쪼개서 바로 작업 순서까지 적어드리겠습니다.

좋아 너가 추천한 링크랑 🐈 고양이 캐릭터 스프라이트 (4방향 이동 및 애니메이션) 포켓몬처럼 필드를 돌아다니는 고양이를 구현하는 데 최적화된 자료들입니다. 1. LPC Cats Rework (가장 추천) 특징: 포켓몬스터 주인공처럼 상하좌우 4방향 걷기 애니메이션 프레임이 모두 포함된 고양이 스프라이트 시트입니다. 다양한 색상의 고양이가 포함되어 있어 여러 종류의 고양이 포켓몬을 만들기 좋습니다. 링크: https://opengameart.org/content/cats-rework 2. Cat Sprites Collection 특징: 치즈 냥이, 턱시도, 샴 등 귀여운 도트 그래픽 고양이들이 걷거나 뛰는 모션이 포함되어 있습니다. 사이즈별로 분리되어 있어 활용하기 편리합니다. 링크: https://opengameart.org/content/cat-sprites 3. Sprout Lands (동물 및 펫) 특징: 파스텔 톤의 부드럽고 귀여운 아트 스타일을 선호하신다면 아주 좋은 에셋입니다. 고양이뿐만 아니라 다른 동물들도 있어 게임 내 NPC나 야생 몬스터로 활용할 수 있습니다. 링크: https://cupnooble.itch.io/sprout-lands-asset-pack (무료 다운로드 가능) 🗺️ 맵 타일셋 (포켓몬 스타일의 필드 및 마을 구현) 저작권 걱정 없이 상업용/비상업용 모두 완전 무료(CC0)로 제공되는 최고 품질의 타일셋들입니다. 1. Kenney Tiny RPG Forest (야생/숲 맵) 특징: 풀숲, 나무, 물가, 흙길 등 '야생 고양이 포켓몬'이 튀어나올 것 같은 자연 맵을 구성하기 완벽한 타일셋입니다. 링크: https://kenney.nl/assets/tiny-rpg-forest 2. Kenney RPG Urban Pack (마을/도시 맵) 특징: 포켓몬스터의 현대적인 마을이나 도시, 체육관, 상점 건물 등을 격자형으로 쉽게 만들 수 있도록 구성된 타일셋입니다. 링크: https://kenney.nl/assets/rpg-urban-pack 3. LPC Tile Atlas (전통 2D RPG 통합 타일셋) 특징: 고전 포켓몬스터나 RPG 메이커 감성의 방대한 타일셋으로, 실내(집 내부)와 실외(필드)를 모두 만들 수 있는 거대한 이미지 묶음입니다. 링크: https://opengameart.org/content/lpc-tile-atlas 이거랑 https://game-icons.net/ 까지 들어갈 수 있게 한번에 프롬프트로 다시 적어줘

아래는 안티그래비티에 한 번에 복사해서 넣을 수 있는 통합 프롬프트입니다.
요청하신 기능인 탐험, 대화, 포획, 배틀, 도감, 진화, 퀘스트를 포함하고,
에셋 소스로 아래 링크들이 모두 반영되도록 강하게 명시한 버전입니다.
제가 추천했던 Kenney / finalbossblues 계열
사용자가 정리한 고양이 스프라이트 / 맵 타일셋
game-icons.net 아이콘 활용
결과물이 디버그 맵이 아니라 진짜 포켓몬풍 필드로 보이도록 제한
그대로 붙여 넣으시면 됩니다.
You are upgrading and expanding an existing web game project into a polished, playable, top-down pixel-art monster-collection RPG.

PROJECT CONTEXT
- The game is a fully original cat-themed monster-collection RPG.
- It should capture the feel of classic handheld exploration games with:
  - top-down overworld exploration
  - tile-based movement
  - tall grass encounters
  - NPC dialogue
  - turn-based battles
  - capture / recruit flow
  - codex
  - evolution
  - quests
- It must NOT copy Pokémon assets, names, UI, regions, or characters.
- The game should feel similar in quality and readability to an old-school monster RPG, but remain legally and creatively original.

CORE GAMEPLAY FEATURES THAT MUST BE INCLUDED
- exploration
- dialogue
- capture / recruit
- turn-based battle
- codex / encyclopedia
- evolution
- quest system

TOP PRIORITY
The overworld must look like a real retro pixel RPG field, not a debug grid, placeholder tilemap, or dashboard-style React app.

That means:
- no text labels inside map tiles
- no flat repetitive placeholder map blocks
- no ugly prototype terrain as the final output
- the game must visually resemble a polished handheld-era top-down pixel RPG field

TECH STACK
- React
- TypeScript with strict mode
- Vite
- Zustand for state management
- Prefer Phaser 3 for overworld rendering while keeping React for menus and overlay UI
- If Phaser migration is too risky, keep Canvas rendering but refactor the tilemap engine properly
- npm install
- npm run dev
- must run locally without broken imports

GRAPHICS AND ASSET PIPELINE
Use proper pixel-art assets and organize them locally inside the project.
Do not rely on text-rendered tiles or placeholder rectangles as final visuals.
Do not hotlink assets in gameplay if local copies are more stable.
Instead:
- download or document source packs
- place them in local asset folders
- wire them through local imports or public asset paths

MANDATORY ASSET SOURCES TO SUPPORT

1) Cat character sprites (4-direction movement + animation)
Use and support these as recommended sources for cat overworld sprites and animated cats:

- LPC Cats Rework
  https://opengameart.org/content/cats-rework
  Notes:
  - strongest recommendation
  - includes 4-direction walking animation frames
  - suitable for field movement similar to classic handheld monster games
  - use for cat companions, wild cats, and possibly special NPC cats

- Cat Sprites Collection
  https://opengameart.org/content/cat-sprites
  Notes:
  - includes cute pixel cats like tuxedo, siamese, orange cats
  - useful for different recruitable species and field variants

- Sprout Lands Asset Pack
  https://cupnooble.itch.io/sprout-lands-asset-pack
  Notes:
  - soft pastel pixel art style
  - can be used for NPC animals, village pets, ambient life, decorative creature variants

2) Map tilesets for overworld / village / field / interiors
Support and document these:

- Kenney Tiny RPG Forest
  https://kenney.nl/assets/tiny-rpg-forest
  Notes:
  - forest, grass, trees, water, dirt path
  - ideal for routes, forests, wild encounter zones

- Kenney RPG Urban Pack
  https://kenney.nl/assets/rpg-urban-pack
  Notes:
  - village/town/city tiles
  - useful for houses, roads, shops, town decorations, structured settlements

- LPC Tile Atlas
  https://opengameart.org/content/lpc-tile-atlas
  Notes:
  - broad classic 2D RPG style atlas
  - supports exterior and interior maps
  - useful for houses, ruins, interior scenes, furniture, dungeon pieces

3) UI and ability/item icons
Use and support:
- Game Icons
  https://game-icons.net/
  Notes:
  - use for type icons, items, codex badges, menu symbols, status icons, quest markers
  - must be used carefully and consistently with the game’s own visual style

4) Additional previously recommended asset references
Also support and document:
- Kenney Roguelike RPG Pack
  https://kenney.nl/assets/roguelike-rpg-pack
- Kenney Tiny Town
  https://kenney.nl/assets/tiny-town
- Kenney Roguelike Characters
  https://kenney.nl/assets/roguelike-characters
- Kenney Roguelike Caves & Dungeons
  https://kenney.nl/assets/roguelike-caves-dungeons
- OpenRTP style tiles
  https://finalbossblues.itch.io/openrtp-tiles

IMPORTANT STYLE RULE
The project must not end up looking like a random mix of clashing art packs.
Choose a consistent primary art direction.
Recommended priority:
1. main world tiles from Kenney Tiny RPG Forest / RPG Urban Pack / LPC Tile Atlas
2. cat sprites from LPC Cats Rework and Cat Sprites Collection
3. UI icons from Game Icons
4. decorative or optional ambient assets from Sprout Lands only if they do not clash badly

VISUAL TARGET
The game should look like a polished retro handheld RPG with:
- tree borders
- tall grass patches
- curved and readable routes
- dirt paths
- village roads
- signs
- flowers and environmental decoration
- fences
- shoreline tiles and water boundaries
- houses with doors and roofs
- proper pathing and visual layering
- readable player and cat sprites
- animated walking
- camera follow
- map transitions with fade
- battle transitions that feel smooth and deliberate

OVERWORLD REQUIREMENTS
Implement or refactor the overworld to support:

- tile size: choose 16x16 or 32x32 and stay consistent
- layered rendering:
  - ground
  - detail
  - collision
  - interaction objects
  - decoration
  - tall grass encounter layer
  - warp layer
- player tile-by-tile movement with interpolation
- 4-direction movement
- walking animation
- collision on trees, walls, houses, water, rocks, fences
- camera follow centered on player
- smooth warp transitions between maps
- interactable NPCs, signs, chests, doors
- tall grass encounter checks only on encounter-designated tiles

MAP DESIGN REQUIREMENTS
Rebuild the current world so it feels natural and game-like.

At minimum, implement these polished maps:

1. Starwhisk Village
- central road
- multiple houses
- elder house
- item shop
- signpost
- flower patches
- fences
- starting plaza feel
- north exit leading to forest
- a few NPCs with dialogue and one quest giver

2. Greenpaw Forest
- multiple tall grass zones
- curved route
- optional hidden item
- at least one side clearing
- at least one early battle or recruit encounter
- readable tree walls and route flow

3. Mosslight Path
- route between village and new zone
- trainer-like NPC cat tamer battle
- split path or optional branch
- decorative rocks/fences

4. Aurora Creek
- shoreline
- water edges
- different encounter table
- bridge or stepping path

5. Shadowvale Ruins
- darker palette
- ancient feeling
- story/event area
- puzzle trigger objects

6. Celestial Gate
- dramatic late-game entrance
- event setup space for legendary content

GAME SYSTEM REQUIREMENTS

1) Exploration
- tile movement
- interact key
- menu key
- camera follow
- warp points
- chest opening
- sign reading
- route transitions

2) Dialogue
- retro dialogue box UI
- speaker name
- multi-page text
- yes/no choices
- quest accept/decline flow
- simple event trigger support

3) Capture / Recruit
- item-based recruit or capture system
- improved recruit chance feedback
- better UX and text logs
- visible low-HP capture bonus
- capture animation or short effect
- not too easy in early game

4) Battle
- turn-based
- enemy cat sprite in front view
- player active cat in back view or side view
- HP bar, name, level
- battle log pacing
- action selection:
  - attack
  - skill
  - item
  - switch
  - run
- type chart support
- better enemy move selection than always using the first move
- visual polish and battle transition improvements

5) Codex
- proper codex / encyclopedia screen
- seen / recruited states
- sprite preview
- species description
- habitat / area found
- evolution chain preview if unlocked

6) Evolution
- level-based evolution
- item-based evolution
- optional story-based evolution
- better evolution presentation screen
- before/after species display
- update codex after evolution

7) Quests
- lightweight but real quest system
- active quests
- completed quests
- objective text
- triggers such as:
  - talk to NPC
  - visit map
  - recruit a cat
  - win a battle
  - obtain item

DATA ARCHITECTURE
Refactor or maintain data in separate files:
- cats
- skills
- items
- maps
- map metadata
- encounter tables
- npcs
- quests
- codex entries
- type chart
- evolution chains
- recruit logic
- legendary events if present

ASSET FOLDER STRUCTURE
Create and use a clean local asset structure like this:

src/assets/
  tilesets/
    overworld/
    village/
    forest/
    ruins/
    interiors/
  sprites/
    player/
    cats/
      overworld/
      battle_front/
      battle_back/
    npcs/
  ui/
    frames/
    icons/
  portraits/

or if using public assets:

public/assets/
  tilesets/
  sprites/
  ui/

Document where each pack came from.

RECOMMENDED LOCAL PATH CONVENTIONS
Use a consistent asset path strategy such as:

const ASSET_PATHS = {
  overworldForestTileset: "/assets/tilesets/overworld/forest_main.png",
  villageTileset: "/assets/tilesets/village/village_main.png",
  urbanTileset: "/assets/tilesets/village/urban_main.png",
  interiorTileset: "/assets/tilesets/interiors/house_main.png",
  ruinsTileset: "/assets/tilesets/ruins/ruins_main.png",

  playerSprite: "/assets/sprites/player/player_sheet.png",

  catOverworldSprites: "/assets/sprites/cats/overworld/",
  catBattleFrontSprites: "/assets/sprites/cats/battle_front/",
  catBattleBackSprites: "/assets/sprites/cats/battle_back/",

  npcSprites: "/assets/sprites/npcs/",
  uiFrames: "/assets/ui/frames/",
  uiIcons: "/assets/ui/icons/"
};

README REQUIREMENTS
Replace the default Vite README.
Document clearly:
- game overview
- controls
- folder structure
- how maps work
- how encounters work
- how to add new cats
- how to add new quests
- how to add new maps
- which asset packs are used
- source links for each asset pack
- any attribution notes if needed

MANDATORY README SOURCE LINK SECTION
Include a section documenting these exact asset sources:

Cat sprites:
- https://opengameart.org/content/cats-rework
- https://opengameart.org/content/cat-sprites
- https://cupnooble.itch.io/sprout-lands-asset-pack

Map tilesets:
- https://kenney.nl/assets/tiny-rpg-forest
- https://kenney.nl/assets/rpg-urban-pack
- https://opengameart.org/content/lpc-tile-atlas

UI icons:
- https://game-icons.net/

Additional optional/reference packs:
- https://kenney.nl/assets/roguelike-rpg-pack
- https://kenney.nl/assets/tiny-town
- https://kenney.nl/assets/roguelike-characters
- https://kenney.nl/assets/roguelike-caves-dungeons
- https://finalbossblues.itch.io/openrtp-tiles

FILE AND CODE QUALITY RULES
- TypeScript strict mode compatible
- no broken imports
- no default placeholder comments left behind as final code
- avoid giant monolithic files if possible
- create reusable systems for tilemaps, NPC interaction, battle, codex, quests
- use metadata-based tile rendering rather than fragile magic numbers if possible
- preserve existing good systems where possible, but polish aggressively
- final result must be cohesive and playable

DELIVERABLES
Produce:
1. improved overworld rendering
2. polished map presentation using proper assets
3. working exploration loop
4. working NPC dialogue
5. working recruit/capture flow
6. improved battle presentation
7. codex screen
8. evolution flow
9. quest system MVP
10. asset folder cleanup and source documentation
11. README update

FINAL REQUIREMENT
The final project must feel like a real playable cat monster-collection RPG prototype.
It must not feel like a debug tilemap demo.
Prioritize overworld visual quality first, because exploration is the foundation for dialogue, capture, battle, codex, evolution, and quests.
추가로, 안티그래비티가 자꾸 텍스트 타일이나 임시 블록맵으로 만들면 아래 문장을 프롬프트 맨 아래에 덧붙이시면 됩니다.
Do NOT render tile labels such as terrain names inside the playable map.
Do NOT use text placeholders as world graphics.
Do NOT treat the overworld as a debug visualization.
The map must be drawn with proper pixel-art tilesets and animated sprites.
그리고 한 줄 더 붙이면 더 강해집니다.
If asset files are missing, create the code structure so the asset paths are ready and clearly documented, but do not fall back to ugly text-rendered tiles as the final presentation.
지금 단계에서는 이 프롬프트로 가는 게 맞습니다.
핵심은 에셋 링크를 넣는 것 자체보다, 그 에셋을 일관된 폴더 구조와 렌더링 파이프라인에 묶는 것입니다.

Feloria Visual and System Upgrade Plan Goal Description Upgrade the existing Feloria prototype into a polished, playable top-down pixel-art monster-collection RPG. This involves ripping out the placeholder rectangle-based map rendering and replacing it with a robust canvas-based tilemap engine supporting layers, camera follow, and animated sprites. We will also expand the game world to 6 distinct maps, introduce Quest and Codex systems, upgrade the battle system visuals and mechanics (type advantages, better capture logic), and properly document the asset pipeline in the README. User Review Required IMPORTANT I have opted to refactor the existing Canvas renderer rather than migrating to Phaser 3. Since our exploration logic, collision, and encounter triggers are already tightly coupled with Zustand and React hooks, keeping Canvas avoids the massive rewrite required to sync Phaser's internal game loop with React's state management. The new Canvas engine will support viewport camera follow, multi-layer rendering (ground, decoration, collision), and sprite animations. Proposed Changes 1. Asset Pipeline & Constants Define the folder structure for local assets and create a constants file for asset paths. [NEW] src/config/assets.ts Define ASSET_PATHS pointing to /assets/tilesets/..., /assets/sprites/..., etc. 2. Map & Rendering Overhaul Update map data structures and completely rewrite WorldCanvas to act as a proper 2D camera viewport. [MODIFY] src/types/game.ts Update MapData to handle generic 2D arrays of tile indices matching the new tileset structures. Add camera and quest typings. [MODIFY] src/components/world/WorldCanvas.tsx Implement a viewport-centric rendering loop. Load physical .png tilesets instead of drawing colors. Draw layers in order: ground -> detail/objects (with Y-sorting for depth) -> player -> foreground. [MODIFY] src/data/maps/*.ts Rebuild starwhisk.ts and greenpaw.ts . [NEW] src/data/maps/mosslight.ts, aurora.ts, shadowvale.ts, celestial.ts Create the 4 new requested maps with layered tile data. 3. Core Systems Expansion Add the Codex and Quest systems to the global state. [MODIFY] src/store/useGameStore.ts Add codex: string[] (seen/caught states). Add activeQuests, completedQuests. [NEW] src/data/quests.ts, src/data/typeChart.ts Define quest data and elemental type multipliers. 4. UI Integrations Build out the new screens and upgrade existing ones. [NEW] src/components/ui/CodexScreen.tsx Screen to view seen/caught cats, habitat, and descriptions. [NEW] src/components/ui/QuestLog.tsx Screen to view active and completed quests. [MODIFY] src/components/ui/GameMenu.tsx Integrate buttons to open the Codex and Quest Log. [MODIFY] src/components/ui/DialogueBox.tsx Support multi-page text and Yes/No choices for quest acceptance. 5. Battle & Capture Upgrades Improve the battle logic and presentation. [MODIFY] src/components/battle/BattleScreen.tsx Upgrade damage calculation to use typeChart. Display front/back sprites using the new asset paths instead of emojis. Implement low-HP capture bonus logic and visual feedback. 6. Documentation Completely replace the README. [MODIFY] README.md Document controls, directory structure, how to add content, and the mandatory asset source links provided by the user. Verification Plan Automated & Manual Testing Rendering: Jump into the game and verify the camera centers on the player and tiles render correctly from asset sheets (or show clear missing-image boundaries if images aren't present yet). Movement: Verify collision works against the new tile data. Battle: Engage in a wild battle, confirm sprite rendering paths are used, test a super-effective move, and capture a cat. Systems: Open the menu, navigate to Codex and Quests. 이렇게 plan을 적어줬는데 이대로 진행하라고 할까?

네, 이 플랜 방향은 괜찮습니다.
특히 Phaser로 갈아엎지 않고 Canvas를 정리해서 가는 판단은 현재 저장소 상태에서는 합리적입니다.
다만, 그대로 진행시키기 전에 꼭 수정해서 못 박아야 할 점이 있습니다.
지금 플랜은 방향은 좋지만, 그대로 두면 또 **“겉만 정리된 MVP”**로 끝날 가능성이 있습니다.
진행해도 되는 이유
좋은 점은 분명합니다.
기존 Zustand/React 구조를 유지해서 리스크를 줄임
WorldCanvas를 진짜 타일맵 렌더러로 바꾸려는 방향이 맞음
map layer, camera follow, sprite path 분리까지 잡음
Codex / Quest / typeChart 추가도 우선순위가 적절함
README 교체까지 포함한 점도 좋음
즉, “기초 엔진 정리 → 맵 품질 개선 → 시스템 확장” 흐름은 맞습니다.

그대로 진행하면 아쉬운 점
이 플랜에는 아직 몇 가지 빠진 게 있습니다.
1. “에셋이 없을 때” 대체 방식 제한이 약함
이전처럼 또 텍스트 타일이나 네모 블록으로 fallback 하면 실패입니다.
반드시 이런 조건을 추가해야 합니다.
If local asset images are missing, do not render text labels inside tiles.
Do not use ugly debug text tiles as a fallback final presentation.
Instead, render a simple but clean placeholder sprite/tile block system with consistent pixel colors and visible tile boundaries only for temporary debugging.
지금 제일 중요한 건 디버그 화면 재발 방지입니다.

2. 맵 데이터 구조가 너무 추상적임
“generic 2D arrays”만 적혀 있으면 다시 허접한 맵이 나올 수 있습니다.
맵 구조를 더 구체적으로 못 박는 게 좋습니다.
예를 들면:
Each map must contain:
- width
- height
- groundLayer
- detailLayer
- collisionLayer
- objectLayer
- encounterLayer
- warpPoints
- npcSpawns
- itemSpawns
- backgroundMusic
이렇게 해야 나중에 탐험, 대화, 퀘스트, 포획이 다 자연스럽게 연결됩니다.

3. 오브젝트 depth 처리 기준을 더 명확히 해야 함
Y-sorting for depth라고만 하면 어설프게 처리할 수 있습니다.
추가로 이렇게 적는 게 좋습니다.
Objects such as trees, signs, houses, and NPCs must support depth sorting so the player can visually pass in front of or behind them when appropriate.
Use tile/object world Y position as the basis for sorting.
이게 있어야 포켓몬 같은 필드 느낌이 납니다.

4. BattleScreen 개선 범위가 조금 약함
지금 battle 쪽은 “타입 상성 + 스프라이트 표시 + 포획 보너스” 정도인데,
사용자님 목표상 전투 템포와 가독성도 중요합니다.
추가 권장 문구:
