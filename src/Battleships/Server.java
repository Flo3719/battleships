package Battleships;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server implements Runnable {

    /** The ServerSocket of this HotelServer */
    private ServerSocket ssock;

    /** List of HotelClientHandlers, one for each connected client */
    private List<GameClientHandler> clients;

    /** Next client number, increasing for every new connection */
    private int next_client_no;

    /** The view of this HotelServer */
    private JoinBoxViewController view;
    
    public Server(JoinBoxViewController view){
        this.view = view;
    }

    @Override
    public void run() {
        boolean openNewSocket = true;
        clients = new ArrayList<>();
        while (openNewSocket) {
            try {
//                // Sets up the hotel application
//                //setup();
//
                while (true) {
                    Socket sock = ssock.accept();
                    String name = "Client "
                            + String.format("%02d", next_client_no++);
                    //String name = clients.get(next_client_no).getName();
                    view.showMessage("New client [" + name + "] connected!");
                    GameClientHandler handler =
                            new GameClientHandler(sock, this, name);
                    clients.add(handler);
                    System.out.println(name + ": " + handler.getName());
                    System.out.println(clients.size());
                    if (clients.size() == 2) {
                    	
                    }
                    new Thread(handler).start();
                }
                //TODO create suitable exception
            } catch (Exception e) {
                // If setup() throws an ExitProgram exception,
                // stop the program.
                openNewSocket = false;
//            } catch (IOException e) {
//                System.out.println("A server IO error occurred: "
//                        + e.getMessage());

//                if (!view.getBoolean("Do you want to open a new socket?")) {
//                    openNewSocket = false;
 //               }
            }
        }
 //       view.showMessage("See you later!");
    }

    public void setup(int port) {
        ssock = null;
        while (ssock == null) {
            // try to open a new ServerSocket
            try {
                view.showMessage("Attempting to open a socket at 127.0.0.1 "
                        + "on port " + port + "...");
                ssock = new ServerSocket(port, 0,
                        InetAddress.getByName("127.0.0.1"));
                view.showMessage("Server started at port " + port);
            } catch (IOException e) {
                view.showMessage("ERROR: could not create a socket on "
                        + "127.0.0.1" + " and port " + port + ".");
            }
        }
    }

    // The following Methods might be better placed in the gameClientHandler if
    // we decide to use it.

    //@Override
    public String getHello(String name) {
        System.out.println("Hello " + name + "!");
        return ProtocolMessages.HELLO + ProtocolMessages.DELIMITER + name;
    }

    public boolean checkPlayerName(String name){
        //TODO implement Requirement S02
        return true;
    }

    public void startGame(){
        //TODO implement requirement S03
    }

    public void getMove(){
        //TODO implement requirement S04
    }

    public void notifyWinner(){
        //TODO implement requirement S05
    }

    public void notifyTimesUp(){
        //TODO implement requirement S06
    }

}
