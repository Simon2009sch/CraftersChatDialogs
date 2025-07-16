package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion;

import me.simoncrafter.CraftersChatDialogs.ComponentUtils;
import me.simoncrafter.CraftersChatDialogs.dialogs.QuestionPlayerManager;
import me.simoncrafter.CraftersChatDialogs.dialogs.QuestionSyncManager;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.ISyncablePath;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.ColorPalets.ColorPalette;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.ColorPalets.ColorPaletteRepeatOptions;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.ColorPalets.ColorPalettes;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.Button;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConfigEditQuestion extends AbstractQuestion<ConfigEditQuestion> implements ISyncablePath {
    private final int MAX_LINES = 14;

    private ConfigEditPlayerData playerData;
    private ConfigEditData configData;
    private AbstractConfigEditSection rootSection;
    private Runnable saveChangesAction = () -> {};
    private Boolean showSaveChangesButton = false;

    protected final Consumer<String> setPathAction = path -> {
        Bukkit.broadcast(Component.text("[SetPathAction] checking and setting path: " + path));
        playerData.setPath(configData.getMinimalValidPath(path));
        Bukkit.broadcast(Component.text("[SetPathAction] path ckeck result: " + playerData.getPath()));

    };
    protected final Function<String, Object> getValueAction = path -> {
        return configData.getValueOrDefault(path, null);
    };
    protected final BiConsumer<String, Object> setValueAction = (path, value) -> {configData.setValue(path, value); Bukkit.broadcast(Component.text("Setting value: " + path + " to " + value));};

    protected final BiConsumer<String, Object> setPlayerSettingAction = (path, value) -> playerData.setSetting(path, value);
    protected final Function<String, Object> getPlayerSettingAction = path -> playerData.getSetting(path);


    private ConfigEditQuestion(ConfigEditPlayerData playerData, ConfigEditData configData) {
        this.playerData = playerData;
        this.configData = configData;
    }

    public void setRootSection(ConfigEditSection rootSection) {
        this.rootSection = rootSection;
    }

    @Contract(value = "_, _ -> new")
    public static ConfigEditQuestion create(ConfigEditPlayerData playerData, ConfigEditData configData) {
        return new ConfigEditQuestion(playerData, configData).colorPalette(ColorPalettes.YELLOW_ISH);
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

        rootSection.pathActionRecursive(setPathAction);
        rootSection.reloadActionRecursive(reloadAction);
        rootSection.disableActionRecursive(customAction);
        rootSection.setValueRecursive(setValueAction);
        rootSection.getValueRecursive(getValueAction);
        rootSection.setPlayerSettingsActionRecursive(setPlayerSettingAction);
        rootSection.getPlayerSettingsActionRecursive(getPlayerSettingAction);
        rootSection.colorPaletteRecursive(colorPalette());

        AbstractConfigEditSection section = rootSection.getSubSectionFromPath(configData.getMinimalValidPath(playerData.getPath()));


        // title
        question(Component.text("--------------", colorPalette().SECONDARY(), TextDecoration.BOLD, TextDecoration.STRIKETHROUGH)
                .append(Component.text(" Config Editor ", colorPalette().PRIMARY(), TextDecoration.BOLD).decoration(TextDecoration.STRIKETHROUGH, false))
                .append(Component.text("--------------", colorPalette().SECONDARY(), TextDecoration.BOLD, TextDecoration.STRIKETHROUGH)));


        // path up button
        if (playerData.getPath().isEmpty()) {
            content = content.append(Component.text("[↑↑]", colorPalette().DISABLED()));
        }else {
            content = content.append(Button.create()
                    .text(Component.text("[↑↑]", colorPalette().SECONDARY()))
                    .addAction(CustomAction.create(p -> goUpOnePathLayer()))
                    .reloadAction(reloadAction)
                    .reloadOnUse(true)
                    .compile());
        }
        // path display
        content = content.appendSpace().append(ColorPalette.applyPattern(makePathDisplayComponent(playerData.getPath()), new ColorPaletteRepeatOptions(colorPalette().PRIMARY(), 1), new ColorPaletteRepeatOptions(colorPalette().PRIMARY_DARKER(), 1))) ;


        //config/content
        for (Component component : correctContentLength(section.getContent(player))) {
            content = content.appendNewline().append(component);
        }

        Button saveButton = Button.create()
                .text(Component.text(" [Save] ", colorPalette().GREEN(), TextDecoration.BOLD).decoration(TextDecoration.STRIKETHROUGH, false))
                .addAction(CustomAction.create(p -> saveChangesAction.run()));
        Button cancelButton = Button.create()
                .text(Component.text(" [Cancel] ", colorPalette().RED(), TextDecoration.BOLD).decoration(TextDecoration.STRIKETHROUGH, false))
                .addAction(CustomAction.create(exitAction::run));
        Component withSaveButton = saveButton.compile()
                .append(Component.text("---", colorPalette().SECONDARY(), TextDecoration.BOLD, TextDecoration.STRIKETHROUGH))
                .append(cancelButton.compile());
        Component withoutSaveButton = Component.text("-----", colorPalette().SECONDARY(), TextDecoration.BOLD, TextDecoration.STRIKETHROUGH)
                .append(cancelButton.compile())
                .append(Component.text("-----", colorPalette().SECONDARY(), TextDecoration.BOLD, TextDecoration.STRIKETHROUGH));


        // footer
        Component footer = Component.text("-----------", colorPalette().SECONDARY(), TextDecoration.BOLD, TextDecoration.STRIKETHROUGH);
        if (showSaveChangesButton) {
            footer = footer.append(withSaveButton);
        }else {
            footer = footer.append(withoutSaveButton);
        }
        footer = footer.append(Component.text("-----------", colorPalette().SECONDARY(), TextDecoration.BOLD, TextDecoration.STRIKETHROUGH));
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
                .text(Component.text("Root", colorPalette().PRIMARY()))
                .reloadAction(reloadAction)
                .reloadOnUse(true)
                .addAction(CustomAction.create(p -> setPathAction.accept("")))
                .reloadAction(reloadAction)
                .reloadOnUse(true)
                .hoverText(Component.text("Click to go to: ", colorPalette().SECONDARY()).append(Component.text("Root", colorPalette().PRIMARY())))
                .compile();
        String[] splitPath = playerData.getPath().split("\\.");
        if (!splitPath[0].isEmpty()) pathDisplay = pathDisplay.append(Component.text(".", colorPalette().SECONDARY()));

        for (int i = 0; i < splitPath.length; i++) {
            int finalI = i;
            pathDisplay = pathDisplay.append(
                    Button.create()
                            .text(Component.text(splitPath[i], colorPalette().PRIMARY()))
                            .reloadAction(reloadAction)
                            .reloadOnUse(true)
                            .addAction(CustomAction.create(p -> setPathAction.accept(String.join(".", Arrays.copyOfRange(splitPath, 0, finalI + 1)))))
                            .reloadAction(reloadAction)
                            .reloadOnUse(true)
                            .hoverText(Component.text("Click to go to: ", colorPalette().SECONDARY()).append(Component.text(splitPath[i], colorPalette().PRIMARY())))
                            .compile());
            if (i < splitPath.length - 1) {
                pathDisplay = pathDisplay.append(Component.text(".", colorPalette().SECONDARY()));
            }
        }

        return ComponentUtils.limitLengthStartWithEllipsis(pathDisplay, 51, colorPalette().DISABLED());
    }

    private List<Component> correctContentLength(List<Component> components) {
        // Ensures a fixed number of lines for UI alignment
        if (components.size() != MAX_LINES) {
            int emptyLines = MAX_LINES - components.size();
            for (int i = 0; i < emptyLines; i++) {
                components.add(Component.text(""));
            }
        }
        return components;
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
        clone.colorPalette(colorPalette());
        clone.saveChangesAction(saveChangesAction());

        return clone;
    }

}
