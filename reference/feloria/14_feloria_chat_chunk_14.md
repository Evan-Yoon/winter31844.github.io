# Feloria 대화 내역 분할본 14

- 원본 파일: Feloria 대화 내역.docx
- 분할 번호: 14/28
- 문단 범위: 2473~2730
- 문자 수(대략): 27736

---

Feloria - Phase 7: Item Usage, Wild Gold Drops, and Inventory Gameplay

Phase 6 is in a good enough state to move forward.

The current project now has:
- world expansion into Mosslight Path
- trainer battles
- healing interaction
- shop purchases
- battle reward summaries
- stable evolution / level-up feedback
- defeated trainer persistence

Now proceed to Phase 7.

Goal:
Make the economy and inventory actually useful during gameplay.

This phase should focus on:
- wild enemy gold drops
- usable healing items
- usable capture items
- visible inventory counts
- inventory integration into battle and party management

Important:
I will do all verification manually myself.
Do not claim systems are verified.
Implement them in a way that is testable and stable.

--------------------------------
PHASE 7 FEATURES
--------------------------------

1. Wild battle gold drops
Add a small gold reward for defeating normal wild creatures.

Rules:
- wild victories should grant a small amount of gold
- trainer battles should still grant higher gold rewards
- captured wild creatures may grant no gold or lower gold depending on design
- gold reward must appear in the post-battle summary

Suggested range:
- wild battle victory: +3 to +10 Gold
- trainer battle victory: keep current larger reward

2. InventoryScene or usable inventory menu
Create a usable inventory interface.

Requirements:
- show current item quantities
- show item descriptions
- support selecting usable items
- keep UI simple and readable

3. Healing item usage
Potion should be usable on party creatures.

Requirements:
- Potion restores HP
- cannot heal above max HP
- using Potion decreases quantity by 1
- updated HP must persist to registry
- if no Potions remain, usage must be blocked

Preferred place to use:
- PartyScene
or
- InventoryScene with target selection

4. Capture item usage in battle
Capture Crystal must be integrated into BattleScene.

Requirements:
- BattleScene should use the player's actual inventory quantity
- using Capture Crystal consumes 1 item
- cannot use it if quantity is 0
- success/failure logic should remain tied to capture chance rules
- trainer battles must still forbid capture item use

5. Inventory persistence
Registry must remain the single source of truth for:
- playerInventory
- playerGold
- playerParty
- playerCollection

No scene-local fake inventory state.

6. Inventory normalization
Keep inventory in normalized structure such as:
playerInventory = {
  potion: quantity,
  capture_crystal: quantity
}

Do not store duplicate item objects in arrays.

7. UI feedback
Whenever an item is used, show readable feedback:
- "Potion used on Cinderclaw! +20 HP"
- "Capture Crystal used!"
- "No Potions left."
- "Cannot capture a trainer's creature."

8. Party integration
PartyScene should allow:
- viewing party members
- selecting a creature
- using healing items on that creature

9. Post-battle summary improvement
Battle result summary should now include:
- EXP gained
- Gold gained
- Total EXP
- total Gold if convenient
- level up message if triggered
- evolution message if triggered

10. Scope control
Do not add new maps or major story content in this phase.
Focus only on making the current economy and items actually usable.

--------------------------------
BUG PREVENTION RULES
--------------------------------

1. Prevent negative item counts
- item quantity can never go below 0

2. Prevent using items on invalid targets
- Potion cannot be used on a non-existent creature
- Potion cannot be used on creatures already at full HP unless explicitly allowed with no effect

3. Prevent capture item use in trainer battles
- disable or hide the option
- also guard in logic, not just UI

4. Prevent fake gold updates
- gold changes must be written back to registry immediately
- do not rely on temporary scene-only values

5. Prevent inventory desync
- shop purchases, battle item use, and party healing item use must all read/write the same playerInventory source

--------------------------------
MANUAL-TEST READY TARGETS
--------------------------------

Implement these so I can test them manually:

- defeating a wild creature gives a small amount of gold
- defeating a trainer still gives larger gold
- buying items increases inventory count
- Potion can be used from a menu on a party creature
- Potion decreases by 1 when used
- HP is restored correctly and persists
- Capture Crystal decreases by 1 when used in battle
- Capture Crystal cannot be used if quantity is 0
- Capture Crystal cannot be used in trainer battles
- post-battle summary shows both EXP and Gold where appropriate

Final note:
The goal of Phase 7 is to make items, gold, and inventory part of the real gameplay loop instead of just supporting systems.
추가로 지금 시점에서 꼭 하셔야 하는 정리 하나는 있습니다.
dist와 node_modules는 저장소에서 빼는 게 맞습니다. 공개 저장소 루트에 둘 다 보입니다.
원하시면 다음에는 제가 .gitignore에 바로 넣을 내용까지 정리해드리겠습니다.

Feloria - Phase 7: Economy and Items Action Phase 7 shifts the focus from simply hoarding resources to actually consuming them to prolong the exploration loop. Proposed Changes 💰 Wild Gold Drops battleSystem.js : Add a new function calculateGold(enemyCat, isTrainer = false). Wild encounters will randomly roll between +3 and +10 Gold. Trainers will statically return their defined bounties. BattleScene.js : Update the victory() routine to dynamically fetch the required Gold payload using the new function, ensuring wild grinding still provides an economic drip. 🎒 Battle Item Consumption BattleScene.js : Update playerCapture() . Fetch playerInventory.capture_crystal from the registry. If quantity is < 1, block the action with a UI message: "Not enough Capture Crystals!" If > 0, deduct 1 and strictly write it back to the Registry before executing the capture roll. 🏥 Inventory Scene and Healing Items inventorySystem.js: We will formally ensure useItem(registry, itemId, targetCreature) exists to encapsulate the logic: Check if inventory has > 0 of the item. Check if the item is a Healing type (like potion). Attempt to use it (Potions fail if currentHp === maxHp). Decrease count and write back. InventoryScene.js [NEW]: Create an overlay scene similar to ShopScene . Reads the ITEMS database alongside playerInventory keys to display owned items and quantities. Selecting a potion will transition to a mini-Party select view (or jump directly to PartyScene in an "item target mode") to apply the heal. MenuScene.js : Add an "Inventory" button to the pause menu, taking the player to the new InventoryScene . Verification Plan Wild Battles: Enter the forest grass. Defeat a cat. Prove the battle summary awards +3 to +10 Gold. Item Count Block: From 0 crystals, try to capture a cat. It must fail. Walk to the shop, buy 2 crystals. Return to a battle, capture a cat. Check inventory: 1 crystal must remain. Healing Usage: Take battle damage. Open the Menu -> Inventory. Click a Potion, select the damaged cat. Verify HP increases, Potion count decreases by 1, and no negative numbers exist if repeatedly spammed.

네, 이 플랜도 방향은 좋습니다.
특히 이번 단계에서
야생 골드 드랍
캡처 아이템 실제 소비
인벤토리에서 포션 사용
메뉴에서 Inventory 진입
이 4개를 묶는 건 맞습니다.
다만 그대로 진행시키기보다는, 아이템/골드/인벤토리 동기화 버그를 막는 조건을 같이 못 박는 게 좋습니다.
아래 문구를 그대로 보내세요.
Approved — proceed with Phase 7.

The direction is correct.
This phase should make gold and items part of the actual gameplay loop.

Important:
I will do all verification manually myself.
Do not claim anything is verified.
Your job is to implement the systems so they are stable and directly testable.

Proceed under the following constraints.

--------------------------------
PHASE 7 GOAL
--------------------------------

The goal of this phase is to make:
- wild gold drops
- usable inventory
- healing item usage
- capture item consumption
feel like real gameplay systems instead of passive data.

Do not add major new content or maps in this phase.
Keep the scope focused on economy and item usage.

--------------------------------
IMPLEMENTATION RULES
--------------------------------

1. Keep Phaser 3 + JavaScript
- Do not switch to TypeScript
- Do not reintroduce React gameplay components
- Keep the current Phaser scene structure

2. Registry must remain the single source of truth
All item, gold, and party state must be read from and written back to Phaser registry:

- playerInventory
- playerGold
- playerParty
- playerCollection

No scene-local fake inventory state.

3. Wild gold drops
Add small gold rewards for wild battle victories.

Rules:
- wild victory gives random +3 to +10 Gold
- trainer battle reward logic remains separate
- gold amount must appear in the battle summary
- captured wild battles may give no gold or reduced gold according to final design, but the rule must be explicit and consistent

4. Capture Crystal usage in battle
Capture Crystal must consume actual inventory quantity.

Rules:
- if quantity <= 0, block capture with a visible message
- if quantity > 0, deduct exactly 1
- write updated inventory back to registry immediately
- then run capture logic
- trainer battles must still forbid capture entirely

5. InventoryScene
Add an InventoryScene accessible from MenuScene.

Requirements:
- show owned item quantities
- show item names and descriptions
- support selecting usable items
- keep UI simple and readable

6. Healing item usage
Potion must be usable on party creatures.

Rules:
- Potion restores HP
- cannot exceed max HP
- using a Potion decreases quantity by 1
- if creature is already at full HP, item should not be consumed
- updated HP must persist to registry immediately

7. Party targeting for items
If using a Potion requires choosing a target:
- use a simple target selection flow
- target must be a real creature from playerParty
- after use, return cleanly to InventoryScene or MenuScene
- do not leave scenes stacked in a broken state

8. Menu integration
MenuScene should now include:
- Party
- Inventory
- Codex
- Quests
- Close

Keep this simple and stable.

9. Summary feedback
Battle result summary should clearly show:
- battle result
- EXP gained
- Gold gained
- total EXP if available
- level up if triggered
- evolution if triggered

--------------------------------
BUG PREVENTION RULES
--------------------------------

BUG 1 — Negative inventory counts
Prevent item quantity from ever going below 0.

BUG 2 — Consuming item on failed usability check
If Potion is used on a full HP creature, do NOT consume the item.

BUG 3 — Capture Crystal deducted but capture not attempted correctly
Only deduct 1 crystal once per use attempt.
Do not double-deduct on repeated clicks or scene updates.

BUG 4 — Gold updated in summary but not saved
Gold changes must be written back to registry before leaving BattleScene.

BUG 5 — Wild and trainer gold reward logic mixing together
Keep trainer reward logic separate from wild reward logic.
Do not accidentally apply both.

BUG 6 — Inventory desync between shop and battle
Shop purchases, battle item use, and healing item use must all read and write the same normalized playerInventory structure.

BUG 7 — Invalid item target handling
Do not allow Potion use on missing/null creatures.
Do not allow target selection to create undefined behavior.

BUG 8 — Repeated input causing duplicate consumption
Guard against button spam or repeated click events so one action only consumes one item.

--------------------------------
DATA STRUCTURE RULES
--------------------------------

Inventory should stay normalized, for example:

playerInventory = {
  potion: quantity,
  capture_crystal: quantity
}

Do not store duplicate item objects in arrays.

--------------------------------
MANUAL-TEST READY TARGETS
--------------------------------

Implement these so I can test them manually:

1. Defeating a wild creature gives +3 to +10 Gold
2. Defeating a trainer still gives trainer-style larger Gold reward
3. Battle summary shows Gold reward clearly
4. Capture fails if no Capture Crystals remain
5. Buying crystals from the shop increases quantity correctly
6. Using a crystal in battle decreases quantity by exactly 1
7. Potion can be used from Inventory
8. Potion heals the selected creature correctly
9. Potion is not consumed if used on a full HP creature
10. Inventory values persist after closing and reopening scenes

--------------------------------
FINAL NOTE
--------------------------------

The goal of Phase 7 is not polish.
The goal is to make items, inventory, and gold reliable gameplay systems that are safe from duplication, desync, and invalid consumption bugs.

Proceed under these constraints.
추가로 짧게 말하면, 이번 단계에서 꼭 막아야 할 핵심 버그는 이 4개입니다.
포션이 0개 밑으로 내려가는 버그
풀피인데 포션이 소비되는 버그
캡처 크리스탈이 한 번 클릭에 두 번 차감되는 버그
상점/전투/인벤토리에서 각자 다른 재고를 보는 버그
이 4개만 막아도 Phase 7은 꽤 안정적으로 갑니다.

https://github.com/Evan-Yoon/Feloria 여기까지 했어. 이제 다음 단계를 알려줘

1m 4s 동안 생각함
지금 기준으로는 다음 단계는 Save/Load를 붙이는 게 맞습니다.
보여주신 화면만 보면 이미 핵심 루프는 꽤 잘 연결됐습니다.
전투 결과 요약에서 EXP / Gold / Level Up / Evolution까지 보입니다.
상점 구매도 동작합니다.
촌장 회복도 됩니다.
트레이너 재대결 방지도 됩니다.
즉, 이제는 새 기능을 더 얹기보다 지금까지 만든 진행 상태를 저장하고 다시 불러올 수 있게 만드는 단계가 우선입니다.
특히 지금 Feloria 저장소는 아직 공개 저장소 기준으로도 초기 상태에 가깝습니다. dist와 node_modules가 그대로 올라가 있고, 커밋 수는 12개이며, README.MD도 사실상 정리되지 않은 상태입니다.
다음 단계 추천
Phase 8: Save/Load, State Restoration, and Real Persistence
왜 이게 다음이냐면, 지금부터는 저장해야 할 상태가 많습니다.
playerParty
playerCollection
playerInventory
playerGold
seen / caught
quests
defeatedTrainers
currentMapId
player position
evolved starter state
이걸 저장 없이 계속 확장하면 나중에 구조가 쉽게 꼬입니다.

아래 프롬프트를 그대로 보내세요.
Feloria - Phase 8: Save/Load, State Restoration, and Persistent Progress

Phase 7 is in a good enough state to move forward.

The current game now has:
- exploration
- wild battles
- trainer battles
- capture
- leveling
- evolution
- healing
- shop purchases
- basic codex
- quest progression
- gold rewards

Now proceed to Phase 8.

Goal:
Add reliable save/load functionality so the current game state can persist across sessions and future content expansion remains stable.

Important:
I will do all verification manually myself.
Do not claim anything is verified.
Implement the systems so they are stable and easy to test.

--------------------------------
PHASE 8 GOAL
--------------------------------

This phase should add:
- manual save
- continue/load from title
- autosave on important progression events
- full state restoration into the current Phaser architecture

Do not add new regions or large new gameplay systems in this phase.
Focus on persistence and stability.

--------------------------------
SAVE / LOAD REQUIREMENTS
--------------------------------

1. Add save system
Implement a saveSystem.js or equivalent module.

Use localStorage for now.

Support at minimum:
- one active save slot
- save timestamp
- save version number

2. Save data contents
The save file must include all important gameplay state:

- playerName
- selectedStarter
- playerParty
- playerCollection
- playerInventory
- playerGold
- seenCreatureIds
- caughtCreatureIds
- active quests / completed quests
- defeatedTrainers
- currentMapId
- player position / spawn coordinates
- story flags if any
- current starter evolution state
- current levels / EXP / HP

3. Continue option
The Continue option on StartScene must now become functional.

Rules:
- if no save exists, Continue should be disabled or show "No save data"
- if save exists, Continue should restore the saved game correctly
- loading should return the player to the correct map and position

4. Manual save access
Add a simple Save option to the pause menu.

Menu should now include:
- Party
- Inventory
- Codex
- Quests
- Save
- Close

5. Autosave triggers
Add autosave after important events such as:
- capturing a creature
- defeating a trainer
- making a shop purchase
- healing the party
- evolution
- completing a quest step
- changing maps

Keep autosave simple and reliable.

6. Load behavior
When loading:
- restore registry state from save data
- restore current map
- restore player position
- restore party HP / EXP / evolution state
- restore inventory and gold
- restore defeated trainer status
- restore codex progress
- restore quest progress

Do not partially load state.

--------------------------------
BUG PREVENTION RULES
--------------------------------

1. Prevent corrupted save crashes
- if save data is missing fields, fall back safely
- use a save version field
- validate required keys before loading

2. Prevent stale registry state
- loading must overwrite active registry state fully
- do not mix old session state with loaded state

3. Prevent save/load desync
- shop, battle, codex, quest, and party scenes must all read/write the same registry-backed state
- save should serialize the source of truth, not scene-local temporary values

4. Prevent bad spawn loads
- saved player position must be restored only to valid walkable tiles
- if position is invalid, fall back to a safe map spawn point

5. Prevent defeated trainer reset
- defeatedTrainers must persist correctly across reloads

6. Prevent evolution rollback
- evolved creature forms must remain evolved after saving and loading

7. Prevent inventory/gold rollback
- purchases and item consumption must persist after reload

--------------------------------
MANUAL-TEST READY TARGETS
--------------------------------

Implement these so I can test them manually:

1. Save from menu
2. Close/reload the game and Continue from title
3. Return to the same map and position
4. Party HP/EXP remain correct after reload
5. Evolved creature remains evolved after reload
6. Inventory and Gold remain correct after reload
7. Captured creatures remain in collection after reload
8. Defeated trainers stay defeated after reload
9. Quest progress remains correct after reload

--------------------------------
FINAL NOTE
--------------------------------

The goal of Phase 8 is not visual polish.
The goal is to make Feloria persistent and safe to expand.

Do not move on to large new content until save/load is stable.
그리고 이 단계 끝나면 그다음은 보통 이 순서가 좋습니다.
Phase 9: 실제 타일셋/스프라이트 적용
Phase 10: 두 번째 마을/시련/보스
Phase 11: 사운드/연출/애니메이션
원하시면 다음엔 .gitignore 정리 문구도 같이 드리겠습니다.


너 답변이 이렇게 보여

맞습니다. 저건 응답이 비정상적으로 섞여서 나온 것입니다.
중간에 내부 형식처럼 보이는 문자열이 끼어들어서 읽기 어렵게 표시됐네요.
아래처럼 깔끔한 일반 텍스트 버전으로 다시 드리겠습니다.
이걸 그대로 안티그래비티에 보내시면 됩니다.

Feloria - Phase 8: Save/Load, State Restoration, and Persistent Progress
Phase 7 is in a good enough state to move forward.
The current game now has:
exploration
wild battles
trainer battles
capture
leveling
evolution
healing
shop purchases
basic codex
quest progression
gold rewards
Now proceed to Phase 8.
Goal:
Add reliable save/load functionality so the current game state can persist across sessions and future content expansion remains stable.
Important:
I will do all verification manually myself.
Do not claim anything is verified.
Implement the systems so they are stable and easy to test.

Phase 8 Goal
This phase should add:
manual save
continue/load from title
autosave on important progression events
full state restoration into the current Phaser architecture
Do not add new regions or large new gameplay systems in this phase.
Focus on persistence and stability.

Save / Load Requirements
Add a save system
Implement a saveSystem.js or equivalent module.
Use localStorage for now.
Support at minimum:
one active save slot
save timestamp
save version number
Save data contents
The save file must include all important gameplay state:
playerName
selectedStarter
playerParty
playerCollection
playerInventory
playerGold
seenCreatureIds
caughtCreatureIds
active quests / completed quests
defeatedTrainers
currentMapId
player position / spawn coordinates
story flags if any
current starter evolution state
current levels / EXP / HP
Continue option
The Continue option on StartScene must now become functional.
Rules:
if no save exists, Continue should be disabled or show "No save data"
if save exists, Continue should restore the saved game correctly
loading should return the player to the correct map and position
Manual save access
Add a simple Save option to the pause menu.
Menu should now include:
Party
Inventory
Codex
Quests
Save
Close
Autosave triggers
Add autosave after important events such as:
capturing a creature
defeating a trainer
making a shop purchase
healing the party
evolution
completing a quest step
changing maps
Keep autosave simple and reliable.
Load behavior
When loading:
restore registry state from save data
restore current map
restore player position
restore party HP / EXP / evolution state
restore inventory and gold
restore defeated trainer status
restore codex progress
restore quest progress
Do not partially load state.

Bug Prevention Rules
Prevent corrupted save crashes
if save data is missing fields, fall back safely
use a save version field
validate required keys before loading
Prevent stale registry state
loading must overwrite active registry state fully
do not mix old session state with loaded state
Prevent save/load desync
shop, battle, codex, quest, and party scenes must all read/write the same registry-backed state
save should serialize the source of truth, not scene-local temporary values
Prevent bad spawn loads
saved player position must be restored only to valid walkable tiles
if position is invalid, fall back to a safe map spawn point
Prevent defeated trainer reset
defeatedTrainers must persist correctly across reloads
Prevent evolution rollback
evolved creature forms must remain evolved after saving and loading
Prevent inventory/gold rollback
purchases and item consumption must persist after reload

Manual-Test Ready Targets
Implement these so I can test them manually:
Save from menu
Close/reload the game and Continue from title
Return to the same map and position
Party HP/EXP remain correct after reload
Evolved creature remains evolved after reload
Inventory and Gold remain correct after reload
Captured creatures remain in collection after reload
Defeated trainers stay defeated after reload
Quest progress remains correct after reload

Final Note
The goal of Phase 8 is not visual polish.
The goal is to make Feloria persistent and safe to expand.
Do not move on to large new content until save/load is stable.

앞으로는 저런 식으로 깨지지 않게 순수 텍스트 형태로만 드리겠습니다.

아 아니야 내 컴퓨터가 문제였네 이제 위에 잘보여. 원래 해주던대로 해줘.

알겠습니다. 앞으로는 원래 방식으로 이어가겠습니다.
지금 다음 단계는 그대로 Phase 8: Save/Load로 가면 됩니다.
아래 프롬프트를 그대로 보내세요.
Feloria - Phase 8: Save/Load, State Restoration, and Persistent Progress

Phase 7 is in a good enough state to move forward.

The current game now has:
- exploration
- wild battles
- trainer battles
- capture
- leveling
- evolution
- healing
- shop purchases
- basic codex
- quest progression
- gold rewards

Now proceed to Phase 8.

Goal:
Add reliable save/load functionality so the current game state can persist across sessions and future content expansion remains stable.

Important:
I will do all verification manually myself.
Do not claim anything is verified.
Implement the systems so they are stable and easy to test.

--------------------------------
PHASE 8 GOAL
--------------------------------

This phase should add:
- manual save
- continue/load from title
- autosave on important progression events
- full state restoration into the current Phaser architecture

Do not add new regions or large new gameplay systems in this phase.
Focus on persistence and stability.

--------------------------------
SAVE / LOAD REQUIREMENTS
--------------------------------

1. Add save system
Implement a saveSystem.js or equivalent module.

Use localStorage for now.

Support at minimum:
- one active save slot
- save timestamp
- save version number

2. Save data contents
The save file must include all important gameplay state:

- playerName
- selectedStarter
- playerParty
- playerCollection
- playerInventory
- playerGold
- seenCreatureIds
- caughtCreatureIds
- active quests / completed quests
- defeatedTrainers
- currentMapId
- player position / spawn coordinates
- story flags if any
- current starter evolution state
- current levels / EXP / HP

3. Continue option
The Continue option on StartScene must now become functional.

Rules:
- if no save exists, Continue should be disabled or show "No save data"
- if save exists, Continue should restore the saved game correctly
- loading should return the player to the correct map and position

4. Manual save access
Add a simple Save option to the pause menu.

Menu should now include:
- Party
- Inventory
- Codex
- Quests
- Save
- Close

5. Autosave triggers
Add autosave after important events such as:
- capturing a creature
- defeating a trainer
- making a shop purchase
- healing the party
- evolution
- completing a quest step
- changing maps

Keep autosave simple and reliable.

6. Load behavior
When loading:
- restore registry state from save data
- restore current map
- restore player position
- restore party HP / EXP / evolution state
- restore inventory and gold
- restore defeated trainer status
- restore codex progress
- restore quest progress

Do not partially load state.

--------------------------------
BUG PREVENTION RULES
--------------------------------

1. Prevent corrupted save crashes
- if save data is missing fields, fall back safely
- use a save version field
- validate required keys before loading

2. Prevent stale registry state
- loading must overwrite active registry state fully
- do not mix old session state with loaded state

3. Prevent save/load desync
- shop, battle, codex, quest, and party scenes must all read/write the same registry-backed state
- save should serialize the source of truth, not scene-local temporary values

4. Prevent bad spawn loads
- saved player position must be restored only to valid walkable tiles
- if position is invalid, fall back to a safe map spawn point

5. Prevent defeated trainer reset
- defeatedTrainers must persist correctly across reloads

6. Prevent evolution rollback
- evolved creature forms must remain evolved after saving and loading

7. Prevent inventory/gold rollback
- purchases and item consumption must persist after reload

--------------------------------
MANUAL-TEST READY TARGETS
--------------------------------

Implement these so I can test them manually:

1. Save from menu
2. Close/reload the game and Continue from title
3. Return to the same map and position
4. Party HP/EXP remain correct after reload
5. Evolved creature remains evolved after reload
6. Inventory and Gold remain correct after reload
7. Captured creatures remain in collection after reload
8. Defeated trainers stay defeated after reload
9. Quest progress remains correct after reload

--------------------------------
FINAL NOTE
--------------------------------

The goal of Phase 8 is not visual polish.
The goal is to make Feloria persistent and safe to expand.

Do not move on to large new content until save/load is stable.

다음 Phase (미리 알려드림) Phase 6 끝나면 다음은 보통 이 순서입니다. Phase 7 진짜 그래픽 타일셋 플레이어 스프라이트 몬스터 스프라이트 UI 정리 Phase 8 새로운 지역 2~3개 보스/체육관 스타일 배틀 더 많은 몬스터 Phase 9 저장/불러오기 사운드 애니메이션 근데 순서가 이렇게 너가 한다고 하지 않았어?

좋은 질문입니다. 👍
결론부터 말하면 둘 다 맞는 흐름인데, 제가 현실적인 개발 순서로 조금 바꿔서 추천한 것입니다. 그래서 헷갈리신 겁니다.
정리해서 설명하겠습니다.

1️⃣ 제가 처음 말했던 일반적인 게임 개발 흐름
이건 게임을 처음부터 설계할 때 보통 사용하는 순서입니다.
Phase 6
콘텐츠 확장
새로운 루트
트레이너 배틀
상점
힐링
Phase 7
그래픽 적용
타일셋
플레이어 스프라이트
몬스터 스프라이트
UI 정리
Phase 8
콘텐츠 확장
새로운 지역
체육관/보스
몬스터 추가
Phase 9
마무리 시스템
저장
사운드
애니메이션
즉 프로토타입 → 그래픽 → 콘텐츠 → 마무리 순서입니다.

2️⃣ 그런데 Feloria 상황에서는 순서를 바꾼 이유
지금 Feloria는 이미
전투
포획
레벨업
진화
상점
트레이너
퀘스트
인벤토리
핵심 RPG 시스템이 이미 다 들어갔습니다.
이 상태에서 Save/Load가 없으면 이런 문제가 생깁니다.
예를 들어:
포획한 몬스터 날아감
진화한 스타터 리셋됨
골드 사라짐
퀘스트 초기화
트레이너 다시 살아남
그래서 콘텐츠를 더 늘리기 전에 저장을 붙이는 것이 안전합니다.

3️⃣ 그래서 Feloria 기준 현실적인 순서
Feloria는 지금 여기까지 왔습니다.
탐험
→ 조우
→ 전투
→ 포획
→ 레벨업
→ 진화
→ 상점
→ 힐링
→ 트레이너
이 상태면 다음은 이게 좋습니다.
Phase 8 (지금 추천한 것)
Save / Load
지금까지 만든 진행 상태 보호
버그 디버깅 쉬움
이후 확장 안전
Phase 9
그래픽 교체
타일셋
캐릭터
몬스터
Phase 10
콘텐츠 확장
새로운 지역
체육관
보스
