package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ClearCharAction implements IAction {

    private boolean disabled = false;


    private ClearCharAction() {
    }


    @Contract("-> new")
    public static @NotNull ClearCharAction create() {
        return new ClearCharAction();
    }


    @Override
    public void run(Player player) {
        if (disabled) {return;}
        AbstractQuestion.clearChat(player);
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
    public ClearCharAction clone() {
        ClearCharAction clone = new ClearCharAction();
        clone.setDisabled(isDisabled());
        return clone;
    }
}
