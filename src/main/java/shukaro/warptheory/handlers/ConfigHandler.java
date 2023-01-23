package shukaro.warptheory.handlers;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import shukaro.warptheory.util.Constants;

import java.io.File;
/**
 * Created by Ark on 3/7/2015.
 * code in part provided by pahimar and TheOldOne822
 */
public class ConfigHandler {

    public static Configuration config;
    public static boolean wussMode = false;
    public static int permWarpMult = 4;
    public static boolean allowPermWarpRemoval = true;
    public static boolean allowGlobalWarpEffects = false;
    public static boolean allowWarpEffects = false;
    public static boolean allowServerKickWarpEffects = false;
    public static boolean allowWarpEffect1 = false;
    public static boolean allowWarpEffect2 = false;
    public static boolean allowWarpEffect3 = false;
    public static boolean allowWarpEffect4 = false;
    public static boolean allowWarpEffect5 = false;
    public static boolean allowWarpEffect6 = false;
    public static boolean allowWarpEffect7 = false;
    public static boolean allowWarpEffect8 = false;
    public static boolean allowWarpEffect9 = false;
    public static boolean allowWarpEffect10 = false;
    public static boolean allowWarpEffect11 = false;
    public static boolean allowWarpEffect12 = false;
    public static boolean allowWarpEffect13 = false;
    public static boolean allowWarpEffect14 = false;
    public static boolean allowWarpEffect15 = false;
    public static boolean allowWarpEffect16 = false;
    public static boolean allowWarpEffect17 = false;
    public static boolean allowWarpEffect18 = false;
    public static boolean allowWarpEffect19 = false;
    public static boolean allowWarpEffect20 = false;
    public static boolean allowWarpEffect21 = false;
    public static boolean allowWarpEffect22 = false;

    public static int minimumWarpForEffect1 = 15;
    public static int minimumWarpForEffect2 = 30;
    public static int minimumWarpForEffect3 = 16;
    public static int minimumWarpForEffect4 = 25;
    public static int minimumWarpForEffect5 = 18;
    public static int minimumWarpForEffect6 = 43;
    public static int minimumWarpForEffect7 = 50;
    public static int minimumWarpForEffect8 = 12;
    public static int minimumWarpForEffect9 = 50;
    public static int minimumWarpForEffect10 = 11;
    public static int minimumWarpForEffect11 = 26;
    public static int minimumWarpForEffect12 = 32;
    public static int minimumWarpForEffect13 = 35;
    public static int minimumWarpForEffect14 = 35;
    public static int minimumWarpForEffect15 = 25;
    public static int minimumWarpForEffect16 = 27;
    public static int minimumWarpForEffect17 = 60;
    public static int minimumWarpForEffect18 = 36;
    public static int minimumWarpForEffect19 = 12;
    public static int minimumWarpForEffect20 = 70;
    public static int minimumWarpForEffect21 = 10;
    public static int minimumWarpForEffect22 = 10;
    
    public static void init(File configFile)
    {
        if (config == null) {
            // Create the config object from the given config file
            config = new Configuration(configFile);

            config.addCustomCategoryComment("general", "Change the inner workings of the mod requires restart");
            config.addCustomCategoryComment("warp_effects", "Toggle specific warp effect, If all disabled Pure Tear will not work");
            config.setCategoryRequiresMcRestart("general", true);

            loadConfiguration();

        }
    }

    private static void loadConfiguration()
    {
        wussMode = config.getBoolean("wussMode", "general", false, "enables less expensive recipes");
        permWarpMult = config.getInt("permWarpMult", "general", 4, 0, Integer.MAX_VALUE, "how much more 'expensive' permanent warp is compared to normal warp");
        allowPermWarpRemoval = config.getBoolean("allowPermWarpRemoval", "general", true, "whether items can remove permanent warp or not");
        allowGlobalWarpEffects = config.getBoolean("allowGlobalWarpEffects", "general", true, "whether warp effects that involve the environment are triggered");
        allowWarpEffects = config.getBoolean("addWarpEffects", "general", true, "whether to add general warp effects. If false extra effects will only be seen when using Pure Tear");
        allowServerKickWarpEffects = config.getBoolean("allowServerErrorWarpEffects", "general", false, "whether to allow warp effects known to cause errors on servers");
        allowWarpEffect1 = config.getBoolean("allowBatsEffect", "warp_effects", true, "whether to allow spawn bats warp effect. ");
        allowWarpEffect2 = config.getBoolean("allowBlinkEffect", "warp_effects", true, "whether to allow random teleport warp effect");
        allowWarpEffect3 = config.getBoolean("allowPoisonEffect", "warp_effects", true, "whether to allow poison warp effect");
        allowWarpEffect4 = config.getBoolean("allowNauseaEffect", "warp_effects", true, "whether to allow nausea warp effect");
        allowWarpEffect5 = config.getBoolean("allowJumpEffect", "warp_effects", true, "whether to allow jump boost warp effect");
        allowWarpEffect6 = config.getBoolean("allowBlindEffect", "warp_effects", true, "whether to allow blindness warp effect");
        allowWarpEffect7 = config.getBoolean("allowDecayEffect", "warp_effects", true, "whether to allow decay warp effect");
        allowWarpEffect8 = config.getBoolean("allowDeafEffect", "warp_effects", true, "whether to allow ears (unable to read messages) warp effect");
        allowWarpEffect9 = config.getBoolean("allowSwampEffect", "warp_effects", true, "whether to allow swamp (random trees) warp effect");
        allowWarpEffect10 = config.getBoolean("allowMuteEffect", "warp_effects", true, "whether to allow tongue (unable to send messages) warp effect");
        allowWarpEffect11 = config.getBoolean("allowFriendEffect", "warp_effects", true, "whether to allow friendly creeper warp effect");
        allowWarpEffect12 = config.getBoolean("allowLiveStockRainEffect", "warp_effects", true, "whether to allow livestock rain warp effect");
        allowWarpEffect13 = config.getBoolean("allowWindEffect", "warp_effects", true, "whether to allow wind warp effect");
        allowWarpEffect14 = config.getBoolean("allowChestEffect", "warp_effects", true, "whether to allow chest scramble warp effect");
        allowWarpEffect15 = config.getBoolean("allowBloodEffect", "warp_effects", true, "whether to allow blood warp effect");
        allowWarpEffect16 = config.getBoolean("allowAccelerationEffect", "warp_effects", true, "whether to allow acceleration warp effect");
        allowWarpEffect17 = config.getBoolean("allowLightningEffect", "warp_effects", true, "whether to allow lightning warp effect");
        allowWarpEffect18 = config.getBoolean("allowWorldHoleEffect", "warp_effects", false, "whether to allow world hole warp effect. may cause Server errors.");
        allowWarpEffect19 = config.getBoolean("allowRainEffect", "warp_effects", true, "whether to allow rain warp effect");
        allowWarpEffect20 = config.getBoolean("allowWitherSpawnEffect", "warp_effects", true, "whether to allow spawn wither warp effect");
        allowWarpEffect21 = config.getBoolean("allowFakeBoomEffect", "warp_effects", true, "whether to allow fake explosion warp effect");
        allowWarpEffect22 = config.getBoolean("allowFakeBoomerEffect", "warp_effects", true, "whether to allow fake creeper warp effect");

        minimumWarpForEffect1 = config.getInt("minWarpBatsEffect", "warp_levels", minimumWarpForEffect1, 1, 200, "Min warp required until spawn bats can happen");
        minimumWarpForEffect2 = config.getInt("minWarpBlinkEffect", "warp_levels", minimumWarpForEffect2, 1, 200, "Min warp required until random teleport can happen");
        minimumWarpForEffect3 = config.getInt("minWarpPoisonEffect", "warp_levels", minimumWarpForEffect3, 1, 200, "Min warp required until poison warp effect can happen");
        minimumWarpForEffect4 = config.getInt("minWarpNauseaEffect", "warp_levels", minimumWarpForEffect4, 1, 200, "Min warp required until nausea warp effect can happen");
        minimumWarpForEffect5 = config.getInt("minWarpJumpEffect", "warp_levels", minimumWarpForEffect5, 1, 200, "Min warp required until jump boost warp effect can happen");
        minimumWarpForEffect6 = config.getInt("minWarpBlindEffect", "warp_levels", minimumWarpForEffect6, 1, 200, "Min warp required until blindness warp effect can happen");
        minimumWarpForEffect7 = config.getInt("minWarpDecayEffect", "warp_levels", minimumWarpForEffect7, 1, 200, "Min warp required until decay warp effect can happen");
        minimumWarpForEffect8 = config.getInt("minWarpDeafEffect", "warp_levels", minimumWarpForEffect8, 1, 200, "Min warp required until ears effect can happen");
        minimumWarpForEffect9 = config.getInt("minWarpSwampEffect", "warp_levels", minimumWarpForEffect9, 1, 200, "Min warp required until random trees effect can happen");
        minimumWarpForEffect10 = config.getInt("minWarpMuteEffect", "warp_levels", minimumWarpForEffect10, 1, 200, "Min warp required until tongue (unable to send messages) warp effect can happen");
        minimumWarpForEffect11 = config.getInt("minWarpFriendEffect", "warp_levels", minimumWarpForEffect11, 1, 200, "Min warp required until friendly creeper warp effect can happen");
        minimumWarpForEffect12 = config.getInt("minWarpLiveStockRainEffect", "warp_levels", minimumWarpForEffect12, 1, 200, "Min warp required until livestock rain warp effect can happen");
        minimumWarpForEffect13 = config.getInt("minWarpWindEffect", "warp_levels", minimumWarpForEffect13, 1, 200, "Min warp required until wind warp effect can happen");
        minimumWarpForEffect14 = config.getInt("minWarpChestEffect", "warp_levels", minimumWarpForEffect14, 1, 200, "Min warp required until chest scramble warp effect can happen");
        minimumWarpForEffect15 = config.getInt("minWarpBloodEffect", "warp_levels", minimumWarpForEffect15, 1, 200, "Min warp required until blood warp effect can happen");
        minimumWarpForEffect16 = config.getInt("minWarpAccelerationEffect", "warp_levels", minimumWarpForEffect16, 1, 200, "Min warp required until acceleration warp effect can happen");
        minimumWarpForEffect17 = config.getInt("minWarpLightningEffect", "warp_levels", minimumWarpForEffect17, 1, 200, "Min warp required until lightning warp effect can happen");
        minimumWarpForEffect18 = config.getInt("minWarpWorldHoleEffect", "warp_levels", minimumWarpForEffect18, 1, 200, "Min warp required until world hole warp effect can happen");
        minimumWarpForEffect19 = config.getInt("minWarpRainEffect", "warp_levels", minimumWarpForEffect19, 1, 200, "Min warp required until rain warp effect can happen");
        minimumWarpForEffect20 = config.getInt("minWarpWitherSpawnEffect", "warp_levels", minimumWarpForEffect20, 1, 200, "Min warp required until spawn wither warp effect can happen");
        minimumWarpForEffect21 = config.getInt("minWarpFakeBoomEffect", "warp_levels", minimumWarpForEffect21, 1, 200, "Min warp required until fake explosion warp effect can happen");
        minimumWarpForEffect22 = config.getInt("minWarpFakeBoomerEffect", "warp_levels", minimumWarpForEffect22, 1, 200, "Min warp required until fake creeper warp effect can happen");
        
        if (config.hasChanged())
            config.save();
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(Constants.modID)) {
            loadConfiguration();
        }
    }
}