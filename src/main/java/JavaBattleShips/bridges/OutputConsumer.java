package JavaBattleShips.bridges;

import JavaBattleShips.clrender.GameRenderer;
import JavaBattleShips.logic.GameLogic;
import commonutils.menudispatcher.Message;

public abstract  class OutputConsumer {
    public abstract void consume (GameLogic logic, Message<?> messageToOutput);
    public abstract void showMainMenu();
    public abstract void showOptionsMenu();
    public abstract String getOutcome();
    public abstract GameRenderer getRenderer();

}
