package Battleships;

import Battleships.Controllers.ClientController;
import Battleships.Controllers.MainViewController;
import Battleships.Models.Board;
import Battleships.Models.Exceptions.OutOfTurnException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComputerPlayer {
    //MainViewController mainViewController;
    private int lastHit;
    private List<Integer> hittedFields = new ArrayList<>();

    public ComputerPlayer() {
        //mainViewController = (MainViewController) MainViewController.sharedInstance;
        for(int i = 0; i<150; i++){
            hittedFields.add(i);
        }
    }
    public String makeTurn() {
        int hit = (int)(Math.random()*hittedFields.size())-1;
        String index = String.valueOf(hittedFields.get(hit));
        hittedFields.remove(hit);
        return index;
    }
    public void setLastHit(int index){
        this.lastHit = lastHit;
    }
}
