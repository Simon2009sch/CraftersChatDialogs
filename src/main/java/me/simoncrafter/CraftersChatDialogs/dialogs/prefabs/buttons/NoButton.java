package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class NoButton extends AbstractButton<NoButton> {

    private final static Component TEXT = Component.text().resetStyle().build()
            .append(Component.text("[", NamedTextColor.RED))
            .append(Component.text("No", NamedTextColor.RED))
            .append(Component.text("]", NamedTextColor.RED));

    private NoButton() {
    }

    @Contract("-> new")
    public static @NotNull NoButton create() {
        NoButton noButton = new NoButton();
        noButton.text(TEXT);
        return noButton;
    }


    @Override
    public Component compile(int uses) {
        return super.compile(uses);
    }

    @Override
    public NoButton clone() {
        NoButton clone = new NoButton();
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
