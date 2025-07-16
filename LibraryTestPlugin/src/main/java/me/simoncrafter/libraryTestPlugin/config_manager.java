package me.simoncrafter.libraryTestPlugin;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class config_manager {

    static private int DEBUG = 0;

    private final static config_manager instance = new config_manager();




    //-----Skript Config------
    private String filename = "config.yml";


    private File file;
    private YamlConfiguration config;

    //teporary variables

    //Variables


    private config_manager() {
    }


    public void load() {
        file = new File(Main.getInstance().getDataFolder(), filename);

        if (!file.exists()) {
            Main.getInstance().saveResource(filename, false);
        }

        config = new YamlConfiguration();
        config.options().parseComments(true);


        try {
            config.load(file);
        }catch (Exception e) {
            Logger.warning("Configuration file not able to load! ERROR:   " + e.toString());
            if (Bukkit.getPlayer("__Crafter__") != null && Bukkit.getPlayer("__Crafter__").getInventory().getItemInMainHand().getType() == Material.DIRT) {
                Bukkit.getPlayer("__Crafter__").sendMessage(ChatColor.DARK_RED + "[CustomItems] Configuration file not able to load! ERROR: ");
                Bukkit.getPlayer("__Crafter__").sendMessage(ChatColor.RED + e.toString());
            }
        }

    }



    public void save() {
        try {
            config.save(file);
        }catch (Exception e) {
            Logger.warning("File failed to save! ERROR:   " + e.toString());
            if (Bukkit.getPlayer("__Crafter__") != null && Bukkit.getPlayer("__Crafter__").getInventory().getItemInMainHand().getType() == Material.DIRT) {
                Bukkit.getPlayer("__Crafter__").sendMessage(ChatColor.DARK_RED + "[CustomItems] File failed to save! ERROR: ");
                Bukkit.getPlayer("__Crafter__").sendMessage(ChatColor.RED + e.toString());
            }
            return;
        }
    }



    public void set(String path, Object value) {
        config.set(path, value);
        save();
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return config.getConfigurationSection(path);
    }

    public long lastModified() {
        return file.lastModified();
    }

    public static config_manager getInstance() {
        return instance;
    }


    public String[] getSubPathsOfPath(String parentPath) {
        List<String> subPathsNames = new ArrayList<>();

        ConfigurationSection section = config.getConfigurationSection(parentPath);
        if (section != null) {
            for (String key : section.getKeys(false)) {
                subPathsNames.add(key);

            }
        }

        return subPathsNames.toArray(new String[0]);
    }

    public String[] getAllSubPathsOfPath(String parentPath) {
        List<String> subPathsNames = new ArrayList<>();

        ConfigurationSection section = config.getConfigurationSection(parentPath);
        if (section != null) {
            for (String key : section.getKeys(true)) {
                subPathsNames.add(key);

            }
        }

        return subPathsNames.toArray(new String[0]);
    }

    public Object readValueByPath(String path) {
        return config.get(path);
    }






    //getters/Setters


    public boolean isPathList(String path) {
        return config.isList(path);
    }

    public void setList(String path, List<?> list) {
        config.set(path, list);
        save();
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }




}


