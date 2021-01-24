package Battleships.Controllers;

import Battleships.Models.GameModel;

public class GameController {
    private GameModel model;

    public GameController(GameClientHandler s0, GameClientHandler s1){
        this.model = new GameModel(s0, s1);
        s0.setGame(this);
        s1.setGame(this);
    }

    public void startGame(){

        System.out.println("GAME: game was created with player " + model.getPlayer(0).getName() + " & " + model.getPlayer(1).getName());
    }
}
