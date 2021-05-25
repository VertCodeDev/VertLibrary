


/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.gui.worker;

import dev.vertcode.vertlibrary.VertLibrary;
import dev.vertcode.vertlibrary.gui.GUI;
import dev.vertcode.vertlibrary.gui.entry.GUIEntry;
import dev.vertcode.vertlibrary.gui.event.GUIClickEvent;
import dev.vertcode.vertlibrary.gui.event.GUICloseEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.Optional;
import java.util.function.BiConsumer;

public final class GUIWorkerListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event instanceof GUIClickEvent || event.getClickedInventory() == null) return;
        if (!(event.getWhoClicked().getOpenInventory().getTopInventory().getHolder() instanceof GUI)) return;
        if (event.getCurrentItem() == null) return;

        GUIWorker.get(event.getInventory().getHolder()).ifPresent(worker -> {
            event.setCancelled(true);
            Bukkit.getPluginManager().callEvent(new GUIClickEvent(worker, event));
        });
    }

    @EventHandler
    public void onGUIClick(GUIClickEvent event) {
        if (event.isCancelled()) return;

        GUIWorker worker = event.getWorker();
        Player player = event.getPlayer();

        Optional<GUIEntry> optionalEntry = worker.getEntry(event.getSlot());
        optionalEntry.ifPresent(entry -> {
            BiConsumer<Player, GUIClickEvent> consumer = optionalEntry.get().getClickAction(event.getClick());
            if (consumer != null) consumer.accept(player, event);
        });
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getInventory().getHolder() instanceof GUI)) return;

        Optional<GUIWorker> optionalWorker = GUIWorker.get(event.getInventory().getHolder());
        optionalWorker.ifPresent(worker -> {
            GUI gui = worker.getGui();
            if (gui.isOpeningInitial()) gui.onOpen(gui, event);
        });
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event instanceof GUICloseEvent) return;
        InventoryHolder inventoryHolder = event.getInventory().getHolder();

        if (!(inventoryHolder instanceof GUI)) return;
        Player player = (Player) event.getPlayer();

        Optional<GUIWorker> optionalWorker = GUIWorker.get(inventoryHolder);
        optionalWorker.ifPresent(worker -> Bukkit.getScheduler().runTaskLater(VertLibrary.getInstance(), () -> {
            GUICloseEvent.Reason reason = GUICloseEvent.Reason.PLAYER;

            if (player.getOpenInventory().getTopInventory()
                    .getHolder() instanceof GUI) reason = GUICloseEvent.Reason.SWITCHED_PAGES;

            Bukkit.getPluginManager().callEvent(new GUICloseEvent(worker, event, reason));
        }, 1L));
    }

    @EventHandler
    public void onGUIClose(GUICloseEvent event) {
        if (event.isCancelled()) return;

        GUIWorker worker = event.getWorker();
        Player player = (Player) event.getPlayer();

        if (event.getClosingReason() == GUICloseEvent.Reason.SWITCHED_PAGES) return;

        worker.getGui().close(player);
        worker.getGui().onClose(worker.getGui(), event);
        worker.getGui().terminateGUI(player, worker);
        worker.getGui().setOpeningInitial(true);
    }
}
