package org.novasparkle.lunaspring.API.Util.Service.realized;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.biome.BiomeType;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.novasparkle.lunaspring.API.Util.Service.LunaService;
import org.novasparkle.lunaspring.LunaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
@Getter
public final class WorldEditService implements LunaService {
    private final LunaPlugin providingPlugin;

    public WorldEditService(LunaPlugin providingPlugin) {
        this.providingPlugin = providingPlugin;
    }

    public EditSession getSession(World world) {
        return WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world));
    }

    public EditSession pasteSchematic(File schemFile, Location location, int offsetX, int offsetY, int offsetZ, boolean ignoreAirBlocks) {
        try {
            ClipboardFormat format = ClipboardFormats.findByFile(schemFile);

            assert format != null;
            ClipboardReader reader = format.getReader(new FileInputStream(schemFile));
            Clipboard clipboard = reader.read();

            EditSession editSession = this.getSession(location.getWorld());
            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                    .to(BlockVector3.at(location.getX() + offsetX, location.getY() + offsetY, location.getZ() + offsetZ))
                    .ignoreAirBlocks(ignoreAirBlocks)
                    .build();

            Operations.complete(operation);
            editSession.close();

            editSession.getBlockBag();
            return editSession;
        } catch (IOException | WorldEditException e) {
            e.fillInStackTrace();
        }
        return null;
    }

    public BiomeType getBiome(int x, int y, int z, World world) {
        EditSession editSession = this.getSession(world);
        BiomeType biomeType = editSession.getBiome(BlockVector3.at(x, y, z));
        editSession.close();
        return biomeType;
    }

    public boolean setBiome(int x, int y, int z, World world, BiomeType biomeType) {
        EditSession editSession = this.getSession(world);
        boolean bool = editSession.setBiome(BlockVector3.at(x, y, z), biomeType);
        editSession.close();
        return bool;
    }

    public void cancel(Operation operation) {
        operation.cancel();
    }

    public void undo(EditSession editSession, EditSession newEditSession) {
        editSession.undo(newEditSession);
        editSession.close();
    }
}
