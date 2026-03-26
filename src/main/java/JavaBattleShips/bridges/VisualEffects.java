package JavaBattleShips.bridges;

import JavaBattleShips.chaxels.*;
import JavaBattleShips.chaxelshapes.*;
import JavaBattleShips.clrender.GameRenderer;
import JavaBattleShips.clrender.Renderer;
import JavaBattleShips.keymaster.KeyMaster;
import JavaBattleShips.logic.AIPlayer;
import JavaBattleShips.logic.Board;
import JavaBattleShips.logic.GameLogic;
import JavaBattleShips.logic.StandardFleet;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import commonutils.Fansi;
import commonutils.STools;
import commonutils.menudispatcher.Message;

import java.util.Scanner;

public class VisualEffects {

    public Message<?> intro() {
        return new Message<>("hello", "hello");
    }

    static int cursorX, cursorY, newX;
    static boolean cursorOnFriendlyField;
    private static ChaxelShape announcement;
    private static String announceText = "";
    private static void announce(String what ) {
        announceText = STools.toLength(what, 40, STools.AL_MID);
    }
    private static String announce() {
        return announceText;
    }

    private static int bombX;
    private static int bombY;
    private static int bombPrevX;
    private static int bombPrevY;
    private static int bombDestinationX;
    private static int bombDestinationY;
    private static final int ENEMY_BOARD_X = 50;
    private static final int ENEMY_BOARD_Y = 10;
    private static long bombArrivalTime = 0;
    private static boolean aiFiringSequenceStarted = false;
    private static boolean aiFired = false;
    private static long aiFiringSequenceBegin = 0;
    private static String status = "begin";
    private static boolean changePlayer = false;
    private static boolean gaming = true;
    private static long ready_for_comp = 0;
    private static int skyIlluminationFactor = 0;

    private static void initiateLaunch(int x, int y, int x1, int y1) {
        bombX = x;
        bombY = y;
        bombDestinationX = x1;
        bombDestinationY = y1;
    }
    private static void showExplosionAt(ChaxelScreen screen, int x , int y) {
        screen.move("explosion", x+2, y-1, 12f);
    }

    private static void fadeBy(ChaxelScreen screen, String id, float fadeStep) {
        ChaxelShape shape = screen.getShape(id);
        for ( ChaxelString string : shape.getRows()) {
            for ( int i = 0; i < string.size(); i++) {
                string.get(i).setBRGBOpacity( Math.max(string.get(i).getBRGBOpacity() - fadeStep, 0 ));
                string.get(i).setRGBOpacity( Math.max(string.get(i).getRGBOpacity() - fadeStep, 0 ));
            }
        }
    }

    private static void illuminateBy(ChaxelScreen screen, String id, int step, boolean forBg, boolean forFg) {
        ChaxelShape shape = screen.getShape(id);
        for ( ChaxelString string : shape.getRows()) {
            for ( int i = 0; i < string.size(); i++) {
                if ( forFg) {
                    string.get(i).setRed(Math.min(255, Math.max(string.get(i).getRed() + step, 0)));
                    string.get(i).setGreen(Math.min(255, Math.max(string.get(i).getGreen() + step, 0)));
                    string.get(i).setBlue(Math.min(255, Math.max(string.get(i).getBlue() + step, 0)));
                }
                if ( forFg) {
                    string.get(i).setBgRed(Math.min(255, Math.max(string.get(i).getBgRed() + step, 0)));
                    string.get(i).setBgGreen(Math.min(255, Math.max(string.get(i).getBgGreen() + step, 0)));
                    string.get(i).setBgBlue(Math.min(255, Math.max(string.get(i).getBgBlue() + step, 0)));
                }
            }
        }
    }
    private static GameRenderer renderer;
    public static long timePassedSince( long event) {
        return Math.abs(event - renderer.instance().getTick());
    }


    public static ChaxelShape flip(ChaxelShape shape) {
        ChaxelShape flipped = new ChaxelShape(shape.row(0).size(), shape.getRows().size());
        int j = 0;
        for (  ChaxelString string : flipped.getRows()) {
            string.clear();
            for ( int i = shape.row(j).size() - 1; i >= 0 ; i--) {
                string.add(ChaxelStrings.copy(shape.row(j).get(i)));
            }
            j++;
        }
        return flipped;
    }

    private static void mainRenderSequence(ChaxelScreen screen, Board humanBoard, Board enemyBoard) {
        screen.move("sun", -15 + (newX / 20) % 200, 2)
                .move("cloud", 70 - (newX / 10 ) % 200, 3)
                .move("cloud_2", 45 - (newX / 30 ) % 220, 1)
                .setShape("board", renderer.renderBoard(humanBoard, true, cursorOnFriendlyField))
                .setShape("enemy_board", renderer.renderBoard(enemyBoard, false, !cursorOnFriendlyField));
        if ( (-15 + (newX / 20) % 200 > 120) &&  skyIlluminationFactor > 30) {
            skyIlluminationFactor--;
            illuminateBy(screen, "sky", -1, true, true);
            illuminateBy(screen, "sky_1", -1, true, true);
            illuminateBy(screen, "sky_2", -1, true, true);
        }
        if ( (-15 + (newX / 20) % 200 > 140) &&  skyIlluminationFactor > 0) {
            skyIlluminationFactor--;
            illuminateBy(screen, "sky", -1, true, true);
            illuminateBy(screen, "sky_1", -1, true, true);
            illuminateBy(screen, "sky_2", -1, true, true);
        }

        if ( (-15 + (newX / 20) % 200 > 170) &&  skyIlluminationFactor < 15) {
            skyIlluminationFactor++;
            illuminateBy(screen, "sky", 1, true, true);
            illuminateBy(screen, "sky_1", 1, true, true);
            illuminateBy(screen, "sky_2", 1, true, true);
        }
        if (  (-15 + (newX / 20) % 200 < 0) && skyIlluminationFactor < 75 ) {
            skyIlluminationFactor++;
            illuminateBy(screen, "sky", 1, true, true);
            illuminateBy(screen, "sky_1", 1, true, true);
            illuminateBy(screen, "sky_2", 1, true, true);
        }


    }

    public static String mainGameImplementation(GameLogic logic) {
        Board board = logic.getFriendlyBoard();
        Board board2 = logic.getAIBoard();

        AIPlayer computer = new AIPlayer(logic.getAiSetting(), logic);
        board.reset(10,10);
        board2.reset(10,10);

        StandardFleet standardFleet = new StandardFleet();
        standardFleet.issueNormal();
        standardFleet.randomPlacementOnABoard(board);
        standardFleet.randomPlacementOnABoard(board2);

        computer.uploadBoard(board);

        renderer =  logic.getOutputConsumer().getRenderer();
        KeyMaster keyMaster = logic.getKeyMaster();
        renderer.instance()
                .createScreen(120, 25)
                .printScreen()
                //.clearAll()
                .pause(50)
                .moveToXY(10,23)
                .print(Fansi.create().b().gradientFg("UltraVersenker Render Engine ~~~ Press Esc to finish", "0;80;240", "0;240;80").reset())
                .printScreen()
                .pause(1000);

        keyMaster.registerKeyRelease(NativeKeyEvent.VC_LEFT, () -> {
            cursorX--;
            if ( cursorX < 0) cursorX = 0;
        });
        keyMaster.registerKeyRelease(NativeKeyEvent.VC_UP, () -> {
            cursorY--;
            if ( cursorY < 0) cursorY = 0;
        });
        keyMaster.registerKeyRelease(NativeKeyEvent.VC_RIGHT, () -> {
            cursorX++;
            if ( cursorX > board.getWidth() - 1) cursorX = board.getWidth() - 1;
        });
        keyMaster.registerKeyRelease(NativeKeyEvent.VC_DOWN, () -> {
            cursorY++;
            if ( cursorY > board.getHeight() - 1) cursorY = board.getHeight() - 1;
        });

        keyMaster.registerKeyRelease(NativeKeyEvent.VC_ENTER, () -> {
            if (status.equals("player_ready_to_fire")) {
                initiateLaunch(0,0, ENEMY_BOARD_X + ((cursorX-2) * 2 ), ENEMY_BOARD_Y + cursorY);
                if ( !board2.isShip( cursorX,  cursorY) ) {
                    announce("Miss!");
                    board2.cell(cursorX, cursorY, Board.DEBRIS);
                    changePlayer = true;
                } else {
                    announce("Hit!");
                    board2.cell(cursorX, cursorY, Board.FIRE);
                }
                status = "player_bomb_flies";
            };
        });


        ChaxelScreen screen =  renderer.instance().getScreen();
        screen.add(new SkyShape().shape(), "sky", 0, 0, 1f);
        screen.add(new SkyShape().shape(), "sky_1", 50, 0, 1f);
        screen.add(new SkyShape().shape(), "sky_2", 100, 0, 1f);
        screen.add(new SunShape().shape(), "sun", 0, 2, 2f);
        screen.add(new CloudShape().shape(), "cloud", 0, 2, 3f);
        screen.add(new CloudShape().shape(), "cloud_2", 0, 2, 4f);
        screen.add(new ChaxelShape(50, 1), "announcement", 0, 9, 6f);
        screen.add(new ShellShape().shape(), "bomb", 1, 9, -1f);
        screen.add(new ExplosionShape().shape(), "explosion", 1, 9, -1f);
        ChaxelShape debugShape = new ChaxelShape(40,10);
        //screen.add( debugShape, "debug", 70, 10, 10f);
        for ( int i = 0; i < 10; i++ ) {
            debugShape.row(i).applyBRGBOpacityMask("                                                        ");
            debugShape.row(i).setRGB(new int[]{100, 125, 255});
        }

        screen.add(renderer.renderBoard(board, true, false), "board", 10, 10, 5f );
        screen.add(renderer.renderBoard(board2, false, false), "enemy_board", ENEMY_BOARD_X, ENEMY_BOARD_Y, 5f );
        Scanner input = new Scanner(System.in);
        announcement = screen.getShape("announcement");
        announcement.row(0).applyBRGBOpacityMask("                                                        ");
        announcement.row(0).setRGB(new int[] {100, 125, 255});
        announce("Move cursor - Press enter to fire" );
        status = "player_ready_to_fire";
        renderer.instance().registerRecurringRedraw(5, () -> {
            newX++;
            renderer.setCursorPosition(cursorX, cursorY);
            System.out.print(Renderer.MOVE_TOBEGINNING);
            renderer.instance().tick();
            announce(status);
            announcement.row(0).applyText(announce());

            mainRenderSequence(screen, board, board2);

            // This is to wait a little bit after computer executed a successful attack and is mounting a new one
            if ( status.equals("computer_ready_to_fire_in_50") && timePassedSince(ready_for_comp) > 50) {
                status = "computer_ready_to_fire";
            }

            // This is when computer is about to fire
            if ( status.equals("computer_ready_to_fire")) {
                aiFiringSequenceBegin = renderer.now();
            }

            // We start flying animation and AI makes a move
            if ( timePassedSince(aiFiringSequenceBegin) < 10 && status.equals("computer_ready_to_fire") ) {
                status = "computer_bomb_flies";
                computer.makeMove();
                announce("cmp: " + computer.getLastX() + ", " +computer.getLastY());
                initiateLaunch(100, 0, 10 + computer.getLastX() * 2 , 10 + computer.getLastY());

                // If computer was not successful, we give human a try
                if ( !computer.getLastMoveSuccessful()) {
                    changePlayer = true;
                }
            }

            // the bomb makes uieeee.....!!!!
            if ( status.equals("player_bomb_flies") || status.equals("computer_bomb_flies")) {
                screen.move("bomb", bombX, bombY, 7f);

                bombPrevX = bombX;
                bombPrevY = bombY;
                if ( renderer.now() % 4 == 0 ) {
                    if (bombX < bombDestinationX + 2) {
                        if (cursorOnFriendlyField) {
                            bombX += 1;
                        } else {
                            bombX = Math.toIntExact(bombX + 4 + Math.round(Math.sqrt(Math.abs(bombX - bombDestinationX))));
                        }
                    }

                    if (bombY < bombDestinationY) {
                        bombY = bombY + 4;
                    }

                    if (bombX > bombDestinationX + 2) {
                        if (cursorOnFriendlyField) {
                            bombX = Math.toIntExact(bombX - 4 - Math.round(Math.sqrt(Math.abs(bombX - bombDestinationX))));
                        } else {
                            bombX -= 1;
                        }
                    }
                    if (bombY > bombDestinationY) {
                        bombY -= 3;
                    }

                    if ((bombX == bombPrevX) && (bombY == bombPrevY)) {
                        status = "bomb_explosion";
                        bombArrivalTime = renderer.instance().getTick();
                    }
                }
            }

            // explosion time...
            if( status.equals("bomb_explosion")) {
                if ( timePassedSince(bombArrivalTime ) > 5 ) {
                    screen.move("bomb", -1f);
                    if ( cursorOnFriendlyField) {
                        showExplosionAt(screen, bombDestinationX - 4, bombDestinationY);
                    } else {
                        showExplosionAt(screen, bombDestinationX, bombDestinationY);
                    }
                }
            }
            // We fade out the explosion...
            if (  status.equals("bomb_explosion") && (timePassedSince( bombArrivalTime + 5 ) > 5 )&& (timePassedSince( bombArrivalTime + 5 ) < 50 )) {
                fadeBy(screen, "explosion", 0.02f);
            }

            // ...and here we have the logic of the game:
            if ( status.equals("bomb_explosion") &&  timePassedSince(bombArrivalTime) > 35 ) {
                screen.remove("explosion")
                        .add(new ExplosionShape().shape(), "explosion", 0, 0, -1);
                bombArrivalTime = 0;

                // change active player, if needed...
                if (changePlayer) {
                    cursorOnFriendlyField = !cursorOnFriendlyField;
                }

                // hide bomb sprite...
                screen.move("bomb", 10, 1);

                // trigger computer to fire
                if (cursorOnFriendlyField) {
                    ready_for_comp = renderer.now();
                    status = "computer_ready_to_fire_in_50";
                    screen.setShape("bomb", flip(new ShellShape().shape()));
                } else {
                    // or giving a human an opportunity to fire
                    status = "player_ready_to_fire";
                    screen.setShape("bomb", new ShellShape().shape());
                }
                changePlayer = false;
            }

            // We render the screen...
            System.out.println(screen.write().display());

            // We check if there's a winner...
            if ( board.allShipsDown()) {
                announce("Computer wins!");
                renderer.instance().cancelRecurringRedraw();
                gaming = false;
            }

            if ( board2.allShipsDown()) {
                announce("You win!");
                for (int i =0; i < 50; i++) {
                    System.out.println("You win!!!");
                }
                renderer.instance().cancelRecurringRedraw();
                gaming = false;
            }
        });

        //System.out.println(announce());

        while(gaming) {
            Thread.onSpinWait();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            //
        }
        System.exit(0);
        return "";
    }
    public static String options() {
        return "";
    }

    public static String exit() {
        return "";
    }

    public static Message<?> menu() {
        return new Message<> ( "menu");
    }

    public static Message<?> beginBattle() {
        return new Message<> ( "battle");
    }

    public static Runnable generalMenuInput() {
        return () -> {};
    }

    public static Runnable battleMenuInput() {
        return () -> {};
    }
}
