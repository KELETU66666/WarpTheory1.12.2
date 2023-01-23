package shukaro.warptheory.handlers.warpevents;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shukaro.warptheory.handlers.IWarpEvent;
import shukaro.warptheory.net.PacketDispatcher;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;
import shukaro.warptheory.util.MiscHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WarpBlood extends IWarpEvent
{
	private final int _mMinWarpLevel;
    public static Map<Integer, ArrayList<BlockCoord>> bloody = new HashMap<Integer, ArrayList<BlockCoord>>();

    public WarpBlood(int pMinWarpLevel)
    {
    	_mMinWarpLevel = pMinWarpLevel;
    	FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public String getName()
    {
        return "blood";
    }

    @Override
    public int getSeverity()
    {
    	return _mMinWarpLevel;
    }

    @Override
    public boolean doEvent(World world, EntityPlayer player)
    {
        ChatHelper.sendToPlayer(player, FormatCodes.Purple.code + FormatCodes.Italic.code + I18n.translateToLocal("chat.warptheory.blood"));
        MiscHelper.modEventInt(player, "blood", 64 + world.rand.nextInt(128));
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END || e.side != Side.SERVER)
            return;
        for (EntityPlayer player : (ArrayList<EntityPlayer>)e.world.playerEntities)
        {
            if (MiscHelper.getWarpTag(player).hasKey("blood"))
            {
                int blood = MiscHelper.getWarpTag(player).getInteger("blood");
                for (int i = 0; i < 6; i++)
                {
                    int targetX = (int)player.posX + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                    int targetY = (int)player.posY + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                    int targetZ = (int)player.posZ + e.world.rand.nextInt(8) - e.world.rand.nextInt(8);
                    if (e.world.isAirBlock(new BlockPos(targetX, targetY - 1, targetZ)) && !e.world.isAirBlock(new BlockPos(targetX, targetY, targetZ)) && e.world.getBlockState(new BlockPos(targetX, targetY, targetZ)).getMaterial().blocksMovement())
                    {
                        PacketDispatcher.sendBloodEvent(player, targetX, targetY + 1, targetZ);
                        MiscHelper.getWarpTag(player).setInteger("blood", --blood);
                        if (blood <= 0)
                        {
                            MiscHelper.getWarpTag(player).removeTag("blood");
                            PacketDispatcher.sendBloodClearEvent(player);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent e)
    {
        if (e.phase != TickEvent.Phase.END)
            return;
        World world = Minecraft.getMinecraft().world;
        if (world != null && world.getTotalWorldTime() % 5 == 0 && bloody.get(world.provider.getDimension()) != null)
        {
            for (BlockCoord c : bloody.get(world.provider.getDimension()))
                MiscHelper.spawnDripParticle(world, c.x, c.y, c.z, world.rand.nextFloat() + 0.2f, 0.0f, 0.0f);
        }
    }
}
