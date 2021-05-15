package me.sedattr.loginbar.commands;

import me.sedattr.loginbar.Variables;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SendCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.hasPermission("send.command"))
            return false;

        if (args.length < 1)
            return false;
        Player player = Bukkit.getPlayerExact(args[0]);
        if (player == null)
            return false;

        String type = args.length > 1 ? args[1] : null;
        Variables.server.send(player, type);
        return true;
    }
}
