package shukaro.warptheory.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.apache.commons.lang3.reflect.FieldUtils;
import shukaro.warptheory.WarpTheory;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * A creeper that explodes, but the explosion doesn't do any damage.
 */
public class EntityFakeCreeper extends EntityCreeper {
    /**
     * The fake creeper will be passive until this many ticks has elapsed.
     */
    public static final int ARMING_TIME = 120;

    protected boolean errorState;

    /**
     * {@link Field} object for the {@link EntityCreeper#explosionRadius} private field.
     *
     * <p>
     * Code must gracefully handle this being null, which will be the case if something went wrong when trying to fetch
     * the field.
     */
    @Nullable
    public static final Field explosionRadiusField;

    static {
        Field explosionRadius = null;
        try {
            explosionRadius = FieldUtils.getField(EntityCreeper.class, "field_82226_g", true);
            if (explosionRadius == null) {
                explosionRadius = FieldUtils.getField(EntityCreeper.class, "explosionRadius", true);
            }
        } catch (Exception e) {
            WarpTheory.logger.error("Got exception trying to access explosionRadius for EntityFakeCreeper", e);
        }
        explosionRadiusField = explosionRadius;
    }

    protected int armingTimeRemaining;

    public EntityFakeCreeper(World world) {
        super(world);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        this.setHealth(100.0f);

        // Unfortunately, EntityCreeper's explosion method is private, so we cannot override it.
        // We will set the creeper's explosion radius to 0, instead. If we are unable to do so,
        // we'll set errorState to true, which will disarm the creeper so that we don't accidentally
        // set off a real explosion.
        errorState = false;
        armingTimeRemaining = ARMING_TIME;

        try {
            // EntityCreeper.explosionRadius
            FieldUtils.writeField(this, "field_82226_g", 0, true);
        } catch (IllegalArgumentException | IllegalAccessException ignored) {
            errorState = true;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        armingTimeRemaining--;
        if (armingTimeRemaining < 0) {
            armingTimeRemaining = 0;
        }
    }

    @Override
    public int getCreeperState() {
        // If something went wrong, force override the creeper state to be negative to prevent
        // accidentally setting off a real explosion.
        if (armingTimeRemaining > 0 || errorState) {
            return -1;
        }

        return super.getCreeperState();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("armingTimeRemaining", armingTimeRemaining);
        tagCompound.setBoolean("errorState", errorState);
        // Force overwrite the explosion radius value from the super method, just in case.
        tagCompound.setByte("ExplosionRadius", (byte) 0);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound) {
        super.readEntityFromNBT(tagCompound);

        if (tagCompound.hasKey("armingTimeRemaining", 99)) {
            armingTimeRemaining = tagCompound.getInteger("armingTimeRemaining");
        }

        if (tagCompound.getBoolean("errorState") || tagCompound.getByte("ExplosionRadius") > 0) {
            errorState = true;
        }
    }
}
