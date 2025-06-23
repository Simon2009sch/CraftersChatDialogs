package me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.InputActions;

import me.simoncrafter.CraftersCommandUtils.dialogs.def.IAction;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

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
    void onResponse(Player player, String message) {
        int out;
        try {
            out = Integer.parseInt(message);
            onResponse.apply(player).accept(out);
            handleSuccess(player);
        }catch (Exception e) {
            handleRetry(player);
        }

    }
}
