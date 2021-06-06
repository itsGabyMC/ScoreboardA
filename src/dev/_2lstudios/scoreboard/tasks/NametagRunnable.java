package dev._2lstudios.scoreboard.tasks;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import dev._2lstudios.scoreboard.instanceables.SidebarPlayer;
import dev._2lstudios.scoreboard.managers.SidebarPlayerManager;
import dev._2lstudios.scoreboard.updaters.NametagUpdater;

public class NametagRunnable implements Runnable {
    private final Server server;
    private final SidebarPlayerManager sidebarPlayerManager;
    private final NametagUpdater nametagUpdater;

    public NametagRunnable(final Server server, final SidebarPlayerManager sidebarPlayerManager, final NametagUpdater nametagUpdater) {
        this.server = server;
        this.sidebarPlayerManager = sidebarPlayerManager;
        this.nametagUpdater = nametagUpdater;
    }

    @Override
    public void run() {
        for (final Player player : server.getOnlinePlayers()) {
            final SidebarPlayer sidebarPlayer = sidebarPlayerManager.getPlayer(player);
            final boolean visible = !player.hasPotionEffect(PotionEffectType.INVISIBILITY);

            if (visible != sidebarPlayer.isVisible()) {
                sidebarPlayer.setVisible(visible);
                nametagUpdater.updateOthers(player);
            }
        }
    }
}
