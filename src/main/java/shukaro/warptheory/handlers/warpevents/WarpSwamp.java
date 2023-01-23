package shukaro.warptheory.handlers.warpevents;


import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import shukaro.warptheory.handlers.ConfigHandler;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.*;

import java.util.ArrayList;
import java.util.Set;

public class WarpSwamp extends IWarpEvent
{
	private final int _mMinWarpLevel;
    public WarpSwamp(int pMinWarpLevel)
    {
    	_mMinWarpLevel = pMinWarpLevel;
    	FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public String getName()
    {
        return "biomeSwamp";
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
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + I18n.translateToLocal("chat.warptheory.swamp"));
        MiscHelper.modEventInt(player, "biomeSwamp", 256 + world.rand.nextInt(256));
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
    	if(ConfigHandler.allowGlobalWarpEffects == false)
    		return;
    	
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        // Growing swamp
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getWarpTag(player).hasKey("biomeSwamp"))
            {
                int biomeSwamp = MiscHelper.getWarpTag(player).getInteger("biomeSwamp");
                int targetX = (int)player.posX + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                int targetY = (int)player.posY + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                int targetZ = (int)player.posZ + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                BlockCoord target = new BlockCoord(targetX, targetY, targetZ);
                if (!MiscHelper.hasNonSolidNeighbor(e.world, target))
                    break;
                boolean grown = false;
                if (target.getBlock(e.world) == Blocks.WATER)
                {
                    if (target.offset(1).isAir(e.world) && e.world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.WATERLILY.getDefaultState(), 3))
                        grown = true;
                }
                else if (target.getBlock(e.world) == Blocks.SAPLING)
                {
                    // Function that grows SAPLING into a tree
                    ((BlockSapling)target.getBlock(e.world)).grow(e.world, new BlockPos(target.x, target.y, target.z), target.getBlock(e.world).getDefaultState(), e.world.rand);
                    grown = true;
                }
                else if (target.getBlock(e.world).getMaterial(target.getBlock(e.world).getDefaultState()) == Material.LEAVES || target.getBlock(e.world) == Blocks.LOG || target.getBlock(e.world) == Blocks.LOG2)
                {
                    for (int j = 0; j < 6; j++)
                    {
                        EnumFacing side = EnumFacing.getFront(2 + e.world.rand.nextInt(4));
                        if (Blocks.VINE.canPlaceBlockOnSide(e.world, new BlockPos(target.x, target.y, target.z), side) && target.offset(side.getHorizontalIndex()).isAir(e.world))
                        {
                            e.world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.VINE.getDefaultState(), 3);
                            grown = true;
                            break;
                        }
                    }
                }
                else
                {
                    if (e.world.rand.nextBoolean() && target.getBlock(e.world).canSustainPlant(target.getBlock(e.world).getDefaultState(), e.world, new BlockPos(target.x, target.y, target.z), EnumFacing.UP, (IPlantable)Blocks.SAPLING))
                    {
                        if (e.world.rand.nextBoolean())
                        {
                            if (target.offset(1).isAir(e.world) || (target.getBlock(e.world) instanceof IPlantable && target.getBlock(e.world) != Blocks.SAPLING))
                                e.world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.SAPLING.getDefaultState(), 3);
                        }
                        else if (e.world.rand.nextBoolean())
                        {
                            if (target.offset(1).isAir(e.world) && target.offset(0).getBlock(e.world) instanceof IGrowable)
                                ((IGrowable)target.getBlock(e.world)).grow(e.world, e.world.rand, new BlockPos(target.x, target.y, target.z), target.getBlock(e.world).getDefaultState()); // Bonemealing
                        }
                        else
                        {
                            if (target.offset(1).isAir(e.world) && target.offset(0).getBlock(e.world) == Blocks.GRASS)
                                e.world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.DIRT.getDefaultState(), 3);
                        }
                        grown = true;
                    }
                    else if (e.world.rand.nextBoolean() && MiscHelper.canTurnToSwampWater(e.world, target))
                    {
                        if (target.copy().offset(1).getBlock(e.world) == Blocks.LOG || target.copy().offset(1).getBlock(e.world) == Blocks.LOG2)
                            e.world.setBlockState(new BlockPos(target.x, target.y, target.z), target.copy().offset(1).getBlock(e.world).getDefaultState(), 3);
                        else
                            e.world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.WATER.getDefaultState(), 3);
                        grown = true;
                    }
                    else if (WarpHandler.decayMappings.containsKey(new NameMetaPair(target.getBlock(e.world), target.getMeta(e.world))) && target.getBlock(e.world).isOpaqueCube(target.getBlock(e.world).getDefaultState()) && target.getBlock(e.world) != Blocks.LOG && target.getBlock(e.world) != Blocks.LOG2)
                    {
                        if (target.getBlock(e.world) != Blocks.DIRT && target.getBlock(e.world) != Blocks.GRASS)
                        {
                            if (target.copy().offset(1).getBlock(e.world).isOpaqueCube(target.copy().offset(1).getBlock(e.world).getDefaultState()))
                                e.world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.DIRT.getDefaultState(), 3);
                            else if (e.world.rand.nextBoolean())
                                e.world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.GRASS.getDefaultState(), 3);
                            else
                                e.world.setBlockState(new BlockPos(target.x, target.y, target.z), Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), 3);
                            grown = true;
                        }
                    }
                }
                if (grown)
                {
                    MiscHelper.getWarpTag(player).setInteger("biomeSwamp", --biomeSwamp);
                    if (biomeSwamp <= 0)
                        MiscHelper.getWarpTag(player).removeTag("biomeSwamp");
                }
            }
        }
    }
}
