package me.isaac.twinkletoys.toy.toys.consumable.fireworkbox;

import me.isaac.twinkletoys.TwinkleToys;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class BoxObj {

    final TwinkleToys main;

    final Block block;

    int life = 20 * 60;

    public BoxObj(TwinkleToys main, Block block) {
        this.main = main;
        this.block = block;
        block.setType(Material.TNT);

        BoxObj boxInstance = this;

        main.fireworkBoxs.add(this);

        new BukkitRunnable() {

            int burstShotTime = randomBurstShotTime();

            boolean valid = true;
            public void run() {

                if ((life + 1) % 30 == 0) shootFirework();

                if (life > 75 && life == burstShotTime) {
                    for (int i = 0; i < main.random.nextInt(4, 10); i++) {
                        Bukkit.getScheduler().runTaskLater(main, r -> {
                            shootFirework();
                        }, i + (main.random.nextInt(1, 4)));
                    }
                    burstShotTime = randomBurstShotTime();
                }

                if (--life <= 0) valid = false;

                if (!main.fireworkBoxs.contains(boxInstance)) valid = false;

                if (!valid) {
                    cancel();
                    remove();
                }

            }
        }.runTaskTimer(main, 0, 1);

    }

    public void remove() {
        block.setType(Material.AIR);
    }

    private void shootFirework() {
        block.getWorld().spawn(block.getLocation().add(.5, 1, .5), Firework.class, e -> {
            e.setFireworkMeta(randomizeFireworkMeta(e.getFireworkMeta()));
            e.setVelocity(randomDirection().normalize().multiply(.02));
        });
    }

    private int randomBurstShotTime() {
        return main.random.nextInt((int) Math.ceil(life * .4), life);
    }

    private FireworkMeta randomizeFireworkMeta(FireworkMeta meta) {
        meta.clearEffects();
        meta.setPower(main.random.nextInt(2) + 1);

        int effects = main.random.nextInt(1, 3);

        for (int i = 0; i < effects; i++) {
            meta.addEffect(FireworkEffect.builder().with(getType()).flicker(randomBoolean()).trail(randomBoolean()).withColor(getColors()).withFade(getColors()).build());
        }

        return meta;
    }

    private boolean randomBoolean() {
        return main.random.nextInt(2) == 0;
    }

    private Set<Color> getColors() {
        Set<Color> set = new HashSet<>();

        do {
            set.add(getColor());
        } while(randomBoolean());

        return set;
    }

    private FireworkEffect.Type getType() {
        return FireworkEffect.Type.values()[main.random.nextInt(FireworkEffect.Type.values().length)];
    }

    private Color getColor() {
        return Color.fromRGB(main.random.nextInt(256), main.random.nextInt(256), main.random.nextInt(256));
    }

    private Vector randomDirection() {
        final int bound = 1;
        double x = main.random.nextDouble(-bound, bound),
                y = main.random.nextDouble(bound),
                z = main.random.nextDouble(-bound, bound);

        return new Vector(x, y, z);
    }

}
