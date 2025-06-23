package me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.InputActions;

import me.simoncrafter.CraftersCommandUtils.dialogs.def.IAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class StringInputAction extends InputAction<StringInputAction> {

    // Function that, given a Player, returns a Consumer<String> which processes the input string
    private Function<Player, Consumer<String>> onResponse = p -> response -> {};

    private StringInputAction() {}

    private StringInputAction(@NotNull Function<Player, Consumer<String>> onResponse) {
        this.onResponse = onResponse;
    }

    @Contract(value = "_ -> new")
    public static @NotNull StringInputAction create(@NotNull Function<Player, Consumer<String>> onResponse) {
        return new StringInputAction(onResponse);
    }

    @Contract(value = "-> new")
    public static @NotNull StringInputAction create() {
        return new StringInputAction();
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull StringInputAction onResponse(@NotNull Function<Player, Consumer<String>> onResponse) {
        this.onResponse = onResponse;
        return this;
    }

    public @NotNull Function<Player, Consumer<String>> onResponse() {
        return onResponse;
    }

    @Override
    void onResponse(@NotNull Player player, @NotNull String message) {
        onResponse.apply(player).accept(message);
    }
}
