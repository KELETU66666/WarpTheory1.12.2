package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IMultiWarpEvent;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.RandomBlockHelper;
import thaumcraft.common.lib.events.WarpEvents;

public class WarpEndermen extends IMultiWarpEvent {
    public WarpEndermen(int minWarp) {
        super("endermen", minWarp, 2, world -> 2 + world.rand.nextInt(4));
    }

    @Override
    public boolean canDo(World world, EntityPlayer player) {
        // Only queue up this event at night.
        return !world.isDaytime();
    }

    @Override
    public int triggerEvent(int eventLevel, int eventAmount, World world, EntityPlayer player) {
        int successful = 0;
        for (int i = 0; i < 6; i++) {
            BlockCoord target = RandomBlockHelper.randomTripleAirBlock(world, player, 24);
            if (target == null) {
                continue;
            }

            EntityEnderman enderman = new EntityEnderman(world);
            enderman.playLivingSound();
            RandomBlockHelper.setLocation(world, enderman, target);
            enderman.setAttackTarget(player);

            switch (eventLevel) {
                case 1:
                    enderman.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 60 * 20));
                    enderman.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 60 * 20, 1));
                    break;
                case 0:
                    // Do nothing.
                    break;
            }

            if (world.spawnEntity(enderman)) {
                successful++;
                if (successful >= eventAmount) {
                    break;
                }
            }
        }

        return successful;
    }
}
