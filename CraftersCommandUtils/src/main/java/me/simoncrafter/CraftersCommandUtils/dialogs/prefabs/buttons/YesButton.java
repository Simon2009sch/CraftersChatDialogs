package me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.buttons;

import me.simoncrafter.CraftersCommandUtils.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersCommandUtils.dialogs.def.IAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
}
