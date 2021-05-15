package me.sedattr.loginbar.commands;

import me.sedattr.loginbar.Variables;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player))
            return false;
        Player player = (Player) commandSender;
        if (!player.hasPermission("server.command"))
            return false;

        String type = null;
        if (args.length > 0 && player.hasPermission("server.other"))
            type = args[0];

        Variables.server.send(player, type);
        return true;
    }
}
