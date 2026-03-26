package commonutils.menudispatcher.extenders;

import commonutils.menudispatcher.Command;
import commonutils.menudispatcher.interfaces.Adjutant;

public class Step {
    public static Command build (String prompt, Adjutant action) {
        return new Command("").setPrompt(prompt)
                .setAction(action);
    }
}
