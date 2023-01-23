package shukaro.warptheory.handlers.warpevents;



import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.net.PacketDispatcher;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;

public class WarpWind extends IWarpEvent
{
	private final int _mMinWarpLevel;
    public WarpWind(int pMinWarpLevel)
    {
    	_mMinWarpLevel = pMinWarpLevel;
    	FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public String getName()
    {
        return "wind";
    }

    @Override
    public int getSeverity()
    {
    	return _mMinWarpLevel;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + I18n.translateToLocal("chat.warptheory.wind"));
        MiscHelper.modEventInt(player, "wind", 5 + world.rand.nextInt(10));
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getWarpTag(player).hasKey("wind") && e.world.rand.nextBoolean() && e.world.getTotalWorldTime() % 20 == 0)
            {
                int wind = MiscHelper.getWarpTag(player).getInteger("wind");
                PacketDispatcher.sendWindEvent(player, e.world.rand.nextDouble() - e.world.rand.nextDouble(), e.world.rand.nextDouble(), e.world.rand.nextDouble() - e.world.rand.nextDouble());
                MiscHelper.getWarpTag(player).setInteger("wind", --wind);
                if (wind <= 0)
                    MiscHelper.getWarpTag(player).removeTag("wind");
            }
        }
    }
}
