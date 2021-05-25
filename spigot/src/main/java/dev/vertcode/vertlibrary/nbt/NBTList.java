


/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.nbt;

import dev.vertcode.vertlibrary.VertLibrary;
import org.bukkit.Bukkit;

import java.lang.reflect.Method;
import java.util.AbstractList;

public class NBTList {

    private static final String version = "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static final String cbVersion = "org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static Class<?> tagListClass;
    private static Class<?> nbtBaseClass;

    static {
        try {
            tagListClass = Class.forName(version + ".NBTTagList");
            nbtBaseClass = Class.forName(version + ".NBTBase");
        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(VertLibrary.getInstance());
        }
    }

    private final Object tagList;

    public NBTList() {
        this(null);
    }

    public NBTList(Object tagCompound) {
        Object toSet = tagCompound;
        if (tagCompound == null) {
            try {
                toSet = tagListClass.newInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        this.tagList = toSet;
    }

    public Object getTagList() {
        return tagList;
    }

    /**
     * Checks if the NBTList is empty or not.
     *
     * @return true | false
     */
    public boolean isEmpty() {
        try {
            Method m = tagListClass.getMethod("isEmpty");
            m.setAccessible(true);
            Object r = m.invoke(this.tagList);
            m.setAccessible(false);
            return r instanceof Boolean ? (Boolean) r : true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }

    /**
     * Add a compound to the NBTlist
     *
     * @param value the {@link NBTEditor} you want to add the compound from
     */
    public void add(NBTEditor value) {
        add(value.getTagCompound());
    }

    /**
     * Add a {@link NBTBaseType} type to the {@link NBTList}
     *
     * @param type  the {@link NBTBaseType} you want to add
     * @param value the object you want to add
     * @param <T>   the object type parameter
     */
    public <T> void add(NBTBaseType type, T value) {
        add(type.make(value));
    }

    /**
     * Add a {@link NBTBaseType} type to the {@link NBTList}
     *
     * @param type   the {@link NBTBaseType} you want to add
     * @param values the objects you want to add
     * @param <T>    the object type parameter
     */
    public <T> void add(NBTBaseType type, T... values) {
        for (T value : values) {
            add(type, value);
        }
    }

    /**
     * Adds a object to the {@link NBTList}
     *
     * @param value the value you want to add
     * @param <T>   the object type parameter
     */
    public <T> void addGeneric(T value) {
        if (value == null) {
            return;
        }
        NBTBaseType type = NBTBaseType.get(value.getClass());
        if (type == null) {
            return;
        }
        add(type, value);
    }

    /**
     * Adds objects to the {@link NBTList}
     *
     * @param values the values you want to add
     * @param <T>    the object type parameter
     */
    public <T> void add(T... values) {
        NBTBaseType type = values.length > 0 ? NBTBaseType.getByClass(values[0].getClass()) : null;
        if (type != null) {
            add(type, values);
        }
    }

    /**
     * Adds a object to the {@link NBTList}
     *
     * @param nbt the value you want to add
     */
    private void add(Object nbt) {
        try {
            Method m = AbstractList.class.getMethod("add", Object.class);
            m.setAccessible(true);
            m.invoke(tagList, nbt);
            m.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}