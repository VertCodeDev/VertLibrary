package dev.vertcode.vertlibrary.reflections;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.time.Instant;

public class ReflectionUtils {

    private static final String _NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    private static final int version = Integer.parseInt(Bukkit.getServer().getClass().getName().split("\\.")[3].split("_")[1]);
    private static final int release = Integer.parseInt(Bukkit.getServer().getClass().getName().split("\\.")[3].split("R")[1]);

    /**
     * Send packet.
     *
     * @param player the player
     * @param packet the packet
     */
    public static void sendPacket(Player player, Object... packet) {
        for (Object o : packet) {
            try {
                Object handle = player.getClass().getMethod("getHandle").invoke(player);
                Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
                playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, o);
            } catch (Exception e) {

            }
        }
    }

    /**
     * Gets player connection.
     *
     * @param player the player
     * @return the player connection
     */
    public static Object getPlayerConnection(Player player) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            return handle.getClass().getField("playerConnection").get(handle);
        } catch (Exception e) {

        }

        return null;
    }

    /**
     * Gets nms class.
     *
     * @param className the class name
     * @return the nms class
     */
    public static Class<?> getNMSClass(String className) {
        try {
            return Class.forName("net.minecraft.server." + _NMS_VERSION + "." + className);
        } catch (Exception e) {

        }

        return null;
    }

    /**
     * Gets nms class.
     *
     * @param classPrefix the class prefix
     * @param className   the class name
     * @return the nms class
     */
    public static Class<?> getNMSClass(String classPrefix, String className) {
        try {
            return Class.forName(classPrefix + "." + _NMS_VERSION + "." + className);
        } catch (Exception e) {

        }

        return null;
    }

    /**
     * New instance object.
     *
     * @param clazz      the clazz
     * @param parameters the parameters
     * @return the object
     */
    public static Object newInstance(Class<?> clazz, Object... parameters) {

        Class<?>[] classes = new Class<?>[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            classes[i] = parameters[i].getClass();
        }

        try {
            Constructor<?> c = clazz.getDeclaredConstructor(classes);
            c.setAccessible(true);
            return c.newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Invoke object.
     *
     * @param object     the object
     * @param method     the method
     * @param parameters the parameters
     * @return the object
     */
    @Nullable
    public static Object invoke(Object object, String method, Object... parameters) {

        Method m;

        Class<?>[] classes = new Class<?>[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            classes[i] = parameters[i].getClass();
        }

        try {
            m = object.getClass().getDeclaredMethod(method, classes);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
        Object o1;
        try {
            boolean b = m.isAccessible();
            m.setAccessible(true);
            o1 = m.invoke(object, parameters);
            m.setAccessible(b);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
        return o1;
    }

    /**
     * Sets field.
     *
     * @param object   the object
     * @param field    the field
     * @param newValue the new value
     * @return the field
     */
    @Nullable
    public static Object setField(Object object, String field, Object newValue) {

        Field f;

        try {
            f = object.getClass().getDeclaredField(field);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            return null;
        }

        try {
            boolean b = f.isAccessible();
            f.setAccessible(true);
            f.set(object, newValue);
            f.setAccessible(b);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        return object;

    }

    /**
     * Gets field.
     *
     * @param object the object
     * @param field  the field
     * @return the field
     */
    @Nullable
    public static Object getField(Object object, String field) {

        Field f;

        try {
            f = object.getClass().getDeclaredField(field);
        } catch (NoSuchFieldException | SecurityException e) {
            try {
                f = object.getClass().getSuperclass().getDeclaredField(field);
            } catch (SecurityException | NoSuchFieldException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        Object o = null;
        try {
            boolean b = f.isAccessible();
            f.setAccessible(true);
            o = f.get(object);
            f.setAccessible(b);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        return o;

    }

    /**
     * Cast object.
     *
     * @param object the object
     * @param clazz  the clazz
     * @return the object
     */
    @Nullable
    public static Object cast(Object object, Class<?> clazz) {
        try {
            return clazz.cast(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets cb class.
     *
     * @param name the name
     * @return the cb class
     */
    public static Class<?> getCBClass(String name) {
        try {
            return Class.forName(
                    "org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().info("[Reflection] Can't find CB Class! (" + "org.bukkit.craftbukkit."
                    + Bukkit.getServer().getClass().getName().split("\\.")[3] + "." + name + ")");
            return null;
        }
    }

    /**
     * Gets complete version.
     *
     * @return the complete version
     */
    public static String getCompleteVersion() {
        return Bukkit.getServer().getClass().getName().split("\\.")[3];
    }

    /**
     * Version is 1 7 boolean.
     *
     * @return the boolean
     */
    public static boolean versionIs1_7() {
        return Bukkit.getServer().getClass().getName().split("\\.")[3].startsWith("v1_7");
    }

    public static void setValue(Field field, Object object, Object value) {
        //If the value has to be null, just return so there is an default value possible
        if (value == null) setRawValue(field, object, null);

            //If its an instant, parse and set it.
        else if (field.getType() == Boolean.class) setRawValue(field, object, Boolean.parseBoolean(value.toString()));
        else if (field.getType() == Integer.class) setRawValue(field, object, Integer.parseInt(value.toString()));
        else if (field.getType() == Double.class) setRawValue(field, object, Double.parseDouble(value.toString()));
        else if (field.getType() == Instant.class) setRawValue(field, object, Instant.parse(value.toString()));
        else setRawValue(field, object, value.toString());
    }

    @SneakyThrows
    public static void setRawValue(Field field, Object object, Object value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
        }
    }

    public static boolean set(Class<?> sourceClass, Object instance, String fieldName, Object value) {
        try {
            Field field = sourceClass.getDeclaredField(fieldName);
            boolean accessible = field.isAccessible();

            Field modifiersField = Field.class.getDeclaredField("modifiers");

            int modifiers = modifiersField.getModifiers();
            boolean isFinal = (modifiers & Modifier.FINAL) == Modifier.FINAL;

            if (!accessible) field.setAccessible(true);
            if (isFinal) {
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, modifiers & ~Modifier.FINAL);
            }

            try {
                field.set(instance, value);
            } finally {
                if (isFinal) modifiersField.setInt(field, modifiers | Modifier.FINAL);
                if (!accessible) field.setAccessible(false);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
