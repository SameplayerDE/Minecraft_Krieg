package sameplayer.krieg.plugin.Enums;

import org.bukkit.Location;
import sameplayer.krieg.plugin.Main;

public class EntryPoints {

    private static final Main plugin = Main.getInstance();

    public enum Points {

        LOBBY(plugin.configToLocation("Wartebereich")),
        GERMAN(plugin.configToLocation("Deutschen.start")),
        BRIT(plugin.configToLocation("Großbritannien.start")),
        DEAD(plugin.configToLocation("Tot")),
        WIN(plugin.configToLocation("Sieger"));


        private Location location;


        Points(Location location) {
            this.location = location;
        }

        public Location toLocation() {
            return location;
        }

    }

}
