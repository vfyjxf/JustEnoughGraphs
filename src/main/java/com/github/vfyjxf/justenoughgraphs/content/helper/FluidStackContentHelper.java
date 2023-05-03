package com.github.vfyjxf.justenoughgraphs.content.helper;

import com.github.vfyjxf.justenoughgraphs.api.content.IContentHelper;
import com.github.vfyjxf.justenoughgraphs.helper.TagHelper;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.Collection;

public class FluidStackContentHelper implements IContentHelper<FluidStack> {

    private final ForgeRegistry<Fluid> forgeRegistry;

    public FluidStackContentHelper() {
        this.forgeRegistry = RegistryManager.ACTIVE.getRegistry(Registry.FLUID_REGISTRY);
    }

    @Override
    public String getModId(FluidStack toGet) {
        Fluid fluid = toGet.getFluid();
        return forgeRegistry.getResourceKey(fluid)
                .map(ResourceKey::location)
                .map(ResourceLocation::getNamespace)
                .orElse("None");
    }

    @Override
    public CompoundTag serialize(FluidStack toSerialize) {
        return toSerialize.writeToNBT(new CompoundTag());
    }

    @Override
    public FluidStack deserialize(CompoundTag toDeserialize) {
        return FluidStack.loadFluidStackFromNBT(toDeserialize);
    }

    @Override
    public FluidStack copy(FluidStack toCopy) {
        return toCopy.copy();
    }

    @Override
    public long getAmount(FluidStack toGet) {
        return toGet.getAmount();
    }

    @Override
    public FluidStack setAmount(FluidStack toSet, long amount) {
        toSet.setAmount((int) amount);
        return toSet;
    }

    @Override
    public CompoundTag getTag(FluidStack toGet) {
        return toGet.getTag();
    }

    @Override
    public boolean matches(FluidStack first, FluidStack second) {
        return first.isFluidEqual(second) && FluidStack.areFluidStackTagsEqual(first, second);
    }

    @Override
    public boolean matchesFuzzy(FluidStack first, FluidStack second) {
        return first.getFluid() == second.getFluid();
    }

    @Override
    public boolean merge(FluidStack first, FluidStack second) {
        if (matches(first, second)) {
            first.setAmount(first.getAmount() + second.getAmount());
            return true;
        }
        return false;
    }

    @Override
    public boolean mergeFuzzy(FluidStack first, FluidStack second) {
        if (matchesFuzzy(first, second)) {
            first.setAmount(first.getAmount() + second.getAmount());
            return true;
        }
        return false;
    }

    @Override
    public Collection<ResourceLocation> getTags(Collection<FluidStack> collection) {
        return TagHelper.getTagEquivalents(collection, FluidStack::getFluid, ForgeRegistries.FLUIDS::tags);
    }
}
