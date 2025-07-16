package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.ClearCharAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.MessageAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.Button;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GenericQuestion extends AbstractQuestion<GenericQuestion> {

    private List<AbstractButton<?>> buttons = new ArrayList<>();

    private Button exitButton = Button.create()
            .addAction(ClearCharAction.create())
            .addAction(MessageAction.create("Exited Question"))
            .text(Component.text("[Exit]", NamedTextColor.RED));

    private GenericQuestion(Component question) {
        question(question);
    }
    private GenericQuestion() {
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull GenericQuestion addExitButton() {
        exitButton.addAction(exitAction);
        this.buttons.add(exitButton);
        return this;
    }
    public @NotNull GenericQuestion removeExitButton() {
        exitButton.removeAction(exitAction);
        this.buttons.remove(exitButton);
        return this;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull GenericQuestion create(@NotNull Component question) {
        return new GenericQuestion(question);
    }

    @Contract(value = "-> new", mutates = "this")
    public static @NotNull GenericQuestion create() {
        return create(Component.text("Generic Question: (No Question set)", NamedTextColor.AQUA));
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
            button.reloadAction(reloadAction);
            content = content.append(Component.text(" ")).append(button.compile());
        }

        content(content);
    }

    public void disableButtons() {
        for (AbstractButton<?> button : buttons) {
            button.setDisabled(true);
        }
    }

    @Override
    public GenericQuestion clone() {
        GenericQuestion clone = new GenericQuestion(question());
        clone.question(question());
        clone.footer(footer());
        clone.permission(permission());
        clone.syncKey(syncKey());
        clone.onReload(onReload());
        clone.player(player());
        clone.colorPalette(colorPalette());

        clone.setButtons(buttons());
        return clone;
    }
}
