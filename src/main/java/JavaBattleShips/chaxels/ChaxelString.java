package JavaBattleShips.chaxels;

import commonutils.Fansi;

import java.util.ArrayList;

public class ChaxelString extends ArrayList<Chaxel> {
    public ChaxelString() {
        super();
    }

    public ChaxelString(Fansi fansiString ) {
        super(ChaxelStrings.toChaxels(fansiString.render()));
    }

    public ChaxelString(String string ) {
        super(ChaxelStrings.toChaxels(string));
    }

    public ChaxelString(ChaxelString oldChaxelString) {
        super(oldChaxelString);
    }

    public static ChaxelString fromChaxel(Chaxel chaxel) {
        ChaxelString chaxelString = new ChaxelString();
        chaxelString.add(ChaxelStrings.copy(chaxel));
        return chaxelString;
    }

    public ChaxelString setZIndex(float zIndex) {
        for ( Chaxel chaxel : this ) {
            chaxel.setZIndex(zIndex);
        }
        return this;
    }
    public ChaxelString setRGB(String rgb) {
        return setRGBfor(false, rgb);
    }
    public ChaxelString setBRGB(String rgb) {
        return setRGBfor(true, rgb);
    }
    private ChaxelString setRGBfor(boolean forBackground, String rgb) {
        int[] nrgb = new int[3];
        String[] rgbs = rgb.split(";");
        for (int i = 0; i < rgbs.length; i++) {
            nrgb[i] = Integer.parseInt(rgbs[i]);
        }
        if (forBackground ) {
            setBRGB(nrgb);
        } else {
            setRGB(nrgb);
        }

        return this;
    }
    public ChaxelString setRGB(int[] rgb) {
        for ( Chaxel chaxel : this ) {
            chaxel.setRed(rgb[0]);
            chaxel.setGreen(rgb[1]);
            chaxel.setBlue(rgb[2]);
        }
        return this;
    }

    public ChaxelString setBRGB(int[] rgb) {
        for ( Chaxel chaxel : this ) {
            chaxel.setBgRed(rgb[0]);
            chaxel.setBgGreen(rgb[1]);
            chaxel.setBgBlue(rgb[2]);
        }
        return this;
    }

    public ChaxelString setRed(int red) {
        for ( Chaxel chaxel : this ) {
            chaxel.setRed(red);
        }
        return this;
    }
    public ChaxelString setGreen(int green) {
        for ( Chaxel chaxel : this ) {
            chaxel.setGreen(green);
        }
        return this;
    }
    public ChaxelString setBlue(int blue) {
        for ( Chaxel chaxel : this ) {
            chaxel.setBlue(blue);
        }
        return this;
    }

    public ChaxelString setBgRed(int bgRed) {
        for ( Chaxel chaxel : this ) {
            chaxel.setBgRed(bgRed);
        }
        return this;
    }
    public ChaxelString setBgGreen(int bgGreen) {
        for ( Chaxel chaxel : this ) {
            chaxel.setBgGreen(bgGreen);
        }
        return this;
    }
    public ChaxelString setBgBlue(int bgBlue) {
        for ( Chaxel chaxel : this ) {
            chaxel.setBgBlue(bgBlue);
        }
        return this;
    }
    public ChaxelString setEffects(String effects) {
        for ( Chaxel chaxel : this ) {
            chaxel.setEffects(effects);
        }
        return this;
    }
    public ChaxelString setEffects(String[] effects) {
        String effectsString = String.join("", effects);
        for ( Chaxel chaxel : this ) {
            chaxel.setEffects(effectsString);
        }
        return this;
    }
    public ChaxelString writeOn(ChaxelString newString, int startPos, boolean ignoreZIndexesAndTheMergeToZeroZIndex ) {
        if ( ignoreZIndexesAndTheMergeToZeroZIndex ) {
            newString.setZIndex(1);
            this.setZIndex(0);
        }
        return ChaxelStrings.printOver(this, newString,  startPos, false, true, true, true);
    }
    public ChaxelString writeOn(ChaxelString newString, int startPos ) {
        return ChaxelStrings.printOver(this, newString,  startPos, false, true, true, true);
    }

    public ChaxelString writeOn(String newString, int startPos) {
        return writeOn(ChaxelStrings.toChaxels(newString), startPos);
    }
    public ChaxelString applyText(String text ) {
        for ( int i = 0; i < text.length(); i++ ) {
            safeAt(i).setChar(text.charAt(i));
        }
        return this;
    }
    public  ChaxelString applyCharacterTransparencyMask(String stringMask) {
        boolean[] mask = new boolean[stringMask.length()];
        for ( int i = 0; i < stringMask.length(); i++ ) {
            switch (stringMask.charAt(i)) {
                case ' ':
                    mask[i] = true;
                    break;
                default:
                    mask[i] = false;
                    break;
            }
        }
        return applyCharacterTransparencyMask(mask);
    }
    public ChaxelString applyCharacterTransparencyMask(boolean[] mask) {
        int i = 0;
        for ( boolean b : mask ) {
            safeAt(i).setTransparent(b);
            i++;
        }
        return this;
    }

    private float getOpacityFromMaskingChar(char c) {
        float opacity;
        switch (c) {
            case '0': case ' ': opacity = 0;
                break;
            case 'f': case 'F': opacity = 1.0f;
                break;

            case '9': case '8': case '7':
            case '6': case '5': case '4':
            case '3': case '2': case '1':
                opacity = Float.parseFloat(String.valueOf(c)) / 10;
                break;
            default: opacity = 1;
        }
        return opacity;
    }
    public ChaxelString applyRGBOpacityMask(String mask) {
        for ( int i = 0; i < mask.length(); i++ ) {
            float opacity = getOpacityFromMaskingChar(mask.charAt(i));
            safeAt(i).setRGBOpacity(opacity);
        }
        return this;
    }

    public ChaxelString applyBRGBOpacityMask(String mask) {
        for ( int i = 0; i < mask.length(); i++ ) {
            float opacity = getOpacityFromMaskingChar(mask.charAt(i));
            safeAt(i).setBRGBOpacity(opacity);
        }
        return this;
    }

    public ChaxelString applyOpacityMask(String mask ) {
        for ( int i = 0; i < mask.length(); i++ ) {
            float opacity = getOpacityFromMaskingChar(mask.charAt(i));
            safeAt(i).setRGBOpacity(opacity);
            safeAt(i).setBRGBOpacity(opacity);
        }
        return this;
    }


    /** Provides safe access to chaxels or "blackhole" to unused"
     **/
    public Chaxel safeAt(int i) {
        if ( i < 0 || i > this.size() -1  )
            return new Chaxel(' ');
        return this.get(i);
    }

    /**
     *  normal at, check for nulls yourself ;)
     *  */
    public Chaxel at(int i) {
        if ( i < 0 || i > this.size() )
            return null;
        return this.get(i);
    }

}
