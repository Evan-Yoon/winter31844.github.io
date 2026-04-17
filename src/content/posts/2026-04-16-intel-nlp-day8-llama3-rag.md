---
title: "[NLP] day 8: LLaMA 3.2 실행과 RAG 파이프라인 구현"
slug: intel-nlp-day8-llama3-rag
date: 2026-04-16
author: Evan Yoon
category: study
subcategory: bootcamp
description: |
  2026년 4월 16일 수업에서는 Meta의 LLaMA 3.2-3B-Instruct를 Colab에서 직접 로드하고,
  4-bit 양자화 설정부터 text-generation 파이프라인 구성까지 실습했다.
  이후 RAG(Retrieval Augmented Generation) 개념과 구성 요소를 학습하고,
  LangChain으로 PDF 문서 기반 Q&A 시스템을 직접 구현했다.
thumbnail: ""
tags:
  - NLP
  - LLaMA
  - RAG
  - LangChain
  - quantization
  - vector-store
  - FAISS
  - ChromaDB
  - HuggingFace
  - nlp-study
readTime: 13
series: "Intel NLP 과정"
seriesOrder: 8
featured: false
draft: false
toc: true
---

2026년 4월 16일 수업은 두 파트로 구성됐다. 앞부분은 Meta의 LLaMA 3.2를 직접 Colab에서 실행하는 실습이었다. 뒷부분은 RAG(Retrieval Augmented Generation)였다. LLM이 "모르는 것"을 외부 문서로 보완하는 구조를 배우고, LangChain으로 PDF 문서 기반 Q&A 시스템을 구현했다.

## 1. LLaMA 3.2 모델 구성

### Meta-Llama 모델 라인업

HuggingFace `meta-llama` 조직에는 현재 세 계열이 있다.

- **Llama 3.2**: 1B, 3B 다국어 텍스트 모델 (text → text)
- **Llama 3.2 Vision**: 11B, 90B 멀티모달 모델 (text + image → text)
- **Llama 3.1**: 8B~405B, ~15조 토큰으로 사전학습된 텍스트 모델

이번 실습에서 사용한 모델: `meta-llama/Llama-3.2-3B-Instruct`. Gated 모델이라 HuggingFace에서 라이선스 동의가 필요하다.

### Base 모델 vs Instruct 모델

같은 3B 파라미터라도 두 버전은 다르게 동작한다.

<div class="compare-wrap">
  <div class="compare-card compare-before">
    <div class="compare-label">Llama-3.2-3B (Base)</div>
    <div class="compare-body">
      <p><strong>유형:</strong> Pretrained Model</p>
      <p><strong>학습 방식:</strong> 다음 단어 예측</p>
      <p><strong>응답 방식:</strong> 텍스트를 이어쓸 뿐</p>
      <p><strong>활용:</strong> Fine-Tuning 기반 특화 작업</p>
    </div>
  </div>
  <div class="compare-card compare-after">
    <div class="compare-label">Llama-3.2-3B-Instruct</div>
    <div class="compare-body">
      <p><strong>유형:</strong> Instruction Tuned Model</p>
      <p><strong>학습 방식:</strong> 지시사항 따르도록 추가 학습</p>
      <p><strong>응답 방식:</strong> 질문에 직접 답변</p>
      <p><strong>활용:</strong> 챗봇, 바로 사용 가능</p>
    </div>
  </div>
</div>

Base 모델에 "서울을 설명해줘"라고 입력하면 문장을 이어 쓰는 수준으로 답한다. Instruct 모델은 구조화된 답변을 생성한다.

### Colab 환경 설정

LLaMA 3B는 GPU가 필요하다. Colab에서 런타임 유형을 **T4 GPU**로 변경해야 한다.

HuggingFace Access Token을 코드에 직접 입력하면 GitHub에 올렸을 때 토큰이 노출된다. Colab Secrets에 저장하고 코드로 불러온다.

```python
from google.colab import userdata
HF_TOKEN = userdata.get('Huggingface')  # Colab Secrets에서 안전하게 로드
```

```python
import torch
from transformers import AutoTokenizer, AutoModelForCausalLM, BitsAndBytesConfig, pipeline

model_name = 'unsloth/Llama-3.2-3B-Instruct'
# unsloth/ prefix는 학습 최적화 버전. 가중치는 meta-llama와 동일하다.
```

## 2. 4-bit 양자화 (Quantization)

**이게 뭔지**: 모델 파라미터를 32비트(float32)에서 4비트로 줄여 메모리를 절약하는 기법.  
**왜 필요한가**: 3B 모델을 float32로 로드하면 약 12GB VRAM이 필요하다. T4 GPU의 VRAM은 16GB뿐이다. 4-bit 양자화로 약 3GB로 줄일 수 있다.

```python
bnb_config = BitsAndBytesConfig(
    load_in_4bit=True,                        # 파라미터를 4비트로 양자화
    bnb_4bit_use_double_quant=True,           # 추가 압축 (메모리 더 절약)
    bnb_4bit_quant_type='nf4',                # NF4 방식 (int4보다 정확도 높음)
    bnb_4bit_compute_dtype=torch.bfloat16     # 실제 연산은 bfloat16으로
)
```

**bfloat16 vs FP16**: 두 형식 모두 16비트를 쓰지만 구성이 다르다.
- `bfloat16`: 지수부 8비트, 가수부 7비트 → 큰 숫자 표현 가능, 학습 중 overflow 위험 적음
- `FP16`: 지수부 5비트, 가수부 10비트 → 정밀도 높지만 overflow 위험 있음

학습 안정성이 중요한 상황에서 `bfloat16`이 더 적합하다.

## 3. 모델 로드와 Pipeline 생성

```python
tokenizer = AutoTokenizer.from_pretrained(model_name, token=HF_TOKEN)
tokenizer.pad_token = tokenizer.eos_token
# Llama 계열은 기본 pad_token이 없음. 배치 처리 시 패딩이 필요하므로 eos_token으로 대체.

model = AutoModelForCausalLM.from_pretrained(
    model_name,
    device_map="auto",            # GPU/CPU 자동 할당
    quantization_config=bnb_config,
    token=HF_TOKEN
)
# 모델 파일 4개 shard (총 약 4.98GB) 다운로드 후 로드
```

```python
text_generator = pipeline(
    "text-generation",
    model=model_name,
    tokenizer=tokenizer,
    max_new_tokens=128    # 생성할 최대 토큰 수
)
```

주요 Pipeline 타입:

<div class="compare-wrap">
  <div class="compare-card">
    <div class="compare-label">text-generation</div>
    <div class="compare-body"><p>텍스트 생성, 이어쓰기</p></div>
  </div>
  <div class="compare-card">
    <div class="compare-label">text-classification</div>
    <div class="compare-body"><p>감정 분석, 카테고리 분류</p></div>
  </div>
  <div class="compare-card">
    <div class="compare-label">ner</div>
    <div class="compare-body"><p>개체명 인식</p></div>
  </div>
  <div class="compare-card">
    <div class="compare-label">question-answering</div>
    <div class="compare-body"><p>컨텍스트 기반 Q&A</p></div>
  </div>
  <div class="compare-card">
    <div class="compare-label">summarization</div>
    <div class="compare-body"><p>요약</p></div>
  </div>
</div>

### 추론 테스트

```python
def get_response(prompt):
    sequences = text_generator(prompt)
    return sequences[0]['generated_text']

# 영어
get_response('What is Machine Learning?')
# 'What is Machine Learning? (A Beginner's Guide)\nMachine learning is a subset of artificial intelligence...'

# 한국어
get_response('서울에 대해서 한글로 설명해 주세요')
# '서울에 대해서 한글로 설명해 주세요.\n서울은 한국의 수도와 가장 큰 도시입니다...'
```

Llama 3.2는 영어 외 8개 언어를 공식 지원한다. 한국어 입력에 한국어로 응답이 나온다.

### 텍스트 분류를 우회하는 방법

`text-classification` 파이프라인을 그대로 쓰면 에러가 난다.

```
The model 'LlamaForCausalLM' is not supported for text-classification.
```

LlamaForCausalLM은 생성 전용 아키텍처라 분류 헤드가 없다. 대신 분류 작업을 프롬프트로 구성해서 text-generation 파이프라인으로 처리한다.

```python
text = 'I love using Hugging Face!'
prompt = f"""This is a sentiment analysis task.
Categorize the following sentence into one of three sentiments: positive, negative, or neutral.
Sentence: {text}
Answer:"""

response = get_response(prompt)
# '...The sentiment of this sentence is positive...'
```

프롬프트를 잘 구성하면 파이프라인이 직접 지원하지 않는 태스크도 처리할 수 있다. 이것이 프롬프트 엔지니어링이다.

## 4. RAG (Retrieval Augmented Generation)

### LLM의 한계와 RAG의 역할

**LLM의 한계**: 학습 시점 이후의 정보는 알지 못한다. 사내 문서, 최신 뉴스, 특정 도메인 데이터는 포함되지 않는다.

**RAG**: 질문이 들어오면 외부 문서에서 관련 내용을 검색해서 LLM에 컨텍스트로 제공한다. 모델 재학습 없이 지식을 확장할 수 있다.

Fine-Tuning과의 차이: Fine-Tuning은 모델 파라미터 자체를 업데이트한다. RAG는 파라미터는 그대로 두고 검색으로 보완한다. 비용이 훨씬 적게 든다.

### RAG 5단계 파이프라인

```
1. 로드     외부 문서(PDF, 웹페이지 등)를 Document 객체로 불러오기
     ↓
2. 분할     긴 문서를 검색 가능한 작은 청크로 나누기
     ↓
3. 임베딩   각 청크를 벡터로 변환해 Vector Store에 저장
     ↓
4. 검색     사용자 질문을 벡터로 변환 후 유사 청크 검색
     ↓
5. 생성     질문 + 검색된 청크를 합쳐 LLM에 전달, 답변 생성
```

## 5. Document Loaders

**이게 뭔지**: 다양한 형태의 문서를 LangChain Document 객체로 불러오는 모듈.  
각 Document 객체는 `page_content`(텍스트)와 `metadata`(출처, 페이지 번호 등)로 구성된다.

```python
from langchain_community.document_loaders import PyPDFLoader

loader = PyPDFLoader('New2026.pdf')
pages  = loader.load()

# pages[3].page_content → 4번째 페이지 텍스트
# pages[3].metadata    → {'source': 'New2026.pdf', 'page': 3}
```

웹 페이지 로더 두 종류:
- `WebBaseLoader`: 원시 HTML 그대로 수집 (노이즈 많음)
- `UnstructuredURLLoader`: 구조화된 텍스트만 추출 (NLP 작업에 더 적합)

## 6. Text Splitters

**왜 분할이 필요한가**: LLM은 한 번에 처리할 수 있는 토큰 수(컨텍스트 길이)가 제한되어 있다. 또한 청크가 작아야 검색 정확도가 높아진다. 청크 하나가 벡터 하나로 임베딩된다.

**CharacterTextSplitter**: 지정한 구분자 1개를 기준으로 분할. 구분자가 없으면 chunk_size를 초과할 수 있다.

```python
from langchain.text_splitter import CharacterTextSplitter

splitter = CharacterTextSplitter(
    separator='\n\n',    # 단락 구분자
    chunk_size=500,      # 목표 청크 크기 (문자 수)
    chunk_overlap=100,   # 청크 간 겹침 — 경계에서 문맥이 잘리는 것 방지
    length_function=len
)
```

**RecursiveCharacterTextSplitter**: 여러 구분자를 순서대로 시도해 chunk_size를 최대한 준수한다.

분할 시도 순서: `\n\n`(단락) → `\n`(줄) → `" "`(단어) → 각 문자

```python
from langchain.text_splitter import RecursiveCharacterTextSplitter

splitter = RecursiveCharacterTextSplitter(
    chunk_size=500,
    chunk_overlap=200
)
```

<div class="compare-wrap">
  <div class="compare-card compare-before">
    <div class="compare-label">CharacterTextSplitter</div>
    <div class="compare-body">
      <p><strong>구분자:</strong> 1개 고정</p>
      <p><strong>chunk_size 준수:</strong> 구분자 없으면 초과 가능</p>
      <p><strong>사용 시점:</strong> 구분자가 명확한 구조화된 텍스트</p>
    </div>
  </div>
  <div class="compare-card compare-after">
    <div class="compare-label">RecursiveCharacterTextSplitter</div>
    <div class="compare-body">
      <p><strong>구분자:</strong> 여러 개 순차 시도</p>
      <p><strong>chunk_size 준수:</strong> 거의 초과하지 않음</p>
      <p><strong>사용 시점:</strong> 일반적인 경우 (권장)</p>
    </div>
  </div>
</div>

`chunk_overlap`이 필요한 이유: 청크 경계에서 문장이 잘리면 의미가 분리된다. 겹치는 부분을 두면 앞 청크의 끝과 뒤 청크의 시작이 연결된 맥락을 가진다.

## 7. Text Embeddings

**이게 뭔지**: 텍스트 청크를 고정 크기의 숫자 벡터로 변환하는 작업.  
**왜 필요한가**: 벡터 간 코사인 유사도나 거리를 계산해서 "의미적으로 가까운 청크"를 검색할 수 있다.

<div class="compare-wrap">
  <div class="compare-card">
    <div class="compare-label">OpenAI Embeddings</div>
    <div class="compare-body">
      <p><strong>차원:</strong> 1536</p>
      <p>유료, 한/영 모두 지원</p>
    </div>
  </div>
  <div class="compare-card">
    <div class="compare-label">BAAI/bge-m3</div>
    <div class="compare-body">
      <p><strong>차원:</strong> 1024</p>
      <p>무료, 한/영 모두 지원</p>
    </div>
  </div>
  <div class="compare-card">
    <div class="compare-label">jhgan/ko-sbert-nli</div>
    <div class="compare-body">
      <p><strong>차원:</strong> 768</p>
      <p>무료, 한국어 특화</p>
    </div>
  </div>
</div>

```python
from langchain.embeddings import HuggingFaceEmbeddings

ko_hf = HuggingFaceEmbeddings(model_name='BAAI/bge-m3')
# 또는 한국어 특화:
# ko_hf = HuggingFaceEmbeddings(model_name='jhgan/ko-sbert-nli')
```

모델 성능 비교는 HuggingFace MTEB Leaderboard에서 확인할 수 있다.

## 8. Vector Stores

**이게 뭔지**: 임베딩 벡터를 저장하고 빠르게 유사도 검색을 수행하는 데이터베이스.  
**왜 일반 DB가 아닌가**: 일반 DB는 정확한 값을 검색한다. Vector Store는 "이 벡터와 가장 비슷한 벡터를 찾아줘"를 고속으로 처리한다.

<div class="compare-wrap">
  <div class="compare-card compare-before">
    <div class="compare-label">ChromaDB</div>
    <div class="compare-body">
      <p><strong>저장 방식:</strong> 디스크 기반 DB</p>
      <p><strong>메타데이터 필터:</strong> 지원</p>
      <p><strong>검색 속도:</strong> 빠름</p>
      <p><strong>용도:</strong> RAG, 챗봇 (필터 필요 시)</p>
    </div>
  </div>
  <div class="compare-card compare-after">
    <div class="compare-label">FAISS</div>
    <div class="compare-body">
      <p><strong>저장 방식:</strong> RAM 기반 (저장 가능)</p>
      <p><strong>메타데이터 필터:</strong> 지원 안함</p>
      <p><strong>검색 속도:</strong> 매우 빠름</p>
      <p><strong>용도:</strong> 대규모 고속 검색</p>
    </div>
  </div>
</div>

**Chroma 사용 예시:**

```python
from langchain.vectorstores import Chroma

# 메모리에만 저장 (세션 종료 시 사라짐)
db = Chroma.from_documents(docs, ko_hf)

# 디스크에 영구 저장
db2 = Chroma.from_documents(docs, ko_hf, persist_directory="./chroma_db")

# 기존 DB 로드
db3 = Chroma(persist_directory="./chroma_db", embedding_function=ko_hf)
```

**FAISS 사용 예시:**

```python
from langchain.vectorstores import FAISS

db = FAISS.from_documents(docs, ko_hf)
db.save_local('faiss_index')

# 로드 — allow_dangerous_deserialization은 신뢰할 수 있는 파일에만 True
new_db = FAISS.load_local('faiss_index', ko_hf, allow_dangerous_deserialization=True)
```

유사도 검색 방법:

```python
# 상위 k개 결과와 유사도 점수 함께 반환
docs = db3.similarity_search_with_relevance_scores(query, k=3)
print(docs[0][0].page_content)  # 가장 유사한 청크 텍스트
print(docs[0][1])               # 유사도 점수

# MMR 검색: 유사성과 다양성을 동시에 고려
# 비슷한 청크가 k개 모두 나오는 것을 방지
docs = new_db.max_marginal_relevance_search(query, k=3)
```

MMR(Maximal Marginal Relevance): 이미 선택된 결과와 너무 비슷한 문서는 제외한다. 결과의 다양성을 높인다.

## 9. Chain Types와 LCEL 체인 구성

검색된 청크를 LLM에 어떻게 전달할지에 따라 4가지 방식이 있다.

<div class="compare-wrap">
  <div class="compare-card">
    <div class="compare-label">Stuff</div>
    <div class="compare-body">
      <p><strong>방식:</strong> 청크 전체를 하나로 합쳐 전달</p>
      <p><strong>적합한 상황:</strong> 청크가 적고 토큰 한계 여유 있을 때</p>
    </div>
  </div>
  <div class="compare-card">
    <div class="compare-label">Map-Reduce</div>
    <div class="compare-body">
      <p><strong>방식:</strong> 각 청크 개별 처리 후 결과 합산</p>
      <p><strong>적합한 상황:</strong> 대용량 문서셋</p>
    </div>
  </div>
  <div class="compare-card">
    <div class="compare-label">Refine</div>
    <div class="compare-body">
      <p><strong>방식:</strong> 첫 청크로 초안 작성, 이후 청크로 순차 개선</p>
      <p><strong>적합한 상황:</strong> 품질 중요, 점진적 개선</p>
    </div>
  </div>
  <div class="compare-card">
    <div class="compare-label">Map-Retrieve</div>
    <div class="compare-body">
      <p><strong>방식:</strong> 쿼리를 섹션별로 분리해 각 관련 부분 검색</p>
      <p><strong>적합한 상황:</strong> 복합 질문</p>
    </div>
  </div>
</div>

### ChatGPT를 LLM으로 사용하는 LCEL 체인

```python
from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import StrOutputParser
from langchain_core.runnables import RunnablePassthrough

# 1. Retriever: 쿼리를 받아 유사 청크 반환
retriever = db.as_retriever(search_type="similarity", search_kwargs={"k": 3})

# 2. LLM 설정
llm = ChatOpenAI(model="gpt-4o-mini", temperature=0, api_key=gptkey)

# 3. 프롬프트 템플릿
template = """당신은 질문에 충실히 답변하는 전문가입니다.
{context}

Question: {question}
Answer:"""
prompt = ChatPromptTemplate.from_template(template)

def format_docs(docs):
    # 검색된 청크들을 하나의 문자열로 합치기
    return "\n\n".join(doc.page_content for doc in docs)

# 4. LCEL 체인: 파이프(|)로 연결
rag_chain = (
    {"context": retriever | format_docs, "question": RunnablePassthrough()}
    | prompt    # 딕셔너리를 채워 완성된 프롬프트 생성
    | llm       # LLM이 응답 생성
    | StrOutputParser()  # 응답에서 순수 텍스트 추출
)
```

실행 순서:
1. 사용자 질문이 `retriever | format_docs`를 통해 context로 변환
2. `RunnablePassthrough()`로 질문 자체도 그대로 통과
3. `ChatPromptTemplate`이 context + question을 합쳐 완성된 프롬프트 생성
4. LLM이 응답 생성
5. `StrOutputParser()`가 순수 문자열만 추출

```python
query    = "2026년 청년의 자금 마련을 위한 정책은?"
response = rag_chain.invoke(query)
# 2026년 청년의 자금 마련을 위한 정책으로는 **청년미래적금**과 **청년일자리도약장려금**이 있습니다.
# 1. **청년미래적금**: 신설, 가입기간 3년, 정부기여금 지원 비율 (일반형) 6% ...
```

### Llama 3.2를 LLM으로 사용하는 경우

HuggingFace pipeline은 LangChain Runnable 인터페이스를 지원하지 않는다. `RunnableLambda`로 감싸서 체인에 연결한다.

```python
from langchain_core.runnables import RunnableLambda

def custom_llm_invoke(input_data):
    # PromptValue 객체를 문자열로 변환
    prompt_text = input_data.to_string()
    sequences   = text_generator(prompt_text)
    return sequences[0]['generated_text']

# 일반 함수를 LangChain이 이해하는 Runnable 객체로 변환
llm = RunnableLambda(custom_llm_invoke)
```

이후 체인 구성은 ChatGPT 버전과 동일하다. `llm` 변수만 교체하면 된다.

---

## 핵심 정리

```
LLaMA 실행 흐름:
HF Token 설정 → BitsAndBytesConfig (4-bit 양자화) → AutoModelForCausalLM 로드
→ pipeline("text-generation") → prompt 설계 → 응답 생성

RAG 파이프라인:
문서 로드 (PyPDFLoader)
→ 청크 분할 (RecursiveCharacterTextSplitter)
→ 임베딩 (HuggingFaceEmbeddings)
→ Vector Store 저장 (Chroma / FAISS)
→ 검색 (similarity_search / MMR)
→ LLM 생성 (LCEL 체인)
```

day 7에서 임베딩이 의미 공간에서 어떤 구조를 가지는지 확인했다면, day 8에서는 그 임베딩을 실제 검색 시스템에서 어떻게 활용하는지로 연결된다. RAG는 지금까지 배운 임베딩, 유사도 검색, LLM을 하나로 이어붙인 구조다.
