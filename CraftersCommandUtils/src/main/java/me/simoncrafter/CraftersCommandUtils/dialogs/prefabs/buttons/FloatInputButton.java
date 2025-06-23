package me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.buttons;

import me.simoncrafter.CraftersCommandUtils.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersCommandUtils.dialogs.def.IAction;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.InputActions.FloatInputAction;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.InputActions.StringInputAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public class FloatInputButton extends AbstractButton<FloatInputButton> {

    private final static Component TEXT = Component.text().resetStyle().build()
            .append(Component.text("[", NamedTextColor.BLUE))
            .append(Component.text("Set Float", NamedTextColor.BLUE))
            .append(Component.text("]", NamedTextColor.BLUE));

    private FloatInputAction action = FloatInputAction.create();
    private final CustomAction customAction = CustomAction.create(p -> {
        action.run(p);
    });

    private FloatInputButton() {
    }

    @Contract(value = "_ -> new")
    public static @NotNull FloatInputButton create(Function<Player, Consumer<Float>> onResponse) {
        FloatInputButton button = new FloatInputButton();
        button.action.onResponse(onResponse);
        button.text(TEXT);
        button.addAction(button.customAction);
        return button;
    }
    @Contract(value = "-> new")
    public static @NotNull FloatInputButton create() {
        FloatInputButton button = new FloatInputButton();
        button.text(TEXT);
        button.addAction(button.customAction);
        return button;
    }


    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull FloatInputButton addFloatPreAction(@NotNull IAction action) {
        this.action.addPreAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull FloatInputButton addFloatPostAction(@NotNull IAction action) {
        this.action.addPostAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull FloatInputButton removeFloatPreAction(@NotNull IAction action) {
        this.action.removePreAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull FloatInputButton removeFloatPostAction(@NotNull IAction action) {
        this.action.removePostAction(action);
        return this;
    }

    @Contract(pure = true)
    public @NotNull FloatInputAction floatInputAction() {
        return action;
    }

    @Contract(value = "_ -> this")
    public @NotNull FloatInputButton floatInputButton(@NotNull FloatInputAction action) {
        this.action = action;
        return this;
    }

    @Override
    public Component compile(int uses) {
        return super.compile(uses);
    }
}
