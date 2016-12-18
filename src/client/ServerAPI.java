package client;

import java.io.*;
import java.net.ConnectException;
import java.util.*;

import client.user.WordCard;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.apache.commons.io.IOUtils;

//定义服务器接口，处理服务器和数据库相关事务
@SuppressWarnings({"deprecation", "ConstantConditions", "unused"})
public class ServerAPI {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static String token;
    private static String baseUrl = "http://192.168.0.5/";

    /**
     * 从数据库中检查用户名和密码是否匹配
     *
     * @param username 用户名
     * @param password 密码
     * @return 匹配则返回true， 否则返回false
     */
    public static boolean checkPassword(String username, String password) throws SomeException {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        HttpResponse response = post(baseUrl + "login", params);
        try {
            String str = EntityUtils.toString(response.getEntity());
            Map resMap = objectMapper.readValue(str, Map.class);
            if ((int) resMap.get("status") == 0) {
                token = (String) resMap.get("token");
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 注册成功，将用户数据写入数据库
     *
     * @param username 用户名
     * @param password 密码
     */
    public static void saveUserData(String username, String password) throws SomeException {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        HttpResponse response = post(baseUrl + "register", params);
        try {
            String str = EntityUtils.toString(response.getEntity());
            Map resMap = objectMapper.readValue(str, Map.class);
            if ((int) resMap.get("status") == 0)
                System.out.println("register successfully");
            else{
                throw new SomeException((String)resMap.get("msg"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将用户状态设置为在线
     *
     * @param username 用户名
     */
    public static void userOnline(String username) {
    }

    /**
     * 将用户状态设置为离线
     *
     * @param username 用户名
     */
    public static void userOffline(String username) throws SomeException {
        get(baseUrl + "logout?token=" + token);
        token = "";
    }

    /**
     * 从数据库中获取当前用户对当前单词的点赞情况
     *
     * @param userName 用户名
     * @param word     单词
     * @return 长度为3的ArrayList，依次是金山，有道，必应的点赞情况，点过赞则为true，否则为false
     */
    public static List<Boolean> getUserFavour(String userName, String word) throws SomeException {
        ArrayList<Boolean> userFavour = new ArrayList<>();
        userFavour.add(false);
        userFavour.add(false);
        userFavour.add(false);
        HttpResponse response = get(baseUrl + "word/getLiked?token=" + token + "&word=" + word);
        try {
            String str = EntityUtils.toString(response.getEntity());
            Map resMap = objectMapper.readValue(str, Map.class);
            if ((int) resMap.get("status") == 0) {
                userFavour.set(0, (boolean) resMap.get("bd"));
                userFavour.set(1, (boolean) resMap.get("yd"));
                userFavour.set(2, (boolean) resMap.get("by"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userFavour;
    }

    /**
     * 从数据库获取某单词的点赞数
     *
     * @param word 该单词
     * @return 长度为3的ArrayList, 依次是金山，有道，必应的点赞数
     */
    public static ArrayList<Integer> getFavoursNum(String word) throws SomeException {
        ArrayList<Integer> favoursNum = new ArrayList<>(3);
        favoursNum.add(0);
        favoursNum.add(0);
        favoursNum.add(0);
        HttpResponse response = get(baseUrl + "word/getRank?token=" + token + "&word=" + word);
        try {
            String str = EntityUtils.toString(response.getEntity());
            Map resMap = objectMapper.readValue(str, Map.class);
            if ((int) resMap.get("status") == 0) {
                favoursNum.set(0, (int) resMap.get("bd"));
                favoursNum.set(1, (int) resMap.get("yd"));
                favoursNum.set(2, (int) resMap.get("by"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return favoursNum;
    }

    /**
     * 从数据库获取用户在线信息
     *
     * @return HaspMap中每个元素代表一个用户，String为用户名，Boolean表示用户是否在线，在线为true，否则为false
     */
    public static HashMap<String, Boolean> getAllUsers() throws SomeException {
        HashMap<String, Boolean> allUsers = new HashMap<>();

        String types[] = {"getOfflineUsers", "getOnlineUsers"};
        boolean t = false;
        for (String type : types) {
            String url = baseUrl + type + "?token=" + token;
            HttpResponse response = get(url);
            try {
                String str = EntityUtils.toString(response.getEntity());
                Map resMap = objectMapper.readValue(str, Map.class);
                if ((int) resMap.get("status") == 0) {
                    String allUserString = (String) resMap.get("users");
                    String allUserArray[] = allUserString.split(" ");
                    for (String userInfo : allUserArray) {
                        if(userInfo.length() < 2)
                            continue;
                        String tmp[] = userInfo.split("-");
                        if (tmp.length < 2) {
                            continue;
                        }
                        String userName = tmp[1];
                        allUsers.put(userName, t);
                    }
                    t = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return allUsers;
    }

    /**
     * 点赞行为，将点赞记录写入数据库
     * @param word     单词
     * @param tool     工具，指的是金山、有道、必应之一
     * @param userName 用户名
     */
    public static void favourAction(String word, String tool, String userName) throws SomeException {
        //可能抛出异常“之前点过赞”
        HttpResponse response = get(baseUrl + "word/like?token=" + token + "&word=" + word + "&type=" + tool);
        try {
            String str = EntityUtils.toString(response.getEntity());
            Map resMap = objectMapper.readValue(str, Map.class);
            if ((int) resMap.get("status") != 0)
                throw new SomeException((String) resMap.get("msg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 分享单词卡
     * word 所发送的单词
     * file 被发送的单词卡图片
     * sender 发送方
     * receiver 接收方
     */
    public static void shareWordCard(WordCard wordCard) throws SomeException {
        String word = wordCard.getWord();
        File file = wordCard.getFile();
        //String sender = wordCard.getSender();
        ArrayList<String> receivers = wordCard.getReceiver();
        for (String receiver : receivers) {
            System.out.println("receiver = " + receiver);
            HttpResponse response = postFile(baseUrl + "card/send?word=" + word + "&token=" + token + "&username=" + receiver, file);
            try {
                String str = EntityUtils.toString(response.getEntity());
                Map resMap = objectMapper.readValue(str, Map.class);
                if ((int) resMap.get("status") != 0) {
                    throw new SomeException((String) resMap.get("msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 接收单词卡
     *
     * @return 返回单词卡列表
     */
    public static ArrayList<WordCard> receiveWordCard() throws SomeException {
        ArrayList<WordCard> allCard = new ArrayList<>();

        HttpResponse response = get(baseUrl + "getCards?token=" + token);
        try {
            String str = EntityUtils.toString(response.getEntity());
            Map resMap = objectMapper.readValue(str, Map.class);
            if ((int) resMap.get("status") != 0) {
                throw new SomeException((String) resMap.get("msg"));
            }
            str = (String) resMap.get("cards");
            String cardsStr[] = str.split("\\s");
            for (String cardStr : cardsStr) {
                String tmp[] = cardStr.split("-");
                if (tmp.length < 3) {
                    System.out.println("cardStr = " + cardStr);
                    continue;
                }
                WordCard wc = new WordCard(tmp[1], null, tmp[2],null, Integer.parseInt(tmp[0]));
                allCard.add(wc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allCard;
    }

    /**
     * 根据单词卡id获取图片
     * @param id 单词卡id
     * @return 图片的文件名
     */
    public static String getReceiveCardPic(int id) throws SomeException {
        HttpResponse response = get(String.format(baseUrl + "/card/get?token=" + token + "&id=%d", id));
        String filename = null;
        try{
            String str = EntityUtils.toString(response.getEntity());
            Map resMap = objectMapper.readValue(str, Map.class);
            if ((int) resMap.get("status") != 0) {
                throw new SomeException((String) resMap.get("msg"));
            }
            filename = (String)resMap.get("pic");
        } catch (IOException e) {
            e.printStackTrace();
            return filename;
        }
        String imageUrl = baseUrl + "card/getPic?token=" + token + "&pic=" + filename;
        response = get(imageUrl);
        try {
            InputStream input;
            input = response.getEntity().getContent();
            OutputStream output = new FileOutputStream(new File("src\\image\\word_card\\" + filename));
            IOUtils.copy(input, output);

            output.flush();
            output.close();
            input.close();        } catch (IOException e) {
            e.printStackTrace();
        }

        return filename;
    }

    private static HttpResponse post(String url, List<NameValuePair> params) throws SomeException {

        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = httpclient.execute(httpPost);
            if (response == null)
                throw new SomeException("can not connect to server");
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200)
                throw new SomeException(String.format("some error in server with code %d", statusCode));
            return response;
        }catch (ConnectException e){
            throw new SomeException("can not connect to server");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static HttpResponse postFile(String url, File f) throws SomeException {
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        try {
            FileBody fileBody = new FileBody(f);
            MultipartEntity entity = new MultipartEntity();
            entity.addPart("file", fileBody);
            httpPost.setEntity(entity);
            HttpResponse response = client.execute(httpPost);
            if (response == null)
                throw new SomeException("can not connect to server");
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200)
                throw new SomeException(String.format("some error in server with code %d", statusCode));
            return response;
        } catch (ConnectException e){
            throw new SomeException("can not connect to server");
        } catch (IOException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    private static HttpResponse get(String url) throws SomeException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpclient.execute(httpGet);
            if (response == null)
                throw new SomeException("can not connect to server");
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200)
                throw new SomeException(String.format("some error in server with code %d", statusCode));
            return response;
        } catch (ConnectException e){
            throw new SomeException("can not connect to server");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
