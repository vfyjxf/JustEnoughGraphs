package com.github.vfyjxf.justenoughgraphs.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.GameRenderer;

public class RenderHelper {

    public static void hLine(PoseStack pPoseStack, int pMinX, int pMaxX, int pY, int pColor) {
        if (pMaxX < pMinX) {
            int i = pMinX;
            pMinX = pMaxX;
            pMaxX = i;
        }

        fill(pPoseStack, pMinX, pY, pMaxX + 1, pY + 1, pColor);
    }

    public static void vLine(PoseStack pPoseStack, int pX, int pMinY, int pMaxY, int pColor) {
        if (pMaxY < pMinY) {
            int i = pMinY;
            pMinY = pMaxY;
            pMaxY = i;
        }

        fill(pPoseStack, pX, pMinY + 1, pX + 1, pMaxY, pColor);
    }

    public static void fill(PoseStack pPoseStack, int pMinX, int pMinY, int pMaxX, int pMaxY, int pColor) {
        innerFill(pPoseStack.last().pose(), pMinX, pMinY, pMaxX, pMaxY, pColor);
    }

    private static void innerFill(Matrix4f pMatrix, int pMinX, int pMinY, int pMaxX, int pMaxY, int pColor) {
        if (pMinX < pMaxX) {
            int i = pMinX;
            pMinX = pMaxX;
            pMaxX = i;
        }

        if (pMinY < pMaxY) {
            int j = pMinY;
            pMinY = pMaxY;
            pMaxY = j;
        }

        float f3 = (float) (pColor >> 24 & 255) / 255.0F;
        float f = (float) (pColor >> 16 & 255) / 255.0F;
        float f1 = (float) (pColor >> 8 & 255) / 255.0F;
        float f2 = (float) (pColor & 255) / 255.0F;
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex(pMatrix, (float) pMinX, (float) pMaxY, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(pMatrix, (float) pMaxX, (float) pMaxY, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(pMatrix, (float) pMaxX, (float) pMinY, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(pMatrix, (float) pMinX, (float) pMinY, 0.0F).color(f, f1, f2, f3).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    /**
     * create a static method ,it can use two points to draw a line, including Slash.
     * Use BufferBuilder and RenderSystem to draw a line.
     */
    public static void drawLine(PoseStack pPoseStack, int pX1, int pY1, int pX2, int pY2, int pColor) {
        float f3 = (float) (pColor >> 24 & 255) / 255.0F;
        float f = (float) (pColor >> 16 & 255) / 255.0F;
        float f1 = (float) (pColor >> 8 & 255) / 255.0F;
        float f2 = (float) (pColor & 255) / 255.0F;
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
        RenderSystem.lineWidth(5.0F);
        bufferbuilder.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.vertex(pPoseStack.last().pose(), (float) pX1, (float) pY1, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.vertex(pPoseStack.last().pose(), (float) pX2, (float) pY2, 0.0F).color(f, f1, f2, f3).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.lineWidth(1.0F);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

}
