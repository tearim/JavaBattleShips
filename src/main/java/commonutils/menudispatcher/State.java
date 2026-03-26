package commonutils.menudispatcher;

import commonutils.Ansi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.BiFunction;

public class State extends CommandCollection<State> {

    public static final String WAIT_REENTER = "wait-reenter";
    public static final String REENTER = "reenter";
    public static final String PRINT = "print";
    public static final String PRINTLN = "println";
    public static final String EXECUTE = "execute";
    public static final String EXIT = "exit";
    public static final String DEFAULT = "default";

    private TextStyle menuNameStyle = new TextStyle("**","**" + Ansi.NL);
    private TextStyle menuDescriptionStyle = new TextStyle("[","]" + Ansi.NL);
    private TextStyle commandNameStyle = new TextStyle("*","*");;
    private TextStyle commandDescriptionStyle = new TextStyle(" ", Ansi.NL);
    private TextStyle commandReplyStyle = new TextStyle("", Ansi.NL);
    private TextStyle errorStyle = new TextStyle("*", "*" + Ansi.NL);

    private ArrayList<Runnable> postExecutionProcedures = new ArrayList<>();

    private String prompt = "";

    public State(String name) {
        super(name);
        registerDefaultResultingCommands();
    }

    public State setMenuNameStyle(TextStyle style) {
        menuNameStyle = style;
        return self();
    }

    public State setMenuDescriptionStyle(TextStyle style) {
        menuDescriptionStyle = style;
        return self();
    }

    public State setCommandNameStyle(TextStyle style) {
        commandNameStyle = style;
        return self();
    }

    public State setCommandDescriptionStyle(TextStyle style) {
        commandDescriptionStyle = style;
        return self();
    }

    public State setCommandReplyStyle(TextStyle style) {
        commandReplyStyle = style;
        return self();
    }

    public State setErrorStyle(TextStyle style) {
        errorStyle = style;
        return self();
    }

    public TextStyle getCommandReplyStyle() {
        return commandReplyStyle;
    }

    public TextStyle getCommandDescriptionStyle() {
        return commandDescriptionStyle;
    }

    public TextStyle getCommandNameStyle() {
        return commandNameStyle;
    }

    public TextStyle getMenuDescriptionStyle() {
        return menuDescriptionStyle;
    }

    public TextStyle getMenuNameStyle() {
        return menuNameStyle;
    }

    public TextStyle getErrorStyle() {
        return errorStyle;
    }


    public Message<String> defaultOnEnter(String cmd ) {

        sortMenu();
        StringBuilder stringBuilder = new StringBuilder(menuNameStyle.style(name)  + menuDescriptionStyle.style(description) );
        for ( Command command : orderedMenu ) {
            if ( !command.getDoNotList()) {
                TextStyle nameStyle = command.getNameStyle() == null? commandNameStyle : command.getNameStyle();
                TextStyle descriptionStyle = command.getDescriptionStyle() == null? commandDescriptionStyle : command.getDescriptionStyle();
                String append =  nameStyle.style( command.getName()) + descriptionStyle.style( command.getDescription() );
                stringBuilder.append(
                        append
                );
            }
        }
        return new Message<>(stringBuilder.toString(), "println");
    }

    protected void sortMenu() {
        if ( orderedMenu != null) return;
        orderedMenu = new ArrayList<>(commands.values());
        Collections.sort(orderedMenu);
    }
    public State setPrompt(String prompt) {
        this.prompt = prompt;
        return self();
    }

    public String getPrompt() {
        return prompt;
    }

    protected void doOnEnter(String cmd) {
        if ( onEnter != null) {
            Message<?> result = onEnter.apply(cmd);
            if ( result.payload() != null && !((String)result.payload()).isEmpty() ) {
                if ( result.payload().equals("println")) {
                    out.println(commandReplyStyle.style(result.subject()));
                }
                if ( result.payload().equals("print")) {
                    out.println(commandReplyStyle.style(result.subject()));
                }
            }
        }
    }
    public record LoopingStateMessage (String inputString, boolean looping, boolean executePreviousCmdResult, boolean justDispatch ){ }
    public LoopingStateMessage resultingCommand(String resultingCommand, Message<?> commandResult) {
        if ( !resultingCommands.containsKey(resultingCommand) ) {
            return resultingCommands.get(DEFAULT).apply(resultingCommand, commandResult);
        }
        return resultingCommands.get(resultingCommand).apply(resultingCommand, commandResult);
    }
    private final HashMap<String, BiFunction<String, Message<?>, LoopingStateMessage>> resultingCommands = new HashMap<>();
    public State registerResultingCommand(String resultingCommand, BiFunction<String, Message<?>, LoopingStateMessage> parser) {
        resultingCommands.put(resultingCommand, parser);
        return self();
    }
    public State deleteResultingCommand(String resultingCommand ) {
        resultingCommands.remove(resultingCommand);
        return self();
    }

    public void registerDefaultResultingCommands() {

        registerResultingCommand(WAIT_REENTER, (attachmentCommandString, commandResult) -> {
            if (!commandResult.subject().isEmpty()) {
                out.println(commandReplyStyle.style(commandResult.subject()));
            }
            Scanner scanner = new Scanner(in);
            scanner.nextLine();
            doOnEnter(currentInvokedCMD);
            return new LoopingStateMessage(null,true, false , false);
        });

        registerResultingCommand(REENTER, (attachmentCommandString, commandResult) -> {
            if ( !commandResult.subject().isEmpty()) {
                out.println(commandReplyStyle.style(commandResult.subject()));
            }
            doOnEnter(currentInvokedCMD);
            return new LoopingStateMessage(null,true, false, false );
        });

        registerResultingCommand(PRINT, (attachmentCommandString, commandResult) -> {
            out.print(commandReplyStyle.style(commandResult.subject()));
            return new LoopingStateMessage(null,true, false, false );
        });

        registerResultingCommand(PRINTLN, (attachmentCommandString, commandResult) -> {
            out.print(commandReplyStyle.style(commandResult.subject()));
            return new LoopingStateMessage(null,true, false, false );
        });

        registerResultingCommand(EXECUTE, (attachmentCommandString, commandResult) -> {
            String attachmentString;
            if ( commandResult.payload() != null ) {
                attachmentString = commandResult.payload().toString();
            } else {
                attachmentString = defaultAttachmentString;
            }
            String attachmentParamString = parameterExtractor.apply(attachmentCommandString, attachmentString);

            if ( !attachmentParamString.isEmpty()) {
                out.println(commandReplyStyle.style(attachmentParamString));
            }
            return new LoopingStateMessage(commandResult.subject(),true, true, false );
        });

        registerResultingCommand(EXIT,  (attachmentCommandString, commandResult) -> {

            if ( commandResult.subject() != null && !commandResult.subject().isEmpty() ) {
                out.println(commandReplyStyle.style(commandResult.subject()));
            }
            return new LoopingStateMessage(attachmentCommandString,false, false, false );
        });

        registerResultingCommand(DEFAULT, (attachmentCommandString, commandResult) -> {
           return new LoopingStateMessage(null,false, false , false);
        });
    }
    public void addPostExecutionProcedure(Runnable procedure) {
        postExecutionProcedures.add(procedure);
    }
    private void executePostExecutionProcedures() {
        for (Runnable procedure : postExecutionProcedures) {
            procedure.run();
        }
        postExecutionProcedures.clear();
    }
    private String currentInvokedCMD = "";
    public Message<?> invoke(String cmd) {
        currentInvokedCMD = cmd;
        doOnEnter(currentInvokedCMD);
        Scanner scanner = new Scanner(in);
        boolean looping = cmd == null || !cmd.equals(INVOKE_ONCE);
        boolean executePreviousCmdResult = false;
        String inputString = "";
        String commandString;
        String paramString;
        Message<?> inputResult;
        Message<?> finalResult;
        String attachmentString;
        Command defaultCommand = null;
        Message<?> commandResult = new Message<>("");
        Message<?> attachmentParsed;
        String attachmentCommandString;
        String attachmentParamString;

        while (looping) {

            /* We initialize the cycle */
            boolean defaultCommandPresent = false;
            boolean commandNotExecuted = true;

            if (!prompt.isEmpty() ) {
                out.print(prompt);
            }

            /* Get input */
            if ( !executePreviousCmdResult ) {
                inputString = scanner.nextLine();
            }
            executePreviousCmdResult = false;

            /* Get message, command and parameter */
            inputResult = commandParser.apply(inputString);
            commandString = commandExtractor.apply(inputResult);
            paramString = parameterExtractor.apply(commandString, inputString);

            if ( commands.get("") != null ) {
                defaultCommandPresent = true;
                defaultCommand = commands.get("");
            }

            if ( commandString != null && commands.containsKey(commandString)) {
                commandResult = commands.get(commandString).execute(paramString);
                commandNotExecuted = false;
            }

            /* If you need to modify currently running state, i.e. add or remove a new command, use
            addPostExecutionProcedure. Direct meddling with remove/add commands might scare Java
            */
            executePostExecutionProcedures();

            /* If there's a default command, we execute it. The difference is that it gets the whole string, not
            the string without command trimmed, as we don't know what is the command.
             */
            if ( commandNotExecuted && defaultCommandPresent ) {
                commandResult = defaultCommand.execute(inputString);
                commandNotExecuted = false;
            }

            if ( commandNotExecuted) {
                commandResult = new Message<>("", true);
            }


            /* If command had any attachment attached, we assume, it is a command for our interface */
            if ( commandResult.payload() != null ) {
                attachmentString = commandResult.payload().toString();
            } else {
                attachmentString = defaultAttachmentString;
            }
            attachmentParsed = commandParser.apply(attachmentString);
            attachmentCommandString = commandExtractor.apply(attachmentParsed);
            attachmentParamString = parameterExtractor.apply(attachmentCommandString, attachmentString);

            LoopingStateMessage reaction = resultingCommand(attachmentCommandString, commandResult);

            if (reaction.inputString()!=null) {
                inputString = reaction.inputString();
            }
            looping = reaction.looping();
            executePreviousCmdResult = reaction.executePreviousCmdResult();

            if ( reaction.justDispatch() ) {
                return commandResult;
            }

        }
        finalResult = new Message<>("exiting menu "+name);
        if ( onLeave != null) {
            finalResult = onLeave.apply(cmd);
            if ( finalResult.payload() != null ) switch (finalResult.payload().toString()) {
                case "print":
                    out.print(finalResult.subject());
                    break;
                case "println":
                    out.println(finalResult.subject());
                    break;
            }
        }
        return finalResult;
    }
    /* Constructors */

    /* Builder-style Setters */

}
