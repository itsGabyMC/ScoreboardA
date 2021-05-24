package dev._2lstudios.scoreboard.utils;

import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

public class BukkitUtil {
    public static Map<String, List<String>> translateColorCodes(final Map<String, List<String>> scoreboards) {
        for (final String key : scoreboards.keySet()) {
            final List<String> scoreboard = scoreboards.get(key);

            for (int i = 0; i < scoreboard.size(); i++) {
                final String line = scoreboard.get(i);

                if (line != null) {
                    scoreboard.set(i, ChatColor.translateAlternateColorCodes('&', line));
                }
            }
        }

        return scoreboards;
    }
}
