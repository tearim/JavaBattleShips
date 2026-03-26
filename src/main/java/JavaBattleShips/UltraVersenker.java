package JavaBattleShips;

import JavaBattleShips.bridges.ChaxelScreenManager;
import JavaBattleShips.bridges.ChaxelVisualEffects;
import JavaBattleShips.bridges.KeyboardInputProvider;
import JavaBattleShips.keymaster.KeyMaster;
import JavaBattleShips.logic.GameLogic;
import JavaBattleShips.relauncher.CLILauncher;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class UltraVersenker {

    public static void main(String[] args) {
        boolean disableCLILaunch = true;
        disableCLILaunch = false;
        if (!disableCLILaunch ) if (args.length == 0) {
            System.out.println("This program is not supposed to run in a built-in console. Currently, this program will run under Windows only. And now, we're trying to launch cmd..");
            CLILauncher cliLauncher = new CLILauncher();
            cliLauncher.launch(args);
            System.exit(0);
        }

        KeyMaster keyMaster = new KeyMaster();
        keyMaster.register();

        keyMaster.registerKeyRelease(NativeKeyEvent.VC_ESCAPE,
                () -> {
                    System.out.println("You pressed escape!");
                    System.exit(0);
                });
        ChaxelScreenManager output = new ChaxelScreenManager(keyMaster);
        GameLogic game = new GameLogic(new KeyboardInputProvider(output),
                output,
                new ChaxelVisualEffects(),
                keyMaster);

        game.beginGameProcess();

    }
}

