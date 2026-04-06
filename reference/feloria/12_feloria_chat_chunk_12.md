# Feloria 대화 내역 분할본 12

- 원본 파일: Feloria 대화 내역.docx
- 분할 번호: 12/28
- 문단 범위: 2264~2372
- 문자 수(대략): 23888

---

Feloria - Phase 5: Leveling, Party Management, and Evolution This phase focuses on creature progression and managing the player's team. We will polish the leveling system, implement the actual evolution state change, and build a robust PartyScene to organize captured creatures. Proposed Changes 📈 Leveling & Evolution battleSystem.js : Verify calculateExp and gainExp . When gainExp sets readyToEvolve = true, we need a new method evolveCreature(creature) that transforms the creature into its evolved form (e.g., Leafkit -> Bramblecat) by updating its id, name, base stats, and calculating its new current stats. BattleScene.js : After victory, if the creature leveled up and is readyToEvolve, trigger the evolveCreature method. Display an evolution message in the battle log before ending the battle. Make sure the updated evolved state is persisted to playerParty and playerCollection. 👥 Party Management & Collection PartyScene.js : Overhaul the scene to support interactivity. Party View: Show up to 3 creatures in the active party. Display full stats (HP, Attack, Defense, Level, EXP). Collection View: Show a scrollable list of creatures in playerCollection that are not currently in the active party. Actions: Click a party member to view options (e.g., "Make Active", "Remove from Party"). Click a collection member to view options (e.g., "Add to Party"). Constraints: Active creature (index 0) is the one sent into battle. "Make Active" swaps a creature to index 0. Max party size is 3. Cannot add more if full. Cannot remove the last creature from the party. 💾 Persistence Validation Ensure that modifying the party in PartyScene updates playerParty in the Phaser registry. When saving an evolved creature, ensure the actual instance reference in playerCollection is properly updated (or since playerParty items are references, they might update automatically, but we must verify). Verification Plan Automated Tests npm run dev to verify scene logic and UI rendering. Manual Verification Leveling & Evolution: Modify StarterSelectScene or a debug trigger to give the starter enough EXP to hit Level 10. Enter a battle, win, and observe the "Leafkit is evolving!" message. Return to the overworld and open the Party menu to verify the starter is now Bramblecat. Party Management: Capture 2-3 wild cats. Open Party Menu. Verify captured cats are either in the party (if space) or collection. "Remove from Party" to move a cat to the collection. "Add to Party" to move a cat from collection to party. "Make Active" to change the first cat. Enter battle and verify the new active cat is sent out. 이렇게 plan을 주네 아니그리고 반드시 생길 버그 7개가 안생기게끔 처음부터 알려줘야지

네, 플랜 방향은 맞습니다.
다만 그대로 진행시키면 높은 확률로 터질 버그들이 있습니다. 그걸 먼저 막는 조건까지 같이 보내는 게 맞습니다.
아래 문구를 안티그래비티에 그대로 보내세요.
플랜 승인 + Phase 5 진행 조건 + 미리 막아야 할 핵심 버그 방지 규칙까지 합친 버전입니다.
Approved — proceed with Phase 5.

The direction is correct:
- leveling
- evolution
- party management
- collection management
- persistence validation

However, implement Phase 5 under the following constraints so common progression bugs do not appear.

--------------------------------
PHASE 5 GOAL
--------------------------------

The goal of this phase is:
- real EXP gain after battle
- level up
- actual evolution state change
- interactive PartyScene
- stable transfer between party and collection
- persistent registry updates

Do not expand into late-game systems yet.
Focus on making progression stable.

--------------------------------
REQUIRED IMPLEMENTATION RULES
--------------------------------

1. Keep Phaser 3 + JavaScript
- Do not switch to TypeScript
- Do not reintroduce React gameplay components
- Keep the current Phaser scene architecture

2. Keep data-driven creature progression
Use creature data for:
- id
- name
- level
- exp
- stats
- evolution target
- evolution level

Do not hardcode starter-only evolution logic inside BattleScene.

3. Evolved creatures must become real updated creature objects
When evolution happens:
- update id
- update name
- update base stats
- recalculate max stats
- preserve current level
- preserve exp
- preserve current HP proportion if possible
- persist the evolved creature back into both playerParty and playerCollection correctly

4. Party management must be safe
Rules:
- maximum party size = 3
- active creature is always index 0
- cannot remove the last creature from party
- add/remove between party and collection must never duplicate creatures
- make active must swap order instead of cloning

5. Registry remains the single source of truth
Use Phaser registry consistently for:
- playerParty
- playerCollection
- seenCreatureIds
- caughtCreatureIds
- playerName
- selectedStarter

Do not create disconnected local copies that are never written back.

--------------------------------
BUG PREVENTION RULES
--------------------------------

These bugs are likely to happen if not handled carefully.
Prevent them from the start.

BUG 1 — Duplicate creature entries
Problem:
A captured or moved creature can appear in both party and collection multiple times.

Prevent it by:
- giving every creature instance a unique instanceId
- moving instances between party and collection by instanceId
- never clone on add/remove unless intentionally creating a new instance
- before inserting, check whether that instanceId already exists in the target list

BUG 2 — Evolution updates only one list
Problem:
A creature evolves in party but still shows old form in collection, or vice versa.

Prevent it by:
- updating the same creature instance consistently
- or if immutable updates are used, update both playerParty and playerCollection using instanceId matching
- never assume both arrays magically stay in sync

BUG 3 — EXP/level up not persisting after battle
Problem:
BattleScene shows level up, but returning to WorldScene resets the creature.

Prevent it by:
- writing updated party data back into registry before ending battle
- ensuring return-to-world flow reads the latest registry state
- not keeping stale cached references in WorldScene

BUG 4 — Current HP becomes invalid after level up/evolution
Problem:
HP goes above max HP, becomes negative, or resets unexpectedly.

Prevent it by:
- after stat recalculation, clamp currentHp to maxHp
- preserve HP ratio if evolving, or at minimum clamp safely
- always validate:
  currentHp = Math.max(1, Math.min(currentHp, maxHp))
  unless the creature actually fainted

BUG 5 — PartyScene actions corrupt order
Problem:
Make Active, Remove, and Add to Party can break party order or leave null/undefined slots.

Prevent it by:
- always rebuild party arrays cleanly after each action
- validate party length after every mutation
- ensure index 0 always exists if party is non-empty
- never leave sparse arrays

BUG 6 — Removing last party member soft-locks the game
Problem:
Player ends up with no usable active creature and cannot enter battle correctly.

Prevent it by:
- block removing the final party member
- if only one creature remains in party, disable Remove action
- always validate party.length >= 1

BUG 7 — Evolution triggers multiple times
Problem:
A creature can re-trigger the same evolution repeatedly after every battle.

Prevent it by:
- after evolving, replace evolution metadata appropriately
- clear readyToEvolve after evolution
- ensure evolved form does not still meet the previous form’s trigger
- evolution check should happen once per battle resolution, not repeatedly during UI updates

--------------------------------
PHASE 5 FUNCTIONAL REQUIREMENTS
--------------------------------

1. EXP and leveling
After winning a battle:
- award EXP using enemy baseExp
- add EXP to active creature
- level up if threshold reached
- update stats
- persist results

2. Evolution
If a creature reaches its evolution level:
- trigger evolution-ready
- perform actual evolution state change
- show a readable evolution message in battle log
- persist evolved form to registry

3. PartyScene overhaul
Implement:
- Party View
- Collection View
- Add to Party
- Remove from Party
- Make Active

Rules:
- max party size = 3
- active creature = index 0
- cannot remove last party creature

4. Collection integration
Captured creatures must go into collection.
If party has room, they may also be placed into party according to the current design.
But do not allow duplication bugs.

--------------------------------
VERIFICATION REQUIREMENTS
--------------------------------

Before moving to the next phase, verify all of the following:

1. Winning a battle gives EXP
2. EXP persists after returning to overworld
3. Level up occurs correctly
4. Evolution changes the actual creature form
5. Evolved creature is correct in PartyScene
6. Evolved creature is correct in Collection
7. Captured creatures do not duplicate unexpectedly
8. Add to Party works
9. Remove from Party works
10. Make Active works
11. Active creature at index 0 is the one used in battle
12. Last party member cannot be removed
13. Party and collection remain valid after repeated operations

--------------------------------
FINAL NOTE
--------------------------------

The goal of Phase 5 is not polish.
The goal is to make progression stable and prevent future state corruption.

Proceed under these constraints.
그리고 말씀하신 부분도 맞습니다.
원래는 “나중에 생길 버그”가 아니라 지금 단계에서 미리 방지 규칙을 같이 넣는 게 맞습니다.
그래서 앞으로는 새 phase 들어갈 때마다 이런 식으로 보시면 됩니다.
기능 요구
상태 저장 규칙
씬 간 데이터 전달 규칙
배열/인스턴스 중복 방지 규칙
검증 항목
이 5개를 같이 보내야 덜 망가집니다.
원하시면 다음엔 제가 Phase 6 들어가기 전에 반드시 체크해야 할 구조 점검표도 미리 만들어드리겠습니다.





두마리 만났고 한마리는 잡고 한마리는 죽였어. https://github.com/Evan-Yoon/Feloria 경험치바가 안보여서 경험치를 받았는지는 모르겠어. 그리고 체력이 없어서 10레벨까지 레벨업을 할 여유가 없어서 진화가 됐는지도 검증을 하지 못했어. 구현이 제대로 됐는지 코드 직접 보면서 다음 단계 어떻게 할지 확인해줘.

1m 9s 동안 생각함
스크린샷 기준으로는 Phase 5가 “부분 완료” 상태입니다.
정확히 말하면:
Party 관리: 동작함
Quest 진행: 동작함
Codex seen/caught 집계: 동작함
EXP / 레벨업 / 진화: 화면만으로는 검증 불가, 코드도 GitHub 쪽 응답 제한 때문에 깊게 확인하지 못함
저장소 루트는 아직 정리 전 상태입니다. README가 기본 Vite 템플릿 그대로이고, 저장소 히스토리도 아주 초기 단계입니다.
지금 스크린샷에서 확인되는 건 이렇습니다.
확인된 것
PartyScene
Make Active가 실제로 동작해서 Thistlefur를 맨 앞으로 올린 상태가 보입니다.
파티 수 제한도 2/3으로 표시됩니다.
따라서 파티 순서 변경 로직은 살아 있습니다.
Codex
Seen: 2 | Caught: 1로 보이므로, 한 마리 조우 + 한 마리 포획 + 다른 한 마리 처치가 도감 집계에 반영된 것으로 보입니다.
다만 이름/설명 해금 UI는 아직 숨김(???) 중심이라 MVP 수준입니다.
Quest
First Steps가 전부 체크되어 있으므로,
Mira 대화
스타터 선택
숲 진입
조우
포획
Mira 복귀
흐름은 연결된 것으로 보입니다.
아직 검증 안 된 것
EXP 지급
Party 화면에 EXP 수치가 보이지만, “전투 후 증가했는지”는 스크린샷만으로 판단이 어렵습니다.
특히 스크린샷상 Emberpaw Lv 5 / EXP 0/250로 보인다면, EXP가 아직 적용되지 않았거나, 적용 후 초기화되는 버그가 있을 수 있습니다.
레벨업
레벨업 메시지, 스탯 증가, 다음 전투 반영 여부가 확인되지 않았습니다.
진화
10레벨 도달 자체를 못 했으니 아직 미검증입니다.
그래서 “구현되었다”고 보기엔 이릅니다.
또 한 가지 중요한 점은, 저장소 루트 기준으로는 아직 React + TypeScript + Vite 기본 README가 남아 있고, Phaser 프로젝트로 완전히 정리된 문서 상태는 아닙니다. 그래서 구조상도 아직 정리 단계라고 보는 게 맞습니다.
판단
지금 상태를 단계로 나누면:
Phase 4: 사실상 완료
Phase 5: 절반 정도 완료
Party / Codex / Quest 쪽은 됨
EXP / 레벨업 / 진화는 검증용 장치가 없어서 아직 불확실
즉, 다음 단계는 바로 Phase 6로 가는 게 아니라, Phase 5.5: progression verification and debugging을 한 번 거치는 게 맞습니다.
아래 문구를 그대로 보내세요.
Feloria - Phase 5.5: EXP, Level-Up, Evolution Verification and Debug Tools

Phase 5 is only partially verified.

The following systems appear to be working from the current build:
- PartyScene active swap
- basic collection / captured creature tracking
- quest progression for First Steps
- Codex seen/caught counts

However, EXP gain, level-up persistence, and evolution are not yet verified in a reliable way.

Do not move on to the next major feature phase yet.
First add proper progression verification and debugging support.

Goals for this phase:
1. Confirm EXP is actually awarded after battle
2. Confirm EXP persists after returning to WorldScene
3. Confirm level-up works correctly
4. Confirm evolution works correctly
5. Add temporary debug tools to test progression quickly

Required implementation:

1. Visible EXP feedback
- In BattleScene, after victory, show:
  - EXP gained
  - current EXP after gain
  - whether level up happened
- In PartyScene, clearly display:
  - current level
  - current EXP
  - EXP needed for next level

2. Debug progression controls
Add temporary debug actions accessible only in development:
- Give active creature +50 EXP
- Give active creature +200 EXP
- Set active creature to level 9
- Fully heal active creature

These can be simple temporary buttons or keyboard shortcuts.
They do not need final UI polish.

3. Level-up validation
After EXP gain:
- update level if threshold reached
- update stats
- persist changes back to registry
- confirm updated values remain after scene transitions

4. Evolution validation
When a starter reaches evolution level:
- trigger evolution message
- replace creature with evolved form
- update party and collection consistently
- preserve correct level / EXP / HP rules
- prevent repeated evolution triggering

5. Add temporary battle result summary
After battle, before returning to WorldScene, show a short summary:
- Victory / Capture / Run
- EXP gained
- Level up? yes/no
- Evolution? yes/no

6. Party / collection consistency checks
Ensure:
- no duplicate creature instance appears in both party and collection incorrectly
- evolved creature form is reflected in both places if applicable
- active creature at index 0 remains correct after battle updates

7. Verification targets
Before Phase 6, verify all of the following:
- defeating an enemy increases EXP
- EXP persists after returning to overworld
- level-up occurs correctly
- stat increases persist
- setting a starter to level 9 and winning a battle can trigger level 10
- evolution actually changes the creature form
- evolved form is visible in PartyScene
- evolved form is visible in collection / codex where appropriate

Final note:
This phase is specifically for progression verification and debugging.
Do not prioritize new content yet.
First make EXP, leveling, and evolution fully testable and reliable.
지금은 새 기능 추가보다 EXP / 레벨업 / 진화가 실제로 되는지 눈으로 확인 가능한 상태를 만드는 게 먼저입니다.

Feloria - Phase 5.5: Progression Verification & Debug Tools This phase focuses on ensuring that Phase 5's progression systems (EXP, Level Up, Evolution) are robust, visually confirmed by the player, and easily testable before moving on to new content. Proposed Changes 🔍 Visible Progression Feedback BattleScene.js : Create a custom UI summary panel that appears after the battle ends, but before returning to the WorldScene . It will display: Battle Result (Victory / Capture / Fled) EXP gained Total EXP now Whether a level up occurred Whether an evolution occurred PartyScene.js : Update the creature cards to explicitly show the required EXP for the next level (e.g., EXP: 30 / 50) so progress is mathematically clear. 🛠️ Debug Progression Controls PartyScene.js : Add temporary debug keyboard listeners accessible while the menu is open: Press 1: Give the active creature (Index 0) +50 EXP. Press 2: Give the active creature (Index 0) +200 EXP (force multi-level or instant evolution). Press 3: Set the active creature's level to 9 and EXP to 400 (pre-evolution state). Press 4: Fully heal the active creature. These controls will call the battleSystem.gainExp logic manually and refresh the UI to prove that stats and evolution checks work exactly as they would in battle. 💾 Safe Evolution Verification partySystem.js / Registry Flow: Validate that the evolveCreature transformation correctly updates both the array reference in playerParty and its mirrored form in playerCollection. Verification Plan Automated Tests npm run dev Manual Verification Targets Debug Controls: Open Party menu. Press 1 to see EXP hit 50, trigger a level up, and see stats increase. Evolution Test: Press 3 to reach Level 9. Press 1 to hit Level 10. Confirm it evolves to Bramblecat/Cinderclaw/Dewtail. Battle Summary: Fight a wild battle and win. A summary screen should pause the game for 3 seconds, displaying EXP gained, before returning to the WorldScene. Post-Battle Persistance: After returning from battle, open the Party menu. Verify the EXP gained from the summary screen is accurately reflected in the registry data. 아그리고 verification은 무조건 내가 진행할거야

좋습니다. 플랜 자체는 괜찮습니다.
그리고 verification은 직접 하신다는 점도 맞게 반영해야 합니다.
즉, 안티그래비티에는 자동 검증이나 수동 검증을 대신하려 하지 말고,
검증 가능한 상태와 디버그 도구만 만들어라고 명확히 지시하는 게 맞습니다.
아래 문구를 그대로 보내세요.
Approved — proceed with Phase 5.5.

This phase should focus on making progression systems testable and visibly verifiable.

Important:
I will do all verification manually myself.
Do not treat automated or manual verification as something you are responsible for completing.
Your job is to implement the systems and provide the debug hooks and UI feedback needed so I can test them directly.

Proceed under the following constraints:

1. Scope
This phase should only focus on:
- visible EXP feedback
- visible level-up feedback
- visible evolution feedback
- PartyScene debug controls
- safe registry updates for evolved creatures
- clear post-battle progression summary

Do not expand into new story content or new regions yet.

2. Battle summary panel
After battle ends, before returning to WorldScene, show a readable summary panel with:
- battle result (Victory / Capture / Fled)
- EXP gained
- total EXP now
- whether level up occurred
- whether evolution occurred

Keep the panel on screen long enough for a human to read it.
Do not auto-dismiss too quickly.

3. PartyScene EXP visibility
Update PartyScene so each party creature clearly shows:
- current level
- current HP
- current EXP
- EXP needed for next level

The goal is that I can look at the Party screen and immediately understand whether EXP was applied correctly.

4. Debug controls
Add temporary debug controls inside PartyScene only while the menu is open:

Press 1:
- Give the active creature +50 EXP

Press 2:
- Give the active creature +200 EXP

Press 3:
- Set the active creature to level 9 and EXP close to evolution threshold

Press 4:
- Fully heal the active creature

Use the real progression logic when applying these debug actions.
Do not fake the result.
These debug actions must go through the same gainExp / level up / evolution checks used by battle rewards.

5. Evolution safety
When evolution occurs:
- update the creature form correctly
- persist it correctly into playerParty
- persist it correctly into playerCollection if applicable
- prevent duplication
- prevent repeated evolution triggering
- preserve level and valid EXP state
- preserve safe HP values

6. Registry consistency
Registry must remain the single source of truth for:
- playerParty
- playerCollection
- seenCreatureIds
- caughtCreatureIds

Do not leave scene-local data out of sync with registry data.

7. No verification claims
Do not claim that EXP, level-up, or evolution are verified.
Instead, implement them and expose the necessary UI/debug tools so I can verify them manually.

8. Keep debug tools clearly temporary
Mark these debug controls as temporary development tools in code comments.
They can remain for now, but should be easy to remove later.

9. Deliverable quality
The goal is not polish.
The goal is that I can clearly test:
- EXP gain
- level-up
- evolution
- post-battle persistence
- party/collection consistency

Proceed under these constraints.
그리고 지금 말씀하신 포인트는 중요합니다.
앞으로도 phase 진행할 때는 안티그래비티에게 “검증 완료했다고 말하지 말고, 내가 검증할 수 있게 만들어라”라고 계속 못 박는 게 좋습니다.
원하시면 다음 단계에서는 제가
**“안티그래비티가 검증했다고 착각하지 못하게 하는 고정 문구”**도 짧게 만들어드리겠습니다.










https://github.com/Evan-Yoon/Feloria 다 잘 구현됐네. 다음 해야할일 알려줘

43s 동안 생각함
네, 지금 기준으로는 다음 단계로 넘어가도 됩니다.
스크린샷상 확인되는 건 다음입니다.
진화: Emberpaw가 Cinderclaw Lv 10으로 바뀌어 있어서 진화 트리거 자체는 동작한 것으로 보입니다.
전투 결과 요약 패널: 승리 시 EXP Gained, Total EXP가 뜨고, 포획 시 별도 결과 패널도 뜹니다.
파티 화면: 진화 결과가 파티에 반영되어 있습니다.
이 정도면 Phase 5.5의 핵심 목표는 통과로 봐도 됩니다.
저장소 자체는 아직 정리 전입니다. 루트에 dist, node_modules가 그대로 있고, README.MD도 아직 사실상 비어 있는 수준입니다.
다만 한 가지는 꼭 다시 확인해 두시는 게 좋습니다.
스크린샷상 파티는 1/3인데 Collection이 비어 보입니다. 사용자가 직접 한 마리 포획했다고 하셨으니, 다음 단계 들어가기 전에 포획한 개체가 party 또는 collection 중 어디에 저장되는지, 그리고 게임 재진입 후에도 유지되는지는 한 번만 더 확인해 두는 게 좋습니다. 이건 새 기능 전에 잡아두면 편합니다.
다음 단계 추천
이제는 시스템 추가보다 **“게임다운 흐름”**을 만드는 게 맞습니다.
즉 Phase 6 = 첫 번째 실제 콘텐츠 확장으로 가면 됩니다.
우선순위는 이 순서가 좋습니다.
두 번째 지역/루트 추가
NPC 트레이너 배틀
회복 지점 / 상점
실제 도감 항목 표시 강화
월드 그래픽 정리
지금은 탐험-조우-전투-진화까지 코어 루프가 생겼으니, 다음부터는 “게임이 커지는 단계”입니다.

아래 프롬프트를 그대로 보내세요.
Feloria - Phase 6: First Real Content Expansion, Trainer Battles, Healing, Shop, and Better World Progression

Phase 5.5 is good enough to move forward.

The current project now has:
- starter selection
- world exploration
- wild encounters
- turn-based battle
- capture
- Codex / Quest / Party screens
- level-up and starter evolution support
- post-battle summary feedback

Now proceed to Phase 6.

Goal:
Turn the current prototype into a more game-like first chapter by adding real progression content beyond wild battles.

Scope for this phase:

1. Add a second real progression route
Expand the current world beyond the basic village/forest loop.

Implement:
- starwhisk_village
- greenpaw_forest
- mosslight_path

Mosslight Path should act as the first route beyond the forest.

2. Add trainer battles
Introduce NPC trainer battles for the first time.

Requirements:
- at least 2 trainer NPCs
- trainers initiate battle when interacted with
- trainers should not be re-battleable after defeat unless explicitly designed
- trainer battles should use party-style creature data, not wild encounter generation

3. Add a healing system
Implement a basic healing point.

Options:
- Elder Mira heals party
or
- a simple village healing station / inn

Requirements:
- restore current HP for all party creatures
- clear fainted state if applicable
- save updated party state back to registry

4. Add a shop system
Implement a very simple shop interaction in Starwhisk Village.

Shop inventory for now:
- Potion
- Yarn Ball

Requirements:
- simple currency system (gold/coins)
- simple purchase UI or menu
- inventory count increases after purchase
- purchases persist through registry/state

5. Inventory foundation
If not already present, implement a simple inventory system for:
- healing items
- capture items

Use data files where possible.

6. Codex improvement
The Codex should now show more than counts.

For seen creatures:
- show creature name
- show habitat
- show short description

For caught creatures:
- show additional details such as level seen or simple stat summary if appropriate

Do not keep all seen creatures as only ??? if data is available.

7. World progression
Tie progression to actual gameplay:
- complete First Steps
- unlock Mosslight Path
- trainer battle blocks path until defeated or spoken to
- village healing/shop becomes meaningful before continuing onward

8. Keep Phaser 3 + JavaScript
- Do not switch to TypeScript
- Do not reintroduce React gameplay components
- Keep scene/data modularity

9. Data organization
Use or expand separate data/modules for:
- creatures
- skills
- encounters
- trainers
- items
- inventory
- shop inventory
- quests
- codex

Do not hardcode all of this directly inside scenes.

10. Verification-ready implementation
Do not claim these systems are verified.
Implement them so they can be tested manually.

Key things that must be testable:
- trainer battle starts and ends correctly
- defeated trainer state persists
- healing restores party correctly
- shop purchases reduce currency and add items
- inventory persists
- Codex shows actual data for seen/caught creatures
- progression from village -> forest -> route feels connected

11. Cleanup / project hygiene
Also clean up obvious repository issues:
- stop committing node_modules
- stop committing dist
- improve README
- document controls and current game loop

Final note:
The goal of Phase 6 is to make Feloria feel like a real early-game RPG chapter, not just a combat/system testbed.
Prioritize trainer battles, healing, shop, and route progression before larger content expansion.
지금 시점에서의 추천 방향
지금부터는 새 시스템 10개를 한 번에 넣는 것보다,
“첫 마을 → 첫 숲 → 첫 루트 → 첫 트레이너 → 회복/상점” 이 흐름을 완성하는 게 제일 좋습니다.
원하시면 다음에는 제가 Phase 6 들어가기 전에 꼭 확인해야 할 버그 방지 체크리스트도 같이 정리해드리겠습니다.
