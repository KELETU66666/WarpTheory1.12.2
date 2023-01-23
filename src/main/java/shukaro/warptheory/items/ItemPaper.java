package shukaro.warptheory.items;


import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.util.Constants;
import shukaro.warptheory.util.FormatCodes;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.items.ItemsTC;

import java.util.List;

import static shukaro.warptheory.handlers.WarpHandler.getIndividualWarps;
import static shukaro.warptheory.items.WarpItems.itemPaper;

public class ItemPaper extends Item
{
    public ItemPaper()
    {
        super();
        this.setHasSubtypes(true);
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
        this.setCreativeTab(WarpTheory.mainTab);
        this.setUnlocalizedName(Constants.ITEM_LITMUS);
        this.setRegistryName("item_warppaper");
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
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn) {
        if (!world.isRemote) {
            ItemStack item = player.getHeldItem(handIn);
            int totalWarp = ThaumcraftApi.internalMethods.getActualWarp(player);
            int[] individualWarps = getIndividualWarps(player);
            String severity;
            if (totalWarp <= 10)
                severity = I18n.translateToLocal("chat.warptheory.minorwarp");
            else if (totalWarp <= 25)
                severity = I18n.translateToLocal("chat.warptheory.averagewarp");
            else if (totalWarp <= 50)
                severity = I18n.translateToLocal("chat.warptheory.majorwarp");
            else
                severity = I18n.translateToLocal("chat.warptheory.deadlywarp");
            player.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE.toString() + TextFormatting.ITALIC + severity));
            player.sendMessage(new TextComponentString(
                    " (" + individualWarps[0] + " " + I18n.translateToLocal("chat.warptheory.permanentwarp") +
                            ", " + individualWarps[1] + " " + I18n.translateToLocal("chat.warptheory.normalwarp") +
                            ", " + individualWarps[2] + " " + I18n.translateToLocal("chat.warptheory.tempwarp") + ")"));
            if (!player.capabilities.isCreativeMode && ThaumcraftApi.internalMethods.getActualWarp(player) > 10) {
                item.setCount(item.getCount() - 1);
            }
        }
        return super.onItemRightClick(world, player, handIn);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(FormatCodes.DarkGrey.code + FormatCodes.Italic.code + I18n.translateToLocal("tooltip.warptheory.paper"));
    }
}
