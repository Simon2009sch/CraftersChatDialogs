package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditValues;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.*;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.MessageAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.*;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditValue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.function.TriFunction;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Named;
import java.beans.ConstructorProperties;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConfigEditGenericValue<T> extends ConfigEditValue<ConfigEditGenericValue<T>> {

    private T value = null;
    private Class<T> type;
    private AbstractButton<?> editButton = null;

    private Function<Object, Component> displayConstructor = DisplayPrefabs.GENERIC;
    private TriConsumer<String, T, AbstractButton<?>> setterActionConstructor = (path, button, value) -> {};

    private ConfigEditGenericValue(Class<T> type, T value) {
        this.value = value;
        this.type = type;
    }
    private ConfigEditGenericValue(Class<T> type) {
        this.type = type;
    }
    public Class<?> getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    public static <T> ConfigEditGenericValue<T> fromValue(@NotNull T value) {
        Class<T> type = (Class<T>) value.getClass();

        Function<Object, Component> displayFunction;

        if (type == String.class) {
            displayFunction = DisplayPrefabs.STRING;
        } else if (type == Integer.class) {
            displayFunction = DisplayPrefabs.INT;
        } else if (Number.class.isAssignableFrom(type)) {
            displayFunction = DisplayPrefabs.LIMITED_DECIMAL; // default for decimal types
        } else if (type == Location.class) {
            displayFunction = DisplayPrefabs.LOCATION;
        } else {
            displayFunction = DisplayPrefabs.GENERIC;
        }

        return create(type, value).displayConstructor(displayFunction);
    }

    public static <T> ConfigEditGenericValue<T> create(Class<T> type, T value) {
        return new ConfigEditGenericValue<>(type, value);
    }

    public static <T> ConfigEditGenericValue<T> create(Class<T> type) {
        return new ConfigEditGenericValue<>(type);
    }


    @Override
    public Object getValue() {
        return value;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public ConfigEditGenericValue<T> displayConstructor(Function<Object, Component> displayConstructor) {
        this.displayConstructor = displayConstructor;
        return this;
    }
    public Function<Object, Component> getDisplayConstructor() {
        return displayConstructor;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public ConfigEditGenericValue<T> value(T value) {
        this.value = value;
        return this;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public ConfigEditGenericValue<T> valueIfNull(T value) {
        if (this.value == null) {
            this.value = value;
        }
        return this;
    }

    public T value() {
        return value;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public ConfigEditGenericValue<T> editButton(AbstractButton<?> editButton) {
        this.editButton = editButton;
        return this;
    }
    public AbstractButton<?> editButton() {
        return editButton;
    }


    @Override
    public Component getButtonsToDisplay() {
        return Component.text("");
    }

    @Override
    public Component getValueToDisplay() {
        if (value == null) {
            Object defaultValue = getValueAction().apply(getCompletePath());
            if (type.isInstance(defaultValue)) {
                value = (T) defaultValue;
            }
            if (value == null) {
                value = getDefaultValue();
            }
        }
        if (editButton == null) {
            return getEditButtonFromType();
        }

        return editButton.addPostAction(disableAction()).compile();
    }

    public Component getEditButtonFromType() {
        AbstractButton<?> editButton = null;
        CustomAction reloadAction = CustomAction.create(p -> reloadAction().apply(p).accept(false));
        Component cancelText = Component.text("").append(Component.text("Type cancel to cancel.")
                .color(colorPalette().HINT())
                .clickEvent(ClickEvent.suggestCommand("cancel"))
                .hoverEvent(HoverEvent.showText(Component.text("Click to paste \"cancel\" in chat [Click to paste]"))));
        Component stringCancelInputText = Component.newline()
                .append(Component.text(" Put a \\ before it to input \"cancel\"")
                        .clickEvent(ClickEvent.suggestCommand("\\cancel"))
                        .hoverEvent(HoverEvent.showText(Component.text("Click to paste \"\\cancel\" in chat [Click to paste]"))));
        if (type == String.class) {
            editButton = StringInputButton.create(StringInputAction.create(player -> value -> {
                        setValueAction().accept(getCompletePath(), value);
                        reloadAction().apply(player)
                                .accept(false);
                    })
                    .addCancelAction(reloadAction)
                    .colorPalette(colorPalette())
                    .prompt(Component.text("")
                            .append(Component.text("Please enter text in chat!", colorPalette().PRIMARY(), TextDecoration.BOLD)
                                    .hoverEvent(HoverEvent.showText(Component.text("String")))
                                    .appendNewline()
                                    .append(Component.text("").style(Style.empty()))
                                    .append(Component.text("Current value: ", colorPalette().SECONDARY()).decoration(TextDecoration.BOLD, false))
                                    .append(Component.text(value.toString(), colorPalette().get("STRING")).decoration(TextDecoration.BOLD, false))
                                    .append(Component.text(" [Click to paste]", colorPalette().SECONDARY(), TextDecoration.BOLD)
                                            .clickEvent(ClickEvent.suggestCommand(value.toString())))
                                    .appendNewline()
                                    .append(cancelText.decoration(TextDecoration.BOLD, false))
                                    .append(stringCancelInputText.decoration(TextDecoration.BOLD, false))
                            )));
        } else if (type == Integer.class) {
            editButton = IntInputButton.create(IntInputAction.create(player -> value -> setValueAction().accept(getCompletePath(), value))
                    .addSuccessAction(reloadAction)
                    .addCancelAction(reloadAction)
                    .reTry(true)
                    .colorPalette(colorPalette())
                    .prompt(Component.text("")
                            .append(Component.text("Please enter a whole number in chat!", colorPalette().PRIMARY(), TextDecoration.BOLD)
                                    .hoverEvent(HoverEvent.showText(Component.text("Integer")))
                                    .appendNewline()
                                    .append(Component.text("").style(Style.empty()))
                                    .append(Component.text("Current value: ", colorPalette().SECONDARY()).decoration(TextDecoration.BOLD, false))
                                    .append(Component.text(value.toString(), colorPalette().get("INTEGER")).decoration(TextDecoration.BOLD, false))
                                    .append(Component.text(" [Click to paste]", colorPalette().SECONDARY())
                                            .clickEvent(ClickEvent.suggestCommand(value.toString())))
                                    .appendNewline()
                                    .append(cancelText)
                            )));
        } else if (type == Double.class) {
            editButton = DoubleInputButton.create(DoubleInputAction.create(player -> value -> setValueAction().accept(getCompletePath(), value))
                    .addSuccessAction(reloadAction)
                    .addCancelAction(reloadAction)
                    .reTry(true)
                    .colorPalette(colorPalette())
                    .prompt(Component.text("")
                            .append(Component.text("Please enter a decimal number in chat!", colorPalette().PRIMARY(), TextDecoration.BOLD)
                                    .hoverEvent(HoverEvent.showText(Component.text("Double")))
                                    .appendNewline()
                                    .append(Component.text("").style(Style.empty()))
                                    .append(Component.text("Current value: ", colorPalette().SECONDARY()).decoration(TextDecoration.BOLD, false))
                                    .append(Component.text(value.toString(), colorPalette().get("DOUBLE")).decoration(TextDecoration.BOLD, false))
                                    .append(Component.text(" [Click to paste]", colorPalette().SECONDARY(), TextDecoration.BOLD)
                                            .clickEvent(ClickEvent.suggestCommand(value.toString())))
                                    .appendNewline()
                                    .append(cancelText)
                            )));
        } else if (type == Float.class) {
            editButton = FloatInputButton.create(FloatInputAction.create(player -> value -> setValueAction().accept(getCompletePath(), value))
                    .addSuccessAction(reloadAction)
                    .addCancelAction(reloadAction)
                    .reTry(true)
                    .colorPalette(colorPalette())
                    .prompt(Component.text("")
                            .append(Component.text("Please enter a decimal number in chat!", colorPalette().PRIMARY(), TextDecoration.BOLD)
                                    .hoverEvent(HoverEvent.showText(Component.text("Float")))
                                    .appendNewline()
                                    .append(Component.text("").style(Style.empty()))
                                    .append(Component.text("Current value: ", colorPalette().SECONDARY()).decoration(TextDecoration.BOLD, false))
                                    .append(Component.text(value.toString(), colorPalette().get("FLOAT")).decoration(TextDecoration.BOLD, false))
                                    .append(Component.text(" [Click to paste]", colorPalette().SECONDARY(), TextDecoration.BOLD)
                                            .clickEvent(ClickEvent.suggestCommand(value.toString())))
                                    .appendNewline()
                                    .append(cancelText)
                            )));
        } else if (type == Location.class) {
            String currentValue = "";
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            DecimalFormat format = new DecimalFormat("0.###", symbols);

            currentValue += ((Location) value).getWorld() == null ? "" : ((Location) value).getWorld()
                    .getName() + " ";
            currentValue += format.format(((Location) value).getX()) + " ";
            currentValue += format.format(((Location) value).getY()) + " ";
            currentValue += format.format(((Location) value).getZ());
            String currentValueRotation = currentValue + " ";
            currentValueRotation += " " + format.format(((Location) value).getPitch()) + " ";
            currentValueRotation += format.format(((Location) value).getYaw());

            editButton = LocationInputButton.create(LocationInputAction.create(player -> value -> setValueAction().accept(getCompletePath(), value))
                    .prompt(Component.text("")
                            .append(Component.text("Please enter a location with the following format in chat!", colorPalette().PRIMARY(), TextDecoration.BOLD))
                            .appendNewline()
                            .append(Component.text("Format: ", colorPalette().SECONDARY()))
                            .append(Component.text("[world] ", colorPalette().get("LOCATION_WORLD")))
                            .append(Component.text("[x] ", colorPalette().get("LOCATION_X")))
                            .append(Component.text("[y] ", colorPalette().get("LOCATION_Y")))
                            .append(Component.text("[z] ", colorPalette().get("LOCATION_Z")))
                            .append(Component.text("[pitch] ", colorPalette().get("LOCATION_PITCH")))
                            .append(Component.text("[yaw]", colorPalette().get("LOCATION_YAW")))
                            .appendNewline()
                            .append(Component.text("Current location is: ", colorPalette().SECONDARY()))
                            .append(displayConstructor.apply(value).decorate(TextDecoration.BOLD))
                            .append(Component.text(" [Click to paste]", colorPalette().SECONDARY(), TextDecoration.BOLD)
                                    .clickEvent(ClickEvent.suggestCommand(currentValueRotation)))
                            .appendNewline()
                            .append(cancelText))
                    .addSuccessAction(reloadAction)
                    .addCancelAction(reloadAction)
                    .reTry(true)
                    .colorPalette(colorPalette()));
        } else if (type == Boolean.class) {
            editButton = ToggleButton.create((Boolean) value);
        } else {
            return displayConstructor.apply(value);
        }
        return editButton.text(displayConstructor.apply(value)).addPostAction(disableAction()).compile();
    }

    // prefabs
    public static final class DisplayPrefabs {
        private final static Component NULL_DISPLAY_VALUE = Component.text("NULL", NamedTextColor.RED, TextDecoration.BOLD);
        public final static Function<Object, Component> STRING = obj -> {
            if (obj instanceof String string) {
                return Component.text("\"", NamedTextColor.GREEN)
                        .append(Component.text(string, NamedTextColor.GREEN))
                        .append(Component.text("\"", NamedTextColor.GREEN));
            }
            if (obj == null) return NULL_DISPLAY_VALUE;
            return Component.text(obj.toString(), NamedTextColor.RED);
        };
        public final static Function<Object, Component> INT = obj -> {
            if (obj instanceof Integer integer) {
                return Component.text(integer, TextColor.fromHexString("#22DDDD"));
            }
            if (obj == null) return NULL_DISPLAY_VALUE;
            return Component.text(obj.toString(), NamedTextColor.RED);
        };
        public final static Function<Object, Component> DECIMAL = obj -> {
            if (obj instanceof Number number) {
                return Component.text(number.doubleValue(), TextColor.fromHexString("#22DDDD"));
            }
            if (obj == null) return NULL_DISPLAY_VALUE;
            return Component.text(obj.toString(), NamedTextColor.RED);
        };
        public final static Function<Object, Component> LIMITED_DECIMAL = obj -> {
            if (obj instanceof Integer integer) {
                return Component.text(integer, TextColor.fromHexString("#22DDDD"));
            }
            if (obj instanceof Number number) {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat format = new DecimalFormat("0.###", symbols); // Force '.' as decimal separator
                String formatted = format.format(number.doubleValue());
                return Component.text(formatted, TextColor.fromHexString("#22DDDD"));
            }
            if (obj == null) return NULL_DISPLAY_VALUE;

            return Component.text(obj.toString(), NamedTextColor.RED);
        };
        public final static Function<Object, Component> LOCATION = obj -> {
            if (obj instanceof Location loc) {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat format = new DecimalFormat("0.###", symbols); // Use dot for decimal

                String X = format.format(loc.getX());
                String Y = format.format(loc.getY());
                String Z = format.format(loc.getZ());

                return Component.text(X + " ", NamedTextColor.RED).append(Component.text(Y + " ", NamedTextColor.GREEN)).append(Component.text(Z, NamedTextColor.BLUE));
            }
            if (obj == null) return NULL_DISPLAY_VALUE;
            return Component.text(obj.toString(), NamedTextColor.RED);
        };
        public final static Function<Object, Component> LOCATION_WORLD = obj -> {
            if (obj instanceof Location loc) {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat format = new DecimalFormat("0.###", symbols); // Use dot for decimal

                String world = loc.getWorld().getName();
                String X = format.format(loc.getX());
                String Y = format.format(loc.getY());
                String Z = format.format(loc.getZ());

                return Component.text(world + " ", NamedTextColor.YELLOW).append(Component.text(X + " ", NamedTextColor.RED)).append(Component.text(Y + " ", NamedTextColor.GREEN)).append(Component.text(Z, NamedTextColor.BLUE));
            }
            if (obj == null) return NULL_DISPLAY_VALUE;
            return Component.text(obj.toString(), NamedTextColor.RED);
        };
        public final static Function<Object, Component> GENERIC = obj -> {
            if (obj == null) return NULL_DISPLAY_VALUE;
            return Component.text(obj.toString(), NamedTextColor.LIGHT_PURPLE);
        };
    }



    @SuppressWarnings("unchecked")
    private T getDefaultValue() {
        if (type == String.class) {
            return (T) "";
        } else if (type == Integer.class) {
            return (T) Integer.valueOf(0);
        } else if (type == Double.class) {
            return (T) Double.valueOf(0.0d);
        } else if (type == Float.class) {
            return (T) Float.valueOf(0.0f);
        } else if (type == Location.class) {
            return (T) new Location(Bukkit.getWorlds().getFirst(), 0, 0, 0);
        }
        return null;
    }

    @Override
    public ConfigEditGenericValue<T> clone() {
        ConfigEditGenericValue<T> copy = new ConfigEditGenericValue<>(this.type, this.value);
        copy.name(this.name())
                .setDescription(this.description())
                .showDescription(this.showDescription())
                .showPermission(this.showPermission())
                .editPermission(this.editPermission())
                .path(this.path())
                .pathName(this.pathName())
                .reloadAction(this.reloadAction())
                .disableAction(this.disableAction())
                .getValueAction(getValueAction())
                .displayConstructor(this.displayConstructor);
        copy.colorPalette(colorPalette());

        return copy;
    }

    @Override
    public void disableButtons() {
        if (editButton != null) editButton.setDisabled(true);
    }

}