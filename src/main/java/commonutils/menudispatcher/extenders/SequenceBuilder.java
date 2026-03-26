package commonutils.menudispatcher.extenders;


import commonutils.menudispatcher.Sequence;
import commonutils.menudispatcher.extenders.Commands;

public class SequenceBuilder {

    public static Sequence simple(String name, String leaveMessage) {
        return new Sequence(name)
                .setOnInterrupt(Commands::error)
                .setOnLeave(s -> {
                    return Commands.message(leaveMessage);
                });
    }

    public static Sequence simpleWithReenter(String name, String leaveMessage) {
        return new Sequence(name)
                .setOnInterrupt(Commands::recoverAfterPrompt)
                .setOnLeave(s -> {
                    return Commands.reenterAfterPrompt(leaveMessage);
                });
    }

}
