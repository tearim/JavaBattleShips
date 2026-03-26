package commonutils.menudispatcher.extenders;

import commonutils.menudispatcher.Message;
import commonutils.menudispatcher.State;

public class Commands {
    /* Errors */
    public static Message<String> retry(String subject) {
        return new Message<>(subject, "retrymsg", true);
    }
    public static Message<String> retry() {
        return new Message<>("", "retry", true);
    }
    public static Message<String> interrupt() {
        return new Message<>("", "interrupt", true);
    }
    public static Message<String> interrupt(String subject) {
        return new Message<>(subject, "interrupt", true);
    }
    public static Message<String> error() {
        return new Message<>("", true);
    }
    public static Message<String> error(String subject) {
        return new Message<>(subject, State.PRINTLN, true);
    }
    public static Message<String> recover() {
        return new Message<>( "", State.REENTER, true );
    }
    public static Message<String> recover(String message) {
        return new Message<>( message, State.REENTER, true );
    }
    public static Message<String> recoverAfterPrompt() {
        return new Message<>( "", State.WAIT_REENTER, true );
    }
    public static Message<String> recoverAfterPrompt(String message) {
        return new Message<>( message, State.WAIT_REENTER, true );
    }
    /* Commands */
    public static Message<String> exit() {
        return new Message<>("", State.EXIT);
    }
    public static Message<String> exit(String subject) {
        return new Message<>(subject, State.EXIT);
    }
    public static Message<String> print(String subject) {
        return new Message<>(subject, State.PRINT);
    }
    public static Message<String> println(String subject) {
        return new Message<>(subject, State.PRINTLN);
    }
    public static Message<String> message(String subject) {
        return new Message<>(subject, State.PRINTLN);
    }
    public static Message<String> message(String subject, boolean isError) {
        return new Message<>(subject, isError);
    }
    public static Message<String> execute(String command) {
        return new Message<>(command, State.EXECUTE);
    }
    public static Message<String> execute(String command, String message) {
        return new Message<>(command, State.EXECUTE + message);
    }
    public static Message<String> reenter() {
        return new Message<>( "", State.REENTER );
    }
    public static Message<String> reenter(String message) {
        return new Message<>( message, State.REENTER );
    }
    public static Message<String> reenterAfterPrompt() {
        return new Message<>( "", State.WAIT_REENTER );
    }
    public static Message<String> reenterAfterPrompt(String message) {
        return new Message<>( message, State.WAIT_REENTER );
    }

}
