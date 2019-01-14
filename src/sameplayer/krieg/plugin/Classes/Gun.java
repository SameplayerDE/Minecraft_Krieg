package sameplayer.krieg.plugin.Classes;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

public abstract class Gun {

    int[] damage = new int[2];
    int[] damageDrop = new int[2];

    int fireRate = 0;
    int magazinSize = 0;

    String name = null;
    String shortname = null;
    ItemStack itemStack = null;

    public Gun(String name, String shortname, int damageA, int damageB, int damageDropA, int damageDropB, int fireRate, int magazinSize) {
        this.name = name;
        this.shortname = shortname;

        this.damage[0] = damageA;
        this.damage[1] = damageB;

        this.damageDrop[0] = damageDropA;
        this.damageDrop[1] = damageDropB;

        this.fireRate = fireRate;
        this.magazinSize = magazinSize;
    }

    public abstract void playSound(Location location);

    public int getFireRate() {
        return fireRate;
    }

    public int getMagazinSize() {
        return magazinSize;
    }

    public int[] getDamage() {
        return damage;
    }

    public String getShortname() {
        return shortname;
    }

    public int getDamageToDeal(int distance) {
        int deal = 0;

        if (damageDrop[0] >= distance) {
            deal = damage[0];
        } else {
            deal = damage[1];
        }

        return deal;
    }

    public int[] getDamageDrop() {
        return damageDrop;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getName() {
        return name;
    }

}
