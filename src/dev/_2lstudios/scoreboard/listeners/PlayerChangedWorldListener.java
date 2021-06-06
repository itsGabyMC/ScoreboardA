package dev._2lstudios.scoreboard.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import dev._2lstudios.scoreboard.updaters.NametagUpdater;
import dev._2lstudios.scoreboard.updaters.SidebarUpdater;

public class PlayerChangedWorldListener implements Listener {
    private final NametagUpdater nametagUpdater;
    private final SidebarUpdater sidebarUpdater;

    public PlayerChangedWorldListener(final NametagUpdater nametagUpdater, final SidebarUpdater sidebarUpdater) {
        this.nametagUpdater = nametagUpdater;
        this.sidebarUpdater = sidebarUpdater;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();

        nametagUpdater.updateAsync(player);
        sidebarUpdater.updateAsync(player);
    }
}
