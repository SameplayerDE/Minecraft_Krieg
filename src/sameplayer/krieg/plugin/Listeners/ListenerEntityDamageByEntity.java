package sameplayer.krieg.plugin.Listeners;

import net.minecraft.server.v1_13_R2.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import sameplayer.krieg.plugin.Classes.Gun;
import sameplayer.krieg.plugin.Enums.WarDeathCause;
import sameplayer.krieg.plugin.Events.PlayerShotEvent;
import sameplayer.krieg.plugin.Events.PlayerWarDeathEvent;
import sameplayer.krieg.plugin.Guns.STG44;
import sameplayer.krieg.plugin.Guns.Saiga;
import sameplayer.krieg.plugin.KriegManager;
import sameplayer.krieg.plugin.Main;

import java.util.ArrayList;

public class ListenerEntityDamageByEntity implements Listener {

    private Main plugin;
    private KriegManager krieg;

    public ListenerEntityDamageByEntity(Main plugin) {
        this.plugin = plugin;
        this.krieg = this.plugin.getKrieg();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

     @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        event.setCancelled(true);

        Gun gun = null;

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player target = (Player) event.getEntity();

        if (event.getDamager() instanceof Player) {

            Player damager = (Player) event.getDamager();

            if (!krieg.isGoing()) {
                return;
            }

            if (damager.getInventory().getItemInMainHand() != null) {

                ItemStack item = damager.getInventory().getItemInMainHand();

                    if (!item.hasItemMeta()) {
                        return;
                    } else {
                        if (!item.getItemMeta().hasDisplayName()) {
                            return;
                        }
                    }

                String displayName = item.getItemMeta().getDisplayName();
                displayName = ChatColor.stripColor(displayName);

                if (item.getType().equals(Material.IRON_PICKAXE)) {

                    if (displayName.equals("Sturmgewehr 44")) {
                        gun = new STG44();
                    }
                    if (displayName.equals("SAIGA")) {
                        gun = new Saiga();
                    }

                    gun.playSound(damager.getLocation());

                }

                PlayerShotEvent playerShotEvent = new PlayerShotEvent(target, damager, gun, 1);
                Bukkit.getPluginManager().callEvent(playerShotEvent);

            }
        }


        /**
         if (!(event.getEntity() instanceof Player)) {
             return;
         }

         Player target = (Player) event.getEntity();

         target.sendMessage(event.getDamager().toString());

         if (event.getDamager() instanceof Player) {

             Player damager = (Player) event.getDamager();

             if (!krieg.isGoing()) {
                 event.setCancelled(true);
                 return;
             }

             if (krieg.isSameTeam(target, damager) && target != damager) {
                 event.setCancelled(true);
                 return;
             }



             if (target.getHealth() - event.getDamage() < 1D) {
                if (event.getDamage() == 25D) {
                     krieg.addKills(damager, 1);
                     krieg.addPoints(damager, 100);

                     PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"☠\"}"), ChatMessageType.GAME_INFO);
                     ((CraftPlayer) damager).getHandle().playerConnection.sendPacket(packetPlayOutChat);
                 }
                 target.setHealth(20D);
                 PlayerDeathEvent deathEvent = new PlayerDeathEvent(target, new ArrayList<ItemStack>(), 0, "");
                 Bukkit.getPluginManager().callEvent(deathEvent);
                 event.setCancelled(true);
             }
         }
         **/
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!krieg.isGoing()) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if (event.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)) {
            PlayerWarDeathEvent warDeathEvent = new PlayerWarDeathEvent(player, null, WarDeathCause.KILLED_BY_DROWN, null);
            Bukkit.getPluginManager().callEvent(warDeathEvent);
        }

    }

    @EventHandler
    public void onVekl(PlayerVelocityEvent event) {
        event.setCancelled(true);
    }

}
