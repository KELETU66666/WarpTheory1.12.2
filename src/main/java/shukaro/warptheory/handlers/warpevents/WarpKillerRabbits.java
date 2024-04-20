package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IMultiWarpEvent;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.RandomBlockHelper;

public class WarpKillerRabbits extends IMultiWarpEvent {
    public WarpKillerRabbits(int minWarp) {
        super("killerRabbits", minWarp, 2, world -> 4 + world.rand.nextInt(4));
    }

    @Override
    public int triggerEvent(int eventLevel, int eventAmount, World world, EntityPlayer player) {
        int successful = 0;
        for (int i = 0; i < 6; i++) {
            BlockCoord target = RandomBlockHelper.randomAirBlock(world, player, 8);
            if (target == null) {
                continue;
            }

            EntityRabbit killer = new EntityRabbit(world);
            killer.playLivingSound();
            killer.setRabbitType(99);
            killer.setCustomNameTag(eventLevel == 1 ? "keletu" : "toast");
            RandomBlockHelper.setLocation(world, killer, target);

            if (eventLevel == 1) {
                killer.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 60 * 20, 1));
                // Fall-through
            }

            if (world.spawnEntity(killer)) {
                successful++;
                if (successful >= eventAmount) {
                    break;
                }
            }
        }

        return successful;
    }
}