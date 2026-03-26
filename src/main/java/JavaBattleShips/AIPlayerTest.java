package JavaBattleShips;

import JavaBattleShips.logic.AIPlayer;
import JavaBattleShips.logic.Board;
import JavaBattleShips.logic.StandardFleet;
import commonutils.Fansi;
import commonutils.STools;

import java.util.*;

public class AIPlayerTest {
    private static List<Integer> values = new ArrayList<>();
    private static HashMap<String, List<Integer>> allvalues = new HashMap<>();
    private static void addValue(Integer value) {
        values.add(value);
    }
    private static void addValue(String key, Integer value) {

        if (!allvalues.containsKey(key)) {
            allvalues.put(key, new LinkedList<>(values));
        }
        allvalues.get(key).add(value);
    }

    private static int medianOf() {
        Collections.sort(values);
        return values.get(values.size()/2);
    }
    private static int medianOf(String key) {
        List<Integer> list = allvalues.get(key);
        Collections.sort(list);
        return list.get(list.size()/2);
    }
    private static int sumOf(List<Integer> list) {
        int result = 0;
        for (Integer value : list) {
            result += value;
        }
        return result;
    }
    public static void displayBoard(Board board) {

        for (int j = -1; j < board.getHeight(); j++) {
            for (int i = -1; i < board.getWidth();  i++) {
                if ( j == -1) {
                    System.out.print((i+1)+" ");
                    continue;
                }
                if ( i == -1) {
                    if ( j == 9) {
                        System.out.print((j+1));
                    } else
                        System.out.print((j+1)+" ");
                    continue;
                }
                if ( board.isShip(i,j) ) {
                    System.out.print("##");
                } else {
                    if ( board.cell(i, j) == Board.DEBRIS)  {
                        System.out.print(Fansi.create().cyan().append("$$").reset().render());
                    }
                    if ( board.cell(i, j) == Board.FIRE)  {
                        System.out.print(Fansi.create().red().append("$$").reset().render());
                    }
                    if ( board.cell(i, j) == Board.SEA)  {
                        System.out.print(Fansi.create().blue().append("--").reset().render());
                    }
                }
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        System.out.println("AIPlayerTest");
        Board enemy = new Board(12, 12);
        AIPlayer computer; //= new AIPlayer("hard", null);
        StandardFleet standardFleet = new StandardFleet();
        standardFleet.issueNormal();
        standardFleet.randomPlacementOnABoard(enemy);
        //computer.uploadBoard(enemy);
        Scanner input = new Scanner(System.in);
        String init = "easy";
        for ( int n = 0; n < 10; n++ ) {
        int move = 0;
        for ( int l = 0; l < 4; l++) {
            float allResult = 0;
            switch ( l ) {
                case 0: init = "easy"; break;
                case 1: init = "normal"; break;
                case 2: init = "hard"; break;
                case 3: init = "hard+"; break;
            }
            System.out.print(STools.toLength(init, 10, STools.AL_LEFT) + ">> ");
            int best = 1000;
            int worst = -1000;
            for (int k = 0; k < 1000; k++) {
                computer = new AIPlayer(init, null);
                enemy = new Board(12,12);
                standardFleet.randomPlacementOnABoard(enemy);
                computer.uploadBoard(enemy);
                move = 0;
                do {
                    move++; //System.out.println("move #"+move);
                    computer.makeMove(); //System.out.println("computer moved: "+ (computer.getLastX() + 1) + " " + (computer.getLastY() + 1));
                    //displayBoard(enemy);
                    if (move > 144) {
                        System.out.print("Fail! ");
                        break;
                    }
                } while (!enemy.allShipsDown());
                if ( move < best ) best = move;
                if ( move > worst) worst = move;
                allResult += move;
                /* String toOutput = "";
                    toOutput = FANSI.begin().RGB(250,10, 10).text("" +move).reset().output();
                if ( move < 94) {
                    toOutput = FANSI.begin().RGB(250,100, 100).text("" +move).reset().output();
                }
                if ( move < 88) {
                    toOutput = FANSI.begin().RGB(250,150, 100).text("" +move).reset().output();
                }
                if ( move < 82) {
                    toOutput = FANSI.begin().RGB(200,150, 150).text("" +move).reset().output();
                }
                if ( move < 78) {
                    toOutput = FANSI.begin().RGB(150,200, 255).text("" +move).reset().output();
                }
                if ( move < 72) {
                    toOutput = FANSI.begin().RGB(150,150, 255).text("" +move).reset().output();
                }
                if ( move < 66) {
                    toOutput = FANSI.begin().RGB(100,150, 255).text("" +move).reset().output();
                }
                if ( move < 60) {
                    toOutput = FANSI.begin().RGB(100,100, 255).text("" +move).reset().output();
                }
                if ( move < 55) {
                    toOutput = FANSI.begin().RGB(50,150, 255).text("" +move).reset().output();
                } //*/
                addValue(move);
                addValue(init, move);
                //if ( move < 100) System.out.print (  "Best: " + best + ", worst: " + worst);
                //if ( k % 40 == 39) System.out.println();
            }
            System.out.print (  "Best: " + best + ", worst: " + worst);
            System.out.println(", mean: " + (allResult / 1000) + ", median: " + medianOf());
            values = new  ArrayList<>();
        }
        }
        for ( String init1 : allvalues.keySet() ) {
            System.out.println("all: " + init1 + ", mean: " + (sumOf(allvalues.get(init1)) / (float)(1000 * 10)) + ", median: " + medianOf(init1));
        }
    }

}
