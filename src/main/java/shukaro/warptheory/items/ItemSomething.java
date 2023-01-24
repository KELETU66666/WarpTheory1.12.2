package shukaro.warptheory.items;


import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.util.Constants;
import shukaro.warptheory.util.FormatCodes;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerWarp;

import java.util.List;

public class ItemSomething extends ItemFood
{
    public ItemSomething()
    {
        super(0, 0, false);
        this.setHasSubtypes(true);
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
        this.setUnlocalizedName(Constants.ITEM_SOMETHING);
        this.setRegistryName("item_something");
        this.setAlwaysEdible();
        this.setCreativeTab(WarpTheory.mainTab);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return this.getUnlocalizedName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.UNCOMMON;
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        if(!worldIn.isRemote) {
            ThaumcraftApi.internalMethods.addWarpToPlayer(player,5 + worldIn.rand.nextInt(5), IPlayerWarp.EnumWarpType.TEMPORARY);
            ThaumcraftApi.internalMethods.addWarpToPlayer(player,4 + worldIn.rand.nextInt(4), IPlayerWarp.EnumWarpType.PERMANENT);
            ThaumcraftApi.internalMethods.addWarpToPlayer(player,5 + worldIn.rand.nextInt(5), IPlayerWarp.EnumWarpType.NORMAL);
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.EAT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(FormatCodes.DarkGrey.code + FormatCodes.Italic.code + I18n.translateToLocal("tooltip.warptheory.something"));
    }
}
