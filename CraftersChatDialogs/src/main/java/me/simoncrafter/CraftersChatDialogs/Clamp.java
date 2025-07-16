package me.simoncrafter.CraftersChatDialogs;

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
    public T lowerValue() {
        return lower;
    }
    public T upperValue() {
        return upper;
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

    @Override
    @Contract(pure = true, value = "-> new")
    public Clamp<T> clone() {
        return new Clamp<>(type, this.lower, this.enableLower, this.upper, this.enableUpper);
    }


    //prefabs

    // Float Clamps
    public static final Clamp<Float> ZERO_FLOAT = Clamp.create(Float.class, 0f, 0f);

    public static final Clamp<Float> NONE_FLOAT = Clamp.create(Float.class, null, null);
    public static final Clamp<Float> ALL_FLOAT = Clamp.create(Float.class, -Float.MAX_VALUE, Float.MAX_VALUE);

    public static final Clamp<Float> POSITIVE_FLOAT_INCLUSIVE = Clamp.create(Float.class, 0f, Float.MAX_VALUE);
    public static final Clamp<Float> POSITIVE_FLOAT_EXCLUSIVE = Clamp.create(Float.class, Float.MIN_VALUE, Float.MAX_VALUE);

    public static final Clamp<Float> NEGATIVE_FLOAT_INCLUSIVE = Clamp.create(Float.class, -Float.MAX_VALUE, 0f);
    public static final Clamp<Float> NEGATIVE_FLOAT_EXCLUSIVE = Clamp.create(Float.class, -Float.MAX_VALUE, -Float.MIN_VALUE);

    // Double Clamps
    public static final Clamp<Double> ZERO_DOUBLE = Clamp.create(Double.class, 0d, 0d);

    public static final Clamp<Double> NONE_DOUBLE = Clamp.create(Double.class, null, null);
    public static final Clamp<Double> ALL_DOUBLE = Clamp.create(Double.class, -Double.MAX_VALUE, Double.MAX_VALUE);

    public static final Clamp<Double> POSITIVE_DOUBLE_INCLUSIVE = Clamp.create(Double.class, 0d, Double.MAX_VALUE);
    public static final Clamp<Double> POSITIVE_DOUBLE_EXCLUSIVE = Clamp.create(Double.class, Double.MIN_VALUE, Double.MAX_VALUE);

    public static final Clamp<Double> NEGATIVE_DOUBLE_INCLUSIVE = Clamp.create(Double.class, -Double.MAX_VALUE, 0d);
    public static final Clamp<Double> NEGATIVE_DOUBLE_EXCLUSIVE = Clamp.create(Double.class, -Double.MAX_VALUE, -Double.MIN_VALUE);

    // Integer Clamps
    public static final Clamp<Integer> ZERO_INT = Clamp.create(Integer.class, 0, 0);

    public static final Clamp<Integer> NONE_INT = Clamp.create(Integer.class, null, null);
    public static final Clamp<Integer> ALL_INT = Clamp.create(Integer.class, Integer.MIN_VALUE, Integer.MAX_VALUE);

    public static final Clamp<Integer> POSITIVE_INT_INCLUSIVE = Clamp.create(Integer.class, 0, Integer.MAX_VALUE);
    public static final Clamp<Integer> POSITIVE_INT_EXCLUSIVE = Clamp.create(Integer.class, 1, Integer.MAX_VALUE);

    public static final Clamp<Integer> NEGATIVE_INT_INCLUSIVE = Clamp.create(Integer.class, Integer.MIN_VALUE, 0);
    public static final Clamp<Integer> NEGATIVE_INT_EXCLUSIVE = Clamp.create(Integer.class, Integer.MIN_VALUE, -1);


}
