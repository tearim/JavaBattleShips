package JavaBattleShips.keymaster;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;

public class KeyMaster implements NativeKeyListener {
    private final HashMap<Integer, HashMap<String, Runnable>> eventLinks;
    public static String CODE_ID_SEPARATOR = "::"; // all hail pa'amaim nequdotaim ;)
    public static final int KEY_PRESSED = NativeKeyEvent.NATIVE_KEY_PRESSED;
    public static final int KEY_RELEASED = NativeKeyEvent.NATIVE_KEY_RELEASED;
    public static final int KEY_TYPED = NativeKeyEvent.NATIVE_KEY_TYPED;


    public String registerKeyPress(int keyCode, Runnable runnable) {
        return registerKeyEvent(KEY_PRESSED, keyCode, runnable);
    }

    public String registerKeyRelease(int keyCode, Runnable runnable) {
        return registerKeyEvent(KEY_RELEASED, keyCode, runnable);
    }

    public String registerKeyType(int keyCode, Runnable runnable) {
        return registerKeyEvent(KEY_TYPED, keyCode, runnable);
    }

    public String registerKeyEvent (int type, int keyCode, Runnable runnable) {
        if ( !eventLinks.containsKey(keyCode) ) {
            eventLinks.put(keyCode, new HashMap<>());
        }
        String unique_ID = type + CODE_ID_SEPARATOR  +keyCode + CODE_ID_SEPARATOR + Instant.now().getEpochSecond();
        eventLinks.get(keyCode).put(unique_ID, runnable );
        return unique_ID;
    }

    public void removeKeyEvent(String unique_ID) {
        if (!unique_ID.contains(CODE_ID_SEPARATOR)) {
            return;
        }
        String[] bits = unique_ID.split(CODE_ID_SEPARATOR);
        try {
            eventLinks.get(Integer.parseInt(bits[1])).remove(unique_ID);
        } catch (Exception e) {
            //  ...
        }
    }
    public void removeALlforKey(int keyCode) {
        try {
            eventLinks.get(keyCode).clear();
        } catch (Exception e) {
            //
        }
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();
        LinkedList<Runnable> runnables = new LinkedList<>();
        if (eventLinks.containsKey(keyCode)) {
            eventLinks.get(keyCode).forEach((key, runnable) -> {
                if (key.startsWith(KEY_TYPED+CODE_ID_SEPARATOR) ) {
                    runnables.add(runnable);
                }
            });
        }
        for (Runnable runnable : runnables) {
            runnable.run();
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();
        LinkedList<Runnable> runnables = new LinkedList<>();
        if (eventLinks.containsKey(keyCode)) {
             eventLinks.get(keyCode).forEach((key, runnable) -> {
                 if (key.startsWith(KEY_RELEASED +CODE_ID_SEPARATOR) ) {
                     runnables.add(runnable);
                 }
             });
        }
        for (Runnable runnable : runnables) {
            runnable.run();
        }
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();
        LinkedList<Runnable> runnables = new LinkedList<>();
        if (eventLinks.containsKey(keyCode)) {
            eventLinks.get(keyCode).forEach((key, runnable) -> {
                if (key.startsWith(KEY_TYPED+CODE_ID_SEPARATOR) ) {
                    runnables.add(runnable);
                }
            });
        }
        for (Runnable runnable : runnables) {
            runnable.run();
        }

    }

    public void register() {
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(this);
    }

    public KeyMaster() {
        eventLinks =  new HashMap<>();
    }
}