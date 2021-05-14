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
import dev._2lstudios.scoreboard.instanceables.ScoreboardPlayer;
import dev._2lstudios.scoreboard.listeners.initializers.ListenerInitializer;
import dev._2lstudios.scoreboard.managers.EssentialsManager;
import dev._2lstudios.scoreboard.managers.PlayerManager;
import dev._2lstudios.scoreboard.tasks.SecondTask;
import dev._2lstudios.scoreboard.utils.ConfigurationUtil;
import dev._2lstudios.scoreboard.utils.VersionUtil;

public class Main extends JavaPlugin {
    private static EssentialsManager essentialsManager;
    private SecondTask secondTask;

    static {
        Main.essentialsManager = null;
    }

    public Main() {
        this.secondTask = null;
    }

    public synchronized void onEnable() {
        VersionUtil.init();
        final TeamsHook teamsHook = new TeamsHook();
        final ConfigurationUtil configurationUtil = new ConfigurationUtil((Plugin) this);
        if (this.getServer().getPluginManager().isPluginEnabled("Teams")) {
            teamsHook.hook();
        }
        Main.essentialsManager = new EssentialsManager(this, configurationUtil);
        new CommandInitializer(this, Main.essentialsManager);
        this.secondTask = new SecondTask((Plugin) this, Main.essentialsManager, teamsHook);
        new ListenerInitializer((Plugin) this, Main.essentialsManager, this.secondTask);
        for (final Player player : this.getServer().getOnlinePlayers()) {
            Main.essentialsManager.getPlayerManager().addPlayer(player);
            if (Main.essentialsManager.getVariableManager().getSidebarManager().isEnabled()
                    || Main.essentialsManager.getVariableManager().isNametagEnabled()) {
                player.setScoreboard(this.getServer().getScoreboardManager().getNewScoreboard());
            }
            this.secondTask.update(player, 0);
        }
    }

    public synchronized void onDisable() {
        final PlayerManager playerManager = Main.essentialsManager.getPlayerManager();
        final Server server = this.getServer();
        final boolean scoreboardEnabled = Main.essentialsManager.getVariableManager().getSidebarManager()
                .isEnabled();
        final boolean nametagEnabled = Main.essentialsManager.getVariableManager().isNametagEnabled();

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
        return Main.essentialsManager;
    }
}
