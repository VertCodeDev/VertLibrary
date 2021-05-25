


/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.item;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import dev.vertcode.vertlibrary.chat.ChatUtils;
import dev.vertcode.vertlibrary.nbt.NBTEditor;
import dev.vertcode.vertlibrary.reflections.ReflectionUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class ItemBuilder {

    private final ItemStack itemStack;
    private final List<ItemFlag> itemFlags = new ArrayList<>();
    private final Map<Enchantment, Integer> enchantments = Maps.newHashMap();
    private ItemMeta meta;
    private String displayName;
    private String rawDisplayName;
    private String skullOwner;
    private String textureUrl;
    private List<String> lore;
    private List<String> rawLore;
    private boolean breakable;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.meta = itemStack.getItemMeta();
        if (this.meta != null) {
            this.displayName = this.meta.getDisplayName();
            this.rawDisplayName = this.meta.getDisplayName();
            this.lore = this.meta.getLore();
            this.rawLore = this.meta.getLore();
            if (this.meta instanceof SkullMeta) {
                this.skullOwner = ((SkullMeta) this.meta).getOwner();
            }
        }
    }

    public ItemBuilder(Material material, short data) {
        this(new ItemStack(material, data));
    }

    public ItemBuilder(Material material) {
        this(material, (short) 1);
    }

    /**
     * @param name         The {@link String} of the displayName you want to set
     * @param placeholders The {@link String...} you want to replace the placeholders from in the item.
     * @return ItemBuilder (Same instance but changed displayName)
     */
    public ItemBuilder setDisplayName(String name, String... placeholders) {
        this.displayName = ChatUtils.getInstance().replacePlaceholders(name, placeholders);
        this.rawDisplayName = name;
        this.meta.setDisplayName(this.displayName);
        return this;
    }

    /**
     * @return The displayName with parsed colors & replaced placeholders.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param name The {@link String} of the displayName you want to set
     * @return ItemBuilder (Same instance but changed displayName)
     */
    public ItemBuilder setDisplayName(String name) {
        this.displayName = ChatUtils.getInstance().colorize(name);
        this.rawDisplayName = name;
        this.meta.setDisplayName(this.displayName);
        return this;
    }

    /**
     * @return The displayName without parsed colors & replaced placeholders.
     */
    public String getRawDisplayName() {
        return rawDisplayName;
    }

    /**
     * @param lore         The {@link List} you want to set the lore to.
     * @param placeholders The {@link String...} you want to replace the placeholders from in the item.
     * @return ItemBuilder (Same instance but changed lore)
     */
    public ItemBuilder setLore(List<String> lore, String... placeholders) {
        this.lore = ChatUtils.getInstance().replacePlaceholders(lore, placeholders);
        this.rawLore = lore;
        this.meta.setLore(this.lore);
        return this;
    }

    /**
     * @return The lore with parsed colors & replaced placeholders.
     */
    public List<String> getLore() {
        return lore;
    }

    /**
     * @param lore The {@link List} you want to set the lore to.
     * @return ItemBuilder (Same instance but changed lore)
     */
    public ItemBuilder setLore(List<String> lore) {
        this.lore = ChatUtils.getInstance().colorize(lore);
        this.rawLore = lore;
        this.meta.setLore(this.lore);
        return this;
    }

    /**
     * @return The lore without parsed colors & replaced placeholders.
     */
    public List<String> getRawLore() {
        return rawLore;
    }

    /**
     * @param enchantment The {@link Enchantment} you want to add.
     * @param level       The level ({@link Integer}) you want the enchantment to be.
     * @return ItemBuilder (Same instance but changed Enchantments)
     */
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * @return The {@link Enchantment} map.
     */
    public Map<Enchantment, Integer> getEnchantments() {
        return enchantments;
    }

    /**
     * @param itemFlag The {@link ItemFlag} you want to add.
     * @return ItemBuilder (Same instance but changed ItemFlags)
     */
    public ItemBuilder addItemFlag(ItemFlag itemFlag) {
        return this.addItemFlags(itemFlag);
    }

    /**
     * @param itemFlags The {@link ItemFlag...} you want to add.
     * @return ItemBuilder (Same instance but changed ItemFlags)
     */
    public ItemBuilder addItemFlags(ItemFlag... itemFlags) {
        this.itemFlags.addAll(Arrays.asList(itemFlags));
        this.meta.addItemFlags(itemFlags);
        return this;
    }

    /**
     * @return The {@link ItemFlag} list.
     */
    public List<ItemFlag> getItemFlags() {
        return itemFlags;
    }

    /**
     * @return The Skull Owner his name.
     */
    public String getSkullOwner() {
        return skullOwner;
    }

    /**
     * @param name The name of the player you want to set the skull owner from.
     */
    public ItemBuilder setSkullOwner(String name) {
        this.skullOwner = name;
        ((SkullMeta) this.meta).setOwner(name);
        return this;
    }

    /**
     * @param url The url of the texture you want to set the skull texture to.
     */
    public ItemBuilder setSkullTexture(String url) {
        this.textureUrl = url;
        if (this.itemStack.getType() != XMaterial.PLAYER_HEAD.parseMaterial())
            throw new IllegalArgumentException("ItemStack is not SKULL_ITEM");
        if (!ReflectionUtils.set(meta.getClass(), meta, "profile", this.createProfileWithTexture(url)))
            throw new IllegalStateException("Unable to inject GameProfile");
        return this;
    }

    /**
     * @return The textureURL you set on the {@link org.bukkit.block.Skull}
     */
    public String getTextureUrl() {
        return textureUrl;
    }

    /**
     * @param texture the Texture url you want to bind to to the {@link GameProfile}
     * @return GameProfile with the texture bound to it.
     */
    private GameProfile createProfileWithTexture(String texture) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();
        propertyMap.put("textures", new Property("textures", texture));
        return profile;
    }

    /**
     * @return boolean (True | False)
     */
    public boolean isSkull() {
        return this.itemStack.getType() == XMaterial.PLAYER_HEAD.parseMaterial() && this.meta instanceof SkullMeta;
    }

    /**
     * Make a item unbreakable / breakable.
     *
     * @param bln boolean (True | False)
     */
    public ItemBuilder setUnbreakable(boolean bln) {
        ItemStack itemStack = new ItemStack(this.itemStack.getType());
        itemStack.setItemMeta(this.meta);
        NBTEditor nbtEditor = NBTEditor.get(itemStack);
        nbtEditor.setBoolean("Unbreakable", bln);
        this.meta = nbtEditor.apply(itemStack).getItemMeta();
        this.breakable = bln;
        return this;
    }

    /**
     * @return Converts the {@link ItemBuilder} to a {@link ItemStack}
     */
    public ItemStack build() {
        this.itemStack.setItemMeta(this.meta);
        return this.itemStack;
    }

    /**
     * @return The {@link ItemMeta} of the {@link ItemBuilder}{@link #build()}
     */
    public ItemMeta getMeta() {
        return this.meta;
    }
}
