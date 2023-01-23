package shukaro.warptheory.util;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shukaro.warptheory.entity.EntityDropParticleFX;
import shukaro.warptheory.handlers.WarpHandler;

import java.util.ArrayList;

public class MiscHelper
{

 //   public static EntityPlayer getPlayerByEntityID(int id)
  //  {
  //      for (EntityPlayer serverPlayer : (ArrayList<EntityPlayer>)getServer().getConfigurationManager().playerEntityList)
  //      {
    //        if (serverPlayer.getEntityId() == id)
  //              return serverPlayer;
   //     }
  //      return null;
 //   }

    public static NBTTagCompound getWarpTag(EntityPlayer player)
    {
        if (!player.getEntityData().hasKey(Constants.modID))
        {
            NBTTagCompound tag = new NBTTagCompound();
            player.getEntityData().setTag(Constants.modID, tag);
            return tag;
        }
        return player.getEntityData().getCompoundTag(Constants.modID);
    }

    public static NBTTagCompound modEventInt(EntityPlayer player, String tagName, int amount)
    {
        NBTTagCompound tag = getWarpTag(player);
        tag.setInteger(tagName, tag.getInteger(tagName) + amount);
        return tag;
    }

    public static boolean hasNonSolidNeighbor(World world, BlockCoord coord)
    {
        for (BlockCoord n : coord.getNearby())
        {
            if (n.isAir(world) || !n.getBlock(world).isOpaqueCube(n.getBlock(world).getDefaultState()))
                return true;
        }
        return false;
    }

    public static boolean canTurnToSwampWater(World world, BlockCoord coord)
    {
        NameMetaPair pair = new NameMetaPair(coord.getBlock(world), coord.getMeta(world));
        boolean contained = true;
        for (int i = 0; i < 6; i++)
        {
            if (i != 1 && (!coord.copy().offset(i).getBlock(world).isOpaqueCube(coord.copy().offset(i).getBlock(world).getDefaultState()) && coord.copy().offset(i).getBlock(world) != Blocks.WATER))
                contained = false;
        }
        BlockCoord cover = coord.copy().offset(1);
        return contained && (coord.isAir(world) || WarpHandler.decayMappings.containsKey(pair)) && (cover.isAir(world) || cover.getBlock(world) == Blocks.LOG || cover.getBlock(world) == Blocks.LOG2 || cover.getBlock(world) instanceof IPlantable);
    }

    public static ArrayList<IInventory> getNearbyTileInventories(EntityPlayer player, int range)
    {
        ArrayList<IInventory> nearby = new ArrayList<IInventory>();
        for (TileEntity te : (ArrayList<TileEntity>)player.world.loadedTileEntityList)
        {
            BlockCoord teCoord = new BlockCoord(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
            BlockCoord playerCoord = new BlockCoord((int)player.posX, (int)player.posY, (int)player.posZ);
            if (te instanceof IInventory && teCoord.getDistance(playerCoord) <= range)
                nearby.add((IInventory)te);
        }
        return nearby;
    }

    @SideOnly(Side.CLIENT)
    public static void spawnDripParticle(World world, int x, int y, int z, float r, float g, float b)
    {
        if (world.isAirBlock(new BlockPos(x, y - 2, z)) && !world.isAirBlock(new BlockPos(x, y - 1, z)) && world.getBlockState(new BlockPos(x, y - 1, z)).getMaterial().blocksMovement())
        {
            double px = x + world.rand.nextFloat();
            double py = y - 1.05D;
            double pz = z + world.rand.nextFloat();
            Particle fx = new EntityDropParticleFX(world, px, py, pz, r, g, b);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
        }
    }
}
