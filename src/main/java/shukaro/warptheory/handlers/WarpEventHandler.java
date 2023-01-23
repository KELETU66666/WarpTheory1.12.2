package shukaro.warptheory.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WarpEventHandler
{
    @SubscribeEvent
    public void livingUpdate(LivingEvent.LivingUpdateEvent e)
    {
        if (e.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) e.getEntity();
            if (player.ticksExisted % 2000 == 0 && !WarpHandler.wuss && !player.isPotionActive(Potion.getPotionById(WarpHandler.potionWarpWardID)) && WarpHandler.getTotalWarp(player) > 0 &&
                    !player.capabilities.isCreativeMode && !player.world.isRemote && player.world.rand.nextInt(100) <= Math.sqrt(WarpHandler.getTotalWarp(player)))
            {
                IWarpEvent event = WarpHandler.queueOneEvent(player, WarpHandler.getTotalWarp(player));
                if (event != null)
                {
                    int warpTemp = WarpHandler.getIndividualWarps(player)[2];
                    if (warpTemp > 0 && event.getCost() <= warpTemp)
                        WarpHandler.removeWarp(player, event.getCost());
                    else if (warpTemp > 0)
                        WarpHandler.removeWarp(player, warpTemp);
                }
            }
            if (player.ticksExisted % 20 == 0 && player.world.rand.nextBoolean())
            {
                IWarpEvent event = WarpHandler.dequeueEvent(player);
                if (event != null && event.canDo(player))
                    event.doEvent(player.world, player);
            }
        }
    }
}
