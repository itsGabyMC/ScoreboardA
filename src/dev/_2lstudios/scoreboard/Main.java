package dev._2lstudios.scoreboard;

import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev._2lstudios.scoreboard.commands.initializers.CommandInitializer;
import dev._2lstudios.scoreboard.hooks.TeamsHook;
import dev._2lstudios.scoreboard.listeners.initializers.ListenerInitializer;
import dev._2lstudios.scoreboard.managers.MainManager;
import dev._2lstudios.scoreboard.managers.PlaceholderAPIManager;
import dev._2lstudios.scoreboard.managers.PrefixSuffixManager;
import dev._2lstudios.scoreboard.managers.SidebarPlayerManager;
import dev._2lstudios.scoreboard.managers.VariableManager;
import dev._2lstudios.scoreboard.tasks.TaskInitializer;
import dev._2lstudios.scoreboard.updaters.HealthbarUpdater;
import dev._2lstudios.scoreboard.updaters.NametagUpdater;
import dev._2lstudios.scoreboard.updaters.SidebarUpdater;
import dev._2lstudios.scoreboard.updaters.TabUpdater;
import dev._2lstudios.scoreboard.utils.ConfigurationUtil;
import dev._2lstudios.scoreboard.utils.ScoreboardUtil;
import dev._2lstudios.scoreboard.utils.VersionUtil;

public class Main extends JavaPlugin {
    private static MainManager mainManager;

    public synchronized void onEnable() {
        VersionUtil.init();

        final Server server = getServer();
        final TeamsHook teamsHook = new TeamsHook();
        final ConfigurationUtil configurationUtil = new ConfigurationUtil(this);

        if (server.getPluginManager().isPluginEnabled("Teams")) {
            teamsHook.hook();
        }

        mainManager = new MainManager(this, configurationUtil);

        final VariableManager variableManager = mainManager.getVariableManager();
        final SidebarPlayerManager sidebarPlayerManager = mainManager.getPlayerManager();
        final PlaceholderAPIManager placeholderAPIManager = mainManager.getPlaceholderAPIManager();
        final PrefixSuffixManager prefixSuffixManager = new PrefixSuffixManager(mainManager, teamsHook);
        final HealthbarUpdater healthbarUpdater = new HealthbarUpdater(this, sidebarPlayerManager, variableManager);
        final NametagUpdater nametagUpdater = new NametagUpdater(this, placeholderAPIManager, sidebarPlayerManager,
                prefixSuffixManager, variableManager, teamsHook);
        final SidebarUpdater sidebarUpdater = new SidebarUpdater(this, sidebarPlayerManager, variableManager,
                placeholderAPIManager);
        final TabUpdater tabUpdater = new TabUpdater(this, prefixSuffixManager, variableManager);

        new CommandInitializer(this, mainManager);
        new ListenerInitializer(this, nametagUpdater, sidebarUpdater, tabUpdater, mainManager);
        new TaskInitializer(this, mainManager, healthbarUpdater, nametagUpdater, sidebarUpdater, tabUpdater);

        for (final Player player : server.getOnlinePlayers()) {
            mainManager.getPlayerManager().addPlayer(player);
        }
    }

    public synchronized void onDisable() {
        final SidebarPlayerManager playerManager = Main.mainManager.getPlayerManager();
        final Server server = this.getServer();
        final VariableManager variableManager = Main.mainManager.getVariableManager();

        for (final Player player : server.getOnlinePlayers()) {
            final UUID uuid = player.getUniqueId();

            ScoreboardUtil.clearPlayer(variableManager, player);
            playerManager.removePlayer(uuid);
        }
    }

    public static MainManager getMainManager() {
        return mainManager;
    }
}
