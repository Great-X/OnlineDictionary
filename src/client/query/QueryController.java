package client.query;

import client.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryController implements Initializable{
    //搜索结果
    private static HashMap<String, String> results;

    //点赞数
    private static HashMap<String, Integer> favours = new HashMap<String, Integer>();

    // 单词输入域
    @FXML
    public TextField inputTextField;

    // 搜索结果显示区域
    @FXML
    public TextArea resultTextArea;

    //百度复选框
    @FXML
    public CheckBox baiduCheckBox;

    //有道复选框
    @FXML
    public CheckBox youdaoCheckBox;

    //必应复选框
    @FXML
    public CheckBox biyingCheckBox;

    //点赞按钮
    @FXML
    public Button favourButton;

    //输入出错时提示
    @FXML
    public Label hintLabel;

    //显示翻译的工具
    @FXML
    public Label toolLabel;

    /**
     * stage初始化
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        results = null;
        favours.put("baidu", 0);
        favours.put("youdao", 0);
        favours.put("biying", 0);
        baiduCheckBox.setSelected(true);
        youdaoCheckBox.setSelected(true);
        biyingCheckBox.setSelected(true);
        resultTextArea.setWrapText(true);

        // 设置三个复选框的监听器
        baiduCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                showResult();
            }
        });
        youdaoCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                showResult();
            }
        });
        biyingCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                showResult();
            }
        });
    }


    /**
     * 好友按钮点击事件
     * @param mouseEvent
     */
    @FXML
    public void userButtonAction(MouseEvent mouseEvent) {
        Main.stageController.setStage("userView", "queryView");
    }


    /**
     * 注销按钮点击事件
     * @param mouseEvent
     */
    @FXML
    public void logoutButtonAction(MouseEvent mouseEvent) {
        Main.stageController.setStage("loginView", "queryView");
    }


    /**
     * 搜索按钮点击事件
     * @param mouseEvent
     */
    @FXML
    public void searchMouseAction(MouseEvent mouseEvent) throws IOException {
        hintLabel.setText("");
        String word = inputTextField.getText();
        if(!isLegal(word)){
            hintLabel.setText("请输入英文单词！");
            return;
        }
        results = client.Translate.translate(word);
        //TODO:获取点赞数
        favours.replace("baidu", 1);
        favours.replace("youdao", 2);
        favours.replace("biying", 3);

        showResult();
    }


    /**
     * 用正则表达式判断输入单词是否合法
     * @param word
     * @return
     */
    private boolean isLegal(String word) {
        Pattern pattern = Pattern.compile("[a-zA-Z]+[\\s0-9a-zA-Z]*");
        Matcher matcher = pattern.matcher(word);
        return matcher.matches();
    }


    /**
     * 消息按钮点击事件
     * @param mouseEvent
     */
    @FXML
    public void messageMouseAction(MouseEvent mouseEvent) {
        Main.stageController.setStage("msgView", "queryView");
    }


    /**
     * 分享按钮点击事件
     * @param mouseEvent
     */
    @FXML
    public void shareMouseAction(MouseEvent mouseEvent) {
    }


    /**
     * 点赞按钮点击事件
     * @param mouseEvent
     */
    @FXML
    public void favourMouseAction(MouseEvent mouseEvent) {
    }


    /**
     * 根据复选框和用户点赞数量显示结果
     */
    private void showResult(){
        if(baiduCheckBox.isSelected()){
            toolLabel.setText("百度");
            resultTextArea.setText(results.get("baidu"));
        }
        else if(youdaoCheckBox.isSelected()){
            toolLabel.setText("有道");
            resultTextArea.setText(results.get("youdao"));
        }
        else if(biyingCheckBox.isSelected()){
            toolLabel.setText("必应");
            resultTextArea.setText(results.get("biying"));
        }
        else{
            toolLabel.setText("百度");
            resultTextArea.setText(results.get("baidu"));
        }
    }
}
