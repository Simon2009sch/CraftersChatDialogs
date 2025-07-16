package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ConfigEditPlayerData {

    private Map<String, Object> settings = new HashMap<>(); // <path, value>
    private int line = 0;
    private String path = "";

    public ConfigEditPlayerData(int line, String path) {
        this.line = line;
        this.path = path;
    }

    public ConfigEditPlayerData() {}

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ConfigEditPlayerData clone() {
        return new ConfigEditPlayerData(line, path);
    }

    public Object getSetting(String path) {
        return settings.get(path);
    }
    public Object getSettingOrDefault(String path, Object defaultValue) {
        return settings.getOrDefault(path, defaultValue);
    }
    public void setSetting(String path, Object value) {
        settings.put(path, value);
    }

}
