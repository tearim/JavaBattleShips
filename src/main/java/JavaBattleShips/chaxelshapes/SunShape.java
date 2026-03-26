package JavaBattleShips.chaxelshapes;

import JavaBattleShips.chaxels.ChaxelShape;

import java.util.ArrayList;
import java.util.Arrays;

public class SunShape extends ChaxelShape {


    public SunShape() {

    }

    public ChaxelShape shape() {

        ChaxelShape sunShape = new ChaxelShape(21, 8);
        sunShape.applyText(new ArrayList<>(Arrays.asList(
                "       ,...,         ",
                "     ,~~~~~~~,       ",
                "  ,~~~~@@@@@~~~~,    ",
                " ~~~~@@@###@@@~~~~   ",
                " ~~@@@#######@@@~~   ",
                "  '~~@@@###@@@~~'    ",
                "     `~~@@@~~'       ",
                "       `...`         ")));
        sunShape.addBgColorToTable('E', new int[] {255,255, 60} );
        sunShape.addBgColorToTable('D', new int[] {230,220, 50} );
        sunShape.addBgColorToTable('C', new int[] {220,200, 40} );
        sunShape.addBgColorToTable('B', new int[] {210,190, 20} );
        sunShape.addBgColorToTable('A', new int[] {200,180, 0 } );

        sunShape.applyColorsFromTable(new ArrayList<>(Arrays.asList(
                "       AABAA         ",
                "     AABCCCBAA       ",
                "  AABCCDEEEDDCBAA    ",
                " ABCCDEEEEEEEDDCBA   ",
                " ABCDDEEEEEEEDDCBA  ",
                "  ABCDDDEEEDDDCBA    ",
                "     ABCDDDCBA       ",
                "       AABAA         ")));

        sunShape.applyTransparencyMask(
                new ArrayList<>(Arrays.asList(
                        "        ***          ",
                        "      ~*****~        ",
                        "   ~$$$*****$$$~     ",
                        "  $$$*H*E§L§L*O$$    ",
                        "  $$$***§§§***$$$    ",
                        "   ~$$$*****$$$~       ",
                        "      ~*****~        ",
                        "        ***          ")));
        sunShape.applyOpacityMask(
                new ArrayList<>(Arrays.asList(
                        "       25552         ",
                        "     258***852       ",
                        "  258$$*****$$$52    ",
                        " 258$***§§§***$852   ",
                        " 258$***§§§***$852   ",
                        "  258$$*****$$852    ",
                        "     258***852       ",
                        "       25552         ")));

        return sunShape;
    }
}
