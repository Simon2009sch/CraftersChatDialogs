package me.simoncrafter.CraftersChatDialogs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.ArrayList;
import java.util.List;

public final class ComponentUtils {

    private ComponentUtils() {} // prevent instantiation

    /**
     * Cut the component at the first occurrence of the given character,
     * preserving styles and events.
     * If the char is not found, returns the original component.
     */
    public static Component cutAtChar(Component component, char splitChar) {
        int index = PlainTextComponentSerializer.plainText().serialize(component).indexOf(splitChar);
        if (index == -1) return component; // char not found

        return cutAtLength(component, index);
    }

    /**
     * Limit the component to maxLength characters,
     * preserving styles, decorations, and events.
     */
    public static Component limitLength(Component component, int maxLength) {
        return cutAtLength(component, maxLength);
    }

    /**
     * Cut the component at the first newline character,
     * preserving styles and events.
     * If no newline is found, returns the original component.
     */
    public static Component cutAtLine(Component component) {
        return cutAtChar(component, '\n');
    }

    // Core recursive cutter by character length, preserves style & events
    private static Component cutAtLength(Component component, int maxLength) {
        return cutAtLengthRecursive(component, maxLength, new int[]{0});
    }

    private static Component cutAtLengthRecursive(Component component, int maxLength, int[] currentLength) {
        if (currentLength[0] >= maxLength) return Component.empty();

        if (component instanceof TextComponent textComponent) {
            String content = textComponent.content();
            int remaining = maxLength - currentLength[0];

            String limited = content.length() <= remaining
                    ? content
                    : content.substring(0, remaining);

            currentLength[0] += limited.length();

            Builder builder = Component.text();
            builder.content(limited);
            builder.style(textComponent.style());

            List<Component> children = textComponent.children();
            for (Component child : children) {
                Component limitedChild = cutAtLengthRecursive(child, maxLength, currentLength);
                if (!limitedChild.equals(Component.empty())) {
                    builder.append(limitedChild);
                }
                if (currentLength[0] >= maxLength) break;
            }

            return builder.build();
        }

        // For non-text components, fallback to plain text truncation
        String plain = PlainTextComponentSerializer.plainText().serialize(component);
        if (plain.length() > maxLength) {
            plain = plain.substring(0, maxLength);
        }
        return Component.text(plain);
    }

    /**
     * Splits the given component into a List of Components,
     * each representing one line of the original (split on '\n'),
     * preserving styles and events.
     */
    public static List<Component> splitByLines(Component component) {
        return splitAtChar(component, '\n');
    }
    /**
     * Splits the given component by the given character,
     * similar to String#split, returning a list of Components
     * preserving styles and events.
     */
    public static List<Component> splitAtChar(Component component, char splitChar) {
        List<Component> result = new ArrayList<>();
        splitAtCharRecursive(component, splitChar, result);
        return result;
    }

    private static void splitAtCharRecursive(Component component, char splitChar, List<Component> result) {
        if (component instanceof TextComponent textComponent) {
            String content = textComponent.content();
            String[] parts = content.split(Character.toString(splitChar), -1);

            for (int i = 0; i < parts.length; i++) {
                Builder partBuilder = Component.text();
                partBuilder.content(parts[i]);
                partBuilder.style(textComponent.style());

                // Append children only on last part, otherwise split children per part is complicated
                if (i == parts.length - 1) {
                    for (Component child : textComponent.children()) {
                        partBuilder.append(child);
                    }
                }

                result.add(partBuilder.build());
            }
        } else {
            // For non-text components, fallback to plain text split
            String plain = PlainTextComponentSerializer.plainText().serialize(component);
            String[] parts = plain.split(Character.toString(splitChar), -1);
            for (String part : parts) {
                result.add(Component.text(part));
            }
        }
    }

    public static List<Component> wordWrap(Component component, int maxCharsPerLine) {
        List<Component> lines = new ArrayList<>();

        List<Component> words = splitAtChar(component, ' ');

        TextComponent.Builder currentLine = Component.text("").toBuilder();
        int currentLength = 0;

        for (Component word : words) {
            String wordText = PlainTextComponentSerializer.plainText().serialize(word);
            int wordLen = wordText.length();

            int additionalLength = (currentLength == 0 ? 0 : 1) + wordLen; // space + word

            if (wordLen > maxCharsPerLine) {
                if (currentLength > 0) {
                    lines.add(currentLine.build());
                    currentLine = Component.text("").toBuilder();
                    currentLength = 0;
                }
                lines.add(word);
                continue;
            }

            if (currentLength + additionalLength > maxCharsPerLine) {
                lines.add(currentLine.build());
                currentLine = Component.text("").toBuilder();
                currentLine.append(word);
                currentLength = wordLen;
            } else {
                if (currentLength != 0) {
                    currentLine.append(Component.text(" "));
                    currentLength += 1;
                }
                currentLine.append(word);
                currentLength += wordLen;
            }
        }

        if (currentLength > 0) {
            lines.add(currentLine.build());
        }

        return lines;
    }

    public static Component limitLengthWithEllipsis(Component component, int maxLength, TextColor dotColor) {
        String plain = PlainTextComponentSerializer.plainText().serialize(component);
        int originalLength = plain.length();

        if (originalLength <= maxLength) {
            return component;
        }

        if (maxLength <= 0) {
            return Component.empty();
        }

        if (maxLength < 3) {
            // Not enough room for full "..."
            return Component.text(".".repeat(maxLength), dotColor);
        }

        int cutLength = maxLength - 3;
        Component cutComp = cutAtLength(component, cutLength);

        Component dots = Component.text("...", dotColor);

        return Component.text("").append(cutComp).append(dots);
    }
    public static Component limitLengthWithEllipsis(Component component, int maxLength) {
        String plain = PlainTextComponentSerializer.plainText().serialize(component);
        int originalLength = plain.length();

        if (originalLength <= maxLength) {
            return component;
        }

        if (maxLength <= 0) {
            return Component.empty();
        }

        if (maxLength < 3) {
            // Not enough room for full "..."
            return Component.text(".".repeat(maxLength));
        }

        int cutLength = maxLength - 3;
        Component cutComp = cutAtLength(component, cutLength);

        Component dots = Component.text("...");

        return Component.text("").append(cutComp).append(dots);
    }


    public static Component limitLengthStartWithEllipsis(Component component, int maxLength) {
        String plain = PlainTextComponentSerializer.plainText().serialize(component);
        int originalLength = plain.length();

        if (originalLength <= maxLength) {
            return component;
        }

        if (maxLength <= 0) {
            return Component.empty();
        }

        if (maxLength < 3) {
            return Component.text(".".repeat(maxLength));
        }

        int keepLength = maxLength - 3;
        String cutPlain = plain.substring(originalLength - keepLength);

        // Rebuild a styled version of just the kept portion
        Component trimmed = cutFromEnd(component, keepLength);

        Component dots = Component.text("...");
        return Component.text().append(dots).append(trimmed).build();
    }

    public static Component limitLengthStartWithEllipsis(Component component, int maxLength, TextColor dotColor) {
        String plain = PlainTextComponentSerializer.plainText().serialize(component);
        int originalLength = plain.length();

        if (originalLength <= maxLength) {
            return component;
        }

        if (maxLength <= 0) {
            return Component.empty();
        }

        if (maxLength < 3) {
            return Component.text(".".repeat(maxLength), dotColor);
        }

        int keepLength = maxLength - 3;
        String cutPlain = plain.substring(originalLength - keepLength);

        // Rebuild a styled version of just the kept portion
        Component trimmed = cutFromEnd(component, keepLength);

        Component dots = Component.text("...", dotColor);
        return Component.text().append(dots).append(trimmed).build();
    }

    private static Component cutFromEnd(Component component, int maxLength) {
        int totalLength = PlainTextComponentSerializer.plainText().serialize(component).length();
        int startIndex = Math.max(0, totalLength - maxLength);
        return cutFromEndRecursive(component, maxLength, new int[]{0}, new int[]{startIndex});
    }

    private static Component cutFromEndRecursive(Component component, int maxLength, int[] currentLength, int[] startIndex) {
        if (currentLength[0] >= startIndex[0] + maxLength) return Component.empty();

        if (component instanceof TextComponent textComponent) {
            String content = textComponent.content();
            int contentLen = content.length();

            Builder builder = Component.text();
            builder.style(textComponent.style());

            for (int i = 0; i < contentLen; i++) {
                if (currentLength[0] >= startIndex[0] + maxLength) break;

                if (currentLength[0] >= startIndex[0]) {
                    builder.append(Component.text(String.valueOf(content.charAt(i)), textComponent.style()));
                }

                currentLength[0]++;
            }

            for (Component child : textComponent.children()) {
                Component processed = cutFromEndRecursive(child, maxLength, currentLength, startIndex);
                if (!processed.equals(Component.empty())) {
                    builder.append(processed);
                }
                if (currentLength[0] >= startIndex[0] + maxLength) break;
            }

            return builder.build();
        }

        // Fallback: plain substring without styling
        String plain = PlainTextComponentSerializer.plainText().serialize(component);
        String sliced = plain.substring(Math.max(0, plain.length() - maxLength));
        return Component.text(sliced);
    }

    public static Component limitLengthStart(Component component, int maxLength) {
        String plain = PlainTextComponentSerializer.plainText().serialize(component);
        int totalLength = plain.length();

        if (totalLength <= maxLength) {
            return component;
        }

        int startIndex = totalLength - maxLength;
        return cutFromStartAtLength(component, startIndex);
    }

    // Helper function: recursively cuts from the start to trim `startIndex` characters
    private static Component cutFromStartAtLength(Component component, int startIndex) {
        return cutFromStartRecursive(component, startIndex, new int[]{0});
    }

    private static Component cutFromStartRecursive(Component component, int startIndex, int[] skipped) {
        if (component instanceof TextComponent textComponent) {
            String content = textComponent.content();
            int length = content.length();

            String trimmed = "";
            if (skipped[0] + length <= startIndex) {
                // Entire content is skipped
                skipped[0] += length;
            } else {
                int start = Math.max(0, startIndex - skipped[0]);
                trimmed = content.substring(start);
                skipped[0] = startIndex;
            }

            TextComponent.Builder builder = Component.text()
                    .content(trimmed)
                    .style(textComponent.style());

            for (Component child : textComponent.children()) {
                Component trimmedChild = cutFromStartRecursive(child, startIndex, skipped);
                if (!trimmedChild.equals(Component.empty())) {
                    builder.append(trimmedChild);
                }
            }

            return builder.build();
        }

        // Fallback for non-text components
        String plain = PlainTextComponentSerializer.plainText().serialize(component);
        if (plain.length() > startIndex) {
            plain = plain.substring(startIndex);
        } else {
            plain = "";
        }
        return Component.text(plain);
    }

}
