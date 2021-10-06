package io.github.thatkawaiisam.gatekeeper.utils;

import net.md_5.bungee.api.ChatColor;

public class MessageUtil {
    
    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
