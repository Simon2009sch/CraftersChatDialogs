package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ClearChatAction implements IAction {

    private boolean disabled = false;


    private ClearChatAction() {
    }


    @Contract("-> new")
    public static @NotNull ClearChatAction create() {
        return new ClearChatAction();
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
    public ClearChatAction clone() {
        ClearChatAction clone = new ClearChatAction();
        clone.setDisabled(isDisabled());
        return clone;
    }
}
