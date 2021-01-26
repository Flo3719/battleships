package Battleships.Controllers;

import java.util.concurrent.TimeUnit;

public class GameTimer implements Runnable {
    private int gameTime;
    private Server server;
    @Override
    public void run() {
        for(int i = gameTime; i == 0; i--){
            try {
                TimeUnit.SECONDS.sleep(1);
                server.sendTimeUpdate(i);
//                System.out.println("TIMER: " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    public GameTimer(Server server, int gameTime){
        this.gameTime = gameTime;
        this.server = server;
    }
}
