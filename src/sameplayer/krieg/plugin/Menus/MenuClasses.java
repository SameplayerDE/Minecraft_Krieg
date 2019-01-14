package sameplayer.krieg.plugin.Menus;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import sameplayer.krieg.plugin.Enums.EntryPoints;
import sameplayer.krieg.plugin.Enums.Kits;
import sameplayer.krieg.plugin.KriegManager;
import sameplayer.krieg.plugin.Main;
import sameplayer.menuapi.plugin.Menu;
import zame.itemfactory.api.ItemFactory;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.UUID;

public class MenuClasses extends Menu {

    private Main plugin;
    private KriegManager krieg;
    private HashSet<UUID> allowed = new HashSet<>();
    private LinkedHashMap<UUID, Kits> selected = new LinkedHashMap<>();

    public MenuClasses(Main plugin) {
        super("Klassen", 9*3);
        this.plugin = plugin;
        this.krieg = this.plugin.getKrieg();
        setItem(0, Kits.PIONEER.toItemStack());
        setItem(1, Kits.ASSAULT.toItemStack());
        setItem(2, Kits.SUPPORTER.toItemStack());
        setItem(3, Kits.SNIPER.toItemStack());
        setItem(8, ItemFactory.generateItemStack("§aBeitreten", Material.SLIME_BALL));
    }

    @Override
    public void click(Player player, ItemStack itemStack) {

    }

    @Override
    public void click(Player player, ItemStack itemStack, ClickType clickType) {

    }

    @Override
    public void click(Player player, ItemStack itemStack, InventoryAction inventoryAction) {

    }

    @Override
    public void click(Player player, int i) {
        if (i == 0) {
            selected.replace(player.getUniqueId(), Kits.PIONEER);
        }
        if (i == 1) {
            selected.replace(player.getUniqueId(), Kits.ASSAULT);
        }
        if (i == 2) {
            selected.replace(player.getUniqueId(), Kits.SUPPORTER);
        }
        if (i == 3) {
            selected.replace(player.getUniqueId(), Kits.SNIPER);
        }
        player.getInventory().setItem(0, selected.get(player.getUniqueId()).toItemStack());
        if (i == 8) {
            allowed.add(player.getUniqueId());
            player.getOpenInventory().close();
            player.setGameMode(GameMode.ADVENTURE);
            player.setHealth(20D);
            krieg.getSoldier(player).setDead(false);

            //SET KIT
            krieg.setClass(player, selected.get(player.getUniqueId()));

            if (krieg.isBrit(player)) {
                player.teleport(EntryPoints.Points.BRIT.toLocation());
            }
            if (krieg.isGerman(player)) {
                player.teleport(EntryPoints.Points.GERMAN.toLocation());
            }
        }
    }

    @Override
    public void click(Player player, int i, ItemStack itemStack) {

    }

    @Override
    public void click(Player player, int i, ClickType clickType) {

    }

    @Override
    public void click(Player player, int i, InventoryAction inventoryAction) {

    }

    @Override
    public void onInit() {

    }

    @Override
    public void updateContent() {

    }

    @Override
    public void doSomething(Object o) {

    }

    @Override
    public void close(Player player) {
        if (!allowed.contains(player.getUniqueId())) {
            show(player);
        }
    }

    @Override
    public void open(Player player) {
        if (allowed.contains(player.getUniqueId())) {
            allowed.remove(player.getUniqueId());
        }
        if (!selected.containsKey(player.getUniqueId())) {
            selected.put(player.getUniqueId(), Kits.ASSAULT);
        }
        player.getInventory().setItem(0, selected.get(player.getUniqueId()).toItemStack());
    }

    @Override
    public void change(Player player, Menu menu) {

    }
}
