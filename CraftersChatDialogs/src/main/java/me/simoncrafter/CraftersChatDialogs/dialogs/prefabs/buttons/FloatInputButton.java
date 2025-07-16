package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.FloatInputAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FloatInputButton extends AbstractButton<FloatInputButton> {

    private final static Component TEXT = Component.text().resetStyle().build()
            .append(Component.text("[", NamedTextColor.BLUE))
            .append(Component.text("Set Float", NamedTextColor.BLUE))
            .append(Component.text("]", NamedTextColor.BLUE));

    private FloatInputAction floatInputAction = FloatInputAction.create();
    private final CustomAction customAction = CustomAction.create(p -> {
        floatInputAction.run(p);
    });

    private FloatInputButton() {
    }

    @Contract(value = "_ -> new")
    public static @NotNull FloatInputButton create(Function<Player, Consumer<Float>> onResponse) {
        FloatInputButton button = new FloatInputButton();
        button.floatInputAction.onResponse(onResponse);
        button.text(TEXT);
        return button;
    }
    @Contract(value = "_ -> new")
    public static @NotNull FloatInputButton create(FloatInputAction floatInputAction) {
        FloatInputButton button = new FloatInputButton();
        button.floatInputAction = floatInputAction;
        button.text(TEXT);
        return button;
    }
    @Contract(value = "-> new")
    public static @NotNull FloatInputButton create() {
        FloatInputButton button = new FloatInputButton();
        button.text(TEXT);
        return button;
    }


    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull FloatInputButton addFloatPreAction(@NotNull IAction action) {
        this.floatInputAction.addPreAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull FloatInputButton addFloatPostAction(@NotNull IAction action) {
        this.floatInputAction.addPostAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull FloatInputButton removeFloatPreAction(@NotNull IAction action) {
        this.floatInputAction.removePreAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull FloatInputButton removeFloatPostAction(@NotNull IAction action) {
        this.floatInputAction.removePostAction(action);
        return this;
    }

    @Contract(pure = true)
    public @NotNull FloatInputAction floatInputAction() {
        return floatInputAction;
    }

    @Contract(value = "_ -> this")
    public @NotNull FloatInputAction floatInputAction(@NotNull FloatInputAction action) {
        this.floatInputAction = action;
        return action;
    }

    @Override
    public Component compile(int uses) {
        addAction(floatInputAction);
        return super.compile(uses);
    }

    @Override
    public FloatInputButton clone() {
        FloatInputButton clone = new FloatInputButton();
        clone.text(text());
        clone.setDisabled(isDisabled());
        clone.setActions(actions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setPostActions(postActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.hoverText(hoverText());
        clone.reloadOnUse(reloadOnUse());
        clone.reloadAction(reloadAction());
        clone.floatInputAction(floatInputAction().clone());
        return clone;
    }
}
