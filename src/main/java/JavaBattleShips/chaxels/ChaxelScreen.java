package JavaBattleShips.chaxels;

import java.util.ArrayList;
import java.util.Collections;

public class ChaxelScreen {
    ArrayList<PositionedChaxelShape> positionedChaxelShapes = new ArrayList<>();
    ChaxelShape canvas;
    ChaxelShape outputCanvas;

    public ChaxelScreen() {

    }
    public ChaxelScreen(int x, int y) {
        canvas = new ChaxelShape(x, y);
    }

    public ArrayList<PositionedChaxelShape> getPositionedChaxelShapes() {
        return positionedChaxelShapes;
    }

    public ChaxelScreen add(ChaxelShape chaxelShape, String id, int x, int y) {
        PositionedChaxelShape newShape = new PositionedChaxelShape(chaxelShape, id, x, y);
        positionedChaxelShapes.add(newShape);
        return this;
    }

    public ChaxelScreen add(ChaxelShape chaxelShape, String id, int x, int y, float z) {
        PositionedChaxelShape newShape = new PositionedChaxelShape(chaxelShape, id, x, y, z);
        positionedChaxelShapes.add(newShape);
        return this;
    }

    public ChaxelScreen remove(String id) {
        positionedChaxelShapes.removeIf( pcs -> pcs.getID().equals(id));
        return this;
    }

    public ChaxelScreen removeAll() {
        positionedChaxelShapes.clear();
        return this;
    }

    public ChaxelScreen move(String id, int x, int y, float z) {
        positionedChaxelShapes.forEach( pcs -> {
            if ( pcs.getID().equals(id)) {
                pcs.setZIndex(z);
                pcs.setPosZ(z);
                pcs.setPosX(x);
                pcs.setPosY(y);
            }
        });
        return this;
    }
    public ChaxelScreen move(String id, int x, int y ) {
        positionedChaxelShapes.forEach( pcs -> {
            if ( pcs.getID().equals(id)) {
                pcs.setPosX(x);
                pcs.setPosY(y);
            }
        });
        return this;
    }

    public ChaxelScreen move(String id,  float z) {
        positionedChaxelShapes.forEach( pcs -> {
            if ( pcs.getID().equals(id)) {
                pcs.setZIndex(z);

                pcs.setPosZ(z);
            }
        });
        return this;

    }

    public ChaxelScreen setShape(String id, ChaxelShape chaxelShape) {
        positionedChaxelShapes.forEach( pcs -> {
            if ( pcs.getID().equals(id)) {
                pcs.reset(chaxelShape.row(0).size(), chaxelShape.getRows().size());
                pcs.write(chaxelShape, 0, 0);
            }
        });
        return this;
    }

    public ChaxelShape getShape(String id ) {
        return positionedChaxelShapes.stream().filter( pcs -> pcs.getID().equals(id)).findFirst().orElse(null);
    }

    public ChaxelScreen write(String id, ChaxelString string, int x, int y) {
        positionedChaxelShapes.forEach( pcs -> {
            if ( pcs.getID().equals(id)) {
                pcs.write(string, x, y);
            }
        });
        return this;
    }


    public ChaxelScreen setCanvas(int x, int y) {
        canvas = new ChaxelShape(x, y);
        return this;
    }

    public ChaxelScreen setCanvas(ChaxelShape chaxelShape ) {
        canvas = chaxelShape;
        canvas.setZIndex(0);
        return this;
    }

    public ChaxelShape getCanvas() {
        return canvas;
    }
    public ChaxelScreen write() {
        outputCanvas = canvas.copy();
        Collections.sort(positionedChaxelShapes);
        for ( PositionedChaxelShape positionedChaxelShape : positionedChaxelShapes ) {
            outputCanvas.write(positionedChaxelShape, positionedChaxelShape.getPosX(), positionedChaxelShape.getPosY());
        }
        return this;
    }
    public String display() {
        return outputCanvas.display();
    }
}
