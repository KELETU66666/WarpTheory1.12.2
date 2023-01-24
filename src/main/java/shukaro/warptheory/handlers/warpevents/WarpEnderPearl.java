package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;

public class WarpEnderPearl extends IWarpEvent {
    public WarpEnderPearl(int minWarp) {
        super("enderpearl", minWarp);
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        if (world.isRemote) return false;

        if (world.spawnEntity(new EntityEnderPearl(world, player))) {
            world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ENDERMEN_SCREAM, SoundCategory.PLAYERS, 1.0F, 1.0F);
            sendChatMessage(player);
        }
        return true;
    }
}
