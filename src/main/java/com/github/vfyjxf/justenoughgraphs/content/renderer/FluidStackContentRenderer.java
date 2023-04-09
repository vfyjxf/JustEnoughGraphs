package com.github.vfyjxf.justenoughgraphs.content.renderer;

import com.github.vfyjxf.justenoughgraphs.api.content.IContentRenderer;
import com.github.vfyjxf.justenoughgraphs.helper.DrawHelper;
import com.github.vfyjxf.justenoughgraphs.helper.TranslationHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class FluidStackContentRenderer implements IContentRenderer<FluidStack> {

    public static final Logger LOGGER = LogManager.getLogger();
    private static final NumberFormat FORMAT = NumberFormat.getIntegerInstance();

    @Override
    public void render(PoseStack poseStack, FluidStack content) {
        DrawHelper.drawFluidForGui(poseStack, content, 1000, 0, 0, 16, 16);
    }

    @Override
    public String getDisplayName(FluidStack content) {
        return content.getDisplayName().getString();
    }

    @Override
    public List<Component> getTooltip(FluidStack content, TooltipFlag tooltipFlag) {
        Fluid fluid = content.getFluid();
        try {
            if (fluid.isSame(Fluids.EMPTY)) {
                return new ArrayList<>();
            }

            List<Component> tooltip = tooltipHelper(content, tooltipFlag);

            long milliBuckets = (content.getAmount() * 1000L) / FluidType.BUCKET_VOLUME;

            MutableComponent amountString = TranslationHelper.translatable("tooltip.liquid.amount.with.capacity", FORMAT.format(milliBuckets), FORMAT.format(FluidType.BUCKET_VOLUME));
            tooltip.add(amountString.withStyle(ChatFormatting.GRAY));
            return tooltip;
        } catch (RuntimeException e) {
            Component displayName = content.getDisplayName();
            LOGGER.error("Failed to get tooltip for fluid: " + displayName.getString(), e);
        }

        return new ArrayList<>();
    }

    private static List<Component> tooltipHelper(FluidStack content, TooltipFlag tooltipFlag) {
        List<Component> tooltip = new ArrayList<>();
        Fluid fluid = content.getFluid();
        if (fluid.isSame(Fluids.EMPTY)) {
            return tooltip;
        }

        tooltip.add(content.getDisplayName());

        if (tooltipFlag.isAdvanced()) {
            ResourceLocation resourceLocation = ForgeRegistries.FLUIDS.getKey(fluid);
            if (resourceLocation != null) {
                MutableComponent advancedId = Component.literal(resourceLocation.toString())
                        .withStyle(ChatFormatting.DARK_GRAY);
                tooltip.add(advancedId);
            }
        }

        return tooltip;
    }

    @Override
    public Font getFontRenderer(Minecraft minecraft, FluidStack content) {
        return IContentRenderer.super.getFontRenderer(minecraft, content);
    }

}
