package me.sedattr.loginbar;

import me.sedattr.loginbar.actionbar.ActionBar1_8;
import me.sedattr.loginbar.actionbar.ActionBar1_10;
import me.sedattr.loginbar.commands.SendCommand;
import me.sedattr.loginbar.commands.ServerCommand;
import me.sedattr.loginbar.helpers.BossBar;
import me.sedattr.loginbar.helpers.Server;
import me.sedattr.loginbar.title.Title1_8;
import me.sedattr.loginbar.title.Title1_11;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class LoginBar extends JavaPlugin {
    public void loadVariables() {
        Variables.config = getConfig();
        Variables.bungeecord = Variables.config.getBoolean("bungeecord");
        if (Variables.bungeecord)
            Variables.server = new Server(this);

        String version = Bukkit.getVersion();

        Variables.title = version.contains("1.7") || version.contains("1.8") || version.contains("1.9") || version.contains("1.10") ? new Title1_8() : new Title1_11();
        Variables.actionBar = version.contains("1.7") || version.contains("1.8") || version.contains("1.9") ? new ActionBar1_8() : new ActionBar1_10();
        Variables.bossBar = version.contains("1.7") || version.contains("1.8") ? null : new BossBar();
    }

    public void onEnable() {
        saveDefaultConfig();
        if (!Bukkit.getPluginManager().isPluginEnabled("AuthMe")) {
            Bukkit.getConsoleSender().sendMessage("§8[§bDeluxeBazaar§8] §cI can't find AuthMe! Plugin is disabling...");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        loadVariables();
        new Events(this);
        if (Variables.bungeecord) {
            Bukkit.getConsoleSender().sendMessage("§8[§bProLoginBar§8] §eEnabled Bungeecord support.");
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

            PluginCommand send = getCommand("send");
            if (send != null)
                send.setExecutor(new SendCommand());

            PluginCommand server = getCommand("server");
            if (server != null)
                server.setExecutor(new ServerCommand());
        }

        Bukkit.getConsoleSender().sendMessage("§8[§bProLoginBar§8] §aPlugin is successfully enabled! §fv" + getDescription().getVersion());
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§8[§bProLoginBar§8] §cPlugin is successfully disabled!");
    }
}
