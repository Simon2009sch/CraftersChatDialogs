package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class YesButton extends AbstractButton<YesButton> {

    private final static Component TEXT = Component.text().resetStyle().build()
            .append(Component.text("[", NamedTextColor.GREEN))
            .append(Component.text("Yes", NamedTextColor.GREEN))
            .append(Component.text("]", NamedTextColor.GREEN));

    private YesButton() {
    }
    @Contract("-> new")
    public static @NotNull YesButton create() {
        YesButton yesButton = new YesButton();
        yesButton.text(TEXT);
        return yesButton;
    }

    @Override
    public Component compile(int uses) {
        return super.compile(uses);
    }

    @Override
    public YesButton clone() {
        YesButton clone = new YesButton();
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
