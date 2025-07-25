package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import org.bukkit.entity.Player;
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
        run(player, "");
    }
    public void run(Player player, String syncKey) {
        run(player, syncKey, false);
    }
    public void run(Player player, String syncKey, boolean respect) {
        if (disabled) {return;}
        question.show(player, syncKey, respect);
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public QuestionAction clone() {
        QuestionAction clone = new QuestionAction(question.clone());
        clone.disabled = disabled;
        return clone;
    }
}
