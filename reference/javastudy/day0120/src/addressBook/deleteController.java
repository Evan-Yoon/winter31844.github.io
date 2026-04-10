package addressBook;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class deleteController {

    @FXML TextField tfView;
    @FXML TextField tfName;
    @FXML TextField tfPhoneNum;
    @FXML TextField tfGender;

    public void onActionSearchBtn(ActionEvent e) throws IOException {
        FileManager fm = new FileManager(rootController.FILE_NAME, false);

        String data = fm.getAllData();
        String num = tfView.getText();

        String[] subData = data.split("\\n");
        for(int i=0;i<subData.length;i++) {
            String[] subSubData = subData[i].split("\\t");
            if(num.equals(subSubData[0])){
                tfName.setText(subSubData[1]);
                tfPhoneNum.setText(subSubData[2]);
                tfGender.setText(subSubData[3]);
            }
        }
    }

    public void onActionDeleteBtn(ActionEvent e) throws IOException {
        FileManager fm = new FileManager(rootController.FILE_NAME, false);
        fm.delData(Integer.parseInt(tfView.getText()));
        rootController.deleteStage.close();
    }

    public void onActionCancelBtn(ActionEvent e) {
        rootController.deleteStage.close();
    }
}
