package dev._2lstudios.scoreboard.tasks;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import dev._2lstudios.scoreboard.updaters.HealthbarUpdater;

public class HealthBarRunnable implements Runnable {
    private final Server server;
    private final HealthbarUpdater healthbarUpdater;

    public HealthBarRunnable(final Server server, final HealthbarUpdater healthbarUpdater) {
        this.server = server;
        this.healthbarUpdater = healthbarUpdater;
    }

    @Override
    public void run() {
        for (final Player player : server.getOnlinePlayers()) {
            healthbarUpdater.update(player);
        }
    }
}
