package Battleships;

import java.io.*;
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

    private BufferedReader in;
    private BufferedWriter out;

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
                while (true) {
                    Socket sock = ssock.accept();

                    in = new BufferedReader(
                            new InputStreamReader(sock.getInputStream()));
                    out = new BufferedWriter(
                            new OutputStreamWriter(sock.getOutputStream()));

                    String msg = in.readLine();
                    String name = msg.split(ProtocolMessages.DELIMITER)[1];

                    out.write(handshakeMessage(name));
                    out.newLine();
                    out.flush();

                    if(nameAvailable(name)){
                        view.showMessage("SERVER: New client " + name +  " connected!");
                        GameClientHandler handler =
                                new GameClientHandler(sock, this, name);
                        clients.add(handler);
                        System.out.println("SERVER: " + clients.size());
                        new Thread(handler).start();
                        //TODO TBD
                        if(clients.size() == 2){
                            Game game = new Game(clients.get(0), clients.get(1));
                        }
                        System.out.println("SERVER: NAME AVAILABLE");
                    }else{
                        System.out.println("SERVER: NAME TAKEN");
                    }
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
                view.showMessage("SERVER: Attempting to open a socket at 127.0.0.1 "
                        + "on port " + port + "...");
                ssock = new ServerSocket(port, 0,
                        InetAddress.getByName("127.0.0.1"));
                view.showMessage("SERVER: Server started at port " + port);
            } catch (IOException e) {
                view.showMessage("ERROR: could not create a socket on "
                        + "127.0.0.1" + " and port " + port + ".");
            }
        }
    }

    // The following Methods might be better placed in the gameClientHandler if
    // we decide to use it.

    //@Override
    public String handshakeMessage(String name) {
        return ProtocolMessages.HELLO + ProtocolMessages.DELIMITER + name;
    }

    public boolean nameAvailable(String name){
        for(GameClientHandler gch : clients){
            if(gch.getName().equals(name)){
                return false;
            }
        }
        return true;
    }

    public List<GameClientHandler> getClients(){
        return clients;
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
