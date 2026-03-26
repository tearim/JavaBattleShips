package JavaBattleShips.logic;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.IntUnaryOperator;

public class AIPlayer extends Player {
    private Board enemyBoard;
    Random rand = new Random();
    int move = 0;
    boolean wakeUp = false;

    private final int EASY = 0;
    private final int NORMAL = 1;
    private final int HARD = 2;
    private final int VERY_HARD = 3;

    private final int EAST = 0;
    private final int NORTH = 1;
    private final int WEST = 2;
    private final int SOUTH = 3;
    private int hardnessSetting = NORMAL;
    private boolean bigShipWasKilled = false;
    private int threeLinerKilled = 0;

    private GameLogic  gameLogic;
    private int lastFireAtX  = 0;
    private int lastFireAtY  = 0;

    private void _debug(int y, String s) {
        gameLogic.debug(y,s);
    }

    public AIPlayer(String setting, GameLogic gameLogic) {
        this.gameLogic = gameLogic;
        this.hardnessSetting =  switch(setting.toLowerCase()) {
           case "easy" -> EASY;
           case "normal" -> NORMAL;
           case  "hard" -> HARD;
           case  "hard+" -> VERY_HARD;
           default -> NORMAL;
        };
    }
    public void uploadBoard(Board board) {
        enemyBoard = board;
    }

    private boolean[] availableDirections(int x, int y) {
        boolean[] result = new boolean[4];
        result[EAST] =  ( x + 1 < enemyBoard.getWidth() ) && ( enemyBoard.cell(x + 1, y) == Board.SEA  || enemyBoard.cell(x + 1, y ) >= Board.SHIP );
        result[WEST] =  ( x - 1 >= 0 ) && ( enemyBoard.cell(x - 1, y) == Board.SEA  || enemyBoard.cell(x - 1, y ) >= Board.SHIP );
        result[SOUTH] = ( y + 1 < enemyBoard.getWidth() ) && ( enemyBoard.cell(x, y + 1) == Board.SEA || enemyBoard.cell(x, y  + 1) >= Board.SHIP );
        result[NORTH] = ( y - 1 >= 0 ) && ( enemyBoard.cell(x , y - 1) == Board.SEA || enemyBoard.cell(x, y - 1) >= Board.SHIP  );
        return result;
    }
    private boolean anyAvailableDirections(boolean[] directions) {
        return directions[EAST] || directions[NORTH] ||directions[WEST] ||directions[SOUTH] ;
    }

    private boolean noAvailableDirections(boolean[] directions) {
        return !(directions[EAST] || directions[WEST] || directions[SOUTH] || directions[NORTH]);
    }

    public boolean getLastMoveSuccessful() {
        return lastHitSuccess;
    }
    private class Coords {
        int x;
        int y;
        public Coords(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    private boolean targetAcquired = false;

    private final ArrayList<Coords> currentShipCoords = new ArrayList<>();
    private boolean lastHitSuccess = false;

    private void copy_2darray(int[][] what, int[][] destination) {
        for (int i = 0; i < what.length; i++) {
            for (int j = 0; j < what[i].length; j++) {
                destination[i][j] = what[i][j];
            }
        }
    }

    private void foreach(int[][] what, IntUnaryOperator operator) {
        for (int i = 0; i < what.length; i++) {
            for (int j = 0; j < what[i].length; j++) {
                what[i][j] = operator.applyAsInt(what[i][j]);
            }
        }
    }
    private static int VERY_SMOL_NUMBER = -10000;
    private Coords dreamFind() {
        int[][] field = new int[enemyBoard.getHeight()][enemyBoard.getWidth()];
        copy_2darray(enemyBoard.getBoard(), field);
        foreach(field, val -> {
            return switch (val) {
                case Board.FIRE -> VERY_SMOL_NUMBER ;
                case Board.DEBRIS -> VERY_SMOL_NUMBER ;
                case Board.SHIP, Board.SHIP_BIG, Board.SHIP_MED, Board.SHIP_SMOL, Board.SHIP_TINY -> 0;
                default -> 0;
            };
        });
        int findingDreamOf = (bigShipWasKilled) ? 3 : 4;
        for (int i = 0; i < enemyBoard.getHeight() - findingDreamOf -1 ; i++) {
            for (int j = 0; j <  enemyBoard.getHeight() - 1; j++) {
                // ORDER OF (j, i) is reversed, as now we need to get x, y properly.
                boolean addToDream = true;
                if ( i + findingDreamOf < enemyBoard.getWidth() ) {
                    for (int k = 0; k < findingDreamOf - 1; k++) {
                        if (field[j][i + k] == VERY_SMOL_NUMBER) {
                            addToDream = false;
                            break;
                        }
                    }
                    if (addToDream) {
                        for (int k = 0; k < findingDreamOf - 1; k++) {
                            field[j][i + k] += 1;
                        }
                    }
                }


                addToDream = true;
                if ( j + findingDreamOf < enemyBoard.getHeight() ) {
                    for (int k = 0; k < findingDreamOf - 1; k++) {
                        if (field[j + k][i] == VERY_SMOL_NUMBER) {
                            addToDream = false;
                            break;
                        }
                    }
                    if (addToDream) {
                        for (int k = 0; k < findingDreamOf - 1; k++) {
                            field[j + k][i] += 1;
                        }
                    }
                }
            }
        }
        int maxXat = -1, maxYat = -1;
        int currentBiggest = -1;
        Random rand = new Random();
        boolean earliest = rand.nextBoolean();
        for (int i = 0; i < enemyBoard.getHeight(); i++) {
            for (int j = 0; j < enemyBoard.getHeight(); j++) {
                if ( earliest) {
                    if (field[i][j] > currentBiggest) {
                        maxXat = j;
                        maxYat = i;
                        currentBiggest = field[i][j];
                    }
                } else {
                    if (field[i][j] >= currentBiggest) {
                        maxXat = j;
                        maxYat = i;
                        currentBiggest = field[i][j];
                    }
                }
            }
        }
        if ( currentBiggest == 0 ) {
            if ( findingDreamOf == 3 || ( findingDreamOf == 4 && threeLinerKilled > 1  ) ) wakeUp = true;
            do {
                maxXat = rand.nextInt(enemyBoard.getWidth());
                maxYat = rand.nextInt(enemyBoard.getHeight());
            } while (enemyBoard.cell(maxXat, maxYat) == Board.DEBRIS || enemyBoard.cell(maxXat, maxYat) == Board.FIRE);
        }
        return new Coords(maxXat, maxYat);
    }
    private void fireAtRandom() {
        int randomX = -1, randomY = -1;
        if (    !bigShipWasKilled &&
                (threeLinerKilled < 2 ) &&
                !wakeUp &&
                hardnessSetting > HARD &&
                (move > 0) ) {
            Coords random = dreamFind();
            randomX = random.x;
            randomY = random.y;
        } else {
            do {
                randomX = rand.nextInt(enemyBoard.getWidth());
                randomY = rand.nextInt(enemyBoard.getHeight());
            } while (enemyBoard.cell(randomX, randomY) == Board.DEBRIS || enemyBoard.cell(randomX, randomY) == Board.FIRE);
        }

        lastFireAtX =  randomX;
        lastFireAtY =  randomY;

        if ( enemyBoard.cell(randomX, randomY) >= Board.SHIP ) {
            enemyBoard.cell(randomX, randomY, Board.FIRE);
            registerTarget(randomX, randomY);
            registerPointlessMovesOf(randomX, randomY);
            lastHitSuccess = true;
        } else {
            enemyBoard.cell(randomX, randomY, Board.DEBRIS);
            lastHitSuccess = false;
        }
    }
    private boolean fireAtTarget(int firingAtX, int firingAtY, int direction) {
        switch (direction) {
            case EAST:  firingAtX++; break;
            case WEST:  firingAtX--; break;
            case NORTH: firingAtY--; break;
            case SOUTH: firingAtY++; break;
        }
        lastFireAtX =  firingAtX;
        lastFireAtY =  firingAtY;
        if ( enemyBoard.cell(firingAtX, firingAtY) >= Board.SHIP ) {
            enemyBoard.cell(firingAtX, firingAtY, Board.FIRE);
            //registerTarget(firingAtX, firingAtY);
            currentShipCoords.add(new Coords(firingAtX, firingAtY));
            registerPointlessMovesOf(currentShipCoords);
            lastHitSuccess = true;
        } else {
            enemyBoard.cell(firingAtX, firingAtY, Board.DEBRIS);
            lastHitSuccess = false;
        }
        return false;
    }
    private void registerPointlessMovesOf(int x, int y) {
        enemyBoard.cell(x+1, y+1, Board.DEBRIS);
        enemyBoard.cell(x-1, y+1, Board.DEBRIS);
        enemyBoard.cell(x+1, y-1, Board.DEBRIS);
        enemyBoard.cell(x-1, y-1, Board.DEBRIS);
    }

    private boolean isTargetHorizontal(ArrayList<Coords> shipBody) {
        // will throw an exception is used on small ships, that's expected.
        return  shipBody.get(0).y == shipBody.get(1).y;
    }
    private void registerPointlessMovesOf(ArrayList<Coords> shipBody) {
        boolean forHorizontal = false;
        if ( shipBody.size() > 1 ) {
            forHorizontal = isTargetHorizontal(shipBody);
        } else {
            registerPointlessMovesOf(shipBody.getFirst().x, shipBody.getFirst().y);
            return;
        }

        for ( Coords coords : shipBody ) {
            if ( forHorizontal ) {
                enemyBoard.cell(coords.x, coords.y+1, Board.DEBRIS);
                enemyBoard.cell(coords.x, coords.y-1, Board.DEBRIS);
            } else {
                enemyBoard.cell(coords.x + 1, coords.y, Board.DEBRIS);
                enemyBoard.cell(coords.x - 1, coords.y, Board.DEBRIS);
            }
            registerPointlessMovesOf(coords.x, coords.y);
        }
    }
    private void registerTarget(int x, int y) {
        currentShipCoords.clear();
        currentShipCoords.add(new Coords(x, y));
        targetAcquired = true;
    }
    private void releaseTarget() {
        targetAcquired = false;
        currentShipCoords.clear();
    }

    private Coords getTargetNWCoords(ArrayList<Coords> shipBody) {
        Coords targetCoords = new Coords(shipBody.getFirst().x, shipBody.getFirst().y);
        for ( int i = 1; i < shipBody.size(); i++ ) {
            if ( shipBody.get(i).y < targetCoords.y ) {
                targetCoords = shipBody.get(i);
            }
            if( shipBody.get(i).x < targetCoords.x ) {
                targetCoords = shipBody.get(i);
            }
        }
        return targetCoords;
    }
    private void preventiveMarkupFor(ArrayList<Coords> shipBody) {
        Coords nwCoords = getTargetNWCoords( shipBody );
        if ( isTargetHorizontal( shipBody ) ) {
            if ( nwCoords.x == 0 ) {
                enemyBoard.cell(nwCoords.x + shipBody.size() - 1, nwCoords.y, Board.DEBRIS );
            } else {
                if ( nwCoords.x + shipBody.size() - 1  == enemyBoard.getWidth() ) {
                    enemyBoard.cell(nwCoords.x  - 1, nwCoords.y, Board.DEBRIS );
                } else {
                    if ( enemyBoard.cell(nwCoords.x - 1, nwCoords.y ) == Board.SEA ) {
                        enemyBoard.cell(nwCoords.x - 1, nwCoords.y, Board.DEBRIS );
                    } else {
                        enemyBoard.cell(nwCoords.x + shipBody.size(), nwCoords.y, Board.DEBRIS );
                    }
                }
            }
        } else {
            if ( nwCoords.y == 0 ) {
                enemyBoard.cell(nwCoords.x, nwCoords.y + shipBody.size(), Board.DEBRIS );
            } else {
                if ( nwCoords.y + shipBody.size() - 1 == enemyBoard.getHeight() ) {
                    enemyBoard.cell(nwCoords.x, nwCoords.y - 1, Board.DEBRIS );
                } else {
                    if ( enemyBoard.cell(nwCoords.x, nwCoords.y - 1 ) == Board.SEA ) {
                        enemyBoard.cell(nwCoords.x, nwCoords.y - 1, Board.DEBRIS );
                    } else {
                        enemyBoard.cell(nwCoords.x, nwCoords.y + shipBody.size(), Board.DEBRIS );
                    }
                }
            }
        }
    }
    private void heuristicAnalysis_thenMarkNotNeeded( ArrayList<Coords> coords ) {
        if ( coords.size() > 3 ) {
            preventiveMarkupFor ( coords );
            bigShipWasKilled = true;
            return;
        }
        if (  coords.size() > 2 && bigShipWasKilled ) {
            preventiveMarkupFor ( coords );
        }
    }
    private boolean isTargetKilled( ArrayList<Coords> coords ) {
        if ( coords.isEmpty() ) {
            return false;
        }
        if ( coords.size() == 1 ){
            return noAvailableDirections(availableDirections(coords.getFirst().x, coords.getFirst().y));
        }
        Coords headCoords = getTargetNWCoords( coords );

        boolean[] headDirections = availableDirections(headCoords.x, headCoords.y);
        boolean[] tailDirections;
        if ( isTargetHorizontal( coords ) ) {
            tailDirections = availableDirections(headCoords.x + coords.size() - 1, headCoords.y);
        } else {
            tailDirections = availableDirections(headCoords.x , headCoords.y + coords.size() - 1);
        }
        if ( hardnessSetting >= HARD ) {
            if ( coords.size() > 2 ) {
                heuristicAnalysis_thenMarkNotNeeded(coords);
            }
        }
        boolean isKilled;
        if (isTargetHorizontal(coords)) {
            isKilled = (!headDirections[EAST] && !tailDirections[WEST]) && (!headDirections[WEST] && !tailDirections[EAST]) ;
        } else {
            isKilled = (!headDirections[NORTH] && !tailDirections[SOUTH]) && (!headDirections[SOUTH] && !tailDirections[NORTH]) ;
        }
        if ( coords.size() == 3 && isKilled ) {
            threeLinerKilled++;
        }
        return isKilled;
    }

    public int getLastX() {
        return lastFireAtX;
    }
    public int getLastY() {
        return lastFireAtY;
    }
    public void makeMove() {
        move++;
        if ( hardnessSetting == EASY) {
            fireAtRandom();
            return;
        }

        if ( !targetAcquired ) {
            fireAtRandom();
            return;
        }

        int fireAtX = currentShipCoords.getLast().x;
        int fireAtY = currentShipCoords.getLast().y;
        boolean[] directions = availableDirections(fireAtX, fireAtY);

        if (isTargetKilled(currentShipCoords)) {
            releaseTarget();
            fireAtRandom();
            return;
        }

        if ( !anyAvailableDirections(directions) ) {
            fireAtX = currentShipCoords.getFirst().x;
            fireAtY = currentShipCoords.getFirst().y;
            directions = availableDirections(fireAtX, fireAtY);
        }

        int dir = EAST;
        for ( int i = 0; i < directions.length * 10; i++ ) { // very bad code
            dir = rand.nextInt(directions.length);
            if ( directions[dir] ) {
                break;
            }
        }

        fireAtTarget(fireAtX, fireAtY, dir);

        if ( isTargetKilled(currentShipCoords) ) {
            releaseTarget();
        }
    }
}