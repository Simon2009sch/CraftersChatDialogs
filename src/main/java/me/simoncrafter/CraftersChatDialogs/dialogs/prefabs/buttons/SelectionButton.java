package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.NumberAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SelectionButton extends AbstractButton<SelectionButton> {

    private final static Component TEXT = Component.text().resetStyle().build()
            .append(Component.text("["))
            .append(Component.text("Button"))
            .append(Component.text("]"));

    private List<Component> values = new ArrayList<>();
    private int selected = 0;
    private TriFunction<Integer, Boolean, Component, Component> valueStructure = VALUE_STRUCTURE_NUMBER_WITH_VALUE;
    private Function<Component, Component> hoverTextStructure = c ->
            Component.empty()
                    .append(Component.text("You can choose between: ", NamedTextColor.YELLOW, TextDecoration.BOLD))
                    .append(Component.empty().style(Style.empty())) // resets the style (like §r)
                    .appendNewline()
                    .append(c);
    private TriFunction<Component, Integer, Component, Component> buttonTextStructure = BUTTON_STRUCTURE_WITH_VALUE;
    private NumberAction<Integer> operation = NumberAction.create(Integer.class);

    private SelectionButton() {
    }

    @Contract("-> new")
    public static @NotNull SelectionButton create() {
        SelectionButton button = new SelectionButton();
        button.text(TEXT);
        return button;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull SelectionButton operation(@NotNull NumberAction<Integer> operation) {
        this.operation = operation;
        return this;
    }
    public @NotNull NumberAction<Integer> operation() {
        return operation;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull SelectionButton values(@NotNull List<Component> values) {
        this.values = values;
        return this;
    }


    @Contract(pure = true)
    public @NotNull List<Component> values() {
        return values;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull SelectionButton addValue(@NotNull Component value) {
        this.values.add(value);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull SelectionButton removeValue(@NotNull Component value) {
        this.values.remove(value);
        return this;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull SelectionButton selected(int selected) {
        this.selected = selected;
        return this;
    }

    @Contract(pure = true)
    public int selected() {
        return selected;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull SelectionButton valueStructure(@NotNull TriFunction<Integer, Boolean, Component, Component> valueStructure) {
        this.valueStructure = valueStructure;
        return this;
    }

    @Contract(pure = true)
    public @NotNull TriFunction<Integer, Boolean, Component, Component> valueStructure() {
        return valueStructure;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull SelectionButton hoverTextStructure(@NotNull Function<Component, Component> hoverTextStructure) {
        this.hoverTextStructure = hoverTextStructure;
        return this;
    }

    @Contract(pure = true)
    public @NotNull Function<Component, Component> hoverTextStructure() {
        return hoverTextStructure;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public @NotNull SelectionButton buttonTextStructure(@NotNull TriFunction<Component, Integer, Component, Component> buttonTextStructure) {
        this.buttonTextStructure = buttonTextStructure;
        return this;
    }

    @Contract(pure = true)
    public @NotNull TriFunction<Component, Integer, Component, Component> buttonTextStructure() {
        return buttonTextStructure;
    }

    private Component getCompiledHoverTextValue() {
        Component out = Component.empty();
        for (int i = 0; i < values.size(); i++) {
            out = out.append(valueStructure.apply(i, i == selected, values.get(i))).appendNewline();
        }
        return out;
    }


    @Override
    public Component compile(int uses) {
        if (selected >= values.size()) {
            selected = values.size() -1;
        }
        operation.clamp(0, values.size() - 1);
        operation.looping(true);
        text(buttonTextStructure.apply(text(), selected, values.get(selected)));
        Component hoverText = hoverTextStructure.apply(getCompiledHoverTextValue());
        if (!PlainTextComponentSerializer.plainText().serialize(hoverText).isEmpty()) {
            hoverText(hoverText);
        }

        addPostAction(operation);

        return super.compile(uses);
    }


    public static List<Component> stringListToComponentList(List<String> in) {
        List<Component> out = new ArrayList<>();
        for (String s : in) {
            out.add(Component.text(s));
        }
        return out;
    }

    @Override
    public SelectionButton clone() {
        SelectionButton clone = new SelectionButton();
        clone.text(text());
        clone.setDisabled(isDisabled());
        clone.setActions(actions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.setPostActions(postActions().stream().map(IAction::clone).collect(Collectors.toList()));
        clone.hoverText(hoverText());
        clone.reloadOnUse(reloadOnUse());
        clone.reloadAction(reloadAction());
        clone.values(values());
        clone.selected(selected());
        clone.valueStructure(valueStructure());
        clone.hoverTextStructure(hoverTextStructure());
        clone.buttonTextStructure(buttonTextStructure());
        return clone;
    }

    public final static TriFunction<Component, Integer, Component, Component> BUTTON_STRUCTURE_WITH_VALUE = (c1, i, c2) -> c1.append(Component.text(": ")).append(c2.decorate(TextDecoration.UNDERLINED));
    public final static TriFunction<Component, Integer, Component, Component> BUTTON_STRUCTURE_WITHOUT_VALUE = (c1, i, c2) -> c1;
    public final static TriFunction<Component, Integer, Component, Component> BUTTON_STRUCTURE_ONLY_VALUE = (c1, i, c2) -> c2.decorate(TextDecoration.UNDERLINED);

    public final static TriFunction<Integer, Boolean, Component, Component> VALUE_STRUCTURE_NUMBER_WITH_VALUE = (i, b, c) -> b ? Component.text(i+1 + " → ").append(c).decorate(TextDecoration.BOLD) : Component.text(i+1 + " → ").append(c);
    public final static TriFunction<Integer, Boolean, Component, Component> VALUE_STRUCTURE_NUMBER_WITHOUT_VALUE = (i, b, c) -> b ? Component.text(i+1).decorate(TextDecoration.BOLD) : Component.text(i+1);
    public final static TriFunction<Integer, Boolean, Component, Component> VALUE_STRUCTURE_ONLY_VALUE = (i, b, c) -> b ? Component.text("→ ").append(c).decorate(TextDecoration.BOLD) : Component.text("→ ").append(c);
}
