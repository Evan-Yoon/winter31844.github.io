package study06;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import java.net.URL;
import java.util.ResourceBundle;

import com.gluonhq.charm.glisten.control.Icon; // Gluon 라이브러리 필요
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class rootController implements Initializable {

    @FXML TextField tfResult; // 결과창

    // FXML 파일의 fx:id와 이름을 정확히 일치시켜야 합니다.
    @FXML Button b1;
    @FXML Button b2;
    @FXML Button b3;
    @FXML Button b4;
    @FXML Button b5;
    @FXML Button b6;
    @FXML Button b7;
    @FXML Button b8;
    @FXML Button b9;
    @FXML Button b0;

    @FXML Button bPlus;
    @FXML Button bMinus;
    @FXML Button bMultiply;
    @FXML Button bDivide;
    @FXML Button bDot;
    @FXML Button bClear;
    @FXML Button bCalculate;

    // FXML에서 Backspace가 Icon으로 되어 있어서 Icon으로 선언
    @FXML Icon bBackspace;

    // 초기화 메서드: 화면이 로드될 때 버튼들에 기능을 부여합니다.
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // 1. 숫자 및 연산자 버튼 동작 설정 (람다식 사용)
        // 버튼을 누르면 해당 버튼의 글자를 가져와서 결과창에 이어 붙입니다.
        b1.setOnAction(e -> tfResult.appendText("1"));
        b2.setOnAction(e -> tfResult.appendText("2"));
        b3.setOnAction(e -> tfResult.appendText("3"));
        b4.setOnAction(e -> tfResult.appendText("4"));
        b5.setOnAction(e -> tfResult.appendText("5"));
        b6.setOnAction(e -> tfResult.appendText("6"));
        b7.setOnAction(e -> tfResult.appendText("7"));
        b8.setOnAction(e -> tfResult.appendText("8"));
        b9.setOnAction(e -> tfResult.appendText("9"));
        b0.setOnAction(e -> tfResult.appendText("0"));

        bPlus.setOnAction(e -> tfResult.appendText("+"));
        bMinus.setOnAction(e -> tfResult.appendText("-"));
        bMultiply.setOnAction(e -> tfResult.appendText("*"));
        bDot.setOnAction(e -> tfResult.appendText("."));

        // 나누기는 FXML에 텍스트가 %로 되어있는데 자바스크립트 연산은 / 여야 하므로 별도 처리
        bDivide.setOnAction(e -> tfResult.appendText("/"));

        // 2. 초기화(C) 버튼
        bClear.setOnAction(e -> tfResult.setText(""));

        // 3. 계산(=) 버튼
        bCalculate.setOnAction(e -> calculate());

        // 4. 백스페이스(Icon) 처리
        // Icon은 setOnAction이 없으므로 마우스 클릭 이벤트를 사용합니다.
        bBackspace.setOnMouseClicked(e -> {
            String currentText = tfResult.getText();
            if (currentText.length() > 0) {
                // 마지막 글자 하나 지우기
                tfResult.setText(currentText.substring(0, currentText.length() - 1));
            }
        });
    }

        // 실제 계산 로직
        private void calculate() {
        String expressionStr = tfResult.getText().trim();
        if (expressionStr.isEmpty()) return;

        try {
            // exp4j 라이브러리를 사용하여 문자열 계산
           Expression e = new ExpressionBuilder(expressionStr).build();
            double result = e.evaluate();

            // 결과 출력 (정수면 소수점 제거)
            if (result == (long) result) {
                tfResult.setText(String.valueOf((long) result));
            } else {
                tfResult.setText(String.valueOf(result));
            }
        } catch (Exception ex) {
            // 잘못된 수식(예: 1++2) 입력 시 에러 표시
            tfResult.setText("Error");
        }
    }
}