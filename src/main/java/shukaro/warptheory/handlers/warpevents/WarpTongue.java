package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

public class WarpTongue extends IWarpEvent
{
	private final int _mMinWarpLevel;
    public WarpTongue(int pMinWarpLevel)
    {
    	_mMinWarpLevel = pMinWarpLevel;
    	MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public String getName()
    {
        return "tongue";
    }

    @Override
    public int getSeverity()
    {
    	return _mMinWarpLevel;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + I18n.translateToLocal("chat.warptheory.tongue"));
        MiscHelper.modEventInt(player, "tongues", 10 + world.rand.nextInt(15));
        return true;
    }

    @SubscribeEvent
    public void onMessageReceived(ServerChatEvent e)
    {
        // Warp tongue
        if (MiscHelper.getWarpTag(e.getPlayer()).hasKey("tongues"))
        {
            int tongues = MiscHelper.getWarpTag(e.getPlayer()).getInteger("tongues");
            e.setComponent(new TextComponentString("<" + ChatHelper.getUsername(e.getComponent()) + "> " + ChatHelper.garbleMessage(e.getComponent())));
            MiscHelper.getWarpTag(e.getPlayer()).setInteger("tongues", --tongues);
            if (tongues <= 0)
                MiscHelper.getWarpTag(e.getPlayer()).removeTag("tongues");
        }
    }
}
