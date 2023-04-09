package com.github.vfyjxf.justenoughgraphs.content;

import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.ContentTypes;
import net.minecraft.world.item.ItemStack;

public class ItemStackContent extends AbstractStackContent<ItemStack> {


    public ItemStackContent(ItemStack content, long amount, float chance) {
        super(content, amount, chance);
    }

    public ItemStackContent(ItemStack content, long amount) {
        super(content, amount);
    }

    @Override
    public ContentType<ItemStack> getType() {
        return ContentTypes.ITEM_STACK;
    }
}
