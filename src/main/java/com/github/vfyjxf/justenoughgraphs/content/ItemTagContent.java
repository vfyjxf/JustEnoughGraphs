package com.github.vfyjxf.justenoughgraphs.content;

import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.ContentTypes;
import com.github.vfyjxf.justenoughgraphs.api.content.IItemTagContent;
import com.github.vfyjxf.justenoughgraphs.utils.CycleTimer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.ArrayList;
import java.util.List;

public class ItemTagContent implements IItemTagContent {

    private final TagKey<Item> tag;
    private final long amount;
    private final float chance;
    private final List<Item> contents;

    public ItemTagContent(ResourceLocation tag, long amount, float chance) {
        ITagManager<Item> tagManager = ForgeRegistries.ITEMS.tags();
        this.tag = tagManager.createTagKey(tag);
        this.amount = amount;
        this.chance = chance;
        this.contents = tagManager.getTag(this.tag).stream().toList();
    }

    @Override
    public ContentType<ItemStack> getType() {
        return ContentTypes.ITEM_STACK;
    }

    @Override
    public ItemStack getContent() {
        Item current = CycleTimer.SHARD.getCycledValue(getContents());
        return new ItemStack(current, (int) getAmount());
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public float getChance() {
        return chance;
    }

    @Override
    public TagKey<Item> getTag() {
        return tag;
    }

    @Override
    public List<Item> getContents() {
        return new ArrayList<>(contents);
    }
}
