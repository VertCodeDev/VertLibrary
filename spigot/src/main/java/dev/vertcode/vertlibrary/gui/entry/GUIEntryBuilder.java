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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class GUIEntryBuilder {

    private final Multimap<ClickType, BiConsumer<Player, GUIClickEvent>> clickActions = ArrayListMultimap.create();

    private Supplier<ItemStack> itemStackSupplier;
    private int slot = -1;

    private TimeUnit updateTimeUnit;
    private long updateOffset;
    private boolean updateAsync = true;

    public GUIEntryBuilder setItem(Supplier<ItemStack> itemStackSupplier) {
        this.itemStackSupplier = itemStackSupplier;
        return this;
    }

    public GUIEntryBuilder setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public GUIEntryBuilder setAction(ClickType clickType, BiConsumer<Player, GUIClickEvent> consumer) {
        clickActions.put(clickType, consumer);
        return this;
    }

    public GUIEntryBuilder setUpdateTimeUnit(TimeUnit updateTimeUnit) {
        this.updateTimeUnit = updateTimeUnit;
        return this;
    }

    public GUIEntryBuilder setUpdateOffset(long updateOffset) {
        this.updateOffset = updateOffset;
        return this;
    }

    public GUIEntryBuilder setUpdateAsync(boolean updateAsync) {
        this.updateAsync = updateAsync;
        return this;
    }

    public GUIEntryBuilder onClick(BiConsumer<Player, GUIClickEvent> consumer) {
        clickActions.put(ClickType.LEFT, consumer);
        clickActions.put(ClickType.SHIFT_LEFT, consumer);
        clickActions.put(ClickType.WINDOW_BORDER_LEFT, consumer);
        clickActions.put(ClickType.RIGHT, consumer);
        clickActions.put(ClickType.SHIFT_RIGHT, consumer);
        clickActions.put(ClickType.WINDOW_BORDER_RIGHT, consumer);
        return this;
    }

    public GUIEntryBuilder onAllClicks(BiConsumer<Player, GUIClickEvent> consumer) {
        Arrays.stream(ClickType.values()).forEach(value -> clickActions.put(value, consumer));
        return this;
    }

    public GUIEntry build() {
        return new GUIEntry() {
            @Override
            public ItemStack getItem() {
                return Optional.ofNullable(itemStackSupplier).map(Supplier::get).orElse(new ItemStack(Material.AIR));
            }

            @Override
            public int getSlot() {
                return slot;
            }

            @Override
            public void setSlot(int slot) {
                GUIEntryBuilder.this.slot = slot;
            }

            @Override
            public TimeUnit getUpdateTimeUnit() {
                return updateTimeUnit;
            }

            @Override
            public long getUpdateOffset() {
                return updateOffset;
            }

            @Override
            public boolean isUpdateAsync() {
                return updateAsync;
            }
        }.setClickActions(clickActions);
    }
}
