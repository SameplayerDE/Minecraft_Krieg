package sameplayer.krieg.plugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import sameplayer.krieg.plugin.Enums.GameState;
import sameplayer.krieg.plugin.KriegManager;
import sameplayer.krieg.plugin.Main;

import java.util.LinkedHashMap;
import java.util.UUID;

public class CommandSetup implements CommandExecutor {

    private Main plugin;
    private KriegManager krieg;
    private LinkedHashMap<UUID, Integer> setup = new LinkedHashMap<>();

    public CommandSetup(Main plugin) {
        this.plugin = plugin;
        this.krieg = this.plugin.getKrieg();
        plugin.getCommand("konfiguration").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player) commandSender;

        if (!player.hasPermission("war.setup")) {

            return true;

        }

        if (strings.length == 0) {

            if (!setup.containsKey(player.getUniqueId())) {

                setup.put(player.getUniqueId(), 0);

                if (!krieg.getGameState().equals(GameState.SETUP)) {
                    krieg.setGameState(GameState.SETUP);
                    player.setGameMode(GameMode.CREATIVE);
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (!online.hasPermission("war.setup")) {
                            online.kickPlayer("§aDer Dienst ist nicht Spielbereit!");
                        }
                    }
                }

                player.sendMessage("§6Konfiguration beginnt nun!");
                player.sendMessage("1. Schritt: Bestimme den Einstiegspunkt des Wartebereichs");
                return true;

            } else {

                int step = setup.get(player.getUniqueId());

                if (step == 0) {

                    Main.getInstance().getConfig().set("Wartebereich.X", player.getLocation().getX());
                    Main.getInstance().getConfig().set("Wartebereich.Y", player.getLocation().getY());
                    Main.getInstance().getConfig().set("Wartebereich.Z", player.getLocation().getZ());
                    Main.getInstance().getConfig().set("Wartebereich.Welt", player.getLocation().getWorld().getName());
                    Main.getInstance().getConfig().set("Wartebereich.Gierung", player.getLocation().getYaw());
                    Main.getInstance().getConfig().set("Wartebereich.Neigung", player.getLocation().getPitch());

                    player.sendMessage("§61. Schritt wurde abgeschlossen!");
                    player.sendMessage("2. Schritt: Bestimme den Einstiegspunkt der Deutschen!");
                    setup.replace(player.getUniqueId(), 1);
                    return true;

                }

                if (step == 1) {

                    Main.getInstance().getConfig().set("Deutschen.start.X", player.getLocation().getX());
                    Main.getInstance().getConfig().set("Deutschen.start.Y", player.getLocation().getY());
                    Main.getInstance().getConfig().set("Deutschen.start.Z", player.getLocation().getZ());
                    Main.getInstance().getConfig().set("Deutschen.start.Welt", player.getLocation().getWorld().getName());
                    Main.getInstance().getConfig().set("Deutschen.start.Gierung", player.getLocation().getYaw());
                    Main.getInstance().getConfig().set("Deutschen.start.Neigung", player.getLocation().getPitch());

                    player.sendMessage("§62. Schritt wurde abgeschlossen!");
                    player.sendMessage("3. Schritt: Bestimme den Einstiegspunkt der Großritannier!");
                    setup.replace(player.getUniqueId(), 2);
                    return true;

                }

                if (step == 2) {

                    Main.getInstance().getConfig().set("Großbritannien.start.X", player.getLocation().getX());
                    Main.getInstance().getConfig().set("Großbritannien.start.Y", player.getLocation().getY());
                    Main.getInstance().getConfig().set("Großbritannien.start.Z", player.getLocation().getZ());
                    Main.getInstance().getConfig().set("Großbritannien.start.Welt", player.getLocation().getWorld().getName());
                    Main.getInstance().getConfig().set("Großbritannien.start.Gierung", player.getLocation().getYaw());
                    Main.getInstance().getConfig().set("Großbritannien.start.Neigung", player.getLocation().getPitch());

                    player.sendMessage("§63. Schritt wurde abgeschlossen!");
                    player.sendMessage("4. Schritt: Bestimme den Einstiegspunkt der Toten!");
                    setup.replace(player.getUniqueId(), 3);
                    return true;

                }

                if (step == 3) {

                    Main.getInstance().getConfig().set("Tot.X", player.getLocation().getX());
                    Main.getInstance().getConfig().set("Tot.Y", player.getLocation().getY());
                    Main.getInstance().getConfig().set("Tot.Z", player.getLocation().getZ());
                    Main.getInstance().getConfig().set("Tot.Welt", player.getLocation().getWorld().getName());
                    Main.getInstance().getConfig().set("Tot.Gierung", player.getLocation().getYaw());
                    Main.getInstance().getConfig().set("Tot.Neigung", player.getLocation().getPitch());

                    player.sendMessage("§64. Schritt wurde abgeschlossen!");
                    player.sendMessage("5. Schritt: Bestimme den Einstiegspunkt der Sieger!");
                    setup.replace(player.getUniqueId(), 4);
                    return true;

                }

                if (step == 4) {

                    Main.getInstance().getConfig().set("Sieger.X", player.getLocation().getX());
                    Main.getInstance().getConfig().set("Sieger.Y", player.getLocation().getY());
                    Main.getInstance().getConfig().set("Sieger.Z", player.getLocation().getZ());
                    Main.getInstance().getConfig().set("Sieger.Welt", player.getLocation().getWorld().getName());
                    Main.getInstance().getConfig().set("Sieger.Gierung", player.getLocation().getYaw());
                    Main.getInstance().getConfig().set("Sieger.Neigung", player.getLocation().getPitch());

                    player.sendMessage("§65. Schritt wurde abgeschlossen!");
                    player.sendMessage("§66 Schritt: §fSchließe die Konfiguration ab!");
                    setup.replace(player.getUniqueId(), 5);
                    return true;

                }

                if (step == 5) {

                    //player.sendMessage("§66 Schritt abgschlossen!");
                    plugin.getConfig().set("Abgeschlossen", true);
                    plugin.saveConfig();
                    setup.remove(player.getUniqueId());
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        online.kickPlayer("§66 Schritt abgschlossen!");
                    }
                    krieg.setGameState(GameState.WAITING_QUEUE);
                    return true;

                }

            }

        }

        return false;

    }

}
