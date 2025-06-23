package me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.buttons;

import me.simoncrafter.CraftersCommandUtils.dialogs.def.AbstractButton;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
}
