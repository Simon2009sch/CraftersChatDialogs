package me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.InputActions;

import me.simoncrafter.CraftersCommandUtils.dialogs.QuestionSyncManager;
import me.simoncrafter.CraftersCommandUtils.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersCommandUtils.dialogs.def.IAction;
import me.simoncrafter.CraftersCommandUtils.dialogs.response.PlayerResponseManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Sound;
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
    private List<@NotNull IAction> prePromtActions = new ArrayList<>();
    private int maxResponseTime = 60;
    private String syncKey = "";
    private Component prompt = Component.text("Input value in chat!")
            .color(NamedTextColor.GOLD)
            .appendNewline()
            .append(Component.text("")
                    .append(Component.text("Type cancel to cancel.")
                            .color(NamedTextColor.GRAY)
                            .clickEvent(ClickEvent.suggestCommand("cancel"))
                            .hoverEvent(HoverEvent.showText(Component.text("Click to paste \"cancel\" in chat")))
                            .appendNewline()
                            .append(Component.text(" Put a \\ before it to input \"cancel\"")
                                    .clickEvent(ClickEvent.suggestCommand("\\cancel"))
                                    .hoverEvent(HoverEvent.showText(Component.text("Click to paste \"\\cancel\" in chat"))))));


    public InputAction() {
    }

    // -- Builder-style fluent methods --

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public T syncKey(@NotNull String syncKey) {
        this.syncKey = syncKey;
        return (T) this;
    }
    public String syncKey() {
        return syncKey;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T addPreAction(@NotNull IAction action) {
        this.preActions.add(action);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T setPreActions(@NotNull List<@NotNull IAction> actions) {
        this.preActions.addAll(actions);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T removePreAction(@NotNull IAction action) {
        this.preActions.remove(action);
        return (T) this;
    }

    @Contract(value = "-> this")
    public @NotNull List<@NotNull IAction> preActions() {
        return preActions;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T addPostAction(@NotNull IAction action) {
        this.postActions.add(action);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T removePostAction(@NotNull IAction action) {
        this.postActions.remove(action);
        return (T) this;
    }


    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T setPostActions(@NotNull List<@NotNull IAction> actions) {
        this.postActions.addAll(actions);
        return (T) this;
    }

    @Contract(value = "-> this")
    public @NotNull List<@NotNull IAction> postActions() {
        return postActions;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T addTimeoutAction(@NotNull IAction action) {
        this.timeoutActions.add(action);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T setTimeoutActions(@NotNull List<@NotNull IAction> actions) {
        this.timeoutActions.addAll(actions);
        return (T) this;
    }

    @Contract(value = "-> this")
    public @NotNull List<@NotNull IAction> timeoutActions() {
        return timeoutActions;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T maxResponseTime(int seconds) {
        this.maxResponseTime = seconds;
        return (T) this;
    }
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T prompt(@NotNull Component prompt) {
        this.prompt = prompt;
        return (T) this;
    }
    public Component prompt() {
        return prompt;
    }

    public int maxResponseTime() {
        return maxResponseTime;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T addCancelAction(@NotNull IAction action) {
        this.cancelActions.add(action);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T setCancelActions(@NotNull List<@NotNull IAction> actions) {
        this.cancelActions.addAll(actions);
        return (T) this;
    }
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this")
    public T removeCancelAction(@NotNull IAction action) {
        this.cancelActions.remove(action);
        return (T) this;
    }
    public @NotNull List<@NotNull IAction> cancelActions() {
        return cancelActions;
    }
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T addPrePromptAction(@NotNull IAction action) {
        this.prePromtActions.add(action);
        return (T) this;
    }
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this", mutates = "this")
    public T setPrePromptActions(@NotNull List<@NotNull IAction> actions) {
        this.prePromtActions.addAll(actions);
        return (T) this;
    }
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> this")
    public T removePrePromptAction(@NotNull IAction action) {
        this.prePromtActions.remove(action);
        return (T) this;
    }
    public @NotNull List<@NotNull IAction> prePromptActions() {
        return prePromtActions;
    }

    // -- Execution --

    abstract void onResponse(@NotNull Player player, @NotNull String message);

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
        for (IAction action : prePromtActions) {
            action.run(player);
        }
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
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }
}
