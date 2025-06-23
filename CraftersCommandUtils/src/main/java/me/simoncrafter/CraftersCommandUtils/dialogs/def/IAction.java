package me.simoncrafter.CraftersCommandUtils.dialogs.def;

import org.bukkit.entity.Player;

public interface IAction {
    void run(Player player);

    void setDisabled(boolean disabled);
    boolean isDisabled();
}
