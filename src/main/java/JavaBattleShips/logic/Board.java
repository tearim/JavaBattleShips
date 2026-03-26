package JavaBattleShips.logic;

public class Board {
    public final static int SEA = 0;

    public final static int FOG = 2;
    public final static int FIRE = 3;
    public final static int DEBRIS = 4;

    public final static int SHIP = 8;
    public final static int SHIP_TINY = 8 | 16;
    public final static int SHIP_SMOL = 8 | 32;
    public final static int SHIP_MED = 8 | 64;
    public final static int SHIP_BIG = 8 | 128;


    private int[][] board;

    private int boardSizeX;
    private int boardSizeY;

    public int getWidth() {
        return boardSizeX;
    }
    public int getHeight() {
        return boardSizeY;
    }

    public Board(int x, int y) {
      reset(x,y);
    }
    public void reset(int x, int y) {
        board = new int[y][x];
        boardSizeX = x;
        boardSizeY = y;
    }
    public void clear() {
        for (int i = 0; i < boardSizeY; i++) {
            for (int j = 0; j < boardSizeX; j++) {
                board[i][j] = Board.SEA;
            }
        }
    }

    /**
     * Boundary-safe function to query board.
     * @param x int
     * @param y int
     */
    public int cell(int x, int y) {
        if ( x < 0 || x >= boardSizeX || y < 0 || y >= boardSizeY ) return 0;
        return board[y][x];
    }

    public void cell(int x, int y, int newVal) {
        if ( x < 0 || x >= boardSizeX || y < 0 || y >= boardSizeY ) return;
        board[y][x] = newVal;
    }

    public boolean isShip(int x, int y) {
        return (cell(x, y) >= SHIP);
    }

    public boolean isFire(int x, int y) {
        return (cell(x, y) == FIRE);
    }

    public boolean isEmpty(int x, int y, int len, boolean horizontal) {
        for ( int i = -1; i <= len; i++ ) {
            if ( horizontal) {
                if (  !
                     (      (cell(x + i, y) == FOG || cell(x + i, y) == SEA) &&
                      (cell(x + i,y - 1) == FOG || cell(x + i, y - 1) == SEA) &&
                      (cell(x + i,y + 1) == FOG || cell(x + i, y + 1) == SEA))
                ) {
                    return false;
                }

            } else {
                if (  !
                     (       (cell(x,y + i) == FOG || cell(x , y + i) == SEA) &&
                      (cell(x - 1,y + i) == FOG || cell(x - 1, y + i) == SEA) &&
                      (cell(x + 1,y + i) == FOG || cell(x + 1, y + i) == SEA))
                ) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean placeShip(Ship ship, int x, int y) {
        if ( ship.isHorizontal() && x + ship.getLength() > boardSizeX ) {
            return false;
        }
        if ( !ship.isHorizontal() && y + ship.getLength() > boardSizeX ) {
            return false;
        }
        if (!isEmpty(x,y,ship.getLength(), ship.isHorizontal())) {
            return false;
        }
        if ( ship.isHorizontal()) {
            for (int i = 0; i < ship.getLength(); i++) {
                cell (x + i , y, 8 + (int)(Math.pow(2,(i + 4))));
            }
        } else {
            for (int i = 0; i < ship.getLength(); i++) {
                cell (x , y + i,  8 + (int)(Math.pow(2,(i + 4))));
            }
        }
        return true;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int getBoardSizeX() {
        return boardSizeX;
    }

    public void setBoardSizeX(int boardSizeX) {
        this.boardSizeX = boardSizeX;
    }

    public int getBoardSizeY() {
        return boardSizeY;
    }

    public void setBoardSizeY(int boardSizeY) {
        this.boardSizeY = boardSizeY;
    }

    public boolean allShipsDown() {
        for ( int i = 0; i < boardSizeY; i++ ) {
            for ( int j = 0; j < boardSizeX; j++ ) {
                if (board[i][j] >= Board.SHIP) {
                    return false;
                }
            }
        }
        return true;
    }
}
