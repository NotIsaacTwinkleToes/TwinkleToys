package me.isaac.twinkletoys.toy.toys.tools.itemlauncher;

import me.isaac.twinkletoys.TwinkleToys;
import me.isaac.twinkletoys.toy.Toy;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class ItemLauncher implements Toy {

    final TwinkleToys main;

    public ItemLauncher(TwinkleToys main) {
        this.main = main;
    }

    @Override
    public String name() {
        return "item launcher";
    }

    @Override
    public String[] description() {
        return new String[] {
                ChatColor.GRAY + "Shoots harmless items.",
                ChatColor.GRAY + "Any item in off hand is shot first.",
                ChatColor.GRAY + "Block type under the player will be shot if not holding an item.",
                ChatColor.GRAY + "Random material is chosen if the player is not walking on a block,",
                ChatColor.GRAY + " and is not holding an item."};
    }

    @Override
    public ItemStack getItemStack() {

        ItemStack item = new ItemStack(Material.BLAZE_ROD);
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

        ItemStack item;

        Player player = ((PlayerInteractEvent) event).getPlayer();

        if (player.getInventory().getItemInOffHand().getType() != Material.AIR) item = player.getInventory().getItemInOffHand();
        else if (player.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) item = new ItemStack(player.getLocation().subtract(0, 1, 0).getBlock().getType());
        else item = new ItemStack(Material.values()[ThreadLocalRandom.current().nextInt(Material.values().length)]);

        player.getWorld().spawn(player.getEyeLocation(), Snowball.class, ball -> {
            ball.setItem(item);
            ball.setVelocity(player.getLocation().getDirection().multiply(1.5));
            ball.getPersistentDataContainer().set(main.keys.toy, PersistentDataType.STRING, name());
        });

    }

}
