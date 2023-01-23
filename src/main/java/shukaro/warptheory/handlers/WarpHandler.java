package shukaro.warptheory.handlers;

import baubles.api.BaublesApi;
import gnu.trove.map.hash.THashMap;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.ITextComponent;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.handlers.warpevents.*;
import shukaro.warptheory.util.MiscHelper;
import shukaro.warptheory.util.NameMetaPair;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.items.IWarpingGear;
import thaumcraft.common.lib.events.PlayerEvents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class WarpHandler
{
    public static Map<String, Integer> warpNormal;
    public static Map<String, Integer> warpTemp;
    public static Map<String, Integer> warpPermanent;
    public static boolean wuss = false;
    public static int potionWarpWardID = -1;

    public static ArrayList<IWarpEvent> warpEvents = new ArrayList<IWarpEvent>();

    public static Map<NameMetaPair, NameMetaPair> decayMappings = new THashMap<NameMetaPair, NameMetaPair>();

    public static void addDecayMapping(Block start, Block end)
    {
        addDecayMapping(start, 0, end, 0);
    }

    public static void addDecayMapping(Block start, int startMeta, Block end, int endMeta)
    {
        decayMappings.put(new NameMetaPair(start, startMeta), new NameMetaPair(end, endMeta));
    }

    public static void addDecayMapping(Block start, int startMeta, Block end)
    {
        decayMappings.put(new NameMetaPair(start, startMeta), new NameMetaPair(end, 0));
    }

    public static void initEvents()
    {
    	if(ConfigHandler.allowWarpEffect1)
    		warpEvents.add(new WarpBats(ConfigHandler.minimumWarpForEffect1));
    	if(ConfigHandler.allowWarpEffect2)
    		warpEvents.add(new WarpBlink(ConfigHandler.minimumWarpForEffect2));
    	if(ConfigHandler.allowWarpEffect3)
    		warpEvents.add(new WarpBuff(ConfigHandler.minimumWarpForEffect3, "poison", new PotionEffect(MobEffects.POISON, 20 * 20)));
    	if(ConfigHandler.allowWarpEffect4)
    		warpEvents.add(new WarpBuff(ConfigHandler.minimumWarpForEffect4, "nausea", new PotionEffect(MobEffects.NAUSEA, 20 * 20)));
    	if(ConfigHandler.allowWarpEffect5)
    		warpEvents.add(new WarpBuff(ConfigHandler.minimumWarpForEffect5, "jump", new PotionEffect(MobEffects.JUMP_BOOST, 20 * 20, 20)));
    	if(ConfigHandler.allowWarpEffect6)
    		warpEvents.add(new WarpBuff(ConfigHandler.minimumWarpForEffect6, "blind", new PotionEffect(MobEffects.BLINDNESS, 20 * 20)));
    	if(ConfigHandler.allowGlobalWarpEffects && ConfigHandler.allowWarpEffect7)
    		warpEvents.add(new WarpDecay(ConfigHandler.minimumWarpForEffect7));
    	if(ConfigHandler.allowWarpEffect8)
    		warpEvents.add(new WarpEars(ConfigHandler.minimumWarpForEffect8));
    	if(ConfigHandler.allowGlobalWarpEffects && ConfigHandler.allowWarpEffect9)
    		warpEvents.add(new WarpSwamp(ConfigHandler.minimumWarpForEffect9));
    	if(ConfigHandler.allowWarpEffect10)
    		warpEvents.add(new WarpTongue(ConfigHandler.minimumWarpForEffect10));
    	if(ConfigHandler.allowWarpEffect11)
    		warpEvents.add(new WarpFriend(ConfigHandler.minimumWarpForEffect11));
    	if(ConfigHandler.allowWarpEffect12)
    		warpEvents.add(new WarpLivestockRain(ConfigHandler.minimumWarpForEffect12));
    	if(ConfigHandler.allowWarpEffect13)
    		warpEvents.add(new WarpWind(ConfigHandler.minimumWarpForEffect13));
    	if(ConfigHandler.allowWarpEffect14)
    		warpEvents.add(new WarpChests(ConfigHandler.minimumWarpForEffect14));
    	if(ConfigHandler.allowWarpEffect15)
    		warpEvents.add(new WarpBlood(ConfigHandler.minimumWarpForEffect15));
    	if(ConfigHandler.allowWarpEffect16)
    		warpEvents.add(new WarpAcceleration(ConfigHandler.minimumWarpForEffect16));
    	if(ConfigHandler.allowWarpEffect17)
    		warpEvents.add(new WarpLightning(ConfigHandler.minimumWarpForEffect17));
 //   	if(ConfigHandler.allowGlobalWarpEffects && ConfigHandler.allowWarpEffect18 && ConfigHandler.allowServerKickWarpEffects)
 //   		warpEvents.add(new WarpFall(ConfigHandler.minimumWarpForEffect18));
    	if(ConfigHandler.allowGlobalWarpEffects && ConfigHandler.allowWarpEffect19)
    		warpEvents.add(new WarpRain(ConfigHandler.minimumWarpForEffect19));
    	if(ConfigHandler.allowGlobalWarpEffects && ConfigHandler.allowWarpEffect20)
    		warpEvents.add(new WarpWither(ConfigHandler.minimumWarpForEffect20));
    	if(ConfigHandler.allowWarpEffect21)
    		warpEvents.add(new WarpFakeSound(ConfigHandler.minimumWarpForEffect21, "fakeexplosion", SoundEvents.ENTITY_GENERIC_EXPLODE, 8));
    	if(ConfigHandler.allowWarpEffect22)
    		warpEvents.add(new WarpFakeSoundBehind(ConfigHandler.minimumWarpForEffect22, "fakecreeper", "creeper.primed", 2));

        addDecayMapping(Blocks.GRASS, Blocks.DIRT);
        addDecayMapping(Blocks.DIRT, 0, Blocks.SAND);
        addDecayMapping(Blocks.DIRT, 1, Blocks.SAND);
        addDecayMapping(Blocks.DIRT, 2, Blocks.DIRT);
        addDecayMapping(Blocks.STONE, Blocks.COBBLESTONE);
        addDecayMapping(Blocks.COBBLESTONE, Blocks.GRAVEL);
        addDecayMapping(Blocks.SANDSTONE, Blocks.SAND);
        addDecayMapping(Blocks.GRAVEL, Blocks.SAND);
        addDecayMapping(Blocks.SAND, Blocks.AIR);
        addDecayMapping(Blocks.LAVA, Blocks.COBBLESTONE);
        addDecayMapping(Blocks.FLOWING_LAVA, Blocks.COBBLESTONE);
        addDecayMapping(Blocks.WATER, Blocks.AIR);
        addDecayMapping(Blocks.SNOW, Blocks.WATER);
        addDecayMapping(Blocks.SNOW_LAYER, Blocks.AIR);
        addDecayMapping(Blocks.ICE, Blocks.WATER);
        addDecayMapping(Blocks.CLAY, Blocks.SAND);
        addDecayMapping(Blocks.MYCELIUM, Blocks.GRASS);
        addDecayMapping(Blocks.STAINED_HARDENED_CLAY, Blocks.HARDENED_CLAY);
        addDecayMapping(Blocks.HARDENED_CLAY, Blocks.CLAY);
        addDecayMapping(Blocks.COAL_ORE, Blocks.STONE);
        addDecayMapping(Blocks.DIAMOND_ORE, Blocks.STONE);
        addDecayMapping(Blocks.EMERALD_ORE, Blocks.STONE);
        addDecayMapping(Blocks.GOLD_ORE, Blocks.STONE);
        addDecayMapping(Blocks.IRON_ORE, Blocks.STONE);
        addDecayMapping(Blocks.LAPIS_ORE, Blocks.STONE);
        addDecayMapping(Blocks.LIT_REDSTONE_ORE, Blocks.STONE);
        addDecayMapping(Blocks.REDSTONE_ORE, Blocks.STONE);
        addDecayMapping(Blocks.QUARTZ_ORE, Blocks.NETHERRACK);
        addDecayMapping(Blocks.NETHERRACK, Blocks.COBBLESTONE);
        addDecayMapping(Blocks.SOUL_SAND, Blocks.SAND);
        addDecayMapping(Blocks.GLOWSTONE, Blocks.COBBLESTONE);
        addDecayMapping(Blocks.LOG, Blocks.DIRT);
        addDecayMapping(Blocks.LOG2, Blocks.DIRT);
        addDecayMapping(Blocks.BROWN_MUSHROOM_BLOCK, Blocks.DIRT);
        addDecayMapping(Blocks.RED_MUSHROOM_BLOCK, Blocks.DIRT);
        addDecayMapping(Blocks.END_STONE, Blocks.COBBLESTONE);
        addDecayMapping(Blocks.OBSIDIAN, Blocks.COBBLESTONE);
    }

    @SuppressWarnings("unchecked")
    public static boolean tcReflect()
    {
        try
        {
            wuss = Class.forName("thaumcraft.common.config.Config").getField("wuss").getBoolean(null);
            potionWarpWardID = Class.forName("thaumcraft.common.config.Config").getField("potionWarpWardID").getInt(null);
        }
        catch (Exception e)
        {
            WarpTheory.logger.warn("Could not reflect into thaumcraft.common.Config to get config settings");
            e.printStackTrace();
        }
        try
        {
            Class tc = Class.forName("thaumcraft.common.Thaumcraft");
            Object proxy = tc.getField("proxy").get(null);
            Object pK = proxy.getClass().getField("playerKnowledge").get(proxy);
            warpNormal = (Map<String, Integer>)pK.getClass().getDeclaredField("warpSticky").get(pK);
            warpTemp = (Map<String, Integer>)pK.getClass().getField("warpTemp").get(pK);
            warpPermanent = (Map<String, Integer>)pK.getClass().getField("warp").get(pK);
        }
        catch (Exception e)
        {
            WarpTheory.logger.warn("Could not reflect into thaumcraft.common.Thaumcraft to get warpNormal mappings, attempting older reflection");
            e.printStackTrace();
            try
            {
                Class tc = Class.forName("thaumcraft.common.Thaumcraft");
                Object proxy = tc.getField("proxy").get(null);
                Object pK = proxy.getClass().getField("playerKnowledge").get(proxy);
                warpNormal = (Map<String, Integer>)pK.getClass().getDeclaredField("warp").get(pK);
                warpTemp = (Map<String, Integer>)pK.getClass().getField("warpTemp").get(pK);
            }
            catch (Exception x)
            {
                WarpTheory.logger.warn("Failed to reflect into thaumcraft.common.Thaumcraft to get warpNormal mapping");
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static void purgeWarp(EntityPlayer player)
    {
        queueMultipleEvents(player, getTotalWarp(player));
        removeWarp(player, getTotalWarp(player));
    }

    public static void removeWarp(EntityPlayer player, int amount)
    {
        if (amount <= 0)
            return;
        if ((ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.NORMAL) != 0 && ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.TEMPORARY) != 0) || tcReflect())
        {
            String name = player.getDisplayName().toString();
            int wp = ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.PERMANENT) != 0 ? ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.PERMANENT) : 0;
            int wn = ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.NORMAL);
            int wt = ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.TEMPORARY);
            if (amount <= wt)
            {
                ThaumcraftApi.internalMethods.addWarpToPlayer(player, - (wt - amount), IPlayerWarp.EnumWarpType.TEMPORARY);
                return;
            }
            else
            {
                ThaumcraftApi.internalMethods.addWarpToPlayer(player, - wt, IPlayerWarp.EnumWarpType.TEMPORARY);
                amount -= wt;
            }
            if (amount <= wn)
            {
                ThaumcraftApi.internalMethods.addWarpToPlayer(player, - (wn - amount), IPlayerWarp.EnumWarpType.NORMAL);
                return;
            }
            else
            {
                ThaumcraftApi.internalMethods.addWarpToPlayer(player, - wn, IPlayerWarp.EnumWarpType.NORMAL);
                amount -= wn;
            }
            if (ConfigHandler.allowPermWarpRemoval)
            {
                if ((int)Math.ceil(amount / ConfigHandler.permWarpMult) <= wp)
                    ThaumcraftApi.internalMethods.addWarpToPlayer(player, - (wp - (int)Math.ceil(amount / ConfigHandler.permWarpMult)), IPlayerWarp.EnumWarpType.PERMANENT);
                else
                    ThaumcraftApi.internalMethods.addWarpToPlayer(player, - wp, IPlayerWarp.EnumWarpType.PERMANENT);
            }
        }
    }

    public static int getTotalWarp(EntityPlayer player)
    {
        if (player == null)
            return 0;
        if ((ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.NORMAL) != 0 && ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.TEMPORARY) != 0) || tcReflect())
        {
            return ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.PERMANENT) + ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.NORMAL) + ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.TEMPORARY) +
                    getWarpFromGear(player);
        }
        return 0;
    }

    public static int[] getIndividualWarps(EntityPlayer player) {
        IPlayerWarp warp = ThaumcraftCapabilities.getWarp(player);
        int[] totals = new int[]{ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.PERMANENT), warp.get(IPlayerWarp.EnumWarpType.NORMAL), warp.get(IPlayerWarp.EnumWarpType.TEMPORARY)};
        return totals;
    }

    public static int queueMultipleEvents(EntityPlayer player, int amount)
    {
        int w = amount;
        while (w > 0)
        {
            IWarpEvent event = queueOneEvent(player, w);
            if (event == null)
                return w;
            w -= event.getCost();
        }
        return w;
    }

    public static IWarpEvent queueOneEvent(EntityPlayer player, int maxSeverity)
    {
        IWarpEvent event = getAppropriateEvent(player, maxSeverity);
        if (event != null)
            queueEvent(player, event);
        return event;
    }

    public static IWarpEvent getAppropriateEvent(EntityPlayer player, int maxSeverity)
    {
        ArrayList<IWarpEvent> shuffled = (ArrayList<IWarpEvent>)warpEvents.clone();
        Collections.shuffle(shuffled);
        for (IWarpEvent e : shuffled)
        {
            if (e.getSeverity() <= maxSeverity)
                return e;
        }
        return null;
    }

    private static int getWarpFromGear(EntityPlayer player) {
        int w = PlayerEvents.getFinalWarp(player.getHeldItemMainhand(), player);

        for(int a = 0; a < 4; ++a) {
            w += PlayerEvents.getFinalWarp((ItemStack)player.inventory.armorInventory.get(a), player);
        }

        IInventory baubles = BaublesApi.getBaubles(player);

        for(int a = 0; a < baubles.getSizeInventory(); ++a) {
            w += PlayerEvents.getFinalWarp(baubles.getStackInSlot(a), player);
        }

        return w;
    }

    public static IWarpEvent getEventFromName(String name)
    {
        for (IWarpEvent event : warpEvents)
        {
            if (event.getName().equals(name))
                return event;
        }
        return null;
    }

    public static void queueEvent(EntityPlayer player, IWarpEvent event)
    {
        String queue;
        if (!MiscHelper.getWarpTag(player).hasKey("queuedEvents"))
            queue = "";
        else
            queue = MiscHelper.getWarpTag(player).getString("queuedEvents");
        queue += event.getName() + " ";
        MiscHelper.getWarpTag(player).setString("queuedEvents", queue);
    }

    public static IWarpEvent dequeueEvent(EntityPlayer player)
    {
        String queue;
        if (!MiscHelper.getWarpTag(player).hasKey("queuedEvents"))
            queue = "";
        else
            queue = MiscHelper.getWarpTag(player).getString("queuedEvents");
        if (queue.length() > 0)
        {
            ArrayList<String> names = new ArrayList<String>();
            for (String n : queue.split(" "))
                names.add(n);
            Collections.shuffle(names);
            String todo = names.remove(player.world.rand.nextInt(names.size()));
            queue = "";
            for (String n : names)
                queue += n + " ";
            MiscHelper.getWarpTag(player).setString("queuedEvents", queue);
            return getEventFromName(todo);
        }
        return null;
    }
}
