package shukaro.warptheory;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.logging.log4j.Logger;
import shukaro.warptheory.entity.EntityDoppelganger;
import shukaro.warptheory.entity.EntityFakeCreeper;
import shukaro.warptheory.entity.EntityPassiveCreeper;
import shukaro.warptheory.gui.WarpTab;
import shukaro.warptheory.handlers.ConfigHandler;
import shukaro.warptheory.handlers.WarpEventHandler;
import shukaro.warptheory.handlers.WarpHandler;
import shukaro.warptheory.init.InitRecipes;
import shukaro.warptheory.init.InitResearch;
import shukaro.warptheory.items.WarpItems;
import shukaro.warptheory.net.CommonProxy;
import shukaro.warptheory.util.Constants;
import shukaro.warptheory.util.NameGenerator;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

@Mod(modid = Constants.modID, name = Constants.modName, version = Constants.modVersion,
        dependencies = "required-after:thaumcraft")
public class WarpTheory {
    @SidedProxy(clientSide = "shukaro.warptheory.net.ClientProxy", serverSide = "shukaro.warptheory.net.CommonProxy")
    public static CommonProxy proxy;

    public static Logger logger;

    public static CreativeTabs mainTab = new WarpTab(I18n.translateToLocal("warptheory.tab"));

    public static NameGenerator normalNames;

    @Mod.Instance(Constants.modID)
    public static WarpTheory instance;

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent evt) {
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        try {
            File folder = new File(event.getModConfigurationDirectory() + "/warptheory/");
            if (!folder.exists())
                folder.mkdirs();
            File normalFile = new File(event.getModConfigurationDirectory() + "/warptheory/normal.txt");
            if (!normalFile.exists()) {
                InputStream in = WarpTheory.class.getResourceAsStream("/assets/warptheory/names/normal.txt");
                Files.copy(in, normalFile.getAbsoluteFile().toPath());
            }
            normalNames = new NameGenerator(normalFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ConfigHandler.init(event.getSuggestedConfigurationFile());

        FMLCommonHandler.instance().bus().register(new ConfigHandler());

        //WarpBlocks.initBlocks();

        WarpItems.initItems();

        proxy.Renders();

        if (ConfigHandler.allowWarpEffects)
            MinecraftForge.EVENT_BUS.register(new WarpEventHandler());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt) {
        WarpHandler.initEvents();
        InitRecipes.initRecipes();
        InitResearch.registerResearch();
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.modID + ":" + "passive_creeper"), EntityPassiveCreeper.class, "creeperPassive", 0, this, 160, 4, true);
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.modID + ":" + "fake_creeper"), EntityFakeCreeper.class, "creeperFake", 1, this, 160, 4, true);
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.modID + ":" + "doppelganger"), EntityDoppelganger.class, "doppelganger", 2, this, 160, 4, true);
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
    }
}
