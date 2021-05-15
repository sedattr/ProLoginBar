package me.sedattr.loginbar.helpers;

import me.sedattr.loginbar.Variables;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BossBar {
    private final ConfigurationSection section;
    private final String version;
    private Map<Player, org.bukkit.boss.BossBar> bars = new HashMap<>();

    public BossBar() {
        this.version = Bukkit.getVersion();
        this.section = Variables.config.getConfigurationSection("boss_bar");
    }

    public void create(Player player) {
        if (this.version.contains("1.8"))
            return;
        if (this.section == null)
            return;

        String style = this.section.getString("style", "SOLID");
        String color = this.section.getString("color", "WHITE");
        String title = this.section.getString("message");
        if (title == null || title.equals(""))
            return;

        org.bukkit.boss.BossBar bar = Bukkit.createBossBar(Utils.colorize(title), BarColor.valueOf(color), BarStyle.valueOf(style));
        bar.setProgress(1.0D);
        bar.setVisible(true);
        bar.addPlayer(player);

        this.bars.put(player, bar);
    }

    public void change(Player player, Integer time, Integer kickTime) {
        if (this.section == null)
            return;
        org.bukkit.boss.BossBar bar = this.bars.get(player);
        if (bar == null)
            return;

        if (this.section.getBoolean("progress")) {
            double removeCount = 1.0D / kickTime;
            bar.setProgress(bar.getProgress() - removeCount);
        }

        if (this.section.getBoolean("colored")) {
            int number = kickTime/3;

            if (time > number*2)
                bar.setColor(BarColor.GREEN);
            else if (time > number)
                bar.setColor(BarColor.YELLOW);
            else
                bar.setColor(BarColor.RED);
        }
    }

    public void remove(Player player) {
        org.bukkit.boss.BossBar bar = this.bars.remove(player);
        if (bar == null)
            return;

        bar.removePlayer(player);
    }
}
