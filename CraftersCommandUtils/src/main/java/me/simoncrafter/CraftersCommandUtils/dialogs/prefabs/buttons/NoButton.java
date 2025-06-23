package me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.buttons;

import me.simoncrafter.CraftersCommandUtils.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersCommandUtils.dialogs.def.IAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
}
