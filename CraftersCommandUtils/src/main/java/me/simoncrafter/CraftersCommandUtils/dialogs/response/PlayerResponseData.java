package me.simoncrafter.CraftersCommandUtils.dialogs.response;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.simoncrafter.CraftersCommandUtils.InstanceData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PlayerResponseData {

    private Player player;
    private int time = 0;
    private int maxTime = 60;
    private Function<Player, Consumer<String>> onResponse = p -> message -> {};
    private List<Consumer<Player>> onTimeOut = new ArrayList<>();
    private BukkitTask timerLoop;
    private boolean canceled = false;

    public PlayerResponseData(Player player, int maxTime, Function<Player, Consumer<String>> onResponse) {
        this.player = player;
        this.maxTime = maxTime;
        this.onResponse = onResponse;
        init();
    }

    public PlayerResponseData(Player player, Function<Player, Consumer<String>> onResponse) {
        this.player = player;
        this.onResponse = onResponse;
        init();
    }

    private void init() {
        timerLoop = new BukkitRunnable(){
            @Override
            public void run() {
                time++;
                if (time >= maxTime) {
                    for (Consumer<Player> consumerList : onTimeOut) {
                        consumerList.accept(player);
                    }
                    cancel();
                    timerLoop.cancel();
                    if (canceled) {
                        cancel();
                    }
                }
            }
        }.runTaskTimer(InstanceData.getPlugin(), 0, 20);
    }



    public void addOnTimeOut(Consumer<Player> onTimeOut) {
        this.onTimeOut.add(onTimeOut);
    }
    public void addOnTimeOut(List<Consumer<Player>> onTimeOuts) {
        this.onTimeOut.addAll(onTimeOuts);
    }
    public void onResponse(AsyncChatEvent event) {
        timerLoop.cancel(); //remove timer loop for performance reasons
        if (canceled) {
            return;
        }
        event.setCancelled(true);
        new BukkitRunnable() {
            @Override
            public void run() {
                onResponse.apply(player).accept(PlainTextComponentSerializer.plainText().serialize(event.message())); // call function
            }
        }.runTask(InstanceData.getPlugin());
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
