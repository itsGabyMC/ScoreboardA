package dev._2lstudios.gameessentials;

import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import dev._2lstudios.gameessentials.commands.initializers.CommandInitializer;
import dev._2lstudios.gameessentials.hooks.TeamsHook;
import dev._2lstudios.gameessentials.instanceables.ScoreboardPlayer;
import dev._2lstudios.gameessentials.listeners.initializers.ListenerInitializer;
import dev._2lstudios.gameessentials.managers.EssentialsManager;
import dev._2lstudios.gameessentials.managers.PlayerManager;
import dev._2lstudios.gameessentials.tasks.SecondTask;
import dev._2lstudios.gameessentials.utils.ConfigurationUtil;
import dev._2lstudios.gameessentials.utils.VersionUtil;

public class GameEssentials extends JavaPlugin {
    private static EssentialsManager essentialsManager;
    private SecondTask secondTask;

    static {
        GameEssentials.essentialsManager = null;
    }

    public GameEssentials() {
        this.secondTask = null;
    }

    public synchronized void onEnable() {
        VersionUtil.init();
        final TeamsHook teamsHook = new TeamsHook();
        final ConfigurationUtil configurationUtil = new ConfigurationUtil((Plugin) this);
        if (this.getServer().getPluginManager().isPluginEnabled("Teams")) {
            teamsHook.hook();
        }
        GameEssentials.essentialsManager = new EssentialsManager(this, configurationUtil);
        new CommandInitializer(this, GameEssentials.essentialsManager);
        this.secondTask = new SecondTask((Plugin) this, GameEssentials.essentialsManager, teamsHook);
        new ListenerInitializer((Plugin) this, GameEssentials.essentialsManager, this.secondTask);
        for (final Player player : this.getServer().getOnlinePlayers()) {
            GameEssentials.essentialsManager.getPlayerManager().addPlayer(player);
            if (GameEssentials.essentialsManager.getVariableManager().getSidebarManager().isEnabled()
                    || GameEssentials.essentialsManager.getVariableManager().isNametagEnabled()) {
                player.setScoreboard(this.getServer().getScoreboardManager().getNewScoreboard());
            }
            this.secondTask.update(player, 0);
        }
    }

    public synchronized void onDisable() {
        final PlayerManager playerManager = GameEssentials.essentialsManager.getPlayerManager();
        final Server server = this.getServer();
        final boolean scoreboardEnabled = GameEssentials.essentialsManager.getVariableManager().getSidebarManager()
                .isEnabled();
        final boolean nametagEnabled = GameEssentials.essentialsManager.getVariableManager().isNametagEnabled();

        server.getScheduler().cancelTasks((Plugin) this);
        this.secondTask.stop();
        for (final Player player : server.getOnlinePlayers()) {
            final UUID uuid = player.getUniqueId();
            final ScoreboardPlayer essentialsPlayer = playerManager.getPlayer(uuid);
            if (essentialsPlayer != null) {
                final Scoreboard scoreboard = player.getScoreboard();
                if (scoreboardEnabled) {
                    scoreboard.clearSlot(DisplaySlot.SIDEBAR);
                }
                if (nametagEnabled) {
                    scoreboard.clearSlot(DisplaySlot.BELOW_NAME);
                }

                playerManager.removePlayer(uuid);
            }
        }
    }

    public static EssentialsManager getEssentialsManager() {
        return GameEssentials.essentialsManager;
    }
}
