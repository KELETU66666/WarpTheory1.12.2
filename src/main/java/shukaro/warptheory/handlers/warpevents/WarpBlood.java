package shukaro.warptheory.handlers.warpevents;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shukaro.warptheory.handlers.IMultiWarpEvent;
import shukaro.warptheory.net.PacketDispatcher;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.MiscHelper;
import shukaro.warptheory.util.RandomBlockHelper;

public class WarpBlood extends IMultiWarpEvent {
    public static ListMultimap<Integer, BlockCoord> bloody =
            MultimapBuilder.hashKeys().arrayListValues().build();
    public static ListMultimap<Integer, BlockCoord> blackBloody =
            MultimapBuilder.hashKeys().arrayListValues().build();

    public WarpBlood(int minWarp) {
        super("blood", minWarp, 2, world -> 64 + world.rand.nextInt(128));
    }

    @Override
    public int triggerEvent(int eventLevel, int eventAmount, World world, EntityPlayer player) {
        int successful = 0;
        for (int i = 0; i < 6; i++) {
            BlockCoord target = RandomBlockHelper.randomBlock(world, player, 8, block -> isBlockValid(world, block));
            if (target == null) {
                continue;
            }

            PacketDispatcher.sendBloodEvent(player, eventLevel, target.x, target.y + 1, target.z);
            successful++;
            if (successful >= eventAmount) {
                PacketDispatcher.sendBloodClearEvent(player);
                break;
            }
        }

        return successful;
    }

    private static boolean isBlockValid(World world, BlockCoord block) {
        return world.isAirBlock(new BlockPos(block.x, block.y - 1, block.z))
                && !block.isAir(world)
                && world.getBlockState(new BlockPos(block.x, block.y, block.z)).getMaterial().blocksMovement();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;

        World world = Minecraft.getMinecraft().world;
        if (world != null && world.getTotalWorldTime() % 5 == 0 && bloody.get(world.provider.getDimension()) != null) {
            for (BlockCoord c : bloody.get(world.provider.getDimension()))
                MiscHelper.spawnDripParticle(world, c.x, c.y, c.z, world.rand.nextFloat() + 0.2f, 0.0f, 0.0f);
            for (BlockCoord c : blackBloody.get(world.provider.getDimension()))
                MiscHelper.spawnDripParticle(world, c.x, c.y, c.z, 0.0f, 0.0f, 0.0f);
        }
    }
}