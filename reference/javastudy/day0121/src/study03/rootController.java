package study03;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class rootController {

    @FXML TextField tfSentence;
    @FXML Label lblResult;

    @FXML
    public void onActionAnalyzeBtn() throws IOException, InterruptedException {
        String inputText = tfSentence.getText();

        if (inputText == null || inputText.trim().isEmpty()) {
            lblResult.setText("문장을 입력해주세요.");
            return;
        }

        // 프로세스 빌더 설정
        ProcessBuilder pb = new ProcessBuilder("python", "src/study03/emotionModelRunner.py");
        pb.redirectErrorStream(true);

        Process process = pb.start();

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream(), "UTF-8"));
        bw.write(inputText);
        bw.newLine();
        bw.flush();
        bw.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));

        String line;
        String result = "분석 실패";

        while ((line = br.readLine()) != null) {
            if (line.startsWith("DEBUG:")) continue;

            String trimmedLine = line.trim();
            if ("1".equals(trimmedLine)) {
                result = "긍정";
                break;
            } else if ("0".equals(trimmedLine)) {
                result = "부정";
                break;
            }
        }

        lblResult.setText(result);

        process.waitFor();
    }
}