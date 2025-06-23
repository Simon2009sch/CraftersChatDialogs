package me.simoncrafter.libraryTestPlugin;

import me.simoncrafter.CraftersCommandUtils.InstanceData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginCommand("dialogtest").setExecutor(new testCommand());
        InstanceData.register(this);
    }

    @Override
    public void onDisable() {

    }


    static public Main getInstance() { return getPlugin(Main.class); }
}
