package me.isaac.twinkletoys.toy.toys.tools.entitymover;

import org.bukkit.entity.Entity;

public class MovingSettings {

    private final Entity entity;
    private double distance = 4, force = 3;

    public MovingSettings(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getForce() {
        return force;
    }

    public void setForce(double force) {
        this.force = force;
    }
}
