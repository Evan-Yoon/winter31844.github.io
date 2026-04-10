package study01;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class rootController {

    @FXML
    ImageView ivAnimal;

    public void onActionPandaBtn(ActionEvent e) {

        Image img = new Image(getClass().getResourceAsStream("/image/panda.jpg"));


        ivAnimal.setImage(img);

    }

        public void onActionTigerBtn(ActionEvent e) {

        Image img = new Image(getClass().getResourceAsStream("/image/tiger.jpg"));


        ivAnimal.setImage(img);

    }

        public void onActionBearBtn(ActionEvent e) {

        Image img = new Image(getClass().getResourceAsStream("/image/bear.jpg"));


        ivAnimal.setImage(img);

    }
}
