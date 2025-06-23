package me.simoncrafter.CraftersCommandUtils.dialogs.prefabs.actions;

import me.simoncrafter.CraftersCommandUtils.Clamp;
import me.simoncrafter.CraftersCommandUtils.dialogs.def.IAction;
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
    public static <T extends Number> @NotNull NumberAction<T> create(Class<T> type, NumberActionOperations actionType, T number, T operatorNumber, Consumer<T> operation) {
        NumberAction<T> action = new NumberAction<>(type);
        action.operation = operation;
        action.actionType = actionType;
        action.number = number;
        action.operatorNumber = operatorNumber;
        action.clamp = Clamp.create(type);
        return action;
    }

    @Contract(value = "_ -> new")
    public NumberAction<T> clamp(Clamp<T> clamp) {
        NumberAction<T> newAction = copy();
        newAction.clamp = clamp;
        return newAction;
    }

    @Contract(value = "_, _ -> new")
    public NumberAction<T> clamp(T lower, T upper) {
        return clamp(Clamp.create(type, lower, upper));
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
        if (disabled) {return;}

        if (number == null || operatorNumber == null) {
            return;
        }

        double a = number.doubleValue();
        double b = operatorNumber.doubleValue();
        switch (actionType) {
            case ADD -> {
                operation.accept(clamp.apply(a + b));
            }
            case SUBTRACT -> {
                operation.accept(clamp.apply(a - b));
            }
            case MULTIPLY -> {
                operation.accept(clamp.apply(a * b));
            }
            case DIVIDE -> {
                operation.accept(clamp.apply(a / b));
            }
            case SET -> {
                operation.accept(clamp.apply(operatorNumber));
            }

        }
    }



    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }
    private NumberAction<T> copy() {
        NumberAction<T> copy = new NumberAction<>(type);
        copy.disabled = this.disabled;
        copy.operation = this.operation;
        copy.actionType = this.actionType;
        copy.number = this.number;
        copy.operatorNumber = this.operatorNumber;
        copy.clamp = this.clamp;
        return copy;
    }
}
