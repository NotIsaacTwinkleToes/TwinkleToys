package me.isaac.twinkletoys.toy.toys.tools.entitymover;

import me.isaac.twinkletoys.TwinkleToys;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class EntityMoverEvents implements Listener {

    TwinkleToys main;

    public EntityMoverEvents(TwinkleToys main) {
        this.main = main;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.FALL && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

        boolean cancel = false;

        for (MovingSettings value : main.movingEntity.values()) {
            if (value.getEntity().equals(e.getEntity())) {
                cancel = true;
                break;
            }
        }
        e.setCancelled(cancel);
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent e) {
        boolean cancel = false;

        for (MovingSettings value : main.movingEntity.values()) {
            if (value.getEntity().equals(e.getEntity())) {
                cancel = true;
                break;
            }
        }
        e.setCancelled(cancel);
    }

    @EventHandler
    public void onHitCarriedEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;
        if (!main.movingEntity.containsKey(player)) return;


        boolean cancel = false;

        for (MovingSettings value : main.movingEntity.values()) {
            if (value.getEntity().equals(e.getEntity())) {
                cancel = true;
                break;
            }
        }
        if (!cancel) return;
        e.setCancelled(true);
        e.setDamage(0);
        main.getToyFromName("entity mover").leftClick(e);
    }

    @EventHandler
    public void onScroll(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        if (!main.movingEntity.containsKey(player)) return;

        PlayerInventory inv = player.getInventory();

        boolean inOffHand = inv.getItemInOffHand().isSimilar(main.getToyFromName("entity mover").getItemStack());

        if (!inOffHand) {
            ItemStack tempItem = inv.getItem(e.getNewSlot());
            inv.setItem(e.getNewSlot(), inv.getItem(e.getPreviousSlot()));
            inv.setItem(e.getPreviousSlot(), tempItem);
        }

        MovingSettings settings = main.movingEntity.get(player);

        switch (e.getPreviousSlot() - e.getNewSlot()) {
            case 1, -8 -> {
                if (inOffHand)
                    settings.setForce(Math.min(20, settings.getForce() + 1));
                else
                    settings.setDistance(Math.min(20, settings.getDistance() + (player.isSneaking() ? .25 : 1)));
            }

            case -1, 8 -> {
                if (inOffHand)
                    settings.setForce(Math.max(0, settings.getForce() - 1));
                else
                    settings.setDistance(Math.max(1, settings.getDistance() - (player.isSneaking() ? .25 : 1)));
            }
            default -> {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "Scroll slower to move the entity"));
                return;
            }
        }

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.LIGHT_PURPLE + (inOffHand ? "Force: " + settings.getForce() : "Distance: " + settings.getDistance())));
    }

}
