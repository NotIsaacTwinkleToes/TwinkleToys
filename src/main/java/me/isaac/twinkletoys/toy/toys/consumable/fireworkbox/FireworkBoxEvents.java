package me.isaac.twinkletoys.toy.toys.consumable.fireworkbox;

import me.isaac.twinkletoys.TwinkleToys;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.checkerframework.checker.nullness.qual.Nullable;

public class FireworkBoxEvents implements Listener {

    TwinkleToys main;

    public FireworkBoxEvents(TwinkleToys main) {
        this.main = main;
    }

    @EventHandler
    public void onBreakBox(BlockBreakEvent e) {

        BoxObj box = getBox(e.getBlock());

        if (box == null) return;

        main.fireworkBoxs.remove(box);
    }

    @Nullable
    private BoxObj getBox(Block block) {
        for (BoxObj box : main.fireworkBoxs) {
            if (box.block.equals(block)) return box;
        }
        return null;
    }

}
