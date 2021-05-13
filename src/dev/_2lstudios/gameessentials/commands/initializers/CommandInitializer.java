package dev._2lstudios.gameessentials.commands.initializers;

import dev._2lstudios.gameessentials.commands.NametagCMD;
import org.bukkit.command.CommandExecutor;
import dev._2lstudios.gameessentials.commands.ScoreboardCMD;
import dev._2lstudios.gameessentials.managers.EssentialsManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandInitializer {
    public CommandInitializer(final JavaPlugin plugin, final EssentialsManager essentialsManager) {
        plugin.getCommand("nametag").setExecutor((CommandExecutor) new NametagCMD(essentialsManager));
        plugin.getCommand("scoreboard").setExecutor((CommandExecutor) new ScoreboardCMD(essentialsManager));
    }
}
