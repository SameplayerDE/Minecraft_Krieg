package sameplayer.krieg.plugin;

import net.minecraft.server.v1_13_R2.ChatMessageType;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import sameplayer.krieg.plugin.Classes.Class;
import sameplayer.krieg.plugin.Classes.PlayerSoldier;
import sameplayer.krieg.plugin.Classes.Soldier;
import sameplayer.krieg.plugin.Enums.EntryPoints;
import sameplayer.krieg.plugin.Enums.GameState;
import sameplayer.krieg.plugin.Enums.Kits;
import sameplayer.krieg.plugin.Enums.Team;
import zame.itemfactory.api.ItemFactory;

import java.util.*;

public class KriegManager {

    private HashSet<Soldier> soldiers = new HashSet<>();
    //private HashSet<PlayerSoldier> soldiers = new HashSet<>();
    private HashSet<UUID> germans = new HashSet<>();
    private HashSet<UUID> brits = new HashSet<>();
    private int ticketsGermany = 20;
    private int ticketsGreatBritain = 20;

    private final int MINIMUM_PLAYERS = 2;
    private Main plugin;

    private GameState gameState = GameState.REBOOTING;

    public KriegManager(Main plugin) {
        this.plugin = plugin;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        Bukkit.getLogger().info(this.gameState.toString() + " > " + gameState.toString());
        this.gameState = gameState;
    }

    public boolean isMinimumMet() {
        return Bukkit.getOnlinePlayers().size() >= MINIMUM_PLAYERS;
    }

    public boolean ready() {
        return gameState.equals(GameState.WAITING_QUEUE) && isMinimumMet();
    }

    public boolean isGoing() {
        return gameState.equals(GameState.RUNNING_FIGHT);
    }

    public boolean isGerman(Player player) {
        return germans.contains(player.getUniqueId());
    }
    public boolean isGerman(UUID uuid) {
        return germans.contains(uuid);
    }

    public boolean isSameTeam(Player player, Player other) {
        return (isBrit(player) && isBrit(other)) || (isGerman(player) && isGerman(other));
    }

    public void setClass(Player player, Kits c) {
        player.getInventory().clear();
        player.getInventory().setContents(c.getContent());
    }

    public boolean isBrit(Player player) {
        return brits.contains(player.getUniqueId());
    }
    public boolean isBrit(UUID uuid) {
        return brits.contains(uuid);
    }

    public void broadcast(String message, Team team) {
        switch (team) {
            case GREAT_BRITAIN:
                send(brits, message);
                break;
            case GERMANY:
                send(germans, message);
                break;
            case BOTH:
                send(germans, message);
                send(brits, message);
                break;
        }
    }

    private void send(HashSet<UUID> members, String message) {
        for (UUID member : members) {
            Player player = Bukkit.getPlayer(member);

            if (player != null) {
                player.sendMessage(message);
            }
        }
    }

    public int getGermanSize() {
        return germans.size();
    }

    public int getBritsSize() {
        return brits.size();
    }

    public void addPlayer(Player player, Team team) {
        switch (team) {
            case GERMANY:
                brits.remove(player.getUniqueId());
                germans.add(player.getUniqueId());
                break;
            case GREAT_BRITAIN:
                germans.remove(player.getUniqueId());
                brits.add(player.getUniqueId());
                break;
            case OFFLINE:
                germans.remove(player.getUniqueId());
                brits.remove(player.getUniqueId());
                break;
        }
    }

    public void stopGame() {
        setGameState(GameState.WON);
        for (Soldier soldier : soldiers) {
            Player player = soldier.getPlayer();
            player.setHealth(20D);
            player.getInventory().clear();
            player.teleport(EntryPoints.Points.WIN.toLocation());
        }
        if (ticketsGermany > ticketsGreatBritain) {
            broadcast("Deutschland hat gesiegt", Team.BOTH);
        }else if (ticketsGreatBritain > ticketsGermany) {
            broadcast("Großbritannien hat gesiegt", Team.BOTH);
        }else if (ticketsGermany == 999) {
            broadcast("Deutschland hat gesiegt", Team.BOTH);
        }else if (ticketsGreatBritain == 999) {
            broadcast("Großbritannien hat gesiegt", Team.BOTH);
        }else{
            broadcast("Unentschieden", Team.BOTH);
        }
        new BukkitRunnable() {
            int countdown = 10;
            @Override
            public void run() {
                if (countdown != 0) {
                    if (countdown == 30 || countdown == 20 || countdown == 10 || countdown == 5 && countdown > 0) {
                        Bukkit.broadcastMessage("§aDu wirst in §e" + countdown + " Sekunden §azur Empfangshalle geschickt");
                    }
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        online.setLevel(countdown);
                    }
                    countdown--;
                }else{
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        online.setLevel(0);
                    }
                    //send back to lobby
                    cancel();
                }

            }
        }.runTaskTimer(plugin, 20*2l, 20l);
    }

    public void setTickets(int i, Team team) {
        switch (team) {
            case GREAT_BRITAIN:
                ticketsGreatBritain = i;
                break;
            case GERMANY:
                ticketsGermany = i;
                break;
            case BOTH:
                ticketsGermany = i;
                ticketsGreatBritain = i;
                break;
        }
    }

    public void reduceTicketsBy(int i, Team team) {
        switch (team) {
            case GREAT_BRITAIN:
                ticketsGreatBritain -= i;
                if (ticketsGreatBritain <= 0) {
                    stopGame();
                }
                break;
            case GERMANY:
                ticketsGermany -= i;
                if (ticketsGermany <= 0) {
                    stopGame();
                }
                break;
            case BOTH:
                ticketsGermany -= i;
                ticketsGreatBritain -= i;
                if (ticketsGreatBritain <= 0 && ticketsGermany <= 0) {
                    stopGame();
                }
                break;
        }
    }

    public void updateStats(Player player) {
        if (isGoing()) {
            Scoreboard scoreboard = player.getScoreboard();
            Objective kills = scoreboard.getObjective("stats");
            kills.getScore("Elim.").setScore(getSoldier(player).getKills());
            kills.getScore("Tode").setScore(getSoldier(player).getDeath());
            kills.getScore("E/T").setScore((int) getSoldier(player).getKD());
            kills.getScore("Punkte").setScore(getSoldier(player).getPoints());
        }
    }

    public void setColor(Player p){
        if (isGoing()) {
            Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective kills = sb.registerNewObjective("stats", "dummy", "Statistik");
            kills.setDisplaySlot(DisplaySlot.SIDEBAR);
            Score killsScore = kills.getScore("Elim.");
            Score deathScore = kills.getScore("Tode");
            Score kdscore = kills.getScore("E/T");
            Score pScore = kills.getScore("Punkte");
            killsScore.setScore(getSoldier(p).getKills());
            kdscore.setScore((int) getSoldier(p).getKD());
            pScore.setScore(getSoldier(p).getPoints());
            deathScore.setScore(getSoldier(p).getDeath());
            org.bukkit.scoreboard.Team red = sb.registerNewTeam("red");
            org.bukkit.scoreboard.Team blue = sb.registerNewTeam("blue");

            red.setColor(ChatColor.RED);
            red.setAllowFriendlyFire(false);
            red.setOption(org.bukkit.scoreboard.Team.Option.NAME_TAG_VISIBILITY, org.bukkit.scoreboard.Team.OptionStatus.FOR_OTHER_TEAMS);
            blue.setColor(ChatColor.AQUA);

            blue.setAllowFriendlyFire(false);
            blue.setOption(org.bukkit.scoreboard.Team.Option.NAME_TAG_VISIBILITY, org.bukkit.scoreboard.Team.OptionStatus.FOR_OTHER_TEAMS);

            if (isBrit(p)) {
                //blue.setPrefix("§b");
                // red.setPrefix("§cDE");
                for (UUID uuid : brits) {
                    blue.addEntry(Bukkit.getPlayer(uuid).getDisplayName());
                }
                for (UUID uuid : germans) {
                    red.addEntry(Bukkit.getPlayer(uuid).getDisplayName());
                }
            }
            if (isGerman(p)) {
                //blue.setPrefix("§b");
                //red.setPrefix("§cGB");
                for (UUID uuid : brits) {
                    red.addEntry(Bukkit.getPlayer(uuid).getDisplayName());
                }
                for (UUID uuid : germans) {
                    blue.addEntry(Bukkit.getPlayer(uuid).getDisplayName());
                }
            }
            p.setScoreboard(sb);
        }
    }

    private void startGame() {



        germans.clear();
        brits.clear();
        Bukkit.broadcastMessage("§aRunde beginnt");
        setGameState(GameState.RUNNING_FIGHT);
        ArrayList<UUID> online = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            online.add(player.getUniqueId());
            player.getInventory().addItem(ItemFactory.generateItemStack("Sturmgewehr 44", Material.IRON_PICKAXE));
            player.getInventory().addItem(ItemFactory.generateItemStack("SAIGA", Material.IRON_PICKAXE));
            soldiers.add(new Soldier(player.getUniqueId()));
        }
        Collections.shuffle(online);
        for (UUID uuid : online) {
            germans.add(uuid);
            Bukkit.getPlayer(uuid).teleport(EntryPoints.Points.GERMAN.toLocation());
        }
        int count = online.size() / 2;
        for (int i = 0; i < count; i++) {
            germans.remove(online.get(i));
            brits.add(online.get(i));
            Bukkit.getPlayer(online.get(i)).teleport(EntryPoints.Points.BRIT.toLocation());
        }
        broadcast("Du spielst auf der Seite der Deutschen", Team.GERMANY);
        broadcast("Du spielst auf der Seite der Großbritannier", Team.GREAT_BRITAIN);
        for (Soldier soldier : soldiers) {
            if (soldier.getOfflinePlayer().isOnline()) {
                setColor(soldier.getPlayer());
            }
        }
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Soldier soldier : soldiers) {
                    if (soldier.getOfflinePlayer().isOnline()) {
                        updateStats(soldier.getPlayer());
                        if (isBrit(soldier.getPlayer())) {
                            PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"§3Großbritannien: §b" + ticketsGreatBritain + " §7| §c" + ticketsGermany + " §4:Deutschland\"}"), ChatMessageType.GAME_INFO);
                            ((CraftPlayer) soldier.getPlayer()).getHandle().playerConnection.sendPacket(packetPlayOutChat);
                        }else if (isGerman(soldier.getPlayer())) {
                            PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"§3Deutschland: §b" + ticketsGermany + " §7| §c" + ticketsGreatBritain + " §4:Großbritannien\"}"), ChatMessageType.GAME_INFO);
                            ((CraftPlayer) soldier.getPlayer()).getHandle().playerConnection.sendPacket(packetPlayOutChat);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 5l);
    }

    public void addKills(Player player, int i) {
        Soldier soldier = getSoldier(player);
        soldier.addKills(i);
    }

    public void addDeaths(Player player, int i) {
        Soldier soldier = getSoldier(player);
        soldier.addDeaths(i);
    }

    public void addPoints(Player player, int i) {
        Soldier soldier = getSoldier(player);
        soldier.addPoints(i);
    }

    public void removeSoldier(UUID uuid) {
        if (soldiers.contains(uuid)) {
            if (isGerman(uuid)) {
                germans.remove(uuid);
            }
            if (isBrit(uuid)) {
                brits.remove(uuid);
            }
        }
    }

    public Soldier getSoldier(Player player) {
        for (Soldier soldier : soldiers) {
            if (soldier.getUUID().equals(player.getUniqueId())) {
                return soldier;
            }
        }
        return null;
    }


    public Soldier getSoldier(UUID uuid) {
        for (Soldier soldier : soldiers) {
            if (soldier.getUUID().equals(uuid)) {
                return soldier;
            }
        }
        return null;
    }

    public void startCountdown() {
        setGameState(GameState.WAITING_START);

        new BukkitRunnable() {
            int countdown = 10;
            @Override
            public void run() {
                if (countdown != 0) {
                    if (countdown == 30 || countdown == 20 || countdown == 10 || countdown <= 5 && countdown > 0) {
                        //Bukkit.broadcastMessage("§aDie Runde beginnt in §e" + countdown + " Sekunden");
                    }
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        online.setLevel(countdown);
                    }
                    countdown--;
                }else{
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        online.setLevel(0);
                    }
                    cancel();
                    if (isMinimumMet() && gameState.equals(GameState.WAITING_START)) {
                        startGame();
                    }else{
                        gameState = GameState.WAITING_QUEUE;
                    }
                }

            }
        }.runTaskTimer(plugin, 20*2l, 20l);
    }

}
