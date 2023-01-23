package shukaro.warptheory.items;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.Constants;
import shukaro.warptheory.util.FormatCodes;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.items.ItemsTC;

import javax.annotation.Nullable;
import java.util.List;

import static shukaro.warptheory.items.WarpItems.itemAmulet;
import static shukaro.warptheory.items.WarpItems.itemCleanser;

public class ItemAmulet extends Item implements IBauble
{
    public ItemAmulet()
    {
        super();
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.setCreativeTab(WarpTheory.mainTab);
        this.setUnlocalizedName(Constants.ITEM_PURE_TALISMAN);
        this.setRegistryName("item_cleansing_amulet");
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
    public BaubleType getBaubleType(ItemStack itemstack)
    {
        return BaubleType.AMULET;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase entity)
    {
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)entity;
            if (player.ticksExisted % 500 != 0 || WarpHandler.getTotalWarp(player) <= 0 || player.world.isRemote)
                return;
            if (player.world.rand.nextInt(100) <= Math.sqrt(WarpHandler.getTotalWarp(player)))
            {
                IWarpEvent event = WarpHandler.queueOneEvent(player, WarpHandler.getTotalWarp(player));
               	WarpHandler.removeWarp(player, (event != null) ? event.getCost() : 1);
            }
        }
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player)
    {
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player)
    {
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player)
    {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player)
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(FormatCodes.DarkGrey.code + FormatCodes.Italic.code + I18n.translateToLocal("tooltip.warptheory.amulet"));
    }
}
