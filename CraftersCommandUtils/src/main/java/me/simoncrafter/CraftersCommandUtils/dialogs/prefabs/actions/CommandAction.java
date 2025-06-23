package me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions;

import me.simoncrafter.CraftersCommandUtils.dialogs.def.IAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CommandAction implements IAction {
    private String command = "";
    private boolean asOP = false;
    private boolean asConsole = false;
    private boolean disabled = false;

    private CommandAction() {}

    private CommandAction(@NotNull String command) {
        this.command = command;
    }

    @Override
    public void run(@NotNull Player player) {
        if (disabled) {
            return;
        }
        if (asConsole) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            return;
        }

        // Execute command as player, possibly with temporary op
        boolean wasOP = player.isOp();
        if (!wasOP) {
            player.setOp(asOP);
        }
        Bukkit.dispatchCommand(player, command);
        player.setOp(wasOP);
    }

    @Contract(value = "-> new")
    public static @NotNull CommandAction create() {
        return new CommandAction();
    }

    @Contract(value = "_ -> new")
    public static @NotNull CommandAction create(@NotNull String command) {
        return new CommandAction(command);
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull CommandAction command(@NotNull String command) {
        this.command = command;
        return this;
    }

    public @NotNull String command() {
        return command;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull CommandAction asOP(boolean asOP) {
        this.asOP = asOP;
        return this;
    }

    public boolean asOP() {
        return asOP;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull CommandAction asConsole(boolean asConsole) {
        this.asConsole = asConsole;
        return this;
    }

    public boolean asConsole() {
        return asConsole;
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
