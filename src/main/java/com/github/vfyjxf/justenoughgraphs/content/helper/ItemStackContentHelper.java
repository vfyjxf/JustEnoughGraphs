package com.github.vfyjxf.justenoughgraphs.content.helper;

import com.github.vfyjxf.justenoughgraphs.api.content.IContentHelper;
import com.github.vfyjxf.justenoughgraphs.helper.TagHelper;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.Collection;

public class ItemStackContentHelper implements IContentHelper<ItemStack> {

    private final ForgeRegistry<Item> forgeRegistry;

    public ItemStackContentHelper() {
        this.forgeRegistry = RegistryManager.ACTIVE.getRegistry(Registry.ITEM_REGISTRY);
    }

    @Override
    public String getModId(ItemStack toGet) {
        Item item = toGet.getItem();
        String modId = item.getCreatorModId(toGet);
        if (modId == null) {
            modId = forgeRegistry.getResourceKey(item)
                    .map(ResourceKey::location)
                    .map(ResourceLocation::getNamespace)
                    .orElse("None");
        }
        return modId;
    }

    @Override
    public CompoundTag serialize(ItemStack toSerialize) {
        return toSerialize.save(new CompoundTag());
    }

    @Override
    public ItemStack deserialize(CompoundTag toDeserialize) {
        return ItemStack.of(toDeserialize);
    }

    @Override
    public ItemStack copy(ItemStack toCopy) {
        return toCopy.copy();
    }

    @Override
    public long getAmount(ItemStack toGet) {
        return toGet.getCount();
    }

    @Override
    public ItemStack setAmount(ItemStack toSet, long amount) {
        toSet.setCount((int) amount);
        return toSet;
    }

    @Override
    public CompoundTag getTag(ItemStack toGet) {
        return toGet.getTag();
    }

    @Override
    public boolean matches(ItemStack first, ItemStack second) {
        return ItemStack.isSameItemSameTags(first, second);
    }

    @Override
    public boolean matchesFuzzy(ItemStack first, ItemStack second) {
        return first.sameItemStackIgnoreDurability(second);
    }

    @Override
    public boolean merge(ItemStack first, ItemStack second) {
        if (ItemStack.isSameItemSameTags(first, second)) {
            first.grow(second.getCount());
            return true;
        }
        return false;
    }

    @Override
    public boolean mergeFuzzy(ItemStack first, ItemStack second) {
        if (ItemStack.isSameIgnoreDurability(first, second)) {
            first.grow(second.getCount());
            return true;
        }
        return false;
    }

    @Override
    public Collection<ResourceLocation> getTags(Collection<ItemStack> collection) {
        return TagHelper.getTagEquivalents(collection, ItemStack::getItem, ForgeRegistries.ITEMS::tags);
    }
}
