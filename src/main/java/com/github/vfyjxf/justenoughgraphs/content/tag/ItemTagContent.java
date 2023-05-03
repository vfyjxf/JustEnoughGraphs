package com.github.vfyjxf.justenoughgraphs.content.tag;

import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.ContentTypes;
import com.github.vfyjxf.justenoughgraphs.utils.CycleTimer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.List;

public class ItemTagContent extends AbstractTagContent<ItemStack, Item> {


    public static ItemTagContent create(ResourceLocation tag, long amount, float chance) {
        ITagManager<Item> tagManager = ForgeRegistries.ITEMS.tags();
        assert tagManager != null;
        TagKey<Item> tagKey = tagManager.createTagKey(tag);
        List<Item> contents = tagManager.getTag(tagKey).stream().toList();
        return new ItemTagContent(tagKey, contents, amount, chance);
    }

    protected ItemTagContent(TagKey<Item> tag, List<Item> contents, long amount, float chance) {
        super(tag, contents, amount, chance);
    }

    @Override
    public ContentType<ItemStack> getType() {
        return ContentTypes.ITEM_STACK;
    }

    @Override
    public ContentType<Item> getTagType() {
        return ContentTypes.ITEM_TAG;
    }

    @Override
    public ItemStack getContent() {
        return new ItemStack(CycleTimer.SHARD.getCycledValue(contents), (int) amount);
    }


}
