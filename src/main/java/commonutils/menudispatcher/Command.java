package commonutils.menudispatcher;

import commonutils.menudispatcher.Message;
import commonutils.menudispatcher.TextStyle;
import commonutils.menudispatcher.interfaces.Adjutant;

public class Command implements Comparable<Command> {
    private String name;
    private String description;
    private boolean doNotList;
    private int order;
    private static int naturalOrder = 0;
    private Adjutant action;
    private Adjutant onError;
    private Adjutant verifier;
    private TextStyle nameStyle;
    private TextStyle descriptionStyle;

    public Message<?> execute (String cmd) {
        if (verifier==null) {
            if ( action ==null) {
                return new Message<>(cmd,false);
            }
            return action.apply(cmd);
        }

        Message<?> result = verifier.apply(cmd);

        if ( result.isError() ) {
            if ( onError==null) {
                return result;
            }
            return onError.apply(result.subject());
        } else {
            if ( action ==null) {
                return result;
            }
            return action.apply(result.subject());
        }
    }
    public String display() {
        return display("");
    }
    public String display(String how ) {
        boolean doDisplay = doNotList;
        if ( how.toLowerCase().contains("force")) {
            doDisplay = true;
        }
        if ( doDisplay ) return "*" + name + "* " + description;
        return "";
    }
    /* Constructors */
    private void emptyInit(boolean doIncrementOrder) {
        name = "";
        description = "";
        if (doIncrementOrder) order = naturalOrder++;
        doNotList = true;
        action = null;
        onError = null;
        verifier = null;
    }
    public Command() {
       emptyInit(true);
    }
    public Command(String name) {
        emptyInit(true);
        this.name = name;
    }
    public Command(String name, int order) {
        emptyInit(false);
        this.name = name;
        this.order = order;
    }
    public Command(String name, String description, Adjutant action) {
        emptyInit(true);
        this.name = name;
        this.description = description;
        doNotList = false;
        this.action = action;
    }
    public Command(String name, String description, int order, Adjutant action) {
        emptyInit(false);
        this.order = order;
        this.name = name;
        this.description = description;
        doNotList = false;
        this.action = action;
    }
    public Command(String name, String description, Adjutant action, Adjutant onError, Adjutant verifier) {
        order = naturalOrder++;
        this.name = name;
        this.description = description;
        doNotList = false;
        this.action = action;
        this.onError = onError;
        this.verifier = verifier;
    }
    public Command(String name, String description, int order, Adjutant action, Adjutant onError, Adjutant verifier) {
        this.order = order;
        this.name = name;
        this.description = description;
        doNotList = false;
        this.action = action;
        this.onError = onError;
        this.verifier = verifier;
    }
    @Override
    public int compareTo ( Command otherCommand ) {
        if ( order > otherCommand.getOrder()) {
            return 1;
        }
        if ( order < otherCommand.getOrder()) {
            return -1;
        }
        return name.compareTo( otherCommand.getName() );
    }

    /* Getters */
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getPrompt() {
        return description;
    }
    public boolean getDoNotList() {
        return doNotList;
    }
    public int getOrder() {
        return order;
    }
    public Adjutant getAction() {
        return action;
    }
    public Adjutant getOnError() {
        return onError;
    }
    public Adjutant getVerifier() {
        return verifier;
    }

    /* Builder-style Setters */
    public Command setName(String name) {
        this.name = name;
        return this;
    }
    public Command setDescription(String description) {
        this.description = description;
        return this;
    }
    public Command setPrompt
            (String description) {
        this.description = description;
        return this;
    }
    public Command setDoNotList( boolean doNotList) {
        this.doNotList = doNotList;
        return this;
    }
    public Command setOrder(int order) {
        this.order = order;
        return this;
    }
    public Command setOnError (Adjutant newFunc) {
        this.onError = newFunc;
        return this;
    }
    public Command setAction(Adjutant newFunc) {
        this.action = newFunc;
        return this;
    }
    public Command setVerifier (Adjutant newFunc) {
        this.verifier = newFunc;
        return this;
    }
    public TextStyle getNameStyle() {
        return nameStyle;
    }
    public Command setCustomNameStyle(TextStyle style) {
        nameStyle = style;
        return this;
    }
    public TextStyle getDescriptionStyle() {
        return descriptionStyle;
    }
    public Command setCustomDescriptionStyle(TextStyle style) {
        descriptionStyle = style;
        return this;
    }

}
