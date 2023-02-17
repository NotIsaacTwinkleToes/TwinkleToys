package me.isaac.twinkletoys.toy.toys.throwable.particlegrenade;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleGrenadeParticle {

    private boolean valid = true;

    Location location;

    final Vector gravity = new Vector(0, -.02, 0);
    private Vector direction;

    public ParticleGrenadeParticle(Location location, Vector direction) {
        this.location = location;
        this.direction = direction;

    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }
}
