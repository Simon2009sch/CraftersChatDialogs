package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion;

import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions.DisplayOption;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions.DisplayOptions;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ConfigEditValue<T extends ConfigEditValue<T>> {
    private Component name = Component.empty();
    private Component description = Component.empty();
    private boolean showDescription = false;
    /*private boolean checkShowPermission = true; TODO: Implement permission checks.
    private boolean checkEditPermission = true;
    private boolean checkDisplayPermission = true;*/
    private Function<Player, Consumer<Boolean>> reloadAction = p -> c -> {};
    private Function<String, Object> getValueAction = s -> null;
    private BiConsumer<String, Object> setValueAction = (path, value) -> {};
    private BiConsumer<String, Object> setPlayerSettingAction = (path, value) -> {};
    private Function<String, Object> getPlayerSettingAction = path -> null;
    private Supplier<String> permissionPrefixAction = () -> "crafterschatdialogs.genericConfigEdit";
    private CustomAction disableAction;
    private String pathName = "";
    private String path = "";
    private @NotNull DisplayOption displayOption = DisplayOptions.DEFAULT.modifyColorPalette(c -> c.override(DisplayOptions.ColorPalettes.Overrides.CONFIG_VALUE_DEFAULT));
    private String syncKey = "";

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T displayOption(@NotNull DisplayOption displayOption) {
        this.displayOption = displayOption;
        return (T) this;
    }
    public final @NotNull DisplayOption displayOption() {
        return displayOption;
    }
    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final T modifyColorPalette(Function<DisplayOption, DisplayOption> modifier) {
        displayOption = modifier.apply(displayOption);
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
    public final T syncKey(@NotNull String syncKey) {
        this.syncKey = syncKey;
        return (T) this;
    }
    public final String syncKey() {
        return syncKey;
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

    /*@Contract(mutates = "this", value = "_ -> this")     TODO: Implement permission checks (see line 22-24)
    @SuppressWarnings("unchecked")
    public final T checkShowPermission(boolean checkShowPermission) {
        this.checkShowPermission = checkShowPermission;
        return (T) this;
    }

    public final boolean checkShowPermission() {
        return checkShowPermission;
    }

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T checkEditPermission(boolean checkEditPermission) {
        this.checkEditPermission = checkEditPermission;
        return (T) this;
    }

    public final boolean checkEditPermission() {
        return checkEditPermission;
    }

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T checkDisplayPermission(boolean checkDisplayPermission) {
        this.checkDisplayPermission = checkDisplayPermission;
        return (T) this;
    }
    public final boolean checkDisplayPermission() {
        return checkDisplayPermission;
    }*/

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

    @Contract(mutates = "this", value = "_ -> this")
    @SuppressWarnings("unchecked")
    public final T permissionPrefixAction(Supplier<String> permissionPrefixAction) {
        this.permissionPrefixAction = permissionPrefixAction;
        return (T) this;
    }
    public final Supplier<String> permissionPrefixAction() {
        return permissionPrefixAction;
    }


    public final String path() {
        return path;
    }
    public abstract Object getValue();

    public abstract Component getValueToDisplay();
    public abstract Component getInactiveValueToDisplay();

    public abstract Component getButtonsToDisplay();
    public abstract Component getInactiveButtonsToDisplay();

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
                /*.checkShowPermission(checkShowPermission()) TODO: Info at line 22-24
                .checkEditPermission(checkEditPermission())*/
                .path(path())
                .pathName(pathName())
                .getValueAction(getValueAction())
                .setValueAction(setValueAction())
                .reloadAction(reloadAction())
                .disableAction(disableAction())
                .displayOption(displayOption())
                .setPlayerSettingAction(setPlayerSettingAction())
                .getPlayerSettingAction(getPlayerSettingAction());
    }
}