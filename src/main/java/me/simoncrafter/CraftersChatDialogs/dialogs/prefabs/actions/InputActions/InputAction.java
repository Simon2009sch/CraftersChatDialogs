package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions;

import me.simoncrafter.CraftersChatDialogs.dialogs.QuestionSyncManager;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions.DisplayOption;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions.DisplayOptions;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.MessageAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.response.PlayerResponseManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class InputAction<T extends InputAction<T>> implements IAction {

    private boolean disabled = false;
    private List<@NotNull IAction> preActions = new ArrayList<>();
    private List<@NotNull IAction> postActions = new ArrayList<>();
    private List<@NotNull IAction> timeoutActions = new ArrayList<>();
    private List<@NotNull IAction> cancelActions = new ArrayList<>();
    private @NotNull IAction messageAction = MessageAction.create("");
    private int maxResponseTime = 60;
    private String syncKey = "";
    private DisplayOption displayOption = DisplayOptions.DEFAULT;
    private Component prompt = Component.text("No Input prompt given!");


    public InputAction() {
    }

    // -- Builder-style fluent methods --

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final T displayOption(@NotNull DisplayOption displayOption) {
        this.displayOption = displayOption;
        return (T) this;
    }
    public final DisplayOption displayOption() {
        return displayOption;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final T syncKey(@NotNull String syncKey) {
        this.syncKey = syncKey;
        return (T) this;
    }
    public final String syncKey() {
        return syncKey;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public final T addPreAction(@NotNull IAction action) {
        this.preActions.add(action);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public final T setPreActions(@NotNull List<@NotNull IAction> actions) {
        this.preActions.addAll(actions);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public final T removePreAction(@NotNull IAction action) {
        this.preActions.remove(action);
        return (T) this;
    }

    @Contract(value = "-> this")
    public final @NotNull List<@NotNull IAction> preActions() {
        return preActions;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public final T addPostAction(@NotNull IAction action) {
        this.postActions.add(action);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public final T removePostAction(@NotNull IAction action) {
        this.postActions.remove(action);
        return (T) this;
    }


    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public final T setPostActions(@NotNull List<@NotNull IAction> actions) {
        this.postActions.addAll(actions);
        return (T) this;
    }

    @Contract(value = "-> this")
    public final @NotNull List<@NotNull IAction> postActions() {
        return postActions;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public final T addTimeoutAction(@NotNull IAction action) {
        this.timeoutActions.add(action);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public final T setTimeoutActions(@NotNull List<@NotNull IAction> actions) {
        this.timeoutActions.addAll(actions);
        return (T) this;
    }

    @Contract(value = "-> this")
    public final @NotNull List<@NotNull IAction> timeoutActions() {
        return timeoutActions;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public final T maxResponseTime(int seconds) {
        this.maxResponseTime = seconds;
        return (T) this;
    }
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public final T prompt(@NotNull Component prompt) {
        this.prompt = prompt;
        return (T) this;
    }
    public final Component prompt() {
        return prompt;
    }

    public final int maxResponseTime() {
        return maxResponseTime;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public final T addCancelAction(@NotNull IAction action) {
        this.cancelActions.add(action);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public final T setCancelActions(@NotNull List<@NotNull IAction> actions) {
        this.cancelActions.addAll(actions);
        return (T) this;
    }
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this")
    public final T removeCancelAction(@NotNull IAction action) {
        this.cancelActions.remove(action);
        return (T) this;
    }
    public final @NotNull List<@NotNull IAction> cancelActions() {
        return cancelActions;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public final T messageAction(@NotNull IAction messageAction) {
        this.messageAction = messageAction;
        return (T) this;
    }
    public final @NotNull IAction messageAction() {
        return messageAction;
    }

    // -- Execution --

    protected abstract void onResponse(@NotNull Player player, @NotNull String message);

    @Override
    public void run(@NotNull Player player) {
        if (disabled) {
            return;
        }

        QuestionSyncManager.addEditingPlayer(syncKey, player); // add the player to the question sync Manager so they don't get synced till they finish editing

        for (IAction action : preActions) {
            action.run(player);
        }
        AbstractQuestion.clearChat(player);

        //retrymessage
        messageAction.run(player);

        if  (!PlainTextComponentSerializer.plainText().serialize(prompt).isEmpty() || prompt == Component.empty() || prompt == Component.text("")) {
            player.sendMessage(prompt);
        }
        PlayerResponseManager.waitForResponse(player, maxResponseTime, p -> message -> {
            setDisabled(true);

            //cancel logic and allowing cancel as input and the \ also
            if (message.equals("cancel")) {
                for (IAction action : cancelActions) {
                    action.run(player);
                }
                QuestionSyncManager.removeEditingPlayer(syncKey, player); // remove the player from the question so they get synced agan
                return;
            }
            if (message.equals("\\cancel")) {
                message = "cancel";
            }
            if (message.startsWith("\\")) {
                message = message.substring(1);
            }
             onResponse(p, message);

            for (IAction action : postActions) {
                action.run(player);
            }
            QuestionSyncManager.removeEditingPlayer(syncKey, player); // remove the player from the question so they get synced agan
        }, List.of(p -> {
            for (IAction action : timeoutActions) {
                action.run(p);
            }
            QuestionSyncManager.removeEditingPlayer(syncKey, player); // remove the player from the question so they get synced agan
        }));
    }

    @Override
    public final void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public final boolean isDisabled() {
        return disabled;
    }

    public abstract T clone();
}
