package shukaro.warptheory.items;


import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.Constants;
import shukaro.warptheory.util.FormatCodes;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.items.ItemsTC;

import javax.annotation.Nullable;
import java.util.List;

import static shukaro.warptheory.items.WarpItems.itemCleanser;

public class ItemCleanser extends ItemFood
{

    public ItemCleanser()
    {
        super(0, 0, false);
        this.setHasSubtypes(true);
        this.setMaxStackSize(16);
        this.setMaxDamage(0);
        this.setCreativeTab(WarpTheory.mainTab);
        this.setUnlocalizedName(Constants.ITEM_WARPCLEANSER);
        this.setAlwaysEdible();
        this.setRegistryName("item_cleanser");
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
    public void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            if (WarpHandler.getTotalWarp(player) > 0)
                ChatHelper.sendToPlayer(player, I18n.translateToLocal("chat.warptheory.purge"));
            else
                ChatHelper.sendToPlayer(player, I18n.translateToLocal("chat.warptheory.purgefail"));
            player.world.playSound(null, player.posX, player.posY, player.posZ,
                    SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.PLAYERS, 1F,
                    1.0F + (float) player.getEntityWorld().rand.nextGaussian() * 0.05F);
            WarpHandler.purgeWarp(player);
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 24;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.EAT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(FormatCodes.DarkGrey.code + FormatCodes.Italic.code + I18n.translateToLocal("tooltip.warptheory.cleanser"));
    }
}
