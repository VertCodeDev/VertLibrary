


/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.gui.worker;

import com.google.common.collect.ArrayListMultimap;
import dev.vertcode.vertlibrary.gui.GUI;
import dev.vertcode.vertlibrary.gui.entry.GUIEntry;
import dev.vertcode.vertlibrary.gui.page.GUIPage;
import dev.vertcode.vertlibrary.worker.Jobs;
import dev.vertcode.vertlibrary.worker.object.AssignmentRunnable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Getter
public final class GUIWorker {

    private static final Map<InventoryHolder, GUIWorker> WORKERS = new HashMap<>();

    public final Map<Integer, GUIWorkerEntry> WORKER_ENTRIES = new HashMap<>();
    public final Map<Integer, GUIEntry> ENTRY_MAP = new HashMap<>();

    private final GUI gui;
    private final Inventory inventory;
    private final Player player;

    public GUIWorker(GUI gui, GUIPage page, Player player) {
        this.gui = gui;
        this.inventory = gui.getInventory();
        this.player = player;

        this.initInventory(page);
    }

    protected static Optional<GUIWorker> get(InventoryHolder holder) {
        return Optional.ofNullable(WORKERS.get(holder));
    }

    public void startWorker() {
        this.openInventory();
        this.startUpdating();
    }

    private void initInventory(GUIPage page) {
        int rows = gui.getRows();
        LinkedList<GUIPage> pages = gui.getPages();

        gui.getEntries().stream().filter(Objects::nonNull).forEachOrdered(this::initEntry);

        if (page != null) {
            page.getEntries().stream().filter(Objects::nonNull).forEachOrdered(this::initEntry);
            int currentPage = gui.getPageNumber(page);

            if (currentPage != 1) {
                Optional.ofNullable(gui.getPreviousArrow().compile(page, player)).ifPresent(entry -> {
                    entry.onAllClicks((player, event) -> {
                        Optional<GUIWorker> before = GUIWorker.get(inventory.getHolder());
                        before.ifPresent(worker -> {
                            worker.stopUpdating();
                            worker.removeWorker();
                        });

                        gui.open(player, currentPage - 1);
                    });

                    if (entry.getSlot() == -1 && rows > 2) entry.setSlot((rows * 9) - 6);
                    this.initEntry(entry);
                });
            } else Optional.ofNullable(gui.getEmptyArrow().compile(page, player)).ifPresent(entry -> {
                entry.setClickActions(ArrayListMultimap.create());

                if (entry.getSlot() == -1 && rows > 2) entry.setSlot((rows * 9) - 6);
                this.initEntry(entry);
            });

            if (currentPage != pages.size()) {
                Optional.ofNullable(gui.getNextArrow().compile(page, player)).ifPresent(entry -> {
                    entry.onAllClicks((player, event) -> {
                        Optional<GUIWorker> before = GUIWorker.get(inventory.getHolder());
                        before.ifPresent(worker -> {
                            worker.stopUpdating();
                            worker.removeWorker();
                        });

                        gui.open(player, currentPage + 1);
                    });

                    if (entry.getSlot() == -1 && rows > 2) entry.setSlot((rows * 9) - 4);
                    this.initEntry(entry);
                });
            } else Optional.ofNullable(gui.getEmptyArrow().compile(page, player)).ifPresent(entry -> {
                entry.setClickActions(ArrayListMultimap.create());

                if (entry.getSlot() == -1 && rows > 2) entry.setSlot((rows * 9) - 4);
                this.initEntry(entry);
            });
        }

        Optional.ofNullable(gui.getCloseButton().compile(page, player)).ifPresent(entry -> {
            entry.onAllClicks((player, event) -> player.closeInventory());

            if (entry.getSlot() == -1 && rows > 2) entry.setSlot((rows * 9) - 5);
            this.initEntry(entry);
        });

        pages.clear();
    }

    private void openInventory() {
        WORKERS.put(inventory.getHolder(), this);
        player.openInventory(inventory);
        gui.setOpeningInitial(false);
    }

    private void initEntry(GUIEntry entry) {
        if (entry.getSlot() != -1) {
            inventory.setItem(entry.getSlot(), entry.getItem());
            ENTRY_MAP.put(entry.getSlot(), entry);
        } else {
            inventory.addItem(entry.getItem());

            int slot = inventory.first(entry.getItem());
            entry.setSlot(slot);

            ENTRY_MAP.put(slot, entry);
        }

        if (entry.getUpdateTimeUnit() != null) {
            WORKER_ENTRIES.put(entry.getSlot(), new GUIWorkerEntry(this, entry));
        }
    }

    protected void startUpdating() {
        WORKER_ENTRIES.values().forEach(GUIWorkerEntry::start);
    }

    public void stopUpdating() {
        WORKER_ENTRIES.values().forEach(GUIWorkerEntry::stop);
    }

    private void updateEntry(GUIEntry entry) {
        inventory.setItem(entry.getSlot(), entry.getItem());
    }

    private void updateEntryAsync(GUIEntry entry) {
        Jobs.FRONTEND_JOB.addAssignment(() -> inventory.setItem(entry.getSlot(), entry.getItem()));
    }

    public Optional<GUIEntry> getEntry(int slot) {
        return Optional.ofNullable(ENTRY_MAP.get(slot));
    }

    public Optional<GUIWorkerEntry> getWorkerEntry(int slot) {
        return Optional.ofNullable(WORKER_ENTRIES.get(slot));
    }

    public void removeWorker() {
        WORKERS.remove(this.inventory.getHolder(), this);
//		this.ENTRY_MAP.clear();
//		this.WORKER_ENTRIES.clear();
    }

    @Override
    public int hashCode() {
        return Objects.hash(gui);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GUIWorker guiWorker = (GUIWorker) o;
        return Objects.equals(gui, guiWorker.gui);
    }

    @Override
    public String toString() {
        return "GUIWorker{" +
                "gui=" + gui +
                ", inventory=" + inventory.getHolder() +
                ", player=" + player.getUniqueId() +
                '}';
    }

    private static class GUIWorkerEntry {

        private final GUIWorker worker;

        @Getter
        private final GUIEntry entry;
        private final TimeUnit timeUnit;
        private final int offset;

        @Setter
        private boolean alive;

        public GUIWorkerEntry(GUIWorker worker, GUIEntry entry) {
            this.worker = worker;
            this.entry = entry;

            this.timeUnit = entry.getUpdateTimeUnit();
            this.offset = Math.toIntExact(entry.getUpdateOffset());

//			this.alive = entry.getUpdateTimeUnit() != null;
        }

        private void start() {
            this.alive = entry.getUpdateTimeUnit() != null;
            if (!alive) return;

            Jobs.FRONTEND_JOB.addAssignment(new AssignmentRunnable() {
                @Override
                public void run() {
                    if (!alive) {
                        this.cancel();
                        return;
                    }

                    if (entry.isUpdateAsync()) worker.updateEntryAsync(entry);
                    else worker.updateEntry(entry);
                }
            }, 0L, (long) offset, timeUnit);
        }

        private void stop() {
            this.alive = false;
        }
    }
}
