package sameplayer.krieg.plugin.Events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import sameplayer.krieg.plugin.Classes.Gun;

public class PlayerGrenadeHitEvent extends PlayerEvent implements Cancellable {

    private Player who, by;
    private boolean isCancelled;

    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerGrenadeHitEvent(Player who, Player by, double damage) {
        super(who);
        this.by = by;
        isCancelled = false;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getEnemy() {
        return by;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }
}
