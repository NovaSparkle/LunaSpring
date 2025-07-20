package org.novasparkle.lunaspring.API.util.service.managers.worldguard;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.util.service.RegionService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class RgManager {
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

    public ProtectedCuboidRegion createRegion(String name, Location minLoc, Location maxLoc) {
        return regionService.createRegion(name, minLoc, maxLoc);
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

    public BlockVector3 getVectorPoint(String regionName, boolean isMinPoint) {
        return regionService.getVectorPoint(regionName, isMinPoint);
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

    public Map<Flag<?>, Object> getFlags(String regionName) {
        return regionService.getFlags(regionName);
    }

    public Map<StateFlag, StateFlag.State> getStateFlags(String regionName) {
        return regionService.getStateFlags(regionName);
    }

    public Map<LFlag, LState> getLStateFlags(String regionName) {
        return regionService.getLStateFlags(regionName);
    }

    public Set<LFlag> getLStateFlags(String regionName, LState filteringState) {
        return regionService.getLStateFlags(regionName, filteringState);
    }

    public void setFlag(String regionName, StateFlag flag, StateFlag.State state) {
        regionService.setFlag(regionName, flag, state);
    }

    public void setFlag(String regionName, LFlag lFlag, StateFlag.State state) {
        regionService.setFlag(regionName, lFlag, state);
    }

    public void setFlag(String regionName, LFlag lFlag, LState lState) {
        regionService.setFlag(regionName, lFlag, lState);
    }




    public void addMember(@NotNull ProtectedRegion region, Player player) {
        regionService.addMember(region, player);
    }

    public void addOwner(@NotNull ProtectedRegion region, Player player) {
        regionService.addOwner(region, player);
    }

    public void removeMember(@NotNull ProtectedRegion region, Player player) {
        regionService.removeMember(region, player);
    }

    public void removeOwner(@NotNull ProtectedRegion region, Player player) {
        regionService.removeOwner(region, player);
    }

    public boolean isOwner(@NotNull ProtectedRegion region, Player player) {
        return regionService.isOwner(region, player);
    }

    public boolean isMember(@NotNull ProtectedRegion region, Player player) {
        return regionService.isMember(region, player);
    }

    public BlockVector3 getVectorPoint(@NotNull ProtectedRegion region, boolean isMinPoint) {
        return regionService.getVectorPoint(region, isMinPoint);
    }

    public Location getPoint(@NotNull ProtectedRegion region, boolean isMinPoint) {
        return regionService.getPoint(region, isMinPoint);
    }

    public boolean containsBlock(@NotNull ProtectedRegion region, int x, int y, int z) {
        return regionService.containsBlock(region, x, y, z);
    }

    public boolean containsBlock(@NotNull ProtectedRegion region, Location location) {
        return containsBlock(region, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public Set<UUID> getMembers(@NotNull ProtectedRegion region) {
        return regionService.getMembers(region);
    }

    public Set<UUID> getOwners(@NotNull ProtectedRegion region) {
        return regionService.getOwners(region);
    }

    public Map<Flag<?>, Object> getFlags(@NotNull ProtectedRegion region) {
        return regionService.getFlags(region);
    }

    public Map<StateFlag, StateFlag.State> getStateFlags(@NotNull ProtectedRegion region) {
        return regionService.getStateFlags(region);
    }

    public Map<LFlag, LState> getLStateFlags(@NotNull ProtectedRegion region) {
        return regionService.getLStateFlags(region);
    }

    public Set<LFlag> getLStateFlags(@NotNull ProtectedRegion region, LState filteringState) {
        return regionService.getLStateFlags(region, filteringState);
    }

    public void setFlag(@NotNull ProtectedRegion region, StateFlag flag, StateFlag.State state) {
        regionService.setFlag(region, flag, state);
    }

    public void setFlag(@NotNull ProtectedRegion region, LFlag lFlag, StateFlag.State state) {
        regionService.setFlag(region, lFlag, state);
    }

    public void setFlag(@NotNull ProtectedRegion region, LFlag lFlag, LState lState) {
        regionService.setFlag(region, lFlag, lState);
    }

    public long getCount(Player player) {
        return regionService.getCount(player);
    }

    public boolean hasRegionsInside(Location location, int cuboidSize) {
        return regionService.hasRegionsInside(location, cuboidSize);
    }

    public Set<ProtectedRegion> getRegions(Location location) {
        return regionService.getRegions(location);
    }
    public List<String> getRegionsIds(Location location) {
        return regionService.getRegionsIDs(location);
    }
}