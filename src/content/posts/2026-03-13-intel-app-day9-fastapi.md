---
title: "[App Dev] Day 9: FastAPI 기본, Python으로 백엔드 서버를 만들다"
slug: intel-app-day9-fastapi
date: 2026-03-13
author: Evan Yoon
category: study
subcategory: bootcamp
description: "Intel AI 앱 개발 9일차. FastAPI로 API 서버를 만들고 React 프론트엔드와 연결하면서 CORS와 엔드포인트 설계를 직접 익혔다."
thumbnail: ""
tags:
  - fastapi
  - python
  - backend
  - cors
  - api
  - bootcamp
readTime: 10
series: "Intel App Dev 2026"
seriesOrder: 9
featured: false
draft: false
toc: true
---

## 이번엔 서버를 직접 만든다

지금까지는 외부 API(JSONPlaceholder, 고양이 이미지)에서 데이터를 가져왔다. 오늘은 반대편, 그 데이터를 제공하는 **서버**를 직접 만드는 시간이었다. 언어는 Python, 프레임워크는 **FastAPI**다.

Python을 이미 배웠기 때문에 문법은 낯설지 않았다. 그런데 "함수를 만드는 것"과 "API 엔드포인트를 만드는 것"이 어떻게 다른지, CORS가 뭔지, 프론트와 백이 어떻게 연결되는지 — 이 개념들이 오늘 새로 연결됐다.

---

## FastAPI란

FastAPI는 Python 기반의 웹 API 프레임워크다. 빠르고, 자동으로 API 문서를 생성해주며, 타입 힌트를 활용한 데이터 검증을 지원한다.

Flask보다 현대적이고, Django보다 가볍다. AI 모델을 API로 감싸서 제공하는 용도로 특히 많이 쓰인다. 우리가 만든 프로젝트에서도 Python ML 모델의 결과를 React 프론트에 제공하는 역할을 한다.

---

## 설치 및 실행

```bash
pip install fastapi uvicorn
```

`uvicorn`은 FastAPI를 실제로 실행하는 ASGI 서버다.

```bash
uvicorn server.main:app --reload
```

`--reload`는 코드를 수정하면 서버가 자동으로 재시작되는 개발 모드 옵션이다. 기본 포트는 `8000`이다.

---

## 기본 구조

```python
# server/main.py
from fastapi import FastAPI

app = FastAPI()

@app.get("/")
def read_root():
    return {"message": "Welcome to FastAPI"}
```

함수 위에 `@app.get("/")` 같은 데코레이터를 붙이면 그 경로로 들어오는 GET 요청을 처리하는 엔드포인트가 된다. 반환값은 딕셔너리 그대로 JSON으로 변환된다.

서버를 실행하고 브라우저에서 `http://localhost:8000`을 열면 `{"message": "Welcome to FastAPI"}`가 보인다. 아무 HTML 없이 JSON만 반환하는 게 처음에는 허무하게 느껴졌지만, 이게 API 서버가 하는 일이다.

---

## 자동 API 문서

FastAPI의 킬러 피처 중 하나가 자동 문서 생성이다. 서버를 켜면 두 가지 문서 URL이 자동으로 생긴다.

- `http://localhost:8000/docs` — Swagger UI
- `http://localhost:8000/redoc` — ReDoc 스타일

코드를 작성하면 문서가 자동으로 업데이트된다. 팀원들과 API 명세를 따로 문서화하지 않아도 된다. 개발 중에 API 명세를 별도로 관리하지 않아도 된다는 점이 실용적이다.

---

## 엔드포인트 추가

프로젝트에 실제로 추가한 엔드포인트들이다.

```python
import random
from fastapi import FastAPI

app = FastAPI()

@app.get("/")
def read_root():
    return {"message": "Welcome"}

@app.get("/about")
def about():
    return {
        "name": "Evan Yoon",
        "phone": "010-xxxx-xxxx",
        "email": "evan@example.com",
    }

@app.get("/animal")
def random_animal():
    animals = [
        {"animal": "고양이", "characteristic": "독립적이고 우아하다"},
        {"animal": "강아지", "characteristic": "충성스럽고 활발하다"},
        {"animal": "토끼", "characteristic": "조용하고 귀엽다"},
        {"animal": "앵무새", "characteristic": "수다스럽고 영리하다"},
    ]
    return random.choice(animals)
```

`/animal` 엔드포인트는 매 요청마다 랜덤 동물 정보를 반환한다. 프론트엔드에서 버튼을 누를 때마다 다른 동물이 나오는 기능을 이 API로 구현했다.

---

## CORS 설정

가장 중요한 설정이 CORS다. CORS(Cross-Origin Resource Sharing)는 브라우저가 다른 도메인의 서버에 요청하는 것을 제한하는 보안 정책이다.

React 개발 서버는 `http://localhost:5173`에서 실행되고, FastAPI 서버는 `http://localhost:8000`에서 실행된다. 포트가 다르면 "다른 출처(Origin)"로 간주된다. CORS 설정이 없으면 React에서 FastAPI로 보내는 요청이 브라우저에서 차단된다.

```python
from fastapi.middleware.cors import CORSMiddleware

app.add_middleware(
    CORSMiddleware,
    allow_origins=[
        "http://localhost:5173",  # React Vite 기본 포트
        "http://localhost:5174",  # Vite가 5173 사용 중이면 5174로 이동
    ],
    allow_credentials=True,
    allow_methods=["*"],     # GET, POST, PUT, DELETE 등 모두 허용
    allow_headers=["*"],
)
```

처음에 이 설정을 빠뜨렸다가 프론트에서 요청할 때 콘솔에 빨간 CORS 에러가 뜨는 걸 경험했다. 에러 메시지가 길고 낯설어서 당황했지만, CORS 설정을 추가하니 바로 해결됐다.

---

## React에서 FastAPI 연결

`RandomAnimal.jsx`는 FastAPI의 `/animal` 엔드포인트를 호출하는 컴포넌트다.

```jsx
function RandomAnimal() {
  const [animal, setAnimal] = useState(null);
  const [loading, setLoading] = useState(false);

  const fetchAnimal = async () => {
    setLoading(true);
    try {
      const res = await fetch("http://localhost:8000/animal");
      const data = await res.json();
      setAnimal(data);
    } catch (err) {
      console.error("서버 연결 실패:", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="text-center p-8">
      {animal && (
        <div className="mb-6 p-6 bg-gray-50 rounded-xl">
          <h2 className="text-3xl font-bold mb-2">{animal.animal}</h2>
          <p
            className="text-lg"
            style={{ color: animal.color || "#6b7280" }}
          >
            {animal.characteristic}
          </p>
        </div>
      )}
      <button
        onClick={fetchAnimal}
        disabled={loading}
        className="px-6 py-3 bg-purple-500 text-white rounded-lg hover:bg-purple-600 disabled:opacity-50"
      >
        {loading ? "불러오는 중..." : "랜덤 동물 뽑기"}
      </button>
    </div>
  );
}
```

외부 API와 완전히 동일한 패턴이다. URL만 `http://localhost:8000/animal`로 바꿨을 뿐이다. 서버를 직접 만들고 프론트가 그 서버와 통신하는 전체 흐름을 처음으로 완성했다.

---

## Path Parameter와 Query Parameter

오늘 기초만 맛봤지만, FastAPI에서 동적 데이터를 받는 방법도 확인했다.

```python
# Path Parameter: 경로 자체에 변수가 들어감
@app.get("/posts/{post_id}")
def get_post(post_id: int):
    return {"post_id": post_id, "title": f"Post {post_id}"}

# Query Parameter: 경로 뒤에 ?key=value 형태
@app.get("/search")
def search(keyword: str, limit: int = 10):
    return {"keyword": keyword, "limit": limit}
```

`http://localhost:8000/posts/3` → `{"post_id": 3, "title": "Post 3"}`  
`http://localhost:8000/search?keyword=python&limit=5` → `{"keyword": "python", "limit": 5}`

Python 타입 힌트(`: int`, `: str`)를 쓰면 FastAPI가 자동으로 타입 변환과 유효성 검사를 해준다. `post_id`에 숫자가 아닌 문자를 넣으면 자동으로 422 에러를 반환한다.

---

## 프론트와 백을 동시에 실행하기

`package.json`에 `concurrently`를 써서 React와 FastAPI를 한 번에 실행했다.

```json
{
  "scripts": {
    "dev": "concurrently \"npm run react\" \"npm run server\"",
    "react": "cd react-web && npm run dev",
    "server": "uvicorn server.main:app --reload"
  }
}
```

루트에서 `npm run dev` 하나로 React와 FastAPI가 동시에 올라온다. 이전에는 터미널을 두 개 열어서 각각 실행해야 했는데, 이 설정으로 훨씬 편해졌다.

---

## 헷갈렸던 점

**포트 번호 기억**: React는 `5173`, FastAPI는 `8000`. 프론트에서 API URL을 틀리게 쓰면 연결이 안 된다.

**CORS는 브라우저 정책이다**: Postman이나 curl로 요청하면 CORS 없이도 된다. CORS는 브라우저가 보안을 위해 적용하는 정책이기 때문에, 브라우저를 거치지 않는 요청에는 적용되지 않는다. "Postman에서는 되는데 React에서 안 된다"면 CORS 문제다.

**`--reload` 옵션**: 개발 중에는 편하지만 파일이 저장될 때마다 서버가 재시작된다. 재시작하는 동안 잠깐 연결이 끊기는 경우가 있다.

---

## 복습용 질문

<details>
<summary>1. CORS가 필요한 이유는 무엇인가?</summary>

브라우저는 보안상 다른 출처(origin)의 서버에 자유롭게 요청하는 것을 제한한다. 출처는 프로토콜+도메인+포트 조합이다. 서버가 `Access-Control-Allow-Origin` 헤더로 허용할 출처를 명시해야 브라우저가 요청을 통과시킨다.

</details>

<details>
<summary>2. FastAPI에서 딕셔너리를 return하면 자동으로 JSON이 되는 이유는?</summary>

FastAPI가 내부적으로 딕셔너리를 JSON으로 직렬화해서 `Content-Type: application/json` 헤더와 함께 응답한다. 별도로 jsonify나 Response 객체를 만들 필요가 없다.

</details>

<details>
<summary>3. Path Parameter와 Query Parameter의 차이는?</summary>

Path Parameter는 URL 경로의 일부(`/posts/{id}`). Query Parameter는 URL 뒤에 `?key=value` 형태로 붙는다. 일반적으로 특정 리소스를 식별할 때는 Path, 검색·필터·정렬 같은 옵션은 Query를 쓴다.

</details>

---

## 한 줄 정리

Python으로 FastAPI 서버를 만들고 React와 연결하는 전체 흐름을 처음으로 완성했다. AI 모델도 이 구조로 감싸면 어디서든 호출할 수 있다. 다음 주부터는 React Native로 넘어가 모바일 앱을 만든다.
