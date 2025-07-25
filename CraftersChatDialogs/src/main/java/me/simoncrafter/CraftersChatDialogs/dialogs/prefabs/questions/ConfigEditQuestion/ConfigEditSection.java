package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion;

import me.simoncrafter.CraftersChatDialogs.ComponentUtils;
import me.simoncrafter.CraftersChatDialogs.InstanceData;
import me.simoncrafter.CraftersChatDialogs.dialogs.QuestionSyncManager;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions.DisplayOption;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.StringWithRulesInputAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.QuestionAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.Button;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.StringWithRulesInputButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfirmQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.GenericQuestion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConfigEditSection extends AbstractConfigEditSection {
    private Map<String, ConfigEditValue<?>> values = new HashMap<>();

    protected ConfigEditSection(Component name) {
        name(name);
    }
    private List<AbstractButton<?>> removeButtons = new ArrayList<>();


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
    public ConfigEditSection displayOptionRecursive(DisplayOption displayOption) {
        return applyRecursive(
                section -> section.displayOption(displayOption),
                value -> value.displayOption(displayOption),
                section -> section.displayOptionRecursive(displayOption)
        );
    }
    @Override
    public ConfigEditSection addOptionButtonActionRecursive(Consumer<AbstractButton<?>> action) {
        return applyRecursive(
                section -> section.addOptionButtonAction(action),
                value -> value,
                section -> section.addOptionButtonActionRecursive(action)
        );
    }

    @Override
    public ConfigEditSection syncKeyRecursive(String key) {
        return applyRecursive(
                section -> section.syncKey(key),
                value -> value.syncKey(key),
                section -> section.syncKeyRecursive(key)
        );
    }

    @Override
    public ConfigEditSection permissionPrefixRecursive(Supplier<String> permissionPrefix) {
        return applyRecursive(
                section -> section.permissionPrefixAction(permissionPrefix),
                value -> value.permissionPrefixAction(permissionPrefix),
                section -> section.permissionPrefixRecursive(permissionPrefix)
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



        List<String> givenNames = new ArrayList<>();

        for (String key : values.keySet()) {
            ConfigEditValue<?> value = values.get(key);
            String plainName = PlainTextComponentSerializer.plainText().serialize(value.getNameToDisplay());

            // Avoid duplicate entries by display name
            if (givenNames.contains(plainName)) continue;
            givenNames.add(plainName);

            // Truncate name and value for consistent formatting
            int nameLength = PlainTextComponentSerializer.plainText().serialize(ComponentUtils.limitLength(value.getNameToDisplay(), 28)).length();
            int valueLength = 42 - nameLength;

            Component valueToDisplay = value.getValueToDisplay();

            // Construct UI line with value and edit button
            Component line = Component.text("")
                    .append(ComponentUtils.limitLengthWithEllipsis(value.getNameToDisplay(), 28))
                    .append(Component.text(": "))
                    .append(ComponentUtils.limitLengthWithEllipsis(valueToDisplay, valueLength, displayOption().colorPalette().SECONDARY()))
                    .append(value.getButtonsToDisplay());


            // ------------------------------------------------------------------------------------------------------------
            //                                                  REMOVE LOGIC
            // ------------------------------------------------------------------------------------------------------------
            if (showRemoveButtons()) {
                CustomAction deleteAction = CustomAction.create(p -> setValueAction().accept(getCompletePath(), UIRemoveValue((Map<String, Object>) getValueAction().apply(getCompletePath()), key)));


                ConfirmQuestion confirmQuestion = ConfirmQuestion.create()
                        .question(Component.text("Remove entry?", displayOption().colorPalette().RED(), TextDecoration.BOLD)
                                .appendNewline()
                                .append(Component.text("").style(Style.empty()))
                                .append(Component.text("  Value: ", displayOption().colorPalette().GRAY()))
                                .append(Component.text(PlainTextComponentSerializer.plainText().serialize(ComponentUtils.limitLengthWithEllipsis(valueToDisplay, 30)), displayOption().colorPalette().DARK_RED()))
                                .appendNewline()
                                .append(Component.text("  Name: ", displayOption().colorPalette().GRAY()))
                                .append(Component.text(String.valueOf(key), displayOption().colorPalette().DARK_RED()))
                        )
                        .addConfirmAction(deleteAction)
                        .addConfirmAction(displayOption().soundOption().CLICK().toSoundAction())
                        .returnToQuestionAction(CustomAction.create(p -> {
                            QuestionSyncManager.removeEditingPlayer(syncKey(), p);
                            reloadAction().apply(p).accept(false);
                        }));

                Button removeButton = Button.create()
                        .text(Component.text("X", displayOption().colorPalette().RED()))
                        .addAction(CustomAction.create(p -> QuestionSyncManager.addEditingPlayer(syncKey(), p)))
                        .addAction(QuestionAction.create(confirmQuestion))
                        .addPostAction(disableAction());
                removeButtons.add(removeButton);
                line = line.appendSpace().append(removeButton.compile());
            }

            content.add(line);
        }
        if (values.isEmpty()) {
            content.add(Component.text("(Nothing here)", displayOption().colorPalette().DISABLED()));
        }


        // ------------------------------------------------------------------------------------------------------------
        //                                                  ADD LOGIC
        // ------------------------------------------------------------------------------------------------------------
        if (showAddButton() && !addButtonPrefabs().isEmpty()) {
            List<AbstractButton<?>> addOptionButtons = new ArrayList<>();

            CustomAction backAction = CustomAction.create(p -> {
                QuestionSyncManager.removeEditingPlayer(syncKey(), p);
                reloadAction().apply(p).accept(false);
            });

            // default for all types
            Component cancelText = Component.text("").append(Component.text("Type cancel to cancel. [Click to paste]")
                            .decoration(TextDecoration.BOLD, false))
                    .color(displayOption().colorPalette().HINT())
                    .clickEvent(ClickEvent.suggestCommand("cancel"));
            // default for string string
            Component stringCancelInputText = Component.newline()
                    .append(Component.text(" Put a \\ before it to input \"cancel\" [Click to paste]", displayOption().colorPalette().HINT())
                            .clickEvent(ClickEvent.suggestCommand("\\cancel"))
                            .decoration(TextDecoration.BOLD, false));

            boolean secondColor = false;
            for (String key : addButtonPrefabs().keySet()) {
                TextColor color;
                if (secondColor) {
                    color = displayOption().colorPalette().SECONDARY();
                } else {
                    color = displayOption().colorPalette().PRIMARY();
                }
                addOptionButtons.add(StringWithRulesInputButton.create(StringWithRulesInputAction.create(p -> s -> {
                                    if (getValueAction().apply(getCompletePath() + "." + s) != null) return;

                                    Object value = getValueAction().apply(getCompletePath());
                                    if (!(value instanceof Map<?, ?> map)) {
                                        return;
                                    }

                                    for (Map.Entry<?, ?> entry : map.entrySet()) {
                                        if (!(entry.getKey() instanceof String)) {
                                            return;
                                        }
                                    }
                                    setValueAction().accept(getCompletePath(), UIPutNewValue((Map<String, Object>) value, s, addButtonPrefabs().get(key)));
                                })
                                .reTry(true)
                                .addSuccessAction(backAction)
                                .addCancelAction(backAction)
                                .addPreAction(displayOption().soundOption().CLICK().toSoundAction())
                                .addReTryAction(displayOption().soundOption().ERROR().toSoundAction())
                                .addSuccessAction(displayOption().soundOption().SUCCESS().toSoundAction())
                                .prompt(Component.text("Please enter a name for the new value", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD)
                                        .appendNewline()
                                        .append(cancelText)
                                        .append(stringCancelInputText))
                                .regexRule("[a-zA-Z0-9_]+"))
                        .text(Component.text("[" + key + "]", color))
                        .addPostAction(disableAction()));
                secondColor = !secondColor;
            }

            GenericQuestion addOptionQuestion = GenericQuestion.create()
                    .question(Component.text("Please select an Option to be put as the created value", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD))
                    .setButtons(addOptionButtons)
                    .addButton(Button.create()
                            .text(Component.text("[Cancel]", displayOption().colorPalette().RED(), TextDecoration.BOLD))
                            .addAction(backAction)
                            .displayOption(displayOption()));

            addOptionButtonAction().accept(Button.create()
                    .text(Component.text("[+]", displayOption().colorPalette().GREEN()))
                    .addAction(CustomAction.create(p -> QuestionSyncManager.addEditingPlayer(syncKey(), p)))
                    .addPostAction(displayOption().soundOption().CLICK().toSoundAction())
                    .addAction(QuestionAction.create(addOptionQuestion)));
        }

        return content;
    }

    private Map<String, Object> UIPutNewValue(Map<String, Object> values, String key, Object value) {
        if (values == null) return new HashMap<>();
        Map<String, Object> map = new HashMap<>(values);
        map.put(key, value);
        return map;
    }

    private Map<String, Object> UIRemoveValue(Map<String, Object> map, String key) {
        if (map == null) return new HashMap<>();
        Map<String, Object> newMap = new HashMap<>(map);
        newMap.remove(key);
        return newMap;
    }


    @Override
    public Object getValue() {
        return new HashMap<>(values);
    }

    @Override
    public Component getValueToDisplay() {
        return getInactiveValueToDisplay();
    }

    @Override
    public Component getInactiveValueToDisplay() {
        return Component.text("");
    }

    @Override
    public Component getButtonsToDisplay() {
        // Compile show button with current reload and path-setting action
        showButton().reloadOnUse(true);
        showButton().reloadAction(reloadAction());
        showButton().text(getInactiveButtonsToDisplay());
        return Component.text(" ").append(showButton().compile());
    }
    @Override
    public Component getInactiveButtonsToDisplay() {
        return Component.text("[↓↓]", displayOption().colorPalette().get("SECTION"));
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

    @Override
    public int getLines() {
        return values.size();
    }

    public @Nullable AbstractConfigEditSection getSubSectionFromPath(String path) {
        if (path.isEmpty()) return this;
        // Traverse path parts recursively to resolve nested sections
        String[] parts = path.split("\\.");
        AbstractConfigEditSection current = this;
        for (String part : parts) {
            current = current.getSubSection(part);
            if (current == null) {
                return null;
            }
        }
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


    @Override
    public void disableButtons() {
        for (ConfigEditValue<?> value : values.values()) {
            value.disableButtons();
        }
    }
}