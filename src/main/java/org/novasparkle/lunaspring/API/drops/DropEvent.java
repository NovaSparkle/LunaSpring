package org.novasparkle.lunaspring.API.drops;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.API.util.service.managers.RegionManager;
import org.novasparkle.lunaspring.API.util.service.managers.WorldEditManager;
import org.novasparkle.lunaspring.API.util.utilities.LunaTask;
import org.novasparkle.lunaspring.API.util.utilities.Utils;
import org.novasparkle.lunaspring.LunaPlugin;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.io.File;
import java.util.List;

@Getter
public abstract class DropEvent {
    private final LunaPlugin lunaPlugin;
    private final DropContainer dropContainer;
    private final Delay delay;

    private Location location;
    private EditSession editSession;
    private String regionId;
    private boolean isStarted = false;
    public DropEvent(LunaPlugin lunaPlugin, DropContainer dropContainer, int secondsToRemove) {
        this.lunaPlugin = lunaPlugin;
        this.dropContainer = dropContainer;
        this.delay = new Delay(this, secondsToRemove);
    }

    public void initLocation(World world, int regionSize, ConfigurationSection spawnSettings) {
        int maxX = spawnSettings.getInt("maxX");
        int maxZ = spawnSettings.getInt("maxZ");

        List<String> invalidBiomes = spawnSettings.getStringList("invalid_biomes");
        List<String> blacklistBlocks = spawnSettings.getStringList("blacklist_blocks");
        assert world != null;

        int attempts = spawnSettings.getInt("findLocationAttempts");
        this.initLocation(world, regionSize, maxX, maxZ, blacklistBlocks, invalidBiomes, attempts);
    }

    public void initLocation(World world, int regionSize, int maxX, int maxZ, List<String> blacklistBlocks, List<String> invalidBiomes, int maxAttempts) {
        int attempts = maxAttempts;
        while (this.location == null && attempts > 0) {
            Location location = Utils.findRandomLocation(world, maxX, maxZ);

            location.add(0, 1, 0);
            if (this.checkRegion(location, regionSize)
                    || this.checkBiome(location, invalidBiomes)
                        || this.checkBlacklist(location.clone().add(0, -1, 0), blacklistBlocks)) {
                attempts--;
                continue;
            }

            this.location = location;
            break;
        }
    }

    public boolean checkRegion(Location location, int cuboidSize) {
        return cuboidSize > -1 && RegionManager.hasRegionsInside(location, cuboidSize + 1);
    }

    public boolean checkBiome(Location location, List<String> biomes) {
        return biomes.contains(location.getBlock().getBiome().name());
    }

    public boolean checkBlacklist(Location location, List<String> blacklist) {
        return blacklist.contains(location.getBlock().getType().name());
    }

    public void insertSchematic(ConfigurationSection schemSection, File schemDir) {
        if (!schemSection.getBoolean("enabled") || this.location == null || this.editSession != null) return;

        File file = new File(schemDir, String.format("%s.schem", schemSection.getString("id")));
        if (!file.exists() || file.isDirectory()) return;

        ConfigurationSection offsets = schemSection.getConfigurationSection("offsets");
        assert offsets != null;
        Bukkit.getScheduler().runTask(this.lunaPlugin, () -> this.editSession = WorldEditManager.pasteSchematic(
                file, this.location,
                offsets.getInt("x"),
                offsets.getInt("y"),
                offsets.getInt("z"),
                offsets.getBoolean("ignore_air_blocks")));
    }

    @OverridingMethodsMustInvokeSuper
    public boolean start() {
        if (this.location == null) return false;

        this.isStarted = true;
        this.delay.runTaskAsynchronously(this.lunaPlugin);
        return true;
    }

    public void createRegion(int regionSize, List<String> flagList) {
        if (this.location == null) return;
        Location minLoc = this.location.clone().add(-regionSize, -regionSize, -regionSize);
        Location maxLoc = this.location.clone().add(regionSize, regionSize, regionSize);

        this.regionId = "drop-" + Utils.getRKey((byte) 8) + "-" + this.dropContainer.getId();
        Bukkit.getScheduler().runTask(this.lunaPlugin, () -> {
            RegionManager.createRegion(this.regionId, minLoc, maxLoc);

            ProtectedRegion region = RegionManager.getRegion(this.regionId);
            flagList.forEach(f -> {
                String[] split = f.split(" <> ");
                if (split.length >= 2) region.setFlag(SFlag.valueOf(split[0]).getStateFlag(), StateFlag.State.valueOf(split[1]));
            });
        });
    }

    public void removeRegion() {
        if (this.regionId != null) RegionManager.removeRegion(this.regionId);
    }

    public void removeSchematic() {
        if (this.editSession != null && this.location != null) WorldEditManager.undo(this.editSession, this.location.getWorld());
    }

    @OverridingMethodsMustInvokeSuper
    public void stop() {
        this.removeSchematic();
        this.removeRegion();

        this.delay.cancel();
        this.isStarted = false;
        this.dropContainer.delete();
    }

    @Getter
    public static class Delay extends LunaTask {
        private int leftSeconds;
        private final DropEvent dropEvent;
        public Delay(DropEvent dropEvent, int seconds) {
            super(0);
            this.dropEvent = dropEvent;
            this.leftSeconds = seconds;
        }

        @Override @SneakyThrows @SuppressWarnings("all")
        public void start() {
            int seconds = 0;
            while (seconds < this.leftSeconds) {
                if (!this.isActive()) return;

                this.leftSeconds--;
                Thread.sleep(1000L);
            }

            this.dropEvent.stop();
        }
    }
}
