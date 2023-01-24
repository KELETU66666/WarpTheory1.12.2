package shukaro.warptheory.handlers.warpevents;


import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import shukaro.warptheory.handlers.IWorldTickWarpEvent;
import shukaro.warptheory.net.PacketDispatcher;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.RandomBlockHelper;

public class WarpBlink extends IWorldTickWarpEvent {
    public WarpBlink(int minWarp) {
        super("blink", minWarp, world -> 10 + world.rand.nextInt(20));
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        for (int i = 0; i < 8; i++) {
            BlockCoord target = RandomBlockHelper.randomBlock(world, player, 16, block -> isBlockValid(world, block));
            if (target == null) {
                continue;
            }

            player.rotationPitch = (world.rand.nextInt(45) + world.rand.nextFloat())
                    - (world.rand.nextInt(45) + world.rand.nextFloat());
            player.rotationYaw = (world.rand.nextInt(360) + world.rand.nextFloat())
                    - (world.rand.nextInt(360) + world.rand.nextFloat());
            double dX = target.x + 0.5;
            double dY = target.y + 0.01;
            double dZ = target.z + 0.5;
            player.setPositionAndUpdate(dX, dY, dZ);
            PacketDispatcher.sendBlinkEvent(world, dX, dY, dZ);
            world.playSound(dX, dY, dZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F, false);

            return 1 + eventAmount / 20;
        }

        return 0;
    }

    private static boolean isBlockValid(World world, BlockCoord block) {
        return block.isAir(world) && block.copy().offset(1).isAir(world) && !block.copy().offset(0).isAir(world);
    }

    @Override
    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e) {
        if (e.world.getTotalWorldTime() % 20 != 0) {
            return;
        }

        super.onTick(e);
    }
}