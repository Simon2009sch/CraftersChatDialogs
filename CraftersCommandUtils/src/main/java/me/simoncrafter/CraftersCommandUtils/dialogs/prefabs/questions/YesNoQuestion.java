package me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.questions;

import me.simoncrafter.CraftersCommandUtils.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersCommandUtils.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersCommandUtils.dialogs.def.IAction;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.buttons.NoButton;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.buttons.YesButton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class YesNoQuestion extends AbstractQuestion<YesNoQuestion> {

    private YesButton yesButton;
    private NoButton noButton;

    @Contract(value = "-> new")
    public static @NotNull YesNoQuestion create() {
        return new YesNoQuestion();
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull YesNoQuestion yesButton(@NotNull YesButton button) {
        this.yesButton = button;
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull YesNoQuestion noButton(@NotNull NoButton button) {
        this.noButton = button;
        return this;
    }

    @Override
    protected void beforeShow(@NotNull Player player) {
        // add disable action and set content
        Component content = Component.text().resetStyle().build();
        CustomAction customAction = CustomAction.create(p -> {
            yesButton.setDisabled(true);
            noButton.setDisabled(true);
        });
        yesButton.addPostAction(customAction);
        noButton.addPostAction(customAction);
        content = content.append(Component.text(" ")).append(yesButton.compile());
        content = content.append(Component.text(" ")).append(noButton.compile());

        content(content);
    }

    public @NotNull YesButton yesButton() {
        return yesButton;
    }

    public @NotNull NoButton noButton() {
        return noButton;
    }

    @Override
    public void disableButtons() {
        yesButton.setDisabled(true);
        noButton.setDisabled(true);
    }
}
