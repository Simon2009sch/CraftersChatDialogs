package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.StringInputAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringInputButton extends AbstractButton<StringInputButton> {

    private final static Component TEXT = Component.text().resetStyle().build()
            .append(Component.text("[", NamedTextColor.BLUE))
            .append(Component.text("Set Text", NamedTextColor.BLUE))
            .append(Component.text("]", NamedTextColor.BLUE));

    private StringInputAction action = StringInputAction.create();

    private StringInputButton() {
    }

    @Contract(value = "_ -> new")
    public static @NotNull StringInputButton create(Function<Player, Consumer<String>> onResponse) {
        StringInputButton button = new StringInputButton();
        button.action.onResponse(onResponse);
        button.text(TEXT);
        return button;
    }
    @Contract(value = "_ -> new")
    public static @NotNull StringInputButton create(StringInputAction stringInputAction) {
        StringInputButton button = new StringInputButton();
        button.action = stringInputAction;
        button.text(TEXT);
        return button;
    }


    @Contract(pure = true)
    public @NotNull StringInputAction stringInputAction() {
        return action;
    }

    @Contract(value = "_ -> this")
    public @NotNull StringInputButton stringInputAction(@NotNull StringInputAction action) {
        this.action = action;
        return this;
    }

    @Override
    public Component compile(int uses) {
        addAction(action);
        return super.compile(uses);
    }

    @Override
    public StringInputButton clone() {
        StringInputButton clone = new StringInputButton();
        clone.text(text());
        clone.setDisabled(isDisabled());
        clone.setActions(actions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setPostActions(postActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.hoverText(hoverText());
        clone.reloadOnUse(reloadOnUse());
        clone.reloadAction(reloadAction());
        clone.stringInputAction(stringInputAction());
        return clone;
    }
}
