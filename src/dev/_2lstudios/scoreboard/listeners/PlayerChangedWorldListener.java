package dev._2lstudios.scoreboard.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import dev._2lstudios.scoreboard.tasks.SecondTask;

public class PlayerChangedWorldListener implements Listener {
    private final SecondTask secondTask;

    public PlayerChangedWorldListener(final SecondTask secondTask) {
        this.secondTask = secondTask;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();

        secondTask.update(player, 0);
    }
}
