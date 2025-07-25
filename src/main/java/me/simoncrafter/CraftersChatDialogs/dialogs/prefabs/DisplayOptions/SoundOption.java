package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions;

import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.SoundAction;
import org.bukkit.Sound;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;

public class SoundOption {
    private final Map<String, SoundData> sounds;

    public SoundOption(Map<String, SoundData> sounds) {
        this.sounds = sounds;
    }

    public SoundData getSound(String name) {
        return sounds.getOrDefault(name.toUpperCase(), new SoundData(Sound.UI_BUTTON_CLICK, 0.0f));
    }
    @Contract("_ -> new")
    public SoundOption putSound(String name, SoundData sound) {
        Map<String, SoundData> newMap = new HashMap<>(sounds);
        newMap.put(name.toUpperCase(), sound);
        return new SoundOption(newMap);
    }
    @Contract("_ -> new")
    public SoundOption putSound(Map<String, SoundData> sounds) {
        Map<String, SoundData> newMap = new HashMap<>(this.sounds);
        newMap.putAll(sounds);
        return new SoundOption(newMap);
    }
    @Contract("_ -> new")
    public SoundOption removeSound(String name) {
        Map<String, SoundData> newMap = new HashMap<>(sounds);
        newMap.remove(name.toUpperCase());
        return new SoundOption(newMap);
    }

    public SoundData CLICK() {
        return getSound("CLICK");
    }
    public SoundData CLICk_ON() {
        return getSound("CLICk_ON");
    }
    public SoundData CLICk_OFF() {
        return getSound("CLICk_OFF");
    }
    public SoundData ERROR() {
        return getSound("ERROR");
    }
    public SoundData SUCCESS() {
        return getSound("SUCCESS");
    }
    public SoundData SCROLL() {
        return getSound("SCROLL");
    }
    public SoundData INPUT_REQUIRED() {
        return getSound("INPUT_REQUIRED");
    }


}
