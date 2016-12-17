package client;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

/**
 * 定义翻译类
 */
public class Translate {
    public static HashMap<String, String> translate(String word) throws SomeException{
        HashMap<String, String> results = new HashMap<String, String>();

        //获取必应词典释义
        String biyingResult = null;
        try {
            biyingResult = biyingTranslate(word);
            results.put("biying", biyingResult);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("网络开小差了（>_<）");
            alert.setContentText("请确认联网后再次打开...");
            alert.showAndWait();
            System.exit(-1);
        }

        //获取金山词典释义
        Main.threadPool.execute(() -> {
            String jinshanResult = jinshanTranslate(word);
            results.put("jinshan", jinshanResult);
        });

        //获取有道词典释义
        Main.threadPool.execute(() -> {
            String youdaoResult = youdaoTranslate(word);
            results.put("youdao", youdaoResult);
        });

        while(results.size() != 3)
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        for(Map.Entry<String, String> entry: results.entrySet()){
            if(entry.getValue().length() == 0)
                throw new SomeException("没有找到该单词");
        }
        return results;
    }


    /**
     * 必应翻译
     * @param word
     * @return
     */
    private static String biyingTranslate(String word) throws IOException {
        String urlString = "http://cn.bing.com/dict/search?q=" + word;
        String content = getHtml(urlString);
        Pattern pattern = Pattern.compile("[\\s*\t\n\r]");
        Matcher matcher = pattern.matcher(content);
        content = matcher.replaceAll("");
        pattern = Pattern.compile("<divclass=\"hd_area\">.*?<ul>(.*?)</ul>");
        matcher = pattern.matcher(content);
        String[] items = null;
        String result = "";
        if(matcher.find()){
            items = matcher.group(1).split("</li>");
            for(int i = 0; i < items.length; i++) {
                pattern = Pattern.compile("<li><spanclass=\"pos(web){0,1}\">(.*?)</span><spanclass=\"def\"><span>(.*?)</span></span>");
                matcher = pattern.matcher(items[i]);
                if(matcher.find()) {
                    result += matcher.group(2);
                    String strTmp = matcher.group(3);
                    strTmp = Pattern.compile("&quot;").matcher(strTmp).replaceAll("\\\"");
                    matcher = Pattern.compile("(.*?)</span><ahref=.*?>(.*?)</a><span>(.*?)</span><span>(.*)").matcher(strTmp);
                    if(matcher.find())
                        result += " " + matcher.group(1) + matcher.group(2) + matcher.group(3) + " " + matcher.group(4) + "\n";
                    else
                        result += " " + strTmp + "\n";
                }
            }
        }
        return result;
    }


    /**
     * 金山翻译
     * @param word
     * @return
     */
    private static String jinshanTranslate(String word) {
        String urlString = "http://www.iciba.com/" + word;
        try {
            String content = getHtml(urlString);
            Pattern pattern = Pattern.compile("[\\s*\t\n\r]");
            Matcher matcher = pattern.matcher(content);
            content = matcher.replaceAll("");
            pattern = Pattern.compile("<ulclass=\"base-listswitch_part\"class=\"\">(.*?)</ul>");
            matcher = pattern.matcher(content);
            String[] items = null;
            String result = "";
            if(matcher.find()){
                items = matcher.group(1).split("</li>");
                for(int i = 0; i < items.length; i++) {
                    pattern = Pattern.compile("<liclass=\"clearfix\"><spanclass=\"prop\">(.*?)</span><p>(.*?)</p>");
                    matcher = pattern.matcher(items[i]);
                    if(matcher.find()) {
                        String strTmp = Pattern.compile("[(<span>)(</span>)]").matcher(matcher.group(2)).replaceAll("");
                        result += matcher.group(1) + " " + strTmp + "\n";
                    }
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 有道翻译
     * @param word
     * @return
     */
    private static String youdaoTranslate(String word){
        String urlString = "http://dict.youdao.com/w/eng/" + word + "/#keyfrom=dict2.index";
        try {
            String content = getHtml(urlString);
            Pattern pattern = Pattern.compile("[\\s*\t\n\r]");
            Matcher matcher = pattern.matcher(content);
            content = matcher.replaceAll("");
            pattern = Pattern.compile("<divid=\"phrsListTab\".*?>(.*?)<ul>(.*?)</ul>");
            matcher = pattern.matcher(content);
            String[] items = null;
            String result = "";
            if(matcher.find()){
                items = matcher.group(2).split("</li>");
                for(int i = 0; i < items.length; i++) {
                    pattern = Pattern.compile("<li>(.*)");
                    matcher = pattern.matcher(items[i]);
                    if(matcher.find())
                        result += matcher.group(1) + "\n";
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取网页html源码
     * @param urlString
     * @return
     * @throws IOException
     */
    private static String getHtml(String urlString) throws IOException {
        StringBuffer html = new StringBuffer();
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStreamReader isr = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(isr);
        String temp;
        while ((temp = br.readLine()) != null) {
            html.append(temp).append("\n");
        }
        br.close();
        isr.close();
        return html.toString();
    }
}
