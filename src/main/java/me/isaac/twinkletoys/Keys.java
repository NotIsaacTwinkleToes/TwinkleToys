package me.isaac.twinkletoys;

import org.bukkit.NamespacedKey;

public class Keys {

    public final NamespacedKey toyItem, toy;

    public Keys(TwinkleToys main) {

        toyItem = new NamespacedKey(main, "toy_item");
        toy = new NamespacedKey(main, "toy");

    }

}
