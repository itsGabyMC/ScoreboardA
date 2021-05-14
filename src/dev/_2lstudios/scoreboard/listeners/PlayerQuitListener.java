package dev._2lstudios.scoreboard.listeners;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import dev._2lstudios.scoreboard.instanceables.ScoreboardPlayer;
import dev._2lstudios.scoreboard.managers.EssentialsManager;
import dev._2lstudios.scoreboard.managers.PlayerManager;
import dev._2lstudios.scoreboard.managers.VariableManager;

public class PlayerQuitListener implements Listener {
    private final Server server;
    private final PlayerManager playerManager;
    private final VariableManager variableManager;
    private final Collection<Player> autofeedPlayers;

    public PlayerQuitListener(final Server server, final EssentialsManager essentialsManager) {
        this.server = server;
        this.playerManager = essentialsManager.getPlayerManager();
        this.variableManager = essentialsManager.getVariableManager();
        this.autofeedPlayers = essentialsManager.getAutoFeedPlayers();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final Scoreboard scoreboard = player.getScoreboard();
        final String playerName = player.getName();
        final ScoreboardPlayer essentialsPlayer = this.playerManager.getPlayer(uuid);
        final boolean scoreboardEnabled = this.variableManager.getSidebarManager().isEnabled();
        final boolean nametagEnabled = this.variableManager.isNametagEnabled();
        this.autofeedPlayers.remove(player);
        essentialsPlayer.clearNametagTeams();
        if (scoreboardEnabled) {
            scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        }
        if (nametagEnabled) {
            scoreboard.clearSlot(DisplaySlot.BELOW_NAME);
            for (final Player ply : this.server.getOnlinePlayers()) {
                if (player != ply) {
                    final ScoreboardPlayer essentialsPly = this.playerManager.getPlayer(ply.getUniqueId());
                    if (essentialsPly != null) {
                        essentialsPly.removeNametagTeam(player);
                    }
                    final Scoreboard scoreboard2 = ply.getScoreboard();
                    final Team team = scoreboard2.getTeam(playerName);
                    if (team != null) {
                        team.unregister();
                    }
                    scoreboard2.resetScores(playerName);
                }
            }
        }

        this.playerManager.removePlayer(uuid);
    }
}
