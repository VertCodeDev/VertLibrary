


/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.config;

import java.util.List;

public interface EnumConfig {

    /**
     * Returns the object loaded from the config as a boolean
     *
     * @return the value as a boolean
     */
    boolean getBoolean();

    /**
     * Returns the object loaded from the config as a {@link String}
     *
     * @return the value as a string
     */
    String getString();

    /**
     * Returns the object loaded from the config as a {@link Integer}
     *
     * @return the value as a integer
     */
    Integer getInteger();

    /**
     * Returns the object loaded from the config as a {@link Double}
     *
     * @return the value as a double
     */
    Double getDouble();

    /**
     * Returns the object loaded from the config as a {@link Float}
     *
     * @return the value as a float
     */
    Float getFloat();

    /**
     * Returns the object loaded from the config as a {@link List}
     *
     * @return the value as a list
     */
    List<Object> getList();

    /**
     * Returns the object loaded from the config as a {@link List}
     *
     * @return the value as a list
     */
    List<String> getStringList();

    /**
     * Returns the object loaded from the config as a {@link Object}
     *
     * @return the value as a object
     */
    Object get();

    /**
     * Set a config option to a other value.
     *
     * @param value the value you want to set the config option to
     */
    void setValue(Object value);

    /**
     * This should be the path where it should be located in the config.
     *
     * @return the path where the option should be in the config
     */
    String getPath();
}