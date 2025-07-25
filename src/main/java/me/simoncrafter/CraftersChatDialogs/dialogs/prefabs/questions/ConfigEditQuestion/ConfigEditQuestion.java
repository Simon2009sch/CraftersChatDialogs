package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion;

import me.simoncrafter.CraftersChatDialogs.Clamp;
import me.simoncrafter.CraftersChatDialogs.ComponentUtils;
import me.simoncrafter.CraftersChatDialogs.dialogs.QuestionPlayerManager;
import me.simoncrafter.CraftersChatDialogs.dialogs.QuestionSyncManager;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.ISyncablePath;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions.DisplayOption;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions.DisplayOptions;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.ClearCharAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.MessageAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.NumberAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.Button;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConfigEditQuestion extends AbstractQuestion<ConfigEditQuestion> implements ISyncablePath {
    private final int MAX_LINES = 13;

    private ConfigEditPlayerData playerData;
    private ConfigEditData configData;
    private AbstractConfigEditSection rootSection;
    private Runnable saveChangesAction = () -> {};
    private Boolean showSaveChangesButton = false;
    private List<AbstractButton<?>> optionButtons = new ArrayList<>();

    protected final Consumer<String> setPathAction = path -> {
        playerData.setPath(configData.getMinimalValidPath(path));
    };
    protected final Function<String, Object> getValueAction = path -> {
        return configData.getValueOrDefault(path, null);
    };
    protected final BiConsumer<String, Object> setValueAction = (path, value) -> configData.setValue(path, value);

    protected final BiConsumer<String, Object> setPlayerSettingAction = (path, value) -> playerData.setSetting(path, value);
    protected final Function<String, Object> getPlayerSettingAction = path -> playerData.getSetting(path);
    protected final Consumer<AbstractButton<?>> addOptionButtonAction = button -> optionButtons.add(button);

    private ConfigEditQuestion(ConfigEditPlayerData playerData, ConfigEditData configData) {
        this.playerData = playerData;
        this.configData = configData;
    }

    public void setRootSection(ConfigEditSection rootSection) {
        this.rootSection = rootSection;
    }

    @Contract(value = "_, _ -> new")
    public static ConfigEditQuestion create(ConfigEditPlayerData playerData, ConfigEditData configData) {
        return new ConfigEditQuestion(playerData, configData).displayOption(DisplayOptions.DEFAULT.colorPalette(DisplayOptions.ColorPalettes.YELLOW_ISH.override(DisplayOptions.ColorPalettes.Overrides.CONFIG_VALUE_DEFAULT)));
    }

    @Contract(value = "_ -> new")
    public ConfigEditQuestion showSaveChangesButton(boolean showSaveChangesButton) {
        this.showSaveChangesButton = showSaveChangesButton;
        return this;
    }
    public boolean showSaveChangesButton() {
        return showSaveChangesButton;
    }

    @Contract(value = "_ -> new")
    public ConfigEditQuestion saveChangesAction(Runnable saveChangesAction) {
        this.saveChangesAction = saveChangesAction;
        return this;
    }
    public Runnable saveChangesAction() {
        return saveChangesAction;
    }


    @Override
    public void beforeShow(@NotNull Player player) {
        if (rootSection == null) {
            rootSection = configData.getNewRootSection();
        }


        // Clear buttons and prepare new UI content before showing question
        Component content = Component.text().resetStyle().build();

        // Action to disable buttons - currently does nothing, but placeholder
        CustomAction customAction = CustomAction.create(p -> {
            rootSection.disableButtons();
        });

        //set actions
        rootSection.pathActionRecursive(setPathAction);
        rootSection.reloadActionRecursive(reloadAction);
        rootSection.disableActionRecursive(customAction);
        rootSection.setValueRecursive(setValueAction);
        rootSection.getValueRecursive(getValueAction);
        rootSection.setPlayerSettingsActionRecursive(setPlayerSettingAction);
        rootSection.getPlayerSettingsActionRecursive(getPlayerSettingAction);
        rootSection.displayOptionRecursive(displayOption());
        rootSection.addOptionButtonActionRecursive(addOptionButtonAction);

        playerData.setPath(configData.getMinimalValidPath(playerData.getPath())); // make path valid
        AbstractConfigEditSection section = rootSection.getSubSectionFromPath(playerData.getPath());
        if (section == null) {
            section = rootSection;
        }



        // title
        question(Component.text("--------------", displayOption().colorPalette().SECONDARY(), TextDecoration.BOLD, TextDecoration.STRIKETHROUGH)
                .append(Component.text(" Config Editor ", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD).decoration(TextDecoration.STRIKETHROUGH, false))
                .append(Component.text("--------------", displayOption().colorPalette().SECONDARY(), TextDecoration.BOLD, TextDecoration.STRIKETHROUGH)));


        // path up button
        if (playerData.getPath().isEmpty()) {
            content = content.append(Component.text("[↑↑]", displayOption().colorPalette().DISABLED()));
        }else {
            content = content.append(Button.create()
                    .text(Component.text("[↑↑]", displayOption().colorPalette().SECONDARY()))
                    .addAction(CustomAction.create(p -> goUpOnePathLayer()))
                    .addAction(displayOption().soundOption().CLICK().toSoundAction())
                    .reloadAction(reloadAction)
                    .reloadOnUse(true)
                    .compile());
        }
        // path display
        content = content.appendSpace().append(makePathDisplayComponent(playerData.getPath())) ;


        //config/content
        int iteration = 1;
        for (Component component : constructLines(section.getContent(player))) {
            content = content.appendNewline().append(component);
            iteration++;
        }


        //Button bar
        content = content.appendNewline();
        Clamp<Integer> scrollingClamp = Clamp.create(Integer.class).lower(true, 0).upper(true, section.getLines() - MAX_LINES + 1);
        NumberAction<Integer> action = NumberAction.create(Integer.class).clamp(scrollingClamp).number(playerData.getLineAtCurrentPath()).operation(i -> playerData.setLineAtCurrentPath(i));
        Button scrollUpButton = Button.create().reloadAction(reloadAction).addAction(displayOption().soundOption().SCROLL().toSoundAction()).reloadOnUse(true).text(Component.text("[↑]", displayOption().colorPalette().SECONDARY())).addAction(action.clone().operatorNumber(-1));
        Button scrollDownButton = Button.create().reloadAction(reloadAction).addAction(displayOption().soundOption().SCROLL().toSoundAction()).reloadOnUse(true).text(Component.text("[↓]", displayOption().colorPalette().SECONDARY())).addAction(action.clone().operatorNumber(1));
        if (scrollingClamp.checkLower(playerData.getLineAtCurrentPath())) {
            content = content.append(scrollUpButton.compile());
        }else {
            content = content.append(scrollUpButton.text().color(displayOption().colorPalette().DISABLED()));
        }
        content = content.appendSpace();
        if (scrollingClamp.checkUpper(playerData.getLineAtCurrentPath())) {
            content = content.append(scrollDownButton.compile());
        }else {
            content = content.append(scrollDownButton.text().color(displayOption().colorPalette().DISABLED()));
        }

        // configurable buttons
        for (AbstractButton<?> button : optionButtons) {
            AbstractButton<?> buttonClone = button.clone();
            content = content.append(Component.text(" "))
                    .append(buttonClone.compile());
        }


        Button saveButton = Button.create()
                .text(Component.text(" [Save] ", displayOption().colorPalette().GREEN(), TextDecoration.BOLD).decoration(TextDecoration.STRIKETHROUGH, false))
                .addAction(CustomAction.create(p -> saveChangesAction.run()))
                .addAction(displayOption().soundOption().CLICK().toSoundAction());
        Button cancelButton = Button.create()
                .text(Component.text(" [Cancel] ", displayOption().colorPalette().RED(), TextDecoration.BOLD).decoration(TextDecoration.STRIKETHROUGH, false))
                .addAction(CustomAction.create(exitAction::run))
                .addAction(ClearCharAction.create())
                .addAction(MessageAction.create(Component.text("Closed Config Editor", displayOption().colorPalette().RED())))
                .addAction(displayOption().soundOption().CLICK().toSoundAction());
        Component withSaveButton = saveButton.compile()
                .append(Component.text("---", displayOption().colorPalette().SECONDARY(), TextDecoration.BOLD, TextDecoration.STRIKETHROUGH))
                .append(cancelButton.compile());
        Component withoutSaveButton = Component.text("-----", displayOption().colorPalette().SECONDARY(), TextDecoration.BOLD, TextDecoration.STRIKETHROUGH)
                .append(cancelButton.compile())
                .append(Component.text("-----", displayOption().colorPalette().SECONDARY(), TextDecoration.BOLD, TextDecoration.STRIKETHROUGH));


        // footer
        Component footer = Component.text("-----------", displayOption().colorPalette().SECONDARY(), TextDecoration.BOLD, TextDecoration.STRIKETHROUGH);
        if (showSaveChangesButton) {
            footer = footer.append(withSaveButton);
        }else {
            footer = footer.append(withoutSaveButton);
        }
        footer = footer.append(Component.text("-----------", displayOption().colorPalette().SECONDARY(), TextDecoration.BOLD, TextDecoration.STRIKETHROUGH));
        footer(footer);

        content(content);
    }


    @Override
    public void disableButtons() {

    }

    @Override
    public String getPath() {
        return playerData.getPath();
    }

    @Override
    public void reload(Player player, boolean triggeredFromCascade) {
        if (!triggeredFromCascade) {
            // Sync reload with other connected clients if not from cascade
            QuestionSyncManager.onReload(syncKey(), this, playerData.getPath());
        }

        // Remove player-specific state
        disableButtons();
        if (!syncKey().isEmpty()) {
            QuestionSyncManager.removePlayerFromQuestion(syncKey(), player);
        }

        // Unregister this question from player and execute reload logic
        QuestionPlayerManager.removeQuestionFromPlayer(player);
        onReload().run(player);
    }

    private Component makePathDisplayComponent(String path) {
        Component pathDisplay = Button.create()
                .text(Component.text("Root", displayOption().colorPalette().PRIMARY()))
                .reloadAction(reloadAction)
                .reloadOnUse(true)
                .addAction(CustomAction.create(p -> setPathAction.accept("")))
                .addAction(displayOption().soundOption().CLICK().toSoundAction())
                .reloadAction(reloadAction)
                .reloadOnUse(true)
                .hoverText(Component.text("Click to go to: ", displayOption().colorPalette().SECONDARY()).append(Component.text("Root", displayOption().colorPalette().PRIMARY())))
                .compile();
        String[] splitPath = playerData.getPath().split("\\.");
        if (!splitPath[0].isEmpty()) pathDisplay = pathDisplay.append(Component.text(".", displayOption().colorPalette().SECONDARY()));

        for (int i = 0; i < splitPath.length; i++) {
            int finalI = i;
            pathDisplay = pathDisplay.append(
                    Button.create()
                            .text(Component.text(splitPath[i], displayOption().colorPalette().PRIMARY()))
                            .reloadAction(reloadAction)
                            .reloadOnUse(true)
                            .addAction(CustomAction.create(p -> setPathAction.accept(String.join(".", Arrays.copyOfRange(splitPath, 0, finalI + 1)))))
                            .addAction(displayOption().soundOption().CLICK().toSoundAction())
                            .reloadAction(reloadAction)
                            .reloadOnUse(true)
                            .hoverText(Component.text("Click to go to: ", displayOption().colorPalette().SECONDARY()).append(Component.text(splitPath[i], displayOption().colorPalette().PRIMARY())))
                            .compile());
            if (i < splitPath.length - 1) {
                pathDisplay = pathDisplay.append(Component.text(".", displayOption().colorPalette().SECONDARY()));
            }
        }

        return ComponentUtils.limitLengthStartWithEllipsis(pathDisplay, 51, displayOption().colorPalette().DISABLED());
    }


    private final Random random = new Random();
    private  String getRandomSpaceAtEnd() {
        return "-".repeat(random.nextInt(5));
    }

    private List<Component> constructLines(List<Component> components) {
        List<Component> out = new ArrayList<>();
        int offset = Math.max(0, playerData.getLineAtCurrentPath());
        int total = components.size();

        // Clamp the start and end
        int start = Math.min(offset, total);
        int end = Math.min(start + MAX_LINES, total);

        for (int i = start; i < end; i++) {
            out.add(Component.text("  ")
                    .hoverEvent(HoverEvent.showText(Component.text("Line: " + i)))
                    .append(components.get(i))
                    .append(Component.text(" ".repeat(random.nextInt(5))).hoverEvent(HoverEvent.showText(Component.empty()))));
        }

        // Pad with blank lines if needed
        while (out.size() < MAX_LINES) {
            out.add(Component.text(" ".repeat(random.nextInt(30))));
        }

        return out;
    }

    private void goUpOnePathLayer() {
        String path = playerData.getPath();
        if (!path.isEmpty()) {
            int lastDotIndex = path.lastIndexOf(".");
            if (lastDotIndex != -1) {
                path = path.substring(0, lastDotIndex);
            } else {
                path = "";
            }
            playerData.setPath(path);
        }
    }

    private @Nullable Object getValueActionCallback(String path) {
        return configData.getValueOrDefault(path, null);
    }

    @Override
    public ConfigEditQuestion clone() {
        ConfigEditQuestion clone = new ConfigEditQuestion(playerData.clone(), configData.clone());
        clone.question(question());
        clone.question(question());
        clone.footer(footer());
        clone.permission(permission());
        clone.syncKey(syncKey());
        clone.onReload(onReload());
        clone.player(player());
        clone.displayOption(displayOption());
        clone.saveChangesAction(saveChangesAction());

        return clone;
    }

}
