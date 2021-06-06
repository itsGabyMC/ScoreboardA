package dev._2lstudios.scoreboard.updaters;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import dev._2lstudios.scoreboard.instanceables.SidebarPlayer;
import dev._2lstudios.scoreboard.managers.SidebarPlayerManager;
import dev._2lstudios.scoreboard.utils.ScoreboardUtil;

public class HealthbarUpdater {
    private final SidebarPlayerManager playerManager;

    public HealthbarUpdater(final SidebarPlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public void update(final Player player) {
        final Scoreboard scoreboard = player.getScoreboard();
        final SidebarPlayer scoreboardPlayer = playerManager.getPlayer(player.getUniqueId());

        if (scoreboardPlayer == null) {
            return;
        }

        final World world = player.getWorld();
        final GameMode gameMode = player.getGameMode();

        if (gameMode != GameMode.SPECTATOR) {
            final Objective healthObjective = ScoreboardUtil.getOrCreateObjective("2LS_Health", scoreboard, scoreboardPlayer);

            if (healthObjective != null) {
                final Location location = player.getLocation();

                if (healthObjective.getDisplaySlot() != DisplaySlot.BELOW_NAME) {
                    healthObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);
                }

                if (!healthObjective.getDisplayName().equals(ChatColor.RED + "\u2764")) {
                    healthObjective.setDisplayName(ChatColor.RED + "\u2764");
                }

                for (final Player ply : world.getPlayers()) {
                    if (ply.isOnline() && !ply.isDead() && world == ply.getWorld()
                            && location.distance(ply.getLocation()) < 12.0) {
                        final int health = (int) ply.getHealth();
                        final Score score = healthObjective.getScore(ply.getName());

                        if (score.getScore() == health) {
                            continue;
                        }

                        score.setScore(health);
                    }
                }
            }
        } else {
            final Objective objective2 = scoreboardPlayer.getObjective("2LS_Health");

            if (objective2 != null) {
                objective2.unregister();
                scoreboardPlayer.setObjective("2LS_Health", null);
            }
        }
    }
}
