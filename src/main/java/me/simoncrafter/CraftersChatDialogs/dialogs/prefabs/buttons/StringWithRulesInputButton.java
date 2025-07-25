package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.StringInputAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.StringWithRulesInputAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StringWithRulesInputButton extends AbstractButton<StringWithRulesInputButton> {

    private final static Component TEXT = Component.text().resetStyle().build()
            .append(Component.text("[", NamedTextColor.BLUE))
            .append(Component.text("Set Text", NamedTextColor.BLUE))
            .append(Component.text("]", NamedTextColor.BLUE));

    private StringWithRulesInputAction action = StringWithRulesInputAction.create();

    private StringWithRulesInputButton() {
    }

    @Contract(value = "_ -> new")
    public static @NotNull StringWithRulesInputButton create(Function<Player, Consumer<String>> onResponse) {
        StringWithRulesInputButton button = new StringWithRulesInputButton();
        button.action.onResponse(onResponse);
        button.text(TEXT);
        return button;
    }
    @Contract(value = "_ -> new")
    public static @NotNull StringWithRulesInputButton create(StringWithRulesInputAction stringInputAction) {
        StringWithRulesInputButton button = new StringWithRulesInputButton();
        button.action = stringInputAction;
        button.text(TEXT);
        return button;
    }


    @Contract(pure = true)
    public @NotNull StringWithRulesInputAction stringInputAction() {
        return action;
    }

    @Contract(value = "_ -> this")
    public @NotNull StringWithRulesInputButton stringInputAction(@NotNull StringWithRulesInputAction action) {
        this.action = action;
        return this;
    }

    @Override
    public Component compile(int uses) {
        addAction(action);
        return super.compile(uses);
    }

    @Override
    public StringWithRulesInputButton clone() {
        StringWithRulesInputButton clone = new StringWithRulesInputButton();
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
