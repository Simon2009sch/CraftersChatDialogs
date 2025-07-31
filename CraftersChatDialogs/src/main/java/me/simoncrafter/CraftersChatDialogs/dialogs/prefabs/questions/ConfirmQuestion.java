package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.*;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.Button;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConfirmQuestion extends AbstractQuestion<ConfirmQuestion> {

    private List<AbstractButton<?>> buttons = new ArrayList<>();
    private IAction returnToQuestionAction = EmptyAction.create();
    private List<IAction> confirmAction = new ArrayList<>();
    private List<IAction> cancelAction = new ArrayList<>();

    private Button exitButton = Button.create()
            .addAction(ClearChatAction.create())
            .addAction(MessageAction.create("Exited Question"))
            .text(Component.text("[Exit]", NamedTextColor.RED));

    private ConfirmQuestion(Component question) {
        question(question);
    }
    private ConfirmQuestion() {
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull ConfirmQuestion addExitButton() {
        exitButton.addAction(exitAction);
        this.buttons.add(exitButton);
        return this;
    }
    public @NotNull ConfirmQuestion removeExitButton() {
        exitButton.removeAction(exitAction);
        this.buttons.remove(exitButton);
        return this;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ConfirmQuestion create(@NotNull Component question) {
        return new ConfirmQuestion(question);
    }

    @Contract(value = "-> new", mutates = "this")
    public static @NotNull ConfirmQuestion create() {
        return create(Component.text("Generic Question: (No Question set)", NamedTextColor.AQUA));
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull ConfirmQuestion addConfirmAction(IAction action) {
        confirmAction.add(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull ConfirmQuestion addCancelAction(IAction action) {
        cancelAction.add(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull ConfirmQuestion returnToQuestionAction(IAction action) {
        this.returnToQuestionAction = action;
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull ConfirmQuestion removeConfirmAction(IAction action) {
        confirmAction.remove(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull ConfirmQuestion removeCancelAction(IAction action) {
        cancelAction.remove(action);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull ConfirmQuestion setConfirmActions(List<IAction> actions) {
        this.confirmAction = actions;
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull ConfirmQuestion setCancelActions(List<IAction> actions) {
        this.cancelAction = actions;
        return this;
    }
    public @NotNull List<IAction> confirmActions() {
        return confirmAction;
    }
    public @NotNull List<IAction> cancelActions() {
        return cancelAction;
    }

    @Override
    public void beforeShow(@NotNull Player player) {
        // add disable action and set content
        Component content = Component.text().resetStyle().build();
        CustomAction customAction = CustomAction.create(p -> {
            disableButtons();
        });

        // add buttons
        Button confirmButton = Button.create()
                .setActions(confirmAction)
                .addPostAction(returnToQuestionAction)
                .text(Component.text("[Confirm]", displayOption().colorPalette().GREEN()));

        Button cancelButton = Button.create()
                .setActions(cancelAction)
                .addPostAction(returnToQuestionAction)
                .text(Component.text("[Cancel]", displayOption().colorPalette().RED()));

        buttons.add(confirmButton);
        buttons.add(cancelButton);

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

    @Override
    public ConfirmQuestion clone() {
        ConfirmQuestion clone = new ConfirmQuestion(question());
        clone.question(question());
        clone.footer(footer());
        clone.permission(permission());
        clone.syncKey(syncKey());
        clone.onReload(onReload());
        clone.player(player());
        clone.displayOption(displayOption());

        clone.setConfirmActions(confirmActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setCancelActions(cancelActions().stream().map(IAction::clone).collect(Collectors.toList()));
        return clone;
    }
}
