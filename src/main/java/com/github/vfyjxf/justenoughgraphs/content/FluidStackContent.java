package com.github.vfyjxf.justenoughgraphs.content;

import com.github.vfyjxf.justenoughgraphs.api.content.ContentType;
import com.github.vfyjxf.justenoughgraphs.api.content.ContentTypes;
import net.minecraftforge.fluids.FluidStack;

public class FluidStackContent extends AbstractStackContent<FluidStack> {


    public FluidStackContent(FluidStack content, long amount, float chance) {
        super(content, amount, chance);
    }

    public FluidStackContent(FluidStack content, long amount) {
        super(content, amount);
    }

    @Override
    public ContentType<FluidStack> getType() {
        return ContentTypes.FLUID_STACK;
    }
}
