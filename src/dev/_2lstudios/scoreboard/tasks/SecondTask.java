package dev._2lstudios.scoreboard.tasks;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import dev._2lstudios.scoreboard.hooks.TeamsHook;
import dev._2lstudios.scoreboard.instanceables.ScoreboardPlayer;
import dev._2lstudios.scoreboard.managers.EssentialsManager;
import dev._2lstudios.scoreboard.managers.PlaceholderAPIManager;
import dev._2lstudios.scoreboard.managers.PlayerManager;
import dev._2lstudios.scoreboard.managers.SidebarManager;
import dev._2lstudios.scoreboard.managers.VariableManager;
import dev._2lstudios.scoreboard.utils.SideboardExpressions;
import dev._2lstudios.scoreboard.utils.VersionUtil;

public class SecondTask {
    private final VariableManager variableManager;
    private final PlayerManager playerManager;
    private final PlaceholderAPIManager placeholderAPIManager;
    private final Collection<Player> autofeedPlayers;
    private final TeamsHook teamsHook;
    private static final String PREFIX_FORMAT = "%vault_prefix%";
    private static final String SUFFIX_FORMAT = "%vault_suffix%";
    private static final String TEAMS_PREFIX_FORMAT = "%vault_prefix%%teams_color_%player_name%%";

    public SecondTask(final Plugin plugin, final EssentialsManager essentialsManager, final TeamsHook teamsHook) {
        final Server server = plugin.getServer();

        this.variableManager = essentialsManager.getVariableManager();
        this.playerManager = essentialsManager.getPlayerManager();
        this.placeholderAPIManager = essentialsManager.getPlaceholderAPIManager();
        this.autofeedPlayers = essentialsManager.getAutoFeedPlayers();
        this.teamsHook = teamsHook;
        server.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            int skipTicks = 10;

            try {
                --skipTicks;

                for (final Player player : server.getOnlinePlayers()) {
                    update(player, skipTicks);
                }

                if (skipTicks <= 0) {
                    skipTicks = 10;
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }, 20L, 20L);
    }

    private String trimToLength(final String string, final int length) {
        return string.substring(0, Math.min(length, string.length()));
    }

    private String getTeamsPrefix(final Player player, final String otherPlayerName) {
        final String prefix = this.placeholderAPIManager.setPlaceholders(player,
                TEAMS_PREFIX_FORMAT.replace("%player_name%", player.getName()));

        return this.trimToLength(prefix, 16);
    }

    private String getPrefix(final Player player) {
        final String prefix = this.placeholderAPIManager.setPlaceholders(player,
                PREFIX_FORMAT.replace("%player_name%", player.getName()));

        return this.trimToLength(prefix, 16);
    }

    private String getSuffix(final Player player) {
        final String suffix = this.placeholderAPIManager.setPlaceholders(player,
                SUFFIX_FORMAT.replace("%player_name%", player.getName()));

        return this.trimToLength(suffix, 16);
    }

    private String getPrefix(ScoreboardPlayer scoreboardPlayer, Player player, final String otherPlayerName) {
        if (scoreboardPlayer.isNametag()) {
            if (this.teamsHook.isHooked()) {
                return this.getTeamsPrefix(player, otherPlayerName);
            } else {
                return this.getPrefix(player);
            }
        } else {
            return ChatColor.translateAlternateColorCodes('&', "&7");
        }
    }

    private String getSuffix(ScoreboardPlayer essentialsPly, Player ply) {
        if (essentialsPly.isNametag()) {
            return this.getSuffix(ply);
        } else {
            return "";
        }
    }

    private Objective getOrCreateObjective(final String objectiveName, final Scoreboard scoreboard,
            final ScoreboardPlayer scoreboardPlayer) {
        final Objective lastScoreboardObjective = scoreboardPlayer.getScoreboardObjective();
        final Objective scoreboardObjective;

        if (lastScoreboardObjective == null) {
            final Objective objective = scoreboard.getObjective(objectiveName);

            if (objective == null) {
                scoreboardObjective = scoreboard.registerNewObjective(objectiveName, "dummy");
                scoreboardObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
            } else {
                scoreboardObjective = objective;
            }

            scoreboardPlayer.setScoreboardObjective(scoreboardObjective);
        } else {
            scoreboardObjective = lastScoreboardObjective;
        }

        return scoreboardObjective;
    }

    public void update(final Player player, final int skipTicks) {
        if (player != null && player.isOnline() && !player.isDead()) {
            if (this.variableManager.isTabEnabled()) {
                this.updateTab(player);
            }
            if (this.autofeedPlayers.contains(player)) {
                player.setFoodLevel(20);
                player.setSaturation(4.0f);
            }
            final Scoreboard scoreboard = player.getScoreboard();
            if (scoreboard != null) {
                synchronized (scoreboard) {
                    if (this.variableManager.getSidebarManager().isEnabled()) {
                        this.updateScoreboard(player, scoreboard);
                    }
                    if (this.variableManager.isNametagEnabled()) {
                        this.updateHealthBelow(player, scoreboard);
                        if (skipTicks <= 0) {
                            this.updateNametag(player, scoreboard);
                        }
                    }
                }
            }
        }
    }

    private void updateTab(final Player player) {
        final String prefix = this.getPrefix(player);
        final String suffix = this.getSuffix(player);
        final String listName = String.valueOf(prefix) + player.getDisplayName() + suffix;

        if (!player.getPlayerListName().equals(listName)) {
            player.setPlayerListName(listName);
        }
    }

    private void updateScoreboard(final Player player, final Scoreboard scoreboard) {
        final ScoreboardPlayer essentialsPlayer = this.playerManager.getPlayer(player.getUniqueId());

        if (essentialsPlayer == null) {
            return;
        }

        if (essentialsPlayer.isScoreboardEnabled()) {
            final Objective objective = getOrCreateObjective("2LS_Sidebar", scoreboard, essentialsPlayer);

            if (objective != null) {
                final SidebarManager boardManager = this.variableManager.getSidebarManager();
                final World world = player.getWorld();
                Collection<String> list;

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
                        line = ChatColor.translateAlternateColorCodes('&',
                                this.placeholderAPIManager.setPlaceholders(player, line));

                        final boolean displayLine = SideboardExpressions.evaluateExpression(line);

                        if (!displayLine) {
                            continue;
                        } else {
                            String expression = SideboardExpressions.extractExpression(line);
                            if (expression != null) {
                                line = line.replace("[" + expression + "]", "");
                            }
                        }

                        if (!line.isEmpty() && !line.contains("[display=false]")) {
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
                    final Collection<String> scoreboardEntries = (Collection<String>) scoreboard.getEntries();

                    for (final String entry : scoreboardEntries) {
                        final Score score2 = objective.getScore(entry);
                        if (score2 == null || (!actualLines.contains(entry) && score2.isScoreSet())) {
                            scoreboard.resetScores(entry);
                        }
                    }
                }
            }
        } else {
            final Objective objective2 = essentialsPlayer.getScoreboardObjective();
            if (objective2 != null) {
                objective2.unregister();
                essentialsPlayer.setScoreboardObjective(null);
            }
        }
    }

    private void updateNametag(final Player player, final Scoreboard scoreboard) {
        final ScoreboardPlayer essentialsPlayer = this.playerManager.getPlayer(player.getUniqueId());

        if (essentialsPlayer == null) {
            return;
        }

        final Collection<Team> teams = (Collection<Team>) scoreboard.getTeams();
        final Collection<String> nametagWhitelist = this.variableManager.getNametagWhitelist();
        final World world = player.getWorld();
        final String playerName = player.getName();

        for (final Player ply : world.getPlayers()) {
            if (ply.isDead() || !ply.isOnline()) {
                break;
            }

            if (world != ply.getWorld()) {
                break;
            }

            final ScoreboardPlayer essentialsPly = this.playerManager.getPlayer(ply.getUniqueId());

            if (essentialsPly == null) {
                break;
            }

            final World plyWorld = ply.getWorld();
            final GameMode plyGameMode = ply.getGameMode();
            Team team = essentialsPlayer.getNametagTeam(ply);

            if (plyGameMode == GameMode.SPECTATOR || ply.isInvisible()
                    || (!nametagWhitelist.isEmpty() && !nametagWhitelist.contains(plyWorld.getName()))
                    || (!this.teamsHook.isSameTeam(player, ply)
                            && ply.hasPotionEffect(PotionEffectType.INVISIBILITY))) {
                if (team == null || !teams.contains(team)) {
                    continue;
                }

                team.unregister();
                essentialsPlayer.removeNametagTeam(ply);
            } else {
                if (!teams.contains(team)) {
                    final String plyName = ply.getName();
                    final String teamName = this.trimToLength(ply.getName(), 16);

                    team = scoreboard.getTeam(teamName);

                    if (team == null) {
                        team = scoreboard.registerNewTeam(teamName);
                    }

                    if (VersionUtil.isOneDotNine()) {
                        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
                    }

                    team.addEntry(plyName);
                    essentialsPlayer.addNametagTeam(ply, team);
                }

                final String prefix = getPrefix(essentialsPly, ply, playerName);
                final String suffix = getSuffix(essentialsPly, ply);

                if (!team.getPrefix().equals(prefix)) {
                    team.setPrefix(prefix);
                }

                if (team.getSuffix().equals(suffix)) {
                    continue;
                }

                team.setSuffix(suffix);
            }
        }
    }

    private void updateHealthBelow(final Player player, final Scoreboard scoreboard) {
        final ScoreboardPlayer scoreboardPlayer = this.playerManager.getPlayer(player.getUniqueId());

        if (scoreboardPlayer == null) {
            return;
        }

        final World world = player.getWorld();
        final String worldName = world.getName();
        final GameMode gameMode = player.getGameMode();

        if (!worldName.equalsIgnoreCase("lobby") && gameMode != GameMode.SPECTATOR) {
            final Objective healthObjective = getOrCreateObjective("2LS_Health", scoreboard, scoreboardPlayer);

            if (healthObjective != null) {
                final Location location = player.getLocation();

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
            final Objective objective2 = scoreboardPlayer.getHealthObjective();

            if (objective2 != null) {
                objective2.unregister();
                scoreboardPlayer.setHealthObjective(null);
            }
        }
    }
}
