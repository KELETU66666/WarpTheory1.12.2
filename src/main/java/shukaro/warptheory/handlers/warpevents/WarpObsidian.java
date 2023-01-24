package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.BlockCoord;

public class WarpObsidian extends IWarpEvent {
    public WarpObsidian(int minWarp) {
        super("obsidian", minWarp);
    }

    @Override
    public boolean canDo(World world, EntityPlayer player) {
        BlockCoord playerHead = new BlockCoord(player).offset(1);
        return playerHead.isAir(world);
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        if (world.isRemote) return true;

        // This should be guaranteed to be true, since we check in canDo().
        // But check again just to be safe (in case the code changes).
        BlockCoord playerHead = new BlockCoord(player).offset(1);
        if (playerHead.isAir(world)) {
            world.setBlockState(new BlockPos(playerHead.x, playerHead.y, playerHead.z), Blocks.OBSIDIAN.getDefaultState());
            world.playSound(player, player.getPosition(), SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.PLAYERS, 1.0F, 1.0F);
            sendChatMessage(player);
        }

        return true;
    }
}
