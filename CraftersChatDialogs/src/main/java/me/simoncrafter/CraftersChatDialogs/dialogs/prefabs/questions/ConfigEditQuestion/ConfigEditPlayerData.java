package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class ConfigEditPlayerData {

    private Map<String, Object> settings = new HashMap<>(); // <path, value>
    private Map<String, Integer> lines = new HashMap<>();
    private String path = "";

    public ConfigEditPlayerData(Map<String, Integer> line, String path) {
        this.lines = line;
        this.path = path;
    }

    public ConfigEditPlayerData() {}

    public Map<String, Integer> getLines() {
        return new HashMap<>(lines);
    }
    public int getLineAtCurrentPath() {
        return lines.getOrDefault(path, 0);
    }

    public void setLineAtCurrentPath(int line) {
        this.lines.put(path, line);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ConfigEditPlayerData clone() {
        return new ConfigEditPlayerData(lines, path);
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
