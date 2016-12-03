package client.query;

import client.Main;
import client.SomeException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.awt.peer.CheckboxPeer;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryController implements Initializable{
    //搜索结果
    private static HashMap<String, String> results;

    //点赞数
    private static HashMap<String, Integer> favours = new LinkedHashMap<>();

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
    public CheckBox baiduCheckBox;
    @FXML
    public CheckBox youdaoCheckBox;
    @FXML
    public CheckBox biyingCheckBox;

    //点赞按钮
    @FXML
    public Button favourButton;

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
        checkBoxs[0] = baiduCheckBox;
        checkBoxs[1] = youdaoCheckBox;
        checkBoxs[2] = biyingCheckBox;
        resultTextAreas[0] = resultTextArea0;
        resultTextAreas[1] = resultTextArea1;
        resultTextAreas[2] = resultTextArea2;
        toolLabels[0] = toolLabel0;
        toolLabels[1] = toolLabel1;
        toolLabels[2] = toolLabel2;

        for(CheckBox checkBox: checkBoxs)
            checkBox.setSelected(true);
        for(TextArea resultTextArea: resultTextAreas)
            resultTextArea.setWrapText(true);

        // 设置三个复选框的监听器
        for(CheckBox checkBox: checkBoxs)
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> showResult());
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
        try {
            results = client.Translate.translate(word);
        } catch (SomeException e) {
            hintLabel.setText(e.getMessage());
            return;
        }

        //获取点赞数
        List<Integer> res = getFavoursNum(word);
        favours.replace("baidu", res.get(0));
        favours.replace("youdao", res.get(1));
        favours.replace("biying", res.get(2));

        //根据点赞数排序
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(favours.entrySet());
        Collections.sort(list, (o1, o2) -> o2.getValue() - o1.getValue());
        favours.clear();
        for(Map.Entry<String, Integer> entry: list){
            favours.put(entry.getKey(), entry.getValue());
        }

        showResult();
    }

    /**
     * TODO:从数据库获取某单词的点赞数
     * @param word 该单词
     * @return 长度为3的ArrayList,依次是百度，有道，必应的点赞数
     */
    private ArrayList<Integer> getFavoursNum(String word) {
        ArrayList<Integer> favoursNum = new ArrayList<Integer>(3);

        //下面代码需要被替换
        favoursNum.add(1);
        favoursNum.add(2);
        favoursNum.add(3);

        return favoursNum;
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
        //初始化
        for(Label toolLabel: toolLabels)
            toolLabel.setText("");
        for(TextArea resultTextArea: resultTextAreas)
            resultTextArea.setVisible(false);

        int index = 0;
        for(Map.Entry<String, Integer> entry: favours.entrySet()){
            String tool = entry.getKey();
            if(tool.equals("baidu")){
                if(!baiduCheckBox.isSelected())
                    continue;
                toolLabels[index].setText("百度");
                resultTextAreas[index].setVisible(true);
                resultTextAreas[index].setText(results.get("baidu"));
                index ++;
            }
            else if(tool.equals("youdao")){
                if(!youdaoCheckBox.isSelected())
                    continue;
                toolLabels[index].setText("有道");
                resultTextAreas[index].setVisible(true);
                resultTextAreas[index].setText(results.get("youdao"));
                index ++;
            }
            else if(tool.equals("biying")){
                if(!biyingCheckBox.isSelected())
                    continue;
                toolLabels[index].setText("必应");
                resultTextAreas[index].setVisible(true);
                resultTextAreas[index].setText(results.get("biying"));
                index ++;
            }
        }
    }
}
