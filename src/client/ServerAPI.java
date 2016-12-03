package client;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//定义服务器接口，处理服务器和数据库相关事务
public class ServerAPI {

    /**
     * TODO:从数据库中检查用户名和密码是否匹配
     * @param username 用户名
     * @param password 密码
     * @return 匹配则返回true， 否则返回false
     */
    public static boolean checkPassword(String username, String password) {
        return true;
    }


    /**
     * TODO: 遍历数据库，看该username是否已经存在
     * @param username 用户名
     * @return 若该username存在则返回true，否则返回false
     */
    public static boolean userNameExist(String username){
        return false;
    }


    /**
     * TODO: 注册成功，将用户数据写入数据库
     * @param username 用户名
     * @param password 密码
     */
    public static void saveUserData(String username, String password) {
    }


    /**
     * TODO: 将用户状态设置为在线
     * @param username 用户名
     */
    public static void userOnline(String username){}


    /**
     * TODO:将用户状态设置为离线
     * @param username 用户名
     */
    public static void userOffline(String username){}


    /**
     * TODO:从数据库中获取当前用户对当前单词的点赞情况
     * @param userName 用户名
     * @param word 单词
     * @return 长度为3的ArrayList，依次是百度，有道，必应的点赞情况，点过赞则为true，否则为false
     */
    public static List<Boolean> getUserFavour(String userName, String word) {
        ArrayList<Boolean> userFavour = new ArrayList<>();
        //下面代码需要被替换
        userFavour.add(true);
        userFavour.add(false);
        userFavour.add(true);

        return userFavour;
    }


    /**
     * TODO:从数据库获取某单词的点赞数
     * @param word 该单词
     * @return 长度为3的ArrayList,依次是百度，有道，必应的点赞数
     */
    public static ArrayList<Integer> getFavoursNum(String word) {
        ArrayList<Integer> favoursNum = new ArrayList<Integer>(3);
        //下面代码需要被替换
        favoursNum.add(1);
        favoursNum.add(2);
        favoursNum.add(3);

        return favoursNum;
    }


    /**
     * TODO:从数据库获取用户在线信息
     * @return HaspMap中每个元素代表一个用户，String为用户名，Boolean表示用户是否在线，在线为true，否则为false
     */
    public static HashMap<String, Boolean> getAllUsers() {
        HashMap<String, Boolean> allUsers = new HashMap<>();
        //下面代码需要被替换
        allUsers.put("Alice", true);
        allUsers.put("Bob", false);
        allUsers.put("Chris", true);

        return allUsers;
    }


    /**
     * TODO:分享单词卡
     * @param word 所发送的单词
     * @param file 被发送的单词卡图片
     * @param sender 发送方
     * @param receiver 接收方数组
     */
    public static void shareWordCard(String word, File file, String sender, String[] receiver){}

}
