package client.register;

import client.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable{
    //用户名输入框
    @FXML
    public TextField userNameField;

    //密码输入框
    @FXML
    public PasswordField passwordField;

    //重复密码输入框
    @FXML
    public PasswordField repeatPasswordField;

    //用户名输入提示
    @FXML
    public Label userNameHintLabel;

    //密码输入提示
    @FXML
    public Label passwordHintLabel;

    //重复输入密码提示
    @FXML
    private Label repeatPasswordHintLabel;

    /**
     * stage初始化
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * 注册确定按钮点击事件
     * @param mouseEvent
     */
    @FXML
    public void registerEnsureMouseAction(MouseEvent mouseEvent) {
        userNameHintLabel.setText("");
        passwordHintLabel.setText("");
        repeatPasswordHintLabel.setText("");

        // 判断空值
        String username = userNameField.getText();
        if (username.length() == 0){
            userNameHintLabel.setText("用户名不能为空！");
            return;
        }
        String password = passwordField.getText();
        if(password.length() == 0){
            passwordHintLabel.setText("密码不能为空！");
            return;
        }
        String repeatPassword = repeatPasswordField.getText();
        if(repeatPassword.length() == 0){
            repeatPasswordHintLabel.setText("请再次确认密码！");
            return;
        }

        // 判断用户申请的用户名是否已经存在
        if(userNameExist(username)){
           userNameHintLabel.setText("该用户名已存在！");
        }

        // 判断两次输入的密码是否相同
        if(!password.equals(repeatPassword)){
            repeatPasswordHintLabel.setText("密码确认错误！");
            return;
        }

        // 注册成功，将用户数据写入数据库，切换到登录界面
        // TODO: 将用户数据写入数据库
        Main.stageController.setStage("loginView", "registerView");
    }


    /**
     * 检测用户申请的用户名是否已经存在
     * @param username
     * @return
     */
    private boolean userNameExist(String username){
        // TODO: 遍历数据库，看该username是否已经存在
        return false;
    }

    /**
     * 返回按钮点击事件
     * @param mouseEvent
     */
    public void backMouseAction(MouseEvent mouseEvent) {
        Main.stageController.setStage("loginView", "registerView");
    }

}
