package shukaro.warptheory.handlers.warpevents;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import shukaro.warptheory.handlers.IWorldTickWarpEvent;
import shukaro.warptheory.net.PacketDispatcher;

public class WarpWind extends IWorldTickWarpEvent {
    public WarpWind(int minWarp) {
        super("wind", minWarp, world -> 5 + world.rand.nextInt(10));
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        PacketDispatcher.sendWindEvent(
                player,
                world.rand.nextDouble() - world.rand.nextDouble(),
                world.rand.nextDouble(),
                world.rand.nextDouble() - world.rand.nextDouble());
        return 1;
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
