package me.sedattr.loginbar.actionbar;

import me.sedattr.loginbar.Variables;
import me.sedattr.loginbar.helpers.Reflection;
import me.sedattr.loginbar.helpers.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class ActionBar1_8 implements ActionBar, Reflection {
    private final ConfigurationSection section;

    public ActionBar1_8() {
        this.section = Variables.config.getConfigurationSection("action_bar");
    }

    @Override
    public void actionBar(Player player, String text) {
        try {
            Constructor<?> constructor = getNMSClass("PacketPlayOutChat").getConstructor(getNMSClass("IChatBaseComponent"), byte.class);
            Object icbc = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, "{\"text\":\"" + text + "\"}");
            Object packet = constructor.newInstance(icbc, (byte) 2);
            Object entityPlayer = player.getClass().getMethod("getHandle", new Class[0]).invoke(player);
            Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
            playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") }).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
