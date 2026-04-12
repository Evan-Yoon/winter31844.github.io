---
title: |
  [Mongle] Day 3
  AI 채팅 연동과 500 에러
slug: mongle-day3-ai-chat-integration
date: 2026-03-25
author: Evan Yoon
category: project
subcategory: team-project
description: React Native 채팅창에 FastAPI + GPT-4o-mini를 연동하고, 의존성 충돌·pycache 충돌·500
  에러를 하나씩 잡아나간 하루.
tags:
  - mongle
  - react-native
  - fastapi
  - gpt-4o-mini
  - streaming
  - debugging
  - team-project
  - troubleshooting
readTime: 11
series: Mongle
seriesOrder: 3
featured: false
draft: false
toc: true
---

## 오전: 데이터 준비와 정리

어제 987개 업체를 JSON으로 수집하고 레이블링까지 끝냈다. 오늘 오전은 그걸 Supabase에 올릴 수 있는 형태로 가공하는 것부터 시작했다.

**09:43** — 먼저 로그인 방식을 업데이트했다. 어제 회원가입 PR이 머지됐으니 로그인 플로우도 맞춰야 한다.

> `로그인 방식 업데이트`

**10:16** — 플래너 더미 데이터 50명 추가.

> `플래너 50명 더미 데이터 추가`

플래너 화면이 구현되고 있는데, 실제 플래너 데이터가 Supabase에 없으면 UI를 테스트할 수 없다. 50명 분량을 생성해서 DB에 올렸다.

**11:33** — 어제 만든 `labeled_vendors.json`을 Supabase가 바로 import할 수 있는 CSV로 변환했다.

> `업체 csv로 변환` — 18,816 lines added

`json_to_csv.py`를 짜서 JSON → CSV 변환, 결과물 `vendors_for_supabase.csv`는 988개 행이 됐다. 오늘 팀원들과 Supabase에 같이 올릴 예정이었는데, AI 연동 작업이 더 급해서 일단 파일만 만들어뒀다.

이 시간대에 Evan-Yoon이 머지한 PR만 3개다.

| 시각 | PR |
|------|----|
| 11:31 | PR #30 — 타임라인 UI |
| 11:53 | PR #32 — 플래너 프로필 UI |
| 12:23 | PR #33 — 일정 대시보드 UI (달력 + 잔금 일정) |

팀원들이 각자 화면을 착착 올려주고 있다. PM으로서 코드 리뷰 후 머지하는 게 루틴이 됐다.

---

## 오후: AI 채팅 연동 — 오늘의 핵심

오후부터는 이번 프로젝트에서 내가 가장 공들인 작업인 **AI 챗봇 연동**에 집중했다.

어제 `intent_parser.py`와 `responder.py`의 뼈대를 만들었고, 오늘은 프론트엔드 채팅 화면과 실제로 붙여야 한다. FastAPI 서버를 로컬에 띄우고 Expo 클라이언트와 통신시키는 것이 오늘의 목표.

---

## 이슈 1: React 의존성 충돌

Expo 캘린더 패키지를 설치하다 바로 막혔다.

```
npm error ERESOLVE unable to resolve dependency tree
npm error peer react-dom@">=16.8.0" from ...
```

`react-dom` 버전이 Peer Dependency 충돌을 일으켰다. React Native 환경에선 `react-dom`이 필요없는데, 일부 패키지가 이를 peer dep으로 요구하면서 버전이 안 맞는 것.

```bash
npm install expo-calendar --legacy-peer-deps
```

`--legacy-peer-deps` 옵션으로 npm이 strict peer dep 검사를 건너뛰게 했다. 설치 완료.

---

## 이슈 2: Git Pull 충돌 — `__pycache__` <!-- short: 이슈 2 -->

main 브랜치를 pull 받다가 충돌이 났다.

```
error: Your local changes to the following files would be overwritten by merge:
  Mongle-server/__pycache__/main.cpython-314.pyc
  Mongle-server/ai/__pycache__/__init__.cpython-314.pyc
```

파이썬이 로컬에서 실행되면서 생성된 `__pycache__` 바이너리 파일들이 git에 트래킹되고 있었다. 전날 내가 이 파일들을 실수로 커밋해버린 것.

```bash
# 로컬 pycache 강제 삭제
find . -type d -name __pycache__ -exec rm -rf {} +
find . -name "*.pyc" -delete

git rm -r --cached Mongle-server/__pycache__
git rm -r --cached Mongle-server/ai/__pycache__
```

그리고 `.gitignore`에 즉시 추가했다.

> `feat: python cache gitignore에 추가`

```
# Python
__pycache__/
*.pyc
*.pyo
*.pyd
.Python
```

---

## 이슈 3: PowerShell `&&` 미지원

FastAPI 서버를 로컬에서 실행하려고 이런 명령을 쳤다.

```bash
cd Mongle-server && uvicorn main:app --reload
```

PowerShell에서는 `&&`가 지원되지 않는다. bash에서는 당연한 체이닝인데, Windows PowerShell은 `;`를 써야 한다.

```powershell
cd Mongle-server; uvicorn main:app --reload
```

그 다음엔 `openai` 모듈 없음 에러.

```
ModuleNotFoundError: No module named 'openai'
```

requirements.txt가 있어도 설치가 안 된 상태였다.

```bash
pip install -r requirements.txt
```

설치 후 서버 재실행. 이제 `http://localhost:8000`이 뜬다.

---

## 채팅창 AI 기능 구현

> `채팅창에 ai 기능 추가` — 16:26
> 15 files changed, 826 insertions(+)

환경 문제들을 다 해결하고 나서야 본 작업을 시작했다. 결과물은 5개 핵심 파일.

### `hooks/useAi.js` — AI 통신의 핵심

```javascript
const VENDOR_DELIMITER = '\n---VENDORS_JSON---\n';

function parseResponse(fullText) {
  const parts = fullText.split(VENDOR_DELIMITER);
  if (parts.length < 2) return { displayText: fullText, vendors: [] };
  try {
    const vendors = JSON.parse(parts[1]);
    return { displayText: parts[0], vendors: Array.isArray(vendors) ? vendors : [] };
  } catch {
    return { displayText: parts[0], vendors: [] };
  }
}
```

백엔드 `responder.py`와 약속한 구분자(`---VENDORS_JSON---`)를 기준으로 응답 텍스트와 업체 JSON을 파싱한다. 텍스트는 말풍선에, 업체 목록은 카드 형태로 별도 렌더링한다.

```javascript
const sendMessage = useCallback(async (text) => {
  // 최근 10개 메시지를 OpenAI history 포맷으로 변환
  const history = [...messages]
    .slice(0, MAX_HISTORY)
    .reverse()
    .map(m => ({
      role: m.role === 'ai' ? 'assistant' : 'user',
      content: m.displayText || m.text || '',
    }));
  // ...
}, [messages, isLoading]);
```

대화 맥락을 유지하기 위해 최근 10개 메시지를 히스토리로 서버에 함께 보낸다. FlatList가 `inverted`로 동작하기 때문에(index 0이 최신), 서버로 보낼 때는 뒤집어서 시간순으로 변환한다.

### `app/(couple)/chat/ai.jsx` — AI 채팅 화면

```javascript
const QUICK_START_CHIPS = [
  '스튜디오 추천해줘',
  '예산 300만원 드레스',
  '플래너 추천해줘',
];

function WelcomeView({ onChipPress }) {
  return (
    <View style={styles.welcomeContainer}>
      <Ionicons name="sparkles" size={32} color="#fff" />
      <Text style={styles.welcomeTitle}>웨딩 AI 어시스턴트</Text>
      <View style={styles.chipsRow}>
        {QUICK_START_CHIPS.map(chip => (
          <TouchableOpacity key={chip} onPress={() => onChipPress(chip)}>
            <Text>{chip}</Text>
          </TouchableOpacity>
        ))}
      </View>
    </View>
  );
}
```

첫 진입 시 빈 채팅창 대신 `WelcomeView`를 보여주고, 퀵스타트 칩을 누르면 바로 메시지가 전송되도록 했다.

---

## 이슈 4: ChatGPT API 500 에러

로컬에서 첫 메시지를 보냈더니 서버에서 500이 떨어졌다.

> `chatGPT api 500 error 수정` — 17:29
> 5 files changed, 168 insertions(+), 103 deletions(-)

원인은 `responder.py`의 history 처리였다. 프론트에서 보낸 history 배열 안에 `role`이나 `content` 키가 없거나, role이 OpenAI 스펙에 맞지 않는 경우 OpenAI API가 400을 뱉고, FastAPI가 이를 잡지 못해 500으로 터졌다.

**수정 전:**
```python
messages = [
    {"role": "system", "content": "..."},
    *history,   # ← 검증 없이 그냥 풀어버림
    {"role": "user", "content": message}
]
```

**수정 후:**
```python
safe_history = [
    {"role": str(m.get("role", "")), "content": str(m.get("content", ""))}
    for m in history
    if isinstance(m, dict)
    and m.get("role") in ("user", "assistant")  # role 검증
    and m.get("content", "").strip()             # 빈 content 제거
]
```

스트리밍 중 에러가 나도 앱이 멈추지 않도록 `try/except`도 감쌌다.

```python
try:
    stream = await client.chat.completions.create(...)
    async for chunk in stream:
        delta = chunk.choices[0].delta.content
        if delta:
            yield delta
except Exception as e:
    yield f"죄송합니다, 응답 생성 중 오류가 발생했습니다. ({type(e).__name__})"
    return
```

수정 후 재테스트. "강남 스튜디오 추천해줘" 입력 → intent 파싱 → 업체 필터 → AI 응답 스트리밍. **연동 성공.**

이날 오후 머지한 PR.

| 시각 | PR |
|------|----|
| 15:30 | PR #35 — 타임라인 UI 완성 |
| 16:20 | PR #36 — 업체 리스트 UI |
| 16:49 | PR #38 — 로그인·회원가입 화면 UI |

---

## 오늘을 돌아보며

프론트엔드(React Native)와 백엔드(FastAPI)를 동시에 로컬에 띄워서 연동하는 과정에서 패키지, 버전, 환경 변수 등 예상치 못한 에러가 많이 발생했다. 하지만 원인을 하나씩 분석하며 해결해나가는 과정이 뜻깊었다.

오늘 발생한 이슈 4개는 전부 **환경 차이**에서 비롯됐다.
- OS가 다르면 쉘 문법이 다르고 (`&&` vs `;`)
- Python을 돌리면 pycache가 생기고
- 패키지를 추가하면 버전 충돌이 나고
- 인터페이스를 맞추지 않으면 런타임에 터진다

코드보다 환경에서 더 많이 배운 하루였다.

내일은 AI 응답 품질을 높이고, 업체 추천 외에 일정 등록 같은 인텐트도 처리하도록 고도화할 예정이다.

---

## 화면 기록

이날은 AI 채팅을 실제 앱 화면에 붙여서 동작시키는 흐름이 핵심이었다. 채팅 목록, 초대 코드, AI 대화 화면을 같이 보면 Day 3에서 연결한 기능 범위가 한 번에 보인다.

<img src="/images/posts/mongle/couple-chat-list-screen.png" alt="Mongle couple 채팅 목록 화면" style="display:block; max-width:360px; width:auto; height:auto; margin:1rem auto;" />

<img src="/images/posts/mongle/couple-chat-invite-code.png" alt="Mongle couple 채팅 초대 코드 화면" style="display:block; max-width:360px; width:auto; height:auto; margin:1rem auto;" />

<img src="/images/posts/mongle/couple-ai-chat-screen.png" alt="Mongle couple AI 챗봇 화면" style="display:block; max-width:360px; width:auto; height:auto; margin:1rem auto;" />

---

**GitHub:** [APP-Project-Team1/mongle](https://github.com/APP-Project-Team1/mongle)
---

## AI 채팅 기능을 붙이며 체감한 제품화의 차이

챗봇 데모를 만드는 것과 실제 서비스 안에 AI 대화를 녹이는 것은 전혀 다른 문제였다. 사용자는 모델이 똑똑한지보다, 지금 내 질문을 이해했는지, 응답이 믿을 만한지, 다음 행동으로 이어질 수 있는지를 먼저 체감한다. 그래서 Day 3에서 중요했던 것은 API 연결 자체보다도 맥락 유지, 실패 응답 처리, 너무 일반적인 답변을 줄이는 방식 같은 제품화 요소였다.

웨딩 준비 맥락에서는 특히 질문의 의도가 넓고 감정적이다. "예산이 부족한데 어떻게 줄일까" 같은 질문은 단순 FAQ 답변으로 해결되지 않는다. 이럴수록 AI 응답이 정보 요약을 넘어서 선택지를 구조화해 주는 방향으로 가야 했다. 이 경험은 이후 RAG와 예산 기능을 붙일 때도 그대로 연결됐다.
