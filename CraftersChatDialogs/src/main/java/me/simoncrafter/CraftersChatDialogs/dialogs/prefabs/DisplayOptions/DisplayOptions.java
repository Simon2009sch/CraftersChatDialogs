package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;

import java.util.HashMap;
import java.util.Map;

public class DisplayOptions {

    public static final DisplayOption DEFAULT = new DisplayOption(ColorPalettes.NEUTRAL_GRAY, SoundOptions.DEFAULT);

    public static final class SoundOptions {
        public static final SoundOption DEFAULT = new SoundOption(Map.ofEntries(
                Map.entry("CLICK", new SoundData(Sound.UI_BUTTON_CLICK, 0.3f)),
                Map.entry("CLICK_ON", new SoundData(Sound.UI_BUTTON_CLICK, 0.3f, 0.6f)),
                Map.entry("CLICK_OFF", new SoundData(Sound.UI_BUTTON_CLICK, 0.3f, 0.5f)),
                Map.entry("ERROR", new SoundData(Sound.ENTITY_VILLAGER_NO, 0.3f, 1f)),
                Map.entry("INPUT_REQUIRED", new SoundData(Sound.UI_BUTTON_CLICK, 0.3f, 1.1f)),
                Map.entry("SUCCESS", new SoundData(Sound.ENTITY_ARROW_HIT_PLAYER, 0.3f, 1f)),
                Map.entry("SCROLL", new SoundData(Sound.ITEM_BOOK_PAGE_TURN))
        ));
    }

    public static final class ColorPalettes {
        private static final ColorPalette DEFAULT = new ColorPalette(null, new HashMap<>());


        public static final ColorPalette FUNCTIONAL_DEFAULT = new ColorPalette(DEFAULT, Map.ofEntries(
                Map.entry("DISABLED", NamedTextColor.DARK_GRAY),
                Map.entry("DISABLED_LIGHT", TextColor.color(NamedTextColor.DARK_GRAY.value() + 0x555555)),
                Map.entry("DISABLED_DARK", TextColor.color(NamedTextColor.DARK_GRAY.value() - 0x555555)),
                Map.entry("ERROR", NamedTextColor.RED),
                Map.entry("FATAL_ERROR", NamedTextColor.DARK_RED),
                Map.entry("SUCCESS", NamedTextColor.GREEN),
                Map.entry("SUCCESS_LIGHT", TextColor.color(NamedTextColor.GREEN.value() + 0x555555)),
                Map.entry("SUCCESS_DARK", TextColor.color(NamedTextColor.GREEN.value() - 0x555555)),
                Map.entry("WARNING", NamedTextColor.YELLOW),
                Map.entry("WARNING_LIGHT", TextColor.color(NamedTextColor.YELLOW.value() + 0x555555)),
                Map.entry("WARNING_DARK", TextColor.color(NamedTextColor.YELLOW.value() - 0x555555)),
                Map.entry("HINT", NamedTextColor.GRAY),
                Map.entry("HINT_LIGHT", TextColor.color(NamedTextColor.GRAY.value() + 0x555555)),
                Map.entry("HINT_DARK", TextColor.color(NamedTextColor.GRAY.value() - 0x555555)),
                Map.entry("MODIFIER", NamedTextColor.LIGHT_PURPLE),
                Map.entry("MODIFIER_LIGHT", TextColor.color(NamedTextColor.LIGHT_PURPLE.value() + 0x555555)),
                Map.entry("MODIFIER_DARK", TextColor.color(NamedTextColor.LIGHT_PURPLE.value() - 0x555555))
        ));

        public static final ColorPalette YELLOW_ISH = new ColorPalette(FUNCTIONAL_DEFAULT, Map.ofEntries(
                Map.entry("PRIMARY", NamedTextColor.GOLD),
                Map.entry("PRIMARY_LIGHT", TextColor.color(NamedTextColor.GOLD.value() + 0x555555)),
                Map.entry("PRIMARY_LIGHTER", TextColor.color(NamedTextColor.GOLD.value() + 0x333333)),
                Map.entry("PRIMARY_DARK", TextColor.color(NamedTextColor.GOLD.value() - 0x555555)),
                Map.entry("PRIMARY_DARKER", TextColor.color(NamedTextColor.GOLD.value() - 0x333333)),
                Map.entry("SECONDARY", NamedTextColor.YELLOW),
                Map.entry("SECONDARY_LIGHT", TextColor.color(NamedTextColor.YELLOW.value() + 0x555555)),
                Map.entry("SECONDARY_LIGHTER", TextColor.color(NamedTextColor.YELLOW.value() + 0x333333)),
                Map.entry("SECONDARY_DARK", TextColor.color(NamedTextColor.YELLOW.value() - 0x555555)),
                Map.entry("SECONDARY_DARKER", TextColor.color(NamedTextColor.YELLOW.value() - 0x333333)),
                Map.entry("TERTIARY", TextColor.fromHexString("#b37800")),
                Map.entry("TERTIARY_LIGHT", TextColor.color(TextColor.fromHexString("#b37800").value() + 0x555555)),
                Map.entry("TERTIARY_LIGHTER", TextColor.color(TextColor.fromHexString("#b37800").value() + 0x333333)),
                Map.entry("TERTIARY_DARK", TextColor.color(TextColor.fromHexString("#b37800").value() - 0x555555)),
                Map.entry("TERTIARY_DARKER", TextColor.color(TextColor.fromHexString("#b37800").value() - 0x333333))
        ));

        public static final ColorPalette COOL_BLUISH = new ColorPalette(FUNCTIONAL_DEFAULT, Map.ofEntries(
                Map.entry("PRIMARY", NamedTextColor.AQUA),
                Map.entry("PRIMARY_LIGHT", TextColor.color(NamedTextColor.AQUA.value() + 0x333333)),
                Map.entry("PRIMARY_LIGHTER", TextColor.color(NamedTextColor.AQUA.value() + 0x555555)),
                Map.entry("PRIMARY_DARK", TextColor.color(NamedTextColor.AQUA.value() - 0x333333)),
                Map.entry("PRIMARY_DARKER", TextColor.color(NamedTextColor.AQUA.value() - 0x555555)),

                Map.entry("SECONDARY", NamedTextColor.BLUE),
                Map.entry("SECONDARY_LIGHT", TextColor.color(NamedTextColor.BLUE.value() + 0x333333)),
                Map.entry("SECONDARY_LIGHTER", TextColor.color(NamedTextColor.BLUE.value() + 0x555555)),
                Map.entry("SECONDARY_DARK", TextColor.color(NamedTextColor.BLUE.value() - 0x333333)),
                Map.entry("SECONDARY_DARKER", TextColor.color(NamedTextColor.BLUE.value() - 0x555555)),

                Map.entry("TERTIARY", TextColor.fromHexString("#005577")),
                Map.entry("TERTIARY_LIGHT", TextColor.color(TextColor.fromHexString("#005577").value() + 0x333333)),
                Map.entry("TERTIARY_LIGHTER", TextColor.color(TextColor.fromHexString("#005577").value() + 0x555555)),
                Map.entry("TERTIARY_DARK", TextColor.color(TextColor.fromHexString("#005577").value() - 0x333333)),
                Map.entry("TERTIARY_DARKER", TextColor.color(TextColor.fromHexString("#005577").value() - 0x555555))
        ));

        public static final ColorPalette WARM_REDDISH = new ColorPalette(FUNCTIONAL_DEFAULT, Map.ofEntries(
                Map.entry("PRIMARY", NamedTextColor.RED),
                Map.entry("PRIMARY_LIGHT", TextColor.color(NamedTextColor.RED.value() + 0x555555)),
                Map.entry("PRIMARY_LIGHTER", TextColor.color(NamedTextColor.RED.value() + 0x333333)),
                Map.entry("PRIMARY_DARK", TextColor.color(NamedTextColor.RED.value() - 0x555555)),
                Map.entry("PRIMARY_DARKER", TextColor.color(NamedTextColor.RED.value() - 0x333333)),

                Map.entry("SECONDARY", TextColor.fromHexString("#cc5500")), // burnt orange
                Map.entry("SECONDARY_LIGHT", TextColor.color(TextColor.fromHexString("#cc5500").value() + 0x555555)),
                Map.entry("SECONDARY_LIGHTER", TextColor.color(TextColor.fromHexString("#cc5500").value() + 0x333333)),
                Map.entry("SECONDARY_DARK", TextColor.color(TextColor.fromHexString("#cc5500").value() - 0x555555)),
                Map.entry("SECONDARY_DARKER", TextColor.color(TextColor.fromHexString("#cc5500").value() - 0x333333)),

                Map.entry("TERTIARY", TextColor.fromHexString("#990000")), // dark red
                Map.entry("TERTIARY_LIGHT", TextColor.color(TextColor.fromHexString("#990000").value() + 0x555555)),
                Map.entry("TERTIARY_LIGHTER", TextColor.color(TextColor.fromHexString("#990000").value() + 0x333333)),
                Map.entry("TERTIARY_DARK", TextColor.color(TextColor.fromHexString("#990000").value() - 0x555555)),
                Map.entry("TERTIARY_DARKER", TextColor.color(TextColor.fromHexString("#990000").value() - 0x333333))
        ));

        public static final ColorPalette NEUTRAL_GRAY = new ColorPalette(FUNCTIONAL_DEFAULT, Map.ofEntries(
                Map.entry("PRIMARY", NamedTextColor.GRAY),
                Map.entry("PRIMARY_LIGHT", TextColor.color(NamedTextColor.GRAY.value() + 0x555555)),
                Map.entry("PRIMARY_LIGHTER", TextColor.color(NamedTextColor.GRAY.value() + 0x333333)),
                Map.entry("PRIMARY_DARK", TextColor.color(NamedTextColor.GRAY.value() - 0x555555)),
                Map.entry("PRIMARY_DARKER", TextColor.color(NamedTextColor.GRAY.value() - 0x333333)),

                Map.entry("SECONDARY", NamedTextColor.DARK_GRAY),
                Map.entry("SECONDARY_LIGHT", TextColor.color(NamedTextColor.DARK_GRAY.value() + 0x555555)),
                Map.entry("SECONDARY_LIGHTER", TextColor.color(NamedTextColor.DARK_GRAY.value() + 0x333333)),
                Map.entry("SECONDARY_DARK", TextColor.color(NamedTextColor.DARK_GRAY.value() - 0x555555)),
                Map.entry("SECONDARY_DARKER", TextColor.color(NamedTextColor.DARK_GRAY.value() - 0x333333)),

                Map.entry("TERTIARY", TextColor.fromHexString("#999999")),
                Map.entry("TERTIARY_LIGHT", TextColor.color(TextColor.fromHexString("#999999").value() + 0x555555)),
                Map.entry("TERTIARY_LIGHTER", TextColor.color(TextColor.fromHexString("#999999").value() + 0x333333)),
                Map.entry("TERTIARY_DARK", TextColor.color(TextColor.fromHexString("#999999").value() - 0x555555)),
                Map.entry("TERTIARY_DARKER", TextColor.color(TextColor.fromHexString("#999999").value() - 0x333333))
        ));

        public static final ColorPalette SOFT_PASTELS = new ColorPalette(FUNCTIONAL_DEFAULT, Map.ofEntries(
                Map.entry("PRIMARY", TextColor.fromHexString("#a8dadc")), // pastel cyan
                Map.entry("PRIMARY_LIGHT", TextColor.color(TextColor.fromHexString("#a8dadc").value() + 0x555555)),
                Map.entry("PRIMARY_LIGHTER", TextColor.color(TextColor.fromHexString("#a8dadc").value() + 0x333333)),
                Map.entry("PRIMARY_DARK", TextColor.color(TextColor.fromHexString("#a8dadc").value() - 0x555555)),
                Map.entry("PRIMARY_DARKER", TextColor.color(TextColor.fromHexString("#a8dadc").value() - 0x333333)),

                Map.entry("SECONDARY", TextColor.fromHexString("#f4a261")), // pastel orange
                Map.entry("SECONDARY_LIGHT", TextColor.color(TextColor.fromHexString("#f4a261").value() + 0x555555)),
                Map.entry("SECONDARY_LIGHTER", TextColor.color(TextColor.fromHexString("#f4a261").value() + 0x333333)),
                Map.entry("SECONDARY_DARK", TextColor.color(TextColor.fromHexString("#f4a261").value() - 0x555555)),
                Map.entry("SECONDARY_DARKER", TextColor.color(TextColor.fromHexString("#f4a261").value() - 0x333333)),

                Map.entry("TERTIARY", TextColor.fromHexString("#e76f51")), // pastel red-orange
                Map.entry("TERTIARY_LIGHT", TextColor.color(TextColor.fromHexString("#e76f51").value() + 0x555555)),
                Map.entry("TERTIARY_LIGHTER", TextColor.color(TextColor.fromHexString("#e76f51").value() + 0x333333)),
                Map.entry("TERTIARY_DARK", TextColor.color(TextColor.fromHexString("#e76f51").value() - 0x555555)),
                Map.entry("TERTIARY_DARKER", TextColor.color(TextColor.fromHexString("#e76f51").value() - 0x333333))
        ));
        public static final ColorPalette GREEN_ISH = new ColorPalette(FUNCTIONAL_DEFAULT, Map.ofEntries(
                Map.entry("PRIMARY", NamedTextColor.DARK_GREEN),
                Map.entry("PRIMARY_LIGHT", TextColor.color(NamedTextColor.DARK_GREEN.value() + 0x333333)),
                Map.entry("PRIMARY_LIGHTER", TextColor.color(NamedTextColor.DARK_GREEN.value() + 0x555555)),
                Map.entry("PRIMARY_DARK", TextColor.color(NamedTextColor.DARK_GREEN.value() - 0x333333)),
                Map.entry("PRIMARY_DARKER", TextColor.color(NamedTextColor.DARK_GREEN.value() - 0x555555)),

                Map.entry("SECONDARY", NamedTextColor.GREEN),
                Map.entry("SECONDARY_LIGHT", TextColor.color(NamedTextColor.GREEN.value() + 0x333333)),
                Map.entry("SECONDARY_LIGHTER", TextColor.color(NamedTextColor.GREEN.value() + 0x555555)),
                Map.entry("SECONDARY_DARK", TextColor.color(NamedTextColor.GREEN.value() - 0x333333)),
                Map.entry("SECONDARY_DARKER", TextColor.color(NamedTextColor.GREEN.value() - 0x555555)),

                Map.entry("TERTIARY", TextColor.fromHexString("#3a7d44")),
                Map.entry("TERTIARY_LIGHT", TextColor.color(TextColor.fromHexString("#3a7d44").value() + 0x333333)),
                Map.entry("TERTIARY_LIGHTER", TextColor.color(TextColor.fromHexString("#3a7d44").value() + 0x555555)),
                Map.entry("TERTIARY_DARK", TextColor.color(TextColor.fromHexString("#3a7d44").value() - 0x333333)),
                Map.entry("TERTIARY_DARKER", TextColor.color(TextColor.fromHexString("#3a7d44").value() - 0x555555))
        ));

        public static final ColorPalette PURPLE_ISH = new ColorPalette(FUNCTIONAL_DEFAULT, Map.ofEntries(
                Map.entry("PRIMARY", TextColor.fromHexString("#6a0dad")), // purple
                Map.entry("PRIMARY_LIGHT", TextColor.color(TextColor.fromHexString("#6a0dad").value() + 0x333333)),
                Map.entry("PRIMARY_LIGHTER", TextColor.color(TextColor.fromHexString("#6a0dad").value() + 0x555555)),
                Map.entry("PRIMARY_DARK", TextColor.color(TextColor.fromHexString("#6a0dad").value() - 0x333333)),
                Map.entry("PRIMARY_DARKER", TextColor.color(TextColor.fromHexString("#6a0dad").value() - 0x555555)),

                Map.entry("SECONDARY", TextColor.fromHexString("#9b30ff")), // medium purple
                Map.entry("SECONDARY_LIGHT", TextColor.color(TextColor.fromHexString("#9b30ff").value() + 0x333333)),
                Map.entry("SECONDARY_LIGHTER", TextColor.color(TextColor.fromHexString("#9b30ff").value() + 0x555555)),
                Map.entry("SECONDARY_DARK", TextColor.color(TextColor.fromHexString("#9b30ff").value() - 0x333333)),
                Map.entry("SECONDARY_DARKER", TextColor.color(TextColor.fromHexString("#9b30ff").value() - 0x555555)),

                Map.entry("TERTIARY", TextColor.fromHexString("#4b0082")), // indigo
                Map.entry("TERTIARY_LIGHT", TextColor.color(TextColor.fromHexString("#4b0082").value() + 0x333333)),
                Map.entry("TERTIARY_LIGHTER", TextColor.color(TextColor.fromHexString("#4b0082").value() + 0x555555)),
                Map.entry("TERTIARY_DARK", TextColor.color(TextColor.fromHexString("#4b0082").value() - 0x333333)),
                Map.entry("TERTIARY_DARKER", TextColor.color(TextColor.fromHexString("#4b0082").value() - 0x555555))
        ));

        public static final ColorPalette BROWN_ISH = new ColorPalette(FUNCTIONAL_DEFAULT, Map.ofEntries(
                Map.entry("PRIMARY", TextColor.fromHexString("#8b4513")), // saddle brown
                Map.entry("PRIMARY_LIGHT", TextColor.color(TextColor.fromHexString("#8b4513").value() + 0x333333)),
                Map.entry("PRIMARY_LIGHTER", TextColor.color(TextColor.fromHexString("#8b4513").value() + 0x555555)),
                Map.entry("PRIMARY_DARK", TextColor.color(TextColor.fromHexString("#8b4513").value() - 0x333333)),
                Map.entry("PRIMARY_DARKER", TextColor.color(TextColor.fromHexString("#8b4513").value() - 0x555555)),

                Map.entry("SECONDARY", TextColor.fromHexString("#a0522d")), // sienna
                Map.entry("SECONDARY_LIGHT", TextColor.color(TextColor.fromHexString("#a0522d").value() + 0x333333)),
                Map.entry("SECONDARY_LIGHTER", TextColor.color(TextColor.fromHexString("#a0522d").value() + 0x555555)),
                Map.entry("SECONDARY_DARK", TextColor.color(TextColor.fromHexString("#a0522d").value() - 0x333333)),
                Map.entry("SECONDARY_DARKER", TextColor.color(TextColor.fromHexString("#a0522d").value() - 0x555555)),

                Map.entry("TERTIARY", TextColor.fromHexString("#5c4033")), // dark brown
                Map.entry("TERTIARY_LIGHT", TextColor.color(TextColor.fromHexString("#5c4033").value() + 0x333333)),
                Map.entry("TERTIARY_LIGHTER", TextColor.color(TextColor.fromHexString("#5c4033").value() + 0x555555)),
                Map.entry("TERTIARY_DARK", TextColor.color(TextColor.fromHexString("#5c4033").value() - 0x333333)),
                Map.entry("TERTIARY_DARKER", TextColor.color(TextColor.fromHexString("#5c4033").value() - 0x555555))
        ));

        public static final class Overrides {
            public static final ColorPalette XYZ = new ColorPalette(null, Map.ofEntries(
                    Map.entry("X", NamedTextColor.RED),
                    Map.entry("Y", NamedTextColor.GREEN),
                    Map.entry("Z", NamedTextColor.BLUE)
            ));
            public static ColorPalette CONFIG_VALUE_DEFAULT = new ColorPalette(null, Map.ofEntries(
                    Map.entry("STRING", TextColor.fromHexString("#55ff55")),
                    Map.entry("INT", TextColor.fromHexString("#55ffff")),
                    Map.entry("DECIMAL", TextColor.fromHexString("#22dddd")),
                    Map.entry("BOOLEAN_TRUE", TextColor.fromHexString("#ffcd66")),
                    Map.entry("BOOLEAN_FALSE", TextColor.fromHexString("#be984d")),
                    Map.entry("LIST", TextColor.fromHexString("#00ffab")),
                    Map.entry("NULL", TextColor.fromHexString("#ff0000")),
                    Map.entry("SECTION", NamedTextColor.YELLOW),
                    Map.entry("LOCATION_WORLD", NamedTextColor.YELLOW),
                    Map.entry("LOCATION_X", NamedTextColor.RED),
                    Map.entry("LOCATION_Y", NamedTextColor.GREEN),
                    Map.entry("LOCATION_Z", NamedTextColor.BLUE),
                    Map.entry("LOCATION_PITCH", NamedTextColor.GREEN),
                    Map.entry("LOCATION_YAW", NamedTextColor.BLUE)
            ));
        }
    }


}
