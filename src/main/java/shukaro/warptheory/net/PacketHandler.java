package shukaro.warptheory.net;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.warpevents.WarpBlood;
import shukaro.warptheory.handlers.warpevents.WarpFakeRain;
import shukaro.warptheory.net.packets.*;
import shukaro.warptheory.util.BlockCoord;
import shukaro.warptheory.util.MiscHelper;

@ChannelHandler.Sharable
public class PacketHandler extends SimpleChannelInboundHandler<IWarpPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IWarpPacket msg) throws Exception {
        INetHandler handler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();

        if (handler instanceof NetHandlerPlayServer) {
            if (msg instanceof DecrementPacket) {
                DecrementPacket dec = (DecrementPacket) msg;
            //    if (dec.id == 0) {
           //         EntityPlayer player = MiscHelper.getPlayerByEntityID(dec.player);
            //        int ears = MiscHelper.getWarpTag(player).getInteger("ears");
          //          MiscHelper.getWarpTag(player).setInteger("ears", --ears);
           //         if (ears <= 0) MiscHelper.getWarpTag(player).removeTag("ears");
           //     }
            }
        } else if (handler instanceof NetHandlerPlayClient) {
            if (msg instanceof EnderParticlesPacket) {
                EnderParticlesPacket blink = (EnderParticlesPacket) msg;
                EntityPlayer player = WarpTheory.proxy.getPlayer();
                if (player == null) return;
                World world = player.world;
                for (int l = 0; l < 128; ++l)
                    world.spawnParticle(
                            EnumParticleTypes.PORTAL,
                            blink.x + world.rand.nextDouble() - world.rand.nextDouble(),
                            blink.y + world.rand.nextDouble() - world.rand.nextDouble(),
                            blink.z + world.rand.nextDouble() - world.rand.nextDouble(),
                            (double) (world.rand.nextFloat() - 0.5F) * 0.2F,
                            (double) (world.rand.nextFloat() - 0.5F) * 0.2F,
                            (double) (world.rand.nextFloat() - 0.5F) * 0.2F);
            } else if (msg instanceof VelocityPacket) {
                VelocityPacket wind = (VelocityPacket) msg;
                EntityPlayer player = WarpTheory.proxy.getPlayer();
                if (player != null) player.addVelocity(wind.x, wind.y, wind.z);
            } else if (msg instanceof BloodPacket) {
                BloodPacket blood = (BloodPacket) msg;
                BlockCoord block = new BlockCoord(blood.x, blood.y, blood.z);
                switch (blood.eventLevel) {
                    case 0:
                        WarpBlood.bloody.put(blood.dim, block);
                        break;

                    case 1:
                        WarpBlood.blackBloody.put(blood.dim, block);
                        break;
                }
            } else if (msg instanceof ClearPacket) {
                ClearPacket clear = (ClearPacket) msg;
                if (clear.id == 0) {
                    WarpBlood.bloody.clear();
                    WarpBlood.blackBloody.clear();
                }
            } else if (msg instanceof ClientEventPacket) {
                ClientEventPacket start = (ClientEventPacket) msg;
                if (start.id == 0) {
                    EntityPlayer player = WarpTheory.proxy.getPlayer();
                    if (player != null) MiscHelper.modEventInt(player, "ears", start.amount);
                }
            } else if (msg instanceof FakeRainPacket) {
                FakeRainPacket packet = (FakeRainPacket) msg;
                WarpFakeRain.rainLevel = packet.eventLevel;
            }
        }
    }
}
