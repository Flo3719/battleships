package Battleships.Views;

import java.io.IOException;
import java.util.Optional;

import Battleships.Controllers.MainViewController;
import Battleships.Models.MainViewDelegate;
import Battleships.Models.Exceptions.OutOfTurnException;
import Battleships.Models.Exceptions.ServerNotAvailableException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;

public class MainView {
    //Variables
    public GridPane enemyGrid;
    public GridPane friendGrid;
    public Label friendNameLabel;
    public Label enemyNameLabel;
    public MenuItem joinMenuItem;

    public Label friendScoreLabel;
    public Label enemyScoreLabel;

    public Label enemyTurnIndicator;
    public Label friendTurnIndicator;

    public Label timeLabel;

    public MainViewDelegate controller;

    public MenuItem startMenuItem;
    public MenuItem giveUpMenuItem;

    //Getters
    public int getRow(int index){
        return index/15 ;
    }
    public int getColumn(int index){
        return (index%15);
    }

    //Setters
    public void setFriendScoreLabel(int points){
        friendScoreLabel.setText(Integer.toString(points) + " Points");
    }
    public void setEnemyScoreLabel(int points){
        enemyScoreLabel.setText(Integer.toString(points) + " Points");
    }
    public void setTimeLabel(int seconds){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int onlyMinutes = seconds/60;
                int onlySeconds = seconds%60;
                String minutesFormatted = String.format("%02d", onlyMinutes);
                String secondsFormatted = String.format("%02d", onlySeconds);
                timeLabel.setText(minutesFormatted + ":" + secondsFormatted);
            }
        });
    }
    public void setFriendNameLabel(String name){
        friendNameLabel.setText(name + "'s Field");
    }
    public void setEnemyNameLabel(String name){
        enemyNameLabel.setText(name + "'s Field");
    }
    public void setPlayField(String indicator, String side, int index, String color){
        if(side.equals("friend")){
            Button button = (Button)friendGrid.getChildren().get(index+1);
            button.setText(indicator.toString());
            button.setStyle(color);
        }else if(side.equals("enemy")){
            Button button = (Button)enemyGrid.getChildren().get(index+1);
            button.setText(indicator);
            button.setStyle(color);
        }
    }

    //Methods
    @FXML
    public void initialize(){
        this.controller = MainViewController.sharedInstance;

        addPlayButtons(friendGrid, "friend");
        addPlayButtons(enemyGrid, "enemy");
        joinMenuItem.setOnAction((ActionEvent event)->{
            controller.handleMenuJoinClick();
            //controller.handleJoinClick(event);
        });
        startMenuItem.setOnAction((ActionEvent event)->{
            try {
				controller.handleStartClick(event);
			} catch (IOException | ServerNotAvailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            //controller.handleJoinClick(event);
        });
        giveUpMenuItem.setOnAction((ActionEvent event)->{
            controller.handleGiveUpClick();
        });

        this.controller.initialize(this);
    }
    /**
     * @param grid
     * @param playerSide String saying "friend" or "enemy"
     */
    public void addPlayButtons(GridPane grid, String playerSide){
    	Button buttons[] = new Button[150];
        for(int i = 0; i<=149; i++){
            // define button
            Button button = new Button(Integer.toString(i));
            buttons[i] = button;
            // set button attributes
            button.setId(playerSide + "PlayButton" + i);
            button.setText("~");
            button.setTextFill(Paint.valueOf("White"));
            button.setMinHeight(37);
            button.setMinWidth(37);
            button.setPrefHeight(37);
            button.setPrefWidth(37);
            if(playerSide.equals("enemy")){
            	buttons[i].setOnAction((ActionEvent event) -> {
                    try {
    					controller.handleButtonClick(event);
    				} catch (OutOfTurnException e) {
    					System.out.println("It is not your turn... Be patient");
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                });
                button.getStyleClass().add("enemy-button");
            }
            // add button to grid
            grid.add(buttons[i], getColumn(i), getRow(i));
        }
    }

    
    /**
     * Shows an alert to the GameClientHandlers connected to the game that has been won.
     * If the alert has a result, the system of the clients is closed.
     * @param msg message to display on the content text of the alert
     * @param walkover tells if the game was won with a walkover
     */
    public void Alert(String msg, boolean walkover) {
    	Platform.runLater(new Runnable() {

			@Override
			public void run() {
		    	javafx.scene.control.Alert a = new javafx.scene.control.Alert(AlertType.NONE);
		    	a.setContentText(msg);
		    	if (walkover) {
		    		a.setHeaderText("WalkOver!");;
		    	} else {
		    		a.setHeaderText("Won on scores");
		    	}
		    	a.setAlertType(AlertType.INFORMATION);
		    	Optional<ButtonType> result = a.showAndWait();
		    	if(result.isPresent()) {
		    		System.exit(0);
		    	}
		    	
			}
		});

    }

    public void setTurn(String side){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                switch (side){
                    case "friend":
                        friendTurnIndicator.setTextFill(Paint.valueOf("#7FFF00"));
                        enemyTurnIndicator.setTextFill(Paint.valueOf("#222222"));
                        break;
                    case "enemy":
                        friendTurnIndicator.setTextFill(Paint.valueOf("#222222"));
                        enemyTurnIndicator.setTextFill(Paint.valueOf("#7FFF00"));
                        break;
                    case "none":
                        friendTurnIndicator.setTextFill(Paint.valueOf("#222222"));
                        enemyTurnIndicator.setTextFill(Paint.valueOf("#222222"));
                }
            }
        });
    }
}
