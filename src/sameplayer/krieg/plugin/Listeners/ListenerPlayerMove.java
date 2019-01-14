package sameplayer.krieg.plugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import sameplayer.krieg.plugin.KriegManager;
import sameplayer.krieg.plugin.Main;

public class ListenerPlayerMove implements Listener {

    private Main plugin;
    private KriegManager krieg;

    public ListenerPlayerMove(Main plugin) {
        this.plugin = plugin;
        this.krieg = this.plugin.getKrieg();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        if (!krieg.isGoing()) {
            return;
        }

        if (krieg.getSoldier(player).isDead()) {
            event.setCancelled(true);
            return;
        }

    }

}
