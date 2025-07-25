package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SoundAction implements IAction {

    private Sound sound = null;
    private SoundCategory category = SoundCategory.MASTER;
    private float volume = 1;
    private float pitch = 1;
    private boolean disabled = false;

    private SoundAction(@NotNull Sound sound) {
        this.sound = sound;
    }

    private SoundAction() {
    }

    @Contract("_ -> new")
    public static @NotNull SoundAction create(@NotNull Sound sound) {
        return create(sound, 1, 1);
    }
    @Contract("_, _ -> new")
    public static @NotNull SoundAction create(@NotNull Sound sound, float volume) {
        return create(sound, volume, 1);
    }
    @Contract("_, _, _ -> new")
    public static @NotNull SoundAction create(@NotNull Sound sound, float volume, float pitch) {
        SoundAction action = new SoundAction(sound);
        action.volume = volume;
        action.pitch = pitch;
        return action;
    }
    @Contract("_, _, _ -> new")
    public static @NotNull SoundAction create(@NotNull Sound sound, float volume, float pitch, SoundCategory category) {
        SoundAction action = new SoundAction(sound);
        action.volume = volume;
        action.pitch = pitch;
        action.category = category;
        return action;
    }


    @Contract("-> new")
    public static @NotNull SoundAction create() {
        return new SoundAction();
    }

    
    @Contract(value = "_ -> this", mutates = "this")
    public SoundAction sound(@NotNull Sound sound) {
        this.sound = sound;
        return this;
    }
    
    @Contract(value = "_ -> this", mutates = "this")
    public SoundAction category(@NotNull SoundCategory category) {
        this.category = category;
        return this;
    }
    
    @Contract(value = "_ -> this", mutates = "this")
    public SoundAction volume(float volume) {
        this.volume = volume;
        return this;
    }
    
    @Contract(value = "_ -> this", mutates = "this")
    public SoundAction pitch(float pitch) {
        this.pitch = pitch;
        return this;
    }
    
    
    public Sound sound() {
        return sound;
    }
    public float volume() {
        return volume;
    }
    public float pitch() {
        return pitch;
    }
    public SoundCategory category() {
        return category;
    }
    
    
    
    
    @Override
    public void run(Player player) {
        if (disabled) {return;}
        player.playSound(player.getLocation().clone().add(0, 1, 0), sound, category, volume, pitch);
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public IAction clone() {
        SoundAction clone = new SoundAction(sound);
        clone.setDisabled(isDisabled());
        clone.category(category);
        clone.volume(volume);
        clone.pitch(pitch);
        return clone;
    }
}
