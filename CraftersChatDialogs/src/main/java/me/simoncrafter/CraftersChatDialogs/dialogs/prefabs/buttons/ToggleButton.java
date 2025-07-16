package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ToggleButton extends AbstractButton<ToggleButton> {

    private final static Component TEXT = Component.text().resetStyle().build()
            .append(Component.text("["))
            .append(Component.text("Toggle"))
            .append(Component.text("]"));

    private boolean state = false;
    private List<IAction> enableActions = new ArrayList<>();
    private List<IAction> disableActions = new ArrayList<>();
    private Component enabledText =  Component.text("[Enabled] ");
    private Component disabledText = Component.text("[Disabled]");
    private Consumer<Boolean> toggleCallback = b -> {};

    private ToggleButton(boolean state) {
        this.state = state;
    }

    @Contract("-> new")
    public static @NotNull ToggleButton create() {
        return create(false);
    }
    @Contract("_ -> new")
    public static @NotNull ToggleButton create(boolean enabled) {
        ToggleButton button = new ToggleButton(enabled);
        button.text(TEXT);
        return button;
    }



   @Contract("_ -> this")
    public ToggleButton state(boolean enabled) {
        this.state = enabled;
        return this;
    }
    public boolean state() {
        return state;
    }
    @Contract("_ -> this")
    public ToggleButton addEnableAction(IAction action) {
        enableActions.add(action);
        return this;
    }
    @Contract("_ -> this")
    public ToggleButton addDisableAction(IAction action) {
        disableActions.add(action);
        return this;
    }
    @Contract("_ -> this")
    public ToggleButton removeEnableAction(IAction action) {
        enableActions.remove(action);
        return this;
    }
    @Contract("_ -> this")
    public ToggleButton removeDisableAction(IAction action) {
        disableActions.remove(action);
        return this;
    }
    @Contract("_ -> this")
    public ToggleButton disableActions(List<IAction> action) {
        this.disableActions = action;
        return this;
    }
    @Contract("_ -> this")
    public ToggleButton enableActions(List<IAction> action) {
        this.enableActions = action;
        return this;
    }
    public List<IAction> enableActions() {
        return enableActions;
    }
    public List<IAction> disableActions() {
        return disableActions;
    }

    @Contract(value = "_->this", mutates = "this")
    public ToggleButton enabledText(Component text) {
        this.enabledText = text;
        return this;
    }

    @Contract(value = "_->this", mutates = "this")
    public ToggleButton disabledText(Component text) {
        this.disabledText = text;
        return this;
    }

    @Contract(value = "_->this", mutates = "this")
    public ToggleButton enabledTextColor(TextColor color) {
        this.enabledText = enabledText.color(color);
        return this;
    }

    @Contract(value = "_->this", mutates = "this")
    public ToggleButton disabledTextColor(TextColor color) {
        this.disabledText = disabledText.color(color);
        return this;
    }
    @Contract(value = "_->this", mutates = "this")
    public ToggleButton toggleCallback(Consumer<Boolean> callback) {
        this.toggleCallback = callback;
        return this;
    }

    public Consumer<Boolean> toggleCallback() {
        return this.toggleCallback;
    }

    public Component enabledText() {
        return this.enabledText;
    }

    public Component disabledText() {
        return this.disabledText;
    }

    public TextColor enabledTextColor() {
        return enabledText.color();
    }

    public TextColor disabledTextColor() {
        return disabledText.color();
    }




    public boolean toggle(Player player) {
        this.state = !this.state;
        if (state) {
            runOnEnableActions(player);
        } else {
            runOnDisableActions(player);
        }
        toggleCallback.accept(this.state);
        return this.state;
    }

    private void runOnDisableActions(Player player) {
        for (IAction action : disableActions) {
            action.run(player);
        }
    }
    private void runOnEnableActions(Player player) {
        for (IAction action : enableActions) {
            action.run(player);
        }
    }

    @Override
    public Component compile(int uses) {
        text(state ? enabledText : disabledText);
        addAction(CustomAction.create(p -> toggle(p)));
        return super.compile(uses);
    }

    @Override
    public ToggleButton clone() {
        ToggleButton clone = new ToggleButton(state);
        clone.text(text());
        clone.setDisabled(isDisabled());
        clone.setActions(actions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setPostActions(postActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.hoverText(hoverText());
        clone.reloadOnUse(reloadOnUse());
        clone.reloadAction(reloadAction());
        clone.enabledText(enabledText());
        clone.disabledText(disabledText());
        clone.enableActions(enableActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.disableActions(disableActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.toggleCallback(toggleCallback());
        return clone;
    }
}
