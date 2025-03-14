package org.novasparkle.lunaspring.Util.managers;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.world.biome.BiomeType;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.World;
import org.novasparkle.lunaspring.LunaSpring;
import org.novasparkle.lunaspring.Util.Service.ServiceRegistrationException;
import org.novasparkle.lunaspring.Util.Service.realized.WorldEditService;

import java.io.File;

@UtilityClass
public class WorldEditManager {
    private WorldEditService service;
    public void init(WorldEditService worldEditService) {service = worldEditService;}

    public EditSession getSession(World world) {
        exceptionCheck();
        return WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world));
    }

    public EditSession pasteSchematic(File schemFile, Location location, int offsetX, int offsetY, int offsetZ, boolean ignoreAirBlocks) {
        exceptionCheck();
        return service.pasteSchematic(schemFile, location, offsetX, offsetY, offsetZ, ignoreAirBlocks);
    }

    public EditSession pasteSchematic(File schemFile, Location location, boolean ignoreAirBlocks) {
        return pasteSchematic(schemFile, location, 0, 0, 0, ignoreAirBlocks);
    }

    public EditSession pasteSchematic(File schemFile, Location location, int x, int y, int z) {
        return pasteSchematic(schemFile, location, x, y, z, false);
    }

    public BiomeType getBiome(int x, int y, int z, World world) {
        exceptionCheck();
        return service.getBiome(x, y, z, world);
    }

    public boolean setBiome(int x, int y, int z, World world, BiomeType biomeType) {
        exceptionCheck();
        return service.setBiome(x, y, z, world, biomeType);
    }

    public BiomeType getBiome(Location location) {
        return getBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld());
    }

    public boolean setBiome(Location location, BiomeType biomeType) {
        return setBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld(), biomeType);
    }

    public void cancel(Operation operation) {
        exceptionCheck();
        service.cancel(operation);
    }

    public void undo(EditSession editSession, EditSession newEditSession) {
        exceptionCheck();
        service.undo(editSession, newEditSession);
    }

    public void undo(EditSession editSession, World world) {
        undo(editSession, getSession(world));
    }

    private static void exceptionCheck() {
        if (service == null || (!LunaSpring.getServiceProvider().isRegistered(service.getClass())))
            throw new ServiceRegistrationException(WorldEditService.class);
    }
}
