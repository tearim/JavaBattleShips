package JavaBattleShips.chaxels;

public class ChaxelColorInfo {
    private int[] rgb;
    private int[] bRGB;
    private float rgbOpacity;
    private float bRGBOpacity;
    public static final String USE_FG = "fg";
    public static final String USE_BG = "bg";
    public static final String USE_BOTH = "both";
    private String usage;

    public ChaxelColorInfo(int[] rgb, boolean isForForeground) {
        if ( isForForeground ) {
            this.rgb = rgb;
            usage = USE_FG;
            rgbOpacity = 1.0f;
        } else {
            this.bRGB = rgb;
            usage = USE_BG;
            bRGBOpacity = 1.0f;
        }
    }

    public ChaxelColorInfo(int[] rgb, float opacity, boolean isForForeground) {
        this(rgb, isForForeground);
        if ( isForForeground ) {
            rgbOpacity = opacity;
        } else {
            bRGBOpacity = opacity;
        }
    }

    public ChaxelColorInfo(int[] rgb, int[] bRGB) {
        this.rgb = rgb;
        this.bRGB = bRGB;
        rgbOpacity = 1;
        bRGBOpacity = 1;
        usage = USE_BOTH;
    }

    public ChaxelColorInfo(int[] rgb, int[] bRGB,  float rgbOpacity, float bRGBOpacity) {
        this.rgb = rgb;
        this.bRGB = bRGB;
        this.rgbOpacity = rgbOpacity;
        this.bRGBOpacity = bRGBOpacity;
        usage = USE_BOTH;
    }

    public int[] getRGB() {
        return rgb;
    }
    public int[] getBRGB() {
        return bRGB;
    }
    public float getRGBOpacity() {
        return rgbOpacity;
    }
    public float getBRGBOpacity() {
        return bRGBOpacity;
    }
    public String getUsage() {
        return usage;
    }

    public void setRGB(int[] rgb) {
        this.rgb = rgb;
    }
    public  void setBRGB(int[] bRGB) {
        this.bRGB = bRGB;
    }
    public void setRGBOpacity(float rgbOpacity) {
        this.rgbOpacity = rgbOpacity;
    }
    public void setBRGBOpacity(float rgbOpacity) {
        this.bRGBOpacity = rgbOpacity;
    }
    public void setUsage(String usage) {
        this.usage = usage;
    }
}
