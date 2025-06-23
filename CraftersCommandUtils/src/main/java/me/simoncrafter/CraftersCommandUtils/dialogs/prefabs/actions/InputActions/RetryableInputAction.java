package me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.InputActions;

import me.simoncrafter.CraftersCommandUtils.dialogs.def.IAction;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.MessageAction;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.SoundAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

public abstract class RetryableInputAction<T extends RetryableInputAction<T>> extends InputAction<T> {

    private boolean tryAgain = false;
    private List<IAction> reTryActions = new ArrayList<>();
    private List<IAction> successActions = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T addSuccessAction(IAction action) {
        this.successActions.add(action);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T removeSuccessAction(IAction action) {
        this.successActions.remove(action);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T successActions(List<IAction> actions) {
        this.successActions = actions;
        return (T) this;
    }
    public List<IAction> successActions() {
        return successActions;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T addReTryAction(IAction action) {
        this.reTryActions.add(action);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T reTryActions(List<IAction> actions) {
        this.reTryActions = actions;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T reTry(boolean tryAgain) {
        this.tryAgain = tryAgain;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T removeReTryAction(IAction action) {
        this.reTryActions.remove(action);
        return (T) this;
    }
    public boolean reTry() {
        return tryAgain;
    }
    // Call when parsing succeeds
    protected void handleSuccess(Player player) {
        for (IAction action : successActions) {
            action.run(player);
        }
    }

    // Call when parsing fails
    protected void handleRetry(Player player) {
        if (tryAgain) {
            for (IAction action : reTryActions) {
                action.run(player);
            }
            setDisabled(false);
            addPrePromptAction(MessageAction.create(Component.text("Please enter a decimal number!", NamedTextColor.RED, TextDecoration.BOLD)));
            run(player);
        }
    }
}
