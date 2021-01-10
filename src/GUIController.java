import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class GUIController {
    public GridPane enemy_grid;
    public Label friend_name_Label;

    public void handleButtonClick(Event evt){
        System.out.println(((Control)evt.getSource()).getId());
    }

}
