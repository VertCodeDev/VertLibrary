/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.vertcode.vertlibrary.base.VertPluginBase;
import dev.vertcode.vertlibrary.conf.GenericLang;
import lombok.Getter;

import java.io.File;
import java.nio.file.Path;
import java.util.logging.Logger;

@Getter
@Plugin(id = "vertlibrary", name = "VertLibrary", version = "1.0.2",
        url = "vertcode.dev", description = "The library plugin for every plugin made by VertCode Development", authors = {"VertCode"})
public class VertLibrary extends VertPluginBase {

    private final Logger logger;

    @Inject
    public VertLibrary(Logger logger, ProxyServer proxyServer, @DataDirectory Path folderPath) {
        super("VertLibrary", proxyServer, folderPath.toFile());
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        this.printLogo();

        registerConfig("language", new File(this.getDataFolder(), "language.json"), GenericLang.class);
    }

}
