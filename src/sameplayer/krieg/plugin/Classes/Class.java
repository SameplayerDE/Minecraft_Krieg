package sameplayer.krieg.plugin.Classes;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Class {

    private String name;
    private String[] lore;
    private Material material;
    private ItemStack[] content;

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemStack[] getContent() {
        return content;
    }

    public String[] getLore() {
        return lore;
    }

}
