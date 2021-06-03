/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.base;

import com.velocitypowered.api.proxy.ProxyServer;
import dev.vertcode.vertlibrary.ascii.AsciGen;
import dev.vertcode.vertlibrary.chat.ChatUtils;
import dev.vertcode.vertlibrary.config.EnumConfig;
import dev.vertcode.vertlibrary.config.EnumConfigLoader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public abstract class VertPluginBase {

    private final String pluginName;
    private final ProxyServer proxyServer;
    private final File dataFolder;
    private final Map<String, EnumConfigLoader> configMap = new HashMap<>();

    /**
     * Easily register a {@link EnumConfig}
     * NOTE: name shouldn't include file extension
     *
     * @param name        the name of the config
     * @param file        the file where the config should be located
     * @param configClass the config class
     */
    public void registerConfig(String name, File file, Class<?> configClass) {
        this.registerConfig(name, file, configClass, null);
    }

    /**
     * Easily register a {@link EnumConfig}
     * NOTE: name shouldn't include file extension
     *
     * @param name        the name of the config
     * @param file        the file where the config should be located
     * @param configClass the config class
     * @param inputStream the {@link InputStream} of the default config
     */
    private void registerConfig(String name, File file, Class<?> configClass, InputStream inputStream) {
        this.configMap.put(name, new EnumConfigLoader(file, configClass, inputStream));
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
        if (this.proxyServer.getPluginManager().isLoaded(pluginName)) runnable.run();
    }

    /**
     * Register a command.
     *
     * @param commandBase the command you want to register
     */
    public void registerCommand(VertCommandBase commandBase) {
        this.proxyServer.getCommandManager().register(commandBase, commandBase);
    }

    /**
     * Register a listener.
     *
     * @param listener the listener you want to register
     */
    public void registerListener(Object listener) {
        this.proxyServer.getEventManager().register(this, listener);
    }

    protected void printLogo() {
        this.proxyServer.getConsoleCommandSource().sendMessage(ChatUtils.getInstance().colorize("&c\n" +
                AsciGen.getInstance().generateAscii(this.pluginName)));
    }

}
