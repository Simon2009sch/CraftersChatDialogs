package me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions;

import me.simoncrafter.CraftersCommandUtils.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersCommandUtils.dialogs.def.IAction;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QuestionAction implements IAction {

    private AbstractQuestion<?> question;
    private boolean disabled = false;

    private QuestionAction(@Nullable AbstractQuestion<?> question) {
        this.question = question;
    }

    private QuestionAction() {
    }

    @Contract(value = "_ -> new")
    public static @NotNull QuestionAction create(@Nullable AbstractQuestion<?> question) {
        return new QuestionAction(question);
    }

    @Contract(value = "-> new")
    public static @NotNull QuestionAction create() {
        return new QuestionAction();
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull QuestionAction question(@NotNull AbstractQuestion<?> question) {
        this.question = question;
        return this;
    }

    public @Nullable AbstractQuestion<?> question() {
        return question;
    }



    @Override
    public void run(Player player) {
        if (disabled) {return;}
        question.show(player);
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }
}
