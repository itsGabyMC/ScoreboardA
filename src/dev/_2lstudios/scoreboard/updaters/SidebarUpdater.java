package dev._2lstudios.scoreboard.updaters;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import dev._2lstudios.scoreboard.instanceables.SidebarPlayer;
import dev._2lstudios.scoreboard.managers.PlaceholderAPIManager;
import dev._2lstudios.scoreboard.managers.SidebarPlayerManager;
import dev._2lstudios.scoreboard.managers.SidebarManager;
import dev._2lstudios.scoreboard.managers.VariableManager;
import dev._2lstudios.scoreboard.utils.ScoreboardUtil;
import dev._2lstudios.scoreboard.utils.SideboardExpressions;

public class SidebarUpdater {
    private final Plugin plugin;
    private final SidebarPlayerManager sidebarPlayerManager;
    private final VariableManager variableManager;
    private final PlaceholderAPIManager placeholderAPIManager;

    public SidebarUpdater(final Plugin plugin, final SidebarPlayerManager sidebarPlayerManager,
            final VariableManager variableManager, final PlaceholderAPIManager placeholderAPIManager) {
        this.plugin = plugin;
        this.sidebarPlayerManager = sidebarPlayerManager;
        this.variableManager = variableManager;
        this.placeholderAPIManager = placeholderAPIManager;
    }

    public void update(final Player player) {
        final Scoreboard scoreboard = player.getScoreboard();
        final SidebarPlayer essentialsPlayer = sidebarPlayerManager.getPlayer(player.getUniqueId());

        if (essentialsPlayer == null) {
            return;
        }

        if (essentialsPlayer.isScoreboardEnabled()) {
            final Objective objective = ScoreboardUtil.getOrCreateObjective("2LS_Sidebar", scoreboard,
                    essentialsPlayer);

            if (objective != null) {
                final SidebarManager boardManager = variableManager.getSidebarManager();
                final World world = player.getWorld();
                Collection<String> list;

                if (objective.getDisplaySlot() != DisplaySlot.SIDEBAR) {
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                }

                if (world != null) {
                    list = boardManager.getSidebars(player, world.getName().toLowerCase());
                } else {
                    list = boardManager.getSidebars(player, "default");
                }

                if (list != null && !list.isEmpty()) {
                    final Collection<String> actualLines = new HashSet<String>();
                    int size = list.size();

                    if (size > 15) {
                        size = 15;
                    }

                    int actualSize = size;

                    for (String line : list) {
                        line = placeholderAPIManager.setPlaceholders(player, line);

                        final boolean displayLine = SideboardExpressions.evaluateExpression(line);

                        if (!displayLine) {
                            continue;
                        } else {
                            line = SideboardExpressions.stripExpressions(line);
                        }

                        if (!line.isEmpty()) {
                            if (line.length() > 40) {
                                line = line.substring(0, 40);
                            }

                            if (actualSize == size) {
                                final String displayName = objective.getDisplayName();
                                if (displayName == null || !displayName.equals(line)) {
                                    objective.setDisplayName(line);
                                }
                            } else {
                                final Score score = objective.getScore(line);
                                if (score.getScore() != actualSize) {
                                    score.setScore(actualSize);
                                }
                            }

                            actualLines.add(line);
                            --actualSize;
                        }
                    }

                    final Collection<String> scoreboardEntries = scoreboard.getEntries();

                    for (final String entry : new HashSet<>(scoreboardEntries)) {
                        final Score score2 = objective.getScore(entry);

                        if (score2 == null || (!actualLines.contains(entry) && score2.isScoreSet())) {
                            scoreboard.resetScores(entry);
                        }
                    }
                }
            }
        } else {
            final Objective objective2 = essentialsPlayer.getObjective("2LS_Sidebar");
            if (objective2 != null) {
                objective2.unregister();
                essentialsPlayer.setObjective("2LS_Sidebar", null);
            }
        }
    }

    public void updateAsync(final Player player) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> update(player));
    }
}
