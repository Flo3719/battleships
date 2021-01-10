package Battleships;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("battleships_no_buttons.fxml"));
        System.out.println();
        primaryStage.setTitle("Battleships");
        primaryStage.setScene(new Scene(root, 1182, 487+29));
        primaryStage.show();
    }

    public void fillGrid(){

    }


    public static void main(String[] args) {
        launch(args);
    }
}
