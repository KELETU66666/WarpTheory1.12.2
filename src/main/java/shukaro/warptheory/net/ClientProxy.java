package shukaro.warptheory.net;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.model.ModelLoader;
import shukaro.warptheory.items.WarpItems;

public class ClientProxy extends CommonProxy
{
    public void init()
    {
        super.init();
    }

    @Override
    public EntityPlayer getPlayer()
    {
        return Minecraft.getMinecraft().player;
    }

    @Override
    public void Renders(){
        ModelLoader.setCustomModelResourceLocation(WarpItems.itemAmulet, 0, new ModelResourceLocation(WarpItems.itemAmulet.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(WarpItems.itemCleanser, 0, new ModelResourceLocation(WarpItems.itemCleanser.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(WarpItems.itemSomething, 0, new ModelResourceLocation(WarpItems.itemSomething.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(WarpItems.itemPaper, 0, new ModelResourceLocation(WarpItems.itemPaper.getRegistryName(), "inventory"));
    }
}
