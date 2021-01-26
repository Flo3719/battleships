package Battleships.Controllers;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import Battleships.Models.ProtocolMessages;

public class GameTimer implements Runnable {
    private int gameTime;
    private Server server;
    private GameController gameController;
    @Override
    public void run() {
        for(int i = gameTime; i > -1; i--){
            try {
                TimeUnit.SECONDS.sleep(1);
                server.sendTimeUpdate(i, gameController);
                if (i == 0) {
                	GameClientHandler winner = gameController.model.checkIfWonOnScore();
                	String msg = ProtocolMessages.WON + ProtocolMessages.CS + winner.getName();
                	server.SendTogameClients(msg, gameController);
                	this.gameController.model.getPlayer(0).shutdown();
                	this.gameController.model.getPlayer(1).shutdown();
                }
//                System.out.println("TIMER: " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    public GameTimer(Server server, int gameTime, GameController gameController){
        this.gameTime = gameTime;
        this.server = server;
        this.gameController = gameController;
    }
}
