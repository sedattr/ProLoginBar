package me.sedattr.loginbar;

import fr.xephi.authme.api.v3.AuthMeApi;
import me.sedattr.loginbar.helpers.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class Events implements Listener {
    private final ConfigurationSection kickSection;
    private final AuthMeApi authMe;
    private final Integer kick;
    private final LoginBar plugin;

    public Events(LoginBar plugin) {
        this.plugin = plugin;
        this.kickSection = Variables.config.getConfigurationSection("kick");
        this.authMe = AuthMeApi.getInstance();

        if (this.kickSection != null)
            this.kick = this.kickSection.getInt("time", 30);
        else
            this.kick = 30;

        if (this.authMe != null)
            Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        final int[] time = {this.kick};

        Player player = e.getPlayer();

        if (this.authMe.isRegistered(player.getName())) {
            Variables.title.send(player, "login", time[0]);
            Variables.actionBar.send(player, "login", time[0]);
        } else {
            Variables.title.send(player, "register", time[0]);
            Variables.actionBar.send(player, "register", time[0]);
        }

        if (Variables.bossBar != null)
            Variables.bossBar.create(player, time[0]);
        new BukkitRunnable() {
            public void run() {
                if (!player.isOnline()) {
                    if (Variables.bossBar != null)
                        Variables.bossBar.remove(player);

                    cancel();
                    return;
                }

                if (time[0] <= 0) {
                    if (Events.this.kickSection != null && Events.this.kickSection.getBoolean("enabled")) {
                        String message = Events.this.kickSection.getString("message");
                        if (message != null && !message.equals(""))
                            player.kickPlayer(Utils.colorize(message));
                    }

                    if (Variables.bossBar != null)
                        Variables.bossBar.remove(player);
                    cancel();
                    return;
                }

                if (Events.this.authMe.isAuthenticated(player)) {
                    Variables.server.send(player, null);

                    if (Variables.bossBar != null)
                        Variables.bossBar.remove(player);
                    cancel();
                    return;
                }

                if (Variables.bossBar != null)
                    Variables.bossBar.change(player, time[0], Events.this.kick);
                Variables.actionBar.send(player, "message", time[0]);
                time[0]--;
            }
        }.runTaskTimer(this.plugin, 0L, 20L);
    }
}
