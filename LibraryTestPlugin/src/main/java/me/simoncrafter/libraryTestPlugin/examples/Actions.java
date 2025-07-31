package me.simoncrafter.libraryTestPlugin.examples;

import me.simoncrafter.CraftersChatDialogs.Clamp;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.*;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.IntInputAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.LocationInputAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.StringInputAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.StringWithRulesInputAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.GenericQuestion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class Actions {


    ClearChatAction clearChatAction = ClearChatAction.create();


    CustomAction customAction = CustomAction.create(p -> {
        // some java code
        p.sendMessage("Counting:");
        for (int i = 1; i < 10; i++) {
            p.sendMessage("Number: " + 1);
        };
    });


    EmptyAction emptyAction = EmptyAction.create();


    MessageAction messageAction = MessageAction.create(Component.text("Message", NamedTextColor.RED));
    MessageAction messageAction2 = MessageAction.create().message(Component.text("Other Message", NamedTextColor.GREEN));


    int number = 1; // outside variable
    NumberAction<Integer> numberAction = NumberAction.create(Integer.class)
            .actionType(NumberActionOperations.DIVIDE)
            .clamp(Clamp.create(Integer.class)
                    .lower(true, -10)
                    .upper(true, 10)
            ).number(number)
            .operatorNumber(1)
            .operation(newNumber -> number = newNumber);


    GenericQuestion question = GenericQuestion.create(); // placeholder question
    QuestionAction questionAction = QuestionAction.create(question);

    SoundAction soundAction = SoundAction.create(Sound.ENTITY_ARROW_HIT_PLAYER, 1, 2, SoundCategory.MASTER);

    CommandAction commandAction = CommandAction.create("/tp @s ~ ~1 ~");
    CommandAction commandAction1 = CommandAction.create()
            .command("/tp @s ~1 ~ ~")
            .asOP(true);
    CommandAction consoleCommandAction = CommandAction.create("/say Hi, I am the server!")
            .asConsole(true);



    // Input actions
    StringInputAction stringInputAction = StringInputAction.create()
            .prompt(Component.text("Please enter some text!"))
            .onResponse(player -> message -> player.sendMessage("You sent: " + message))
            .addCancelAction(MessageAction.create("Cancelled"));


    String ruleRegex = "(Cat|Dog|Hamster|Fish)";
    StringWithRulesInputAction stringWithRulesInputAction = StringWithRulesInputAction.create()
            .prompt(Component.text("Please enter some text!"))
            .onResponse(player -> message -> player.sendMessage("You sent: " + message))
            .reTry(true)
            .addReTryAction(CustomAction.create(player -> reTryStringWithRulesAction(player)))
            .addSuccessAction(MessageAction.create("Success!"))
            .regexRule(ruleRegex);

    void reTryStringWithRulesAction(Player player) {
        stringWithRulesInputAction.run(player);
    }


    IntInputAction intInputAction = IntInputAction.create()
            .prompt(Component.text("Please enter a number!"))
            .onResponse(player -> number -> player.sendMessage("Your number is: " + number))
            .addCancelAction(MessageAction.create("Cancelled"))
            .reTry(true)
            .addReTryAction(CustomAction.create(p -> reTryIntInputAction(p)))
            .addSuccessAction(MessageAction.create("Success!"));

    void reTryIntInputAction(Player player) {
        intInputAction.run(player);
    }


    LocationInputAction locationInputAction = LocationInputAction.create()
            .prompt(Component.text("Please enter a location!"))
            .onResponse(player -> loc -> player.sendMessage(
                    Component.text("You sent: ")
                            .append(Component.text(loc.getWorld() + " ", NamedTextColor.YELLOW))
                            .append(Component.text(loc.x() + " ", NamedTextColor.RED))
                            .append(Component.text(loc.y() + " ", NamedTextColor.GREEN))
                            .append(Component.text(loc.z() + " ", NamedTextColor.BLUE))
                            .append(Component.text(loc.getPitch() + " ", NamedTextColor.RED))
                            .append(Component.text(loc.getYaw(), NamedTextColor.GREEN))))
            .addCancelAction(MessageAction.create("Cancelled"))
            .addSuccessAction(MessageAction.create("Success!"))
            .reTry(true)
            .addReTryAction(CustomAction.create(p -> reTryLocationInputAction(p)));

    void reTryLocationInputAction(Player player) {
        locationInputAction.run(player);
    }
}
