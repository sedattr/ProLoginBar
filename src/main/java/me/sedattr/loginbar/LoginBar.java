package me.sedattr.loginbar;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class LoginBar extends JavaPlugin {
    private static LoginBar instance;
    public static ConfigurationSection config;
    public static Location fly = null;
    public static List<Player> sending = new ArrayList<>();

    public void loadFly() {
        ConfigurationSection section = config.getConfigurationSection("spawn");
        World world = Bukkit.getWorld(section.getString("world", "world"));
        if (world == null)
            return;

        fly = new Location(world, section.getDouble("x"), section.getDouble("y"), section.getDouble("z"), section.getInt("yaw"), section.getInt("pitch"));
    }

    public void sendTitle(Player player, String type) {
        ConfigurationSection section = LoginBar.config.getConfigurationSection(type);

        Title.sendTitle(player, Events.colorize(section.getString("title")), Events.colorize(section.getString("subtitle")), section.getInt("fade_in", 1)*20, section.getInt("stay", 1)*20, section.getInt("fade_out", 1)*20);
    }

    public static LoginBar getInstance() {
        return instance;
    }

    public void sendToServer(Player player, String type) {
        ConfigurationSection section = config.getConfigurationSection("server");
        if (sending.contains(player)) {
            String message = Events.colorize(section.getString("wait_message"));
            if (!message.equals(""))
                player.sendMessage(message);
            return;
        }

        sending.add(player);
        sendTitle(player, "teleport");

        String message = Events.colorize(section.getString("teleporting_message"));
        if (!message.equals(""))
            player.sendMessage(message);

        new BukkitRunnable() {
            @Override
            public void run() {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                try {
                    sending.remove(player);

                    out.writeUTF("Connect");
                    out.writeUTF(type != null ? type : section.getString("name", "skyblock"));
                    player.sendPluginMessage(LoginBar.getInstance(), "BungeeCord", b.toByteArray());
                } catch (Exception e) {
                    sending.remove(player);

                    e.printStackTrace();
                    String message = Events.colorize(section.getString("error_message"));
                    if (!message.equals(""))
                        player.sendMessage(message);
                    return;
                }

                sending.remove(player);
                String message = Events.colorize(section.getString("success_message"));
                if (!message.equals(""))
                    player.sendMessage(message);
            }
        }.runTaskLater(this, section.getInt("time", 1) * 20L);
    }

    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        config = getConfig();
        loadFly();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Bukkit.getConsoleSender().sendMessage("§8[§bLoginBar§8] §aPlugin enabled! Made by SedatTR#8666.");

        getCommand("send").setExecutor(new SendCommand());
        getCommand("skyblock").setExecutor(new SkyblockCommand());
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§8[§bLoginBar§8] §cPlugin disabled!");
    }
}
