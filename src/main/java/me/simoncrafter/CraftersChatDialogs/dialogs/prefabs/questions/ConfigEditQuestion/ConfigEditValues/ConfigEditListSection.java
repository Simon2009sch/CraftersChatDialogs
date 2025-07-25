package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditValues;

import me.simoncrafter.CraftersChatDialogs.ComponentUtils;
import me.simoncrafter.CraftersChatDialogs.dialogs.QuestionSyncManager;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions.DisplayOption;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.QuestionAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.Button;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.AbstractConfigEditSection;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditSection;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditValue;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfirmQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.GenericQuestion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
import java.util.function.Supplier;

public class ConfigEditListSection extends AbstractConfigEditSection {
    private List<ConfigEditValue<?>> values = new ArrayList<>();

    private boolean showMoveButtons = false;


    private ConfigEditListSection(Component name) {
        name(name);
    }
    private List<Button> moveUpButtons = new ArrayList<>();
    private List<Button> moveDownButton = new ArrayList<>();
    private List<Button> removeButtons = new ArrayList<>();

    public static ConfigEditListSection create(Component name) {
        return new ConfigEditListSection(name);
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

    @Contract(mutates = "this", value = "-> this")
    public ConfigEditListSection showMoveButtons(boolean showMoveButtons) {
        this.showMoveButtons = showMoveButtons;
        return this;
    }
    public boolean showMoveButtons() {
        return showMoveButtons;
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

    public ConfigEditListSection displayOptionRecursive(DisplayOption displayOption) {
        return applyRecursive(
                section -> section.displayOption(displayOption),
                value -> value.displayOption(displayOption),
                section -> section.displayOptionRecursive(displayOption)
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

    @Override
    public ConfigEditListSection addOptionButtonActionRecursive(Consumer<AbstractButton<?>> action) {
        return applyRecursive(
                section -> section.addOptionButtonAction(action),
                value -> value,
                section -> section.addOptionButtonActionRecursive(action)
        );
    }

    @Override
    public ConfigEditListSection syncKeyRecursive(String key) {
        return applyRecursive(
                section -> section.syncKey(key),
                value -> value.syncKey(key),
                section -> section.syncKeyRecursive(key)
        );
    }

    @Override
    public ConfigEditListSection permissionPrefixRecursive(Supplier<String> permissionPrefix) {
        return applyRecursive(
                section -> section.permissionPrefixAction(permissionPrefix),
                value -> value.permissionPrefixAction(permissionPrefix),
                section -> section.permissionPrefixRecursive(permissionPrefix)
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

    @Override
    public int getLines() {
        return values.size();
    }

    public List<Component> getContent(Player player) {
        List<Component> content = new ArrayList<>();

        // Add header line (section name)

        List<String> givenNames = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {
            ConfigEditValue<?> value = values.get(i);
            String plainName = PlainTextComponentSerializer.plainText().serialize(value.getNameToDisplay());

            // Avoid duplicate entries by display name
            if (givenNames.contains(plainName)) continue;
            givenNames.add(plainName);


            // Truncate name and value for consistent formatting
            int nameLength = PlainTextComponentSerializer.plainText().serialize(ComponentUtils.limitLength(value.getNameToDisplay(), 28)).length();
            int valueLength = 42 - nameLength;

            Component preValueText = Component.empty();
            if (value instanceof ConfigEditListSection) {
                preValueText = Component.text("List:");
            }else if (value instanceof ConfigEditSection) {
                preValueText = Component.text("Section:");
            }

            Component valueToDisplay = value.getValueToDisplay();

            // Construct UI line with value and edit button
            Component line = Component.text("")
                    .append(Component.text("- ", displayOption().colorPalette().SECONDARY()).hoverEvent(HoverEvent.showText(Component.text("Line: " + i))))
                    .append(preValueText)
                    .append(ComponentUtils.limitLengthWithEllipsis(valueToDisplay, valueLength, displayOption().colorPalette().SECONDARY()))
                    .append(value.getButtonsToDisplay());

            // ------------------------------------------------------------------------------------------------------------
            //                                                  MOVE LOGIC
            // ------------------------------------------------------------------------------------------------------------
            int finalI = i;
            if (showMoveButtons) { // TODO
                Object listObject = getValueAction().apply(getCompletePath());
                List<Object> list;
                if (listObject instanceof List<?> l) {
                    list = (List<Object>) l;
                } else {
                    continue;
                };
                Button moveUp = Button.create()
                        .text(Component.text("↑", displayOption().colorPalette().SECONDARY()))
                        .addAction(CustomAction.create(p -> setValueAction().accept(getCompletePath(), UIMoveValue(list, finalI, -1))))
                        .addPostAction(disableAction())
                        .reloadAction(reloadAction())
                        .addAction(displayOption().soundOption().CLICK().toSoundAction())
                        .reloadOnUse(true);
                Button moveDown = Button.create()
                        .text(Component.text("↓", displayOption().colorPalette().SECONDARY()))
                        .addAction(CustomAction.create(p -> setValueAction().accept(getCompletePath(), UIMoveValue(list, finalI, 1))))
                        .addPostAction(disableAction())
                        .reloadAction(reloadAction())
                        .addAction(displayOption().soundOption().CLICK().toSoundAction())
                        .reloadOnUse(true);
                moveDownButton.add(moveDown);
                moveUpButtons.add(moveUp);
                // check if entry is the last or foirst and disable the move buttons accordingly
                // move down logic
                line = line.appendSpace();
                if (finalI == values.size() - 1) {
                    line = line.append(moveDown.text().color(displayOption().colorPalette().DISABLED()));
                } else {
                    line = line.append(moveDown.compile());
                }

                // move up logic
                if (finalI == 0) {
                    line = line.append(moveUp.text().color(displayOption().colorPalette().DISABLED()));
                } else {
                    line = line.append(moveUp.compile());
                }
            }

            // ------------------------------------------------------------------------------------------------------------
            //                                                  REMOVE LOGIC
            // ------------------------------------------------------------------------------------------------------------
            if (showRemoveButtons()) {
                CustomAction deleteAction = CustomAction.create(p -> setValueAction().accept(getCompletePath(), UIRemoveValueFromList((List<Object>) getValueAction().apply(getCompletePath()), finalI)));


                ConfirmQuestion confirmQuestion = ConfirmQuestion.create()
                        .question(Component.text("Remove entry?", displayOption().colorPalette().RED(), TextDecoration.BOLD)
                                .appendNewline()
                                .append(Component.text("").style(Style.empty()))
                                .append(Component.text("  Value: ", displayOption().colorPalette().GRAY()))
                                .append(Component.text(PlainTextComponentSerializer.plainText().serialize(ComponentUtils.limitLengthWithEllipsis(valueToDisplay, 30)), displayOption().colorPalette().DARK_RED()))
                                .appendNewline()
                                .append(Component.text("  Line: ", displayOption().colorPalette().GRAY()))
                                .append(Component.text(String.valueOf(finalI), displayOption().colorPalette().DARK_RED()))
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
                        .addAction(displayOption().soundOption().CLICK().toSoundAction())
                        .addPostAction(disableAction());
                removeButtons.add(removeButton);
                line = line.appendSpace().append(removeButton.compile());
            }


            content.add(line);
        }
        if (content.isEmpty()) {
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

            boolean secondColor = false;
            if (addButtonPrefabs().size() == 1) {
                addOptionButtonAction().accept(Button.create()
                        .text(Component.text("[+]", displayOption().colorPalette().GREEN()))
                        .addAction(CustomAction.create(p -> setValueAction().accept(getCompletePath(), UIAddValueToList((List) getValueAction().apply(getCompletePath()), addButtonPrefabs().values()
                                .iterator()
                                .next()))))
                        .addAction(displayOption().soundOption().CLICK().toSoundAction())
                        .reloadAction(reloadAction())
                        .reloadOnUse(true));
            } else {
                for (String key : addButtonPrefabs().keySet()) {
                    TextColor color;
                    if (secondColor) {
                        color = displayOption().colorPalette().SECONDARY();
                    } else {
                        color = displayOption().colorPalette().PRIMARY();
                    }
                    addOptionButtons.add(Button.create()
                            .text(Component.text("[" + key + "]", color))
                            .addAction(CustomAction.create(p -> setValueAction().accept(getCompletePath(), UIAddValueToList((List) getValueAction().apply(getCompletePath()), addButtonPrefabs().get(key)))))
                            .addPostAction(displayOption().soundOption().CLICK().toSoundAction())
                            .addPostAction(backAction)
                            .addPostAction(disableAction()));
                    secondColor = !secondColor;
                }
                GenericQuestion addOptionQuestion = GenericQuestion.create()
                        .question(Component.text("Please select an Option to be put as the created value", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD))
                        .setButtons(addOptionButtons)
                        .addButton(Button.create()
                                .text(Component.text("[Cancel]", displayOption().colorPalette().RED(), TextDecoration.BOLD))
                                .addAction(backAction)
                                .addAction(displayOption().soundOption().CLICK().toSoundAction())
                                .displayOption(displayOption()));
                addOptionButtonAction().accept(Button.create()
                        .text(Component.text("[+]", displayOption().colorPalette().GREEN()))
                        .addAction(CustomAction.create(p -> QuestionSyncManager.addEditingPlayer(syncKey(), p)))
                        .addAction(displayOption().soundOption().CLICK().toSoundAction())
                        .addAction(QuestionAction.create(addOptionQuestion)));
            }




        }
        return content;
    }

    private List<Object> UIAddValueToList(List<Object> values, Object value) {
        if (values == null) return new ArrayList<>();
        List<Object> newValues = new ArrayList<>(values);
        newValues.add(value);
        return newValues;
    }

    private List<Object> UIMoveValue(List<Object> values, int index, int offset) {
        if (values == null) return new ArrayList<>();
        List<Object> newValues = new ArrayList<>(values);

        if (index < 0 || index >= newValues.size()) {
            return newValues;
        }

        Object item = newValues.remove(index);

        // Clamp newIndex to the valid insertion range: [0, newValues.size()]
        int newIndex = index + offset;
        if (newIndex < 0) {
            newIndex = 0;
        } else if (newIndex > newValues.size()) {
            newIndex = newValues.size();
        }

        newValues.add(newIndex, item);
        return newValues;
    }
    private List<Object> UIRemoveValueFromList(List<Object> values, int index) {
        if (values == null) return new ArrayList<>();
        List<Object> newValues = new ArrayList<>(values);
        newValues.remove(index);
        return newValues;
    }


    @Override
    public Object getValue() {
        return new ArrayList<>(values);
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
        return Component.text("[...]", displayOption().colorPalette().get("LIST"));
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
        pathAction().accept(getCompletePath() + "." + pathName());
    }

    @Override
    public void disableButtons() {
        for (ConfigEditValue<?> value : values) {
            value.disableButtons();
        }
        for (Button button : moveUpButtons) {
            button.setDisabled(true);
        }
        for (Button button : moveDownButton) {
            button.setDisabled(true);
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