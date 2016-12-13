package client.user;

import java.io.File;
import java.util.ArrayList;

//单词卡类
public class WordCard {
    private String word = "";
    private File file = null;
    private String sender = "";
    private ArrayList<String> receiver = new ArrayList<>();

    public WordCard(String word, File file, String userName, ArrayList<String> sendToUsers) {
        this.word = word;
        this.file = file;
        this.sender = userName;
        this.receiver = sendToUsers;
    }

    public String getWord(){
        return word;
    }

    public File getFile(){
        return file;
    }

    public String getSender(){
        return sender;
    }

    public ArrayList<String> getReceiver(){
        return receiver;
    }
}
