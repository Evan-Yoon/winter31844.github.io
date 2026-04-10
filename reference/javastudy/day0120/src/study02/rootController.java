package study02;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class rootController {

@FXML
TextArea taContent;

@FXML
private void initialize() throws IOException {
    FileManager fm = new FileManager("src/study02/memo.txt", false);

    taContent.setText(fm.getAllData());
}

public void onActionClearBtn(ActionEvent e) {
    taContent.setText("");
}

public void onActionSaveBtn(ActionEvent e) throws IOException {
    FileManager fm = new FileManager("src/study02/memo.txt", false);

    fm.dataCreate(taContent.getText());
}

}
