package shukaro.warptheory.handlers.warpevents;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WarpInventorySwap extends IWarpEvent {
    public WarpInventorySwap(int minWarp) {
        super("inventoryswap", minWarp);
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player) {
        if (world.isRemote) return true;

        NonNullList<ItemStack> inventory = player.inventory.mainInventory;
        // Find empty and full slots in the player's main inventory (excluding hotbar).
        List<Integer> emptySlots = new ArrayList<>();
        List<Integer> fullSlots = new ArrayList<>();
        for (int i = 10; i < 36; i++) {
            if (inventory.get(i) == ItemStack.EMPTY) {
                emptySlots.add(i);
            } else {
                fullSlots.add(i);
            }
        }

        boolean canSwapHandItem = player.inventory.getCurrentItem() != ItemStack.EMPTY && !emptySlots.isEmpty();
        boolean canSwapInventory = !fullSlots.isEmpty();
        if (canSwapHandItem && canSwapInventory) {
            // Pick one of the two to do.
            if (world.rand.nextBoolean()) {
                canSwapHandItem = false;
            } else {
                canSwapInventory = false;
            }
        }

        if (canSwapHandItem) {
            int swapIndex = emptySlots.get(world.rand.nextInt(emptySlots.size()));

            inventory.set(swapIndex, player.inventory.getCurrentItem());
            inventory.set(player.inventory.currentItem, ItemStack.EMPTY);
        } else if (canSwapInventory) {
            int swapIndex1 = fullSlots.get(world.rand.nextInt(fullSlots.size()));
            List<Integer> otherIndices = IntStream.range(10, 36).boxed().collect(Collectors.toList());
            int swapIndex2 = otherIndices.get(world.rand.nextInt(otherIndices.size()));

            ItemStack temp = inventory.get(swapIndex1);
            inventory.set(swapIndex1, inventory.get(swapIndex2));
            inventory.set(swapIndex2, temp);
        }

        if (canSwapHandItem || canSwapInventory) {
            //TODO: What's the fuck of this function?
            //player.inventory.inventoryChanged = true;
            world.playSound(player, player.getPosition(), SoundEvents.BLOCK_LAVA_POP, SoundCategory.PLAYERS, 1.0F, 1.0F);
            // No message for this one.
        }

        return true;
    }
}
