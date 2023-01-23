package shukaro.warptheory.handlers.warpevents;



import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.ConfigHandler;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;

public class WarpWither extends IWarpEvent
{
	private final int _mMinWarpLevel;
    public WarpWither(int pMinWarpLevel)
    {
    	_mMinWarpLevel = pMinWarpLevel;
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public String getName()
    {
        return "wither";
    }

    @Override
    public int getSeverity()
    {
    	return _mMinWarpLevel;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + I18n.translateToLocal("chat.warptheory.wither"));
        MiscHelper.modEventInt(player, "wither", 1);
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
    	if(ConfigHandler.allowGlobalWarpEffects == false)
    		return;
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getWarpTag(player).hasKey("wither"))
            {
                int wither = MiscHelper.getWarpTag(player).getInteger("wither");
                for (int i = 0; i < 6; i++)
                {
                    int targetX = (int)player.posX + e.world.rand.nextInt(4) - e.world.rand.nextInt(4);
                    int targetY = (int)player.posY + e.world.rand.nextInt(4) - e.world.rand.nextInt(4);
                    int targetZ = (int)player.posZ + e.world.rand.nextInt(4) - e.world.rand.nextInt(4);

                    if (!e.world.getBlockState(new BlockPos(targetX, targetY - 1, targetZ)).getMaterial().blocksMovement())
                        return;
                    for (int xb = targetX - 1; xb < targetX + 1; xb++)
                    {
                        for (int yb = targetY; yb < targetY + 2; yb++)
                        {
                            for (int zb = targetZ - 1; zb < targetZ + 1; zb++)
                            {
                                if (e.world.getBlockState(new BlockPos(xb, yb, zb)).getMaterial().blocksMovement())
                                    return;
                            }
                        }
                    }

                    EntityLightningBolt bolt = new EntityLightningBolt(e.world, targetX, targetY, targetZ, false);
                    e.world.addWeatherEffect(bolt);
                    e.world.playSound(targetX, targetY, targetZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 4.0F, (1.0F + (e.world.rand.nextFloat() - e.world.rand.nextFloat()) * 0.2F) * 0.7F, false);
                    e.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, targetX, targetY, targetZ, 1.0D, 0.0D, 0.0D);
                    EntityWither ew = new EntityWither(e.world);
                    ew.setLocationAndAngles((double)targetX + 0.5D, (double)targetY - 0.5D, (double)targetZ + 0.5D, e.world.rand.nextFloat(), e.world.rand.nextFloat());
                    ew.ignite();
                    if (e.world.spawnEntity(ew))
                    {
                        MiscHelper.getWarpTag(player).setInteger("wither", --wither);
                        if (wither <= 0)
                            MiscHelper.getWarpTag(player).removeTag("wither");
                        break;
                    }
                }
            }
        }
    }
}
