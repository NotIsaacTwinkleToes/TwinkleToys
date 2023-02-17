package me.isaac.twinkletoys.toy.toys.throwable.particlegrenade;

import me.isaac.twinkletoys.TwinkleToys;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ParticleGrenadeEvents implements Listener {

    final TwinkleToys main;

    public ParticleGrenadeEvents(TwinkleToys main) {
        this.main = main;
    }

    final double bound = 1;

    @EventHandler
    public void onGrenadeLand(ProjectileHitEvent e) {
        if (!e.getEntity().getPersistentDataContainer().has(main.keys.toy, PersistentDataType.STRING)) return;
        if (!e.getEntity().getPersistentDataContainer().get(main.keys.toy, PersistentDataType.STRING).equals("particle grenade")) return;

        playParticles(e.getEntity().getLocation());
        e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 1);

    }

    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent e) {
        PersistentDataContainer pdc = e.getEntity().getPersistentDataContainer();

        if (!pdc.has(main.keys.toy, PersistentDataType.STRING)) return;
        if (!pdc.get(main.keys.toy, PersistentDataType.STRING).equals("particle grenade")) return;

        playParticles(e.getEntity().getLocation());

    }

    private void playParticles(Location location) {
        double x, y, z;

        List<ParticleGrenadeParticle> particles = new ArrayList<>();

        for (int i = 0; i < 150; i++) {

            x = main.random.nextDouble(-bound, bound);
            y = main.random.nextDouble(-bound, bound);
            z = main.random.nextDouble(-bound, bound);

            particles.add(new ParticleGrenadeParticle(location.clone(), new Vector(x, y, z)));
        }

        new BukkitRunnable() {
            int i = 0;

            Particle displayParticle;
            boolean flame = true;

            public void run() {

                if (particles.size() == 0) cancel();

                i++;

                for (int particleIndex = 0; particleIndex < particles.size(); particleIndex++) {

                    ParticleGrenadeParticle particle = particles.get(particleIndex);

                    if (particle.isValid() && Math.abs(particle.getDirection().getX()) < .01 && Math.abs(particle.getDirection().getZ()) < .01)
                        particle.setValid(false);

                    if (particle.isValid() && i > 500)
                        particle.setValid(false);

                    if (particle.isValid() && i > 4 && !particle.location.getBlock().getType().isAir())
                        particle.setValid(false);

                    if (!particle.isValid())
                        particles.remove(particleIndex);

                    particle.getDirection().add(particle.gravity);

                    particle.location.add(particle.getDirection().normalize().multiply(.3));

                    displayParticle = flame ? Particle.FLAME : Particle.SMOKE_NORMAL;
                    flame = !flame;

                    location.getWorld().spawnParticle(displayParticle, particle.location, 0);

                }
            }
        }.runTaskTimerAsynchronously(main, 0, 1);

    }

}
