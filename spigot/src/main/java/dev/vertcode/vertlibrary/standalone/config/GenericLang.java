/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.standalone.config;

import dev.vertcode.vertlibrary.chat.ChatUtils;
import dev.vertcode.vertlibrary.config.EnumConfig;

import java.util.List;

public enum GenericLang implements EnumConfig {

    NO_PERMISSIONS("&c&lVert &8» &cYou do not have permissions to use this."),
    MUST_BE_PLAYER("&c&lVert &8» &cYou must be a player to use this."),

    COMMAND$SOMETHING_WENT_WRONG("&c&lVert &8» &cSomething went wrong while running this command," +
            " please tell a admin to look at the error in the console."),

    VERTLIBRARY$GUI$CANNOT_DISABLE_VERTLIBRARY("&c&lVert &8» &cYou cannot disable VertLibrary!"),
    VERTLIBRARY$GUI$ERRORED_NEED_RESTART("&c&lVert &8» &cYou cannot enable a plugin that has errored, this requires a restart.");

    private Object value;

    GenericLang(Object value) {
        this.value = value;
    }

    @Override
    public boolean getBoolean() {
        return (boolean) value;
    }

    @Override
    public String getString() {
        return ChatUtils.getInstance().colorize((String) value);
    }

    @Override
    public Integer getInteger() {
        return (Integer) value;
    }

    @Override
    public Double getDouble() {
        return (Double) value;
    }

    @Override
    public Float getFloat() {
        return (Float) value;
    }

    @Override
    public List<Object> getList() {
        return (List<Object>) value;
    }

    @Override
    public List<String> getStringList() {
        return ChatUtils.getInstance().colorize((List<String>) value);
    }

    @Override
    public Object get() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String getPath() {
        return ChatUtils.getInstance().replacePlaceholders(this.name().toLowerCase(), "$", ".", "_", "-");
    }
}
