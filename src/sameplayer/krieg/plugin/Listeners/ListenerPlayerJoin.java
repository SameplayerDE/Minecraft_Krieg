package sameplayer.krieg.plugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.*;
import sameplayer.krieg.plugin.Enums.EntryPoints;
import sameplayer.krieg.plugin.Enums.GameState;
import sameplayer.krieg.plugin.Enums.Team;
import sameplayer.krieg.plugin.KriegManager;
import sameplayer.krieg.plugin.Main;
import zame.itemfactory.api.ItemFactory;

public class ListenerPlayerJoin implements Listener {

    private Main plugin;
    private KriegManager krieg;

    public ListenerPlayerJoin(Main plugin) {
        this.plugin = plugin;
        this.krieg = this.plugin.getKrieg();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        PlayerInventory playerInventory = player.getInventory();

        playerInventory.clear();
        playerInventory.setArmorContents(null);

        player.setGameMode(GameMode.ADVENTURE);

        player.setLevel(0);
        player.setExp(0);
        player.setHealth(20);
        player.setSaturation(20);
        player.setExhaustion(0);

        if (krieg.isGoing()) {

        }else{
            if (krieg.getGameState().equals(GameState.WAITING_QUEUE)) {
                player.teleport(EntryPoints.Points.LOBBY.toLocation());
            }
            if (krieg.ready()) {
                krieg.startCountdown();
            }
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (!krieg.isGoing()) {
            return;
        }

        krieg.addPlayer(event.getPlayer(), Team.OFFLINE);

        if (krieg.getGermanSize() < 1) {
            krieg.setTickets(999, Team.GREAT_BRITAIN);
            krieg.stopGame();
            krieg.setTickets(0, Team.BOTH);
            return;
        }

        if (krieg.getBritsSize() < 1) {
            krieg.setTickets(999, Team.GERMANY);
            krieg.stopGame();
            krieg.setTickets(0, Team.BOTH);
            return;
        }

    }

}
