package me.simoncrafter.CraftersChatDialogs.dialogs.response;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlayerResponseManager {

    private static List<PlayerResponseData> responses = new ArrayList<>();

    public static void  waitForResponse(Player player, int maxTime, Function<Player, Consumer<String>> onResponse, List<Consumer<Player>> onTimeout) {
        PlayerResponseData data = new PlayerResponseData(player, maxTime, onResponse);
        data.addOnTimeOut(p -> {
            responses.remove(data);
        });
        data.addOnTimeOut(onTimeout);
        responses.add(data);
    }
    public static void waitForResponse(Player player, int maxTime, Function<Player, Consumer<String>> onResponse) {
        waitForResponse(player, maxTime, onResponse, null);
    }

    public static void waitForResponse(Player player, Function<Player, Consumer<String>> onResponse) {
        waitForResponse(player, 60, onResponse, null);
    }


    public static void onChatMessage(AsyncChatEvent event) {
        for (PlayerResponseData data : responses) {
            if (data.getPlayer() == event.getPlayer()) {
                data.onResponse(event);
                responses.remove(data);
                return;
            }
        }
    }
    public static void cancelWaitingForResponse(Player player) {
        for (PlayerResponseData data : responses) {
            if (data.getPlayer() == player) {
                responses.remove(data);
                data.setCanceled(true);
                return;
            }
        }
    }



}
