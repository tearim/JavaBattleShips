package JavaBattleShips.chaxels;

public class PositionedChaxelShape extends ChaxelShape implements Comparable<PositionedChaxelShape>{
    private int posX;
    private int posY;
    private float posZ;
    private String id;
    public PositionedChaxelShape(ChaxelShape chaxelShape) {
        super(chaxelShape);
    }
    public PositionedChaxelShape(ChaxelShape chaxelShape, String id,  int posX, int posY) {
        super(chaxelShape);
        this.posX = posX;
        this.posY = posY;
        this.id = id;
    }
    public PositionedChaxelShape(ChaxelShape chaxelShape, String id, int posX, int posY, float posZ) {
        super(chaxelShape);
        this.posX = posX;
        this.posY = posY;
        this.setZIndex(posZ);
        this.posZ = posZ;
        this.id = id;
    }

    @Override
    public int compareTo(PositionedChaxelShape o) {
        if ( o.posZ > this.posZ ) {
            return -1;
        }
        if ( o.posZ < this.posZ ) {
            return 1;
        }
        return 0;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public float getPosZ() {
        return posZ;
    }

    public void setPosZ(float posZ) {
        this.posZ = posZ;
    }
}
