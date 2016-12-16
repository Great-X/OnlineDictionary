package client;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Main extends Application {
    //工程路径
    public static String basePath = "file:\\" + System.getProperty("user.dir");

    // 定义一个stage管理器
    public static StageController stageController = new StageController();

    //定义当前stage名称
    public static String curStage = "";

    // 定义一个线程池
    public static ExecutorService threadPool = Executors.newCachedThreadPool();

    //定义当前用户
    public static Boolean isOnline = false;
    public static String userName = "";

    public static Map<String, String> stages = new HashMap<>();

    @Override
    public void start(Stage primaryStage) throws Exception{
        stages.put("loginView", "login/LoginView.fxml");
        stages.put("queryView", "query/QueryView.fxml");
        stages.put("registerView", "register/RegisterView.fxml");
        stages.put("userView", "user/UserView.fxml");
        stages.put("msgView", "message/MsgView.fxml");

        // 显示查询窗口
        stageController.setStage("queryView");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
