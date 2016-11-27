package client.message;

import client.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MsgController implements Initializable{

    /**
     * stage初始化
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * 返回按钮点击事件
     * @param actionEvent
     */
    @FXML
    public void backAction(ActionEvent actionEvent) {
        Main.stageController.setStage("queryView", "msgView");
    }
}
