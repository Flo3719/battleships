import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIController {
    public GridPane enemyGrid;
    public Label friendNameLabel;
    public Label enemyNameLabel;

    public Button friendPlayButton0;


    public void handleButtonClick(Event evt){
        System.out.println(((Control)evt.getSource()).getId());
        friendNameLabel.setText(((Control)evt.getSource()).getId());
        friendPlayButton0.setText("hi");
    }
}
