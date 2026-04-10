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

    @FXML
    TextArea taAddress;

    public static final String FILE_NAME = "src\\addressBook\\data.txt";

    

    @FXML
    public void initialize() throws IOException {
        FileManager fm = new FileManager(FILE_NAME, false);

        taAddress.setText(fm.getAllData()); 
    }

    @FXML
    private void openCreateStage() throws Exception {
        Parent create = FXMLLoader.load(getClass().getResource("create.fxml"));
        Scene scene = new Scene(create);

        createStage = new Stage();
        
        createStage.setTitle("최강주소록 - 신규입력");
        createStage.setScene(scene);        
        createStage.show();
    }   

    @FXML
    private void openUpdateStage() throws Exception {
        Parent update = FXMLLoader.load(getClass().getResource("update.fxml"));
        Scene scene = new Scene(update);

        updateStage = new Stage();
        
        updateStage.setTitle("최강주소록 - 수정");
        updateStage.setScene(scene);        
        updateStage.show();
    } 

}
