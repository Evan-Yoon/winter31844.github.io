# Feloria 대화 내역 분할본 23

- 원본 파일: Feloria 대화 내역.docx
- 분할 번호: 23/28
- 문단 범위: 4205~4483
- 문자 수(대략): 27536

---

import Phaser from 'phaser'; import { ASSETS } from '../config/assetPaths.js'; import { CREATURES } from '../data/creatures.js'; import { pixelArtGenerator } from '../systems/pixelArtGenerator.js'; import bgContinent from '../data/graphics/startscene/펠로리아 대륙.png'; import bgAncientCats from '../data/graphics/startscene/고대 고양이.png'; import bgTwistedForest from '../data/graphics/startscene/뒤틀린 숲.png'; import bgShadow from '../data/graphics/startscene/흑막.png'; import bgTitleScreen from '../data/graphics/startscene/Feloria_title_screen.png'; /** * PreloadScene * Responsible for loading all game assets. * Displays a loading bar to the user. */ export class PreloadScene extends Phaser.Scene { constructor() { super({ key: 'PreloadScene' }); } preload() { console.log('PreloadScene: Loading assets...'); // Create loading UI this.createLoadingUI(); // 1. Generic Image Assets (Faces, Battlebacks, Parallaxes, Tilesets) const imageCats = ['BATTLEBACKS1', 'BATTLEBACKS2', 'PARALLAXES', 'TILESETS']; imageCats.forEach(cat => { Object.values(ASSETS[cat]).forEach(asset => { this.load.image(asset.KEY, asset.PATH); }); }); // 2. Spritesheets (Characters, Animations, Faces, Monster Fallbacks) const sheetCats = ['CHARACTERS', 'ANIMATIONS', 'FACES', 'SPRITES']; sheetCats.forEach(cat => { Object.values(ASSETS[cat]).forEach(asset => { if (asset.FRAME_CONFIG) { this.load.spritesheet(asset.KEY, asset.PATH, asset.FRAME_CONFIG); } else { this.load.image(asset.KEY, asset.PATH); } }); }); // Error handling: if an asset fails to load, it won't crash the scene this.load.on('loaderror', (file) => { console.warn(Failed to load asset: ${file.key} at ${file.src}); }); // Load Start Scene Cutscene Backgrounds (Legacy/Fallback) this.load.image('bg_continent', bgContinent); this.load.image('bg_ancient_cats', bgAncientCats); this.load.image('bg_twisted_forest', bgTwistedForest); this.load.image('bg_shadow', bgShadow); this.load.image('bg_title_screen', bgTitleScreen); } create() { console.log('PreloadScene: Assets loaded, creating placeholder textures...'); // Create placeholder textures at runtime so the game is playable without external files this.createPlaceholderTextures(); // Transition to StartScene this.scene.start('StartScene'); } createLoadingUI() { const { width, height } = this.cameras.main; const progressBar = this.add.graphics(); const progressBox = this.add.graphics(); progressBox.fillStyle(0x222222, 0.8); progressBox.fillRect(width / 2 - 160, height / 2 - 25, 320, 50); const loadingText = this.make.text({ x: width / 2, y: height / 2 - 50, text: 'Loading Feloria...', style: { font: '20px monospace', fill: '#ffffff' } }); loadingText.setOrigin(0.5, 0.5); this.load.on('progress', (value) => { progressBar.clear(); progressBar.fillStyle(0xffffff, 1); progressBar.fillRect(width / 2 - 150, height / 2 - 15, 300 * value, 30); }); this.load.on('complete', () => { progressBar.destroy(); progressBox.destroy(); loadingText.destroy(); }); } /** * Generates simple placeholder textures for development using pixelArtGenerator. * This provides a massive visual upgrade over plain rectangles! */ createPlaceholderTextures() { // ---------------------------------------------------------------- // 1. TILESET (Empty, Grass, Dirt, Water, Wall, TallGrass, Tree) // ---------------------------------------------------------------- const tEmpty = Array(16).fill('................'); const tGrass = [ 'EEEEEEEEEEEEEEEE', 'EEGEEEEEEEEEEEEE', 'EEEEEEEGEEEEEEEE', 'EEEEEEEEEEEEEEEE', 'EEEEEEEEEEEEGEEE', 'EGEEEEEEEEEEEEEE', 'EEEEEEEEEEEEEEEE', 'EEEEEEGEEEEEEEEE', 'EEEEEEEEEEEEEEEE', 'EEEEEEEEEEEGEEEE', 'EEGEEEEEEEEEEEEE', 'EEEEEEEEEEEEEEEE', 'EEEEEEEGEEEEEEEE', 'EEEEEEEEEEEEEEEE', 'EEEEEEEEEEEEGEEE', 'EEEEEEEEEEEEEEEE' ]; const tDirt = [ 'vvvvvvvvvvvvvvvv', 'vvWvvvvvvvvvvvvv', 'vvvvvvvwvvvvvvvv', 'vvvvvvvvvvvvWvvv', 'vvvvwvvvvvvvvvvv', 'vvvvvvvvvvWvvvvv', 'vvvvvvvvwvvvvvvv', 'vvWvvvvvvvvvvvvv', 'vvvvvvvvvvvvvvvv', 'vvvvvWvvvvvvvvwv', 'vvvvvvvvvvvvvvvv', 'vvwvvvvvvWvvvvvv', 'vvvvvvvvvvvvvvvv', 'vvWvvvvvvvvvvwvv', 'vvvvvvvvvvvvvvvv', 'vvvvvvvvwvvvvvvv' ]; const tWater = [ 'bbbbbbbbbbbbbbbb', 'bbbbbsbbbbbbbbbb', 'bbbbbbbbbbbbbbbs', 'bbbbbbbbbbbbbbbb', 'bbsbbbbbbbbbbbbb', 'bbbbbbbbbsbbbbbb', 'bbbbbbbbbbbbbbbb', 'bbbbbbbbbbbbbbbb', 'bbbbbbbbbbbsbbbb', 'bbbbbbbbbbbbbbbb', 'bbbbbsbbbbbbbbbb', 'bbbbbbbbbbbbbbbb', 'bbbbbbbbbbbbbbbs', 'bbsbbbbbbbbbbbbb', 'bbbbbbbbbbbbbbbb', 'bbbbbbbbbsbbbbbb' ]; const tWall = [ '2222222222222222', '3333333133333331', '3333333133333331', '3333333133333331', '1111111111111111', '3331333333313333', '3331333333313333', '3331333333313333', '1111111111111111', '3333333133333331', '3333333133333331', '3333333133333331', '1111111111111111', '3331333333313333', '3331333333313333', '3331333333313333' ]; const tTallGrass = [ 'EEEEEEEEEEEEEEEE', 'EEEGGGEEEEEGGGEE', 'EEGGgGGEEEEGGgGE', 'EGgGGGEEEEEGGgGE', 'GGGGGGGEEEGGGGGG', 'GgGGgGGEEEGgGGgG', 'GGGGGGGGEEGGGGGG', 'EEGGGGGEEEEGGGGG', 'EGGgGGEEEEEGGgGE', 'GGGGGGGEEEGGGGGG', 'GgGGgGGEEEGgGGgG', 'GGGGGGGGEEGGGGGG', 'EEEGGGEEEEEGGGEE', 'EEGGGGGEEEEGGGGG', 'EGGgGGEEEEEGGgGE', 'EEEEEEEEEEEEEEEE' ]; const tTree = [ '......GGGG......', '....GGGGGGGG....', '...GGGGGGGGGG...', '..GGGGGGGGGGGG..', '..GGGGGGGGGGGG..', '.GGGGGGGGGGGGGG.', '.GGGGGGGGGGGGGG.', '.GGGGGGGGGGGGGG.', '.GGGGGGGGGGGGGG.', '..GGGGGGGGGGGG..', '...GGGGGGGGGG...', '.....wwwwww.....', '.....wWwwWw.....', '.....wwWwww.....', '.....wwwwww.....', '......wwww......' ]; pixelArtGenerator.createTileset(this, 'overworld-tiles', [tEmpty, tGrass, tDirt, tWater, tWall, tTallGrass, tTree], 2); // ---------------------------------------------------------------- // 2. PLAYER SPRITE (Human Explorer: Down, Up, Left, Right) // ---------------------------------------------------------------- const pDown = [ '.....HHHHHH.....', '....HHHHHHHH....', '....HHffffHH....', '....Hff00ffH....', '....HffffffH....', '.....ffffff.....', '....bbbbbbbb....', '...bbbbbbbbbb...', '...bbbbbbbbbb...', '...bbbbbbbbbb...', '....KKKKKKKK....', '....KKKKKKKK....', '....KK....KK....', '....KK....KK....', '....00....00....', '....00....00....' ]; const pUp = [ '.....HHHHHH.....', '....HHHHHHHH....', '....HHHHHHHH....', '....HHHHHHHH....', '....HHHHHHHH....', '.....HHHHHH.....', '....bbbbbbbb....', '...bbbbbbbbbb...', '...bbbbbbbbbb...', '...bbbbbbbbbb...', '....KKKKKKKK....', '....KKKKKKKK....', '....KK....KK....', '....KK....KK....', '....00....00....', '....00....00....' ]; const pLeft = [ '.....HHHHH......', '....HHHHHHH.....', '....HffffHH.....', '....Hff0fHH.....', '....HffffHH.....', '.....fffff......', '....bbbbbbb.....', '...bbbbbbbbb....', '...bbbbbbbbb....', '...bbbbbbbbb....', '....KKKKKKK.....', '....KKKKKKK.....', '....KK...KK.....', '....KK...KK.....', '....00...00.....', '....00...00.....' ]; const pRight = [ '......HHHHH.....', '.....HHHHHHH....', '.....HHffffH....', '.....HHf0ffH....', '.....HHffffH....', '......fffff.....', '.....bbbbbbb....', '....bbbbbbbbb...', '....bbbbbbbbb...', '....bbbbbbbbb...', '.....KKKKKKK....', '.....KKKKKKK....', '.....KK...KK....', '.....KK...KK....', '.....00...00....', '.....00...00....' ]; pixelArtGenerator.createSpritesheet(this, 'player', [pDown, pUp, pLeft, pRight], 16, 16, 2); // ---------------------------------------------------------------- // 3. NPC SPRITES (Human Villagers) // ---------------------------------------------------------------- // Elder Mira (Gray Hair, Purple Robe) const miraFrame = [ '.....444444.....', '....44444444....', '....44ffff44....', '....44f00f44....', '....44ffff44....', '.....ffffff.....', '....UUUUUUUU....', '...UUUUUUUUUU...', '...UUUUUUUUUU...', '...UUUUUUUUUU...', '...UUUUUUUUUU...', '...UUUUUUUUUU...', '...UUUUUUUUUU...', '...UUUUUUUUUU...', '....00....00....', '....00....00....' ]; pixelArtGenerator.createTexture(this, 'npc_mira', miraFrame, 2); // Common NPC / Shopkeeper (Brown Hair, Green Shirt) const npcFrame = [ '.....HHHHHH.....', '....HHHHHHHH....', '....HHffffHH....', '....Hff00ffH....', '....HffffffH....', '.....ffffff.....', '....GGGGGGGG....', '...GGGGGGGGGG...', '...GGGGGGGGGG...', '...GGGGGGGGGG...', '....KKKKKKKK....', '....KKKKKKKK....', '....KK....KK....', '....KK....KK....', '....00....00....', '....00....00....' ]; pixelArtGenerator.createTexture(this, 'npc', npcFrame, 2); // Trainer NPC (Red Cap, Red Vest) const trainerFrame = [ '.....RRRRRR.....', '....RRRRRRRR....', '....RRffffRR....', '....Rf00ffRH....', '....RffffffH....', '.....ffffff.....', '....rrrrrrrr....', '...rrrrrrrrrr...', '...rrrr00rrrr...', '...rrrr00rrrr...', '....KKKKKKKK....', '....KKKKKKKK....', '....KK....KK....', '....KK....KK....', '....00....00....', '....00....00....' ]; pixelArtGenerator.createTexture(this, 'npc_trainer', trainerFrame, 2); // ---------------------------------------------------------------- // 4. CREATURE SPRITES // ---------------------------------------------------------------- // Map existing types to basic letters for the generator palette const typeLetters = { 'Forest': 'G', 'Grass': 'g', 'Fire': 'R', 'Water': 'B', 'Rock': '2', 'Shadow': '1', 'Ice': 's', 'Storm': 'Y', 'Spirit': 'D', 'Mystic': 'B', 'Ice/Mystic': 'c', 'Fire/Storm': 'p', 'Forest/Spirit': 'l', 'Spirit/Shadow': '1' }; Object.keys(CREATURES).forEach(key => { const cat = CREATURES[key]; const clr = typeLetters[cat.type] || '4'; // Default to white // Dynamically build a detailed frame using the mapped color character const cFrame = [ ....${clr}......${clr}...., ...${clr}${clr}${clr}....${clr}${clr}${clr}..., ...${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}..., ..${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}.., ..${clr}${clr}${clr}44${clr}${clr}44${clr}${clr}${clr}.., ..${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}.., ..${clr}${clr}${clr}${clr}1111${clr}${clr}${clr}${clr}.., ...${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}..., ....${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}...., ...${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}..., ..${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}.., ..${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}${clr}.., ..${clr}${clr}...${clr}${clr}...${clr}${clr}.., ..${clr}${clr}...${clr}${clr}...${clr}${clr}.., ..${clr}${clr}...${clr}${clr}...${clr}${clr}.., ...........${clr}${clr}${clr}.. ]; // We use pixelSize 4 for creatures so they are 64x64 on screen natively before scaling pixelArtGenerator.createTexture(this, key.toLowerCase(), cFrame, 4); }); const uiFrameGraphics = this.make.graphics({ x: 0, y: 0, add: false }); uiFrameGraphics.fillStyle(0x000000, 0.9).fillRect(0, 0, 64, 64); uiFrameGraphics.lineStyle(2, 0xffffff, 1).strokeRect(0, 0, 64, 64); uiFrameGraphics.generateTexture('ui-frame', 64, 64); } }

좋습니다. 이 PreloadScene.js만 보면 여기서 직접 크롭을 고치는 파일은 아닙니다.
지금 기준으로 핵심은 이렇게 보시면 됩니다.
지금 직접 봐야 할 파일 순서
1. src/game/config/assetPaths.js
가장 먼저 봐야 합니다.
이유:
PreloadScene.js는 ASSETS.FACES에 들어있는 설정대로만 불러옵니다.
즉 얼굴 이미지가 spritesheet로 잘릴지, 그냥 image로 불릴지는 assetPaths.js의 FRAME_CONFIG 유무로 결정됩니다.
지금 PreloadScene.js의 이 부분이 핵심입니다.
const sheetCats = ['CHARACTERS', 'ANIMATIONS', 'FACES', 'SPRITES'];
sheetCats.forEach(cat => {
  Object.values(ASSETS[cat]).forEach(asset => {
    if (asset.FRAME_CONFIG) {
      this.load.spritesheet(asset.KEY, asset.PATH, asset.FRAME_CONFIG);
    } else {
      this.load.image(asset.KEY, asset.PATH);
    }
  });
});
즉 FACES에 FRAME_CONFIG가 없으면 망합니다.

2. src/game/scenes/DialogScene.js
여기서 실제 얼굴을 어떻게 그리고 있는지 봐야 합니다.
지금 스크린샷상으로는 이 줄이 있었죠:
this.faceSprite = this.add.image(facePadding, portraitY, this.faceKey, this.faceIndex);
이건 this.faceKey가 spritesheet로 로드된 상태라면 틀린 방식입니다.
이 경우는 보통 image보다 sprite를 쓰는 게 안전합니다.
즉 여기서 확인해야 할 것:
this.faceKey
this.faceIndex
add.image로 그리고 있는지
add.sprite로 바꿔야 하는지

3. NPC/대사 데이터 파일
보통 다음 중 하나입니다.
src/game/data/npcs.js
src/game/data/dialogues/...
여기서 확인할 것:
faceKey가 정확히 "People4" 같은 키인지
faceIndex가 0~7 범위인지
잘못된 경로 문자열이 아니라 PreloadScene에서 등록된 key 이름을 쓰는지

지금 PreloadScene.js에서 바로 확인된 것
좋은 점
이 구조 자체는 괜찮습니다.
if (asset.FRAME_CONFIG) {
  this.load.spritesheet(...)
}
즉 구조는 맞습니다.

문제 가능성이 큰 곳
PreloadScene.js 자체보다 assetPaths.js에서 FACES 설정이 잘못되었을 가능성이 가장 큽니다.
예를 들어 여기서 이렇게 되어 있어야 합니다.
FACES: {
  PEOPLE4: {
    KEY: 'People4',
    PATH: '/assets/faces/People4.png',
    FRAME_CONFIG: {
      frameWidth: 144,
      frameHeight: 144
    }
  }
}
만약 이렇게 되어 있으면 안 됩니다.
FACES: {
  PEOPLE4: {
    KEY: 'People4',
    PATH: '/assets/faces/People4.png'
  }
}
이러면 그냥 한 장짜리 이미지처럼 로드됩니다.

결론
지금 직접 조정해야 할 우선순위는 이겁니다.
1순위
src/game/config/assetPaths.js
여기서 FACES 항목을 확인하세요.
반드시 각 face sheet가 이렇게 되어 있어야 합니다.
FRAME_CONFIG: {
  frameWidth: 144,
  frameHeight: 144
}

2순위
src/game/scenes/DialogScene.js
여기서 이 부분을 보세요.
this.faceKey
this.faceIndex
this.add.image(...)
만약 face sheet를 spritesheet로 불러왔다면,
여기는 이렇게 바꾸는 걸 먼저 시도하는 게 좋습니다.
this.faceSprite = this.add.sprite(
  facePadding,
  portraitY,
  this.faceKey,
  this.faceIndex
);
this.faceSprite.setOrigin(0, 0);

3순위
npcs.js 또는 dialogue 데이터
예:
faceKey: "People4",
faceIndex: 0
이런 식으로 맞는지 확인.

바로 확인할 체크리스트
assetPaths.js
FACES: {
  PEOPLE4: {
    KEY: 'People4',
    PATH: '/assets/faces/People4.png',
    FRAME_CONFIG: {
      frameWidth: 144,
      frameHeight: 144
    }
  }
}
DialogScene.js
this.add.sprite(..., this.faceKey, this.faceIndex)
npc/dialogue data
faceKey: 'People4'
faceIndex: 0

그리고 하나 더
이 PreloadScene.js에는 아직 예전 경로가 남아 있습니다.
import bgContinent from '../data/graphics/startscene/펠로리아 대륙.png';
import bgAncientCats from '../data/graphics/startscene/고대 고양이.png';
import bgTwistedForest from '../data/graphics/startscene/뒤틀린 숲.png';
import bgShadow from '../data/graphics/startscene/흑막.png';
import bgTitleScreen from '../data/graphics/startscene/Feloria_title_screen.png';
이건 지금 public/assets/... 체계로 옮기는 중이면 나중에 정리 대상입니다.
지금 얼굴 크롭 문제와는 직접 관계 없지만, 경로 체계 통일성은 깨져 있습니다.

다음 메시지로 assetPaths.js의 FACES 부분이랑 DialogScene.js 얼굴 그리는 부분 20줄 정도만 보내주시면,

FACES: { ACTOR1: { KEY: 'face_actor1', PATH: '/assets/faces/Actor1.png', FRAME_CONFIG: { frameWidth: 144, frameHeight: 144 } }, PEOPLE1: { KEY: 'face_people1', PATH: '/assets/faces/People1.png', FRAME_CONFIG: { frameWidth: 144, frameHeight: 144 } }, PEOPLE4: { KEY: 'face_elder', PATH: '/assets/faces/People4.png', FRAME_CONFIG: { frameWidth: 144, frameHeight: 144 } }, EVIL: { KEY: 'face_evil', PATH: '/assets/faces/Evil.png', FRAME_CONFIG: { frameWidth: 144, frameHeight: 144 } }, },

좋습니다. 문제 거의 찾았습니다.
지금 FACES 설정 자체는 크롭 크기 144×144로 정상입니다.
즉 문제는 frame 크기가 아니라 key 이름 불일치 가능성이 큽니다.

지금 보이는 핵심 문제
지금 assetPaths.js는 이렇게 되어 있습니다.
FACES: {
  ACTOR1: { KEY: 'face_actor1', PATH: '/assets/faces/Actor1.png', FRAME_CONFIG: { frameWidth: 144, frameHeight: 144 } },
  PEOPLE1: { KEY: 'face_people1', PATH: '/assets/faces/People1.png', FRAME_CONFIG: { frameWidth: 144, frameHeight: 144 } },
  PEOPLE4: { KEY: 'face_elder', PATH: '/assets/faces/People4.png', FRAME_CONFIG: { frameWidth: 144, frameHeight: 144 } },
  EVIL: { KEY: 'face_evil', PATH: '/assets/faces/Evil.png', FRAME_CONFIG: { frameWidth: 144, frameHeight: 144 } },
},
여기서 실제 Phaser에 등록되는 key는:
face_actor1
face_people1
face_elder
face_evil
입니다.
그런데 스크린샷상 DialogScene.js 로그를 보면 이런 식이었죠.
console.log(`DialogScene: faceKey=${this.faceKey} ...`)
그리고 현재 화면상으로는 아마 this.faceKey가
People4 또는 PEOPLE4 같은 값을 쓰고 있을 가능성이 큽니다.
즉:
로드된 key
face_elder
실제 사용하려는 key
People4
이렇게 다르면 꼬입니다.

정리: 지금 확인된 것
정상
FRAME_CONFIG: { frameWidth: 144, frameHeight: 144 }
facesheet를 spritesheet로 로드하는 구조
의심되는 진짜 문제
DialogScene 또는 npcs.js에서 faceKey로 잘못된 문자열을 넘기고 있음
또는 add.image()를 쓰고 있어 spritesheet frame을 제대로 안 씀

지금 바로 봐야 할 것
1. DialogScene.js
이 부분 확인:
this.faceSprite = this.add.image(facePadding, portraitY, this.faceKey, this.faceIndex);
이 줄이 맞으면 먼저 바꿔야 합니다.
수정
this.faceSprite = this.add.sprite(
  facePadding,
  portraitY,
  this.faceKey,
  this.faceIndex
);
this.faceSprite.setOrigin(0, 0);

2. npcs.js 또는 dialogue data
여기서 얼굴 키를 어떻게 넣는지 확인해야 합니다.
잘못된 예
faceKey: "People4"
지금 assetPaths 기준 맞는 값
faceKey: "face_elder"
즉 KEY 값을 써야 합니다.

가장 가능성 높은 원인 1순위
지금 거의 확률 높게 이겁니다:
PATH는 People4.png인데
실제 Phaser key는 face_elder인데
DialogScene은 People4로 찾고 있다
그러면 texture는 로드돼 있어도 의도한 frame을 못 찾거나 다른 방식으로 꼬일 수 있습니다.

추천 수정 방식
사실 가장 헷갈리지 않으려면 KEY를 파일명 느낌으로 통일하는 게 좋습니다.
예를 들면 이렇게:
FACES: {
  ACTOR1: { KEY: 'Actor1', PATH: '/assets/faces/Actor1.png', FRAME_CONFIG: { frameWidth: 144, frameHeight: 144 } },
  PEOPLE1: { KEY: 'People1', PATH: '/assets/faces/People1.png', FRAME_CONFIG: { frameWidth: 144, frameHeight: 144 } },
  PEOPLE4: { KEY: 'People4', PATH: '/assets/faces/People4.png', FRAME_CONFIG: { frameWidth: 144, frameHeight: 144 } },
  EVIL: { KEY: 'Evil', PATH: '/assets/faces/Evil.png', FRAME_CONFIG: { frameWidth: 144, frameHeight: 144 } },
},
이렇게 하면 faceKey: "People4"로 넘겨도 안 헷갈립니다.
지금처럼
PEOPLE4 -> KEY: face_elder
이런 별칭은 나중에 자꾸 꼬입니다.

지금 직접 만질 파일 우선순위
제일 먼저
DialogScene.js
add.image인지 add.sprite인지 확인
this.faceKey 실제 값 확인
두 번째
npcs.js
faceKey 값이 face_elder인지 확인
세 번째
assetPaths.js
key 이름을 단순하게 바꾸는 것도 추천

지금 바로 할 수 있는 가장 빠른 해결
방법 A
DialogScene.js에서 add.sprite(...)로 변경
방법 B
NPC 데이터에서
faceKey: "face_elder"
로 맞추기

제 판단
지금은 크롭 크기 문제는 아닙니다.
지금 FACES 설정만 보면 144×144는 맞습니다.
지금 남은 건 거의 둘 중 하나입니다.
faceKey mismatch
add.image 대신 add.sprite를 안 쓴 문제

DialogScene.js에서 얼굴 그리는 부분 15~20줄만 보내주시면
정확히 어디를 한 줄 바꾸면 되는지 바로 짚어드리겠습니다.

import Phaser from "phaser"; import { ASSETS } from "../config/assetPaths.js"; /** * DialogScene * An overlay scene that displays dialogue text boxes. */ export class DialogScene extends Phaser.Scene { constructor() { super({ key: "DialogScene" }); } init(data) { this.dialogue = data.dialogue; // Filter out special tags like [SHOP_PROMPT] from display this.displayPages = this.dialogue.pages.filter((p) => !p.startsWith("[")); this.currentPage = 0; this.faceKey = this.dialogue.faceKey || null; this.faceIndex = this.dialogue.faceIndex || 0; this.onComplete = data.onComplete; } create() { const { width, height } = this.cameras.main; const padding = 20; const boxHeight = 240; // Increased to fit 144x144 face + name comfortably // UI Box (Gray with white border) const box = this.add.container(padding, height - boxHeight - padding); const bg = this.add .rectangle(0, 0, width - padding * 2, boxHeight, 0x000000, 0.9) .setOrigin(0) .setStrokeStyle(4, 0xffffff); box.add(bg); // Speaker Name Box (Small header style) const nameText = this.dialogue.name || "???"; this.speakerName = this.add.text(20, 15, nameText, { font: 'bold 24px "Malgun Gothic", "Apple SD Gothic Neo", sans-serif', fill: "#f1c40f", }); // Portrait (Below Name) const facePadding = 20; const portraitY = 55; // Below name const faceSize = 144; // 120에서 RPG Maker 기본 얼굴 크기인 144로 수정 console.log(DialogScene: faceKey=${this.faceKey}, exists=${this.textures.exists(this.faceKey)}); if (this.faceKey && this.textures.exists(this.faceKey)) { // --- 수정된 부분: Phaser Texture Manager로 이미지 동적 분할 --- const texture = this.textures.get(this.faceKey); const frameName = face_${this.faceIndex}; // 예: 'face_0', 'face_1' // 아직 이 인덱스의 프레임이 잘려있지 않다면 계산해서 자릅니다. if (!texture.has(frameName)) { const cols = 4; // 4열로 구성된 스프라이트 시트 const col = this.faceIndex % cols; const row = Math.floor(this.faceIndex / cols); // 원본 텍스처에서 정확한 좌표(x, y)와 크기(width, height)를 지정하여 프레임을 추가합니다. texture.add(frameName, 0, col * faceSize, row * faceSize, faceSize, faceSize); } // this.faceIndex(숫자) 대신 새롭게 잘라낸 프레임 이름(문자열)을 사용합니다. this.faceSprite = this.add.sprite( facePadding, portraitY, this.faceKey, frameName ); // ------------------------------------------------------------- this.faceSprite.setOrigin(0, 0); box.add(this.faceSprite); } else { console.warn(DialogScene: Portrait texture '${this.faceKey}' not found!); } // Content Text (Adjusted for 144x144 portrait) const textX = this.faceSprite ? facePadding + 144 + 25 : 30; const textY = 60; const textWidth = width - (padding * 2) - textX - 40; this.contentText = this.add.text(textX, textY, "", { font: 'bold 32px "Malgun Gothic", "Apple SD Gothic Neo", sans-serif', fill: "#ffffff", wordWrap: { width: textWidth }, lineSpacing: 10, }); // Continue Hint this.hintText = this.add .text(width - padding * 3, boxHeight - 25, "Space를 눌러 계속", { font: "italic 16px Arial", fill: "#bdc3c7", }) .setOrigin(1, 1); box.add([this.speakerName, this.contentText, this.hintText]); // Show first page this.displayPage(); // Input to progress this.input.keyboard.on("keydown-SPACE", () => this.nextPage()); this.input.keyboard.on("keydown-ENTER", () => this.nextPage()); } displayPage() { const rawText = this.displayPages[this.currentPage]; const playerName = this.registry.get("playerName") || "Hero"; const formattedText = rawText.replace(/{playerName}/g, playerName); this.contentText.setText(formattedText); } nextPage() { this.currentPage++; if (this.currentPage < this.displayPages.length) { this.displayPage(); } else { this.finish(); } } finish() { if (this.onComplete) this.onComplete(); this.scene.stop(); } }

26s 동안 생각함
좋습니다. DialogScene.js는 이제 원인이 꽤 명확합니다.
핵심 결론
지금 코드는 얼굴 시트를 이미 spritesheet로 로드했는데,
DialogScene에서 다시 직접 잘라서 프레임을 추가하고 있습니다.
즉 지금은:
PreloadScene에서 이미 144x144로 잘라짐
DialogScene에서 또 texture.add(...)로 직접 잘라서 씀
이중 처리라서 꼬일 가능성이 큽니다.

지금 이 파일에서 바로 고칠 부분
현재 문제 구간
이 부분입니다.
const texture = this.textures.get(this.faceKey);
const frameName = `face_${this.faceIndex}`;

if (!texture.has(frameName)) {
  const cols = 4;
  const col = this.faceIndex % cols;
  const row = Math.floor(this.faceIndex / cols);

  texture.add(frameName, 0, col * faceSize, row * faceSize, faceSize, faceSize);
}

this.faceSprite = this.add.sprite(
  facePadding,
  portraitY,
  this.faceKey,
  frameName
);
이건 지우는 게 맞습니다.
왜냐하면 load.spritesheet(..., { frameWidth: 144, frameHeight: 144 })를 했으면
Phaser가 이미 프레임을 0, 1, 2, 3, 4, 5, 6, 7로 만들어놨습니다.
따라서 여기서는 그냥 숫자 프레임 인덱스를 쓰면 됩니다.

가장 안전한 수정 방법
아래처럼 바꾸세요.
console.log(`DialogScene: faceKey=${this.faceKey}, faceIndex=${this.faceIndex}, exists=${this.textures.exists(this.faceKey)}`);

if (this.faceKey && this.textures.exists(this.faceKey)) {
  this.faceSprite = this.add.sprite(
    facePadding,
    portraitY,
    this.faceKey,
    this.faceIndex
  );

  this.faceSprite.setOrigin(0, 0);
  box.add(this.faceSprite);
} else {
  console.warn(`DialogScene: Portrait texture '${this.faceKey}' not found!`);
}
즉 중간의 texture.add(...) 블록 전체 삭제입니다.

왜 이게 맞냐
지금 assetPaths.js는 이미 이렇게 되어 있습니다.
FRAME_CONFIG: { frameWidth: 144, frameHeight: 144 }
즉 Phaser는 People4.png를 자동으로 이렇게 자릅니다.
0 1 2 3
4 5 6 7
그래서 this.faceIndex = 0이면 왼쪽 위 얼굴,
this.faceIndex = 1이면 그 옆 얼굴을 바로 가져옵니다.
직접 다시 자를 필요가 없습니다.

추가로 같이 고치면 좋은 것
1. || 0 대신 ?? 0
지금 이 줄:
this.faceIndex = this.dialogue.faceIndex || 0;
실제로 0일 때는 문제 없긴 하지만, 더 명확하게 하려면 이렇게 쓰는 게 좋습니다.
this.faceIndex = this.dialogue.faceIndex ?? 0;

2. textX도 faceSize 변수 쓰기
지금은 이렇게 되어 있죠.
const textX = this.faceSprite ? facePadding + 144 + 25 : 30;
이건 하드코딩 대신 이렇게 바꾸는 게 좋습니다.
const textX = this.faceSprite ? facePadding + faceSize + 25 : 30;

3. faceKey 값 확인
지금 asset key는 이렇습니다.
face_actor1
face_people1
face_elder
face_evil
따라서 dialogue.faceKey에는 반드시 이런 값이 들어가야 합니다.
예:
faceKey: "face_elder",
faceIndex: 0
만약 People4나 PEOPLE4를 넣고 있으면 안 됩니다.

바로 적용용 수정본
아래처럼 바꾸시면 됩니다.
init(data) {
  this.dialogue = data.dialogue;
  this.displayPages = this.dialogue.pages.filter((p) => !p.startsWith("["));
  this.currentPage = 0;
  this.faceKey = this.dialogue.faceKey ?? null;
  this.faceIndex = this.dialogue.faceIndex ?? 0;
  this.onComplete = data.onComplete;
}

create() {
  const { width, height } = this.cameras.main;
  const padding = 20;
  const boxHeight = 240;

  const box = this.add.container(padding, height - boxHeight - padding);

  const bg = this.add
    .rectangle(0, 0, width - padding * 2, boxHeight, 0x000000, 0.9)
    .setOrigin(0)
    .setStrokeStyle(4, 0xffffff);
  box.add(bg);

  const nameText = this.dialogue.name || "???";
  this.speakerName = this.add.text(20, 15, nameText, {
    font: 'bold 24px "Malgun Gothic", "Apple SD Gothic Neo", sans-serif',
    fill: "#f1c40f",
  });

  const facePadding = 20;
  const portraitY = 55;
  const faceSize = 144;

  console.log(
    `DialogScene: faceKey=${this.faceKey}, faceIndex=${this.faceIndex}, exists=${this.textures.exists(this.faceKey)}`
  );

  if (this.faceKey && this.textures.exists(this.faceKey)) {
    this.faceSprite = this.add.sprite(
      facePadding,
      portraitY,
      this.faceKey,
      this.faceIndex
    );
    this.faceSprite.setOrigin(0, 0);
    box.add(this.faceSprite);
  } else {
    console.warn(`DialogScene: Portrait texture '${this.faceKey}' not found!`);
  }

  const textX = this.faceSprite ? facePadding + faceSize + 25 : 30;
  const textY = 60;
  const textWidth = width - (padding * 2) - textX - 40;

  this.contentText = this.add.text(textX, textY, "", {
    font: 'bold 32px "Malgun Gothic", "Apple SD Gothic Neo", sans-serif',
    fill: "#ffffff",
    wordWrap: { width: textWidth },
    lineSpacing: 10,
  });

  this.hintText = this.add
    .text(width - padding * 3, boxHeight - 25, "Space를 눌러 계속", {
      font: "italic 16px Arial",
      fill: "#bdc3c7",
    })
    .setOrigin(1, 1);

  box.add([this.speakerName, this.contentText, this.hintText]);

  this.displayPage();

  this.input.keyboard.on("keydown-SPACE", () => this.nextPage());
  this.input.keyboard.on("keydown-ENTER", () => this.nextPage());
}

요약
지금 직접 만질 곳은:
DialogScene.js
texture.add(...) 블록 삭제
this.add.sprite(..., this.faceIndex)로 단순화
그리고 꼭 확인할 것:
dialogue.faceKey가 face_elder 같은 실제 preload key인지

다음으로 npcs.js에서 현석 대사 데이터에 faceKey, faceIndex가 어떻게 들어가 있는지만 보여주시면, 그 값까지 맞는지 바로 확인해드리겠습니다.

assetPaths.js
JavaScript
creatures.zip
ZIP 아카이브
creatures.zip 파일도 assetPaths.js처럼 형식을 추출해줘

1m 57s 동안 생각함
네. creatures.zip은 확인해봤고, 구조는 이렇습니다.
압축 내부 폴더: creatures/
파일 10개:
creatures_01.png
creatures_02.png
...
creatures_10.png
모든 이미지 크기: 1536 × 1024
중요한 점이 하나 있습니다.
이 파일들은 RPG Maker 캐릭터 시트처럼 균일한 spritesheet가 아닙니다.
지금 creatures_01.png를 보면:
한 장 안에 고양이 10마리
이름 텍스트가 이미지에 같이 박혀 있음
각 고양이마다 여백이 다름
이라서, 그대로 load.spritesheet()로 쓰는 건 비추천입니다.
우선은 이미지 묶음(contact sheet) 으로 보고, 가장 안전한 방법은:
먼저 개별 고양이 PNG로 분리
그다음 assetPaths.js에 개별 이미지 등록
입니다.

1. 지금 바로 쓸 수 있는 assetPaths.js 형식
우선 zip 안의 원본 시트 자체를 등록하려면 이렇게 하면 됩니다.
CREATURE_SHEETS: {
  CREATURES_01: {
    KEY: "creatures_01",
    PATH: "/assets/sprites/creatures/creatures_01.png"
  },
  CREATURES_02: {
    KEY: "creatures_02",
    PATH: "/assets/sprites/creatures/creatures_02.png"
  },
  CREATURES_03: {
    KEY: "creatures_03",
    PATH: "/assets/sprites/creatures/creatures_03.png"
  },
  CREATURES_04: {
    KEY: "creatures_04",
    PATH: "/assets/sprites/creatures/creatures_04.png"
  },
  CREATURES_05: {
    KEY: "creatures_05",
    PATH: "/assets/sprites/creatures/creatures_05.png"
  },
  CREATURES_06: {
    KEY: "creatures_06",
    PATH: "/assets/sprites/creatures/creatures_06.png"
  },
  CREATURES_07: {
    KEY: "creatures_07",
    PATH: "/assets/sprites/creatures/creatures_07.png"
  },
  CREATURES_08: {
    KEY: "creatures_08",
    PATH: "/assets/sprites/creatures/creatures_08.png"
  },
  CREATURES_09: {
    KEY: "creatures_09",
    PATH: "/assets/sprites/creatures/creatures_09.png"
  },
  CREATURES_10: {
    KEY: "creatures_10",
    PATH: "/assets/sprites/creatures/creatures_10.png"
  }
},
그리고 PreloadScene.js에서는 이건 spritesheet가 아니라 image로 로드해야 합니다.
Object.values(ASSETS.CREATURE_SHEETS).forEach(asset => {
  this.load.image(asset.KEY, asset.PATH);
});

2. 더 좋은 방식: 개별 고양이 이미지로 분리해서 등록
지금 형태는 전투에 바로 쓰기에는 불편합니다.
그래서 권장 구조는 이겁니다.
public/assets/sprites/creatures/
  leafkit.png
  bramblecat.png
  emberpaw.png
  cinderclaw.png
  misttail.png
  dewtail.png
  thistlefur.png
  mosslynx.png
  verdantlynx.png
  ...
그러면 assetPaths.js는 이렇게 갑니다.
