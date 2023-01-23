package shukaro.warptheory.handlers.warpevents;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;

public class WarpAcceleration extends IWarpEvent
{
	private final int _mMinWarpLevel;
	
    public WarpAcceleration(int pMinWarpLevel)
    {
    	_mMinWarpLevel = pMinWarpLevel;
    	FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public String getName()
    {
        return "acceleration";
    }

    @Override
    public int getSeverity()
    {
        return _mMinWarpLevel;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + I18n.translateToLocal("chat.warptheory.acceleration"));
        MiscHelper.modEventInt(player, "acceleration", 6000 + world.rand.nextInt(12000));
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getWarpTag(player).hasKey("acceleration"))
            {
                int acceleration = MiscHelper.getWarpTag(player).getInteger("acceleration");
                e.world.setWorldTime(e.world.getWorldTime() + 2);
                MiscHelper.getWarpTag(player).setInteger("acceleration", --acceleration);
                if (acceleration <= 0)
                    MiscHelper.getWarpTag(player).removeTag("acceleration");
            }
        }
    }
}
