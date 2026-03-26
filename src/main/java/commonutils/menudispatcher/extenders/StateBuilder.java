package commonutils.menudispatcher.extenders;

import commonutils.menudispatcher.Command;
import commonutils.menudispatcher.State;
import commonutils.menudispatcher.TextStyle;

public class StateBuilder {
    public static final int LAST_COMMAND = 1000;
    public static final String DEFAULT_EXIT_DESCRIPTION = "This command will exit the menu";
    public static State simple (String name, String description, String exitMessage ) {
        return new State(name).setDescription(description)
                .addCommand(new Command("exit",
                        DEFAULT_EXIT_DESCRIPTION, LAST_COMMAND,
                s -> {
                    return Commands.exit(exitMessage);
                }))
                .addCommand(Option.hiddenDefaultNotSupported());
    }
    public static State simple (String name, String description, String exitMessage, TextStyle errorStyle ) {
        return new State(name).setDescription(description)
                .addCommand(new Command("exit",
                        DEFAULT_EXIT_DESCRIPTION, LAST_COMMAND,
                        s -> {
                            return Commands.exit(exitMessage);
                        }))
                .addCommand(Option.hiddenDefaultNotSupported(errorStyle));
    }
    public static State simple (String name, String description, String exitCommandDescription, String exitMessage ) {
        return new State(name).setDescription(description)
                .addCommand(new Command("exit",
                        exitCommandDescription, LAST_COMMAND,
                        s -> {
                            return Commands.exit(exitMessage);
                        }))
                .addCommand(Option.hiddenDefaultNotSupported());
    }

    public static State simple (String name, String description, String exitCommandDescription, String exitMessage, TextStyle errorStyle) {
        return new State(name).setDescription(description)
                .addCommand(new Command("exit",
                        exitCommandDescription, LAST_COMMAND,
                        s -> {
                            return Commands.exit(exitMessage);
                        }))
                .setErrorStyle(errorStyle)
                .addCommand(Option.hiddenDefaultNotSupported(errorStyle));
    }
}
