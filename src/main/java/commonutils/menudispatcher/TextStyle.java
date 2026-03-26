package commonutils.menudispatcher;

import java.util.function.Function;

public class TextStyle {
    private String before;
    private String after;
    private Function<String, String> effect;
    public TextStyle(String before, String after) {
        this.before = before;
        this.after = after;
        this.effect = null;
    }
    public TextStyle(String before, String after, Function<String, String> effect) {
        this(before, after);
        this.effect = null;
    }

    public String getBefore() {
        return before;
    }
    public void setBefore(String before) {
        this.before = before;
    }
    public String getAfter() {
        return after;
    }
    public void setAfter(String after) {
        this.after = after;
    }

    public void setEffect(Function<String, String> effect) {
        this.effect = effect;
    }
    public String style(String text) {
        if ( effect == null ) {
            return before + text + after;
        }
        text = effect.apply(before+"|||"+after+"|||"+text);
        return before + text + after;
    }
}
