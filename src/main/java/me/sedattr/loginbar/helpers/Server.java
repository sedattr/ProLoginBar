package me.sedattr.loginbar.helpers;

import me.sedattr.loginbar.LoginBar;
import me.sedattr.loginbar.Variables;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class Server {
    private final ConfigurationSection section;
    private final LoginBar plugin;

    public Server(LoginBar plugin) {
        this.plugin = plugin;
        this.section = Variables.config.getConfigurationSection("server");
    }

    public void message(Player player, String text) {
        if (this.section == null)
            return;

        String message = this.section.getString(text);
        if (message == null || message.equals(""))
            return;

        player.sendMessage(Utils.colorize(message));
    }


    public void send(Player player, String server) {
        if (server == null)
            server = this.section.getString("name");
        if (server == null)
            return;

        if (Variables.sending.contains(player)) {
            message(player, "wait");

            return;
        }

        int teleport = this.section.getInt("time", 3);
        Variables.sending.add(player);
        Variables.title.send(player, "teleporting", teleport);
        message(player, "teleporting");

        String finalServer = server;
        new BukkitRunnable() {
            @Override
            public void run() {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                try {
                    out.writeUTF("Connect");
                    out.writeUTF(finalServer);
                    player.sendPluginMessage(Server.this.plugin, "BungeeCord", b.toByteArray());

                    Variables.sending.remove(player);
                    message(player, "success");
                } catch (Exception e) {
                    Variables.sending.remove(player);
                    message(player, "error");

                    e.printStackTrace();
                }
            }
        }.runTaskLater(this.plugin, teleport* 20L);
    }
}
