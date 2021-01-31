package Battleships.Tests;

import Battleships.Models.PositionModel;
import Battleships.Models.ShipModel;
import Battleships.Models.ShipType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShipModelTest {
    ShipModel ship;
    @BeforeEach
    public void init(){
        ship = new ShipModel(ShipType.BATTLESHIP, 0);

    }

    @Test
    public void testGetOrientation(){
        List<PositionModel> noPositions = new ArrayList<>();
        assertEquals(noPositions, ship.getPositions());
        PositionModel posA = new PositionModel(0,0);
        PositionModel posB = new PositionModel(1,0);
        PositionModel posC = new PositionModel(2,0);
        PositionModel posD = new PositionModel(3,0);
        ship.addPosition(posA);
        ship.addPosition(posB);
        ship.addPosition(posC);
        ship.addPosition(posD);
        List<PositionModel> positions = new ArrayList<>();
        positions.add(posA);
        positions.add(posB);
        positions.add(posC);
        positions.add(posD);
        assertEquals(ship.getPositions(), positions);
    }

    @Test
    public void testGetMarkerIndex(){
        PositionModel posA = new PositionModel(0,0);
        PositionModel posB = new PositionModel(1,0);
        PositionModel posC = new PositionModel(2,0);
        PositionModel posD = new PositionModel(3,0);
        ship.addPosition(posA);
        ship.addPosition(posB);
        ship.addPosition(posC);
        ship.addPosition(posD);
        assertEquals(0, ship.getMarkerIndex());
    }

    @Test
    public void testSunk(){
        PositionModel posA = new PositionModel(0,0);
        PositionModel posB = new PositionModel(1,0);
        PositionModel posC = new PositionModel(2,0);
        PositionModel posD = new PositionModel(3,0);
        ship.addPosition(posA);
        ship.addPosition(posB);
        ship.addPosition(posC);
        ship.addPosition(posD);
        assertFalse(ship.Sunk());
        for(PositionModel pos : ship.getPositions()){
            pos.setHasBeenGuessed(true);
        }
        assertTrue(ship.Sunk());
    }
}
