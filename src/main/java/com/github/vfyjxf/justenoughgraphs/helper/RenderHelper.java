package com.github.vfyjxf.justenoughgraphs.helper;

import com.github.vfyjxf.justenoughgraphs.utils.Rect;
import com.github.vfyjxf.justenoughgraphs.utils.Vector3;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * @author KilaBash
 * From LDLib
 */
public class RenderHelper {

    public static void drawFluidTexture(PoseStack poseStack, float xCoord, float yCoord, TextureAtlasSprite textureSprite, int maskTop, int maskRight, float zLevel, int fluidColor) {
        float uMin = textureSprite.getU0();
        float uMax = textureSprite.getU1();
        float vMin = textureSprite.getV0();
        float vMax = textureSprite.getV1();
        uMax = uMax - maskRight / 16f * (uMax - uMin);
        vMax = vMax - maskTop / 16f * (vMax - vMin);

        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        Matrix4f mat = poseStack.last().pose();
        buffer.vertex(mat, xCoord, yCoord + 16, zLevel).uv(uMin, vMax).color(fluidColor).endVertex();
        buffer.vertex(mat, xCoord + 16 - maskRight, yCoord + 16, zLevel).uv(uMax, vMax).color(fluidColor).endVertex();
        buffer.vertex(mat, xCoord + 16 - maskRight, yCoord + maskTop, zLevel).uv(uMax, vMin).color(fluidColor).endVertex();
        buffer.vertex(mat, xCoord, yCoord + maskTop, zLevel).uv(uMin, vMin).color(fluidColor).endVertex();

        BufferUploader.drawWithShader(buffer.end());
    }


    public static void drawFluidForGui(PoseStack poseStack, FluidStack fluidStack, long tankCapacity, int startX, int startY, int widthT, int heightT) {
        ResourceLocation LOCATION_BLOCKS_TEXTURE = InventoryMenu.BLOCK_ATLAS;
        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        TextureAtlasSprite fluidStillSprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(fluidTypeExtensions.getStillTexture(fluidStack));
        int fluidColor = fluidTypeExtensions.getTintColor(fluidStack) | 0xff000000;
        int scaledAmount = (int) (fluidStack.getAmount() * heightT / tankCapacity);
        if (fluidStack.getAmount() > 0 && scaledAmount < 1) {
            scaledAmount = 1;
        }
        if (scaledAmount > heightT || fluidStack.getAmount() == tankCapacity) {
            scaledAmount = heightT;
        }
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, LOCATION_BLOCKS_TEXTURE);

        final int xTileCount = widthT / 16;
        final int xRemainder = widthT - xTileCount * 16;
        final int yTileCount = scaledAmount / 16;
        final int yRemainder = scaledAmount - yTileCount * 16;

        final int yStart = startY + heightT;

        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                int width = xTile == xTileCount ? xRemainder : 16;
                int height = yTile == yTileCount ? yRemainder : 16;
                int x = startX + xTile * 16;
                int y = yStart - (yTile + 1) * 16;
                if (width > 0 && height > 0) {
                    int maskTop = 16 - height;
                    int maskRight = 16 - width;
                    drawFluidTexture(poseStack, x, y, fluidStillSprite, maskTop, maskRight, 0, fluidColor);
                }
            }
        }
        RenderSystem.enableBlend();
    }

    public static void drawBorder(PoseStack poseStack, int border, int x, int y, int width, int height, int color) {
        GuiComponent.fill(poseStack, x, y, x + width, y + border, color);
        GuiComponent.fill(poseStack, x, y + height - border, x + width, y + height, color);
        GuiComponent.fill(poseStack, x, y + border, x + border, y + height - border, color);
        GuiComponent.fill(poseStack, x + width - border, y + border, x + width, y + height - border, color);
    }

    public static void drawStringSized(PoseStack poseStack, String text, float x, float y, int color, boolean dropShadow, float scale, boolean center) {
        poseStack.pushPose();
        Font fontRenderer = Minecraft.getInstance().font;
        double scaledTextWidth = center ? fontRenderer.width(text) * scale : 0.0;
        poseStack.translate(x - scaledTextWidth / 2.0, y, 0.0f);
        poseStack.scale(scale, scale, scale);
        if (dropShadow) {
            fontRenderer.drawShadow(poseStack, text, 0, 0, color);
        } else {
            fontRenderer.draw(poseStack, text, 0, 0, color);
        }
        poseStack.popPose();
    }

    public static void drawStringFixedCorner(PoseStack poseStack, String text, float x, float y, int color, boolean dropShadow, float scale) {
        Font fontRenderer = Minecraft.getInstance().font;
        float scaledWidth = fontRenderer.width(text) * scale;
        float scaledHeight = fontRenderer.lineHeight * scale;
        drawStringSized(poseStack, text, x - scaledWidth, y - scaledHeight, color, dropShadow, scale, false);
    }

    public static void drawText(PoseStack poseStack, String text, float x, float y, float scale, int color) {
        drawText(poseStack, text, x, y, scale, color, false);
    }

    public static void drawText(PoseStack poseStack, String text, float x, float y, float scale, int color, boolean shadow) {
        Font fontRenderer = Minecraft.getInstance().font;
        RenderSystem.disableBlend();
        poseStack.pushPose();
        poseStack.scale(scale, scale, 0f);
        float sf = 1 / scale;
        if (shadow) {
            fontRenderer.drawShadow(poseStack, text, x * sf, y * sf, color);
        } else {
            fontRenderer.draw(poseStack, text, x * sf, y * sf, color);
        }
        poseStack.popPose();
        RenderSystem.enableBlend();
    }

    public static void drawItemStack(PoseStack poseStack, ItemStack itemStack, int x, int y, int color, @Nullable String altTxt) {
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.mulPoseMatrix(poseStack.last().pose());
        posestack.translate(0.0D, 0.0D, 32.0D);
        RenderSystem.applyModelViewMatrix();
        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        RenderSystem.setShaderColor(r, g, b, a);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);

        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();

        itemRenderer.blitOffset = 200.0F;
        itemRenderer.renderAndDecorateItem(itemStack, x, y);
        itemRenderer.renderGuiItemDecorations(mc.font, itemStack, x, y, altTxt);
        itemRenderer.blitOffset = 0.0F;

        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
    }


    public static void drawSolidRect(PoseStack poseStack, int x, int y, int width, int height, int color) {
        Gui.fill(poseStack, x, y, x + width, y + height, color);
        RenderSystem.enableBlend();
    }

    public static void drawSolidRect(PoseStack poseStack, Rect rect, int color) {
        drawSolidRect(poseStack, rect.left, rect.up, rect.right, rect.down, color);
    }

    public static void drawRectShadow(PoseStack poseStack, int x, int y, int width, int height, int distance) {
        drawGradientRect(poseStack, x + distance, y + height, width - distance, distance, 0x4f000000, 0, false);
        drawGradientRect(poseStack, x + width, y + distance, distance, height - distance, 0x4f000000, 0, true);

        float startAlpha = (float) (0x4f) / 255.0F;
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
        x += width;
        y += height;
        Matrix4f mat = poseStack.last().pose();
        buffer.vertex(mat, x, y, 0).color(0, 0, 0, startAlpha).endVertex();
        buffer.vertex(mat, x, y + distance, 0).color(0, 0, 0, 0).endVertex();
        buffer.vertex(mat, x + distance, y + distance, 0).color(0, 0, 0, 0).endVertex();

        buffer.vertex(mat, x, y, 0).color(0, 0, 0, startAlpha).endVertex();
        buffer.vertex(mat, x + distance, y + distance, 0).color(0, 0, 0, 0).endVertex();
        buffer.vertex(mat, x + distance, y, 0).color(0, 0, 0, 0).endVertex();
        tesselator.end();
        RenderSystem.enableTexture();
    }

    public static void drawGradientRect(PoseStack poseStack, int x, int y, int width, int height, int startColor, int endColor) {
        drawGradientRect(poseStack, x, y, width, height, startColor, endColor, false);
    }

    public static void drawGradientRect(PoseStack poseStack, float x, float y, float width, float height, int startColor, int endColor, boolean horizontal) {
        float startAlpha = (float) (startColor >> 24 & 255) / 255.0F;
        float startRed = (float) (startColor >> 16 & 255) / 255.0F;
        float startGreen = (float) (startColor >> 8 & 255) / 255.0F;
        float startBlue = (float) (startColor & 255) / 255.0F;
        float endAlpha = (float) (endColor >> 24 & 255) / 255.0F;
        float endRed = (float) (endColor >> 16 & 255) / 255.0F;
        float endGreen = (float) (endColor >> 8 & 255) / 255.0F;
        float endBlue = (float) (endColor & 255) / 255.0F;
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Matrix4f mat = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        if (horizontal) {
            buffer.vertex(mat, x + width, y, 0).color(endRed, endGreen, endBlue, endAlpha).endVertex();
            buffer.vertex(mat, x, y, 0).color(startRed, startGreen, startBlue, startAlpha).endVertex();
            buffer.vertex(mat, x, y + height, 0).color(startRed, startGreen, startBlue, startAlpha).endVertex();
            buffer.vertex(mat, x + width, y + height, 0).color(endRed, endGreen, endBlue, endAlpha).endVertex();
            tesselator.end();
        } else {
            buffer.vertex(mat, x + width, y, 0).color(startRed, startGreen, startBlue, startAlpha).endVertex();
            buffer.vertex(mat, x, y, 0).color(startRed, startGreen, startBlue, startAlpha).endVertex();
            buffer.vertex(mat, x, y + height, 0).color(endRed, endGreen, endBlue, endAlpha).endVertex();
            buffer.vertex(mat, x + width, y + height, 0).color(endRed, endGreen, endBlue, endAlpha).endVertex();
            tesselator.end();
        }
        RenderSystem.enableTexture();
    }

    /**
     * Renders a line segment perpendicular to a two-dimensional coordinate system.
     */
    public static void drawVerticalLine(PoseStack poseStack, int x1, int y1, int x2, int y2, int color) {
        //use GuiComponent.fill instead
        if (x2 < x1) {
            int i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y2 < y1) {
            int j = y1;
            y1 = y2;
            y2 = j;
        }

    }

    public static void drawLine(PoseStack poseStack, int x1, int y1, int x2, int y2, int color, float width) {
        drawLine(poseStack, new Vec2(x1, y1), new Vec2(x2, y2), color, width);
    }

    public static void drawLine(PoseStack poseStack, Vec2 start, Vec2 end, int color, float width) {
        drawLineStrip(poseStack, List.of(start, end), color, width);
    }

    public static void drawLines(PoseStack poseStack, List<Vec2> points, int startColor, int endColor, float width) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        drawColorLines(poseStack, bufferbuilder, points, startColor, endColor, width);

        tesselator.end();
        RenderSystem.enableTexture();
        RenderSystem.defaultBlendFunc();
    }

    public static void drawColorLines(PoseStack poseStack, VertexConsumer builder, List<Vec2> points, int colorStart, int colorEnd, float width) {
        if (points.size() < 2) return;
        Matrix4f mat = poseStack.last().pose();
        Vec2 lastPoint = points.get(0);
        Vec2 point = points.get(1);
        Vector3 vec = null;
        int sa = (colorStart >> 24) & 0xff, sr = (colorStart >> 16) & 0xff, sg = (colorStart >> 8) & 0xff, sb = colorStart & 0xff;
        int ea = (colorEnd >> 24) & 0xff, er = (colorEnd >> 16) & 0xff, eg = (colorEnd >> 8) & 0xff, eb = colorEnd & 0xff;
        ea = (ea - sa);
        er = (er - sr);
        eg = (eg - sg);
        eb = (eb - sb);
        for (int i = 1; i < points.size(); i++) {
            float s = (i - 1f) / points.size();
            float e = i * 1f / points.size();
            point = points.get(i);
            vec = new Vector3(point.x - lastPoint.x, point.y - lastPoint.y, 0).rotate(Math.toRadians(90), Vector3.Z).normalize().multiply(-width);
            builder.vertex(mat, lastPoint.x + (float) vec.x, lastPoint.y + (float) vec.y, 0)
                    .color((sr + er * s) / 255, (sg + eg * s) / 255, (sb + eb * s) / 255, (sa + ea * s) / 255)
                    .endVertex();
            vec.multiply(-1);
            builder.vertex(mat, lastPoint.x + (float) vec.x, lastPoint.y + (float) vec.y, 0)
                    .color((sr + er * e) / 255, (sg + eg * e) / 255, (sb + eb * e) / 255, (sa + ea * e) / 255)
                    .endVertex();
            lastPoint = point;
        }
        vec.multiply(-1);
        builder.vertex(mat, point.x + (float) vec.x, point.y + (float) vec.y, 0)
                .color(sr + er, sg + eg, sb + eb, sa + ea)
                .endVertex();
        vec.multiply(-1);
        builder.vertex(mat, point.x + (float) vec.x, point.y + (float) vec.y, 0)
                .color(sr + er, sg + eg, sb + eb, sa + ea)
                .endVertex();
    }

    public static void drawColorTexLines(PoseStack poseStack, VertexConsumer builder, List<Vec2> points, int colorStart, int colorEnd, float width) {
        if (points.size() < 2) return;
        Matrix4f mat = poseStack.last().pose();
        Vec2 lastPoint = points.get(0);
        Vec2 point = points.get(1);
        Vector3 vec = null;
        int sa = (colorStart >> 24) & 0xff, sr = (colorStart >> 16) & 0xff, sg = (colorStart >> 8) & 0xff, sb = colorStart & 0xff;
        int ea = (colorEnd >> 24) & 0xff, er = (colorEnd >> 16) & 0xff, eg = (colorEnd >> 8) & 0xff, eb = colorEnd & 0xff;
        ea = (ea - sa);
        er = (er - sr);
        eg = (eg - sg);
        eb = (eb - sb);
        for (int i = 1; i < points.size(); i++) {
            float s = (i - 1f) / points.size();
            float e = i * 1f / points.size();
            point = points.get(i);
            float u = (i - 1f) / points.size();
            vec = new Vector3(point.x - lastPoint.x, point.y - lastPoint.y, 0).rotate(Math.toRadians(90), Vector3.Z).normalize().multiply(-width);
            builder.vertex(mat, lastPoint.x + (float) vec.x, lastPoint.y + (float) vec.y, 0).uv(u, 0)
                    .color((sr + er * s) / 255, (sg + eg * s) / 255, (sb + eb * s) / 255, (sa + ea * s) / 255)
                    .endVertex();
            vec.multiply(-1);
            builder.vertex(mat, lastPoint.x + (float) vec.x, lastPoint.y + (float) vec.y, 0).uv(u, 1)
                    .color((sr + er * e) / 255, (sg + eg * e) / 255, (sb + eb * e) / 255, (sa + ea * e) / 255)
                    .endVertex();
            lastPoint = point;
        }
        builder.vertex(mat, point.x + (float) vec.x, point.y + (float) vec.y, 0).uv(1, 0)
                .color(sr + er, sg + eg, sb + eb, sa + ea)
                .endVertex();
        vec.multiply(-1);
        builder.vertex(mat, point.x + (float) vec.x, point.y + (float) vec.y, 0).uv(1, 1)
                .color(sr + er, sg + eg, sb + eb, sa + ea)
                .endVertex();
    }


    public static void drawLineStrip(PoseStack poseStack, List<Vec2> points, int color, float width) {
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        Matrix4f mat = poseStack.last().pose();
        buffer.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        for (int index = 0; index < points.size(); index++) {
            Vec2 curr = points.get(index);
            float x = curr.x;
            float y = curr.y;

            Vec2 prev = (index == 0) ? null : points.get(index - 1);
            Vec2 next = (index == points.size() - 1) ? null : points.get(index + 1);

            float dx, dy;
            if (prev == null) {
                dx = next.x - x;
                dy = next.y - y;
            } else if (next == null) {
                dx = x - prev.x;
                dy = y - prev.y;
            } else {
                dx = next.x - prev.x;
                dy = next.y - prev.y;
            }
            float dLen = (float) Math.sqrt(dx * dx + dy * dy);
            float nx = dx / dLen * width / 2;
            float ny = dy / dLen * width / 2;

            buffer.vertex(mat, x + ny, y - nx, 0F)
                    .color(red, green, blue, alpha)
                    .endVertex();
            buffer.vertex(mat, x - ny, y + nx, 0F)
                    .color(red, green, blue, alpha)
                    .endVertex();
        }
        tesselator.end();
        RenderSystem.enableTexture();
        RenderSystem.defaultBlendFunc();
    }

    public static void drawTextureRect(PoseStack poseStack, float x, float y, float width, float height) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        Matrix4f mat = poseStack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(mat, x, y + height, 0).uv(0, 0).endVertex();
        buffer.vertex(mat, x + width, y + height, 0).uv(1, 0).endVertex();
        buffer.vertex(mat, x + width, y, 0).uv(1, 1).endVertex();
        buffer.vertex(mat, x, y, 0).uv(0, 1).endVertex();
        tesselator.end();
    }
}
