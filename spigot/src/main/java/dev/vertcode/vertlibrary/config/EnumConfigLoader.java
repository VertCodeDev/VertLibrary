


/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.config;

import dev.vertcode.vertlibrary.base.VertPluginBase;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;

public class EnumConfigLoader {

    private final VertPluginBase plugin;
    private final File configFile;
    private final Class<?> configClass;
    @Getter
    private YamlConfiguration yamlConfiguration;

    public EnumConfigLoader(VertPluginBase plugin, File configFile, Class<?> configClass) {
        if (!EnumConfig.class.isAssignableFrom(configClass))
            throw new NullPointerException("The configClass must implement EnumConfig");
        this.plugin = plugin;
        this.configFile = configFile;
        this.configClass = configClass;

        this.initializeConfig();
    }

    /**
     * Reloads the config from the disk.
     */
    public void reload() {
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
        for (EnumConfig enumConstant : (EnumConfig[]) configClass.getEnumConstants()) {
            if (this.yamlConfiguration.contains(enumConstant.getPath()))
                enumConstant.setValue(this.yamlConfiguration.get(enumConstant.getPath()));
            else this.yamlConfiguration.set(enumConstant.getPath(), enumConstant.get());
        }
        save();
    }

    /**
     * Saves the config to the disk.
     */
    public void save() {
        if (this.yamlConfiguration == null) return;

        try {
            this.yamlConfiguration.save(this.configFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Initializes the config (Will automatically run on creation of the class)
     */
    public void initializeConfig() {
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();

        if (!configFile.exists()) {
            try {
                InputStream defaultConfigFile = plugin.getResource(configFile.getName());
                if (defaultConfigFile != null) plugin.saveResource(configFile.getName(), false);
                else configFile.createNewFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        reload();
    }
}
