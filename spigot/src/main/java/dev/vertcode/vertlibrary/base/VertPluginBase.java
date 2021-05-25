package dev.vertcode.vertlibrary.base;

import dev.vertcode.vertlibrary.VertLibrary;
import dev.vertcode.vertlibrary.ascii.AsciGen;
import dev.vertcode.vertlibrary.chat.ChatUtils;
import dev.vertcode.vertlibrary.config.EnumConfig;
import dev.vertcode.vertlibrary.config.EnumConfigLoader;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
public abstract class VertPluginBase extends JavaPlugin {

    private final Map<String, EnumConfigLoader> configMap = new HashMap<>();
    private boolean pluginEnabled = false;
    private boolean pluginErrored = false;
    private int totalListeners = 0;

    @Override
    public void onDisable() {
        if (!pluginErrored) VertLibrary.getInstance().getLoadedCommands().remove(this.getDescription().getName());
        this.onShutdown();
        this.pluginEnabled = false;
        this.totalListeners = 0;
    }

    @Override
    public void onEnable() {
        printLogo();
        try {
            this.onStartup();
            VertLibrary.getInstance().getLoadedPlugins().remove(this.getDescription().getName().toLowerCase());
            VertLibrary.getInstance().getLoadedPlugins().put(this.getDescription().getName().toLowerCase(), this);
            this.pluginEnabled = true;
        } catch (Exception ex) {
            this.pluginErrored = true;
            this.pluginEnabled = false;
            ex.printStackTrace();
        }
    }

    /**
     * Easily register a {@link Listener}
     *
     * @param listener the listener you want to register
     */
    public void registerListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
        this.totalListeners++;
    }

    /**
     * Easily register a {@link VertCommandBase}
     *
     * @param vertCommandBase the command you want to register
     */
    public void registerCommand(VertCommandBase vertCommandBase) {
        if (vertCommandBase == null)
            throw new NullPointerException("VertCommandBase cannot be null while registering it.");

        try {
            this.getCommand(vertCommandBase.getCommand()).setExecutor(vertCommandBase);
            this.getCommand(vertCommandBase.getCommand()).setTabCompleter(vertCommandBase);
            VertLibrary.getInstance().getLoadedCommands().computeIfAbsent(this.getDescription().getName(),
                    s -> new ArrayList<>()).add(vertCommandBase);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Easily register a {@link EnumConfig}
     * NOTE: name shouldn't include file extension
     *
     * @param name        the name of the config
     * @param file        the file where the config should be located
     * @param configClass the config class
     */
    public void registerConfig(String name, File file, Class<?> configClass) {
        this.configMap.put(name, new EnumConfigLoader(this, file, configClass));
    }

    /**
     * Easily register a {@link EnumConfig}
     * NOTE: name shouldn't include file extension
     *
     * @param name        the name of the config
     * @param configClass the config class
     */
    public void registerConfig(String name, Class<?> configClass) {
        this.registerConfig(name, new File(this.getDataFolder(), name + ".yml"), configClass);
    }

    /**
     * Get a cached {@link EnumConfigLoader}
     *
     * @param name the name of the config
     * @return a optional of with or without the {@link EnumConfigLoader} inside
     */
    public Optional<EnumConfigLoader> getConfigLoader(String name) {
        return Optional.ofNullable(this.configMap.get(name));
    }

    /**
     * Only executes the runnable if the plugin is enabled
     *
     * @param pluginName the name of the plugin that should be enabled
     * @param runnable   the runnable you want to run
     */
    public void executeIfPluginIsEnabled(String pluginName, Runnable runnable) {
        if (this.getServer().getPluginManager().isPluginEnabled(pluginName)) runnable.run();
    }

    private void printLogo() {
        Bukkit.getConsoleSender().sendMessage(ChatUtils.getInstance().colorize("&c\n" +
                AsciGen.getInstance().generateAscii(this.getDescription().getName())));
    }

    public abstract void onStartup();

    public abstract void onShutdown();

}
