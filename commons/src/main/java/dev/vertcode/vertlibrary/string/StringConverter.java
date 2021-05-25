package dev.vertcode.vertlibrary.string;

import java.util.Optional;

/**
 * This class has a few methods that will convert a string into a certain type,
 * if the string cannot be converted it will return a empty {@link Optional}
 */
public class StringConverter {

    private static StringConverter instance;

    /**
     * @return The instance of this class
     */
    public static StringConverter getInstance() {
        if (instance == null) instance = new StringConverter();
        return instance;
    }

    /**
     * Converts a string into a {@link Integer} in a optional, which will be empty if the string isn't a {@link Integer}
     *
     * @param str The string you want to convert to a {@link Integer}
     * @return The {@link Optional} containing the {@link Integer}
     */
    public Optional<Integer> toInteger(String str) {
        try {
            return Optional.of(Integer.parseInt(str));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    /**
     * Converts a string into a {@link Double} in a optional, which will be empty if the string isn't a {@link Double}
     *
     * @param str The string you want to convert to a {@link Double}
     * @return The {@link Optional} containing the {@link Double}
     */
    public Optional<Double> toDouble(String str) {
        try {
            return Optional.of(Double.parseDouble(str));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    /**
     * Converts a string into a {@link Long} in a optional, which will be empty if the string isn't a {@link Long}
     *
     * @param str The string you want to convert to a {@link Long}
     * @return The {@link Optional} containing the {@link Long}
     */
    public Optional<Long> toLong(String str) {
        try {
            return Optional.of(Long.parseLong(str));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    /**
     * Converts a string into a {@link Float} in a optional, which will be empty if the string isn't a {@link Float}
     *
     * @param str The string you want to convert to a {@link Float}
     * @return The {@link Optional} containing the {@link Float}
     */
    public Optional<Float> toFloat(String str) {
        try {
            return Optional.of(Float.parseFloat(str));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    /**
     * Converts a string into a {@link Byte} in a optional, which will be empty if the string isn't a {@link Byte}
     *
     * @param str The string you want to convert to a {@link Byte}
     * @return The {@link Optional} containing the {@link Byte}
     */
    public Optional<Byte> toByte(String str) {
        try {
            return Optional.of(Byte.parseByte(str));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    /**
     * Converts a string into a {@link Short} in a optional, which will be empty if the string isn't a {@link Short}
     *
     * @param str The string you want to convert to a {@link Short}
     * @return The {@link Optional} containing the {@link Short}
     */
    public Optional<Short> toShort(String str) {
        try {
            return Optional.of(Short.parseShort(str));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
