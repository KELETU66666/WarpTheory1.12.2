package shukaro.warptheory.handlers.warpevents;



import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.net.PacketDispatcher;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;

public class WarpBlink extends IWarpEvent
{
	private final int _mMinWarpLevel;
    public WarpBlink(int pMinWarpLevel)
    {
    	_mMinWarpLevel = pMinWarpLevel;
    	FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public String getName()
    {
        return "blink";
    }

    @Override
    public int getSeverity()
    {
    	return _mMinWarpLevel;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + I18n.translateToLocal("chat.warptheory.blink"));
        MiscHelper.modEventInt(player, "blink", 10 + world.rand.nextInt(20));
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getWarpTag(player).hasKey("blink") && e.world.getTotalWorldTime() % 20 == 0)
            {
                int blink = MiscHelper.getWarpTag(player).getInteger("blink");
                for (int i = 0; i < 8; i++)
                {
                    int targetX = (int)player.posX + e.world.rand.nextInt(16) - e.world.rand.nextInt(16);
                    int targetY = (int)player.posY + e.world.rand.nextInt(16) - e.world.rand.nextInt(16);
                    int targetZ = (int)player.posZ + e.world.rand.nextInt(16) - e.world.rand.nextInt(16);
                    BlockCoord target = new BlockCoord(targetX, targetY, targetZ);
                    if (target.isAir(e.world) && target.copy().offset(1).isAir(e.world) && !target.copy().offset(0).isAir(e.world))
                    {
                        player.rotationPitch = (e.world.rand.nextInt(90) + e.world.rand.nextFloat()) - (e.world.rand.nextInt(90) + e.world.rand.nextFloat());
                        player.rotationYaw = (e.world.rand.nextInt(360) + e.world.rand.nextFloat()) - (e.world.rand.nextInt(360) + e.world.rand.nextFloat());
                        double dX = target.x + e.world.rand.nextDouble();
                        double dY = target.y + e.world.rand.nextDouble();
                        double dZ = target.z + e.world.rand.nextDouble();
                        player.setPositionAndUpdate(dX, dY, dZ);
                        PacketDispatcher.sendBlinkEvent(e.world, dX, dY, dZ);
                        e.world.playSound(dX, dY, dZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F, false);
                        MiscHelper.getWarpTag(player).setInteger("blink", --blink);
                        if (blink <= 0)
                            MiscHelper.getWarpTag(player).removeTag("blink");
                        break;
                    }
                }
            }
        }
    }
}
