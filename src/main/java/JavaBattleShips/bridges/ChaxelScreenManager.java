package JavaBattleShips.bridges;

import JavaBattleShips.chaxels.Chaxel;
import JavaBattleShips.clrender.GameRenderer;
import JavaBattleShips.clrender.Renderer;
import JavaBattleShips.keymaster.KeyMaster;
import JavaBattleShips.logic.GameLogic;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import commonutils.Fansi;
import commonutils.STools;
import commonutils.menudispatcher.Message;

import java.util.*;

public class ChaxelScreenManager extends OutputConsumer {
    private final static int WIDTH = 120;
    private final static int HEIGHT = 25;
    @Override
    public void consume(GameLogic logic, Message<?> messageToOutput) {
        if ( messageToOutput.subject().equals(GameLogic.BATTLE_MENU)) {
            VisualEffects.mainGameImplementation(logic);
        }
    }

    private final GameRenderer renderer;
    private String state = "";
    private KeyMaster keyMaster ;

    public ChaxelScreenManager(KeyMaster keymaster) {
        renderer = new GameRenderer();
        renderer.instance().reset().createScreen(WIDTH,HEIGHT);
        keyMaster = keymaster;
    }

    @Override
    public GameRenderer getRenderer() {
        return renderer;
    }

    private String outcome = "";
    public String getOutcome() {
        return outcome;
    }

    private int menuPosition = 0;
    volatile private String upKeyEvent = "";
    volatile private String downKeyEvent = "";
    volatile private String enterKeyEvent = "";
    String indicator;
    int currentYOffset;
    volatile boolean inMenu = true;
    private void showMenu(String title, HashMap<String, String> options, Runnable onChoice ) {
        menuPosition = 0;
        inMenu = true;

        upKeyEvent = keyMaster.registerKeyRelease(NativeKeyEvent.VC_UP, () -> {
            menuPosition--;
            if ( menuPosition < 0) menuPosition = options.size();
        });

        downKeyEvent = keyMaster.registerKeyRelease(NativeKeyEvent.VC_DOWN, () -> {
            menuPosition++;
            if ( menuPosition > options.size()) menuPosition = 0;
        });

        enterKeyEvent = keyMaster.registerKeyRelease(NativeKeyEvent.VC_ENTER, () -> {
            renderer.instance().cancelRecurringRedraw();
            onChoice.run();
            inMenu = false;
        });

        for ( int i = 0; i < 20; i++) {
            renderer.instance()
                    .moveToXY(0,i)
                    .print("                                                                                                                      ");
        }

        renderer.instance()
                .moveToXY(WIDTH/2 - 10,HEIGHT/2 - 5)
                .print(title)
                .moveToXY(WIDTH/2 - 10,HEIGHT/2 - 4)
                .print(Fansi.create().b().gradientFg("- UltraVersenker -", "0;30;180", "0;180;30").reset());


        List<String> choices = new ArrayList<>(options.keySet());
        Collections.sort(choices);
        renderer.instance().registerRecurringRedraw(100, () -> {
            renderer.instance().tick();
            renderer.setCursorPosition(0, 0);
            System.out.print(Renderer.MOVE_TOBEGINNING);
            int menuBgPosition = 0;
            currentYOffset = 0;
            indicator = "[ ]";
            for ( String string : choices ) {
                if ( menuPosition == currentYOffset) {
                    indicator = "[*]" ;
                    outcome = options.get(string);
                } else {
                    indicator = "[ ]" ;
                }
                if ( !indicator.equals("[*]") ) {
                    for (int i = -15; i < 45; i++) {
                        for (int j = -1; j < 2; j++) {
                            Chaxel cha = renderer.getCanvasChaxel(WIDTH / 2 - 14 + i, HEIGHT / 2 + currentYOffset * 2 + j);
                            cha.setBRGB(new int[]{0, 0, 0});
                            cha.setAfterClear(Fansi.RESET);
                        }
                    }
                } else {
                    menuBgPosition = currentYOffset;
                }
                    renderer.instance().moveToXY(WIDTH / 2 - 10, HEIGHT / 2 + currentYOffset * 2)
                            .print(Fansi.create().b().append(STools.toLength(indicator + string , 20, STools.AL_MID)));
                currentYOffset++;
            }
            Random random = new Random();
            for ( int i = -15; i < 45; i++ ) {
                for ( int j = -1; j < 2; j++ ) {
                    int xBase = (i + 15) ;
                    if ( xBase > 30 ) xBase = 30 - (xBase - 30);
                    int base = (xBase / 2) * 5 + switch (j) {
                        case -1 -> 40;
                        case 0 -> 60;
                        case 1 -> 40;
                        default -> 0;
                    };
                    Chaxel cha = renderer.getCanvasChaxel(WIDTH / 2 - 14 + i, HEIGHT / 2 + menuBgPosition * 2 + j);
                    if ( j == 0) {
                        cha.setRGB(new int[]{ 255 - random.nextInt(100, 130), 255 - random.nextInt(50, 80), 255});
                    }
                    cha.setBRGB(new int[]{ base + random.nextInt(1, 5), 0, 0});
                    cha.setAfterClear(Fansi.RESET);
                }
            }

            renderer.instance().printScreen();
        });
        while (inMenu) {
            Thread.onSpinWait();
        };
    }
    public void showMainMenu() {
        HashMap<String, String> options = new HashMap<>();
        options.put("00 NEW GAME", GameLogic.GAME);
        options.put("01 OPTIONS ", GameLogic.OPTIONS);
        options.put("02 EXIT    ", GameLogic.EXIT);
        showMenu(
                Fansi.create().b().gradientFg("=== MAIN MENU ===", "0;240;80", "0;80;240").reset().render(),
                options, () -> {
                    //renderer.instance().cancelRecurringRedraw();
                    keyMaster.removeKeyEvent(enterKeyEvent);
                    keyMaster.removeKeyEvent(downKeyEvent);
                    keyMaster.removeKeyEvent(upKeyEvent);
                }
        );

    }

    public void showOptionsMenu() {
        HashMap<String, String> options = new HashMap<>();
        options.put("00 EASY  ", "easy");
        options.put("02 HARD  ", "hard");
        options.put("03 HARD+ ", "hard+");
        options.put("01 NORMAL", "normal");
        showMenu(
                Fansi.create().b().gradientFg("=== AI HARDNESS: ===", "0;240;80", "0;80;240").reset().render(),
                options, () -> {
                    //renderer.instance().cancelRecurringRedraw();
                    keyMaster.removeKeyEvent(enterKeyEvent);
                    keyMaster.removeKeyEvent(downKeyEvent);
                    keyMaster.removeKeyEvent(upKeyEvent);
                }
        );

    }
}
