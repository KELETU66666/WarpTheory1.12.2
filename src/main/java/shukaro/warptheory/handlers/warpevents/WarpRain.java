package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.ConfigHandler;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;

public class WarpRain extends IWarpEvent
{
	private final int _mMinWarpLevel;
	public WarpRain(int pMinWarpLevel)
	{ 
		_mMinWarpLevel = pMinWarpLevel;
	}
	
    @Override
    public String getName()
    {
        return "rain";
    }

    @Override
    public int getSeverity()
    {
    	return _mMinWarpLevel;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
    	if(ConfigHandler.allowGlobalWarpEffects == false)
    		return false;
    	
        if (!world.getWorldInfo().isThundering())
        {
            ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + I18n.translateToLocal("chat.warptheory.rain"));
            world.getWorldInfo().setRaining(true);
            world.getWorldInfo().setThundering(true);
            return true;
        }
        return false;
    }
}
