package addressBook;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class updateController {

    @FXML TextField tfNum;
    @FXML TextField tfName;
    @FXML TextField tfPhoneNum;
    @FXML TextField tfGender;



    public void onActionCancelBtn(ActionEvent e){
        rootController.updateStage.close();
    }
}
