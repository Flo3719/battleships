package Battleships.Models.Exceptions;

public class ServerNotAvailableException extends Exception{
    public ServerNotAvailableException(String msg){
        super(msg);
    }
}
