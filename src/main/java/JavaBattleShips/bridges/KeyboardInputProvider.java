package JavaBattleShips.bridges;

public class KeyboardInputProvider extends InputProvider {
    OutputConsumer output;
    public KeyboardInputProvider(OutputConsumer output) {
        this.output = output;
    }

    @Override
    public String userChoiceFrom(String context, Runnable effects) {
        effects.run();
        return output.getOutcome();
    }
}
