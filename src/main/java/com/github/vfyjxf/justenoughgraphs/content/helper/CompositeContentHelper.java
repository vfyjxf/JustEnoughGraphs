package com.github.vfyjxf.justenoughgraphs.content.helper;

import com.github.vfyjxf.justenoughgraphs.api.content.ICompositeContent;
import com.github.vfyjxf.justenoughgraphs.api.content.IContentHelper;
import com.github.vfyjxf.justenoughgraphs.api.content.IdentifierContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class CompositeContentHelper implements IContentHelper<ICompositeContent> {
    @Override
    public String getModId(ICompositeContent toGet) {
        return null;
    }

    @Override
    public String getIdentifier(ICompositeContent toGet, IdentifierContext type) {
        return null;
    }

    @Override
    public CompoundTag serialize(ICompositeContent toSerialize) {
        return null;
    }

    @Override
    public @Nullable ICompositeContent deserialize(CompoundTag toDeserialize) {
        return null;
    }

    @Override
    public ICompositeContent copy(ICompositeContent toCopy) {
        return null;
    }

    @Override
    public long getAmount(ICompositeContent toGet) {
        return 0;
    }

    @Override
    public ICompositeContent setAmount(ICompositeContent toSet, long amount) {
        return null;
    }

    @Override
    public @Nullable CompoundTag getTag(ICompositeContent toGet) {
        return null;
    }

    @Override
    public boolean matches(ICompositeContent first, ICompositeContent second) {
        return false;
    }

    @Override
    public boolean matchesFuzzy(ICompositeContent first, ICompositeContent second) {
        return false;
    }

    @Override
    public boolean merge(ICompositeContent first, ICompositeContent second) {
        return false;
    }

    @Override
    public boolean mergeFuzzy(ICompositeContent first, ICompositeContent second) {
        return false;
    }

    @Override
    public Collection<ResourceLocation> getTags(Collection<ICompositeContent> collection) {
        return null;
    }
}
