package dev._2lstudios.gameessentials.managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.gameessentials.instanceables.ScoreboardPlayer;
import dev._2lstudios.gameessentials.utils.ConfigurationUtil;

public class PlayerManager {
    private final Plugin plugin;
    private final ConfigurationUtil configurationUtil;
    private final Map<UUID, ScoreboardPlayer> essentialsPlayers;
    private final Collection<ScoreboardPlayer> changed;

    public PlayerManager(final Plugin plugin, final ConfigurationUtil configurationUtil) {
        this.plugin = plugin;
        this.configurationUtil = configurationUtil;
        this.essentialsPlayers = new HashMap<UUID, ScoreboardPlayer>();
        this.changed = new HashSet<ScoreboardPlayer>();
        for (final Player player : plugin.getServer().getOnlinePlayers()) {
            this.addPlayer(player);
        }
    }

    public ScoreboardPlayer getPlayer(final UUID uuid) {
        return this.essentialsPlayers.getOrDefault(uuid, null);
    }

    public ScoreboardPlayer addPlayer(final Player player) {
        final ScoreboardPlayer essentialsPlayer = new ScoreboardPlayer(this.plugin, this, this.configurationUtil,
                player);
        this.essentialsPlayers.put(player.getUniqueId(), essentialsPlayer);
        return essentialsPlayer;
    }

    public void removePlayer(final UUID uuid) {
        this.essentialsPlayers.remove(uuid);
    }

    public void addChanged(final ScoreboardPlayer essentialsPlayer) {
        this.changed.add(essentialsPlayer);
    }

    public Collection<ScoreboardPlayer> getChanged() {
        return this.changed;
    }

    public Collection<ScoreboardPlayer> getPlayers() {
        return this.essentialsPlayers.values();
    }
}
