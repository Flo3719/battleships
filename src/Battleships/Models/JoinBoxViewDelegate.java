package Battleships.Models;

import Battleships.Controllers.ClientController;
import Battleships.Views.JoinBoxView;

public interface JoinBoxViewDelegate {
    public static JoinBoxViewDelegate sharedInstance = null;
    public void setView(JoinBoxView view);
    public void handleHostClick(String name,String port);
    public void handleJoinClick(String name,String ip,String port, Board board, ClientController clientController);
    public void showMessage(String message);
}
