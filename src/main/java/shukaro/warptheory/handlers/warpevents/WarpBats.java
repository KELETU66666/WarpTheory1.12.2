package shukaro.warptheory.handlers.warpevents;


import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
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

public class WarpBats extends IWarpEvent
{
	private final int _mMinWarpLevel;
    public WarpBats(int pMinWarpLevel)
    {
    	_mMinWarpLevel = pMinWarpLevel;
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public String getName()
    {
        return "bats";
    }

    @Override
    public int getSeverity()
    {
    	return _mMinWarpLevel;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + I18n.translateToLocal("chat.warptheory.bats"));
        MiscHelper.modEventInt(player, "bats", 15 + world.rand.nextInt(30));
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        // Spawning bats
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getWarpTag(player).hasKey("bats"))
            {
                int bats = MiscHelper.getWarpTag(player).getInteger("bats");
                for (int i = 0; i < 6; i++)
                {
                    int targetX = (int)player.posX + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                    int targetY = (int)player.posY + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                    int targetZ = (int)player.posZ + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                    if (e.world.isAirBlock(new BlockPos(targetX, targetY, targetZ)))
                    {
                        EntityBat bat = new EntityBat(e.world);
                        bat.playLivingSound();
                        bat.setLocationAndAngles((double)targetX + e.world.rand.nextDouble(), (double)targetY + e.world.rand.nextDouble(), (double)targetZ + e.world.rand.nextDouble(), e.world.rand.nextFloat(), e.world.rand.nextFloat());
                        if (e.world.spawnEntity(bat))
                        {
                            MiscHelper.getWarpTag(player).setInteger("bats", --bats);
                            if (bats <= 0)
                                MiscHelper.getWarpTag(player).removeTag("bats");
                            break;
                        }
                    }
                }
            }
        }
    }
}
