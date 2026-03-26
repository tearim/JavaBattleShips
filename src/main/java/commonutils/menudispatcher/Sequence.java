package commonutils.menudispatcher;


import commonutils.menudispatcher.Command;
import commonutils.menudispatcher.CommandCollection;
import commonutils.menudispatcher.Message;
import commonutils.menudispatcher.interfaces.Adjutant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class Sequence extends CommandCollection<Sequence> {
    public static final String NO_PROMPTS = "NO_PROMPTS";
    protected Adjutant onInterrupt;

    protected LinkedList<Command> commandSequence = new LinkedList<>();

    public Sequence() {
    }

    protected void sortMenu() {
        if ( commandSequence.size() > 1) return;
        orderedMenu = new ArrayList<>(commands.values());
        Collections.sort(orderedMenu);
        commandSequence.addAll(orderedMenu);
    }
    public Sequence(String name) {
        this.name = name;
    }

    public Message<String> defaultOnEnter(String cmd) {
        return new Message<String>(description, "println");
    }
    private String displayCurrentCommandPrompt(Command command) {
        if ( !command.getDescription().isEmpty() ) {
            return command.getDescription();
        }
        return "";
    }
    public Message<?> invoke(String cmd) {
        sortMenu();
        if ( onEnter != null) {
            Message<?> result = onEnter.apply(cmd);
            if ( result.payload() != null && !((String)result.payload()).isEmpty() ) {
                if ( result.payload().equals("println")) {
                    out.println(result.subject());
                }
                if ( result.payload().equals("print")) {
                    out.print(result.subject());
                }
            }
        }

        Command command;
        Message<?> result = new Message<>("");
        String inputString;
        Scanner scanner = new Scanner(in);
        boolean interrupted = false;
        String errorCommand = "";
        String successCommand = "";
        while (!commandSequence.isEmpty() && !interrupted ) {
            command = commandSequence.poll();
            if ( command == null) {
                break;
            }
            if (!cmd.contains(NO_PROMPTS)){
                out.print(displayCurrentCommandPrompt(command));
            }
            inputString = scanner.nextLine();
            result = command.execute(inputString);

            if ( result.isError() ) {
                boolean subLooping = true;
                while (subLooping) {
                    errorCommand = result.payload() != null?result.payload().toString():"";
                    switch (errorCommand) {
                        case "retrymsg":
                            out.println(result.subject());
                        case "retry":
                            subLooping = true;
                            if (!cmd.contains(NO_PROMPTS)){
                                out.print(displayCurrentCommandPrompt(command));
                            }
                            inputString = scanner.nextLine();
                            result = command.execute(inputString);
                            break;
                        case "interrupt":
                            interrupted = true;
                        default:
                            subLooping = false;
                            break;
                    }
                }
            } else {
                successCommand = result.payload() != null?result.payload().toString():"";
                switch (successCommand) {
                    case "silent":
                        break;
                    case "interrupt":
                        interrupted = true;
                        break;
                    case "print":
                        out.print(result.subject());
                        break;
                    case "println":
                    default:
                        out.println(result.subject());
                        break;

                }
            }
        }
        if (interrupted && onInterrupt != null ) {
            result = onInterrupt.apply(result.subject());

        }
        if ( !interrupted && onLeave != null ) {
            result = onLeave.apply(result.subject());
        }
        if ( result.payload() != null ) {
            switch ( result.payload().toString() ) {
                case "print":
                    out.print(result.subject());
                    break;
                case "println":
                    out.println(result.subject());
                    break;
            }
        }
        return result;
    }

    public Sequence addStep( Command command) {
        return super.addCommand(command);
    }

    public Sequence setOnInterrupt(Adjutant onInterrupt) {
        this.onInterrupt = onInterrupt;
        return self();
    }
}
