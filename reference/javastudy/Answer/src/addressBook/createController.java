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

    public void onActionSaveBtn(ActionEvent e) throws IOException{
        FileManager fm = new FileManager(rootController.FILE_NAME, true);

        Model m = new Model();

        m.setName(tfName.getText());
        m.setPhoneNum(tfPhoneNum.getText());
        boolean g = tfGender.getText().equals("남자") ? true : false;
        m.setGender(g); 
        fm.dataCreate(m.toString() + "\n");

        rootController.createStage.close();       
    }

    public void onActionCancelBtn(ActionEvent e){ 
        rootController.createStage.close();
    }




}
