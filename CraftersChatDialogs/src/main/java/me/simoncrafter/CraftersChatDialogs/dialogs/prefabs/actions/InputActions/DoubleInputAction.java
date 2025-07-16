package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DoubleInputAction extends RetryableInputAction<DoubleInputAction> {

    private Function<Player, Consumer<Double>> onResponse = p -> response -> {};


    public DoubleInputAction(Function<Player, Consumer<Double>> onResponse) {
        this.onResponse = onResponse;
    }

    public DoubleInputAction() {
    }

    @Contract(value = "-> new")
    public static DoubleInputAction create() {
        return new DoubleInputAction();
    }

    @Contract(value = "-> new")
    public static DoubleInputAction create(Function<Player, Consumer<Double>> onResponse) {
        return new DoubleInputAction(onResponse);
    }

    public DoubleInputAction onResponse(Function<Player, Consumer<Double>> handler) {
        this.onResponse = handler;
        return this;
    }




    @Override
    protected void onResponse(Player player, String message) {
        double out;
        try {
            out = Double.parseDouble(message);
            onResponse.apply(player).accept(out);
            handleSuccess(player);
        }catch (Exception e) {
            handleRetry(player);
        }

    }

    @Override
    public DoubleInputAction clone() {
        DoubleInputAction clone = new DoubleInputAction(onResponse);
        clone.setPreActions(preActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setPostActions(postActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setTimeoutActions(timeoutActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setCancelActions(cancelActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setPrePromptActions(prePromptActions().stream().map(IAction::clone).collect(Collectors.toList()));

        clone.successActions(successActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.reTryActions(reTryActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.reTry(reTry());

        clone.maxResponseTime(maxResponseTime());
        clone.syncKey(syncKey());
        clone.setDisabled(isDisabled());
        clone.prompt(prompt());
        clone.reTryMessage(reTryMessage());
        clone.colorPalette(colorPalette());



        return clone;
    }

}
