package me.isaac.twinkletoys.toy.toys.consumable.fireworkbox;

import me.isaac.twinkletoys.TwinkleToys;
import me.isaac.twinkletoys.toy.Toy;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class FireworkBox implements Toy {

    TwinkleToys main;

    public FireworkBox(TwinkleToys main) {
        this.main = main;
    }

    @Override
    public String name() {
        return "firework box";
    }

    @Override
    public String[] description() {
        return new String[] {
                ChatColor.GRAY + "When placed fireworks will spawn from the box for a minute-ish",
                ChatColor.GRAY + "A random amount of fireworks are shot, always ending in a \"finale\".",
                ChatColor.GRAY + "     enjoy :)"
        };
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(Material.TNT);
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
        if (((PlayerInteractEvent) event).getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = ((PlayerInteractEvent) event).getClickedBlock().getRelative(((PlayerInteractEvent) event).getBlockFace());

        new BoxObj(main, block);

    }
}
