package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.ColorPalets;

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
import java.util.function.Supplier;

public class ColorPalette {
    private final ColorPalette BASE;
    private final ColorPalette override;
    private final Map<String, TextColor> colors;

    public ColorPalette(ColorPalette BASE, Map<String, TextColor> colors) {
        this.BASE = BASE;
        this.colors = colors;
        this.override = null;
    }

    public ColorPalette(ColorPalette BASE, Map<String, TextColor> colors, ColorPalette override) {
        this.BASE = BASE;
        this.colors = colors;
        this.override = override;
    }

    // ======== Base Colors ========
    public TextColor BLACK() { return get("black"); }
    public TextColor DARK_BLUE() { return get("dark_blue"); }
    public TextColor DARK_GREEN() { return get("dark_green"); }
    public TextColor DARK_AQUA() { return get("dark_aqua"); }
    public TextColor DARK_RED() { return get("dark_red"); }
    public TextColor DARK_PURPLE() { return get("dark_purple"); }
    public TextColor GOLD() { return get("gold"); }
    public TextColor GRAY() { return get("gray"); }
    public TextColor DARK_GRAY() { return get("dark_gray"); }
    public TextColor BLUE() { return get("blue"); }
    public TextColor GREEN() { return get("green"); }
    public TextColor AQUA() { return get("aqua"); }
    public TextColor RED() { return get("red"); }
    public TextColor LIGHT_PURPLE() { return get("light_purple"); }
    public TextColor YELLOW() { return get("yellow"); }
    public TextColor WHITE() { return get("white"); }

    // ======== Primary Variants ========
    public TextColor PRIMARY() {
        return getColorOrFallback("primary", this::WHITE);
    }
    public TextColor PRIMARY_LIGHT() {
        return getColorOrFallback("primary_light", this::PRIMARY);
    }
    public TextColor PRIMARY_LIGHTER() {
        return getColorOrFallback("primary_lighter", this::PRIMARY_LIGHT);
    }
    public TextColor PRIMARY_DARK() {
        return getColorOrFallback("primary_dark", this::PRIMARY);
    }
    public TextColor PRIMARY_DARKER() {
        return getColorOrFallback("primary_darker", this::PRIMARY_DARK);
    }

    // ======== Secondary Variants ========
    public TextColor SECONDARY() {
        return getColorOrFallback("secondary", this::PRIMARY);
    }
    public TextColor SECONDARY_LIGHT() {
        return getColorOrFallback("secondary_light", this::SECONDARY);
    }
    public TextColor SECONDARY_LIGHTER() {
        return getColorOrFallback("secondary_lighter", this::SECONDARY_LIGHT);
    }
    public TextColor SECONDARY_DARK() {
        return getColorOrFallback("secondary_dark", this::SECONDARY);
    }
    public TextColor SECONDARY_DARKER() {
        return getColorOrFallback("secondary_darker", this::SECONDARY_DARK);
    }

    // ======== Tertiary Variants ========
    public TextColor TERTIARY() {
        return getColorOrFallback("tertiary", this::SECONDARY);
    }
    public TextColor TERTIARY_LIGHT() {
        return getColorOrFallback("tertiary_light", this::TERTIARY);
    }
    public TextColor TERTIARY_LIGHTER() {
        return getColorOrFallback("tertiary_lighter", this::TERTIARY_LIGHT);
    }
    public TextColor TERTIARY_DARK() {
        return getColorOrFallback("tertiary_dark", this::TERTIARY);
    }
    public TextColor TERTIARY_DARKER() {
        return getColorOrFallback("tertiary_darker", this::TERTIARY_DARK);
    }

    // ======== Disabled ========
    public TextColor DISABLED() {
        return get("disabled");
    }
    public TextColor DISABLED_LIGHT() {
        return getColorOrFallback("disabled_light", this::DISABLED);
    }
    public TextColor DISABLED_DARK() {
        return getColorOrFallback("disabled_dark", this::DISABLED);
    }

    // ======== Error, Success, Warning, Hint ========
    public TextColor ERROR() {
        return getColorOrFallback("error", this::RED);
    }
    public TextColor FATAL_ERROR() {
        return getColorOrFallback("fatal_error", this::ERROR);
    }

    public TextColor SUCCESS() {
        return getColorOrFallback("success", this::GREEN);
    }
    public TextColor SUCCESS_LIGHT() {
        return getColorOrFallback("success_light", this::SUCCESS);
    }
    public TextColor SUCCESS_DARK() {
        return getColorOrFallback("success_dark", this::SUCCESS);
    }

    public TextColor WARNING() {
        return getColorOrFallback("warning", this::YELLOW);
    }
    public TextColor WARNING_LIGHT() {
        return getColorOrFallback("warning_light", this::WARNING);
    }
    public TextColor WARNING_DARK() {
        return getColorOrFallback("warning_dark", this::WARNING);
    }

    public TextColor HINT() {
        return getColorOrFallback("hint", this::GRAY);
    }
    public TextColor HINT_LIGHT() {
        return getColorOrFallback("hint_light", this::HINT);
    }
    public TextColor HINT_DARK() {
        return getColorOrFallback("hint_dark", this::HINT);
    }

    public TextColor MODIFIER() {
        return getColorOrFallback("modifier", this::LIGHT_PURPLE);
    }
    public TextColor MODIFIER_LIGHT() {
        return getColorOrFallback("modifier_light", this::MODIFIER);
    }
    public TextColor MODIFIER_DARK() {
        return getColorOrFallback("modifier_dark", this::MODIFIER);
    }

    // ======== Helper Methods ========

    private TextColor getColorOrFallback(String key, Supplier<TextColor> fallback) {
        TextColor color = get(key);
        return color != null ? color : fallback.get();
    }

    public TextColor get(String color) {
        String colorName = color.toUpperCase();
        if (override != null && override.getColors().containsKey(colorName)) {
            TextColor overrideColor = override.get(colorName);
            if (overrideColor != null) return overrideColor;
        }else if (colors != null && colors.containsKey(colorName)) {
            TextColor customColor = colors.get(colorName);
            if (customColor != null) return customColor;
        } else if (BASE != null) {
            TextColor inherited = BASE.get(colorName);
            if (inherited != null) return inherited;
        }

        return switch (colorName) {
            case "BLACK" -> NamedTextColor.BLACK;
            case "DARK_BLUE" -> NamedTextColor.DARK_BLUE;
            case "DARK_GREEN" -> NamedTextColor.DARK_GREEN;
            case "DARK_AQUA" -> NamedTextColor.DARK_AQUA;
            case "DARK_RED" -> NamedTextColor.DARK_RED;
            case "DARK_PURPLE" -> NamedTextColor.DARK_PURPLE;
            case "GOLD" -> NamedTextColor.GOLD;
            case "GRAY" -> NamedTextColor.GRAY;
            case "DARK_GRAY" -> NamedTextColor.DARK_GRAY;
            case "BLUE" -> NamedTextColor.BLUE;
            case "GREEN" -> NamedTextColor.GREEN;
            case "AQUA" -> NamedTextColor.AQUA;
            case "RED" -> NamedTextColor.RED;
            case "LIGHT_PURPLE" -> NamedTextColor.LIGHT_PURPLE;
            case "YELLOW" -> NamedTextColor.YELLOW;
            case "WHITE" -> NamedTextColor.WHITE;
            default -> null;
        };
    }

    public static Component applyPattern(Component original, ColorPaletteRepeatOptions... options) {
        List<Component> charComponents = splitToCharacterComponents(original);

        Component output = Component.empty();

        int charIndex = 0;
        int patternIndex = 0;

        while (charIndex < charComponents.size()) {
            ColorPaletteRepeatOptions option = options[patternIndex];

            int repeatCount = option.repeat();
            for (int i = 0; i < repeatCount && charIndex < charComponents.size(); i++, charIndex++) {
                Component c = charComponents.get(charIndex);

                var styleBuilder = c.style().toBuilder();
                for (Map.Entry<TextDecoration, TextDecoration.State> decoEntry : option.decorations().entrySet()) {
                    styleBuilder = styleBuilder.decoration(decoEntry.getKey(), decoEntry.getValue());
                }
                c = c.style(styleBuilder.build());
                c = c.color(option.color());

                output = output.append(c);
            }

            // Move to the next option, looping back to start if needed
            patternIndex = (patternIndex + 1) % options.length;
        }

        return output;
    }

    // Helper function that splits a Component into a list of Components for each character,
// preserving the original styles, events, etc.
    public static List<Component> splitToCharacterComponents(Component component) {
        List<Component> result = new ArrayList<>();

        if (component instanceof TextComponent textComponent) {
            String content = textComponent.content();

            for (int i = 0; i < content.length(); i++) {
                // Extract one character as a new component preserving original style and events
                TextComponent charComp = Component.text(content.charAt(i))
                        .style(textComponent.style());
                result.add(charComp);
            }

            // Recurse for children components (if any)
            for (Component child : component.children()) {
                result.addAll(splitToCharacterComponents(child));
            }
        } else {
            // For non-text components, either handle accordingly or just add them as is
            // (You might want to support other types depending on your use case)
            result.add(component);
        }

        return result;
    }

    public Map<String, TextColor> getColors() {
        return new HashMap<>(colors);
    }

    @Contract(value = "_ -> new")
    public ColorPalette override(ColorPalette override) {
        return new ColorPalette(this, getColors(), override);
    }
    @Contract(value = "_,_ -> new")
    public ColorPalette addOverride(String key, TextColor value) {
        Map<String, TextColor> overrideColors = new HashMap<>(override.getColors());
        overrideColors.put(key, value);
        return new ColorPalette(this, getColors(), new ColorPalette(null, overrideColors));
    }
    @Contract(value = "_ -> new")
    public ColorPalette addOverrides(Map<String, TextColor> colors) {
        Map<String, TextColor> overrideColors = new HashMap<>(override.getColors());
        overrideColors.putAll(colors);
        return new ColorPalette(this, getColors(), new ColorPalette(null, overrideColors));
    }
    @Contract(value = "_ -> new")
    public ColorPalette removeOverride(String key) {
        Map<String, TextColor> overrideColors = new HashMap<>(override.getColors());
        overrideColors.remove(key);
        return new ColorPalette(this, getColors(), new ColorPalette(null, overrideColors));
    }
    @Contract(value = "_ -> new")
    public ColorPalette removeOverrides(List<String> colors) {
        Map<String, TextColor> overrideColors = new HashMap<>(override.getColors());
        overrideColors.keySet().removeAll(colors);
        return new ColorPalette(this, getColors(), new ColorPalette(null, overrideColors));
    }

    public ColorPalette override() {
        return override;
    }

}
