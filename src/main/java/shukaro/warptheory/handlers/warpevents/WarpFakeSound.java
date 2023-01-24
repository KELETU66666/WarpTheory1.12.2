package shukaro.warptheory.handlers.warpevents;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import shukaro.warptheory.handlers.IWorldTickWarpEvent;

public class WarpFakeSound extends IWorldTickWarpEvent {
    protected final SoundEvent sound;
    protected final int distance; // radius in blocks about player in which it can occur
    protected final float volume;
    protected final float pitch;

    public WarpFakeSound(String name, int minWarp, SoundEvent sound) {
        this(name, minWarp, sound, 16);
    }

    public WarpFakeSound(String name, int minWarp, SoundEvent sound, int distance) {
        this(name, minWarp, sound, distance, 1.0f, 1.0f);
    }

    public WarpFakeSound(String name, int minWarp, SoundEvent sound, int distance, float volume, float pitch) {
        super(name, minWarp, world -> 1);
        this.sound = sound;
        this.distance = distance;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void sendChatMessage(EntityPlayer player) {
        // No message. Otherwise kinda spoils the surprise.  Nobody will pay attention if they see "fake explosion
        // happened!" message
    }

    @Override
    public int triggerEvent(int eventAmount, World world, EntityPlayer player) {
        // (-distance, distance) swing
        // [0, 2*distance] - distance ~= [-distance, distance]  more or less
        int targetX = (int) player.posX + world.rand.nextInt(2 * distance) - (distance);
        int targetY = (int) player.posY + world.rand.nextInt(2 * distance) - (distance);
        int targetZ = (int) player.posZ + world.rand.nextInt(2 * distance) - (distance);

        world.playSound((double)targetX, (double)targetY, (double)targetZ, sound, SoundCategory.NEUTRAL, 1.0F, 1.0F, false);

        return 1;
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent e) {
        if (e.world.getTotalWorldTime() % 20 != 0) {
            return;
        }

        super.onTick(e);
    }
}
