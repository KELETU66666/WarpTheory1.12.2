package shukaro.warptheory.handlers.warpevents;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;
import thaumcraft.common.lib.SoundsTC;

public class WarpCoin extends IWarpEvent {
    public WarpCoin(int minWarp) {
        super("coin", minWarp);
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        if (world.isRemote) return true;

        player.entityDropItem(new ItemStack(Items.GOLD_NUGGET), 0.0f);
        world.playSound(player, player.getPosition(), SoundsTC.coins, SoundCategory.PLAYERS, 1.0F, 1.0F);
        sendChatMessage(player);
        return true;
    }
}