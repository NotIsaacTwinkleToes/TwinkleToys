package me.isaac.twinkletoys.events;

import me.isaac.twinkletoys.TwinkleToys;
import me.isaac.twinkletoys.toy.Toy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class UseToy implements Listener {

    final TwinkleToys main;

    public UseToy(TwinkleToys main) {
        this.main = main;
    }

    @EventHandler
    public void onUseToy(PlayerInteractEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) return;

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!item.hasItemMeta() || item.getItemMeta() == null) return;

        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();

        if (!pdc.has(main.keys.toyItem, PersistentDataType.STRING)) return;

        Toy toy = main.getToyFromName(pdc.get(main.keys.toyItem, PersistentDataType.STRING));

        switch (e.getAction()) {
            case LEFT_CLICK_AIR:
                if (player.isSneaking()) toy.sneakLeft(e);
                else toy.leftClick(e);
                break;
            case LEFT_CLICK_BLOCK:
                if (player.isSneaking()) toy.sneakLeftBlock(e);
                else toy.leftBlock(e);
                break;
            case RIGHT_CLICK_AIR:
                if (player.isSneaking()) toy.sneakRight(e);
                else toy.rightClick(e);
                break;
            case RIGHT_CLICK_BLOCK:
                if (player.isSneaking()) toy.sneakRightBlock(e);
                else toy.rightBlock(e);
        }

        e.setCancelled(true);

    }

}
