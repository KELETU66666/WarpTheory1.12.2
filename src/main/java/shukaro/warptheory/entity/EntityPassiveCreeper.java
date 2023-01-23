package shukaro.warptheory.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.World;

public class EntityPassiveCreeper extends EntityCreeper
{
    public EntityPassiveCreeper(World world)
    {
        super(world);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
    }

    @Override
    public int getCreeperState() { return -1; }
}
