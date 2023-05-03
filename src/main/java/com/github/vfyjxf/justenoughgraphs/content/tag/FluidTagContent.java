package com.github.vfyjxf.justenoughgraphs.content.tag;

import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.ContentTypes;
import com.github.vfyjxf.justenoughgraphs.utils.CycleTimer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.List;

public class FluidTagContent extends AbstractTagContent<FluidStack, Fluid> {

    public static FluidTagContent create(ResourceLocation tag, long amount, float chance) {
        ITagManager<Fluid> tagManager = ForgeRegistries.FLUIDS.tags();
        assert tagManager != null;
        TagKey<Fluid> tagKey = tagManager.createTagKey(tag);
        List<Fluid> contents = tagManager.getTag(tagKey).stream().toList();
        return new FluidTagContent(tagKey, contents, amount, chance);
    }

    protected FluidTagContent(TagKey<Fluid> tag, List<Fluid> contents, long amount, float chance) {
        super(tag, contents, amount, chance);
    }

    @Override
    public ContentType<FluidStack> getType() {
        return ContentTypes.FLUID_STACK;
    }

    @Override
    public ContentType<Fluid> getTagType() {
        return ContentTypes.FLUID_TAG;
    }

    @Override
    public FluidStack getContent() {
        return new FluidStack(CycleTimer.SHARD.getCycledValue(contents), (int) amount);
    }
}
