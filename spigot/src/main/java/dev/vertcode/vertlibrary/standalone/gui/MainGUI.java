


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
import dev.vertcode.vertlibrary.base.VertPluginBase;
import dev.vertcode.vertlibrary.gui.GUI;
import dev.vertcode.vertlibrary.gui.entry.GUIEntryBuilder;
import dev.vertcode.vertlibrary.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class MainGUI extends GUI {

    public MainGUI() {
        super("VertLibrary » Main Menu", 3);
    }

    @Override
    protected void setupInventory(Player player) {
        ItemStack redGlass = new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE.parseItem()).setDisplayName("&8*").build();
        ItemStack whiteGlass = new ItemBuilder(XMaterial.WHITE_STAINED_GLASS_PANE.parseItem()).setDisplayName("&8*").build();

        for (int i = 0; i <= getRows() * 9 - 1; i += 2) {
            addItem(new GUIEntryBuilder().setSlot(i).setItem(() -> redGlass).build());
            if (i + 1 < getRows() * 9) addItem(new GUIEntryBuilder().setSlot(i + 1).setItem(() -> whiteGlass).build());
        }

        addItem(new GUIEntryBuilder()
                .setItem(() -> new ItemBuilder(Material.BEACON)
                        .setDisplayName("&c&lVertLibrary")
                        .setLore(Arrays.asList("",
                                "&cDiscord &8» &fvertcode.dev/discord",
                                "&cWebsite &8» &fComing Soon",
                                "&cDeveloper &8» &fVertCode#0001"
                        ))
                        .build())
                .setSlot(12)
                .build());

        addItem(new GUIEntryBuilder()
                .setItem(() -> new ItemBuilder(XMaterial.BOOK.parseItem())
                        .setDisplayName("&c&lPlugins")
                        .setLore(Arrays.asList("",
                                "&cLoaded Plugins &8» &f" + VertLibrary.getInstance().getLoadedPlugins().size(),
                                "&cLoaded Commands &8» &f" + VertLibrary.getInstance().getLoadedCommands().values().stream()
                                        .map(List::size).flatMapToInt(IntStream::of).sum(),
                                "&cLoaded Listeners &8» &f" + VertLibrary.getInstance().getLoadedPlugins().values().stream()
                                        .map(VertPluginBase::getTotalListeners).flatMapToInt(IntStream::of).sum(),
                                "",
                                "&cClick &7to open the &cplugin &7menu."))
                        .build())
                .onAllClicks((player1, guiClickEvent) -> new PluginsGUI().open(player1))
                .setSlot(14)
                .build());

    }

}
