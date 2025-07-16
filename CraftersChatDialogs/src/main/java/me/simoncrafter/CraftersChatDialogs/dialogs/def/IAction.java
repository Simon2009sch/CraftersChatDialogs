package me.simoncrafter.CraftersChatDialogs.dialogs.def;

import org.bukkit.entity.Player;

public interface IAction extends Cloneable {
    void run(Player player);

    void setDisabled(boolean disabled);
    boolean isDisabled();
    IAction clone();
}
