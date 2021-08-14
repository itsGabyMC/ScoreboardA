package dev._2lstudios.scoreboard.managers;

import java.util.HashSet;

import org.bukkit.plugin.Plugin;

import dev._2lstudios.scoreboard.utils.ConfigurationUtil;

import org.bukkit.entity.Player;
import java.util.Collection;

public class MainManager {
    private final SidebarPlayerManager playerManager;
    private final VariableManager variableManager;
    private final PlaceholderAPIManager placeholderAPIManager;
    private final Collection<Player> autoFeedPlayers;

    public MainManager(final Plugin plugin, final ConfigurationUtil configurationUtil) {
        this.playerManager = new SidebarPlayerManager(plugin, configurationUtil);
        this.variableManager = new VariableManager(configurationUtil);
        this.placeholderAPIManager = new PlaceholderAPIManager();
        this.autoFeedPlayers = new HashSet<Player>();
    }

    public SidebarPlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public VariableManager getVariableManager() {
        return this.variableManager;
    }

    public PlaceholderAPIManager getPlaceholderAPIManager() {
        return this.placeholderAPIManager;
    }

    public void reload() {
        this.variableManager.reload();
    }

    public Collection<Player> getAutoFeedPlayers() {
        return this.autoFeedPlayers;
    }
}
