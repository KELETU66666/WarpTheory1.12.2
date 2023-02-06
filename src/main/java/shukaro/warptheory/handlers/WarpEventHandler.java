package shukaro.warptheory.handlers;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import shukaro.warptheory.entity.IHealable;
import shukaro.warptheory.entity.IHurtable;
import thaumcraft.common.lib.potions.PotionWarpWard;

import static shukaro.warptheory.handlers.WarpHandler.wuss;

public class WarpEventHandler {
    @SubscribeEvent
    public void livingUpdate(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.getEntity();
            boolean appliable =
                    (!player.isPotionActive(PotionWarpWard.instance) && WarpHandler.getUnavoidableCount(player) > 0)
                            && !wuss
                            && !player.capabilities.isCreativeMode;
            boolean tickflag = ConfigHandler.allowWarpEffects
                    && !player.world.isRemote
                    && player.ticksExisted > 0
                    && player.ticksExisted % 2000 == 0;
            if (tickflag
                    && appliable
                    && WarpHandler.getTotalWarp(player) > 0
                    && player.world.rand.nextInt(100) <= Math.sqrt(WarpHandler.getTotalWarp(player))) {
                IWarpEvent event = WarpHandler.queueOneEvent(player, WarpHandler.getTotalWarp(player));
                if (event != null) {
                    int warpTemp = WarpHandler.getIndividualWarps(player)[2];
                    if (warpTemp > 0 && event.getCost() <= warpTemp) WarpHandler.removeWarp(player, event.getCost());
                    else if (warpTemp > 0) WarpHandler.removeWarp(player, warpTemp);
                }
            }
            if (player.ticksExisted % 20 == 0 && player.world.rand.nextBoolean()) {
                IWarpEvent event = WarpHandler.dequeueEvent(player);
                WarpHandler.addUnavoidableCount(player, -1);
                if (event != null && event.canDo(player.world, player) && appliable)
                    event.doEvent(player.world, player);
            }
        }
    }

    @SubscribeEvent
    public void livingHeal(LivingHealEvent e) {
        if (e.getEntityLiving() instanceof IHealable) {
            ((IHealable) e.getEntityLiving()).onHeal(e);
        }
    }

    @SubscribeEvent
    public void livingHurt(LivingHurtEvent e) {
        if (e.getEntityLiving() instanceof IHurtable) {
            ((IHurtable) e.getEntityLiving()).onHurt(e);
        }
    }
}
