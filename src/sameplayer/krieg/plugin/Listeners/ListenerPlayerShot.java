package sameplayer.krieg.plugin.Listeners;

import net.minecraft.server.v1_13_R2.EnumItemSlot;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityEquipment;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import sameplayer.krieg.plugin.Classes.Gun;
import sameplayer.krieg.plugin.Classes.Soldier;
import sameplayer.krieg.plugin.Enums.WarDeathCause;
import sameplayer.krieg.plugin.Events.PlayerShotEvent;
import sameplayer.krieg.plugin.Events.PlayerWarDeathEvent;
import sameplayer.krieg.plugin.Guns.STG44;
import sameplayer.krieg.plugin.KriegManager;
import sameplayer.krieg.plugin.Main;

public class ListenerPlayerShot implements Listener {


    private Main plugin;
    private KriegManager krieg;

    public ListenerPlayerShot(Main plugin) {
        this.plugin = plugin;
        this.krieg = this.plugin.getKrieg();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler
    public void onPlayerShot(PlayerShotEvent event) {

        Player target = event.getPlayer();
        Player shooter = event.getEnemy();
        Gun gun = event.getGun();
        Location hitLocation = event.getHitLocation();
        World world = event.getWorld();
        int distance = event.getDistance();

        world.spawnParticle(Particle.BLOCK_CRACK, hitLocation, 10, Material.REDSTONE_BLOCK.createBlockData());

        if (krieg.isSameTeam(target, shooter)) {
            return;
        }

        if (!krieg.getSoldier(target).isDead()) {
            if (target.getHealth() - event.getDamage() < 1D) {
                PlayerWarDeathEvent warDeathEvent = new PlayerWarDeathEvent(target, shooter, WarDeathCause.KILLED_BY_GUN, gun);
                Bukkit.getPluginManager().callEvent(warDeathEvent);
            }else{
                //PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(shooter.getEntityId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(new ItemStack(Material.CARVED_PUMPKIN)));
                //((CraftPlayer)shooter).getHandle().playerConnection.sendPacket(packet);
                target.sendTitle("", "§c█", 0, 20, 5);
                target.setHealth(target.getHealth() - event.getDamage());
                /**new BukkitRunnable() {

                    @Override
                    public void run() {
                        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(shooter.getEntityId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)));
                        ((CraftPlayer)shooter).getHandle().playerConnection.sendPacket(packet);
                    }
                }.runTaskLater(plugin, 1l);**/
                shooter.sendTitle("", "§f✖", 0, 20, 5);
            }
            target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_HURT, 1, 2);
            target.playEffect(EntityEffect.HURT);
            //shooter.playSound(shooter.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 1, 1.25f);
        }

    }

}
