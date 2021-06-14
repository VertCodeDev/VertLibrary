/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.gui.event;

import dev.vertcode.vertlibrary.gui.entry.GUIEntry;
import dev.vertcode.vertlibrary.gui.worker.GUIWorker;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GUIClickEvent extends InventoryClickEvent implements Cancellable {

    private final GUIWorker worker;
    private boolean cancelled;

    public GUIClickEvent(@NotNull GUIWorker worker, InventoryClickEvent event) {
        super(event.getView(), event.getSlotType(), event.getSlot(), event.getClick(), event.getAction());

        this.worker = worker;
    }

    public Player getPlayer() {
        return (Player) this.getWhoClicked();
    }

    public void updateItem() {
        worker.getEntry(this.getSlot()).ifPresent(entry -> worker.getInventory().setItem(entry.getSlot(), entry.getItem()));
    }

    public void updateItem(GUIEntry entry) {
        worker.getInventory().setItem(this.getSlot(), entry.getItem());
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public GUIWorker getWorker() {
        return worker;
    }

    @Override
    public int hashCode() {
        return Objects.hash(worker, cancelled);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GUIClickEvent that = (GUIClickEvent) o;
        return cancelled == that.cancelled && Objects.equals(worker, that.worker);
    }
}