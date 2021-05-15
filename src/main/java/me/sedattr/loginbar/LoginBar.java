package me.sedattr.loginbar;

import me.sedattr.loginbar.commands.SendCommand;
import me.sedattr.loginbar.commands.ServerCommand;
import me.sedattr.loginbar.helpers.ActionBar;
import me.sedattr.loginbar.helpers.BossBar;
import me.sedattr.loginbar.helpers.Server;
import me.sedattr.loginbar.helpers.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class LoginBar extends JavaPlugin {
    public void loadVariables() {
        Variables.config = getConfig();

        Variables.title = new Title();
        Variables.actionBar = new ActionBar();
        Variables.bossBar = !Bukkit.getVersion().contains("1.8") ? new BossBar() : null;
        Variables.server = new Server(this);
    }

    public void onEnable() {
        saveDefaultConfig();
        loadVariables();

        if (!Bukkit.getPluginManager().isPluginEnabled("AuthMe"))
            return;

        new Events(this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        PluginCommand send = getCommand("send");
        if (send != null)
            send.setExecutor(new SendCommand());

        PluginCommand server = getCommand("server");
        if (server != null)
            server.setExecutor(new ServerCommand());

        Bukkit.getConsoleSender().sendMessage("§8[§bLoginBar§8] §aPlugin enabled! Made by SedatTR#8666.");
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§8[§bLoginBar§8] §cPlugin disabled!");
    }
}
