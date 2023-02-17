package me.isaac.twinkletoys.toy.toys.consumable.pushableblocks;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class PushableBlocksEvents implements Listener {

    @EventHandler
    public void onBlockBreak(EntityChangeBlockEvent e) {

        if (e.getTo() == Material.AIR) return;

        e.setCancelled(true);

    }

}
