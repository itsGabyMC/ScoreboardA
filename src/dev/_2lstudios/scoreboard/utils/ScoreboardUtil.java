package dev._2lstudios.scoreboard.utils;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import dev._2lstudios.scoreboard.instanceables.SidebarPlayer;

public class ScoreboardUtil {
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
}
