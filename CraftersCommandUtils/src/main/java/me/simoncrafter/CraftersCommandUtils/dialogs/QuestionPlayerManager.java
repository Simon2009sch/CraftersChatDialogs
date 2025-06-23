package me.simoncrafter.CraftersCommandUtils.dialogs;

import me.simoncrafter.CraftersCommandUtils.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersCommandUtils.dialogs.def.AbstractQuestion;
import me.simoncrafter.CraftersCommandUtils.dialogs.response.PlayerResponseManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class QuestionPlayerManager {

    private static Map<Player, AbstractQuestion<?>> playerQuestionMap = new HashMap<>();

    public static boolean allowPlayerToOpenQuestion(Player player) {
        return playerQuestionMap.get(player) == null;
    }
    public static void addQuestionToPlayer(Player player, AbstractQuestion<?> question) {
        playerQuestionMap.put(player, question);
    }
    public static void removeQuestionFromPlayer(Player player) {
        playerQuestionMap.remove(player);
    }
    public static void onForceOpenQuestion(Player player) {
        removeQuestionFromPlayer(player);
        PlayerResponseManager.cancelWaitingForResponse(player);
    }

}
