package client.query;

import client.Main;
import client.ServerAPI;
import client.SomeException;
import client.user.UserController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.peer.CheckboxPeer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

    //用户头像
    @FXML
    public ImageView userImage;

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

    //点赞图标
    public ImageView[] favourImages = new ImageView[3];
    @FXML
    public ImageView favourImage0;
    @FXML
    public ImageView favourImage1;
    @FXML
    public ImageView favourImage2;

    //点赞数
    public Label[] favourNumLabels = new Label[3];
    @FXML
    public Label favourNumLabel0;
    @FXML
    public Label favourNumLabel1;
    @FXML
    public Label favourNumLabel2;

    //分享图标
    public ImageView[] shareImages = new ImageView[3];
    @FXML
    public ImageView shareImage0;
    @FXML
    public ImageView shareImage1;
    @FXML
    public ImageView shareImage2;

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
    public ImageView[] toolImages = new ImageView[3];
    @FXML
    public ImageView toolImage0;
    @FXML
    public ImageView toolImage1;
    @FXML
    public ImageView toolImage2;

    //功能按钮
    @FXML
    public Button loginButton;
    @FXML
    public Button logoutButton;
    @FXML
    public Button userButton;
    @FXML
    public Button msgButton;

    private final String jinshanImagePath = Main.baseImagePath + "ui\\jinshan.png";
    private final String youdaoImagePath = Main.baseImagePath + "ui\\youdao.png";
    private final String biyingImagePath = Main.baseImagePath + "ui\\biying.png";
    private final String touristImagePath = Main.baseImagePath + "ui\\tourist.png";
    private final String favourImagePath = Main.baseImagePath + "ui\\favour.png";
    private final String unfavourImagePath = Main.baseImagePath + "ui\\unfavour.png";

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
        toolImages[0] = toolImage0;
        toolImages[1] = toolImage1;
        toolImages[2] = toolImage2;
        favourButtons[0] = favourButton0;
        favourButtons[1] = favourButton1;
        favourButtons[2] = favourButton2;
        favourImages[0] = favourImage0;
        favourImages[1] = favourImage1;
        favourImages[2] = favourImage2;
        favourNumLabels[0] = favourNumLabel0;
        favourNumLabels[1] = favourNumLabel1;
        favourNumLabels[2] = favourNumLabel2;
        shareButtons[0] = shareButton0;
        shareButtons[1] = shareButton1;
        shareButtons[2] = shareButton2;
        shareImages[0] = shareImage0;
        shareImages[1] = shareImage1;
        shareImages[2] = shareImage2;

        for(CheckBox checkBox: checkBoxs)
            checkBox.setSelected(true);
        for(TextArea resultTextArea: resultTextAreas)
            resultTextArea.setWrapText(true);

        // 设置三个复选框的监听器
        for(CheckBox checkBox: checkBoxs)
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> showResult());

        //设置输入文本域监听器
        inputTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER)
                    search();
            }
        });
        Map<String, String> name = new HashMap<>();
        name.put("jinshan", jinshanImagePath);
        name.put("youdao", youdaoImagePath);
        name.put("biying", biyingImagePath);
        toolImages[0].setImage(new Image(name.get("jinshan")));
        toolImages[1].setImage(new Image(name.get("youdao")));
        toolImages[2].setImage(new Image(name.get("biying")));


        for(int i = 0; i < 3; i++) {
            toolImages[i].setVisible(true);
            shareButtons[i].setVisible(false);
            shareImages[i].setVisible(false);
            favourButtons[i].setVisible(false);
            favourImages[i].setVisible(false);

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
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File("src\\image\\word_card\\" + curWord + ".png"));
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
                    showResult();
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
        if (curWord.length() != 0)
            showResult();

        b = false;
        userButton.setVisible(b);
        msgButton.setVisible(b);
        logoutButton.setVisible(b);
        b = true;
        //根据用户是否离线来隐藏一些控件
        if(!Main.isOnline){
            userImage.setImage(new Image(touristImagePath));
        }
        else{
            createUserImage();
            userImage.setImage(new Image(Main.baseImagePath + "user\\" + Main.userName + ".png"));
        }
    }

    /**
     * 随机生成用户头像
     */
    public void createUserImage(){
        String username = Main.userName;
        File f = new File("src\\image\\user\\" + username + ".png");
        try {
            FileReader fr = new FileReader(f);
        } catch (FileNotFoundException e) {
            int hash = username.hashCode();
            String binaryString = Integer.toBinaryString(hash);
            StringBuilder stringBuffer = new StringBuilder(binaryString);
            String matrix = "";
            int i,j,k;
            for(i = 0; i < 15; i++){
                matrix += stringBuffer.charAt(i % binaryString.length());
            }
            int color[] = new int[3];
            for(j = 0; j < 3; j ++){
                color[j] = 0;
                for(k = 0; k < 8; k++,i++){
                    color[j] = color[j] * 2 + stringBuffer.charAt(i % binaryString.length()) - '0';
                }
            }
            BufferedImage identicon = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = identicon.getGraphics();
            graphics.setColor(Color.white); // 背景色
            graphics.fillRect(0, 0, identicon.getWidth(), identicon.getHeight());
            graphics.setColor(new Color(color[0], color[1], color[2])); // 图案前景色
            for (i = 0; i < 5; i++) {
                for (j = 0; j < 3; j++) {
                    if (matrix.charAt(i*3+j) == '1') {
                        graphics.fillRect(j * 8, i * 8, 8, 8);
                        graphics.fillRect((4 - j)*8, i * 8, 8, 8);
                    }
                }
            }
            ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
            try {
                ImageOutputStream ios = ImageIO.createImageOutputStream(f);
                writer.setOutput(ios);
                writer.write(identicon);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
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
    public void searchMouseAction(MouseEvent mouseEvent){
        search();
    }

    /**
     * 搜索单词
     */
    private void search(){
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
        showResult();
    }


    /**
     * 用正则表达式判断输入单词是否合法
     * @param word
     * @return
     */
    private boolean isLegal(String word) {
        Pattern pattern = Pattern.compile("[a-zA-Z]+[0-9a-zA-Z]*");
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
        results.getResult("jinshan").setIsSelected(jinshanCheckBox.isSelected());
        results.getResult("youdao").setIsSelected(youdaoCheckBox.isSelected());
        results.getResult("biying").setIsSelected(biyingCheckBox.isSelected());

        //根据点赞数排序
        results.sort();

        //初始化
        for(int i = 0; i < 3; i++){
            toolImages[i].setVisible(false);
            resultTextAreas[i].setVisible(false);
            favourButtons[i].setVisible(false);
            shareButtons[i].setVisible(false);
            favourImages[i].setVisible(false);
            shareImages[i].setVisible(false);
            favourNumLabels[i].setText("");
        }

        for(int i = 0; i < results.getSelectNum(); i++){
            String tool = results.getResult(i).getTool();
            showUI(tool, i);
        }
    }
    /**
     * 显示UI控件
     * @param tool 搜索网站名
     * @param index 控件下标
     */
    private void showUI(String tool, int index){
        Map<String, String> name = new HashMap<>();
        name.put("jinshan", jinshanImagePath);
        name.put("youdao", youdaoImagePath);
        name.put("biying", biyingImagePath);
        toolImages[index].setVisible(true);
        toolImages[index].setImage(new Image(name.get(tool)));
        resultTextAreas[index].setVisible(true);
        if(Main.isOnline) {
            favourButtons[index].setVisible(true);
            shareButtons[index].setVisible(true);
            favourImages[index].setVisible(true);
            if(results.getResult(tool).getUserFavour()){
                favourImages[index].setImage(new Image(favourImagePath));
            }
            else{
                favourImages[index].setImage(new Image(unfavourImagePath));
            }
            favourNumLabels[index].setText(String.valueOf(results.getResult(index).getFavourNum()));
            shareImages[index].setVisible(true);
        }
        resultTextAreas[index].setText(results.getResult(tool).getContent());
    }


    /**
     * 登录按钮点击事件
     * @param mouseEvent
     */
    static boolean b = true;
    public void loginButtonAction(MouseEvent mouseEvent) {
        if(!Main.isOnline)
            Main.stageController.setStage("loginView", "queryView");
        else{
            userButton.setVisible(b);
            msgButton.setVisible(b);
            logoutButton.setVisible(b);
            b = !b;
        }
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
