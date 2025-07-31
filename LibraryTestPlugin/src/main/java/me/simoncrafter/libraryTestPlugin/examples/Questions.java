package me.simoncrafter.libraryTestPlugin.examples;

import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.MessageAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.QuestionAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.SoundAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.Button;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.NoButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.YesButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfirmQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.GenericQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.YesNoQuestion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Questions {


    // Yes / No Question:
    //  Takes in a Yes-Button instance and a No-Button instance
    public static void yesNoQuestion(Player player) {
        YesNoQuestion question = YesNoQuestion.create(Component.text("Do you like coffee for breakfast?"))
                .yesButton(YesButton.create().addAction(MessageAction.create("Great! Me too!")))
                .noButton(NoButton.create().addAction(MessageAction.create(Component.text("Aw, what else do you like?", NamedTextColor.RED))));
        question.show(player);
    }

    // Confirm Question:
    //  Takes in a Confirmation Action, cancel action and a action that lets it return to the question that it was called from
    //  The return action will be called on cancel and confirm
    public static void confirmQuestion(Player player) {
        GenericQuestion question = GenericQuestion.create(Component.text("Press the button to get to the confirm question"));
        question.addButton(Button.create()
                .text(Component.text("[Open Confirm Dialog]"))
                .addAction(QuestionAction.create()
                        .question(ConfirmQuestion.create()
                                .addCancelAction(MessageAction.create("Cancelled"))
                                .addCancelAction(SoundAction.create(Sound.ENTITY_VILLAGER_NO))
                                .addConfirmAction(MessageAction.create("Confirmed"))
                                .addConfirmAction(SoundAction.create(Sound.ENTITY_VILLAGER_YES))
                                .returnToQuestionAction(QuestionAction.create(question))
                        )
                )
        );

        question.show(player);
    }

    // Generic Question:
    //  Takes in a List of button Instances
    public static void genericQuestion(Player player) {
        GenericQuestion question = GenericQuestion.create(Component.text("Press the button to get to the generic question"))
                .addButton(Button.create()
                        .text(Component.text("[Sound]"))
                        .addAction(SoundAction.create(Sound.ITEM_GOAT_HORN_SOUND_4)))
                .addButton(Button.create()
                        .text(Component.text("[Message]")))
                .addButton(Button.create()
                        .text(Component.text("[CustomAction]"))
                        .addAction(CustomAction.create(p -> {
                            for (int i = 0; i < 360; i++) {
                                double rad = i / 180d * Math.PI;
                                for (int ii = 0; ii < 360; ii++) {
                                    double rad1 = ii / 180d * Math.PI;
                                    p.getWorld().spawnParticle(Particle.FLAME, p.getLocation().add(Math.sin(rad) * Math.cos(rad1), 0.5 + Math.sin(rad), Math.cos(rad) * Math.sin(rad1)), 2);
                                }
                            }
                        })))
                .addExitButton();

        question.show(player);
    }

}
