package Battleships.Controllers;

import Battleships.Models.Exceptions.ServerNotAvailableException;
import Battleships.Models.ProtocolMessages;
import javafx.application.Platform;

public class ClientTimeHandler implements Runnable{
    private ClientController clientController;

    @Override
    public void run() {
        while(true){
            try {
                String message = clientController.readLineFromServer();
                if(message.contains(ProtocolMessages.TIME)){
                    String msg[] = message.split(ProtocolMessages.CS);
                    System.out.println(message);
                    if(clientController.readLineFromServer() != ""){
                        clientController.getMainViewController().view.setTimeLabel(Integer.parseInt(msg[1]));
                    }
                }
            } catch (ServerNotAvailableException e) {
                e.printStackTrace();
            } catch (NumberFormatException e){
                e.printStackTrace();
            }
        }

    }
    public ClientTimeHandler(ClientController clientController){
        this.clientController = clientController;
    }
}
