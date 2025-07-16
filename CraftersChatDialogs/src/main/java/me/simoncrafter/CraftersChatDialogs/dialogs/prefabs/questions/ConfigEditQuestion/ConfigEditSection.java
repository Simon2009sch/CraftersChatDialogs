package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion;

import me.simoncrafter.CraftersChatDialogs.ComponentUtils;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.ColorPalets.ColorPalette;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.ColorPalets.ColorPalettes;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.Button;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditValues.ConfigEditListSection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConfigEditSection extends AbstractConfigEditSection {
    private Map<String, ConfigEditValue<?>> values = new HashMap<>();

    protected ConfigEditSection(Component name) {
        name(name);
    }

    public static ConfigEditSection create(Component name) {
        return new ConfigEditSection(name);
    }


    @Contract(mutates = "this", value = "-> this")
    public ConfigEditSection values(Map<String, ConfigEditValue<?>> values) {
        Map<String, ConfigEditValue<?>> newValues = new HashMap<>();

        // Set path names on each value for internal path tracking
        for (Map.Entry<String, ConfigEditValue<?>> entry : values.entrySet()) {
            newValues.put(entry.getKey(), entry.getValue().pathName(entry.getKey()).path(getCompletePath()));
        }
        this.values = newValues;
        return this;
    }

    @Contract(mutates = "this", value = "_, _ -> this")
    public ConfigEditSection putValue(String name, ConfigEditValue<?> value) {
        if (this.values == null) this.values = new HashMap<>();
        this.values.put(name, value);
        return this;
    }



    @Contract(mutates = "this", value = "_ -> this")
    public ConfigEditSection removeValue(String name) {
        if (this.values != null) {
            this.values.remove(name);
        }
        return this;
    }

    public Map<String, ConfigEditValue<?>> values() {
        return values;
    }


    public @Nullable AbstractConfigEditSection getSubSection(String name) {
        ConfigEditValue<?> value = getSubValue(name);
        if (value instanceof AbstractConfigEditSection section) {
            return section;
        }else {
            return null;
        }
    }

    @Override
    protected void onChangePath(String path) {
        if (values == null) {
            return;
        }
        for (Map.Entry<String, ConfigEditValue<?>> entry : values.entrySet()) {
            entry.getValue().path(getCompletePath());
        }
    }

    @Override
    protected void onChangePathName(String pathName) {
        if (values == null) {
            return;
        }
        for (Map.Entry<String, ConfigEditValue<?>> entry : values.entrySet()) {
            entry.getValue().path(getCompletePath());
        }
    }

    public ConfigEditSection pathActionRecursive(Consumer<String> action) {
        return applyRecursive(
                section -> section.pathAction(action),
                value -> value,
                section -> section.pathActionRecursive(action)
        );
    }

    public ConfigEditSection disableActionRecursive(CustomAction action) {
        return applyRecursive(
                section -> section.disableAction(action),
                value -> value.disableAction(action),
                section -> section.disableActionRecursive(action)
        );
    }

    public ConfigEditSection reloadActionRecursive(Function<Player, Consumer<Boolean>> action) {
        return applyRecursive(
                section -> section.reloadAction(action),
                value -> value.reloadAction(action),
                section -> section.reloadActionRecursive(action)
        );
    }

    public ConfigEditSection getValueRecursive(Function<String, Object> action) {
        return applyRecursive(
                section -> section.getValueAction(action),
                value -> value.getValueAction(action),
                section -> section.getValueRecursive(action)
        );
    }

    public ConfigEditSection setValueRecursive(BiConsumer<String, Object> action) {
        return applyRecursive(
                section -> section.setValueAction(action),
                value -> value.setValueAction(action),
                section -> section.setValueRecursive(action)
        );
    }

    public ConfigEditSection getPlayerSettingsActionRecursive(Function<String, Object> action) {
        return applyRecursive(
                section -> section.getPlayerSettingAction(action),
                value -> value.getPlayerSettingAction(action),
                section -> section.getPlayerSettingsActionRecursive(action)
        );
    }
    public ConfigEditSection setPlayerSettingsActionRecursive(BiConsumer<String, Object> action) {
        return applyRecursive(
                section -> section.setPlayerSettingAction(action),
                value -> value.setPlayerSettingAction(action),
                section -> section.setPlayerSettingsActionRecursive(action)
        );
    }
    public ConfigEditSection setMaxLinesRecursive(int maxLines) {
        return applyRecursive(
                section -> section.maxLines(maxLines),
                (value) -> value,
                section -> section.setMaxLinesRecursive(maxLines)
        );
    }
    public ConfigEditSection colorPaletteRecursive(ColorPalette palette) {
        return applyRecursive(
                section -> section.colorPalette(palette),
                value -> value.colorPalette(palette),
                section -> section.colorPaletteRecursive(palette)
        );
    }

    private ConfigEditSection applyRecursive(
            Consumer<ConfigEditSection> selfAction,
            Function<ConfigEditValue<?>, ConfigEditValue<?>> valueMapper,
            Function<AbstractConfigEditSection, ConfigEditValue<?>> sectionRecursor
    ) {
        selfAction.accept(this);

        Map<String, ConfigEditValue<?>> newValues = new HashMap<>();
        for (Map.Entry<String, ConfigEditValue<?>> entry : values.entrySet()) {
            ConfigEditValue<?> value = valueMapper.apply(entry.getValue());
            if (value instanceof AbstractConfigEditSection section) {
                value = sectionRecursor.apply(section);
            }
            newValues.put(entry.getKey(), value);
        }

        values(newValues);
        return this;
    }

    public List<Component> getContent(Player player) {
        List<Component> content = new ArrayList<>();

        // Add header line (section name)

        List<String> givenNames = new ArrayList<>();

        for (ConfigEditValue<?> value : values.values()) {
            String plainName = PlainTextComponentSerializer.plainText().serialize(value.getNameToDisplay());

            // Avoid duplicate entries by display name
            if (givenNames.contains(plainName)) continue;
            givenNames.add(plainName);

            // Skip if player lacks permission to view
            if (!value.checkShowPermission(player)) continue;

            // Truncate name and value for consistent formatting
            int nameLength = PlainTextComponentSerializer.plainText().serialize(ComponentUtils.limitLength(value.getNameToDisplay(), 28)).length();
            int valueLength = 42 - nameLength;

            // Construct UI line with value and edit button
            Component line = Component.text("  ")
                    .append(ComponentUtils.limitLengthWithEllipsis(value.getNameToDisplay(), 28))
                    .append(Component.text(": "))
                    .append(ComponentUtils.limitLengthWithEllipsis(value.getValueToDisplay(), valueLength, colorPalette().SECONDARY()))
                    .append(value.checkEditPermission(player)
                            ? value.getButtonsToDisplay()
                            : Component.text(PlainTextComponentSerializer.plainText().serialize(value.getButtonsToDisplay()), colorPalette().DISABLED()));
            content.add(line);
        }

        return content;
    }


    @Override
    public Object getValue() {
        return new HashMap<>(values);
    }

    @Override
    public Component getValueToDisplay() {
        return Component.text("");
    }

    @Override
    public Component getButtonsToDisplay() {
        // Compile show button with current reload and path-setting action
        showButton().reloadOnUse(true);
        showButton().reloadAction(reloadAction());
        showButton().text(Component.text("[↓↓]", colorPalette().get("SECTION")));
        return Component.text(" ").append(showButton().compile());
    }

    public List<String> getSubSections() {
        List<String> list = new ArrayList<>();
        for (ConfigEditValue<?> value : values.values()) {
            if (value instanceof ConfigEditSection) {
                list.add(value.pathName());
            }
        }
        return list;
    }

    public List<String> getSubValuePaths() {
        List<String> list = new ArrayList<>();
        for (ConfigEditValue<?> value : values.values()) {
            list.add(value.pathName());
        }
        return list;
    }

    public @Nullable ConfigEditValue<?> getSubValue(String name) {
        if (name.isEmpty()) return this;
        return values.get(name);
    }



    public @Nullable AbstractConfigEditSection getSubSectionFromPath(String path) {
        if (path.isEmpty()) return this;
        Bukkit.broadcast(Component.text("[GetSubSectionFromPath] path: " + path));
        // Traverse path parts recursively to resolve nested sections
        String[] parts = path.split("\\.");
        AbstractConfigEditSection current = this;
        for (String part : parts) {
            current = current.getSubSection(part);
            if (current == null) {
                Bukkit.broadcast(Component.text("[GetSubSectionFromPath] Failed to resolve path: " + path));
                return null;
            }
        }
        Bukkit.broadcast(Component.text("[GetSubSectionFromPath] Resolved path: " + path));
        return  current;
    }

    @Override
    public ConfigEditSection clone() {
        ConfigEditSection copy = copyBaseFieldsInto(ConfigEditSection.create(name()));

        // Deep copy sub-values
        Map<String, ConfigEditValue<?>> copiedValues = new HashMap<>();
        for (Map.Entry<String, ConfigEditValue<?>> entry : values().entrySet()) {
            copiedValues.put(entry.getKey(), entry.getValue().clone());
        }

        copy.values(copiedValues);
        return copy;
    }

    protected ConfigEditSection copyBaseFieldsInto(ConfigEditSection target) {
        return ((ConfigEditSection) super.copyBaseFieldsInto(target))
                .values(values);
    }

    private void setPathButtonCallback() {
        Bukkit.broadcast(Component.text("[SetPathAction] Activating path action with full new path: " + getCompletePath() + "." + pathName()));
        pathAction().accept(getCompletePath() + "." + pathName());
    }

    @Override
    public void disableButtons() {
        for (ConfigEditValue<?> value : values.values()) {
            value.disableButtons();
        }
    }
}