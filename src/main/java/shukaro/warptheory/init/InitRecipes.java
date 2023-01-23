package shukaro.warptheory.init;


import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import shukaro.warptheory.items.WarpItems;
import shukaro.warptheory.util.Constants;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.crafting.IngredientNBTTC;
import thaumcraft.api.crafting.ShapedArcaneRecipe;
import thaumcraft.api.items.ItemsTC;

import static shukaro.warptheory.items.WarpItems.itemPaper;

public class InitRecipes {
    private static ResourceLocation defaultGroup = new ResourceLocation("");

    public static void initRecipes() {
        initArcaneRecipes();
        initCrucibleRecipes();
        initInfusionRecipes();
    }

private static void initArcaneRecipes() {
    ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(Constants.modName, "warp_paper"), new ShapedArcaneRecipe(
            defaultGroup,
            "WARP_PAPER",
            30,
            new AspectList(),
            new ItemStack(itemPaper,3, 0),
            "PSP",
            'P', new ItemStack(Items.PAPER),
            'S', new ItemStack(ItemsTC.salisMundus)));
}

private static void initCrucibleRecipes() {
    ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(Constants.modName, "taint_meat1"), new CrucibleRecipe(
            "TAINT_MEAT",
            new ItemStack(WarpItems.itemSomething),
            new ItemStack(Items.CHICKEN),
            new AspectList().add(Aspect.FLUX, 20).add(Aspect.EXCHANGE, 10)
    ));
    ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(Constants.modName, "taint_meat2"), new CrucibleRecipe(
            "TAINT_MEAT",
            new ItemStack(WarpItems.itemSomething),
            new ItemStack(Items.PORKCHOP),
            new AspectList().add(Aspect.FLUX, 20).add(Aspect.EXCHANGE, 10)
    ));
    ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(Constants.modName, "taint_meat3"), new CrucibleRecipe(
            "TAINT_MEAT",
            new ItemStack(WarpItems.itemSomething),
            new ItemStack(Items.BEEF),
            new AspectList().add(Aspect.FLUX, 20).add(Aspect.EXCHANGE, 10)
    ));
    ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(Constants.modName, "taint_meat4"), new CrucibleRecipe(
            "TAINT_MEAT",
            new ItemStack(WarpItems.itemSomething),
            new ItemStack(Items.MUTTON),
            new AspectList().add(Aspect.FLUX, 20).add(Aspect.EXCHANGE, 10)
    ));
    ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(Constants.modName, "taint_meat5"), new CrucibleRecipe(
            "TAINT_MEAT",
            new ItemStack(WarpItems.itemSomething),
            new ItemStack(Items.RABBIT),
            new AspectList().add(Aspect.FLUX, 20).add(Aspect.EXCHANGE, 10)
    ));
    ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(Constants.modName, "taint_meat6"), new CrucibleRecipe(
            "TAINT_MEAT",
            new ItemStack(WarpItems.itemSomething),
            new ItemStack(Items.FISH),
            new AspectList().add(Aspect.FLUX, 20).add(Aspect.EXCHANGE, 10)
    ));}
private static void initInfusionRecipes() {

    ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Constants.modName, "item_cleanser"), new InfusionRecipe(
            "PURE_TEAR",
            new ItemStack(WarpItems.itemCleanser),
            4,
            new AspectList().add(Aspect.EXCHANGE, 60).add(Aspect.ELDRITCH, 60),
            new ItemStack(Items.NETHER_STAR),
            new ItemStack(Items.GHAST_TEAR),
            "gemQuartz",
            "gemDiamond",
            new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("thaumcraft", "salis_mundus"))),
            new ItemStack(Items.GHAST_TEAR),
            "gemQuartz",
            "gemDiamond",
            new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("thaumcraft", "salis_mundus")))));


    ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(Constants.modName, "cleansing_amulet"), new InfusionRecipe(
            "CLEANSING_AMULET",
            new ItemStack(WarpItems.itemAmulet),
            8,
            new AspectList().add(Aspect.EXCHANGE, 125).add(Aspect.ELDRITCH, 125).add(Aspect.MAGIC, 250),
            new ItemStack(ItemsTC.baubles, 1, 0),
            new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("warptheory", "item_cleanser"))),
            "ingotGold",
            "gemDiamond",
            new ItemStack(ItemsTC.salisMundus),
            new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("warptheory", "item_cleanser"))),
            "ingotGold",
            "gemDiamond",
            new ItemStack(ItemsTC.salisMundus)));
}
}
