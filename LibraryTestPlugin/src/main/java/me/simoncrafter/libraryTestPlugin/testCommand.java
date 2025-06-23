package me.simoncrafter.libraryTestPlugin;

import me.simoncrafter.CraftersCommandUtils.Clamp;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.*;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.InputActions.FloatInputAction;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.buttons.Button;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.buttons.NoButton;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.buttons.StringInputButton;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.buttons.YesButton;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.questions.GenericQuestion;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.questions.MovementQuestion;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.questions.YesNoQuestion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class testCommand implements CommandExecutor, TabExecutor {

    String name = "[UNKNOWN]";
    Location location = new Location(Bukkit.getWorld("world"), 34, 77, -323);
    float yaw = 0;
    float pitch = 0;
    Entity pig = null;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args[0].equals("yes_no")) {
            CustomAction onHasSchoolBadGrades = CustomAction.create((player) -> {
                player.sendMessage(Component.text("Well then\nfuck you", NamedTextColor.RED, TextDecoration.BOLD));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.kick(Component.text("Only smart people allowed!", NamedTextColor.RED, TextDecoration.BOLD));
                    }
                }.runTaskLater(Main.getInstance(), 40);
            });

            YesNoQuestion onHasSchool = YesNoQuestion.create()
                    .question(Component.text("Did you get good grades?", NamedTextColor.DARK_AQUA, TextDecoration.BOLD))
                    .yesButton(YesButton.create().addAction(MessageAction.create(Component.text("Great!", NamedTextColor.GREEN))))
                    .noButton(NoButton.create().addAction(onHasSchoolBadGrades));

            YesNoQuestion yesNoQuestion = YesNoQuestion.create()
                    .question(Component.text("Did you ever finish a school?", NamedTextColor.DARK_AQUA, TextDecoration.BOLD))
                    .noButton(NoButton.create().addAction(MessageAction.create("Oh no, thats sad")))
                    .yesButton(YesButton.create().addAction(QuestionAction.create(onHasSchool)));



            yesNoQuestion.show((Player) sender);

        }else if (args[0].equals("input")) {

            sendTextInputQuestion((Player) sender);
            Bukkit.broadcast(Component.text("End of dialog"));
        } else if (args[0].equals("move")) {

            for (Entity entity : ((Player) sender).getWorld().getEntities()) {
                if (entity.getScoreboardTags().contains("guinnypig")) {
                    pig = entity;
                }
            }

            if (pig == null) {
                Entity newPig = ((Player) sender).getWorld().spawnEntity(location, EntityType.PIG);
                newPig.addScoreboardTag("guinnypig");
                ((LivingEntity) newPig).setAI(false);
                pig = newPig;
            }

            sendMovementQuestion((Player) sender);
        }

        return true;
    }



    private void sendMovementQuestion(Player player) {
        if (pig.isDead() || pig == null) {
            Entity newPig = player.getWorld().spawnEntity(location, EntityType.PIG);
            newPig.addScoreboardTag("guinnypig");
            ((LivingEntity) newPig).setAI(false);
            pig = newPig;
        }
        pig.teleport(location);
        pig.setRotation(yaw, pitch);

        Clamp<Float> angleClamp = Clamp.create(Float.class, -90f, 90f);
        Clamp<Float> locationClamp = Clamp.create(Float.class, null, 100f);

        MovementQuestion movementQuestion = MovementQuestion
                .create()
                .onReload(this::sendMovementQuestion) //short hand lambda expression
                .setLocation(location)
                .setRotation(pitch, yaw)
                .addPlusXButtonAction(NumberAction.create(Float.class).number((float) location.getX()).operatorNumber(1f).operation(f -> location.setX(f)))
                .addMinusXButtonAction(NumberAction.create(Float.class).number((float) location.getX()).operatorNumber(-1f).operation(f -> location.setX(f)))
                .addPlusYButtonAction(NumberAction.create(Float.class).clamp(locationClamp.copy()).number((float) location.getY()).operatorNumber(1f).operation(f -> location.setY(f)))
                .addMinusYButtonAction(NumberAction.create(Float.class).number((float) location.getY()).operatorNumber(-1f).operation(f -> location.setY(f)))
                .addPlusZButtonAction(NumberAction.create(Float.class).number((float) location.getZ()).operatorNumber(1f).operation(f -> location.setZ(f)))
                .addMinusZButtonAction(NumberAction.create(Float.class).number((float) location.getZ()).operatorNumber(-1f).operation(f -> location.setZ(f)))
                .addPlusYawButtonAction(NumberAction.create(Float.class).number(yaw).operatorNumber(22.5f).operation(f -> yaw = f))
                .addMinusYawButtonAction(NumberAction.create(Float.class).number(yaw).operatorNumber(-22.5f).operation(f -> yaw = f))
                .addPlusPitchButtonAction(NumberAction.create(Float.class).clamp(angleClamp.copy()).number(pitch).operatorNumber(22.5f).operation(f -> pitch = f))
                .addMinusPitchButtonAction(NumberAction.create(Float.class).clamp(angleClamp.copy()).number(pitch).operatorNumber(-22.5f).operation(f -> pitch = f))
                .setSetXButtonAction(p -> f -> NumberAction.create(Float.class).number((float)location.getX()).actionType(NumberActionOperations.SET).operation(f1 -> location.setX(f1)).operatorNumber(f).run(p))
                .setSetYButtonAction(p -> f -> NumberAction.create(Float.class).clamp(locationClamp.copy()).number((float)location.getY()).actionType(NumberActionOperations.SET).operation(f1 -> location.setY(f1)).operatorNumber(f).run(p))
                .setSetZButtonAction(p -> f -> NumberAction.create(Float.class).number((float) location.getZ()).actionType(NumberActionOperations.SET).operation(f1 -> location.setZ(f1)).operatorNumber(f).run(p))
                .setSetYawButtonAction(p -> f -> NumberAction.create(Float.class).number(yaw).operatorNumber(f).actionType(NumberActionOperations.SET).operation(f1 -> yaw = f1).operatorNumber(f).run(p))
                .setSetPitchButtonAction(p -> f -> NumberAction.create(Float.class).clamp(angleClamp.copy()).number(pitch).actionType(NumberActionOperations.SET).operation(f1 -> pitch = f1).operatorNumber(f).run(p))
                .enableRoll(false)
                .addPlusRollButtonAction(CommandAction.create("say nö"))
                .pitchClamp(angleClamp)
                .yClamp(locationClamp);
        movementQuestion.show(player, "movement");


    }

    private void sendTextInputQuestion(Player player) {
        Component questionComponent = Component.text("What is your name?");
        if (name != "[UNKNOWN]") {
            questionComponent = questionComponent.appendNewline().append(Component.text("Your name is: " + name));
        }
        GenericQuestion question = GenericQuestion.create()
                .question(questionComponent);

        Button exitButton = Button.create().addAction(MessageAction.create("Bye!")).text(Component.text("[Exit]", NamedTextColor.RED));

        StringInputButton stringInputButton = StringInputButton.create(p -> message -> {
                    name = message;
                    sendTextInputQuestion(player);
                })
                .addPreAction(MessageAction.create(Component.text("Please enter your name!", NamedTextColor.GRAY, TextDecoration.ITALIC)))
                .addPostAction(MessageAction.create(Component.text("Got your message", NamedTextColor.GRAY, TextDecoration.ITALIC)));



        question.setButtons(List.of(stringInputButton, exitButton));

        question.show(player);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of("yes_no", "input", "move");
    }
}
