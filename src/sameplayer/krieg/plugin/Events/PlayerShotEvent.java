package sameplayer.krieg.plugin.Events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import sameplayer.krieg.plugin.Classes.Gun;

public class PlayerShotEvent extends PlayerEvent implements Cancellable {

    private Player who, by;
    private Gun gun;
    private boolean isCancelled;
    private Location hitLocation;
    private int distance;

    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerShotEvent(Player who, Player by, Gun gun, int distance) {
        super(who);
        this.by = by;
        isCancelled = false;
        this.gun = gun;
        this.distance = distance;
        this.hitLocation = who.getEyeLocation();
    }

    public PlayerShotEvent(Player who, Player by, Gun gun, Location hitLocation, int distance) {
        super(who);
        this.by = by;
        isCancelled = false;
        this.gun = gun;
        this.hitLocation = hitLocation;
        this.distance = distance;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public int getDistance() {
        return distance;
    }

    public World getWorld() {
        return hitLocation.getWorld();
    }

    public int getDamage() {
        return gun.getDamageToDeal(distance);
    }

    public Location getHitLocation() {
        return hitLocation;
    }

    public Gun getGun() {
        return gun;
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
