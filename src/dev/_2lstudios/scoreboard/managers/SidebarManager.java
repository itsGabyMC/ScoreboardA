package dev._2lstudios.scoreboard.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import dev._2lstudios.scoreboard.utils.BukkitUtil;

public class SidebarManager {
    private boolean enabled;
    private Map<String, List<String>> sidebars;
    private Map<Player, List<String>> customSidebars;

    public SidebarManager() {
        this.enabled = false;
        this.sidebars = new HashMap<String, List<String>>();
        this.customSidebars = new HashMap<Player, List<String>>();
    }

    public void reload(final boolean enabled, final Map<String, List<String>> scoreboards) {
        this.enabled = enabled;
        this.sidebars = BukkitUtil.translateColorCodes(scoreboards);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public List<String> getSidebars(final Player player, final String scoreboard) {
        if (this.customSidebars.containsKey(player)) {
            return this.customSidebars.get(player);
        }

        return this.sidebars.getOrDefault(scoreboard, this.sidebars.getOrDefault("default", null));
    }

    public void setCustomSidebar(final Player player, final List<String> customScoreboard) {
        if (customScoreboard == null) {
            this.customSidebars.remove(player);
        } else {
            this.customSidebars.put(player, customScoreboard);
        }
    }
}
