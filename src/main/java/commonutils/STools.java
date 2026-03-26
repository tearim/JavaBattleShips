package commonutils;

public class STools {
    public static final int AL_LEFT = 1;
    public static final int AL_MID = 2;
    public static final int AL_RIGHT = 3;

    public static String toLength(String s, int len ) {
        return toLength(s, len, AL_MID);
    }
    public static String toLength(String s, int len, int align ) {
        if (s.length() > len  ) {
            return s.substring(0, len - 3) + "...";
        }
        if ( s.length() == len) {
            return s;
        }
        int diff = len - s.length();
        String add1 = "";
        String add2 = "";
        if ( align == AL_RIGHT || align == AL_LEFT ) {
            for ( int i = 0; i < diff; i++ ) {
                add1 += " ";
            }
            if ( align == AL_LEFT ) return s + add1;
            return add1 + s;
        }
        for ( int i = 0; i < diff; i++ ) {
            if( i % 2 == 0 ) { add1 += " "; }
            if( i % 2 == 1 ) { add2 += " "; }
        }
        return add1 + s + add2;
    }
    public static String errorMsg(String error) {
        return Fansi.create().red().bold("Error! ").n().append(error).render();
    }
}
