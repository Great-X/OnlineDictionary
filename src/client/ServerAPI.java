package client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.user.WordCard;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

//定义服务器接口，处理服务器和数据库相关事务
@SuppressWarnings("deprecation")
public class ServerAPI {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static String token;
    private static String baseUrl = "http://127.0.0.1/";
    /**
     * 从数据库中检查用户名和密码是否匹配
     * @param username 用户名
     * @param password 密码
     * @return 匹配则返回true， 否则返回false
     */
    public static boolean checkPassword(String username, String password) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        HttpResponse response = post(baseUrl + "login", params);
        if(response == null)
            return false;
        if (response.getStatusLine().getStatusCode() == 200)
        {
            String str;
            try {
                str = EntityUtils.toString(response.getEntity());
                Map resMap = objectMapper.readValue(str, Map.class);
                if((int)resMap.get("status") == 0){
                    token = (String) resMap.get("token");
                    return true;
                }
                else{
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }


    /**
     *  遍历数据库，看该username是否已经存在
     * @param username 用户名
     * @return 若该username存在则返回true，否则返回false
     */
    public static boolean userNameExist(String username){
        HttpResponse response = get(baseUrl + "checkUser?username=" + username);
        if(response == null)
            return true;
        if (response.getStatusLine().getStatusCode() == 200)
        {
            String str;
            try {
                str = EntityUtils.toString(response.getEntity());
                Map resMap = objectMapper.readValue(str, Map.class);
                return (int) resMap.get("status") != 0;
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        }
        return true;
    }


    /**
     * 注册成功，将用户数据写入数据库
     * @param username 用户名
     * @param password 密码
     */
    public static void saveUserData(String username, String password) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        HttpResponse response = post(baseUrl + "register", params);
        assert response != null;

        if (response.getStatusLine().getStatusCode() == 200)
        {
            String str;
            try {
                str = EntityUtils.toString(response.getEntity());
                Map resMap = objectMapper.readValue(str, Map.class);
                if((int)resMap.get("status") == 0)
                    System.out.println("register successfully");
                else
                    System.out.println((String)resMap.get("msg"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 将用户状态设置为在线
     * @param username 用户名
     */
    public static void userOnline(String username){
    }


    /**
     * 将用户状态设置为离线
     * @param username 用户名
     */
    public static void userOffline(String username) throws SomeException {
        HttpResponse response = get(baseUrl + "logout?token=" + token);
        token = "";
        if (response == null || response.getStatusLine().getStatusCode() != 200)
            throw new SomeException("无法访问服务器");
    }


    /**
     * 从数据库中获取当前用户对当前单词的点赞情况
     * @param userName 用户名
     * @param word 单词
     * @return 长度为3的ArrayList，依次是金山，有道，必应的点赞情况，点过赞则为true，否则为false
     */
    public static List<Boolean> getUserFavour(String userName, String word) {
        ArrayList<Boolean> userFavour = new ArrayList<>();
        userFavour.add(false);
        userFavour.add(false);
        userFavour.add(false);
        HttpResponse response = get(baseUrl + "getLiked?token=" + token + "&word=" + word);
        if(response == null)
            return userFavour;
        if (response.getStatusLine().getStatusCode() == 200)
        {
            String str;
            try {
                str = EntityUtils.toString(response.getEntity());
                Map resMap = objectMapper.readValue(str, Map.class);
                if((int)resMap.get("status") == 0)
                {
                    userFavour.add((boolean)resMap.get("bd"));
                    userFavour.add((boolean)resMap.get("yd"));
                    userFavour.add((boolean)resMap.get("by"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userFavour;
    }


    /**
     * 从数据库获取某单词的点赞数
     * @param word 该单词
     * @return 长度为3的ArrayList,依次是金山，有道，必应的点赞数
     */
    public static ArrayList<Integer> getFavoursNum(String word) {
        ArrayList<Integer> favoursNum = new ArrayList<>(3);
        favoursNum.add(0);
        favoursNum.add(0);
        favoursNum.add(0);
        HttpResponse response = get(baseUrl + "getRank?token=" + token + "&word=" + word);
        if(response == null)
            return favoursNum;
        if (response.getStatusLine().getStatusCode() == 200)
        {
            String str;
            try {
                str = EntityUtils.toString(response.getEntity());
                Map resMap = objectMapper.readValue(str, Map.class);
                if((int)resMap.get("status") == 0)
                {
                    favoursNum.add((int)resMap.get("bd"));
                    favoursNum.add((int)resMap.get("yd"));
                    favoursNum.add((int)resMap.get("by"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return favoursNum;
    }


    /**
     * 从数据库获取用户在线信息
     * @return HaspMap中每个元素代表一个用户，String为用户名，Boolean表示用户是否在线，在线为true，否则为false
     */
    public static HashMap<String, Boolean> getAllUsers() {
        HashMap<String, Boolean> allUsers = new HashMap<>();

        String types[] = {"getOfflineUsers", "getOnlineUsers"};
        boolean t = false;
        for(String type: types){
            String url = baseUrl + type + "?token=" + token;
            HttpResponse response = get(url);
            if(response == null)
                continue;
            if (response.getStatusLine().getStatusCode() == 200)
            {
                String str;
                try {
                    str = EntityUtils.toString(response.getEntity());
                    Map resMap = objectMapper.readValue(str, Map.class);
                    if((int)resMap.get("status") == 0)
                    {
                        String allUserString = (String)resMap.get("users");
                        String allUserArray[] = allUserString.split(" ");
                        for(String userInfo: allUserArray){
                            String userName = userInfo.split("-")[1];
                            allUsers.put(userName, t);
                        }
                        t = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return allUsers;
    }

    /**
     * TODO:点赞行为，将点赞记录写入数据库
     * @param word 单词
     * @param tool 工具，指的是金山、有道、必应之一
     * @param userName 用户名
     */
    public static void favourAction(String word, String tool, String userName) throws SomeException{
        //可能抛出异常“之前点过赞”
    }

    /**
     * TODO:分享单词卡
     * word 所发送的单词
     * file 被发送的单词卡图片
     * sender 发送方
     * receiver 接收方
     */
    public static void shareWordCard(WordCard wordCard) {
        String word = wordCard.getWord();
        File file = wordCard.getFile();
        String sender = wordCard.getSender();
        ArrayList<String> receiver = wordCard.getReceiver();
    }

    /**
     * TODO:接收单词卡
     * @return 返回单词卡列表
     */
    public static ArrayList<WordCard> receiveWordCard(){
        return null;
    }




    private static HttpResponse post(String url, List<NameValuePair> params){

        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(url);
        try
        {
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            return httpclient.execute(httpPost);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private static HttpResponse get(String url){
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            return httpclient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
