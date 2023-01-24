package shukaro.warptheory.handlers.warpevents;


import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import shukaro.warptheory.handlers.IWorldTickWarpEvent;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.MiscHelper;
import shukaro.warptheory.util.NameMetaPair;
import shukaro.warptheory.util.RandomBlockHelper;

import java.util.Set;


public class WarpDecay extends IWorldTickWarpEvent {
    public WarpDecay(int minWarp) {
        super("biomeDecay", minWarp, world -> 512 + world.rand.nextInt(256));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean canDo(World world, EntityPlayer player) {
        for (String n : (Set<String>) MiscHelper.getWarpTag(player).getKeySet()) {
            if (n.startsWith("biome") && !n.equals(getName())) return false;
        }
        return true;
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        BlockCoord target =
                RandomBlockHelper.randomBlock(world, player, 8, block -> MiscHelper.hasNonSolidNeighbor(world, block));
        if (target == null) {
            return 0;
        }

        NameMetaPair pair = new NameMetaPair(target.getBlock(world), target.getMeta(world));
        if (WarpHandler.decayMappings.containsKey(pair)
                || pair.getBlock() instanceof IPlantable
                || pair.getBlock().getMaterial(pair.getBlock().getDefaultState()) == Material.LEAVES) {
            NameMetaPair decayed = WarpHandler.decayMappings.get(pair);
            if (decayed == null) decayed = new NameMetaPair(Blocks.AIR, 0);
            if (world.setBlockState(new BlockPos(target.x, target.y, target.z), decayed.getBlock().getDefaultState(), 3)) {
                if (target.isAir(world))
                return 1;
            }
        }

        return 0;
    }
}