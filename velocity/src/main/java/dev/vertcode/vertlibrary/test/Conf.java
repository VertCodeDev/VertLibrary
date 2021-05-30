package dev.vertcode.vertlibrary.test;

import dev.vertcode.vertlibrary.chat.ChatUtils;
import dev.vertcode.vertlibrary.config.EnumConfig;

import java.util.List;

public enum Conf implements EnumConfig {

    TEST("TEST"),

    ;

    private Object value;

    Conf(Object value) {
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
        return ChatUtils.getInstance().replacePlaceholdersString(this.name().toLowerCase(), "$", ".", "_", "-");
    }
}
