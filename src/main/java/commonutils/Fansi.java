package commonutils;

/**
 * Fansi - An ANSI Styling Utility Class to enhance your console output.
 * Written by Mark Gondelman for developers who want stylish and better-looking terminal output.
 * Free to use and modify.
 *
 * Fansi is a lightweight ANSI styling utility for producing colored and
 * formatted console output. It supports standard ANSI colors, 256‑color
 * mode, true RGB colors, text effects, and per‑character gradients.
 * <p>
 * The API is fluent and chainable, allowing expressive styling such as:
 * <pre>
 *   new Fansi().red().bold().text("Error").reset().text(" details...");
 * </pre>
 * <p>
 * This class is dependency‑free and safe to embed in any Java project.
 */
public class Fansi {
    public static final boolean NO_RESET = false;
    private String currentColor = FG_DEF;
    private String currentEffect = EFF_RESET;
    private String currentBackground = BG_DEF;

    public static final String FG_BLACK = "30";
    public static final String FG_RED = "31";
    public static final String FG_GREEN = "32";
    public static final String FG_YELLOW = "33";
    public static final String FG_BLUE = "34";
    public static final String FG_MAGENTA = "35";
    public static final String FG_CYAN = "36";
    public static final String FG_WHITE = "37";
    public static final String FG_DEF = "39";

    public static final String BG_BLACK = "40";
    public static final String BG_RED = "41";
    public static final String BG_GREEN = "42";
    public static final String BG_YELLOW = "43";
    public static final String BG_BLUE = "44";
    public static final String BG_MAGENTA = "45";
    public static final String BG_CYAN = "46";
    public static final String BG_WHITE = "47";
    public static final String BG_DEF = "49";

    public static final String EFF_RESET = "0";
    public static final String EFF_BOLD = "1";
    public static final String EFF_FAINT = "2";
    public static final String EFF_ITALIC = "3";
    public static final String EFF_UNDERLINE = "4";
    public static final String EFF_SLOWBLINK = "5";
    public static final String EFF_RAPIDBLINK = "6";
    public static final String EFF_INVERSE = "7";
    public static final String EFF_CONCEAL = "8";
    public static final String EFF_STRIKETHROUGH = "9";

    public static final String NL = System.lineSeparator();
    public static final String TAB = "\u0009";

    public static final String ESC_B = (char) 27 + "[";
    public static final String ESC_E = "m";
    public static final String RESET = ESC_B + "0" + ESC_E;
    private StringBuilder output = null;
    private boolean bufferMode = false;
    public Fansi(String output) {
        this.output = new StringBuilder(output);
        bufferMode = true;
    }
    public Fansi() {
        this("");
    }
    public Fansi setColor(String color ) {
        currentColor = color;
        return this;
    }
    public Fansi setEffect (String effect ) {
        currentEffect = effect;
        return this;
    }
    public Fansi setBackground(String background) {
        currentBackground = background;
        return this;
    }
    private Fansi applyStyleAndText(String string, String what) {
        bufferMode = true;
        if (currentEffect.equals(EFF_RESET)) {
            output.append(ESC_B).append(what).append(ESC_E).append(string).append(ESC_B).append(currentEffect).append(";").append(currentColor).append(";").append(currentBackground).append(ESC_E);
        } else {
            output.append(ESC_B).append(what).append(ESC_E).append(string).append(ESC_B).append(currentEffect).append(ESC_E);
        }
        return this;
    }
    private Fansi applyEffect(String what) {
        currentEffect = what;
        bufferMode = true;
        output.append(ESC_B).append(what).append(ESC_E);
        return this;
    }
    public Fansi normal() {
        String toPreserve = ";" + currentColor + ";" + currentBackground ;
        return applyEffect(EFF_RESET + toPreserve );
    }
    public Fansi normal(String s) {
        String toPreserve = ";" + currentColor + ";" + currentBackground ;
        return applyStyleAndText(s, EFF_RESET + toPreserve );
    }

    public Fansi bold() {return applyEffect(EFF_BOLD);}
    public Fansi bold(String s) {return applyStyleAndText(s, EFF_BOLD);}
    public Fansi italic() {return applyEffect(EFF_ITALIC);}
    public Fansi italic(String s) {return applyStyleAndText(s,EFF_ITALIC);}
    public Fansi underline() {return applyEffect(EFF_UNDERLINE);}
    public Fansi underline(String s) {return applyStyleAndText(s,EFF_UNDERLINE);}
    public Fansi strikethrough() {return applyEffect(EFF_STRIKETHROUGH);}
    public Fansi strikethrough(String s) {return applyStyleAndText(s,EFF_STRIKETHROUGH);}

    /*
     * SHORTCUTS FOR STYLES
     */
    public Fansi n() {return normal();}
    public Fansi i() {return italic();}
    public Fansi b() {return bold();}
    public Fansi u() {return underline();}
    public Fansi s() {return strikethrough();}

    public Fansi bi () {return applyEffect(EFF_BOLD).applyEffect(EFF_ITALIC); }
    public Fansi biu () {return applyEffect(EFF_BOLD).applyEffect(EFF_ITALIC).applyEffect(EFF_UNDERLINE);}
    public Fansi bisu () {return applyEffect(EFF_BOLD).applyEffect(EFF_ITALIC).applyEffect(EFF_STRIKETHROUGH).applyEffect(EFF_UNDERLINE);}
    public Fansi bsu () {return applyEffect(EFF_ITALIC).applyEffect(EFF_STRIKETHROUGH).applyEffect(EFF_UNDERLINE);}
    public Fansi bu () {return applyEffect(EFF_BOLD).applyEffect(EFF_UNDERLINE);}

    public Fansi is (){return applyEffect(EFF_ITALIC).applyEffect(EFF_STRIKETHROUGH);}
    public Fansi isu () {return applyEffect(EFF_ITALIC).applyEffect(EFF_STRIKETHROUGH).applyEffect(EFF_UNDERLINE);}
    public Fansi iu () {return applyEffect(EFF_ITALIC).applyEffect(EFF_UNDERLINE);}

    public Fansi su () {return applyEffect(EFF_STRIKETHROUGH).applyEffect(EFF_UNDERLINE);}


    public Fansi effect(String e) {
        return applyEffect(e);
    }
    public Fansi blue() {
        return applyEffect(FG_BLUE);
    }
    public Fansi blue(boolean BG) {
        if (BG) return applyEffect(BG_BLUE);
        return applyEffect(FG_BLUE);
    }
    public Fansi black() {
        return applyEffect(FG_BLACK);
    }
    public Fansi black (boolean BG) {
        if (BG) return applyEffect(BG_BLACK);
        return applyEffect(FG_BLACK);
    }
    public Fansi red() {
        return applyEffect(FG_RED);
    }
    public Fansi red (boolean BG) {
        if (BG) return applyEffect(BG_RED);
        return applyEffect(FG_RED);
    }
    public Fansi green() {
        return applyEffect(FG_GREEN);
    }
    public Fansi green (boolean BG) {
        if (BG) return applyEffect(BG_GREEN);
        return applyEffect(FG_GREEN);
    }
    public Fansi yellow() {
        return applyEffect(FG_YELLOW);
    }
    public Fansi yellow (boolean BG) {
        if (BG) return applyEffect(BG_YELLOW);
        return applyEffect(FG_YELLOW);
    }
    public Fansi magenta() {
        return applyEffect(FG_MAGENTA);
    }
    public Fansi magenta (boolean BG) {
        if (BG) return applyEffect(BG_MAGENTA);
        return applyEffect(FG_MAGENTA);
    }
    public Fansi cyan() {
        return applyEffect(FG_CYAN);
    }
    public Fansi cyan (boolean BG) {
        if (BG) return applyEffect(BG_CYAN);
        return applyEffect(FG_CYAN);
    }
    public Fansi white() {
        return applyEffect(FG_WHITE);
    }
    public Fansi white (boolean BG) {
        if (BG) return applyEffect(BG_WHITE);
        return applyEffect(FG_WHITE);
    }

    public Fansi c256(String colorId) {
        return applyEffect("38;5;"+colorId);
    }
    public Fansi bc256(String colorId) {
        return applyEffect("48;5;"+colorId);
    }

    /**
     * Sets the foreground color using true RGB values.
     *
     * @param r red component (0–255)
     * @param g green component (0–255)
     * @param b blue component (0–255)
     * @return this Fansi instance for chaining
     */
    public Fansi RGB(int r, int g, int b) {
        return applyEffect("38;2;"+r+";"+g+";"+b);
    }

    /**
     * Sets the background color using true RGB values.
     *
     * @param r red component (0–255)
     * @param g green component (0–255)
     * @param b blue component (0–255)
     * @return this Fansi instance for chaining
     */
    public Fansi bRGB(int r, int g, int b) {
        return applyEffect("48;2;"+r+";"+g+";"+b);
    }


    public Fansi nl() {
        return append(NL);
    }
    public Fansi tab() {return append(TAB);}
    public Fansi tab(int n) {
        Fansi self = this;
        for ( int i = 0; i < n; i++) {
            self = self.tab();
        }
        return self;
    }
    public Fansi reset() {return applyEffect(EFF_RESET);}

    /**
     * Appends plain text to the internal buffer without modifying the current
     * color or effect state.
     *
     * @param s the text to append
     * @return this Fansi instance for chaining
     */
    public Fansi append(String s) {
        if ( output == null) {
            output = new StringBuilder(s);
        } else {
            output.append(s);
        }
        bufferMode = true;
        return this;
    }

    public String getOutputString(String str) {
        return  (char)27 +"[" +
                currentColor +
                (currentBackground.isEmpty()?"":";" + currentBackground) +
                (currentEffect.isEmpty()?"":";" + currentEffect)
                +"m" +
                str +
                (char)27 +"[0m";
    }
    /**
     * Returns the current buffered output as a plain string without applying
     * ANSI reset codes.
     *
     * @return the buffered output
     */
    public String render() {
        return output.toString();
    }

    public Fansi render(String str) {
        if ( output == null) {
            System.out.print(getOutputString(str));
        } else {
            System.out.print(getOutputString(output.append(str).toString()));
        };
        return this;
    }

    /**
     * Prints the buffered output to {@code System.out} using the current
     * color/effect state.
     */

    public void print() {
        System.out.print(output);

    }


    /**
     * Prints the buffered output followed by a newline.
     */
    public void println() {
        System.out.println(output);
    }

    /**
     * Prints the buffered output to {@code System.err} using the current
     * color/effect state and then clears the buffer.
     */
    public void flush() {
        System.out.println(output);
        output.replace(0, output.length(), "");
    }

    /**
     * Applies a background RGB gradient to the given string.
     * <p>
     * The gradient is calculated per character, interpolating between the
     * starting and ending RGB colors. Colors must be provided in the format
     * {@code "r;g;b"} where each component is in the range 0–255.
     *
     * @param s     the text to apply the gradient to
     * @param begin the starting RGB color in the format "r;g;b"
     * @param end   the ending RGB color in the format "r;g;b"
     * @return this Fansi instance for chaining
     */
    public Fansi gradientBg(String s, String begin, String end) {
        return _gradient(s, begin, end, null, null, true);
    }

    /**
     * Applies a foreground RGB gradient to the given string.
     * <p>
     * The gradient is calculated per character, interpolating between the
     * starting and ending RGB colors. Colors must be provided in the format
     * {@code "r;g;b"} where each component is in the range 0–255.
     *
     * @param s     the text to apply the gradient to
     * @param begin the starting RGB color in the format "r;g;b"
     * @param end   the ending RGB color in the format "r;g;b"
     * @return this Fansi instance for chaining
     */
    public Fansi gradientFg(String s, String begin, String end) {
        return  _gradient(s, begin, end, null, null, false);
    }

    /**
     * Applies both foreground and background RGB gradients to the given string.
     * <p>
     * Each gradient is calculated independently per character. All color
     * parameters must be in the format {@code "r;g;b"} with values in the
     * range 0–255.
     *
     * @param s      the text to apply the gradient to
     * @param begin  starting foreground RGB color ("r;g;b")
     * @param end    ending foreground RGB color ("r;g;b")
     * @param bBegin starting background RGB color ("r;g;b")
     * @param bEnd   ending background RGB color ("r;g;b")
     * @return this Fansi instance for chaining
     */
    public Fansi gradientBoth(String s, String begin, String end, String bBegin, String bEnd ) {
        return  _gradient(s, begin, end, bBegin, bEnd, true);
    }

    /**
     * Internal gradient implementation used by all public gradient methods.
     * <p>
     * Performs per-character interpolation between the provided RGB values.
     * If background colors are supplied, both foreground and background
     * gradients are applied simultaneously.
     *
     * @param s             the text to colorize
     * @param begin         starting foreground RGB ("r;g;b")
     * @param end           ending foreground RGB ("r;g;b")
     * @param begin1        starting background RGB ("r;g;b"), or null
     * @param end1          ending background RGB ("r;g;b"), or null
     * @param forBackground true to apply only a background gradient
     * @return this Fansi instance for chaining
     */
    private Fansi _gradient(String s, String begin, String end, String begin1, String end1, boolean forBackground ) {
        int l = s.length();
        String[] bColorsStr = begin.split(";");
        String[] eColorsStr = end.split(";");
        int[] bColors = new int[3];
        int[] eColors = new int[3];
        String[] b1ColorsStr = null;
        String[] e1ColorsStr = null;
        int[] b1Colors = null;
        int[] e1Colors = null;
        if ( begin1 != null) {
            b1ColorsStr = begin1.split(";");
            e1ColorsStr = end1.split(";");
            b1Colors = new int[3];
            e1Colors = new int[3];
        }

        try {
            for (int i = 0; i < 3; i++) {
                bColors[i] = Math.min(Math.max(0,Integer.parseInt(bColorsStr[i])), 255);
                eColors[i] = Math.min(Math.max(0,Integer.parseInt(eColorsStr[i])), 255);
                if ( begin1 != null) {
                    b1Colors[i] = Math.min(Math.max(0,Integer.parseInt(b1ColorsStr[i])), 255);
                    e1Colors[i] = Math.min(Math.max(0,Integer.parseInt(e1ColorsStr[i])), 255);
                }
            }
        } catch (Exception e) {
            return this;
        }
        if ( l < 2) {
            if ( begin1 != null ) {
                return RGB(bColors[0],bColors[1],bColors[2]).bRGB(b1Colors[0],b1Colors[1],b1Colors[2]).append(s);
            }
            if ( forBackground ) {
                return bRGB(bColors[0],bColors[1],bColors[2]).append(s);
            }
            return RGB(bColors[0],bColors[1],bColors[2]).append(s);
        }
        int rStep = (eColors[0] - bColors[0]) / (l - 1);
        int gStep = (eColors[1] - bColors[1]) / (l - 1);
        int bStep = (eColors[2] - bColors[2]) / (l - 1);
        int r1Step = 0; int g1Step = 0; int b1Step = 0;
        if ( begin1 != null ) {
            r1Step = (e1Colors[0] - b1Colors[0]) / (l - 1);
            g1Step = (e1Colors[1] - b1Colors[1]) / (l - 1);
            b1Step = (e1Colors[2] - b1Colors[2]) / (l - 1);
        }

        Fansi self = this;
        for ( int i = 0; i < l; i++) {
            if (begin1 == null) {
                if ( forBackground ) {
                    self = bRGB(bColors[0] + rStep * i,
                            bColors[1] + gStep * i,
                            bColors[2] + bStep * i);
                } else {
                    self = RGB(bColors[0] + rStep * i,
                            bColors[1] + gStep * i,
                            bColors[2] + bStep * i);
                }
            } else {
                self = RGB(bColors[0] + rStep * i,
                        bColors[1] + gStep * i,
                        bColors[2] + bStep * i).
                        bRGB(b1Colors[0] + r1Step * i,
                                b1Colors[1] + g1Step * i,
                                b1Colors[2] + b1Step * i);
            }


            self.append(""+s.charAt(i));
        }
        return self;
    }
    public Fansi custom(String s) {
        return applyEffect(s);
    }
    public static Fansi create() {
        return create(null);
    }

    /**
     * Creates a new Fansi instance and optionally applies an initial reset.
     *
     * @param noReset if true, no reset code is applied
     * @return a new Fansi instance
     */

    public static Fansi noReset(boolean noReset) {
        Fansi fansi = new Fansi().append("");
        if (noReset == NO_RESET) {
            return fansi;
        }
        return fansi.reset();
    }
    public static Fansi create(String s) {
        Fansi fansi = new Fansi().append("").reset();
        if ( s == null ) {
            return fansi;
        }
        return fansi.append(s);
    }
}
