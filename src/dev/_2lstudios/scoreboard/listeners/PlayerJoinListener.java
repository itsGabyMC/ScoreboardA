package dev._2lstudios.scoreboard.listeners;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.ScoreboardManager;

import dev._2lstudios.scoreboard.managers.EssentialsManager;
import dev._2lstudios.scoreboard.managers.SidebarPlayerManager;
import dev._2lstudios.scoreboard.managers.VariableManager;
import dev._2lstudios.scoreboard.updaters.NametagUpdater;
import dev._2lstudios.scoreboard.updaters.SidebarUpdater;
import dev._2lstudios.scoreboard.updaters.TabUpdater;

public class PlayerJoinListener implements Listener {
    private final Server server;
    private final SidebarPlayerManager scoreboardPlayerManager;
    private final NametagUpdater nametagUpdater;
    private final SidebarUpdater sidebarUpdater;
    private final TabUpdater tabUpdater;
    private final VariableManager variableManager;

    public PlayerJoinListener(final Server server, final EssentialsManager essentialsManager,
            final NametagUpdater nametagUpdater, final SidebarUpdater sidebarUpdater, final TabUpdater tabUpdater,
            final VariableManager variableManager) {
        this.server = server;
        this.scoreboardPlayerManager = essentialsManager.getPlayerManager();
        this.nametagUpdater = nametagUpdater;
        this.sidebarUpdater = sidebarUpdater;
        this.tabUpdater = tabUpdater;
        this.variableManager = variableManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final ScoreboardManager scoreboardManager = server.getScoreboardManager();

        scoreboardPlayerManager.addPlayer(player);

        if (variableManager.getSidebarManager().isEnabled() || variableManager.isNametagEnabled()
                && player.getScoreboard() == scoreboardManager.getMainScoreboard()) {
            player.setScoreboard(scoreboardManager.getNewScoreboard());
        }

        nametagUpdater.updateAsync(player);
        sidebarUpdater.updateAsync(player);
        tabUpdater.updateAsync(player);
    }
}
