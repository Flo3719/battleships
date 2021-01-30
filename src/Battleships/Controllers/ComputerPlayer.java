package Battleships.Controllers;

import Battleships.Controllers.ClientController;
import Battleships.Controllers.MainViewController;
import Battleships.Models.Board;
import Battleships.Models.Exceptions.OutOfTurnException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComputerPlayer {
    //MainViewController mainViewController;
    private List<Integer> unhittedFields = new ArrayList<>();
    private List<Integer> hits = new ArrayList<>();

    public ComputerPlayer() {
        //mainViewController = (MainViewController) MainViewController.sharedInstance;
        for(int i = 0; i<150; i++){
            unhittedFields.add(i);
        }
    }
    public String makeNaiveTurn() {
        int index = (int)(Math.random()*unhittedFields.size());
        System.out.println(index);
        String hit = String.valueOf(unhittedFields.get(index));
        unhittedFields.remove(index);
        //return "15";
        return hit;
    }
    public void setLastHit(String index){
        this.hits.add(Integer.parseInt(index));
        System.out.println("size of the hitslist: " + hits.size());
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
    	System.out.println(unhittedFields.size());
        if(hits.isEmpty()){
            return makeNaiveTurn();
        }
        else {
        int someHit = hits.get((int)(Math.random()*hits.size()));
        int row = someHit/15;
        int col = someHit%15;

        if((row+1)<15 && containsValue(Integer.valueOf((row*15) + col))){
            System.out.println("FIRST CASE");
            unhittedFields.remove(unhittedFields.indexOf(Integer.valueOf((row*15) + col)));
            return String.valueOf((row*15) + col);
        }
        if((row-1)>0 && containsValue(Integer.valueOf((row*15) + col))){
            System.out.println("SECOND CASE");
            unhittedFields.remove(unhittedFields.indexOf(Integer.valueOf((row*15) + col)));
            return String.valueOf((row*15) + col);
        }
        if((col-1)>0 && containsValue(Integer.valueOf((row*15) + col))){
            System.out.println("THIRD CASE");
            unhittedFields.remove(unhittedFields.indexOf(Integer.valueOf((row*15) + col)));
            return String.valueOf((row*15) + col);
        }
        if((col+1)<10 && containsValue(Integer.valueOf((row*15) + col))){
            System.out.println("FOURTH CASE");
            unhittedFields.remove(unhittedFields.indexOf(Integer.valueOf((row*15) + col)));
            return String.valueOf((row*15) + col);
        } else {
        System.out.println("LAST CASE");
        return makeNaiveTurn();
        }
        
        }
    }
}











