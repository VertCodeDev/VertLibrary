/*
 * VertCode Development  - Wesley Breukers
 *
 * © 2020 - 2021 VertCode Development
 *
 * All Rights Reserved.
 * GUI UTILITIES & MONGO DB MADE BY Cody Lynn (Discord: Codiq#3662)
 */

package dev.vertcode.vertlibrary.event.enm;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;

/**
 * @Author Borlea
 * @Github https://github.com/borlea/
 * @Website http://thederpygolems.ca/
 * Jul 30, 2015 6:46:16 PM
 */
public enum ArmorType {

    HELMET(5),
    CHESTPLATE(6),
    LEGGINGS(7),
    BOOTS(8);

    private final int slot;

    ArmorType(int slot) {
        this.slot = slot;
    }

    /**
     * Attempts to match the ArmorType for the specified ItemStack.
     *
     * @param itemStack The ItemStack to parse the type of.
     * @return The parsed ArmorType. (null if none were found.)
     */
    public final static ArmorType matchType(final ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        if (itemStack.getType().name().toLowerCase().contains("skull")) {
            return HELMET;
        }

        if (XMaterial.GOLDEN_HELMET.isSimilar(itemStack)) {
            return HELMET;
        }
        if (XMaterial.GOLDEN_CHESTPLATE.isSimilar(itemStack)) {
            return CHESTPLATE;
        }
        if (XMaterial.GOLDEN_LEGGINGS.isSimilar(itemStack)) {
            return LEGGINGS;
        }
        if (XMaterial.GOLDEN_BOOTS.isSimilar(itemStack)) {
            return BOOTS;
        }

        switch (itemStack.getType()) {
            case DIAMOND_HELMET:
            case IRON_HELMET:
            case CHAINMAIL_HELMET:
            case LEATHER_HELMET:
                return HELMET;
            case DIAMOND_CHESTPLATE:
            case IRON_CHESTPLATE:
            case CHAINMAIL_CHESTPLATE:
            case LEATHER_CHESTPLATE:
                return CHESTPLATE;
            case DIAMOND_LEGGINGS:
            case IRON_LEGGINGS:
            case CHAINMAIL_LEGGINGS:
            case LEATHER_LEGGINGS:
                return LEGGINGS;
            case DIAMOND_BOOTS:
            case IRON_BOOTS:
            case CHAINMAIL_BOOTS:
            case LEATHER_BOOTS:
                return BOOTS;
            default:
                return null;
        }
    }

    public int getSlot() {
        return slot;
    }

}