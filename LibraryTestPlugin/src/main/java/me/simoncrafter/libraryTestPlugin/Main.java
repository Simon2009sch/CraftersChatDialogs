package me.simoncrafter.libraryTestPlugin;

import me.simoncrafter.CraftersChatDialogs.InstanceData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static testCommand command = new testCommand();

    @Override
    public void onEnable() {
        Bukkit.getPluginCommand("dialogtest").setExecutor(command);
        InstanceData.register(this);
        config_manager.getInstance().load();
    }

    @Override
    public void onDisable() {

    }
    public static void reload() {
        command.fileReloaded = true;
        config_manager.getInstance().load();
    }

    static public Main getInstance() { return getPlugin(Main.class); }
}
