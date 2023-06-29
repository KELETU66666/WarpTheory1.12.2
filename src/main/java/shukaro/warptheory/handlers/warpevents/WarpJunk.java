package shukaro.warptheory.handlers.warpevents;


import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;

import java.util.function.Supplier;

public class WarpJunk extends IWarpEvent {
    private static final ImmutableList<Supplier<ItemStack>> JUNK = ImmutableList.of(
            () -> new ItemStack(Items.WHEAT_SEEDS),
            () -> new ItemStack(Items.ROTTEN_FLESH),
            () -> new ItemStack(Items.BONE),
            () -> new ItemStack(Items.SNOWBALL),
            () -> new ItemStack(Items.SLIME_BALL),
            () -> new ItemStack(Items.FEATHER),
            () -> new ItemStack(Items.DYE, 1, 0),
            () -> new ItemStack(Items.STICK),
            () -> new ItemStack(Items.APPLE),
            () -> new ItemStack(Items.BRICK),
            () -> new ItemStack(Blocks.RED_FLOWER),
            () -> new ItemStack(Blocks.YELLOW_FLOWER),
            () -> new ItemStack(Blocks.BROWN_MUSHROOM),
            () -> new ItemStack(Blocks.RED_MUSHROOM),
            () -> new ItemStack(Blocks.COBBLESTONE),
            () -> new ItemStack(Blocks.GRAVEL),
            () -> new ItemStack(Blocks.SAND),
            () -> new ItemStack(Blocks.DIRT));

    public WarpJunk(int minWarp) {
        super("junk", minWarp);
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        if (world.isRemote) return true;

        boolean successful = false;
        NonNullList<ItemStack> inventory = player.inventory.mainInventory;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i) == ItemStack.EMPTY) {
                inventory.set(i, getJunk(world));
                successful = true;
            }
        }

        if (successful) {
            //TODO: What's the fuck of this function?
            //player.inventory.inventoryChanged = true;
            world.playSound(null, player.getPosition(), SoundEvents.BLOCK_LAVA_POP, SoundCategory.PLAYERS, 1.0F, 1.0F);
            sendChatMessage(player);
        }

        return true;
    }

    private static ItemStack getJunk(World world) {
        ItemStack itemStack = JUNK.get(world.rand.nextInt(JUNK.size())).get();
        for (int i = 1; i < 4; i++) {
            if (world.rand.nextInt(1 << i) == 0) {
                itemStack.setCount(itemStack.getCount()+1);
            }
        }

        return itemStack;
    }
}
