


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

public enum LibraryConf implements EnumConfig {

    LAG_CATCHER$ENABLED(false);

    private Object value;

    LibraryConf(Object value) {
        this.value = value;
    }

    @Override
    public boolean getBoolean() {
        return (boolean) value;
    }

    @Override
    public String getString() {
        return (String) value;
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
        return (List<String>) value;
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
