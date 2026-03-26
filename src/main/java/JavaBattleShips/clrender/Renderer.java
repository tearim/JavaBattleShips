package JavaBattleShips.clrender;

import JavaBattleShips.chaxels.ChaxelScreen;
import JavaBattleShips.chaxels.ChaxelStrings;
import commonutils.Fansi;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Renderer {
    private long tick = 0;
    public void tick() {
        tick++;
    }
    public long getTick(){
        return tick;
    }
    private  int x;
    private  int y;

    public static final String RESET_SCREEN = Fansi.ESC_B + "2J";
    public static final String MOVE_TOBEGINNING = Fansi.ESC_B + "H";
    public static final int MAX_RENDER_WAIT = 5000;
    public Renderer reset() {
        System.out.println(RESET_SCREEN);
        return this;
    }
    public ChaxelScreen screen = null;
    public Renderer createScreen(int x, int y) {
        screen = new ChaxelScreen(x,y);
        return this;
    }
    public Renderer moveToBeginning() {
        System.out.println(RESET_SCREEN);
        System.out.println(MOVE_TOBEGINNING);
        System.out.println(Fansi.ESC_B + "3J");
        //System.out.println(
        return this;
    }
    public Renderer moveToXY(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
    public Renderer clearAll() {
        screen.getCanvas().reset(x, y);
        screen.removeAll();
        printScreen();
        return this;
    }

    public Renderer pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // ... nothing...
        }
        return this;
    }

    public Renderer printScreen() {
      //  System.out.print(RESET_SCREEN);
        System.out.print(MOVE_TOBEGINNING);
        System.out.println(screen.write().display());
        return this;
    }
    public Renderer print(String s) {
        screen.getCanvas().write(ChaxelStrings.toChaxels(s), x, y);
        y++;
        return this;
    }
    public Renderer print(Fansi f) {
        print(f.render());
        return this;
    }

    ScheduledFuture<?> redrawschedule;
    public Renderer registerRecurringRedraw(int millis, Runnable r) {
        tick();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        if ( millis < 0 ) millis = 0;
        if ( millis > MAX_RENDER_WAIT) millis = MAX_RENDER_WAIT;
        cancelRecurringRedraw();
        redrawschedule = scheduler.scheduleAtFixedRate(r, 0, millis, TimeUnit.MILLISECONDS);
        return this;
    }

    public ChaxelScreen getScreen() {
        return screen;
    }

    public Renderer cancelRecurringRedraw() {
        if (redrawschedule != null) {
            redrawschedule.cancel(false);
        }
        return this;
    }
}
