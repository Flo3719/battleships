package Battleships.Views;

import java.io.IOException;


import Battleships.Controllers.MainViewController;
import Battleships.Models.MainViewDelegate;
import Battleships.Models.Exceptions.OutOfTurnException;
import Battleships.Models.Exceptions.ServerNotAvailableException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;

public class MainView {
    public GridPane enemyGrid;
    public GridPane friendGrid;
    public Label friendNameLabel;
    public Label enemyNameLabel;
    public MenuItem joinMenuItem;

    public Label friendScoreLabel;
    public Label enemyScoreLabel;

    public Label timeLabel;

    public MainViewDelegate controller;

    public MenuItem startMenuItem;


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

        this.controller.initialize(this);
    }
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
            Button button = (Button)friendGrid.getChildren().get(index+1);
            button.setText(indicator);
        }
    }
    //TODO: remove the following and replace it by a check in the actual Map/list where the state is saved. The view should not store data.
    public String getPlayField(int index){
        Button button = (Button)friendGrid.getChildren().get(index+1);
        return button.getText();
    }
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
            buttons[i].setOnAction((ActionEvent event) -> {
                try {
					controller.handleButtonClick(event);
				} catch (OutOfTurnException e) {
					System.out.println("It is not your turn... Be patient");
				}
            });
            if(playerSide.equals("enemy")){
                button.getStyleClass().add("enemy-button");
            }
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
