package client.register;

import client.Main;
import client.ServerAPI;
import client.SomeException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    @FXML
    public ImageView userNameHintImage;

    //密码输入提示
    @FXML
    public Label passwordHintLabel;
    @FXML
    public ImageView passwordHintImage;

    //重复输入密码提示
    @FXML
    public Label repeatPasswordHintLabel;
    @FXML
    public ImageView repeatPasswordHintImage;

    private final String correctImagePath = Main.baseImagePath + "ui\\correct.png";
    private final String errorImagePath = Main.baseImagePath + "ui\\error.png";

    /**
     * stage初始化
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userNameField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)
                    register();
            }
        });
        passwordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)
                    register();
            }
        });
        repeatPasswordField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)
                    register();
            }
        });
    }

    /**
     * 注册确定按钮点击事件
     * @param mouseEvent
     */
    @FXML
    public void registerEnsureMouseAction(MouseEvent mouseEvent) {
        register();
    }

    /**
     * 注册
     */
    private void register(){
        userNameHintLabel.setText("");
        passwordHintLabel.setText("");
        repeatPasswordHintLabel.setText("");
        userNameHintImage.setImage(null);
        passwordHintImage.setImage(null);
        repeatPasswordHintImage.setImage(null);

        // 判断空值
        String username = userNameField.getText();
        if (username.length() == 0){
            userNameHintLabel.setText("用户名不能为空！");
            userNameHintImage.setImage(new Image(errorImagePath));
            return;
        }
        String password = passwordField.getText();
        if(password.length() == 0){
            passwordHintLabel.setText("密码不能为空！");
            passwordHintImage.setImage(new Image(errorImagePath));
            return;
        }
        String repeatPassword = repeatPasswordField.getText();
        if(repeatPassword.length() == 0){
            repeatPasswordHintLabel.setText("请再次确认密码！");
            repeatPasswordHintImage.setImage(new Image(errorImagePath));
            return;
        }

        // 判断用户申请的用户名是否已经存在
        try {
            if(ServerAPI.userNameExist(username)){
                userNameHintLabel.setText("该用户名已存在！");
                userNameHintImage.setImage(new Image(errorImagePath));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        // 判断两次输入的密码是否相同
        if(!password.equals(repeatPassword)){
            repeatPasswordHintLabel.setText("密码确认错误！");
            repeatPasswordHintImage.setImage(new Image(errorImagePath));
            return;
        }

        userNameHintImage.setImage(new Image(correctImagePath));
        passwordHintImage.setImage(new Image(correctImagePath));
        repeatPasswordHintImage.setImage(new Image(correctImagePath));

        //保存用户到数据库
        try {
            ServerAPI.saveUserData(username, password);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        // 注册成功，将用户数据写入数据库，切换到登录界面
        Main.stageController.setStage("loginView", "registerView");
    }

    /**
     * 返回按钮点击事件
     * @param actionEvent
     */
    public void backAction(ActionEvent actionEvent) {
        Main.stageController.setStage("loginView", "registerView");
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
