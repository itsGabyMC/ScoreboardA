package dev._2lstudios.scoreboard.instanceables;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import dev._2lstudios.scoreboard.managers.SidebarPlayerManager;
import dev._2lstudios.scoreboard.utils.ConfigurationUtil;

public class SidebarPlayer {
    private final Map<Player, Team> nametagTeams;
    private final Map<String, Objective> objectives;
    private boolean nametag;
    private boolean scoreboard;
    private boolean visible;

    public SidebarPlayer(final Plugin plugin, final SidebarPlayerManager playerManager,
            final ConfigurationUtil configurationUtil, final Player player) {
        this.nametagTeams = new HashMap<Player, Team>();
        this.objectives = new HashMap<String, Objective>();
        this.nametag = true;
        this.scoreboard = true;
        this.visible = !player.hasPotionEffect(PotionEffectType.INVISIBILITY);
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

    public Objective getObjective(String objectiveName) {
        return objectives.getOrDefault(objectiveName, null);
    }

    public void setObjective(String objectiveName, Objective scoreboardObjective) {
        objectives.put(objectiveName, scoreboardObjective);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
