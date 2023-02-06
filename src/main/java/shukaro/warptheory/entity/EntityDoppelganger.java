package shukaro.warptheory.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.FormatCodes;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.*;

public class EntityDoppelganger extends EntityCreature implements IHealable, IHurtable {
    /**
     * The number of ticks we will wait between each attempt to find our player.
     */
    protected static final int FIND_PLAYER_WAIT_TICKS = 20;

    /**
     * The number of ticks we will wait between each heal tick.
     */
    protected static final int HEAL_WAIT_TICKS = 40;

    protected static final DataParameter<String> UUID_DATA_WATCHER_ID = EntityDataManager.createKey(EntityDoppelganger.class, DataSerializers.STRING);
    protected static final String UUID_NBT_TAG = "playerUuid";

    // This will only be populated on the client.
    protected static final Map<UUID, GameProfile> gameProfileCache = new HashMap<>();

    protected int findPlayerWait;
    protected int healWait;

    // This will only be set on the server.
    protected WeakReference<EntityPlayer> player;

    public EntityDoppelganger(World world) {
        super(world);
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        tasks.addTask(3, new EntityAIWander(this, 0.8D));
        tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(5, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, false, true));

        findPlayerWait = 0;
        healWait = HEAL_WAIT_TICKS;
        player = new WeakReference<>(null);
    }

    /**
     * Should be called once shortly after construction, to initialize things like HP and name.
     */
    public void initialize(EntityPlayer player) {
        this.player = new WeakReference<>(player);
        this.getDataManager().set(UUID_DATA_WATCHER_ID, player.getUniqueID().toString());

        String name =
                I18n.translateToLocalFormatted("chat.warptheory.doppelganger.name", player.getName());
        this.setCustomNameTag(name);

        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(player.getMaxHealth());
        this.setHealth(player.getHealth());
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @SuppressWarnings("unchecked")
    public ResourceLocation getPlayerSkin() {
        String uuidString = getDataManager().get(UUID_DATA_WATCHER_ID);
        if (uuidString.isEmpty()) {
            return null;
        }
        UUID uuid = UUID.fromString(uuidString);

        GameProfile gameProfile = gameProfileCache.get(uuid);
        if (gameProfile == null) {
            gameProfile =
                    Minecraft.getMinecraft().getSessionService().fillProfileProperties(new GameProfile(uuid, null), true);
            gameProfileCache.put(uuid, gameProfile);
        }

        SkinManager skinManager = Minecraft.getMinecraft().getSkinManager();
        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textureMap = skinManager.loadSkinFromCache(gameProfile);

        if (textureMap.containsKey(MinecraftProfileTexture.Type.SKIN)) {
            return skinManager.loadSkin(
                    textureMap.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
        }

        return null;
    }

    @Override
    public void onHeal(LivingHealEvent e) {
        if (e.getAmount() <= 0.5f) {
            // The doppelgänger's passive regen also counts as healing, but we don't want to pass
            // that on to the player. So check for larger than 0.5f healing amount.
            return;
        }

        float currentDamage = getMaxHealth() - getHealth();
        float amount = Math.min(e.getAmount(), currentDamage);
        if (amount <= 0f) {
            return;
        }

        EntityPlayer entityPlayer = player.get();
        if (entityPlayer != null && entityPlayer.getHealth() < entityPlayer.getMaxHealth()) {
            entityPlayer.heal(amount);
            ChatHelper.sendToPlayer(
                    entityPlayer,
                    FormatCodes.Purple.code
                            + FormatCodes.Italic.code
                            + I18n.translateToLocal("chat.warptheory.doppelganger.heal"));
        }
    }

    @Override
    public void onHurt(LivingHurtEvent e) {
        EntityPlayer entityPlayer = player.get();
        if (entityPlayer != null) {
            DamageSource damageSource = DamageSource.causeIndirectMagicDamage(this, this);
            float damage = Math.min(e.getAmount(), getHealth());
            entityPlayer.attackEntityFrom(damageSource, damage);

            if (getHealth() > e.getAmount()) {
                ChatHelper.sendToPlayer(
                        entityPlayer,
                        FormatCodes.Purple.code
                                + FormatCodes.Italic.code
                                + I18n.translateToLocal("chat.warptheory.doppelganger.hurt"));
            } else {
                ChatHelper.sendToPlayer(
                        entityPlayer,
                        FormatCodes.Purple.code
                                + FormatCodes.Italic.code
                                + I18n.translateToLocal("chat.warptheory.doppelganger.die"));
            }
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataManager().register(UUID_DATA_WATCHER_ID, "");
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!world.isRemote) {
            String uuid = getDataManager().get(UUID_DATA_WATCHER_ID);
            if ((player.get() == null || player.get().isDead) && !uuid.isEmpty()) {
                if (findPlayerWait > 0) {
                    findPlayerWait--;
                } else {
                    findPlayerWait = FIND_PLAYER_WAIT_TICKS;

                    UUID uuidObj = UUID.fromString(uuid);
                    MinecraftServer server = this.world.getMinecraftServer();

                    List<EntityPlayerMP> players = new ArrayList<>();
                    if (server != null)
                        players.addAll(server.getPlayerList().getPlayers());
                    for (EntityPlayerMP entityPlayer : players) {
                        if (entityPlayer.getUniqueID().equals(uuidObj)) {
                            player = new WeakReference<>(entityPlayer);
                            return;
                        }
                    }
                }
            }
        }

        if (getHealth() < getMaxHealth()) {
            if (healWait > 0) {
                healWait--;
            } else {
                healWait = HEAL_WAIT_TICKS;
                // If you increase the healing amount here, you may also need to modify onHeal()
                // to prevent passing this healing on to the player.
                heal(0.5f);
            }
        } else {
            healWait = HEAL_WAIT_TICKS;
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25d);
    }

    @Override
    public boolean isAIDisabled() {
        return false;
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        return true;
    }

    @Override
    protected Item getDropItem() {
        return Items.ROTTEN_FLESH;
    }

    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int par1) {
        ItemStack head = new ItemStack(Items.SKULL, 1, 2);
        if (player.get() != null) {
            head.setItemDamage(3);
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("SkullOwner", String.valueOf(player.get().getName()));
            head.setTagCompound(nbt);
        }

        entityDropItem(head, 0.0f);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        String uuid = getDataManager().get(UUID_DATA_WATCHER_ID);
        if (!uuid.isEmpty()) {
            nbt.setString(UUID_NBT_TAG, uuid);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey(UUID_NBT_TAG)) {
            getDataManager().set(UUID_DATA_WATCHER_ID, player.get().getUniqueID().toString());
        }
    }
}