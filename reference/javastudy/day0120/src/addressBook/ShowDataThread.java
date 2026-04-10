package addressBook;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ShowDataThread extends Thread{

    @FXML
    TextArea taText;

    public ShowDataThread(TextArea taText) {
        this.taText = taText;
    }

    @Override
    public void run() {
        while (true) {
            FileManager fm = new FileManager(rootController.FILE_NAME, false);

            try {
                taText.setText(fm.getAllData());
                Thread.sleep(1000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
