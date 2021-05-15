package me.sedattr.loginbar.title;

import org.bukkit.entity.Player;

public interface Title {
    void title(Player player, String title, Integer fadeIn, Integer stay, Integer fadeOut);
    void subtitle(Player player, String subtitle, Integer fadeIn, Integer stay, Integer fadeOut);
    void send(Player player, String type, Integer time);
}
