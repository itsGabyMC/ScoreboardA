package dev._2lstudios.scoreboard.updaters;

import java.util.Collection;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import dev._2lstudios.scoreboard.hooks.TeamsHook;
import dev._2lstudios.scoreboard.instanceables.SidebarPlayer;
import dev._2lstudios.scoreboard.managers.PlaceholderAPIManager;
import dev._2lstudios.scoreboard.managers.PrefixSuffixManager;
import dev._2lstudios.scoreboard.managers.SidebarPlayerManager;
import dev._2lstudios.scoreboard.managers.VariableManager;
import dev._2lstudios.scoreboard.utils.StringUtil;
import dev._2lstudios.scoreboard.utils.VersionUtil;

public class NametagUpdater {
    private final Plugin plugin;
    private final SidebarPlayerManager sidebarPlayerManager;
    private final PrefixSuffixManager prefixSuffixManager;
    private final VariableManager variableManager;
    private final TeamsHook teamsHook;

    public NametagUpdater(final Plugin plugin, final PlaceholderAPIManager placeholderAPIManager,
            final SidebarPlayerManager sidebarPlayerManager, final PrefixSuffixManager prefixSuffixManager,
            final VariableManager variableManager, final TeamsHook teamsHook) {
        this.plugin = plugin;
        this.sidebarPlayerManager = sidebarPlayerManager;
        this.prefixSuffixManager = prefixSuffixManager;
        this.variableManager = variableManager;
        this.teamsHook = teamsHook;
    }

    private boolean checkNametagTeam(final Player player, final Player ply) {
        final boolean isVisible = !ply.hasPotionEffect(PotionEffectType.INVISIBILITY);

        return teamsHook.isSameTeam(player, ply) || isVisible;
    }

    private boolean checkNametagWorld(final World world) {
        final Collection<String> nametagWhitelist = variableManager.getNametagWhitelist();

        return nametagWhitelist.isEmpty() || nametagWhitelist.contains(world.getName());
    }

    private Team getOrCreateTeam(final Scoreboard scoreboard, final SidebarPlayer scoreboardPlayer, final Player ply) {
        final Collection<Team> teams = scoreboard.getTeams();
        Team team = scoreboardPlayer.getNametagTeam(ply);

        if (!teams.contains(team)) {
            final String plyName = ply.getName();
            final String teamName = StringUtil.trimToLength(plyName, 16);

            team = scoreboard.getTeam(teamName);

            if (team == null) {
                team = scoreboard.registerNewTeam(teamName);
            }

            if (VersionUtil.isOneDotNine()) {
                team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            }

            team.addEntry(plyName);
            scoreboardPlayer.addNametagTeam(ply, team);
        }

        return team;
    }

    public void addNametag(final Player player, final Player otherPlayer) {
        if (otherPlayer.isDead() || !otherPlayer.isOnline()) {
            return;
        }

        final SidebarPlayer sidebarPlayer = sidebarPlayerManager.getPlayer(player);
        final SidebarPlayer otherSidebarPlayer = sidebarPlayerManager.getPlayer(otherPlayer);

        if (otherSidebarPlayer == null || sidebarPlayer == null) {
            return;
        }

        final Scoreboard scoreboard = player.getScoreboard();
        final World world = player.getWorld();

        if (checkNametagWorld(world) && checkNametagTeam(player, otherPlayer)) {
            final Team team = getOrCreateTeam(scoreboard, sidebarPlayer, otherPlayer);
            final String prefix = prefixSuffixManager.getPrefix(otherSidebarPlayer, otherPlayer, player);
            final String suffix = prefixSuffixManager.getSuffix(otherSidebarPlayer, otherPlayer);

            if (!team.getPrefix().equals(prefix)) {
                team.setPrefix(prefix);
            }

            if (!team.getSuffix().equals(suffix)) {
                team.setSuffix(suffix);
            }
        } else {
            final Team team = sidebarPlayer.getNametagTeam(otherPlayer);

            if (team != null) {
                team.unregister();
                sidebarPlayer.removeNametagTeam(otherPlayer);
            }
        }
    }

    public void updateOthers(final Player player) {
        if (!variableManager.isNametagEnabled()) {
            return;
        }

        for (final Player otherPlayer : plugin.getServer().getOnlinePlayers()) {
            addNametag(otherPlayer, player);
        }
    }

    public void updateOthersAsync(final Player player) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> updateOthers(player));
    }

    public void update(final Player player) {
        for (final Player otherPlayer : plugin.getServer().getOnlinePlayers()) {
            addNametag(otherPlayer, player);
            addNametag(player, otherPlayer);
        }
    }

    public void updateAsync(final Player player) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> update(player));
    }

    public void update() {
        if (!variableManager.isNametagEnabled()) {
            return;
        }

        final Collection<? extends Player> onlinePlayers = plugin.getServer().getOnlinePlayers();

        for (final Player player : onlinePlayers) {
            update(player);
        }
    }
}
