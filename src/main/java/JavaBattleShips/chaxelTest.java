package JavaBattleShips;

import JavaBattleShips.chaxels.ChaxelScreen;
import JavaBattleShips.chaxels.ChaxelShape;
import JavaBattleShips.chaxels.ChaxelString;
import JavaBattleShips.chaxels.ChaxelStrings;
import commonutils.Fansi;

import java.util.ArrayList;
import java.util.Arrays;

public class chaxelTest {
    public static void main(String[] args) {
        String str = Fansi.create().bisu().gradientBoth("Hello, dear world, how are you?", "20;50;60", "120;20;220", "250;230;210", "180;250;140").reset().append(" then regular text").render();
        String str1 = Fansi.create().b().RGB(255,0,0).append("PRINTED OVER").reset().render();
        String str2 = Fansi.create().u().RGB(0,255,0).append("undertext").reset().render();
        ChaxelString chaxels = ChaxelStrings.toChaxels(str);
        System.out.println(str);
        System.out.println(str1);

        System.out.println("Just text: " + ChaxelStrings.chaxelsToText(chaxels));
        System.out.println("Re-Render: " + ChaxelStrings.escapedString(chaxels));


        System.out.println("Just text: " + ChaxelStrings.escapedString(chaxels));
        ChaxelString chaxelsToPrint = ChaxelStrings.toChaxels(str1, 2);
        chaxelsToPrint.applyCharacterTransparencyMask("1111 1 1 1 ");
        ChaxelString chaxels1 = ChaxelStrings.printOver(chaxels, chaxelsToPrint, 4);
        chaxelsToPrint.setZIndex(5);
        System.out.println(ChaxelStrings.escapedString(chaxels1.writeOn(chaxelsToPrint, 10)));

        ChaxelString row1 = new ChaxelString(Fansi.create().append("       ").bRGB(250, 240, 40).RGB(255,255,120).append(".***.").reset().append("    "));
        ChaxelString row2 = new ChaxelString(Fansi.create().append("     ").bRGB(250, 240, 40).RGB(255,255,130).append(".~*****~.").reset().append("   "));
        ChaxelString row3 = new ChaxelString(Fansi.create().append("  ").bRGB(250, 240, 40).RGB(255,255,140).append(".~$$$*****$$$~.").reset().append("  "));
        ChaxelString row4 = new ChaxelString(Fansi.create().append(" ").bRGB(250, 240, 40).RGB(255,255,150).append("~$$$***§§§***$$$~").reset().append(" "));

        ChaxelString skyRow = new ChaxelString(Fansi.create().append("").bRGB(50, 240, 255).RGB(255,255,255) .append("~~~~~       ~~~           @@$$$%@%%~~~               ").reset().append(" "));
        ChaxelString skyRow1 = new ChaxelString(Fansi.create().append("").bRGB(50, 243, 255).RGB(255,255,255).append("  ~~~                 ````@@@###%%@@@@~~~            ").reset().append(" "));
        ChaxelString skyRow2 = new ChaxelString(Fansi.create().append("").bRGB(60, 245, 255).RGB(255,255,255).append("    ~~~~~               ~~~ @@@$$$@~~~~~~            ").reset().append(" "));
        ChaxelString skyRow3 = new ChaxelString(Fansi.create().append("").bRGB(70, 247, 255).RGB(255,255,255).append("       ~~~~~                                         ").reset().append(" "));
        ChaxelString skyRow4 = new ChaxelString(Fansi.create().append("").bRGB(80, 248, 255).RGB(255,255,255).append("                   ~~~@~~~          ~~@~~            ").reset().append(" "));
        ChaxelString skyRow5 = new ChaxelString(Fansi.create().append("").bRGB(90, 252, 255).RGB(255,255,255).append("                                                     ").reset().append(" "));
        ChaxelShape sunShape = new ChaxelShape(new ArrayList<ChaxelString>(Arrays.asList(
                row1,
                row2,
                row3,
                row4,
                ChaxelStrings.copy(row4),
                ChaxelStrings.copy(row3),
                ChaxelStrings.copy(row2),
                ChaxelStrings.copy(row1)
        )));

        ChaxelShape skyShape = new ChaxelShape(new ArrayList<>(Arrays.asList(
                skyRow, skyRow, skyRow1, skyRow1,skyRow2,skyRow2, skyRow3,skyRow3, skyRow4,skyRow4, skyRow5,  skyRow5
        )));
        sunShape.applyTransparencyMask(
                new ArrayList<>(Arrays.asList(
                        "       .***.         ",
                        "     .~*****~.       ",
                        "  .~$$$*****$$$~.    ",
                        " ~$$$***§§§***$$$~   ",
                        " ~$$$***§§§***$$$~   ",
                        "  .~$$$*****$$$~.    ",
                        "     .~*****~.       ",
                        "       .***.         ")));
        sunShape.applyOpacityMask(
                new ArrayList<>(Arrays.asList(
                        "       .***.         ",
                        "     .~*****~.       ",
                        "  .~$$$*****$$$~.    ",
                        " ~$$$***§§§***$$$~   ",
                        " ~$$$***§§§***$$$~   ",
                        "  .~$$$*****$$$~.    ",
                        "     .~*****~.       ",
                        "       .***.         ")));

        sunShape.setZIndex(1);

        ChaxelScreen screen = new ChaxelScreen();
        screen.setCanvas(skyShape);
        screen.add(sunShape, "sun", 0, 0, 2f);
        System.out.println(screen.write().display()); //*/

     /*   ChaxelScreen screen =  new ChaxelScreen();
        screen.setCanvas(new SkyShape().shape());
        screen.add(new SunShape().shape(), "sun", 0, 2, 2f);
        System.out.println(screen.write().display()); //*/

        for ( int i = 0; i < 5; i++) {
            screen.move("sun", i, i / 2);
            System.out.println("New Frame");
            System.out.println(screen.write().display());
        }
      /*  for ( int i = -20; i < 42; i++  ) {
            ChaxelString toPrint = ChaxelStrings.printOver(chaxels1, ChaxelStrings.toChaxels(str2, 1), i);
            System.out.println(ChaxelStrings.escapedString(toPrint));
        }
        chaxels1.setBRGB("20;50;90");
        chaxels1.setRGB("0;255;0");
        chaxels1.setZIndex(0);
        System.out.println(ChaxelStrings.escapedString(chaxels1.writeOn("helloo", 1)));
        /* Old test:
        chaxels.forEach( chaxel -> {
            System.out.println(chaxel.getChar() + " \n Z: " +chaxel.getZIndex() +  "\n bR " + chaxel.getBgRed() + " bG " + chaxel.getBgGreen() + " bB " + chaxel.getBgBlue() + "\n" +
                    " r " + chaxel.getRed() + " g " + chaxel.getGreen() + " b " + chaxel.getBlue() +
                    "  effects: " + (chaxel.isBold()?"bold ":"") + (chaxel.isItalic()?"italic ":"")  + (chaxel.isUnderline()?"underline ":"")  + (chaxel.isStrikeThrough()?"strike through ":"") );
        }); */

    }
}
