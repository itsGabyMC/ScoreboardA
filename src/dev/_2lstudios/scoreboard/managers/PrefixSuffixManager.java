package dev._2lstudios.scoreboard.managers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev._2lstudios.scoreboard.hooks.TeamsHook;
import dev._2lstudios.scoreboard.instanceables.SidebarPlayer;
import dev._2lstudios.scoreboard.utils.StringUtil;

public class PrefixSuffixManager {
    private static final String TEAMS_PREFIX_FORMAT = "%vault_prefix%%teams_color_%player_name%%";
    private static final String PREFIX_FORMAT = "%vault_prefix%";
    private static final String SUFFIX_FORMAT = "%vault_suffix%";
    private final PlaceholderAPIManager placeholderAPIManager;
    private final TeamsHook teamsHook;

    public PrefixSuffixManager(final MainManager essentialsManager, final TeamsHook teamsHook) {
        this.placeholderAPIManager = essentialsManager.getPlaceholderAPIManager();
        this.teamsHook = teamsHook;
    }

    public String getPrefix(final Player player) {
        final String prefix = placeholderAPIManager.setPlaceholders(player, PREFIX_FORMAT);

        return StringUtil.trimToLength(prefix, 16);
    }

    public String getSuffix(final Player player) {
        final String suffix = placeholderAPIManager.setPlaceholders(player, SUFFIX_FORMAT);

        return StringUtil.trimToLength(suffix, 16);
    }

    public String getTeamsPrefix(final Player player, final Player otherPlayer) {
        final String prefix = placeholderAPIManager.setPlaceholders(player,
                TEAMS_PREFIX_FORMAT.replace("%player_name%", otherPlayer.getName()));

        return StringUtil.trimToLength(prefix, 16);
    }

    public String getPrefix(final SidebarPlayer scoreboardPlayer, final Player player, final Player otherPlayer) {
        if (scoreboardPlayer.isNametag()) {
            if (teamsHook.isHooked()) {
                return getTeamsPrefix(player, otherPlayer);
            } else {
                return getPrefix(player);
            }
        } else {
            return ChatColor.translateAlternateColorCodes('&', "&7");
        }
    }

    public String getSuffix(SidebarPlayer essentialsPly, Player ply) {
        if (essentialsPly.isNametag()) {
            return getSuffix(ply);
        } else {
            return "";
        }
    }
}
