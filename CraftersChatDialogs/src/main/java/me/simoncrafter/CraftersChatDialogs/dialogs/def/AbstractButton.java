package me.simoncrafter.CraftersChatDialogs.dialogs.def;

import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions.DisplayOption;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions.DisplayOptions;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractButton<T extends AbstractButton<T>> {
    private List<@NotNull IAction> actions = new ArrayList<>();
    private List<@NotNull IAction> postActions = new ArrayList<>();
    private @NotNull Component text = Component.empty();
    private final @NotNull UUID uuid = UUID.randomUUID();
    private Component hoverText = Component.empty();
    private boolean disabled = false;
    private Function<Player, Consumer<Boolean>> reloadAction = p -> c -> {};
    private boolean reloadOnUse = false;
    private DisplayOption displayOption = DisplayOptions.DEFAULT;
    // Builder-style methods below


    /**
     * <b>Only run this method out of a question. (Or if you know what your doing, whatever)</b>
     */
    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T reloadAction(@NotNull Function<Player, Consumer<Boolean>> reloadAction) {
        this.reloadAction = reloadAction;
        return (T) this;
    }
    public final Function<Player, Consumer<Boolean>> reloadAction() {
        return this.reloadAction;
    }
    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T reloadOnUse(boolean reloadOnUse) {
        this.reloadOnUse = reloadOnUse;
        return (T) this;
    }
    public final boolean reloadOnUse() {
        return this.reloadOnUse;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T displayOption(@NotNull DisplayOption displayOption) {
        this.displayOption = displayOption;
        return (T) this;
    }
    public DisplayOption displayOption() {
        return this.displayOption;
    }
    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final T modifyDisplayOption(Function<DisplayOption, DisplayOption> modifier) {
        displayOption = modifier.apply(displayOption);
        return (T) this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T addPreAction(@NotNull IAction action) {
        this.actions.add(action);
        return (T) this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T setActions(@NotNull List<@NotNull IAction> actions) {
        this.actions = new ArrayList<>(actions);
        return (T) this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T addAction(@NotNull IAction action) {
        this.actions.add(action);
        return (T) this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T removeAction(@NotNull IAction action) {
        this.actions.remove(action);
        return (T) this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T text(@NotNull Component text) {
        this.text = text;
        return (T) this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T addPostAction(@NotNull IAction action) {
        this.postActions.add(action);
        return (T) this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T removePostAction(@NotNull IAction action) {
        this.postActions.remove(action);
        return (T) this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T setPostActions(@NotNull List<@NotNull IAction> postActions) {
        this.postActions = new ArrayList<>(postActions);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public final T setDisabled(boolean enabled) {
        this.disabled = enabled;
        return (T) this;
    }

    public final boolean isDisabled() {
        return disabled;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public final @NotNull T hoverText(@NotNull Component hoverText) {
        this.hoverText = hoverText;
        return (T) this;
    }
    public final @NotNull Component hoverText() {
        return hoverText;
    }

    public final @NotNull List<@NotNull IAction> postActions() {
        return postActions;
    }

    public final @NotNull List<@NotNull IAction> actions() {
        return actions;
    }

    public final @NotNull Component text() {
        return text;
    }

    public final @NotNull UUID uuid() {
        return uuid;
    }

    public @NotNull Component compile() {
        return compile(1);
    }

    public @NotNull Component compile(int uses) {
        ClickCallback clickCallback = audience -> onPress((Player) audience);
        return Component.text("")
                .append(text.clickEvent(ClickEvent.callback(clickCallback, ClickCallback.Options.builder().uses(uses).build())).hoverEvent(hoverText));
    }

    public final void onPress(@NotNull Player player) {
        if (disabled) {
            return;
        }
        setDisabled(true);
        for (IAction action : actions) {
            action.run(player);
        }
        for (IAction action : postActions) {
            action.run(player);
        }
        if (reloadOnUse && reloadAction != null) reloadAction.apply(player).accept(false);
    }

    public abstract T clone();

}
