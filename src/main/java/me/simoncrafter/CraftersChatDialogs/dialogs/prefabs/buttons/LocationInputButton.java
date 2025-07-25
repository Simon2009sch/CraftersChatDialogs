package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.LocationInputAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LocationInputButton extends AbstractButton<LocationInputButton> {

    private final static Component TEXT = Component.text().resetStyle().build()
            .append(Component.text("[", NamedTextColor.BLUE))
            .append(Component.text("Set Location", NamedTextColor.BLUE))
            .append(Component.text("]", NamedTextColor.BLUE));

    private LocationInputAction locationInputAction = LocationInputAction.create();
    private final CustomAction customAction = CustomAction.create(p -> {
        locationInputAction.run(p);
    });

    private LocationInputButton() {
    }

    @Contract(value = "_ -> new")
    public static @NotNull LocationInputButton create(Function<Player, Consumer<Location>> onResponse) {
        LocationInputButton button = new LocationInputButton();
        button.locationInputAction.onResponse(onResponse);
        button.text(TEXT);
        return button;
    }
    @Contract(value = "_ -> new")
    public static @NotNull LocationInputButton create(LocationInputAction locationInputAction) {
        LocationInputButton button = new LocationInputButton();
        button.locationInputAction = locationInputAction;
        button.text(TEXT);
        return button;
    }
    @Contract(value = "-> new")
    public static @NotNull LocationInputButton create() {
        LocationInputButton button = new LocationInputButton();
        button.text(TEXT);
        return button;
    }


    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull LocationInputButton addLocationPreAction(@NotNull IAction action) {
        this.locationInputAction.addPreAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull LocationInputButton addLocationPostAction(@NotNull IAction action) {
        this.locationInputAction.addPostAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull LocationInputButton removeLocationPreAction(@NotNull IAction action) {
        this.locationInputAction.removePreAction(action);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull LocationInputButton removeLocationPostAction(@NotNull IAction action) {
        this.locationInputAction.removePostAction(action);
        return this;
    }

    @Contract(pure = true)
    public @NotNull LocationInputAction locationInputAction() {
        return locationInputAction;
    }

    @Contract(value = "_ -> this")
    public @NotNull LocationInputAction locationInputAction(@NotNull LocationInputAction action) {
        this.locationInputAction = action;
        return action;
    }

    @Override
    public Component compile(int uses) {
        addAction(locationInputAction);
        return super.compile(uses);
    }

    @Override
    public LocationInputButton clone() {
        LocationInputButton clone = new LocationInputButton();
        clone.text(text());
        clone.setDisabled(isDisabled());
        clone.setActions(actions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setPostActions(postActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.hoverText(hoverText());
        clone.reloadOnUse(reloadOnUse());
        clone.reloadAction(reloadAction());
        clone.locationInputAction(locationInputAction().clone());
        return clone;
    }
}
