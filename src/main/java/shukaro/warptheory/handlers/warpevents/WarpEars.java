package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.net.PacketDispatcher;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

public class WarpEars extends IWarpEvent
{
	private final int _mMinWarpLevel;
    public WarpEars(int pMinWarpLevel)
    {
    	_mMinWarpLevel = pMinWarpLevel;
    	MinecraftForge.EVENT_BUS.register(this); 
    }

    @Override
    public String getName()
    {
        return "ears";
    }

    @Override
    public int getSeverity()
    {
    	return _mMinWarpLevel;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + I18n.translateToLocal("chat.warptheory.ears"));
        int amount = 10 + world.rand.nextInt(30);
        if (!world.isRemote)
        {
            MiscHelper.modEventInt(player, "ears", amount);
            PacketDispatcher.sendEarStartEvent(player, amount);
        }
        return true;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onMessageReceived(ClientChatReceivedEvent e)
    {
        EntityPlayer player = WarpTheory.proxy.getPlayer();
        if (player == null || ChatHelper.getUsername(e.getMessage()).length() == 0 || player.getCommandSenderEntity().equals(ChatHelper.getUsername(e.getMessage())))
            return;

        // Warp ears
        if (MiscHelper.getWarpTag(player).hasKey("ears"))
        {
            e.setMessage(new TextComponentString(ChatHelper.getFormattedUsername(e.getMessage()) + " " + ChatHelper.garbleMessage(e.getMessage())));
            PacketDispatcher.sendEarDecrementEvent(player);
            int ears = MiscHelper.getWarpTag(player).getInteger("ears");
            MiscHelper.getWarpTag(player).setInteger("ears", --ears);
            if (ears <= 0)
                MiscHelper.getWarpTag(player).removeTag("ears");
        }
    }
}
