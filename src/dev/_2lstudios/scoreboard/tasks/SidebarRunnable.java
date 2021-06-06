package dev._2lstudios.scoreboard.tasks;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import dev._2lstudios.scoreboard.updaters.SidebarUpdater;

public class SidebarRunnable implements Runnable {
    private final Server server;
    private final SidebarUpdater sidebarUpdater;

    public SidebarRunnable(final Server server, final SidebarUpdater sidebarUpdater) {
        this.server = server;
        this.sidebarUpdater = sidebarUpdater;
    }

    @Override
    public void run() {
        for (final Player player : server.getOnlinePlayers()) {
            sidebarUpdater.update(player);
        }
    }
}
