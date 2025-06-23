package me.simoncrafter.CraftersCommandUtils.dialogs;

import me.simoncrafter.CraftersCommandUtils.dialogs.def.AbstractQuestion;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class QuestionSyncManager {

    private static Map<String, QuestionPlayerSyncData> questions = new HashMap<>();

    public static void addQuestion(String key, Player player, AbstractQuestion<?> question) {
        if (!questions.containsKey(key) || questions.get(key) == null) {
            questions.put(key, new QuestionPlayerSyncData(new HashMap<>()));
        }
        questions.get(key).addPlayer(player, question);

    }
    public static void removeQuestion(String key, Player player) {
        if (questions.containsKey(key) && questions.get(key) != null) {
            questions.get(key).removePlayer(player);
        }
    }

    public static void addEditingPlayer(String key, Player player) {
        if (questions.containsKey(key)) {
            questions.get(key).addEditingPlayer(player);
        }
    }
    public static void removeEditingPlayer(String key, Player player) {
        if (questions.containsKey(key)) {
            questions.get(key).removeEditingPlayer(player);
        }
    }

    public static void onReload(String key, AbstractQuestion<?> question) {
        if (key == null || key.isEmpty()) {
            return;
        }
        if (questions.containsKey(key) || questions.get(key) != null) {
            questions.get(key).onReload(question);
        }
    }
}
