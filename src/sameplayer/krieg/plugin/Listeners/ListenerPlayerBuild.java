package sameplayer.krieg.plugin.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import sameplayer.krieg.plugin.Enums.GameState;
import sameplayer.krieg.plugin.KriegManager;
import sameplayer.krieg.plugin.Main;

public class ListenerPlayerBuild implements Listener {

    private Main plugin;
    private GameState gameState;
    private KriegManager krieg;

    public ListenerPlayerBuild(Main plugin) {
        this.plugin = plugin;
        updateVariables();
    }

    private void updateVariables() {
        this.krieg = this.plugin.getKrieg();
        this.gameState = this.krieg.getGameState();
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {

        updateVariables();

        Player player = event.getPlayer();

    }

}
