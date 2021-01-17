package Battleships;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server implements Runnable {

    /** The ServerSocket of this HotelServer */
    private ServerSocket ssock;

    /** List of HotelClientHandlers, one for each connected client */
    //private List<HotelClientHandler> clients;

    /** Next client number, increasing for every new connection */
    //private int next_client_no;

    /** The view of this HotelServer */
    private JoinBoxViewController view;

    public Server(JoinBoxViewController view){
        this.view = view;
    }

    @Override
    public void run() {
//        boolean openNewSocket = true;
//        while (openNewSocket) {
//            try {
//                // Sets up the hotel application
//                //setup();
//
//                while (true) {
//                    Socket sock = ssock.accept();
//                    String name = "Client "
//                            + String.format("%02d", next_client_no++);
//                    view.showMessage("New client [" + name + "] connected!");
//                    HotelClientHandler handler =
//                            new HotelClientHandler(sock, this, name);
//                    new Thread(handler).start();
//                    clients.add(handler);
//                }
//
//            } catch (ExitProgram e1) {
//                // If setup() throws an ExitProgram exception,
//                // stop the program.
//                openNewSocket = false;
//            } catch (IOException e) {
//                System.out.println("A server IO error occurred: "
//                        + e.getMessage());
//
//                if (!view.getBoolean("Do you want to open a new socket?")) {
//                    openNewSocket = false;
//                }
//            }
//        }
//        view.showMessage("See you later!");
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
