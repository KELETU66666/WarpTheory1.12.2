package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import shukaro.warptheory.handlers.ITimerWarpEvent;

public class WarpEyeBlink extends ITimerWarpEvent {
    /**
     * This timer does nothing, but as long as it's still ticking, this event won't fire again.
     */
    private static final String COOLDOWN_TIMER = "timer";

    public WarpEyeBlink(int minWarp) {
        super("eyeblink", minWarp, world -> 3 + world.rand.nextInt(9), COOLDOWN_TIMER);
    }

    @Override
    public void sendChatMessage(EntityPlayer player) {
        // No message for this one.
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        if (!player.isPotionActive(MobEffects.BLINDNESS)) {
            player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 20));
            setTimer(player, COOLDOWN_TIMER, world.rand.nextInt(16) + 2);
            return 1;
        }

        return 0;
    }

    @Override
    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e) {
        if (e.world.getTotalWorldTime() % (10) != 0) {
            return;
        }

        super.onTick(e);
    }
}
