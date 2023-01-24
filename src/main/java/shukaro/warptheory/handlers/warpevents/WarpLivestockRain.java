package shukaro.warptheory.handlers.warpevents;


import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IMultiWarpEvent;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.RandomBlockHelper;

public class WarpLivestockRain extends IMultiWarpEvent {
    public WarpLivestockRain(int minWarp) {
        super("livestock", minWarp, 3, world -> 5 + world.rand.nextInt(10));
    }

    @Override
    public int triggerEvent(int eventLevel, int eventAmount, World world, EntityPlayer player) {
        int successful = 0;
        for (int i = 0; i < 6; i++) {
            BlockCoord target = RandomBlockHelper.randomBlock(world, player, 8, block -> isValid(world, block));
            if (target == null) {
                continue;
            }
            target.y += 25;

            EntityLiving victim;
            switch (eventLevel) {
                case 2:
                    switch (world.rand.nextInt(3)) {
                        case 0:
                            victim = new EntityIllusionIllager(world);
                            // Tainted cows have too much health to die, so reduce their HP.
                            victim.setHealth(20.0f);
                            break;
                        case 1:
                            victim = new EntityVindicator(world);
                            break;
                        case 2:
                            // There is a chance that some of these entities may survive, due to
                            // landing on an adjacent block.
                            //
                            // Tainted sheep are capable of spreading taint, so instead, we use a
                            // custom version of tainted sheep that cannot spread taint.
                            victim = new EntityEvoker(world);
                            break;
                        default:
                            victim = new EntityWitch(world);
                            break;
                    }
                    break;

                case 1:
                    victim = new EntitySquid(world);
                    break;

                case 0:
                default:
                    switch (world.rand.nextInt(3)) {
                        case 0:
                            victim = new EntityCow(world);
                            break;
                        case 1:
                            victim = new EntityPig(world);
                            break;
                        case 2:
                            victim = new EntitySheep(world);
                            break;
                        default:
                            victim = new EntityChicken(world);
                            break;
                    }
                    break;
            }

            victim.playLivingSound();
            RandomBlockHelper.setLocation(world, victim, target);
            if (world.spawnEntity(victim)) {
                successful++;
                if (successful >= eventAmount) {
                    break;
                }
            }
        }

        return successful;
    }

    private static boolean isValid(World world, BlockCoord block) {
        for (int i = 0; i <= 25; i++) {
            if (!world.isAirBlock(new BlockPos(block.x, block.y + i, block.z))) {
                return false;
            }
        }

        return true;
    }
}