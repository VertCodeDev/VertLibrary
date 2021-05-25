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

package dev.vertcode.vertlibrary.standalone.gui;

import com.cryptomorin.xseries.XMaterial;
import dev.vertcode.vertlibrary.VertLibrary;
import dev.vertcode.vertlibrary.chat.ChatUtils;
import dev.vertcode.vertlibrary.gui.GUI;
import dev.vertcode.vertlibrary.gui.entry.GUIEntryBuilder;
import dev.vertcode.vertlibrary.item.ItemBuilder;
import dev.vertcode.vertlibrary.standalone.config.GenericLang;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class PluginsGUI extends GUI {

    public PluginsGUI() {
        super("VertLibrary » Plugins", 6);
    }

    @Override
    public void onClose(GUI gui, InventoryCloseEvent event) {
        final MainGUI mainGUI = new MainGUI();
        mainGUI.open((Player) event.getPlayer());
    }

    @Override
    protected void setupInventory(Player player) {
        this.addItem(new GUIEntryBuilder().setItem(() -> new ItemBuilder(XMaterial.BEACON.parseItem())
                .setDisplayName("&aVertLibrary")
                .setLore(Arrays.asList(
                        "",
                        "&cVersion &8» &f" + VertLibrary.getInstance().getDescription().getVersion(),
                        "&cAuthor(s) &8» &f" + String.join(", ",
                                VertLibrary.getInstance().getDescription().getAuthors()),
                        "&cCommands loaded &8» &f" + VertLibrary.getInstance().getLoadedCommands()
                                .getOrDefault(VertLibrary.getInstance().getDescription().getName(),
                                        new ArrayList<>()).size(),
                        "&cListeners loaded &8» &f" + VertLibrary.getInstance().getTotalListeners()
                ))
                .build()).setSlot(0).build());

        VertLibrary.getInstance().getLoadedPlugins().values().stream().filter(vertPlugin ->
                !(vertPlugin instanceof VertLibrary)).map(vertPlugin -> new GUIEntryBuilder().setItem(() -> {
            if (vertPlugin.isPluginEnabled()) {
                return new ItemBuilder(XMaterial.BOOK.parseItem())
                        .setDisplayName("&a" + vertPlugin.getDescription().getName())
                        .setLore(Arrays.asList(
                                "",
                                "&cVersion &8» &f" + vertPlugin.getDescription().getVersion(),
                                "&cAuthor(s) &8» &f" + String.join(", ",
                                        vertPlugin.getDescription().getAuthors()),
                                "&cCommands loaded &8» &f" + VertLibrary.getInstance().getLoadedCommands()
                                        .getOrDefault(vertPlugin.getDescription().getName(),
                                                new ArrayList<>()).size(),
                                "&cListeners loaded &8» &f" + vertPlugin.getTotalListeners(),
                                "",
                                "&c&oRight Click &7&oto &c&odisable &7&othe plugin."
                        ))
                        .build();
            }
            return new ItemBuilder(XMaterial.BARRIER.parseItem())
                    .setDisplayName("&c" + vertPlugin.getDescription().getName())
                    .setLore(Arrays.asList(
                            "",
                            "&cVersion: &f" + vertPlugin.getDescription().getVersion(),
                            "&cAuthor(s): &f" + String.join(", ",
                                    vertPlugin.getDescription().getAuthors()),
                            "&cErrored: &f" + vertPlugin.isPluginErrored(),
                            "",
                            "&c&oRight Click &7&oto &a&oenable &7&othe plugin."
                    ))
                    .build();
        })
                .setAction(ClickType.RIGHT, (player1, guiClickEvent) -> {
                    if (vertPlugin instanceof VertLibrary) {
                        player1.sendMessage(ChatUtils.getInstance()
                                .colorize(GenericLang.VERTLIBRARY$GUI$CANNOT_DISABLE_VERTLIBRARY.getString()));
                        return;
                    }
                    if (vertPlugin.isPluginErrored()) {
                        player1.sendMessage(ChatUtils.getInstance()
                                .colorize(GenericLang.VERTLIBRARY$GUI$ERRORED_NEED_RESTART.getString()));
                        return;
                    }
                    if (vertPlugin.isPluginEnabled()) {
                        VertLibrary.getInstance().getServer().getPluginManager().disablePlugin(vertPlugin);
                        guiClickEvent.updateItem();
                        return;
                    }
                    VertLibrary.getInstance().getServer().getPluginManager().enablePlugin(vertPlugin);
                    guiClickEvent.updateItem();
                })
                .setUpdateOffset(10)
                .setUpdateTimeUnit(TimeUnit.SECONDS)
                .setUpdateAsync(true)
                .build()).forEachOrdered(this::addItem);

    }
}
