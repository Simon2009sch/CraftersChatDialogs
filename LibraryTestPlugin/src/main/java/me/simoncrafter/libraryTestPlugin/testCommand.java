package me.simoncrafter.libraryTestPlugin;

import me.simoncrafter.CraftersChatDialogs.Clamp;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions.DisplayOption;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions.DisplayOptions;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.*;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.LocationInputAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.*;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.AbstractConfigEditSection;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditData;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditPlayerData;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditValues.ConfigEditGenericValue;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditValues.ConfigEditListSection;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.GenericQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.MovementQuestion;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.YesNoQuestion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class testCommand implements CommandExecutor, TabExecutor {

    String name = "[UNKNOWN]";
    Location location = new Location(Bukkit.getWorld("world"), 34, 77, -323);
    float yaw = 0;
    float pitch = 0;
    Entity pig = null;
    Map<Player, Map<String, Object>> stepValues = new HashMap<>();



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
        } else if (args[0].equals("toggle")) {
            sendTestToggleQuestion((Player) sender);
        } else if (args[0].equals("config")) {
            sendEditQuestion((Player) sender);
        } else if (args[0].equals("RELOAD_TEST_CONFIG")) {
            Main.reload();
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
        Clamp<Double> locationClamp = Clamp.create(Double.class, null, 100d);

        final String movementStepString = "movement";
        final String rotationStepString = "rotation";

        //enshure player is in the map
        stepValues.computeIfAbsent(player, k -> new HashMap<>());

        double movementStep = (double) stepValues.get(player).getOrDefault(movementStepString, 1d);
        float rotationStep = (float) stepValues.get(player).getOrDefault(rotationStepString, 22.5f);


        MovementQuestion movementQuestion = MovementQuestion
                .create()
                .onReload(this::sendMovementQuestion) //short hand lambda expression
                .setLocation(location)
                .setRotation(pitch, yaw)
                .addPlusXButtonAction(NumberAction.create(Double.class).number(location.getX()).operatorNumber(movementStep).operation(in -> location.setX(in)))
                .addMinusXButtonAction(NumberAction.create(Double.class).number(location.getX()).operatorNumber(-movementStep).operation(in -> location.setX(in)))
                .addPlusYButtonAction(NumberAction.create(Double.class).clampButClone(locationClamp.clone()).number(location.getY()).operatorNumber(movementStep).operation(in -> location.setY(in)))
                .addMinusYButtonAction(NumberAction.create(Double.class).number(location.getY()).operatorNumber(-movementStep).operation(in -> location.setY(in)))
                .addPlusZButtonAction(NumberAction.create(Double.class).number(location.getZ()).operatorNumber(movementStep).operation(in -> location.setZ(in)))
                .addMinusZButtonAction(NumberAction.create(Double.class).number(location.getZ()).operatorNumber(-movementStep).operation(in -> location.setZ(in)))
                .addPlusYawButtonAction(NumberAction.create(Float.class).number(yaw).operatorNumber(rotationStep).operation(in -> yaw = in))
                .addMinusYawButtonAction(NumberAction.create(Float.class).number(yaw).operatorNumber(-rotationStep).operation(in -> yaw = in))
                .addPlusPitchButtonAction(NumberAction.create(Float.class).clampButClone(angleClamp.clone()).number(pitch).operatorNumber(rotationStep).operation(in -> pitch = in))
                .addMinusPitchButtonAction(NumberAction.create(Float.class).clampButClone(angleClamp.clone()).number(pitch).operatorNumber(-rotationStep).operation(in -> pitch = in))
                .setSetXButtonAction(p -> in -> NumberAction.create(Double.class).number(location.getX()).actionType(NumberActionOperations.SET).operation(in1 -> location.setX(in1)).operatorNumber(in).run(p))
                .setSetYButtonAction(p -> in -> NumberAction.create(Double.class).clampButClone(locationClamp.clone()).number(location.getY()).actionType(NumberActionOperations.SET).operation(in1 -> location.setY(in1)).operatorNumber(in).run(p))
                .setSetZButtonAction(p -> in -> NumberAction.create(Double.class).number(location.getZ()).actionType(NumberActionOperations.SET).operation(in1 -> location.setZ(in1)).operatorNumber(in).run(p))
                .setSetYawButtonAction(p -> in -> NumberAction.create(Float.class).number(yaw).actionType(NumberActionOperations.SET).operation(in1 -> yaw = in1).operatorNumber(in).run(p))
                .setSetPitchButtonAction(p -> in -> NumberAction.create(Float.class).clampButClone(angleClamp.clone()).number(pitch).actionType(NumberActionOperations.SET).operation(in1 -> pitch = in1).operatorNumber(in).run(p))
                .enableRoll(false)
                .addPlusRollButtonAction(CommandAction.create("say nö"))
                .pitchClamp(angleClamp)
                .yClamp(locationClamp)
                .showMovementStepButton(true)
                .showRotationStepButton(true)
                .movementStep(movementStep)
                .rotationStep(rotationStep)
                .movementStepButtonDoubleInputAction(p -> in -> NumberAction.create(Double.class).number(movementStep).actionType(NumberActionOperations.SET).operation(in1 -> stepValues.get(player).put(movementStepString, in1)).operatorNumber(in).run(p))
                .rotationStepButtonFloatInputAction(p -> in -> NumberAction.create(Float.class).number(rotationStep).actionType(NumberActionOperations.SET).operation(in1 -> stepValues.get(player).put(rotationStepString, in1)).operatorNumber(in).run(p))
                .addExitButtonAction(CustomAction.create(p -> stepValues.remove(player)))
                .displayOption(DisplayOptions.DEFAULT.colorPalette(DisplayOptions.ColorPalettes.YELLOW_ISH.override(DisplayOptions.ColorPalettes.Overrides.XYZ)));
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

    private boolean testToggleEnabled = false;
    private int selectedValue = 0;
    private void sendTestToggleQuestion(Player player) {
        ToggleButton toggleButton = ToggleButton.create(testToggleEnabled).reloadOnUse(true).toggleCallback(b -> testToggleEnabled = b).enabledText(Component.text("[Light]", NamedTextColor.WHITE)).disabledText(Component.text("[Light]", NamedTextColor.DARK_GRAY));
        SelectionButton selectionButton = SelectionButton.create()
                .values(SelectionButton.stringListToComponentList(List.of("Simon", "Julian", "Alex", "Flo", "Jakob", "Maxi", "Karl", "Julia", "Hubert", "Pirat", "Gamer123"))).reloadOnUse(true)
                .operation(NumberAction.create(Integer.class).number(selectedValue).operatorNumber(1).operation(in -> selectedValue = in)).selected(selectedValue)
                .buttonTextStructure(SelectionButton.BUTTON_STRUCTURE_ONLY_VALUE)
                .valueStructure(SelectionButton.VALUE_STRUCTURE_ONLY_VALUE);
        GenericQuestion question = GenericQuestion.create()
                .onReload(this::sendTestToggleQuestion).addButton(toggleButton).addButton(selectionButton).addExitButton();
        question.show(player, "testToggle");
    }

    private Map<Player, ConfigEditPlayerData> configEditPlayers = new HashMap<>();
    Map<String, Object> defaultValues = Map.ofEntries(
            Map.entry("number", 1),
            Map.entry("string", "string"),
            Map.entry("bool", true),
            Map.entry("list", List.of("1", "2", Map.ofEntries(Map.entry("a", "b")))),
            Map.entry("section", Map.ofEntries(
            Map.entry("deeper", Map.ofEntries(
            Map.entry("deepest", "simon"),
            Map.entry("deepest2", "julian")
            )),
            Map.entry("something_very_cool", "Das is ja cool!"), // cannot write to this
            Map.entry("something with spaces", "moinson"))
            ),
            Map.entry("location", new Location(Bukkit.getWorld("world"), 34, 77, -323)));

    Map<String, Object> values = null;
    public boolean fileReloaded = false;
    @SuppressWarnings("unchecked")
    private void sendEditQuestion(Player player) {
        if (values == null || fileReloaded) {
            fileReloaded = false;
            values = ConfigEditData.configSectionToMap(config_manager.getInstance()
                    .getConfigurationSection("Arena"));
            values = ConfigEditData.setDeepRegexPath(values, "spawn", (path, current) -> current instanceof String ? ConfigEditData.deserializeLocation((String) current) : current);
            values = ConfigEditData.setDeepRegexPath(values, "(locations(\\..+)*\\.\\d+)?(arenas\\.\\d+\\.spawn)?(arenas\\.\\d+\\.locations\\.\\d+)?", (path, current) -> {
                if (current instanceof String) {
                    return ConfigEditData.deserializeLocation((String) current);
                }else {
                    return current;
                }
            });

        }
        DisplayOption displayOption = new DisplayOption(DisplayOptions.ColorPalettes.COOL_BLUISH.override(DisplayOptions.ColorPalettes.Overrides.CONFIG_VALUE_DEFAULT), DisplayOptions.SoundOptions.DEFAULT);

        ConfigEditData data = ConfigEditData.create(values);
        configEditPlayers.putIfAbsent(player, new ConfigEditPlayerData());

        data.setDeepRegexDisplayData("empty(\\..*)?", (path, current) -> {
            if (current instanceof AbstractConfigEditSection section) {
                section.addButtonPrefabs(ConfigEditData.DataCreationPrefabs.ALL);
                section.showAddButton(true);
                section.showRemoveButtons(true);
                if (section instanceof ConfigEditListSection listSection) {
                    listSection.showMoveButtons(true);
                }
                return section;
            }
            return current;
        });
        data.setDeepRegexDisplayData("arenas", (path, current) -> {
            if (current instanceof AbstractConfigEditSection section) {
                Map<String, Object> map = new HashMap<>();

                map.put("Arena", ConfigEditData.makeMutable(Map.ofEntries(
                        Map.entry("name","arena_name"),
                        Map.entry("playerCount", 2),
                        Map.entry("maxPlayers", 4),
                        Map.entry("spawn", new Location(Bukkit.getWorld("world"), 34, 77, -323)),
                        Map.entry("locations", List.of("20 10 20", new Location(Bukkit.getWorld("world"), 34, 77, -323))),
                        Map.entry("deathMatch", false),
                        Map.entry("teams", Map.ofEntries(
                                Map.entry("red", Map.ofEntries(
                                        Map.entry("Name", "red"),
                                        Map.entry("Color", "red"),
                                        Map.entry("Players", 2)
                                )),
                                Map.entry("blue", Map.ofEntries(
                                        Map.entry("Name", "blue"),
                                        Map.entry("Color", "blue"),
                                        Map.entry("Players", 1)
                                ))
                        )
                ))));

                section.addButtonPrefabs(map);
                section.showAddButton(true);
                section.showRemoveButtons(true);
                if (section instanceof ConfigEditListSection listSection) {
                    listSection.showMoveButtons(true);
                }
                return section;
            }
            return current;
        });

        data.setDeepRegexDisplayData("arenas\\.\\d+\\.locations", (path, current) -> {
            if (current instanceof ConfigEditListSection section) {
                section.addButtonPrefabs(Map.ofEntries(Map.entry("Location", new Location(null, 0, 0, 0))));
                section.showAddButton(true);
                section.showRemoveButtons(true);
                section.showMoveButtons(true);
                return section;
            }
            return current;
        });

        data.setDeepRegexDisplayData("(locations(\\..+)*\\.\\d+)?", (path, current) -> {
            if (current instanceof ConfigEditGenericValue<?> value && value.getType() == Location.class) {
                value.displayConstructor(ConfigEditGenericValue.DisplayPrefabs.LOCATION_FULL);

                Component cancelText = Component.text("").append(Component.text("Type cancel to cancel.")
                        .color(displayOption.colorPalette().HINT())
                        .clickEvent(ClickEvent.suggestCommand("cancel"))
                        .hoverEvent(HoverEvent.showText(Component.text("Click to paste \"cancel\" in chat [Click to paste]")))
                        .appendNewline()
                        .append(Component.text(" Put a \\ before it to input \"cancel\"")
                                .clickEvent(ClickEvent.suggestCommand("\\cancel"))
                                .hoverEvent(HoverEvent.showText(Component.text("Click to paste \"\\cancel\" in chat [Click to paste]")))));
                CustomAction reloadAction = CustomAction.create(p -> value.reloadAction().apply(p).accept(false));

                String currentValue = "";
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat format = new DecimalFormat("0.###", symbols);

                currentValue += ((Location) value.value()).getWorld() == null ? "" : ((Location) value.value()).getWorld()
                        .getName() + " ";
                currentValue += format.format(((Location) value.value()).getX()) + " ";
                currentValue += format.format(((Location) value.value()).getY()) + " ";
                currentValue += format.format(((Location) value.value()).getZ());


                String currentValueRotation = currentValue;
                currentValueRotation += " " + format.format(((Location) value.value()).getPitch()) + " ";
                currentValueRotation += format.format(((Location) value.value()).getYaw());


                String playerLocationString = player.getWorld().getName() + " "
                        + format.format(player.getLocation().getX()) + " "
                        + format.format(player.getLocation().getY()) + " "
                        + format.format(player.getLocation().getZ());

                String playerLocationRotationString = " " + format.format(player.getLocation().getPitch()) + " "
                        + format.format(player.getLocation().getYaw());

                LocationInputButton editButton = LocationInputButton.create(LocationInputAction.create(p -> v -> value.setValueAction().accept(value.getCompletePath(), v))
                        .prompt(Component.text("")
                                .append(Component.text("Please enter a location with the following format in chat!", displayOption.colorPalette().PRIMARY(), TextDecoration.BOLD))
                                .appendNewline()
                                .append(Component.text("Formata: ", displayOption.colorPalette().SECONDARY()))
                                .append(Component.text("[world] ", displayOption.colorPalette().get("LOCATION_WORLD")))
                                .append(Component.text("[x] ", displayOption.colorPalette().get("LOCATION_X")))
                                .append(Component.text("[y] ", displayOption.colorPalette().get("LOCATION_Y")))
                                .append(Component.text("[z] ", displayOption.colorPalette().get("LOCATION_Z")))
                                .append(Component.text("[pitch] ", displayOption.colorPalette().get("LOCATION_PITCH")))
                                .append(Component.text("[yaw]", displayOption.colorPalette().get("LOCATION_YAW")))
                                .appendNewline()
                                .append(Component.text("Current location is: ", displayOption.colorPalette().SECONDARY()))
                                .append(value.displayConstructor().apply(value.value(), displayOption.colorPalette()).decorate(TextDecoration.BOLD))
                                .append(Component.text(" [Click to paste]", displayOption.colorPalette().SECONDARY_DARK(), TextDecoration.BOLD)
                                        .clickEvent(ClickEvent.suggestCommand(currentValue)))
                                .appendNewline()
                                .append(Component.text("Paste Options: ", displayOption.colorPalette().SECONDARY()))
                                .appendNewline()
                                .append(Component.text("  [Paste Player Location]", displayOption.colorPalette().SECONDARY_DARK(), TextDecoration.BOLD)
                                        .clickEvent(ClickEvent.suggestCommand(playerLocationString)))
                                .appendNewline()
                                .append(Component.text("  [Paste Player Location with Rotation]", displayOption.colorPalette().SECONDARY_DARK(), TextDecoration.BOLD)
                                        .clickEvent(ClickEvent.suggestCommand(playerLocationString + playerLocationRotationString)))
                                .appendNewline()
                                .append(cancelText))
                        .addSuccessAction(reloadAction)
                        .addCancelAction(reloadAction)
                        .reTry(true)
                        .displayOption(displayOption)
                        .putWorld(true)
                        .putRotation(true))
                        .text(value.displayConstructor().apply(value.value(), displayOption.colorPalette()));
                value.editButton(editButton);
                return value;
            }
            return current;
        });

        ConfigEditQuestion question = ConfigEditQuestion.create(configEditPlayers.get(player), data)
                .onReload(this::sendEditQuestion)
                //.displayOption(displayOption)
                .showSaveChangesButton(true)
                .saveChangesAction(this::saveConfigCallback);

        question.setRootSection(data.getRootSection());

        question.show(player, "configEdit");
        Button reloadButton = Button.create()
                .addAction(CustomAction.create(player1 -> Main.reload()))
                .reloadAction(p -> b -> sendEditQuestion(p))
                .reloadOnUse(true)
                .text(Component.text("[Reload]", NamedTextColor.RED, TextDecoration.BOLD));
        player.sendMessage(reloadButton.compile());
        saveConfigCallback();
    }
    private void saveConfigCallback() {
        config_manager.getInstance().set("Arena", ConfigEditData.applyMapToSection(
                values,
                config_manager.getInstance().getConfigurationSection("Arena"),
                Map.ofEntries(
                        Map.entry(Location.class, ConfigEditData.Serializers.LOCATION_FULL)
                )));
        config_manager.getInstance().save();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of("yes_no", "input", "move", "toggle", "config", "RELOAD_TEST_CONFIG ");
    }
}
