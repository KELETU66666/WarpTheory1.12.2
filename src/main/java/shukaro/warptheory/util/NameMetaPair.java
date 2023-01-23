package shukaro.warptheory.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class NameMetaPair
{
    private ResourceLocation name;
    private int meta;

    public NameMetaPair(Item item, int meta)
    {
        this(Item.REGISTRY.getNameForObject(item), meta);
    }

    public NameMetaPair(Block block, int meta)
    {
        this(Block.REGISTRY.getNameForObject(block), meta);
    }

    public NameMetaPair(ResourceLocation name, int meta)
    {
        this.name = name;
        this.meta = meta;
    }

    public ResourceLocation getName() { return this.name; }

    public void setName(ResourceLocation name) { this.name = name; }

    public Block getBlock()
    {
        return (Block)Block.REGISTRY.getObject(name);
    }

    public Item getItem()
    {
        return (Item)Item.REGISTRY.getObject(name);
    }

    public int getMetadata()
    {
        return meta;
    }

    public void setMeta(int meta) { this.meta = meta; }

    public ItemStack getStack()
    {
        if (isValidBlock())
            return new ItemStack(getBlock(), 1, getMetadata());
        else if (isValidItem())
            return new ItemStack(getItem(), 1, getMetadata());
        else
            return null;
    }

    public boolean isValidBlock()
    {
        return getBlock() != null && (meta >= 0 && meta < 65536);
    }

    public boolean isValidItem() { return getItem() != null && (meta >= 0 && meta < 65536); }

    public int hashCode()
    {
        return name.hashCode() | meta;
    }

    public boolean equals(Object o)
    {
        if (o == null || !(o instanceof NameMetaPair)) return false;
        NameMetaPair imp = (NameMetaPair)o;
        return this.name.equals(imp.name) && this.meta == imp.meta;
    }

    public boolean equals(NameMetaPair o)
    {
        return this.name.equals(o.name) && this.meta == o.meta;
    }

    public String toString()
    {
        return this.name + "@" + this.meta;
    }
}
