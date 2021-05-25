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

package dev.vertcode.vertlibrary.gui;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.vertcode.vertlibrary.VertLibrary;
import dev.vertcode.vertlibrary.chat.ChatUtils;
import dev.vertcode.vertlibrary.gui.entry.GUIEntry;
import dev.vertcode.vertlibrary.gui.entry.GUIEntryBuilder;
import dev.vertcode.vertlibrary.gui.entry.GUIEntryFunction;
import dev.vertcode.vertlibrary.gui.page.GUIPage;
import dev.vertcode.vertlibrary.gui.page.GUIPageBuilder;
import dev.vertcode.vertlibrary.gui.worker.GUIWorker;
import dev.vertcode.vertlibrary.object.Pair;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * A custom chest based inventory called a GUI (Graphical User Interface) implementing an
 * {@link InventoryHolder} from bukkit. Optional features include pages, borders, arrows and close buttons.
 *
 * <p>
 * Entries represent the actual items in the GUI, can be added through either
 * {@link GUI#addItem(GUIEntry)} or through a page {@link GUIPage#addItem(GUIEntry)}
 *
 * <p>
 * Example usage (basics):
 *
 * <pre>
 * {@code
 * public final TestGUI extends GUI {
 *
 *     public TestGUI() {
 *         super("Title_Here", 3);
 *     }
 *
 *     @Override
 *     protected void setupInventory(Player player) {
 *         this.fillBorders();
 *
 *         this.addItem(new GUIEntryBuilder()
 * 					.setSlot(13).setUpdateOffset(1).setUpdateTimeUnit(TimeUnit.SECONDS)
 * 					.setItem(() -> new ItemBuilder(Material.DIAMOND_BLOCK)
 * 							.setName(ChatUtil.format("&dUpdatable: &3" + player.getLevel()))
 * 							.setLore(ChatUtil.format("&7This is a lore"))
 * 							.toItemStack())
 * 					.build());
 *     }
 * }
 * }
 * </pre>
 *
 * <p>
 * Example declaration (using usage from above):
 *
 * <pre>
 * {@code
 * final TestGUI gui = new TestGUI();
 * gui.open(Player);
 * }
 * </pre>
 * <p>
 * NOTE: Every GUI will be cached, and will expire 15 minutes after access. {@link GUI#workerCache}
 */
public abstract class GUI implements InventoryHolder {

    private static final Map<UUID, GUIWorker> ACTIVE_WORKERS = new HashMap<>();
    private static final Cache<Pair<UUID, String>, GUIWorker> workerCache = CacheBuilder.newBuilder()
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .build();

    private final LinkedList<GUIEntry> ENTRIES = new LinkedList<>();
    private final LinkedList<GUIPage> PAGES = new LinkedList<>();

    private String title;
    private int rows;

    private Inventory inventory;
    private boolean openingInitial = true;

    /**
     * Creates a new instance of GUI
     *
     * @param title the title of the new GUI
     * @param rows  the amount of rows the GUI should have, must be between 1-6 (including)
     */
    public GUI(String title, int rows) {
        this.title = title;
        this.rows = rows;
    }

    /**
     * Add a newly created entry with {@link GUIEntryBuilder} to the GUI which
     * will be included in initializing the GUI through it's {@link GUIWorker}
     *
     * @param entry the newly created entry to display in this {@link GUI}
     * @see GUIEntry for possible object contents
     * @see GUIEntryBuilder for a rather more detailed example declarations
     * @see GUIWorker#initInventory(GUIPage) for initializing entries details
     */
    protected void addItem(GUIEntry entry) {
        ENTRIES.add(entry);
    }

    /**
     * Add a newly created page with {@link GUIPageBuilder} to the GUI which
     * will be included in initializing the GUI through it's {@link GUIWorker}
     *
     * <p>
     * The {@link GUIPage#getIdentifier()} will be set before adding the page to
     * the pages list to be sure the cache can read the possible page identifier
     *
     * @param page the newly created page to be accessible for this {@link GUI}
     * @see GUIPage for possible object contents
     * @see GUIPageBuilder for a rather more detailed example declarations
     * @see GUIWorker#initInventory(GUIPage) for initializing pages details
     */
    protected void addPage(GUIPage page) {
        page.setIdentifier(hashCode() + ":page:" + (PAGES.size() + 1));
        PAGES.add(page);
    }

    /**
     * @see GUI#open(Player, int)
     */
    public void open(Player player) {
        this.open(player, 1);
    }

    /**
     * Open the {@link GUI} for the specified player with a given page number
     *
     * @param player     the player to open the {@link GUI} for
     * @param pageNumber the number of the {@link GUIPage} to open
     */
    public void open(Player player, int pageNumber) {
        VertLibrary.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(VertLibrary.getInstance(),
                () -> handleOpen(player, pageNumber), 1L);
    }

    @SneakyThrows
    private void handleOpen(Player player, int pageNumber) {
        this.setupInventory(player);
        GUIPage page = PAGES.size() > 0 ? PAGES.get(pageNumber - 1) : null;

        GUIWorker worker = workerCache.get(new Pair<>(player.getUniqueId(), page == null ? String.valueOf(hashCode()) : page.getIdentifier()), () -> {
            this.inventory = Bukkit.createInventory(this, rows * 9, ChatUtils.getInstance().colorize(page == null ?
                    title : page.getTitle()));
            return new GUIWorker(this, page, player);
        });

        worker.startWorker();
        ACTIVE_WORKERS.put(player.getUniqueId(), worker);
    }

    public void close(Player player) {
        GUIWorker worker = ACTIVE_WORKERS.get(player.getUniqueId());
        if (worker == null)
            throw new IllegalStateException("Can't close the inventory for " + player.getName() + ", no working GUI found.");

        worker.stopUpdating();
        worker.removeWorker();
    }

    public int getPageNumber(GUIPage page) {
        return PAGES.indexOf(page) + 1;
    }

    public void terminateGUI(Player player, GUIWorker worker) {
        ACTIVE_WORKERS.remove(player.getUniqueId(), worker);
    }

    public void onOpen(GUI gui, InventoryOpenEvent event) {
    }

    public void onClose(GUI gui, InventoryCloseEvent event) {
    }

    public GUIEntryFunction<GUIPage, Player, GUIEntry> getCloseButton() {
        return (page, player) -> null;
    }

    public GUIEntryFunction<GUIPage, Player, GUIEntry> getPreviousArrow() {
        return (page, player) -> null;
    }

    public GUIEntryFunction<GUIPage, Player, GUIEntry> getNextArrow() {
        return (page, player) -> null;
    }

    public GUIEntryFunction<GUIPage, Player, GUIEntry> getEmptyArrow() {
        return (page, player) -> null;
    }

    public ItemStack getBorder() {
        return new ItemStack(Material.AIR);
    }

    protected abstract void setupInventory(Player player);

    public void fillBorders() {
        int rows = this.getRows();

        if (rows >= 3) {
            for (int i = 0; i <= 8; i++) {
                this.addItem(new GUIEntryBuilder().setSlot(i).setItem(this::getBorder).build());
            }

            for (int i = 8; i < (rows * 9) - 9; i += 9) {
                this.addItem(new GUIEntryBuilder().setSlot(i).setItem(this::getBorder).build());
                this.addItem(new GUIEntryBuilder().setSlot(i + 1).setItem(this::getBorder).build());
            }

            for (int i = (rows * 9) - 9; i < rows * 9; i++) {
                this.addItem(new GUIEntryBuilder().setSlot(i).setItem(this::getBorder).build());
            }
        }
    }

    public boolean isInGUI(Player player) {
        return ACTIVE_WORKERS.containsKey(player.getUniqueId());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public LinkedList<GUIEntry> getEntries() {
        return ENTRIES;
    }

    public LinkedList<GUIPage> getPages() {
        return PAGES;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public boolean isOpeningInitial() {
        return openingInitial;
    }

    public void setOpeningInitial(boolean openingInitial) {
        this.openingInitial = openingInitial;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getClass());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GUI gui = (GUI) o;
        return Objects.equals(inventory, gui.inventory);
    }

    @Override
    public String toString() {
        return "GUI{" +
                "ENTRIES=" + ENTRIES +
                ", PAGES=" + PAGES +
                ", title='" + title + '\'' +
                ", rows=" + rows +
                ", inventory=" + inventory +
                '}';
    }
}
