package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class DisplayOption {
    private final ColorPalette colorPalette;
    private final SoundOption soundOption;

    public DisplayOption(ColorPalette colorPalette, SoundOption soundOption) {
        this.colorPalette = colorPalette;
        this.soundOption = soundOption;
    }



    @Contract("_ -> new")
    public DisplayOption colorPalette(ColorPalette colorPalette) {
         return new DisplayOption(colorPalette, soundOption);
    }
    @Contract("_ -> new")
    public DisplayOption modifyColorPalette(Function<ColorPalette, ColorPalette> modifier) {
        return new DisplayOption(modifier.apply(colorPalette), soundOption);
    }
    public ColorPalette colorPalette() {
        return colorPalette;
    }


    @Contract("_ -> new")
    public DisplayOption soundOption(SoundOption soundOption) {
        return new DisplayOption(colorPalette, soundOption);
    }
    @Contract("_ -> new")
    public DisplayOption modifySoundOption(Function<SoundOption, SoundOption> modifier) {
        return new DisplayOption(colorPalette, modifier.apply(soundOption));
    }
    public SoundOption soundOption() {
        return soundOption;
    }
}
