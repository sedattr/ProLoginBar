package me.sedattr.loginbar.helpers;

import java.lang.reflect.Constructor;

import me.sedattr.loginbar.Variables;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class Title implements Reflection {
    private final ConfigurationSection section;

    public Title() {
        this.section = Variables.config.getConfigurationSection("title");
    }

    public void title(Player player, String title, Integer fadeIn, Integer stay, Integer fadeOut) {
        try {
            Object e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
            Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, "{\"text\":\"" + title + "\"}");
            Constructor<?> subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            Object titlePacket = subtitleConstructor.newInstance(e, chatTitle, fadeIn*20, stay*20, fadeOut*20);

            sendPacket(player, titlePacket);

            e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
            chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, "{\"text\":\"" + title + "\"}");
            subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"));
            titlePacket = subtitleConstructor.newInstance(e, chatTitle);

            sendPacket(player, titlePacket);

        }

        catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void subtitle(Player player, String subtitle, Integer fadeIn, Integer stay, Integer fadeOut) {
        try {
            Object e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
            Object chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, "{\"text\":\"" + subtitle + "\"}");
            Constructor<?> subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            Object subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn*20, stay*20, fadeOut*20);

            sendPacket(player, subtitlePacket);

            e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
            chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, "{\"text\":\"" + subtitle + "\"}");
            subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);

            sendPacket(player, subtitlePacket);
        }

        catch (Exception e1) {
            e1.printStackTrace();
        }
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
