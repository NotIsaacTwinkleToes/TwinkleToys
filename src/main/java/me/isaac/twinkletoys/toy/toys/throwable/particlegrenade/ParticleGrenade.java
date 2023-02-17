package me.isaac.twinkletoys.toy.toys.throwable.particlegrenade;

import me.isaac.twinkletoys.TwinkleToys;
import me.isaac.twinkletoys.toy.Toy;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class ParticleGrenade implements Toy {

    final TwinkleToys main;

    ItemStack item = new ItemStack(Material.CREEPER_SPAWN_EGG);

    public ParticleGrenade(TwinkleToys main) {
        this.main = main;

        ItemMeta itemm = item.getItemMeta();
        assert itemm != null;
        itemm.setDisplayName(ChatColor.WHITE + WordUtils.capitalizeFully(name()));
        itemm.getPersistentDataContainer().set(main.keys.toyItem, PersistentDataType.STRING, name());
        itemm.setLore(Arrays.stream(description()).toList());
        item.setItemMeta(itemm);

    }

    @Override
    public String name() {
        return "particle grenade";
    }

    @Override
    public String[] description() {
        return new String[] {
                ChatColor.GRAY + "Throws a creeper egg and upon impact,",
                ChatColor.GRAY + "explodes into many particles."
        };
    }

    @Override
    public ItemStack getItemStack() {
        return item.clone();
    }

    @Override
    public void leftClick(Event event) {
        assert event instanceof PlayerInteractEvent;
        ((PlayerInteractEvent) event).setCancelled(false);
    }

    @Override
    public void leftBlock(Event event) {
        assert event instanceof PlayerInteractEvent;
        ((PlayerInteractEvent) event).setCancelled(false);
    }

    @Override
    public void rightBlock(Event event) {
        assert event instanceof PlayerInteractEvent;
        assert ((PlayerInteractEvent) event).getClickedBlock() != null;

        Location blockLocation = ((PlayerInteractEvent) event).getClickedBlock().getLocation();

        double x = blockLocation.getX() + .5, y = blockLocation.getY() + .5, z = blockLocation.getZ() + .5;

        switch (((PlayerInteractEvent) event).getBlockFace()) {
            case SOUTH -> // +z
                    z += 1;
            case NORTH -> z -= 1;
            case WEST -> // -x
                    x += -1;
            case EAST -> // +x
                    x += 1;
            case DOWN -> y += -2;
            case UP -> y += 1;
            default -> {
            }
        }

        blockLocation.getWorld().spawn(new Location(blockLocation.getWorld(), x, y, z), Creeper.class, creeper -> {
            creeper.getPersistentDataContainer().set(main.keys.toy, PersistentDataType.STRING, name());
        });
    }

    @Override
    public void rightClick(Event event) {

        Player player = ((PlayerInteractEvent) event).getPlayer();

        player.getWorld().spawn(player.getEyeLocation(), Snowball.class, s -> {
            Arrow arrow = player.getWorld().spawnArrow(player.getEyeLocation(), player.getEyeLocation().getDirection(), 1, 6);
            s.setVelocity(arrow.getVelocity());
            arrow.remove();
            s.setItem(item);
            s.setBounce(true);
            s.getPersistentDataContainer().set(main.keys.toy, PersistentDataType.STRING, name());
        });
    }
}
