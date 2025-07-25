package me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion;

import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditValues.ConfigEditGenericValue;
import me.simoncrafter.CraftersChatDialogs.dialogs.prefabs.questions.ConfigEditQuestion.ConfigEditValues.ConfigEditListSection;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// Quick note: ChatGPT mostly(~80%) wrote this file. (With a few minor changes from me)
//   I was to lazy to think of all the edge cases myself.
//   Also it wrote all the docs comments for me.
//   And it's basicly the same thing over and over again. And that's what AI is for. ;-)


public class ConfigEditData {

    private Map<String, Object> values = new HashMap<>();
    private Map<String, ConfigEditValue<?>> displayMap = new HashMap<>();
    private Consumer<ConfigEditData> setterCallback = newData -> {};
    private ConfigEditSection rootSection = null;

    private ConfigEditData(Map<String, Object> values) {
        this.values = values;
    }

    /**
     * Creates a new ConfigEditData instance with the provided map of values.
     *
     * @param map The map containing configuration values
     * @return A new ConfigEditData instance
     */
    public static ConfigEditData create(Map<String, Object> map) {
        return new ConfigEditData(map);
    }
    /**
     * Creates a new empty ConfigEditData instance.
     *
     * @return A new empty ConfigEditData instance
     */
    public static ConfigEditData create() {
        return new ConfigEditData(new HashMap<>());
    }

    /**
     * Creates and returns a new root section with all values from this ConfigEditData.
     *
     * @return A new ConfigEditSection containing all configuration values
     */
    public @NotNull ConfigEditSection getNewRootSection() {
        rootSection = ConfigEditSection.create(Component.text(""))
                .values(buildDisplayMapRecursive(values));
        return rootSection;
    }
    /**
     * Returns the existing root section, creating a new one if it doesn't exist.
     *
     * @return The existing or newly created root section
     */
    public @NotNull ConfigEditSection getRootSectionAndCreateIfAbsent() {
        if (rootSection == null) {
            rootSection = getNewRootSection();
        }
        return rootSection;
    }
    /**
     * Returns the current root section, or null if none exists.
     *
     * @return The current root section, or null if not set
     */
    public @Nullable ConfigEditSection getRootSection() {
        return rootSection;
    }


    private Map<String, ConfigEditValue<?>> buildDisplayMapRecursive(Map<String, Object> map) {
        Map<String, ConfigEditValue<?>> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            result.put(entry.getKey(), buildValueTree(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    private ConfigEditValue<?> buildValueTree(String keyOrIndex, Object value) {
        Component label = Component.text(keyOrIndex);

        if (isMapOfStringObject(value)) {
            Map<String, Object> map = (Map<String, Object>) value;

            Map<String, ConfigEditValue<?>> childValues = new HashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                childValues.put(entry.getKey(), buildValueTree(entry.getKey(), entry.getValue()).pathName(entry.getKey()));
            }

            return ConfigEditSection.create(label).values(childValues);

        } else if (isObjectList(value)) {
            List<Object> list = (List<Object>) value;

            List<ConfigEditValue<?>> childValues = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                childValues.add(buildValueTree(String.valueOf(i), list.get(i)).pathName(i + ""));
            }

            return ConfigEditListSection.create(label).values(childValues);

        } else {
            return ConfigEditGenericValue.fromValue(value);
        }
    }

    /**
     * Finds the minimal valid path that points to a valid section (Map or List).
     *
     * @param path The path to check
     * @return The longest valid path that points to a section, or empty string if none found
     */
    public String getMinimalValidPath(String path) {
        String currentPath = path;

        while (!currentPath.isEmpty()) {
            Object currentSection = getSectionAny(currentPath);
            if (currentSection instanceof Map || currentSection instanceof List) {
                return currentPath;
            }

            // Remove last segment
            int lastDotIndex = currentPath.lastIndexOf('.');
            if (lastDotIndex == -1) {
                currentPath = "";
            } else {
                currentPath = currentPath.substring(0, lastDotIndex);
            }
        }
        return "";
    }

    /**
     * Gets a configuration section as a Map.
     *
     * @param path The path to the section (dot notation)
     * @return The section as a Map, or null if the path doesn't point to a valid section
     */
    public Map<String, Object> getSection(String path) {
        if (path.isEmpty()) {
            return values;
        }

        String[] parts = path.split("\\.");
        Object current = values;

        for (String part : parts) {
            current = getChild(current, part);
            if (current == null) return null;
        }

        // Only return if the result is a Map (i.e., a section)
        return (current instanceof Map) ? (Map<String, Object>) current : null;
    }

    /**
     * Gets any configuration value at the given path, regardless of type.
     *
     * @param path The path to the value (dot notation)
     * @return The value at the path, or null if not found
     */
    public Object getSectionAny(String path) {
        if (path.isEmpty()) {
            return values;
        }

        String[] parts = path.split("\\.");
        Object current = values;

        for (String part : parts) {
            current = getChild(current, part);
            if (current == null) return null;
        }

        return current; // Could be Map, List, or other
    }



    private Object getChild(Object parent, String key) {
        if (parent instanceof Map map) {
            return map.get(key); // Return anything — let caller decide if it's valid
        } else if (parent instanceof List list) {
            try {
                int index = Integer.parseInt(key);
                if (index >= 0 && index < list.size()) {
                    return list.get(index); // Return even non-Map/List
                }
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Gets a value from the configuration.
     *
     * @param path The path to the value (dot notation)
     * @return The value, or null if not found or path is invalid
     */
    public @Nullable Object getValue(String path) {
        if (path.isEmpty()) {
            return values;
        }

        String[] parts = path.split("\\.");
        Object current = values;

        for (int i = 0; i < parts.length - 1; i++) {
            if (isMapOfStringObject(current)) {
                Map<String, Object> map = (Map<String, Object>) current;
                current = map.get(parts[i]);
            } else if (isObjectList(current)) {
                List<Object> list = (List<Object>) current;
                try {
                    int index = Integer.parseInt(parts[i]);
                    if (index >= 0 && index < list.size()) {
                        current = list.get(index);
                    } else {
                        return null;
                    }
                } catch (NumberFormatException e) {
                    return null;
                }
            } else {
                return null;
            }
        }

        // Final step: handle last part
        String last = parts[parts.length - 1];

        if (isMapOfStringObject(current)) {
            return ((Map<String, Object>) current).get(last);
        } else if (isObjectList(current)) {
            try {
                int index = Integer.parseInt(last);
                List<Object> list = (List<Object>) current;
                if (index >= 0 && index < list.size()) {
                    return list.get(index);
                } else {
                    return null;
                }
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }



    /**
     * Associates the specified value with the specified key in this configuration.
     *
     * @param key The key with which the specified value is to be associated
     * @param value The value to be associated with the specified key
     */
    public void putValue(String key, Object value) {
        values.put(key, value);
    }

    /**
     * Sets a value at the specified path in the configuration.
     * Supports nested paths using dot notation.
     *
     * @param path The path to set (dot notation)
     * @param value The value to set
     */
    public void setValue(String path, Object value) {
        String[] parts = path.split("\\.");
        Object current = values;

        for (int i = 0; i < parts.length - 1; i++) {
            current = getChild(current, parts[i]);
            if (current == null) return; // Invalid path
        }

        String finalKey = parts[parts.length - 1];
        if (current instanceof Map map) {
            map.put(finalKey, value);
        } else if (current instanceof List list) {
            try {
                int index = Integer.parseInt(finalKey);
                if (index >= 0 && index < list.size()) {
                    list.set(index, value);
                } else if (index == list.size()) {
                    list.add(value); // Optionally allow appending
                }
            } catch (NumberFormatException e) {
                return; // Invalid index
            }
        } else {
            return; // Unsupported container
        }

        setterCallback.accept(this);
    }


    /**
     * Gets a value from the configuration, returning a default value if not found.
     *
     * @param path The path to the value (dot notation)
     * @param defaultValue The default value to return if the path doesn't exist
     * @return The value at the path, or the default value if not found
     */
    public @Nullable Object getValueOrDefault(String path, Object defaultValue) {
        Object value = getValue(path);
        return value != null ? value : defaultValue;
    }

    private boolean isMapOfStringObject(Object obj) {
        if (!(obj instanceof Map<?, ?> map)) {
            return false;
        }

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!(entry.getKey() instanceof String)) {
                return false;
            }
        }

        return true;
    }
    private boolean isObjectList(Object obj) {
        return obj instanceof List<?> list;
    }

    /**
     * Creates a deep copy of this ConfigEditData instance.
     *
     * @return A new ConfigEditData instance with the same values
     */
    @Override
    public ConfigEditData clone() {
        ConfigEditData clone = create();
        clone.values = new HashMap<>(this.values);
        clone.displayMap = new HashMap<>(this.displayMap);
        clone.setterCallback = this.setterCallback;
        clone.rootSection = this.rootSection;
        return clone;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> makeMutable(Map<String, Object> input) {
        Map<String, Object> mutable = new HashMap<>();

        for (Map.Entry<String, Object> entry : input.entrySet()) {
            Object value = entry.getValue();

            if (value instanceof Map<?, ?> subMap && isMapOfStringObject(subMap)) {
                mutable.put(entry.getKey(), makeMutable((Map<String, Object>) subMap));
            } else if (value instanceof List<?> list) {
                mutable.put(entry.getKey(), makeMutableList(list));
            } else {
                mutable.put(entry.getKey(), value);
            }
        }

        return mutable;
    }

    @SuppressWarnings("unchecked")
    private static List<Object> makeMutableList(List<?> input) {
        List<Object> result = new ArrayList<>();
        for (Object item : input) {
            if (item instanceof Map<?, ?> subMap && isMapOfStringObject(subMap)) {
                result.add(makeMutable((Map<String, Object>) subMap));
            } else if (item instanceof List<?> subList) {
                result.add(makeMutableList(subList));
            } else {
                result.add(item);
            }
        }
        return result;
    }

    private static boolean isMapOfStringObject(Map<?, ?> map) {
        for (Map.Entry<?, ?> e : map.entrySet()) {
            if (!(e.getKey() instanceof String)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Applies the contents of a Map to a ConfigurationSection.
     * This is a convenience method that calls applyMapToSection with an empty serializers map.
     *
     * @param map The map containing the values to apply
     * @param section The ConfigurationSection to modify
     * @return The modified ConfigurationSection
     */
    public static ConfigurationSection applyMapToSection(Map<String, Object> map, ConfigurationSection section) {
        return applyMapToSection(map, section, Map.of());
    }

    /**
     * Recursively applies the contents of a Map<String, Object> to a Bukkit ConfigurationSection.
     * Handles nested maps and lists, and applies custom serializers where defined.
     *
     * @param map The map to apply to the ConfigurationSection.
     * @param section The Bukkit ConfigurationSection to modify.
     * @param serializers Map from Class to serializer function for custom object types.
     * @return The ConfigurationSection with the map data applied.
     * @throws IllegalArgumentException If a map contains non-string keys.
     */
    public static ConfigurationSection applyMapToSection(
            Map<String, Object> map,
            ConfigurationSection section,
            Map<Class<?>, Function<Object, Object>> serializers
    ) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map<?, ?> subMap) {
                // Validate keys are strings
                boolean validKeys = subMap.keySet().stream().allMatch(k -> k instanceof String);
                if (!validKeys) {
                    throw new IllegalArgumentException("Map keys must be Strings: key=" + key);
                }
                ConfigurationSection subSection = section.createSection(key);
                // Recursive call on map
                applyMapToSection((Map<String, Object>) subMap, subSection, serializers);

            } else if (value instanceof List<?> list) {
                // Recursively handle lists, serialize elements if needed
                List<Object> serializedList = new ArrayList<>();
                for (Object element : list) {
                    serializedList.add(serializeValue(element, serializers));
                }
                section.set(key, serializedList);

            } else {
                // Apply serializer if present
                Object serializedValue = serializeValue(value, serializers);
                section.set(key, serializedValue);
            }
        }
        return section;
    }

    /**
     * Helper method to serialize a single value using the serializers map if applicable.
     * Recursively handles nested Maps and Lists.
     */
    private static Object serializeValue(Object value, Map<Class<?>, Function<Object, Object>> serializers) {
        if (value instanceof Map<?, ?> subMap) {
            // Recursively serialize map to a new LinkedHashMap
            Map<String, Object> serializedMap = new LinkedHashMap<>();
            for (Map.Entry<?, ?> e : subMap.entrySet()) {
                if (!(e.getKey() instanceof String)) {
                    throw new IllegalArgumentException("Map keys must be Strings");
                }
                serializedMap.put((String) e.getKey(), serializeValue(e.getValue(), serializers));
            }
            return serializedMap;

        } else if (value instanceof List<?> list) {
            // Recursively serialize each element in the list
            List<Object> serializedList = new ArrayList<>();
            for (Object element : list) {
                serializedList.add(serializeValue(element, serializers));
            }
            return serializedList;

        } else {
            // Use a custom serializer if one is registered for the exact class (not subclasses)
            Function<Object, Object> serializer = serializers.get(value.getClass());
            if (serializer != null) {
                return serializer.apply(value);
            }
            return value;
        }
    }


    /**
     * Converts a ConfigurationSection to a Map.
     * Note: The resulting map is not ordered like in the original file.
     * For ordered maps, use create() with your own Map implementation.
     *
     * @param section The ConfigurationSection to convert
     * @return A Map representing the ConfigurationSection's contents
     */
    public static Map<String, Object> configSectionToMap(ConfigurationSection section) {
        Map<String, Object> result = new LinkedHashMap<>();

        for (String key : section.getKeys(false)) {
            Object value = section.get(key);

            if (value instanceof ConfigurationSection subSection) {
                result.put(key, configSectionToMap(subSection)); // Recurse for subsections
            } else {
                result.put(key, value);
            }
        }

        return result;
    }

    /**
     * Parses a String representation of a location into a Bukkit Location object.
     * Expected format: "[world] x y z [pitch yaw]" where 'world' is optional.
     *
     * @param string The location string to parse.
     * @return A Location object if parsing succeeds; null otherwise.
     */
    public static @Nullable Location deserializeLocation(String string) {
        Location loc = new Location(null, 0, 0, 0, 0 ,0);
        final String regex = "^(?:(?<world>[^\\s]+)\\s+)?(?<x>-?\\d*\\.?\\d+)\\s+(?<y>-?\\d*\\.?\\d+)\\s+(?<z>-?\\d*\\.?\\d+)(?:(\\s+(?<pitch>-?\\d*\\.?\\d+)\\s+(?<yaw>-?\\d*\\.?\\d+))?)?\\s*$";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(string);

        if (!matcher.find()) { // return null if no location was found
            return null;
        }

        if (matcher.group("world") != null) {
            loc.setWorld(Bukkit.getWorld(matcher.group("world")));
        }

        if (matcher.group("x") == null || matcher.group("y") == null || matcher.group("z") == null) { // return null if no coordinates
            return null;
        }
        loc.setX(Double.parseDouble(matcher.group("x")));
        loc.setY(Double.parseDouble(matcher.group("y")));
        loc.setZ(Double.parseDouble(matcher.group("z")));

        if (matcher.group("pitch") != null && matcher.group("yaw") != null) {
            loc.setPitch(Float.parseFloat(matcher.group("pitch")));
            loc.setYaw(Float.parseFloat(matcher.group("yaw")));
        }

        return loc;
    }

    public ConfigEditData setDeepRegexDisplayData(
            String regexPath,
            BiFunction<String, ConfigEditValue<?>, ConfigEditValue<?>> modifier
    ) {
        // Ensure we have a built rootSection
        if (rootSection == null) {
            getRootSectionAndCreateIfAbsent();
        }
        // Kick off the recursive walk from the root, with an empty starting path
        walkAndModify(rootSection, "", regexPath, modifier);
        return this;
    }

    @SuppressWarnings("unchecked")
    private void walkAndModify(
            ConfigEditValue<?> current,
            String currentPath,
            String regex,
            BiFunction<String, ConfigEditValue<?>, ConfigEditValue<?>> modifier
    ) {
        // If this is a section (map)
        if (current instanceof ConfigEditSection section) {
            for (Map.Entry<String, ConfigEditValue<?>> entry : section.values().entrySet()) {
                String key = entry.getKey();
                String fullPath = currentPath.isEmpty() ? key : currentPath + "." + key;
                ConfigEditValue<?> child = entry.getValue();

                // Recurse into children first (post-order)
                walkAndModify(child, fullPath, regex, modifier);

                // Then if this path matches, replace the child entry
                if (fullPath.matches(regex)) {
                    section.values().put(key, modifier.apply(fullPath, child));
                }
            }

            // If this is a list section
        } else if (current instanceof ConfigEditListSection listSection) {
            List<ConfigEditValue<?>> list = listSection.valuesList();
            for (int i = 0; i < list.size(); i++) {
                String fullPath = currentPath.isEmpty() ? String.valueOf(i) : currentPath + "." + i;
                ConfigEditValue<?> child = list.get(i);

                // Recurse into each element
                walkAndModify(child, fullPath, regex, modifier);

                // Replace elements whose path matches
                if (fullPath.matches(regex)) {
                    list.set(i, modifier.apply(fullPath, child));
                }
            }

            // Otherwise it’s a leaf (generic value); its parent handles replacement
        } else {
            // nothing to recurse into for leaves
        }
    }
    /**
     * Recursively traverses nested maps and lists, applying a transformer to entries
     * whose path matches the regex. Supports mutation inside both maps and lists.
     *
     * @param input The input nested map to copy and modify
     * @param regexPath The regex pattern to match full paths against
     * @param transformer Function to transform matched entries: (path, oldValue) -> newValue
     * @return A deep-copied map with transformations applied
     */
    public static Map<String, Object> setDeepRegexPath(Map<String, Object> input, String regexPath, BiFunction<String, Object, Object> transformer) {
        Map<String, Object> mutable = deepCopyMutable(input);
        // For the root map, parent is null, key is null, currentValue is mutable itself
        applyDeepRegexPath(null, null, mutable, "", regexPath, transformer);
        return mutable;
    }

    /**
     * Recursive helper that updates the value in the parent container at keyOrIndex if regex matches.
     * Warning: In big Maps this can be expensive. Create your own maps if performance is a concern.
     * @param parent Parent container (Map or List), or null if at root
     * @param keyOrIndex Key (String) if parent is Map, or index (Integer) if parent is List, or null if at root
     * @param currentValue The current value being inspected
     * @param currentPath The full path string for currentValue
     * @param regex The regex pattern for matching paths
     * @param transformer Function to transform matching values
     */
    @SuppressWarnings("unchecked")
    private static void applyDeepRegexPath(Object parent, Object keyOrIndex, Object currentValue, String currentPath, String regex, BiFunction<String, Object, Object> transformer) {
        if (currentValue instanceof Map<?, ?> map) {
            Map<String, Object> castMap = (Map<String, Object>) map;
            for (Map.Entry<String, Object> entry : castMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                String fullPath = currentPath.isEmpty() ? key : currentPath + "." + key;
                applyDeepRegexPath(castMap, key, value, fullPath, regex, transformer);
            }
        } else if (currentValue instanceof List<?> list) {
            List<Object> castList = (List<Object>) list;
            for (int i = 0; i < castList.size(); i++) {
                Object value = castList.get(i);
                String fullPath = currentPath.isEmpty() ? Integer.toString(i) : currentPath + "." + i;
                applyDeepRegexPath(castList, i, value, fullPath, regex, transformer);
            }
        } else {
            // Leaf node - check regex and replace value in parent container if matched
            if (currentPath.matches(regex) && parent != null && keyOrIndex != null) {
                Object newValue = transformer.apply(currentPath, currentValue);
                if (parent instanceof Map<?, ?> mapParent && keyOrIndex instanceof String keyStr) {
                    ((Map<String, Object>) mapParent).put(keyStr, newValue);
                } else if (parent instanceof List<?> listParent && keyOrIndex instanceof Integer idx) {
                    ((List<Object>) listParent).set(idx, newValue);
                }
            }
        }
    }

    public static ConfigurationSection applyMapToSectionWithRegexTransformers(
            Map<String, Object> map,
            ConfigurationSection section,
            Map<String, BiFunction<String, Object, Object>> pathTransformers
    ) {
        applyMapRecursive(map, section, pathTransformers, "");
        return section;
    }

    @SuppressWarnings("unchecked")
    private static void applyMapRecursive(
            Object currentValue,
            ConfigurationSection currentSection,
            Map<String, BiFunction<String, Object, Object>> pathTransformers,
            String currentPath
    ) {
        if (currentValue instanceof Map<?, ?> map) {
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) map).entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                String fullPath = currentPath.isEmpty() ? key : currentPath + "." + key;

                if (value instanceof Map<?, ?> subMap) {
                    boolean validKeys = subMap.keySet().stream().allMatch(k -> k instanceof String);
                    if (!validKeys) {
                        throw new IllegalArgumentException("Map keys must be Strings: path=" + fullPath);
                    }
                    ConfigurationSection childSection = currentSection.createSection(key);
                    applyMapRecursive(subMap, childSection, pathTransformers, fullPath);

                } else if (value instanceof List<?> list) {
                    List<Object> processedList = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        Object element = list.get(i);
                        String elementPath = fullPath + "." + i;

                        Object transformed = applyMatchingTransformer(elementPath, element, pathTransformers);
                        processedList.add(transformed);
                    }
                    currentSection.set(key, processedList);

                } else {
                    Object transformed = applyMatchingTransformer(fullPath, value, pathTransformers);
                    currentSection.set(key, transformed);
                }
            }
        }
    }

    private static Object applyMatchingTransformer(String path, Object value, Map<String, BiFunction<String, Object, Object>> transformers) {
        for (Map.Entry<String, BiFunction<String, Object, Object>> entry : transformers.entrySet()) {
            if (path.matches(entry.getKey())) {
                return entry.getValue().apply(path, value);
            }
        }
        return value;
    }

    /**
     * Creates a deep mutable copy of the given map. Nested maps and lists are also copied to mutable equivalents.
     *
     * @param source The map to copy.
     * @return A new mutable deep copy of the source map.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> deepCopyMutable(Map<String, Object> source) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            Object val = entry.getValue();
            if (val instanceof Map) {
                result.put(entry.getKey(), deepCopyMutable((Map<String, Object>) val));
            } else if (val instanceof List) {
                result.put(entry.getKey(), new ArrayList<>((List<?>) val));
            } else {
                result.put(entry.getKey(), val);
            }
        }
        return result;
    }

    /**
     * Merges the configuration from the file, that was overridden by commands, file editor, etc. while prioritizing the values from the file
     * @param fileConfig
     * @param values
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> mergeModifiedConfig(ConfigurationSection fileConfig, Map<String, Object> values) {
        Map<String, Object> out = new HashMap<>();

        for (String key : fileConfig.getKeys(false)) {
            Object fileVal = fileConfig.get(key);
            Object currentVal = values.get(key);

            // If it's a nested section
            if (fileConfig.isConfigurationSection(key)) {
                ConfigurationSection subSection = fileConfig.getConfigurationSection(key);
                if (currentVal instanceof Map) {
                    out.put(key, mergeModifiedConfig(subSection, (Map<String, Object>) currentVal));
                } else {
                    out.put(key, configSectionToMap(subSection));
                }
            }

            // If it's a list
            else if (fileConfig.isList(key)) {
                List<Object> mergedList = new ArrayList<>();
                List<?> fileList = fileConfig.getList(key);
                List<?> valueList = (currentVal instanceof List) ? (List<?>) currentVal : List.of();

                for (int i = 0; i < fileList.size(); i++) {
                    Object fileItem = fileList.get(i);
                    Object valueItem = i < valueList.size() ? valueList.get(i) : null;

                    if (fileItem instanceof ConfigurationSection) {
                        // This should not happen — sections in lists are usually not supported by Bukkit
                        mergedList.add(configSectionToMap((ConfigurationSection) fileItem));
                    } else if (fileItem instanceof Map && valueItem instanceof Map) {
                        // Recursively merge maps in list
                        mergedList.add(mergeMap((Map<String, Object>) fileItem, (Map<String, Object>) valueItem));
                    } else {
                        // Fallback to values from `values`, otherwise use file's value
                        mergedList.add(valueItem != null ? valueItem : fileItem);
                    }
                }
                out.put(key, mergedList);
            }

            // For regular values
            else {
                out.put(key, currentVal != null ? currentVal : fileVal);
            }
        }

        return out;
    }


    @SuppressWarnings("unchecked")
    private static Map<String, Object> mergeMap(Map<String, Object> fileMap, Map<String, Object> valueMap) {
        Map<String, Object> merged = new HashMap<>(fileMap);
        for (String key : valueMap.keySet()) {
            Object val = valueMap.get(key);
            Object base = fileMap.get(key);
            if (val instanceof Map && base instanceof Map) {
                merged.put(key, mergeMap((Map<String, Object>) base, (Map<String, Object>) val));
            } else {
                merged.put(key, val);
            }
        }
        return merged;
    }

    public static final class Serializers {
        private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        private static final DecimalFormat locationFormatter = new DecimalFormat("0.###", symbols);

        public static final Function<Object, Object> TO_STRING = Object::toString;
        public static final Function<Object, Object> WORLD_NAME = obj ->
                obj instanceof World world ? world.getName() : obj;

        public static final Function<Object, Object> LOCATION_POSITION = obj ->
                obj instanceof Location location ?
                        locationFormatter.format(location.getX()) + " " +
                                locationFormatter.format(location.getY()) + " " +
                                locationFormatter.format(location.getZ())
                        : obj;

        public static final Function<Object, Object> LOCATION_POSITION_AND_ROTATION = obj ->
                obj instanceof Location location ?
                        locationFormatter.format(location.getX()) + " " +
                                locationFormatter.format(location.getY()) + " " +
                                locationFormatter.format(location.getZ()) + " " +
                                locationFormatter.format(location.getPitch()) + " " +
                                locationFormatter.format(location.getYaw())
                        : obj;

        public static final Function<Object, Object> LOCATION_FULL = obj -> {
                if (obj instanceof Location location) {
                        String worldText = location.getWorld() == null ? "" : location.getWorld().getName() + " ";
                        return worldText +
                                locationFormatter.format(location.getX()) + " " +
                                locationFormatter.format(location.getY()) + " " +
                                locationFormatter.format(location.getZ()) + " " +
                                locationFormatter.format(location.getPitch()) + " " +
                                locationFormatter.format(location.getYaw());
                }
                return obj;
        };
        public static final Function<Object, Object> LOCATION_WORLD = obj -> {
            if (obj instanceof Location location) {
                String worldText = location.getWorld() == null ? "" : location.getWorld().getName() + " ";
                return worldText +
                        locationFormatter.format(location.getX()) + " " +
                        locationFormatter.format(location.getY()) + " " +
                        locationFormatter.format(location.getZ()) + " ";
            }
            return obj;
        };
    }
    public static final class DataCreationPrefabs {
        public static final Map<String, Object> ALL = makeMutable(Map.ofEntries(
                Map.entry("String",""),
                Map.entry("Integer", 0),
                Map.entry("Double", 0d),
                Map.entry("Float", 0f),
                Map.entry("Boolean", false),
                Map.entry("List", new ArrayList<>()),
                Map.entry("Section", new HashMap<>())
        ));
        public static final Map<String, Object> NO_SECTION = makeMutable(Map.ofEntries(
                Map.entry("String",""),
                Map.entry("Integer", 0),
                Map.entry("Double", 0d),
                Map.entry("Float", 0f),
                Map.entry("Boolean", false),
                Map.entry("List", new ArrayList<>())
        ));
        public static final Map<String, Object> MINIMAL = makeMutable(Map.ofEntries(
                Map.entry("String",""),
                Map.entry("Integer", 0),
                Map.entry("Double", 0d),
                Map.entry("Boolean", false)
        ));
        public static final Map<String, Object> NUMBERS = makeMutable(Map.ofEntries(
                Map.entry("Integer", 0),
                Map.entry("Double", 0d),
                Map.entry("Float", 0f)
        ));
    }
}
