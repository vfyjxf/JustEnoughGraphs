package com.github.vfyjxf.justenoughgraphs.api.content;

import com.github.vfyjxf.justenoughgraphs.api.Globals;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.math.BigInteger;
import java.util.List;

public class ContentTypes {

    public static final ContentType<ItemStack> ITEM_STACK = ContentType.create(Globals.MOD_ID, "item_stack", ItemStack.class);
    public static final ContentType<Item> ITEM_TAG = ContentType.create(Globals.MOD_ID, "item_tag", Item.class);
    public static final ContentType<FluidStack> FLUID_STACK = ContentType.create(Globals.MOD_ID, "fluid_stack", FluidStack.class);
    public static final ContentType<Fluid> FLUID_TAG = ContentType.create(Globals.MOD_ID, "fluid_tag", Fluid.class);
    public static final ContentType<List> COMPOSITE = ContentType.create(Globals.MOD_ID, "composite", List.class);
    public static final ContentType<List> LIST = ContentType.create(Globals.MOD_ID, "list", List.class);

    public static final ContentType<Long> NUMERICAL_ENERGY_LONG = ContentType.create(Globals.MOD_ID, "numerical_energy_long", Long.class);
    public static final ContentType<BigInteger> NUMERICAL_ENERGY_BIGINTEGER = ContentType.create(Globals.MOD_ID, "numerical_energy_big_integer", BigInteger.class);
    public static final ContentType<Long> NUMERICAL_TIME = ContentType.create(Globals.MOD_ID, "numerical_time", Long.class);

}
