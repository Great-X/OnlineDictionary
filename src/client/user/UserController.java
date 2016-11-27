package client.user;

import client.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.TreeMap;


public class UserController implements Initializable{

    // 定义存放所有用户的数组
    public static ArrayList<UserObject> users = new ArrayList<UserObject>();

    //显示用户列表
    @FXML
    public ListView userList;

    /**
     * stage初始化
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 加载所有用户
        // TODO:从数据库获取用户在线信息
        users.add(new UserObject("Clay", true));
        users.add(new UserObject("Bob", false));
        users.add(new UserObject("Alice", true));

        /**
         * 初始化用户列表
         */
        Collections.sort(users);
        userList.setItems(FXCollections.observableList(users));
        userList.setCellFactory(new Callback<ListView<UserObject>, ListCell<UserObject>>() {
            @Override
            public ListCell call(ListView param) {
                return new UserListCell();
            }
        });

    }

    /**
     * 返回按钮点击事件
     * @param mouseEvent
     */
    public void backMouseAction(MouseEvent mouseEvent) {
        Main.stageController.setStage("queryView", "userView");
    }


}
