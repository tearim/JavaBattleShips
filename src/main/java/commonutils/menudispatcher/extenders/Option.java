package commonutils.menudispatcher.extenders;

import commonutils.menudispatcher.Command;
import commonutils.menudispatcher.TextStyle;
import commonutils.menudispatcher.extenders.Commands;
import commonutils.menudispatcher.interfaces.Adjutant;

public class Option {
    public final static String DEFAULT_NO_EMPTY_COMMANDS = "Empty commands are not supported";
    public final static String DEFAULT_COMMAND_NOT_RECOGNIZED = "Unfortunately, your input: *'INPUT_STRING'* is not a recognized command";

    public static Command add(String nameAtDescription, Adjutant action) {
        String[] parts = nameAtDescription.split("@", 2);
        return new Command(parts[0]).setDescription(parts[1]).setAction(action).setDoNotList(false);
    }
    public static Command hidden(String name, Adjutant action) {
        return new Command(name).setAction(action).setDoNotList(true);
    }
    public static Command hiddenDefault(Adjutant action) {
        return new Command("").setAction(action).setDoNotList(true);
    }
    public static Command hiddenDefaultNotSupported() {
        return new Command("").setDoNotList(true).setAction(
          s -> {
              if (s.isEmpty()) {
                  return Commands.message(DEFAULT_NO_EMPTY_COMMANDS);
              }
              return Commands.message(DEFAULT_COMMAND_NOT_RECOGNIZED.replace("INPUT_STRING", s));
          }
        );
    }
    public static Command hiddenDefaultNotSupported(TextStyle textStyle) {
        return new Command("").setDoNotList(true).setAction(
                s -> {
                    if (s.isEmpty()) {
                        return Commands.message(DEFAULT_NO_EMPTY_COMMANDS);
                    }
                    return Commands.message(DEFAULT_COMMAND_NOT_RECOGNIZED.replace("*'INPUT_STRING'*", textStyle.style(s)));
                }
        );
    }
}
