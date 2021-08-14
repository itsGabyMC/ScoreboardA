package dev._2lstudios.scoreboard.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev._2lstudios.scoreboard.managers.MainManager;
import dev._2lstudios.scoreboard.managers.SidebarPlayerManager;
import dev._2lstudios.scoreboard.managers.VariableManager;
import dev._2lstudios.scoreboard.updaters.NametagUpdater;
import dev._2lstudios.scoreboard.updaters.SidebarUpdater;
import dev._2lstudios.scoreboard.updaters.TabUpdater;
import dev._2lstudios.scoreboard.utils.ScoreboardUtil;

public class PlayerJoinListener implements Listener {
    private final SidebarPlayerManager scoreboardPlayerManager;
    private final NametagUpdater nametagUpdater;
    private final SidebarUpdater sidebarUpdater;
    private final TabUpdater tabUpdater;
    private final VariableManager variableManager;

    public PlayerJoinListener(final MainManager essentialsManager, final NametagUpdater nametagUpdater,
            final SidebarUpdater sidebarUpdater, final TabUpdater tabUpdater, final VariableManager variableManager) {
        this.scoreboardPlayerManager = essentialsManager.getPlayerManager();
        this.nametagUpdater = nametagUpdater;
        this.sidebarUpdater = sidebarUpdater;
        this.tabUpdater = tabUpdater;
        this.variableManager = variableManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (variableManager.getSidebarManager().isEnabled() || variableManager.isNametagEnabled()
                || variableManager.isHealthEnabled()) {
            ScoreboardUtil.setExternalBoard(player);
        }

        scoreboardPlayerManager.addPlayer(player);
        nametagUpdater.updateAsync(player);
        sidebarUpdater.updateAsync(player);
        tabUpdater.updateAsync(player);
    }
}
