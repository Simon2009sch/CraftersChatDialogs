package me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.buttons;

import me.simoncrafter.CraftersCommandUtils.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersCommandUtils.dialogs.def.IAction;
import me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions.CustomAction;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ToggleButton extends AbstractButton<ToggleButton> {

    private final static Component TEXT = Component.text().resetStyle().build()
            .append(Component.text("["))
            .append(Component.text("Toggle"))
            .append(Component.text("]"));

    private boolean enabled = false;
    private List<IAction> enableActions = new ArrayList<>();
    private List<IAction> disableActions = new ArrayList<>();

    private ToggleButton(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isDisabled() {
        return enabled;
    }
   @Contract("_ -> this")
    public ToggleButton enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
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

    public boolean toggle(Player player) {
        this.enabled = !this.enabled;
        if (enabled) {
            runOnEnableActions(player);
        } else {
            runOnDisableActions(player);
        }
        return !this.enabled;
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
        addAction(CustomAction.create(p -> toggle(p)));
        return super.compile(uses);
    }
}
