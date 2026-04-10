package addressBook;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class createController {

    @FXML
    TextField tfName;

    @FXML
    TextField tfPhoneNum;

    @FXML
    TextField tfGender;

    @FXML
    public void onActionSaveBtn(ActionEvent e) throws IOException {
        FileManager fm = new FileManager(rootController.FILE_NAME, true);

        Model m = new Model();

        m.setName(tfName.getText());
        m.setPhoneNum(tfPhoneNum.getText());
        boolean g = tfGender.getText().equals("남") ? true : false;
        m.setGender(g);
        fm.dataCreate(m.toString() + "\n");
        // taText.setText(fm.getAllData());
        rootController.createStage.close();
        // Stage stage = (Stage) tfName.getScene().getWindow(); //tfName을 통해 Stage 얻기
        // stage.close();
    }
    public void onActionCancelBtn(ActionEvent e) {
        rootController.createStage.close();
    }
}
