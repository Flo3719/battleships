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
    private List<Integer> unhittedFields = new ArrayList<>();

    public ComputerPlayer() {
        //mainViewController = (MainViewController) MainViewController.sharedInstance;
        for(int i = 0; i<150; i++){
            unhittedFields.add(i);
        }
    }
    public String makeNaiveTurn() {
        int index = (int)(Math.random()*unhittedFields.size());
        String hit = String.valueOf(unhittedFields.get(index));
        unhittedFields.remove(index);
        //return "15";
        return hit;
    }
    public void setLastHit(String index){
        this.lastHit = Integer.parseInt(index);
    }

    public boolean containsValue(Integer integer){
        for(Integer iterator : unhittedFields){
            if(iterator.equals(integer)){
                return true;
            }
        }
        return false;
    }

    public String makeTurn(){
        if(lastHit == 0){
            return makeNaiveTurn();
        }
        int row = lastHit/10;
        int col = lastHit%10;

        if((row+1)<15 && containsValue(Integer.valueOf((row+1)*col))){
            System.out.println("FIRST CASE");
            unhittedFields.remove(unhittedFields.indexOf(Integer.valueOf((row+1)*col)));
            return String.valueOf((row+1)*col);
        }
        if((row-1)>0 && containsValue(Integer.valueOf((row-1)*col))){
            System.out.println("SECOND CASE");
            unhittedFields.remove(unhittedFields.indexOf(Integer.valueOf((row-1)*col)));
            return String.valueOf((row+1)*col);
        }
        if((col-1)>0 && containsValue(Integer.valueOf((col-1)*row))){
            System.out.println("THIRD CASE");
            unhittedFields.remove(unhittedFields.indexOf(Integer.valueOf((col-1)*row)));
            return String.valueOf((col+1)*row);
        }
        if((col+1)<10 && containsValue(Integer.valueOf((col+1)*row))){
            System.out.println("FOURTH CASE");
            unhittedFields.remove(unhittedFields.indexOf(Integer.valueOf((col+1)*row)));
            return String.valueOf((col+1)*row);
        }
        System.out.println("LAST CASE");
        return makeNaiveTurn();
    }
}











