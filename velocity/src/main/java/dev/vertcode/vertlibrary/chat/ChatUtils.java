/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */
package dev.vertcode.vertlibrary.chat;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
import java.util.stream.Collectors;

public class ChatUtils {

    private static ChatUtils instance;
    private static LegacyComponentSerializer legacyComponentSerializer = LegacyComponentSerializer.builder().character('&').hexCharacter('#')
            .hexColors().build();

    /**
     * Returns the instance of this class.
     *
     * @return the instance of ChatUtils
     */
    public static ChatUtils getInstance() {
        if (instance == null) instance = new ChatUtils();
        return instance;
    }

    /**
     * This method colorized a string with.
     *
     * @param str the {@link TextComponent} you want to colorize
     * @return the colorized {@link TextComponent}
     */
    public TextComponent colorize(String str) {
        return legacyComponentSerializer.deserialize(str);
    }

    /**
     * This method colorized a list with strings with the {@link #colorize(String)} method.
     *
     * @param list the {@link List} you want to colorize
     * @return the colorized {@link List}
     */
    public List<String> colorize(List<String> list) {
        return list.stream().map(string -> this.colorize(string).content()).collect(Collectors.toList());
    }

    /**
     * This method replaces placeholders with a value and colorizes the {@link String}.
     *
     * @param str          the {@link String} you want to replace the placeholders from.
     * @param placeholders the KEY, VALUE (Placeholder, Replacement)
     * @return the colorized {@link TextComponent} with placeholders replaced
     */
    public TextComponent replacePlaceholders(String str, String... placeholders) {
        if (str.isEmpty()) return colorize(str);
        if (placeholders.length % 2 != 0) throw new IllegalArgumentException("All placeholders must have a value.");

        for (int i = 0; i < placeholders.length; i += 2)
            if (str.contains(placeholders[i])) str = str.replace(placeholders[i], placeholders[i + 1]);

        return colorize(str);
    }

    /**
     * This method replaces placeholders with a value and colorizes the {@link List}.
     *
     * @param list         the {@link List} you want to replace the placeholders from.
     * @param placeholders the KEY, VALUE (Placeholder, Replacement)
     * @return the colorized {@link List} with placeholders replaced
     */
    public List<String> replacePlaceholders(List<String> list, String... placeholders) {
        return list.stream().map(s -> this.replacePlaceholders(s, placeholders).content()).collect(Collectors.toList());
    }

    /**
     * This method replaces placeholders with a value and colorizes the {@link String}.
     *
     * @param str          the {@link String} you want to replace the placeholders from.
     * @param placeholders the KEY, VALUE (Placeholder, Replacement)
     * @return the colorized {@link String} with placeholders replaced
     */
    public String replacePlaceholdersString(String str, String... placeholders) {
        if (str.isEmpty()) return str;
        if (placeholders.length % 2 != 0) throw new IllegalArgumentException("All placeholders must have a value.");

        for (int i = 0; i < placeholders.length; i += 2)
            if (str.contains(placeholders[i])) str = str.replace(placeholders[i], placeholders[i + 1]);

        return str;
    }

}
