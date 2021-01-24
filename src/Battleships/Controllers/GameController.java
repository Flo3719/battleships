package Battleships.Controllers;

import java.io.IOException;

import Battleships.Models.GameModel;
import javafx.application.Platform;

public class GameController {
    public GameModel model;
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
        try {
			server.sendTurnIndicator(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //System.out.println("GAME: game was created with player " + model.getPlayer(0).getName() + " & " + model.getPlayer(1).getName());
    }
}
