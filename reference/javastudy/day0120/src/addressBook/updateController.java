package addressBook;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class updateController {

    @FXML TextField tfNum;
    @FXML TextField tfName;
    @FXML TextField tfPhoneNum;
    @FXML TextField tfGender;

    public void onActionSearchBtn(ActionEvent e) throws IOException {
        FileManager fm = new FileManager(rootController.FILE_NAME, false);

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
    }

    public void onActionUpdateBtn(ActionEvent e) throws IOException {
        FileManager fm = new FileManager(rootController.FILE_NAME, false);
        Model m = new Model();
        m.setIdx(Integer.parseInt(tfNum.getText()));
        m.setName(tfName.getText());
        m.setPhoneNum(tfPhoneNum.getText());
        m.setGender(tfGender.getText().equals("남자") ? true : false);

        fm.updateData(Integer.parseInt(tfNum.getText()), m);
        rootController.updateStage.close();
    }

    public void onActionCancelBtn(ActionEvent e) {
        rootController.updateStage.close();
    }
}
