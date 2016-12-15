package client.query;

import client.Main;
import client.ServerAPI;
import client.SomeException;
import client.user.UserController;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.peer.CheckboxPeer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryController implements Initializable{
    //当前单词
    private static String curWord = "";

    //搜索结果
    private static QueryResults results = new QueryResults();

    // 单词输入域
    @FXML
    public TextField inputTextField;

    // 搜索结果显示区域
    public TextArea[] resultTextAreas = new TextArea[3];
    @FXML
    public TextArea resultTextArea0;
    @FXML
    public TextArea resultTextArea1;
    @FXML
    public TextArea resultTextArea2;

    //复选框
    public CheckBox[] checkBoxs = new CheckBox[3];
    @FXML
    public CheckBox jinshanCheckBox;
    @FXML
    public CheckBox youdaoCheckBox;
    @FXML
    public CheckBox biyingCheckBox;

    //点赞按钮
    public Button[] favourButtons = new Button[3];
    @FXML
    public Button favourButton0;
    @FXML
    public Button favourButton1;
    @FXML
    public Button favourButton2;

    //分享按钮
    public Button[] shareButtons = new Button[3];
    @FXML
    public Button shareButton0;
    @FXML
    public Button shareButton1;
    @FXML
    public Button shareButton2;

    //输入出错时提示
    @FXML
    public Label hintLabel;

    //显示翻译的工具
    public Label[] toolLabels = new Label[3];
    @FXML
    public Label toolLabel0;
    @FXML
    public Label toolLabel1;
    @FXML
    public Label toolLabel2;

    //功能按钮
    @FXML
    public Button loginButton;
    @FXML
    public Button logoutButton;
    @FXML
    public Button userButton;
    @FXML
    public Button msgButton;


    /**
     * 返回当前单词
     * @return string
     */
    public static String getCurWord(){
        return curWord;
    }

    /**
     * stage初始化
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        checkBoxs[0] = jinshanCheckBox;
        checkBoxs[1] = youdaoCheckBox;
        checkBoxs[2] = biyingCheckBox;
        resultTextAreas[0] = resultTextArea0;
        resultTextAreas[1] = resultTextArea1;
        resultTextAreas[2] = resultTextArea2;
        toolLabels[0] = toolLabel0;
        toolLabels[1] = toolLabel1;
        toolLabels[2] = toolLabel2;
        favourButtons[0] = favourButton0;
        favourButtons[1] = favourButton1;
        favourButtons[2] = favourButton2;
        shareButtons[0] = shareButton0;
        shareButtons[1] = shareButton1;
        shareButtons[2] = shareButton2;

        toolLabel0.setText("金山");
        toolLabel1.setText("有道");
        toolLabel2.setText("必应");

        for(CheckBox checkBox: checkBoxs)
            checkBox.setSelected(true);
        for(TextArea resultTextArea: resultTextAreas)
            resultTextArea.setWrapText(true);

        // 设置三个复选框的监听器
        for(CheckBox checkBox: checkBoxs)
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> showResult());

        for(int i = 0; i < 3; i++) {
            shareButtons[i].setVisible(false);
            favourButtons[i].setVisible(false);

            int finalI = i;
            //设置分享按钮的监听器
            shareButtons[i].setOnAction(event -> {
                if(curWord.length() == 0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("单词卡不能为空!");
                    alert.setContentText("当前单词为空");
                    alert.showAndWait();
                    return;
                }
                WritableImage image = resultTextAreas[finalI].snapshot(new SnapshotParameters(), null);
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File(curWord + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                UserController.isSharing = true;
                Main.stageController.setStage("userView", "queryView");
            });

            //设置点赞按钮的监听器
            favourButtons[i].setOnAction(event -> {
                if(curWord.length() == 0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("搞不清楚你要赞什么!");
                    alert.setContentText("当前单词为空");
                    alert.showAndWait();
                    return;
                }
                try {
                    ServerAPI.favourAction(curWord, results.getResult(finalI).getTool(), Main.userName);
                } catch (SomeException e) {
                    //e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("error");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            });
        }

        //设置文本显示
        inputTextField.setText(curWord);
        showResult();

        //根据用户是否离线来隐藏一些控件
        if(!Main.isOnline){
            userButton.setVisible(false);
            msgButton.setVisible(false);
            logoutButton.setVisible(false);
            loginButton.setVisible(true);
        }
        else{
            userButton.setVisible(true);
            msgButton.setVisible(true);
            logoutButton.setVisible(true);
            loginButton.setVisible(false);
        }
    }


    /**
     * 好友按钮点击事件
     * @param mouseEvent
     */
    @FXML
    public void userButtonAction(MouseEvent mouseEvent) {
        UserController.isSharing = false;
        Main.stageController.setStage("userView", "queryView");
    }


    /**
     * 注销按钮点击事件
     * @param mouseEvent
     */
    @FXML
    public void logoutButtonAction(MouseEvent mouseEvent) {
        try {
            ServerAPI.userOffline(Main.userName);
        }catch (SomeException e){
            e.getMessage();
        }
        Main.isOnline = false;
        Main.userName = "";
        Main.stageController.setStage("loginView", "queryView");
    }


    /**
     * 搜索按钮点击事件
     * @param mouseEvent
     */
    @FXML
    public void searchMouseAction(MouseEvent mouseEvent) throws IOException {
        hintLabel.setText("");
        if(!isLegal(inputTextField.getText())){
            hintLabel.setText("请输入英文单词！");
            return;
        }
        String word = inputTextField.getText();
        try {
            results.setResultsContent(client.Translate.translate(word));
        } catch (SomeException e) {
            hintLabel.setText(e.getMessage());
            return;
        }
        curWord = word;

        //获取点赞数
        try {
            results.setResultsFavourNum(ServerAPI.getFavoursNum(curWord));

        //检查该用户之前有没有对该单词点赞
            if(Main.isOnline) {
                results.setResultsUserFavour(ServerAPI.getUserFavour(Main.userName, curWord));
            }
        } catch (SomeException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        //根据点赞数排序
        results.sort();

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
     * 根据复选框和用户点赞数量显示结果
     */
    private void showResult(){
        //初始化
        for(Label toolLabel: toolLabels)
            toolLabel.setText("");
        for(TextArea resultTextArea: resultTextAreas)
            resultTextArea.setVisible(false);
        for(Button favourButton: favourButtons)
            favourButton.setVisible(false);
        for(Button shareButton: shareButtons)
            shareButton.setVisible(false);

        int index = 0;
        for(int i = 0; i < 3; i++){
            String tool = results.getResult(i).getTool();
            if(tool.equals("jinshan")){
                if(!jinshanCheckBox.isSelected())
                    continue;
                showUI(tool, index);
                index ++;
            }
            else if(tool.equals("youdao")){
                if(!youdaoCheckBox.isSelected())
                    continue;
                showUI(tool, index);
                index ++;
            }
            else if(tool.equals("biying")){
                if(!biyingCheckBox.isSelected())
                    continue;
                showUI(tool, index);
                index ++;
            }
        }
    }
    /**
     * 显示UI控件
     * @param tool 搜索网站名
     * @param index 控件下标
     */
    private void showUI(String tool, int index){
        Map<String, String> name = new HashMap<>();
        name.put("jinshan", "金山");
        name.put("youdao", "有道");
        name.put("biying", "必应");
        toolLabels[index].setText(name.get(tool));
        resultTextAreas[index].setVisible(true);
        if(Main.isOnline) {
            favourButtons[index].setVisible(true);
            shareButtons[index].setVisible(true);
        }
        resultTextAreas[index].setText(results.getResult(tool).getContent());
    }


    /**
     * 登录按钮点击事件
     * @param mouseEvent
     */
    public void loginButtonAction(MouseEvent mouseEvent) {
        Main.stageController.setStage("loginView", "queryView");
    }
}
