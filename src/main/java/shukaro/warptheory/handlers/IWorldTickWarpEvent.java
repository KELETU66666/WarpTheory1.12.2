package shukaro.warptheory.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import shukaro.warptheory.util.MiscHelper;

import java.util.List;
import java.util.function.Function;

public abstract class IWorldTickWarpEvent extends IWarpEvent {
    protected final Function<World, Integer> incrementFunction;

    protected IWorldTickWarpEvent(String name, int minWarp, Function<World, Integer> incrementFunction) {
        super(name, minWarp);
        this.incrementFunction = incrementFunction;

        FMLCommonHandler.instance().bus().register(this);
    }

    /**
     * Returns the number of successful triggers of the event.
     */
    public abstract int triggerEvent(int eventAmount, World world, EntityPlayer player);

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        sendChatMessage(player);
        MiscHelper.modEventInt(player, name, incrementFunction.apply(world));
        return true;
    }

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public void onTick(TickEvent.WorldTickEvent e) {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER) return;

        for (EntityPlayer player : (List<EntityPlayer>) e.world.playerEntities) {
            if (MiscHelper.getWarpTag(player).hasKey(name)) {
                int eventAmount = MiscHelper.getWarpTag(player).getInteger(name);
                int decrement = triggerEvent(eventAmount, e.world, player);
                decreaseTag(player, name, decrement);
            }
        }
    }
}
