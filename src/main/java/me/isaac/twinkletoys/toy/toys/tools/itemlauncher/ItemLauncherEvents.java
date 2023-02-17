package me.isaac.twinkletoys.toy.toys.tools.itemlauncher;

import me.isaac.twinkletoys.TwinkleToys;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

public class ItemLauncherEvents implements Listener {

    TwinkleToys main;

    public ItemLauncherEvents(TwinkleToys main) {
        this.main = main;
    }

    @EventHandler
    public void onItemHit(ProjectileHitEvent e) {
        if (!(e.getEntity() instanceof Snowball snowball)) return;

        PersistentDataContainer pdc = snowball.getPersistentDataContainer();

        if (!pdc.has(main.keys.toy, PersistentDataType.STRING)) return;
        if (!pdc.get(main.keys.toy, PersistentDataType.STRING).equals("item launcher")) return;

        for (int i = 0; i < 100; i++) {

            double x = main.random.nextDouble(-1, 1),
                    y = main.random.nextDouble(-1, 1),
                    z = main.random.nextDouble(-1, 1);

            e.getEntity().getWorld().spawn(e.getEntity().getLocation(), Snowball.class, ball -> {
                ball.setItem(snowball.getItem());
                ball.setVelocity(new Vector(x, y, z).multiply(.3));
            });

        }

    }

}
