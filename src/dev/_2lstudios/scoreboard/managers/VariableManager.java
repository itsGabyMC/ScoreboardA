package dev._2lstudios.scoreboard.managers;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.Configuration;

import dev._2lstudios.scoreboard.utils.ConfigurationUtil;

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

    private void reloadNametag(final Configuration config) {
        this.nametagWhitelist = new HashSet<String>(config.getStringList("nametag.whitelist"));
        this.nametagEnabled = config.getBoolean("nametag.enabled");
    }

    private void reloadSidebar(final Configuration config) {
        final boolean sideBarEnabled = config.getBoolean("scoreboard.enabled");
        final Map<String, List<String>> sidebars = new HashMap<String, List<String>>();

        for (final String string : config.getConfigurationSection("scoreboard").getKeys(false)) {
            if (!string.equalsIgnoreCase("enabled")) {
                sidebars.put(string, config.getStringList("scoreboard." + string));
            }
        }

        this.sidebarManager.reload(sideBarEnabled, sidebars);
    }

    void reload() {
        final Configuration config = this.configurationUtil.getOrCreate("%datafolder%/config.yml");

        this.tabEnabled = config.getBoolean("tab.enabled");

        reloadNametag(config);
        reloadSidebar(config);
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
