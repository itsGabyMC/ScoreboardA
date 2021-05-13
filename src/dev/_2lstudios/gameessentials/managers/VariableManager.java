package dev._2lstudios.gameessentials.managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.configuration.Configuration;

import dev._2lstudios.gameessentials.utils.ConfigurationUtil;

public class VariableManager {
    private final ConfigurationUtil configurationUtil;
    private final SidebarManager sidebarManager;
    private Collection<String> nametagWhitelist;
    private boolean nametagEnabled;
    private boolean tabEnabled;

    public VariableManager(final ConfigurationUtil configurationUtil) {
        this.configurationUtil = configurationUtil;
        this.sidebarManager = new SidebarManager();
        this.reload();
    }

    void reload() {
        this.configurationUtil.createConfiguration("%datafolder%/config.yml");
        this.configurationUtil.createConfiguration("%datafolder%/motd.yml");
        this.configurationUtil.createConfiguration("%datafolder%/spawn.yml");
        this.configurationUtil.createConfiguration("%datafolder%/kill_actions.yml");
        final Configuration configYml = (Configuration) this.configurationUtil
                .getConfiguration("%datafolder%/config.yml");
        this.nametagWhitelist = new HashSet<String>(configYml.getStringList("nametag.whitelist"));
        this.nametagEnabled = configYml.getBoolean("nametag.enabled");
        this.tabEnabled = configYml.getBoolean("tab.enabled");
        final boolean sideBarEnabled = configYml.getBoolean("scoreboard.enabled");
        final Map<String, Collection<String>> sidebars = new HashMap<String, Collection<String>>();
        for (final String string : configYml.getConfigurationSection("scoreboard").getKeys(false)) {
            if (!string.equalsIgnoreCase("enabled")) {
                sidebars.put(string, configYml.getStringList("scoreboard." + string));
            }
        }
        this.sidebarManager.reload(sideBarEnabled, sidebars);
    }

    public boolean isNametagEnabled() {
        return this.nametagEnabled;
    }

    public boolean isTabEnabled() {
        return this.tabEnabled;
    }

    public Collection<String> getNametagWhitelist() {
        return this.nametagWhitelist;
    }

    public SidebarManager getSidebarManager() {
        return this.sidebarManager;
    }
}
