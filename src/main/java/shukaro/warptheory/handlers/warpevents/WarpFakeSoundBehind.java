package shukaro.warptheory.handlers.warpevents;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class WarpFakeSoundBehind extends WarpFakeSound {

    public WarpFakeSoundBehind(String name, int minWarp, SoundEvent sound) {
        super(name, minWarp, sound, 16);
    }

    public WarpFakeSoundBehind(String name, int minWarp, SoundEvent sound, int distance) {
        super(name, minWarp, sound, distance);
    }

    public WarpFakeSoundBehind(String name, int minWarp, SoundEvent sound, int distance, float volume, float pitch) {
        super(name, minWarp, sound, distance, volume, pitch);
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        double yaw = player.getRotationYawHead();
        double targetX =
                player.posX - (distance * Math.sin(Math.toRadians(90 - yaw))) * (Math.sin(Math.toRadians(yaw)));
        double targetZ =
                player.posZ - (distance * Math.sin(Math.toRadians(90 - yaw))) * (Math.cos(Math.toRadians(yaw)));

        world.playSound((double)targetX, player.posY, (double)targetZ, SoundEvents.ENTITY_WITHER_AMBIENT, SoundCategory.NEUTRAL, 1.0F, 1.0F, false);

        return 1;
    }
}
