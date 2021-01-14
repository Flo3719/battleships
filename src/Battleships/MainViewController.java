package Battleships;

import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Control;

public class MainViewController implements MainViewDelegate{
    protected MainView view;

    //Singleton
    public static MainViewDelegate sharedInstance = new MainViewController();
    private MainViewController(){}

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
}
