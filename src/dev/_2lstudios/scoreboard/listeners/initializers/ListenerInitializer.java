package dev._2lstudios.scoreboard.listeners.initializers;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import dev._2lstudios.scoreboard.listeners.PlayerJoinListener;
import dev._2lstudios.scoreboard.listeners.PlayerQuitListener;
import dev._2lstudios.scoreboard.managers.EssentialsManager;
import dev._2lstudios.scoreboard.tasks.SecondTask;

public class ListenerInitializer {
    public ListenerInitializer(final Plugin plugin, final EssentialsManager essentialsManager,
            final SecondTask secondTask) {
        final Server server = plugin.getServer();
        final PluginManager pluginManager = server.getPluginManager();

        pluginManager.registerEvents(new PlayerJoinListener(server, essentialsManager, secondTask), plugin);
        pluginManager.registerEvents(new PlayerQuitListener(server, essentialsManager), plugin);
    }
}
