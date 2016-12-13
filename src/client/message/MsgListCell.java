package client.message;

import client.user.WordCard;
import com.jfoenix.controls.JFXListCell;
import com.sun.org.apache.xerces.internal.parsers.CachingParserPool;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class MsgListCell extends JFXListCell<WordCard>{
    VBox vBox = new VBox();
    Label wordLabel = new Label();
    Label senderLabel = new Label();
    ImageView imageView = new ImageView();
    String lastItem;

    public MsgListCell(){
        super();
        vBox.getChildren().addAll(wordLabel, senderLabel, imageView);
    }

    @Override
    public void updateItem(WordCard item, boolean empty){
        super.updateItem(item, empty);
        setText(null);
        if(empty){
            lastItem = null;
            setGraphic(null);
        }
        else {

            lastItem = item.toString();
            if(item == null){
                wordLabel.setText("<null>");
                senderLabel.setText("<null>");
                imageView.setVisible(false);
            }
            else {
                wordLabel.setText(item.getWord());
                senderLabel.setText(item.getSender());
                //imageView.setImage(new Image("hello.png"));
            }
            setGraphic(vBox);
        }
    }
}
