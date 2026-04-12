---
title: "Java 학습 12일차: 데이터 라벨링 UI 완성과 감정 분석 모델 연동"
slug: java-study-day12-data-labeling-ui-and-sentiment-analysis
date: 2026-01-22
author: Evan Yoon
category: study
subcategory: bootcamp
description: "1월 22일 Java 학습 기록. JavaFX 라벨링 도구를 다듬고 중립 라벨과 CSS를 보강한 뒤, Python 데이터 증강과 모델 재학습, JavaFX 감정 분석기 연동까지 하나의 흐름으로 완성한 과정을 자세히 정리했다."
tags:
  - java
  - study
  - javafx
  - machine-learning
  - sentiment-analysis
  - data-labeling
  - python
readTime: 18
series: Java Study
seriesOrder: 12
featured: false
draft: false
toc: true
---

## Java 학습 12일차에 작은 AI 프로젝트 형태가 실제로 완성되기 시작했다 <!-- short: AI 프로젝트 완성 -->
`reference/javastudy/day0121/src` 안의 1월 22일 작업 흐름을 보면, 이날은 단순한 Java 학습을 넘어 작은 프로젝트를 끝까지 연결하는 단계에 도달했다. `study02`에서는 데이터 라벨링 도구가 UI와 저장 구조를 갖춘 실제 작업용 툴 형태로 다듬어졌고, `study03`에서는 Python 기반 감정 분석 모델의 데이터 증강, 재학습, JavaFX 연동까지 하나의 흐름으로 이어졌다.

즉, day12는 문법 복습이 아니라 "데이터 수집 -> 라벨링 -> 학습 -> 예측 -> UI 연결"이라는 전체 사이클을 체감한 날이었다.

이날 학습 흐름을 크게 나누면 이렇다.

1. JavaFX 라벨링 앱의 UI와 저장 흐름을 다듬는다.
2. 긍정/중립/부정 3단계 라벨 구조를 정리한다.
3. CSS와 카드형 UI로 반복 작업용 화면을 더 읽기 좋게 만든다.
4. 기존 감정 분석 모델의 성능 문제를 확인한다.
5. Python 스크립트로 데이터를 증강하고 모델을 재학습한다.
6. 학습된 모델을 JavaFX 감정 분석 화면에서 다시 호출한다.

즉, day12는 앱 하나와 모델 하나를 따로 다룬 날이 아니라, 둘을 연결해 하나의 작은 시스템으로 보기 시작한 날이었다.

## 1. JavaFX 데이터 라벨링 앱이 "초안"에서 "실제 도구"에 가까워졌다 <!-- short: 기능 고도화 -->
`study02/root.fxml`를 보면 라벨링 앱의 목적이 한눈에 드러난다.

```xml
<Label text="DATA LABELING" styleClass="title-label" />
<Label fx:id="lblPg" text="1 / 100" styleClass="status-label" />
<Label fx:id="lblData" text="데이터 로딩 중..." styleClass="data-label" />

<Button onAction="#onActionLikeBtn" text="긍정 (Positive)" styleClass="btn, btn-positive" />
<Button onAction="#onActionNeutralBtn" text="중립 (Neutral)" styleClass="btn, btn-neutral" />
<Button onAction="#onActionDislikeBtn" text="부정 (Negative)" styleClass="btn, btn-negative" />
```

이 UI는 단순히 버튼 세 개를 나열한 것이 아니다.

- 현재 작업 중인 문장 표시
- 진행 상황 표시
- 3단계 감정 라벨 버튼
- 반복 작업에 적합한 단순한 흐름

즉, "문장을 하나씩 보여 주고, 사람이 판단하고, 바로 저장하고, 다음으로 넘어간다"는 라벨링 도구의 핵심 흐름이 화면 자체에 잘 반영돼 있다.

## 2. 라벨링 앱의 핵심은 `initialize()`에 들어 있다
`study02/rootController.java`의 `initialize()`는 이 앱의 출발점이다.

```java
String str = loadFm.getAllData();
String[] subStr = str.split("\\n");
```

그 다음 표시 가능한 데이터 개수를 먼저 센다.

```java
for(int i=0;i<subStr.length;i++){
    String[] subSubStr = subStr[i].split("]");
    try {
        if(subSubStr[1] != null)
            size++;
    } catch (ArrayIndexOutOfBoundsException e) {
    }
}
```

그리고 정제된 문장만 배열에 담는다.

```java
preData = new String[size];
int cnt = 0;
for(int i=0;i<subStr.length;i++){
    String[] subSubStr = subStr[i].split("]");
    try {
        preData[cnt++] = subSubStr[1].trim();
    } catch (ArrayIndexOutOfBoundsException e) {
    }
}
```

마지막으로 첫 문장과 진행률을 화면에 띄운다.

```java
lblData.setText(preData[0]);
lblPg.setText("1" + "/" + size);
```

즉, 이 라벨링 앱은 단순 파일 뷰어가 아니라, 원본 데이터에서 실제 작업용 데이터만 추출해 메모리에 올리고 순차적으로 보여 주는 구조를 가진다.

## 3. 중립 라벨 추가는 작아 보여도 중요한 설계 변화였다
`study02/README.txt`를 보면 원래는 긍정/부정만 있던 구조에 중립 라벨을 직접 추가했다고 설명돼 있다.

```text
- 긍정 : 라벨값 1 저장
- 중립 : 라벨값 2 저장 (직접 기능 추가)
- 부정 : 라벨값 0 저장
```

이 변화는 단순 버튼 하나 추가가 아니다. 실제 데이터를 보다 보니 긍정/부정 어느 쪽에도 딱 맞지 않는 문장이 많았고, 그 문제를 해결하기 위해 라벨 체계를 바꾼 것이다.

즉, day12는 "코드만 구현"한 날이 아니라, 데이터를 보며 분류 체계 자체를 조정한 날이기도 하다.

### 왜 이게 중요할까
현실 데이터는 교과서처럼 깔끔하지 않다. 라벨 체계를 너무 단순하게 잡으면 사람이 억지로 데이터를 분류하게 되고, 그 결과 모델 학습 품질도 나빠질 수 있다. 그래서 "중립" 같은 현실적인 범주를 추가하는 판단이 중요하다.

## 4. CSS와 카드형 UI가 반복 작업 효율을 높였다
`study02/application.css`는 단순 장식이 아니라 작업 도구의 가독성을 높이는 역할을 한다.

예를 들어 카드 영역:

```css
.card-pane {
    -fx-background-color: white;
    -fx-background-radius: 15;
    -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);
}
```

버튼 스타일:

```css
.btn-positive {
    -fx-background-color: linear-gradient(to right, #36D1DC, #5B86E5);
}

.btn-neutral {
    -fx-background-color: linear-gradient(to right, #11998e, #38ef7d);
}

.btn-negative {
    -fx-background-color: linear-gradient(to right, #FF416C, #FF4B2B);
}
```

이런 스타일은 보기 좋기만 한 게 아니다.

- 긴 문장이 한눈에 들어오게 하고
- 버튼 역할을 색으로 직관화하고
- 반복 클릭 작업에서 피로를 줄인다

즉, day12에서는 UI를 "예쁘게 꾸민다"보다 "오래 써도 덜 피곤한 작업 화면으로 만든다"는 쪽으로 생각이 깊어졌다.

## 5. 라벨링 결과는 버튼을 누를 때마다 즉시 저장된다
긍정 버튼:

```java
String t = lblData.getText();
String str = t + ", 1\n";
saveFm.dataCreate(str);
```

부정 버튼:

```java
String t = lblData.getText();
String str = t + ", 0\n";
saveFm.dataCreate(str);
```

중립 버튼도 같은 패턴으로 간다.

중요한 건 클릭할 때마다 바로 `labeled_data.txt`에 기록된다는 점이다. 긴 라벨링 작업에서는 중간 저장이 아니라 "즉시 누적 저장"이 훨씬 안전하다.

### 왜 즉시 저장 구조가 좋을까
- 앱이 중간에 종료돼도 손실이 적고
- 작업 진척이 파일에 바로 남고
- 이어서 다시 작업하기 쉽다

이건 작은 구현처럼 보여도 실제 라벨링 도구에서는 매우 중요한 안정성 포인트다.

## 6. day12의 핵심은 "라벨링 후 끝"이 아니라 그 데이터를 학습까지 연결한 것이다 <!-- short: 핵심 lesson -->
이날의 진짜 핵심은 `study03`로 이어지는 흐름이다. `README.txt`를 보면 문제 인식부터 명확하다.

```text
기존 10,000개 데이터셋으로 학습 시,
'좋아요'와 같은 명백한 긍정 단어를 부정으로 예측하는 편향성 확인.
```

즉, 단순히 모델을 한 번 돌려본 게 아니라, 실제 예측 결과를 보고 "이 모델은 이상하다"는 문제를 발견한 것이다.

이게 중요하다. 머신러닝 작업은 모델을 돌리는 것보다 결과를 해석하고 문제를 발견하는 게 더 핵심일 때가 많기 때문이다.

## 7. 부족한 데이터를 직접 만들어 문제를 해결했다
문제 해결 방법으로 선택한 것이 `make9999Sentence.py`다.

이 스크립트는 긍정/부정 문장을 조합해서 자동으로 생성한다.

```python
def generate_random_sentence(label):
    adv = random.choice(adverbs)
    ending = random.choice(endings)
    ctx = random.choice(positive_contexts if label == 1 else negative_contexts)
    pred = random.choice(positive_predicates if label == 1 else negative_predicates)
    return f"{adv} {ctx} {pred}{ending}"
```

그리고 9,999개의 문장을 만든다.

```python
data = [[generate_random_sentence(random.randint(0, 1)), random.randint(0, 1)] for _ in range(9999)]
df.to_csv(filename, index=False, header=False, encoding='utf-8-sig')
```

즉, day12는 단순히 "데이터가 부족하네" 하고 끝난 게 아니라, Python으로 데이터 증강 스크립트까지 직접 만든 날이었다.

## 8. 데이터 증강은 왜 효과적일 수 있을까
감정 분석 모델이 특정 단어를 잘못 예측하는 건 대개 학습 데이터 분포가 부족하거나 치우쳐 있기 때문이다. 그래서 긍정/부정 문장 패턴을 더 많이 공급하면 모델이 표현 패턴을 더 넓게 학습할 수 있다.

README에도 이런 흐름이 정리돼 있다.

```text
generated_sentiment_9999.csv 생성
sentiment_20000_ko.csv 규모로 확장
Google Colab 환경에서 재학습
```

즉, 라벨링 도구로 수집한 데이터와 Python으로 생성한 증강 데이터를 함께 써서 더 나은 모델을 만드는 방향으로 갔다.

이건 아주 중요한 감각이다. 문제를 모델 구조만으로 해결하려 하지 않고, 데이터 측면에서 해결하려는 접근이기 때문이다.

## 9. 감정 분석기는 JavaFX와 Python 모델 사이의 다리 역할을 한다
`study03/root.fxml`는 감정 분석기 화면이다.

```xml
<TextField fx:id="tfSentence" promptText="여기에 문장을 입력하세요..." />
<Button fx:id="btnAnalyze" onAction="#onActionAnalyzeBtn" text="분석하기" />
<Label fx:id="lblResult" text="대기 중..." />
```

즉, 사용자는 여기서 문장을 직접 입력하고, 버튼을 누르면 결과를 받는다.

이 UI는 단순하지만 의미가 크다. 라벨링 도구가 "학습 데이터를 만드는 화면"이었다면, 감정 분석기는 "학습된 모델을 실제로 사용하는 화면"이기 때문이다.

## 10. JavaFX 컨트롤러가 Python 모델 실행 전체를 조율한다
`study03/rootController.java`는 버튼 클릭 시 Python 스크립트를 실행한다.

```java
ProcessBuilder pb = new ProcessBuilder("python", "src/study03/emotionModelRunner.py");
pb.redirectErrorStream(true);

Process process = pb.start();
```

그다음 Java가 문장을 Python으로 보낸다.

```java
BufferedWriter bw = new BufferedWriter(
    new OutputStreamWriter(process.getOutputStream(), "UTF-8")
);
bw.write(inputText);
bw.newLine();
bw.flush();
bw.close();
```

그리고 결과를 읽는다.

```java
BufferedReader br = new BufferedReader(
    new InputStreamReader(process.getInputStream(), "UTF-8")
);
```

즉, 자바 컨트롤러는 단순 UI 이벤트 처리기가 아니라:

- 입력 검증
- 외부 프로세스 실행
- 표준 입출력 연동
- 결과 해석
- UI 반영

까지 모두 조율하는 허브 역할을 한다.

## 11. 결과값을 사람이 읽을 수 있는 라벨로 바꾸는 마지막 단계
Python 모델은 숫자를 출력한다.

```python
print(int(pred))
```

자바 쪽은 그 값을 사람이 읽을 수 있는 형태로 바꾼다.

```java
if ("1".equals(trimmedLine)) {
    result = "긍정";
    break;
} else if ("0".equals(trimmedLine)) {
    result = "부정";
    break;
}
```

즉, 모델은 `0`과 `1`만 알아도 되고, UI는 그 결과를 사용자 친화적으로 해석해서 보여 준다. 이 역할 분리가 중요하다.

또 디버그 출력은 건너뛴다.

```java
if (line.startsWith("DEBUG:")) continue;
```

즉, 실제 연동에서는 "모델 출력"과 "로그 출력"을 구분해서 처리해야 한다는 점까지 의식하고 있다.

## 12. Python 쪽도 연동을 위해 UTF-8과 파일 경로를 신경 쓴다
`emotionModelRunner.py`는 단순 예측만 하는 게 아니라 인코딩 문제까지 고려한다.

```python
sys.stdin = io.TextIOWrapper(sys.stdin.detach(), encoding='utf-8')
sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding='utf-8')
```

그리고 모델과 벡터라이저 파일을 읽는다.

```python
MODEL_PATH = "src/study03/model.pkl"
VECTORIZER_PATH = "src/study03/vectorizer.pkl"

model = joblib.load(MODEL_PATH)
vectorizer = joblib.load(VECTORIZER_PATH)
```

이 부분이 중요한 이유는 실제 연동에서는 알고리즘보다 환경 문제가 더 자주 발목을 잡기 때문이다.

- 한글 인코딩
- 경로 문제
- 파일 누락

같은 것들이 바로 그런 예다. day12는 이런 현실적인 부분까지 직접 다뤘다는 점에서 의미가 크다.

## 13. 이 날은 사실상 작은 캡스톤 프로젝트였다
1월 22일 작업은 앞선 모든 학습을 거의 다 끌어온다.

- Java 기본 문법
- 클래스와 객체 설계
- 파일 입출력
- JavaFX UI
- FXML과 컨트롤러
- 이벤트 처리
- 외부 프로세스 실행
- Python 스크립트
- 머신러닝 모델 파일 로드
- 데이터 증강과 재학습

즉, 이 날은 하나의 문법 주제를 더 배운 날이 아니라, 지금까지 배운 것을 전부 연결해 작은 시스템으로 만든 날이라고 보는 편이 맞다.

## 14. day12가 중요한 이유는 "도구를 연결하는 힘"을 보여 주기 때문이다 <!-- short: 도구 연결의 가치 -->
자바만 잘 쓴다고 해서 감정 분석기가 자동으로 만들어지는 것은 아니다. Python만 안다고 해서 UI가 생기는 것도 아니다. 라벨링 도구만 있어도 모델이 바로 좋아지지 않는다.

day12의 핵심은 이걸 다 연결한 점이다.

- 사람이 데이터 라벨링을 하고
- 데이터가 저장되고
- 부족한 데이터를 Python으로 더 만들고
- 모델을 재학습하고
- 결과 모델을 JavaFX 화면에서 다시 호출한다

즉, "하나의 언어를 잘 쓰는 것"을 넘어 "여러 도구를 엮어 실제로 동작하는 흐름을 만든다"는 감각이 생기기 시작한 날이다.

## 15. 오늘 실습을 통해 얻은 핵심 감각
1월 22일 실습에서 꼭 남겨야 할 핵심은 아래와 같다.

- 라벨링 도구는 단순 UI가 아니라 데이터 구축 도구다.
- 실제 데이터는 긍정/부정만으로 안 나뉘는 경우가 많아 라벨 체계 자체를 조정해야 할 수 있다.
- 반복 작업용 UI는 CSS와 레이아웃 설계도 중요하다.
- 모델 성능 문제는 데이터 증강으로 해결할 수 있다.
- Python 스크립트는 데이터 생성과 모델 예측 모두에 유용하게 쓰인다.
- JavaFX는 학습된 모델을 사용하는 프런트엔드 역할을 할 수 있다.
- Java와 Python은 표준 입출력 계약만 잘 맞추면 충분히 강력하게 연결된다.
- 작은 프로젝트라도 데이터 수집부터 예측 UI까지 전체 사이클을 경험하는 것이 매우 중요하다.

이 감각은 이후 어떤 언어나 프레임워크로 넘어가도 그대로 남는다. 실제 프로젝트는 늘 하나의 기술만으로 끝나지 않기 때문이다.

## 오늘의 복습 질문
이 글만 읽고 아래 질문에 답할 수 있으면 day12 내용은 잘 정리된 것이다.

<details>
<summary>1. 라벨링 앱에서 중립 라벨을 추가한 이유는 무엇일까?</summary>

실제 문장은 긍정과 부정만으로 깔끔하게 나뉘지 않기 때문이다. 현실 데이터 분포를 더 정확히 반영하려면 중립 범주가 필요했다.

</details>

<details>
<summary>2. 즉시 저장 방식은 왜 라벨링 도구에서 특히 중요할까?</summary>

반복 작업 도중 종료나 오류가 발생해도 이미 라벨링한 결과를 잃지 않게 해 주기 때문이다.

</details>

<details>
<summary>3. <code>application.css</code> 같은 스타일 파일이 데이터 작업 도구에서도 중요한 이유는 무엇일까?</summary>

작업 버튼, 상태 표시, 레이아웃 가독성이 좋아져 반복 작업 효율이 올라가기 때문이다. 데이터 도구도 결국 사람이 오래 쓰는 UI다.

</details>

<details>
<summary>4. 모델이 &quot;좋아요&quot; 같은 표현을 잘못 분류했다는 사실은 어떤 문제를 시사할까?</summary>

학습 데이터가 부족하거나 특정 표현 패턴을 충분히 반영하지 못했다는 뜻이다. 데이터 품질과 다양성 문제가 드러난 것이다.

</details>

<details>
<summary>5. 데이터 증강은 왜 모델 성능 개선에 도움이 될 수 있을까?</summary>

유사한 표현을 더 많이 학습시켜 모델이 다양한 문장 패턴을 보게 하기 때문이다. 데이터 부족 문제를 어느 정도 보완할 수 있다.

</details>

<details>
<summary>6. JavaFX 감정 분석기에서 자바와 파이썬은 각각 어떤 역할을 맡고 있을까?</summary>

자바는 UI와 사용자 입력 처리를 맡고, 파이썬은 학습된 모델을 이용해 실제 감정 예측을 수행한다.

</details>

<details>
<summary>7. Python 모델이 숫자를 반환하고 Java가 그걸 &quot;긍정/부정&quot;으로 바꾸는 구조의 장점은 무엇일까?</summary>

모델 로직과 UI 표시 로직을 분리할 수 있다. Python은 예측에 집중하고, Java는 화면 표현을 유연하게 바꿀 수 있다.

</details>

<details>
<summary>8. day12를 작은 캡스톤 프로젝트라고 볼 수 있는 이유는 무엇일까?</summary>

데이터 수집, 라벨링, 증강, 재학습, 예측 UI까지 앞서 배운 기술들을 하나의 흐름으로 연결했기 때문이다.

</details>

## 마무리
1월 22일은 자바 학습 12일의 마지막 날이면서, 그동안 배운 내용이 가장 선명하게 하나로 묶인 날이었다. JavaFX로 반복 작업용 라벨링 도구를 만들고, 데이터 분류 체계를 현실에 맞게 조정하고, Python으로 데이터를 증강해 모델을 다시 학습시키고, 그 모델을 다시 JavaFX 화면에서 호출하는 구조까지 만들었기 때문이다.

즉, day12는 "감정 분석 모델을 붙여 봤다" 수준이 아니라, 데이터를 만들고 다듬고 학습시키고 보여 주는 전체 흐름을 처음 끝까지 경험한 날이었다. Java 학습이 여기서 끝났다는 점도 상징적이다. 문법 학습이 아니라 "작동하는 시스템을 만드는 방향"으로 제대로 넘어섰기 때문이다.
