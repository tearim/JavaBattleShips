package commonutils.menudispatcher;

import commonutils.ParamLine;
import commonutils.menudispatcher.Command;
import commonutils.menudispatcher.Message;
import commonutils.menudispatcher.interfaces.Adjutant;
import commonutils.menudispatcher.interfaces.CommandExtractor;
import commonutils.menudispatcher.interfaces.ParameterExtractor;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;


public abstract class CommandCollection<T extends CommandCollection<?>> {
    protected String name;
    protected String description;
    protected HashMap<String, Command> commands;
    protected ArrayList<Command> orderedMenu;
    protected Adjutant onEnter;
    protected Adjutant onLeave;
    protected Adjutant commandParser;
    protected String defaultAttachmentString = "println";
    protected final static String INVOKE_ONCE = "ONCE";
    protected CommandExtractor commandExtractor = this::defaultCommandExtractor;
    protected ParameterExtractor parameterExtractor = this::defaultParameterExtractor;
    protected PrintStream out = System.out;
    protected InputStream in = System.in;

    public T self() {
        return (T)this;
    }

    public T addCommand(Command command) {
        commands.put(command.getName(), command);
        return self();
    }

    /* Two shorteners for children */
    public T step(Command command) {
        commands.put(command.getName() + " " + commands.size(), command);
        return self();
    }
    public T option(Command command) {
        commands.put(command.getName(), command);
        return self();
    }


    public T removeCommand(Command command) {
        commands.remove(command.getName());
        return self();
    }
    public T removeCommand(String command) {
        commands.remove(command);
        return self();
    }
    protected Message<?> defaultParser(String command) {
        ParamLine pLine = new ParamLine();
        String[] words = pLine.parse(command);
        return new Message<String[]>( command, words);
    }
    protected String defaultCommandExtractor(Message<?> msg  ) {
        Object rawAttchment = msg.payload();
        if ( rawAttchment == null ) {
            return "";
        }
        String[] params = (String[]) rawAttchment;
        if ( params.length == 0) {
            return "";
        }
        return params[0];
    }
    protected String defaultParameterExtractor(String cmd, String rawParam ) {
        return rawParam.replace(cmd, "" ).trim();
    }
    protected abstract void sortMenu();
    public Message<?> invoke() {
        return invoke("");
    }
    public Message<?> invokeOnce() {
        return invoke(INVOKE_ONCE);
    }
    public abstract Message<?> invoke(String cmd);
    public abstract Message<String> defaultOnEnter(String cmd );


    public CommandCollection() {
        description = "";
        commands = new HashMap<>();
        onEnter = this::defaultOnEnter;
        onLeave = null;
        this.commandParser = this::defaultParser;
    }
    public CommandCollection(String name ) {
        this();
        this.name = name;
    }
    public CommandCollection(String name, Adjutant onEnter) {
        this(name);
        this.onEnter = onEnter;
    }
    public CommandCollection(String name, Adjutant onEnter,  Adjutant onLeave) {
        this(name, onEnter);
        this.onLeave = onLeave;
    }
    public CommandCollection(String name, Adjutant onEnter,  Adjutant onLeave, Adjutant commandParser  ) {
        this(name, onEnter, onLeave);
        this.commandParser = commandParser;
    }
    public CommandCollection(String name, String description ) {
        this(name);
        this.description = description;
    }
    public CommandCollection(String name, String description, Adjutant onEnter) {
        this(name, description);
        this.onEnter = onEnter;
    }
    public CommandCollection(String name, String description, Adjutant onEnter,  Adjutant onLeave) {
        this(name, description, onEnter);
        this.onLeave = onLeave;
    }
    public CommandCollection(String name, String description, Adjutant onEnter,  Adjutant onLeave, Adjutant commandParser  ) {
        this(name, description, onEnter, onLeave);
        this.commandParser = commandParser;
    }

    /* Builder-style Setters */
    public T setName(String name) {
        this.name = name;
        return self();
    }
    public T resetCommands() {
        this.orderedMenu = null;
        this.commands = new HashMap<>();
        return self();
    }

    public T requestReorder() {
        this.orderedMenu = null;
        return self();
    }
    public T setDescription(String description) {
        this.description = description;
        return self();
    }
    public T setCommands(HashMap<String, Command> commands) {
        this.commands = commands;
        return self();
    }
    public T setOnEnter(Adjutant onEnter ) {
        this.onEnter = onEnter;
        return self();
    }
    public T setOnLeave(Adjutant onLeave ) {
        this.onLeave = onLeave;
        return self();
    }
    public T setCommandParser(Adjutant commandParser ) {
        this.commandParser = commandParser;
        return self();
    }
    public T setCommandExtractor( CommandExtractor commandExtractor) {
        this.commandExtractor = commandExtractor;
        return self();
    }
    public T setParameterExtractor ( ParameterExtractor parameterExtractor ) {
        this.parameterExtractor = parameterExtractor;
        return self();
    }
    public T setOutStream (PrintStream out ) {
        this.out = out;
        return self();
    }
    public T setInStream(InputStream in) {
        this.in = in;
        return self();
    }

    /* Getters */
    public String getName () {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public HashMap<String, Command> getCommands() {
        return commands;
    }
    public Adjutant getOnEnter() {
        return onEnter;
    }
    public Adjutant getOnLeave() {
        return onLeave;
    }
    public Adjutant getCommandParser() {
        return commandParser;
    }
    public InputStream getInStream () {
        return in;
    }
    public PrintStream getOutStream () {
        return out;
    }
}
