package me.sedattr.loginbar;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SendCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.hasPermission("send.admin"))
            return false;

        if (args.length < 2)
            return false;

        Player player = Bukkit.getPlayerExact(args[0]);
        if (player == null)
            return false;

        String type = args[1];
        LoginBar.getInstance().sendToServer(player, type);

        return true;
    }
}
