package me.sedattr.loginbar.helpers;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String colorize(String s) {
        if (s == null || s.equals(""))
            return "";

        if (!Bukkit.getVersion().contains("1.16"))
            return ChatColor.translateAlternateColorCodes('&', s);

        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher match = pattern.matcher(s);
        while (match.find()) {
            String hexColor = s.substring(match.start(), match.end());
            s = s.replace(hexColor, ChatColor.of(hexColor).toString());
            match = pattern.matcher(s);
        }

        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
