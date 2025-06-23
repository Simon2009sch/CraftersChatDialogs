package me.simoncrafter.CraftersCommandUtils;

import org.bukkit.NamespacedKey;

public class PersistentDataContainer {

    public static final NamespacedKey DIALOG_RESPONSE = new NamespacedKey(InstanceData.getPlugin(), "DIALOG_RESPONSE");
    public static final NamespacedKey IN_DIALOG = new NamespacedKey("crafters_command_dialogs", "DIALOG_RESPONSE");

}
