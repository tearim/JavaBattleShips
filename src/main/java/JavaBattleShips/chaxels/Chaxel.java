package JavaBattleShips.chaxels;

import commonutils.Fansi;

public class Chaxel {
    private Character character;
    private Float zIndex;
    private int r, g, b;
    private int bR, bG, bB;
    private String effects;
    private String afterClear;
    public static final String CLEAR_EFFECTS = "clear_all_effects";
    private boolean noColors;
    private boolean noBgColors;
    private boolean transparent;
    private float RGBOpacity;
    private float BRGBOpacity;
    public Chaxel(Character cha) {
        this.character = cha;
        defInit();
    }

    public Chaxel(Character cha, int[] rgb, int[] bRgb, float zIndex) {
        this.character = cha;
        r = rgb[0];
        g = rgb[1];
        b = rgb[2];
        bR = bRgb[0];
        bG = bRgb[1];
        bB = bRgb[2];
        this.zIndex = zIndex;
        effects = afterClear = "";
        RGBOpacity = BRGBOpacity = 1;
    }

    public Chaxel(Character cha, int[] rgb, int[] bRgb, float zIndex, String effects) {
        this(cha, rgb, bRgb, zIndex);
        this.effects = effects;
    }
    public void setRGB(int[] rgb) {
        r = rgb[0];
        g = rgb[1];
        b = rgb[2];
        noColors = false;
    }

    public void setBRGB(int[] bRgb) {
        bR = bRgb[0];
        bG = bRgb[1];
        bB = bRgb[2];
        noBgColors = false;
    }

    private void readForegroundColors(String str ) {
        String[] bits = str.split("m")[0].split(";", 3);
        try {
            r = Integer.parseInt(bits[0]);
            g = Integer.parseInt(bits[1]);
            b = Integer.parseInt(bits[2]);
            noColors = false;
        }  catch (NumberFormatException e) {
            //
        }

    }

    private void readBackgroundColors(String str ) {
        String[] bits = str.split("m")[0].split(";", 3);
        try {
            bR = Integer.parseInt(bits[0]);
            bG = Integer.parseInt(bits[1]);
            bB = Integer.parseInt(bits[2]);
            noBgColors = false;
        }  catch (NumberFormatException e) {
            //
        }
    }

    private void defInit() {
        r = 255;
        g = 255;
        b = 255;
        bR = 0;
        bG = 0;
        bB = 0;
        zIndex = 0.0f;
        effects = afterClear ="";
        RGBOpacity = BRGBOpacity = 1;
    }
    public Character getChar() {
        return character;
    }

    public void setChar(Character character) {
        this.character = character;
    }

    public Float getZIndex() {
        return zIndex;
    }
    public void setZIndex(Float zIndex) {
        this.zIndex = zIndex;
    }

    public int getRed() {
        return r;
    }
    public void setRed(int r) {
        noColors = false;
        this.r = r;
    }
    public int getGreen() {
        return g;
    }
    public void setGreen(int g) {
        noColors = false;
        this.g = g;
    }
    public int getBlue() {
        return b;
    }
    public void setBlue(int b) {
        noColors = false;
        this.b = b;
    }
    public int getBgRed() {
        return bR;
    }
    public void setBgRed(int bR) {
        noBgColors = false;
        this.bR = bR;
    }
    public int getBgGreen() {
        return bG;
    }
    public void setBgGreen(int bG) {
        noBgColors = false;
        this.bG = bG;
    }
    public int getBgBlue() {
        return bB;
    }
    public void setBgBlue(int bB) {
        noBgColors = false;
        this.bB = bB;
    }

    public int[] getRGB() {
        return new int[]{r, g, b};
    }

    public int[] getBRGB() {
        return new int[]{bR, bG, bB};
    }

    public void setEffects(String effects) {
        this.effects = effects;
    }

    public String getEffects() {
        return effects;
    }
    public boolean isBold() {
        return effects.contains(Fansi.ESC_B + Fansi.EFF_BOLD + Fansi.ESC_E );
    }
    public boolean isItalic() {
        return effects.contains(Fansi.ESC_B + Fansi.EFF_ITALIC + Fansi.ESC_E );
    }
    public boolean isUnderline() {
        return effects.contains(Fansi.ESC_B + Fansi.EFF_UNDERLINE + Fansi.ESC_E );
    }
    public boolean isStrikeThrough() {
        return effects.contains(Fansi.ESC_B + Fansi.EFF_STRIKETHROUGH + Fansi.ESC_E );
    }
    public boolean hasReset() {
        return effects.contains(Fansi.ESC_B + Fansi.EFF_RESET + Fansi.ESC_E );
    }
    public boolean hasEffect(String effect) {
        return effects.contains(effect);
    }
    public String getPrintableColors() {
        return  (noColors ? "" : Fansi.ESC_B + "38;2;" + r +";"+g+";" + b + Fansi.ESC_E )+
                (noBgColors ? "" : Fansi.ESC_B + "48;2;" + bR +";"+bG+";" + bB + Fansi.ESC_E );

    }
    public void setEffect (String effect ) {
        switch (effect) {
            case CLEAR_EFFECTS:
                effects = "";
                break;
            case Fansi.EFF_RESET:
                r = g = b = 255;
                bR = bG = bB = 0;
                effects = Fansi.ESC_B + Fansi.EFF_RESET + Fansi.ESC_E;
                break;
            case Fansi.EFF_BOLD:
            case Fansi.EFF_FAINT:
            case Fansi.EFF_ITALIC:
            case Fansi.EFF_UNDERLINE:
            case Fansi.EFF_SLOWBLINK:
            case Fansi.EFF_RAPIDBLINK:
            case Fansi.EFF_INVERSE:
            case Fansi.EFF_CONCEAL:
            case Fansi.EFF_STRIKETHROUGH:
                effects = effects.replace(Fansi.ESC_B + Fansi.EFF_RESET + Fansi.ESC_E, "") +
                                (effects.contains(Fansi.ESC_B + effect + Fansi.ESC_E)
                                ?""
                                : Fansi.ESC_B + effect + Fansi.ESC_E);
        }
    }

    public boolean isColorless() {
        return noColors;
    }
    public boolean isBgColorless() {
        return noBgColors;
    }
    public void setColorless ( boolean noColors ) {
        this.noColors = noColors;
    }
    public void setBgColorless ( boolean noColors ) {
        this.noBgColors = noColors;
    }
    public void setAfterClear (String afterClear) {
        this.afterClear = afterClear;
    }
    public String getAfterClear () {
        return afterClear;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public void setRGBOpacity(float BRGBOpacity) {
        this.RGBOpacity = BRGBOpacity;
    }

    public float getRGBOpacity() {
        return RGBOpacity;
    }

    public void setBRGBOpacity(float BRGBOpacity) {
        this.BRGBOpacity = BRGBOpacity;
    }

    public float getBRGBOpacity() {
        return BRGBOpacity;
    }

    @Override
    public String toString() {
        return getEffects() + getPrintableColors() +  character + afterClear;
    }
}
