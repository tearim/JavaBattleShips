package commonutils.menudispatcher;

import commonutils.ParamLine;
import commonutils.menudispatcher.extenders.*;

public class Tests {
    public static int attempts = 3;
    public static void replenishGlobalAttempts() {
        attempts = 3;
    }
    public static int showGlobalAttempts() {
        return attempts;
    }
    public static int useGlobalAttempts() {
        return --attempts;
    }
    public static void main(String[] args) {
        TestFunctions ic = new TestFunctions();
        Command cmd = new Command(
                "isnum",
                "checks whether input is an integer",
                ic::action,
                ic::fail,
                ic::isInt
        );
        Sequence userSequence = SequenceBuilder.simpleWithReenter( "userinfo",
                        "Thanks for entering all user data").
                setOnEnter(s -> {
                    replenishGlobalAttempts();
                    return Commands.println("Please enter all user data:");
                })
                .step(Step.build("Add user name: ",
                        s -> {
                            if ( s.isEmpty() ) {
                                return Commands.retry("Empty username not allowed");
                            }
                            if (!s.matches("^[\\w&&[\\D]]+([\\s]+)?[\\w&&[\\D]]+$")) {
                                return Commands.retry("Username must be non-numeric and contain one or two words");
                            }
                            return Commands.message("Username added!");
                        }))
                .step(Step.build("Add user email: ",
                        s -> {
                            if ( showGlobalAttempts()  < 1 ) {
                                return Commands.interrupt("User was not added: Wrong email.");
                            }
                            if ( s.isEmpty() ) {
                                return Commands.retry("Empty email not allowed. " + (useGlobalAttempts() + 1) + " more attempt(s) allowed.");
                            }
                            if (!s.matches("^[\\w\\.\\-]+@[\\w-.]+\\.[\\w&&[\\D]]{2,10}$")) {
                                return Commands.retry("This is not a valid email. " + (useGlobalAttempts() + 1) + " more attempt(s) allowed.");
                            }
                            return Commands.message("Email added!");
                        }));


        State state = StateBuilder.simple("state",
                        "A default tester for menus",
                        "Thanks for using the menu!")
                .option(cmd)
                .option(Option.add("chainedcmd@Triggers another command",
                        Commands::execute))
                .option(Option.add("cmsg@Chains a command after a nicely message",
                        s-> {
                            ParamLine paramLine = new ParamLine();
                            String[] words = paramLine.parse(s);
                            if ( words.length < 2 ) {
                                return Commands.println("To few parameters!");
                            }
                            StringBuilder sb = new StringBuilder();
                            for ( int i = 1; i < words.length; i++) {
                                sb.append(words[i]).append(" ");
                            }
                            //System.out.println(">>>>" + sb.toString().trim());
                            return Commands.execute(sb.toString().trim(), words[0] );
                        }))
                .option(Option.add("twoparam@Checks if two parameters are given.",
                        s -> {
                            ParamLine paramLine = new ParamLine();
                            String[] words = paramLine.parse(s);
                            if ( words.length < 2 ) {
                                return Commands.message("Too few params! ");
                            }
                            if ( words.length > 2) {
                                return Commands.message("Too many params! ");
                            }
                            return Commands.message("Thanks for giving two params");
                        })
                )
                .option(Option.add("user@Attempts to add a username",
                        s -> {
                            return userSequence.invoke(s);
                        }) );

        state.invoke();
    }
}
