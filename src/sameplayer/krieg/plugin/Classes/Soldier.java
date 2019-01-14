package sameplayer.krieg.plugin.Classes;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Soldier {

    private UUID uuid;
    private int kills = 0;
    private int deaths = 0;
    private double KD = 0;

    private int points = 0;

    private boolean dead = false;

    public Soldier(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public double getKD() {
        if (kills == 0 && deaths == 0) {
            return 0;
        } else if (deaths == 0) {
            return kills;
        }
        return kills / deaths;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(getUUID());
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getUUID());
    }

    public int getDeath() {
        return deaths;
    }

    public int getKills() {
        return kills;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int i) {
        points += i;
    }

    public void addKills(int i) {
        kills += i;
    }

    public void addDeaths(int i) {
        deaths += i;
    }
}
