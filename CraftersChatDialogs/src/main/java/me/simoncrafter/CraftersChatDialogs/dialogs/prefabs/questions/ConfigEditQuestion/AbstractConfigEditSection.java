package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions.DisplayOption;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.Button;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractConfigEditSection extends ConfigEditValue<AbstractConfigEditSection> {
    private Consumer<String> pathAction;
    private Button showButton = Button.create().addAction(displayOption().soundOption().CLICK().toSoundAction()).addAction(CustomAction.create(p -> pathAction.accept(getCompletePath())));
    private Consumer<AbstractButton<?>> addOptionButtonAction = button -> {};

    private boolean showRemoveButtons = false;
    private boolean showAddButton = false;
    private Map<String, Object> addButtonPrefabs = new HashMap<>();


    @Contract(mutates = "this", value = "-> this")
    public AbstractConfigEditSection showRemoveButtons(boolean showRemoveButtons) {
        this.showRemoveButtons = showRemoveButtons;
        return this;
    }
    public boolean showRemoveButtons() {
        return showRemoveButtons;
    }

    @Contract(mutates = "this", value = "-> this")
    public AbstractConfigEditSection showAddButton(boolean showAddButton) {
        this.showAddButton = showAddButton;
        return this;
    }
    public boolean showAddButton() {
        return showAddButton;
    }

    @Contract(mutates = "this", value = "_ -> this")
    public AbstractConfigEditSection addButtonPrefabs(Map<String, Object> prefabs) {
        this.addButtonPrefabs = prefabs;
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public AbstractConfigEditSection addAddButtonPrefab(String key, Object prefab) {
        this.addButtonPrefabs.put(key, prefab);
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public AbstractConfigEditSection removeAddButtonPrefab(String key) {
        this.addButtonPrefabs.remove(key);
        return this;
    }
    public Map<String, Object> addButtonPrefabs() {
        return new HashMap<>(addButtonPrefabs);
    }

    @Contract(value = "_ -> this", mutates = "this")
    public AbstractConfigEditSection addOptionButtonAction(Consumer<AbstractButton<?>> action) {
        this.addOptionButtonAction = action;
        return this;
    }
    public Consumer<AbstractButton<?>> addOptionButtonAction() {
        return addOptionButtonAction;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public AbstractConfigEditSection showButton(Button button) {
        this.showButton = button;
        return this;
    }
    public Button showButton() {
        return showButton;
    }
    @Contract(mutates = "this", value = "_ -> this")
    public AbstractConfigEditSection pathAction(Consumer<String> pathAction) {
        this.pathAction = pathAction;
        return this;
    }
    public Consumer<String> pathAction() {
        return pathAction;
    }




    public abstract AbstractConfigEditSection disableActionRecursive(CustomAction action);
    public abstract AbstractConfigEditSection reloadActionRecursive(Function<Player, Consumer<Boolean>> action);
    public abstract AbstractConfigEditSection getValueRecursive(Function<String, Object> action);
    public abstract AbstractConfigEditSection setValueRecursive(BiConsumer<String, Object> action);
    public abstract AbstractConfigEditSection getPlayerSettingsActionRecursive(Function<String, Object> action);
    public abstract AbstractConfigEditSection setPlayerSettingsActionRecursive(BiConsumer<String, Object> action);
    public abstract AbstractConfigEditSection displayOptionRecursive(DisplayOption displayOption);
    public abstract AbstractConfigEditSection pathActionRecursive(Consumer<String> action);
    public abstract AbstractConfigEditSection addOptionButtonActionRecursive(Consumer<AbstractButton<?>> action);
    public abstract AbstractConfigEditSection syncKeyRecursive(String key);
    public abstract AbstractConfigEditSection permissionPrefixRecursive(Supplier<String> permissionPrefix);

    public abstract @Nullable AbstractConfigEditSection getSubSectionFromPath(String path);
    public abstract AbstractConfigEditSection getSubSection(String path);
    public abstract List<Component> getContent(Player player);
    public abstract int getLines();




    protected AbstractConfigEditSection copyBaseFieldsInto(ConfigEditSection target) {
        return super.copyBaseFieldsInto(target)
                .addOptionButtonAction(addOptionButtonAction())
                .disableAction(disableAction())
                .showButton(showButton());
    }
}
