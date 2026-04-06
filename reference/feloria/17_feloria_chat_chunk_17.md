# Feloria 대화 내역 분할본 17

- 원본 파일: Feloria 대화 내역.docx
- 분할 번호: 17/28
- 문단 범위: 3210~3273
- 문자 수(대략): 23686

---

Feloria - Phase 10: NPC Roles, World Reactivity, and Chapter Structure

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
Turn the current prototype into a believable RPG world by giving NPCs meaningful roles and making the world react to player progress.

Important:
All NPC dialogue must be written in Korean.

--------------------------------
NPC ROLE SYSTEM
--------------------------------

Add structured NPC roles.

NPC roles include:

- healer
- shopkeeper
- quest_giver
- hint_npc
- trainer
- lore_npc
- gatekeeper
- collector

NPC behavior must connect to existing systems.

--------------------------------
MAP NPC ASSIGNMENTS
--------------------------------

Starwhisk Village NPCs

1. 촌장 미라 (Elder Mira)
Role:
- healer
- quest_giver

Dialogue before first quest:

"어서 오너라, 여행자야."
"요즘 숲의 기운이 조금 이상하단다."

Dialogue after starter selection:

"네 고양이를 잘 돌봐주거라."
"숲에서는 언제 어떤 일이 벌어질지 모른단다."

Healing interaction:

"피곤해 보이는구나."
"내가 네 파티를 회복시켜 줄게."

2. 상인 토비 (Shopkeeper Toby)
Role:
shopkeeper

Dialogue:

"어서 와! 필요한 물건이 있나?"
"포션은 전투에서 정말 유용하지."

After purchase:

"좋은 선택이야."
"다음에도 또 와!"

3. 마을 주민 리나
Role:
hint_npc

Dialogue before forest:

"숲에 들어가면 풀숲을 조심해."
"야생 고양이들이 갑자기 나타나거든."

After first capture:

"와! 벌써 고양이를 잡았구나!"
"점점 멋진 트레이너가 되어가고 있네."

4. 고양이 연구가 유진
Role:
lore_npc

Dialogue:

"이 세계에는 아주 다양한 고양이들이 살고 있어."
"어떤 고양이는 숲에서, 어떤 고양이는 물가에서 살아."

After codex progress:

"도감을 채우고 있구나!"
"훌륭한 탐험가가 될 것 같아."

--------------------------------
Greenpaw Forest NPCs

1. 숲 탐험가 다린
Role:
hint_npc

Dialogue:

"이 숲은 생각보다 깊어."
"풀숲에서는 언제든 야생 고양이가 나타날 수 있어."

After encounter:

"전투에서 살아남았구나!"
"다음엔 더 강한 고양이도 만날 거야."

2. 초보 트레이너 카일
Role:
trainer

Dialogue before battle:

"너도 트레이너야?"
"내 고양이랑 한번 붙어보자!"

After defeat:

"와… 내가 졌어."
"더 열심히 훈련해야겠어."

--------------------------------
Mosslight Path NPCs

1. 여행자 노아
Role:
hint_npc

Dialogue:

"이 길은 고대 숲으로 이어진단다."
"하지만 조심해. 강한 트레이너들이 기다리고 있어."

2. 트레이너 세라
Role:
trainer

Dialogue before battle:

"이 길을 지나가려면 나를 이겨야 해!"

After defeat:

"대단하네…"
"넌 정말 강한 트레이너야."

--------------------------------
Ancient Forest NPCs

1. 신비한 여행자 에반
Role:
lore_npc

Dialogue:

"이 숲은 아주 오래된 곳이야."
"고대 고양이들이 잠들어 있다는 이야기도 있지."

2. 숲 수호자 견습 루크
Role:
trainer

Dialogue:

"이 숲을 지나가려면 실력을 보여줘야 해!"

--------------------------------
Mosslight Shrine NPCs

1. 신전 수호자 로완 (Boss)

Role:
boss trainer

Dialogue before battle:

"여기까지 온 걸 보니 꽤 실력이 있군."
"하지만 이 신전을 지나가려면 나를 이겨야 한다."

After defeat:

"훌륭하다…"
"이 신전의 시험을 통과했다."

Reward dialogue:

"이 유물을 받아라."
"이제 너는 다음 길로 나아갈 자격이 있다."

2. 순례자 엘리

Role:
hint_npc

Dialogue:

"신전에 들어가기 전에 준비를 단단히 해."
"앞에는 아주 강한 상대가 기다리고 있어."

--------------------------------
NPC DATA STRUCTURE
--------------------------------

NPC definitions should include:

- id
- name
- role
- mapId
- sprite
- dialogueBefore
- dialogueAfter
- optional trainerId
- optional questTrigger

Do not hardcode NPC behavior directly inside WorldScene.

--------------------------------
WORLD REACTIVITY
--------------------------------

NPC dialogue must change based on player progress:

Examples:

Before first capture
→ NPC explains catching creatures

After first capture
→ NPC congratulates player

Before boss defeat
→ villagers warn about shrine

After boss defeat
→ villagers praise the player

--------------------------------
BUG PREVENTION RULES
--------------------------------

1. NPC roles must not conflict.
Talking to healer should not open shop or start battle.

2. Trainer defeat state must persist through save/load.

3. Quest dialogue must read quest state from the quest system.

4. NPC interaction must use the same interaction system across all maps.

--------------------------------
FINAL GOAL
--------------------------------

NPCs should make the world feel alive.

They should:
- guide the player
- provide hints
- trigger quests
- start battles
- reward progress
- explain the world

Feloria should feel like a living world rather than a test map.

이 프롬프트의 효과
이걸 넣으면 Feloria가 이렇게 바뀝니다.
Starwhisk Village
 ├ 촌장 미라 (회복 + 퀘스트)
 ├ 상인 토비 (상점)
 ├ 주민 리나 (힌트)
 └ 연구가 유진 (세계관)

Greenpaw Forest
 ├ 탐험가 다린 (힌트)
 └ 트레이너 카일

Mosslight Path
 ├ 여행자 노아
 └ 트레이너 세라

Ancient Forest
 ├ 여행자 에반
 └ 트레이너 루크

Mosslight Shrine
 ├ 순례자 엘리
 └ 보스 로완
즉 맵이 살아있는 RPG처럼 변합니다.

다음 단계 (진짜 중요)
NPC 역할이 들어가면 다음은 Phase 11입니다.
보스 보상 시스템
+ 새로운 몬스터 10종
+ 체육관/배지 시스템
원하시면 다음 답변에서
Feloria에 어울리는 고양이 몬스터 40종 + 진화 트리도 만들어 드리겠습니다.

Phase 10: NPC Roles, World Reactivity, and Chapter Structure We will implement a unified NPC system to make the world feel alive and reactive to player progress, using Korean dialogue. Proposed Changes Data Layer [NEW] src/game/data/npcs.js Create a centralized definitions file for all NPCs following the requested structure. Fields: id, name, role, mapId, sprite, getDialogue() (a function or structured array to dynamically return dialogue based on registry state), trainerId (for trainers), questTrigger (for quest givers/resolvers). Roles: healer, shopkeeper, quest_giver, hint_npc, trainer, lore_npc, gatekeeper, collector, boss_trainer. [MODIFY] src/game/data/trainers.js Update the trainer definitions to match the new NPC IDs/Names (e.g., Kyle, Sera, Luke, Boss Rowan). Logic Layer [MODIFY] src/game/scenes/WorldScene.js Refactor the handleInteraction method. Remove hardcoded dialogue switch statements. Lookup the NPC by spawn.id in npcs.js. Execute behavior based on npc.role: healer: Heal party, show dialogue. shopkeeper: Show dialogue -> Open ShopScene. trainer: Check if defeated -> Show dialogue -> Open BattleScene. hint_npc / lore_npc: Evaluate state -> Show corresponding dynamic dialogue. Maps Layer [MODIFY] Various maps Update the specific map JSON files ( starwhisk_village.json , greenpaw_forest.json , etc.) spawns arrays so that the id perfectly matches the keys in npcs.js. Place the new NPCs where appropriate. Verification Plan Check Starwhisk Village to ensure Elder Mira heals and Toby opens the shop. Advance "First Steps" quest and verify Lina's dialogue changes. Verify Trainer combat execution and persistence (e.g. Sera on Mosslight Path). Visually verify the Dialog UI renders the Korean text correctly without missing glyphs.

네, 이 플랜 방향은 좋습니다.
특히 이번에는 NPC 로직을 WorldScene 하드코딩에서 빼고 npcs.js로 모으는 방향이 맞습니다.
좋은 점은 이렇습니다.
NPC 역할을 데이터화하려는 점
getDialogue()로 상태 반응형 대사를 만들려는 점
trainer / healer / shopkeeper / lore_npc를 역할별로 분리한 점
맵의 spawn id와 NPC 정의를 일치시키려는 점
즉, 지금 Phase 10은 “맵에 세워진 오브젝트”였던 NPC를 실제 RPG 시스템의 일부로 바꾸는 단계라서, 이 방향이 맞습니다.
다만 그대로 진행시키기보다, 아래 조건을 같이 붙여서 보내는 게 좋습니다.
특히 NPC 상태 반응, 한국어 렌더링, 역할 충돌 방지, 세이브/로드 연동은 꼭 못 박아야 합니다.
아래 문구를 그대로 보내세요.
Approved — proceed with Phase 10.

The overall direction is correct.
This phase should make the world feel alive by turning NPCs into real gameplay actors instead of static map objects.

Important:
I will do all verification manually myself.
Do not claim anything is verified.
Implement the systems so they are stable and directly testable.

Proceed under the following constraints.

--------------------------------
PHASE 10 GOAL
--------------------------------

This phase should focus on:
- unified NPC data
- role-based NPC interaction
- world reactivity based on player progress
- Korean dialogue support
- cleaner WorldScene interaction logic

Do not add too many unrelated new systems in this phase.
Use existing systems through NPC roles.

--------------------------------
IMPLEMENTATION RULES
--------------------------------

1. Keep Phaser 3 + JavaScript
- Do not switch to TypeScript
- Do not reintroduce React gameplay components
- Keep the current Phaser scene architecture

2. NPC data must be centralized
Create and use:
- src/game/data/npcs.js

This file should become the source of truth for important NPC behavior.

Each NPC definition should include:
- id
- name
- role
- mapId
- sprite
- trainerId (if applicable)
- questTrigger (if applicable)
- getDialogue(gameState) or equivalent dialogue resolver

Do not hardcode important NPC dialogue inside WorldScene.

3. WorldScene interaction must become role-based
Refactor handleInteraction so that interaction is resolved by NPC role.

Expected role behaviors:
- healer -> heal party + show dialogue
- shopkeeper -> show dialogue + open ShopScene
- trainer -> check defeated state + pre-battle dialogue + BattleScene
- boss_trainer -> special trainer flow + boss dialogue
- quest_giver -> start/complete/update quest
- hint_npc -> contextual advice
- lore_npc -> worldbuilding dialogue
- gatekeeper -> block/unlock progress based on conditions
- collector -> react to codex/capture milestones

4. Registry remains the single source of truth
NPC reactions must read from the same persistent state used everywhere else, including:
- active quests
- completed quests
- defeatedTrainers
- seenCreatureIds
- caughtCreatureIds
- playerCollection
- playerParty
- currentMapId

Do not use scene-local permanent flags for NPC progression.

--------------------------------
KOREAN DIALOGUE REQUIREMENTS
--------------------------------

5. All NPC dialogue must be in Korean
- speaker names in Korean
- dialogue text in Korean
- role responses in Korean

6. Korean font/glyph support must render correctly
Do not assume default browser/game font will display Korean well.
Use a font or rendering setup that correctly supports Hangul.

Requirements:
- no missing glyphs
- no broken spacing
- no garbled text
- dialogue boxes must remain readable

7. Dialogue formatting
- keep lines reasonably short
- avoid overly long full-screen paragraphs
- support multi-page dialogue if needed
- speaker name should display clearly

--------------------------------
ROLE SAFETY RULES
--------------------------------

8. Prevent NPC role conflicts
An NPC with multiple responsibilities must still have a predictable interaction flow.

Examples:
- Elder Mira may be healer + quest_giver
- but the interaction result must be clear and deterministic

Recommended approach:
- primary role executes first
- optional secondary role is offered via dialogue choice if needed

Do not make one interaction accidentally:
- heal and open shop
- start battle and complete a quest at the same time
unless explicitly designed

9. Trainer persistence
Trainer NPCs must:
- start battle only if not yet defeated
- switch to post-battle dialogue if defeated
- remain defeated after save/load

10. Quest reactivity
Quest-related NPC dialogue must change based on real quest progress.

Examples:
- before First Steps starts
- after starter selection
- after first capture
- after shrine cleared

Dialogue variation must come from quest state, not hardcoded one-off booleans.

--------------------------------
MAP / SPAWN RULES
--------------------------------

11. Map spawn IDs must match NPC data exactly
All map spawn ids in:
- starwhisk_village
- greenpaw_forest
- mosslight_path
- ancient_forest
- mosslight_shrine

must match npc ids in npcs.js exactly.

Do not allow mismatches between map data and NPC data.

12. NPC placement should reinforce gameplay purpose
Make sure important NPCs are positioned intentionally:
- healer in village center or obvious safe spot
- shopkeeper inside or near shop
- hint NPC near route entrances
- lore NPC near landmarks
- boss trainer at shrine goal point

--------------------------------
BUG PREVENTION RULES
--------------------------------

1. Prevent broken NPC lookup
If a spawn id is missing from npcs.js, fail safely and log clearly instead of crashing interaction flow.

2. Prevent defeated trainer rebattle bug
Trainer state must be checked before battle starts, and must persist after save/load.

3. Prevent dialogue desync
NPC dialogue must always be derived from the same current registry state.

4. Prevent Korean text rendering bugs
Use font support that can display Hangul correctly in all dialogue/menu scenes where NPC text appears.

5. Prevent role handler duplication
Do not scatter NPC logic across multiple files in inconsistent ways.
WorldScene should dispatch by role, but NPC content should come from data files.

--------------------------------
MANUAL-TEST READY TARGETS
--------------------------------

Implement these so I can test them manually:

1. Elder Mira heals correctly and reacts to quest progress
2. Toby opens the shop correctly
3. Lina changes dialogue after quest progress
4. Trainers like Kyle / Sera / Luke start battle correctly and remain defeated afterward
5. At least one lore NPC gives worldbuilding dialogue
6. Korean dialogue renders correctly with no missing glyphs
7. Save/load preserves NPC state reactions correctly

--------------------------------
FINAL NOTE
--------------------------------

The goal of Phase 10 is to make Feloria feel like a living RPG world.
NPCs should not just occupy the map.
They should guide, heal, sell, battle, explain, react, and reinforce progression.
추가로 한 가지 더 말씀드리면, 지금 단계에서는 getDialogue()를 너무 복잡하게 함수형으로만 만들기보다,
처음엔 이런 식으로 해도 좋습니다.
dialogue: {
  default: [...],
  afterStarter: [...],
  afterFirstCapture: [...],
  afterBoss: [...]
}
그리고 getDialogue(gameState)는 이 상태 키를 고르는 정도만 하게 만들면 관리가 쉬워집니다.
원하시면 다음엔 제가 **NPC 데이터 구조 예시 (npcs.js 샘플 코드)**까지 바로 만들어드리겠습니다.

🐾 펠로리아(Feloria) 스토리 기획: 고대 고양이의 요람 [시즌 1 메인 스토리: 엇갈린 수호자들] 1. 평화로운 시작과 불길한 징조 (스타위스크 마을 ~ 그린포 숲) 상황: 스타위스크 마을의 촌장 현석은 언제나처럼 다정하게 플레이어를 맞이하며, 최근 "숲의 기운이 이상하다"며 첫 고양이를 건네주고 조사를 부탁합니다. 전개: 숲 탐험가 다린과 초보 트레이너 카일을 만나며 숲이 평소보다 공격적인 야생 고양이들로 들끓고 있음을 알게 됩니다. 고양이 연구가 유진은 도감을 채우는 플레이어에게 '고대 고양이'에 대한 전설을 넌지시 들려줍니다. 2. 고대 숲의 비밀과 신전으로의 길 (모스라이트 오솔길 ~ 고대 숲) 상황: 여행자 노아와 세라, 루크 등 강한 트레이너들이 길을 막아서며 플레이어의 실력을 시험합니다. 핵심 인물 등장: 고대 숲에서 만난 신비한 여행자 에반이 결정적인 떡밥을 던집니다. "이 숲은 아주 오래된 곳이야. 고대 고양이들이 잠들어 있다는 이야기도 있지... 그리고 누군가 그 힘을 탐내고 있어." 플레이어는 이 '누군가'가 숲의 기운을 망치고 있는 정체불명의 악당이라고 굳게 믿게 됩니다. 3. 모스라이트 신전의 결전과 충격의 반전 상황: 순례자 엘리의 경고를 듣고 진입한 신전 최심부. 그곳에는 신전 수호자 로완이 굳건히 버티고 있습니다. 플레이어는 로완이 흑막이거나, 흑막에게 조종당하고 있다고 생각하고 전력을 다해 그를 쓰러뜨립니다. 반전: 쓰러진 로완이 숨을 헐떡이며 말합니다. "어리석은 녀석... 네가 무슨 짓을 했는지 아느냐? 네가 나를 꺾음으로써, 고대 고양이의 봉인을 지키던 마지막 결계가 풀려버렸다..." 최종 보스 등장: 그때, 촌장 현석이 특유의 인자한 웃음을 지으며 박수를 치며 등장합니다. "훌륭하구나. 로완, 이 고지식한 친구가 길을 비켜주지 않아 곤란하던 참이었단다. 역시 내가 선택한 아이답게 훌륭히 자라주었어." 현석은 처음부터 고대 고양이의 압도적인 마력을 손에 넣어 펠로리아 대륙을 자신의 통치하에 두려는 야망을 품고 있었습니다. 하지만 수호자 로완의 결계를 깰 수 없어, 순수하고 잠재력 있는 플레이어를 속여 육성한 뒤 로완을 처리하게 만든 것이었습니다. 시즌 1 결말: 현석과의 치열한 최종전. 패배한 현석은 물러나지만, 고대 고양이의 봉인은 결국 산산조각 나며 전설 속 고양이들이 펠로리아 대륙 전역으로 흩어지는 연출과 함께 시즌 1이 막을 내립니다. [시즌 2 확장 힌트: 전설의 귀환] 깨어난 고대 고양이들이 대륙 곳곳의 기후와 생태계를 뒤바꿔 놓습니다. 플레이어는 이를 수습하기 위해 대륙 전체로 모험을 떠나야 하며, 잠적했던 현석이 고대 고양이를 포획하는 거대 조직을 결성해 새로운 악당 세력으로 등장합니다. /** * npcs.js * Centralized data structure for all NPCs in Feloria. * Defines roles, reactivity, and Korean dialogue based on the game state. */ export const NPCS = { // --- STARWHISK VILLAGE --- mira: { id: "choi", name: "촌장 현석", role: "healer_quest", sprite: "npc_mira", getDialogue: (registry) => { const activeQuests = registry.get("activeQuests") || {}; const firstSteps = activeQuests["first_steps"]; // Quest progression checks if (!firstSteps || !firstSteps.objectives[1].completed) { // Starter not chosen yet return [ "어서 오너라, 여행자야.", "요즘 숲의 기운이 조금 이상하단다. 무언가 거대한 것이 꿈틀대는 것 같아..." ]; } if (firstSteps && !firstSteps.completed) { return [ "네 고양이를 잘 돌봐주거라.", "숲에서는 언제 어떤 일이 벌어질지 모른단다. 그게 누구의 계획이든 말이지.", ]; } // Default / Healer Dialogue return ["피곤해 보이는구나. 이리 오렴.", "내가 네 파티를 아주 '건강하게' 유지해 주마."]; }, }, shopkeeper: { id: "shopkeeper", name: "상인 토비", role: "shopkeeper", sprite: "npc", getDialogue: (registry) => { return ["어서 와! 필요한 물건이 있나?", "포션은 전투에서 정말 유용하지. 특히 깊은 숲에선 필수야!"]; }, }, villager1: { id: "villager1", name: "마을 주민 리나", role: "hint_npc", sprite: "npc", getDialogue: (registry) => { const collection = registry.get("playerCollection") || []; if (collection.length > 1) { // Has captured at least one cat (since starter is 1) return [ "와! 벌써 고양이를 잡았구나!", "촌장님이 널 아주 특별하게 생각하시는 것 같더라.", ]; } return [ "숲에 들어가면 풀숲을 조심해.", "야생 고양이들이 갑자기 나타나거든. 요즘 들어 유독 더 사나워졌어.", ]; }, }, eugene: { id: "eugene", name: "고양이 연구가 유진", role: "lore_npc", sprite: "npc", getDialogue: (registry) => { const caughtIds = registry.get("caughtCreatureIds") || []; if (caughtIds.length >= 3) { return ["도감을 채우고 있구나!", "언젠가 전설 속 '고대 고양이'도 만날 수 있을지 몰라."]; } return [ "이 세계에는 아주 다양한 고양이들이 살고 있어.", "어떤 고양이는 숲에서, 어떤 고양이는 물가에서 살아.", ]; }, }, // --- GREENPAW FOREST --- darin: { id: "darin", name: "숲 탐험가 다린", role: "hint_npc", sprite: "npc", getDialogue: (registry) => { // Very simple reactive check: if their highest level cat is > 3 const party = registry.get("playerParty") || []; const hasStrongCat = party.some((c) => c.level > 3); if (hasStrongCat) { return ["전투에서 살아남았구나!", "다음엔 더 강한 고양이도 만날 거야. 안쪽은 기운이 심상치 않거든."]; } return [ "이 숲은 생각보다 깊어.", "풀숲에서는 언제든 야생 고양이들이 널 노릴 수 있어.", ]; }, }, trainer_kyle: { id: "trainer_kyle", name: "초보 트레이너 카일", role: "trainer", trainerId: "kyle", sprite: "npc", getDialogue: (registry) => { const defeated = registry.get("defeatedTrainers") || []; if (defeated.includes("kyle")) { return ["와… 내가 졌어.", "더 열심히 훈련해야겠어."]; } return ["너도 트레이너야?", "내 고양이랑 한번 붙어보자!"]; }, }, // --- MOSSLIGHT PATH --- noah: { id: "noah", name: "여행자 노아", role: "hint_npc", sprite: "npc", getDialogue: (registry) => { return [ "이 길은 고대 숲으로 이어진단다.", "하지만 조심해. 숲을 지키려는 자들이 예민해져 있어.", ]; }, }, trainer_sera: { id: "trainer_sera", name: "트레이너 세라", role: "trainer", trainerId: "sera", sprite: "npc", getDialogue: (registry) => { const defeated = registry.get("defeatedTrainers") || []; if (defeated.includes("sera")) { return ["대단하네…", "넌 정말 강한 트레이너야. 신전에 갈 자격이 있어."]; } return ["이 길을 지나가려면 나를 이겨야 해!"]; }, }, // --- ANCIENT FOREST --- evan: { id: "evan", name: "신비한 여행자 에반", role: "lore_npc", sprite: "npc", getDialogue: (registry) => { return [ "이 숲은 아주 오래된 곳이야.", "고대 고양이들이 잠들어 있다는 이야기가 있지.", "…그리고 누군가 신전의 봉인을 노리고 있는 것 같아." ]; }, }, trainer_luke: { id: "trainer_luke", name: "숲 수호자 견습 루크", role: "trainer", trainerId: "luke", sprite: "npc", getDialogue: (registry) => { const defeated = registry.get("defeatedTrainers") || []; if (defeated.includes("luke")) { return ["너라면 이 숲의 시련을 이겨낼 수 있을 거다. 로완 님을 도와줘."]; } return ["더 이상은 못 간다! 신전을 보호해야 해!"]; }, }, // --- MOSSLIGHT SHRINE --- ellie: { id: "ellie", name: "순례자 엘리", role: "hint_npc", sprite: "npc", getDialogue: (registry) => { return [ "신전에 들어가기 전에 준비를 단단히 해.", "수호자님은 강한 힘으로 무언가를 억누르고 계셔.", ]; }, }, trainer_guardian_rowan: { id: "trainer_guardian_rowan", name: "신전 수호자 로완", role: "boss_trainer", trainerId: "guardian_rowan", sprite: "npc_mira", // Uses unique sprite getDialogue: (registry) => { const defeated = registry.get("defeatedTrainers") || []; if (defeated.includes("guardian_rowan")) { return [ "크윽... 훌륭하다...", "하지만 네가 무슨 짓을 했는지 아느냐?", "결계가... 깨져버렸어. 설마 이 모든 게 그 자의 계획이었단 말인가!" ]; } return [ "여기까지 온 걸 보니 꽤 실력이 있군.", "하지만 이 앞은 절대 지나갈 수 없다. 세계의 균형을 위해 나를 꺾어봐라!", ]; }, }, }; 이런식으로 하려고 해. 게임 시작 할 때 먼저 이 게임이 어떤 설정인지 보여주고, 시작하자마자 촌장과 강제로 대화가 시작되어 퀘스트를 받으면서 이런식으로 진행이 되도록 위 스토리, 대사를 묶어서 프롬프트로 만들어줘

좋습니다. 지금 코드 구조를 보면 이미 NPC 데이터 기반 구조 + getDialogue() 방식 + registry 상태 기반 대사 분기가 잘 잡혀 있습니다.
그래서 다음 단계는 스토리 인트로 → 강제 이벤트 → 첫 퀘스트 시작을 시스템으로 붙이는 것입니다.
즉 게임 흐름을 이렇게 만들면 됩니다.
게임 시작
↓
인트로 컷신
↓
스타위스크 마을
↓
촌장 현석 강제 대화
↓
첫 고양이 선택
↓
First Steps 퀘스트 시작
↓
숲 조사
↓
신전 보스
↓
스토리 반전
아래 프롬프트는 지금 만든 NPC 시스템 + 스토리 + 퀘스트 + 강제 이벤트를 한 번에 연결하도록 설계된 안티그래비티용 프롬프트입니다.
그대로 보내시면 됩니다.

Feloria Story Integration Prompt
Feloria - Story Intro and First Chapter Narrative Integration

Integrate the Season 1 main storyline into the existing NPC and quest system.

The current game already has:
- NPC role system
- quest system
- dialogue system
- exploration maps
- trainer battles
- save/load
- codex
- party system

This task adds structured story flow to the game.

--------------------------------
STORY FLOW OVERVIEW
--------------------------------

The first chapter should play as follows:

Game Start
→ Story Intro Scene
→ Player enters Starwhisk Village
→ Forced dialogue with Village Chief Hyunseok
→ Starter selection
→ Quest "First Steps" begins
→ Investigation of Greenpaw Forest
→ Travel through Mosslight Path
→ Ancient Forest exploration
→ Mosslight Shrine boss battle
→ Major story twist

--------------------------------
INTRO CUTSCENE
--------------------------------

Before the player gains control of the character, show a short narrative intro.

Display the following Korean narration:

"펠로리아 대륙."

"아주 오래전, 이 대륙에는 세계의 균형을 지키는 존재들이 있었다."

"사람들은 그들을 '고대 고양이'라 불렀다."

"하지만 지금… 숲의 기운이 뒤틀리기 시작했다."

"그리고 그 힘을 노리는 누군가가 움직이고 있다."

Fade into Starwhisk Village.

--------------------------------
FORCED FIRST NPC INTERACTION
--------------------------------

When the player spawns in Starwhisk Village for the first time:

Automatically trigger dialogue with NPC:

촌장 현석 (Chief Hyunseok)

This interaction must be forced once.

Dialogue:

현석:
"어서 오너라, 여행자야."

"요즘 숲의 기운이 심상치 않단다."

"야생 고양이들이 이유 없이 사나워지고 있어."

"그래서 부탁이 있다."

"이 아이를 데리고 숲의 상태를 조사해 줄 수 있겠느냐?"

This leads into StarterSelectScene.

--------------------------------
STARTER SELECTION
--------------------------------

Starter cats remain:

Leafkit
Emberpaw
Misttail

After selection:

현석:
"좋은 선택이구나."

"네가 이 아이와 함께라면 숲의 비밀을 밝혀낼 수 있을 거다."

"조심하거라."

--------------------------------
FIRST QUEST START
--------------------------------

Quest Name:
"첫 발걸음"

Objectives:

1. 촌장 현석과 대화하기
2. 첫 고양이 선택하기
3. 그린포 숲으로 이동하기
4. 야생 고양이와 조우하기
5. 고양이 한 마리 포획하기
6. 촌장에게 돌아가기

Completing the quest unlocks Mosslight Path.

--------------------------------
NPC STORY DIALOGUE
--------------------------------

These NPCs should provide narrative hints.

고양이 연구가 유진:

"이 세계에는 아주 다양한 고양이들이 살고 있어."

"전설에 따르면, 아주 오래된 '고대 고양이'도 존재한다고 하더군."

숲 탐험가 다린:

"요즘 숲이 이상해."

"야생 고양이들이 뭔가에 자극받은 것처럼 공격적이야."

여행자 노아:

"이 길은 고대 숲으로 이어진다."

"하지만 조심해. 신전 쪽에서 이상한 기운이 느껴져."

신비한 여행자 에반:

"이 숲은 아주 오래된 곳이야."

"고대 고양이들이 잠들어 있다는 이야기도 있지."

"그리고 누군가… 그 힘을 노리고 있는 것 같아."

--------------------------------
BOSS BATTLE SETUP
--------------------------------

Location:
Mosslight Shrine

Boss:
신전 수호자 로완

Dialogue before battle:

"여기까지 온 걸 보니 꽤 실력이 있군."

"하지만 이 앞은 절대 지나갈 수 없다."

"세계의 균형을 위해 나를 꺾어봐라!"

--------------------------------
STORY TWIST
--------------------------------

After defeating Rowan:

Rowan:

"크윽…"

"어리석은 녀석…"

"네가 무슨 짓을 했는지 아느냐?"

"네가 나를 쓰러뜨림으로써…"

"봉인을 지키던 마지막 결계가 깨져버렸다."

Then Chief Hyunseok appears.

Hyunseok:

"훌륭하구나."

"로완, 이 고지식한 친구가 길을 비켜주지 않아 곤란하던 참이었단다."

"역시 내가 선택한 아이답게 훌륭히 자라주었어."

Reveal:

Hyunseok is the true antagonist.

--------------------------------
CHAPTER END
--------------------------------

Hyunseok retreats.

The ancient seal breaks.

Narration:

"고대 고양이의 봉인이 깨졌다."

"전설 속 존재들이 펠로리아 대륙 곳곳으로 흩어지기 시작했다."

End of Season 1 Chapter.

--------------------------------
IMPLEMENTATION REQUIREMENTS
--------------------------------

Use existing systems:

- dialogue system
- quest system
- NPC roles
- trainer battle
- map progression

Do not hardcode story logic inside WorldScene.

Story triggers must be connected to:

- quest state
- defeatedTrainers
- map entry
- NPC interaction

이 프롬프트의 효과
이걸 넣으면 Feloria는 다음처럼 진행됩니다.
인트로
↓
촌장 현석
↓
스타터 선택
↓
숲 조사
↓
NPC 힌트
↓
신전 보스
↓
스토리 반전
즉 이제 게임이 단순 시스템 테스트가 아니라 실제 RPG 스토리 게임처럼 작동합니다.

다음 단계 (중요)
이 스토리 구조가 들어가면 다음 단계는 보통 이것입니다.
Phase 11
컷신 시스템
카메라 연출
보스 등장 연출
음악
원하시면 제가 Feloria 스토리를 훨씬 강력하게 만드는 "보스 연출 + 컷신 프롬프트"도 만들어 드리겠습니다.

좋아 보스 연출 + 컷신 프롬프트 + 봉인이 풀리면서 고대 전설의 고양이(속성별)가 전 세계로 흩어지는 연출 + 고양이가 진화하는 연출 + 고양이 레벨업 연출 만들어줘

좋습니다.
지금 Feloria는 이미 엔진 / 전투 / 파티 / 진화 / 상점 / 저장 / NPC / 퀘스트까지 다 만들어진 상태라서 다음 단계는 “연출” 단계입니다.
즉 시스템은 그대로 두고 게임이 진짜 RPG처럼 느껴지게 만드는 단계입니다.
아래 프롬프트는 한 번에 넣으면 다음 연출 시스템이 모두 추가되도록 설계했습니다.
추가되는 것:
1️⃣ 컷신 시스템
2️⃣ 보스 등장 연출
3️⃣ 봉인 붕괴 + 전설 고양이 분산
4️⃣ 레벨업 연출
5️⃣ 진화 연출
그대로 보내시면 됩니다.

Feloria Cinematic & Event System Prompt
