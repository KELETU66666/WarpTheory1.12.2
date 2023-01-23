package shukaro.warptheory.handlers.warpevents;



import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.ConfigHandler;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.*;

import java.util.ArrayList;
import java.util.Set;

public class WarpDecay extends IWarpEvent
{
	private final int _mMinWarpLevel;
    public WarpDecay(int pMinWarpLevel) 
    {
    	_mMinWarpLevel = pMinWarpLevel;
    	FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public String getName()
    {
        return "biomeDecay";
    }

    @Override
    public int getSeverity()
    {
    	return _mMinWarpLevel;
    }

    @Override
    public boolean canDo(EntityPlayer player)
    {
        for (String n : (Set<String>)MiscHelper.getWarpTag(player).getKeySet())
        {
            if (n.startsWith("biome") && !n.equals(getName()))
                return false;
        }
        return true;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + I18n.translateToLocal("chat.warptheory.decay"));
        MiscHelper.modEventInt(player, getName(), 256 * 2 + world.rand.nextInt(256));
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
    	if(ConfigHandler.allowGlobalWarpEffects == false)
    		return;
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        // Decay terrain
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getWarpTag(player).hasKey("biomeDecay"))
            {
                int decay = MiscHelper.getWarpTag(player).getInteger("biomeDecay");
                int targetX = (int)player.posX + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                int targetY = (int)player.posY + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                int targetZ = (int)player.posZ + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                BlockCoord target = new BlockCoord(targetX, targetY, targetZ);
                NameMetaPair pair = new NameMetaPair(target.getBlock(e.world), target.getMeta(e.world));
                if (!MiscHelper.hasNonSolidNeighbor(e.world, target))
                    return;
                if (WarpHandler.decayMappings.containsKey(pair) || pair.getBlock() instanceof IPlantable || pair.getBlock().getMaterial(pair.getBlock().getDefaultState()) == Material.LEAVES)
                {
                    NameMetaPair decayed = WarpHandler.decayMappings.get(pair);
                    if (decayed == null)
                        decayed = new NameMetaPair(Blocks.AIR, 0);
                    if (e.world.setBlockState(new BlockPos(target.x, target.y, target.z), decayed.getBlock().getDefaultState(), 3))
                    {
                        MiscHelper.getWarpTag(player).setInteger("biomeDecay", --decay);
                        if (decay <= 0)
                            MiscHelper.getWarpTag(player).removeTag("biomeDecay");
                    }
                }
            }
        }
    }
}
