package client.message;

import client.Main;
import client.ServerAPI;
import client.user.UserListCell;
import client.user.WordCard;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MsgController implements Initializable{
    //接受的单词卡集合
    private List<WordCard> cards = new ArrayList<>();

    //单词卡显示列表
    @FXML
    public JFXListView msgList;

    /**
     * stage初始化
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //接受单词卡
        //cards = ServerAPI.receiveWordCard();
        cards.add(new WordCard("hello", new File("hello.png"), "haha", null));
        cards.add(new WordCard("hello2", new File("hello.png"), "haha2", null));

        msgList.setItems(FXCollections.observableArrayList(cards));
        msgList.setCellFactory(new Callback<ListView<WordCard>, ListCell<WordCard>>() {
            @Override
            public ListCell call(ListView param) {
                return new MsgListCell();
            }
        });
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
