package client.user;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * 定义用户列表表元
 */
public class UserListCell extends ListCell<UserObject> {
    HBox hbox = new HBox();
    CheckBox userNameBox = new CheckBox();
    Label userNameLabel = new Label();
    String lastItem;

    public UserListCell(){
        super();
        if(UserController.isSharing)
            hbox.getChildren().addAll(userNameBox);
        else
            hbox.getChildren().addAll(userNameLabel);
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
                userNameBox.setText("<null>");
            }
            else {
                userNameBox.setText(item.getName());
                userNameLabel.setText(item.getName());
                userNameBox.setTextFill(item.getStatus()== true? Color.BLUE:Color.GRAY);
                userNameLabel.setTextFill(item.getStatus()== true? Color.BLUE:Color.GRAY);
                userNameBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if(newValue)
                        UserController.sendToUsers.add(item.getName());
                    else
                        UserController.sendToUsers.remove(item.getName());
                });
            }
            setGraphic(hbox);
        }
    }
}
