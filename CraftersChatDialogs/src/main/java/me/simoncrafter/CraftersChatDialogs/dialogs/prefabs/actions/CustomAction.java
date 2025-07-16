package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CustomAction implements IAction {
    private Consumer<Player> action = p -> {};
    private boolean disabled = false;

    private CustomAction() {}

    private CustomAction(@NotNull Consumer<Player> action) {
        this.action = action;
    }

    @Override
    public void run(@Nullable Player player) {
        if (disabled) {return;}
        action.accept(player);
    }

    @Contract("-> new")
    public static @NotNull CustomAction create() {
        return new CustomAction();
    }

    @Contract("_ -> new")
    public static @NotNull CustomAction create(@NotNull Consumer<Player> action) {
        return new CustomAction(action);
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull CustomAction action(@NotNull Consumer<Player> action) {
        this.action = action;
        return this;
    }

    public @NotNull Consumer<Player> action() {
        return action;
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public CustomAction clone() {
        CustomAction clone = new CustomAction(action);
        clone.disabled = disabled;
        return clone;
    }
}
