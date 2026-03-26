package JavaBattleShips.bridges;

import commonutils.menudispatcher.Message;

public class ChaxelVisualEffects extends VisualEffects{
    public Message<?> intro() {
        return new Message<>("begin", "hello");
    }
}
