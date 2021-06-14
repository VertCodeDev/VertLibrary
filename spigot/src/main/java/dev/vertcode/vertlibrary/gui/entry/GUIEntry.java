/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.gui.entry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.vertcode.vertlibrary.gui.event.GUIClickEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public abstract class GUIEntry {

    @Getter
    private Multimap<ClickType, BiConsumer<Player, GUIClickEvent>> clickActions = ArrayListMultimap.create();

    public abstract ItemStack getItem();

    public abstract int getSlot();

    public abstract void setSlot(int slot);

    public abstract TimeUnit getUpdateTimeUnit();

    public abstract long getUpdateOffset();

    public abstract boolean isUpdateAsync();

    public GUIEntry onAllClicks(BiConsumer<Player, GUIClickEvent> consumer) {
        for (ClickType value : ClickType.values()) {
            clickActions.put(value, consumer);
        }
        return this;
    }

    public GUIEntry setClickActions(Multimap<ClickType, BiConsumer<Player, GUIClickEvent>> clickActions) {
        this.clickActions = clickActions;
        return this;
    }

    public Collection<BiConsumer<Player, GUIClickEvent>> getClickAction(ClickType clickType) {
        return this.clickActions.get(clickType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clickActions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GUIEntry guiEntry = (GUIEntry) o;
        return Objects.equals(clickActions, guiEntry.clickActions);
    }

    @Override
    public String toString() {
        return "GUIEntry{" +
                "clickActions=" + clickActions +
                '}';
    }
}
