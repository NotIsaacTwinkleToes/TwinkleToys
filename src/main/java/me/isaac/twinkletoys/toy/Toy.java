package me.isaac.twinkletoys.toy;

import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public interface Toy {

    /**
     * @return name saved to itemstacks for further use
     */
    String name();
    String[] description();

    ItemStack getItemStack();

    void leftClick(Event event);

    default void sneakLeft(Event event) {
        leftClick(event);
    }

    default void leftBlock(Event event) {
        leftClick(event);
    }

    default void sneakLeftBlock(Event event) {
        leftBlock(event);
    }

    default void rightClick(Event event) {
        leftClick(event);
    }

    default void sneakRight(Event event) {
        rightClick(event);
    }

    default void rightBlock(Event event) {
        rightClick(event);
    }

    default void sneakRightBlock(Event event) {
        rightBlock(event);
    }

}
