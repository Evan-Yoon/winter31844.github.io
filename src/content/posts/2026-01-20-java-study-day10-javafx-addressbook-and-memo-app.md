---
title: "Java 학습 10일차: JavaFX 이미지 뷰어, 메모장, GUI 주소록 CRUD"
slug: java-study-day10-javafx-addressbook-and-memo-app
date: 2026-01-20
author: Evan Yoon
category: study
subcategory: bootcamp
description: "1월 20일 Java 학습 기록. JavaFX로 이미지 리소스 전환, 파일 기반 메모장, 다중 창 주소록 CRUD를 구현하며 콘솔 프로그램을 실제 GUI 앱 구조로 확장한 과정을 자세히 정리했다."
tags:
  - java
  - study
  - javafx
  - address-book
  - gui
  - memo-app
  - crud
readTime: 17
series: Java Study
seriesOrder: 10
featured: false
draft: false
toc: true
---

## 어제 배운 JavaFX를 실제 애플리케이션 형태로 밀어붙인 날이었다
`reference/javastudy/day0120/src`에는 `study01`, `study02`, `addressBook` 세 묶음이 들어 있다. day9가 JavaFX의 생명주기, FXML, 컨트롤러, 계산기 UI처럼 "기초 구조"를 배우는 날이었다면, day10은 그 구조를 이용해 실제로 쓸 만한 작은 GUI 앱을 만드는 날이었다.

이날 학습 흐름을 크게 나누면 이렇다.

1. 이미지 리소스를 불러와 버튼 이벤트로 화면을 바꾼다.
2. 메모장처럼 파일과 UI를 연결한다.
3. 주소록 데이터를 파일에 저장하는 CRUD 구조를 GUI로 옮긴다.
4. 기능별로 창을 나누고 컨트롤러를 분리한다.
5. 백그라운드 갱신까지 시도하며 UI 앱 구조를 더 현실적으로 만든다.

즉, day10은 JavaFX를 "예제용 화면"에서 "실제 프로그램 형태"로 끌고 가기 시작한 날이었다.

## 1. JavaFX에서 리소스 파일을 다루는 법을 배웠다
`study01`은 이미지 뷰어 예제다. `root.fxml`를 보면 `ImageView`와 세 개의 버튼이 있다.

```xml
<ImageView fx:id="ivAnimal" fitHeight="150.0" fitWidth="200.0">
   <image>
      <Image url="@../image/tiger.jpg" />
   </image>
</ImageView>
```

처음 화면에는 호랑이 이미지가 보이고, 아래 버튼을 누르면 다른 이미지로 바뀐다.

```xml
<Button onAction="#onActionTigerBtn" text="호랑이" />
<Button onAction="#onActionPandaBtn" text="팬더" />
<Button onAction="#onActionBearBtn" text="곰" />
```

컨트롤러는 아주 직관적이다.

```java
@FXML
ImageView ivAnimal;

public void onActionPandaBtn(ActionEvent e) {
    Image img = new Image(getClass().getResourceAsStream("/image/panda.jpg"));
    ivAnimal.setImage(img);
}
```

이 예제는 단순해 보여도 중요한 개념이 여럿 들어 있다.

- 리소스 파일 경로를 코드에서 어떻게 불러오는지
- `ImageView`를 컨트롤러와 어떻게 연결하는지
- 버튼 이벤트에 따라 화면 상태를 어떻게 바꾸는지

즉, "버튼을 누르면 화면이 바뀐다"는 GUI의 가장 기본적인 상호작용을 이미지 예제로 익힌 셈이다.

## 2. `getResourceAsStream()` 감각은 이후에도 계속 중요하다
이미지 뷰어에서 핵심은 이 한 줄이다.

```java
new Image(getClass().getResourceAsStream("/image/panda.jpg"))
```

이 방식은 단순히 이미지를 읽는 문법이 아니다. 자바 애플리케이션 안에서 패키지 리소스를 경로로 읽는 기본 패턴이다.

나중에 불러올 대상은 이미지가 아닐 수도 있다.

- 아이콘
- 설정 파일
- 템플릿 파일
- 내장된 데이터 파일

즉, day10의 이미지 예제는 JavaFX 그림 하나 바꾸는 정도가 아니라, 리소스 로딩의 감각을 익히는 중요한 출발점이었다.

## 3. 메모장: 파일 입출력을 GUI와 연결하기
`study02`는 메모장 예제다. `root.fxml`는 매우 단순하다.

```xml
<TextArea fx:id="taContent" />
<Button onAction="#onActionSaveBtn" text="Save" />
<Button onAction="#onActionClearBtn" text="Clear" />
```

이전까지는 파일 입출력을 콘솔에서만 다뤘지만, 이제 그 파일 읽기/쓰기를 JavaFX UI와 연결한다.

컨트롤러 초기화 부분:

```java
@FXML
private void initialize() throws IOException {
    FileManager fm = new FileManager("src/study02/memo.txt", false);
    taContent.setText(fm.getAllData());
}
```

저장 버튼:

```java
public void onActionSaveBtn(ActionEvent e) throws IOException {
    FileManager fm = new FileManager("src/study02/memo.txt", false);
    fm.dataCreate(taContent.getText());
}
```

지우기 버튼:

```java
public void onActionClearBtn(ActionEvent e) {
    taContent.setText("");
}
```

### 왜 이 예제가 중요한가
콘솔 프로그램과 GUI 프로그램은 겉보기는 달라도 내부 논리는 크게 다르지 않다.

- 데이터를 읽는다.
- 화면에 보여 준다.
- 사용자가 수정한다.
- 다시 저장한다.

즉, day10의 메모장은 "파일 입출력 + JavaFX 컨트롤러"가 연결되는 첫 실용 예제라고 볼 수 있다.

## 4. GUI에서도 파일 로직은 별도 클래스로 분리하는 게 좋다
`study02/FileManager.java`는 메모장 파일 처리를 담당한다.

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

이 구조의 장점은 분명하다.

- 컨트롤러는 화면 처리에 집중하고
- 파일 저장/조회 로직은 `FileManager`가 맡는다

즉, GUI 앱이라고 해서 모든 코드를 컨트롤러에 몰아넣지 않고, 역할을 분리하는 감각을 배우기 시작한 것이다. 이건 이후 주소록, 메모장, JavaFX 프로젝트 전체에서 매우 중요해진다.

## 5. GUI 주소록: 콘솔 CRUD가 화면 앱으로 확장됐다 <!-- short: 5. GUI 주소록 -->
day10의 핵심은 역시 `addressBook` 패키지다. 구조를 보면 이미 꽤 애플리케이션답다.

- `AppMain.java`: 메인 창 실행
- `root.fxml`: 목록 화면
- `create.fxml`: 신규 입력 창
- `update.fxml`: 수정 창
- `delete.fxml`: 삭제 창
- `FileManager.java`: 파일 저장/조회/수정/삭제
- `Model.java`: 주소록 한 건의 데이터 모델

즉, day4와 day8에서 콘솔과 파일로 다루던 주소록이 이제 JavaFX 기반 GUI 앱으로 올라온 것이다.

## 6. 메인 화면은 목록을 보여 주고, 기능별 창을 여는 허브 역할을 한다
`addressBook/root.fxml`는 메인 화면이다.

```xml
<TextArea fx:id="taText" prefHeight="262.0" prefWidth="395.0" />
<Button onAction="#openCreateStage" text="신규" />
<Button onAction="#openUpdateStage" text="수정" />
<Button onAction="#openDeleteStage" text="삭제" />
```

`rootController.java`는 버튼을 누를 때마다 새 창을 연다.

```java
private void openCreateStage() throws Exception {
    Parent create = FXMLLoader.load(getClass().getResource("create.fxml"));
    Scene scene = new Scene(create);

    createStage = new Stage();
    createStage.setScene(scene);
    createStage.setTitle("최강 주소록 - 신규 입력");
    createStage.show();
}
```

수정 창과 삭제 창도 같은 패턴이다.

### 여기서 중요한 점
이제 앱이 단일 화면이 아니라 기능별 화면을 분리해 가진다. 즉, 하나의 `Stage`만 다루던 초기 JavaFX 예제에서 벗어나 "창이 여러 개인 앱"으로 확장되고 있다.

## 7. `Stage`를 기능별로 나누면 UI 흐름이 훨씬 명확해진다
`rootController.java`는 아래처럼 정적 `Stage` 참조를 들고 있다.

```java
public static Stage createStage;
public static Stage updateStage;
public static Stage deleteStage;
```

이 방식은 학습용 구조로는 꽤 직관적이다. 각 기능 창을 열고, 해당 컨트롤러에서 저장/취소 후 `close()` 할 수 있기 때문이다.

예를 들어 `createController.java`에서는 저장 후 바로 창을 닫는다.

```java
rootController.createStage.close();
```

즉, 메인 화면과 서브 화면 사이의 관계를 단순하게 이해하기에 좋은 구조다.

## 8. 신규 입력 창: 모델 객체를 만들고 파일에 추가한다 <!-- short: 8. 신규 입력 창 -->
`create.fxml`는 이름, 전화번호, 성별을 입력받는 화면이다.

```xml
<TextField fx:id="tfName" />
<TextField fx:id="tfPhoneNum" />
<TextField fx:id="tfGender" />
<Button onAction="#onActionSaveBtn" text="저장" />
```

`createController.java`는 입력값을 받아 모델을 만든다.

```java
Model m = new Model();

m.setName(tfName.getText());
m.setPhoneNum(tfPhoneNum.getText());
boolean g = tfGender.getText().equals("남") ? true : false;
m.setGender(g);
fm.dataCreate(m.toString() + "\n");
```

여기서 중요한 흐름은 다음과 같다.

1. 화면 입력값을 읽는다.
2. `Model` 객체에 담는다.
3. `toString()`으로 파일 저장 형식 문자열을 만든다.
4. `FileManager`로 파일에 추가한다.
5. 창을 닫는다.

즉, UI 입력과 데이터 모델, 파일 저장이 한 흐름으로 이어진다.

## 9. `Model` 클래스가 데이터 형식을 책임진다
`Model.java`는 주소록 한 건을 표현한다.

```java
private int idx;
private String name;
private String phoneNum;
private boolean gender;
```

특히 생성자에서 현재 파일 내용을 읽어 다음 순번을 자동으로 계산하는 점이 인상적이다.

```java
public Model() throws IOException {
    FileManager fm = new FileManager(rootController.FILE_NAME, true);
    String data = fm.getAllData();
    if(data.length() == 0)
        idx = 1;
    else {
        idx = data.split("\\n").length + 1;
    }
}
```

그리고 `toString()`이 파일 저장 형식을 정의한다.

```java
return this.idx + "\t" +
       this.name + "\t" +
       this.phoneNum + "\t" +
       (this.gender ? "남" : "여");
```

즉, 모델 객체는 단순 데이터 묶음이 아니라 저장 포맷까지 책임지는 역할을 한다.

## 10. 수정 창: 먼저 조회하고, 그다음 바꾼다
`update.fxml`는 순번을 입력해 기존 데이터를 조회한 뒤 수정하는 구조다.

컨트롤러의 조회 부분:

```java
String data = fm.getAllData();
String num = tfNum.getText();

String[] subData = data.split("\\n");
for(int i=0;i<subData.length;i++) {
    String[] subSubData = subData[i].split("\\t");
    if(num.equals(subSubData[0])){
        tfName.setText(subSubData[1]);
        tfPhoneNum.setText(subSubData[2]);
        tfGender.setText(subSubData[3]);
    }
}
```

수정 버튼은 이렇게 처리한다.

```java
Model m = new Model();
m.setIdx(Integer.parseInt(tfNum.getText()));
m.setName(tfName.getText());
m.setPhoneNum(tfPhoneNum.getText());
m.setGender(tfGender.getText().equals("남자") ? true : false);

fm.updateData(Integer.parseInt(tfNum.getText()), m);
```

이 구조는 데이터 수정의 전형적인 흐름을 보여 준다.

1. 대상을 찾는다.
2. 기존 값을 화면에 채운다.
3. 사용자가 수정한다.
4. 새 모델을 만들어 저장한다.

즉, CRUD 중 `U(Update)`의 감각을 GUI로 익히는 과정이다.

## 11. 삭제 창: 조회 후 확인하고 삭제한다
`delete.fxml`와 `deleteController.java`도 비슷한 흐름이다.

조회:

```java
if(num.equals(subSubData[0])){
    tfName.setText(subSubData[1]);
    tfPhoneNum.setText(subSubData[2]);
    tfGender.setText(subSubData[3]);
}
```

삭제:

```java
fm.delData(Integer.parseInt(tfView.getText()));
rootController.deleteStage.close();
```

이 구조는 단순하지만 매우 중요하다. 사용자에게 삭제 대상이 맞는지 한 번 보여 주고, 그다음 실제 삭제를 수행하기 때문이다. 즉, UI는 단순 입력창이 아니라 작업 확인 단계까지 제공하는 도구라는 점을 이해하게 된다.

## 12. `FileManager`가 사실상 주소록의 서비스 계층 역할을 한다
`addressBook/FileManager.java`는 CRUD 로직의 핵심이다.

추가:

```java
public void dataCreate(String tupleStr) throws IOException
```

전체 조회:

```java
public String getAllData() throws IOException
```

삭제:

```java
public void delData(int delNum) throws IOException
```

수정:

```java
public void updateData(int updateNum, Model m) throws IOException
```

특히 삭제와 수정은 파일 전체를 읽고, 줄 단위로 쪼개고, 필요한 줄만 바꾼 뒤 다시 전체를 덮어쓴다.

이 방식은 비효율적일 수 있지만, 학습용으로는 아주 좋다. 왜냐하면 "데이터를 어떻게 구조화하고 다시 조합하는가"가 눈에 보이기 때문이다.

즉, 아직 DB는 아니지만 CRUD의 본질은 충분히 담겨 있다.

## 13. 백그라운드 갱신까지 시도했다
`rootController.java`의 `initialize()`는 `ShowDataThread`를 시작한다.

```java
Thread thread = new ShowDataThread(taText);
thread.setDaemon(true);
thread.start();
```

`ShowDataThread.java`는 1초마다 파일 내용을 읽어 `TextArea`에 반영한다.

```java
while (true) {
    FileManager fm = new FileManager(rootController.FILE_NAME, false);
    taText.setText(fm.getAllData());
    Thread.sleep(1000);
}
```

이 구조는 학습 단계답게 단순하지만, 중요한 의미가 있다. GUI 앱도 단순 이벤트 처리만 있는 게 아니라 백그라운드 작업과 화면 갱신이 결합될 수 있다는 점을 직접 실험한 것이다.

다만 실무적으로는 JavaFX UI 스레드 규칙 때문에 더 신중한 방식이 필요하다는 점도 나중에 배우게 된다. 그래도 입문 단계에서 "화면은 고정된 것이 아니라 계속 갱신될 수 있다"는 감각을 얻는 데는 좋은 예제다.

## 14. day10은 JavaFX를 "진짜 앱 구조"로 밀어 올린 날이었다
day9의 계산기는 하나의 화면 안에서 입력과 계산을 처리하는 구조였다. day10은 그보다 한 단계 더 나아간다.

- 파일과 연결된 메모장
- 이미지 리소스를 바꾸는 뷰어
- 여러 창으로 분리된 주소록 CRUD
- 별도 파일 매니저와 모델 클래스
- 백그라운드 데이터 표시 갱신

즉, 이제는 단순한 위젯 실습이 아니라 애플리케이션 구조를 생각하게 된다.

## 15. 오늘 실습을 통해 얻은 핵심 감각
1월 20일 실습에서 꼭 남겨야 할 핵심은 아래와 같다.

- JavaFX UI는 이벤트에 따라 화면 상태를 바꿀 수 있다.
- 리소스 파일은 `getResourceAsStream()`으로 읽어 올 수 있다.
- 파일 입출력 로직은 컨트롤러와 분리하는 편이 좋다.
- 메모장처럼 단순한 앱도 읽기-수정-저장 흐름을 가진다.
- GUI 주소록은 콘솔 CRUD를 화면 기반으로 확장한 형태다.
- 기능별 창을 분리하면 앱 구조가 더 명확해진다.
- 모델 클래스는 데이터와 저장 형식을 함께 표현할 수 있다.
- 파일 기반 CRUD도 이미 충분히 애플리케이션답다.
- 백그라운드 작업과 UI 갱신은 GUI 앱에서 중요한 주제다.

이 감각이 생기면 이후 JavaFX 프로젝트를 만들 때도 "화면만 그리는 것"이 아니라 "기능을 어떤 화면과 어떤 클래스로 나눌지"를 생각하게 된다.

## 오늘의 복습 질문
이 글만 읽고 아래 질문에 답할 수 있으면 day10 내용은 잘 정리된 것이다.

<details>
<summary>1. <code>ImageView</code>에 새 이미지를 반영하려면 어떤 흐름이 필요할까?</summary>

리소스나 파일에서 이미지를 읽어 `Image` 객체를 만들고, 그것을 `ImageView`에 다시 설정하면 된다.

</details>

<details>
<summary>2. 메모장 예제에서 파일 읽기/쓰기 로직을 컨트롤러 밖으로 뺀 이유는 무엇일까?</summary>

UI 로직과 파일 처리 로직의 책임을 분리하기 위해서다. 코드가 더 읽기 쉽고 재사용과 유지보수가 쉬워진다.

</details>

<details>
<summary>3. GUI 주소록에서 메인 창과 신규/수정/삭제 창을 나눈 장점은 무엇일까?</summary>

기능별 흐름이 분리되어 화면 역할이 명확해지고, 각 작업을 더 독립적으로 관리할 수 있다.

</details>

<details>
<summary>4. <code>Model.toString()</code>이 중요한 이유는 무엇일까?</summary>

리스트나 화면에 객체를 표시할 때 사람이 읽기 좋은 문자열 형태를 제공하기 때문이다. 객체 내용을 자연스럽게 보여 줄 수 있다.

</details>

<details>
<summary>5. 수정 기능은 왜 &quot;조회 -&gt; 값 채우기 -&gt; 수정 -&gt; 저장&quot; 흐름으로 가는 것이 자연스러울까?</summary>

기존 데이터를 먼저 보여 주고 사용자가 필요한 부분만 바꾼 뒤 다시 저장하는 방식이기 때문이다. 수정 UX와 데이터 흐름이 명확하다.

</details>

<details>
<summary>6. 파일 기반 CRUD에서 삭제나 수정 시 전체 파일을 다시 쓰는 이유는 무엇일까?</summary>

텍스트 파일은 DB처럼 특정 레코드만 정교하게 바꾸기 어렵기 때문이다. 메모리에서 수정한 전체 내용을 다시 저장하는 편이 단순하다.

</details>

<details>
<summary>7. 백그라운드 갱신 스레드는 어떤 역할을 하나?</summary>

UI를 멈추지 않게 하면서 뒤에서 데이터 상태를 갱신하거나 주기적으로 화면 반영을 돕는 역할을 한다.

</details>

<details>
<summary>8. day9 계산기와 day10 주소록의 구조 차이는 무엇일까?</summary>

day9는 단일 화면 중심의 입력-계산-출력 구조에 가깝고, day10은 파일 저장, 모델 분리, 여러 창, CRUD 흐름까지 포함한 더 애플리케이션다운 구조다.

</details>

## 마무리
1월 20일은 JavaFX 학습이 예제 위젯 수준을 벗어나 실제 프로그램 구조를 갖추기 시작한 날이었다. 화면 하나를 띄우는 데서 끝나지 않고, 이미지 리소스를 바꾸고, 메모를 파일에 저장하고, 주소록을 CRUD 방식으로 다루고, 기능별 창을 분리해 컨트롤러를 나누는 단계까지 갔기 때문이다.

즉, day10은 "JavaFX 화면을 만든 날"이 아니라 "JavaFX로 실제 앱을 설계하기 시작한 날"이었다. 이후의 더 복잡한 프로젝트도 결국 여기서 익힌 파일 분리, 컨트롤러 분리, 모델 분리, 화면 분리 감각 위에서 커지게 된다.
