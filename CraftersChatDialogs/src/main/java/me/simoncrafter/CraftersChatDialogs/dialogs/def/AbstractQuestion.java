package me.simoncrafter.CraftersChatDialogs.dialogs.def;

import me.simoncrafter.CraftersChatDialogs.dialogs.QuestionPlayerManager;
import me.simoncrafter.CraftersChatDialogs.dialogs.QuestionSyncManager;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.ColorPalets.ColorPalette;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.ColorPalets.ColorPalettes;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
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
import java.util.function.Function;

public abstract class AbstractQuestion<T extends AbstractQuestion<T>> {
    private final UUID uuid = UUID.randomUUID();
    private Permission permission;
    private Component question = Component.empty();
    private Component content = Component.empty();
    private Component footer = Component.empty();
    private @NotNull CustomAction onReload = CustomAction.create();
    private @Nullable String syncKey = "";
    private Player player;
    private ColorPalette colorPalette = ColorPalettes.NEUTRAL_GRAY;

    public final Function<Player, Consumer<Boolean>> reloadAction = (p) -> (cascade) -> reload(p, cascade);

    protected final CustomAction exitAction = CustomAction.create((player) -> exit());

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T permission(@Nullable Permission permission) {
        this.permission = permission;
        return (T) this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T colorPalette(@NotNull ColorPalette colorPalette) {
        this.colorPalette = colorPalette;
        return (T) this;
    }
    public final @NotNull ColorPalette colorPalette() {
        return colorPalette;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final T modifyColorPalette(Function<ColorPalette, ColorPalette> modifier) {
        colorPalette = modifier.apply(colorPalette);
        return (T) this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T question(@NotNull Component question) {
        this.question = question;
        return (T) this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T content(@NotNull Component content) {
        this.content = content;
        return (T) this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T footer(@NotNull Component footer) {
        this.footer = footer;
        return (T) this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T onReload(@NotNull CustomAction onReload) {
        this.onReload = onReload;
        return (T) this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    @SuppressWarnings("unchecked")
    public final @NotNull T onReload(@NotNull Consumer<Player> onReload) {
        this.onReload = CustomAction.create(onReload);
        return (T) this;
    }
    @Contract(value = "-> this", mutates = "this")
    public final @NotNull String syncKey() {
        return syncKey;
    }
    @Contract(value = "-> this", mutates = "this")
    protected final @NotNull T syncKey(String syncKey) {
        this.syncKey = syncKey;
        return (T) this;
    }

    public final @Nullable Permission permission() {
        return permission;
    }
    public Player player() {
        return player;
    }
    @Contract(value = "_ -> this", mutates = "this")
    protected T player(@NotNull Player player) {
        this.player = player;
        return (T) this;
    }

    public final Component question() {
        return question;
    }

    public final Component content() {
        return content;
    }

    public final Component footer() {
        return footer;
    }

    public final @NotNull UUID uuid() {
        return uuid;
    }

    public final @NotNull CustomAction onReload() {
        return onReload;
    }

    protected abstract void beforeShow(@NotNull Player player);


    public void show(@NotNull Player player) {
        show(player, "");
    }

    public void show(@NotNull Player player, @NotNull String syncKey) {
        show(player, syncKey, false);
    }
    public void show(@NotNull Player player, @NotNull String syncKey, boolean respect) {
        this.player = player;
        if (permission != null && !player.hasPermission(permission)) {
            player.sendMessage(
                    Component.text("Permission denied!").color(NamedTextColor.RED)
                            .hoverEvent(HoverEvent.showText(Component.text("You need: " + permission.getName())))
            );
            return;
        }
        // check if player is in dialog and force logic
        if (!QuestionPlayerManager.allowPlayerToOpenQuestion(player) && respect) {
            return;
        } else if (!respect) {
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
        QuestionSyncManager.removePlayerFromQuestion(syncKey, player);
    }

    public  static void clearChat(@NotNull Player player) {
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
    public void reload(Player player, boolean triggeredFromCascade) {
        if (!triggeredFromCascade) {
            QuestionSyncManager.onReload(syncKey, this);
        }
        disableButtons();
        if (syncKey != null && !syncKey.isEmpty()) {
            QuestionSyncManager.removePlayerFromQuestion(syncKey, player);
        }

        QuestionPlayerManager.removeQuestionFromPlayer(player);
        onReload.run(player);
    }
    public void reload(boolean triggeredFromCascade) {
        reload(player, triggeredFromCascade);
    }

    public abstract T clone();
}
