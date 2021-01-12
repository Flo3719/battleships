import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIController {
    public GridPane enemyGrid;
    public GridPane friendGrid;
    public Label friendNameLabel;
    public Label enemyNameLabel;

    public Button friendPlayButton0;

    @FXML
    public void initialize(){
        addPlayButtons(friendGrid);
    }

    public void handleButtonClick(Event evt){
        System.out.println(((Control)evt.getSource()).getId());
        friendNameLabel.setText(((Control)evt.getSource()).getId());
    }

    public void addPlayButtons(GridPane grid){


//        Button button = new Button("~");
//        grid.add(button, getColumn(5), getRow(5));
        Button buttons[] = new Button[150];
        //List<Button> buttons = new ArrayList<>();
        for(int i = 0; i<=149; i++){
            // define button
            Button button = new Button(Integer.toString(i));
            buttons[i] = button;

            // set button attributes
            button.setId("friendPlayButton"+i);
            button.setText("~");
            button.setTextFill(Paint.valueOf("White"));
            button.setMinHeight(37);
            button.setMinWidth(37);
            button.setPrefHeight(37);
            button.setPrefWidth(37);
            buttons[i].setOnAction((ActionEvent event) -> {
                handleButtonClick(event);
            });

            // add button to grid
            grid.add(buttons[i], getColumn(i), getRow(i));
        }
    }

    public int getRow(int index){
        return index/15 ;
    }
    public int getColumn(int index){
        return (index%15);
    }
}
