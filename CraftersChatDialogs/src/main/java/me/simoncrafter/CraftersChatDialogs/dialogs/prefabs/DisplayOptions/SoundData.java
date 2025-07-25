package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions;

import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.SoundAction;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.jetbrains.annotations.Contract;

public class SoundData {
    private final Sound sound;
    private final SoundCategory category;
    private final float volume;
    private final float pitch;

    public SoundData(Sound sound, SoundCategory category, float volume, float pitch) {
        this.sound = sound;
        this.category = category;
        this.volume = volume;
        this.pitch = pitch;
    }

    public SoundData(Sound sound) {
        this(sound, SoundCategory.MASTER, 1, 1);
    }

    public SoundData(Sound sound, float volume) {
        this(sound, SoundCategory.MASTER, volume, 1);
    }

    public SoundData(Sound sound, float volume, float pitch) {
        this(sound, SoundCategory.MASTER, volume, pitch);
    }

    public SoundData(Sound sound, SoundCategory category) {
        this(sound, category, 1, 1);
    }

    public SoundData(Sound sound, SoundCategory category, float volume) {
        this(sound, category, volume, 1);
    }

    public Sound sound() {
        return sound;
    }
    public SoundCategory category() {
        return category;
    }
    public float volume() {
        return volume;
    }
    public float pitch() {
        return pitch;
    }

    public SoundAction toSoundAction() {
        return SoundAction.create(sound, volume, pitch, category);
    }

    @Contract(value = "_ -> new")
    public SoundData sound(Sound sound) {
        return new SoundData(sound, category, volume, pitch);
    }
    @Contract(value = "_ -> new")
    public SoundData category(SoundCategory category) {
        return new SoundData(sound, category, volume, pitch);
    }
    @Contract(value = "_ -> new")
    public SoundData volume(float volume) {
        return new SoundData(sound, category, volume, pitch);
    }
    @Contract(value = "_ -> new")
    public SoundData pitch(float pitch) {
        return new SoundData(sound, category, volume, pitch);
    }
}
