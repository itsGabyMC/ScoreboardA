package dev._2lstudios.gameessentials.instanceables;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import dev._2lstudios.gameessentials.managers.PlayerManager;
import dev._2lstudios.gameessentials.utils.ConfigurationUtil;

public class ScoreboardPlayer {
    private final Map<Player, Team> nametagTeams;
    private Objective scoreboardObjective;
    private Objective healthObjective;
    private boolean nametag;
    private boolean scoreboard;

    public ScoreboardPlayer(final Plugin plugin, final PlayerManager playerManager,
            final ConfigurationUtil configurationUtil, final Player player) {
        this.nametagTeams = new HashMap<Player, Team>();
        this.scoreboardObjective = null;
        this.healthObjective = null;
        this.nametag = true;
        this.scoreboard = true;
    }

    public boolean isNametag() {
        return this.nametag;
    }

    public void setNametagEnabled(final boolean nametag) {
        this.nametag = nametag;
    }

    public boolean isScoreboardEnabled() {
        return this.scoreboard;
    }

    public void setScoreboardEnabled(final boolean scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void setScoreboardObjective(final Objective objective) {
        this.scoreboardObjective = objective;
    }

    public Objective getScoreboardObjective() {
        return this.scoreboardObjective;
    }

    public void setHealthObjective(final Objective objective) {
        this.healthObjective = objective;
    }

    public Objective getHealthObjective() {
        return this.healthObjective;
    }

    public void addNametagTeam(final Player player, final Team team) {
        this.nametagTeams.put(player, team);
    }

    public Team getNametagTeam(final Player player) {
        return this.nametagTeams.getOrDefault(player, null);
    }

    public void removeNametagTeam(final Player ply) {
        this.nametagTeams.remove(ply);
    }

    public void clearNametagTeams() {
        this.nametagTeams.clear();
    }
}
