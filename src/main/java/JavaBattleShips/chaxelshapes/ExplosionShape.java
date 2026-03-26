package JavaBattleShips.chaxelshapes;

import JavaBattleShips.chaxels.ChaxelShape;

import java.util.ArrayList;
import java.util.Arrays;

public class ExplosionShape extends ChaxelShape {
    public ExplosionShape() {
        super();
    }
    public ChaxelShape shape() {
        reset(5, 3);
        applyText(new ArrayList<>(Arrays.asList(
              "``#//",
              "==*==",
              "//#``"
        )));
        addColorToTable('*', new int[] {255,200,200}, new int[] {255,100,100});
        addColorToTable('#', new int[] {235,230,190},  new int[] {235,230,190} );
        addColorToTable('=', new int[] {235,230,190},  new int[] {235,230,190} );
        addColorToTable('`',new int[] {225,210,150},  new int[] {235,230,190} );
        addColorToTable('/',new int[] {225,210,150},  new int[] {235,230,190} );

        applyColorsFromTable(new ArrayList<>(Arrays.asList(
                "``#//",
                "==*==",
                "//#``"
        )));

        applyTransparencyMask(new ArrayList<>(Arrays.asList(
                "` #/ ",
                "==*==",
                " /# `"
        )));
        applyOpacityMask(new ArrayList<>(Arrays.asList(
                "57975",
                "79*97",
                "57975"
        )));


        return this;
    }
}
