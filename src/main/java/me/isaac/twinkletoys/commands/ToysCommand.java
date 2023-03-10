package me.isaac.twinkletoys.commands;

import me.isaac.twinkletoys.TwinkleToys;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToysCommand implements CommandExecutor {

    final TwinkleToys main;

    public ToysCommand(TwinkleToys main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        player.openInventory(main.toysInv);

        return true;
    }

}
