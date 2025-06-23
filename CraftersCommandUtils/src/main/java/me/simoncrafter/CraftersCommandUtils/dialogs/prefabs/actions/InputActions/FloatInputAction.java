package me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.InputActions;

import me.simoncrafter.CraftersCommandUtils.dialogs.def.IAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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
    void onResponse(Player player, String message) {
        float out;
        try {
            out = Float.parseFloat(message);
            onResponse.apply(player).accept(out);
            handleSuccess(player);
        }catch (Exception e) {
            handleRetry(player);
        }

    }
}
