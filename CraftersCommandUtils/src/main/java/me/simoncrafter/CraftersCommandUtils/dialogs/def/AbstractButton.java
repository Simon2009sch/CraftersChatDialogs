package me.simoncrafter.CraftersCommandUtils.dialogs.def;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractButton<T extends AbstractButton<T>> {
    private List<@NotNull IAction> actions = new ArrayList<>();
    private List<@NotNull IAction> postActions = new ArrayList<>();
    private @NotNull Component text = Component.empty();
    private final @NotNull UUID uuid = UUID.randomUUID();
    private Component hoverText = Component.empty();
    private boolean disabled = false;
    // Builder-style methods below

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public @NotNull T setActions(@NotNull List<@NotNull IAction> actions) {
        this.actions = new ArrayList<>(actions);
        return (T) this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public @NotNull T addAction(@NotNull IAction action) {
        this.actions.add(action);
        return (T) this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public @NotNull T removeAction(@NotNull IAction action) {
        this.actions.remove(action);
        return (T) this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public @NotNull T text(@NotNull Component text) {
        this.text = text;
        return (T) this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public @NotNull T addPostAction(@NotNull IAction action) {
        this.postActions.add(action);
        return (T) this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public @NotNull T removePostAction(@NotNull IAction action) {
        this.postActions.remove(action);
        return (T) this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public @NotNull T setPostActions(@NotNull List<@NotNull IAction> postActions) {
        this.postActions = new ArrayList<>(postActions);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T setDisabled(boolean enabled) {
        this.disabled = enabled;
        return (T) this;
    }

    public boolean isDisabled() {
        return disabled;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull T hoverText(@NotNull Component hoverText) {
        this.hoverText = hoverText;
        return (T) this;
    }
    public @NotNull Component hoverText() {
        return hoverText;
    }

    public @NotNull List<@NotNull IAction> postActions() {
        return postActions;
    }

    public @NotNull List<@NotNull IAction> actions() {
        return actions;
    }

    public @NotNull Component text() {
        return text;
    }

    public @NotNull UUID uuid() {
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

    public void onPress(@NotNull Player player) {
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
    }




}
