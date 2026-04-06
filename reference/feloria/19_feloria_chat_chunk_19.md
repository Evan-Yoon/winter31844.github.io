# Feloria 대화 내역 분할본 19

- 원본 파일: Feloria 대화 내역.docx
- 분할 번호: 19/28
- 문단 범위: 3499~3558
- 문자 수(대략): 24552

---

/** * creatures.js * Master list of all available cat-based monsters in Feloria. * STRICT RULE: All creatures must be feline-based. */ export const CREATURES = { // --- STARTER CREATURES --- LEAFKIT: { id: 'LEAFKIT', name: 'Leafkit', type: 'Forest', description: 'A playful green kitten. It smells like fresh pine.', baseHp: 20, baseAttack: 5, baseDefense: 4, catchRate: 1.0, skills: ['vine_swipe', 'leaf_dart', 'forest_guard'], evolution: { target: 'BRAMBLECAT', level: 10 } }, BRAMBLECAT: { id: 'BRAMBLECAT', name: 'Bramblecat', type: 'Forest', description: 'Its fur has hardened into thick protective vines.', baseHp: 45, baseAttack: 12, baseDefense: 10, catchRate: 0.5, skills: ['thorn_whip', 'root_snare', 'nature_roar'], evolution: null }, EMBERPAW: { id: 'EMBERPAW', name: 'Emberpaw', type: 'Fire', description: 'A fiery kitten. Its paws are warm to the touch.', baseHp: 18, baseAttack: 7, baseDefense: 3, catchRate: 1.0, skills: ['ember_bite', 'flame_dash', 'heat_claw'], evolution: { target: 'CINDERCLAW', level: 10 } }, CINDERCLAW: { id: 'CINDERCLAW', name: 'Cinderclaw', type: 'Fire', description: 'Fierce and hot-headed. Its claws leave scorch marks.', baseHp: 40, baseAttack: 16, baseDefense: 8, catchRate: 0.5, skills: ['inferno_slash', 'blazing_pounce', 'firestorm'], evolution: null }, MISTTAIL: { id: 'MISTTAIL', name: 'Misttail', type: 'Water', description: 'A slick, agile kitten that loves to swim.', baseHp: 22, baseAttack: 4, baseDefense: 6, catchRate: 1.0, skills: ['water_slash', 'mist_burst', 'tidal_wave'], evolution: { target: 'DEWTAIL', level: 10 } }, DEWTAIL: { id: 'DEWTAIL', name: 'Dewtail', type: 'Water', description: 'A swift swimmer. It can summon light rains when upset.', baseHp: 50, baseAttack: 10, baseDefense: 14, catchRate: 0.5, skills: ['aqua_fang', 'tidal_crash', 'ocean_wrath'], evolution: null }, // --- FOREST CREATURES --- SNAGPUSS: { id: 'SNAGPUSS', name: 'Snagpuss', type: 'Grass', description: 'A curious thorn-furred kitten that hides in tall grass.', baseHp: 15, baseAttack: 4, baseDefense: 5, catchRate: 0.8, skills: ['scratch', 'vine_swipe', 'leaf_dart'], evolution: { target: 'THISTLEFUR', level: 12 } }, THISTLEFUR: { id: 'THISTLEFUR', name: 'Thistlefur', type: 'Grass', description: 'Covered in prickly burrs, making it hard to attack directly.', baseHp: 38, baseAttack: 11, baseDefense: 13, catchRate: 0.4, skills: ['bite', 'thorn_whip', 'root_snare'], evolution: null }, MOSSLYNX: { id: 'MOSSLYNX', name: 'Mosslynx', type: 'Forest', description: 'A quiet moss-covered wildcat that blends into the forest floor.', baseHp: 18, baseAttack: 6, baseDefense: 4, catchRate: 0.7, skills: ['quick_strike', 'vine_swipe', 'forest_guard'], evolution: { target: 'VERDANTLYNX', level: 14 } }, VERDANTLYNX: { id: 'VERDANTLYNX', name: 'Verdantlynx', type: 'Forest', description: 'A mature lynx with branches growing from its shoulders.', baseHp: 48, baseAttack: 16, baseDefense: 10, catchRate: 0.2, skills: ['flurry', 'nature_roar', 'thorn_whip'], evolution: null }, THORNKIT: { id: 'THORNKIT', name: 'Thornkit', type: 'Grass', description: 'A small kitten covered in soft thorns that harden when scared.', baseHp: 15, baseAttack: 5, baseDefense: 3, catchRate: 0.8, skills: ['scratch', 'vine_swipe', 'root_snare'], evolution: { target: 'THORNMANE', level: 12 } }, THORNMANE: { id: 'THORNMANE', name: 'Thornmane', type: 'Grass', description: 'A fierce predator with a mane of razor-sharp brambles.', baseHp: 42, baseAttack: 20, baseDefense: 8, catchRate: 0.2, skills: ['pounce', 'thorn_whip', 'nature_roar'], evolution: null }, FERNCLAW: { id: 'FERNCLAW', name: 'Fernclaw', type: 'Grass', description: 'Its claws grow like sharp leaves used to defend its territory.', baseHp: 16, baseAttack: 7, baseDefense: 3, catchRate: 0.7, skills: ['scratch', 'leaf_dart', 'vine_swipe'], evolution: { target: 'VINEFANG', level: 13 } }, VINEFANG: { id: 'VINEFANG', name: 'Vinefang', type: 'Grass', description: 'It restrains prey with vines extending from its tail.', baseHp: 40, baseAttack: 16, baseDefense: 8, catchRate: 0.3, skills: ['bite', 'root_snare', 'thorn_whip'], evolution: null }, BARKPELT: { id: 'BARKPELT', name: 'Barkpelt', type: 'Forest', description: 'Its fur resembles tree bark and protects it from predators.', baseHp: 22, baseAttack: 3, baseDefense: 8, catchRate: 0.8, skills: ['scratch', 'forest_guard', 'leaf_dart'], evolution: { target: 'IRONBARK', level: 15 } }, IRONBARK: { id: 'IRONBARK', name: 'Ironbark', type: 'Forest', description: 'Its hide is practically impenetrable to ordinary strikes.', baseHp: 55, baseAttack: 8, baseDefense: 20, catchRate: 0.3, skills: ['pounce', 'nature_roar', 'root_snare'], evolution: null }, THISTLEKIT: { id: 'THISTLEKIT', name: 'Thistlekit', type: 'Grass', description: 'A tiny feline that rolls into thorny bushes to hide.', baseHp: 12, baseAttack: 5, baseDefense: 2, catchRate: 0.9, skills: ['quick_strike', 'vine_swipe', 'leaf_dart'], evolution: { target: 'THORNPROWLER', level: 11 } }, THORNPROWLER: { id: 'THORNPROWLER', name: 'Thornprowler', type: 'Grass', description: 'Hunts aggressively by ambushing from dense undergrowth.', baseHp: 32, baseAttack: 14, baseDefense: 7, catchRate: 0.4, skills: ['flurry', 'thorn_whip', 'root_snare'], evolution: null }, // --- FIRE CREATURES --- SPARKPAW: { id: 'SPARKPAW', name: 'Sparkpaw', type: 'Fire', description: 'A playful kitten that leaves sparks wherever it runs.', baseHp: 15, baseAttack: 6, baseDefense: 3, catchRate: 0.8, evolution: { target: 'BLAZECLAW', level: 14 } }, BLAZECLAW: { id: 'BLAZECLAW', name: 'Blazeclaw', type: 'Fire', description: 'Every step it takes scorches the earth beneath it.', baseHp: 38, baseAttack: 16, baseDefense: 9, catchRate: 0.3, evolution: null }, CINDERCAT: { id: 'CINDERCAT', name: 'Cindercat', type: 'Fire', description: 'Its fur glows faintly like embers in the dark.', baseHp: 18, baseAttack: 5, baseDefense: 5, catchRate: 0.7, evolution: { target: 'INFERMANE', level: 16 } }, INFERMANE: { id: 'INFERMANE', name: 'Infermane', type: 'Fire', description: 'Its majestic mane is made entirely of continuously burning flames.', baseHp: 45, baseAttack: 14, baseDefense: 12, catchRate: 0.2, evolution: null }, ASHLYNX: { id: 'ASHLYNX', name: 'Ashlynx', type: 'Fire', description: 'It thrives in places where fire has recently passed.', baseHp: 17, baseAttack: 7, baseDefense: 4, catchRate: 0.7, evolution: { target: 'PYROLYNX', level: 15 } }, PYROLYNX: { id: 'PYROLYNX', name: 'Pyrolynx', type: 'Fire', description: 'It controls the temperature of the air around it.', baseHp: 42, baseAttack: 18, baseDefense: 8, catchRate: 0.3, evolution: null }, // --- WATER CREATURES --- RIPPLEPAW: { id: 'RIPPLEPAW', name: 'Ripplepaw', type: 'Water', description: 'A kitten that chases ripples across shallow streams.', baseHp: 16, baseAttack: 5, baseDefense: 5, catchRate: 0.8, skills: ['scratch', 'water_slash', 'mist_burst'], evolution: { target: 'TIDALCLAW', level: 13 } }, TIDALCLAW: { id: 'TIDALCLAW', name: 'Tidalclaw', type: 'Water', description: 'Its claws carry the weight of a crashing ocean wave.', baseHp: 40, baseAttack: 13, baseDefense: 13, catchRate: 0.4, evolution: null }, STREAMTAIL: { id: 'STREAMTAIL', name: 'Streamtail', type: 'Water', description: 'Its long tail helps it swim against strong currents.', baseHp: 18, baseAttack: 6, baseDefense: 4, catchRate: 0.7, skills: ['quick_strike', 'water_slash', 'mist_burst'], evolution: { target: 'FLOODLYNX', level: 15 } }, FLOODLYNX: { id: 'FLOODLYNX', name: 'Floodlynx', type: 'Water', description: 'When it roars, local water sources crest into sudden floods.', baseHp: 46, baseAttack: 15, baseDefense: 10, catchRate: 0.3, evolution: null }, DEWKIT: { id: 'DEWKIT', name: 'Dewkit', type: 'Water', description: 'A shy cat whose fur always feels slightly damp.', baseHp: 14, baseAttack: 4, baseDefense: 6, catchRate: 0.9, skills: ['scratch', 'mist_burst', 'water_slash'], evolution: { target: 'MISTLYNX', level: 12 } }, MISTLYNX: { id: 'MISTLYNX', name: 'Mistlynx', type: 'Water', description: 'It can vanish entirely inside heavy fog.', baseHp: 38, baseAttack: 10, baseDefense: 15, catchRate: 0.4, evolution: null }, // --- ROCK CREATURES --- PEBBLEPAW: { id: 'PEBBLEPAW', name: 'Pebblepaw', type: 'Rock', description: 'A stubborn kitten that collects shiny stones.', baseHp: 19, baseAttack: 5, baseDefense: 7, catchRate: 0.8, skills: ['scratch', 'pebble_toss', 'bite'], evolution: { target: 'STONECLAW', level: 14 } }, STONECLAW: { id: 'STONECLAW', name: 'Stoneclaw', type: 'Rock', description: 'Its claws are tough enough to sheer solid marble.', baseHp: 45, baseAttack: 14, baseDefense: 16, catchRate: 0.3, evolution: null }, GRANITECAT: { id: 'GRANITECAT', name: 'Granitecat', type: 'Rock', description: 'Its fur hardens like stone when threatened.', baseHp: 20, baseAttack: 4, baseDefense: 9, catchRate: 0.7, skills: ['quick_strike', 'pebble_toss', 'rock_smash'], evolution: { target: 'TITANPELT', level: 16 } }, TITANPELT: { id: 'TITANPELT', name: 'Titanpelt', type: 'Rock', description: 'Practically immovable. It sleeps in the center of rockslides.', baseHp: 55, baseAttack: 10, baseDefense: 22, catchRate: 0.2, evolution: null }, BOULDERLYNX: { id: 'BOULDERLYNX', name: 'Boulderlynx', type: 'Rock', description: 'Massive feline known for pushing rocks onto rivals.', baseHp: 48, baseAttack: 16, baseDefense: 14, catchRate: 0.3, skills: ['flurry', 'pebble_toss', 'rock_smash'], evolution: { target: 'CLIFFMAW', level: 25 } }, CLIFFMAW: { id: 'CLIFFMAW', name: 'Cliffmaw', type: 'Rock', description: 'Apex mountain predator with a bite that shatters stalagmites.', baseHp: 70, baseAttack: 22, baseDefense: 18, catchRate: 0.1, evolution: null }, // --- SHADOW CREATURES --- NIGHTKIT: { id: 'NIGHTKIT', name: 'Nightkit', type: 'Shadow', description: 'A quiet shadow kitten that moves silently.', baseHp: 14, baseAttack: 6, baseDefense: 3, catchRate: 0.8, skills: ['scratch', 'shadow_sneak', 'bite'], evolution: { target: 'NIGHTPELT', level: 15 } }, NIGHTPELT: { id: 'NIGHTPELT', name: 'Nightpelt', type: 'Shadow', description: 'Uses the cover of true darkness to ambush its prey.', baseHp: 38, baseAttack: 16, baseDefense: 8, catchRate: 0.3, skills: ['pounce', 'dark_pulse', 'shadow_sneak'], evolution: null }, DUSKPAW: { id: 'DUSKPAW', name: 'Duskpaw', type: 'Shadow', description: 'Appears only at dusk when shadows stretch long.', baseHp: 16, baseAttack: 6, baseDefense: 4, catchRate: 0.7, skills: ['scratch', 'shadow_sneak', 'dark_pulse'], evolution: { target: 'UMBRAFANG', level: 16 } }, UMBRAFANG: { id: 'UMBRAFANG', name: 'Umbrafang', type: 'Shadow', description: 'Its bite is said to sever light from the target.', baseHp: 42, baseAttack: 17, baseDefense: 10, catchRate: 0.3, skills: ['bite', 'dark_pulse', 'shadow_sneak'], evolution: null }, SHADECLAW: { id: 'SHADECLAW', name: 'Shadeclaw', type: 'Shadow', description: 'Its body seems to flicker in and out of darkness.', baseHp: 15, baseAttack: 8, baseDefense: 3, catchRate: 0.6, skills: ['quick_strike', 'shadow_sneak', 'dark_pulse'], evolution: { target: 'VOIDLYNX', level: 18 } }, VOIDLYNX: { id: 'VOIDLYNX', name: 'Voidlynx', type: 'Shadow', description: 'A terrifying predator that hunts completely unseen.', baseHp: 45, baseAttack: 20, baseDefense: 7, catchRate: 0.2, skills: ['quick_strike', 'phantom_claw', 'dark_pulse'], evolution: null }, // --- ICE CREATURES --- FROSTKIT: { id: 'FROSTKIT', name: 'Frostkit', type: 'Ice', description: 'A playful kitten that slides across frozen lakes.', baseHp: 16, baseAttack: 5, baseDefense: 5, catchRate: 0.8, skills: ['scratch', 'ice_shard', 'frost_breath'], evolution: { target: 'GLACIERPAW', level: 15 } }, GLACIERPAW: { id: 'GLACIERPAW', name: 'Glacierpaw', type: 'Ice', description: 'Its heavy paws leave frozen footprints even in summer.', baseHp: 42, baseAttack: 14, baseDefense: 13, catchRate: 0.3, skills: ['bite', 'ice_shard', 'blizzard_claw'], evolution: null }, SNOWPELT: { id: 'SNOWPELT', name: 'Snowpelt', type: 'Ice', description: 'Its fur blends perfectly with falling snow.', baseHp: 18, baseAttack: 6, baseDefense: 4, catchRate: 0.7, skills: ['quick_strike', 'ice_shard', 'frost_breath'], evolution: { target: 'BLIZZARDFANG', level: 16 } }, BLIZZARDFANG: { id: 'BLIZZARDFANG', name: 'Blizzardfang', type: 'Ice', description: 'Able to whip up blinding blizzards with a swish of its tail.', baseHp: 48, baseAttack: 12, baseDefense: 16, catchRate: 0.2, skills: ['scratch', 'blizzard_claw', 'absolute_zero'], evolution: null }, // --- STORM CREATURES --- STORMKIT: { id: 'STORMKIT', name: 'Stormkit', type: 'Storm', description: 'Its fur crackles softly with static electricity.', baseHp: 15, baseAttack: 7, baseDefense: 3, catchRate: 0.7, skills: ['scratch', 'spark_strike', 'gust'], evolution: { target: 'THUNDERLYNX', level: 16 } }, THUNDERLYNX: { id: 'THUNDERLYNX', name: 'Thunderlynx', type: 'Storm', description: 'A roaring cat whose howl sounds like thunder.', baseHp: 40, baseAttack: 18, baseDefense: 8, catchRate: 0.3, skills: ['bite', 'spark_strike', 'thunder_paw'], evolution: null }, GALECLAW: { id: 'GALECLAW', name: 'Galeclaw', type: 'Storm', description: 'Known for leaping incredible distances on the wind.', baseHp: 17, baseAttack: 8, baseDefense: 4, catchRate: 0.6, skills: ['flurry', 'gust', 'spark_strike'], evolution: { target: 'TEMPESTFANG', level: 18 } }, TEMPESTFANG: { id: 'TEMPESTFANG', name: 'Tempestfang', type: 'Storm', description: 'It strikes with the unpredictable velocity of a hurricane.', baseHp: 38, baseAttack: 21, baseDefense: 7, catchRate: 0.2, skills: ['flurry', 'thunder_paw', 'storm_call'], evolution: null }, // --- SPIRIT CREATURES --- WHISPERKIT: { id: 'WHISPERKIT', name: 'Whisperkit', type: 'Spirit', description: 'Said to be the spirit of a long forgotten feline.', baseHp: 14, baseAttack: 4, baseDefense: 7, catchRate: 0.7, skills: ['scratch', 'shadow_sneak', 'phantom_claw'], evolution: { target: 'SOULPELT', level: 19 } }, SOULPELT: { id: 'SOULPELT', name: 'Soulpelt', type: 'Spirit', description: 'A guardian of graveyards that guides lost souls home.', baseHp: 38, baseAttack: 11, baseDefense: 18, catchRate: 0.3, skills: ['bite', 'phantom_claw', 'soul_reap'], evolution: null }, PHANTOMLYNX: { id: 'PHANTOMLYNX', name: 'Phantomlynx', type: 'Spirit', description: 'Its footsteps make no sound.', baseHp: 16, baseAttack: 7, baseDefense: 4, catchRate: 0.5, skills: ['quick_strike', 'shadow_sneak', 'phantom_claw'], evolution: { target: 'SPECTRALCLAW', level: 21 } }, SPECTRALCLAW: { id: 'SPECTRALCLAW', name: 'Spectralclaw', type: 'Spirit', description: 'It can pass through solid objects to catch its prey.', baseHp: 44, baseAttack: 19, baseDefense: 10, catchRate: 0.2, skills: ['phantom_claw', 'soul_reap', 'spectral_strike'], evolution: null }, // --- MYSTIC CREATURES --- RUNECLAW: { id: 'RUNECLAW', name: 'Runeclaw', type: 'Mystic', description: 'Strange glowing runes appear in its fur.', baseHp: 18, baseAttack: 6, baseDefense: 5, catchRate: 0.6, skills: ['scratch', 'mana_burst', 'star_fall'], evolution: { target: 'ARCANEFANG', level: 20 } }, ARCANEFANG: { id: 'ARCANEFANG', name: 'Arcanefang', type: 'Mystic', description: 'Capable of bending minor reality with its arcane growls.', baseHp: 45, baseAttack: 15, baseDefense: 12, catchRate: 0.2, skills: ['bite', 'star_fall', 'cosmic_roar'], evolution: null }, STARLIGHTKIT: { id: 'STARLIGHTKIT', name: 'Starlightkit', type: 'Mystic', description: 'Its eyes shine like distant stars.', baseHp: 16, baseAttack: 5, baseDefense: 6, catchRate: 0.7, skills: ['scratch', 'mana_burst', 'solar_beam'], evolution: { target: 'CELESTIPELT', level: 22 } }, CELESTIPELT: { id: 'CELESTIPELT', name: 'Celestipelt', type: 'Mystic', description: 'It is said it descends from the Milky Way on clear nights.', baseHp: 42, baseAttack: 13, baseDefense: 16, catchRate: 0.2, skills: ['star_fall', 'cosmic_roar', 'celestial_strike'], evolution: null }, // --- RARE CREATURES --- AURORACAT: { id: 'AURORACAT', name: 'Auroracat', type: 'Ice/Mystic', description: 'Its ribbons of fur resemble the northern lights.', baseHp: 60, baseAttack: 16, baseDefense: 18, catchRate: 0.05, skills: ['ice_shard', 'mana_burst', 'solar_beam'], evolution: null }, EMBERLYNX: { id: 'EMBERLYNX', name: 'Emberlynx', type: 'Fire/Storm', description: 'A terrifying beast combining wildflame and lightning.', baseHp: 55, baseAttack: 22, baseDefense: 12, catchRate: 0.05, skills: ['inferno_slash', 'storm_call', 'blazing_pounce'], evolution: null }, VERDANTCLAW: { id: 'VERDANTCLAW', name: 'Verdantclaw', type: 'Forest/Spirit', description: 'Protector of the deepest grottos. Ancient and powerful.', baseHp: 70, baseAttack: 14, baseDefense: 22, catchRate: 0.05, skills: ['vine_whip', 'nature_roar', 'spirit_drain'], evolution: null }, // --- LEGENDARY FELINES --- SOLARION: { id: 'SOLARION', name: 'Solarion', type: 'Fire', description: 'A legendary feline said to embody the sun itself. Blindingly radiant.', baseHp: 100, baseAttack: 35, baseDefense: 25, catchRate: 0.01, skills: ['solar_beam', 'firestorm', 'holy_smite'], evolution: null }, GLACIARA: { id: 'GLACIARA', name: 'Glaciara', type: 'Ice', description: 'A legendary feline said to embody the sun itself. Blindingly radiant.', baseHp: 100, baseAttack: 35, baseDefense: 25, catchRate: 0.01, skills: ['ice_shard', 'blizzard_claw', 'absolute_zero'], evolution: null }, TEMPESTCLAW: { id: 'TEMPESTCLAW', name: 'Tempestclaw', type: 'Storm', description: 'A legendary feline said to embody the sun itself. Blindingly radiant.', baseHp: 100, baseAttack: 35, baseDefense: 25, catchRate: 0.01, skills: ['thunder_paw', 'storm_call', 'hurricane_strike'], evolution: null }, VERDANTLYNX: { id: 'VERDANTLYNX', name: 'Verdantlynx', type: 'Forest', description: 'The ancient guardian of the deep woods. Flowers bloom wherever it steps.', baseHp: 120, baseAttack: 28, baseDefense: 30, catchRate: 0.01, skills: ['nature_roar', 'phantom_claw', 'soul_reap'], evolution: null }, UMBRAFANG: { id: 'UMBRAFANG', name: 'Umbrafang', type: 'Shadow', description: 'A creature of pure shadow. It is said to consume the light around it.', baseHp: 85, baseAttack: 38, baseDefense: 22, catchRate: 0.01, skills: ['dark_pulse', 'shadow_sneak', 'void_strike'], evolution: null }, AQUARION: { id: 'AQUARION', name: 'Aquarion', type: 'Water', description: 'Master of the ocean depths. Its movements are as fluid as a rushing river.', baseHp: 105, baseAttack: 30, baseDefense: 28, catchRate: 0.01, evolution: null }, TERRACLAW: { id: 'TERRACLAW', name: 'Terraclaw', type: 'Rock', description: 'Forged from the earth itself. Its hide is impenetrable bedrock.', baseHp: 130, baseAttack: 25, baseDefense: 45, catchRate: 0.01, evolution: null }, LUMINA: { id: 'LUMINA', name: 'Lumina', type: 'Light', description: 'A glowing entity of pure hope and light. It blinds those with dark intentions.', baseHp: 95, baseAttack: 32, baseDefense: 25, catchRate: 0.01, evolution: null }, AETHERION: { id: 'AETHERION', name: 'Aetherion', type: 'Mystic', description: 'A celestial cat that drifts through the cosmic winds of Aether.', baseHp: 90, baseAttack: 35, baseDefense: 35, catchRate: 0.01, evolution: null }, NOCTYRA: { id: 'NOCTYRA', name: 'Noctyra', type: 'Spirit', description: 'A ghost-like feline that shepherds lost souls through the night forest.', baseHp: 100, baseAttack: 30, baseDefense: 30, catchRate: 0.01, evolution: null } }; 지금 이상태인데 이게 지금 몇개지?

지금 코드상으로는:
작성한 엔트리 수: 71개
실제로 JS 객체에 남는 수: 69개
이유는 중복 키 2개 때문입니다.
중복된 키:
VERDANTLYNX
UMBRAFANG
자바스크립트 객체에서는 같은 키를 두 번 쓰면 뒤에 있는 값이 앞의 값을 덮어씁니다.
그래서 실제 runtime에서는:
숲 진화형 VERDANTLYNX → 전설 VERDANTLYNX로 덮임
어둠 진화형 UMBRAFANG → 전설 UMBRAFANG로 덮임
즉 현재 상태는 이렇게 보시면 됩니다.
정리
총 선언 수: 71
중복 2개 제외 실제 개수: 69
카테고리별 선언 수
스타터: 6
숲: 12
불: 6
물: 6
바위: 6
어둠: 6
얼음: 4
폭풍: 4
영혼: 4
신비: 4
레어: 3
전설: 10
합계 71
지금 바로 고쳐야 하는 것
전설 쪽 이름을 바꾸는 게 좋습니다. 예를 들면:
전설 VERDANTLYNX → VERDANTLORD 또는 ANCIENT_VERDANTLYNX
전설 UMBRAFANG → UMBRAREX 또는 ANCIENT_UMBRAFANG
이렇게 바꾸면 기존 진화 라인이 안 깨집니다.
원하시면 제가 바로 중복 없이 정리된 creatures.js 이름 목록으로 다시 정리해드리겠습니다.

그럼 이미 있는거에 31종을 실제로 있는 고양이 종류 포함해서 추가해줘(총 100개가 되게) 한글로 해야하고 스킬은 200개로 만들어서 스킬을 등급 3개로 나눠서 기본 고양이가 3등급 스킬셋 세개로 구성, 중간 진화된 고양이가 2등급 중 3개 구성, 최종진화형이 3등급 3개 구성, 전설급은 별도 고유 강력 스킬셋 네개씩 구성해줘

22s 동안 생각함
좋습니다.
지금 기준으로는 기존 69종 + 신규 31종 = 총 100종,
스킬은 총 200개, 그리고 등급은 아래처럼 정리해서 가는 게 맞습니다.
제가 한 가지는 합리적으로 보정해서 구성하겠습니다.
기본형: 3등급 스킬 3개
중간 진화형: 2등급 스킬 3개
최종 진화형: 1등급 스킬 3개
전설형: 전설 전용 고유 스킬 4개
사용자님 문장에서 “최종진화형이 3등급”이라고 적혀 있었는데,
등급 3단계 구조상 그건 아마 오타로 보고 최종은 1등급으로 맞춰서 드리겠습니다.
아래는 안티그래비티에 한 번에 넣기 좋은 프롬프트와,
바로 코드에 반영하기 좋은 데이터 구조 규칙 + 신규 31종 리스트 + 200스킬 생성 규칙을 합친 버전입니다.

안티그래비티용 통합 프롬프트
Feloria - Creature Expansion to 100 Total and Skill Expansion to 200 Total

Current creatures.js already contains 69 unique creatures.
Expand the roster by adding 31 new cat-based creatures so the total becomes exactly 100 unique creatures.

Important:
All creature names and skill names must be written in Korean.
IDs may remain ASCII-safe for code stability, but visible display names must be Korean.

Use these rules strictly.

--------------------------------
CREATURE ROSTER RULES
--------------------------------

1. Total creature count must become 100 unique creatures.
Current unique count is 69.
Add exactly 31 new creatures.

2. Include real cat breeds among the newly added creatures.
Examples of real breeds that may be included:
- 러시안 블루
- 샴
- 페르시안
- 메인쿤
- 벵갈
- 랙돌
- 스코티시 폴드
- 노르웨이 숲
- 스핑크스
- 브리티시 숏헤어
- 아비시니안
- 터키시 앙고라
- 사바나
- 먼치킨
- 버만
- 데본 렉스
- 콘월 렉스
- 오리엔탈 숏헤어
- 아메리칸 숏헤어
- 이집션 마우
- 터키시 반
- 맹크스
- 네벨룽
- 하바나 브라운
- 라가머핀
- 차트뢰
- 싱가푸라
- 소말리
- 토이거
- 버밀라
- 셀커크 렉스

3. All creatures must still be feline-based.
No non-cat monsters.

4. Evolution structure
Use this 3-tier structure where applicable:

- basic form = 3등급 스킬 3개
- mid evolution = 2등급 스킬 3개
- final evolution = 1등급 스킬 3개

Legendary creatures:
- 4 unique legendary-only skills each

5. Keep existing creatures, but avoid ID collisions.
Do not duplicate keys like VERDANTLYNX or UMBRAFANG again.

--------------------------------
SKILL SYSTEM RULES
--------------------------------

Create a total of exactly 200 skills.

Skill grades:
- 1등급 = strongest standard skills
- 2등급 = mid-tier evolution skills
- 3등급 = basic starter/basic wild skills
- 전설 전용 = legendary unique skills

Rules:
- every basic creature uses exactly 3 grade-3 skills
- every middle evolution uses exactly 3 grade-2 skills
- every final evolution uses exactly 3 grade-1 skills
- every legendary uses exactly 4 unique legendary skills

6. Skill data structure
Each skill should include:

- id
- name (Korean)
- grade
- type
- power
- accuracy
- category (attack / buff / debuff / utility)
- animationKey
- effectType

7. Basic attack balance
Basic attack must remain intentionally weak.
Skills are the main combat option.

--------------------------------
NEW CREATURE ADDITION PLAN
--------------------------------

Add these 31 new creatures.

A. Real breed inspired creatures
1. 러시안 블루
2. 샴
3. 페르시안
4. 메인쿤
5. 벵갈
6. 랙돌
7. 스코티시 폴드
8. 노르웨이 숲
9. 스핑크스
10. 브리티시 숏헤어
11. 아비시니안
12. 터키시 앙고라
13. 사바나
14. 먼치킨
15. 버만
16. 데본 렉스
17. 콘월 렉스
18. 오리엔탈 숏헤어
19. 아메리칸 숏헤어
20. 이집션 마우
21. 터키시 반
22. 맹크스
23. 네벨룽
24. 하바나 브라운
25. 라가머핀
26. 차트뢰
27. 싱가푸라
28. 소말리
29. 토이거
30. 버밀라
31. 셀커크 렉스

For each new creature:
- assign type
- assign Korean description
- assign stats
- assign catchRate
- assign skill set according to grade rules
- assign evolution if desired, but keep total unique creatures at 100

--------------------------------
IMPLEMENTATION PREFERENCE
--------------------------------

Use this visible naming style:
name: "러시안 블루"

Use ASCII-safe ids like:
id: "RUSSIAN_BLUE"

For skills:
id: "ice_claw"
name: "빙결 발톱"

--------------------------------
DELIVERABLES
--------------------------------

1. Update creatures.js so total unique creatures = 100
2. Create / expand skills.js so total unique skills = 200
3. Ensure every creature references valid skills
4. Ensure no duplicate creature IDs
5. Ensure no duplicate skill IDs
6. Keep all display names in Korean

--------------------------------
IMPORTANT QUALITY RULES
--------------------------------

- Avoid placeholder nonsense names
- Use natural Korean fantasy skill names
- Keep type identity clear
- Keep evolution progression feeling stronger at each stage
- Legendary skills must feel clearly stronger and more special than standard skills

바로 코드에 넣기 좋은 규칙
스킬 등급 규칙
grade: 3 // 기본형
grade: 2 // 중간 진화
grade: 1 // 최종 진화
grade: 0 // 전설 전용
권장 파워 범위
3등급: 20 ~ 38
2등급: 35 ~ 55
1등급: 50 ~ 78
전설전용: 75 ~ 110

신규 31종 추가용 creatures.js 샘플
아래는 기존 69종에 이어서 바로 붙이기 좋은 31종 샘플입니다.
이건 “실제 품종 포함” 조건을 맞춘 버전입니다.
