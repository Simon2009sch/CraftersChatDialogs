package me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions;

import me.simoncrafter.CraftersCommandUtils.dialogs.def.IAction;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MessageAction implements IAction {

    private Component message = Component.empty();
    private boolean disabled = false;

    private MessageAction(@NotNull Component message) {
        this.message = message;
    }

    private MessageAction(@NotNull String message) {
        this.message = Component.text(message);
    }

    private MessageAction() {
    }

    @Contract("_ -> new")
    public static @NotNull MessageAction create(@NotNull String message) {
        return new MessageAction(message);
    }

    @Contract("_ -> new")
    public static @NotNull MessageAction create(@NotNull Component message) {
        return new MessageAction(message);
    }

    @Contract("-> new")
    public static @NotNull MessageAction create() {
        return new MessageAction();
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull MessageAction message(@NotNull Component message) {
        this.message = message;
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull MessageAction message(@NotNull String message) {
        this.message = Component.text(message);
        return this;
    }

    public @NotNull Component message() {
        return message;
    }

    @Override
    public void run(Player player) {
        if (disabled) {return;}
        player.sendMessage(message);
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }
}
