package shukaro.warptheory.handlers.warpevents;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import shukaro.warptheory.handlers.IWorldTickWarpEvent;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;
import java.util.Random;

public class WarpChests extends IWorldTickWarpEvent {
    public WarpChests(int minWarp) {
        super("chests", minWarp, world -> 15 + world.rand.nextInt(30));
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        int successful = 0;

        ArrayList<IInventory> inventories = MiscHelper.getNearbyTileInventories(player, 8);
        ArrayList<TileEntityChest> chestInventories = new ArrayList<TileEntityChest>();
        for (IInventory i : inventories) {
            if (i instanceof TileEntityChest) chestInventories.add((TileEntityChest) i);
        }
        if (chestInventories.size() <= 0) return 0;

        TileEntityChest inv1 = chestInventories.get(world.rand.nextInt(chestInventories.size()));
        TileEntityChest inv2 = chestInventories.get(world.rand.nextInt(chestInventories.size()));

        if (world.rand.nextInt(10) == 0) {
            if (world.rand.nextBoolean())
                world.playSound((double)inv1.getPos().getX(), (double)inv1.getPos().getY() + 0.5D, (double)inv1.getPos().getZ(), SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F, false);
            else
                world.playSound((double)inv1.getPos().getX(), (double)inv1.getPos().getY() + 0.5D, (double)inv1.getPos().getZ(), SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F, false);
            successful++;
        }

        if (!shuffle(world.rand, inv1, inv2)) shuffle(world.rand, inv1, inv2);

        return successful;
    }

    private static boolean shuffle(Random rand, IInventory inv1, IInventory inv2) {
        int firstSlot = rand.nextInt(inv1.getSizeInventory());
        int secondSlot = rand.nextInt(inv2.getSizeInventory());
        ItemStack firstContents = inv1.getStackInSlot(firstSlot);
        ItemStack secondContents = inv2.getStackInSlot(secondSlot);
        if (inv1.isItemValidForSlot(firstSlot, secondContents) && inv2.isItemValidForSlot(secondSlot, firstContents)) {
            inv1.setInventorySlotContents(firstSlot, secondContents);
            inv2.setInventorySlotContents(secondSlot, firstContents);
        }
        return inv1.isItemValidForSlot(firstSlot, secondContents)
                && inv2.isItemValidForSlot(secondSlot, firstContents)
                && (firstContents != null || secondContents != null);
    }

    @Override
    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e) {
        if (e.world.getTotalWorldTime() % 10 != 0) {
            return;
        }

        super.onTick(e);
    }
}
