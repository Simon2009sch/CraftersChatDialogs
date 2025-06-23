package me.simoncrafter.CraftersCommandUtils.dialogs;

import me.simoncrafter.CraftersCommandUtils.dialogs.def.AbstractQuestion;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionPlayerSyncData {

    private Map<Player, AbstractQuestion<?>> players = new HashMap<>();
    private List<Player> editingPlayers = new ArrayList<>();

    public QuestionPlayerSyncData(Map<Player, AbstractQuestion<?>> players) {
        this.players = players;
    }

    public void addEditingPlayer(Player player) {
        editingPlayers.add(player);
    }
    public void removeEditingPlayer(Player player) {
        editingPlayers.remove(player);
    }
    public void setEditingPlayers(List<Player> players) {
        this.editingPlayers = players;
    }
    public boolean isEditingPlayer(Player player) {
        return editingPlayers.contains(player);
    }
    public List<Player> getEditingPlayers() {
        return editingPlayers;
    }
    public void addPlayer(Player player, AbstractQuestion<?> question) {
        players.put(player, question);
    }
    public void removePlayer(Player player) {
        players.remove(player);
    }
    public Map<Player, AbstractQuestion<?>> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Player, AbstractQuestion<?>> players) {
        this.players = players;
    }

    public void onReload(AbstractQuestion<?> question) {
        if (!players.containsValue(question)) {
            return;
        }
        for (Player q : players.keySet()) {
            if (editingPlayers.contains(q)) {
                continue;
            }
            if (players.get(q) == question) {
                continue;
            }
            players.get(q).reload(true);
        }
    }


}
