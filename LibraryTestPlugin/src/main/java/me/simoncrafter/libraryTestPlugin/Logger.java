package me.simoncrafter.libraryTestPlugin;

public class Logger {
    static short DEBUG = -1;

    public static void info(String info) {
        Main.getInstance().getLogger().info(info);
    }

    public static void warning(String warning) {
        Main.getInstance().getLogger().warning(warning);
    }

    public static void normalDebug(String message) {
        if (DEBUG > 0) {
            return;
        }
        Main.getInstance().getLogger().info("[D] " + message);
    }

    public static void error(String error) {
        Main.getInstance().getLogger().severe(error);
    }
    public static void hardError(String error) {
        throw new RuntimeException(error);
    }

    public static void DEBUG(String debug) {
        if (DEBUG >= 1) {
            Main.getInstance().getLogger().info("[D] " +debug);
        }
    }
    public static void DEBUG(String debug, int level) {
        if (DEBUG >= level) {
            Main.getInstance().getLogger().info("[D" + level + "] " +debug);
        }
    }





}