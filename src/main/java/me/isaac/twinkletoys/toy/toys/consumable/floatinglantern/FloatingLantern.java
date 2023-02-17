package me.isaac.twinkletoys.toy.toys.consumable.floatinglantern;

import me.isaac.twinkletoys.TwinkleToys;
import me.isaac.twinkletoys.toy.Toy;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class FloatingLantern implements Toy {

    TwinkleToys main;

    public FloatingLantern(TwinkleToys main) {
        this.main = main;
    }

    @Override
    public String name() {
        return "floating lantern";
    }

    @Override
    public String[] description() {
        return new String[] {
                ChatColor.GRAY + "Spawns a floating lantern companion.",
        };
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(Material.LANTERN);
        ItemMeta itemm = item.getItemMeta();
        assert itemm != null;
        itemm.setDisplayName(ChatColor.WHITE + WordUtils.capitalizeFully(name()));
        itemm.getPersistentDataContainer().set(main.keys.toyItem, PersistentDataType.STRING, name());
        itemm.setLore(Arrays.stream(description()).toList());
        item.setItemMeta(itemm);

        return item;
    }

    @Override
    public void leftClick(Event event) {
        assert event instanceof PlayerInteractEvent;
        summonLight(((PlayerInteractEvent) event).getPlayer(), 0);
        ((PlayerInteractEvent) event).getPlayer().getInventory().getItemInMainHand().setAmount(((PlayerInteractEvent) event).getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
    }

    private void summonLight(Player owner, int currentLife) {

        FallingBlock light = newLight(owner);

        main.floatingLights.add(light);

        new BukkitRunnable() {

            int i = currentLife;
            final int maxLife = 600;

            Vector direction = new Vector();

            Block savedBlock = null;
            BlockData savedData = null;

            @Override
            public void run() {

                randomizeVector(direction);

                if (light.getLocation().distance(owner.getEyeLocation()) > 15)
                    light.teleport(owner);
                else if (light.getLocation().distance(owner.getEyeLocation()) > 4)
                    direction = owner.getEyeLocation().subtract(light.getLocation()).toVector().normalize().multiply(.3);
                else if (light.getLocation().getY() < owner.getEyeLocation().getY() - .5)
                    direction.setY(main.random.nextDouble(.05, .15));

                light.setVelocity(direction);
                light.setTicksLived(1);

                playParticle(light.getLocation());

                if (savedBlock != null) {
                    savedBlock.setBlockData(savedData);
                    main.savedBlockData.remove(savedBlock);
                }

                savedBlock = light.getLocation().getBlock();

                if (!main.savedBlockData.containsKey(savedBlock)) {
                    savedData = savedBlock.getBlockData();
                    main.savedBlockData.put(savedBlock, savedData);

                    savedBlock.setType(Material.LIGHT);
                } else savedBlock = null;

                light.getLocation().getBlock().setType(Material.LIGHT);

                if (!light.isValid()) {
                    main.floatingLights.remove(light);
                    cancel();

                    if (savedBlock != null) {
                        savedBlock.setBlockData(savedData);
                        main.savedBlockData.remove(savedBlock);
                    }

                    summonLight(owner, i);
                    return;
                }

                if (i++ > maxLife || !owner.isValid()) {
                    cancel();
                    Bukkit.getScheduler().runTask(main, light::remove);

                    if (savedBlock != null) {
                        savedBlock.setBlockData(savedData);
                        main.savedBlockData.remove(savedBlock);
                    }

                    main.floatingLights.remove(light);
                }

            }
        }.runTaskTimer(main, 0, 10);

    }

    public Vector randomizeVector(Vector vector) {
        vector.setX(main.random.nextDouble(-1, 1));
        vector.setY(main.random.nextDouble(-1, 1));
        vector.setZ(main.random.nextDouble(-1, 1));
        vector.normalize().multiply(.1);
        return vector;
    }

    private FallingBlock newLight(Player owner) {
        Lantern lantern = (Lantern) Material.LANTERN.createBlockData();

        lantern.setHanging(true);

        FallingBlock light = owner.getWorld().spawnFallingBlock(owner.getEyeLocation(), lantern);

        light.setDropItem(false);
        light.setGravity(false);

        return light;

    }

    private void playParticle(Location location) {

        if (location.getWorld() == null) return;

        Vector direction = randomizeVector(new Vector());

        location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 0, direction.getX(), direction.getY(), direction.getZ());

    }

}
