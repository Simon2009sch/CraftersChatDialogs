package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditValues;

import me.simoncrafter.CraftersChatDialogs.ComponentUtils;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.ColorPalets.ColorPalette;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.Button;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.AbstractConfigEditSection;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditSection;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditValue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConfigEditListSection extends AbstractConfigEditSection {
    private List<ConfigEditValue<?>> values = new ArrayList<>();

    private boolean showMoveButtons = true;

    private ConfigEditListSection(Component name) {
        name(name);
    }


    public static ConfigEditListSection create(Component name) {
        return new ConfigEditListSection(name);
    }

    @Contract(mutates = "this", value = "-> this")
    public ConfigEditListSection showMoveButtons(boolean showMoveButtons) {
        this.showMoveButtons = showMoveButtons;
        return this;
    }
    public boolean showMoveButtons() {
        return showMoveButtons;
    }

    @Contract(mutates = "this", value = "_ -> this")
    public ConfigEditListSection values(List<ConfigEditValue<?>> values) {
        List<ConfigEditValue<?>> newValues = new ArrayList<>();

        // Set path names on each value for internal path tracking
        for (int i = 0; i < values.size(); i++) {
            newValues.add(values.get(i).path(getCompletePath()).pathName(i + "").path(getCompletePath()));
        }
        this.values = newValues;
        return this;
    }



    public List<ConfigEditValue<?>> valuesList() {
        return new ArrayList<>(values);
    }
    public Map<String, ConfigEditValue<?>> values() {
        Map<String, ConfigEditValue<?>> out = new HashMap<>();
        for (int i = 0; i < values.size(); i++) {
            out.put(String.valueOf(i), values.get(i));
        }
        return out;
    }
    public ConfigEditListSection valuesList(List<ConfigEditValue<?>> values) {
        List<ConfigEditValue<?>> newValues = new ArrayList<>();

        // Set path names on each value for internal path tracking
        for (int i = 0; i < values.size(); i++) {
            newValues.add(values.get(i).pathName(i + ""));
        }
        this.values = newValues;
        return this;
    }
    public ConfigEditListSection values(Map<String, ConfigEditValue<?>> values) {
        List<ConfigEditValue<?>> newValues = new ArrayList<>();
        // Set path names on each value for internal path tracking
        for (int i = 0; i < values.size(); i++) {
            newValues.add(values.get(i + "").pathName(i + ""));
        }
        this.values = newValues;
        return this;
    }

    public List<String> getSubSections() {
        List<String> list = new ArrayList<>();
        for (ConfigEditValue<?> value : values) {
            if (value instanceof ConfigEditSection) {
                list.add(value.pathName());
            }
        }
        return list;
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

    public @Nullable AbstractConfigEditSection getSubSection(String name) {
        ConfigEditValue<?> value = getSubValue(name);
        if (value instanceof AbstractConfigEditSection section) {
            return section;
        }else {
            return null;
        }
    }

    public List<String> getSubValuePaths() {
        List<String> list = new ArrayList<>();
        for (ConfigEditValue<?> value : values) {
            list.add(value.pathName());
        }
        return list;
    }

    public @Nullable ConfigEditValue<?> getSubValue(String name) {
        if (name.isEmpty()) return this;
        int index = 0;
        try {
            index = Integer.parseInt(name);
        } catch (NumberFormatException e) {
            return null;
        }
        return values.get(index);
    }
    public @Nullable ConfigEditValue<?> getSubValue(int index) {
        return values.get(index);
    }


    @Override
    protected void onChangePath(String path) {
        if (values == null) {
            return;
        }
        for (ConfigEditValue<?> entry : values) {
            entry.path(getCompletePath());
        }
    }

    @Override
    protected void onChangePathName(String pathName) {
        if (values == null) {
            return;
        }
        for (ConfigEditValue<?> entry : values) {
            entry.path(getCompletePath());
        }
    }




    @Override
    public ConfigEditListSection pathActionRecursive(Consumer<String> action) {
        return applyRecursive(
                section -> section.pathAction(action),
                value -> value,
                section -> section.pathActionRecursive(action)
        );
    }

    public ConfigEditListSection disableActionRecursive(CustomAction action) {
        return applyRecursive(
                section -> section.disableAction(action),
                value -> value.disableAction(action),
                section -> section.disableActionRecursive(action)
        );
    }

    public ConfigEditListSection reloadActionRecursive(Function<Player, Consumer<Boolean>> action) {
        return applyRecursive(
                section -> section.reloadAction(action),
                value -> value.reloadAction(action),
                section -> section.reloadActionRecursive(action)
        );
    }

    public ConfigEditListSection getValueRecursive(Function<String, Object> action) {
        return applyRecursive(
                section -> section.getValueAction(action),
                value -> value.getValueAction(action),
                section -> section.getValueRecursive(action)
        );
    }

    public ConfigEditListSection setValueRecursive(BiConsumer<String, Object> action) {
        return applyRecursive(
                section -> section.setValueAction(action),
                value -> value.setValueAction(action),
                section -> section.setValueRecursive(action)
        );
    }

    public ConfigEditListSection colorPaletteRecursive(ColorPalette palette) {
        return applyRecursive(
                section -> section.colorPalette(palette),
                value -> value.colorPalette(palette),
                section -> section.colorPaletteRecursive(palette)
        );
    }

    public ConfigEditListSection getPlayerSettingsActionRecursive(Function<String, Object> action) {
        return applyRecursive(
                section -> section.getPlayerSettingAction(action),
                value -> value.getPlayerSettingAction(action),
                section -> section.getPlayerSettingsActionRecursive(action)
        );
    }
    public ConfigEditListSection setPlayerSettingsActionRecursive(BiConsumer<String, Object> action) {
        return applyRecursive(
                section -> section.setPlayerSettingAction(action),
                value -> value.setPlayerSettingAction(action),
                section -> section.setPlayerSettingsActionRecursive(action)
        );
    }
    public ConfigEditListSection setMaxLinesRecursive(int maxLines) {
        return applyRecursive(
                section -> section.maxLines(maxLines),
                (value) -> value,
                section -> section.setMaxLinesRecursive(maxLines)
        );
    }

    private ConfigEditListSection applyRecursive(
            Consumer<ConfigEditListSection> selfAction,
            Function<ConfigEditValue<?>, ConfigEditValue<?>> valueMapper,
            Function<AbstractConfigEditSection, ConfigEditValue<?>> sectionRecursor
    ) {
        selfAction.accept(this);

        List<ConfigEditValue<?>> newValues = new ArrayList<>();
        for (ConfigEditValue<?> entry : values) {
            ConfigEditValue<?> value = valueMapper.apply(entry);
            if (value instanceof AbstractConfigEditSection section) {
                value = sectionRecursor.apply(section);
            }
            newValues.add(value);
        }

        values(newValues);
        return this;
    }



    public List<Component> getContent(Player player) {
        List<Component> content = new ArrayList<>();

        // Add header line (section name)

        List<String> givenNames = new ArrayList<>();

        Bukkit.broadcast(Component.text("List Content: " + values.size()));
        for (int i = 0; i < values.size(); i++) {
            Bukkit.broadcast(Component.text("[ConfigEditListSection] i: " + i + ", Content: " + values.get(i).getValue()));
            ConfigEditValue<?> value = values.get(i);
            String plainName = PlainTextComponentSerializer.plainText().serialize(value.getNameToDisplay());

            // Avoid duplicate entries by display name
            if (givenNames.contains(plainName)) continue;
            givenNames.add(plainName);

            // Skip if player lacks permission to view
            if (!value.checkShowPermission(player)) continue;

            // Truncate name and value for consistent formatting
            int nameLength = PlainTextComponentSerializer.plainText().serialize(ComponentUtils.limitLength(value.getNameToDisplay(), 28)).length();
            int valueLength = 42 - nameLength;

            Component preValueText = Component.empty();
            if (value instanceof ConfigEditListSection) {
                preValueText = Component.text("List:");
            }else if (value instanceof ConfigEditSection) {
                preValueText = Component.text("Section:");
            }

            // Construct UI line with value and edit button
            Component line = Component.text("  ")
                    .append(Component.text("- ", colorPalette().SECONDARY()).hoverEvent(HoverEvent.showText(Component.text("This is index: " + i))))
                    .append(preValueText)
                    .append(ComponentUtils.limitLengthWithEllipsis(value.getValueToDisplay(), valueLength, colorPalette().SECONDARY()))
                    .append(value.checkEditPermission(player)
                            ? value.getButtonsToDisplay()
                            : Component.text(PlainTextComponentSerializer.plainText().serialize(value.getButtonsToDisplay()), colorPalette().DISABLED()));

            if (showMoveButtons) {
                Button moveUp = Button.create().text(Component.text("[↑]", colorPalette().SECONDARY()));
                Button moveDown = Button.create().text(Component.text("[↓]", colorPalette().SECONDARY()));
            }

            content.add(line);
        }
        if (content.isEmpty()) {
            content.add(Component.text("(Nothing here)", colorPalette().DISABLED()));
        }

        return content;
    }


    @Override
    public Object getValue() {
        return new ArrayList<>(values);
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
        showButton().text(Component.text("[...]", colorPalette().get("LIST")));
        return Component.text(" ").append(showButton().compile());
    }


    @Override
    public ConfigEditListSection clone() {
        // Create a new instance and copy base fields from ConfigEditSection and ConfigEditListSection
        ConfigEditListSection copy = copyBaseFieldsInto(ConfigEditListSection.create(name()));

        return copy;
    }

    protected ConfigEditListSection copyBaseFieldsInto(ConfigEditListSection target) {
        // Call base class copyBaseFieldsInto first (copies ConfigEditSection fields)
        super.copyBaseFieldsInto(target);

        return target;
    }

    private void setPathButtonCallback() {
        Bukkit.broadcast(Component.text("[SetPathAction] Activating path action with full new path: " + getCompletePath() + "." + pathName()));
        pathAction().accept(getCompletePath() + "." + pathName());
    }

    @Override
    public void disableButtons() {
        for (ConfigEditValue<?> value : values) {
            value.disableButtons();
        }
    }
    public static String debugCaller() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();

        // [0] = getStackTrace
        // [1] = debugCaller
        // [2] = values(...)
        // [3] = actual external caller
        if (stack.length > 5) {
            StackTraceElement caller = stack[4]; // ← This is what you want
            return "Called from: " +
                    caller.getClassName() + "." +
                    caller.getMethodName() + " (" +
                    caller.getFileName() + ":" +
                    caller.getLineNumber() + ")";
        }
        return "Caller not found";
    }

}