package dev._2lstudios.scoreboard.listeners;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import dev._2lstudios.scoreboard.managers.MainManager;
import dev._2lstudios.scoreboard.managers.SidebarPlayerManager;
import dev._2lstudios.scoreboard.managers.VariableManager;
import dev._2lstudios.scoreboard.utils.ScoreboardUtil;

public class PlayerQuitListener implements Listener {
    private final SidebarPlayerManager playerManager;
    private final VariableManager variableManager;
    private final Collection<Player> autofeedPlayers;

    public PlayerQuitListener(final MainManager essentialsManager) {
        this.playerManager = essentialsManager.getPlayerManager();
        this.variableManager = essentialsManager.getVariableManager();
        this.autofeedPlayers = essentialsManager.getAutoFeedPlayers();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        ScoreboardUtil.clearPlayer(variableManager, player);

        this.autofeedPlayers.remove(player);
        this.playerManager.removePlayer(uuid);
    }
}
