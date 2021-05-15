package me.sedattr.loginbar;

import me.sedattr.loginbar.helpers.ActionBar;
import me.sedattr.loginbar.helpers.BossBar;
import me.sedattr.loginbar.helpers.Server;
import me.sedattr.loginbar.helpers.Title;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Variables {
    public static List<Player> sending = new ArrayList<>();
    public static FileConfiguration config;
    public static Title title;
    public static ActionBar actionBar;
    public static Server server;
    public static BossBar bossBar;
}
