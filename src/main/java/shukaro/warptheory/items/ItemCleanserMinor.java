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
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.Constants;
import shukaro.warptheory.util.FormatCodes;

import java.util.List;

public class ItemCleanserMinor extends ItemFood {

    public ItemCleanserMinor() {
        super(0, 0, false);
        this.setAlwaysEdible();
        this.setMaxStackSize(16);
        this.setMaxDamage(0);
        this.setUnlocalizedName(Constants.ITEM_WARPCLEANSERMINOR);
        this.setRegistryName("item_cleanser_minor");
        this.setCreativeTab(WarpTheory.mainTab);
    }

    @Override
    public void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            if (WarpHandler.getTotalWarp(player) == 0) {
                ChatHelper.sendToPlayer(player, I18n.translateToLocal("chat.warptheory.purgefail"));
            } else
                WarpHandler.purgeWarpMinor(player);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.UNCOMMON;
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
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(FormatCodes.DarkGrey.code + FormatCodes.Italic.code + I18n.translateToLocal("tooltip.warptheory.cleanserminor"));
    }
}
