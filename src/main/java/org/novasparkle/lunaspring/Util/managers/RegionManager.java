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
        exceptionCheck();
        return regionService.getRegionContainer();
    }

    public com.sk89q.worldguard.protection.managers.RegionManager getRegionManager(World world) {
        exceptionCheck();
        return regionService.getRegionManager(world);
    }

    public void createRegion(String name, Location minLoc, Location maxLoc) {
        exceptionCheck();
        regionService.createRegion(name, minLoc, maxLoc);
    }

    public void removeRegion(String name) {
        exceptionCheck();
        regionService.removeRegion(name);
    }

    @SuppressWarnings("all")
    public ProtectedRegion getRegion(String regionName) {
        exceptionCheck();
        return regionService.getRegion(regionName);
    }

    public void addMember(String regionName, Player player) {
        exceptionCheck();
        regionService.addMember(regionName, player);
    }

    public void addOwner(String regionName, Player player) {
        exceptionCheck();
        regionService.addOwner(regionName, player);
    }

    public void removeMember(String regionName, Player player) {
        exceptionCheck();
        regionService.removeMember(regionName, player);
    }

    public void removeOwner(String regionName, Player player) {
        exceptionCheck();
        regionService.removeOwner(regionName, player);
    }

    public boolean isOwner(String regionName, Player player) {
        exceptionCheck();
        return regionService.isOwner(regionName, player);
    }

    public World getWorld(String regionName) {
        exceptionCheck();
        return regionService.getWorld(regionName);
    }

    public Location getPoint(String regionName, boolean isMinPoint) {
        exceptionCheck();
        return regionService.getPoint(regionName, isMinPoint);
    }

    public boolean containsBlock(String regionName, int x, int y, int z) {
        exceptionCheck();
        return regionService.containsBlock(regionName, x, y, z);
    }

    public boolean containsBlock(String regionName, Location location) {
        return containsBlock(regionName, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public Set<UUID> getMembers(String regionName) {
        exceptionCheck();
        return regionService.getMembers(regionName);
    }

    public Set<UUID> getOwners(String regionName) {
        exceptionCheck();
        return regionService.getOwners(regionName);
    }

    public void setFlag(String regionName, StateFlag flag, StateFlag.State stateFlag) {
        exceptionCheck();
        regionService.setFlag(regionName, flag, stateFlag);
    }

    public int getCount(Player player) {
        exceptionCheck();
        return regionService.getCount(player);
    }

    public boolean hasRegionsInside(Player player, Location location, int cuboidSize) {
        exceptionCheck();
        return regionService.hasRegionsInside(player, location, cuboidSize);
    }

    private static void exceptionCheck() {
        if (regionService == null || (!LunaSpring.getServiceProvider().isRegistered(regionService.getClass())))
            throw new ServiceRegistrationException(RegionManager.class);
    }
}
