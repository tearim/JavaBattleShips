package JavaBattleShips.logic;

import java.util.ArrayList;
import java.util.Random;

public class StandardFleet {
    private final ArrayList<Ship> ships;

    public StandardFleet() {
        this.ships = new ArrayList<>();
    }

    public void issueNormal() {
        Random rand = new Random();
        ships.clear();

        ships.add( new Ship(1, true) );
        ships.add( new Ship(1, true) );
        ships.add( new Ship(1, true) );
        ships.add( new Ship(1, true) );

        // med
        ships.add( new Ship(2, rand.nextBoolean()) );
        ships.add( new Ship(2, rand.nextBoolean()) );
        ships.add( new Ship(2, rand.nextBoolean()) );

        // large
        ships.add( new Ship(3, rand.nextBoolean()) );
        ships.add( new Ship(3, rand.nextBoolean()) );

        // byg
        ships.add( new Ship(4, rand.nextBoolean()) );

    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    private boolean requestReDoPlacement =  false;
    public void randomPlacementOnABoard(Board board) {
        Random rand = new Random();
        ships.stream().forEach( ship -> {
            int attempts = 0;
            boolean success = false;
            do {
                attempts++;
                success = board.placeShip(ship, rand.nextInt(board.getWidth()), rand.nextInt(board.getHeight()));
                if ( attempts >= 100 ) {
                    // in rare cases ships cannot be placed.
                    requestReDoPlacement = true;
                    success = true;
                    board.clear();
                }
            } while (!success);
        });
        if (requestReDoPlacement) {
            requestReDoPlacement = false;
            board.clear();
            randomPlacementOnABoard(board);
        }

    }
}
