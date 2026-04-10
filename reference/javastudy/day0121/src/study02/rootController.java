package study02;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class rootController {

    @FXML Label lblData;
    @FXML Label lblPg;

    FileManager loadFm = new FileManager("src\\study02\\data.txt", false);
    FileManager saveFm = new FileManager("src\\study02\\labeled_data.txt", true);
    int size = 0;
    String[] preData;
    int count = 0;

    @FXML
    public void initialize() throws IOException {
        String str = loadFm.getAllData();
        String[] subStr = str.split("\\n");

        // ^정제된 데이터의 개수 파악
        for(int i=0;i<subStr.length;i++){
            String[] subSubStr = subStr[i].split("]");
            try {
                if(subSubStr[1] != null)
                    size++;
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }

        // ^정제된 데이터를 preData 배열에 저장
        preData = new String[size];
        int cnt = 0;
        for(int i=0;i<subStr.length;i++){
            String[] subSubStr = subStr[i].split("]");
            try {
                preData[cnt++] = subSubStr[1].trim();
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        lblData.setText(preData[0]);
        lblPg.setText("1" + "/" + size);
    }

    public void onActionLikeBtn(ActionEvent e) throws IOException {
        if(count >= size) exit();
        String t = lblData.getText();
        String str = t + ", 1\n";
        saveFm.dataCreate(str);

        lblData.setText(preData[++count]);
        lblPg.setText((count+1) + "/" + size);
    }

        public void onActionNeutralBtn(ActionEvent e) throws IOException {
        if(count >= size) exit();
        String t = lblData.getText();
        String str = t + ", 1\n";
        saveFm.dataCreate(str);

        lblData.setText(preData[++count]);
        lblPg.setText((count+1) + "/" + size);
    }

    public void onActionDislikeBtn(ActionEvent e) throws IOException {
        if(count >= size) exit();
        String t = lblData.getText();
        String str = t + ", 0\n";
        saveFm.dataCreate(str);

        lblData.setText(preData[++count]);
        lblPg.setText((count+1) + "/" + size);
    }

    private void exit() {
        // ^JavaFX 애플리케이션 전체를 종료
        Platform.exit();

        // ^stage.close()는 현재 창만 닫음
        // Stage stage = (Stage) lblData.getScene().getWindow();
        // stage.close();

        // ^System.exit(0);는 JVM 강제 종료
    }
}
