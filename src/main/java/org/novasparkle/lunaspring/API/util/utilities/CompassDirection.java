package org.novasparkle.lunaspring.API.util.utilities;

import lombok.RequiredArgsConstructor;
import org.bukkit.block.BlockFace;

@RequiredArgsConstructor
public enum CompassDirection {
    NORTH(BlockFace.NORTH, BlockFace.NORTH),
    NORTH_EAST(BlockFace.NORTH_EAST, BlockFace.EAST),
    EAST(BlockFace.EAST, BlockFace.EAST),
    SOUTH_EAST(BlockFace.SOUTH_EAST, BlockFace.EAST),
    SOUTH(BlockFace.SOUTH, BlockFace.SOUTH),
    SOUTH_WEST(BlockFace.SOUTH_WEST, BlockFace.WEST),
    WEST(BlockFace.WEST, BlockFace.WEST),
    NORTH_WEST(BlockFace.NORTH_WEST, BlockFace.WEST);

    public final BlockFace extendedFace;
    public final BlockFace face;
}