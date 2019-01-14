package sameplayer.krieg.plugin.Enums;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import zame.itemfactory.api.ItemFactory;

public enum Kits {

    PIONEER("§ePionier", Material.PURPLE_DYE,
            new String[] {"", "§cMP7"},
            new ItemStack[] {
                    new ItemStack(Material.CARVED_PUMPKIN)}
            ),
    ASSAULT("§eSturmsoldat", Material.ORANGE_DYE,
            new String[] {"", "§cSAR21"},
            new ItemStack[] {new ItemStack(Material.CARVED_PUMPKIN)}
            ),
    SUPPORTER("§eUnterstützer", Material.LIME_DYE,
            new String[] {"", "§cMG4"},
            new ItemStack[] {new ItemStack(Material.CARVED_PUMPKIN)}
            ),
    SNIPER("§eScharfschütze", Material.PINK_DYE,
            new String[] {"", "§cSCOUT ELITE"},
            new ItemStack[] {new ItemStack(Material.CARVED_PUMPKIN)}
            );

    private String name;
    private String[] lore;
    private Material material;
    private ItemStack[] content;

    Kits(String name, Material material, String[] lore, ItemStack[] content) {
        this.name = name;
        this.material = material;
        this.lore = lore;
        this.content = content;
    }

    public ItemStack toItemStack() {
        return ItemFactory.generateItemStack(name, material, lore);
    }

    public String[] getLore() {
        return lore;
    }

    public ItemStack[] getContent() {
        return content;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

}
