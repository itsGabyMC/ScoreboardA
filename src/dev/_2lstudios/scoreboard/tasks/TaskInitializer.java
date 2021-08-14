package dev._2lstudios.scoreboard.tasks;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.scoreboard.managers.MainManager;
import dev._2lstudios.scoreboard.managers.SidebarPlayerManager;
import dev._2lstudios.scoreboard.managers.VariableManager;
import dev._2lstudios.scoreboard.updaters.HealthbarUpdater;
import dev._2lstudios.scoreboard.updaters.NametagUpdater;
import dev._2lstudios.scoreboard.updaters.SidebarUpdater;
import dev._2lstudios.scoreboard.updaters.TabUpdater;

public class TaskInitializer {
    public TaskInitializer(final Plugin plugin, final MainManager mainManager, final HealthbarUpdater healthbarUpdater,
            final NametagUpdater nametagUpdater, final SidebarUpdater sidebarUpdater, final TabUpdater tabUpdater) {
        final Server server = plugin.getServer();
        final VariableManager variableManager = mainManager.getVariableManager();
        final SidebarPlayerManager sidebarPlayerManager = mainManager.getPlayerManager();

        if (variableManager.isHealthEnabled()) {
            server.getScheduler().runTaskTimerAsynchronously(plugin, new HealthBarRunnable(healthbarUpdater), 20L, 20L);
        }

        if (variableManager.isNametagEnabled()) {
            server.getScheduler().runTaskTimerAsynchronously(plugin,
                    new NametagRunnable(server, sidebarPlayerManager, nametagUpdater), 20L, 20L);
        }

        if (variableManager.getSidebarManager().isEnabled()) {
            server.getScheduler().runTaskTimerAsynchronously(plugin, new SidebarRunnable(server, sidebarUpdater), 20L,
                    20L);
        }

        if (variableManager.isTabEnabled()) {
            server.getScheduler().runTaskTimerAsynchronously(plugin, new TabRunnable(tabUpdater), 20L, 20L);
        }
    }
}
