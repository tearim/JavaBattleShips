package JavaBattleShips.chaxelshapes;

import JavaBattleShips.chaxels.ChaxelShape;

import java.util.ArrayList;
import java.util.Arrays;

public class CloudShape extends ChaxelShape {
    public CloudShape() {
        super();
    }

    public ChaxelShape shape() {
        reset(10, 4);
        applyText(new ArrayList<>(Arrays.asList(
                "    /^^\\ ",
                "  (UUUUU) ",
                " (UUUUUUU)",
                "  //////  "
        )));
        //addBgColorToTable()
        applyTransparencyMask(new ArrayList<>(Arrays.asList(
                "    /^^\\ ",
                "  (UUUUU) ",
                " (UUUUUUU)",
                "  //////  "
        )));


        addBgColorToTable('A', new int[] {180,180,190} );
        addBgColorToTable('B', new int[] {160,170,190} );
        addBgColorToTable('C', new int[] {120,130,140} );
        addBgColorToTable('D', new int[] { 90, 90,120} );
        addBgColorToTable('R', new int[] { 80, 80, 80} );

        applyColorsFromTable(new ArrayList<>(Arrays.asList(
                "    AAAAA ",
                "  AABBCCD ",
                " ABBCAABCD",
                "  RRRRRR  "
        )));

        applyOpacityMask(new ArrayList<>(Arrays.asList(
                "    76673 ",
                "  5843663 ",
                " 588448663",
                "  737373  "
        )));


        return this;

    }
}
