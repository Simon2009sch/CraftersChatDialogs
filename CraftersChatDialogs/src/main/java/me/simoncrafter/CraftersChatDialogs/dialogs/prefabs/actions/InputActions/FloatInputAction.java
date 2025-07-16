package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FloatInputAction extends RetryableInputAction<FloatInputAction> {

    private Function<Player, Consumer<Float>> onResponse = p -> response -> {};


    public FloatInputAction(Function<Player, Consumer<Float>> onResponse) {
        this.onResponse = onResponse;
    }

    public FloatInputAction() {
    }

    @Contract(value = "-> new")
    public static FloatInputAction create() {
        return new FloatInputAction();
    }

    @Contract(value = "-> new")
    public static FloatInputAction create(Function<Player, Consumer<Float>> onResponse) {
        return new FloatInputAction(onResponse);
    }

    public FloatInputAction onResponse(Function<Player, Consumer<Float>> handler) {
        this.onResponse = handler;
        return this;
    }




    @Override
    protected void onResponse(Player player, String message) {
        float out;
        try {
            out = Float.parseFloat(message);
            onResponse.apply(player).accept(out);
            handleSuccess(player);
        }catch (Exception e) {
            handleRetry(player);
        }

    }

    @Override
    public FloatInputAction clone() {
        FloatInputAction clone = new FloatInputAction(onResponse);
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
