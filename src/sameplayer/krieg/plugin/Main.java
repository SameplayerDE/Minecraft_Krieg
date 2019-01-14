package sameplayer.krieg.plugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import sameplayer.krieg.plugin.Commands.CommandSetup;
import sameplayer.krieg.plugin.Enums.GameState;
import sameplayer.krieg.plugin.Listeners.*;
import sameplayer.krieg.plugin.Menus.MenuClasses;
import sameplayer.menuapi.plugin.MenuManager;

public class Main extends JavaPlugin {

    private static Main m;
    private static MenuManager menuManager;
    private static PluginMessenger pluginMessenger;
    private static KriegManager krieg;

    @Override
    public void onEnable() {

        m = this;
        pluginMessenger = new PluginMessenger();
        krieg = new KriegManager(this);
        menuManager = new MenuManager(this);

        regsiterCommands();
        regsiterListeners();
        regsiterMessengers();

        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();

        new BukkitRunnable() {

            @Override
            public void run() {

                if (!getConfig().getBoolean("Abgeschlossen")) {
                    krieg.setGameState(GameState.SETUP);
                }else{
                    krieg.setGameState(GameState.WAITING_QUEUE);
                }

            }
        }.runTaskLater(this, 20l);

    }

    private void regsiterCommands() {
        new CommandSetup(this);
    }

    private void regsiterListeners() {
        new ListenerPlayerJoin(this);
        new ListenerEntityDamageByEntity(this);
        new ListenerProjectileLaunch(this);
        new ListenerPlayerInteract(this);
        new ListenerPlayerShot(this);
        new ListenerPlayerWarDeath(this);
        new ListenerPlayerMove(this);

        menuManager.addMenu("Klassen", new MenuClasses(this));

    }

    private void regsiterMessengers() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", pluginMessenger);
    }

    public static Main getInstance() {
        return m;
    }

    //UTILS GETTER SETTER

    public static KriegManager getKrieg() {
        return krieg;
    }

    public static MenuManager getMenuManager() {
        return menuManager;
    }

    public static Location configToLocation(String path) {

        double x, y, z;
        float yaw, pitch;
        String world;

        x = Main.getInstance().getConfig().getDouble(path + ".X");
        y = Main.getInstance().getConfig().getDouble(path + ".Y");
        z = Main.getInstance().getConfig().getDouble(path + ".Z");

        yaw = (float) Main.getInstance().getConfig().getDouble(path + ".Gierung");
        pitch = (float) Main.getInstance().getConfig().getDouble(path + ".Neigung");

        world = Main.getInstance().getConfig().getString(path + ".Welt");

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);


    }

    //

}
