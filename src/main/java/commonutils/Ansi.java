package commonutils;

public class Ansi {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String WHITE = "\u001B[37m";
    public static final String BLUE = "\u001B[34m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\033[0;1m";
    public static final String NL = System.lineSeparator();

    public static String bold ( String str ) {
        return BOLD + str + RESET;
    }

    public static String black ( String str ) {
        return BLACK + str + RESET;
    }
    public static String white ( String str ) {
        return WHITE + str + RESET;
    }
    public static String blue ( String str) {
        return BLUE + str + RESET;
    }
    public static String red ( String str ) {
        return RED + str + RESET;
    }
    public static String green ( String str) {
        return GREEN + str + RESET;
    }
    public static String yellow ( String str) {
        return YELLOW + str + RESET;
    }
    public static String purple ( String str) {
        return PURPLE + str + RESET;
    }
    public static String cyan ( String str) {
        return CYAN + str + RESET;
    }

    public static String bBlack ( String str ) {
        return BOLD + BLACK + str + RESET;
    }
    public static String bWhite ( String str ) {
        return BOLD + WHITE + str + RESET;
    }
    public static String bBlue ( String str) {
        return BOLD + BLUE + str + RESET;
    }
    public static String bRed ( String str ) {
        return BOLD + RED + str + RESET;
    }
    public static String bGreen ( String str) {
        return BOLD + GREEN + str + RESET;
    }
    public static String bYellow ( String str) {
        return BOLD + YELLOW + str + RESET;
    }
    public static String bPurple ( String str) {
        return BOLD + PURPLE + str + RESET;
    }
    public static String bCyan ( String str) {
        return BOLD + CYAN + str + RESET;
    }
}
