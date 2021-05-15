package me.sedattr.loginbar.title;

import me.sedattr.loginbar.Variables;
import me.sedattr.loginbar.helpers.Reflection;
import me.sedattr.loginbar.helpers.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Title1_11 implements Title, Reflection {
    private final ConfigurationSection section;

    public Title1_11() {
        this.section = Variables.config.getConfigurationSection("title");
    }

    public void title(Player player, String title, Integer fadeIn, Integer stay, Integer fadeOut) {
        player.sendTitle(title, null, fadeIn*20, stay*20, fadeOut*20);
    }

    public void subtitle(Player player, String subtitle, Integer fadeIn, Integer stay, Integer fadeOut) {
        player.sendTitle(null, subtitle, fadeIn*20, stay*20, fadeOut*20);
    }

    public void send(Player player, String type, Integer time) {
        if (this.section == null)
            return;

        ConfigurationSection titleSection = this.section.getConfigurationSection(type);
        if (titleSection == null)
            return;

        int fadeIn = titleSection.getInt("fade_in");
        int stay = titleSection.getInt("stay");
        int fadeOut = titleSection.getInt("fade_out");
        if (fadeIn <= 0 && stay <= 0 && fadeOut <= 0)
            return;

        String title = titleSection.getString("title");
        if (title != null && !title.equals(""))
            title(player, Utils.colorize(title
                    .replace("%time%", String.valueOf(time))), fadeIn, stay, fadeOut);

        String subtitle = titleSection.getString("subtitle");
        if (subtitle != null && !subtitle.equals(""))
            subtitle(player, Utils.colorize(subtitle
                    .replace("%time%", String.valueOf(time))), fadeIn, stay, fadeOut);
    }
}
