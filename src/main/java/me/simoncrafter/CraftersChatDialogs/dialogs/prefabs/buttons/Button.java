package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class Button extends AbstractButton<Button> {

    private final static Component TEXT = Component.text().resetStyle().build()
            .append(Component.text("["))
            .append(Component.text("Button"))
            .append(Component.text("]"));

    private Button() {
    }

    @Contract("-> new")
    public static @NotNull Button create() {
        Button button = new Button();
        button.text(TEXT);
        return button;
    }

    @Override
    public Component compile(int uses) {
        return super.compile(uses);
    }

    @Override
    public Button clone() {
        Button clone = new Button();
        clone.text(text());
        clone.setDisabled(isDisabled());
        clone.setActions(actions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setPostActions(postActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.hoverText(hoverText());
        clone.reloadOnUse(reloadOnUse());
        clone.reloadAction(reloadAction());
        return clone;
    }
}
