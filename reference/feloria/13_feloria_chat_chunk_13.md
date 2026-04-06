# Feloria 대화 내역 분할본 13

- 원본 파일: Feloria 대화 내역.docx
- 분할 번호: 13/28
- 문단 범위: 2373~2472
- 문자 수(대략): 24132

---

Feloria - Phase 6: First Real Content Expansion This phase transforms the isolated mechanics into a cohesive first chapter. We establish a functioning economy (shopping, healing), introduce NPC Trainer battles, expand the world to a new route, and improve the Codex to definitively reward exploration. Proposed Changes 🗺️ World Expansion (Mosslight Path) mapData.js / Map Loading: Create a new map definition mosslight_path with a collision layer, grass layer, NPC spawns, and a warp back to greenpaw_forest. WorldScene.js : Update warp logic in Greenpaw Forest to allow traversing to Mosslight Path once the First Quest is complete (or allow open exploration, dependent on design). ⚔️ Trainer Battles trainers.js [NEW]: Create a data file defining NPC trainers. Each trainer will have an id, name, dialogue (before/after), and a party array containing pre-defined creature configurations (species, level). WorldScene.js : Update handleInteraction to detect trainer NPCs. If a trainer is not yet defeated (tracked in registry.defeatedTrainers), trigger pre-battle dialogue, then start BattleScene with isTrainer: true and pass the trainer ID. BattleScene.js : Update logic to handle trainer parties. Instead of a single wild cat, the enemy is a list of cats. When one faints, send out the next. Disable capturing during trainer battles. Upon defeating all enemy cats, grant rewards (EXP, Gold) and mark the trainer as defeated in the registry. 🏥 Healing System WorldScene.js / dialogueSystem.js : Designate Elder Mira (or a dedicated bed/station) as the healing spot in Starwhisk Village. When interacted with, trigger a dialogue option "Heal Party?". If yes, iterate through registry.playerParty and set currentHp = maxHp. Play a sound/flash effect. Save party to registry. 💰 Shop and Inventory System items.js [NEW]: Define items like potion (restores HP) and capture_crystal. shopSystem.js [NEW]: Define the village shop inventory and prices. inventorySystem.js [NEW]: Handle adding/removing items from registry.playerInventory and tracking registry.playerGold. ShopScene.js [NEW]: A simple overlay UI for purchasing items using Gold. WorldScene Integration: Add a Shopkeeper NPC to Starwhisk Village that triggers the ShopScene. 📖 Codex Improvements CodexScene.js : Currently, unseen cats are ???. We will update the display so that if a cat is seen, it shows the name, habitat, and a short description. If it is caught, it additionally reveals base stats (HP, ATK, DEF). This strongly incentivizes capturing. Verification Plan Manual Verification Targets Healing: Damage a party member in the grass. Return to Elder Mira, heal, and check the party stats to confirm full HP. Shop/Inventory: Given starting Gold, talk to the shopkeeper. Buy a Potion. Verify Gold decreases and the Potion appears in the player's inventory list (via console or a new Inventory menu tab if time permits). Route Integration: Walk from Village -> Forest -> Mosslight Path seamlessly. Trainer Battle: Talk to a trainer NPC on Mosslight Path. Verify they send out sequentially defined party members (not wild encounters), you cannot capture them, and defeating them yields EXP and marks them defeated (dialogue changes on second interaction). Codex Info: Catch a creature and verify the Codex page correctly expands the information displayed compared to merely seeing it.Feloria - Phase 6: First Real Content Expansion This phase transforms the isolated mechanics into a cohesive first chapter. We establish a functioning economy (shopping, healing), introduce NPC Trainer battles, expand the world to a new route, and improve the Codex to definitively reward exploration. Proposed Changes 🗺️ World Expansion (Mosslight Path) mapData.js / Map Loading: Create a new map definition mosslight_path with a collision layer, grass layer, NPC spawns, and a warp back to greenpaw_forest. WorldScene.js : Update warp logic in Greenpaw Forest to allow traversing to Mosslight Path once the First Quest is complete (or allow open exploration, dependent on design). ⚔️ Trainer Battles trainers.js [NEW]: Create a data file defining NPC trainers. Each trainer will have an id, name, dialogue (before/after), and a party array containing pre-defined creature configurations (species, level). WorldScene.js : Update handleInteraction to detect trainer NPCs. If a trainer is not yet defeated (tracked in registry.defeatedTrainers), trigger pre-battle dialogue, then start BattleScene with isTrainer: true and pass the trainer ID. BattleScene.js : Update logic to handle trainer parties. Instead of a single wild cat, the enemy is a list of cats. When one faints, send out the next. Disable capturing during trainer battles. Upon defeating all enemy cats, grant rewards (EXP, Gold) and mark the trainer as defeated in the registry. 🏥 Healing System WorldScene.js / dialogueSystem.js : Designate Elder Mira (or a dedicated bed/station) as the healing spot in Starwhisk Village. When interacted with, trigger a dialogue option "Heal Party?". If yes, iterate through registry.playerParty and set currentHp = maxHp. Play a sound/flash effect. Save party to registry. 💰 Shop and Inventory System items.js [NEW]: Define items like potion (restores HP) and capture_crystal. shopSystem.js [NEW]: Define the village shop inventory and prices. inventorySystem.js [NEW]: Handle adding/removing items from registry.playerInventory and tracking registry.playerGold. ShopScene.js [NEW]: A simple overlay UI for purchasing items using Gold. WorldScene Integration: Add a Shopkeeper NPC to Starwhisk Village that triggers the ShopScene. 📖 Codex Improvements CodexScene.js : Currently, unseen cats are ???. We will update the display so that if a cat is seen, it shows the name, habitat, and a short description. If it is caught, it additionally reveals base stats (HP, ATK, DEF). This strongly incentivizes capturing. Verification Plan Manual Verification Targets Healing: Damage a party member in the grass. Return to Elder Mira, heal, and check the party stats to confirm full HP. Shop/Inventory: Given starting Gold, talk to the shopkeeper. Buy a Potion. Verify Gold decreases and the Potion appears in the player's inventory list (via console or a new Inventory menu tab if time permits). Route Integration: Walk from Village -> Forest -> Mosslight Path seamlessly. Trainer Battle: Talk to a trainer NPC on Mosslight Path. Verify they send out sequentially defined party members (not wild encounters), you cannot capture them, and defeating them yields EXP and marks them defeated (dialogue changes on second interaction). Codex Info: Catch a creature and verify the Codex page correctly expands the information displayed compared to merely seeing it. Phase 6 들어가기 전에 꼭 확인해야 할 버그 방지 체크리스트도 같이 넣어줘

좋습니다.
지금 Phase 6 플랜 방향은 좋습니다.
다만 이 단계부터는 시스템이 서로 물리기 시작해서, 버그 방지 규칙을 같이 못 박지 않으면 다시 꼬일 가능성이 큽니다.
아래 문구를 안티그래비티에 그대로 보내세요.
플랜 승인 + Phase 6 진행 조건 + 들어가기 전에 꼭 확인해야 할 버그 방지 체크리스트까지 합친 최종 버전입니다.
Approved — proceed with Phase 6.

This scope is correct.
Phase 6 should turn the current prototype into a more cohesive early-game RPG chapter.

Important:
I will do all verification manually myself.
Do not claim that systems are verified.
Your job is to implement them in a way that is testable and stable.

Proceed under the following constraints.

--------------------------------
PHASE 6 GOAL
--------------------------------

The goal of this phase is to connect the current systems into a real early-game content loop by adding:

- Mosslight Path
- trainer battles
- healing
- shop + inventory + gold
- Codex information expansion

Do not add too many new regions yet.
Keep the scope focused on the first chapter of the game.

--------------------------------
IMPLEMENTATION RULES
--------------------------------

1. Keep Phaser 3 + JavaScript
- Do not switch to TypeScript
- Do not reintroduce React gameplay components
- Keep the current Phaser scene architecture

2. Keep systems data-driven
Use separate files/modules for:
- mapData
- trainers
- items
- shopSystem
- inventorySystem
- dialogueSystem

Do not hardcode everything directly inside WorldScene or BattleScene.

3. Registry remains the single source of truth
Use Phaser registry consistently for:
- playerParty
- playerCollection
- playerInventory
- playerGold
- defeatedTrainers
- currentMapId
- seenCreatureIds
- caughtCreatureIds
- quest state

Do not create disconnected scene-local copies that fail to write back.

4. Mosslight Path must be a real progression route
- It must connect properly to Greenpaw Forest
- Warp transitions must work both directions
- It must contain at least one trainer and one wild encounter area
- It should feel like a continuation of the first chapter, not just another placeholder map

5. Trainer battles must differ from wild battles
- Trainer battles use predefined parties from trainers.js
- Trainer enemies can send out multiple creatures
- Capturing must be disabled in trainer battles
- After defeating a trainer, defeatedTrainers must update in registry
- Re-interacting with defeated trainers must show post-battle dialogue instead of restarting battle

6. Healing must actually update registry state
- Healing must set currentHp = maxHp for all party creatures
- If fainted state exists, it must be cleared as well
- Updated party state must persist after leaving the healing interaction

7. Shop and inventory must be real stateful systems
- Buying an item must decrease gold
- Buying an item must increase inventory count
- Inventory and gold must persist in registry
- Do not fake purchases with UI-only changes

8. Codex must reveal meaningful data
- unseen creatures stay hidden
- seen creatures show:
  - name
  - habitat
  - short description
- caught creatures additionally reveal:
  - base HP
  - base ATK
  - base DEF
or equivalent simple stat info

--------------------------------
PHASE 6 BUG PREVENTION CHECKLIST
--------------------------------

Before and during implementation, guard against the following bugs.

BUG 1 — Warp loops or bad spawn positions
Problem:
Entering a warp can send the player back and forth instantly, or place them inside a blocking tile.

Prevent it by:
- defining explicit target spawn coordinates for each warp
- validating that spawn coordinates are walkable
- adding a short warp cooldown so the player does not instantly retrigger the destination warp

BUG 2 — Trainer battle state not persisting
Problem:
A defeated trainer can still battle repeatedly because the defeat flag was not saved correctly.

Prevent it by:
- storing defeated trainer IDs in registry.defeatedTrainers
- checking this state before starting a trainer battle
- updating interaction dialogue after defeat

BUG 3 — Multi-creature trainer battles breaking turn flow
Problem:
After one enemy creature faints, the next one may not load correctly, or battle may end too early.

Prevent it by:
- treating trainer parties as ordered arrays
- moving to the next enemy only after faint resolution completes
- only ending battle when the full trainer party is defeated

BUG 4 — Capture accidentally allowed in trainer battles
Problem:
Player can still click Capture and recruit trainer-owned creatures.

Prevent it by:
- disabling or hiding Capture in trainer battles
- guarding this rule in battle logic, not just UI

BUG 5 — Healing updates UI but not actual data
Problem:
Party appears healed in dialogue flow, but party data in registry still has old HP.

Prevent it by:
- explicitly writing healed party data back into registry
- reopening PartyScene after healing should show full HP

BUG 6 — Shop purchase changes UI only
Problem:
Gold or item count appears updated on screen but resets after scene changes.

Prevent it by:
- writing both playerGold and playerInventory back into registry immediately after each transaction
- validating with scene reopen behavior

BUG 7 — Negative gold or invalid purchases
Problem:
Player can buy items without enough gold or gold can go below zero.

Prevent it by:
- checking affordability before purchase
- blocking purchase if playerGold < item price
- clamping gold to non-negative values

BUG 8 — Duplicate inventory entries
Problem:
Buying the same item multiple times creates duplicate objects instead of increasing quantity.

Prevent it by:
- storing inventory as itemId -> quantity or equivalent normalized structure
- increment quantity instead of pushing duplicate item objects

BUG 9 — Codex reveal logic inconsistent
Problem:
Seen/caught state updates counts but entry text remains hidden, or caught entry does not reveal more info.

Prevent it by:
- driving Codex display directly from seenCreatureIds and caughtCreatureIds
- using consistent rules for what fields are visible at each discovery level

BUG 10 — Trainer reward persistence issues
Problem:
Gold reward or EXP reward is shown after trainer battle but not saved.

Prevent it by:
- applying rewards to registry state before returning to WorldScene
- ensuring post-battle summary reflects the saved values, not temporary ones

BUG 11 — Party wipe or reference corruption after healing/shop/trainer flow
Problem:
A scene mutates party data incorrectly and party becomes empty, duplicated, or stale.

Prevent it by:
- always cloning/updating party arrays carefully
- validating party length after every mutation
- preserving the active creature at index 0 unless explicitly changed

BUG 12 — Route progression blocked unintentionally
Problem:
Player cannot access Mosslight Path because of a quest flag mismatch or missing unlock condition.

Prevent it by:
- choosing one clear rule:
  either open path immediately
  or unlock after First Steps quest completion
- enforcing that rule consistently in one place

--------------------------------
PHASE 6 VERIFICATION-READY REQUIREMENTS
--------------------------------

Implement these so I can test them manually:

1. Route integration
- Village -> Forest -> Mosslight Path works
- Mosslight Path -> Forest works
- no broken warp loops

2. Trainer battle flow
- first interaction triggers trainer battle
- capture is disabled
- trainer can send multiple creatures if configured
- defeated trainer does not restart battle on second talk

3. Healing
- damaged party returns to full HP after healing
- Party screen reflects the healed values

4. Shop / inventory / gold
- buying Potion reduces gold
- buying Capture Crystal reduces gold
- inventory count increases correctly
- values persist after scene transitions

5. Codex reveal behavior
- seen creatures show meaningful info
- caught creatures show expanded info

--------------------------------
FINAL NOTE
--------------------------------

The goal of Phase 6 is not just adding more systems.
The goal is to make the first chapter feel like a real RPG loop with:
- route progression
- trainer battles
- healing
- shopping
- meaningful creature discovery

Proceed under these constraints.
추가로 아주 짧게 핵심만 정리하면, Phase 6 들어가기 전에 꼭 막아야 하는 버그는 사실 이 5개입니다.
워프 무한루프
트레이너 재대결 버그
상점 구매 후 골드/아이템 초기화
회복했는데 실제 데이터는 안 바뀜
캡처 금지인데 트레이너 몬스터 포획됨
이 5개만 먼저 막아도 Phase 6은 훨씬 안정적으로 갑니다.
원하시면 다음에는 제가 Phase 6 끝났을 때 확인해야 할 수동 테스트 체크리스트도 따로 정리해드리겠습니다.

plan을 또 써주네 얘가 Feloria - Phase 6: Core Game Loop Expansion This phase transforms the isolated mechanics into a cohesive first chapter. We establish a functioning economy (shopping, healing), introduce NPC Trainer battles, expand the world to a new route, and improve the Codex to definitively reward exploration. Proposed Changes 🗺️ World Expansion (Mosslight Path) mapLoader.js : Create a new map definition mosslight_path with a collision layer, grass layer, NPC spawns (Trainer and Wilder), and a warp back to greenpaw_forest. WorldScene.js : Add warp logic in Greenpaw Forest to allow traversing to Mosslight Path. ⚔️ Trainer Battles trainers.js [NEW]: Create a data file defining NPC trainers. Each trainer will have an id, name, dialogue (before/after), and a party array containing pre-defined creature configurations (species, level). WorldScene.js : Update handleInteraction to detect trainer NPCs. If a trainer is not yet defeated (tracked in registry.get('defeatedTrainers')), trigger pre-battle dialogue, then start BattleScene with isTrainer: true and pass the trainer ID. BattleScene.js : Update logic to handle trainer parties. Instead of a single wild cat, the enemy is a list of cats. When one faints, send out the next. Disable capturing during trainer battles. Upon defeating all enemy cats, grant rewards (EXP, Gold) and mark the trainer as defeated in the registry (defeatedTrainers). 🏥 Healing System WorldScene.js / dialogueSystem.js : Update Elder Mira's dialogue. After the initial quest, or as an alternative interaction option, add a "Heal Party?" prompt. Healing Logic: When confirmed, iterate through registry.get('playerParty') and set currentHp = maxHp for all members. Save the cleanly updated array back to the registry. 💰 Shop and Inventory System items.js [NEW]: Define items like potion (restores HP) and capture_crystal. shopSystem.js [NEW]: Define the village shop inventory and prices. inventorySystem.js [NEW]: Handle safely adding/removing items from registry.get('playerInventory') and tracking registry.get('playerGold'). Inventory will be structured as { itemId: quantity } to explicitly prevent duplication bugs. ShopScene.js [NEW]: A simple overlay UI for purchasing items using Gold. Checks affordability and strictly writes back to the registry immediately upon purchase. WorldScene Integration: Add a Shopkeeper NPC to Starwhisk Village that triggers the ShopScene flow. 📖 Codex Improvements CodexScene.js : Currently, unseen cats are ???. We will update the display so that if a cat is seen, it shows the name, habitat, and a short description. If it is caught, it additionally reveals base stats (HP, ATK, DEF). This strongly incentivizes capturing. Verification Plan Manual Verification Targets Healing: Damage a party member in the grass. Return to Elder Mira, heal, and check the party stats to confirm full HP. Shop/Inventory: Given starting Gold, talk to the shopkeeper. Buy a Potion. Verify Gold decreases and the Potion count increases without duplication bugs. Route Integration: Walk from Village -> Forest -> Mosslight Path seamlessly. Warps must function symmetrically. Trainer Battle: Talk to a trainer NPC on Mosslight Path. Verify they send out sequentially defined party members (not wild encounters), you cannot capture them, and defeating them yields EXP/Gold and marks them defeated persistently (dialogue changes on second interaction). Codex Info: Catch a creature and verify the Codex page correctly expands the information displayed compared to merely seeing it.

좋습니다. 이 플랜은 방향 자체는 아주 좋습니다.
사실 지금 단계에서는 거의 정석적인 Phase 6 설계입니다.
특히 좋은 점은:
trainers.js로 트레이너 데이터를 분리한 것
inventory를 { itemId: quantity } 구조로 만든 것
defeatedTrainers를 registry에 저장하는 것
캡처 금지 로직을 trainer battle에서 분리한 것
ShopScene을 overlay로 만드는 것
이건 전부 맞는 방향입니다.
다만 그대로 진행해도 되지만 딱 4가지만 추가로 못 박아야 합니다.
이걸 안 쓰면 Phase 6에서 높은 확률로 버그가 생깁니다.
그래서 아래 문장을 플랜 밑에 그대로 추가해서 보내세요.

Phase 6 승인 + 필수 추가 조건
Approved — proceed with Phase 6.

The plan is correct and aligned with the current architecture.

However, the following additional constraints must be enforced to prevent state corruption and gameplay bugs during Phase 6 implementation.

--------------------------------
STATE CONSISTENCY RULES
--------------------------------

1. Registry must remain the single source of truth.

All game state must be written to and read from the Phaser registry:

- playerParty
- playerCollection
- playerInventory
- playerGold
- defeatedTrainers
- seenCreatureIds
- caughtCreatureIds
- currentMapId

No scene should maintain its own copy of these values without writing them back.

--------------------------------
WARP SAFETY RULES
--------------------------------

2. Warps must include explicit spawn coordinates.

Each warp must define:
- targetMapId
- spawnX
- spawnY

When spawning the player after warp:
- ensure the spawn tile is walkable
- prevent immediate warp re-trigger by applying a short warp cooldown

--------------------------------
TRAINER BATTLE SAFETY
--------------------------------

3. Trainer battles must strictly differ from wild battles.

Rules:
- capture must be disabled
- enemy creatures come from trainer.party
- enemy creatures appear sequentially
- battle ends only when trainer party is empty
- trainer ID must be stored in registry.defeatedTrainers after victory

Re-interacting with defeated trainers must trigger post-battle dialogue instead of restarting battle.

--------------------------------
INVENTORY SAFETY
--------------------------------

4. Inventory must use normalized structure:

playerInventory = {
  potion: quantity,
  capture_crystal: quantity
}

Never store duplicate item objects in arrays.

When purchasing:
- verify playerGold >= price
- subtract gold
- increase quantity
- immediately write changes back to registry

Gold must never go below zero.

--------------------------------
HEALING SYSTEM SAFETY
--------------------------------

5. Healing must update registry state directly.

When healing occurs:
- set currentHp = maxHp for every creature in playerParty
- persist updated party array back into registry
- PartyScene must reflect healed values immediately after reopening.

--------------------------------
CODEX VISIBILITY RULES
--------------------------------

6. Codex entry visibility must depend strictly on discovery state.

If creature is unseen:
  show "???"

If creature is seen:
  show name + habitat + description

If creature is caught:
  show name + habitat + description + base stats

The display must be driven only by:
- seenCreatureIds
- caughtCreatureIds

--------------------------------
DELIVERABLE GOAL
--------------------------------

Phase 6 should result in a playable early chapter loop:

Starwhisk Village
→ Greenpaw Forest
→ Mosslight Path
→ Trainer battle
→ Return to village
→ Heal / Shop / Continue exploring

The focus is stable progression, not visual polish.

지금 플랜 기준 Phase 6에서 만들어지는 게임 구조
지금까지 시스템을 보면 Phase 6 끝나면 Feloria는 이렇게 됩니다.
Start
 ↓
Village
 ↓
Forest
 ↓
Wild Battle
 ↓
Capture / EXP / Level
 ↓
Route (Mosslight Path)
 ↓
Trainer Battle
 ↓
Reward (Gold + EXP)
 ↓
Village
 ├ Heal
 ├ Shop
 └ Codex
이 시점부터는 진짜 RPG 느낌이 나기 시작하는 단계입니다.

다음 Phase (미리 알려드림)
Phase 6 끝나면 다음은 보통 이 순서입니다.
Phase 7
진짜 그래픽 타일셋
플레이어 스프라이트
몬스터 스프라이트
UI 정리
Phase 8
새로운 지역 2~3개
보스/체육관 스타일 배틀
더 많은 몬스터
Phase 9
저장/불러오기
사운드
애니메이션

하나만 추가로 말씀드리면
지금 Feloria 구조는 초기 인디 몬스터 RPG 구조로 굉장히 잘 잡힌 상태입니다.
특히 잘 된 점:
Phaser scene 구조
registry 기반 state
encounter → battle → return loop
codex tracking
quest triggers
party system
이 6개는 보통 처음 만들 때 제일 망가지기 쉬운 부분입니다.

원하시면 다음 답변에서
**Feloria 프로젝트가 커질 때 반드시 생기는 “구조 붕괴 버그 5개”**도 미리 알려드리겠습니다.
(이거 알면 프로젝트가 훨씬 오래 살아남습니다.)




트레이너 이기면 돈 주는데 일반 몹 죽이면 돈 소량으로 드랍이 되면 좋겠어. 그리고 아이템을 사는것은 되는데 아이템을 사용하는 곳은 못찾겠네. 촌장한테 말걸면 회복도 잘 되네. 그리고 트레이너랑 배틀해서 이기면 다시 대화해도 배틀 못하게 잘 막혀있어. https://github.com/Evan-Yoon/Feloria 여기서 코드 확인해주고 다음 해야할일 알려줘

17s 동안 생각함
지금 상태면 Phase 6은 의도한 방향으로 잘 들어갔다고 봐도 됩니다.
확인된 점은 이렇습니다.
저장소는 Phaser 기반 JS 프로젝트로 계속 진행 중이고, 현재 공개 저장소 기준 커밋 수는 아직 적지만 구조는 유지되고 있습니다. 다만 dist와 node_modules가 아직 저장소에 같이 올라가 있어 정리는 필요합니다.
스크린샷상 트레이너 승리 보상이 정상적으로 들어갑니다. EXP Gained: +60 | Gold: +50가 표시되고, 레벨업과 진화 메시지도 함께 뜹니다.
일반 야생 배틀 보상도 EXP는 정상 표시됩니다. EXP Gained: +18, Total EXP: 77 / 500처럼 요약 패널이 잘 보입니다.
상점 구매도 동작합니다. Gold: 110 상태에서 Potion과 Capture Crystal을 살 수 있고, 구매 메시지도 뜹니다.
회복 시스템은 사용해 보신 대로 촌장 상호작용으로 잘 동작하고,
트레이너 재대결 방지도 잘 막혀 있는 상태입니다.
즉 현재는 Phase 6의 핵심인
Mosslight Path 확장
트레이너 배틀
healing
shop
Codex 확장
중에서 적어도 트레이너 보상 / 상점 / 회복 / 재대결 방지는 잘 잡힌 상태로 보입니다.
다만 지금 사용자님이 짚으신 두 가지가 바로 다음 우선순위입니다.
지금 바로 보강해야 할 것
첫째, 일반 몹도 소량의 골드 드랍을 주는 것.
지금 스크린샷 기준으로는 트레이너만 골드를 주고, 야생은 EXP만 주는 구조로 보입니다.
이건 게임 감각상 꽤 중요합니다.
추천은 이 정도입니다.
야생 전투 승리: +3 ~ +10 Gold
포획 성공: Gold 없음 또는 아주 소량
트레이너 승리: 지금처럼 더 큰 보상 유지
둘째, 아이템 사용 UI/플로우입니다.
지금은 “사는 것”은 되지만 “쓰는 곳”이 없는 상태라 시스템이 반쪽입니다.
이건 다음 단계에서 반드시 붙여야 합니다.
우선순위는:
PartyScene에서 Potion 사용
BattleScene에서 Capture Crystal 사용
필요하면 World 메뉴에서 Inventory 열기
다음 단계 추천
지금은 새 지역을 더 늘리기보다 아이템과 경제를 완성하는 게 맞습니다.
즉 다음은 Phase 7: Inventory Usage, Wild Gold Drops, and Usable Item Flow가 좋습니다.
아래 프롬프트를 그대로 보내세요.
