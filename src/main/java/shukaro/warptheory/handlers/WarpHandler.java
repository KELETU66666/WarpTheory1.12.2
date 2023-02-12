package shukaro.warptheory.handlers;

import baubles.api.BaublesApi;
import gnu.trove.map.hash.THashMap;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import shukaro.warptheory.WarpTheory;
import shukaro.warptheory.util.ChatHelper;
import shukaro.warptheory.util.MiscHelper;
import shukaro.warptheory.util.NameMetaPair;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.common.lib.events.PlayerEvents;

import java.util.*;

public class WarpHandler
{
    public static boolean wuss = false;
    private static HashMap<UUID, Integer> Unavoidable = new HashMap<UUID, Integer>();
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
        Arrays.stream(WarpEventRegistry.values()).forEach(warpEvent -> warpEvent.createWarpEvent(warpEvents::add));

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

    public static void purgeWarp(EntityPlayer player) {
        int count = queueMultipleEvents(player, getTotalWarp(player));
        addUnavoidableCount(player, count);
        removeWarp(player, getTotalWarp(player));
    }

    public static void purgeWarpMinor(EntityPlayer player) {
        int[] warp = getIndividualWarps(player);
        if (warp[0] + warp[1] + warp[2] >= 50) {
            removeWarp(player, 5);
            ChatHelper.sendToPlayer(player, I18n.translateToLocal("chat.warptheory.purgeminor"));
        } else ChatHelper.sendToPlayer(player, I18n.translateToLocal("chat.warptheory.purgefailed"));
    }

    public static void removeWarp(EntityPlayer player, int amount)
    {
        if (amount <= 0)
            return;
        if (ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.TEMPORARY) + ThaumcraftApi.internalMethods.getActualWarp(player) != 0)
        {
            int wp = ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.PERMANENT);
            int wn = ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.NORMAL);
            int wt = ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.TEMPORARY);
            if (amount <= wt)
            {
                ThaumcraftApi.internalMethods.addWarpToPlayer(player, - amount, IPlayerWarp.EnumWarpType.TEMPORARY);
                return;
            }
            else
            {
                ThaumcraftApi.internalMethods.addWarpToPlayer(player, - wt, IPlayerWarp.EnumWarpType.TEMPORARY);
                amount -= wt;
            }
            if (amount <= wn)
            {
                ThaumcraftApi.internalMethods.addWarpToPlayer(player, - amount, IPlayerWarp.EnumWarpType.NORMAL);
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
        if ((ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.TEMPORARY) + ThaumcraftApi.internalMethods.getActualWarp(player) != 0))
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

    public static void setUnavoidableCount(EntityPlayer player, int count) {
        if (ConfigHandler.disableRebound) return;
        UUID uuid = player.getUniqueID();
        Unavoidable.put(uuid, Math.max(0, count));
    }

    public static void addUnavoidableCount(EntityPlayer player, int count) {
        if (ConfigHandler.disableRebound) return;
        UUID uuid = player.getUniqueID();
        if (!Unavoidable.containsKey(uuid)) Unavoidable.put(uuid, 0);
        count = Math.max(0, count + Unavoidable.get(uuid));
        Unavoidable.put(uuid, count);
    }

    public static int getUnavoidableCount(EntityPlayer player) {
        if (ConfigHandler.disableRebound) return 0;
        UUID uuid = player.getUniqueID();
        if (!Unavoidable.containsKey(uuid)) Unavoidable.put(uuid, 0);
        return Unavoidable.get(uuid);
    }
}
