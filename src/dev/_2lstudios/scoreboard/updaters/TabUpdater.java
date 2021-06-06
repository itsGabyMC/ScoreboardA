package dev._2lstudios.scoreboard.updaters;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.scoreboard.managers.PrefixSuffixManager;

public class TabUpdater {
    private final Plugin plugin;
    private final PrefixSuffixManager prefixSuffixManager;

    public TabUpdater(final Plugin plugin, final PrefixSuffixManager prefixSuffixManager) {
        this.plugin = plugin;
        this.prefixSuffixManager = prefixSuffixManager;
    }

    public void update(final Player player) {
        final String prefix = prefixSuffixManager.getPrefix(player);
        final String suffix = prefixSuffixManager.getSuffix(player);
        final String listName = String.valueOf(prefix) + player.getDisplayName() + suffix;

        if (!player.getPlayerListName().equals(listName)) {
            player.setPlayerListName(listName);
        }
    }

    public void updateAsync(final Player player) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> update(player));
    }
}
