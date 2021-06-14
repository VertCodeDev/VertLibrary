/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.nbt;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;

public enum NBTBaseType {

    BYTE(byte.class, "Byte"),
    BYTE_ARRAY(byte[].class, "ByteArray"),
    DOUBLE(double.class, "Double"),
    FLOAT(float.class, "Float"),
    INT(int.class, "Int"),
    INT_ARRAY(int[].class, "IntArray"),
    LONG(long.class, "Long"),
    SHORT(short.class, "Short"),
    STRING(String.class, "String");

    private Class<?> innerClazz;
    private Class<?> nbtBaseClass;

    <T> NBTBaseType(Class<T> innerClazz, String name) {
        try {
            this.innerClazz = innerClazz;
            String version = "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            this.nbtBaseClass = Class.forName(version + ".NBTTag" + name);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static NBTBaseType get(Class<?> clazz) {
        for (NBTBaseType type : values())
            if (type.innerClazz.equals(clazz))
                return type;

        if (clazz == Float.class) return FLOAT;
        if (clazz == Integer.class) return INT;
        return null;
    }

    public static NBTBaseType getByClass(Class<?> clazz) {
        for (NBTBaseType t : values())
            if (t.innerClazz.equals(clazz))
                return t;
        return null;
    }

    public static NBTBaseType getByNBTBaseClass(Class<?> clazz) {
        for (NBTBaseType t : values())
            if (t.nbtBaseClass.equals(clazz))
                return t;
        return null;
    }

    public static NBTBaseType getFromObject(@NotNull Object o) {
        NBTBaseType t = getByClass(o.getClass());
        if (t != null)
            return t;

        try {
            return getByClass((Class) o.getClass().getField("TYPE").get(null));
        } catch (Exception ignore) {
        }
        return null;
    }

    public <T> Object make(T value) {
        try {
            Constructor m = nbtBaseClass.getConstructor(innerClazz);
            m.setAccessible(true);
            Object o = m.newInstance(value);
            m.setAccessible(false);

            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}