package me.isaac.twinkletoys.toy.toys.consumable.pushableblocks;

import me.isaac.twinkletoys.TwinkleToys;
import me.isaac.twinkletoys.toy.Toy;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class PushableBlocks implements Toy {

    final TwinkleToys main;

    public PushableBlocks(TwinkleToys main) {
        this.main = main;
    }

    @Override
    public String name() {
        return "pushable block";
    }

    @Override
    public String[] description() {
        return new String[] {
                ChatColor.GRAY + "A temporary block that when touched",
                ChatColor.GRAY + "by an entity is launched away."};
    }

    @Override
    public ItemStack getItemStack() {

        ItemStack item = new ItemStack(Material.SHULKER_SHELL);
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
        PlayerInteractEvent e = (PlayerInteractEvent) event;

        Block clickedBlock = e.getClickedBlock();

        if (clickedBlock == null) return;

        World world = clickedBlock.getWorld();

        FallingBlock fallingBlock = world.spawnFallingBlock(clickedBlock.getLocation(), clickedBlock.getBlockData());

        clickedBlock.setType(Material.AIR);

        newRunnable(fallingBlock);

    }

    private void newRunnable(FallingBlock block) {

        main.pushableBlocks.add(block);

        block.setDropItem(false);
        block.setHurtEntities(false);
        block.setGravity(false);

        new BukkitRunnable() {
            @Override
            public void run() {

                block.setTicksLived(1);

                for (Entity entity : block.getNearbyEntities(.2, .2, .2)) {

                    Vector velocity = block.getLocation().subtract(entity.getLocation()).toVector();

                    velocity.add(new Vector(0, .5, 0));

                    double length = entity.getVelocity().length() * 2 + .4;

                    block.setGravity(true);
                    block.setVelocity(velocity.normalize().multiply(length));

                }

                if (!block.isValid()) {
                    cancel();

                    Location location = block.getLocation();

                    while (!location.clone().subtract(0, .2, 0).getBlock().getType().isAir())
                        location.add(0, .2, 0);

                    newRunnable(block.getWorld().spawnFallingBlock(location, block.getBlockData()));
                    main.pushableBlocks.remove(block);
                }
            }
        }.runTaskTimer(main, 0, 1);

    }

}
