package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.NoButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.YesButton;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class YesNoQuestion extends AbstractQuestion<YesNoQuestion> {

    private YesButton yesButton;
    private NoButton noButton;

    @Contract(value = "-> new")
    public static @NotNull YesNoQuestion create() {
        return new YesNoQuestion();
    }

    @Contract(value = "_ -> new")
    public static @NotNull YesNoQuestion create(Component question) {
        YesNoQuestion YNQuestion = new YesNoQuestion();
        YNQuestion.question(question);
        return YNQuestion;
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

    @Override
    public YesNoQuestion clone() {
        YesNoQuestion clone = new YesNoQuestion();
        clone.question(question());
        clone.question(question());
        clone.footer(footer());
        clone.permission(permission());
        clone.syncKey(syncKey());
        clone.onReload(onReload());
        clone.player(player());
        clone.yesButton(yesButton().clone());
        clone.noButton(noButton().clone());
        clone.displayOption(displayOption());

        return clone;
    }
}
