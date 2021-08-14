package dev._2lstudios.scoreboard.commands.initializers;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import dev._2lstudios.scoreboard.commands.NametagCMD;
import dev._2lstudios.scoreboard.commands.ScoreboardCMD;
import dev._2lstudios.scoreboard.managers.MainManager;

public class CommandInitializer {
    public CommandInitializer(final JavaPlugin plugin, final MainManager essentialsManager) {
        plugin.getCommand("nametag").setExecutor((CommandExecutor) new NametagCMD(essentialsManager));
        plugin.getCommand("scoreboard").setExecutor((CommandExecutor) new ScoreboardCMD(essentialsManager));
    }
}
