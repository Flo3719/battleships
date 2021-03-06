package Battleships.Tests;

import Battleships.Models.Board;
import Battleships.Models.ShipModel;
import Battleships.Models.ShipType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class BoardTest {
    @Test
    public void testAddShips(){
        Board board = new Board();
        // addShips() is called in the board constructor
        assertEquals(28, board.getShips().size());
    }

    @Test
    public void testPositionShips(){
        Board board = new Board();
        board.positionShips();
        for(ShipModel ship : board.getShips()){
            if(ship.getPositions().isEmpty()){
                fail("All ships should be positioned.");
            }
            assertTrue(ship.getShipName().contains(ship.getShipType().identifier));
        }
    }

    @Test
    public void testIsFreeField(){
        Board board = new Board();
        assertTrue(board.isFreeField(0, 0));
        assertTrue(board.isFreeField(14, 9));
        assertTrue(board.isFreeField(3, 1));
        assertTrue(board.isFreeField(2, 1));
    }

    @Test
    public void testPlaceEast(){
        Board board = new Board();
        board.placeEast(new ShipModel(ShipType.BATTLESHIP, 0), 0, 0);
        board.placeEast(new ShipModel(ShipType.CARRIER, 0), 0, 2);
        assertFalse(board.isFreeField(0, 0));
        assertTrue(board.isFreeField(14, 9));
        assertTrue(board.isFreeField(3, 1));
        assertFalse(board.isFreeField(1, 2));
    }

    @Test
    public void testPlaceSouth(){
        Board board = new Board();
        board.placeSouth(new ShipModel(ShipType.BATTLESHIP, 0), 0, 0);
        board.placeSouth(new ShipModel(ShipType.CARRIER, 0), 2, 0);
        assertFalse(board.isFreeField(0, 0));
        assertTrue(board.isFreeField(14, 9));
        assertTrue(board.isFreeField(3, 1));
        assertFalse(board.isFreeField(0, 2));
        assertFalse(board.isFreeField(2, 1));
    }

    @Test
    public void testPlaceNorth(){
        Board board = new Board();
        board.placeNorth(new ShipModel(ShipType.BATTLESHIP, 0), 0, 3);
        board.placeNorth(new ShipModel(ShipType.CARRIER, 0), 2, 4);
        assertFalse(board.isFreeField(0, 0));
        assertTrue(board.isFreeField(14, 9));
        assertTrue(board.isFreeField(3, 1));
        assertFalse(board.isFreeField(0, 2));
        assertFalse(board.isFreeField(2, 1));
    }

    @Test
    public void testPlaceWest(){
        Board board = new Board();
        board.placeWest(new ShipModel(ShipType.BATTLESHIP, 0), 3, 0);
        board.placeWest(new ShipModel(ShipType.CARRIER, 0), 4, 2);
        assertFalse(board.isFreeField(0, 0));
        assertTrue(board.isFreeField(14, 9));
        assertTrue(board.isFreeField(3, 1));
        assertFalse(board.isFreeField(1, 2));
    }
}
