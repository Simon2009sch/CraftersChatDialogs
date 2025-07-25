package me.simoncrafter.CraftersChatDialogs;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.simoncrafter.CraftersChatDialogs.dialogs.response.PlayerResponseManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Listeners implements Listener {

    @EventHandler
    public void playerAsyncChatEvent(AsyncChatEvent event) {
        PlayerResponseManager.onChatMessage(event);
    }

}
