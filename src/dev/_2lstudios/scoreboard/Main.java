package dev._2lstudios.scoreboard;

import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import dev._2lstudios.scoreboard.commands.initializers.CommandInitializer;
import dev._2lstudios.scoreboard.hooks.TeamsHook;
import dev._2lstudios.scoreboard.instanceables.SidebarPlayer;
import dev._2lstudios.scoreboard.listeners.initializers.ListenerInitializer;
import dev._2lstudios.scoreboard.managers.EssentialsManager;
import dev._2lstudios.scoreboard.managers.PlaceholderAPIManager;
import dev._2lstudios.scoreboard.managers.PrefixSuffixManager;
import dev._2lstudios.scoreboard.managers.SidebarPlayerManager;
import dev._2lstudios.scoreboard.managers.VariableManager;
import dev._2lstudios.scoreboard.tasks.HealthBarRunnable;
import dev._2lstudios.scoreboard.tasks.NametagRunnable;
import dev._2lstudios.scoreboard.tasks.SidebarRunnable;
import dev._2lstudios.scoreboard.updaters.HealthbarUpdater;
import dev._2lstudios.scoreboard.updaters.NametagUpdater;
import dev._2lstudios.scoreboard.updaters.SidebarUpdater;
import dev._2lstudios.scoreboard.updaters.TabUpdater;
import dev._2lstudios.scoreboard.utils.ConfigurationUtil;
import dev._2lstudios.scoreboard.utils.VersionUtil;

public class Main extends JavaPlugin {
    private static EssentialsManager essentialsManager;

    public synchronized void onEnable() {
        VersionUtil.init();

        final Server server = getServer();
        final TeamsHook teamsHook = new TeamsHook();
        final ConfigurationUtil configurationUtil = new ConfigurationUtil(this);

        if (server.getPluginManager().isPluginEnabled("Teams")) {
            teamsHook.hook();
        }

        essentialsManager = new EssentialsManager(this, configurationUtil);

        final VariableManager variableManager = essentialsManager.getVariableManager();
        final SidebarPlayerManager sidebarPlayerManager = essentialsManager.getPlayerManager();
        final PlaceholderAPIManager placeholderAPIManager = essentialsManager.getPlaceholderAPIManager();
        final PrefixSuffixManager prefixSuffixManager = new PrefixSuffixManager(essentialsManager, teamsHook);
        final HealthbarUpdater healthbarUpdater = new HealthbarUpdater(sidebarPlayerManager, variableManager);
        final NametagUpdater nametagUpdater = new NametagUpdater(this, placeholderAPIManager, sidebarPlayerManager,
                prefixSuffixManager, variableManager, teamsHook);
        final SidebarUpdater sidebarUpdater = new SidebarUpdater(this, sidebarPlayerManager, variableManager,
                placeholderAPIManager);
        final TabUpdater tabUpdater = new TabUpdater(this, prefixSuffixManager);

        new CommandInitializer(this, essentialsManager);
        new ListenerInitializer(this, nametagUpdater, sidebarUpdater, tabUpdater, essentialsManager);

        if (variableManager.isHealthEnabled()) {
            server.getScheduler().runTaskTimerAsynchronously(this, new HealthBarRunnable(server, healthbarUpdater), 20L,
                    20L);
        }

        if (variableManager.isNametagEnabled()) {
            server.getScheduler().runTaskTimerAsynchronously(this,
                    new NametagRunnable(server, sidebarPlayerManager, nametagUpdater), 20L, 20L);
        }

        if (variableManager.getSidebarManager().isEnabled()) {
            server.getScheduler().runTaskTimerAsynchronously(this, new SidebarRunnable(server, sidebarUpdater), 20L,
                    20L);
        }

        for (final Player player : server.getOnlinePlayers()) {
            essentialsManager.getPlayerManager().addPlayer(player);

            if (variableManager.getSidebarManager().isEnabled() || variableManager.isNametagEnabled()) {
                player.setScoreboard(server.getScoreboardManager().getNewScoreboard());
            }

            nametagUpdater.update(player);
            sidebarUpdater.update(player);
            tabUpdater.update(player);
        }
    }

    public synchronized void onDisable() {
        final SidebarPlayerManager playerManager = Main.essentialsManager.getPlayerManager();
        final Server server = this.getServer();
        final VariableManager variableManager = Main.essentialsManager.getVariableManager();

        server.getScheduler().cancelTasks((Plugin) this);

        for (final Player player : server.getOnlinePlayers()) {
            final UUID uuid = player.getUniqueId();
            final SidebarPlayer essentialsPlayer = playerManager.getPlayer(uuid);

            if (essentialsPlayer != null) {
                final Scoreboard scoreboard = player.getScoreboard();

                if (variableManager.getSidebarManager().isEnabled()) {
                    scoreboard.clearSlot(DisplaySlot.SIDEBAR);
                }
        
                if (variableManager.isHealthEnabled()) {
                    scoreboard.clearSlot(DisplaySlot.BELOW_NAME);
                }
        
                if (variableManager.isNametagEnabled()) {
                    scoreboard.clearSlot(DisplaySlot.PLAYER_LIST);
                }

                playerManager.removePlayer(uuid);
            }
        }
    }

    public static EssentialsManager getEssentialsManager() {
        return Main.essentialsManager;
    }
}
