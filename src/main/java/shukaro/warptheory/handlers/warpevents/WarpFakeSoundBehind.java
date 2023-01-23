package shukaro.warptheory.handlers.warpevents;



import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;

public class WarpFakeSoundBehind extends IWarpEvent
{
	private final int _mMinWarpLevel;
    private final String name;
    private final String sound;
    private int distance = 16; //approximate distance behind player in blocks to play sound

    public WarpFakeSoundBehind(int pMinWarpLevel, String name, String sound)
    {
    	_mMinWarpLevel = pMinWarpLevel;
        this.name = name;
        this.sound = sound;
        FMLCommonHandler.instance().bus().register(this);
    }

    public WarpFakeSoundBehind(int pMinWarpLevel, String name, String sound, int distance)
    {
    	_mMinWarpLevel = pMinWarpLevel;
        this.name = name;
        this.sound = sound;
        this.distance = distance;
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getSeverity() { return _mMinWarpLevel; }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        //No message
        //ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + I18n.translateToLocal("chat.warptheory.fakesoundbehind"));
        MiscHelper.modEventInt(player, getName(), 1);
        return true;
    }


    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {

            int fakesound = MiscHelper.getWarpTag(player).getInteger(getName());
            if (fakesound > 0 && e.world.getTotalWorldTime() % 20 == 0)
            {

                double yaw = player.getRotationYawHead();
                double targetX = player.posX - (distance * Math.sin(Math.toRadians(90 - yaw))) * (Math.sin(Math.toRadians(yaw)));
                double targetZ = player.posZ - (distance * Math.sin(Math.toRadians(90 - yaw))) * (Math.cos(Math.toRadians(yaw)));

                //wtf do last two parameters do?  Documentation appears non-existent.
                //TODO: replace with something not a guess
                e.world.playSound((double)targetX, player.posY, (double)targetZ, SoundEvents.ENTITY_WITHER_AMBIENT, SoundCategory.NEUTRAL, 1.0F, 1.0F, false);

                MiscHelper.getWarpTag(player).setInteger(getName(), --fakesound);
            }
        }
    }
}
