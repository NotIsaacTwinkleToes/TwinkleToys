package me.isaac.twinkletoys;

import me.isaac.twinkletoys.commands.ToysCommand;
import me.isaac.twinkletoys.events.ClickInToysInventory;
import me.isaac.twinkletoys.events.UseToy;
import me.isaac.twinkletoys.toy.Toy;
import me.isaac.twinkletoys.toy.toys.consumable.floatinglantern.FloatingLantern;
import me.isaac.twinkletoys.toy.toys.consumable.fireworkbox.BoxObj;
import me.isaac.twinkletoys.toy.toys.consumable.fireworkbox.FireworkBox;
import me.isaac.twinkletoys.toy.toys.consumable.fireworkbox.FireworkBoxEvents;
import me.isaac.twinkletoys.toy.toys.throwable.particlegrenade.ParticleGrenade;
import me.isaac.twinkletoys.toy.toys.throwable.particlegrenade.ParticleGrenadeEvents;
import me.isaac.twinkletoys.toy.toys.tools.entitymover.EntityMover;
import me.isaac.twinkletoys.toy.toys.tools.entitymover.EntityMoverEvents;
import me.isaac.twinkletoys.toy.toys.tools.entitymover.MovingSettings;
import me.isaac.twinkletoys.toy.toys.tools.itemlauncher.ItemLauncher;
import me.isaac.twinkletoys.toy.toys.tools.itemlauncher.ItemLauncherEvents;
import me.isaac.twinkletoys.toy.toys.consumable.pushableblocks.PushableBlocks;
import me.isaac.twinkletoys.toy.toys.consumable.pushableblocks.PushableBlocksEvents;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class TwinkleToys extends JavaPlugin {

    public final Keys keys;

    public final List<Toy> toys;

    public final Set<FallingBlock> floatingLights = new HashSet<>();
    public final Set<BoxObj> fireworkBoxs = new HashSet<>();
    public final Set<FallingBlock> pushableBlocks = new HashSet<>();

    public final Map<Block, BlockData> savedBlockData = new HashMap<>();

    public final Map<Player, MovingSettings> movingEntity = new HashMap<>();
    public final Inventory toysInv = Bukkit.createInventory(null, 9 * 1, "Twinkle Toys");

    public final ThreadLocalRandom random = ThreadLocalRandom.current();

    public TwinkleToys() {

        keys = new Keys(this);

        toys = List.of(
                new ParticleGrenade(this),
                new ItemLauncher(this),
                new FloatingLantern(this),
                new EntityMover(this),
                new FireworkBox(this),
                new PushableBlocks(this)
        );

        toys.forEach(toy -> {
            toysInv.addItem(toy.getItemStack());
        });

    }

    @Override
    public void onEnable() {

        registerCommands();
        registerEvents();

    }

    @Override
    public void onDisable() {

        floatingLights.forEach(FallingBlock::remove);
        savedBlockData.forEach(Block::setBlockData);
        fireworkBoxs.forEach(BoxObj::remove);
        pushableBlocks.forEach(Entity::remove);

    }

    public void startMovingEntity(Player player, Entity entity) {
        movingEntity.put(player, new MovingSettings(entity));
        entity.setGravity(false);

        if (movingEntity.size() == 1) movingEntityRunnable();

    }

    private void movingEntityRunnable() {

        new BukkitRunnable() {
            public void run() {
                if (movingEntity.size() == 0) cancel();

                for (Player player : movingEntity.keySet()) {

                    Entity entity = movingEntity.get(player).getEntity();

                    Location targetLocation = player.getEyeLocation().add(player.getLocation().getDirection().multiply(movingEntity.get(player).getDistance()));

                    targetLocation.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, targetLocation, 0);

                    double speed = Math.min((targetLocation.distanceSquared(entity.getLocation())) / 10, 1.5);

                    entity.setVelocity(targetLocation.subtract(entity.getLocation()).toVector().normalize().multiply(speed));

                    if (!entity.isValid()) movingEntity.remove(player);

                }

            }
        }.runTaskTimerAsynchronously(this, 0, 5);

    }

    public Toy getToyFromName(String name) {
        for (Toy toy : toys) {
            if (toy.name().equalsIgnoreCase(name)) return toy;
        }
        throw new NullPointerException("Toy with name " + name + " does not exist!");
    }

    private void registerCommands() {

        getCommand("toys").setExecutor(new ToysCommand(this));

    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new UseToy(this), this);
        pm.registerEvents(new ClickInToysInventory(this), this);

        pm.registerEvents(new ParticleGrenadeEvents(this), this);

        pm.registerEvents(new ItemLauncherEvents(this), this);

        pm.registerEvents(new EntityMoverEvents(this), this);

        pm.registerEvents(new FireworkBoxEvents(this), this);

        pm.registerEvents(new PushableBlocksEvents(), this);

    }

}
