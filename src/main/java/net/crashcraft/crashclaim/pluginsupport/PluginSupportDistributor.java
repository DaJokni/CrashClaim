package net.crashcraft.crashclaim.pluginsupport;

import net.crashcraft.crashclaim.config.GlobalConfig;
import net.crashcraft.crashclaim.config.GroupSettings;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class PluginSupportDistributor implements PluginSupport {
    private final Set<PluginSupport> enabled;

    public PluginSupportDistributor(PluginSupportManager manager){
        enabled = manager.getEnabledSupport();
    }

    @Override
    public boolean isUnsupportedVersion(String version) {
        throw new RuntimeException("Unsupported Operation.");
    }

    @Override
    public boolean canLoad() {
        return true;
    }

    @Override
    public String getPluginName() {
        throw new RuntimeException("Unsupported Operation.");
    }

    @Override
    public void load(Plugin plugin) {
        throw new RuntimeException("Unsupported Operation.");
    }

    @Override
    public void enable(Plugin plugin) {
        throw new RuntimeException("Unsupported Operation.");
    }

    @Override
    public void disable() {
        throw new RuntimeException("Unsupported Operation.");
    }

    @Override
    public boolean canClaim(Location minLoc, Location maxLoc) {
        for (PluginSupport support : enabled) {
            if (!support.canClaim(minLoc, maxLoc)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean canInteract(Player player, Location location) {
        for (PluginSupport support : enabled) {
            if (!support.canInteract(player, location)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public GroupSettings getPlayerGroupSettings(Player player) {
        for (PluginSupport support : enabled) {
            GroupSettings groupSettings = support.getPlayerGroupSettings(player);
            if (groupSettings != null) {
                return groupSettings;
            }
        }
        return GlobalConfig.groupSettings.get("default");
    }
}
