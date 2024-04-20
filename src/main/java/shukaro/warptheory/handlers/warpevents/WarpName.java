package shukaro.warptheory.handlers.warpevents;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.IWarpEvent;

public class WarpName extends IWarpEvent {
    public WarpName(int minWarp) {
        super("randomName", minWarp);
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        if(world.isRemote) return true;

        NonNullList<ItemStack> inventory = player.inventory.mainInventory;
        for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
            ItemStack stack = inventory.get(world.rand.nextInt(i + 1));
            if(stack != ItemStack.EMPTY)
                stack.setStackDisplayName(WarpTheory.normalNames.compose(world.rand.nextInt(3) + 2));
        }

        return true;
    }
}
