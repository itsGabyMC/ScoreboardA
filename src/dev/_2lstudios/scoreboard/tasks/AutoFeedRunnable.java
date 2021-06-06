package dev._2lstudios.scoreboard.tasks;

import java.util.Collection;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import dev._2lstudios.scoreboard.hooks.TeamsHook;
import dev._2lstudios.scoreboard.managers.EssentialsManager;

public class AutoFeedRunnable implements Runnable {
    private final Collection<Player> autofeedPlayers;

    public AutoFeedRunnable(final Plugin plugin, final EssentialsManager essentialsManager, final TeamsHook teamsHook) {
        this.autofeedPlayers = essentialsManager.getAutoFeedPlayers();
    }

    @Override
    public void run() {
        for (final Player player : autofeedPlayers) {
            player.setFoodLevel(20);
            player.setSaturation(4.0f);
        }
    }
}
