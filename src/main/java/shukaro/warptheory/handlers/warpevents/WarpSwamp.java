package shukaro.warptheory.handlers.warpevents;


import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import shukaro.warptheory.handlers.IWorldTickWarpEvent;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.MiscHelper;
import shukaro.warptheory.util.NameMetaPair;
import shukaro.warptheory.util.RandomBlockHelper;

import java.util.Set;

public class WarpSwamp extends IWorldTickWarpEvent {
    public WarpSwamp(int minWarp) {
        super("biomeSwamp", minWarp, world -> 256 + world.rand.nextInt(256));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean canDo(World world, EntityPlayer player) {
        for (String n : (Set<String>) MiscHelper.getWarpTag(player).getKeySet()) {
            if (n.startsWith("biome") && !n.equals(getName())) return false;
        }
        return true;
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        BlockCoord target =
                RandomBlockHelper.randomBlock(world, player, 8, block -> MiscHelper.hasNonSolidNeighbor(world, block));
        if (target == null) {
            return 0;
        }

        boolean grown = false;
        if (target.getBlock(world) == Blocks.WATER) {
            if (target.offset(1).isAir(world) && world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.WATERLILY.getDefaultState(), 3))
                grown = true;
        } else if (target.getBlock(world) == Blocks.SAPLING) {
            // Function that grows SAPLING into a tree
            ((BlockSapling) target.getBlock(world)).grow(world, new BlockPos(target.x, target.y, target.z), target.getBlock(world).getDefaultState(), world.rand);
            grown = true;
        } else if (target.getBlock(world).getMaterial(target.getBlock(world).getDefaultState()) == Material.LEAVES
                || target.getBlock(world) == Blocks.LOG
                || target.getBlock(world) == Blocks.LOG2) {
            for (int j = 0; j < 6; j++) {
                int side = 2 + world.rand.nextInt(4);
                if (Blocks.VINE.canPlaceBlockOnSide(world, new BlockPos(target.x, target.y, target.z), EnumFacing.getFront(side))
                        && target.offset(side).isAir(world)) {
                    world.setBlockState(new BlockPos(
                            target.x,
                            target.y,
                            target.z),
                            Blocks.VINE.getStateFromMeta(EnumFacing.HORIZONTALS.length),
                            3);
                    grown = true;
                    break;
                }
            }
        } else {
            if (world.rand.nextBoolean()
                    && target.getBlock(world)
                    .canSustainPlant(target.getBlock(world).getDefaultState(), world, new BlockPos(target.x, target.y, target.z), EnumFacing.UP, (IPlantable)
                            Blocks.SAPLING)) {
                if (world.rand.nextBoolean()) {
                    if (target.offset(1).isAir(world)
                            || (target.getBlock(world) instanceof IPlantable
                            && target.getBlock(world) != Blocks.SAPLING))
                        world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.SAPLING.getStateFromMeta(world.rand.nextInt(6)), 3);
                } else if (world.rand.nextBoolean()) {
                    if (target.offset(1).isAir(world) && target.offset(0).getBlock(world) instanceof IGrowable)
                        ((IGrowable) target.getBlock(world))
                                .grow(world, world.rand, new BlockPos(target.x, target.y, target.z), target.getBlock(world).getDefaultState()); // Bonemealing
                } else {
                    if (target.offset(1).isAir(world) && target.offset(0).getBlock(world) == Blocks.GRASS)
                        world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.DIRT.getStateFromMeta(2), 3);
                }
                grown = true;
            } else if (world.rand.nextBoolean() && MiscHelper.canTurnToSwampWater(world, target)) {
                if (target.copy().offset(1).getBlock(world) == Blocks.LOG
                        || target.copy().offset(1).getBlock(world) == Blocks.LOG2)
                    world.setBlockState(new BlockPos(
                            target.x,
                            target.y,
                            target.z),
                            target.copy().offset(1).getBlock(world).getStateFromMeta(target.copy().offset(1).getMeta(world)),
                            3);
                else world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.WATER.getStateFromMeta(0), 3);
                grown = true;
            } else if (WarpHandler.decayMappings.containsKey(
                    new NameMetaPair(target.getBlock(world), target.getMeta(world)))
                    && target.getBlock(world).isOpaqueCube(target.getBlock(world).getDefaultState())
                    && target.getBlock(world) != Blocks.LOG
                    && target.getBlock(world) != Blocks.LOG2) {
                if (target.getBlock(world) != Blocks.DIRT && target.getBlock(world) != Blocks.GRASS) {
                    if (target.copy().offset(1).getBlock(world).isOpaqueCube(target.copy().offset(1).getBlock(world).getDefaultState()))
                        world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.DIRT.getStateFromMeta(0), 3);
                    else if (world.rand.nextBoolean()) world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.GRASS.getDefaultState(), 3);
                    else world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.DIRT.getStateFromMeta(2), 3);
                    grown = true;
                }
            }
        }

        if (grown) {
            return 1;
        } else {
            return 0;
        }
    }
}
