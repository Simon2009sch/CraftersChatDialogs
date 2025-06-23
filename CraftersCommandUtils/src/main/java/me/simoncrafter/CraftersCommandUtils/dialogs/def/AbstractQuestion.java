package me.simoncrafter.CraftersCommandUtils.dialogs.def;

import me.simoncrafter.CraftersCommandUtils.dialogs.QuestionPlayerManager;
import me.simoncrafter.CraftersCommandUtils.dialogs.QuestionSyncManager;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.CustomAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

public abstract class AbstractQuestion<T extends AbstractQuestion<T>> {
    private final UUID uuid = UUID.randomUUID();
    private @Nullable Permission permission;
    private @Nullable Component question;
    private @Nullable Component content;
    private @Nullable Component footer;
    private @Nullable CustomAction onReload;
    private @Nullable String syncKey = "";
    private Player player;

    protected final CustomAction exitAction = CustomAction.create((player) -> exit());

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public @NotNull T permission(@Nullable Permission permission) {
        this.permission = permission;
        return (T) this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public @NotNull T question(@NotNull Component question) {
        this.question = question;
        return (T) this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public @NotNull T content(@NotNull Component content) {
        this.content = content;
        return (T) this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public @NotNull T footer(@NotNull Component footer) {
        this.footer = footer;
        return (T) this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public @NotNull T onReload(@NotNull CustomAction onReload) {
        this.onReload = onReload;
        return (T) this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public @NotNull T onReload(@NotNull Consumer<Player> onReload) {
        this.onReload = CustomAction.create(onReload);
        return (T) this;
    }
    @Contract(value = "-> this", mutates = "this")
    public @NotNull String syncKey() {
        return syncKey;
    }




    public @Nullable Permission permission() {
        return permission;
    }

    public @Nullable Component question() {
        return question;
    }

    public @Nullable Component content() {
        return content;
    }

    public @Nullable Component footer() {
        return footer;
    }

    public @NotNull UUID uuid() {
        return uuid;
    }

    public @Nullable CustomAction onReload() {
        return onReload;
    }

    protected abstract void beforeShow(@NotNull Player player);


    public void show(@NotNull Player player) {
        show(player, "");
    }

    public void show(@NotNull Player player, @NotNull String syncKey) {
        show(player, syncKey, false);
    }
    public void show(@NotNull Player player, @NotNull String syncKey, boolean force) {
        this.player = player;
        if (permission != null && !player.hasPermission(permission)) {
            player.sendMessage(
                    Component.text("Permission denied!").color(NamedTextColor.RED)
                            .hoverEvent(HoverEvent.showText(Component.text("You need: " + permission.getName())))
            );
            return;
        }
        // check if player is in dialog and force logic
        if (!QuestionPlayerManager.allowPlayerToOpenQuestion(player) && !force) {
            return;
        } else if (force) {
            QuestionPlayerManager.onForceOpenQuestion(player);
        }

        this.syncKey = syncKey;
        QuestionPlayerManager.addQuestionToPlayer(player, this);

        //add sync if needed
        if (!syncKey.isEmpty()) {
            QuestionSyncManager.addQuestion(syncKey, player, this);
        }

        beforeShow(player);
        clearChat(player);

        Component out = Component.text("")
                .append(question != null ? question : Component.empty())
                .appendNewline()
                .append(content != null ? content : Component.empty())
                .appendNewline()
                .append(footer != null ? footer : Component.empty());
        player.sendMessage(out);
    }

    protected void exit() {
        QuestionPlayerManager.removeQuestionFromPlayer(player);
    }

    public static void clearChat(@NotNull Player player) {
        Component out = Component.empty();
        for (int i = 0; i < 30; i++) {
            out = out.append(Component.newline());
        }
        player.sendMessage(out);
    }

    abstract public void disableButtons();

    public void reload(Player player) {
        reload(player, false);
    }
    private void reload(Player player, boolean triggeredFromCascade) {
        if (!triggeredFromCascade) {
            QuestionSyncManager.onReload(syncKey, this);
        }
        disableButtons();
        if (syncKey != null && !syncKey.isEmpty()) {
            QuestionPlayerManager.removeQuestionFromPlayer(player);
        }
        if (onReload == null) {
            return;
        }
        onReload.run(player);
    }
    public void reload(boolean triggeredFromCascade) {
        reload(player, triggeredFromCascade);
    }

}
