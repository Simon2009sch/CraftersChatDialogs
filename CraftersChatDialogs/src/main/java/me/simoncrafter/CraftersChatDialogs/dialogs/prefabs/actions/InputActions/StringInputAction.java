package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    protected void onResponse(@NotNull Player player, @NotNull String message) {
        onResponse.apply(player).accept(message);
    }

    @Override
    public StringInputAction clone() {
        StringInputAction clone = new StringInputAction(onResponse);
        clone.setPreActions(preActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setPostActions(postActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setTimeoutActions(timeoutActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setCancelActions(cancelActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setPrePromptActions(prePromptActions().stream().map(IAction::clone).collect(Collectors.toList()));

        clone.maxResponseTime(maxResponseTime());
        clone.syncKey(syncKey());
        clone.setDisabled(isDisabled());
        clone.prompt(prompt());
        clone.colorPalette(colorPalette());

        return clone;
    }
}
