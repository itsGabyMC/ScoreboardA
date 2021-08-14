package dev._2lstudios.scoreboard.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import dev._2lstudios.scoreboard.instanceables.SidebarPlayer;
import dev._2lstudios.scoreboard.managers.VariableManager;

public class ScoreboardUtil {
    public static void setExternalBoard(final Player player) {
        final ScoreboardManager scoreboardManager = Bukkit.getServer().getScoreboardManager();

        if (player.getScoreboard() == scoreboardManager.getMainScoreboard()) {
            player.setScoreboard(scoreboardManager.getNewScoreboard());
        }
    }

    public static Objective getOrCreateObjective(final String objectiveName, final Scoreboard scoreboard,
            final SidebarPlayer scoreboardPlayer) {
        final Objective lastScoreboardObjective = scoreboardPlayer.getObjective(objectiveName);
        final Objective scoreboardObjective;

        if (lastScoreboardObjective == null) {
            final Objective objective = scoreboard.getObjective(objectiveName);

            if (objective == null) {
                scoreboardObjective = scoreboard.registerNewObjective(objectiveName, "dummy");
            } else {
                scoreboardObjective = objective;
            }

            scoreboardPlayer.setObjective(objectiveName, scoreboardObjective);
        } else {
            scoreboardObjective = lastScoreboardObjective;
        }

        return scoreboardObjective;
    }

    public static void clearPlayer(final VariableManager variableManager, final Player player) {
        final Scoreboard scoreboard = player.getScoreboard();

        if (variableManager.getSidebarManager().isEnabled()) {
            scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        }

        if (variableManager.isHealthEnabled()) {
            scoreboard.clearSlot(DisplaySlot.BELOW_NAME);
        }

        if (variableManager.isNametagEnabled()) {
            scoreboard.clearSlot(DisplaySlot.PLAYER_LIST);
        }
    }
}
