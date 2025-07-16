package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion;

import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.ColorPalets.ColorPalette;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.Button;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditValues.ConfigEditListSection;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractConfigEditSection extends ConfigEditValue<AbstractConfigEditSection> {
    private Consumer<String> pathAction;
    private Button showButton = Button.create().addAction(CustomAction.create(p -> {
        if (pathAction != null) {
            Bukkit.broadcast(Component.text("[ShowButton] showing sub section from path: " + getCompletePath() + "\n  pathName: " + pathName()));
            pathAction.accept(getCompletePath());
        }else {
            Bukkit.broadcast(Component.text("[ShowButton] pathAction is null"));
        }
    }));
    private Component actionBarLine = Component.empty();
    private int maxLines = 14;

    @Contract(value = "_ -> this", mutates = "this")
    public AbstractConfigEditSection actionBarLine(Component line) {
        this.actionBarLine = line;
        return this;
    }
    public Component actionBarLine() {
        return actionBarLine;
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
    @Contract(mutates = "this", value = "_ -> this")
    public AbstractConfigEditSection maxLines(int maxLines) {
        this.maxLines = maxLines;
        return this;
    }
    public int maxLines() {
        return maxLines;
    }




    public abstract AbstractConfigEditSection disableActionRecursive(CustomAction action);
    public abstract AbstractConfigEditSection reloadActionRecursive(Function<Player, Consumer<Boolean>> action);
    public abstract AbstractConfigEditSection getValueRecursive(Function<String, Object> action);
    public abstract AbstractConfigEditSection setValueRecursive(BiConsumer<String, Object> action);
    public abstract AbstractConfigEditSection getPlayerSettingsActionRecursive(Function<String, Object> action);
    public abstract AbstractConfigEditSection setPlayerSettingsActionRecursive(BiConsumer<String, Object> action);
    public abstract AbstractConfigEditSection setMaxLinesRecursive(int maxLines);
    public abstract AbstractConfigEditSection colorPaletteRecursive(ColorPalette palette);
    public abstract AbstractConfigEditSection pathActionRecursive(Consumer<String> action);

    public abstract @Nullable AbstractConfigEditSection getSubSectionFromPath(String path);
    public abstract AbstractConfigEditSection getSubSection(String path);
    public abstract List<Component> getContent(Player player);

    protected AbstractConfigEditSection copyBaseFieldsInto(ConfigEditSection target) {
        return super.copyBaseFieldsInto(target)
                .actionBarLine(actionBarLine())
                .disableAction(disableAction())
                .showButton(showButton());
    }
}
