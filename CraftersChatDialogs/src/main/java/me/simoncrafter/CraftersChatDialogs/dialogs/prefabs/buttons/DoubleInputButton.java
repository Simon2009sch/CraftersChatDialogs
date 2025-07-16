package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.DoubleInputAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DoubleInputButton extends AbstractButton<DoubleInputButton> {

    private final static Component TEXT = Component.text().resetStyle().build()
            .append(Component.text("[", NamedTextColor.BLUE))
            .append(Component.text("Set Double", NamedTextColor.BLUE))
            .append(Component.text("]", NamedTextColor.BLUE));

    private DoubleInputAction doubleInputAction = DoubleInputAction.create();
    private final CustomAction customAction = CustomAction.create(p -> {
        doubleInputAction.run(p);
    });

    private DoubleInputButton() {
    }


    @Contract(value = "_ -> new")
    public static @NotNull DoubleInputButton create(Function<Player, Consumer<Double>> onResponse) {
        DoubleInputButton button = new DoubleInputButton();
        button.doubleInputAction.onResponse(onResponse);
        button.text(TEXT);
        return button;
    }
    @Contract(value = "_ -> new")
    public static @NotNull DoubleInputButton create(DoubleInputAction doubleInputAction) {
        DoubleInputButton button = new DoubleInputButton();
        button.doubleInputAction = doubleInputAction;
        button.text(TEXT);
        return button;
    }
    @Contract(value = "-> new")
    public static @NotNull DoubleInputButton create() {
        DoubleInputButton button = new DoubleInputButton();
        button.text(TEXT);
        return button;
    }


    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull DoubleInputButton addDoublePreAction(@NotNull IAction action) {
        this.doubleInputAction.addPreAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull DoubleInputButton addDoublePostAction(@NotNull IAction action) {
        this.doubleInputAction.addPostAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull DoubleInputButton removeDoublePreAction(@NotNull IAction action) {
        this.doubleInputAction.removePreAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull DoubleInputButton removeDoublePostAction(@NotNull IAction action) {
        this.doubleInputAction.removePostAction(action);
        return this;
    }

    @Contract(pure = true)
    public @NotNull DoubleInputAction doubleInputAction() {
        return doubleInputAction;
    }

    @Contract(value = "_ -> this")
    public @NotNull DoubleInputAction doubleInputAction(@NotNull DoubleInputAction action) {
        this.doubleInputAction = action;
        return action;
    }

    @Override
    public Component compile(int uses) {
        addAction(doubleInputAction);
        return super.compile(uses);
    }

    @Override
    public DoubleInputButton clone() {
        DoubleInputButton clone = new DoubleInputButton();
        clone.text(text());
        clone.setDisabled(isDisabled());
        clone.setActions(actions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setPostActions(postActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.hoverText(hoverText());
        clone.reloadOnUse(reloadOnUse());
        clone.reloadAction(reloadAction());
        clone.doubleInputAction(doubleInputAction().clone());
        return clone;
    }
}
