package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import shukaro.warptheory.handlers.ITimerWarpEvent;
import shukaro.warptheory.items.WarpItems;
import thaumcraft.common.lib.SoundsTC;

public class WarpLitmusPaper extends ITimerWarpEvent {
    /**
     * This timer does nothing, but as long as it's still ticking, this event won't fire again.
     */
    private static final String COOLDOWN_TIMER = "timer";

    public WarpLitmusPaper(int minWarp) {
        super("litmuspaper", minWarp, world -> 1, COOLDOWN_TIMER);
    }

    @Override
    public boolean canDo(World world, EntityPlayer player) {
        // Don't even queue up this event while it's on cooldown, so that it doesn't reduce the
        // chances of other warp events.
        return getTimer(player, COOLDOWN_TIMER) == 0;
    }

    @Override
    public void sendChatMessage(EntityPlayer player) {
        // We'll play the message when we successfully trigger the event.
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        player.dropItem(WarpItems.itemPaper, 1 + world.rand.nextInt(4));
        world.playSound(null, player.getPosition(), SoundsTC.zap, SoundCategory.PLAYERS, 1.0F, 1.0F);
        super.sendChatMessage(player);

        // Don't allow this event to happen again for eight hours.
        setTimer(player, COOLDOWN_TIMER, 480);

        // Don't allow this event to stack more than once.
        return eventAmount;
    }

    @Override
    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e) {
        if (e.world.getTotalWorldTime() % (60 * 20) != 0) {
            return;
        }

        super.onTick(e);
    }
}
