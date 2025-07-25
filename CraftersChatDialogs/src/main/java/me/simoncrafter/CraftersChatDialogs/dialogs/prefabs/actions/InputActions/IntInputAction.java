package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import org.bukkit.entity.Player;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IntInputAction extends RetryableInputAction<IntInputAction> {

    private Function<Player, Consumer<Integer>> onResponse = p -> response -> {};

    public IntInputAction() {
    }

    public IntInputAction(Function<Player, Consumer<Integer>> onResponse) {
        this.onResponse = onResponse;
    }

    public static IntInputAction create(Function<Player, Consumer<Integer>> onResponse) {
        return new IntInputAction(onResponse);
    }
    public static IntInputAction create() {
        return new IntInputAction();
    }

    public IntInputAction onResponse(Function<Player, Consumer<Integer>> onResponse) {
        this.onResponse = onResponse;
        return this;
    }
    public Function<Player, Consumer<Integer>> onResponse(IAction action) {
        return onResponse;
    }

    @Override
    protected void onResponse(Player player, String message) {
        int out;
        try {
            out = Integer.parseInt(message);
            onResponse.apply(player).accept(out);
            handleSuccess(player);
        }catch (NumberFormatException e) {
            handleRetry(player);
        }
    }

    @Override
    public IntInputAction clone() {
        IntInputAction clone = new IntInputAction(onResponse);
        clone.setPreActions(preActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setPostActions(postActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setTimeoutActions(timeoutActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setCancelActions(cancelActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.messageAction(messageAction().clone());

        clone.successActions(successActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.reTryActions(reTryActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.reTry(reTry());

        clone.maxResponseTime(maxResponseTime());
        clone.syncKey(syncKey());
        clone.setDisabled(isDisabled());
        clone.prompt(prompt());
        clone.reTryMessage(reTryMessage());
        clone.displayOption(displayOption());



        return clone;
    }
}
