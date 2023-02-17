package me.isaac.twinkletoys.toy.toys.tools.entitymover;

import me.isaac.twinkletoys.TwinkleToys;
import me.isaac.twinkletoys.toy.Toy;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;

public class EntityMover implements Toy {

    TwinkleToys main;

    public EntityMover(TwinkleToys main) {
        this.main = main;
    }

    @Override
    public String name() {
        return "entity mover";
    }

    @Override
    public String[] description() {
        return new String[] {
                ChatColor.GRAY + "Interact with an entity to pick it up.",
                ChatColor.GRAY + "Right-Clicking will drop the entity.",
                ChatColor.GRAY + "Left-Clicking launches the entity.",
                ChatColor.GRAY + "Scrolling will change how far the entity is.",
                ChatColor.GRAY + "Putting the stick in your off hand and scrolling will change the launch force."
        };
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(Material.STICK);
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
        Player player;
        if (event instanceof PlayerInteractEvent)
            player = ((PlayerInteractEvent) event).getPlayer();
        else if (event instanceof EntityDamageByEntityEvent)
            player = (Player) ((EntityDamageByEntityEvent) event).getDamager();
        else return;


        Entity entity;

        if (main.movingEntity.containsKey(player)) {
            MovingSettings settings = main.movingEntity.remove(player);
            entity = settings.getEntity();
            entity.setGravity(true);
            entity.setFallDistance(0);
            entity.setVelocity(player.getLocation().getDirection().multiply(settings.getForce()));
            return;
        }

        entity = getTargetEntity(player, 30);

        if (entity == null) return;

        main.startMovingEntity(player, entity);
    }

    @Override
    public void rightClick(Event event) {
        assert event instanceof PlayerInteractEvent;
        Player player = ((PlayerInteractEvent) event).getPlayer();

        Entity entity;

        if (main.movingEntity.containsKey(player)) {
            entity = main.movingEntity.remove(player).getEntity();
            entity.setVelocity(new Vector(0, 0, 0));
            entity.setGravity(true);
            entity.setFallDistance(0);
            return;
        }

        entity = getTargetEntity(player, 30);

        if (entity == null) return;

        main.startMovingEntity(player, entity);
    }

    @Nullable
    public Entity getTargetEntity(Player player, double distance) {

        RayTraceResult result = player.getWorld().rayTrace(player.getEyeLocation(),
                player.getLocation().getDirection(),
                distance,
                FluidCollisionMode.NEVER,
                true,
                2,
                p -> !p.equals(player));

        if (result == null) return null;

        return result.getHitEntity();
    }

}
