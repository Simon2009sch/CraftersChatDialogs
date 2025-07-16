package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.IntInputAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IntInputButton extends AbstractButton<IntInputButton> {

    private final static Component TEXT = Component.text().resetStyle().build()
            .append(Component.text("[", NamedTextColor.BLUE))
            .append(Component.text("Set Int", NamedTextColor.BLUE))
            .append(Component.text("]", NamedTextColor.BLUE));

    private IntInputAction intInputAction = IntInputAction.create();
    private final CustomAction customAction = CustomAction.create(p -> {
        intInputAction.run(p);
    });

    private IntInputButton() {
    }

    @Contract(value = "_ -> new")
    public static @NotNull IntInputButton create(Function<Player, Consumer<Integer>> onResponse) {
        IntInputButton button = new IntInputButton();
        button.intInputAction.onResponse(onResponse);
        button.text(TEXT);
        return button;
    }
    @Contract(value = "_ -> new")
    public static @NotNull IntInputButton create(IntInputAction intInputAction) {
        IntInputButton button = new IntInputButton();
        button.intInputAction = intInputAction;
        button.text(TEXT);
        return button;
    }
    @Contract(value = "-> new")
    public static @NotNull IntInputButton create() {
        IntInputButton button = new IntInputButton();
        button.text(TEXT);
        return button;
    }


    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull IntInputButton addIntPreAction(@NotNull IAction action) {
        this.intInputAction.addPreAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull IntInputButton addIntPostAction(@NotNull IAction action) {
        this.intInputAction.addPostAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull IntInputButton removeIntPreAction(@NotNull IAction action) {
        this.intInputAction.removePreAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull IntInputButton removeIntPostAction(@NotNull IAction action) {
        this.intInputAction.removePostAction(action);
        return this;
    }

    @Contract(pure = true)
    public @NotNull IntInputAction intInputAction() {
        return intInputAction;
    }

    @Contract(value = "_ -> this")
    public @NotNull IntInputAction intInputAction(@NotNull IntInputAction action) {
        this.intInputAction = action;
        return action;
    }

    @Override
    public Component compile(int uses) {
        addAction(intInputAction);
        return super.compile(uses);
    }

    @Override
    public IntInputButton clone() {
        IntInputButton clone = new IntInputButton();
        clone.text(text());
        clone.setDisabled(isDisabled());
        clone.setActions(actions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setPostActions(postActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.hoverText(hoverText());
        clone.reloadOnUse(reloadOnUse());
        clone.reloadAction(reloadAction());
        clone.intInputAction(intInputAction().clone());
        return clone;
    }
}
