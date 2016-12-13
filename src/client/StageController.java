package client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;

/**
 * 管理不同的stage
 */
public class StageController {
    //建立一个专门存储Stage的Map，全部用于存放Stage对象
    private HashMap<String, Stage> stages = new HashMap<String, Stage>();


    /**
     * 将加载好的Stage放到Map中进行管理
     *
     * @param name  设定Stage的名称
     * @param stage Stage的对象
     */
    public void addStage(String name, Stage stage) {
        stages.put(name, stage);
    }


    /**
     * 通过Stage名称获取Stage对象
     *
     * @param name Stage的名称
     * @return 对应的Stage对象
     */
    public Stage getStage(String name) {
        return stages.get(name);
    }


    /**
     * 加载窗口地址，需要fxml资源文件属于独立的窗口并用Pane容器或其子类继承
     *
     * @param name      注册好的fxml窗口的文件
     * @param resources fxml资源地址
     * @param styles    可变参数，init使用的初始化样式资源设置
     * @return 是否加载成功
     */
    public boolean loadStage(String name, String resources, StageStyle... styles) {
        try {
            //加载FXML资源文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resources));
            Pane tempPane = (Pane) loader.load();

            //构造对应的Stage
            Scene tempScene = new Scene(tempPane);
            Stage tempStage = new Stage();
            tempStage.setScene(tempScene);
            tempStage.setResizable(false);
            tempStage.setTitle("我的词典");
            tempStage.setOnCloseRequest(event -> {
                try {
                    ServerAPI.userOffline(Main.userName);
                }catch (SomeException e){
                    e.getMessage();
                }
                System.exit(0);});

            //配置initStyle
            for (StageStyle style : styles) {
                tempStage.initStyle(style);
            }

            //将设置好的Stage放到HashMap中
            this.addStage(name, tempStage);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 显示Stage但不隐藏任何Stage
     *
     * @param name 需要显示的窗口的名称
     * @return 是否显示成功
     */
    public boolean setStage(String name) {
        Main.curStage = name;
        loadStage(name, Main.stages.get(name));
        this.getStage(name).show();
        return true;
    }


    /**
     * 显示Stage并隐藏对应的窗口
     *
     * @param show  需要显示的窗口
     * @param close 需要删除的窗口
     * @return
     */
    public boolean setStage(String show, String close) {
        getStage(close).close();
        setStage(show);
        return true;
    }

}
