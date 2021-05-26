/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary;

import dev.vertcode.vertlibrary.base.VertCommandBase;
import dev.vertcode.vertlibrary.base.VertPluginBase;
import dev.vertcode.vertlibrary.gui.worker.GUIWorkerListener;
import dev.vertcode.vertlibrary.standalone.command.VertLibraryCommand;
import dev.vertcode.vertlibrary.standalone.config.GenericLang;
import dev.vertcode.vertlibrary.standalone.config.LibraryConf;
import dev.vertcode.vertlibrary.standalone.listener.ArmorListener;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public final class VertLibrary extends VertPluginBase {

    private static VertLibrary instance;
    private final Map<String, VertPluginBase> loadedPlugins = new HashMap<>();
    private final Map<String, List<VertCommandBase>> loadedCommands = new HashMap<>();

    public static VertLibrary getInstance() {
        return instance;
    }

    @Override
    public void onStartup() {
        instance = this;

        registerConfig("config", LibraryConf.class);
        registerConfig("language", GenericLang.class);

        this.registerListener(new GUIWorkerListener());
        this.registerListener(new ArmorListener());
        this.registerCommand(new VertLibraryCommand());
    }

    @Override
    public void onShutdown() {
    }
}
