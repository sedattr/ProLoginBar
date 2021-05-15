package me.sedattr.loginbar.actionbar;

import me.sedattr.loginbar.Variables;
import me.sedattr.loginbar.helpers.Reflection;
import me.sedattr.loginbar.helpers.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ActionBar1_10 implements ActionBar, Reflection {
    private final ConfigurationSection section;

    public ActionBar1_10() {
        this.section = Variables.config.getConfigurationSection("action_bar");
    }

    @Override
    public void actionBar(Player player, String text) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(text));
    }

    @Override
    public void send(Player player, String type, Integer time) {
        if (this.section == null)
            return;

        String message = this.section.getString(type);
        if (message == null || message.equals(""))
            return;

        actionBar(player, Utils.colorize(message
                .replace("%time%", String.valueOf(time))));
    }
}
