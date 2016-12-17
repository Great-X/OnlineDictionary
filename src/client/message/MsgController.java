package client.message;

import client.Main;
import client.ServerAPI;
import client.SomeException;
import client.user.UserListCell;
import client.user.WordCard;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        try {
            cards = ServerAPI.receiveWordCard();
        } catch (SomeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }

        msgList.setItems(FXCollections.observableArrayList(cards));
        msgList.setCellFactory(new Callback<ListView<WordCard>, ListCell<WordCard>>() {
            @Override
            public ListCell call(ListView param) {
                return new MsgListCell();
            }
        });

        msgList.getSelectionModel().selectedIndexProperty().addListener(observable -> {
            Object[] tmp = msgList.getSelectionModel().getSelectedIndices().toArray();
            if(tmp.length > 0){
                int no = (int)tmp[0];
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(cards.get(no).getWord());
                alert.setContentText("send by " + cards.get(no).getSender());
                alert.setGraphic(new ImageView(new Image(Main.baseImagePath + "word_card\\" + cards.get(no).getFile().toString())));
                alert.showAndWait();
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
