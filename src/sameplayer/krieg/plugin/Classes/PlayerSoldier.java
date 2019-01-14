package sameplayer.krieg.plugin.Classes;

import net.minecraft.server.v1_13_R2.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.HashMap;
import java.util.UUID;

public class PlayerSoldier extends CraftPlayer {

    private static final HashMap<UUID, PlayerSoldier> PLAYERS = new HashMap<>();

    private final Player PLAYER;

    private int kills = 0;
    private int deaths = 0;
    private double KD = 0;

    private int points = 0;

    private PlayerSoldier(Player player) {
        super((CraftServer) Bukkit.getServer(), ((CraftPlayer) player).getHandle());
        PLAYERS.put(player.getUniqueId(), this);
        PLAYER = player;
    }

    public double getKD() {
        if (kills == 0 && deaths == 0) {
            return 0;
        } else if (deaths == 0) {
            return kills;
        }
        return kills / deaths;
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

    @Override
    public boolean hasPermission(String str) {
        return PLAYER.hasPermission(str);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return PLAYER.hasPermission(perm);
    }

    /**
     * Do not call this!
     */
    public void onLeave() {
        if (PLAYERS.containsKey(getUniqueId())) {
            PLAYERS.remove(getUniqueId());
        }
    }

    public static PlayerSoldier getPlayer(UUID uuid) {
        if (PLAYERS.containsKey(uuid)) {
            return PLAYERS.get(uuid);
        } else {
            Player p = Bukkit.getPlayer(uuid);
            return p == null ? null : new PlayerSoldier(p);
        }
    }
}