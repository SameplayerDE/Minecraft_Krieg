package sameplayer.krieg.plugin.Guns;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import sameplayer.krieg.plugin.Classes.Gun;

public class STG44 extends Gun {

    public STG44() {
        super("Sturmgewehr 44", "StG44", 6, 4, 40, 80, 9, 30);
    }

    @Override
    public void playSound(Location location) {
        World world = location.getWorld();
        world.playSound(location, Sound.ENTITY_IRON_GOLEM_HURT, 10, 2);
        world.playSound(location, Sound.ENTITY_SKELETON_HURT, 10, 2);
        world.playSound(location, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 10, 2);
    }
}
