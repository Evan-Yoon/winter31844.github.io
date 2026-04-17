---
title: "[NLP] day 9: Fine-Tuning 실습과 Gemma·LLaMA 모델 적응"
slug: intel-nlp-day9-fine-tuning-llama-gemma
date: 2026-04-17
author: Evan Yoon
category: study
subcategory: bootcamp
description: |
  2026년 4월 17일 수업에서는 Fine-Tuning의 개념과 진행 과정을 정리하고,
  Colab에서 CSV 데이터를 JSONL로 바꿔 Hugging Face Dataset으로 올린 뒤
  Gemma 3 4B와 LLaMA 3.2 3B-Instruct를 LoRA 방식으로 미세조정하는 실습을 진행했다.
thumbnail: ""
tags:
  - NLP
  - Fine-Tuning
  - LoRA
  - QLoRA
  - Unsloth
  - Gemma
  - LLaMA
  - HuggingFace
  - nlp-study
readTime: 13
series: "Intel NLP 과정"
seriesOrder: 9
featured: false
draft: false
toc: true
---

2026년 4월 17일 수업은 RAG 다음 단계로 넘어가는 흐름이었다. day 8에서는 외부 문서를 검색해 답변을 보강하는 구조를 다뤘고, day 9에서는 모델 자체를 특정 데이터에 맞게 적응시키는 Fine-Tuning을 실습했다. 수업 자료 후반부에서는 Fine-Tuning의 전체 절차와 LoRA의 의미를 정리했고, 노트북 실습에서는 `CallCrazy` 서비스 Q&A 데이터를 이용해 Gemma와 LLaMA 계열 모델을 각각 미세조정해 봤다.

## 1. Fine-Tuning이 필요한 이유

범용 LLM은 일반적인 질의응답은 잘 처리하지만, 특정 서비스나 도메인의 세부 정보까지 정확히 알고 있지는 않다. 예를 들어 배달 서비스의 가격, 운영 방식, 차별점처럼 공개 범용 지식이 아닌 내용은 모델이 기본 상태로는 제대로 답하기 어렵다.

이럴 때 선택지가 두 가지다.

- **RAG**: 외부 문서를 검색해서 답변에 참고 자료를 붙인다.
- **Fine-Tuning**: 모델이 특정 형식의 질문과 답변 패턴을 더 잘 따르도록 가중치 일부를 학습한다.

이번 수업에서는 두 방식의 차이를 분명히 봤다. RAG는 최신 문서 반영에 강하고, Fine-Tuning은 특정 말투나 응답 형식, 서비스 전용 질의응답 패턴을 모델에 익히는 데 적합하다.

## 2. Fine-Tuning 진행 순서

수업 자료에서는 Fine-Tuning 과정을 다음 순서로 정리했다.

1. 환경 설정 및 라이브러리 설치
2. 데이터 로드 및 전처리
3. 데이터셋 업로드
4. 모델 로드 및 설정
5. LoRA 기반 파인튜닝 준비
6. 데이터셋 매핑 후 학습 실행
7. 모델 저장 및 추론
8. 저장한 모델을 다시 로드해 테스트

실습도 거의 같은 흐름으로 진행됐다. Colab에서 필요한 패키지를 설치하고, CSV 데이터를 JSONL로 변환한 뒤 Hugging Face Dataset으로 업로드했다. 이후 `unsloth`를 이용해 모델을 로드하고, LoRA를 붙여 학습한 다음, 저장한 모델을 다시 불러와 실제 질문으로 테스트했다.

## 3. 학습용 데이터 준비

이번 실습의 데이터는 `instruction`, `input`, `response` 세 컬럼으로 구성된 CSV였다. 핵심은 빈 칸 없이 한 행이 하나의 Q&A 예제가 되도록 맞추는 것이다.

- `instruction`: 어떤 방식으로 답해야 하는지 설명
- `input`: 실제 질문
- `response`: 기대하는 답변

Colab에서는 업로드한 CSV를 `pandas`로 읽은 뒤 JSONL로 변환했다.

```python
file_name = next(iter(uploaded))
df = pd.read_csv(io.BytesIO(uploaded[file_name]), encoding='cp949')
df = df.astype(str)

jsonl_file_name = 'call_me.jsonl'
jsonl_path = f'/content/{jsonl_file_name}'

with open(jsonl_path, 'w', encoding='utf-8') as f:
    for _, row in df.iterrows():
        json.dump(row.to_dict(), f, ensure_ascii=False)
        f.write('\n')
```

여기서 `ensure_ascii=False`를 써야 한글이 유니코드 이스케이프가 아니라 실제 문자열로 저장된다. JSONL은 한 줄에 하나의 JSON 객체가 들어가므로 Hugging Face Dataset에 올리기 쉽고, 이후 `load_dataset()`으로 바로 불러올 수 있다는 점이 편했다.

업로드는 `HfApi().upload_file()`로 진행했다.

```python
api.upload_file(
    path_or_fileobj=jsonl_path,
    path_in_repo=jsonl_file_name,
    repo_id="JaeminKim/callme",
    repo_type="dataset"
)
```

수업 자료에서도 CSV나 Excel 파일을 직접 다루는 것보다, Dataset 저장소에 올려 두고 URL 기반으로 관리하는 방식이 협업과 버전 관리에 유리하다고 정리했다.

## 4. LoRA와 Unsloth로 학습 준비

이번 실습의 핵심은 전체 모델을 다시 학습하는 방식이 아니라 **LoRA 기반 PEFT(Parameter-Efficient Fine-Tuning)** 였다. 수업 자료에서는 LoRA를 "기존 가중치는 고정하고, 저차원 행렬만 추가로 학습하는 방식"으로 설명했다.

장점은 분명했다.

- 전체 파라미터를 다 업데이트하지 않아 메모리 부담이 적다.
- Colab T4 같은 환경에서도 실습이 가능하다.
- 특정 태스크에 맞춘 적응을 비교적 짧은 시간에 수행할 수 있다.

모델 로드는 `FastLanguageModel.from_pretrained()`를 사용했다.

```python
model_name = 'unsloth/Llama-3.2-3B-Instruct'
max_seq_length = 2048
dtype = None
load_in_4bit = True

model, tokenizer = FastLanguageModel.from_pretrained(
    model_name=model_name,
    max_seq_length=max_seq_length,
    dtype=dtype,
    load_in_4bit=load_in_4bit
)
```

LLaMA 실습에서는 `load_in_4bit=True`로 설정해 QLoRA 형태로 메모리를 줄였다. 반면 Gemma 실습에서는 `unsloth/gemma-3-4B-it`를 불러오면서 chat template를 적용하는 흐름으로 진행했다.

LoRA 설정은 두 노트북에서 거의 동일했다.

```python
model = FastLanguageModel.get_peft_model(
    model,
    r=16,
    target_modules=["q_proj", "k_proj", "v_proj", "o_proj",
                    "gate_proj", "up_proj", "down_proj"],
    lora_alpha=16,
    lora_dropout=0,
    bias="none",
    use_gradient_checkpointing="unsloth",
    random_state=3407,
    use_rslora=False,
    loftq_config=None,
)
```

수업 자료 기준으로 보면 각 파라미터의 의미는 다음 정도로 정리할 수 있다.

- `r`: LoRA 저차원 행렬의 rank
- `lora_alpha`: LoRA 가중치 반영 스케일
- `lora_dropout`: 과적합 방지를 위한 dropout 비율
- `target_modules`: LoRA를 적용할 projection 계층
- `use_gradient_checkpointing="unsloth"`: VRAM 절감을 위한 최적화 옵션

특히 `unsloth`는 메모리 절약과 속도 측면에서 Colab 실습에 잘 맞았다. 수업 자료에서도 gradient checkpointing, 빠른 LoRA, 4비트 양자화 지원이 장점으로 정리되어 있었다.

### LoRA 주요 파라미터를 어떻게 이해하면 되는가

수업 자료를 따라가면서 느낀 점은, LoRA 설정은 외워서 넣기보다 각 값이 무엇을 조절하는지 이해하는 편이 훨씬 낫다는 것이었다.

- `r`이 커질수록 더 복잡한 적응이 가능하지만 메모리 사용량도 늘어난다.
- `lora_alpha`는 LoRA가 만든 변화량을 얼마나 크게 반영할지 정하는 계수다.
- `lora_dropout`은 과적합을 줄이기 위한 장치인데, 이번 실습에서는 `0`으로 두고 빠르게 학습했다.
- `target_modules`는 Transformer 내부 어느 projection 층에 적응 레이어를 붙일지 정하는 부분이다.

특히 `q_proj`, `k_proj`, `v_proj`, `o_proj`는 attention 계산과 직접 연결되고, `gate_proj`, `up_proj`, `down_proj`는 MLP 블록의 표현력 조절과 관련된다. 모든 가중치를 학습하는 대신 이런 핵심 모듈만 선택적으로 조정하니, 적은 비용으로도 모델 성향을 바꿀 수 있다는 점이 LoRA의 실용적인 장점이었다.

## 5. Gemma 3 4B 실습: chat template 기반 학습

첫 번째 노트북에서는 `unsloth/gemma-3-4B-it`를 사용했다. 이 실습의 특징은 데이터를 곧바로 `instruction/input/response` 문자열로 합치는 대신, 먼저 `messages` 구조로 바꾼 뒤 모델 전용 chat template를 적용했다는 점이다.

```python
from unsloth.chat_templates import get_chat_template

tokenizer = get_chat_template(
    tokenizer,
    chat_template='gemma-3'
)
```

데이터셋 변환도 채팅 형식에 맞춰 진행했다.

```python
def convert_to_chat_format(examples):
    messages_list = []
    for question, response in zip(examples["input"], examples["response"]):
        messages = [
            {"role": "user", "content": question.strip()},
            {"role": "assistant", "content": response.strip()},
        ]
        messages_list.append(messages)
    return {"messages": messages_list}
```

이후 `tokenizer.apply_chat_template()`를 적용해 최종 학습 텍스트를 만들었다. Gemma처럼 대화형 포맷을 강하게 전제하는 모델에서는 이 방식이 더 자연스럽다고 느꼈다.

학습은 `SFTTrainer`와 `SFTConfig`로 진행했다.

```python
trainer = SFTTrainer(
    model=model,
    tokenizer=tokenizer,
    train_dataset=dataset,
    eval_dataset=None,
    args=SFTConfig(
        dataset_text_field='text',
        per_device_train_batch_size=2,
        gradient_accumulation_steps=4,
        warmup_steps=5,
        max_steps=100,
        learning_rate=2e-4,
        logging_steps=1,
        optim="adamw_8bit",
        weight_decay=0.01,
        lr_scheduler_type="linear",
        seed=3407,
        report_to="none",
    ),
)
```

추가로 `train_on_responses_only()`를 적용해서 사용자 질문까지 학습 대상으로 보는 대신, 답변 생성 부분에 집중하도록 설정했다.

```python
trainer = train_on_responses_only(
    trainer,
    instruction_part='<start_of_turn>user\n',
    response_part='<start_of_turn>model\n',
)
```

이 설정의 의미도 같이 정리해 둘 필요가 있었다.

- `per_device_train_batch_size`: 한 번에 GPU에 올리는 샘플 수
- `gradient_accumulation_steps`: 작은 배치를 여러 번 누적해 큰 배치처럼 학습하는 방식
- `warmup_steps`: 초반 학습률을 천천히 올려 학습을 안정화하는 구간
- `learning_rate`: 가중치를 얼마나 빠르게 갱신할지 정하는 값
- `optim="adamw_8bit"`: 메모리 사용량을 줄이기 위한 8비트 옵티마이저
- `lr_scheduler_type="linear"`: 학습이 진행될수록 learning rate를 선형으로 줄이는 방식

Colab 환경에서는 GPU 메모리가 넉넉하지 않기 때문에, 배치 크기를 무작정 키우기보다 `gradient_accumulation_steps`를 활용하는 쪽이 현실적이었다. 이런 설정들이 단순한 문법이 아니라, 제한된 자원 안에서 학습을 성립시키는 장치라는 점이 분명히 보였다.

학습 후에는 `How much is Callcrazy service?`, `What is the uniqueness of Callcrazy service?` 같은 질문으로 모델 반응을 확인했다. 반대로 `Explain about Seoul` 같은 서비스와 무관한 질문도 넣어 봤는데, 이 테스트가 모델이 어느 범위까지 도메인 적응을 했는지 보는 데 유용했다.

여기서 중요한 점은 "정답을 더 잘 맞히는가"만 보는 것이 아니라, **모델이 어느 질문에 강해지고 어느 질문에는 여전히 일반 모델처럼 반응하는가**를 같이 보는 것이다. Fine-Tuning은 모델 전체 지식을 새로 만드는 과정이 아니라, 특정 데이터 분포에 맞춰 응답 경향을 조정하는 과정에 가깝다는 점을 확인할 수 있었다.

## 6. LLaMA 3.2 3B 실습: Alpaca 프롬프트 기반 학습

두 번째 노트북에서는 `unsloth/Llama-3.2-3B-Instruct`를 사용했고, 데이터 포맷은 Gemma와 달리 **Alpaca 스타일 프롬프트**로 구성했다.

```python
alpaca_prompt = """제공된 dataset은 CallCrazy라서 배달 서비스에 대한 Q&A 입니다. 주어진 데이터를 바탕으로 적절한 응답을 작성하세요.

### Instruction:
{}

### Input:
{}

### Response:
{}"""
```

이후 각 행의 `instruction`, `input`, `response`를 이 템플릿에 넣고, 끝에 `EOS_TOKEN`을 붙여 학습 텍스트를 만들었다.

```python
def formatting_prompts_func(examples):
    instructions = examples["instruction"]
    inputs = examples["input"]
    responses = examples["response"]
    texts = []

    for instruction, input, response in zip(instructions, inputs, responses):
        text = alpaca_prompt.format(instruction, input, response) + EOS_TOKEN
        texts.append(text)

    return {"text": texts}
```

수업 자료에서도 EOS 토큰을 붙이지 않으면 생성이 길게 이어질 수 있다고 설명했는데, 실제로 instruction tuning에서는 종료 지점을 명확히 주는 게 중요했다.

또 하나 정리할 부분은, Alpaca 형식이 왜 여전히 많이 쓰이는가 하는 점이다.  
이 형식은 사람이 읽기에도 구조가 분명하고, 모델 입장에서도 "지시문", "입력", "응답"의 경계를 안정적으로 학습할 수 있다. 즉, 복잡한 대화 템플릿이 없는 상황에서도 instruction-following 데이터를 비교적 일관되게 만들 수 있다는 장점이 있다.

LLaMA 쪽 학습 설정은 다음과 같았다.

```python
trainer = SFTTrainer(
    model=model,
    tokenizer=tokenizer,
    train_dataset=dataset,
    dataset_text_field="text",
    max_seq_length=max_seq_length,
    dataset_num_proc=2,
    packing=False,
    args=TrainingArguments(
        per_device_train_batch_size=4,
        gradient_accumulation_steps=4,
        warmup_steps=5,
        max_steps=100,
        learning_rate=2e-4,
        fp16=not is_bfloat16_supported(),
        bf16=is_bfloat16_supported(),
        logging_steps=1,
        optim="adamw_8bit",
        weight_decay=0.01,
        lr_scheduler_type="linear",
        seed=3407,
        output_dir="outputs",
    ),
)
```

Gemma 실습과 비교하면, 같은 LoRA 기반이어도 데이터 포맷과 템플릿 전략이 다를 수 있다는 점이 분명했다. 모델에 맞는 입력 형식을 맞춰 주는 것이 생각보다 중요했다.

정리하면 다음처럼 볼 수 있었다.

- **Gemma 실습**: 채팅형 모델에 맞춰 `messages`와 chat template를 사용
- **LLaMA 실습**: instruction tuning에 맞춰 Alpaca prompt를 사용

같은 데이터셋을 쓰더라도 모델 구조와 학습 방식에 따라 전처리 전략이 달라진다는 점이 이번 실습의 핵심 중 하나였다.

## 7. 추론 단계에서 확인한 것

수업 자료 후반부에서는 학습만큼이나 추론 단계 설명도 자세히 다뤘다. 실제로 모델이 학습되었는지 확인하려면, 입력을 토큰화하고 GPU로 옮긴 뒤 `generate()`로 응답을 만드는 과정까지 이해해야 한다.

```python
inputs = tokenizer(
    [text],
    return_tensors="pt"
).to("cuda")

outputs = model.generate(
    **inputs,
    max_new_tokens=1024,
    temperature=1.0,
    top_p=0.95,
    top_k=64,
)
```

여기서 같이 정리한 포인트는 다음과 같았다.

- `return_tensors="pt"`: 토크나이저 출력을 PyTorch 텐서로 변환
- `.to("cuda")`: 입력 데이터를 GPU 메모리로 이동
- `max_new_tokens`: 새로 생성할 최대 토큰 수
- `temperature`: 출력 다양성 조절
- `top_p`, `top_k`: 샘플링 범위를 조절해 응답 성향을 바꾸는 옵션

즉, 학습된 모델이라고 해서 무조건 좋은 답을 내는 것이 아니라, 추론 파라미터에 따라서도 결과가 달라질 수 있다. Fine-Tuning은 학습 단계에서 끝나는 작업이 아니라, 추론 설정까지 포함해서 최종 응답 품질을 맞추는 작업이라는 점이 중요했다.

## 8. 저장, 업로드, 재로딩까지 확인

학습이 끝난 뒤에는 LoRA가 적용된 모델과 tokenizer를 로컬에 저장하고 Hugging Face Hub에도 올렸다.

```python
save_path = "fine-tuned-gemma-model"
trainer.model.save_pretrained(save_path)
trainer.processing_class.save_pretrained(save_path)

repo_name = "JaeminKim/Gemma_callme"
trainer.model.push_to_hub(repo_name)
trainer.processing_class.push_to_hub(repo_name)
```

이후 저장된 모델을 다시 불러와 추론하는 단계까지 확인했다.

```python
model, tokenizer = FastLanguageModel.from_pretrained(
    model_name="JaeminKim/Gemma_callme",
    max_seq_length=4096,
    dtype=None,
    load_in_4bit=True,
)

FastLanguageModel.for_inference(model)
```

수업 자료에서도 Fine-Tuning 실습은 학습 자체보다, 저장한 모델을 다시 로드해 동일한 질문에 일관되게 응답하는지 확인하는 단계까지 포함해야 한다고 정리했다. 실제 서비스 적용을 생각하면 이 과정이 빠지면 안 된다.

추가로 이 단계는 "학습이 끝났다"를 확인하는 절차이기도 하다. Colab 세션은 끊기기 쉽기 때문에, 학습 결과를 Hub에 올려 두고 다시 불러올 수 있어야 다음 실험이나 배포 단계로 넘어갈 수 있다.

## 9. day 8의 RAG와 day 9의 Fine-Tuning 차이

이번 수업을 정리하면서 가장 크게 남은 부분은 day 8과 day 9의 역할 차이였다.

- **RAG**는 문서를 검색해서 모델의 입력 컨텍스트를 보강한다.
- **Fine-Tuning**은 모델이 특정 형식의 응답을 더 잘 생성하도록 적응시킨다.

즉, 최신 자료나 긴 문서 기반 질의응답은 RAG가 유리하고, 서비스 고유의 말투나 응답 규칙, 짧고 반복적인 Q&A 패턴은 Fine-Tuning이 더 잘 맞는다. 실제 프로젝트에서는 둘 중 하나만 쓰기보다, 필요한 범위에 따라 결합하는 경우가 많다는 점도 자연스럽게 이해됐다.

## 마무리

day 9에서는 Fine-Tuning을 단순히 "모델을 다시 학습한다" 수준으로 보지 않고, 데이터 준비 형식, Hugging Face 업로드, LoRA 적용, 학습 설정, 저장과 재사용까지 한 흐름으로 정리할 수 있었다. 같은 데이터셋이라도 Gemma는 chat template 중심으로, LLaMA는 Alpaca 프롬프트 중심으로 다뤘다는 점이 특히 인상적이었다.

이번 실습으로 RAG와 Fine-Tuning을 각각 따로 배운 것이 아니라, 어떤 문제가 검색 보강에 가깝고 어떤 문제가 모델 적응에 가까운지 구분해서 볼 수 있게 됐다.
