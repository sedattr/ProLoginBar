package me.sedattr.loginbar;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.api.v3.AuthMeApi;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

public class Events implements Listener {
    public static String colorize(String s) {
        if (s == null || s.equals(""))
            return "";

        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().isOp())
            return;

        String message = e.getMessage().substring(1).toLowerCase();
        if (message.startsWith("skyblock") || message.startsWith("login") || message.startsWith("register") || message.startsWith("reg") || message.startsWith("l"))
            return;
        if (message.startsWith("giriş") || message.startsWith("kayıt") || message.startsWith("kayit") || message.startsWith("giris"))
            return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        if (!(e.getEntity() instanceof Player))
            return;

        Player player = (Player) e.getEntity();
        if (player.isOp())
            return;

        e.setCancelled(true);
        player.setFoodLevel(20);
     }

    @EventHandler(ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (player.isOp())
            return;

        String message = e.getMessage().substring(1).toLowerCase();
        if (message.startsWith("skyblock") || message.startsWith("login") || message.startsWith("register") || message.startsWith("reg") || message.startsWith("l"))
            return;
        e.setCancelled(true);
        if (AuthMeApi.getInstance().isAuthenticated(player))
            return;

        if (message.startsWith("giriş") || message.startsWith("kayıt") || message.startsWith("kayit") || message.startsWith("giris")) {
            player.chat("/" + e.getMessage()
                    .replace("/giriş", "login")
                    .replace("/giris", "login")
                    .replace("/kayit", "register")
                    .replace("/kayıt", "register"));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player))
            return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onInteract(EntityDamageByEntityEvent e) {
        if (e.getDamager().isOp())
            return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (!(e.getEntity() instanceof Player))
            return;
        if (e.getEntity().isOp())
            return;

        e.setCancelled(true);
        ((Player)e.getEntity()).setFoodLevel(20);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getPlayer().isOp())
            return;

        e.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (e.getPlayer().isOp())
            return;

        e.setCancelled(true);
    }
    
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);

        final int[] time = {LoginBar.config.getInt("kick.time", 30)};

        Player player = e.getPlayer();
        if (LoginBar.fly != null) {
            player.teleport(LoginBar.fly);
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();

            player.setFoodLevel(20);
            player.setHealth(20);

            player.setFlying(false);
            player.setAllowFlight(false);
        }

        if (AuthMeApi.getInstance().isRegistered(player.getName()))
            LoginBar.getInstance().sendTitle(player, "login");
        else
            LoginBar.getInstance().sendTitle(player, "register");

        new ActionBar().sendActionbar(player, colorize(LoginBar.config.getString("action_bar.message").replace("%time%", String.valueOf(time[0]))));
        new BukkitRunnable() {
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                if (time[0] <= 1 || AuthMeApi.getInstance().isAuthenticated(player)) {
                    if (!AuthMeApi.getInstance().isAuthenticated(player)) {
                        if (LoginBar.config.getBoolean("kick.enabled"))
                            player.kickPlayer(colorize(LoginBar.config.getString("kick.message")));
                    } else
                        LoginBar.getInstance().sendToServer(player, null);
                    cancel();
                    return;
                }

                new ActionBar().sendActionbar(player, colorize(LoginBar.config.getString("action_bar.message").replace("%time%", String.valueOf(time[0]))));
                time[0]--;
            }
        }.runTaskTimer(LoginBar.getInstance(), 0L, 20L);
    }
}
