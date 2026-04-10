---
title: "Java 학습 11일차: Java-Python 연동, 라벨링 도구, 감정 분석 준비"
slug: java-study-day11-java-python-interop-and-labeling-prep
date: 2026-01-21
author: Evan Yoon
category: study
subcategory: self-study
description: "1월 21일 Java 학습 기록. ProcessBuilder로 Java에서 Python 스크립트를 실행하고, 문장 데이터 전처리와 JavaFX 라벨링 도구 초안, 감정 분석 모델 연동 준비까지 이어진 과정을 자세히 정리했다."
tags:
  - java
  - study
  - processbuilder
  - python
  - labeling
  - javafx
  - ai
readTime: 17
series: Java Study
seriesOrder: 11
featured: false
draft: false
toc: true
---

## 오늘은 자바 앱이 자기 세계를 넘어 다른 프로그램과 연결되기 시작한 날이었다
`reference/javastudy/day0121/src`는 이전까지의 Java 문법 학습이나 JavaFX 화면 구성과는 결이 조금 다르다. `study01`에서는 Java가 Python 스크립트를 직접 실행하고, `study02`에서는 문장 데이터 라벨링용 JavaFX UI를 만들고, `study03`에서는 그 라벨링과 Python 감정 분석 모델을 하나의 흐름으로 잇기 위한 구조까지 보인다.

즉, day11은 "자바 코드만 잘 쓰는 단계"에서 "자바 앱이 외부 도구와 협업하는 단계"로 넘어가는 전환점이었다.

이날 학습 흐름을 크게 나누면 이렇다.

1. `ProcessBuilder`로 Java에서 Python을 실행한다.
2. 표준 입출력으로 Java와 Python 사이에 데이터를 주고받는다.
3. 라벨링용 원본 데이터를 읽고 정제하는 과정을 만든다.
4. JavaFX 기반 라벨링 UI를 설계하고 파일 저장 구조를 붙인다.
5. 감정 분석 모델을 JavaFX 화면에서 호출하는 준비까지 연결한다.

즉, day11은 단순한 앱 구현보다 "연결"과 "파이프라인"이 핵심인 날이었다.

## 1. Java가 다른 프로그램을 실행할 수 있다는 걸 확인했다
`study01/MainApp.java`는 매우 짧지만 의미가 크다.

```java
Scanner scan = new Scanner(System.in);
System.out.println("몇 단?");
String n = scan.next();

ProcessBuilder pb = new ProcessBuilder("python", "src\\study01\\gugudan.py", n);
Process process = pb.start();
```

이 코드는 자바 콘솔에서 입력받은 단수를 그대로 Python 스크립트에 넘긴다. 즉, Java가 "외부 프로세스 실행기" 역할까지 할 수 있다는 걸 보여 준다.

이전까지는 자바 코드 안에서만 모든 걸 처리했다. 그런데 이제는 필요한 기능을 다른 언어 프로그램에 맡기고, 자바는 그것을 조정하는 쪽으로 확장된다.

## 2. 표준 출력 읽기: Python 결과를 자바가 다시 받아온다
`ProcessBuilder`로 실행만 하는 게 아니라, 결과도 읽어 온다.

```java
BufferedReader reader = new BufferedReader(
    new InputStreamReader(process.getInputStream())
);

String line;
while((line = reader.readLine()) != null){
    System.out.println(line);
}
```

즉, Python이 콘솔에 출력한 내용을 Java가 다시 읽어 자기 화면이나 콘솔에 보여 줄 수 있다.

이 흐름이 중요한 이유는 아주 명확하다.

- Java는 UI를 맡고
- Python은 계산이나 모델 추론을 맡고
- 둘은 표준 입출력으로 데이터를 주고받을 수 있다

이것이 이후 감정 분석기 구조의 핵심이 된다.

## 3. `waitFor()`와 종료 코드: 외부 프로세스도 실행 흐름의 일부다
`study01/MainApp.java` 마지막 부분도 중요하다.

```java
int exitCode = process.waitFor();
System.out.println("파이썬 종료 코드 = " + exitCode);
```

여기서 `waitFor()`는 Python 프로세스가 끝날 때까지 Java가 기다리게 만든다. 그리고 종료 코드를 확인할 수 있다.

즉, 자바 입장에서는 외부 프로그램도 그냥 "한 번 실행하고 끝"이 아니라, 성공했는지 실패했는지 확인해야 하는 작업 단위다.

이 감각은 나중에:

- Python 모델 호출
- 외부 CLI 도구 실행
- FFmpeg, Git, OCR 프로그램 같은 외부 툴 연동

같은 곳에서도 그대로 이어진다.

## 4. Python 쪽은 아주 단순하지만, 그래서 더 의미가 있다
`study01/gugudan.py`는 매우 짧다.

```python
import sys

n = int(sys.argv[1])
for i in range(1, 10):
    print(f"{n} x {i} = {n * i}")
```

이 예제의 의미는 Python 로직 자체가 아니라 인터페이스 형태에 있다.

- Java가 인자를 넘긴다.
- Python이 그 인자를 읽는다.
- 결과를 표준 출력으로 돌려준다.

즉, 언어가 달라도 "입력 -> 처리 -> 출력" 계약만 맞추면 연결할 수 있다는 것을 가장 단순한 형태로 확인한 셈이다.

## 5. 라벨링용 원본 데이터를 먼저 정리하는 단계가 필요했다
`study02/MainApp.java`는 아직 GUI가 아니라 콘솔 전처리 테스트에 가깝다.

```java
FileManager originalFm = new FileManager("src\\study02\\data.txt", false);

String str = originalFm.getAllData();
String[] subStr = str.split("\\n");
for(int i=0;i<subStr.length;i++){
    String[] subSubStr = subStr[i].split("]");
    try {
        System.out.println(i + " " + subSubStr[1].trim());
    } catch (ArrayIndexOutOfBoundsException e) {
    }
}
```

이 코드는 원본 데이터셋에서 `]` 뒤의 실제 문장 부분만 뽑아내려는 시도다.

즉, day11은 곧바로 예쁜 UI를 만드는 날이 아니라, 그 UI에 넣을 데이터가 어떤 형태여야 하는지 정리하는 날이기도 했다.

### 왜 전처리가 먼저일까
라벨링 도구는 결국 사람이 문장을 하나씩 읽고 평가하게 만드는 앱이다. 그런데 원본 데이터에 메타 정보나 불필요한 접두부가 섞여 있으면 라벨링 효율이 떨어진다. 그래서 화면을 만들기 전에 어떤 텍스트를 보여 줄지 정제하는 과정이 먼저 필요하다.

## 6. 파일 입출력 유틸은 계속 재사용된다
`study02/FileManager.java`는 day10에서도 보였던 형태와 거의 비슷하다.

```java
public void dataCreate(String tupleStr) throws IOException {
    OutputStream os = new FileOutputStream(fileName, append);
    Writer writer = new OutputStreamWriter(os);
    writer.write(tupleStr);
    writer.flush();
    writer.close();
}

public String getAllData() throws IOException {
    String dataStr = "";
    Reader reader = new FileReader(fileName);
    ...
    return dataStr;
}
```

이것이 중요한 이유는 학습 흐름이 이어지고 있다는 점이다.

- day8: 파일 입출력 기초
- day10: JavaFX 메모장/주소록 파일 처리
- day11: 라벨링 데이터 읽기/쓰기 재사용

즉, 한 번 만든 IO 감각이 새로운 프로젝트에서도 그대로 재활용되기 시작했다.

## 7. JavaFX 라벨링 앱 초안은 이미 구조가 꽤 구체적이다
`study02/AppMain.java`는 라벨링 앱용 JavaFX 창을 띄운다.

```java
Parent root = FXMLLoader.load(getClass().getResource("root.fxml"));
primaryStage.setTitle("라벨링 앱");
primaryStage.setScene(scene);
```

`root.fxml`는 단순하지만 목적이 분명하다.

```xml
<Label text="DATA LABELING" />
<Label fx:id="lblPg" text="1 / 100" />
<Label fx:id="lblData" text="데이터 로딩 중..." />
<Button onAction="#onActionLikeBtn" text="긍정 (Positive)" />
<Button onAction="#onActionNeutralBtn" text="중립 (Neutral)" />
<Button onAction="#onActionDislikeBtn" text="부정 (Negative)" />
```

즉, 이 앱의 핵심 흐름은 처음부터 명확하다.

1. 문장 하나를 보여 준다.
2. 사용자가 긍정/중립/부정을 누른다.
3. 결과를 저장한다.
4. 다음 문장으로 넘어간다.

이건 작은 UI지만, 사실상 라벨링 툴의 본질이 이미 다 들어 있다.

## 8. 컨트롤러는 원본 데이터를 정제해서 메모리에 올린다
`study02/rootController.java`의 `initialize()`는 day11에서 매우 중요하다.

```java
String str = loadFm.getAllData();
String[] subStr = str.split("\\n");

for(int i=0;i<subStr.length;i++){
    String[] subSubStr = subStr[i].split("]");
    try {
        if(subSubStr[1] != null)
            size++;
    } catch (ArrayIndexOutOfBoundsException e) {
    }
}
```

그 다음 정제된 문장만 `preData` 배열에 저장한다.

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

즉, 이 라벨링 앱은 단순히 파일을 읽는 것이 아니라:

- 원본 전체를 읽고
- 라벨링 가능한 문장만 걸러내고
- 메모리 배열에 적재한 뒤
- 그 배열을 순차적으로 화면에 보여 준다

이 흐름을 보면 데이터 전처리와 UI가 어떻게 만나는지 감이 잡힌다.

## 9. 진행 상태 표시와 순차 탐색 구조
초기화 마지막 부분은 UI 경험 측면에서 꽤 중요하다.

```java
lblData.setText(preData[0]);
lblPg.setText("1" + "/" + size);
```

즉, 사용자는 지금 몇 번째 데이터를 보고 있는지 바로 알 수 있다. 이런 진행률 표시 하나만 있어도 라벨링 도구는 훨씬 실제 앱처럼 느껴진다.

이후 버튼을 누를 때마다:

```java
lblData.setText(preData[++count]);
lblPg.setText((count+1) + "/" + size);
```

다음 문장과 진행률이 함께 갱신된다.

즉, 작은 디테일이지만 "도구"로서의 사용성을 생각하기 시작한 흔적이 보인다.

## 10. 라벨링 결과를 즉시 파일에 저장하는 구조
각 버튼 핸들러는 거의 같은 패턴을 가진다.

긍정:

```java
String t = lblData.getText();
String str = t + ", 1\n";
saveFm.dataCreate(str);
```

부정:

```java
String t = lblData.getText();
String str = t + ", 0\n";
saveFm.dataCreate(str);
```

즉, 사람이 클릭한 결과가 바로 `labeled_data.txt`에 누적된다.

### 왜 즉시 저장이 중요할까
라벨링 작업은 길고 반복적일 수 있다. 프로그램이 중간에 꺼지거나 오류가 나면 진행 내용이 날아가면 안 된다. 그래서 버튼을 누를 때마다 바로 저장하는 구조가 안정적이다.

이는 README에도 적혀 있는 핵심 아이디어와 맞닿아 있다. 학습 단계지만 이미 실무적인 안정성 감각이 조금씩 들어가고 있다.

## 11. 아직 초안이지만, 라벨링 도구의 뼈대는 이미 완성돼 있다
`study02/rootController.java`를 보면 중립 버튼도 현재는 `1`로 저장되고 있어 라벨 정의가 완전히 정리되진 않았다.

```java
public void onActionNeutralBtn(ActionEvent e) throws IOException {
    ...
    String str = t + ", 1\n";
    saveFm.dataCreate(str);
}
```

이건 아직 초안 단계라는 뜻이다. 하지만 중요한 건 완벽한 라벨 체계보다 먼저 "사람이 문장을 보고 버튼으로 라벨링하고 결과를 쌓는 도구 구조"를 만들어 냈다는 점이다.

즉, day11의 핵심은 완성도보다 방향성과 연결 구조다.

## 12. 감정 분석기로 확장되는 구조도 이미 보이기 시작한다
`study03`는 day11의 확장 방향을 잘 보여 준다. `AppMain.java`는 "감정 분석기"라는 이름의 JavaFX 앱을 띄우고, `root.fxml`는 문장 입력창과 결과 라벨을 가진다.

```xml
<TextField fx:id="tfSentence" promptText="여기에 문장을 입력하세요..." />
<Button fx:id="btnAnalyze" onAction="#onActionAnalyzeBtn" text="분석하기" />
<Label fx:id="lblResult" text="대기 중..." />
```

컨트롤러는 버튼 클릭 시 Python 모델을 실행한다.

```java
ProcessBuilder pb = new ProcessBuilder("python", "src/study03/emotionModelRunner.py");
pb.redirectErrorStream(true);
Process process = pb.start();
```

그리고 Java가 Python 쪽 표준 입력으로 문장을 넘긴다.

```java
BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream(), "UTF-8"));
bw.write(inputText);
bw.newLine();
bw.flush();
bw.close();
```

즉, `study01`의 구구단 예제가 여기서 실제 AI 연동 형태로 발전한다.

## 13. Python 모델 실행 스크립트도 입력/출력 계약을 분명히 갖는다
`study03/emotionModelRunner.py`는 모델과 벡터라이저를 불러온 뒤, 표준 입력에서 문장을 읽고 예측값을 출력한다.

```python
text = sys.stdin.readline().strip()

x = vectorizer.transform([text])
pred = model.predict(x)[0]

print(int(pred))
```

이 구조는 매우 중요하다. Java와 Python 사이의 계약이 명확하기 때문이다.

- Java -> 표준 입력으로 문장 전달
- Python -> 모델 예측 후 `0` 또는 `1` 출력
- Java -> 그 결과를 읽어 화면에 "긍정/부정"으로 변환

즉, 서로 다른 언어를 연결할 때는 거대한 프레임워크보다 이런 단순한 입출력 계약이 오히려 강력할 수 있다.

## 14. 데이터 생성과 모델 개선까지 하나의 흐름으로 연결된다
`study03/README.txt`와 `make9999Sentence.py`를 보면, 이 프로젝트는 단순 실행 실험을 넘어 데이터 수집-라벨링-증강-학습-예측까지 이어지는 전체 흐름을 목표로 하고 있다.

`make9999Sentence.py`는 긍정/부정 문장을 자동 생성한다.

```python
data = [[generate_random_sentence(random.randint(0, 1)), random.randint(0, 1)] for _ in range(9999)]
df.to_csv(filename, index=False, header=False, encoding='utf-8-sig')
```

즉, day11은 단순히 "Java에서 Python 한 번 실행해 봄"이 아니다. 이후 감정 분석기라는 더 큰 프로젝트를 위한 데이터 파이프라인과 실행 구조를 본격적으로 깔기 시작한 날이다.

## 15. 오늘 실습을 통해 얻은 핵심 감각
1월 21일 실습에서 꼭 남겨야 할 핵심은 아래와 같다.

- Java는 `ProcessBuilder`로 외부 프로그램을 실행할 수 있다.
- 표준 입력과 출력은 언어가 다른 프로그램 사이를 연결하는 좋은 통로다.
- 외부 프로세스도 종료 코드와 예외 처리를 포함한 작업 단위로 다뤄야 한다.
- 데이터 라벨링 도구는 UI보다 먼저 데이터 구조와 전처리 흐름이 중요하다.
- JavaFX는 단순 화면이 아니라 반복 작업용 도구 UI도 만들 수 있다.
- 즉시 저장 구조는 긴 작업에서 안정성을 높여 준다.
- Java와 Python은 역할을 나눠 협업할 수 있다.
- 작은 실험 코드가 이후 AI 앱 구조의 기반이 될 수 있다.

이 감각이 생기면 자바 앱이 더 이상 닫힌 프로그램이 아니라, 다른 도구와 모델을 호출하고 조합하는 허브가 될 수 있다는 걸 이해하게 된다.

## 오늘의 복습 질문
이 글만 읽고 아래 질문에 답할 수 있으면 day11 내용은 잘 정리된 것이다.

<details>
<summary>1. <code>ProcessBuilder</code>는 왜 단순 라이브러리 호출과 다른 의미를 가질까?</summary>

자바 내부 코드가 아니라 외부 프로세스 자체를 실행하고 제어하기 때문이다. 즉, 다른 언어 프로그램을 하나의 작업 단위로 다루게 된다.

</details>

<details>
<summary>2. Java에서 Python 결과를 받으려면 어떤 스트림을 읽어야 할까?</summary>

보통 Python의 표준 출력에 해당하는 프로세스의 입력 스트림을 읽어 결과를 받는다.

</details>

<details>
<summary>3. <code>waitFor()</code>는 왜 필요할까?</summary>

외부 프로세스가 끝날 때까지 기다리고 종료 상태를 확인하기 위해서다. 작업 완료 전에 결과를 읽거나 다음 단계를 진행하는 실수를 줄여 준다.

</details>

<details>
<summary>4. 라벨링 도구에서 원본 데이터를 먼저 정제하는 이유는 무엇일까?</summary>

중복, 잡음, 불필요한 형식을 먼저 정리해야 라벨링 품질이 올라가고 이후 모델 학습 데이터도 더 안정적이 되기 때문이다.

</details>

<details>
<summary>5. 왜 라벨링 결과를 버튼 클릭 때마다 바로 저장하는 구조가 유리할까?</summary>

작업 중간에 앱이 꺼지거나 오류가 나도 이미 한 작업을 잃지 않기 때문이다. 긴 반복 작업에서 안정성이 높아진다.

</details>

<details>
<summary>6. JavaFX 라벨링 앱에서 <code>lblPg</code> 같은 진행 표시가 왜 중요할까?</summary>

사용자가 현재 얼마나 진행했는지 바로 알 수 있어 작업 피로를 줄이고, 도구가 제대로 동작 중인지 확인하기 쉽다.

</details>

<details>
<summary>7. Java와 Python 사이의 입력/출력 계약은 어떻게 설계되어 있나?</summary>

Java가 입력 형식을 맞춰 Python에 넘기고, Python은 약속된 출력 형식으로 결과를 돌려주는 식으로 설계된다. 양쪽이 같은 규칙을 공유해야 안정적으로 연결된다.

</details>

<details>
<summary>8. 구구단 예제와 감정 분석기 예제는 어떤 점에서 같은 구조를 공유할까?</summary>

둘 다 Java가 외부 Python 프로세스를 실행하고, 입력을 주고, 결과를 표준 출력으로 받아 처리한다는 점에서 같은 연동 구조를 가진다.

</details>

## 마무리
1월 21일은 화려한 완성 앱을 만든 날이라기보다, 이후 더 큰 프로젝트가 가능해지게 만드는 연결부를 만든 날이었다. Java가 Python을 실행하고 결과를 받아올 수 있다는 걸 확인했고, 라벨링 데이터를 정제하고 저장하는 흐름을 만들었고, 결국 JavaFX 화면에서 감정 분석 모델을 호출할 수 있는 구조까지 이어졌기 때문이다.

즉, day11은 "Java-Python 연동 실험"이 아니라 "자바 앱이 외부 모델과 데이터 파이프라인을 품기 시작한 첫날"이었다. 이후 데이터 라벨링, 모델 재학습, 감정 분석 UI 확장은 모두 이날 만든 연결 구조 위에서 자연스럽게 자라난다.
