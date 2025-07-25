package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class EmptyAction implements IAction {

    private EmptyAction() {
    }


    @Contract("-> new")
    public static @NotNull EmptyAction create() {
        return new EmptyAction();
    }


    @Override
    public void run(Player player) {}

    @Override
    public void setDisabled(boolean disabled) {}

    @Override
    public boolean isDisabled() {
        return true;
    }

    @Override
    public EmptyAction clone() {
        return create();
    }
}
