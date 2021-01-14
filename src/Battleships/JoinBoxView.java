package Battleships;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JoinBoxView {

    public static void display(){
        Stage joinStage = new Stage();

        // Labels and TextFields
        Label nameLabel = new Label("Name:");
        TextField nameTextField = new TextField();
        Label ipLabel = new Label("IP:");
        TextField ipTextField = new TextField();
        Label portLabel = new Label("Port:");
        TextField portTextField = new TextField();

        // Buttons
        Button joinButton = new Button();
        Button hostButton = new Button();
        joinButton.setText("Join");
        hostButton.setText("Host");
        //joinButton.setOnAction(event -> JoinServer(nameTextField, ipTextField, portTextField));
        //hostButton.setOnAction(event -> HostServer(nameTextField, portTextField));

        // Layout Boxes
        HBox nameBox = new HBox(10, nameLabel, nameTextField);
        HBox ipBox = new HBox(10, ipLabel, ipTextField);
        HBox portBox = new HBox(10, portLabel, portTextField);
        HBox internetBox = new HBox(30, ipBox, portBox);
        HBox buttonsBox = new HBox(10, joinButton, hostButton);
        VBox textFields = new VBox(10, nameBox, internetBox);

        // BorderPane
        BorderPane root = new BorderPane();
        root.setCenter(textFields);
        root.setBottom(buttonsBox);

        // Scene
        Scene scene = new Scene(root, 500, 100);

        // Stage
        joinStage.setTitle("Battleships - Join or Host");
        joinStage.setScene(scene);
        joinStage.showAndWait();

    }

    //@Override
    //public void start(Stage joinStage) throws Exception{

    //}

    //public static void main(String[] args) {
        //launch(args);
    //}
}