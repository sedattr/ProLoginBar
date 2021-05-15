package me.sedattr.loginbar.actionbar;

import org.bukkit.entity.Player;

public interface ActionBar {
    public void actionBar(Player player, String text);
    public void send(Player player, String type, Integer time);
}
