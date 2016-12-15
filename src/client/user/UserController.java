package client.user;

import client.Main;
import client.ServerAPI;
import client.SomeException;
import client.query.QueryController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.function.BooleanSupplier;


public class UserController implements Initializable{
    //表示当前是否在发送单词卡的过程
    public static boolean isSharing = false;

    //发送的用户集合
    public static ArrayList<String> sendToUsers = new ArrayList<>();

    // 定义存放所有用户的数组
    private static ArrayList<UserObject> users = new ArrayList<>();

    //显示用户列表
    @FXML
    public ListView userList;

    //确定按钮
    @FXML
    public Button ensureButton;

    /**
     * stage初始化
     * @param location URL路径
     * @param resources 资源
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        users.clear();
        // 加载所有用户
        try {
            Map<String, Boolean> res = ServerAPI.getAllUsers();
            for(Map.Entry<String, Boolean> entry: res.entrySet()){
                users.add(new UserObject(entry.getKey(), entry.getValue()));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        Collections.sort(users);
        userList.setItems(FXCollections.observableList(users));
        userList.setCellFactory(new Callback<ListView<UserObject>, ListCell<UserObject>>() {
            @Override
            public ListCell call(ListView param) {
                return new UserListCell();
            }
        });

        if(!isSharing){
            ensureButton.setVisible(false);
        }
        else{
            ensureButton.setVisible(true);
        }
    }


    /**
     * 返回按钮点击事件
     * @param mouseEvent
     */
    public void backMouseAction(MouseEvent mouseEvent) {
        Main.stageController.setStage("queryView", "userView");
    }


    /**
     * 确定按钮点击事件
     * @param mouseEvent
     */
    public void ensureMouseAction(MouseEvent mouseEvent) {
        Main.threadPool.execute(() -> {
            String word = QueryController.getCurWord();
            try {
                ServerAPI.shareWordCard(new WordCard(word, new File(word + ".png"), Main.userName, sendToUsers));
            }catch (SomeException e) {
                //e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
        Main.stageController.setStage("queryView", "userView");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("分享成功!");
        alert.showAndWait();
    }
}
