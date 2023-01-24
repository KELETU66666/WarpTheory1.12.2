package shukaro.warptheory.handlers.warpevents;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;

import java.util.Collection;

public class WarpBuff extends IWarpEvent {
    private final ImmutableList<PotionEffect> potionEffects;

    public WarpBuff(String name, int minWarp, PotionEffect... effects) {
        super(name, minWarp);
        this.potionEffects = ImmutableList.copyOf(effects);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean doEvent(World world, EntityPlayer player) {
        if (world.isRemote) return true;

        for (PotionEffect effect : potionEffects) {
            Potion id = effect.getPotion();
            int duration = effect.getDuration();
            int level = effect.getAmplifier();

            if (player.isPotionActive(id)) {
                for (PotionEffect e : (Collection<PotionEffect>) player.getActivePotionEffects()) {
                    if (e.getPotion() == id) {
                        effect = new PotionEffect(id, duration + e.getDuration(), level);
                        break;
                    }
                }
            } else {
                effect = new PotionEffect(id, duration, level);
            }

            effect.getCurativeItems().clear();
            player.addPotionEffect(effect);
        }

        sendChatMessage(player);
        return true;
    }
}
