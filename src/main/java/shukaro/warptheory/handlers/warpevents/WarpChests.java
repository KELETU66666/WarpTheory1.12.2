package shukaro.warptheory.handlers.warpevents;



import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;

import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;
import java.util.Random;

public class WarpChests extends IWarpEvent
{
	private final int _mMinWarpLevel;
    public WarpChests(int pMinWarpLevel)
    {
    	_mMinWarpLevel = pMinWarpLevel;
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public String getName()
    {
        return "chests";
    }

    @Override
    public int getSeverity()
    {
    	return _mMinWarpLevel;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + I18n.translateToLocal("chat.warptheory.chests"));
        MiscHelper.modEventInt(player, "chests", 15 + world.rand.nextInt(30));
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER || e.world.getTotalWorldTime() % 10 != 0)
            return;
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getWarpTag(player).hasKey("chests"))
            {
                int chests = MiscHelper.getWarpTag(player).getInteger("chests");
                ArrayList<IInventory> inventories = MiscHelper.getNearbyTileInventories(player, 8);
                ArrayList<TileEntityChest> chestInventories = new ArrayList<TileEntityChest>();
                for (IInventory i : inventories)
                {
                    if (i instanceof TileEntityChest)
                        chestInventories.add((TileEntityChest)i);
                }
                if (chestInventories.size() <= 0)
                    return;
                TileEntityChest inv1 = chestInventories.get(e.world.rand.nextInt(chestInventories.size()));
                TileEntityChest inv2 = chestInventories.get(e.world.rand.nextInt(chestInventories.size()));
                if (e.world.rand.nextInt(10) == 0)
                {
                    if (e.world.rand.nextBoolean())
                        e.world.playSound((double)inv1.getPos().getX(), (double)inv1.getPos().getY() + 0.5D, (double)inv1.getPos().getZ(), SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, e.world.rand.nextFloat() * 0.1F + 0.9F, false);
                    else
                        e.world.playSound((double)inv2.getPos().getX(), (double)inv2.getPos().getY() + 0.5D, (double)inv2.getPos().getZ(), SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, e.world.rand.nextFloat() * 0.1F + 0.9F, false);
                    MiscHelper.getWarpTag(player).setInteger("chests", --chests);
                }
                if (!shuffle(e.world.rand, inv1, inv2))
                    shuffle(e.world.rand, inv1, inv2);
                if (chests <= 0)
                    MiscHelper.getWarpTag(player).removeTag("chests");
            }
        }
    }

    private boolean shuffle(Random rand, IInventory inv1, IInventory inv2)
    {
        int firstSlot = rand.nextInt(inv1.getSizeInventory());
        int secondSlot = rand.nextInt(inv2.getSizeInventory());
        ItemStack firstContents = inv1.getStackInSlot(firstSlot);
        ItemStack secondContents = inv2.getStackInSlot(secondSlot);
        if (inv1.isItemValidForSlot(firstSlot, secondContents) && inv2.isItemValidForSlot(secondSlot, firstContents))
        {
            inv1.setInventorySlotContents(firstSlot, secondContents);
            inv2.setInventorySlotContents(secondSlot, firstContents);
        }
        return inv1.isItemValidForSlot(firstSlot, secondContents) && inv2.isItemValidForSlot(secondSlot, firstContents) && (firstContents != null || secondContents != null);
    }
}
