package sameplayer.krieg.plugin.Guns;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import sameplayer.krieg.plugin.Classes.Gun;

public class Saiga extends Gun {

    public Saiga() {
        super("SAIGA", "SAIGA", 10, 3, 10, 13, 2, 12);
    }

    @Override
    public void playSound(Location location) {
        World world = location.getWorld();
        world.playSound(location, Sound.ENTITY_IRON_GOLEM_HURT, 10, 2);
        world.playSound(location, Sound.ENTITY_IRON_GOLEM_DEATH, 10, 1);
        world.playSound(location, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 10, 1);
    }
}
