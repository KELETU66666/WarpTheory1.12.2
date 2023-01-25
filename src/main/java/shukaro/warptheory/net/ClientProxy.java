package shukaro.warptheory.net;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import shukaro.warptheory.entity.EntityDoppelganger;
import shukaro.warptheory.entity.RenderDoppelganger;
import shukaro.warptheory.handlers.ConfigHandler;
import shukaro.warptheory.items.WarpItems;
import shukaro.warptheory.util.Constants;

public class ClientProxy extends CommonProxy
{
    public void init()
    {
        super.init();

        RenderingRegistry.registerEntityRenderingHandler(EntityDoppelganger.class, new RenderDoppelganger());
        //RenderingRegistry.registerEntityRenderingHandler(EntityPhantom.class, new RenderPhantom());
    }

    @Override
    public EntityPlayer getPlayer()
    {
        return Minecraft.getMinecraft().player;
    }

    @Override
    public void Renders(){
        if(ConfigHandler.useNewTextures)
            ModelLoader.setCustomModelResourceLocation(WarpItems.itemAmulet, 0, new ModelResourceLocation(Constants.modID + ":" + "item_cleansing_amulet_new", "inventory"));
        else
            ModelLoader.setCustomModelResourceLocation(WarpItems.itemAmulet, 0, new ModelResourceLocation(WarpItems.itemAmulet.getRegistryName(), "inventory"));
        if(ConfigHandler.useNewTextures)
            ModelLoader.setCustomModelResourceLocation(WarpItems.itemCleanser, 0, new ModelResourceLocation(Constants.modID + ":" + "item_cleanser_new", "inventory"));
        else
            ModelLoader.setCustomModelResourceLocation(WarpItems.itemCleanser, 0, new ModelResourceLocation(WarpItems.itemCleanser.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(WarpItems.itemSomething, 0, new ModelResourceLocation(WarpItems.itemSomething.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(WarpItems.itemPaper, 0, new ModelResourceLocation(WarpItems.itemPaper.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(WarpItems.itemCleanserMinor, 0, new ModelResourceLocation(WarpItems.itemCleanserMinor.getRegistryName(), "inventory"));
    }
}
