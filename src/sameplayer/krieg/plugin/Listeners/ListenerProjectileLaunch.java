package sameplayer.krieg.plugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;
import sameplayer.krieg.plugin.KriegManager;
import sameplayer.krieg.plugin.Main;

public class ListenerProjectileLaunch implements Listener {

    private Main plugin;
    private KriegManager krieg;

    public ListenerProjectileLaunch(Main plugin) {
        this.plugin = plugin;
        this.krieg = this.plugin.getKrieg();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {

        ProjectileSource source = event.getEntity().getShooter();

        if (!krieg.isGoing()) {
            event.setCancelled(true);
            return;
        }

        if (!(source instanceof Player)) {
            event.setCancelled(true);
            return;
        }

        Player shooter = (Player) source;

        if (krieg.isGerman(shooter)) {

        }

    }

}
