


/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.gui.event;

import dev.vertcode.vertlibrary.gui.worker.GUIWorker;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GUICloseEvent extends InventoryCloseEvent implements Cancellable {

	private final GUIWorker worker;
	private final Reason reason;

	private boolean cancelled;

	public GUICloseEvent(@NotNull GUIWorker worker, InventoryCloseEvent event, Reason reason) {
		super(event.getView());

		this.worker = worker;
		this.reason = reason;
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

	public GUICloseEvent.Reason getClosingReason() {
		return reason;
	}

	@Override
	public int hashCode() {
		return Objects.hash(worker, reason, cancelled);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GUICloseEvent that = (GUICloseEvent) o;
		return cancelled == that.cancelled && Objects.equals(worker, that.worker) && reason == that.reason;
	}

	public enum Reason {
		SWITCHED_PAGES,
		PLAYER
	}
}
