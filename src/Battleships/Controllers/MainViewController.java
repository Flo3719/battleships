package Battleships.Controllers;

import Battleships.Models.MainViewDelegate;

import java.io.IOException;

import Battleships.Models.Board;
import Battleships.Models.PositionModel;
import Battleships.Models.Exceptions.ServerNotAvailableException;
import Battleships.Views.JoinBoxView;
import Battleships.Views.MainView;
import javafx.event.Event;
import javafx.scene.control.Control;

public class MainViewController implements MainViewDelegate {
    protected MainView view;
    private Board board;
    private ClientController clientController;
    public JoinBoxView joinBoxView;
    
    //Singleton
    public static MainViewDelegate sharedInstance = new MainViewController();
    private MainViewController(){
        this.board = new Board();
        this.clientController = new ClientController(this);
    }

    // To Manipulate the game implement "MainViewController.sharedInstance" for example by creating an instance variable
    // of type "MainViewDelegate" called "controller" and assign it to it. (For example like in "MainView.java")
    // To make manipulations that directly involve the view, use the .view attribute of the sharedInstance.

    @Override
    public MainView getView() {
        return this.view;
    }

    @Override
    public void initialize(MainView view) {
        this.view = view;
        //TODO: fix this. the ships should not be placed before joining/hosting.Maybe separate setView method again?
        this.board.positionShips();
        this.addShips(this.board);
        //Regarding Requirement C02 (launch it on initialization)
        handleMenuJoinClick();
        //handleJoinClick(event);
    }

    @Override
    public void handleButtonClick(Event evt) {
        System.out.println(((Control)evt.getSource()).getId());
        String button[];
        button = ((Control)evt.getSource()).getId().split("PlayButton");
        if(button[0].equals("enemy")){
            //tryAttack(button[1]);
            System.out.println("enemy field attacked");
        }
    }

    @Override
    public void handleStartClick(Event evt) throws IOException, ServerNotAvailableException{
    	this.clientController.startGame();
    }

    public void handleMenuJoinClick() {
        this.joinBoxView = new JoinBoxView(this.clientController);
        joinBoxView.display(this.board);
        
//        this.view.setEnemyScoreLabel(2);
//        this.view.setFriendScoreLabel(3);
//        this.view.setTimeLabel(1038);
//        this.view.setFriendNameLabel("Florian");
//        this.view.setEnemyNameLabel("Koen");
        try{
            //this.view.setPlayField('X', "friend", 10);
        // TODO create custom exception!
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private int getIndex(int col, int row){
        return row*15+col;
    }

    public Board getBoard(){
        return this.board;
    }

    public void addShips(Board board){
        for(int y = 0; y < board.HEIGHT; y++){
            for(int x = 0; x < board.WIDTH; x++){
                PositionModel pos = board.positions[x][y];
                if(pos.ship != null) {
                    switch (pos.ship.shipType) {
                        case PATROL_BOAT:
                            //TODO implement enum for indicators + add to the setPlayField funtion that it changes the color according to indicator/as param
                            //TODO add numbers to the indicator P1, C1, C2, etc
                            this.view.setPlayField(pos.ship.shipName, "friend", getIndex(x, y), pos.ship.shipType.color);
                            break;
                        case SUPER_PATROL:
                            this.view.setPlayField(pos.ship.shipName, "friend", getIndex(x, y), pos.ship.shipType.color);
                            break;
                        case DESTROYER:
                            this.view.setPlayField(pos.ship.shipName, "friend", getIndex(x, y), pos.ship.shipType.color);
                            break;
                        case BATTLESHIP:
                            this.view.setPlayField(pos.ship.shipName, "friend", getIndex(x, y), pos.ship.shipType.color);
                            break;
                        case CARRIER:
                            this.view.setPlayField(pos.ship.shipName, "friend", getIndex(x, y), pos.ship.shipType.color);
                            break;
                    }
                }
            }
        }
    }
}
