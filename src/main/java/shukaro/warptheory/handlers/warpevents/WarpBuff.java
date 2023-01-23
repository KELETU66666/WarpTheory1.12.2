package shukaro.warptheory.handlers.warpevents;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;

import java.util.Collection;

public class WarpBuff extends IWarpEvent
{
	private final int _mMinWarpLevel;
    private String name;
    private Potion id;
    private int duration;
    private int level;

    public WarpBuff(int pMinWarpLevel, String name, PotionEffect effect)
    {
    	_mMinWarpLevel = pMinWarpLevel;
        this.name = name;
        this.id = effect.getPotion();
        this.duration = effect.getDuration();
        this.level = effect.getAmplifier();
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public int getSeverity()
    {
    	return _mMinWarpLevel;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        if (world.isRemote)
            return true;
        PotionEffect effect = null;
        if (player.isPotionActive(id))
        {
            for (PotionEffect e : (Collection<PotionEffect>)player.getActivePotionEffects())
            {
                if (e.getPotion() == id)
                {
                    effect = new PotionEffect(id, duration + e.getDuration(), level);
                    break;
                }
            }
        }
        else
            effect = new PotionEffect(id, duration, level);
        if (effect != null)
        {
            effect.getCurativeItems().clear();
            player.addPotionEffect(effect);
            ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + I18n.translateToLocal("chat.warptheory." + getName()));
        }
        return true;
    }
}
