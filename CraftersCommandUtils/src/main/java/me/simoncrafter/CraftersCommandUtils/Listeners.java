package me.simoncrafter.CraftersCommandUtils;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.simoncrafter.CraftersCommandUtils.dialogs.response.PlayerResponseManager;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Listeners implements Listener {

    @EventHandler
    public void playerAsyncChatEvent(AsyncChatEvent event) {
        PlayerResponseManager.onChatMessage(event);
    }

}
