package com.github.vfyjxf.justenoughgraphs.api.content;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IItemTagContent extends IContent<ItemStack> {

    TagKey<Item> getTag();

    List<Item> getContents();

}
