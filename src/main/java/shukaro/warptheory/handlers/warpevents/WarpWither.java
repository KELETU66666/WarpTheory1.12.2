package shukaro.warptheory.handlers.warpevents;


import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWorldTickWarpEvent;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.RandomBlockHelper;

public class WarpWither extends IWorldTickWarpEvent {
    public WarpWither(int minWarp) {
        super("wither", minWarp, world -> 1);
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        BlockCoord target = RandomBlockHelper.randomBlock(world, player, 4, block -> isValid(world, block));
        if (target == null) {
            return 0;
        }

        world.addWeatherEffect(new EntityLightningBolt(world, target.x, target.y, target.z, false));
        world.playSound(target.x, target.y, target.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F, false);
        world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, target.x, target.y, target.z, 1.0D, 0.0D, 0.0D);
        EntityWither wither = new EntityWither(world);
        wither.setLocationAndAngles(
                target.x + 0.5D, target.y - 0.5D, target.z + 0.5D, world.rand.nextFloat(), world.rand.nextFloat());
        wither.ignite();

        if (world.spawnEntity(wither)) {
            return 1;
        } else {
            return 0;
        }
    }

    private static boolean isValid(World world, BlockCoord block) {
        if (!world.getBlockState(new BlockPos(block.x, block.y - 1, block.z)).getMaterial().blocksMovement()) return false;

        for (int xb = block.x - 1; xb < block.x + 1; xb++) {
            for (int yb = block.y; yb < block.y + 2; yb++) {
                for (int zb = block.z - 1; zb < block.z + 1; zb++) {
                    if (world.getBlockState(new BlockPos(xb, yb, zb)).getMaterial().blocksMovement()) return false;
                }
            }
        }

        return true;
    }
}