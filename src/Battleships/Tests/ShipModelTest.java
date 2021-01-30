package Battleships.Tests;

import Battleships.Models.ShipModel;
import Battleships.Models.ShipType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShipModelTest {
    @BeforeEach
    public void init(){

    }

    @Test
    public void testGetOrientation(){
        ShipModel ship = new ShipModel(ShipType.BATTLESHIP, 0);
        ship.getPositions();
    }
}
