package JavaBattleShips.logic;

import JavaBattleShips.bridges.InputProvider;
import JavaBattleShips.bridges.OutputConsumer;
import JavaBattleShips.bridges.VisualEffects;
import JavaBattleShips.chaxels.ChaxelShape;
import JavaBattleShips.clrender.Renderer;
import JavaBattleShips.keymaster.KeyMaster;

public class GameLogic {
    private InputProvider input;
    private OutputConsumer output;
    private VisualEffects visualEffects;
    private KeyMaster keyMaster;
    public static final String GAME = "game";
    public static final String OPTIONS = "options";
    public static final String EXIT = "exit";
    public static final String WIN = "win";
    public static final String LOSE = "lose";
    public static final String GENERAL_MENU = "general";
    public static final String BATTLE_MENU = "battle";
    public static final String HIT = "hit";
    public static final String MISS = "miss";
    public static final String KILL = "kill";
    private AIPlayer aiStateMachine;
    private Board board;
    private Board aiBoard;
    private String aiSetting = "normal";


    public GameLogic(InputProvider input, OutputConsumer output, VisualEffects visualEffects, KeyMaster keyMaster) {
        this.input = input;
        this.output = output;
        this.visualEffects = visualEffects;
        this.keyMaster = keyMaster;
        board = new Board(10, 10);
        aiBoard = new Board(10, 10);
    }

    public Board getFriendlyBoard() {
        return board;
    }
    public Board getAIBoard() {
        return aiBoard;
    }
    public KeyMaster getKeyMaster() {
        return keyMaster;
    }

    public InputProvider getInputProvider() {
        return input;
    }
    public OutputConsumer getOutputConsumer() {
        return output;
    }

    public void greetingScreen() {
        output.consume( this, visualEffects.intro() );
    }
    public String generalMenu() {
        //output.showMainMenu();
        return input.userChoiceFrom(GENERAL_MENU, output::showMainMenu);
    }
    public String getAiSetting() {
        return aiSetting;
    }
    public void setAiSetting(String aiSetting) {
        this.aiSetting = aiSetting;
    }

    public String beginBattle() {
        System.out.println("Beginning battle");
        Board userBoard, computerBoard;
        output.consume(this, visualEffects.beginBattle());
        userBoard = userPlaceShips();
        computerBoard = computerPlaceShips();
        boolean running = true;
        do {
            drawBoards();
            boolean continueFiring = false;
            String result;
            do {
                result = makeMove(computerBoard, input.userChoiceFrom(BATTLE_MENU, VisualEffects::battleMenuInput ));
                continueFiring = !result.equals(MISS);
            } while (continueFiring);
            if ( computerBoard.allShipsDown() ) {
                return WIN;
            }
            do {
                result = makeMove(userBoard,  aiStateMachine.nextChoice(userBoard) );
                continueFiring = !result.equals(MISS);
            } while (continueFiring);
            if ( userBoard.allShipsDown() ) {
                return LOSE;
            }

        } while ( running );

        return WIN;
    }

    public void beginGameProcess() {
        greetingScreen();
        boolean running = true;
        String selection = "";
        do {
            switch (selection) {
                case GAME:
                    selection = beginBattle();
                    break;
                case WIN:
                    showWinScreen();
                    break;
                case LOSE:
                    showLoseScreen();
                    break;
                case OPTIONS:
                    setAiSetting(showOptions());
                    selection = "";
                    break;
                case EXIT:
                    running = false;
                    break;
                default:
                    selection = generalMenu();
            }
        } while (running);
        outroScreen();

    }

    public void showWinScreen() {

    }

    public void showLoseScreen() {

    }

    public void debug(int y, String s) {
        ChaxelShape debug = output.getRenderer().instance().getScreen().getShape("debug");
        if (debug == null) return;
        //debug.row(y).writeOn(ChaxelStrings.toChaxels(s), x);
        debug.row(y).applyText(s);
        System.out.println(Renderer.MOVE_TOBEGINNING);
        System.out.println(output.getRenderer().instance().getScreen().write().display());
    }

    public String showOptions() {
        return input.userChoiceFrom(GENERAL_MENU, output::showOptionsMenu);
    }

    public void outroScreen() {
        System.out.println("Goodbye!!!");
        System.exit(0);
    }

    public void drawBoards() {
        System.out.println("here");
    }

    public String makeMove(Board userBoard, String input) {
        return "";
    }

    private Board userPlaceShips()  {
        return new Board(10, 10);
    }

    public Board computerPlaceShips() {
        return new Board(10, 10);
    }
}
