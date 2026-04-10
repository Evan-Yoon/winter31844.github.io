package addressBook;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class rootController {

    public static Stage createStage;
    public static Stage updateStage;
    public static Stage deleteStage;

    @FXML private TextArea taText;

    public static final String FILE_NAME = "src\\addressBook\\Data.txt";

    @FXML
    public void initialize() throws IOException {

        Thread thread = new ShowDataThread(taText);
        thread.setDaemon(true);
        thread.start();

        // FileManager fm = new FileManager(FILE_NAME, false);
        // taText.setText(fm.getAllData());
    }

    @FXML
    private void openCreateStage() throws Exception {
        Parent create = FXMLLoader.load(getClass().getResource("create.fxml"));
        Scene scene = new Scene(create);

        createStage = new Stage();

        createStage.setScene(scene);
        createStage.setTitle("최강 주소록 - 신규 입력");
        createStage.show();
    }

    @FXML
    private void openUpdateStage() throws Exception {
        Parent update = FXMLLoader.load(getClass().getResource("update.fxml"));
        Scene scene = new Scene(update);

        updateStage = new Stage();

        updateStage.setScene(scene);
        updateStage.setTitle("최강 주소록 - 수정");
        updateStage.show();
    }

    @FXML
    private void openDeleteStage() throws Exception {
        Parent delete = FXMLLoader.load(getClass().getResource("delete.fxml"));
        Scene scene = new Scene(delete);

        deleteStage = new Stage();

        deleteStage.setScene(scene);
        deleteStage.setTitle("최강 주소록 - 삭제");
        deleteStage.show();
    }
}
