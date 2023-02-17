package me.isaac.twinkletoys.events;

import me.isaac.twinkletoys.TwinkleToys;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickInToysInventory implements Listener {

    final TwinkleToys main;

    public ClickInToysInventory(TwinkleToys main) {
        this.main = main;
    }

    @EventHandler
    public void onClickInInventory(InventoryClickEvent e) {
        if (!e.getView().getTopInventory().equals(main.toysInv)) return;
        e.setCancelled(true);

        if (e.getCurrentItem() != null && e.getWhoClicked().getInventory().firstEmpty() != -1) e.getWhoClicked().getInventory().addItem(e.getCurrentItem());

    }

}
