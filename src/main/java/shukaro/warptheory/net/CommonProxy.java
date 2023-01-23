package shukaro.warptheory.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import shukaro.warptheory.util.Constants;

import java.util.EnumMap;

public class CommonProxy
{
    public static EnumMap<Side, FMLEmbeddedChannel> warpChannel;

    public void init()
    {
        warpChannel = NetworkRegistry.INSTANCE.newChannel(Constants.modID, new WarpMessageToMessageCodec(), new PacketHandler());
    }

    public void Renders(){

    }

    public EntityPlayer getPlayer()
    {
        return null;
    }
}
