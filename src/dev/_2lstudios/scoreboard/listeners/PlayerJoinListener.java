package dev._2lstudios.scoreboard.listeners;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.ScoreboardManager;

import dev._2lstudios.scoreboard.managers.EssentialsManager;
import dev._2lstudios.scoreboard.managers.PlayerManager;
import dev._2lstudios.scoreboard.tasks.SecondTask;

public class PlayerJoinListener implements Listener {
    private final Server server;
    private final PlayerManager playerManager;
    private final SecondTask secondTask;

    public PlayerJoinListener(final Server server, final EssentialsManager essentialsManager,
            final SecondTask secondTask) {
        this.server = server;
        this.playerManager = essentialsManager.getPlayerManager();
        this.secondTask = secondTask;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final ScoreboardManager scoreboardManager = this.server.getScoreboardManager();

        this.playerManager.addPlayer(player);

        if (player.getScoreboard() == scoreboardManager.getMainScoreboard()) {
            player.setScoreboard(scoreboardManager.getNewScoreboard());
        }

        this.secondTask.update(player, 0);
    }
}
