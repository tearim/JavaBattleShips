package JavaBattleShips.clrender;

import JavaBattleShips.chaxels.*;
import commonutils.Fansi;
import JavaBattleShips.logic.Board;

import java.util.ArrayList;

public class GameRenderer {

    public static Renderer commonRenderer;
    public GameRenderer() {
        commonRenderer = new Renderer();
    }
    public Renderer instance() {
        return commonRenderer;
    }
    private int curX, curY;
    public void setCursorPosition(int x, int y) {
        curX = x;
        curY = y;
    }
    public long now() {
        return commonRenderer.getTick();
    }
    public Chaxel getCanvasChaxel(int x, int y) {
        return commonRenderer.getScreen().getCanvas().row(y).safeAt(x);
    }
    private String renderBoardLine(int[] line,  int x, int y, int beginY, boolean friendly, boolean showCursor) {
        int factor, rbHelper, gbHelper, bbHelper;
        Fansi resLine = new Fansi().create("");
        long cycle = commonRenderer.getTick();
        long privateCycle = 0;
        for ( int i = 0; i < line.length; i++) {
            int displaying = switch ((int)line[i]){
                case Board.SHIP -> friendly?line[i]:Board.FOG;
                case Board.SHIP_BIG-> friendly?line[i]:Board.FOG;
                case Board.SHIP_MED-> friendly?line[i]:Board.FOG;
                case Board.SHIP_SMOL-> friendly?line[i]:Board.FOG;
                case Board.SHIP_TINY-> friendly?line[i]:Board.FOG;
                case Board.SEA -> friendly?line[i]:Board.FOG;
                default -> line[i];
            };
            String shipDecal = null;
            int shipShade = 0;
            switch (displaying) {
                case Board.FOG:
                    privateCycle = cycle / 30 + y * 6  - i * i ;
                    if ( privateCycle < 0 ) privateCycle = 0;
                    factor = (privateCycle % 100 ) < 50 ?1:-1;
                    rbHelper = (int)(factor == 1 ? 10 + (privateCycle  % 50) : 60 - (privateCycle  % 50));
                    gbHelper = (int)(factor == 1 ? 10 + (privateCycle  % 50)  : 60 - (privateCycle  % 50));
                    bbHelper = (int)(factor == 1 ? 10 + (privateCycle  % 50) : 60 - (privateCycle % 50));
                    if ( showCursor && ( i == curX && y - beginY == curY )) {
                        rbHelper = 200;
                        gbHelper = 250;
                        bbHelper = 200;
                    }
                    switch ( (int)(privateCycle % 100 /25) ) {
                        case 0: resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(70, 70, 68).append("@#").reset(); break;
                        case 1: resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(70, 74, 65).append("#@").reset();break;
                        case 2: resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(70, 76, 62).append("##").reset();break;
                        case 3: resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(70, 75, 68).append("@@").reset();break;
                    }
                    break;
                case Board.DEBRIS:
                    privateCycle = Math.max(cycle / 20 - y * 4 - i * 6, 0);
                    factor = (privateCycle % 70 ) < 35 ?1:-1;
                    rbHelper = (int)(factor == 1 ? 10 + (privateCycle % 35) : 45 - (privateCycle % 35));
                    gbHelper = (int)(factor == 1 ? 70 + (privateCycle % 35)  : 105 - (privateCycle % 35));
                    bbHelper = (int)(factor == 1 ? 20 + (privateCycle % 35) : 55 - (privateCycle % 35));
                    if ( showCursor && ( i == curX && y - beginY == curY )) {
                        rbHelper = 250;
                        gbHelper = 250;
                        bbHelper = 250;
                    }
                    switch ( (int)(privateCycle/20) % 5 ) {
                        case 0: resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(70, 50, 50).append(":^").reset();break;
                        case 1: resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(70, 150, 50).append(".^").reset();break;
                        case 2: resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(70, 50, 150).append("^.").reset();break;
                        case 3: resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(30, 50, 50).append(".:").reset();break;
                        case 4: resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(30, 50, 70).append(":.").reset();break;
                    }
                    break;
                case Board.SEA:
                    privateCycle = cycle / 20 + y * 4 + i * 6;
                    factor = (privateCycle % 50 ) < 25 ?1:-1;
                    rbHelper = (int)(factor == 1 ? 20 + (privateCycle % 25) : 45 - (privateCycle % 25));
                    gbHelper = (int)(factor == 1 ? 40 + (privateCycle % 25)  : 65 - (privateCycle % 25));
                    bbHelper = (int)(factor == 1 ? 70 + (privateCycle % 25) : 105 - (privateCycle % 25));
                    if ( showCursor && ( i == curX && y - beginY == curY )) {
                        rbHelper = 200;
                        gbHelper = 250;
                        bbHelper = 200;
                    }
                    switch ( (int)(privateCycle/20) % 4 ) {
                        case 0: resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(30, 70, 150).append("~~").reset(); break;
                        case 1: resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(32, 74, 175).append("~^").reset();break;
                        case 2: resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(29, 168, 195).append("^~").reset();break;
                        case 3: resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(10, 22, 90).append("~~").reset();break;
                    }
                    break;
                case Board.FIRE:
                    privateCycle =Math.max( cycle / 10 + y - i * 6, 0);
                    factor = (privateCycle % 50 ) < 25 ?1:-1;
                    rbHelper = (int)(factor == 1 ? 220 + (privateCycle % 25) : 245 - (privateCycle % 25));
                    gbHelper = (int)(factor == 1 ? 40 + (privateCycle % 25)  : 65 - (privateCycle % 25));
                    bbHelper = (int)(factor == 1 ? 70 + (privateCycle % 25) : 105 - (privateCycle % 25));
                    if ( showCursor && ( i == curX && y - beginY == curY )) {
                        rbHelper = 250;
                        gbHelper = 250;
                        bbHelper = 250;
                    }
                    switch ( (int)(privateCycle/10) % 4 ) {
                        case 0: resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(230, 170, 150).append("|@").reset(); break;
                        case 1: resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(255, 190, 150).append("@\\").reset(); break;
                        case 2: resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(255, 210, 120).append("/@").reset(); break;
                        case 3: resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(230, 150, 190).append("/|").reset(); break;

                    }
                    break;
                case Board.SHIP:
                case Board.SHIP_TINY: if (shipDecal == null) {shipDecal = "'."; shipShade = 0;}
                case Board.SHIP_SMOL: if (shipDecal == null) {shipDecal = "::"; shipShade = 20;}
                case Board.SHIP_MED:  if (shipDecal == null) {shipDecal = "//"; shipShade = 40; }
                case Board.SHIP_BIG:  if (shipDecal == null) {shipDecal = "=|"; shipShade = 60; }
                    rbHelper = 170 -shipShade;
                    gbHelper = 70 - shipShade;
                    bbHelper = 70 - shipShade;
                    if ( showCursor && ( i == curX && y - beginY == curY )) {
                        rbHelper = 200;
                        gbHelper = 200;
                        bbHelper = 250;
                    }
                    privateCycle = cycle + y * 10 - i * 3;
                    if ( privateCycle % 200 > 150 ) {
                        resLine.bRGB(rbHelper, gbHelper, bbHelper).RGB(130, 230, 150).append(""+shipDecal.charAt(0)).RGB(250, 190, 90).append(""+shipDecal.charAt(1)).reset();
                    } else {
                        resLine.bRGB(rbHelper - 10, gbHelper + 10, bbHelper - 10 ).RGB(130, 230, 150).append(""+shipDecal.charAt(0)).RGB(60, 190, 90).append(""+shipDecal.charAt(1)).reset();
                    }
                    break;
                default:
                    resLine.bRGB(40, 60, 40).append("" + line[i]).reset();break;
            }
        }
        return resLine.render();
        //commonRenderer.moveToXY(x,y);
        //commonRenderer.print(resLine);
    }
    public ChaxelShape renderBoard(Board board,boolean friendly, boolean showCursor) {
        ArrayList<ChaxelString> rows = new  ArrayList<>();
        for ( int i = 0; i < board.getBoardSizeY(); i++) {
            rows.add(ChaxelStrings.toChaxels(renderBoardLine(board.getBoard()[i], 0, i, 0, friendly, !friendly)));
        }
        return new ChaxelShape(rows);
    }

}
