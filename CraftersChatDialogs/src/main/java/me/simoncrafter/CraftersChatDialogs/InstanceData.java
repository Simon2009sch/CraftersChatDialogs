package me.simoncrafter.CraftersChatDialogs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class InstanceData {

    private static Plugin plugin;

    public static void register(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new Listeners(), plugin);
        setPlugin(plugin);
    }


    public static Plugin getPlugin() {
        return plugin;
    }

    public static void setPlugin(Plugin plugin) {
        InstanceData.plugin = plugin;
    }


}
