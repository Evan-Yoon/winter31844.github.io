---
title: "Java 학습 9일차: JavaFX 생명주기, FXML, 컨트롤러, 계산기 UI"
slug: java-study-day9-javafx-basics-and-calculator-ui
date: 2026-01-19
author: Evan Yoon
category: study
subcategory: bootcamp
description: "1월 19일 Java 학습 기록. JavaFX 애플리케이션 생명주기, 코드 기반 UI와 FXML, fx:id와 @FXML 주입, 컨트롤러 이벤트 처리, exp4j를 이용한 계산기 UI 구현까지 실습 내용을 자세히 정리했다."
tags:
  - java
  - study
  - javafx
  - fxml
  - ui
  - controller
  - desktop-app
readTime: 16
series: Java Study
seriesOrder: 9
featured: false
draft: false
toc: true
---

## 오늘은 콘솔을 벗어나 화면이 있는 프로그램으로 들어간 날이었다
`reference/javastudy/day0119/src`는 이전까지의 `javaStudy` 패키지와 분위기가 완전히 다르다. `study01`부터 `study06`, 그리고 `hellofx`까지 각 폴더마다 `AppMain.java`, `FXML`, `Controller`가 등장한다. 즉, 이제는 `Scanner`와 `println()`으로 입출력하던 단계를 넘어 창, 버튼, 텍스트필드, 레이아웃을 다루는 데스크톱 UI 개발로 넘어간 것이다.

이날 학습 흐름을 크게 나누면 이렇다.

1. JavaFX 애플리케이션 생명주기를 이해한다.
2. 자바 코드만으로 간단한 UI를 구성해 본다.
3. FXML로 화면 레이아웃을 분리한다.
4. `fx:id`와 `@FXML`로 화면 요소를 컨트롤러에 연결한다.
5. 버튼 이벤트를 처리해 입력과 출력을 화면에서 주고받는다.
6. 계산기 UI를 만들어 문자열 입력, 계산, 결과 표시까지 연결한다.

즉, day9는 "자바 문법 공부"가 "실제 데스크톱 앱 개발"로 확장되기 시작한 날이었다.

## 1. JavaFX 앱은 `main()`만으로 끝나지 않는다
`study01/AppMain.java`는 JavaFX 생명주기를 보여 주는 아주 좋은 예제다.

```java
public class AppMain extends Application {
    public AppMain(){
        System.out.println(Thread.currentThread().getName()+" AppMain() 생성자 호출");
    }

    @Override
    public void init() throws Exception {
        System.out.println(Thread.currentThread().getName()+" init() 호출");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(Thread.currentThread().getName()+" start() 호출");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        System.out.println(Thread.currentThread().getName()+" stop() 호출");
    }
}
```

그리고 `main()`에서는 `launch(args)`를 호출한다.

```java
public static void main(String[] args) {
    System.out.println(Thread.currentThread().getName()+" main() 호출");
    launch(args);
}
```

### 여기서 배워야 할 핵심
JavaFX 프로그램은 단순히 `main()` 안에서 모든 걸 처리하는 구조가 아니다. 프레임워크가 정한 순서에 따라 생명주기가 진행된다.

1. `main()`
2. 생성자
3. `init()`
4. `start()`
5. `stop()`

이 흐름을 이해해야 나중에 초기화 코드를 어디에 둘지, 창을 띄우는 코드는 어디에 넣을지 헷갈리지 않는다.

## 2. `Stage`와 `Scene`: 창과 화면의 개념
JavaFX에서 자주 보이는 두 단어가 있다.

- `Stage`: 프로그램 창 자체
- `Scene`: 그 창 안에 들어가는 화면

예를 들어 `study02/AppMain.java`는 이런 구조다.

```java
Scene scene = new Scene(root);

primaryStage.setTitle("AppMain");
primaryStage.setScene(scene);
primaryStage.show();
```

즉, `root` 레이아웃으로 화면을 만들고, 그것을 `Scene`에 담고, `Scene`을 다시 `Stage`에 붙여서 보여 주는 흐름이다.

이 구조는 이후 FXML을 쓰든, 컨트롤러를 쓰든 JavaFX 전체에서 계속 반복된다.

## 3. 코드로 직접 UI 만들기
`study02/AppMain.java`는 자바 코드만으로 UI를 만든다.

```java
VBox root = new VBox();
root.setPrefWidth(350);
root.setPrefHeight(150);
root.setAlignment(Pos.CENTER);
root.setSpacing(20);

Label label = new Label();
label.setText("Hello, JavaFX");
label.setFont(new Font(50));

Button button = new Button();
button.setText("확인");
button.setOnAction(event -> Platform.exit());

root.getChildren().add(label);
root.getChildren().add(button);
```

이 예제는 매우 단순하지만 중요한 이유가 있다. FXML을 보기 전에 먼저 UI가 사실은 객체들의 트리 구조라는 걸 코드로 직접 이해하게 해 주기 때문이다.

- `VBox` 안에
- `Label`과 `Button`이 들어가고
- 버튼에는 이벤트가 연결된다

즉, UI도 결국 객체를 조립해서 만드는 구조라는 감각을 먼저 익히는 셈이다.

## 4. FXML: 화면 구조를 자바 코드와 분리하기
`study03`부터는 FXML이 등장한다.

`AppMain.java`:

```java
Parent root = FXMLLoader.load(getClass().getResource("root.fxml"));
Scene scene = new Scene(root);
```

`root.fxml`:

```xml
<HBox spacing="10.0" xmlns="http://javafx.com/javafx/25" xmlns:fx="http://javafx.com/fxml/1">
   <children>
      <TextField prefWidth="105.0" />
      <Button text="확인" />
   </children>
</HBox>
```

이제 화면의 구조와 스타일은 XML 쪽에 두고, 실행 코드는 Java에 둔다.

### 왜 FXML이 중요한가
코드만으로 UI를 만들 수도 있지만, 화면이 복잡해질수록 가독성이 급격히 떨어진다. FXML을 쓰면:

- 레이아웃 구조를 한눈에 보기 쉽고
- UI와 로직을 분리하기 쉽고
- Scene Builder 같은 도구와도 잘 맞는다

즉, FXML은 JavaFX에서 UI를 더 선언적으로 다루는 방법이다.

## 5. 단순 레이아웃에서 폼 UI로 확장하기
`study04/root.fxml`는 로그인 폼 비슷한 구조다.

```xml
<AnchorPane prefHeight="129.0" prefWidth="254.0">
   <children>
      <Label text="아이디" />
      <Label text="패스워드" />
      <TextField ... />
      <TextField ... />
      <Button text="로그인" />
      <Button text="취소" />
   </children>
</AnchorPane>
```

이 단계의 의미는 기능 구현보다 "화면 배치 감각"에 있다. 단순한 `HBox`, `VBox`를 넘어서 위치 기반 폼을 만들고, 여러 컨트롤이 한 화면에 모이는 구조를 익히는 것이다.

즉, JavaFX는 단순 출력 도구가 아니라 실제 업무 화면 같은 폼 UI도 만들 수 있다는 걸 보기 시작한 단계다.

## 6. `fx:id`와 `@FXML`: 화면 요소를 자바 코드와 연결하기 <!-- short: 6. `fx -->
`hellofx`와 `study05`는 이 연결 구조를 가장 잘 보여 준다.

`hellofx.fxml`에는 이런 부분이 있다.

```xml
<StackPane fx:controller="hellofx.Controller">
    <children>
        <Label fx:id="label" text="Label" />
    </children>
</StackPane>
```

컨트롤러는 이렇게 필드를 가진다.

```java
public class Controller {
    @FXML
    private Label label;

    public void initialize() {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
    }
}
```

여기서 꼭 이해해야 할 것은 세 가지다.

- `fx:controller`: 이 FXML을 누가 제어할지 지정
- `fx:id`: FXML 안의 UI 요소에 이름 부여
- `@FXML`: 그 요소를 자바 필드와 연결

즉, FXML은 단순한 그림이 아니라 컨트롤러와 연결되는 살아 있는 화면 정의다.

## 7. `initialize()`: 화면이 로드된 뒤 초기 설정을 넣는 자리 <!-- short: 7. `initialize()` -->
`hellofx/Controller.java`의 `initialize()`는 자주 쓰이는 패턴이다.

```java
public void initialize() {
    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");
    label.setText(...);
}
```

컨트롤러가 만들어지고 FXML 필드 주입이 끝난 뒤 초기 UI 상태를 설정할 때 이 메서드가 유용하다.

예를 들어:

- 기본 텍스트 설정
- 리스트 초기 데이터 로드
- 이벤트 핸들러 추가
- 초기 포커스 설정

같은 작업이 여기에 들어갈 수 있다.

## 8. 버튼 이벤트 처리: 화면 입력을 로직으로 연결하기
`study05`는 day9에서 중요한 전환점이다. 이제 단순 화면 출력이 아니라 실제 입력값 계산이 들어간다.

`root.fxml`:

```xml
<TextField fx:id="tfX" />
<TextField fx:id="tfY" />
<Button onAction="#onActionBtn" text="=" />
<Label fx:id="lblResult" text="0" />
```

컨트롤러:

```java
@FXML TextField tfX;
@FXML TextField tfY;
@FXML Label lblResult;

public void onActionBtn(ActionEvent e) {
    int x = Integer.parseInt(tfX.getText());
    int y = Integer.parseInt(tfY.getText());
    int result = x + y;
    lblResult.setText(String.valueOf(result));
}
```

이 예제에는 이미 중요한 흐름이 다 들어 있다.

1. 화면에서 입력을 읽는다.
2. 문자열을 숫자로 변환한다.
3. 계산한다.
4. 결과를 다시 화면에 반영한다.

즉, 콘솔에서 하던 `Scanner -> parseInt -> 계산 -> println` 흐름이 이제 JavaFX UI 안으로 옮겨온 것이다.

## 9. 계산기 UI: 여러 버튼과 이벤트를 한 화면에 묶기 <!-- short: 9. 계산기 UI -->
`study06/root.fxml`는 계산기 화면을 꽤 본격적으로 구성한다.

- 숫자 버튼 `0~9`
- 연산 버튼 `+`, `-`, `*`, `%`
- `C`, `CE`, `Backspace`, `=`
- 결과 표시용 `TextField`

레이아웃은 `GridPane` 중심으로 짜여 있다.

```xml
<GridPane ...>
   <children>
      <Button fx:id="b7" text="7" />
      <Button fx:id="bPlus" text="+" />
      <Button fx:id="bCalculate" text="=" />
      <Icon fx:id="bBackspace" content="BACKSPACE" />
   </children>
</GridPane>
```

이 단계는 단순한 예제 UI를 넘어, 실제 앱처럼 여러 컨트롤을 한 화면에 정리하는 감각을 배우는 구간이다.

## 10. 컨트롤러에서 여러 버튼을 초기화하는 패턴
`study06/rootController.java`는 `Initializable`을 구현하고 있다.

```java
public class rootController implements Initializable {
    @FXML TextField tfResult;
    @FXML Button b1;
    @FXML Button b2;
    ...

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        b1.setOnAction(e -> tfResult.appendText("1"));
        b2.setOnAction(e -> tfResult.appendText("2"));
        ...
        bPlus.setOnAction(e -> tfResult.appendText("+"));
        bMinus.setOnAction(e -> tfResult.appendText("-"));
    }
}
```

이 코드는 JavaFX에서 자주 보는 이벤트 초기화 패턴이다. 화면이 준비되면 각 버튼에 동작을 붙인다. 숫자는 숫자를 붙이고, 연산자는 연산 기호를 붙인다.

### 왜 `appendText()`가 유용할까
계산기는 현재 입력 문자열 뒤에 계속 값을 붙여야 한다. 그래서 `setText()`보다 `appendText()`가 자연스럽다. 이런 작은 선택이 UI 로직에서 중요하다.

## 11. 계산 로직: 문자열 수식을 실제 계산으로 바꾸기
`study06/rootController.java`의 핵심은 `calculate()`다.

```java
private void calculate() {
    String expressionStr = tfResult.getText().trim();
    if (expressionStr.isEmpty()) return;

    try {
        Expression e = new ExpressionBuilder(expressionStr).build();
        double result = e.evaluate();

        if (result == (long) result) {
            tfResult.setText(String.valueOf((long) result));
        } else {
            tfResult.setText(String.valueOf(result));
        }
    } catch (Exception ex) {
        tfResult.setText("Error");
    }
}
```

이 예제는 UI 실습이면서 동시에 꽤 좋은 애플리케이션 구조 예제다.

- 화면 입력을 문자열로 수집하고
- 외부 라이브러리(`exp4j`)로 계산하고
- 결과를 포맷해서 다시 화면에 반영하고
- 잘못된 수식은 `Error`로 처리한다

즉, 단순 버튼 나열이 아니라 입력-처리-출력 전체 흐름이 하나의 UI 안에 들어 있다.

## 12. 외부 라이브러리를 UI에 연결하는 감각
이번 날은 JavaFX만 배운 게 아니다. 계산기에서 `exp4j` 라이브러리를 사용한다.

```java
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
```

이 의미는 꽤 크다. 이제 자바 앱은 표준 라이브러리만 쓰는 게 아니라, 필요한 기능을 외부 라이브러리와 연결해 더 빠르게 만들 수 있다는 걸 체감하기 시작한 것이다.

즉, "UI를 만든다"와 "기능 라이브러리를 붙인다"가 함께 가는 실전 감각이 조금씩 생긴다.

## 13. 일반 버튼과 `Icon`의 이벤트 방식이 다를 수 있다
계산기 컨트롤러에서 흥미로운 부분 하나는 `Backspace` 처리다.

```java
@FXML Icon bBackspace;

bBackspace.setOnMouseClicked(e -> {
    String currentText = tfResult.getText();
    if (currentText.length() > 0) {
        tfResult.setText(currentText.substring(0, currentText.length() - 1));
    }
});
```

일반 `Button`은 `setOnAction()`을 썼지만, `Icon`은 `setOnMouseClicked()`를 사용한다. 즉, UI 컴포넌트 종류에 따라 이벤트 연결 방식이 달라질 수 있다는 점도 배운 셈이다.

이런 차이는 나중에 메뉴, 체크박스, 리스트뷰, 커스텀 노드 등을 다룰 때도 계속 중요하다.

## 14. 코드 기반 UI와 FXML 기반 UI를 둘 다 경험한 게 중요했다
day9의 좋은 점은 한 가지 방식만 본 게 아니라는 점이다.

- `study02`: 코드로 직접 UI 구성
- `study03~06`, `hellofx`: FXML 기반 UI 구성

이 두 방식을 모두 경험해야 왜 FXML이 편한지, 또 어떤 경우에는 코드 기반이 더 직관적인지 비교할 수 있다.

입문 단계에서는 단순히 "FXML이 좋다"보다 "UI도 결국 객체 트리고, FXML은 그것을 분리해서 더 읽기 좋게 만드는 방법"이라고 이해하는 편이 더 단단하다.

## 15. 오늘 실습을 통해 얻은 핵심 감각
1월 19일 실습에서 꼭 남겨야 할 핵심은 아래와 같다.

- JavaFX 앱은 `main()`만이 아니라 정해진 생명주기에 따라 동작한다.
- `Stage`는 창, `Scene`은 그 안의 화면이다.
- UI는 코드로도 만들 수 있고 FXML로도 만들 수 있다.
- `fx:id`와 `@FXML`은 화면 요소를 컨트롤러와 연결하는 핵심이다.
- 이벤트 처리로 화면 입력과 로직을 연결할 수 있다.
- 컨트롤러는 화면 상태를 읽고 갱신하는 중심 역할을 한다.
- 외부 라이브러리를 붙이면 UI 앱 기능을 더 빠르게 확장할 수 있다.
- 계산기 같은 작은 앱도 이미 입력, 처리, 출력 구조를 모두 가진다.

이 감각은 이후 JavaFX 주소록, 메모장, 데이터 라벨링 툴 같은 더 큰 화면 앱으로 자연스럽게 이어진다.

## 오늘의 복습 질문
이 글만 읽고 아래 질문에 답할 수 있으면 day9 내용은 잘 정리된 것이다.

<details>
<summary>1. JavaFX의 <code>main()</code>, <code>init()</code>, <code>start()</code>, <code>stop()</code>은 각각 언제 실행될까?</summary>

`main()`은 프로그램 시작점이고, `init()`은 UI가 뜨기 전 초기화, `start()`는 실제 화면 구성을 시작할 때, `stop()`은 앱 종료 직전에 호출된다.

</details>

<details>
<summary>2. <code>Stage</code>와 <code>Scene</code>은 어떻게 다를까?</summary>

`Stage`는 창 자체이고, `Scene`은 그 창 안에 들어가는 화면 내용이다.

</details>

<details>
<summary>3. 코드 기반 UI와 FXML 기반 UI는 각각 어떤 장점이 있을까?</summary>

코드 기반 UI는 흐름을 한 파일에서 직접 제어하기 쉽고, FXML 기반 UI는 화면 구조와 로직을 분리해 더 읽기 좋고 관리하기 쉽다.

</details>

<details>
<summary>4. <code>fx:id</code>와 <code>@FXML</code>은 왜 같이 써야 할까?</summary>

FXML 화면 요소와 컨트롤러 필드를 연결하기 위해서다. `fx:id`가 화면 쪽 이름이고, `@FXML`은 컨트롤러에서 그 요소를 주입받게 한다.

</details>

<details>
<summary>5. <code>onAction=&quot;#onActionBtn&quot;</code>는 어떤 의미일까?</summary>

버튼 등의 액션 이벤트가 발생했을 때 컨트롤러의 `onActionBtn` 메서드를 호출하라는 뜻이다.

</details>

<details>
<summary>6. 컨트롤러의 <code>initialize()</code>는 어떤 용도로 쓰기 좋을까?</summary>

화면이 로드된 직후 필요한 초기 설정, 값 채우기, 이벤트 바인딩 같은 준비 작업에 쓰기 좋다.

</details>

<details>
<summary>7. 계산기 예제에서 <code>appendText()</code>를 쓰는 이유는 무엇일까?</summary>

기존 입력 뒤에 새 숫자나 기호를 이어 붙여 표시하기 위해서다. 계산기 입력 흐름에 자연스럽다.

</details>

<details>
<summary>8. <code>exp4j</code> 같은 외부 라이브러리를 붙이는 것이 왜 유용할까?</summary>

복잡한 수식 파싱과 계산 로직을 직접 다 짜지 않아도 되기 때문이다. 개발 속도와 안정성을 높여 준다.

</details>

## 마무리
1월 19일은 자바 학습이 콘솔 중심 사고에서 화면 중심 사고로 넘어간 날이었다. 입력과 출력이 더 이상 터미널 문자열이 아니라 버튼, 텍스트필드, 레이블, 창으로 바뀌었고, 프로그램 구조도 프레임워크 생명주기와 컨트롤러 중심으로 재편되기 시작했기 때문이다.

특히 계산기 예제는 단순히 예쁜 화면 하나를 만든 것이 아니라, 화면 구성, 이벤트 처리, 문자열 입력, 계산 로직, 결과 반영까지 하나의 데스크톱 앱 흐름을 처음 완성해 본 실습이었다. day9는 "JavaFX 문법 입문"이 아니라 "자바로 진짜 화면 앱을 만들기 시작한 첫날"이었다.
