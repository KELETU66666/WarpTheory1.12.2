package shukaro.warptheory.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class WarpItems
{
    public static Item itemCleanser;
    public static Item itemAmulet;
    public static Item itemSomething;
    public static Item itemPaper;

    public static void initItems()
    {
        itemCleanser = new ItemCleanser();
        ForgeRegistries.ITEMS.register(itemCleanser);
        itemAmulet = new ItemAmulet();
        ForgeRegistries.ITEMS.register(itemAmulet);
        itemSomething = new ItemSomething();
        ForgeRegistries.ITEMS.register(itemSomething);
        itemPaper = new ItemPaper();
        ForgeRegistries.ITEMS.register(itemPaper);
    }
}
