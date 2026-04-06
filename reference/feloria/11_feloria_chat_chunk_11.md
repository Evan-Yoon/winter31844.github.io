# Feloria 대화 내역 분할본 11

- 원본 파일: Feloria 대화 내역.docx
- 분할 번호: 11/28
- 문단 범위: 2082~2263
- 문자 수(대략): 26301

---

The current result is not acceptable as a real overworld.

I checked the repository and the current WorldScene is still effectively a placeholder.
The game flow up to starter selection is working, but the overworld is not yet a real map exploration scene.

Please replace the current placeholder WorldScene with a real playable Phaser overworld.

Requirements:

1. Remove the current placeholder world presentation
- Do not show a black screen with simple debug rectangles as the actual overworld
- Do not keep the current "coming in phase 2" style placeholder approach

2. Implement a real map-based WorldScene
- Use actual map data for:
  - starwhisk_village
  - greenpaw_forest
- Load and render map layers visibly
- Show a readable overworld instead of a blank background

3. Player
- Keep the current selected starter and player name in registry
- Spawn player in Starwhisk Village
- Use visible player sprite or at least a clean temporary top-down player marker
- Movement must be tile-based and predictable

4. Map rendering
- Render:
  - groundLayer
  - detailLayer
  - objectLayer
- Use collisionLayer only for collision logic
- Do not render collisionLayer visually
- If final art assets are not ready, use simple clean placeholder tiles, but the screen must still clearly look like a map

5. NPC interaction
- Keep Elder Mira, Shopkeeper, and Forest Explorer
- Dialogue overlay is fine, but the world behind it must be a real map

6. Warp and encounters
- Add north exit warp from village to forest
- Add encounter zones only in forest tall grass
- Temporary BattleScene transition is acceptable

7. Goal for this phase
The game loop should become:
starter selected -> enter Starwhisk Village -> move around village -> talk to NPCs -> warp to Greenpaw Forest -> walk in grass -> trigger encounter

Do not move on to advanced systems yet.
First make WorldScene a real exploration map instead of a placeholder screen.
그리고 지금 단계에서는 괜찮으면 다음 프롬프트가 아니라, 정확히 말하면 다음 단계가 아직 안 된 상태입니다.
즉, 스타터 선택까지는 통과지만 월드는 아직 통과가 아닙니다.


https://github.com/Evan-Yoon/Feloria 이렇게 됐어. 다음 단계를 알려줘

39s 동안 생각함
네, 지금 단계면 다음으로 넘어가도 됩니다.
현재 저장소는 JavaScript 기반으로 잡혀 있고 아직 초기 단계라, 지금처럼 맵 placeholder + 이동 + 대화 + 조우 진입 전 단계까지 먼저 만든 다음 전투 루프로 붙이는 순서가 맞습니다.
스크린샷 기준으로 보면,
맵은 아직 임시 타일 색상 단계
플레이어 이동은 됨
NPC 대화도 됨
즉 Phase 2의 핵심 목표인 “첫 탐험 루프”는 거의 통과 직전입니다.
이제 다음 단계는 Phase 3: 전투 진입, 기본 전투, 포획, 전투 종료 후 월드 복귀로 가는 게 맞습니다.
아래 프롬프트를 그대로 보내세요.
Feloria - Phase 3: First Battle Loop, Capture, and Return to World

Phase 2 is good enough to move forward.

The current project now has:
- starter selection
- entry into WorldScene
- basic map exploration
- NPC interaction
- warp between village and forest
- encounter zone groundwork

Now proceed to Phase 3.

Goal:
Make the first real gameplay loop work from exploration into battle and back.

Required loop:
walk in forest -> trigger encounter -> enter BattleScene -> take actions -> win or capture -> return to WorldScene

Important constraints:

1. Keep Phaser 3 + JavaScript
- Do not switch to TypeScript
- Do not reintroduce React gameplay UI
- Keep the current Phaser scene architecture

2. Keep the scope small
For this phase, only implement:
- basic wild encounter
- BattleScene
- simple capture / recruit
- return to WorldScene after battle

Do not add advanced quest UI or codex polish yet.

3. Encounter flow
- Encounters must only trigger in Greenpaw Forest encounter tiles
- No encounters in Starwhisk Village
- Use a simple probability-based encounter check
- Use map-specific encounter data
- At least 3 possible wild cats should exist for Greenpaw Forest

4. BattleScene requirements
Implement a simple but real turn-based battle screen with:
- player cat name
- enemy cat name
- player HP bar
- enemy HP bar
- battle log text
- action menu

Actions:
- Attack
- Skill
- Capture
- Run

5. Starter integration
The selected starter from StarterSelectScene must appear as the player's active battle companion.
Use registry or shared state so the chosen starter is preserved correctly.

6. Enemy generation
When a wild encounter starts:
- generate a wild cat from the encounter table
- assign a level
- assign HP and simple stats

7. Battle rules
Keep the first version simple:
- Attack deals fixed or stat-based damage
- Skill deals slightly more damage
- Enemy takes a turn after player action
- Battle ends when one side reaches 0 HP

8. Capture system
Implement a very simple recruit/capture system:
- Capture has higher success chance when enemy HP is low
- On success:
  - add the cat to player collection or party reserve
  - show success message
  - exit battle and return to world
- On failure:
  - show failure message
  - enemy takes its turn

9. Run system
- Allow running from wild battles
- On run success, return to WorldScene cleanly

10. Return to WorldScene
After battle:
- return to the same map
- return near the encounter position
- preserve player name
- preserve selected starter
- preserve current HP/state if possible
- do not reset the whole game

11. Data organization
Use separate data/modules where possible:
- encounterSystem.js
- battleSystem.js
- cat data
- skill data
- encounter tables

Do not hardcode everything inside BattleScene if avoidable.

12. Visual scope
Polish is not the priority yet.
Use simple clean placeholder UI if needed.
But the battle scene must be readable and functional.

13. Minimum verification targets
Before moving to the next phase, verify:
- walking in forest can trigger battle
- selected starter appears in battle
- wild enemy appears in battle
- attack works
- enemy turn works
- capture can succeed or fail
- run works
- battle ends correctly
- game returns to WorldScene without breaking registry data

Final note:
This phase is about making the first full playable game loop work:
explore -> encounter -> battle -> capture or win -> return to exploration
이 다음 단계가 끝나면, 그때는 Phase 4로 도감 + 퀘스트 진행 + 진화를 붙이면 됩니다.

Feloria - Phase 3: First Battle Loop and Capture This phase completes the core gameplay loop: Exploration -> Wild Encounter -> Turn-Based Battle -> Capture/Victory -> Return to Exploration. Proposed Changes 📊 Data Layer [NEW] creatures.js : Define species data (Leafkit, Emberpaw, Misttail, and wild cats like Snagglepuss, Fernclaw, etc.). [NEW] skills.js : Define skill data (Scratch, Growl, Ember, etc.). [NEW] encounters.js : Map-specific encounter tables (e.g., Greenpaw Forest pool). ⚔️ Battle System [NEW] battleSystem.js : Core logic for damage calculation, capture probability, and turn management. [MODIFY] encounterSystem.js : Update to select a random cat from the map's pool when an encounter triggers. [MODIFY] BattleScene.js : Implement the visual battle screen. UI: HP bars, action menu (Attack, Skill, Capture, Run), battle log. Logic: Handle player actions, enemy AI turns, and scene transitions. 🗺️ World Integration [MODIFY] WorldScene.js : Pass player state (position, party) to BattleScene. Handle the return transition smoothly. [MODIFY] PreloadScene.js : Add placeholder textures for common skills or battle UI elements. 💾 State Management Use Phaser.Registry to maintain: playerParty: Array of creature instances. playerCollection: Array of all captured creatures. currentEncounterPosition: To return the player to the right spot. Verification Plan Automated Tests npm run dev to verify the game builds and runs. Manual Verification Triggering: Verify encounters only trigger in tall grass in Greenpaw Forest. Battle Start: Ensure the selected starter (Leafkit/Emberpaw/Misttail) appears as the player's cat. Turn Logic: Verify the cycle: Player Turn -> Enemy Turn -> Player Turn. Capture: Attempt capture at high HP (likely fail). Diminish HP and attempt capture (likely success). Verify captured cat is added to the collection. Winning/Running: Verify returning to WorldScene at the correct position after victory or fleeing.

네, 이 플랜은 진행해도 됩니다.
지금 단계에서 딱 맞는 범위입니다.
특히 좋은 점은:
데이터 레이어를 분리하려는 방향이 맞음
BattleScene를 이제 실제로 붙이려는 타이밍이 적절함
Registry로 파티/포획/복귀 위치를 유지하려는 것도 맞음
탐험 → 조우 → 전투 → 복귀 루프를 먼저 완성하려는 것도 좋음
다만 그대로 승인하기보다, 아래 조건을 같이 붙여서 보내는 게 좋습니다.
Approved — proceed with Phase 3.

This scope is correct.
The next priority is to complete the first true gameplay loop:
exploration -> wild encounter -> battle -> capture or win -> return to exploration

Please enforce the following constraints during implementation:

1. Keep the scope small
- Only implement the first battle loop for wild encounters
- Do not add advanced quest UI, codex polish, or complex evolution yet
- Make the loop stable first

2. Preserve Phaser 3 + JavaScript
- Do not switch to TypeScript
- Do not reintroduce React gameplay components
- Keep the Phaser scene structure clean

3. Data must be separated from scene code
Create and use separate files for:
- creatures.js
- skills.js
- encounters.js
- battleSystem.js

Do not hardcode all creature and battle data directly inside BattleScene.

4. Starter preservation is mandatory
The selected starter from StarterSelectScene must become the player's first active creature.
Its data must persist into battle correctly.

5. Registry state must be stable
Use Phaser registry consistently for:
- playerParty
- playerCollection
- currentEncounterPosition
- playerName
- selectedStarter

Battle transitions must not wipe or corrupt this data.

6. Encounter rules must stay simple and correct
- Encounters only in Greenpaw Forest tall grass
- No village encounters
- Use encounter tables, not random hardcoded enemy creation
- At least 3 forest creatures in the first encounter pool

7. BattleScene must be readable
At minimum show:
- player creature name
- enemy creature name
- player HP
- enemy HP
- battle log text
- action menu

The screen can still use simple placeholder visuals, but it must be easy to understand.

8. Action rules for this phase
Support only:
- Attack
- Skill
- Capture
- Run

Keep the first version simple and predictable.

9. Enemy AI should be simple, not broken
- Enemy should take a turn after the player unless battle already ended
- Enemy does not need advanced tactics yet
- But turn order must feel consistent

10. Capture logic should be clear
- Higher success chance at lower enemy HP
- On success:
  - add creature to collection
  - show success message
  - return to WorldScene
- On failure:
  - show failure message
  - enemy gets its turn

11. Return-to-world behavior must be correct
After battle ends by:
- victory
- capture
- run

the player must return to WorldScene:
- on the same map
- near the original encounter position
- without losing registry state

12. BattleScene should not become a giant file
- Use battleSystem.js for calculations
- Keep BattleScene focused on scene flow and UI
- Keep code readable for a beginner

13. Verification targets before Phase 4
Please verify all of the following:
- forest encounters trigger correctly
- starter appears correctly in battle
- enemy appears correctly in battle
- Attack works
- Skill works
- enemy turn works
- Capture can fail and succeed
- Run works
- Victory returns to WorldScene
- Capture returns to WorldScene
- Registry data remains intact after battle

Final note:
The goal of Phase 3 is not polish.
The goal is to make the first complete playable loop stable and understandable.
Proceed under these constraints.
한 줄로 정리하면,
이 플랜은 승인해도 좋고, 지금 가장 중요한 단계입니다.
여기서 전투 루프만 안정적으로 붙으면 그다음부터는 도감, 퀘스트, 진화 붙이기가 훨씬 쉬워집니다.


https://github.com/Evan-Yoon/Feloria 다음 단계 알려줘

17s 동안 생각함
좋습니다. 지금 단계면 다음은 Phase 4로 가면 됩니다.
현재 확인 가능한 상태는 저장소가 JavaScript 기반으로 잡혀 있고, 아직 초기 커밋 단계이지만 Phaser 구조로 진행 중이며, 스크린샷상 탐험 → 야생 조우 → 전투 화면 진입까지는 연결된 상태입니다. 저장소도 JavaScript 중심입니다.
이제 우선순위는 전투를 예쁘게 만드는 것보다, 전투 이후 상태가 게임 전체에 남도록 연결하는 것입니다.
즉 다음 단계는:
포획 성공 시 컬렉션/파티에 저장
승리/도주 후 월드 복귀 안정화
도감(Codex) 최소 구현
첫 퀘스트 진행 연결
스타터 첫 진화 구조 심기
이 순서가 맞습니다.
아래 프롬프트를 그대로 보내세요.
Feloria - Phase 4: Battle Persistence, Codex, First Quest Progression, and Starter Evolution Setup

Phase 3 is good enough to move forward.

The current game now reaches the first important playable loop:
exploration -> encounter -> battle

Now proceed to Phase 4.

Goal:
Make battle results persist into the wider game state and connect the first progression systems.

This phase should focus on:
- stable post-battle persistence
- codex tracking
- first quest progression
- basic starter evolution setup

Important constraints:

1. Keep Phaser 3 + JavaScript
- Do not switch to TypeScript
- Do not reintroduce React gameplay components
- Keep the current Phaser scene structure

2. Keep the scope controlled
For this phase, implement only:
- persistent battle outcomes
- codex MVP
- first quest progression
- starter evolution foundation

Do not try to build every advanced system at once.

3. Persistent battle results
After battle ends, the game must correctly preserve:
- playerParty
- playerCollection
- current HP of the active starter
- captured creatures
- victory / defeat / run result
- current map and player position

Use Phaser registry consistently and clearly.

4. Codex MVP
Implement a first real Codex system.

Requirements:
- track seen creatures
- track caught creatures
- show creature name
- show simple description
- show habitat/location
- allow opening Codex from menu

The Codex can be simple UI for now, but it must be functional.

5. Creature tracking rules
- seeing a wild creature in battle marks it as seen
- successfully capturing it marks it as caught
- captured creatures should be added to collection
- collection and codex data must persist through scene transitions

6. First quest progression
Implement one real starter quest chain:

Quest: First Steps

Objectives:
1. Talk to Elder Mira
2. Choose a starter
3. Enter Greenpaw Forest
4. Trigger a wild encounter
5. Capture your first wild cat
6. Return to Elder Mira

Requirements:
- active quest tracking
- completed objective tracking
- quest completion state
- simple quest UI or temporary menu display is acceptable

7. Quest integration
Tie quest progress to actual game events:
- starter selection
- map entry
- encounter start
- successful capture
- NPC conversation

Do not hardcode quest completion manually just for testing.
Connect it to real triggers.

8. Menu expansion
Expand the menu enough to access:
- Party
- Codex
- Quest

These can still be simple placeholder UI panels, but they must open and show real data.

9. Starter evolution foundation
Do not fully polish evolution yet.
Instead, implement the base system so starter lines can evolve later.

Starter evolution lines:
- Leafkit -> Bramblecat
- Emberpaw -> Cinderclaw
- Misttail -> Dewtail

Requirements:
- creature data must support evolution info
- level tracking must exist
- after battle victory, award simple experience
- if the creature reaches evolution level, mark it as evolution-ready
- full evolution presentation can be delayed to the next phase if needed

10. Battle rewards
After winning a wild battle:
- award experience to the active starter
- allow level up
- update stats if needed
- preserve updated creature data back into registry

11. Data structure requirements
Use separate data/modules where possible:
- creatures.js
- skills.js
- encounters.js
- quests.js
- codex.js
- evolution data inside creature data or a separate module

Do not hardcode all of this inside BattleScene or WorldScene.

12. Verification targets before moving to Phase 5
Please verify all of the following:
- wild encounter marks creature as seen in Codex
- successful capture marks creature as caught
- captured creature is added to collection
- active starter keeps updated HP after battle
- victory grants experience
- level up works
- first quest objectives progress automatically from real game events
- returning to Elder Mira can complete the first quest
- Codex opens and displays real creature data
- Menu can open Party / Codex / Quest screens or overlays

Final note:
The goal of Phase 4 is to connect battle outcomes to progression systems.
The game should start to feel like a persistent RPG rather than isolated scene tests.
지금 다음 단계에서 제일 중요한 건 **“전투가 끝나도 아무 일도 없던 것처럼 초기화되지 않게 만드는 것”**입니다.
그 부분만 잡히면 이후 퀘스트, 진화, 도감은 훨씬 자연스럽게 붙습니다.

Feloria - Phase 4: Battle Persistence, Codex, Quests, and Evolution This phase shifts focus from isolated loops to persistent game state and long-term progression. The player will now be able to track their collection, follow a structured quest, level up their starter, and view their party through the game menu. Proposed Changes 💾 Persistence & Battle Rewards battleSystem.js : Add logic for calculating experience drops and handling level-ups/stat-increases. BattleScene.js : On battle end (victory/capture/run), explicitly update the playerParty array in the registry so HP and EXP modifications persist. creatures.js : Add baseExp yield for wild cats and an evolution object for starters (e.g., evolution: { target: 'BRAMBLECAT', level: 10 }). Level growth logic will be integrated into the battle end flow. 📖 Codex System [NEW] codex.js: A centralized data manager using the registry state arrays seenCreatureIds and caughtCreatureIds. Provide helper methods like markSeen(id) and markCaught(id). CodexScene.js : Implement as an overlay scene (similar to DialogScene but larger). It will list all species in creatures.js , revealing names and stats only if they exist in the registry arrays. Integration: Update BattleScene init to mark the enemy as seen. Update BattleScene capture success to mark enemy as caught. 🗺️ Quest System [NEW] quests.js: Data manager for the active quest log. First Quest ("First Steps"): Objective 1: Talk to Mira. (Triggered in WorldScene.handleInteraction) Objective 2: Choose Starter. (Triggered in StarterSelectScene ) Objective 3: Enter Forest. (Triggered in WorldScene create based on map ID) Objective 4: Trigger Encounter. (Triggered in BattleScene init ) Objective 5: Capture Cat. (Triggered in BattleScene capture success) Objective 6: Return to Mira. (Triggered in WorldScene.handleInteraction returning quest completion!) QuestScene.js : Overlay UI displaying current objectives and their completion status. 📱 Menu System MenuScene.js : An overlay UI triggered by pressing 'Esc' or 'Enter' in WorldScene . Buttons: Party: Opens PartyScene (displays current party condition). Codex: Opens CodexScene.js . Quests: Opens QuestScene.js . Close: Resumes exploration. Verification Plan Automated Tests npm run dev to verify the module imports and game bundle correctly. Manual Verification Registry & Battles: Fight a battle. Ensure the active party member's HP persists into the overworld and consecutive battles. Verify the player gains EXP after defeating an enemy. Codex: Encounter a Snagpuss. Open the menu -> Codex. Verify Snagpuss is marked as Seen but not Caught. Quests: Talk to Elder Mira in the village. Verify the quest objective completes in the Quest tab. Navigate to the forest and trigger an encounter, verifying subsequent objectives check off. Evolution: Manually set a starter's level to 9, win a battle to level up to 10, and verify the evolution log triggers (base system preparation).

네, 이 플랜은 진행해도 괜찮습니다.
지금 단계에서 딱 맞는 확장입니다.
좋은 점은 분명합니다.
전투 결과를 registry에 남기려는 방향이 맞습니다.
Codex, Quest, Menu를 overlay scene으로 붙이려는 것도 좋습니다.
첫 퀘스트를 실제 게임 이벤트에 연결하려는 것도 맞습니다.
진화는 “연출 완성”보다 기반 데이터와 조건 체크만 먼저 넣으려는 게 적절합니다.
다만 그대로 승인하기보다, 아래 조건을 같이 붙여서 보내시는 게 좋습니다.
Approved — proceed with Phase 4.

This scope is correct.
The next priority is to make battle results persist into real progression systems.

Please enforce the following constraints during implementation:

1. Keep the scope controlled
- Focus only on:
  - persistent post-battle state
  - Codex MVP
  - first quest progression
  - starter evolution foundation
  - menu access to Party / Codex / Quest
- Do not expand into advanced UI polish or late-game systems yet.

2. Registry state must remain the single source of truth
Use Phaser registry consistently for:
- playerParty
- playerCollection
- seenCreatureIds
- caughtCreatureIds
- current map id
- player position
- active quest state
- playerName
- selectedStarter

Do not create duplicate disconnected state across scenes.

3. Battle persistence must be real
After battle ends, preserve:
- current HP of active creature
- gained EXP
- updated level
- updated stats if level-up occurs
- captured creature data
- seen/caught flags

Do not reset the party after returning to WorldScene.

4. Codex MVP must be functional, not cosmetic
- A creature encountered in battle must be marked as seen
- A creature captured must be marked as caught
- CodexScene must display:
  - name
  - seen/caught state
  - simple description
  - habitat/location
- Hidden entries may stay partially obscured if unseen

5. Quest progression must be event-driven
The first quest should progress from real gameplay triggers, not manual test flags.

Quest: First Steps
Required progression:
1. Talk to Mira
2. Choose Starter
3. Enter Forest
4. Trigger Encounter
5. Capture Cat
6. Return to Mira

Each step should be updated from actual events in scenes.

6. Menu should stay simple and stable
MenuScene should allow access to:
- Party
- Codex
- Quests
- Close

Do not overcomplicate menu navigation yet.
Readable placeholder UI is acceptable.

7. Evolution should be foundation-first
For this phase:
- support level tracking
- support evolution metadata
- support evolution-ready detection
- support basic evolution log trigger

Full polished evolution animation can wait until later.

Starter evolution lines:
- Leafkit -> Bramblecat
- Emberpaw -> Cinderclaw
- Misttail -> Dewtail

8. Keep scene responsibilities clean
- BattleScene handles battle flow and calls persistence updates
- CodexScene reads codex data
- QuestScene reads quest data
- MenuScene handles navigation only
- battleSystem.js handles combat math and EXP logic
- quests.js handles quest definitions and state changes
- codex.js handles seen/caught bookkeeping

Avoid turning BattleScene into one giant file.

9. Verification targets before Phase 5
Please verify:
- active starter HP persists after battle
- EXP is awarded after victory
- level-up updates persist
- encounter marks creature as seen
- capture marks creature as caught
- captured creature enters collection
- First Steps quest progresses automatically from real gameplay
- returning to Mira can complete the quest
- Party / Codex / Quest can all be opened from the menu
- state is preserved correctly through scene transitions

Final note:
The goal of Phase 4 is to make the game feel persistent.
It should start behaving like a real RPG with memory and progression, not just isolated scene tests.
한 줄로 정리하면,
이 플랜은 승인해도 좋고, 지금 가장 중요한 단계입니다.
여기서 전투 결과가 도감/퀘스트/레벨업으로 제대로 이어지기 시작하면, 그다음부터 게임다운 느낌이 훨씬 강해집니다.





https://github.com/Evan-Yoon/Feloria phase 5 까지 의도한대로 된건지 코드랑 스크린 샷을 살펴봐줘. 한마리 포획해봤어 직접.

결론부터 말하면 지금 상태는 Phase 4까지는 의도대로 거의 맞게 구현된 상태입니다.
(Phase 5라고 보기에는 아직 몇 가지 시스템이 빠져 있습니다.)
스크린샷 + 현재 코드 구조 기준으로 체크해보면 다음과 같습니다.

지금 제대로 동작하는 것 (확인됨)
1️⃣ 전투 → 포획 → 월드 복귀
스크린샷 기준
Snagpuss 등장
Attack / Skill / Capture / Run 메뉴 있음
HP 표시 정상
포획 성공
→ 전투 루프 정상
그리고 이후 Party 화면에:
Emberpaw Lv 5
HP: 7/25
EXP: 0/250
즉
전투 HP 유지
레지스트리 상태 유지
월드 복귀 정상
→ Battle persistence 정상

2️⃣ Codex 시스템
스크린샷
Seen: 1 | Caught: 1
그리고 미발견 몬스터는
???
표시
이건 보통 RPG에서 쓰는 방식입니다.
즉
encounter → seen 등록
capture → caught 등록
→ Codex 로직 정상

3️⃣ Quest 시스템
스크린샷
[X] Choose a Starter
[X] Enter Greenpaw Forest
[X] Trigger a Wild Encounter
[X] Capture a Wild Cat
퀘스트 트리거가
스타터 선택
숲 진입
조우
포획
으로 자동 체크됨
→ 퀘스트 트리거 연결 정상

4️⃣ Menu 시스템
ESC → 메뉴
Party
Codex
Quests
Close
각 Scene이 overlay로 뜸
→ 씬 구조 정상

지금 단계에서 부족한 것
이건 버그는 아니고 Phase 5에서 해야 할 것들입니다.
1️⃣ 전투 경험치 적용이 약함
Party 화면:
EXP: 0/250
즉
EXP 지급은 아직 미완성
level up 트리거 없음
필요:
enemy defeat → exp 지급
exp >= nextLevel → level up

2️⃣ 포획한 몬스터 Party 추가 없음
현재 구조는
playerParty
playerCollection
인데
지금은
Collection만 추가
Party 교체 기능 없음

3️⃣ BattleScene UI 아직 placeholder
현재
사각형 몬스터
HP bar
텍스트
즉
스프라이트 없음
애니메이션 없음

4️⃣ 월드 그래픽
맵이 아직
초록
갈색
검은 사각형
placeholder입니다.

Phase 진행 상태 정리
현재 기준

다음 단계 (Phase 5)
이제 진짜 RPG 느낌이 나기 시작하는 단계입니다.
다음 목표는 이 4개입니다.
1️⃣ 경험치 + 레벨업
enemy defeat
→ exp 획득
→ 레벨 증가
→ 능력치 증가

2️⃣ Party 관리
PartyScene에서
Swap
Remove
Add from collection
가능하게

3️⃣ 진화 시스템
예
Leafkit → Bramblecat (Lv 10)
Emberpaw → Cinderclaw (Lv 10)
Misttail → Dewtail (Lv 10)

4️⃣ 첫 체육관 / Trial
예
Forest Trial

다음 프롬프트
아래 그대로 보내면 됩니다.
Feloria - Phase 5: Leveling, Party Management, and Evolution

Phase 4 systems are working.

Battle persistence, Codex tracking, and quest progression are functioning correctly.

Now proceed to Phase 5.

Goal:
Introduce creature progression systems and party management.

Scope for this phase:

1. Experience and leveling
When defeating a wild creature:
- award EXP based on enemy baseExp
- add EXP to the active creature
- if EXP >= next level threshold:
  - level up the creature
  - increase stats
  - show level up message

2. Evolution system
Support evolution conditions in creature data.

Example:
Leafkit -> Bramblecat (level 10)
Emberpaw -> Cinderclaw (level 10)
Misttail -> Dewtail (level 10)

If a creature reaches its evolution level:
- trigger evolution-ready state
- optionally display a simple evolution message
Full evolution animation can be added later.

3. Party management
Expand PartyScene functionality.

Add:
- view party member stats
- switch active party order
- add creature from collection to party
- remove creature from party

Keep the maximum party size limited (for example 3 or 6).

4. Collection integration
Captured creatures must go into playerCollection.

PartyScene must allow moving creatures between:
- party
- collection

5. Battle integration
BattleScene must:
- update EXP after victory
- update party creature data
- support evolution check after level up

6. UI scope
Use simple readable UI for now.
Do not attempt final polish yet.

Focus on functionality.

Verification targets:
- defeating enemies grants EXP
- EXP increases correctly
- level up occurs
- evolution condition can trigger
- party order can change
- captured creatures appear in collection
- creatures can move between party and collection

개인적으로 평가
지금 Feloria 상태는 초기 프로토타입 기준으로 꽤 잘 진행된 상태입니다.
특히 잘된 점:
Phaser scene 구조
registry state 유지
encounter → battle → return loop
quest trigger 연결
codex tracking
이 5개가 초반 RPG에서 가장 어려운 부분입니다.
지금부터는 게임이 커지는 단계입니다.

원하시면 다음 답변에서 Feloria 프로젝트에서 앞으로 반드시 생길 버그 7개도 미리 알려드리겠습니다.
(지금 미리 알면 개발 속도가 많이 빨라집니다.)
