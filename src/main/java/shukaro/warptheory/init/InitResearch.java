package shukaro.warptheory.init;

import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;

public class InitResearch {
    public static void registerResearch() {

        ResearchCategories.registerCategory("WARPTHEORY", "WARP",
                new AspectList(),
                new ResourceLocation("warptheory", "textures/items/r_itemcleanser.png"),
                new ResourceLocation("thaumcraft", "textures/gui/gui_research_back_4.jpg"),
                new ResourceLocation("thaumcraft", "textures/gui/gui_research_back_over.png"));
        ThaumcraftApi.registerResearchLocation(new ResourceLocation("warptheory", "research/warptheory.json"));

    }
}
