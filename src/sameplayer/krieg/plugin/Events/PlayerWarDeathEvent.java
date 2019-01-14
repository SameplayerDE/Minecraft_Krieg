package sameplayer.krieg.plugin.Events;

import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import sameplayer.krieg.plugin.Classes.Gun;
import sameplayer.krieg.plugin.Classes.Soldier;
import sameplayer.krieg.plugin.Enums.WarDeathCause;

public class PlayerWarDeathEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private Player dead = null;
    private Player killer = null;
    private WarDeathCause cause;
    private Gun gun = null;

    public PlayerWarDeathEvent(Player who, @Nullable Player killer, WarDeathCause cause, @Nullable Gun gun) {
        super();
        this.dead = who;
        this.killer = killer;
        this.cause = cause;
        this.gun = gun;
    }

    public Player getDead() {
        return dead;
    }

    public Player getKiller() {
        return killer;
    }

    public Gun getGun() {
        return gun;
    }

    public WarDeathCause getCause() {
        return cause;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
