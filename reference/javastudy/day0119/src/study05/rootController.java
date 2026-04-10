package study05;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class rootController {

    @FXML
    TextField tfX;

    @FXML
    TextField tfY;

    @FXML
    Label lblResult;

    public void onActionBtn(ActionEvent e) {
        int x = Integer.parseInt(tfX.getText());
        int y = Integer.parseInt(tfY.getText());
        int result = x + y;
        lblResult.setText(String.valueOf(result));
    }
}
