package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.actions;

import me.simoncrafter.CraftersChatDialogs.Clamp;
import me.simoncrafter.CraftersChatDialogs.dialogs.def.IAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class NumberAction<T extends Number> implements IAction {

    private boolean disabled = false;
    private Consumer<T> operation = newNumber -> {};
    private NumberActionOperations actionType = NumberActionOperations.ADD;
    private T number = null;
    private T operatorNumber = null;
    private boolean looping = false;

    private Clamp<T> clamp = null;

    private Class<T> type;

    private NumberAction(Class<T> type) {
        this.type = type;
    }

    @Contract(value = "_ -> new")
    public static <T extends Number> @NotNull NumberAction<T> create(Class<T> type) {
        NumberAction<T> action = new NumberAction<>(type);
        action.clamp = Clamp.create(type);
        return action;
    }
    @Contract(value = "_ -> new")
    public static <T extends Number> @NotNull NumberAction<T> create(Class<T> type, T number, T operatorNumber, Consumer<T> operation) {
        NumberAction<T> action = new NumberAction<>(type);
        action.operation = operation;
        action.number = number;
        action.operatorNumber = operatorNumber;
        action.clamp = Clamp.create(type);
        return action;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public NumberAction<T> looping(boolean looping) {
        this.looping = looping;
        return this;
    }

    public boolean looping() {
        return looping;
    }

    @Contract(value = "_ -> new")
    public NumberAction<T> clampButClone(Clamp<T> clamp) {
        NumberAction<T> newAction = clone();
        newAction.clamp = clamp;
        return newAction;
    }

    @Contract(value = "_, _ -> new")
    public NumberAction<T> clampButClone(T lower, T upper) {
        return clampButClone(Clamp.create(type, lower, upper));
    }

    @Contract(value = "_ -> this", mutates = "this")
    public NumberAction<T> clamp(Clamp<T> clamp) {
        this.clamp = clamp;
        return this;
    }
    @Contract(value = "_, _ -> this", mutates = "this")
    public NumberAction<T> clamp(T lower, T upper) {
        this.clamp = Clamp.create(type, lower, upper);
        return this;
    }

    public Clamp<T> clamp() {
        return clamp;
    }

    @Contract(value = "_ -> this", mutates = "this")
    public NumberAction<T> operation(Consumer<T> operation) {
        this.operation = operation;
        return this;
    }
    public Consumer<T> operation() {
        return operation;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public NumberAction<T> actionType(NumberActionOperations actionType) {
        this.actionType = actionType;
        return this;
    }
    public NumberActionOperations actionType() {
        return actionType;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public NumberAction<T> number(T number) {
        this.number = number;
        return this;
    }
    public T number() {
        return number;
    }
    @Contract(value = "_ -> this", mutates = "this")
    public NumberAction<T> operatorNumber(T operatorNumber) {
        this.operatorNumber = operatorNumber;
        return this;
    }
    public T operatorNumber() {
        return operatorNumber;
    }



    @Override
    public void run(Player player) {
        if (disabled) return;
        if (number == null || operatorNumber == null) return;

        double a = number.doubleValue();
        double b = operatorNumber.doubleValue();
        double result;


        switch (actionType) {
            case ADD -> result = a + b;
            case SUBTRACT -> result = a - b;
            case MULTIPLY -> result = a * b;
            case DIVIDE -> result = a / b;
            case SET -> result = b;
            default -> result = a + b;
        }

        // If not looping, just clamp normally
        if (!looping) {
            T clamped = clamp.apply(result);
            operation.accept(clamped);
            return;
        }

        // Looping enabled: make sure clamp bounds are enabled and non-null
        if (!clamp.lower() || !clamp.upper() || clamp.lowerValue() == null || clamp.upperValue() == null) {
            // If bounds not properly set, fallback to normal clamp
            operation.accept(clamp.apply(result));
            return;
        }


        double min = clamp.lowerValue().doubleValue();
        double max = clamp.upperValue().doubleValue();
        double range = max - min + 1;

        // Wrap the result inside [min, max] range with modulo
        double wrapped = ((result - min) % range);
        if (wrapped < 0) wrapped += range;
        wrapped += min;

        operation.accept(castToT(wrapped));
    }



    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }


    @SuppressWarnings("unchecked")
    private T castToT(double value) {
        if (type == Integer.class) return (T) Integer.valueOf((int) value);
        if (type == Double.class) return (T) Double.valueOf(value);
        if (type == Float.class) return (T) Float.valueOf((float) value);
        if (type == Long.class) return (T) Long.valueOf((long) value);
        if (type == Short.class) return (T) Short.valueOf((short) value);
        if (type == Byte.class) return (T) Byte.valueOf((byte) value);
        throw new IllegalArgumentException("Unsupported number type: " + type.getName());
    }

    @Override
    public NumberAction<T> clone() {
        NumberAction<T> clone = new NumberAction<>(type);
        clone.disabled = this.disabled;
        clone.operation = this.operation;
        clone.actionType = this.actionType;
        clone.number = this.number;
        clone.operatorNumber = this.operatorNumber;
        clone.clamp = this.clamp;
        return clone;
    }
}
