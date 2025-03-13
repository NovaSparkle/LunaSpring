package org.novasparkle.lunaspring.Util.Service.realized;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.Util.Service.LunaService;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class RegionService implements LunaService {
    public RegionContainer getRegionContainer() {
        return WorldGuard.getInstance().getPlatform().getRegionContainer();
    }

    public RegionManager getRegionManager(World world) {
        return this.getRegionContainer().get(BukkitAdapter.adapt(world));
    }

    public void createRegion(String name, Location minLoc, Location maxLoc) {
        World world = minLoc.getWorld();
        if (world != maxLoc.getWorld()) return;

        BlockVector3 minVector = BlockVector3.at(minLoc.getBlockX(), minLoc.getBlockY(), minLoc.getBlockZ());
        BlockVector3 maxVector = BlockVector3.at(maxLoc.getBlockX(), maxLoc.getBlockY(), maxLoc.getBlockZ());
        ProtectedCuboidRegion region = new ProtectedCuboidRegion(name, minVector, maxVector);

        this.getRegionManager(world).addRegion(region);
    }

    public void removeRegion(String name) {
        this.getRegionManager(this.getWorld(name)).removeRegion(name);
    }

    @SuppressWarnings("all")
    public ProtectedRegion getRegion(String regionName) {
        return this.getRegionContainer().getLoaded().stream()
                .filter(regionManager -> regionManager.hasRegion(regionName))
                .map(regionManager -> regionManager.getRegion(regionName))
                .findFirst()
                .orElse(null);
    }

    public void addMember(String regionName, Player player) {
        ProtectedRegion region = this.getRegion(regionName);
        if (region == null) return;

        DefaultDomain domain = region.getMembers();
        domain.addPlayer(player.getName());
        region.setMembers(domain);
    }

    public void addOwner(String regionName, Player player) {
        ProtectedRegion region = this.getRegion(regionName);
        if (region == null) return;

        DefaultDomain domain = region.getOwners();
        domain.addPlayer(player.getName());
        region.setOwners(domain);
    }

    public void removeMember(String regionName, Player player) {
        ProtectedRegion region = this.getRegion(regionName);
        if (region == null) return;

        DefaultDomain domain = region.getMembers();
        domain.removePlayer(player.getName());
        region.setMembers(domain);
    }

    public void removeOwner(String regionName, Player player) {
        ProtectedRegion region = this.getRegion(regionName);
        if (region == null) return;

        DefaultDomain domain = region.getOwners();
        domain.removePlayer(player.getName());
        region.setOwners(domain);
    }

    @SuppressWarnings("deprecation")
    public boolean isOwner(String regionName, Player player) {
        ProtectedRegion region = this.getRegion(regionName);
        return region != null && region.isOwner(player.getName());
    }

    public World getWorld(String regionName) {
        for (World world : Bukkit.getWorlds()) {
            RegionManager manager = this.getRegionManager(world);
            if (manager != null && manager.hasRegion(regionName)) return world;
        }
        return null;
    }

    public Location getPoint(String regionName, boolean isMinPoint) {
        ProtectedRegion region = this.getRegion(regionName);
        if (region == null) return null;

        BlockVector3 vector3 = isMinPoint ? region.getMinimumPoint() : region.getMaximumPoint();
        World world = this.getWorld(regionName);
        return world == null ? null : new Location(
                world, vector3.getBlockX(), vector3.getBlockY(), vector3.getBlockZ());
    }

    public boolean containsBlock(String regionName, int x, int y, int z) {
        ProtectedRegion region = this.getRegion(regionName);
        return region != null && region.contains(x, y, z);
    }

    public Set<UUID> getMembers(String regionName) {
        ProtectedRegion region = this.getRegion(regionName);
        return region == null ? null : region.getMembers().getUniqueIds();
    }

    public Set<UUID> getOwners(String regionName) {
        ProtectedRegion region = this.getRegion(regionName);
        return region == null ? null : region.getOwners().getUniqueIds();
    }

    public void setFlag(String regionName, StateFlag flag, StateFlag.State stateFlag) {
        ProtectedRegion region = this.getRegion(regionName);
        if (region == null) return;

        region.setFlag(flag, stateFlag);
    }

    public int getCount(Player player) {
        int amo = 0;
        for (RegionManager regionManager : this.getRegionContainer().getLoaded()) {
            amo += regionManager.getRegions().values()
                    .stream()
                    .filter(rg -> this.isOwner(rg.getId(), player))
                    .collect(Collectors.toSet()).size();
        }
        return amo;
    }

    public boolean hasRegionsInside(Player player, Location location, int cuboidSize) {
        RegionManager manager = this.getRegionManager(location.getWorld());
        BlockVector3 max = BlockVector3.at(
                location.getX() + cuboidSize, location.getY() + cuboidSize, location.getZ() + cuboidSize);
        BlockVector3 min = BlockVector3.at(
                location.getX() - cuboidSize, location.getY() - cuboidSize, location.getZ() - cuboidSize);

        ProtectedRegion rg = new ProtectedCuboidRegion(player.getName(), min, max);
        ApplicableRegionSet set = manager.getApplicableRegions(rg);

        return !set.getRegions().isEmpty();
    }
}
