package shukaro.warptheory.handlers.warpevents;


import net.minecraft.block.Block;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWorldTickWarpEvent;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.MiscHelper;
import shukaro.warptheory.util.RandomBlockHelper;

import java.util.Set;

public class WarpSnow extends IWorldTickWarpEvent {
    public WarpSnow(int minWarp) {
        super("biomeSnow", minWarp, world -> 16 + world.rand.nextInt(16));
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
        if (target.isAir(world) && !below.isAir(world)) {
            world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.SNOW_LAYER.getDefaultState());
            world.playSound(target.x, target.y, target.z, SoundEvents.BLOCK_SNOW_HIT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            return 1;
        }

        if (target.getBlock(world) == Blocks.SNOW_LAYER) {
            int metadata = target.getMeta(world);
            if (metadata < 7) {
                world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.SNOW_LAYER.getStateFromMeta(metadata + 1), 3);
            } else {
                world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.SNOW.getDefaultState());
            }
            world.playSound(target.x, target.y, target.z, SoundEvents.BLOCK_SNOW_HIT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            return 1;
        }

        BlockCoord above = target.copy().offset(1);
        if (target.getBlock(world) == Blocks.WATER && above.isAir(world)) {
            world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.ICE.getDefaultState());
            world.playSound(target.x, target.y, target.z, SoundEvents.BLOCK_SNOW_HIT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            return 1;
        }

        Block belowBlock = below.getBlock(world);
        if ((belowBlock == Blocks.ICE || belowBlock == Blocks.SNOW || belowBlock == Blocks.SNOW_LAYER)
                && target.isAir(world)
                && above.isAir(world)) {
            EntitySnowman snowGolem = new EntitySnowman(world);
            snowGolem.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 3600 * 20));
            RandomBlockHelper.setLocation(world, snowGolem, target);

            if (world.spawnEntity(snowGolem)) {
                world.playSound(target.x, target.y, target.z, SoundEvents.BLOCK_SNOW_HIT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                return 1;
            }
        }

        return 0;
    }
}
