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

package dev.vertcode.vertlibrary.nbt;

import dev.vertcode.vertlibrary.VertLibrary;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NBTEditor {

    private static final String version = "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static final String cbVersion = "org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static Class<?> tagCompoundClass;
    private static Class<?> nbtBaseClass;
    private static Class<?> nmsItemstackClass;
    private static Class<?> craftItemstackClass;

    static {
        try {
            tagCompoundClass = Class.forName(version + ".NBTTagCompound");
            nbtBaseClass = Class.forName(version + ".NBTBase");
            nmsItemstackClass = Class.forName(version + ".ItemStack");
            craftItemstackClass = Class.forName(cbVersion + ".inventory.CraftItemStack");
        } catch (Exception ex) {
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(VertLibrary.getInstance());
        }
    }

    private final Object tagCompound;

    public NBTEditor() {
        this(null);
    }

    public NBTEditor(Object tagCompound) {
        Object toSet = tagCompound;
        if (tagCompound == null) {
            try {
                toSet = tagCompoundClass.newInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        this.tagCompound = toSet;
    }

    /**
     * Receive the {@link NBTEditor} of a {@link ItemStack}
     *
     * @param item the {@link ItemStack} you want to receive the {@link NBTEditor} from
     * @return the {@link NBTEditor} from the item
     */
    public static NBTEditor get(ItemStack item) {
        try {
            Method m = craftItemstackClass.getMethod("asNMSCopy", ItemStack.class);
            m.setAccessible(true);
            Object nmsStack = m.invoke(null, item);
            m.setAccessible(false);

            Method getCompound = nmsItemstackClass.getMethod("getTag");
            getCompound.setAccessible(true);
            Object nbtCompound = getCompound.invoke(nmsStack);
            getCompound.setAccessible(false);

            return new NBTEditor(nbtCompound);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * @return The TagCompound of the item
     */
    public Object getTagCompound() {
        return tagCompound;
    }

    /**
     * Finds the {@link NBTEditor} for a compound, if not found it will return null.
     *
     * @param key the key of where the compound should be located
     * @return the {@link NBTEditor} of the compound or null
     */
    @Nullable
    public NBTEditor getCompoundNullable(String key) {
        try {
            return getCompoundThrows(key);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * Finds the {@link NBTEditor} for a compound, if not found it will return null, can throw exceptions.
     *
     * @param key the key of where the compound should be located
     * @return the {@link NBTEditor} of the compound or null
     * @throws NoSuchMethodException     can throw {@link NoSuchFieldException}
     * @throws InvocationTargetException can throw {@link InvocationTargetException}
     * @throws IllegalAccessException    can throw {@link IllegalAccessException}
     */
    public NBTEditor getCompoundThrows(String key) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method m = tagCompoundClass.getMethod("getCompound", String.class);
        m.setAccessible(true);
        Object r = m.invoke(this.tagCompound, key);
        m.setAccessible(false);
        return r == null ? null : new NBTEditor(r);
    }

    /**
     * Finds the {@link NBTEditor} for a compound, if not found it will return null.
     *
     * @param key the key of where the compound should be located
     * @return the {@link NBTEditor} of the compound or null
     */
    public NBTEditor getCompound(String key) {
        NBTEditor nbt = getCompoundNullable(key);
        return nbt == null ? null : new NBTEditor();
    }

    /**
     * Finds the {@link NBTList} by the key, if not found it will return null.
     *
     * @param key the key of where the list should be located
     * @return the {@link NBTList} or null
     */
    @Nullable
    public NBTList getListNullable(String key) {
        try {
            return getListThrows(key);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Finds the {@link NBTList} by the key, if not found it will return null.
     *
     * @param key the key of where the list should be located
     * @return the {@link NBTList} or null
     * @throws NoSuchMethodException     can throw {@link NoSuchMethodException}
     * @throws InvocationTargetException can throw {@link InvocationTargetException}
     * @throws IllegalAccessException    can throw {@link IllegalAccessException}
     */
    public NBTList getListThrows(String key) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method m = tagCompoundClass.getMethod("get", String.class);
        m.setAccessible(true);
        Object r = m.invoke(this.tagCompound, key);
        m.setAccessible(false);
        return r == null ? null : new NBTList(r);
    }

    /**
     * Put a object at the location of the key.
     *
     * @param key the location of where you want to set the object at
     * @param o   the object you want to set at the location
     */
    public void setObject(String key, Object o) {
        if (o instanceof String) {
            setString(key, (String) o);
        } else if (o instanceof Integer) {
            setInt(key, (Integer) o);
        } else if (o instanceof Double) {
            setDouble(key, (Double) o);
        } else if (o instanceof Long) {
            setLong(key, (Long) o);
        } else if (o instanceof List) {
            NBTList list = new NBTList();
            for (Object e : (List) o) {
                if (e instanceof Map) {
                    NBTEditor mapNBT = new NBTEditor();
                    for (Object k : ((Map) e).keySet()) {
                        if (k instanceof String) {
                            Object v = ((Map) e).get(k);
                            mapNBT.setObject((String) k, v);
                        }
                    }
                    list.add(mapNBT);
                } else {
                    list.addGeneric(e);
                }
            }
            set(key, list);
        }
    }

    /**
     * Returns a {@link NBTList} if not found it will return a empty {@link NBTList}
     *
     * @param key the key of where the list should be located
     * @return the {@link NBTList}
     */
    @NotNull
    public NBTList getList(String key) {
        NBTList nbt = getListNullable(key);
        return nbt != null ? nbt : new NBTList();
    }


    /**
     * Returns a {@link String} if not found it will return null.
     *
     * @param key the key of where the string should be located
     * @return the {@link String}
     */
    public String getString(String key) {
        try {
            Method m = tagCompoundClass.getMethod("getString", String.class);
            m.setAccessible(true);
            Object r = m.invoke(this.tagCompound, key);
            m.setAccessible(false);
            return r instanceof String ? (String) r : null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Put a {@link String} at the location of the key.
     *
     * @param key   the location of where you want to set the {@link String} at
     * @param value the string you want to put at the key
     */
    public void setString(String key, String value) {
        try {
            Method m = tagCompoundClass.getMethod("setString", String.class, String.class);
            m.setAccessible(true);
            m.invoke(this.tagCompound, key, value);
            m.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns a {@link Integer} if not found it will return null.
     *
     * @param key the key of where the {@link Integer} should be located
     * @return the {@link Integer}
     */
    public Integer getInt(String key) {
        try {
            Method m = tagCompoundClass.getMethod("getInt", String.class);
            m.setAccessible(true);
            Object r = m.invoke(this.tagCompound, key);
            m.setAccessible(false);
            return r instanceof Integer ? (Integer) r : null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Put a {@link Integer} at the location of the key.
     *
     * @param key   the location of where you want to set the {@link Integer} at
     * @param value the {@link Integer} you want to put at the key
     */
    public void setInt(String key, Integer value) {
        try {
            Method m = tagCompoundClass.getMethod("setInt", String.class, int.class);
            m.setAccessible(true);
            m.invoke(this.tagCompound, key, value);
            m.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns a {@link Double} if not found it will return null.
     *
     * @param key the key of where the {@link Double} should be located
     * @return the {@link Double}
     */
    public Double getDouble(String key) {
        try {
            Method m = tagCompoundClass.getMethod("getDouble", String.class);
            m.setAccessible(true);
            Object r = m.invoke(this.tagCompound, key);
            m.setAccessible(false);
            return r instanceof Double ? (Double) r : null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Put a {@link Double} at the location of the key.
     *
     * @param key   the location of where you want to set the {@link Double} at
     * @param value the {@link Double} you want to put at the key
     */
    public void setDouble(String key, Double value) {
        try {
            Method m = tagCompoundClass.getMethod("setDouble", String.class, double.class);
            m.setAccessible(true);
            m.invoke(this.tagCompound, key, value);
            m.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Put a {@link Short} at the location of the key.
     *
     * @param key   the location of where you want to set the {@link Short} at
     * @param value the {@link Short} you want to put at the key
     */
    public void setShort(String key, Short value) {
        try {
            Method m = tagCompoundClass.getMethod("setShort", String.class, short.class);
            m.setAccessible(true);
            m.invoke(this.tagCompound, key, value);
            m.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Put a {@link Boolean} at the location of the key.
     *
     * @param key   the location of where you want to set the {@link Boolean} at
     * @param value the {@link Boolean} you want to put at the key
     */
    public void setBoolean(String key, Boolean value) {
        try {
            Method m = tagCompoundClass.getMethod("setBoolean", String.class, boolean.class);
            m.setAccessible(true);
            m.invoke(this.tagCompound, key, value);
            m.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns a {@link Long} if not found it will return null.
     *
     * @param key the key of where the {@link Long} should be located
     * @return the {@link Long}
     */
    public Long getLong(String key) {
        try {
            Method m = tagCompoundClass.getMethod("getLong", String.class);
            m.setAccessible(true);
            Object r = m.invoke(this.tagCompound, key);
            m.setAccessible(false);
            return r instanceof Long ? (Long) r : null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Put a {@link Long} at the location of the key.
     *
     * @param key   the location of where you want to set the {@link Long} at
     * @param value the {@link Long} you want to put at the key
     */
    public void setLong(String key, Long value) {
        try {
            Method m = tagCompoundClass.getMethod("setLong", String.class, long.class);
            m.setAccessible(true);
            m.invoke(this.tagCompound, key, value);
            m.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Put a {@link NBTEditor} at the location of the key.
     *
     * @param key   the location of where you want to set the {@link NBTEditor} at
     * @param value the {@link NBTEditor} you want to put at the key
     */
    public void set(String key, NBTEditor value) {
        try {
            Method m = tagCompoundClass.getMethod("set", String.class, nbtBaseClass);
            m.setAccessible(true);
            m.invoke(this.tagCompound, key, value.tagCompound);
            m.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Put a {@link NBTList} at the location of the key.
     *
     * @param key   the location of where you want to set the {@link NBTList} at
     * @param value the {@link NBTList} you want to put at the key
     */
    public void set(String key, NBTList value) {
        try {
            Method m = tagCompoundClass.getMethod("set", String.class, nbtBaseClass);
            m.setAccessible(true);
            m.invoke(this.tagCompound, key, value.getTagList());
            m.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Put a {@link NBTBaseType} & object at the location of the key.
     *
     * @param key   the location of where you want to set the {@link NBTBaseType} at
     * @param value the {@link NBTBaseType} you want to put at the key
     */
    public void set(String key, NBTBaseType type, Object value) {
        try {
            Object toPut = type.make(value);
            Method m = tagCompoundClass.getMethod("set", String.class, nbtBaseClass);
            m.setAccessible(true);
            m.invoke(this.tagCompound, key, toPut);
            m.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Put a {@link String} {@link Map} at the location of the key.
     *
     * @param map the location of where you want to set the {@link String} {@link Map} at
     */
    public void setStrings(Map<String, String> map) {
        try {
            Method m = tagCompoundClass.getMethod("setString", String.class, String.class);
            m.setAccessible(true);
            map.forEach((String key, String value) -> {
                try {
                    m.invoke(this.tagCompound, key, value);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            m.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Checks if the key can be found in the NBT of the item
     *
     * @param key the key you want to check if it exists
     * @return true | false
     */
    public boolean hasKey(String key) {
        try {
            Method m = tagCompoundClass.getMethod("hasKey", String.class);
            m.setAccessible(true);
            Object o = m.invoke(this.tagCompound, key);
            m.setAccessible(false);

            return o instanceof Boolean && (Boolean) o;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Applies the NBT settings to the {@link ItemStack}
     *
     * @param item the item you want to apply the NBT to
     * @return the item with the applied NBT
     */
    public ItemStack apply(ItemStack item) {
        try {
            Method nmsGet = craftItemstackClass.getMethod("asNMSCopy", ItemStack.class);
            nmsGet.setAccessible(true);
            Object nmsStack = nmsGet.invoke(null, item);
            nmsGet.setAccessible(false);

            Method nbtSet = nmsItemstackClass.getMethod("setTag", tagCompoundClass);
            nbtSet.setAccessible(true);
            nbtSet.invoke(nmsStack, this.tagCompound);
            nbtSet.setAccessible(false);

            Method m = craftItemstackClass.getMethod("asBukkitCopy", nmsItemstackClass);
            m.setAccessible(true);
            Object o = m.invoke(null, nmsStack);
            m.setAccessible(false);

            return o instanceof ItemStack ? (ItemStack) o : null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String toString() {
        return "NBTEditor(" + compoundString() + ")";
    }

    public String compoundString() {
        return Objects.toString(tagCompound);
    }
}