package dev._2lstudios.scoreboard.managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.scoreboard.instanceables.SidebarPlayer;
import dev._2lstudios.scoreboard.utils.ConfigurationUtil;

public class SidebarPlayerManager {
    private final Plugin plugin;
    private final ConfigurationUtil configurationUtil;
    private final Map<UUID, SidebarPlayer> essentialsPlayers;
    private final Collection<SidebarPlayer> changed;

    public SidebarPlayerManager(final Plugin plugin, final ConfigurationUtil configurationUtil) {
        this.plugin = plugin;
        this.configurationUtil = configurationUtil;
        this.essentialsPlayers = new HashMap<UUID, SidebarPlayer>();
        this.changed = new HashSet<SidebarPlayer>();
        for (final Player player : plugin.getServer().getOnlinePlayers()) {
            this.addPlayer(player);
        }
    }

    public SidebarPlayer getPlayer(final UUID uuid) {
        return this.essentialsPlayers.getOrDefault(uuid, null);
    }

    public SidebarPlayer getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }

    public SidebarPlayer addPlayer(final Player player) {
        final SidebarPlayer essentialsPlayer = new SidebarPlayer(this.plugin, this, this.configurationUtil,
                player);
        this.essentialsPlayers.put(player.getUniqueId(), essentialsPlayer);
        return essentialsPlayer;
    }

    public void removePlayer(final UUID uuid) {
        this.essentialsPlayers.remove(uuid);
    }

    public void addChanged(final SidebarPlayer essentialsPlayer) {
        this.changed.add(essentialsPlayer);
    }

    public Collection<SidebarPlayer> getChanged() {
        return this.changed;
    }

    public Collection<SidebarPlayer> getPlayers() {
        return this.essentialsPlayers.values();
    }
}
