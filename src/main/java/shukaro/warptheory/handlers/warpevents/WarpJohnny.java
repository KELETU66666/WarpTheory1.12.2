package shukaro.warptheory.handlers.warpevents;


import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IMultiWarpEvent;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.RandomBlockHelper;

public class WarpJohnny extends IMultiWarpEvent {
    public WarpJohnny(int minWarp) {
        super("johnny", minWarp, 3, world -> 1);
    }

    @Override
    public int triggerEvent(int eventLevel, int eventAmount, World world, EntityPlayer player) {
        int successful = 0;

        for (int i = 0; i < 6; i++) {
            BlockCoord target = RandomBlockHelper.randomDoubleAirBlock(world, player, 16);
            if (target == null) {
                continue;
            }

            EntityVindicator johnny = new EntityVindicator(world);
            switch (eventLevel) {
                case 2:
                case 1:
                    johnny.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.DIAMOND_AXE));
                    break;

                case 0:
                default:
                    johnny.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
                    break;
            }
            if (eventLevel == 2) {
                johnny.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 60 * 20, 2));
                johnny.addPotionEffect(new PotionEffect(MobEffects.SPEED, 60 * 20, 2));
            }

            try {
                johnny.setCustomNameTag("Johnny");
            } catch (Exception x) {
                x.printStackTrace();
            }

            johnny.playLivingSound();
            RandomBlockHelper.setLocation(world, johnny, target);
            if (world.spawnEntity(johnny)) {
                successful++;
                if (successful >= eventAmount) {
                    break;
                }
            }
        }

        return successful;
    }
}