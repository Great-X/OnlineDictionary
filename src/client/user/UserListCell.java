package client.user;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * 定义用户列表表元
 */
public class UserListCell extends ListCell<UserObject> {
    HBox hbox = new HBox();
    Label userNameLable = new Label();
    String lastItem;

    public UserListCell(){
        super();
        hbox.getChildren().addAll(userNameLable);
    }


    /**
     * 更新表元，包括更新用户名，根据用户状态改变用户名颜色
     * @param item
     * @param empty
     */
    @Override
    protected void updateItem(UserObject item, boolean empty){
        super.updateItem(item, empty);
        setText(null);
        if(empty){
            lastItem = null;
            setGraphic(null);
        }
        else{
            lastItem = item.toString();
            if(item == null){
                userNameLable.setText("<null>");
            }
            else {
                userNameLable.setText(item.getName());
                userNameLable.setTextFill(item.getStatus()== true? Color.BLUE:Color.GRAY);
            }
            setGraphic(hbox);
        }
    }
}
