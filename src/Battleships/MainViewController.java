package Battleships;

import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Control;

import java.util.ArrayList;

public class MainViewController implements MainViewDelegate{
    protected MainView view;

    Board board = new Board();

    //Singleton
    public static MainViewDelegate sharedInstance = new MainViewController();
    private MainViewController(){
    }

    // To Manipulate the game implement "MainViewController.sharedInstance" for example by creating an instance variable
    // of type "MainViewDelegate" called "controller" and assign it to it. (For example like in "MainView.java")
    // To make manipulations that directly involve the view, use the .view attribute of the sharedInstance.

    @Override
    public MainView getView() {
        return this.view;
    }

    @Override
    public void setView(MainView view) {
        this.view = view;
        this.board.positionShips();
        this.addShips(this.board);
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
    public void handleJoinClick(Event evt) {
        JoinBoxView joinBoxView = new JoinBoxView();
        joinBoxView.display();
        this.view.setEnemyScoreLabel(2);
        this.view.setFriendScoreLabel(3);
        this.view.setTimeLabel(1038);
        this.view.setFriendNameLabel("Florian");
        this.view.setEnemyNameLabel("Koen");
        try{
            this.view.setPlayField('X', "friend", 10);
            //TODO create custom exception!
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private int getIndex(int col, int row){
        return row*15+col;
    }

    public void addShips(Board board){
        for(int y = 0; y < board.HEIGHT-1; y++){
            for(int x = 0; x < board.WIDTH-1; x++){
                PositionModel pos = board.positions[x][y];
                if(pos.ship != null) {
                    switch (pos.ship.shipType) {
                        case PATROL_BOAT:
                            System.out.print("P");
                            this.view.setPlayField('P', "friend", getIndex(x, y));
                            break;
                        case SUPER_PATROL:
                            System.out.print("S");
                            this.view.setPlayField('S', "friend", getIndex(x, y));
                            break;
                        case DESTROYER:
                            System.out.print("D");
                            this.view.setPlayField('D', "friend", getIndex(x, y));
                            break;
                        case BATTLESHIP:
                            System.out.print("B");
                            this.view.setPlayField('B', "friend", getIndex(x, y));
                            break;
                        case CARRIER:
                            System.out.print("C");
                            this.view.setPlayField('C', "friend", getIndex(x, y));
                            break;
                    }
                }
                else {
                    System.out.print(" ");
                }
            }
        }
    }
}
