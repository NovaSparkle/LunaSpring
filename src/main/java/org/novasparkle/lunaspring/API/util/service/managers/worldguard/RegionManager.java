package org.novasparkle.lunaspring.API.util.service.managers.worldguard;

import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.util.service.RegionService;

import java.util.Set;
import java.util.UUID;

@UtilityClass
public class RegionManager {
    private final RegionService regionService;
    static {
        regionService = new RegionService();
    }

    public RegionContainer getRegionContainer() {
        return regionService.getRegionContainer();
    }

    public com.sk89q.worldguard.protection.managers.RegionManager getRegionManager(World world) {
        return regionService.getRegionManager(world);
    }

    public void createRegion(String name, Location minLoc, Location maxLoc) {
        regionService.createRegion(name, minLoc, maxLoc);
    }

    public void removeRegion(String name) {
        regionService.removeRegion(name);
    }

    public ProtectedRegion getRegion(String regionName) {
        return regionService.getRegion(regionName);
    }

    public void addMember(String regionName, Player player) {
        regionService.addMember(regionName, player);
    }

    public void addOwner(String regionName, Player player) {
        regionService.addOwner(regionName, player);
    }

    public void removeMember(String regionName, Player player) {
        regionService.removeMember(regionName, player);
    }

    public void removeOwner(String regionName, Player player) {
        regionService.removeOwner(regionName, player);
    }

    public boolean isOwner(String regionName, Player player) {
        return regionService.isOwner(regionName, player);
    }

    public boolean isMember(String regionName, Player player) {
        return regionService.isMember(regionName, player);
    }

    public World getWorld(String regionName) {
        return regionService.getWorld(regionName);
    }

    public Location getPoint(String regionName, boolean isMinPoint) {
        return regionService.getPoint(regionName, isMinPoint);
    }

    public boolean containsBlock(String regionName, int x, int y, int z) {
        return regionService.containsBlock(regionName, x, y, z);
    }

    public boolean containsBlock(String regionName, Location location) {
        return containsBlock(regionName, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public Set<UUID> getMembers(String regionName) {
        return regionService.getMembers(regionName);
    }

    public Set<UUID> getOwners(String regionName) {
        return regionService.getOwners(regionName);
    }

    public void setFlag(String regionName, StateFlag flag, StateFlag.State state) {
        regionService.setFlag(regionName, flag, state);
    }

    public void setFlag(String regionName, LFlag lFlag, StateFlag.State state) {
        regionService.setFlag(regionName, lFlag, state);
    }

    public int getCount(Player player) {
        return regionService.getCount(player);
    }

    public boolean hasRegionsInside(Location location, int cuboidSize) {
        return regionService.hasRegionsInside(location, cuboidSize);
    }
}