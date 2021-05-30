/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.config;

import eu.vertcode.vertconfig.VertConfigs;
import eu.vertcode.vertconfig.object.VertConfig;
import lombok.Getter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class EnumConfigLoader {

    private final File configFile;
    private final Class<?> configClass;
    private final InputStream inputStream;
    @Getter
    private VertConfig vertConfig;

    public EnumConfigLoader(File configFile, Class<?> configClass, InputStream inputStream) {
        if (!EnumConfig.class.isAssignableFrom(configClass))
            throw new NullPointerException("The configClass must implement EnumConfig");
        this.configFile = configFile;
        this.configClass = configClass;
        this.inputStream = inputStream;

        this.initializeConfig();
    }

    /**
     * Reloads the config from the disk.
     */
    public void reload() {
        this.vertConfig = VertConfigs.getInstance().getConfig(configFile);
        for (EnumConfig enumConstant : (EnumConfig[]) configClass.getEnumConstants()) {
            if (vertConfig.get(enumConstant.getPath(), enumConstant.get().getClass()) != null)
                enumConstant.setValue(vertConfig.get(enumConstant.getPath(), enumConstant.get().getClass()));
            else this.vertConfig.set(enumConstant.getPath(), enumConstant.get());
        }
        save();
    }

    /**
     * Saves the config to the disk.
     */
    public void save() {
        if (this.vertConfig == null) return;

        try {
            this.vertConfig.save();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Initializes the config (Will automatically run on creation of the class)
     */
    public void initializeConfig() {
        if (!configFile.getParentFile().exists()) configFile.getParentFile().mkdir();

        if (!configFile.exists()) {
            try (FileOutputStream outputStream = new FileOutputStream(this.configFile)) {
                if (inputStream != null) {
                    int read;
                    byte[] bytes = new byte[1024];
                    while ((read = this.inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                    }
                } else configFile.createNewFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        reload();
    }
}
