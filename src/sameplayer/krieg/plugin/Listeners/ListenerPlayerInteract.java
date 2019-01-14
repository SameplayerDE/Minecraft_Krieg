package sameplayer.krieg.plugin.Listeners;

import net.minecraft.server.v1_13_R2.BlockData;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftSheep;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import sameplayer.krieg.plugin.Classes.Gun;
import sameplayer.krieg.plugin.Enums.GameState;
import sameplayer.krieg.plugin.Enums.WarDeathCause;
import sameplayer.krieg.plugin.Events.PlayerGrenadeHitEvent;
import sameplayer.krieg.plugin.Events.PlayerShotEvent;
import sameplayer.krieg.plugin.Events.PlayerWarDeathEvent;
import sameplayer.krieg.plugin.Guns.STG44;
import sameplayer.krieg.plugin.Guns.Saiga;
import sameplayer.krieg.plugin.KriegManager;
import sameplayer.krieg.plugin.Main;

import java.util.HashMap;
import java.util.function.Predicate;

public class ListenerPlayerInteract implements Listener {

    private Main plugin;
    private KriegManager krieg;

    public ListenerPlayerInteract(Main plugin) {
        this.plugin = plugin;
        this.krieg = this.plugin.getKrieg();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        final Player player = event.getPlayer();

        if (!krieg.isGoing()) {
            event.setCancelled(true);
            if (krieg.getGameState().equals(GameState.SETUP)){
                event.setCancelled(false);
            }
            return;
        }

        if (event.getItem() == null) {
            return;
        }

        ItemStack item = event.getItem();

        if (!item.hasItemMeta()) {
            return;
        }else{
            if (!item.getItemMeta().hasDisplayName()) {
                return;
            }
        }

        String displayName = item.getItemMeta().getDisplayName();
        displayName = ChatColor.stripColor(displayName);

        if (item.getType().equals(Material.IRON_PICKAXE)) {

            Gun gun = null;

            if (displayName.equals("Sturmgewehr 44")) {
                gun = new STG44();
            }
            if (displayName.equals("SAIGA")) {
                gun = new Saiga();
            }

            event.setCancelled(true);

            gun.playSound(player.getLocation());

            Location eyeLoc = player.getEyeLocation();
            Vector dir = player.getEyeLocation().getDirection().normalize();

            RayTraceResult result = player.getWorld().rayTrace(eyeLoc, dir, 100, FluidCollisionMode.NEVER, true, 0, entity -> entity != player && entity instanceof Player);

            if (result != null) {

                if (result.getHitPosition() == null) {
                    return;
                }

                int distance = (int) result.getHitPosition().distance(player.getLocation().toVector()) + 1;

                Location hitLocation = result.getHitPosition().toLocation(player.getWorld());

                if (result.getHitEntity() == null) {
                    hitLocation.getWorld().spawnParticle(Particle.BLOCK_CRACK, hitLocation, 10, result.getHitBlock().getBlockData());
                    hitLocation.getWorld().spawnParticle(Particle.BLOCK_DUST, hitLocation, 10, result.getHitBlock().getBlockData());
                    return;
                }

                Player hit = (Player) result.getHitEntity();

                PlayerShotEvent playerShotEvent = new PlayerShotEvent(hit, player, gun, hitLocation, distance);
                Bukkit.getPluginManager().callEvent(playerShotEvent);

                //EntityDamageByEntityEvent entityDamageByEntityEvent = new EntityDamageByEntityEvent(player, hit, EntityDamageEvent.DamageCause.ENTITY_ATTACK, gun.getDamageToDeal(distance));
                //Bukkit.getPluginManager().callEvent(entityDamageByEntityEvent);

            }

        }

        if (item.getType().equals(Material.GHAST_TEAR)) {

            event.setCancelled(true);

            item.setAmount(item.getAmount() - 1);
            ItemStack grenade = new ItemStack(Material.GHAST_TEAR, 1);

            Item grenadeEntity = player.getWorld().dropItem(player.getEyeLocation(), grenade);
            grenadeEntity.setPickupDelay(32767);
            grenadeEntity.setVelocity(player.getEyeLocation().getDirection().normalize());
            grenadeEntity.setGlowing(true);

            new BukkitRunnable() {
                int count = 0;
                @Override
                public void run() {

                    count++;
                    if (count >= 5) {
                        grenadeEntity.getWorld().playSound(grenadeEntity.getLocation(), Sound.ENTITY_PLAYER_BURP, 15f, 2f);
                        grenadeEntity.getWorld().spawnParticle(Particle.BLOCK_DUST, grenadeEntity.getLocation(), 50, 1, 4, 1, 0, Material.DIRT.createBlockData());
                        grenadeEntity.getWorld().spawnParticle(Particle.CLOUD, grenadeEntity.getLocation(), 50, 2, 0, 2, 0);
                        grenadeEntity.getWorld().spawnParticle(Particle.SMOKE_LARGE, grenadeEntity.getLocation(), 100, 1, 3, 1, 0);
                        grenadeEntity.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, grenadeEntity.getLocation(), 1, 0, 0, 0, 0);
                        grenadeEntity.getWorld().spawnParticle(Particle.FLAME, grenadeEntity.getLocation(), 10, 1, 1, 1, 0);
                        grenadeEntity.getWorld().playSound(grenadeEntity.getLocation(), Sound.BLOCK_GLASS_BREAK, 115f, 0f);
                        grenadeEntity.getWorld().playSound(grenadeEntity.getLocation(), Sound.BLOCK_ANVIL_LAND, 15f, 0f);
                        grenadeEntity.getWorld().playSound(grenadeEntity.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 15f, 2f);
                        if (player != null) {
                            for (Entity entity : grenadeEntity.getNearbyEntities(5, 5, 5)) {
                                if (!(entity instanceof Player)) {
                                    continue;
                                }
                                Player enemy = (Player) entity;
                                if (grenadeEntity.getNearbyEntities(2, 2, 2).contains(enemy)) {
                                    //PlayerWarDeathEvent warDeathEvent = new PlayerWarDeathEvent(enemy, player, WarDeathCause.KILLED_WITH_GRENADE, null);
                                    //Bukkit.getPluginManager().callEvent(warDeathEvent);
                                    PlayerGrenadeHitEvent grenadeHitEvent = new PlayerGrenadeHitEvent(enemy, player, 20D);
                                    Bukkit.getPluginManager().callEvent(grenadeHitEvent);
                                    continue;
                                }
                                PlayerGrenadeHitEvent grenadeHitEvent = new PlayerGrenadeHitEvent(enemy, player, 15D);
                                Bukkit.getPluginManager().callEvent(grenadeHitEvent);
                                continue;
                            }
                        }
                        grenadeEntity.remove();
                        cancel();
                    }

                }
            }.runTaskTimer(plugin, 0l, 20l);

        }

    }

    @EventHandler
    public void onItemMerge(ItemMergeEvent event) {
        event.setCancelled(true);
    }

}
