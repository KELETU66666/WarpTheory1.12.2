package shukaro.warptheory.handlers.warpevents;


import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWorldTickWarpEvent;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.MiscHelper;
import shukaro.warptheory.util.RandomBlockHelper;
import thaumcraft.api.blocks.BlocksTC;

import java.util.Set;

public class WarpMushrooms extends IWorldTickWarpEvent {
    public WarpMushrooms(int minWarp) {
        super("biomeMushrooms", minWarp, world -> 16 + world.rand.nextInt(16));
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
    @SuppressWarnings("unchecked")
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        BlockCoord target =
                RandomBlockHelper.randomBlock(world, player, 16, block -> MiscHelper.hasNonSolidNeighbor(world, block));
        if (target == null) {
            return 0;
        }

        BlockCoord below = target.copy().offset(0);
        if (target.isAir(world) && below.getBlock(world).isTopSolid(below.getBlock(world).getDefaultState())) {
            int mushroomRandom = world.rand.nextInt(99);
            if (mushroomRandom == 0) {
                world.setBlockState(new BlockPos(target.x, target.y, target.z), BlocksTC.vishroom.getDefaultState(), 3);
            } else if (mushroomRandom % 2 == 0) {
                world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.BROWN_MUSHROOM.getDefaultState());
            } else {
                world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.RED_MUSHROOM.getDefaultState());
            }
            world.playSound(target.x, target.y, target.z, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            return 1;
        }

        BlockCoord above = target.copy().offset(1);
        if ((target.getBlock(world) == Blocks.GRASS || target.getBlock(world) == Blocks.DIRT) && above.isAir(world)) {
            world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.MYCELIUM.getDefaultState());
            world.playSound(target.x, target.y, target.z, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            return 1;
        }

        AxisAlignedBB boundingBox = new AxisAlignedBB(
                (double) target.x - 1,
                (double) target.y - 1,
                (double) target.z - 1,
                (double) target.x + 1,
                (double) target.y + 1,
                (double) target.z + 1);
        for (Entity entity :  world.getEntitiesWithinAABB(EntityCow.class, boundingBox)) {
            // Check for exact class match, because we don't want to transform subclasses.
            if (entity.getClass() == EntityCow.class) {
                EntityMooshroom mooshroom = new EntityMooshroom(world);
                mooshroom.copyLocationAndAnglesFrom(entity);
                mooshroom.setGrowingAge(((EntityCow) entity).getGrowingAge());

                if (world.spawnEntity(mooshroom)) {
                    world.playSound(target.x, target.y, target.z, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                    mooshroom.playLivingSound();
                    entity.setDead();
                    return 1;
                }
            }
        }

        return 0;
    }
}