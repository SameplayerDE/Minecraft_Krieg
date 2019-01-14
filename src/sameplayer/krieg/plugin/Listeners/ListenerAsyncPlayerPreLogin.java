package sameplayer.krieg.plugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import sameplayer.krieg.plugin.Enums.GameState;
import sameplayer.krieg.plugin.KriegManager;
import sameplayer.krieg.plugin.Main;

public class ListenerAsyncPlayerPreLogin implements Listener {

    private Main plugin;
    private KriegManager krieg;
    private GameState gameState;

    public ListenerAsyncPlayerPreLogin(Main plugin) {
        this.plugin = plugin;
        updateVariables();
    }

    private void updateVariables() {
        this.krieg = this.plugin.getKrieg();
        this.gameState = this.krieg.getGameState();
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {

        updateVariables();

        Player player = Bukkit.getPlayer(event.getUniqueId());

        if (!gameState.equals(GameState.REBOOTING)) {

            if (gameState.equals(GameState.WAITING_QUEUE)) {



            }else if (gameState.equals(GameState.WAITING_START)) {



            }else if (gameState.equals(GameState.WAITING_FIGHT)) {



            } else if (gameState.equals(GameState.WAITING_RESET_OBJECTIVE)) {



            }

        }else{

            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cDer Dienst ist noch nicht bereit");

        }

    }

}

