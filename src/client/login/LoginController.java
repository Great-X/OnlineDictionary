package client.login;

import client.Main;
import client.ServerAPI;
import client.SomeException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable{

    // 用户名文本域
    @FXML
    public TextField usernameField;

    // 密码域
    @FXML
    public PasswordField passwordField;

    /**
     * stage初始化
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usernameField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)
                    login();
            }
        });

        passwordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)
                    login();
            }
        });
    }


    /**
     * 登录按钮点击事件
     */
    @FXML
    public void loginButtonAction(MouseEvent event){
        login();
    }

    /**
     * 登录
     */
    private void login(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username.length() == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("error");
            alert.setContentText("请输入用户名！");
            alert.showAndWait();
        }
        else if (password.length() == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("error");
            alert.setContentText("请输入密码！");
            alert.showAndWait();
        }
        else {
            try {
                if (ServerAPI.checkPassword(username, password)) {
                    Main.isOnline = true;
                    Main.userName = username;
                    ServerAPI.userOnline(username);
                    Main.stageController.setStage("queryView", "loginView");
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("error");
                    alert.setContentText("用户名/密码输入错误！");
                    alert.showAndWait();
                }
            } catch (SomeException e) {
                //e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    /*
    * 注册按钮点击事件
    * */
    public void registerButtonAction(MouseEvent mouseEvent) {
        Main.stageController.setStage("registerView", "loginView");
    }

    /**
     * 返回
     * @param actionEvent
     */
    public void backAction(ActionEvent actionEvent) {
        Main.stageController.setStage("queryView", "loginView");
    }

    /**
     * 关闭界面
     * @param actionEvent
     */
    public void closeAction(ActionEvent actionEvent) {
        if(Main.isOnline) {
            try {
                ServerAPI.userOffline(Main.userName);
            } catch (SomeException e) {
                e.getMessage();
            }
        }
        System.exit(0);
    }
}
