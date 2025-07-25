package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditValues;

import me.simoncrafter.CraftersChatDialogs.dialogs.def.AbstractButton;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions.ColorPalette;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.DisplayOptions.DisplayOption;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.CustomAction;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions.InputActions.*;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.buttons.*;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditValue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ConfigEditGenericValue<T> extends ConfigEditValue<ConfigEditGenericValue<T>> {

    private T value = null;
    private Class<T> type;
    private AbstractButton<?> editButton = null;

    private BiFunction<Object, ColorPalette, Component> displayConstructor = DisplayPrefabs.GENERIC;
    private TriConsumer<String, T, AbstractButton<?>> setterActionConstructor = (path, button, value) -> {};

    private Function<AbstractButton<?>, AbstractButton<?>> editButtonModifier = button -> button;

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

    @Contract(value = "_ -> this", mutates = "this")
    public ConfigEditGenericValue<T> editButtonModifier(Function<AbstractButton<?>, AbstractButton<?>> editButtonModifier) {
        this.editButtonModifier = editButtonModifier;
        return this;
    }
    public Function<AbstractButton<?>, AbstractButton<?>> editButtonModifier() {
        return editButtonModifier;
    }

    @SuppressWarnings("unchecked")
    public static <T> ConfigEditGenericValue<T> fromValue(@NotNull T value) {
        Class<T> type = (Class<T>) value.getClass();

        BiFunction<Object, ColorPalette, Component> displayFunction;

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
    public ConfigEditGenericValue<T> displayConstructor(BiFunction<Object, ColorPalette, Component> displayConstructor) {
        this.displayConstructor = displayConstructor;
        return this;
    }
    public BiFunction<Object, ColorPalette, Component> displayConstructor() {
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
        return getInactiveButtonsToDisplay();
    }
    @Override
    public Component getInactiveButtonsToDisplay() {
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
            AbstractButton<?> editButtonFromType = getEditButtonFromType();
            if (editButtonFromType != null) {
                editButton = editButtonFromType;
            }else {
                return displayConstructor.apply(value, displayOption().colorPalette());
            }
        }
        editButton.text(getInactiveValueToDisplay());
        editButton = editButtonModifier.apply(editButton.clone()); // apply api user defined action

        return editButton.addPostAction(disableAction()).compile();
    }
    @Override
    public Component getInactiveValueToDisplay() {
        return displayConstructor.apply(value, displayOption().colorPalette());
    }

    public AbstractButton<?> getEditButtonFromType() {
        return getEditButtonFromType(displayOption());
    }
    public AbstractButton<?> getEditButtonFromType(DisplayOption displayOption) {
        AbstractButton<?> editButton = null;
        CustomAction reloadAction = CustomAction.create(p -> reloadAction().apply(p).accept(false));

        // default for all types
        Component cancelText = Component.text("").append(Component.text("Type cancel to cancel. [Click to paste]")
                        .decoration(TextDecoration.BOLD, false))
                .color(displayOption().colorPalette().HINT())
                .clickEvent(ClickEvent.suggestCommand("cancel"));
        // default for string string
        Component stringCancelInputText = Component.newline()
                .append(Component.text(" Put a \\ before it to input \"cancel\" [Click to paste]", displayOption().colorPalette().HINT())
                        .clickEvent(ClickEvent.suggestCommand("\\cancel"))
                        .decoration(TextDecoration.BOLD, false));

        // check type and create edit button
        if (type == String.class) {
            editButton = StringInputButton.create(StringInputAction.create(player -> value -> {
                        setValueAction().accept(getCompletePath(), value);
                        reloadAction().apply(player)
                                .accept(false);
                    })
                    .addCancelAction(reloadAction)
                    .displayOption(displayOption())
                    .prompt(Component.text("")
                            .append(Component.text("Please enter text in chat!", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD)
                                    .hoverEvent(HoverEvent.showText(Component.text("String")))
                                    .appendNewline()
                                    .append(Component.text("")
                                            .style(Style.empty()))
                                    .append(Component.text("Current value: ", displayOption().colorPalette().SECONDARY())
                                            .decoration(TextDecoration.BOLD, false))
                                    .append(Component.text(value.toString(), displayOption().colorPalette().get("STRING"))
                                            .decoration(TextDecoration.BOLD, false))
                                    .append(Component.text(" [Click to paste]", displayOption().colorPalette().SECONDARY(), TextDecoration.BOLD)
                                            .clickEvent(ClickEvent.suggestCommand(value.toString())))
                                    .appendNewline()
                                    .append(cancelText.decoration(TextDecoration.BOLD, false))
                                    .append(stringCancelInputText.decoration(TextDecoration.BOLD, false))))
                    .syncKey(syncKey())
                    .addPostAction(displayOption().soundOption().SUCCESS().toSoundAction())

            ).addAction(displayOption().soundOption().INPUT_REQUIRED().toSoundAction());
        } else if (type == Integer.class) {
            editButton = IntInputButton.create(IntInputAction.create(player -> value -> setValueAction().accept(getCompletePath(), value))
                    .addSuccessAction(reloadAction)
                    .addCancelAction(reloadAction)
                    .reTry(true)
                    .displayOption(displayOption())
                    .prompt(Component.text("")
                            .append(Component.text("Please enter a whole number in chat!", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD)
                                    .hoverEvent(HoverEvent.showText(Component.text("Integer")))
                                    .appendNewline()
                                    .append(Component.text("")
                                            .style(Style.empty()))
                                    .append(Component.text("Current value: ", displayOption().colorPalette().SECONDARY())
                                            .decoration(TextDecoration.BOLD, false))
                                    .append(Component.text(value.toString(), displayOption().colorPalette().get("INT"))
                                            .decoration(TextDecoration.BOLD, false))
                                    .append(Component.text(" [Click to paste]", displayOption().colorPalette().SECONDARY())
                                            .clickEvent(ClickEvent.suggestCommand(value.toString())))
                                    .appendNewline()
                                    .append(cancelText)))
                    .syncKey(syncKey())
                    .addSuccessAction(displayOption().soundOption().SUCCESS().toSoundAction())
                    .addReTryAction(displayOption().soundOption().ERROR().toSoundAction())

            ).addAction(displayOption().soundOption().INPUT_REQUIRED().toSoundAction());
        } else if (type == Double.class) {
            editButton = DoubleInputButton.create(DoubleInputAction.create(player -> value -> setValueAction().accept(getCompletePath(), value))
                    .addSuccessAction(reloadAction)
                    .addCancelAction(reloadAction)
                    .reTry(true)
                    .displayOption(displayOption())
                    .prompt(Component.text("")
                            .append(Component.text("Please enter a decimal number in chat!", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD)
                                    .hoverEvent(HoverEvent.showText(Component.text("Double")))
                                    .appendNewline()
                                    .append(Component.text("")
                                            .style(Style.empty()))
                                    .append(Component.text("Current value: ", displayOption().colorPalette().SECONDARY())
                                            .decoration(TextDecoration.BOLD, false))
                                    .append(Component.text(value.toString(), displayOption().colorPalette().get("DECIMAL"))
                                            .decoration(TextDecoration.BOLD, false))
                                    .append(Component.text(" [Click to paste]", displayOption().colorPalette().SECONDARY(), TextDecoration.BOLD)
                                            .clickEvent(ClickEvent.suggestCommand(value.toString())))
                                    .appendNewline()
                                    .append(cancelText)))
                    .syncKey(syncKey())
                    .addSuccessAction(displayOption().soundOption().SUCCESS().toSoundAction())
                    .addReTryAction(displayOption().soundOption().ERROR().toSoundAction())

            ).addAction(displayOption().soundOption().INPUT_REQUIRED().toSoundAction());
        } else if (type == Float.class) {
            editButton = FloatInputButton.create(FloatInputAction.create(player -> value -> setValueAction().accept(getCompletePath(), value))
                    .addSuccessAction(reloadAction)
                    .addCancelAction(reloadAction)
                    .reTry(true)
                    .displayOption(displayOption())
                    .prompt(Component.text("")
                            .append(Component.text("Please enter a decimal number in chat!", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD)
                                    .hoverEvent(HoverEvent.showText(Component.text("Float")))
                                    .appendNewline()
                                    .append(Component.text("")
                                            .style(Style.empty()))
                                    .append(Component.text("Current value: ", displayOption().colorPalette().SECONDARY())
                                            .decoration(TextDecoration.BOLD, false))
                                    .append(Component.text(value.toString(), displayOption().colorPalette().get("DECIMAL"))
                                            .decoration(TextDecoration.BOLD, false))
                                    .append(Component.text(" [Click to paste]", displayOption().colorPalette().SECONDARY(), TextDecoration.BOLD)
                                            .clickEvent(ClickEvent.suggestCommand(value.toString())))
                                    .appendNewline()
                                    .append(cancelText)))
                    .syncKey(syncKey())
                    .addSuccessAction(displayOption().soundOption().SUCCESS().toSoundAction())
                    .addReTryAction(displayOption().soundOption().ERROR().toSoundAction())

            ).addAction(displayOption().soundOption().INPUT_REQUIRED().toSoundAction());
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
                            .append(Component.text("Please enter a location with the following format in chat!", displayOption().colorPalette().PRIMARY(), TextDecoration.BOLD))
                            .appendNewline()
                            .append(Component.text("Format: ", displayOption().colorPalette().SECONDARY()))
                            .append(Component.text("[world] ", displayOption().colorPalette().get("LOCATION_WORLD")))
                            .append(Component.text("[x] ", displayOption().colorPalette().get("LOCATION_X")))
                            .append(Component.text("[y] ", displayOption().colorPalette().get("LOCATION_Y")))
                            .append(Component.text("[z] ", displayOption().colorPalette().get("LOCATION_Z")))
                            .append(Component.text("[pitch] ", displayOption().colorPalette().get("LOCATION_PITCH")))
                            .append(Component.text("[yaw]", displayOption().colorPalette().get("LOCATION_YAW")))
                            .appendNewline()
                            .append(Component.text("Current location is: ", displayOption().colorPalette().SECONDARY()))
                            .append(displayConstructor.apply(value, displayOption().colorPalette())
                                    .decorate(TextDecoration.BOLD))
                            .append(Component.text(" [Click to paste]", displayOption().colorPalette().SECONDARY(), TextDecoration.BOLD)
                                    .clickEvent(ClickEvent.suggestCommand(currentValueRotation)))
                            .appendNewline()
                            .append(cancelText))
                    .syncKey(syncKey())
                    .addSuccessAction(reloadAction)
                    .addCancelAction(reloadAction)
                    .reTry(true)
                    .displayOption(displayOption())
                    .addSuccessAction(displayOption().soundOption().SUCCESS().toSoundAction())
                    .addReTryAction(displayOption().soundOption().ERROR().toSoundAction())

            ).addAction(displayOption().soundOption().INPUT_REQUIRED().toSoundAction());
        } else if (type == Boolean.class) {
            editButton = ToggleButton.create((Boolean) value)
                    .toggleCallback(bool -> setValueAction().accept(getCompletePath(), bool))
                    .displayOption(displayOption())
                    .reloadAction(reloadAction())
                    .reloadOnUse(true)
                    .enabledText(Component.text("true", displayOption().colorPalette().get("BOOLEAN_TRUE")))
                    .disabledText(Component.text("false", displayOption().colorPalette().get("BOOLEAN_FALSE")))
                    .addEnableAction(displayOption().soundOption().CLICk_ON().toSoundAction())
                    .addDisableAction(displayOption().soundOption().CLICk_OFF().toSoundAction());
        } else {
            return null;
        }
        return editButton;
    }

    // prefabs
    public static final class DisplayPrefabs {
        private final static Component NULL_DISPLAY_VALUE = Component.text("NULL", NamedTextColor.RED, TextDecoration.BOLD);
        public final static BiFunction<Object, ColorPalette, Component> STRING = (obj, color) -> {
            if (obj instanceof String string) {
                return Component.text("\"", color.get("STRING"))
                        .append(Component.text(string, color.get("STRING")))
                        .append(Component.text("\"", color.get("STRING")));
            }
            if (obj == null) return NULL_DISPLAY_VALUE;
            return Component.text(obj.toString(), NamedTextColor.RED);
        };

        public final static BiFunction<Object, ColorPalette, Component> INT = (obj, color) -> {
            if (obj instanceof Integer integer) {
                return Component.text(integer, color.get("INT"));
            }
            if (obj == null) return NULL_DISPLAY_VALUE;
            return Component.text(obj.toString(), NamedTextColor.RED);
        };

        public final static BiFunction<Object, ColorPalette, Component> DECIMAL = (obj, color) -> {
            if (obj instanceof Number number) {
                return Component.text(number.doubleValue(), color.get("DECIMAL"));
            }
            if (obj == null) return NULL_DISPLAY_VALUE;
            return Component.text(obj.toString(), NamedTextColor.RED);
        };

        public final static BiFunction<Object, ColorPalette, Component> LIMITED_DECIMAL = (obj, color) -> {
            if (obj instanceof Integer integer) {
                return Component.text(integer, color.get("DECIMAL"));
            }
            if (obj instanceof Number number) {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat format = new DecimalFormat("0.###", symbols);
                String formatted = format.format(number.doubleValue());
                return Component.text(formatted, color.get("DECIMAL"));
            }
            if (obj == null) return NULL_DISPLAY_VALUE;
            return Component.text(obj.toString(), NamedTextColor.RED);
        };

        public final static BiFunction<Object, ColorPalette, Component> LOCATION = (obj, color) -> {
            if (obj instanceof Location loc) {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat format = new DecimalFormat("0.###", symbols);
                String X = format.format(loc.getX());
                String Y = format.format(loc.getY());
                String Z = format.format(loc.getZ());

                return Component.text(X + " ", color.get("LOCATION_X"))
                        .append(Component.text(Y + " ", color.get("LOCATION_Y")))
                        .append(Component.text(Z, color.get("LOCATION_Z")));
            }
            if (obj == null) return NULL_DISPLAY_VALUE;
            return Component.text(obj.toString(), NamedTextColor.RED);
        };

        public final static BiFunction<Object, ColorPalette, Component> LOCATION_WORLD = (obj, color) -> {
            if (obj instanceof Location loc) {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat format = new DecimalFormat("0.###", symbols);
                String world = loc.getWorld() == null ? "" : loc.getWorld().getName() + " ";
                String X = format.format(loc.getX());
                String Y = format.format(loc.getY());
                String Z = format.format(loc.getZ());

                return Component.text(world, color.get("LOCATION_WORLD"))
                        .append(Component.text(X + " ", color.get("LOCATION_X")))
                        .append(Component.text(Y + " ", color.get("LOCATION_Y")))
                        .append(Component.text(Z, color.get("LOCATION_Z")));
            }
            if (obj == null) return NULL_DISPLAY_VALUE;
            return Component.text(obj.toString(), NamedTextColor.RED);
        };

        public final static BiFunction<Object, ColorPalette, Component> LOCATION_ROTATION = (obj, color) -> {
            if (obj instanceof Location loc) {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat format = new DecimalFormat("0.###", symbols);

                String X = format.format(loc.getX());
                String Y = format.format(loc.getY());
                String Z = format.format(loc.getZ());
                String Yaw = format.format(loc.getYaw());
                String Pitch = format.format(loc.getPitch());

                return Component.text(X + " ", color.get("LOCATION_X"))
                        .append(Component.text(Y + " ", color.get("LOCATION_Y")))
                        .append(Component.text(Z + " ", color.get("LOCATION_Z")))
                        .append(Component.text(Yaw + " ", color.get("LOCATION_YAW")))
                        .append(Component.text(Pitch, color.get("LOCATION_PITCH")));
            }
            if (obj == null) return NULL_DISPLAY_VALUE;
            return Component.text(obj.toString(), NamedTextColor.RED);
        };

        public final static BiFunction<Object, ColorPalette, Component> LOCATION_FULL = (obj, color) -> {
            if (obj instanceof Location loc) {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat format = new DecimalFormat("0.###", symbols);

                String world = loc.getWorld() == null ? "" : loc.getWorld().getName() + " ";
                String X = format.format(loc.getX());
                String Y = format.format(loc.getY());
                String Z = format.format(loc.getZ());
                String Yaw = format.format(loc.getYaw());
                String Pitch = format.format(loc.getPitch());

                return Component.text(world, color.get("LOCATION_WORLD"))
                        .append(Component.text(X + " ", color.get("LOCATION_X")))
                        .append(Component.text(Y + " ", color.get("LOCATION_Y")))
                        .append(Component.text(Z + " ", color.get("LOCATION_Z")))
                        .append(Component.text(Yaw + " ", color.get("LOCATION_YAW")))
                        .append(Component.text(Pitch, color.get("LOCATION_PITCH")));
            }
            if (obj == null) return NULL_DISPLAY_VALUE;
            return Component.text(obj.toString(), NamedTextColor.RED);
        };

        public final static BiFunction<Object, ColorPalette, Component> GENERIC = (obj, color) -> {
            if (obj == null) return NULL_DISPLAY_VALUE;
            return Component.text(obj.toString(), color.get("GENERIC"));
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
                /*.checkShowPermission(this.checkShowPermission()) TODO: See ConfigEditValue line 22-24
                .checkEditPermission(this.checkEditPermission())*/
                .path(this.path())
                .pathName(this.pathName())
                .reloadAction(this.reloadAction())
                .disableAction(this.disableAction())
                .getValueAction(getValueAction())
                .displayConstructor(this.displayConstructor);
        copy.displayOption(displayOption());

        return copy;
    }

    @Override
    public void disableButtons() {
        if (editButton != null) editButton.setDisabled(true);
    }

}