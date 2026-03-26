package JavaBattleShips.chaxels;

import java.util.ArrayList;
import java.util.HashMap;

public class ChaxelShape {
    private ArrayList<ChaxelString> rows;
    private final HashMap<Character, ChaxelColorInfo> colorTable;
    private int width;

    public ChaxelShape() {
        super();
        rows = new ArrayList<>();
        colorTable = new HashMap<>();
    }
    public ChaxelShape(int x, int y) {
        rows = new ArrayList<>();
        colorTable = new HashMap<>();
        reset(x, y);
    }

    public ChaxelShape(ChaxelShape chaxelShape) {
        rows = new ArrayList<>();
        colorTable = new HashMap<>();
        initFromRows(chaxelShape.rows);
    }

    public ChaxelShape(ArrayList<ChaxelString> newRows) {
        rows = new ArrayList<>();
        colorTable = new HashMap<>();
        initFromRows(newRows);
    }

    private void initFromRows(ArrayList<ChaxelString> newRows) {
        if ( newRows == null) {
            return;
        }
        if (newRows.isEmpty()) {
            return;
        }
        width = newRows.getFirst().size();
        rows.add(ChaxelStrings.copy(newRows.getFirst()));
        for ( int i = 1; i < newRows.size(); i++ ) {
            rows.add(ChaxelStrings.copy(newRows.get(i)));
            if ( row(i).size() > width )
                width = row(i).size();
        }
    }
    public void applyText(ArrayList<String> mask) {
        for ( int i = 0; i < mask.size(); i++ ) {
            row(i).applyText(mask.get(i));
        }
    }
    public void applyTransparencyMask(ArrayList<String> mask) {
        for ( int i = 0; i < mask.size(); i++ ) {
            row(i).applyCharacterTransparencyMask(mask.get(i));
        }
    }

    public void applyRGBOpacityMask(ArrayList<String> mask) {
        for ( int i = 0; i < mask.size(); i++ ) {
            row(i).applyRGBOpacityMask(mask.get(i));
        }
    }

    public void applyBRGBOpacityMask(ArrayList<String> mask) {
        for ( int i = 0; i < mask.size(); i++ ) {
            row(i).applyBRGBOpacityMask(mask.get(i));
        }
    }

    public void applyOpacityMask(ArrayList<String> mask) {
        for ( int i = 0; i < mask.size(); i++ ) {
            row(i).applyOpacityMask(mask.get(i));
        }
    }

    public ChaxelString row (int i) {
        if ( rows == null || i < 0 || i > rows.size()) {
            return new ChaxelString();
        }
        return rows.get(i);
    }

    public void reset(int x, int y) {
        width = x;
        rows.clear();
        for ( int i = 0; i < y; i++ ) {
            rows.add(ChaxelStrings.toChaxels(
                    SafeSubStrings.createStringOfLength(x)
            ));
        }
    }

    public ArrayList<ChaxelString> getRows() {
        return rows;
    }

    /**
     * Writes another chaxel Shape onto given shape (clipping and zIndex)
     * @param chaxelShape
     * @param x
     * @param y
     */
    public void write(ChaxelShape chaxelShape, int x, int y) {
        int currentRow = 0;
        for ( ChaxelString chaxelString : chaxelShape.getRows() ) {
            write(chaxelString, x, y + currentRow);
            currentRow++;
        }
    }
    public void write(ChaxelString chaxels, int x, int y) {
        if ( y >= rows.size()) return;
        rows.get(y).writeOn(chaxels, x, true);
    }
    public void write(String chaxelize, int x, int y) {
        write(ChaxelStrings.toChaxels(chaxelize), x, y);
    }

    public void write(Chaxel chaxel, int x, int y) {
        write( ChaxelString.fromChaxel(chaxel), x, y);
    }

    /**
     * Prepares a ChaxelShape to be written in a PrintStream
     * @return
     */
    public String display() {
        StringBuilder sb = new StringBuilder();
        for ( ChaxelString chaxelString : rows ) {
            sb.append(ChaxelStrings.escapedString(chaxelString)).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public void setZIndex(float zIndex) {
        for (ChaxelString chaxelString : rows) {
            chaxelString.setZIndex(zIndex);
        }
    }

    public ChaxelShape copy() {
        ChaxelShape result = new ChaxelShape();
        result.rows = new ArrayList<>();
        for (ChaxelString chaxelString : rows) {
            result.rows.add(ChaxelStrings.copy(chaxelString));
        }
        return result;
    }

    public ChaxelShape addColorToTable(Character ch, int[] rgb ) {
        ChaxelColorInfo colorInfo = new ChaxelColorInfo(rgb, 1, true);
        return addColorToTable(ch, colorInfo);
    }

    public ChaxelShape addBgColorToTable(Character ch, int[] rgb ) {
        ChaxelColorInfo colorInfo = new ChaxelColorInfo(rgb, 1, false);
        return addColorToTable(ch, colorInfo);
    }


    public ChaxelShape addColorToTable(Character ch, int[] rgb, float opacity ) {
        ChaxelColorInfo colorInfo = new ChaxelColorInfo(rgb, opacity, true);
        return addColorToTable(ch, colorInfo);
    }

    public ChaxelShape addBgColorToTable(Character ch, int[] rgb, float opacity ) {
        ChaxelColorInfo colorInfo = new ChaxelColorInfo(rgb, opacity, false);
        return addColorToTable(ch, colorInfo);
    }

    public ChaxelShape addColorToTable(Character ch, int[] rgb, int[] bRGB ) {
        ChaxelColorInfo colorInfo = new ChaxelColorInfo(rgb, bRGB, 1, 1);
        return addColorToTable(ch, colorInfo);
    }

    public ChaxelShape addColorToTable(Character ch, int[] rgb, int[] bRGB, float opacity, float bOpacity ) {
        ChaxelColorInfo colorInfo = new ChaxelColorInfo(rgb, bRGB, opacity, bOpacity);
        return addColorToTable(ch, colorInfo);
    }

    public ChaxelShape removeColorFromTable(ChaxelShape ch ) {
        colorTable.remove(ch);
        return this;
    }

    public ChaxelShape clearColorTable() {
        colorTable.clear();
        return this;
    }

    public ChaxelShape addColorToTable(Character ch, ChaxelColorInfo colorInfo) {
        colorTable.put(ch, colorInfo);
        return this;
    }
    public ChaxelShape applyColorsFromTable(ArrayList<String> colorMask) {
        int y = 0;
        for ( String mask :  colorMask ) {
            for ( int i = 0; i < mask.length(); i++ ) {
                if ( colorTable.containsKey(mask.charAt(i)) ) {
                    ChaxelColorInfo colorInfo = colorTable.get(mask.charAt(i));
                    switch (colorInfo.getUsage()) {
                        case ChaxelColorInfo.USE_BG:
                            row(y).safeAt(i).setBRGB(colorInfo.getBRGB());
                            row(y).safeAt(i).setBRGBOpacity(colorInfo.getBRGBOpacity());
                            break;
                        case ChaxelColorInfo.USE_FG:
                            row(y).safeAt(i).setRGB(colorInfo.getRGB());
                            row(y).safeAt(i).setRGBOpacity(colorInfo.getRGBOpacity());
                            break;
                        case ChaxelColorInfo.USE_BOTH:
                            row(y).safeAt(i).setBRGB(colorInfo.getBRGB());
                            row(y).safeAt(i).setBRGBOpacity(colorInfo.getBRGBOpacity());
                            row(y).safeAt(i).setRGB(colorInfo.getRGB());
                            row(y).safeAt(i).setRGBOpacity(colorInfo.getRGBOpacity());

                    }
                }
            }
            y++;
        }
        return this;
    }
}
