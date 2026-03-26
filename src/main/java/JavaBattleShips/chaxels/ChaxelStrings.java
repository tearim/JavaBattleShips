package JavaBattleShips.chaxels;

import commonutils.Fansi;

import java.util.List;


public class ChaxelStrings {
    public static ChaxelString toChaxels(String source) {
        return toChaxels(source, 0);
    }
    public static ChaxelString toChaxels(String source, float zIndex) {
        ChaxelString chaxels = new ChaxelString();
        if ( source == null || source.isEmpty()) {
            return chaxels;
        }

        boolean inEscape = false;
        int i = 0;
        int[] currentRGB = {255, 255, 255};
        int[] currentBRGB = {0, 0, 0};
        char currentChar;
        boolean clearAfterOneUse = false;
        String currentEffects = "";
        boolean colorless = true;
        boolean colorlessBg = true;
        do {
            if ( SafeSubStrings.safeCharAt(source, i)  == (char) 27 ) {
                inEscape = true;
                switch (SafeSubStrings.safeSubStr(source, i+1, 6)) {
                    case "[38;2;":
                        currentRGB = strToRGB(SafeSubStrings.safeSubStr(source, i+7, 12));
                        colorless = false;
                        break;
                    case "[48;2;":
                        currentBRGB = strToRGB(SafeSubStrings.safeSubStr(source, i+7, 12));
                        colorlessBg = false;
                        break;
                    default:
                        String next2Symbols = SafeSubStrings.safeSubStr(source, i+ 2, 2);
                        switch(next2Symbols) {
                            case "0m":
                                currentRGB = new int[]{255, 255, 255};
                                currentBRGB = new int[]{0,0,0};
                                currentEffects = Fansi.ESC_B + Fansi.EFF_RESET + Fansi.ESC_E;
                                clearAfterOneUse = true;
                                colorless = true;
                                colorlessBg = true;
                                break;
                            case "1m": case "2m": case "3m":  case "4m":  case "5m":
                            case "6m": case "7m": case "8m":  case "9m":
                                if ( !currentEffects.contains(next2Symbols)) {
                                    currentEffects = currentEffects.replace(Fansi.ESC_B + Fansi.EFF_RESET + Fansi.ESC_E, "")+ Fansi.ESC_B + next2Symbols;
                                    clearAfterOneUse = false;
                                }

                                break;
                            case Fansi.BG_BLACK: currentBRGB = new int[]{0,0,0}; colorlessBg = false; break;
                            case Fansi.BG_RED: currentBRGB = new int[]{255,0,0}; colorlessBg = false; break;
                            case Fansi.BG_GREEN: currentBRGB = new int[]{0,255,0}; colorlessBg = false; break;
                            case Fansi.BG_YELLOW: currentBRGB = new int[]{255,255,0}; colorlessBg = false; break;
                            case Fansi.BG_BLUE: currentBRGB = new int[]{0,0,255}; colorlessBg = false; break;
                            case Fansi.BG_MAGENTA: currentBRGB = new int[]{255,0,255}; colorlessBg = false; break;
                            case Fansi.BG_CYAN: currentBRGB = new int[]{0,255,255}; colorlessBg = false; break;
                            case Fansi.BG_WHITE: currentBRGB = new int[]{255,255,255}; colorlessBg = false; break;
                            case Fansi.BG_DEF: currentBRGB = new int[]{0,0,0}; colorlessBg = true; break;

                            case Fansi.FG_BLACK: currentRGB = new int[]{0,0,0}; colorless = false; break;
                            case Fansi.FG_RED: currentRGB = new int[]{255,0,0}; colorless = false; break;
                            case Fansi.FG_GREEN: currentRGB = new int[]{0,255,0}; colorless = false; break;
                            case Fansi.FG_YELLOW: currentRGB = new int[]{255,255,0}; colorless = false; break;
                            case Fansi.FG_BLUE: currentRGB = new int[]{0,0,255}; colorless = false; break;
                            case Fansi.FG_MAGENTA: currentRGB = new int[]{255,0,255}; colorless = false; break;
                            case Fansi.FG_CYAN: currentRGB = new int[]{0,255,255}; colorless = false; break;
                            case Fansi.FG_WHITE: currentRGB = new int[]{255,255,255}; colorless = false; break;
                            case Fansi.FG_DEF: currentRGB = new int[]{0,0,0}; colorless = true; break;

                        }
                }
            } else {
                if ( !inEscape ) {
                    currentChar = SafeSubStrings.safeCharAt(source, i);
                    Chaxel chaxel = new Chaxel(currentChar, currentRGB, currentBRGB, zIndex, currentEffects);
                    chaxel.setColorless(colorless);
                    chaxel.setBgColorless(colorlessBg);
                    chaxels.add(chaxel);
                    if ( clearAfterOneUse ) {
                        currentEffects = "";
                        clearAfterOneUse = false;
                    }
                }
            }
            if ( inEscape ) {
                if ( SafeSubStrings.safeCharAt(source, i) == 'm' ) {
                    inEscape = false;
                }
            }
            i++;
        } while (i < source.length()  );
        return chaxels;
    }

    public static ChaxelString copy(ChaxelString chaxels) {
        ChaxelString copyString = new ChaxelString();
        for ( Chaxel chaxel : chaxels) {
            copyString.add(copy(chaxel));
        }
        return copyString;
    }

    public static Chaxel copy(Chaxel chaxel) {
        if ( chaxel == null ) {
            return null;
        }
        Chaxel copy = new Chaxel(chaxel.getChar(), chaxel.getRGB(), chaxel.getBRGB(), chaxel.getZIndex(), chaxel.getEffects());
        copy.setTransparent(chaxel.isTransparent());
        copy.setColorless(chaxel.isColorless());
        copy.setBgColorless(chaxel.isBgColorless());
        copy.setAfterClear(chaxel.getAfterClear());
        copy.setRGBOpacity(chaxel.getRGBOpacity());
        copy.setBRGBOpacity(chaxel.getBRGBOpacity());
        return copy;
    }

    public static ChaxelString printOver (ChaxelString oldChaxels, ChaxelString newChaxels, int startPosition, boolean ignoreZ, boolean clipPrintedText, boolean useOldString, boolean mergeZIndex) {
        ChaxelString result;
        if ( useOldString) {
            result = oldChaxels;
        } else {
            result = new ChaxelString(oldChaxels);
        }
        if ( startPosition +  newChaxels.size() <= 0 ) {
            return oldChaxels;
        }
        if ( clipPrintedText ) {
            if ( startPosition  > oldChaxels.size() ) {
                return oldChaxels;
            }
        }
        //aresult.get(Math.max(0,startPosition-1)).setAfterClear(FANSI.RESET);
        int newChaxelPosition = 0;
        int lastInsertedPosition = 0;
        String currentEffects = "";
        String[] effectArray = new String[10];
        String inheritedEffects = "";
        String[] currentEffectArray;
        for ( int i = startPosition; i < Math.max( newChaxels.size() + startPosition, oldChaxels.size() ); i++ ) {
            newChaxelPosition = i - startPosition;

            if ( i < 0) {
                if (newChaxels.get(newChaxelPosition).getEffects().isEmpty()) {
                    continue;
                }
                currentEffectArray = newChaxels.get(newChaxelPosition).getEffects().split(Fansi.ESC_E);
                for (String effect : currentEffectArray) {
                    int effectPos = Integer.parseInt(effect.replace(Fansi.ESC_B, ""));
                    if (effectPos == 0) {
                        for (int j = 1; j < 10; j++) {
                            effectArray[j] = "";
                        }
                    }
                    effectArray[effectPos] = effect + Fansi.ESC_E;
                    inheritedEffects = String.join("", effectArray[effectPos]);
                }
                continue;
            }


            if ( i <  oldChaxels.size() ) {
                if ( newChaxelPosition < newChaxels.size() ) {
                    if (  newChaxels.get(newChaxelPosition).getZIndex() > oldChaxels.get(i).getZIndex() ) {
                        Chaxel curatedCopy = copy(newChaxels.get(newChaxelPosition));
                        if (curatedCopy.isTransparent()) {
                            curatedCopy.setChar(oldChaxels.get(i).getChar());
                        }
                        if ( mergeZIndex ) {
                            curatedCopy.setZIndex(oldChaxels.get(i).getZIndex());
                        }
                        curatedCopy.setTransparent(oldChaxels.get(i).isTransparent());
                        curatedCopy.setRGB(applyRGBToRGBWithOpacity(result.get(i).getRGB(), curatedCopy.getRGB(), curatedCopy.getRGBOpacity()));
                        curatedCopy.setRGBOpacity(oldChaxels.get(i).getRGBOpacity());
                        curatedCopy.setBRGB(applyRGBToRGBWithOpacity(result.get(i).getBRGB(), curatedCopy.getBRGB(), curatedCopy.getBRGBOpacity()));
                        curatedCopy.setBRGBOpacity(oldChaxels.get(i).getBRGBOpacity());
                        result.set(i, curatedCopy);
                        //System.out.println(newChaxelPosition);
                        //result.set(i).setChar('~');
                        if ( !inheritedEffects.isEmpty() ) {
                            result.get(i).setEffects(inheritedEffects);
                            inheritedEffects = "";
                        }
                        lastInsertedPosition = i;
                    }
                } else {
                    if ( !clipPrintedText && i >= oldChaxels.size() ) {
                        if ( !inheritedEffects.isEmpty() ) {
                            result.get(i).setEffects(inheritedEffects);
                            inheritedEffects = "";
                        }
                        result.add(copy(newChaxels.get(newChaxelPosition)));
                    }
                }
            }
        }
        if ( lastInsertedPosition > 0 ) {
            result.get(lastInsertedPosition).setAfterClear(Fansi.RESET);
        }
        return result;

    }
    public static ChaxelString printOver (ChaxelString oldChaxels, ChaxelString newChaxels, int startPosition ) {
        return printOver(oldChaxels, newChaxels, startPosition, false, true, false, true);
    }

    public static String escapedString(ChaxelString chaxels) {
        StringBuilder sb = new StringBuilder();
        for (Chaxel chaxel : chaxels) {
                sb.append(chaxel);
        }
        return sb.toString();
    }

    public static String chaxelsToText(List<Chaxel> chaxels) {
        StringBuilder sb = new StringBuilder();
        for (Chaxel chaxel : chaxels) {
            sb.append(chaxel.getChar());
        }
        return sb.toString();
    }

    public static int[] strToRGB(String source) {
        String[] split = source.split("m")[0].split(";", 3);
        int[] rgb = new int[3];
        try {
            rgb[0] = Integer.parseInt(split[0]);
            rgb[1] = Integer.parseInt(split[1]);
            rgb[2] = Integer.parseInt(split[2]);
        }  catch (NumberFormatException e) {
            //
        }
        return rgb;
    }

    public static int[] applyRGBToRGBWithOpacity(int[] background, int[] dye, float opacity) {
        if ( opacity >= 1 ) {
            return dye;
        }
        if ( opacity <= 0 ) {
            return background;
        }
        int[] result = new int[background.length];
        for ( int i = 0; i < background.length; i++ ) {
            float delta = dye[i] - background[i];
            result[i] = background[i] + Math.round(delta * opacity);
        }
        return result;
    }

}
