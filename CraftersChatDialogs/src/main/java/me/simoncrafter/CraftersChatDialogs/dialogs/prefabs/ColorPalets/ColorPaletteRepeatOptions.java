package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.ColorPalets;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ColorPaletteRepeatOptions {
    private TextColor color = NamedTextColor.WHITE;
    private int repeat = 1;
    private Map<TextDecoration, TextDecoration.State> decorations = new HashMap<>();

    public ColorPaletteRepeatOptions(TextColor color, int repeat) {
        this.color = color;
        this.repeat = repeat;
    }

    public Map<TextDecoration, TextDecoration.State> decorations() {
        return decorations;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public ColorPaletteRepeatOptions decorations(Map<TextDecoration, TextDecoration.State> decorations) {
        this.decorations = decorations;
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public ColorPaletteRepeatOptions decoration(TextDecoration decoration, boolean state) {
        this.decorations.put(decoration, TextDecoration.State.byBoolean(state));
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public ColorPaletteRepeatOptions decorate(TextDecoration decoration) {
        this.decorations.put(decoration, TextDecoration.State.TRUE);
        return this;
    }

    public TextColor color() {
        return color;
    }
    @Contract(value = "_ -> this", mutates = "this")

    public ColorPaletteRepeatOptions color(TextColor color) {
        this.color = color;
        return this;
    }

    public int repeat() {
        return repeat;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public ColorPaletteRepeatOptions repeat(int repeat) {
        this.repeat = repeat;
        return this;
    }
}
