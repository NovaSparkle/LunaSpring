package org.novasparkle.lunaspring.Util.managers;

import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Util.Service.ServiceRegistrationException;
import org.novasparkle.lunaspring.Util.Service.realized.RegionService;

import java.util.Set;
import java.util.UUID;

@UtilityClass
public class RegionManager {
    private RegionService regionService;
    public void init(RegionService service) {
        regionService = service;
    }

    public RegionContainer getRegionContainer() {
        regionService.exceptionCheck(regionService, RegionManager.class);
        return regionService.getRegionContainer();
    }

    public com.sk89q.worldguard.protection.managers.RegionManager getRegionManager(World world) {
        regionService.exceptionCheck(regionService, RegionManager.class);
        return regionService.getRegionManager(world);
    }

    public void createRegion(String name, Location minLoc, Location maxLoc) {
        regionService.exceptionCheck(regionService, RegionManager.class);
        regionService.createRegion(name, minLoc, maxLoc);
    }

    public void removeRegion(String name) {
        regionService.exceptionCheck(regionService, RegionManager.class);
        regionService.removeRegion(name);
    }

    @SuppressWarnings("all")
    public ProtectedRegion getRegion(String regionName) {
        regionService.exceptionCheck(regionService, RegionManager.class);
        return regionService.getRegion(regionName);
    }

    public void addMember(String regionName, Player player) {
        regionService.exceptionCheck(regionService, RegionManager.class);
        regionService.addMember(regionName, player);
    }

    public void addOwner(String regionName, Player player) {
        regionService.exceptionCheck(regionService, RegionManager.class);
        regionService.addOwner(regionName, player);
    }

    public void removeMember(String regionName, Player player) {
        regionService.exceptionCheck(regionService, RegionManager.class);
        regionService.removeMember(regionName, player);
    }

    public void removeOwner(String regionName, Player player) {
        regionService.exceptionCheck(regionService, RegionManager.class);
        regionService.removeOwner(regionName, player);
    }

    public boolean isOwner(String regionName, Player player) {
        regionService.exceptionCheck(regionService, RegionManager.class);
        return regionService.isOwner(regionName, player);
    }

    public World getWorld(String regionName) {
        regionService.exceptionCheck(regionService, RegionManager.class);
        return regionService.getWorld(regionName);
    }

    public Location getPoint(String regionName, boolean isMinPoint) {
        regionService.exceptionCheck(regionService, RegionManager.class);
        return regionService.getPoint(regionName, isMinPoint);
    }

    public boolean containsBlock(String regionName, int x, int y, int z) {
        regionService.exceptionCheck(regionService, RegionManager.class);
        return regionService.containsBlock(regionName, x, y, z);
    }

    public boolean containsBlock(String regionName, Location location) {
        return containsBlock(regionName, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public Set<UUID> getMembers(String regionName) {
        regionService.exceptionCheck(regionService, RegionManager.class);
        return regionService.getMembers(regionName);
    }

    public Set<UUID> getOwners(String regionName) {
        regionService.exceptionCheck(regionService, RegionManager.class);
        return regionService.getOwners(regionName);
    }

    public void setFlag(String regionName, StateFlag flag, StateFlag.State stateFlag) {
        regionService.exceptionCheck(regionService, RegionManager.class);
        regionService.setFlag(regionName, flag, stateFlag);
    }

    public int getCount(Player player) {
        regionService.exceptionCheck(regionService, RegionManager.class);
        return regionService.getCount(player);
    }

    public boolean hasRegionsInside(Location location, int cuboidSize) {
        regionService.exceptionCheck(regionService, RegionManager.class);
        return regionService.hasRegionsInside(location, cuboidSize);
    }
}
