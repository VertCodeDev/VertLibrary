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

/*
 * VertCode Development  - Wesley Breukers
 *
 * [2019] - [2021] VertCode Development
 * All Rights Reserved.
 *
 * GUI UTILITIES MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.gui.page;

import dev.vertcode.vertlibrary.chat.ChatUtils;
import dev.vertcode.vertlibrary.gui.entry.GUIEntry;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class GUIPage implements InventoryHolder {

	private final List<GUIEntry> entries = new ArrayList<>();
	private final Inventory inventory;

	private final String title;
	private final int rows;

	private String identifier;

	public GUIPage(String title, int rows) {

		this.title = title;
		this.rows = rows;

		this.inventory = Bukkit.createInventory(this, rows * 9, ChatUtils.getInstance().colorize(title));
	}

	public void addItem(GUIEntry entry) {
		entries.add(entry);
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getClass());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GUIPage guiPage = (GUIPage) o;
		return Objects.equals(entries, guiPage.entries) && Objects.equals(inventory, guiPage.inventory);
	}
}
