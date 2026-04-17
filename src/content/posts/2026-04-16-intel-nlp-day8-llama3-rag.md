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

2026년 4월 16일 수업은 두 파트로 구성됐다. 앞부분은 Meta의 LLaMA 3.2 모델을 직접 Colab에서 실행하는 실습이었다. 모델 라이선스 확인, HuggingFace Access Token 설정, 4-bit 양자화 구성, text-generation 파이프라인 생성까지 전 과정을 진행했다. 뒷부분은 RAG(Retrieval Augmented Generation)였다. LLM의 지식 한계를 외부 문서로 보완하는 구조를 배우고, LangChain으로 PDF 문서 기반 Q&A 시스템을 구현했다.

## 1. LLaMA 3.2 모델 구성

### Meta-Llama 모델 라인업

HuggingFace의 `meta-llama` 조직에는 현재 세 계열이 있다.

- **Llama 3.2**: 1B, 3B 사이즈의 다국어 텍스트 모델 (text in / text out)
- **Llama 3.2 Vision**: 11B, 90B 사이즈의 멀티모달 모델 (text + image in / text out)
- **Llama 3.1**: 8B~405B 파라미터, ~15조 토큰으로 사전학습된 텍스트 모델

이번 실습에서 사용한 모델은 `meta-llama/Llama-3.2-3B-Instruct`다. Gated 모델이라 사용 전에 HuggingFace에서 라이선스 동의가 필요하다.

### Base vs. Instruct 모델

같은 3B 파라미터라도 두 버전의 특성이 다르다.

| 특성 | Llama-3.2-3B | Llama-3.2-3B-Instruct |
|---|---|---|
| 모델 유형 | Pretrained Model | Instruction Tuned Model |
| 주요 목적 | 일반적인 언어 모델링 및 텍스트 생성 | 사용자 지시/질문에 대한 구조화된 응답 |
| 응답 특성 | 다음 단어 예측 중점, 직접 응답 제한적 | 명령/질문에 직접 응답하도록 최적화 |
| 활용 사례 | Fine-Tuning 기반 특화 작업 | 챗봇, 가상 비서 등 바로 활용 가능 |

### Colab 환경 설정

LLaMA 3B 모델을 돌리려면 GPU가 필요하다. Colab에서 런타임 유형을 **T4 GPU**로 변경해서 사용했다.

HuggingFace Access Token은 Colab의 보안 비밀(Secrets)에 저장한다. 코드에 토큰을 직접 하드코딩하지 않고 다음처럼 불러온다.

```python
from google.colab import userdata
HF_TOKEN = userdata.get('Huggingface')
```

### 모듈 임포트 및 모델명 설정

```python
import json
import torch  # quantization 용
from transformers import (AutoTokenizer,
                          AutoModelForCausalLM,
                          BitsAndBytesConfig,
                          pipeline)

model_name = 'unsloth/Llama-3.2-3B-Instruct'
```

`unsloth/` prefix는 학습 최적화가 적용된 버전이다. 실제 가중치는 `meta-llama/Llama-3.2-3B-Instruct`와 동일하다.

## 2. Quantization Configuration

3B 모델을 float32로 로드하면 약 12GB VRAM이 필요하다. T4 GPU의 VRAM은 16GB지만 여유가 없다. 4-bit 양자화로 메모리를 줄였다.

```python
bnb_config = BitsAndBytesConfig(
    load_in_4bit=True,
    bnb_4bit_use_double_quant=True,
    bnb_4bit_quant_type='nf4',
    bnb_4bit_compute_dtype=torch.bfloat16
)
```

각 파라미터의 역할:

- `load_in_4bit=True`: 모델 파라미터를 4비트로 양자화
- `bnb_4bit_use_double_quant=True`: Double quantization 활성화. 메모리 사용을 추가로 줄인다
- `bnb_4bit_quant_type='nf4'`: NF4(Normal Float 4-bit) 방식. 일반적인 int4보다 정확도가 높다
- `bnb_4bit_compute_dtype=torch.bfloat16`: 실제 연산은 bfloat16으로 수행. FP16보다 표현 범위가 넓어 학습 안정성이 높다

bfloat16과 FP16의 차이: bfloat16은 가수부가 7-bit라 정밀도가 낮지만 큰 숫자를 다룰 수 있어 학습 중 손실 발산 위험이 적다. FP16은 가수부 10-bit로 정밀도는 높지만 overflow 위험이 있다.

## 3. 모델 로드와 Pipeline 생성

### Tokenizer와 Model 로드

```python
tokenizer = AutoTokenizer.from_pretrained(model_name, token=HF_TOKEN)
tokenizer.pad_token = tokenizer.eos_token

model = AutoModelForCausalLM.from_pretrained(
    model_name,
    device_map="auto",   # GPU or CPU 자동 선택
    quantization_config=bnb_config,
    token=HF_TOKEN
)
```

`tokenizer.pad_token = tokenizer.eos_token`을 설정하는 이유: 배치 처리 시 입력 길이가 다를 때 패딩이 필요한데, Llama 계열 모델은 기본적으로 pad_token이 없어서 eos_token으로 대체한다.

모델 다운로드 시 shards 4개 파일(총 약 4.98GB)이 순차적으로 내려온다.

### text-generation Pipeline 생성

```python
text_generator = pipeline(
    "text-generation",
    model=model_name,
    tokenizer=tokenizer,
    max_new_tokens=128,
    # device=0  # GPU 사용 시
)
```

Transformers의 `pipeline`은 다양한 태스크를 지원한다. 주요 종류:

| Pipeline 타입 | 용도 |
|---|---|
| `text-generation` | 텍스트 생성 |
| `text-classification` | 감정 분석, 카테고리 분류 |
| `ner` | Named Entity Recognition |
| `question-answering` | 컨텍스트 기반 Q&A |
| `summarization` | 텍스트 요약 |
| `translation_en_to_kr` | 번역 |

### Inference

Q&A 응답 함수를 만들어 테스트했다.

```python
def get_response(prompt):
    sequences = text_generator(prompt)
    gen_text = sequences[0]['generated_text']
    return gen_text
```

영어 질문:
```python
prompt = 'What is Machine Learning?'
response = get_response(prompt)
# 'What is Machine Learning? (A Beginner's Guide)\nMachine learning is a subset of
# artificial intelligence...'
```

한국어 질문:
```python
prompt = '서울에 대해서 한글로 설명해 주세요'
response = get_response(prompt)
# '서울에 대해서 한글로 설명해 주세요.\n서울은 한국의 수도와 가장 큰 도시입니다...'
```

한국어 입력에도 한국어로 응답이 나왔다. Llama 3.2는 영어 외 8개 언어를 공식 지원한다.

### Text Classification의 우회 방법

`text-classification` 파이프라인을 그대로 사용하면 에러가 난다.

```
The model 'LlamaForCausalLM' is not supported for text-classification.
```

LlamaForCausalLM은 텍스트 생성 전용 아키텍처라 분류 헤드가 없다. 대신 text-generation 파이프라인으로 프롬프트를 분류 작업처럼 구성해서 결과를 얻는다.

```python
text = 'I love using Hugging Face!'
prompt = f"""This is a sentiment analysis task.
You will categorize the following sentence into one of three sentiments:
positive, negative, or neutral.
categorize this sentence: {text}
"""

response = get_response(prompt)
# '...The sentiment of this sentence is positive...'
```

파이프라인이 직접 지원하지 않는 태스크도 프롬프트 엔지니어링으로 처리할 수 있다.

## 4. RAG (Retrieval Augmented Generation)

### LLM의 한계와 RAG의 역할

LLM은 사전학습 시점까지의 공개 데이터로만 지식이 제한된다. 모델을 재학습하지 않고 외부 지식을 활용하게 하는 프레임워크가 RAG다.

Fine-Tuning도 같은 목적이지만 재학습 과정이 필요하고 비싼 GPU를 써야 할 가능성이 높다. RAG는 외부 문서를 검색해서 컨텍스트로 제공하는 방식이라 비용 부담이 적다.

### RAG의 5단계 파이프라인

RAG는 다음 순서로 동작한다.

1. **데이터 로드**: 웹 페이지, PDF, CSV 등 다양한 소스에서 문서를 불러온다
2. **텍스트 분할**: 로드된 문서를 작은 청크(chunk)로 나눈다. 검색과 모델 입력 효율화를 위해
3. **임베딩 및 저장**: 청크를 벡터로 변환해 Vector Store에 저장한다
4. **검색**: 사용자 질문을 벡터로 변환하고, 유사도가 높은 청크를 찾는다
5. **응답 생성**: 질문 + 검색된 청크를 합친 프롬프트를 LLM에 전달해 답변을 생성한다

### Document Loaders

LangChain의 Document Loader는 다양한 소스에서 문서를 RAG용 객체로 불러오는 모듈이다. 각 Document 객체는 두 가지로 구성된다:
- `page_content`: 문서의 실제 텍스트
- `metadata`: 출처, 페이지 번호 등 부가 정보

**PDF 로드** 예시:

```python
from langchain_community.document_loaders import PyPDFLoader

loader = PyPDFLoader('New2026.pdf')
pages = loader.load()
pages[3]  # 4번째 페이지 Document 객체
```

웹 페이지 로더는 `WebBaseLoader`(원시 HTML 수집)와 `UnstructuredURLLoader`(구조화된 텍스트 추출) 두 종류가 있다. NLP 후속 작업에는 텍스트를 정리해서 반환하는 `UnstructuredURLLoader`가 더 적합하다.

### Text Splitters

큰 문서를 그대로 모델에 넣으면 토큰 한계를 초과하고 검색 정확도도 떨어진다. 적절한 크기로 분할해야 한다. 청크 하나는 벡터 하나로 임베딩된다.

두 가지 주요 스플리터:

**CharacterTextSplitter**: 지정한 구분자 1개 기준으로 분할. 구분자를 만나지 못하면 chunk_size를 초과하는 경우가 생긴다.

```python
from langchain.text_splitter import CharacterTextSplitter

text_splitter = CharacterTextSplitter(
    separator='\n\n',
    chunk_size=500,
    chunk_overlap=100,
    length_function=len,
)
```

**RecursiveCharacterTextSplitter**: 여러 구분자를 순서대로 시도해 chunk_size를 유지하면서 분할한다. 실제로는 chunk_size를 거의 넘지 않는다.

분할 순서: `\n\n` (단락) → `\n` (줄) → `" "` (단어) → 각 문자

```python
from langchain.text_splitter import RecursiveCharacterTextSplitter

text_splitter = RecursiveCharacterTextSplitter(
    chunk_size=500,
    chunk_overlap=200,
    length_function=len,
)
```

`chunk_overlap`은 연속된 청크 사이에 겹치는 문자 수다. 청크 경계에서 문맥이 잘리는 문제를 완화한다. CharacterTextSplitter는 `split_text()`(문자열 입력 → 문자열 리스트 반환)와 `split_documents()`(Document 리스트 입력 → Document 리스트 반환, 메타데이터 유지)를 모두 지원한다.

### Text Embeddings

텍스트를 고정 크기의 숫자 벡터로 변환하는 작업이다. 의미적으로 유사한 텍스트는 벡터 공간에서 가까운 위치에 놓인다. RAG의 검색 단계에서 코사인 유사도나 거리를 계산해 관련 청크를 찾는 데 사용한다.

주요 임베딩 모델:

| 모델 | 차원 | 특징 |
|---|---|---|
| OpenAI Embeddings | 1536 | 유료, 한/영 모두 지원 |
| BAAI/bge-m3 (HuggingFace) | 1024 | 무료, 한/영 모두 지원 |
| jhgan/ko-sbert-nli (HuggingFace) | 768 | 무료, 한국어 특화 |

임베딩 모델 성능 벤치마크는 HuggingFace의 MTEB Leaderboard에서 확인할 수 있다. 상위 모델들은 수천~수만 개 파라미터를 가진다.

```python
from langchain.embeddings import HuggingFaceEmbeddings

ko_hf = HuggingFaceEmbeddings(model_name='BAAI/bge-m3')
# 또는 한국어 특화:
# ko_hf = HuggingFaceEmbeddings(model_name='jhgan/ko-sbert-nli')
```

### Vector Stores

임베딩된 벡터를 저장하고 고속으로 유사도 검색을 수행하는 데이터베이스다. 일반 관계형 DB와 달리 벡터 간 유사도 연산에 특화되어 있다.

주요 두 가지 옵션:

| 항목 | ChromaDB | FAISS |
|---|---|---|
| 개발 목적 | 벡터 DB (저장+검색) | 고속 벡터 검색 라이브러리 |
| 저장 방식 | 디스크 기반 DB | RAM 기반 (디스크 저장 가능) |
| 메타데이터 필터 | 지원 | 지원 안함 |
| 검색 속도 | 빠름 (DB 오버헤드 존재) | 매우 빠름 |
| 사용 목적 | RAG, LLM, 챗봇 | 대규모 고속 ANN 검색 |

**Chroma 사용 예시:**

```python
from langchain.vectorstores import Chroma

# 메모리 내 저장 (세션 종료 시 사라짐)
db = Chroma.from_documents(docs, ko_hf)

# 디스크에 영구 저장
db2 = Chroma.from_documents(docs, ko_hf, persist_directory="./chroma_db")

# 디스크에서 로드
db3 = Chroma(persist_directory="./chroma_db", embedding_function=ko_hf)
```

**FAISS 사용 예시:**

```python
from langchain.vectorstores import FAISS

db = FAISS.from_documents(docs, ko_hf)
db.save_local('faiss_index')

# 로드
new_db = FAISS.load_local('faiss_index', ko_hf, allow_dangerous_deserialization=True)
```

`allow_dangerous_deserialization=True`는 메타데이터를 포함한 전체 데이터를 불러온다. 신뢰할 수 없는 파일에는 `False`로 설정해야 보안상 안전하다.

유사도 검색 방법:

```python
# 상위 k개 결과 + 유사도 점수 반환
docs = db3.similarity_search_with_relevance_scores(query, k=3)
print('가장 유사한 문서 :\n{}\n'.format(docs[0][0].page_content))
print('문서 유사도 : {}'.format(docs[0][1]))

# MMR 검색 (유사성 + 다양성 균형)
docs = new_db.max_marginal_relevance_search(query, k=3)
```

MMR(Maximal Marginal Relevance)은 결과 집합 내 중복을 최소화하면서 질의와 관련성 높은 문서를 반환한다. 유사한 청크가 반복되는 걸 방지해서 결과 다양성을 높인다.

### Retrieval Chain Types

검색된 문서를 LLM에 전달하는 방식에는 4가지 Chain Type이 있다.

1. **Stuff Chain**: 검색된 모든 문서를 하나의 입력으로 합쳐 LLM에 전달. 간단하지만 토큰 한계 초과 위험
2. **Map-Reduce Chain**: 각 문서를 개별 처리(map)하고 결과를 결합(reduce). 대용량 문서셋에 적합
3. **Refine Chain**: 첫 번째 문서로 초안을 생성하고 이후 문서로 순차적으로 개선. 점진적 품질 향상
4. **Map-Retrieve Chain**: 쿼리를 문서의 섹션별로 나눠 가장 관련성 높은 부분을 검색

## 5. RAG 실습: Colab에서 전체 파이프라인 구현

### ChatGPT를 LLM으로 사용하는 경우

```python
# 1. Retriever 설정
retriever = db.as_retriever(
    search_type="similarity",
    search_kwargs={"k": 3}
)

# 2. LLM 설정
llm = ChatOpenAI(
    model="gpt-4o-mini",
    temperature=0,
    streaming=True,
    callbacks=[StreamingStdOutCallbackHandler()],
    api_key=gptkey
)

# 3. 프롬프트 템플릿 설정
template = """당신은 법률과 정보에 대한 전문가 입니다. 질문에 충실히 답변해 주세요.
{context}

Question: {question}
Answer:"""

prompt = ChatPromptTemplate.from_template(template)

def format_docs(docs):
    return "\n\n".join(doc.page_content for doc in docs)

# 4. LCEL 체인 생성
rag_chain = (
    {"context": retriever | format_docs, "question": RunnablePassthrough()}
    | prompt
    | llm
    | StrOutputParser()
)
```

LCEL(LangChain Expression Language) 체인의 실행 순서:
1. 입력 딕셔너리 생성 - `retriever`로 context 추출, `RunnablePassthrough()`로 question 그대로 통과
2. `ChatPromptTemplate`으로 완성된 프롬프트 문자열 생성
3. LLM이 응답 생성
4. `StrOutputParser()`로 순수 문자열 추출

테스트 결과:

```python
query = "2026년 청년의 자금 마련을 위한 정책은?"
response = rag_chain.invoke(query)
# 2026년 청년의 자금 마련을 위한 정책으로는 **청년미래적금**과 **청년일자리도약장려금**이 있습니다.
# 1. **청년미래적금**: 신설, 가입기간 3년, 정부기여금 지원 비율 (일반형) 6% / (우대형) 12%...
```

### Llama 3.2를 LLM으로 사용하는 경우

HuggingFace pipeline은 LangChain의 Runnable 인터페이스를 기본 지원하지 않는다. `RunnableLambda`로 래핑해서 체인에 연결한다.

```python
def custom_llm_invoke(input_data):
    # PromptValue 객체를 문자열로 변환
    prompt_text = input_data.to_string()
    
    sequences = text_generator(prompt_text)
    gen_text = sequences[0]['generated_text']
    
    return gen_text

# 일반 함수 → Runnable 객체로 변환
llm = RunnableLambda(custom_llm_invoke)
```

이후 체인 구성은 ChatGPT 버전과 동일하다. LLM 변수만 교체하면 된다.

동일한 쿼리("2026년 청년의 자금 마련을 위한 정책은?")를 Llama 3.2로 실행했을 때도 PDF에서 청년미래적금 관련 내용을 검색해서 응답을 생성했다. ChatGPT 버전에 비해 응답 포맷이 덜 구조화되는 경향이 있었다.

## 6. 4월 16일 수업에서 얻은 것

이날 수업은 두 가지 흐름이 교차했다.

하나는 LLaMA 3.2를 로컬(Colab)에서 직접 돌리는 경험이다. API 호출이 아니라 모델 파일을 직접 다운받고, 양자화 설정을 직접 잡아주고, 파이프라인을 직접 구성했다. 양자화가 왜 필요한지, bfloat16과 nf4가 각각 어떤 역할인지 코드 수준에서 확인했다.

다른 하나는 RAG의 전체 파이프라인을 조립하는 경험이다. 문서 로드 → 청크 분할 → 임베딩 → 벡터 저장 → 검색 → 응답 생성까지 각 단계에서 어떤 선택지가 있고, 선택에 따라 결과가 어떻게 달라지는지를 직접 확인했다. CharacterTextSplitter와 RecursiveCharacterTextSplitter의 chunk_size 처리 방식 차이, Chroma와 FAISS의 트레이드오프, MMR과 단순 유사도 검색의 차이가 모두 이 파이프라인 안에 있다.

day 7에서 임베딩이 의미 공간에서 어떤 구조를 갖는지 살펴봤다면, 이날부터는 그 임베딩을 실제 응용 시스템에서 어떻게 사용하는지로 연결됐다.
