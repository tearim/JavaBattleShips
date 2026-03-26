package commonutils.menudispatcher;

public record Message<T>(String subject, T payload, boolean isError) {
    public Message(String subject) {
        this(subject, null, false);
    }

    public Message(String subject, boolean isError) {
        this(subject, null, isError);
    }

    public Message(String subject, T t) {
        this(subject, t, false);
    }

    public String display() {
        return (isError) ? "ERROR! " + subject : subject;
    }
}
