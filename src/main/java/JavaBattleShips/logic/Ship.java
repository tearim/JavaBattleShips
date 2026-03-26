package JavaBattleShips.logic;

public class Ship {

    private int length;
    private boolean isHorizontal;
    private boolean[] segments;

    public Ship(int length, boolean isHorizontal) {
        this.length = length;
        this.isHorizontal = isHorizontal;
        segments = new boolean[length];
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public boolean[] getSegments() {
        return segments;
    }

    public void setSegments(boolean[] segments) {
        this.segments = segments;
    }

}
