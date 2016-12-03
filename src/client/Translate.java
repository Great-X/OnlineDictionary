package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 定义翻译类
 */
public class Translate {
    public static HashMap<String, String> translate(String word) {
        HashMap<String, String> results = new HashMap<String, String>();

        //获取百度词典释义
        Main.threadPool.execute(() -> {
            String baiduResult = baiduTranslate(word);
            results.put("baidu", baiduResult);
        });

        //获取有道词典释义
        Main.threadPool.execute(() -> {
            String youdaoResult = youdaoTranslate(word);
            results.put("youdao", youdaoResult);
        });

        //获取必应词典释义
        Main.threadPool.execute(() -> {
            String biyingResult = biyingTranslate(word);
            results.put("biying", biyingResult);
        });

        while(results.size() != 3);
        return results;
    }


    /**
     * 必应翻译
     * @param word
     * @return
     */
    private static String biyingTranslate(String word) {
        String urlString = "http://cn.bing.com/dict/search?q=" + word;
        try {
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
                    if(matcher.find())
                        result += matcher.group(2) + " " + matcher.group(3) + "\n";
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 百度翻译
     * @param word
     * @return
     */
    private static String baiduTranslate(String word) {
        String urlString = "http://dict.baidu.com/s?wd=" + word + "&ptype=english";
        try {
            String content = getHtml(urlString);
            Pattern pattern = Pattern.compile("[\\s*\t\n\r]");
            Matcher matcher = pattern.matcher(content);
            content = matcher.replaceAll("");
            pattern = Pattern.compile("<divclass=\"en-content\"><div><p>(.*?)</p></div>");
            matcher = pattern.matcher(content);
            String[] items = null;
            String result = "";
            if(matcher.find()){
                items = matcher.group(1).split("</p><p>");
                for(int i = 0; i < items.length; i++) {
                    pattern = Pattern.compile("<strong>(.*?)</strong><span>(.*?)</span>");
                    matcher = pattern.matcher(items[i]);
                    if(matcher.find())
                        result += matcher.group(1) + " " + matcher.group(2) + "\n";
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
