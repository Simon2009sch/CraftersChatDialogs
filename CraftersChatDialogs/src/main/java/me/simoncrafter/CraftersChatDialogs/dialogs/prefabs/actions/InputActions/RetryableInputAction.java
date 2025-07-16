package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.MessageAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

public abstract class RetryableInputAction<T extends RetryableInputAction<T>> extends InputAction<T> {

    private boolean tryAgain = false;
    private List<IAction> reTryActions = new ArrayList<>();
    private List<IAction> successActions = new ArrayList<>();
    private Component reTryMessage = Component.text("Try again?", NamedTextColor.RED, TextDecoration.BOLD);

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

    @Contract(value = "-> this", mutates = "this")
    public List<IAction> reTryActions() {
        return reTryActions;
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
    @Contract(value = "_ -> this", mutates = "this")
    public T reTryMessage(Component reTryMessage) {
        this.reTryMessage = reTryMessage;
        return (T) this;
    }
    public Component reTryMessage() {
        return reTryMessage;
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
            removePrePromptAction(MessageAction.create(reTryMessage));
            addPrePromptAction(MessageAction.create(reTryMessage));
            run(player);
        }
    }
}
