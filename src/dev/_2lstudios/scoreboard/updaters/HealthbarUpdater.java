package dev._2lstudios.scoreboard.updaters;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import dev._2lstudios.scoreboard.instanceables.SidebarPlayer;
import dev._2lstudios.scoreboard.managers.SidebarPlayerManager;
import dev._2lstudios.scoreboard.managers.VariableManager;
import dev._2lstudios.scoreboard.utils.ScoreboardUtil;

public class HealthbarUpdater {
    private final String HEALTH_OBJECTIVE = "2LS_HP";
    private final String HEALTH_DISPLAYNAME = ChatColor.RED + "\u2764";
    private final Plugin plugin;
    private final SidebarPlayerManager playerManager;
    private final VariableManager variableManager;

    public HealthbarUpdater(final Plugin plugin, final SidebarPlayerManager playerManager, final VariableManager variableManager) {
        this.plugin = plugin;
        this.playerManager = playerManager;
        this.variableManager = variableManager;
    }

    public void update(final Player player) {
        final Scoreboard scoreboard = player.getScoreboard();
        final SidebarPlayer scoreboardPlayer = playerManager.getPlayer(player.getUniqueId());

        if (scoreboardPlayer == null) {
            return;
        }

        final World world = player.getWorld();
        final GameMode gameMode = player.getGameMode();

        if (gameMode != GameMode.SPECTATOR && player.isOnline()) {
            final Objective healthObjective = ScoreboardUtil.getOrCreateObjective(HEALTH_OBJECTIVE, scoreboard,
                    scoreboardPlayer);

            if (healthObjective != null) {
                final Location location = player.getLocation();

                if (healthObjective.getDisplaySlot() != DisplaySlot.BELOW_NAME) {
                    healthObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);
                }

                if (!HEALTH_DISPLAYNAME.equals(healthObjective.getDisplayName())) {
                    healthObjective.setDisplayName(HEALTH_DISPLAYNAME);
                }

                for (final Player ply : world.getPlayers()) {
                    if (ply.isOnline() && !ply.isDead() && location.distance(ply.getLocation()) < 10.0) {
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
            final Objective healthObjective = scoreboardPlayer.getObjective(HEALTH_OBJECTIVE);

            if (healthObjective != null) {
                healthObjective.unregister();
                scoreboardPlayer.setObjective(HEALTH_OBJECTIVE, null);
            }
        }
    }

    public void update() {
        if (!variableManager.isHealthEnabled()) {
            return;
        }

        for (final Player player : plugin.getServer().getOnlinePlayers()) {
            update(player);
        }
    }
}
