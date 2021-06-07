package dev._2lstudios.scoreboard.listeners.initializers;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import dev._2lstudios.scoreboard.listeners.PlayerChangedWorldListener;
import dev._2lstudios.scoreboard.listeners.PlayerJoinListener;
import dev._2lstudios.scoreboard.listeners.PlayerQuitListener;
import dev._2lstudios.scoreboard.managers.EssentialsManager;
import dev._2lstudios.scoreboard.managers.VariableManager;
import dev._2lstudios.scoreboard.updaters.NametagUpdater;
import dev._2lstudios.scoreboard.updaters.SidebarUpdater;
import dev._2lstudios.scoreboard.updaters.TabUpdater;

public class ListenerInitializer {
    public ListenerInitializer(final Plugin plugin, final NametagUpdater nametagUpdater,
            final SidebarUpdater sidebarUpdater, final TabUpdater tabUpdater, final EssentialsManager essentialsManager) {
        final Server server = plugin.getServer();
        final PluginManager pluginManager = server.getPluginManager();
        final VariableManager variableManager = essentialsManager.getVariableManager();

        pluginManager.registerEvents(new PlayerChangedWorldListener(nametagUpdater, sidebarUpdater), plugin);
        pluginManager.registerEvents(
                new PlayerJoinListener(server, essentialsManager, nametagUpdater, sidebarUpdater, tabUpdater, variableManager),
                plugin);
        pluginManager.registerEvents(new PlayerQuitListener(server, essentialsManager), plugin);
    }
}
