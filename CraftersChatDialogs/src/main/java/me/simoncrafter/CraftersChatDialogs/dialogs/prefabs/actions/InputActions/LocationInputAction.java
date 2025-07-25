package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LocationInputAction extends RetryableInputAction<LocationInputAction> {

    private Function<Player, Consumer<Location>> onResponse = p -> response -> {};
    private boolean putWorld = true;
    private boolean putRotation = true;

    public LocationInputAction() {
    }

    public LocationInputAction(Function<Player, Consumer<Location>> onResponse) {
        this.onResponse = onResponse;
    }

    public static LocationInputAction create(Function<Player, Consumer<Location>> onResponse) {
        return new LocationInputAction(onResponse).init();
    }
    public static LocationInputAction create() {
        return new LocationInputAction().init();
    }

    private LocationInputAction init() {
        prompt(
                Component.text("Please enter a location with the following format in chat!", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD)
                        .appendNewline()
                        .append(Component.text("Format: ", displayOption().colorPalette().PRIMARY()))
                        .append(Component.text("[world] ", displayOption().colorPalette().YELLOW()))
                        .append(Component.text("[x] ", displayOption().colorPalette().RED()))
                        .append(Component.text("[y] ", displayOption().colorPalette().GREEN()))
                        .append(Component.text("[z] ", displayOption().colorPalette().BLUE()))
                        .append(Component.text("[pitch] ", displayOption().colorPalette().GREEN()))
                        .append(Component.text("[yaw]", displayOption().colorPalette().BLUE())
                        .appendNewline()
                        .append(Component.text("")
                                .append(Component.text("Type cancel to cancel.")
                                        .color(displayOption().colorPalette().HINT())
                                        .clickEvent(ClickEvent.suggestCommand("cancel"))
                                        .hoverEvent(HoverEvent.showText(Component.text("Click to paste \"cancel\" in chat")))
                                        .appendNewline()
                                        .append(Component.text(" Put a \\ before it to input \"cancel\"")
                                                .clickEvent(ClickEvent.suggestCommand("\\cancel"))
                                                .hoverEvent(HoverEvent.showText(Component.text("Click to paste \"\\cancel\" in chat"))))))
        ));
        return this;
    }

    public LocationInputAction onResponse(Function<Player, Consumer<Location>> onResponse) {
        this.onResponse = onResponse;
        return this;
    }
    public Function<Player, Consumer<Location>> onResponse(IAction action) {
        return onResponse;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public LocationInputAction putWorld(boolean putWorld) {
        this.putWorld = putWorld;
        return this;
    }
    public boolean putWorld() {
        return putWorld;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public LocationInputAction putRotation(boolean putRotation) {
        this.putRotation = putRotation;
        return this;
    }
    public boolean putRotation() {
        return putRotation;
    }

    @Override
    protected void onResponse(Player player, String message) {
        Location out = new Location(null, 0, 0, 0, 0 ,0);

        final String regex = "^(?:(?<world>[^\\s]+)\\s+)?(?<x>-?\\d*\\.?\\d+)\\s+(?<y>-?\\d*\\.?\\d+)\\s+(?<z>-?\\d*\\.?\\d+)(?:(\\s+(?<pitch>-?\\d*\\.?\\d+)\\s+(?<yaw>-?\\d*\\.?\\d+))?)?\\s*$";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            if (matcher.group("x") == null || matcher.group("y") == null || matcher.group("z") == null) {
                reTryMessage(Component.text("Invalid location format", displayOption().colorPalette().ERROR(), TextDecoration.BOLD));
                handleRetry(player);
                return;
            }

            if (matcher.group("world") != null && putWorld) {
                World world = Bukkit.getWorld(matcher.group("world"));
                if (world == null) {
                    List<World> worlds = Bukkit.getWorlds();
                    Component worldList = Component.text("There are the following worlds: ", NamedTextColor.GOLD, TextDecoration.BOLD);
                    for (World w : worlds) {
                        worldList = Component.text("→ ", NamedTextColor.WHITE, TextDecoration.BOLD)
                                .append(worldList)
                                .append(Component.text(w.getName(), NamedTextColor.GOLD)
                                        .decoration(TextDecoration.BOLD, false))
                                .appendNewline();
                    }
                    reTryMessage(Component.text("Please specify a valid world", NamedTextColor.RED, TextDecoration.BOLD)
                            .hoverEvent(HoverEvent.showText(worldList))
                            .append(Component.text(" (Hover to see a list of worlds)", NamedTextColor.GRAY)
                                    .decoration(TextDecoration.BOLD, false)));
                    handleRetry(player);
                    return;
                }
                out.setWorld(world);
            }
            double x = Double.parseDouble(matcher.group("x"));
            double y = Double.parseDouble(matcher.group("y"));
            double z = Double.parseDouble(matcher.group("z"));
            out.setX(x);
            out.setY(y);
            out.setZ(z);

            if (putRotation && matcher.group("pitch") != null && matcher.group("yaw") != null) {
                float pitch = Float.parseFloat(matcher.group("pitch"));
                float yaw = Float.parseFloat(matcher.group("yaw"));
                out.setPitch(pitch);
                out.setYaw(yaw);
            }
            onResponse.apply(player).accept(out);
            handleSuccess(player);
        }else {
            reTryMessage(Component.text("Invalid location format", displayOption().colorPalette().ERROR(), TextDecoration.BOLD));
            handleRetry(player);
        }

    }

    @Override
    public LocationInputAction clone() {
        LocationInputAction clone = new LocationInputAction(onResponse);
        clone.setPreActions(preActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setPostActions(postActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setTimeoutActions(timeoutActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setCancelActions(cancelActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.messageAction(messageAction().clone());

        clone.successActions(successActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.reTryActions(reTryActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.reTry(reTry());

        clone.maxResponseTime(maxResponseTime());
        clone.syncKey(syncKey());
        clone.setDisabled(isDisabled());
        clone.prompt(prompt());
        clone.displayOption(displayOption());



        return clone;
    }

}
