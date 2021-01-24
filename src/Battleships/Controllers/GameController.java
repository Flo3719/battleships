package Battleships.Controllers;

import Battleships.Models.GameModel;
import javafx.application.Platform;

public class GameController {
    private GameModel model;
    private GameTimer gameTimer;
    private Server server;

    public GameController(Server server, GameClientHandler s0, GameClientHandler s1){
        this.model = new GameModel(s0, s1);
        s0.setGame(this);
        s1.setGame(this);
        this.server = server;
    }

    public void startGame(){
        gameTimer = new GameTimer(server, model.getMaximumGameTime());
        Thread gameTimerThread = new Thread(gameTimer);
        gameTimerThread.start();
        //System.out.println("GAME: game was created with player " + model.getPlayer(0).getName() + " & " + model.getPlayer(1).getName());
    }
}
