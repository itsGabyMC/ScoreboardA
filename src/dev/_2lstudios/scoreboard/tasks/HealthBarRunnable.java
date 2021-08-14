package dev._2lstudios.scoreboard.tasks;

import dev._2lstudios.scoreboard.updaters.HealthbarUpdater;

public class HealthBarRunnable implements Runnable {
    private final HealthbarUpdater healthbarUpdater;

    public HealthBarRunnable(final HealthbarUpdater healthbarUpdater) {
        this.healthbarUpdater = healthbarUpdater;
    }

    @Override
    public void run() {
        healthbarUpdater.update();
    }
}
