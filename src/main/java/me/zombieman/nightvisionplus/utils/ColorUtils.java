package me.zombieman.nightvisionplus.utils;

import com.tcoded.legacycolorcodeparser.LegacyColorCodeParser;
import me.zombieman.nightvisionplus.NightVisionPlus;
import org.bukkit.ChatColor;

public class ColorUtils {

    public ColorUtils(NightVisionPlus plugin) {
    }

    public static String color(String string) {
        string = LegacyColorCodeParser.convertHexToLegacy('&', string);
        string = ChatColor.translateAlternateColorCodes('&', string);
        return string;
    }
}