package me.simoncrafter.CraftersChatDialogs.dialogs;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractQuestion;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class QuestionSyncManager {

    private static Map<String, QuestionPlayerSyncData> questions = new HashMap<>();

    public static void addQuestion(String key, Player player, AbstractQuestion<?> question) {
        if (!isValidSyncKey(key)) return;
        if (!questions.containsKey(key) || questions.get(key) == null) {
            questions.put(key, new QuestionPlayerSyncData(new HashMap<>()));
        }
        questions.get(key).addPlayer(player, question);

    }
    public static void removePlayerFromQuestion(String key, Player player) {
        if (!isValidSyncKey(key)) return;
        if (questions.containsKey(key) && questions.get(key) != null) {
            questions.get(key).removePlayer(player);


            if (questions.get(key).getPlayers().isEmpty()) { // remove if no players are in it
                questions.remove(key);
            }
        }
    }

    public static void addEditingPlayer(String key, Player player) {
        if (!isValidSyncKey(key)) return;
        if (questions.containsKey(key)) {
            questions.get(key).addEditingPlayer(player);
        }
    }
    public static void removeEditingPlayer(String key, Player player) {
        if (!isValidSyncKey(key)) return;
        if (questions.containsKey(key)) {
            questions.get(key).removeEditingPlayer(player);
        }
    }

    public static void onReload(String key, AbstractQuestion<?> question) {
        onReload(key, question, "");
    }
    public static void onReload(String key, AbstractQuestion<?> question, String path) {
        if (!isValidSyncKey(key)) return;
        if (questions.containsKey(key) || questions.get(key) != null) {
            questions.get(key).onReload(question, path);
        }
    }

    private static boolean isValidSyncKey(String key) {
        return key != null && !key.isEmpty();
    }
}
