package JavaBattleShips.relauncher;

import java.io.File;
import java.util.Map;

public class CLILauncher {
    private String getAbsoluteDir() {
        return this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
    }
    public void launch(String[] args) {
        if ( args.length != 0 ) return;
        try {
            System.out.println(getAbsoluteDir());
            System.out.println(   "/K java -classpath "+getAbsoluteDir()+";"+getAbsoluteDir()+"dependency/jnativehook-2.2.2.jar --enable-native-access=ALL-UNNAMED JavaBattleShips.UltraVersenker -l \"");
            ProcessBuilder pb = new ProcessBuilder("cmd","/c start cmd " +
                    "/K java -classpath "+getAbsoluteDir()+";"+getAbsoluteDir().replace("classes", "dependency")+File.separator+"jnativehook-2.2.2.jar --enable-native-access=ALL-UNNAMED JavaBattleShips.UltraVersenker -l \""
            );
            Map<String, String> env = pb.environment();
            env.put("JAVA_HOME", System.getProperty("java.home"));
            env.put("PATH", System.getProperty("java.home") +"\\bin;" + System.getProperty("PATH"));

            pb.directory(new File(getAbsoluteDir()))
                    .start();

        } catch (Exception ex) {
            System.out.println("Launch failed: " + ex.getMessage());
        }
    }
}
