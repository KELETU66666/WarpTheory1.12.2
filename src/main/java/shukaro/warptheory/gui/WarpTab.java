package shukaro.warptheory.gui;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import shukaro.warptheory.items.WarpItems;

public class WarpTab extends CreativeTabs
{
    public WarpTab(String label)
    {
        super(label);
    }

    @Override
    public ItemStack getTabIconItem()
    {
        return WarpItems.itemCleanser.getDefaultInstance();
    }
}
