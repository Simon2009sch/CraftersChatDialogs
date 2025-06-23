package me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.questions;

import me.simoncrafter.CraftersCommandUtils.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersCommandUtils.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.CustomAction;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GenericQuestion extends AbstractQuestion<GenericQuestion> {

    List<AbstractButton<?>> buttons = new ArrayList<>();

    private GenericQuestion(Component question) {
        question(question);
    }
    private GenericQuestion() {
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull GenericQuestion create(@NotNull Component question) {
        return new GenericQuestion(question);
    }

    @Contract(value = "-> new", mutates = "this")
    public static @NotNull GenericQuestion create() {
        return new GenericQuestion();
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull GenericQuestion setButtons(@NotNull List<AbstractButton<?>> buttons) {
        this.buttons.clear();
        this.buttons.addAll(buttons);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull GenericQuestion addButton(@NotNull AbstractButton<?> button) {
        this.buttons.add(button);
        return this;
    }

    public @NotNull List<AbstractButton<?>> buttons() {
        return new ArrayList<>(buttons);
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull GenericQuestion removeButton(@NotNull AbstractButton<?> button) {
        this.buttons.remove(button);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull GenericQuestion removeButton(@NotNull int index) {
        this.buttons.remove(index);
        return this;
    }


    @Override
    public void beforeShow(@NotNull Player player) {
        // add disable action and set content
        Component content = Component.text().resetStyle().build();
        CustomAction customAction = CustomAction.create(p -> {
            disableButtons();
        });
        for (AbstractButton<?> button : buttons) {
            button.addPostAction(customAction);
            content = content.append(Component.text(" ")).append(button.compile());
        }

        content(content);
    }

    public void disableButtons() {
        for (AbstractButton<?> button : buttons) {
            button.setDisabled(true);
        }
    }


}
