package dev._2lstudios.scoreboard.tasks;

import dev._2lstudios.scoreboard.updaters.TabUpdater;

public class TabRunnable implements Runnable {
    private final TabUpdater tabUpdater;

    public TabRunnable(final TabUpdater tabUpdater) {
        this.tabUpdater = tabUpdater;
    }

    @Override
    public void run() {
        tabUpdater.update();
    }
}
