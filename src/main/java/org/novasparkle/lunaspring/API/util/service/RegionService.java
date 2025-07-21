package org.novasparkle.lunaspring.API.util.service;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.util.service.managers.worldguard.LFlag;
import org.novasparkle.lunaspring.API.util.service.managers.worldguard.LState;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public final class RegionService implements LunaService {
    public RegionContainer getRegionContainer() {
        return WorldGuard.getInstance().getPlatform().getRegionContainer();
    }

    public RegionManager getRegionManager(World world) {
        return this.getRegionContainer().get(BukkitAdapter.adapt(world));
    }

    public ProtectedCuboidRegion createRegion(String name, Location minLoc, Location maxLoc) {
        World world = minLoc.getWorld();
        if (world != maxLoc.getWorld()) return null;

        BlockVector3 minVector = BlockVector3.at(minLoc.getBlockX(), minLoc.getBlockY(), minLoc.getBlockZ());
        BlockVector3 maxVector = BlockVector3.at(maxLoc.getBlockX(), maxLoc.getBlockY(), maxLoc.getBlockZ());
        ProtectedCuboidRegion region = new ProtectedCuboidRegion(name, minVector, maxVector);

        this.getRegionManager(world).addRegion(region);
        return region;
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
    public void addMember(@NotNull ProtectedRegion region, Player player) {
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
    public void addOwner(@NotNull ProtectedRegion region, Player player) {
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
    public void removeMember(@NotNull ProtectedRegion region, Player player) {
        DefaultDomain domain = region.getMembers();
        domain.removePlayer(player.getName());
        region.setMembers(domain);
    }

    public void removeOwner(@NotNull ProtectedRegion region, Player player) {
        DefaultDomain domain = region.getOwners();
        domain.removePlayer(player.getName());
        region.setOwners(domain);
    }

    @SuppressWarnings("deprecation")
    public boolean isOwner(String regionName, Player player) {
        ProtectedRegion region = this.getRegion(regionName);
        return region != null && region.isOwner(player.getName());
    }

    @SuppressWarnings("deprecation")
    public boolean isMember(String regionName, Player player) {
        ProtectedRegion region = this.getRegion(regionName);
        return region != null && region.isMember(player.getName());
    }

    @SuppressWarnings("deprecation")
    public boolean isOwner(@NotNull ProtectedRegion region, Player player) {
        return region.isOwner(player.getName());
    }

    @SuppressWarnings("deprecation")
    public boolean isMember(@NotNull ProtectedRegion region, Player player) {
        return region.isMember(player.getName());
    }

    public World getWorld(String regionName) {
        for (World world : Bukkit.getWorlds()) {
            RegionManager manager = this.getRegionManager(world);
            if (manager != null && manager.hasRegion(regionName)) return world;
        }
        return null;
    }

    public BlockVector3 getVectorPoint(String regionName, boolean isMinPoint) {
        ProtectedRegion region = this.getRegion(regionName);
        if (region == null) return null;

        return isMinPoint ? region.getMinimumPoint() : region.getMaximumPoint();
    }
    public BlockVector3 getVectorPoint(@NotNull ProtectedRegion region, boolean isMinPoint) {
        return isMinPoint ? region.getMinimumPoint() : region.getMaximumPoint();
    }

    public Location getPoint(String regionName, boolean isMinPoint) {
        BlockVector3 vector3 = this.getVectorPoint(regionName, isMinPoint);
        World world = this.getWorld(regionName);
        return world == null || vector3 == null ? null : new Location(world, vector3.getBlockX(), vector3.getBlockY(), vector3.getBlockZ());
    }
    public Location getPoint(@NotNull ProtectedRegion region, boolean isMinPoint) {
        BlockVector3 vector3 = this.getVectorPoint(region, isMinPoint);
        World world = this.getWorld(region.getId());
        return world == null || vector3 == null ? null : new Location(world, vector3.getBlockX(), vector3.getBlockY(), vector3.getBlockZ());
    }

    public boolean containsBlock(String regionName, int x, int y, int z) {
        ProtectedRegion region = this.getRegion(regionName);
        return region != null && region.contains(x, y, z);
    }
    public boolean containsBlock(@NotNull ProtectedRegion region, int x, int y, int z) {
        return region.contains(x, y, z);
    }

    public Set<UUID> getMembers(String regionName) {
        ProtectedRegion region = this.getRegion(regionName);
        return region == null ? null : region.getMembers().getUniqueIds();
    }

    public Set<UUID> getOwners(String regionName) {
        ProtectedRegion region = this.getRegion(regionName);
        return region == null ? null : region.getOwners().getUniqueIds();
    }

    public Set<UUID> getMembers(@NotNull ProtectedRegion region) {
        return region.getMembers().getUniqueIds();
    }

    public Set<UUID> getOwners(@NotNull ProtectedRegion region) {
        return region.getOwners().getUniqueIds();
    }

    public void setFlag(String regionName, StateFlag flag, StateFlag.State stateFlag) {
        ProtectedRegion region = this.getRegion(regionName);
        if (region != null)
            region.setFlag(flag, stateFlag);
    }
    public void setFlag(@NotNull ProtectedRegion region, StateFlag flag, StateFlag.State stateFlag) {
        region.setFlag(flag, stateFlag);
    }

    public void setFlag(String regionName, LFlag lFlag, StateFlag.State state) {
        this.setFlag(regionName, lFlag.getWGFlag(), state);
    }

    public void setFlag(String regionName, LFlag lFlag, LState lState) {
        this.setFlag(regionName, lFlag.getWGFlag(), lState.getWGState());
    }
    public void setFlag(@NotNull ProtectedRegion region, LFlag lFlag, StateFlag.State state) {
        this.setFlag(region, lFlag.getWGFlag(), state);
    }

    public void setFlag(@NotNull ProtectedRegion region, LFlag lFlag, LState lState) {
        this.setFlag(region, lFlag.getWGFlag(), lState.getWGState());
    }

    public Map<Flag<?>, Object> getFlags(String regionName) {
        ProtectedRegion region = this.getRegion(regionName);
        if (region == null) return null;

        return region.getFlags();
    }
    public Map<Flag<?>, Object> getFlags(@NotNull ProtectedRegion region) {
        return region.getFlags();
    }

    public Map<StateFlag, StateFlag.State> getStateFlags(String regionName) {
        Map<Flag<?>, Object> flags = this.getFlags(regionName);
        if (flags == null) return null;

        return flags.entrySet()
                .stream()
                .filter(en -> en.getKey() instanceof StateFlag && en.getValue() instanceof StateFlag.State)
                .collect(Collectors.toMap(en -> (StateFlag) en.getKey(), en -> (StateFlag.State) en.getValue()));
    }

    public Map<StateFlag, StateFlag.State> getStateFlags(@NotNull ProtectedRegion region) {
        Map<Flag<?>, Object> flags = this.getFlags(region);
        if (flags == null) return null;

        return flags.entrySet()
                .stream()
                .filter(en -> en.getKey() instanceof StateFlag && en.getValue() instanceof StateFlag.State)
                .collect(Collectors.toMap(en -> (StateFlag) en.getKey(), en -> (StateFlag.State) en.getValue()));
    }

    public Map<LFlag, LState> getLStateFlags(String regionName) {
        Map<StateFlag, StateFlag.State> flags = this.getStateFlags(regionName);
        if (flags == null) return null;

        Set<String> lFlags = Stream.of(LFlag.values()).map(Enum::name).collect(Collectors.toSet());
        return flags.entrySet()
                .stream()
                .filter(en -> lFlags.contains(en.getKey().getName()))
                .collect(Collectors.toMap(en -> LFlag.valueOf(en.getKey().getName()), en -> LState.valueOf(en.getValue().name())));
    }

    public Set<LFlag> getLStateFlags(String regionName, LState filteringState) {
        Map<LFlag, LState> flags = this.getLStateFlags(regionName);
        if (flags == null) return null;

        return flags.entrySet()
                .stream()
                .filter(en -> en.getValue() == filteringState)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
    public Map<LFlag, LState> getLStateFlags(@NotNull ProtectedRegion region) {
        Map<StateFlag, StateFlag.State> flags = this.getStateFlags(region);
        if (flags == null) return null;

        Set<String> lFlags = Stream.of(LFlag.values()).map(Enum::name).collect(Collectors.toSet());
        return flags.entrySet()
                .stream()
                .filter(en -> lFlags.contains(en.getKey().getName()))
                .collect(Collectors.toMap(en -> LFlag.valueOf(en.getKey().getName()), en -> LState.valueOf(en.getValue().name())));
    }

    public Set<LFlag> getLStateFlags(@NotNull ProtectedRegion region, LState filteringState) {
        Map<LFlag, LState> flags = this.getLStateFlags(region);
        if (flags == null) return null;

        return flags.entrySet()
                .stream()
                .filter(en -> en.getValue() == filteringState)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public long getCount(Player player) {
        long amo = 0;
        for (RegionManager regionManager : this.getRegionContainer().getLoaded()) {
            amo += regionManager.getRegions().values()
                    .stream()
                    .filter(rg -> this.isOwner(rg.getId(), player))
                    .count();
        }
        return amo;
    }

    public Set<ProtectedRegion> getRegions(Location location) {
        RegionManager manager = this.getRegionManager(location.getWorld());
        BlockVector3 position = BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        return manager.getApplicableRegions(position).getRegions();
    }

    public List<String> getRegionsIDs(Location location) {
        RegionManager manager = this.getRegionManager(location.getWorld());
        BlockVector3 position = BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        return manager.getApplicableRegionsIDs(position);
    }

    public boolean hasRegionsInside(Location location, int cuboidSize) {
        RegionManager manager = this.getRegionManager(location.getWorld());
        BlockVector3 max = BlockVector3.at(
                location.getX() + cuboidSize, location.getY() + cuboidSize, location.getZ() + cuboidSize);
        BlockVector3 min = BlockVector3.at(
                location.getX() - cuboidSize, location.getY() - cuboidSize, location.getZ() - cuboidSize);

        ProtectedRegion rg = new ProtectedCuboidRegion(Utils.getRKey((byte) 24), min, max);
        ApplicableRegionSet set = manager.getApplicableRegions(rg);

        return !set.getRegions().isEmpty();
    }
}
