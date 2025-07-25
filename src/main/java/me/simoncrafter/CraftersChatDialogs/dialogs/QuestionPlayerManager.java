package me.simoncrafter.CraftersChatDialogs.dialogs;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.response.PlayerResponseManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class QuestionPlayerManager {

    private static Map<Player, AbstractQuestion<?>> playerQuestionMap = new HashMap<>();

    public static boolean allowPlayerToOpenQuestion(Player player) {
        return playerQuestionMap.get(player) == null;
    }
    public static void addQuestionToPlayer(Player player, AbstractQuestion<?> question) {
        if (playerQuestionMap.get(player) != null) {
            playerQuestionMap.get(player).disableButtons(); // remove and disable previous question
        }
        playerQuestionMap.put(player, question);
    }
    public static void removeQuestionFromPlayer(Player player) {
        if (playerQuestionMap.get(player) != null) {
            playerQuestionMap.get(player).disableButtons(); // remove and disable previous question
        }
        playerQuestionMap.remove(player);
    }
    public static void onForceOpenQuestion(Player player) {
        removeQuestionFromPlayer(player);
        PlayerResponseManager.cancelWaitingForResponse(player);
    }

}
