package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion;

import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.ColorPalets.ColorPalette;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.ColorPalets.ColorPalettes;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ConfigEditValue<T extends ConfigEditValue<T>> {
    private Component name = Component.empty();
    private Component description = Component.empty();
    private boolean showDescription = false;
    private Permission showPermission = null;
    private Permission editPermission = null;
    private Function<Player, Consumer<Boolean>> reloadAction = p -> c -> {};
    private Function<String, Object> getValueAction = s -> null;
    private BiConsumer<String, Object> setValueAction = (path, value) -> {};
    private BiConsumer<String, Object> setPlayerSettingAction = (path, value) -> {};
    private Function<String, Object> getPlayerSettingAction = path -> null;
    private CustomAction disableAction;
    private String pathName = "";
    private String path = "";
    private @NotNull ColorPalette colorPalette = ColorPalettes.Overrides.CONFIG_VALUE_DEFAULT;


    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T colorPalette(@NotNull ColorPalette colorPalette) {
        this.colorPalette = colorPalette;
        return (T) this;
    }
    public final @NotNull ColorPalette colorPalette() {
        return colorPalette;
    }
    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final T modifyColorPalette(Function<ColorPalette, ColorPalette> modifier) {
        colorPalette = modifier.apply(colorPalette);
        return (T) this;
    }

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T setDescription(Component description) {
        this.description = description;
        return (T) this;
    }

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T setDescriptionShow(Component description) {
        this.description = description;
        this.showDescription = true;
        return (T) this;
    }

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T setDescriptionHide(Component description) {
        this.description = description;
        this.showDescription = false;
        return (T) this;
    }

    public final Component description() {
        return description;
    }

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T showDescription(boolean showDescription) {
        this.showDescription = showDescription;
        return (T) this;
    }

    public final boolean showDescription() {
        return showDescription;
    }

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T showPermission(Permission permission) {
        this.showPermission = permission;
        return (T) this;
    }

    public final Permission showPermission() {
        return showPermission;
    }

    public final boolean checkShowPermission(Player player) {
        // Default to allowed if no permission is required
        return showPermission == null || player.hasPermission(showPermission);
    }

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T editPermission(Permission permission) {
        this.editPermission = permission;
        return (T) this;
    }

    public final Permission editPermission() {
        return editPermission;
    }

    public final boolean checkEditPermission(Player player) {
        // Default to allowed if no edit permission is required
        return editPermission == null || player.hasPermission(editPermission);
    }

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T setPermission(Permission permission) {
        this.showPermission = permission;
        this.editPermission = permission;
        return (T) this;
    }

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T name(Component name) {
        this.name = name;
        return (T) this;
    }

    public final Component name() {
        return name;
    }

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T getValueAction(Function<String, Object> action) {
        this.getValueAction = action;
        return (T) this;
    }
    public final Function<String, Object> getValueAction() {
        return getValueAction;
    }

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T setValueAction(BiConsumer<String, Object> action) {
        this.setValueAction = action;
        return (T) this;
    }
    public final BiConsumer<String, Object> setValueAction() {
        return setValueAction;
    }

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T reloadAction(Function<Player, Consumer<Boolean>> action) {
        this.reloadAction = action;
        return (T) this;
    }

    public final Function<Player, Consumer<Boolean>> reloadAction() {
        return reloadAction;
    }

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T disableAction(CustomAction action) {
        this.disableAction = action;
        return (T) this;
    }

    public final CustomAction disableAction() {
        return disableAction;
    }

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    protected void onChangePathName(String pathName) {}
    public final T pathName(String pathName) {
        this.pathName = pathName;
        onChangePathName(pathName);
        return (T) this;
    }

    public final String pathName() {
        return pathName;
    }

    protected void onChangePath(String path) {}
    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T path(String path) {
        this.path = path;
        onChangePath(path);
        return (T) this;
    }

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T setPlayerSettingAction(BiConsumer<String, Object> action) {
        this.setPlayerSettingAction = action;
        return (T) this;
    }
    public final BiConsumer<String, Object> setPlayerSettingAction() {
        return setPlayerSettingAction;
    }

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T getPlayerSettingAction(Function<String, Object> action) {
        this.getPlayerSettingAction = action;
        return (T) this;
    }
    public final Function<String, Object> getPlayerSettingAction() {
        return getPlayerSettingAction;
    }


    public final String path() {
        return path;
    }
    public abstract Object getValue();

    public abstract Component getValueToDisplay();

    public abstract Component getButtonsToDisplay();

    public Component getNameToDisplay() {
        if (name == null || PlainTextComponentSerializer.plainText().serialize(name).isEmpty()) {
            name = Component.text(pathName);
        }
        // Attach hover tooltip with description if enabled
        return showDescription ? name.hoverEvent(HoverEvent.showText(description)) : name;
    }

    public abstract T clone();

    public abstract void disableButtons();

    public String getCompletePath() {
        return path + (path.isEmpty() ? "" : ".") + pathName;
    }



    protected <V extends ConfigEditValue<V>> V copyBaseFieldsInto(V target) {
        return target
                .name(name())
                .setDescription(description())
                .showDescription(showDescription())
                .showPermission(showPermission())
                .editPermission(editPermission())
                .path(path())
                .pathName(pathName())
                .getValueAction(getValueAction())
                .setValueAction(setValueAction())
                .reloadAction(reloadAction())
                .disableAction(disableAction())
                .colorPalette(colorPalette())
                .setPlayerSettingAction(setPlayerSettingAction())
                .getPlayerSettingAction(getPlayerSettingAction());
    }
}