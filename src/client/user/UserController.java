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
import java.util.*;
import java.util.function.BooleanSupplier;


public class UserController implements Initializable{

    // 定义存放所有用户的数组
    private static ArrayList<UserObject> users = new ArrayList<>();

    //显示用户列表
    @FXML
    public ListView userList;

    /**
     * stage初始化
     * @param location URL路径
     * @param resources 资源
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 加载所有用户
        try {
            Map<String, Boolean> res = getAllUsers();
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

    }

    /**
     * TODO:从数据库获取用户在线信息
     * @return HaspMap中每个元素代表一个用户，String为用户名，Boolean表示用户是否在线，在线为true，否则为false
     */
    private HashMap<String, Boolean> getAllUsers() {
        return new HashMap<String, Boolean>();
    }

    /**
     * 返回按钮点击事件
     * @param mouseEvent
     */
    public void backMouseAction(MouseEvent mouseEvent) {
        Main.stageController.setStage("queryView", "userView");
    }


}
