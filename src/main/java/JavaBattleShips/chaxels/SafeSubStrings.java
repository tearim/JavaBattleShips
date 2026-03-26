package JavaBattleShips.chaxels;

public class SafeSubStrings {
    public static char safeCharAt(String s, int pos) {
        if ( pos > -1 && pos < s.length() ) {
            return s.charAt(pos);
        }
        return (char)0;
    }

    public static String safeSubStr(String s, int beg, int len) {
        return s.substring(beg, Math.min(beg + len,  s.length() ));
    }

    public static String createStringOfLength(int l) {
        return new String (new char[l]).replace('\0', ' ');
    }
}
