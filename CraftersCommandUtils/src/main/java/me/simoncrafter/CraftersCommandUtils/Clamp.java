package me.simoncrafter.CraftersCommandUtils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class Clamp<T extends Number> {

    private final Class<T> type;

    private final @Nullable T lower;
    private final @Nullable T upper;

    private final boolean enableLower;
    private final boolean enableUpper;

    private Clamp(Class<T> type, @Nullable T lower, boolean enableLower, @Nullable T upper, boolean enableUpper) {
        this.type = type;
        this.lower = lower;
        this.enableLower = enableLower;
        this.upper = upper;
        this.enableUpper = enableUpper;
    }

    @Contract(pure = true, value = "_ -> new")
    public static <T extends Number> Clamp<T> create(Class<T> type) {
        return new Clamp<>(type, null, false, null, false);
    }
    @Contract(pure = true, value = "_, _, _ -> new")
    public static <T extends Number> Clamp<T> create(Class<T> type, @Nullable T lower, @Nullable T upper) {
        T safeLower = lower != null ? lower : castZero(type);
        boolean enableLower = lower != null;

        T safeUpper = upper != null ? upper : castZero(type);
        boolean enableUpper = upper != null;

        return new Clamp<>(type, safeLower, enableLower, safeUpper, enableUpper);
    }

    public boolean upper() {
        return enableUpper;
    }

    public boolean lower() {
        return enableLower;
    }

    // --- LOWER ---

    @Contract(value = "_ -> new", pure = true)
    public Clamp<T> lower(boolean enable) {
        return new Clamp<>(type, this.lower, enable, this.upper, this.enableUpper);
    }

    @Contract(value = "_ -> new", pure = true)
    public Clamp<T> lower(T value) {
        return new Clamp<>(type, value, this.enableLower, this.upper, this.enableUpper);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public Clamp<T> lower(boolean enable, T value) {
        return new Clamp<>(type, value, enable, this.upper, this.enableUpper);
    }

    // --- UPPER ---

    @Contract(value = "_ -> new", pure = true)
    public Clamp<T> upper(boolean enable) {
        return new Clamp<>(type, this.lower, this.enableLower, this.upper, enable);
    }

    @Contract(value = "_ -> new", pure = true)
    public Clamp<T> upper(T value) {
        return new Clamp<>(type, this.lower, this.enableLower, value, this.enableUpper);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public Clamp<T> upper(boolean enable, T value) {
        return new Clamp<>(type, this.lower, this.enableLower, value, enable);
    }

    // --- CLAMP METHODS ---

    public boolean checkUpperIgnoreEnabled(T value) {
        return upper != null && value.doubleValue() < upper.doubleValue();
    }
    public boolean checkLowerIgnoreEnabled(T value) {
        return lower != null && value.doubleValue() > lower.doubleValue();
    }

    public boolean checkUpper(T value) {
        return enableUpper && checkUpperIgnoreEnabled(value);
    }
    public boolean checkLower(T value) {
        return enableLower && checkLowerIgnoreEnabled(value);
    }

    public boolean checkIgnoreEnabled(T value) {
        return checkUpperIgnoreEnabled(value) && checkLowerIgnoreEnabled(value);
    }

    public boolean check(T value) {
        return checkUpper(value) && checkLower(value);
    }

    public T apply(T input) {
        return apply(input.doubleValue());
    }

    public T apply(double value) {
        if (enableLower && lower != null && value < lower.doubleValue()) {
            value = lower.doubleValue();
        }
        if (enableUpper && upper != null && value > upper.doubleValue()) {
            value = upper.doubleValue();
        }
        return castToT(value);
    }

    // --- Inclusive check methods ---

    public boolean checkUpperInclusiveIgnoreEnabled(T value) {
        return upper != null && value.doubleValue() <= upper.doubleValue();
    }

    public boolean checkLowerInclusiveIgnoreEnabled(T value) {
        return lower != null && value.doubleValue() >= lower.doubleValue();
    }

    public boolean checkInclusiveIgnoreEnabled(T value) {
        return checkUpperInclusiveIgnoreEnabled(value) && checkLowerInclusiveIgnoreEnabled(value);
    }

    public boolean checkUpperInclusive(T value) {
        return enableUpper && checkUpperInclusiveIgnoreEnabled(value);
    }

    public boolean checkLowerInclusive(T value) {
        return enableLower && checkLowerInclusiveIgnoreEnabled(value);
    }

    public boolean checkInclusive(T value) {
        return checkUpperInclusive(value) && checkLowerInclusive(value);
    }

    // --- Inclusive apply methods ---

    public T applyInclusive(T input) {
        return applyInclusive(input.doubleValue());
    }

    public T applyInclusive(double value) {
        if (enableLower && lower != null && value <= lower.doubleValue()) {
            value = lower.doubleValue();
        }
        if (enableUpper && upper != null && value >= upper.doubleValue()) {
            value = upper.doubleValue();
        }
        return castToT(value);
    }

    public T applyIgnoreEnabledInclusive(T input) {
        return applyIgnoreEnabledInclusive(input.doubleValue());
    }

    public T applyIgnoreEnabledInclusive(double value) {
        if (lower != null && value <= lower.doubleValue()) {
            value = lower.doubleValue();
        }
        if (upper != null && value >= upper.doubleValue()) {
            value = upper.doubleValue();
        }
        return castToT(value);
    }

    // --- UTILITY ---

    private T castToT(double value) {
        if (type == Integer.class) return type.cast((int) value);
        if (type == Double.class) return type.cast(value);
        if (type == Float.class) return type.cast((float) value);
        if (type == Long.class) return type.cast((long) value);
        if (type == Short.class) return type.cast((short) value);
        if (type == Byte.class) return type.cast((byte) value);
        throw new UnsupportedOperationException("Unsupported number type: " + type.getName());
    }

    @SuppressWarnings("unchecked")
    private static <T extends Number> T castZero(Class<T> type) {
        if (type == Integer.class) return (T) Integer.valueOf(0);
        if (type == Double.class) return (T) Double.valueOf(0.0);
        if (type == Float.class) return (T) Float.valueOf(0f);
        if (type == Long.class) return (T) Long.valueOf(0L);
        if (type == Short.class) return (T) Short.valueOf((short) 0);
        if (type == Byte.class) return (T) Byte.valueOf((byte) 0);
        throw new UnsupportedOperationException("Unsupported number type: " + type.getName());
    }

    @Contract(pure = true, value = "-> new")
    public Clamp<T> copy() {
        return new Clamp<>(type, this.lower, this.enableLower, this.upper, this.enableUpper);
    }

}
