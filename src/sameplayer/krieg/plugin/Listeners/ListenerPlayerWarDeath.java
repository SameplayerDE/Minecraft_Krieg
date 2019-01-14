package sameplayer.krieg.plugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import sameplayer.krieg.plugin.Classes.Soldier;
import sameplayer.krieg.plugin.Enums.EntryPoints;
import sameplayer.krieg.plugin.Enums.Team;
import sameplayer.krieg.plugin.Enums.WarDeathCause;
import sameplayer.krieg.plugin.Events.PlayerWarDeathEvent;
import sameplayer.krieg.plugin.KriegManager;
import sameplayer.krieg.plugin.Main;
import sameplayer.menuapi.plugin.MenuManager;

public class ListenerPlayerWarDeath implements Listener {

    private Main plugin;
    private KriegManager krieg;
    private MenuManager manager;

    public ListenerPlayerWarDeath(Main plugin) {
        this.plugin = plugin;
        this.krieg = this.plugin.getKrieg();
        this.manager = this.plugin.getMenuManager();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerWarDeath(PlayerWarDeathEvent event) {

        Player dead = event.getDead();

        krieg.addDeaths(dead, 1);

        dead.teleport(EntryPoints.Points.DEAD.toLocation());
        dead.setGameMode(GameMode.SPECTATOR);
        dead.getInventory().clear();
        dead.getOpenInventory().close();
        krieg.getSoldier(dead).setDead(true);

        manager.getMenu("Klassen").show(dead);

        new BukkitRunnable() {

            @Override
            public void run() {
                //able to spawn
            }

        }.runTaskLater(plugin, 20*5l);

        if (krieg.isGerman(dead)) {
            krieg.reduceTicketsBy(1, Team.GERMANY);
        }

        if (krieg.isBrit(dead)) {
            krieg.reduceTicketsBy(1, Team.GREAT_BRITAIN);
        }

        if (event.getKiller() == null) {
            return;
        }

        Player killer = event.getKiller();

        killer.sendTitle("", "§c☠", 5, 20, 5);
        krieg.addPoints(killer, 100);
        krieg.addKills(killer, 1);
        dead.setHealth(20D);

        //krieg.broadcast("§e" + dead.getName() + " [" + event.getGun().getShortname() + "] " + killer.getName(), Team.BOTH);

    }

}
