package client.login;

import client.Main;
import client.ServerAPI;
import client.SomeException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    // 提示区域
    @FXML
    public Label hintLabel;

    /**
     * stage初始化
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    /**
     * 登录按钮点击事件
     */
    @FXML
    public void loginButtonAction(MouseEvent event){
        hintLabel.setText("");
        String username = usernameField.getText();
        String password = passwordField.getText();
        try {
            if (ServerAPI.checkPassword(username, password)) {
                Main.isOnline = true;
                Main.userName = username;
                ServerAPI.userOnline(username);
                Main.stageController.setStage("queryView", "loginView");
            } else {
                hintLabel.setText("用户名/密码输入错误！");
            }
        }catch (SomeException e) {
            //e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    /*
    * 注册按钮点击事件
    * */
    public void registerButtonAction(MouseEvent mouseEvent) {
        Main.stageController.setStage("registerView", "loginView");
    }
}
